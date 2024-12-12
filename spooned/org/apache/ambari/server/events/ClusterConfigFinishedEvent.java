package org.apache.ambari.server.events;
public class ClusterConfigFinishedEvent extends org.apache.ambari.server.events.AmbariEvent {
    private final long clusterId;

    private final java.lang.String clusterName;

    public ClusterConfigFinishedEvent(long clusterId, java.lang.String clusterName) {
        super(org.apache.ambari.server.events.AmbariEvent.AmbariEventType.CLUSTER_CONFIG_FINISHED);
        this.clusterId = clusterId;
        this.clusterName = clusterName;
    }

    public long getClusterId() {
        return clusterId;
    }

    public java.lang.String getClusterName() {
        return clusterName;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder buffer = new java.lang.StringBuilder("ClusterConfigChangedEvent{");
        buffer.append("clusterId=").append(getClusterId());
        buffer.append("clusterName=").append(getClusterName());
        buffer.append("}");
        return buffer.toString();
    }
}