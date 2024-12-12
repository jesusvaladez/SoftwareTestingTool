package org.apache.ambari.server.metrics.system.impl;
import org.apache.commons.lang.StringUtils;
import org.eclipse.persistence.sessions.SessionProfiler;
import org.eclipse.persistence.tools.profiler.PerformanceMonitor;
@com.google.inject.Singleton
public class AmbariPerformanceMonitor extends org.eclipse.persistence.tools.profiler.PerformanceMonitor {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.metrics.system.impl.AmbariPerformanceMonitor.class);

    private boolean isInitialized = false;

    private org.apache.ambari.server.metrics.system.impl.DatabaseMetricsSource metricsSource;

    private static java.lang.String entityPackagePrefix = "org.apache";

    public AmbariPerformanceMonitor() {
        super();
        org.apache.ambari.server.metrics.system.impl.AmbariPerformanceMonitor.LOG.info("AmbariPerformanceMonitor instantiated");
        init();
    }

    private void init() {
        if (metricsSource == null) {
            metricsSource = ((org.apache.ambari.server.metrics.system.impl.DatabaseMetricsSource) (org.apache.ambari.server.metrics.system.impl.MetricsServiceImpl.getSource("database")));
        }
        if (metricsSource != null) {
            org.apache.ambari.server.metrics.system.impl.AmbariPerformanceMonitor.LOG.info("AmbariPerformanceMonitor initialized");
            long interval = java.lang.Long.parseLong(metricsSource.getConfigurationValue("dumptime", "60000"));
            this.setDumpTime(interval);
            java.lang.String profileWeight = metricsSource.getConfigurationValue("query.weight", "HEAVY");
            this.setProfileWeight(getWeight(profileWeight));
            isInitialized = true;
        } else {
            org.apache.ambari.server.metrics.system.impl.AmbariPerformanceMonitor.LOG.info("AmbariPerformanceMonitor not yet initialized.");
        }
    }

    @java.lang.Override
    public void dumpResults() {
        lastDumpTime = java.lang.System.currentTimeMillis();
        java.util.Set<java.lang.String> operations = new java.util.TreeSet<>(this.operationTimings.keySet());
        java.util.Map<java.lang.String, java.lang.Long> metrics = new java.util.HashMap<>();
        for (java.lang.String operation : operations) {
            java.lang.String[] splits = operation.split(":");
            java.lang.Object value = this.operationTimings.get(operation);
            if (value == null) {
                value = java.lang.Long.valueOf(0);
            }
            if (value instanceof java.lang.Long) {
                java.util.List<java.lang.String> list = new java.util.ArrayList<>();
                for (int i = 0; i < splits.length; i++) {
                    if (splits[i].startsWith(org.apache.ambari.server.metrics.system.impl.AmbariPerformanceMonitor.entityPackagePrefix)) {
                        java.lang.String[] queryClassSplits = splits[i].split("\\.");
                        list.add(queryClassSplits[queryClassSplits.length - 1]);
                    } else if ((splits[i] != null) && (!splits[i].equals("null"))) {
                        list.add(splits[i]);
                    }
                }
                metrics.put(org.apache.commons.lang.StringUtils.join(list, "."), ((java.lang.Long) (value)));
            }
        }
        if (!metrics.isEmpty()) {
            if (!isInitialized) {
                init();
            }
            if (isInitialized) {
                org.apache.ambari.server.metrics.system.impl.AmbariPerformanceMonitor.LOG.debug("Publishing {} metrics to sink.", metrics.size());
                metricsSource.publish(metrics);
            }
        }
    }

    private int getWeight(java.lang.String value) {
        if (org.apache.commons.lang.StringUtils.isEmpty(value) || value.equals("NONE")) {
            return org.eclipse.persistence.sessions.SessionProfiler.NONE;
        }
        if (value.equals("ALL")) {
            return org.eclipse.persistence.sessions.SessionProfiler.ALL;
        }
        if (value.equals("NORMAL")) {
            return org.eclipse.persistence.sessions.SessionProfiler.NORMAL;
        }
        return org.eclipse.persistence.sessions.SessionProfiler.HEAVY;
    }
}