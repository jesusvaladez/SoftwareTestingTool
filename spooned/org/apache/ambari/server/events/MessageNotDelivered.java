package org.apache.ambari.server.events;
public class MessageNotDelivered extends org.apache.ambari.server.events.AmbariEvent {
    private final java.lang.Long hostId;

    public MessageNotDelivered(java.lang.Long hostId) {
        super(org.apache.ambari.server.events.AmbariEvent.AmbariEventType.MESSAGE_NOT_DELIVERED);
        this.hostId = hostId;
    }

    public java.lang.Long getHostId() {
        return hostId;
    }
}