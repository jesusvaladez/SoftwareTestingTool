package org.apache.ambari.server.agent;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
public class HeartBeatResponse extends org.apache.ambari.server.agent.stomp.StompResponse {
    @com.google.gson.annotations.SerializedName("responseId")
    @com.fasterxml.jackson.annotation.JsonProperty("id")
    private long responseId;

    @com.google.gson.annotations.SerializedName("executionCommands")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private java.util.List<org.apache.ambari.server.agent.ExecutionCommand> executionCommands = new java.util.ArrayList<>();

    @com.google.gson.annotations.SerializedName("statusCommands")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private java.util.List<org.apache.ambari.server.agent.StatusCommand> statusCommands = new java.util.ArrayList<>();

    @com.google.gson.annotations.SerializedName("cancelCommands")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private java.util.List<org.apache.ambari.server.agent.CancelCommand> cancelCommands = new java.util.ArrayList<>();

    @com.google.gson.annotations.SerializedName("alertDefinitionCommands")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private java.util.List<org.apache.ambari.server.agent.AlertDefinitionCommand> alertDefinitionCommands = null;

    @com.google.gson.annotations.SerializedName("alertExecutionCommands")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private java.util.List<org.apache.ambari.server.agent.AlertExecutionCommand> alertExecutionCommands = null;

    @com.google.gson.annotations.SerializedName("registrationCommand")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private org.apache.ambari.server.agent.RegistrationCommand registrationCommand;

    @com.google.gson.annotations.SerializedName("restartAgent")
    @com.fasterxml.jackson.annotation.JsonProperty("restartAgent")
    private java.lang.Boolean restartAgent = null;

    @com.google.gson.annotations.SerializedName("hasMappedComponents")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private boolean hasMappedComponents = false;

    @com.google.gson.annotations.SerializedName("hasPendingTasks")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private boolean hasPendingTasks = false;

    @com.google.gson.annotations.SerializedName("recoveryConfig")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private org.apache.ambari.server.agent.RecoveryConfig recoveryConfig;

    @com.google.gson.annotations.SerializedName("clusterSize")
    @com.fasterxml.jackson.annotation.JsonIgnore
    private int clusterSize = -1;

    public long getResponseId() {
        return responseId;
    }

    public void setResponseId(long responseId) {
        this.responseId = responseId;
    }

    public java.util.List<org.apache.ambari.server.agent.ExecutionCommand> getExecutionCommands() {
        return executionCommands;
    }

    public void setExecutionCommands(java.util.List<org.apache.ambari.server.agent.ExecutionCommand> executionCommands) {
        this.executionCommands = executionCommands;
    }

    public java.util.List<org.apache.ambari.server.agent.StatusCommand> getStatusCommands() {
        return statusCommands;
    }

    public void setStatusCommands(java.util.List<org.apache.ambari.server.agent.StatusCommand> statusCommands) {
        this.statusCommands = statusCommands;
    }

    public java.util.List<org.apache.ambari.server.agent.CancelCommand> getCancelCommands() {
        return cancelCommands;
    }

    public void setCancelCommands(java.util.List<org.apache.ambari.server.agent.CancelCommand> cancelCommands) {
        this.cancelCommands = cancelCommands;
    }

    public org.apache.ambari.server.agent.RegistrationCommand getRegistrationCommand() {
        return registrationCommand;
    }

    public void setRegistrationCommand(org.apache.ambari.server.agent.RegistrationCommand registrationCommand) {
        this.registrationCommand = registrationCommand;
    }

    public org.apache.ambari.server.agent.RecoveryConfig getRecoveryConfig() {
        return recoveryConfig;
    }

    public void setRecoveryConfig(org.apache.ambari.server.agent.RecoveryConfig recoveryConfig) {
        this.recoveryConfig = recoveryConfig;
    }

    public java.util.List<org.apache.ambari.server.agent.AlertDefinitionCommand> getAlertDefinitionCommands() {
        return alertDefinitionCommands;
    }

    public void setAlertDefinitionCommands(java.util.List<org.apache.ambari.server.agent.AlertDefinitionCommand> commands) {
        alertDefinitionCommands = commands;
    }

    public java.lang.Boolean isRestartAgent() {
        return restartAgent;
    }

    public void setRestartAgent(java.lang.Boolean restartAgent) {
        this.restartAgent = restartAgent;
    }

    public boolean hasMappedComponents() {
        return hasMappedComponents;
    }

    public void setHasMappedComponents(boolean hasMappedComponents) {
        this.hasMappedComponents = hasMappedComponents;
    }

    public boolean hasPendingTasks() {
        return hasPendingTasks;
    }

    public void setHasPendingTasks(boolean hasPendingTasks) {
        this.hasPendingTasks = hasPendingTasks;
    }

    public void addExecutionCommand(org.apache.ambari.server.agent.ExecutionCommand execCmd) {
        executionCommands.add(execCmd);
    }

    public void addStatusCommand(org.apache.ambari.server.agent.StatusCommand statCmd) {
        statusCommands.add(statCmd);
    }

    public void addCancelCommand(org.apache.ambari.server.agent.CancelCommand cancelCmd) {
        cancelCommands.add(cancelCmd);
    }

    public void addAlertDefinitionCommand(org.apache.ambari.server.agent.AlertDefinitionCommand command) {
        if (null == alertDefinitionCommands) {
            alertDefinitionCommands = new java.util.ArrayList<>();
        }
        alertDefinitionCommands.add(command);
    }

    public void addAlertExecutionCommand(org.apache.ambari.server.agent.AlertExecutionCommand command) {
        if (null == alertExecutionCommands) {
            alertExecutionCommands = new java.util.ArrayList<>();
        }
        alertExecutionCommands.add(command);
    }

    public void setClusterSize(int clusterSize) {
        this.clusterSize = clusterSize;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder buffer = new java.lang.StringBuilder("HeartBeatResponse{");
        buffer.append("responseId=").append(responseId);
        buffer.append(", executionCommands=").append(executionCommands);
        buffer.append(", statusCommands=").append(statusCommands);
        buffer.append(", cancelCommands=").append(cancelCommands);
        buffer.append(", alertDefinitionCommands=").append(alertDefinitionCommands);
        buffer.append(", registrationCommand=").append(registrationCommand);
        buffer.append(", restartAgent=").append(restartAgent);
        buffer.append(", recoveryConfig=").append(recoveryConfig);
        buffer.append('}');
        return buffer.toString();
    }
}