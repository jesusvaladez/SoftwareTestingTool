package org.apache.ambari.server.metrics.system.impl;
public abstract class AbstractMetricsSource implements org.apache.ambari.server.metrics.system.MetricsSource {
    protected org.apache.ambari.server.metrics.system.MetricsSink sink;

    protected org.apache.ambari.server.metrics.system.impl.MetricsConfiguration configuration;

    @java.lang.Override
    public void init(org.apache.ambari.server.metrics.system.impl.MetricsConfiguration configuration, org.apache.ambari.server.metrics.system.MetricsSink sink) {
        this.sink = sink;
    }
}