package org.apache.ambari.server.controller.metrics;
public abstract class ThreadPoolEnabledPropertyProvider extends org.apache.ambari.server.controller.internal.AbstractPropertyProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.class);

    public static final java.util.Set<java.lang.String> healthyStates = java.util.Collections.singleton("STARTED");

    protected final java.lang.String hostNamePropertyId;

    private final org.apache.ambari.server.controller.metrics.MetricHostProvider metricHostProvider;

    private final java.lang.String clusterNamePropertyId;

    private static java.util.concurrent.ThreadPoolExecutor EXECUTOR_SERVICE;

    private static int THREAD_POOL_CORE_SIZE;

    private static int THREAD_POOL_MAX_SIZE;

    private static int THREAD_POOL_WORKER_QUEUE_SIZE;

    private static long COMPLETION_SERVICE_POLL_TIMEOUT;

    private static final long THREAD_POOL_TIMEOUT_MILLIS = 30000L;

    @com.google.inject.Inject
    public static void init(org.apache.ambari.server.configuration.Configuration configuration) {
        org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.THREAD_POOL_CORE_SIZE = configuration.getPropertyProvidersThreadPoolCoreSize();
        org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.THREAD_POOL_MAX_SIZE = configuration.getPropertyProvidersThreadPoolMaxSize();
        org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.THREAD_POOL_WORKER_QUEUE_SIZE = configuration.getPropertyProvidersWorkerQueueSize();
        org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.COMPLETION_SERVICE_POLL_TIMEOUT = configuration.getPropertyProvidersCompletionServiceTimeout();
        org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.EXECUTOR_SERVICE = org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.initExecutorService();
    }

    private static final com.google.common.cache.Cache<java.lang.String, java.lang.Throwable> exceptionsCache = com.google.common.cache.CacheBuilder.newBuilder().expireAfterWrite(5, java.util.concurrent.TimeUnit.MINUTES).build();

    public ThreadPoolEnabledPropertyProvider(java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.PropertyInfo>> componentMetrics, java.lang.String hostNamePropertyId, org.apache.ambari.server.controller.metrics.MetricHostProvider metricHostProvider, java.lang.String clusterNamePropertyId) {
        super(componentMetrics);
        this.hostNamePropertyId = hostNamePropertyId;
        this.metricHostProvider = metricHostProvider;
        this.clusterNamePropertyId = clusterNamePropertyId;
    }

    private static java.util.concurrent.ThreadPoolExecutor initExecutorService() {
        java.util.concurrent.ThreadPoolExecutor threadPoolExecutor = new org.apache.ambari.server.controller.utilities.ScalingThreadPoolExecutor(org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.THREAD_POOL_CORE_SIZE, org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.THREAD_POOL_MAX_SIZE, org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.THREAD_POOL_TIMEOUT_MILLIS, java.util.concurrent.TimeUnit.MILLISECONDS, org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.THREAD_POOL_WORKER_QUEUE_SIZE);
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        java.util.concurrent.ThreadFactory threadFactory = new com.google.common.util.concurrent.ThreadFactoryBuilder().setDaemon(true).setNameFormat("ambari-property-provider-thread-%d").build();
        threadPoolExecutor.setThreadFactory(threadFactory);
        return threadPoolExecutor;
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> populateResources(java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException {
        if (!checkAuthorizationForMetrics(resources, clusterNamePropertyId)) {
            return resources;
        }
        final org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.Ticket ticket = new org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.Ticket();
        final java.util.concurrent.CompletionService<org.apache.ambari.server.controller.spi.Resource> completionService = new org.apache.ambari.server.controller.utilities.BufferedThreadPoolExecutorCompletionService<>(org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.EXECUTOR_SERVICE);
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            completionService.submit(getPopulateResourceCallable(resource, request, predicate, ticket));
        }
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> keepers = new java.util.HashSet<>();
        try {
            for (int i = 0; i < resources.size(); ++i) {
                java.util.concurrent.Future<org.apache.ambari.server.controller.spi.Resource> resourceFuture = completionService.poll(org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.COMPLETION_SERVICE_POLL_TIMEOUT, java.util.concurrent.TimeUnit.MILLISECONDS);
                if (resourceFuture == null) {
                    ticket.invalidate();
                    org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.LOG.error("Timed out after waiting {}ms waiting for request {}", org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.COMPLETION_SERVICE_POLL_TIMEOUT, request);
                    break;
                }
                org.apache.ambari.server.controller.spi.Resource resource = resourceFuture.get();
                if (resource != null) {
                    keepers.add(resource);
                }
            }
        } catch (java.lang.InterruptedException e) {
            org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.logException(e);
        } catch (java.util.concurrent.ExecutionException e) {
            org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.rethrowSystemException(e.getCause());
        }
        return keepers;
    }

    private java.util.concurrent.Callable<org.apache.ambari.server.controller.spi.Resource> getPopulateResourceCallable(final org.apache.ambari.server.controller.spi.Resource resource, final org.apache.ambari.server.controller.spi.Request request, final org.apache.ambari.server.controller.spi.Predicate predicate, final org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.Ticket ticket) {
        return new java.util.concurrent.Callable<org.apache.ambari.server.controller.spi.Resource>() {
            @java.lang.Override
            public org.apache.ambari.server.controller.spi.Resource call() throws org.apache.ambari.server.controller.spi.SystemException {
                return populateResource(resource, request, predicate, ticket);
            }
        };
    }

    protected abstract org.apache.ambari.server.controller.spi.Resource populateResource(org.apache.ambari.server.controller.spi.Resource resource, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate, org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.Ticket ticket) throws org.apache.ambari.server.controller.spi.SystemException;

    protected void setPopulateTimeout(long populateTimeout) {
        org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.COMPLETION_SERVICE_POLL_TIMEOUT = populateTimeout;
    }

    protected static boolean isRequestedPropertyId(java.lang.String propertyId, java.lang.String requestedPropertyId, org.apache.ambari.server.controller.spi.Request request) {
        return request.getPropertyIds().isEmpty() || propertyId.startsWith(requestedPropertyId);
    }

    protected static java.lang.String getCacheKeyForException(final java.lang.Throwable throwable) {
        if (throwable == null) {
            return "";
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (java.lang.Throwable t : com.google.common.base.Throwables.getCausalChain(throwable)) {
            if (t != null) {
                sb.append(t.getClass().getName());
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    protected static java.lang.String logException(final java.lang.Throwable throwable) {
        final java.lang.String msg = "Caught exception getting metrics : " + throwable.getLocalizedMessage();
        java.lang.String cacheKey = org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.getCacheKeyForException(throwable);
        if (org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.LOG.isDebugEnabled()) {
            org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.LOG.debug(msg, throwable);
        } else {
            try {
                org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.exceptionsCache.get(cacheKey, new java.util.concurrent.Callable<java.lang.Throwable>() {
                    @java.lang.Override
                    public java.lang.Throwable call() {
                        org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.LOG.error(msg + ", skipping same exceptions for next 5 minutes", throwable);
                        return throwable;
                    }
                });
            } catch (java.util.concurrent.ExecutionException ignored) {
            }
        }
        return msg;
    }

    protected static void rethrowSystemException(java.lang.Throwable throwable) throws org.apache.ambari.server.controller.spi.SystemException {
        java.lang.String msg = org.apache.ambari.server.controller.metrics.ThreadPoolEnabledPropertyProvider.logException(throwable);
        if (throwable instanceof org.apache.ambari.server.controller.spi.SystemException) {
            throw ((org.apache.ambari.server.controller.spi.SystemException) (throwable));
        }
        throw new org.apache.ambari.server.controller.spi.SystemException(msg, throwable);
    }

    public java.lang.String getHost(org.apache.ambari.server.controller.spi.Resource resource, java.lang.String clusterName, java.lang.String componentName) throws org.apache.ambari.server.controller.spi.SystemException {
        return hostNamePropertyId == null ? metricHostProvider.getHostName(clusterName, componentName) : ((java.lang.String) (resource.getPropertyValue(hostNamePropertyId)));
    }

    protected java.lang.String getSpec(java.lang.String protocol, java.lang.String hostName, java.lang.String port, java.lang.String url) {
        return ((((protocol + "://") + hostName) + ":") + port) + url;
    }

    protected static class Ticket {
        private volatile boolean valid = true;

        public void invalidate() {
            valid = false;
        }

        public boolean isValid() {
            return valid;
        }
    }
}