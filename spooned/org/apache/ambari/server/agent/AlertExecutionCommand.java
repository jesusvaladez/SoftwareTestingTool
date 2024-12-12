package org.apache.ambari.server.agent;
public class AlertExecutionCommand extends org.apache.ambari.server.agent.AgentCommand {
    @com.google.gson.annotations.SerializedName("clusterName")
    @com.fasterxml.jackson.annotation.JsonProperty("clusterName")
    private final java.lang.String m_clusterName;

    @com.google.gson.annotations.SerializedName("hostName")
    @com.fasterxml.jackson.annotation.JsonProperty("hostName")
    private final java.lang.String m_hostName;

    @com.google.gson.annotations.SerializedName("alertDefinition")
    @com.fasterxml.jackson.annotation.JsonProperty("alertDefinition")
    private final org.apache.ambari.server.state.alert.AlertDefinition m_definition;

    public AlertExecutionCommand(java.lang.String clusterName, java.lang.String hostName, org.apache.ambari.server.state.alert.AlertDefinition definition) {
        super(org.apache.ambari.server.agent.AgentCommand.AgentCommandType.ALERT_EXECUTION_COMMAND);
        m_clusterName = clusterName;
        m_hostName = hostName;
        m_definition = definition;
    }

    @java.lang.Override
    public org.apache.ambari.server.agent.AgentCommand.AgentCommandType getCommandType() {
        return org.apache.ambari.server.agent.AgentCommand.AgentCommandType.ALERT_EXECUTION_COMMAND;
    }
}