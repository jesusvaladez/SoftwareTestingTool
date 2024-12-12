package org.apache.ambari.server.metrics.system.impl;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.MetricSet;
import com.codahale.metrics.jvm.BufferPoolMetricSet;
import com.codahale.metrics.jvm.FileDescriptorRatioGauge;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
public class JvmMetricsSource extends org.apache.ambari.server.metrics.system.impl.AbstractMetricsSource {
    static final com.codahale.metrics.MetricRegistry registry = new com.codahale.metrics.MetricRegistry();

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.metrics.system.impl.JvmMetricsSource.class);

    private java.util.concurrent.ScheduledExecutorService executor = java.util.concurrent.Executors.newScheduledThreadPool(1);

    private static java.lang.String JVM_PREFIX = "jvm";

    private int interval = 10;

    @java.lang.Override
    public void init(org.apache.ambari.server.metrics.system.impl.MetricsConfiguration configuration, org.apache.ambari.server.metrics.system.MetricsSink sink) {
        super.init(configuration, sink);
        registerAll(org.apache.ambari.server.metrics.system.impl.JvmMetricsSource.JVM_PREFIX + ".gc", new com.codahale.metrics.jvm.GarbageCollectorMetricSet(), org.apache.ambari.server.metrics.system.impl.JvmMetricsSource.registry);
        registerAll(org.apache.ambari.server.metrics.system.impl.JvmMetricsSource.JVM_PREFIX + ".buffers", new com.codahale.metrics.jvm.BufferPoolMetricSet(java.lang.management.ManagementFactory.getPlatformMBeanServer()), org.apache.ambari.server.metrics.system.impl.JvmMetricsSource.registry);
        registerAll(org.apache.ambari.server.metrics.system.impl.JvmMetricsSource.JVM_PREFIX + ".memory", new com.codahale.metrics.jvm.MemoryUsageGaugeSet(), org.apache.ambari.server.metrics.system.impl.JvmMetricsSource.registry);
        registerAll(org.apache.ambari.server.metrics.system.impl.JvmMetricsSource.JVM_PREFIX + ".threads", new com.codahale.metrics.jvm.ThreadStatesGaugeSet(), org.apache.ambari.server.metrics.system.impl.JvmMetricsSource.registry);
        org.apache.ambari.server.metrics.system.impl.JvmMetricsSource.registry.register(org.apache.ambari.server.metrics.system.impl.JvmMetricsSource.JVM_PREFIX + ".file.open.descriptor.ratio", new com.codahale.metrics.jvm.FileDescriptorRatioGauge());
        interval = java.lang.Integer.parseInt(configuration.getProperty("interval", "10"));
        org.apache.ambari.server.metrics.system.impl.JvmMetricsSource.LOG.info("Initialized JVM Metrics source...");
    }

    @java.lang.Override
    public void start() {
        try {
            executor.scheduleWithFixedDelay(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    try {
                        org.apache.ambari.server.metrics.system.impl.JvmMetricsSource.LOG.debug("Publishing JVM metrics to sink");
                        sink.publish(getMetrics());
                    } catch (java.lang.Exception e) {
                        org.apache.ambari.server.metrics.system.impl.JvmMetricsSource.LOG.debug("Error in publishing JVM metrics to sink.");
                    }
                }
            }, interval, interval, java.util.concurrent.TimeUnit.SECONDS);
            org.apache.ambari.server.metrics.system.impl.JvmMetricsSource.LOG.info("Started JVM Metrics source...");
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.metrics.system.impl.JvmMetricsSource.LOG.info("Throwing exception when starting metric source", e);
        }
    }

    private void registerAll(java.lang.String prefix, com.codahale.metrics.MetricSet metricSet, com.codahale.metrics.MetricRegistry registry) {
        for (java.util.Map.Entry<java.lang.String, com.codahale.metrics.Metric> entry : metricSet.getMetrics().entrySet()) {
            if (entry.getValue() instanceof com.codahale.metrics.MetricSet) {
                registerAll((prefix + ".") + entry.getKey(), ((com.codahale.metrics.MetricSet) (entry.getValue())), registry);
            } else {
                registry.register((prefix + ".") + entry.getKey(), entry.getValue());
            }
        }
    }

    public java.util.List<org.apache.ambari.server.metrics.system.SingleMetric> getMetrics() {
        java.util.List<org.apache.ambari.server.metrics.system.SingleMetric> metrics = new java.util.ArrayList<>();
        java.util.Map<java.lang.String, com.codahale.metrics.Gauge> gaugeSet = org.apache.ambari.server.metrics.system.impl.JvmMetricsSource.registry.getGauges(new org.apache.ambari.server.metrics.system.impl.JvmMetricsSource.NonNumericMetricFilter());
        for (java.lang.String metricName : gaugeSet.keySet()) {
            java.lang.Number value = ((java.lang.Number) (gaugeSet.get(metricName).getValue()));
            metrics.add(new org.apache.ambari.server.metrics.system.SingleMetric(metricName, value.doubleValue(), java.lang.System.currentTimeMillis()));
        }
        return metrics;
    }

    public class NonNumericMetricFilter implements com.codahale.metrics.MetricFilter {
        @java.lang.Override
        public boolean matches(java.lang.String name, com.codahale.metrics.Metric metric) {
            if (name.equalsIgnoreCase("jvm.threads.deadlocks")) {
                return false;
            }
            return true;
        }
    }
}