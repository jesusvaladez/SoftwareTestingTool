package org.apache.ambari.server.controller.internal;
public class UpgradeSummary {
    private java.lang.String displayText;

    private java.lang.Long requestId;

    private java.lang.Long stageId;

    private java.lang.Long taskId;

    private java.lang.String hostName;

    private org.apache.ambari.server.orm.entities.HostRoleCommandEntity failedTask;

    public UpgradeSummary(java.lang.String displayText, java.lang.Long requestId, java.lang.Long stageId, java.lang.Long taskId, java.lang.String hostName, org.apache.ambari.server.orm.entities.HostRoleCommandEntity failedTask) {
        this.displayText = displayText;
        this.requestId = requestId;
        this.stageId = stageId;
        this.taskId = taskId;
        this.hostName = hostName;
        this.failedTask = failedTask;
    }

    public java.lang.Long getStageId() {
        return stageId;
    }

    public java.lang.Long getTaskId() {
        return taskId;
    }

    public UpgradeSummary(org.apache.ambari.server.orm.entities.HostRoleCommandEntity hrc) {
        this("", hrc.getRequestId(), hrc.getStageId(), hrc.getTaskId(), hrc.getHostName(), hrc);
        displayText = "Failed";
        if (hrc.getCommandDetail() != null) {
            displayText += " calling " + hrc.getCommandDetail();
        }
        if (hrc.getHostName() != null) {
            displayText += " on host " + hrc.getHostName();
        }
    }

    public java.lang.String getDisplayText() {
        return this.displayText;
    }

    public org.apache.ambari.server.orm.entities.HostRoleCommandEntity getFailedTask() {
        return failedTask;
    }
}