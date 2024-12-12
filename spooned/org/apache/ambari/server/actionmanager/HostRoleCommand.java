package org.apache.ambari.server.actionmanager;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
public class HostRoleCommand {
    private final org.apache.ambari.server.Role role;

    private final org.apache.ambari.server.actionmanager.ServiceComponentHostEventWrapper event;

    private long taskId = -1;

    private long stageId = -1;

    private long requestId = -1;

    private long hostId = -1;

    private java.lang.String hostName;

    private org.apache.ambari.server.actionmanager.HostRoleStatus status = org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING;

    private java.lang.String stdout = "";

    private java.lang.String stderr = "";

    public java.lang.String outputLog = null;

    public java.lang.String errorLog = null;

    private java.lang.String structuredOut = "";

    private int exitCode = 999;

    private long startTime = -1;

    private long originalStartTime = -1;

    private long endTime = -1;

    private long lastAttemptTime = -1;

    private short attemptCount = 0;

    private final boolean retryAllowed;

    private final boolean autoSkipFailure;

    private org.apache.ambari.server.RoleCommand roleCommand;

    private java.lang.String commandDetail;

    private java.lang.String customCommandName;

    private org.apache.ambari.server.actionmanager.ExecutionCommandWrapper executionCommandWrapper;

    private boolean isBackgroundCommand = false;

    private java.lang.String opsDisplayName;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.ExecutionCommandDAO executionCommandDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.actionmanager.ExecutionCommandWrapperFactory ecwFactory;

    @com.google.inject.assistedinject.AssistedInject
    public HostRoleCommand(java.lang.String hostName, org.apache.ambari.server.Role role, org.apache.ambari.server.state.ServiceComponentHostEvent event, org.apache.ambari.server.RoleCommand command, org.apache.ambari.server.orm.dao.HostDAO hostDAO, org.apache.ambari.server.orm.dao.ExecutionCommandDAO executionCommandDAO, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperFactory ecwFactory) {
        this(hostName, role, event, command, false, false, hostDAO, executionCommandDAO, ecwFactory);
    }

    @com.google.inject.assistedinject.AssistedInject
    public HostRoleCommand(java.lang.String hostName, org.apache.ambari.server.Role role, org.apache.ambari.server.state.ServiceComponentHostEvent event, org.apache.ambari.server.RoleCommand roleCommand, boolean retryAllowed, boolean autoSkipFailure, org.apache.ambari.server.orm.dao.HostDAO hostDAO, org.apache.ambari.server.orm.dao.ExecutionCommandDAO executionCommandDAO, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperFactory ecwFactory) {
        this.hostDAO = hostDAO;
        this.executionCommandDAO = executionCommandDAO;
        this.ecwFactory = ecwFactory;
        this.role = role;
        this.event = new org.apache.ambari.server.actionmanager.ServiceComponentHostEventWrapper(event);
        this.roleCommand = roleCommand;
        this.retryAllowed = retryAllowed;
        this.autoSkipFailure = autoSkipFailure;
        this.hostName = hostName;
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = this.hostDAO.findByName(hostName);
        if (null != hostEntity) {
            hostId = hostEntity.getHostId();
        }
    }

    @com.google.inject.assistedinject.AssistedInject
    public HostRoleCommand(org.apache.ambari.server.state.Host host, org.apache.ambari.server.Role role, org.apache.ambari.server.state.ServiceComponentHostEvent event, org.apache.ambari.server.RoleCommand roleCommand, boolean retryAllowed, boolean autoSkipFailure, org.apache.ambari.server.orm.dao.HostDAO hostDAO, org.apache.ambari.server.orm.dao.ExecutionCommandDAO executionCommandDAO, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperFactory ecwFactory) {
        this.hostDAO = hostDAO;
        this.executionCommandDAO = executionCommandDAO;
        this.ecwFactory = ecwFactory;
        this.role = role;
        this.event = new org.apache.ambari.server.actionmanager.ServiceComponentHostEventWrapper(event);
        this.roleCommand = roleCommand;
        this.retryAllowed = retryAllowed;
        this.autoSkipFailure = autoSkipFailure;
        hostId = host.getHostId();
        hostName = host.getHostName();
    }

