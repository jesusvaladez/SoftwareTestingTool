package org.apache.ambari.server.agent;
public abstract class AgentCommand {
    private org.apache.ambari.server.agent.AgentCommand.AgentCommandType commandType;

    public AgentCommand() {
        commandType = org.apache.ambari.server.agent.AgentCommand.AgentCommandType.STATUS_COMMAND;
    }

    public AgentCommand(org.apache.ambari.server.agent.AgentCommand.AgentCommandType type) {
        commandType = type;
    }

    public enum AgentCommandType {

        EXECUTION_COMMAND,
        BACKGROUND_EXECUTION_COMMAND,
        STATUS_COMMAND,
        CANCEL_COMMAND,
        REGISTRATION_COMMAND,
        ALERT_DEFINITION_COMMAND,
        ALERT_EXECUTION_COMMAND;}

    public org.apache.ambari.server.agent.AgentCommand.AgentCommandType getCommandType() {
        return commandType;
    }

    public void setCommandType(org.apache.ambari.server.agent.AgentCommand.AgentCommandType commandType) {
        this.commandType = commandType;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (("AgentCommand{" + "commandType=") + commandType) + '}';
    }
}