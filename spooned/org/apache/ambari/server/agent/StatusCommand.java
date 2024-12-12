package org.apache.ambari.server.agent;
public class StatusCommand extends org.apache.ambari.server.agent.AgentCommand {
    public StatusCommand() {
        super(org.apache.ambari.server.agent.AgentCommand.AgentCommandType.STATUS_COMMAND);
    }

    @com.google.gson.annotations.SerializedName("clusterName")
    @com.fasterxml.jackson.annotation.JsonProperty("clusterName")
    private java.lang.String clusterName;

    @com.google.gson.annotations.SerializedName("serviceName")
    @com.fasterxml.jackson.annotation.JsonProperty("serviceName")
    private java.lang.String serviceName;

    @com.google.gson.annotations.SerializedName("role")
    private java.lang.String role;

    @com.google.gson.annotations.SerializedName("componentName")
    @com.fasterxml.jackson.annotation.JsonProperty("componentName")
    private java.lang.String componentName;

    @com.google.gson.annotations.SerializedName("configurations")
    @com.fasterxml.jackson.annotation.JsonProperty("configurations")
    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations;

    @com.google.gson.annotations.SerializedName("configurationAttributes")
    @com.fasterxml.jackson.annotation.JsonProperty("configurationAttributes")
    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> configurationAttributes;

    @com.google.gson.annotations.SerializedName("commandParams")
    @com.fasterxml.jackson.annotation.JsonProperty("commandParams")
    private java.util.Map<java.lang.String, java.lang.String> commandParams = new java.util.HashMap<>();

    @com.google.gson.annotations.SerializedName("hostLevelParams")
    @com.fasterxml.jackson.annotation.JsonProperty("hostLevelParams")
    private java.util.Map<java.lang.String, java.lang.String> hostLevelParams = new java.util.HashMap<>();

    @com.google.gson.annotations.SerializedName("hostname")
    @com.fasterxml.jackson.annotation.JsonProperty("hostname")
    private java.lang.String hostname = null;

    @com.google.gson.annotations.SerializedName("payloadLevel")
    @com.fasterxml.jackson.annotation.JsonProperty("payloadLevel")
    private org.apache.ambari.server.agent.StatusCommand.StatusCommandPayload payloadLevel = org.apache.ambari.server.agent.StatusCommand.StatusCommandPayload.DEFAULT;

    @com.google.gson.annotations.SerializedName("desiredState")
    @com.fasterxml.jackson.annotation.JsonProperty("desiredState")
    private org.apache.ambari.server.state.State desiredState;

    @com.google.gson.annotations.SerializedName("hasStaleConfigs")
    @com.fasterxml.jackson.annotation.JsonProperty("hasStaleConfigs")
    private java.lang.Boolean hasStaleConfigs;

    @com.google.gson.annotations.SerializedName("executionCommandDetails")
    @com.fasterxml.jackson.annotation.JsonProperty("executionCommandDetails")
    private org.apache.ambari.server.agent.ExecutionCommand executionCommand;

    public org.apache.ambari.server.agent.ExecutionCommand getExecutionCommand() {
        return executionCommand;
    }

    public void setExecutionCommand(org.apache.ambari.server.agent.ExecutionCommand executionCommand) {
        this.executionCommand = executionCommand;
    }

    public org.apache.ambari.server.state.State getDesiredState() {
        return desiredState;
    }

    public void setDesiredState(org.apache.ambari.server.state.State desiredState) {
        this.desiredState = desiredState;
    }

    public java.lang.Boolean getHasStaleConfigs() {
        return hasStaleConfigs;
    }

    public void setHasStaleConfigs(java.lang.Boolean hasStaleConfigs) {
        this.hasStaleConfigs = hasStaleConfigs;
    }

    public org.apache.ambari.server.agent.StatusCommand.StatusCommandPayload getPayloadLevel() {
        return payloadLevel;
    }

    public void setPayloadLevel(org.apache.ambari.server.agent.StatusCommand.StatusCommandPayload payloadLevel) {
        this.payloadLevel = payloadLevel;
    }

    public java.lang.String getClusterName() {
        return clusterName;
    }

    public void setClusterName(java.lang.String clusterName) {
        this.clusterName = clusterName;
    }

    public java.lang.String getServiceName() {
        return serviceName;
    }

    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }

    public java.lang.String getComponentName() {
        return componentName;
    }

    public void setComponentName(java.lang.String componentName) {
        this.componentName = componentName;
        role = componentName;
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations) {
        this.configurations = configurations;
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> getConfigurationAttributes() {
        return configurationAttributes;
    }

    public void setConfigurationAttributes(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> configurationAttributes) {
        this.configurationAttributes = configurationAttributes;
    }

    public java.util.Map<java.lang.String, java.lang.String> getHostLevelParams() {
        return hostLevelParams;
    }

    public void setHostLevelParams(java.util.Map<java.lang.String, java.lang.String> params) {
        hostLevelParams = params;
    }

    public java.util.Map<java.lang.String, java.lang.String> getCommandParams() {
        return commandParams;
    }

    public void setCommandParams(java.util.Map<java.lang.String, java.lang.String> commandParams) {
        this.commandParams = commandParams;
    }

    public void setHostname(java.lang.String hostname) {
        this.hostname = hostname;
    }

    public java.lang.String getHostname() {
        return hostname;
    }

    public java.lang.String getRole() {
        return role;
    }

    public enum StatusCommandPayload {

        MINIMAL,
        DEFAULT,
        EXECUTION_COMMAND;}
}