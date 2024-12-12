package org.apache.ambari.server.events;
public class ClusterProvisionStartedEvent extends org.apache.ambari.server.events.AmbariEvent {
    private final long clusterId;

    public ClusterProvisionStartedEvent(long clusterId) {
        super(org.apache.ambari.server.events.AmbariEvent.AmbariEventType.CLUSTER_PROVISION_STARTED);
        this.clusterId = clusterId;
    }

    public long getClusterId() {
        return clusterId;
    }
}