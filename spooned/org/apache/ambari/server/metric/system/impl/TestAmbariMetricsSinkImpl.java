package org.apache.ambari.server.metric.system.impl;
import org.apache.hadoop.metrics2.sink.timeline.AbstractTimelineMetricsSink;
public class TestAmbariMetricsSinkImpl extends org.apache.hadoop.metrics2.sink.timeline.AbstractTimelineMetricsSink implements org.apache.ambari.server.metrics.system.MetricsSink {
    @java.lang.Override
    public void publish(java.util.List<org.apache.ambari.server.metrics.system.SingleMetric> metrics) {
        org.apache.ambari.server.metric.system.impl.LOG.info(("Published " + metrics.size()) + " metrics.");
    }

    @java.lang.Override
    public boolean isInitialized() {
        return true;
    }

    @java.lang.Override
    protected java.lang.String getCollectorUri(java.lang.String host) {
        return constructTimelineMetricUri(getCollectorProtocol(), host, getCollectorPort());
    }

    @java.lang.Override
    protected java.lang.String getCollectorProtocol() {
        return "http";
    }

    @java.lang.Override
    protected java.lang.String getCollectorPort() {
        return "6188";
    }

    @java.lang.Override
    protected int getTimeoutSeconds() {
        return 1000;
    }

    @java.lang.Override
    protected java.lang.String getZookeeperQuorum() {
        return null;
    }

    @java.lang.Override
    protected java.util.Collection<java.lang.String> getConfiguredCollectorHosts() {
        return java.util.Collections.singletonList("localhost");
    }

    @java.lang.Override
    protected java.lang.String getHostname() {
        return "localhost";
    }

    protected boolean isHostInMemoryAggregationEnabled() {
        return true;
    }

    protected int getHostInMemoryAggregationPort() {
        return 61888;
    }

    @java.lang.Override
    protected java.lang.String getHostInMemoryAggregationProtocol() {
        return "http";
    }

    @java.lang.Override
    public void init(org.apache.ambari.server.metrics.system.impl.MetricsConfiguration configuration) {
    }
}