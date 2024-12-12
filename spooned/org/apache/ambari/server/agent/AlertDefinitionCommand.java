package org.apache.ambari.server.agent;
public class AlertDefinitionCommand extends org.apache.ambari.server.agent.AgentCommand {
    @com.google.gson.annotations.SerializedName("clusterName")
    private final java.lang.String m_clusterName;

    @com.google.gson.annotations.SerializedName("hostName")
    private final java.lang.String m_hostName;

    @com.google.gson.annotations.SerializedName("publicHostName")
    private final java.lang.String m_publicHostName;

    @com.google.gson.annotations.SerializedName("hash")
    private final java.lang.String m_hash;

    @com.google.gson.annotations.SerializedName("alertDefinitions")
    @com.fasterxml.jackson.annotation.JsonProperty("alertDefinitions")
    private final java.util.List<org.apache.ambari.server.state.alert.AlertDefinition> m_definitions;

    @com.google.gson.annotations.SerializedName("configurations")
    @com.fasterxml.jackson.annotation.JsonProperty("configurations")
    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> m_configurations;

    public AlertDefinitionCommand(java.lang.String clusterName, java.lang.String hostName, java.lang.String publicHostName, java.lang.String hash, java.util.List<org.apache.ambari.server.state.alert.AlertDefinition> definitions) {
        super(org.apache.ambari.server.agent.AgentCommand.AgentCommandType.ALERT_DEFINITION_COMMAND);
        m_clusterName = clusterName;
        m_hostName = hostName;
        m_publicHostName = publicHostName;
        m_hash = hash;
        m_definitions = definitions;
    }

    @java.lang.Override
    public org.apache.ambari.server.agent.AgentCommand.AgentCommandType getCommandType() {
        return org.apache.ambari.server.agent.AgentCommand.AgentCommandType.ALERT_DEFINITION_COMMAND;
    }

    public java.lang.String getHash() {
        return m_hash;
    }

    public java.util.List<org.apache.ambari.server.state.alert.AlertDefinition> getAlertDefinitions() {
        return m_definitions;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("clusterName")
    public java.lang.String getClusterName() {
        return m_clusterName;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("hostName")
    public java.lang.String getHostName() {
        return m_hostName;
    }

    public void addConfigs(org.apache.ambari.server.state.ConfigHelper configHelper, org.apache.ambari.server.state.Cluster cluster) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configTags = configHelper.getEffectiveDesiredTags(cluster, m_hostName);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations = configHelper.getEffectiveConfigProperties(cluster, configTags);
        m_configurations = configurations;
    }
}