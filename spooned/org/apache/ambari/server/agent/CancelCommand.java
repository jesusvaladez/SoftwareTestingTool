package org.apache.ambari.server.agent;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
public class CancelCommand extends org.apache.ambari.server.agent.AgentCommand {
    public CancelCommand() {
        super(org.apache.ambari.server.agent.AgentCommand.AgentCommandType.CANCEL_COMMAND);
    }

    @com.google.gson.annotations.SerializedName("target_task_id")
    @com.fasterxml.jackson.annotation.JsonProperty("target_task_id")
    private long targetTaskId;

    @com.google.gson.annotations.SerializedName("reason")
    @com.fasterxml.jackson.annotation.JsonProperty("reason")
    private java.lang.String reason;

    public long getTargetTaskId() {
        return targetTaskId;
    }

    public void setTargetTaskId(long targetTaskId) {
        this.targetTaskId = targetTaskId;
    }

    public java.lang.String getReason() {
        return reason;
    }

    public void setReason(java.lang.String reason) {
        this.reason = reason;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.agent.CancelCommand that = ((org.apache.ambari.server.agent.CancelCommand) (o));
        if (targetTaskId != that.targetTaskId)
            return false;

        return reason != null ? reason.equals(that.reason) : that.reason == null;
    }

    @java.lang.Override
    public int hashCode() {
        int result = ((int) (targetTaskId ^ (targetTaskId >>> 32)));
        result = (31 * result) + (reason != null ? reason.hashCode() : 0);
        return result;
    }
}