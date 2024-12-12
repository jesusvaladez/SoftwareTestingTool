package org.apache.ambari.server.controller;
import org.apache.commons.lang.StringUtils;
public class ShortTaskStatus {
    protected long requestId;

    protected long taskId;

    protected long stageId;

    protected java.lang.String hostName;

    protected java.lang.String role;

    protected java.lang.String command;

    protected java.lang.String status;

    protected java.lang.String customCommandName;

    protected java.lang.String outputLog;

    protected java.lang.String errorLog;

    public ShortTaskStatus() {
    }

    public ShortTaskStatus(int taskId, long stageId, java.lang.String hostName, java.lang.String role, java.lang.String command, java.lang.String status, java.lang.String customCommandName, java.lang.String outputLog, java.lang.String errorLog) {
        this.taskId = taskId;
        this.stageId = stageId;
        this.hostName = translateHostName(hostName);
        this.role = role;
        this.command = command;
        this.status = status;
        this.customCommandName = customCommandName;
        this.outputLog = outputLog;
        this.errorLog = errorLog;
    }

    public ShortTaskStatus(org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand) {
        this.taskId = hostRoleCommand.getTaskId();
        this.stageId = hostRoleCommand.getStageId();
        this.command = hostRoleCommand.getRoleCommand().toString();
        this.hostName = translateHostName(hostRoleCommand.getHostName());
        this.role = hostRoleCommand.getRole().toString();
        this.status = hostRoleCommand.getStatus().toString();
        this.customCommandName = hostRoleCommand.getCustomCommandName();
        this.outputLog = hostRoleCommand.getOutputLog();
        this.errorLog = hostRoleCommand.getErrorLog();
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public long getRequestId() {
        return requestId;
    }

    public java.lang.String getCustomCommandName() {
        return customCommandName;
    }

    public void setCustomCommandName(java.lang.String customCommandName) {
        this.customCommandName = customCommandName;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public long getStageId() {
        return stageId;
    }

    public void setStageId(long stageId) {
        this.stageId = stageId;
    }

    public java.lang.String getHostName() {
        return hostName;
    }

    public void setHostName(java.lang.String hostName) {
        this.hostName = translateHostName(hostName);
    }

    public java.lang.String getRole() {
        return role;
    }

    public void setRole(java.lang.String role) {
        this.role = role;
    }

    public java.lang.String getCommand() {
        return command;
    }

    public void setCommand(java.lang.String command) {
        this.command = command;
    }

    public java.lang.String getOutputLog() {
        return outputLog;
    }

    public void setOutputLog(java.lang.String outputLog) {
        this.outputLog = outputLog;
    }

    public java.lang.String getErrorLog() {
        return errorLog;
    }

    public void setErrorLog(java.lang.String errorLog) {
        this.errorLog = errorLog;
    }

    public java.lang.String getStatus() {
        return status;
    }

    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("ShortTaskStatusDump ").append(", stageId=").append(stageId).append(", taskId=").append(taskId).append(", hostname=").append(hostName).append(", role=").append(role).append(", command=").append(command).append(", status=").append(status).append(", outputLog=").append(outputLog).append(", errorLog=").append(errorLog);
        return sb.toString();
    }

    private java.lang.String translateHostName(java.lang.String hostName) {
        return org.apache.commons.lang.StringUtils.isEmpty(hostName) ? org.apache.ambari.server.utils.StageUtils.getHostName() : hostName;
    }
}