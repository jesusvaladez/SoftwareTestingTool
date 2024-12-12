package org.apache.ambari.server.agent;
import org.codehaus.jackson.annotate.JsonProperty;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
public class RegistrationResponse extends org.apache.ambari.server.agent.stomp.StompResponse {
    @org.codehaus.jackson.annotate.JsonProperty("response")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private org.apache.ambari.server.agent.RegistrationStatus response;

    @org.codehaus.jackson.annotate.JsonProperty("alertDefinitionCommands")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private java.util.List<org.apache.ambari.server.agent.AlertDefinitionCommand> alertDefinitionCommands = new java.util.ArrayList<>();

    @org.codehaus.jackson.annotate.JsonProperty("exitstatus")
    @com.fasterxml.jackson.annotation.JsonProperty("exitstatus")
    private int exitstatus;

    @org.codehaus.jackson.annotate.JsonProperty("log")
    @com.fasterxml.jackson.annotation.JsonProperty("log")
    private java.lang.String log;

    @org.codehaus.jackson.annotate.JsonProperty("responseId")
    @com.fasterxml.jackson.annotation.JsonProperty("id")
    private long responseId;

    @org.codehaus.jackson.annotate.JsonProperty("recoveryConfig")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private org.apache.ambari.server.agent.RecoveryConfig recoveryConfig;

    @org.codehaus.jackson.annotate.JsonProperty("agentConfig")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private java.util.Map<java.lang.String, java.lang.String> agentConfig;

    @org.codehaus.jackson.annotate.JsonProperty("statusCommands")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private java.util.List<org.apache.ambari.server.agent.StatusCommand> statusCommands = null;

    @com.fasterxml.jackson.annotation.JsonIgnore
    public org.apache.ambari.server.agent.RegistrationStatus getResponseStatus() {
        return response;
    }

    public void setResponseStatus(org.apache.ambari.server.agent.RegistrationStatus response) {
        this.response = response;
    }

    public java.util.List<org.apache.ambari.server.agent.StatusCommand> getStatusCommands() {
        return statusCommands;
    }

    public void setStatusCommands(java.util.List<org.apache.ambari.server.agent.StatusCommand> statusCommands) {
        this.statusCommands = statusCommands;
    }

    public java.util.List<org.apache.ambari.server.agent.AlertDefinitionCommand> getAlertDefinitionCommands() {
        return alertDefinitionCommands;
    }

    public void setAlertDefinitionCommands(java.util.List<org.apache.ambari.server.agent.AlertDefinitionCommand> commands) {
        alertDefinitionCommands = commands;
    }

    public long getResponseId() {
        return responseId;
    }

    public void setResponseId(long responseId) {
        this.responseId = responseId;
    }

    public void setExitstatus(int exitstatus) {
        this.exitstatus = exitstatus;
    }

    public void setLog(java.lang.String log) {
        this.log = log;
    }

    public org.apache.ambari.server.agent.RecoveryConfig getRecoveryConfig() {
        return recoveryConfig;
    }

    public void setRecoveryConfig(org.apache.ambari.server.agent.RecoveryConfig recoveryConfig) {
        this.recoveryConfig = recoveryConfig;
    }

    public java.util.Map<java.lang.String, java.lang.String> getAgentConfig() {
        return agentConfig;
    }

    public void setAgentConfig(java.util.Map<java.lang.String, java.lang.String> agentConfig) {
        this.agentConfig = agentConfig;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder buffer = new java.lang.StringBuilder("RegistrationResponse{");
        buffer.append("response=").append(response);
        buffer.append(", responseId=").append(responseId);
        buffer.append(", statusCommands=").append(statusCommands);
        buffer.append(", alertDefinitionCommands=").append(alertDefinitionCommands);
        buffer.append(", recoveryConfig=").append(recoveryConfig);
        buffer.append(", agentConfig=").append(agentConfig);
        buffer.append('}');
        return buffer.toString();
    }
}