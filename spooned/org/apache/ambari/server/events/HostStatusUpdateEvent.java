package org.apache.ambari.server.events;
public class HostStatusUpdateEvent extends org.apache.ambari.server.events.AmbariEvent {
    private java.lang.String hostName;

    private java.lang.String hostStatus;

    public HostStatusUpdateEvent(java.lang.String hostName, java.lang.String hostStatus) {
        super(org.apache.ambari.server.events.AmbariEvent.AmbariEventType.HOST_STATUS_CHANGE);
        this.hostName = hostName;
        this.hostStatus = hostStatus;
    }

    public java.lang.String getHostName() {
        return hostName;
    }

    public void setHostName(java.lang.String hostName) {
        this.hostName = hostName;
    }

    public java.lang.String getHostStatus() {
        return hostStatus;
    }

    public void setHostStatus(java.lang.String hostStatus) {
        this.hostStatus = hostStatus;
    }
}