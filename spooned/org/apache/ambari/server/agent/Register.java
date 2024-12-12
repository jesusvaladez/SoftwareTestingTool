package org.apache.ambari.server.agent;
import org.codehaus.jackson.annotate.JsonProperty;
public class Register {
    private int responseId = -1;

    private long timestamp;

    private long agentStartTime;

    private java.lang.String hostname;

    private int currentPingPort;

    private org.apache.ambari.server.agent.HostInfo hardwareProfile;

    private java.lang.String publicHostname;

    private org.apache.ambari.server.agent.AgentEnv agentEnv;

    private java.lang.String agentVersion;

    private java.lang.String prefix;

    @org.codehaus.jackson.annotate.JsonProperty("responseId")
    @com.fasterxml.jackson.annotation.JsonProperty("id")
    public int getResponseId() {
        return responseId;
    }

    @org.codehaus.jackson.annotate.JsonProperty("responseId")
    @com.fasterxml.jackson.annotation.JsonProperty("id")
    public void setResponseId(int responseId) {
        this.responseId = responseId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public java.lang.String getHostname() {
        return hostname;
    }

    public void setHostname(java.lang.String hostname) {
        this.hostname = hostname;
    }

    public org.apache.ambari.server.agent.HostInfo getHardwareProfile() {
        return hardwareProfile;
    }

    public void setHardwareProfile(org.apache.ambari.server.agent.HostInfo hardwareProfile) {
        this.hardwareProfile = hardwareProfile;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public java.lang.String getPublicHostname() {
        return publicHostname;
    }

    public void setPublicHostname(java.lang.String name) {
        this.publicHostname = name;
    }

    public org.apache.ambari.server.agent.AgentEnv getAgentEnv() {
        return agentEnv;
    }

    public void setAgentEnv(org.apache.ambari.server.agent.AgentEnv env) {
        this.agentEnv = env;
    }

    public java.lang.String getAgentVersion() {
        return agentVersion;
    }

    public java.lang.String getPrefix() {
        return prefix;
    }

    public void setPrefix(java.lang.String prefix) {
        this.prefix = prefix;
    }

    public void setAgentVersion(java.lang.String agentVersion) {
        this.agentVersion = agentVersion;
    }

    public int getCurrentPingPort() {
        return currentPingPort;
    }

    public void setCurrentPingPort(int currentPingPort) {
        this.currentPingPort = currentPingPort;
    }

    public long getAgentStartTime() {
        return agentStartTime;
    }

    public void setAgentStartTime(long agentStartTime) {
        this.agentStartTime = agentStartTime;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.String ret = (((((((((((((((("responseId=" + responseId) + "\n") + "timestamp=") + timestamp) + "\n") + "startTime=") + agentStartTime) + "\n") + "hostname=") + hostname) + "\n") + "currentPingPort=") + currentPingPort) + "\n") + "prefix=") + prefix) + "\n";
        if (hardwareProfile != null)
            ret = (ret + "hardwareprofile=") + this.hardwareProfile;

        return ret;
    }
}