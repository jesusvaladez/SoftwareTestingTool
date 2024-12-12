package org.apache.ambari.server.metrics.system;
public interface MetricsSink {
    void init(org.apache.ambari.server.metrics.system.impl.MetricsConfiguration configuration);

    void publish(java.util.List<org.apache.ambari.server.metrics.system.SingleMetric> metrics);

    boolean isInitialized();
}