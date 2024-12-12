package org.apache.ambari.server.state.services;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectReader;
@org.apache.ambari.server.AmbariService
public class MetricsRetrievalService extends com.google.common.util.concurrent.AbstractService {
    public enum MetricSourceType {

        JMX,
        REST;}

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.state.services.MetricsRetrievalService.class);

    private static final int EXCEPTION_CACHE_TIMEOUT_MINUTES = 20;

    private static final com.google.common.cache.Cache<java.lang.String, java.lang.Throwable> s_exceptionCache = com.google.common.cache.CacheBuilder.newBuilder().expireAfterWrite(org.apache.ambari.server.state.services.MetricsRetrievalService.EXCEPTION_CACHE_TIMEOUT_MINUTES, java.util.concurrent.TimeUnit.MINUTES).build();

    @com.google.inject.Inject
    private org.apache.ambari.server.configuration.Configuration m_configuration;

    @com.google.inject.Inject
    private com.google.gson.Gson m_gson;

    private com.google.common.cache.Cache<java.lang.String, org.apache.ambari.server.controller.jmx.JMXMetricHolder> m_jmxCache;

    private com.google.common.cache.Cache<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> m_restCache;

    private java.util.concurrent.ThreadPoolExecutor m_threadPoolExecutor;

    private final org.codehaus.jackson.map.ObjectReader m_jmxObjectReader;

    private final java.util.Set<java.lang.String> m_queuedUrls = com.google.common.collect.Sets.newConcurrentHashSet();

    private com.google.common.cache.Cache<java.lang.String, java.lang.String> m_ttlUrlCache;

    private int m_queueMaximumSize;

    public MetricsRetrievalService() {
        org.codehaus.jackson.map.ObjectMapper jmxObjectMapper = new org.codehaus.jackson.map.ObjectMapper();
        jmxObjectMapper.configure(DeserializationConfig.Feature.USE_ANNOTATIONS, false);
        jmxObjectMapper.configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true);
        m_jmxObjectReader = jmxObjectMapper.reader(org.apache.ambari.server.controller.jmx.JMXMetricHolder.class);
    }

    @java.lang.Override
    protected void doStart() {
        int jmxCacheExpirationMinutes = m_configuration.getMetricsServiceCacheTimeout();
        m_jmxCache = com.google.common.cache.CacheBuilder.newBuilder().expireAfterWrite(jmxCacheExpirationMinutes, java.util.concurrent.TimeUnit.MINUTES).build();
        m_restCache = com.google.common.cache.CacheBuilder.newBuilder().expireAfterWrite(jmxCacheExpirationMinutes, java.util.concurrent.TimeUnit.MINUTES).build();
        int ttlSeconds = m_configuration.getMetricsServiceRequestTTL();
        boolean ttlCacheEnabled = m_configuration.isMetricsServiceRequestTTLCacheEnabled();
        if (ttlCacheEnabled) {
            m_ttlUrlCache = com.google.common.cache.CacheBuilder.newBuilder().expireAfterWrite(ttlSeconds, java.util.concurrent.TimeUnit.SECONDS).build();
        }
        int corePoolSize = m_configuration.getMetricsServiceThreadPoolCoreSize();
        int maxPoolSize = m_configuration.getMetricsServiceThreadPoolMaxSize();
        m_queueMaximumSize = m_configuration.getMetricsServiceWorkerQueueSize();
        int threadPriority = m_configuration.getMetricsServiceThreadPriority();
        m_threadPoolExecutor = new org.apache.ambari.server.controller.utilities.ScalingThreadPoolExecutor(corePoolSize, maxPoolSize, 30, java.util.concurrent.TimeUnit.SECONDS, m_queueMaximumSize);
        m_threadPoolExecutor.allowCoreThreadTimeOut(true);
        m_threadPoolExecutor.setRejectedExecutionHandler(new java.util.concurrent.ThreadPoolExecutor.DiscardOldestPolicy());
        java.util.concurrent.ThreadFactory threadFactory = new com.google.common.util.concurrent.ThreadFactoryBuilder().setDaemon(true).setNameFormat("ambari-metrics-retrieval-service-thread-%d").setPriority(threadPriority).setUncaughtExceptionHandler(new org.apache.ambari.server.state.services.MetricsRetrievalService.MetricRunnableExceptionHandler()).build();
        m_threadPoolExecutor.setThreadFactory(threadFactory);
        org.apache.ambari.server.state.services.MetricsRetrievalService.LOG.info("Initializing the Metrics Retrieval Service with core={}, max={}, workerQueue={}, threadPriority={}", corePoolSize, maxPoolSize, m_queueMaximumSize, threadPriority);
        if (ttlCacheEnabled) {
            org.apache.ambari.server.state.services.MetricsRetrievalService.LOG.info("Metrics Retrieval Service request TTL cache is enabled and set to {} seconds", ttlSeconds);
        }
        notifyStarted();
    }

    public void setThreadPoolExecutor(java.util.concurrent.ThreadPoolExecutor threadPoolExecutor) {
        m_threadPoolExecutor = threadPoolExecutor;
    }

    @java.lang.Override
    protected void doStop() {
        m_jmxCache.invalidateAll();
        m_restCache.invalidateAll();
        if (null != m_ttlUrlCache) {
            m_ttlUrlCache.invalidateAll();
        }
        m_queuedUrls.clear();
        m_threadPoolExecutor.shutdownNow();
        notifyStopped();
    }

    public void submitRequest(org.apache.ambari.server.state.services.MetricsRetrievalService.MetricSourceType type, org.apache.ambari.server.controller.utilities.StreamProvider streamProvider, java.lang.String url) {
        if (m_queuedUrls.contains(url)) {
            return;
        }
        if ((null != m_ttlUrlCache) && (null != m_ttlUrlCache.getIfPresent(url))) {
            return;
        }
        java.util.concurrent.BlockingQueue<java.lang.Runnable> queue = m_threadPoolExecutor.getQueue();
        int queueSize = queue.size();
        if (queueSize > java.lang.Math.floor(0.9F * m_queueMaximumSize)) {
            org.apache.ambari.server.state.services.MetricsRetrievalService.LOG.warn("The worker queue contains {} work items and is at {}% of capacity", queueSize, (((float) (queueSize)) / m_queueMaximumSize) * 100);
        }
        m_queuedUrls.add(url);
        java.lang.Runnable runnable = null;
        switch (type) {
            case JMX :
                runnable = new org.apache.ambari.server.state.services.MetricsRetrievalService.JMXRunnable(m_jmxCache, m_queuedUrls, m_ttlUrlCache, m_jmxObjectReader, streamProvider, url);
                break;
            case REST :
                runnable = new org.apache.ambari.server.state.services.MetricsRetrievalService.RESTRunnable(m_restCache, m_queuedUrls, m_ttlUrlCache, m_gson, streamProvider, url);
                break;
            default :
                org.apache.ambari.server.state.services.MetricsRetrievalService.LOG.warn("Unable to retrieve metrics for the unknown type {}", type);
                break;
        }
        if (null != runnable) {
            m_threadPoolExecutor.execute(runnable);
        }
    }

    public org.apache.ambari.server.controller.jmx.JMXMetricHolder getCachedJMXMetric(java.lang.String jmxUrl) {
        return m_jmxCache.getIfPresent(jmxUrl);
    }

    public java.util.Map<java.lang.String, java.lang.String> getCachedRESTMetric(java.lang.String restUrl) {
        return m_restCache.getIfPresent(restUrl);
    }

    private static abstract class MetricRunnable implements java.lang.Runnable {
        protected final org.apache.ambari.server.controller.utilities.StreamProvider m_streamProvider;

        protected final java.lang.String m_url;

        private final java.util.Set<java.lang.String> m_queuedUrls;

        private final com.google.common.cache.Cache<java.lang.String, java.lang.String> m_ttlUrlCache;

        private MetricRunnable(org.apache.ambari.server.controller.utilities.StreamProvider streamProvider, java.lang.String url, java.util.Set<java.lang.String> queuedUrls, com.google.common.cache.Cache<java.lang.String, java.lang.String> ttlUrlCache) {
            m_streamProvider = streamProvider;
            m_url = url;
            m_queuedUrls = queuedUrls;
            m_ttlUrlCache = ttlUrlCache;
        }

        @java.lang.Override
        public final void run() {
            long startTime = 0;
            long endTime = 0;
            boolean isDebugEnabled = org.apache.ambari.server.state.services.MetricsRetrievalService.LOG.isDebugEnabled();
            if (isDebugEnabled) {
                startTime = java.lang.System.currentTimeMillis();
            }
            java.io.InputStream inputStream = null;
            try {
                if (isDebugEnabled) {
                    endTime = java.lang.System.currentTimeMillis();
                    org.apache.ambari.server.state.services.MetricsRetrievalService.LOG.debug("Loading metric JSON from {} took {}ms", m_url, endTime - startTime);
                }
                inputStream = m_streamProvider.readFrom(m_url);
                processInputStreamAndCacheResult(inputStream);
                if (null != m_ttlUrlCache) {
                    m_ttlUrlCache.put(m_url, m_url);
                }
            } catch (java.io.IOException exception) {
                org.apache.ambari.server.state.services.MetricsRetrievalService.LOG.debug("Removing cached values for url {}", m_url);
                removeCachedMetricsForCurrentURL();
                logException(exception, m_url);
            } catch (java.lang.Exception exception) {
                logException(exception, m_url);
            } finally {
                org.apache.commons.io.IOUtils.closeQuietly(inputStream);
                m_queuedUrls.remove(m_url);
            }
        }

        protected abstract void removeCachedMetricsForCurrentURL();

        protected abstract void processInputStreamAndCacheResult(java.io.InputStream inputStream) throws java.lang.Exception;

        final void logException(java.lang.Throwable throwable, java.lang.String url) {
            java.lang.String cacheKey = buildCacheKey(throwable, url);
            if (null == org.apache.ambari.server.state.services.MetricsRetrievalService.s_exceptionCache.getIfPresent(cacheKey)) {
                org.apache.ambari.server.state.services.MetricsRetrievalService.s_exceptionCache.put(cacheKey, throwable);
                org.apache.ambari.server.state.services.MetricsRetrievalService.LOG.error("Unable to retrieve metrics from {}. Subsequent failures will be suppressed from the log for {} minutes.", url, org.apache.ambari.server.state.services.MetricsRetrievalService.EXCEPTION_CACHE_TIMEOUT_MINUTES, throwable);
            }
        }

        private java.lang.String buildCacheKey(java.lang.Throwable throwable, java.lang.String url) {
            if ((null == throwable) || (null == url)) {
                return "";
            }
            java.lang.String throwableName = throwable.getClass().getSimpleName();
            return (throwableName + "-") + url;
        }
    }

    private static final class JMXRunnable extends org.apache.ambari.server.state.services.MetricsRetrievalService.MetricRunnable {
        private final org.codehaus.jackson.map.ObjectReader m_jmxObjectReader;

        private final com.google.common.cache.Cache<java.lang.String, org.apache.ambari.server.controller.jmx.JMXMetricHolder> m_cache;

        private JMXRunnable(com.google.common.cache.Cache<java.lang.String, org.apache.ambari.server.controller.jmx.JMXMetricHolder> cache, java.util.Set<java.lang.String> queuedUrls, com.google.common.cache.Cache<java.lang.String, java.lang.String> ttlUrlCache, org.codehaus.jackson.map.ObjectReader jmxObjectReader, org.apache.ambari.server.controller.utilities.StreamProvider streamProvider, java.lang.String jmxUrl) {
            super(streamProvider, jmxUrl, queuedUrls, ttlUrlCache);
            m_cache = cache;
            m_jmxObjectReader = jmxObjectReader;
        }

        @java.lang.Override
        protected void removeCachedMetricsForCurrentURL() {
            m_cache.invalidate(m_url);
        }

        @java.lang.Override
        protected void processInputStreamAndCacheResult(java.io.InputStream inputStream) throws java.lang.Exception {
            org.apache.ambari.server.controller.jmx.JMXMetricHolder jmxMetricHolder = m_jmxObjectReader.readValue(inputStream);
            m_cache.put(m_url, jmxMetricHolder);
        }
    }

    private static final class RESTRunnable extends org.apache.ambari.server.state.services.MetricsRetrievalService.MetricRunnable {
        private final com.google.gson.Gson m_gson;

        private final com.google.common.cache.Cache<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> m_cache;

        private RESTRunnable(com.google.common.cache.Cache<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> cache, java.util.Set<java.lang.String> queuedUrls, com.google.common.cache.Cache<java.lang.String, java.lang.String> ttlUrlCache, com.google.gson.Gson gson, org.apache.ambari.server.controller.utilities.StreamProvider streamProvider, java.lang.String restUrl) {
            super(streamProvider, restUrl, queuedUrls, ttlUrlCache);
            m_cache = cache;
            m_gson = gson;
        }

        @java.lang.Override
        protected void removeCachedMetricsForCurrentURL() {
            m_cache.invalidate(m_url);
        }

        @java.lang.Override
        protected void processInputStreamAndCacheResult(java.io.InputStream inputStream) throws java.lang.Exception {
            java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<java.util.Map<java.lang.Object, java.lang.Object>>() {}.getType();
            com.google.gson.stream.JsonReader jsonReader = new com.google.gson.stream.JsonReader(new java.io.BufferedReader(new java.io.InputStreamReader(inputStream)));
            java.util.Map<java.lang.String, java.lang.String> jsonMap = m_gson.fromJson(jsonReader, type);
            m_cache.put(m_url, jsonMap);
        }
    }

    private static final class MetricRunnableExceptionHandler implements java.lang.Thread.UncaughtExceptionHandler {
        @java.lang.Override
        public void uncaughtException(java.lang.Thread t, java.lang.Throwable e) {
            org.apache.ambari.server.state.services.MetricsRetrievalService.LOG.error("Asynchronous metric retrieval encountered an exception with thread {}", t, e);
        }
    }
}