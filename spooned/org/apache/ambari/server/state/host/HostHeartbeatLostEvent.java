package org.apache.ambari.server.state.host;
public class HostHeartbeatLostEvent extends org.apache.ambari.server.state.HostEvent {
    public HostHeartbeatLostEvent(java.lang.String hostName) {
        super(hostName, org.apache.ambari.server.state.HostEventType.HOST_HEARTBEAT_LOST);
    }
}