    @com.google.inject.assistedinject.AssistedInject
    public HostRoleCommand(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.orm.entities.HostRoleCommandEntity hostRoleCommandEntity, org.apache.ambari.server.orm.dao.HostDAO hostDAO, org.apache.ambari.server.orm.dao.ExecutionCommandDAO executionCommandDAO, org.apache.ambari.server.actionmanager.ExecutionCommandWrapperFactory ecwFactory) {
        this.hostDAO = hostDAO;
        this.executionCommandDAO = executionCommandDAO;
        this.ecwFactory = ecwFactory;
        taskId = hostRoleCommandEntity.getTaskId();
        stageId = (null != hostRoleCommandEntity.getStageId()) ? hostRoleCommandEntity.getStageId() : hostRoleCommandEntity.getStage().getStageId();
        requestId = (null != hostRoleCommandEntity.getRequestId()) ? hostRoleCommandEntity.getRequestId() : hostRoleCommandEntity.getStage().getRequestId();
        if (null != hostRoleCommandEntity.getHostEntity()) {
            hostId = hostRoleCommandEntity.getHostId();
        }
        hostName = hostRoleCommandEntity.getHostName();
        role = hostRoleCommandEntity.getRole();
        status = hostRoleCommandEntity.getStatus();
        stdout = (hostRoleCommandEntity.getStdOut() != null) ? new java.lang.String(hostRoleCommandEntity.getStdOut()) : "";
        stderr = (hostRoleCommandEntity.getStdError() != null) ? new java.lang.String(hostRoleCommandEntity.getStdError()) : "";
        outputLog = hostRoleCommandEntity.getOutputLog();
        errorLog = hostRoleCommandEntity.getErrorLog();
        structuredOut = (hostRoleCommandEntity.getStructuredOut() != null) ? new java.lang.String(hostRoleCommandEntity.getStructuredOut()) : "";
        exitCode = hostRoleCommandEntity.getExitcode();
        startTime = (hostRoleCommandEntity.getStartTime() != null) ? hostRoleCommandEntity.getStartTime() : -1L;
        originalStartTime = (hostRoleCommandEntity.getOriginalStartTime() != null) ? hostRoleCommandEntity.getOriginalStartTime() : -1L;
        endTime = (hostRoleCommandEntity.getEndTime() != null) ? hostRoleCommandEntity.getEndTime() : -1L;
        lastAttemptTime = (hostRoleCommandEntity.getLastAttemptTime() != null) ? hostRoleCommandEntity.getLastAttemptTime() : -1L;
        attemptCount = hostRoleCommandEntity.getAttemptCount();
        retryAllowed = hostRoleCommandEntity.isRetryAllowed();
        autoSkipFailure = hostRoleCommandEntity.isFailureAutoSkipped();
        roleCommand = hostRoleCommandEntity.getRoleCommand();
        event = new org.apache.ambari.server.actionmanager.ServiceComponentHostEventWrapper(hostRoleCommandEntity.getEvent());
        commandDetail = hostRoleCommandEntity.getCommandDetail();
        opsDisplayName = hostRoleCommandEntity.getOpsDisplayName();
        customCommandName = hostRoleCommandEntity.getCustomCommandName();
        isBackgroundCommand = hostRoleCommandEntity.isBackgroundCommand();
    }

