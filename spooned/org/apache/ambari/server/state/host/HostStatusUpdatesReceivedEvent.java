package org.apache.ambari.server.state.host;
public class HostStatusUpdatesReceivedEvent extends org.apache.ambari.server.state.HostEvent {
    private final long timestamp;

    public HostStatusUpdatesReceivedEvent(java.lang.String hostName, long timestamp) {
        super(hostName, org.apache.ambari.server.state.HostEventType.HOST_STATUS_UPDATES_RECEIVED);
        this.timestamp = timestamp;
    }

    @java.lang.Override
    public long getTimestamp() {
        return timestamp;
    }
}