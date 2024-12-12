package org.apache.ambari.server.events;
public class HostsAddedEvent extends org.apache.ambari.server.events.ClusterEvent {
    protected final java.util.Set<java.lang.String> m_hostNames;

    public HostsAddedEvent(long clusterId, java.util.Set<java.lang.String> hostNames) {
        super(org.apache.ambari.server.events.AmbariEvent.AmbariEventType.HOST_ADDED, clusterId);
        m_hostNames = hostNames;
    }

    public java.util.Set<java.lang.String> getHostNames() {
        return m_hostNames;
    }
}