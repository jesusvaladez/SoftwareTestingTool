package org.apache.ambari.server.events;
public class StackUpgradeFinishEvent extends org.apache.ambari.server.events.ClusterEvent {
    public org.apache.ambari.server.state.Cluster getCluster() {
        return cluster;
    }

    protected final org.apache.ambari.server.state.Cluster cluster;

    public StackUpgradeFinishEvent(org.apache.ambari.server.state.Cluster cluster) {
        super(org.apache.ambari.server.events.AmbariEvent.AmbariEventType.FINALIZE_UPGRADE_FINISH, cluster.getClusterId());
        this.cluster = cluster;
    }
}