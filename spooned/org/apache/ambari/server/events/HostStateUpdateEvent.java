package org.apache.ambari.server.events;
public class HostStateUpdateEvent extends org.apache.ambari.server.events.AmbariEvent {
    private java.lang.String hostName;

    private org.apache.ambari.server.state.HostState hostState;

    public HostStateUpdateEvent(java.lang.String hostName, org.apache.ambari.server.state.HostState hostState) {
        super(org.apache.ambari.server.events.AmbariEvent.AmbariEventType.HOST_STATE_CHANGE);
        this.hostName = hostName;
        this.hostState = hostState;
    }

    public java.lang.String getHostName() {
        return hostName;
    }

    public void setHostName(java.lang.String hostName) {
        this.hostName = hostName;
    }

    public org.apache.ambari.server.state.HostState getHostState() {
        return hostState;
    }

    public void setHostState(org.apache.ambari.server.state.HostState hostState) {
        this.hostState = hostState;
    }
}