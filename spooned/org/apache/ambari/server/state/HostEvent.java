package org.apache.ambari.server.state;
public abstract class HostEvent extends org.apache.ambari.server.state.fsm.event.AbstractEvent<org.apache.ambari.server.state.HostEventType> {
    private final java.lang.String hostName;

    public HostEvent(java.lang.String hostName, org.apache.ambari.server.state.HostEventType type) {
        super(type);
        this.hostName = hostName;
    }

    public java.lang.String getHostName() {
        return hostName;
    }
}