    public org.apache.ambari.server.orm.entities.HostRoleCommandEntity constructNewPersistenceEntity() {
        org.apache.ambari.server.orm.entities.HostRoleCommandEntity hostRoleCommandEntity = new org.apache.ambari.server.orm.entities.HostRoleCommandEntity();
        hostRoleCommandEntity.setRole(role);
        hostRoleCommandEntity.setStatus(status);
        hostRoleCommandEntity.setStdError(stderr.getBytes());
        hostRoleCommandEntity.setExitcode(exitCode);
        hostRoleCommandEntity.setStdOut(stdout.getBytes());
        hostRoleCommandEntity.setStructuredOut(structuredOut.getBytes());
        hostRoleCommandEntity.setStartTime(startTime);
        hostRoleCommandEntity.setOriginalStartTime(originalStartTime);
        hostRoleCommandEntity.setEndTime(endTime);
        hostRoleCommandEntity.setLastAttemptTime(lastAttemptTime);
        hostRoleCommandEntity.setAttemptCount(attemptCount);
        hostRoleCommandEntity.setRetryAllowed(retryAllowed);
        hostRoleCommandEntity.setAutoSkipOnFailure(autoSkipFailure);
        hostRoleCommandEntity.setRoleCommand(roleCommand);
        hostRoleCommandEntity.setCommandDetail(commandDetail);
        hostRoleCommandEntity.setOpsDisplayName(opsDisplayName);
        hostRoleCommandEntity.setCustomCommandName(customCommandName);
        hostRoleCommandEntity.setBackgroundCommand(isBackgroundCommand);
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = hostDAO.findById(hostId);
        if (null != hostEntity) {
            hostRoleCommandEntity.setHostEntity(hostEntity);
        }
        hostRoleCommandEntity.setEvent(event.getEventJson());
        if (requestId >= 0) {
            hostRoleCommandEntity.setRequestId(requestId);
        }
        if (stageId >= 0) {
            hostRoleCommandEntity.setStageId(stageId);
        }
        if (taskId >= 0) {
            hostRoleCommandEntity.setTaskId(taskId);
        }
        return hostRoleCommandEntity;
    }

    org.apache.ambari.server.orm.entities.ExecutionCommandEntity constructExecutionCommandEntity() {
        org.apache.ambari.server.orm.entities.ExecutionCommandEntity executionCommandEntity = new org.apache.ambari.server.orm.entities.ExecutionCommandEntity();
        executionCommandEntity.setCommand(executionCommandWrapper.getJson().getBytes());
        return executionCommandEntity;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public void setStageId(long stageId) {
        this.stageId = stageId;
    }

    public void setTaskId(long taskId) {
        if (this.taskId != (-1)) {
            throw new java.lang.RuntimeException("Attempt to set taskId again, not allowed");
        }
        this.taskId = taskId;
        if (executionCommandWrapper != null) {
            executionCommandWrapper.getExecutionCommand().setTaskId(taskId);
            executionCommandWrapper.invalidateJson();
        }
    }

    public void setHost(long hostId, java.lang.String hostName) {
        this.hostId = hostId;
        this.hostName = hostName;
    }

    public java.lang.String getHostName() {
        return hostName;
    }

    public long getHostId() {
        return hostId;
    }

    public org.apache.ambari.server.Role getRole() {
        return role;
    }

    public java.lang.String getCommandDetail() {
        return commandDetail;
    }

    public void setCommandDetail(java.lang.String commandDetail) {
        this.commandDetail = commandDetail;
    }

    public java.lang.String getOpsDisplayName() {
        return opsDisplayName;
    }

    public void setOpsDisplayName(java.lang.String opsDisplayName) {
        this.opsDisplayName = opsDisplayName;
    }

    public java.lang.String getCustomCommandName() {
        return customCommandName;
    }

    public void setCustomCommandName(java.lang.String customCommandName) {
        this.customCommandName = customCommandName;
    }

    public org.apache.ambari.server.actionmanager.HostRoleStatus getStatus() {
        return status;
    }

    public void setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus status) {
        this.status = status;
    }

    public org.apache.ambari.server.actionmanager.ServiceComponentHostEventWrapper getEvent() {
        return event;
    }

    public java.lang.String getStdout() {
        return stdout;
    }

    public void setStdout(java.lang.String stdout) {
        this.stdout = stdout;
    }

    public java.lang.String getStderr() {
        return stderr;
    }

