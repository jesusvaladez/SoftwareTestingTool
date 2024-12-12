package org.apache.ambari.server.events;
public abstract class HostEvent extends org.apache.ambari.server.events.AmbariEvent {
    protected final java.lang.String m_hostName;

    public HostEvent(org.apache.ambari.server.events.AmbariEvent.AmbariEventType eventType, java.lang.String hostName) {
        super(eventType);
        m_hostName = hostName;
    }

    public java.lang.String getHostName() {
        return m_hostName;
    }
}