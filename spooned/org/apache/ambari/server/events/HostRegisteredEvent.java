package org.apache.ambari.server.events;
public class HostRegisteredEvent extends org.apache.ambari.server.events.HostEvent {
    private java.lang.Long hostId;

    public HostRegisteredEvent(java.lang.String hostName, java.lang.Long hostId) {
        super(org.apache.ambari.server.events.AmbariEvent.AmbariEventType.HOST_REGISTERED, hostName);
        this.hostId = hostId;
    }

    public java.lang.Long getHostId() {
        return hostId;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder buffer = new java.lang.StringBuilder("HostRegistered{ ");
        buffer.append("hostName=").append(m_hostName);
        buffer.append("}");
        return buffer.toString();
    }
}