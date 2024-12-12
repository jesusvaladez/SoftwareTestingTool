package org.apache.ambari.server.agent.stomp.dto;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
public class ExecutionCommandsCluster {
    @com.fasterxml.jackson.annotation.JsonProperty("commands")
    private java.util.List<org.apache.ambari.server.agent.ExecutionCommand> executionCommands = new java.util.ArrayList<>();

    @com.fasterxml.jackson.annotation.JsonProperty("cancelCommands")
    private java.util.List<org.apache.ambari.server.agent.CancelCommand> cancelCommands = new java.util.ArrayList<>();

    public ExecutionCommandsCluster(java.util.List<org.apache.ambari.server.agent.ExecutionCommand> executionCommands, java.util.List<org.apache.ambari.server.agent.CancelCommand> cancelCommands) {
        this.executionCommands = executionCommands;
        this.cancelCommands = cancelCommands;
    }

    public java.util.List<org.apache.ambari.server.agent.ExecutionCommand> getExecutionCommands() {
        return executionCommands;
    }

    public void setExecutionCommands(java.util.List<org.apache.ambari.server.agent.ExecutionCommand> executionCommands) {
        this.executionCommands = executionCommands;
    }

    public java.util.List<org.apache.ambari.server.agent.CancelCommand> getCancelCommands() {
        return cancelCommands;
    }

    public void setCancelCommands(java.util.List<org.apache.ambari.server.agent.CancelCommand> cancelCommands) {
        this.cancelCommands = cancelCommands;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.agent.stomp.dto.ExecutionCommandsCluster that = ((org.apache.ambari.server.agent.stomp.dto.ExecutionCommandsCluster) (o));
        if (executionCommands != null ? !executionCommands.equals(that.executionCommands) : that.executionCommands != null)
            return false;

        return cancelCommands != null ? cancelCommands.equals(that.cancelCommands) : that.cancelCommands == null;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (executionCommands != null) ? executionCommands.hashCode() : 0;
        result = (31 * result) + (cancelCommands != null ? cancelCommands.hashCode() : 0);
        return result;
    }
}