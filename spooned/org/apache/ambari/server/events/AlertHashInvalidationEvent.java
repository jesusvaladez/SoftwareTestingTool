package org.apache.ambari.server.events;
public class AlertHashInvalidationEvent extends org.apache.ambari.server.events.ClusterEvent {
    private final java.util.Collection<java.lang.String> m_hosts;

    public AlertHashInvalidationEvent(long clusterId, java.util.Collection<java.lang.String> hosts) {
        super(org.apache.ambari.server.events.AmbariEvent.AmbariEventType.ALERT_DEFINITION_HASH_INVALIDATION, clusterId);
        m_hosts = hosts;
    }

    public java.util.Collection<java.lang.String> getHosts() {
        return m_hosts;
    }
}