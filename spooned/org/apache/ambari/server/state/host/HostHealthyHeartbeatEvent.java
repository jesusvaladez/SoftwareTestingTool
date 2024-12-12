package org.apache.ambari.server.state.host;
public class HostHealthyHeartbeatEvent extends org.apache.ambari.server.state.HostEvent {
    private final long heartbeatTime;

    private org.apache.ambari.server.agent.AgentEnv agentEnv = null;

    private java.util.List<org.apache.ambari.server.agent.DiskInfo> mounts = new java.util.ArrayList<>();

    public HostHealthyHeartbeatEvent(java.lang.String hostName, long heartbeatTime, org.apache.ambari.server.agent.AgentEnv env, java.util.List<org.apache.ambari.server.agent.DiskInfo> mounts) {
        super(hostName, org.apache.ambari.server.state.HostEventType.HOST_HEARTBEAT_HEALTHY);
        this.heartbeatTime = heartbeatTime;
        agentEnv = env;
        this.mounts = mounts;
    }

    public long getHeartbeatTime() {
        return heartbeatTime;
    }

    public org.apache.ambari.server.agent.AgentEnv getAgentEnv() {
        return agentEnv;
    }

    public java.util.List<org.apache.ambari.server.agent.DiskInfo> getMounts() {
        return mounts;
    }
}