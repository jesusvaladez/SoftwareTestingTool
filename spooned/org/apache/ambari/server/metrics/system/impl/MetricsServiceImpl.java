package org.apache.ambari.server.metrics.system.impl;
import org.apache.commons.lang.StringUtils;
@com.google.inject.Singleton
public class MetricsServiceImpl implements org.apache.ambari.server.metrics.system.MetricsService {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.metrics.system.impl.MetricsServiceImpl.class);

    private static java.util.Map<java.lang.String, org.apache.ambari.server.metrics.system.MetricsSource> sources = new java.util.HashMap<>();

    private static org.apache.ambari.server.metrics.system.MetricsSink sink = null;

    private org.apache.ambari.server.metrics.system.impl.MetricsConfiguration configuration = null;

    @com.google.inject.Inject
    org.apache.ambari.server.controller.AmbariManagementController amc;

    @com.google.inject.Inject
    org.apache.ambari.server.events.publishers.STOMPUpdatePublisher STOMPUpdatePublisher;

    @java.lang.Override
    public void start() {
        org.apache.ambari.server.metrics.system.impl.MetricsServiceImpl.LOG.info("********* Initializing AmbariServer Metrics Service **********");
        try {
            configuration = org.apache.ambari.server.metrics.system.impl.MetricsConfiguration.getMetricsConfiguration();
            if (configuration == null) {
                return;
            }
            org.apache.ambari.server.metrics.system.impl.MetricsServiceImpl.sink = new org.apache.ambari.server.metrics.system.impl.AmbariMetricSinkImpl(amc);
            initializeMetricsSink();
            initializeMetricSources();
            if (!org.apache.ambari.server.metrics.system.impl.MetricsServiceImpl.sink.isInitialized()) {
                java.util.concurrent.Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        if (!org.apache.ambari.server.metrics.system.impl.MetricsServiceImpl.sink.isInitialized()) {
                            org.apache.ambari.server.metrics.system.impl.MetricsServiceImpl.LOG.info("Attempting to initialize metrics sink");
                            initializeMetricsSink();
                            if (org.apache.ambari.server.metrics.system.impl.MetricsServiceImpl.sink.isInitialized()) {
                                org.apache.ambari.server.metrics.system.impl.MetricsServiceImpl.LOG.info("Metric sink initialization successful");
                            }
                        }
                    }
                }, 5, 5, java.util.concurrent.TimeUnit.MINUTES);
            }
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.metrics.system.impl.MetricsServiceImpl.LOG.info("Unable to initialize MetricsService : ", e.getMessage());
        }
    }

    private void initializeMetricsSink() {
        org.apache.ambari.server.metrics.system.impl.MetricsServiceImpl.LOG.info("********* Configuring Metric Sink **********");
        org.apache.ambari.server.metrics.system.impl.MetricsServiceImpl.sink.init(configuration);
    }

    private void initializeMetricSources() {
        try {
            org.apache.ambari.server.metrics.system.impl.MetricsServiceImpl.LOG.info("********* Configuring Metric Sources **********");
            java.lang.String commaSeparatedSources = configuration.getProperty("metric.sources");
            if (org.apache.commons.lang.StringUtils.isEmpty(commaSeparatedSources)) {
                org.apache.ambari.server.metrics.system.impl.MetricsServiceImpl.LOG.info("No metric sources configured.");
                return;
            }
            java.lang.String[] sourceNames = commaSeparatedSources.split(",");
            for (java.lang.String sourceName : sourceNames) {
                if (org.apache.commons.lang.StringUtils.isEmpty(sourceName)) {
                    continue;
                }
                sourceName = sourceName.trim();
                java.lang.String className = configuration.getProperty(("source." + sourceName) + ".class");
                java.lang.Class sourceClass;
                try {
                    sourceClass = java.lang.Class.forName(className);
                } catch (java.lang.ClassNotFoundException ex) {
                    org.apache.ambari.server.metrics.system.impl.MetricsServiceImpl.LOG.info("Source class not found for source name :" + sourceName);
                    continue;
                }
                org.apache.ambari.server.metrics.system.impl.AbstractMetricsSource src = ((org.apache.ambari.server.metrics.system.impl.AbstractMetricsSource) (sourceClass.newInstance()));
                src.init(org.apache.ambari.server.metrics.system.impl.MetricsConfiguration.getSubsetConfiguration(configuration, ("source." + sourceName) + "."), org.apache.ambari.server.metrics.system.impl.MetricsServiceImpl.sink);
                org.apache.ambari.server.metrics.system.impl.MetricsServiceImpl.sources.put(sourceName, src);
                if (src instanceof org.apache.ambari.server.metrics.system.impl.StompEventsMetricsSource) {
                    STOMPUpdatePublisher.registerAPI(src);
                    STOMPUpdatePublisher.registerAgent(src);
                }
                src.start();
            }
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.metrics.system.impl.MetricsServiceImpl.LOG.error("Error when configuring metric sink and source", e);
        }
    }

    public static org.apache.ambari.server.metrics.system.MetricsSource getSource(java.lang.String type) {
        return org.apache.ambari.server.metrics.system.impl.MetricsServiceImpl.sources.get(type);
    }

    public static org.apache.ambari.server.metrics.system.MetricsSink getSink() {
        return org.apache.ambari.server.metrics.system.impl.MetricsServiceImpl.sink;
    }
}