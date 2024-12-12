package org.apache.ambari.server.state.host;
public class HostUnhealthyHeartbeatEvent extends org.apache.ambari.server.state.HostEvent {
    private final long heartbeatTime;

    private final org.apache.ambari.server.state.HostHealthStatus healthStatus;

    public HostUnhealthyHeartbeatEvent(java.lang.String hostName, long heartbeatTime, org.apache.ambari.server.state.HostHealthStatus healthStatus) {
        super(hostName, org.apache.ambari.server.state.HostEventType.HOST_HEARTBEAT_UNHEALTHY);
        this.heartbeatTime = heartbeatTime;
        this.healthStatus = healthStatus;
    }

    public long getHeartbeatTime() {
        return heartbeatTime;
    }

    public org.apache.ambari.server.state.HostHealthStatus getHealthStatus() {
        return healthStatus;
    }
}