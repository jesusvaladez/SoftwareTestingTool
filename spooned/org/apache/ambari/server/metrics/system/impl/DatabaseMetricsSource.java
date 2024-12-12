package org.apache.ambari.server.metrics.system.impl;
import org.apache.commons.lang.StringUtils;
public class DatabaseMetricsSource extends org.apache.ambari.server.metrics.system.impl.AbstractMetricsSource {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.metrics.system.impl.DatabaseMetricsSource.class);

    private static java.lang.String dbMonitorPrefix = "monitor.";

    private java.util.concurrent.ExecutorService executor;

    private org.apache.ambari.server.metrics.system.impl.MetricsConfiguration configuration;

    private java.util.Set<java.lang.String> includedMetricKeywords = new java.util.HashSet<>();

    private java.util.Set<java.util.regex.Pattern> acceptedEntityPatterns = new java.util.HashSet<>();

    private java.util.Set<java.lang.String> acceptedEntities = new java.util.HashSet<>();

    private static java.lang.String TIMER = "Timer.";

    private static java.lang.String COUNTER = "Counter.";

    @java.lang.Override
    public void init(org.apache.ambari.server.metrics.system.impl.MetricsConfiguration metricsConfig, org.apache.ambari.server.metrics.system.MetricsSink sink) {
        super.init(metricsConfig, sink);
        configuration = metricsConfig;
        initializeFilterSets();
        org.apache.ambari.server.metrics.system.impl.DatabaseMetricsSource.LOG.info("Initialized Ambari DB Metrics Source...");
    }

    private void initializeFilterSets() {
        java.lang.String commaSeparatedValues = configuration.getProperty(org.apache.ambari.server.metrics.system.impl.DatabaseMetricsSource.dbMonitorPrefix + "query.keywords.include");
        if (org.apache.commons.lang.StringUtils.isNotEmpty(commaSeparatedValues)) {
            includedMetricKeywords.addAll(java.util.Arrays.asList(commaSeparatedValues.split(",")));
        }
        commaSeparatedValues = configuration.getProperty(org.apache.ambari.server.metrics.system.impl.DatabaseMetricsSource.dbMonitorPrefix + "entities");
        if (org.apache.commons.lang.StringUtils.isNotEmpty(commaSeparatedValues)) {
            java.lang.String[] entityPatterns = commaSeparatedValues.split(",");
            for (java.lang.String pattern : entityPatterns) {
                acceptedEntityPatterns.add(java.util.regex.Pattern.compile(pattern));
            }
        }
    }

    @java.lang.Override
    public void start() {
        java.util.concurrent.ThreadFactory threadFactory = new com.google.common.util.concurrent.ThreadFactoryBuilder().setNameFormat("DatabaseMetricsSource-%d").build();
        executor = java.util.concurrent.Executors.newSingleThreadExecutor(threadFactory);
        org.apache.ambari.server.metrics.system.impl.DatabaseMetricsSource.LOG.info("Started Ambari DB Metrics source...");
    }

    public void publish(final java.util.Map<java.lang.String, java.lang.Long> metricsMap) {
        try {
            executor.submit(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    long currentTime = java.lang.System.currentTimeMillis();
                    for (java.util.Iterator<java.util.Map.Entry<java.lang.String, java.lang.Long>> it = metricsMap.entrySet().iterator(); it.hasNext();) {
                        java.util.Map.Entry<java.lang.String, java.lang.Long> metricEntry = it.next();
                        java.lang.String metricName = metricEntry.getKey();
                        if (!acceptMetric(metricName)) {
                            it.remove();
                        }
                    }
                    final java.util.List<org.apache.ambari.server.metrics.system.SingleMetric> metrics = new java.util.ArrayList<>();
                    for (java.lang.String metricName : metricsMap.keySet()) {
                        double value = metricsMap.get(metricName).doubleValue();
                        metrics.add(new org.apache.ambari.server.metrics.system.SingleMetric(metricName, value, currentTime));
                        if (metricName.startsWith(org.apache.ambari.server.metrics.system.impl.DatabaseMetricsSource.COUNTER)) {
                            java.lang.String baseMetricName = metricName.substring(org.apache.ambari.server.metrics.system.impl.DatabaseMetricsSource.COUNTER.length());
                            if (metricsMap.containsKey(org.apache.ambari.server.metrics.system.impl.DatabaseMetricsSource.TIMER + baseMetricName)) {
                                double timerValue = metricsMap.get(org.apache.ambari.server.metrics.system.impl.DatabaseMetricsSource.TIMER + baseMetricName).doubleValue();
                                if (value != 0.0) {
                                    metrics.add(new org.apache.ambari.server.metrics.system.SingleMetric(baseMetricName, timerValue / value, currentTime));
                                }
                            }
                        }
                    }
                    sink.publish(metrics);
                }
            });
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.metrics.system.impl.DatabaseMetricsSource.LOG.info("Exception when publishing Database metrics to sink", e);
        }
    }

    public boolean acceptMetric(java.lang.String metricName) {
        boolean accept = false;
        if (acceptedEntities.contains(metricName)) {
            accept = true;
        } else {
            for (java.util.regex.Pattern p : acceptedEntityPatterns) {
                java.util.regex.Matcher m = p.matcher(metricName);
                if (m.find()) {
                    accept = true;
                }
            }
        }
        for (java.lang.String keyword : includedMetricKeywords) {
            if (metricName.contains(keyword)) {
                accept = true;
            }
        }
        java.lang.String[] splits = metricName.split("\\.");
        if (splits.length <= 2) {
            accept = true;
        }
        if (accept) {
            acceptedEntities.add(metricName);
            return true;
        }
        return false;
    }

    public java.lang.String getConfigurationValue(java.lang.String key, java.lang.String defaultValue) {
        return this.configuration.getProperty(org.apache.ambari.server.metrics.system.impl.DatabaseMetricsSource.dbMonitorPrefix + key, defaultValue);
    }
}