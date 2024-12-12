package org.apache.ambari.server.controller.logging;
import org.apache.commons.collections.MapUtils;
@org.apache.ambari.server.AmbariService
public class LogSearchDataRetrievalService extends com.google.common.util.concurrent.AbstractService {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService.class);

    private static int MAX_RETRIES_FOR_FAILED_METADATA_REQUEST = 10;

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory loggingRequestHelperFactory;

    @com.google.inject.Inject
    private com.google.inject.Injector injector;

    @com.google.inject.Inject
    private org.apache.ambari.server.configuration.Configuration ambariServerConfiguration;

    private com.google.common.cache.Cache<java.lang.String, java.util.Set<java.lang.String>> logFileNameCache;

    private com.google.common.cache.Cache<java.lang.String, java.lang.String> logFileTailURICache;

    private final java.util.Set<java.lang.String> currentRequests = com.google.common.collect.Sets.newConcurrentHashSet();

    private final java.util.Map<java.lang.String, java.util.concurrent.atomic.AtomicInteger> componentRequestFailureCounts = com.google.common.collect.Maps.newConcurrentMap();

    private java.util.concurrent.Executor executor;

    @java.lang.Override
    protected void doStart() {
        org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService.LOG.debug("Initializing caches");
        final int maxTimeoutForCacheInHours = ambariServerConfiguration.getLogSearchMetadataCacheExpireTimeout();
        org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService.LOG.debug("Caches configured with a max expire timeout of {} hours.", maxTimeoutForCacheInHours);
        logFileNameCache = com.google.common.cache.CacheBuilder.newBuilder().expireAfterWrite(maxTimeoutForCacheInHours, java.util.concurrent.TimeUnit.HOURS).build();
        logFileTailURICache = com.google.common.cache.CacheBuilder.newBuilder().expireAfterWrite(maxTimeoutForCacheInHours, java.util.concurrent.TimeUnit.HOURS).build();
        executor = java.util.concurrent.Executors.newSingleThreadExecutor();
    }

    @java.lang.Override
    protected void doStop() {
        org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService.LOG.debug("Invalidating LogSearch caches");
        logFileNameCache.invalidateAll();
        logFileTailURICache.invalidateAll();
    }

    public java.util.Set<java.lang.String> getLogFileNames(java.lang.String component, java.lang.String host, java.lang.String cluster) {
        final java.lang.String key = org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService.generateKey(component, host);
        java.util.Set<java.lang.String> cacheResult = logFileNameCache.getIfPresent(key);
        if (cacheResult != null) {
            org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService.LOG.debug("LogFileNames result for key = {} found in cache", key);
            return cacheResult;
        } else if ((!componentRequestFailureCounts.containsKey(component)) || (componentRequestFailureCounts.get(component).get() < org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService.MAX_RETRIES_FOR_FAILED_METADATA_REQUEST)) {
            if (currentRequests.contains(key)) {
                org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService.LOG.debug("LogFileNames request has been made for key = {}, but not completed yet", key);
            } else {
                org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService.LOG.debug("LogFileNames result for key = {} not in cache, queueing up remote request", key);
                currentRequests.add(key);
                startLogSearchFileNameRequest(host, component, cluster);
            }
        } else {
            org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService.LOG.debug("Too many failures occurred while attempting to obtain log file metadata for component = {}, Ambari will ignore this component for LogSearch Integration", component);
        }
        return null;
    }

    public java.lang.String getLogFileTailURI(java.lang.String baseURI, java.lang.String component, java.lang.String host, java.lang.String cluster) {
        java.lang.String key = org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService.generateKey(component, host);
        java.lang.String result = logFileTailURICache.getIfPresent(key);
        if (result != null) {
            return result;
        } else if (loggingRequestHelperFactory != null) {
            org.apache.ambari.server.controller.logging.LoggingRequestHelper helper = loggingRequestHelperFactory.getHelper(getController(), cluster);
            if (helper != null) {
                java.lang.String tailFileURI = helper.createLogFileTailURI(baseURI, component, host);
                if (tailFileURI != null) {
                    logFileTailURICache.put(key, tailFileURI);
                    return tailFileURI;
                }
            }
        } else {
            org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService.LOG.debug("LoggingRequestHelperFactory not set on the retrieval service, this probably indicates an error in setup of this service.");
        }
        return null;
    }

    protected void setLoggingRequestHelperFactory(org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory loggingRequestHelperFactory) {
        this.loggingRequestHelperFactory = loggingRequestHelperFactory;
    }

    void setInjector(com.google.inject.Injector injector) {
        this.injector = injector;
    }

    protected void setExecutor(java.util.concurrent.Executor executor) {
        this.executor = executor;
    }

    void setConfiguration(org.apache.ambari.server.configuration.Configuration ambariServerConfiguration) {
        this.ambariServerConfiguration = ambariServerConfiguration;
    }

    protected java.util.Set<java.lang.String> getCurrentRequests() {
        return currentRequests;
    }

    protected java.util.Map<java.lang.String, java.util.concurrent.atomic.AtomicInteger> getComponentRequestFailureCounts() {
        return componentRequestFailureCounts;
    }

    private void startLogSearchFileNameRequest(java.lang.String host, java.lang.String component, java.lang.String cluster) {
        executor.execute(new org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService.LogSearchFileNameRequestRunnable(host, component, cluster, logFileNameCache, currentRequests, injector.getInstance(org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory.class), componentRequestFailureCounts));
    }

    private org.apache.ambari.server.controller.AmbariManagementController getController() {
        return org.apache.ambari.server.controller.AmbariServer.getController();
    }

    private static java.lang.String generateKey(java.lang.String component, java.lang.String host) {
        return (component + "+") + host;
    }

    static class LogSearchFileNameRequestRunnable implements java.lang.Runnable {
        private final java.lang.String host;

        private final java.lang.String component;

        private final java.lang.String cluster;

        private final java.util.Set<java.lang.String> currentRequests;

        private final com.google.common.cache.Cache<java.lang.String, java.util.Set<java.lang.String>> logFileNameCache;

        private org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory loggingRequestHelperFactory;

        private final java.util.Map<java.lang.String, java.util.concurrent.atomic.AtomicInteger> componentRequestFailureCounts;

        private org.apache.ambari.server.controller.AmbariManagementController controller;

        LogSearchFileNameRequestRunnable(java.lang.String host, java.lang.String component, java.lang.String cluster, com.google.common.cache.Cache<java.lang.String, java.util.Set<java.lang.String>> logFileNameCache, java.util.Set<java.lang.String> currentRequests, org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory loggingRequestHelperFactory, java.util.Map<java.lang.String, java.util.concurrent.atomic.AtomicInteger> componentRequestFailureCounts) {
            this(host, component, cluster, logFileNameCache, currentRequests, loggingRequestHelperFactory, componentRequestFailureCounts, org.apache.ambari.server.controller.AmbariServer.getController());
        }

        LogSearchFileNameRequestRunnable(java.lang.String host, java.lang.String component, java.lang.String cluster, com.google.common.cache.Cache<java.lang.String, java.util.Set<java.lang.String>> logFileNameCache, java.util.Set<java.lang.String> currentRequests, org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory loggingRequestHelperFactory, java.util.Map<java.lang.String, java.util.concurrent.atomic.AtomicInteger> componentRequestFailureCounts, org.apache.ambari.server.controller.AmbariManagementController controller) {
            this.host = host;
            this.component = component;
            this.cluster = cluster;
            this.logFileNameCache = logFileNameCache;
            this.currentRequests = currentRequests;
            this.loggingRequestHelperFactory = loggingRequestHelperFactory;
            this.componentRequestFailureCounts = componentRequestFailureCounts;
            this.controller = controller;
        }

        @java.lang.Override
        public void run() {
            org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService.LOG.debug("LogSearchFileNameRequestRunnable: starting...");
            try {
                org.apache.ambari.server.controller.logging.LoggingRequestHelper helper = loggingRequestHelperFactory.getHelper(controller, cluster);
                if (helper != null) {
                    org.apache.ambari.server.controller.logging.HostLogFilesResponse logFilesResponse = helper.sendGetLogFileNamesRequest(host);
                    if ((logFilesResponse != null) && org.apache.commons.collections.MapUtils.isNotEmpty(logFilesResponse.getHostLogFiles())) {
                        org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService.LOG.debug("LogSearchFileNameRequestRunnable: request was successful, updating cache");
                        for (java.util.Map.Entry<java.lang.String, java.util.List<java.lang.String>> componentEntry : logFilesResponse.getHostLogFiles().entrySet()) {
                            final java.lang.String key = org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService.generateKey(componentEntry.getKey(), host);
                            logFileNameCache.put(key, new java.util.HashSet<>(componentEntry.getValue()));
                        }
                    } else {
                        org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService.LOG.debug("LogSearchFileNameRequestRunnable: remote request was not successful for component = {} on host ={}", component, host);
                        if (!componentRequestFailureCounts.containsKey(component)) {
                            componentRequestFailureCounts.put(component, new java.util.concurrent.atomic.AtomicInteger());
                        }
                        componentRequestFailureCounts.get(component).incrementAndGet();
                    }
                } else {
                    org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService.LOG.debug("LogSearchFileNameRequestRunnable: request helper was null.  This may mean that LogSearch is not available, or could be a potential connection problem.");
                }
            } finally {
                currentRequests.remove(org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService.generateKey(component, host));
            }
        }

        protected void setLoggingRequestHelperFactory(org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory loggingRequestHelperFactory) {
            this.loggingRequestHelperFactory = loggingRequestHelperFactory;
        }

        protected void setAmbariManagementController(org.apache.ambari.server.controller.AmbariManagementController controller) {
            this.controller = controller;
        }
    }
}