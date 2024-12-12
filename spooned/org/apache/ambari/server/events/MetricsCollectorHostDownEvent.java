package org.apache.ambari.server.events;
public class MetricsCollectorHostDownEvent extends org.apache.ambari.server.events.AmbariEvent {
    private final java.lang.String clusterName;

    private final java.lang.String collectorHost;

    public MetricsCollectorHostDownEvent(java.lang.String clusterName, java.lang.String collectorHost) {
        super(org.apache.ambari.server.events.AmbariEvent.AmbariEventType.METRICS_COLLECTOR_HOST_DOWN);
        this.clusterName = clusterName;
        this.collectorHost = collectorHost;
    }

    public java.lang.String getClusterName() {
        return clusterName;
    }

    public java.lang.String getCollectorHost() {
        return collectorHost;
    }
}