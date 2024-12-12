package org.apache.ambari.server.events;
public class ClusterProvisionedEvent extends org.apache.ambari.server.events.AmbariEvent {
    private final long clusterId;

    public ClusterProvisionedEvent(long clusterId) {
        super(org.apache.ambari.server.events.AmbariEvent.AmbariEventType.CLUSTER_PROVISIONED);
        this.clusterId = clusterId;
    }

    public long getClusterId() {
        return clusterId;
    }
}