    public void setStderr(java.lang.String stderr) {
        this.stderr = stderr;
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

    public int getExitCode() {
        return exitCode;
    }

    public void setExitCode(int exitCode) {
        this.exitCode = exitCode;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getOriginalStartTime() {
        return originalStartTime;
    }

    public void setOriginalStartTime(long originalStartTime) {
        this.originalStartTime = originalStartTime;
    }

    public long getLastAttemptTime() {
        return lastAttemptTime;
    }

    public void setLastAttemptTime(long lastAttemptTime) {
        this.lastAttemptTime = lastAttemptTime;
    }

    public short getAttemptCount() {
        return attemptCount;
    }

    public void incrementAttemptCount() {
        attemptCount++;
    }

    public boolean isRetryAllowed() {
        return retryAllowed;
    }

    public java.lang.String getStructuredOut() {
        return structuredOut;
    }

    public void setStructuredOut(java.lang.String structuredOut) {
        this.structuredOut = structuredOut;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public org.apache.ambari.server.actionmanager.ExecutionCommandWrapper getExecutionCommandWrapper() {
        if ((taskId != (-1)) && (executionCommandWrapper == null)) {
            org.apache.ambari.server.orm.entities.ExecutionCommandEntity commandEntity = executionCommandDAO.findByPK(taskId);
            if (commandEntity == null) {
                throw new java.lang.RuntimeException("Invalid DB state, broken one-to-one relation for taskId=" + taskId);
            }
            executionCommandWrapper = ecwFactory.createFromJson(new java.lang.String(commandEntity.getCommand()));
        }
        return executionCommandWrapper;
    }

    public void setExecutionCommandWrapper(org.apache.ambari.server.actionmanager.ExecutionCommandWrapper executionCommandWrapper) {
        this.executionCommandWrapper = executionCommandWrapper;
    }

    public org.apache.ambari.server.RoleCommand getRoleCommand() {
        return roleCommand;
    }

    public void setRoleCommand(org.apache.ambari.server.RoleCommand roleCommand) {
        this.roleCommand = roleCommand;
    }

    public long getStageId() {
        return stageId;
    }

    public long getRequestId() {
        return requestId;
    }

    public boolean isBackgroundCommand() {
        return isBackgroundCommand;
    }

    public void setBackgroundCommand(boolean isBackgroundCommand) {
        this.isBackgroundCommand = isBackgroundCommand;
    }

    public boolean isFailureAutoSkipped() {
        return autoSkipFailure;
    }

    @java.lang.Override
    public int hashCode() {
        return java.lang.Long.valueOf(taskId).hashCode();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object other) {
        if (!(other instanceof org.apache.ambari.server.actionmanager.HostRoleCommand)) {
            return false;
        }
        org.apache.ambari.server.actionmanager.HostRoleCommand o = ((org.apache.ambari.server.actionmanager.HostRoleCommand) (other));
        return hashCode() == o.hashCode();
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append("HostRoleCommand State:\n");
        builder.append("  TaskId: ").append(taskId).append("\n");
        builder.append("  Role: ").append(role).append("\n");
        builder.append("  Status: ").append(status).append("\n");
        builder.append("  Event: ").append(event).append("\n");
        builder.append("  RetryAllowed: ").append(retryAllowed).append("\n");
        builder.append("  AutoSkipFailure: ").append(autoSkipFailure).append("\n");
        builder.append("  Output log: ").append(outputLog).append("\n");
        builder.append("  Error log: ").append(errorLog).append("\n");
        builder.append("  stdout: ").append(stdout).append("\n");
        builder.append("  stderr: ").append(stderr).append("\n");
        builder.append("  exitcode: ").append(exitCode).append("\n");
        builder.append("  Start time: ").append(startTime).append("\n");
        builder.append("  Original Start time: ").append(originalStartTime).append("\n");
        builder.append("  Last attempt time: ").append(lastAttemptTime).append("\n");
        builder.append("  attempt count: ").append(attemptCount).append("\n");
        return builder.toString();
    }
}