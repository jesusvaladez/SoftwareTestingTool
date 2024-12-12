package org.apache.ambari.server.events;
public class ClusterEvent extends org.apache.ambari.server.events.AmbariEvent {
    protected final long m_clusterId;

    public ClusterEvent(org.apache.ambari.server.events.AmbariEvent.AmbariEventType eventType, long clusterId) {
        super(eventType);
        m_clusterId = clusterId;
    }

    public long getClusterId() {
        return m_clusterId;
    }
}