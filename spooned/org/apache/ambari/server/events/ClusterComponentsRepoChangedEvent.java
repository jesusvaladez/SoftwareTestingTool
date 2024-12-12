package org.apache.ambari.server.events;
public class ClusterComponentsRepoChangedEvent extends org.apache.ambari.server.events.ClusterEvent {
    public ClusterComponentsRepoChangedEvent(long clusterId) {
        super(org.apache.ambari.server.events.AmbariEvent.AmbariEventType.SERVICE_COMPONENT_REPO_CHANGE, clusterId);
    }
}