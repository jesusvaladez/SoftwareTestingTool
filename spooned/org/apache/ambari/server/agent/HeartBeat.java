package org.apache.ambari.server.agent;
import org.codehaus.jackson.annotate.JsonProperty;
public class HeartBeat {
    @com.fasterxml.jackson.annotation.JsonProperty("id")
    private long responseId = -1;

    @com.fasterxml.jackson.annotation.JsonProperty("staleAlerts")
    private java.util.List<org.apache.ambari.server.agent.StaleAlert> staleAlerts = new java.util.ArrayList<>();

    private long timestamp;

    private java.lang.String hostname;

    java.util.List<org.apache.ambari.server.agent.CommandReport> reports = new java.util.ArrayList<>();

    java.util.List<org.apache.ambari.server.agent.ComponentStatus> componentStatus = new java.util.ArrayList<>();

    private java.util.List<org.apache.ambari.server.agent.DiskInfo> mounts = new java.util.ArrayList<>();

    org.apache.ambari.server.agent.HostStatus nodeStatus;

    private org.apache.ambari.server.agent.AgentEnv agentEnv = null;

    private java.util.List<org.apache.ambari.server.state.Alert> alerts = null;

    private org.apache.ambari.server.agent.RecoveryReport recoveryReport;

    private long recoveryTimestamp = -1;

    public long getResponseId() {
        return responseId;
    }

    public void setResponseId(long responseId) {
        this.responseId = responseId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public java.lang.String getHostname() {
        return hostname;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setHostname(java.lang.String hostname) {
        this.hostname = hostname;
    }

    public long getRecoveryTimestamp() {
        return recoveryTimestamp;
    }

    public void setRecoveryTimestamp(long recoveryTimestamp) {
        this.recoveryTimestamp = recoveryTimestamp;
    }

    @org.codehaus.jackson.annotate.JsonProperty("reports")
    @com.fasterxml.jackson.annotation.JsonProperty("reports")
    public java.util.List<org.apache.ambari.server.agent.CommandReport> getReports() {
        return reports;
    }

    @org.codehaus.jackson.annotate.JsonProperty("reports")
    @com.fasterxml.jackson.annotation.JsonProperty("reports")
    public void setReports(java.util.List<org.apache.ambari.server.agent.CommandReport> reports) {
        this.reports = reports;
    }

    public org.apache.ambari.server.agent.HostStatus getNodeStatus() {
        return nodeStatus;
    }

    public void setNodeStatus(org.apache.ambari.server.agent.HostStatus nodeStatus) {
        this.nodeStatus = nodeStatus;
    }

    public org.apache.ambari.server.agent.RecoveryReport getRecoveryReport() {
        return recoveryReport;
    }

    public void setRecoveryReport(org.apache.ambari.server.agent.RecoveryReport recoveryReport) {
        this.recoveryReport = recoveryReport;
    }

    public org.apache.ambari.server.agent.AgentEnv getAgentEnv() {
        return agentEnv;
    }

    public void setAgentEnv(org.apache.ambari.server.agent.AgentEnv env) {
        agentEnv = env;
    }

    @org.codehaus.jackson.annotate.JsonProperty("componentStatus")
    @com.fasterxml.jackson.annotation.JsonProperty("componentStatus")
    public java.util.List<org.apache.ambari.server.agent.ComponentStatus> getComponentStatus() {
        return componentStatus;
    }

    @org.codehaus.jackson.annotate.JsonProperty("componentStatus")
    @com.fasterxml.jackson.annotation.JsonProperty("componentStatus")
    public void setComponentStatus(java.util.List<org.apache.ambari.server.agent.ComponentStatus> componentStatus) {
        this.componentStatus = componentStatus;
    }

    @org.codehaus.jackson.annotate.JsonProperty("mounts")
    @com.fasterxml.jackson.annotation.JsonProperty("mounts")
    public java.util.List<org.apache.ambari.server.agent.DiskInfo> getMounts() {
        return mounts;
    }

    @org.codehaus.jackson.annotate.JsonProperty("mounts")
    @com.fasterxml.jackson.annotation.JsonProperty("mounts")
    public void setMounts(java.util.List<org.apache.ambari.server.agent.DiskInfo> mounts) {
        this.mounts = mounts;
    }

    public java.util.List<org.apache.ambari.server.state.Alert> getAlerts() {
        return alerts;
    }

    public void setAlerts(java.util.List<org.apache.ambari.server.state.Alert> alerts) {
        this.alerts = alerts;
    }

    public java.util.List<org.apache.ambari.server.agent.StaleAlert> getStaleAlerts() {
        return staleAlerts;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((((((((((((("HeartBeat{" + "responseId=") + responseId) + ", timestamp=") + timestamp) + ", hostname='") + hostname) + '\'') + ", reports=") + reports) + ", componentStatus=") + componentStatus) + ", nodeStatus=") + nodeStatus) + ", recoveryReport=") + recoveryReport) + '}';
    }
}