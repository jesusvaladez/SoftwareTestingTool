package org.apache.ambari.server.agent.stomp.dto;
public class HostStatusReport {
    @com.fasterxml.jackson.annotation.JsonProperty("agentEnv")
    private org.apache.ambari.server.agent.AgentEnv agentEnv;

    @com.fasterxml.jackson.annotation.JsonProperty("mounts")
    private java.util.List<org.apache.ambari.server.agent.DiskInfo> mounts;

    public HostStatusReport() {
    }

    public org.apache.ambari.server.agent.AgentEnv getAgentEnv() {
        return agentEnv;
    }

    public void setAgentEnv(org.apache.ambari.server.agent.AgentEnv agentEnv) {
        this.agentEnv = agentEnv;
    }

    public java.util.List<org.apache.ambari.server.agent.DiskInfo> getMounts() {
        return mounts;
    }

    public void setMounts(java.util.List<org.apache.ambari.server.agent.DiskInfo> mounts) {
        this.mounts = mounts;
    }
}