package org.apache.ambari.server.metrics.system;
public interface MetricsSource {
    void init(org.apache.ambari.server.metrics.system.impl.MetricsConfiguration configuration, org.apache.ambari.server.metrics.system.MetricsSink sink);

    void start();
}