package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import org.apache.commons.lang.ArrayUtils;
import static org.apache.commons.lang.StringUtils.defaultString;
@javax.persistence.Entity
@javax.persistence.Table(name = "host_role_command", indexes = { @javax.persistence.Index(name = "idx_hrc_request_id", columnList = "request_id"), @javax.persistence.Index(name = "idx_hrc_status_role", columnList = "status, role") })
@javax.persistence.TableGenerator(name = "host_role_command_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "host_role_command_id_seq", initialValue = 1)
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "HostRoleCommandEntity.findTaskIdsByRequestStageIds", query = "SELECT command.taskId FROM HostRoleCommandEntity command WHERE command.stageId = :stageId AND command.requestId = :requestId"), @javax.persistence.NamedQuery(name = "HostRoleCommandEntity.findCountByCommandStatuses", query = "SELECT COUNT(command.taskId) FROM HostRoleCommandEntity command WHERE command.status IN :statuses"), @javax.persistence.NamedQuery(name = "HostRoleCommandEntity.findByRequestIdAndStatuses", query = "SELECT task FROM HostRoleCommandEntity task WHERE task.requestId=:requestId AND task.status IN :statuses ORDER BY task.taskId ASC"), @javax.persistence.NamedQuery(name = "HostRoleCommandEntity.findTasksByStatusesOrderByIdDesc", query = "SELECT task FROM HostRoleCommandEntity task WHERE task.requestId = :requestId AND task.status IN :statuses ORDER BY task.taskId DESC"), @javax.persistence.NamedQuery(name = "HostRoleCommandEntity.findNumTasksAlreadyRanInStage", query = "SELECT COUNT(task.taskId) FROM HostRoleCommandEntity task WHERE task.requestId = :requestId AND task.taskId > :taskId AND task.stageId > :stageId AND task.status NOT IN :statuses"), @javax.persistence.NamedQuery(name = "HostRoleCommandEntity.findByCommandStatuses", query = "SELECT command FROM HostRoleCommandEntity command WHERE command.status IN :statuses ORDER BY command.requestId, command.stageId"), @javax.persistence.NamedQuery(name = "HostRoleCommandEntity.findByHostId", query = "SELECT command FROM HostRoleCommandEntity command WHERE command.hostId=:hostId"), @javax.persistence.NamedQuery(name = "HostRoleCommandEntity.findByHostRole", query = "SELECT command FROM HostRoleCommandEntity command WHERE command.hostEntity.hostName=:hostName AND command.requestId=:requestId AND command.stageId=:stageId AND command.role=:role ORDER BY command.taskId"), @javax.persistence.NamedQuery(name = "HostRoleCommandEntity.findByHostRoleNullHost", query = "SELECT command FROM HostRoleCommandEntity command WHERE command.hostEntity IS NULL AND command.requestId=:requestId AND command.stageId=:stageId AND command.role=:role"), @javax.persistence.NamedQuery(name = "HostRoleCommandEntity.findByStatusBetweenStages", query = "SELECT command FROM HostRoleCommandEntity command WHERE command.requestId = :requestId AND command.stageId >= :minStageId AND command.stageId <= :maxStageId AND command.status = :status"), @javax.persistence.NamedQuery(name = "HostRoleCommandEntity.updateAutoSkipExcludeRoleCommand", query = "UPDATE HostRoleCommandEntity command SET command.autoSkipOnFailure = :autoSkipOnFailure WHERE command.requestId = :requestId AND command.roleCommand <> :roleCommand"), @javax.persistence.NamedQuery(name = "HostRoleCommandEntity.updateAutoSkipForRoleCommand", query = "UPDATE HostRoleCommandEntity command SET command.autoSkipOnFailure = :autoSkipOnFailure WHERE command.requestId = :requestId AND command.roleCommand = :roleCommand"), @javax.persistence.NamedQuery(name = "HostRoleCommandEntity.removeByTaskIds", query = "DELETE FROM HostRoleCommandEntity command WHERE command.taskId IN :taskIds"), @javax.persistence.NamedQuery(name = "HostRoleCommandEntity.findHostsByCommandStatus", query = "SELECT DISTINCT(host.hostName) FROM HostRoleCommandEntity command, HostEntity host WHERE (command.requestId >= :iLowestRequestIdInProgress AND command.requestId <= :iHighestRequestIdInProgress) AND command.status IN :statuses AND command.hostId = host.hostId AND host.hostName IS NOT NULL"), @javax.persistence.NamedQuery(name = "HostRoleCommandEntity.getBlockingHostsForRequest", query = "SELECT DISTINCT(host.hostName) FROM HostRoleCommandEntity command, HostEntity host WHERE command.requestId >= :lowerRequestIdInclusive AND command.requestId < :upperRequestIdExclusive AND command.status IN :statuses AND command.isBackgroundCommand=0 AND command.hostId = host.hostId AND host.hostName IS NOT NULL"), @javax.persistence.NamedQuery(name = "HostRoleCommandEntity.findLatestServiceChecksByRole", query = "SELECT NEW org.apache.ambari.server.orm.dao.HostRoleCommandDAO.LastServiceCheckDTO(command.role, MAX(command.endTime)) FROM HostRoleCommandEntity command WHERE command.roleCommand = :roleCommand AND command.endTime > 0 AND command.stage.clusterId = :clusterId GROUP BY command.role ORDER BY command.role ASC"), @javax.persistence.NamedQuery(name = "HostRoleCommandEntity.findByRequestId", query = "SELECT command FROM HostRoleCommandEntity command WHERE command.requestId = :requestId ORDER BY command.taskId") })
public class HostRoleCommandEntity {
    private static int MAX_COMMAND_DETAIL_LENGTH = 250;

    @javax.persistence.Column(name = "task_id")
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "host_role_command_id_generator")
    private java.lang.Long taskId;

    @javax.persistence.Column(name = "request_id", insertable = false, updatable = false, nullable = false)
    @javax.persistence.Basic
    private java.lang.Long requestId;

    @javax.persistence.Column(name = "stage_id", insertable = false, updatable = false, nullable = false)
    @javax.persistence.Basic
    private java.lang.Long stageId;

    @javax.persistence.Column(name = "host_id", insertable = false, updatable = false, nullable = true)
    @javax.persistence.Basic
    private java.lang.Long hostId;

    @javax.persistence.Column(name = "role")
    private java.lang.String role;

    @javax.persistence.Column(name = "event", length = 32000)
    @javax.persistence.Basic
    @javax.persistence.Lob
    private java.lang.String event = "";

    @javax.persistence.Column(name = "exitcode", nullable = false)
    @javax.persistence.Basic
    private java.lang.Integer exitcode = 0;

    @javax.persistence.Column(name = "status", nullable = false)
    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    private org.apache.ambari.server.actionmanager.HostRoleStatus status = org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING;

    @javax.persistence.Column(name = "std_error")
    @javax.persistence.Lob
    @javax.persistence.Basic
    private byte[] stdError = new byte[0];

    @javax.persistence.Column(name = "std_out")
    @javax.persistence.Lob
    @javax.persistence.Basic
    private byte[] stdOut = new byte[0];

    @javax.persistence.Column(name = "output_log")
    @javax.persistence.Basic
    private java.lang.String outputLog = null;

    @javax.persistence.Column(name = "error_log")
    @javax.persistence.Basic
    private java.lang.String errorLog = null;

    @javax.persistence.Column(name = "structured_out")
    @javax.persistence.Lob
    @javax.persistence.Basic
    private byte[] structuredOut = new byte[0];

    @javax.persistence.Basic
    @javax.persistence.Column(name = "start_time", nullable = false)
    private java.lang.Long startTime = -1L;

    @javax.persistence.Basic
    @javax.persistence.Column(name = "original_start_time", nullable = false)
    private java.lang.Long originalStartTime = -1L;

    @javax.persistence.Basic
    @javax.persistence.Column(name = "end_time", nullable = false)
    private java.lang.Long endTime = -1L;

    @javax.persistence.Basic
    @javax.persistence.Column(name = "last_attempt_time", nullable = false)
    private java.lang.Long lastAttemptTime = -1L;

    @javax.persistence.Basic
    @javax.persistence.Column(name = "attempt_count", nullable = false)
    private java.lang.Short attemptCount = 0;

    @javax.persistence.Column(name = "retry_allowed", nullable = false)
    private java.lang.Integer retryAllowed = java.lang.Integer.valueOf(0);

    @javax.persistence.Column(name = "auto_skip_on_failure", nullable = false)
    private java.lang.Integer autoSkipOnFailure = java.lang.Integer.valueOf(0);

    @javax.persistence.Column(name = "role_command")
    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    private org.apache.ambari.server.RoleCommand roleCommand;

    @javax.persistence.Column(name = "command_detail")
    @javax.persistence.Basic
    private java.lang.String commandDetail;

    @javax.persistence.Column(name = "ops_display_name")
    @javax.persistence.Basic
    private java.lang.String opsDisplayName;

    @javax.persistence.Column(name = "custom_command_name")
    @javax.persistence.Basic
    private java.lang.String customCommandName;

    @javax.persistence.OneToOne(mappedBy = "hostRoleCommand", cascade = javax.persistence.CascadeType.REMOVE, fetch = javax.persistence.FetchType.LAZY)
    private org.apache.ambari.server.orm.entities.ExecutionCommandEntity executionCommand;

    @javax.persistence.ManyToOne(cascade = { javax.persistence.CascadeType.MERGE })
    @javax.persistence.JoinColumns({ @javax.persistence.JoinColumn(name = "request_id", referencedColumnName = "request_id", nullable = false), @javax.persistence.JoinColumn(name = "stage_id", referencedColumnName = "stage_id", nullable = false) })
    private org.apache.ambari.server.orm.entities.StageEntity stage;

    @javax.persistence.ManyToOne(cascade = { javax.persistence.CascadeType.MERGE, javax.persistence.CascadeType.REFRESH })
    @javax.persistence.JoinColumn(name = "host_id", referencedColumnName = "host_id", nullable = true)
    private org.apache.ambari.server.orm.entities.HostEntity hostEntity;

    @javax.persistence.OneToOne(mappedBy = "hostRoleCommandEntity", cascade = javax.persistence.CascadeType.REMOVE)
    private org.apache.ambari.server.orm.entities.TopologyLogicalTaskEntity topologyLogicalTaskEntity;

    @javax.persistence.Basic
    @javax.persistence.Column(name = "is_background", nullable = false)
    private short isBackgroundCommand = 0;

    public java.lang.Long getTaskId() {
        return taskId;
    }

    public void setTaskId(java.lang.Long taskId) {
        this.taskId = taskId;
    }

    public java.lang.Long getRequestId() {
        return requestId;
    }

    public void setRequestId(java.lang.Long requestId) {
        this.requestId = requestId;
    }

    public java.lang.Long getStageId() {
        return stageId;
    }

    public void setStageId(java.lang.Long stageId) {
        this.stageId = stageId;
    }

    public java.lang.String getHostName() {
        return hostEntity != null ? hostEntity.getHostName() : null;
    }

    public java.lang.Long getHostId() {
        return hostEntity != null ? hostEntity.getHostId() : null;
    }

    public org.apache.ambari.server.Role getRole() {
        return org.apache.ambari.server.Role.valueOf(role);
    }

    public void setRole(org.apache.ambari.server.Role role) {
        this.role = role.name();
    }

    public java.lang.String getEvent() {
        return StringUtils.defaultString(event);
    }

    public void setEvent(java.lang.String event) {
        this.event = event;
    }

    public java.lang.Integer getExitcode() {
        return exitcode;
    }

    public void setExitcode(java.lang.Integer exitcode) {
        this.exitcode = exitcode;
    }

    public org.apache.ambari.server.actionmanager.HostRoleStatus getStatus() {
        return status;
    }

    public void setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus status) {
        this.status = status;
    }

    public byte[] getStdError() {
        return org.apache.commons.lang.ArrayUtils.nullToEmpty(stdError);
    }

    public void setStdError(byte[] stdError) {
        this.stdError = stdError;
    }

    public byte[] getStdOut() {
        return org.apache.commons.lang.ArrayUtils.nullToEmpty(stdOut);
    }

    public void setStdOut(byte[] stdOut) {
        this.stdOut = stdOut;
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

    public java.lang.Long getStartTime() {
        return startTime;
    }

    public void setStartTime(java.lang.Long startTime) {
        this.startTime = startTime;
    }

    public java.lang.Long getOriginalStartTime() {
        return originalStartTime;
    }

    public void setOriginalStartTime(java.lang.Long originalStartTime) {
        this.originalStartTime = originalStartTime;
    }

    public java.lang.Long getLastAttemptTime() {
        return lastAttemptTime;
    }

    public void setLastAttemptTime(java.lang.Long lastAttemptTime) {
        this.lastAttemptTime = lastAttemptTime;
    }

    public java.lang.Short getAttemptCount() {
        return attemptCount;
    }

    public void setAttemptCount(java.lang.Short attemptCount) {
        this.attemptCount = attemptCount;
    }

    public org.apache.ambari.server.RoleCommand getRoleCommand() {
        return roleCommand;
    }

    public void setRoleCommand(org.apache.ambari.server.RoleCommand roleCommand) {
        this.roleCommand = roleCommand;
    }

    public byte[] getStructuredOut() {
        return structuredOut;
    }

    public void setStructuredOut(byte[] structuredOut) {
        this.structuredOut = structuredOut;
    }

    public java.lang.Long getEndTime() {
        return endTime;
    }

    public void setEndTime(java.lang.Long endTime) {
        this.endTime = endTime;
    }

    public java.lang.String getCommandDetail() {
        return commandDetail;
    }

    public void setCommandDetail(java.lang.String commandDetail) {
        java.lang.String truncatedCommandDetail = commandDetail;
        if (commandDetail != null) {
            if (commandDetail.length() > org.apache.ambari.server.orm.entities.HostRoleCommandEntity.MAX_COMMAND_DETAIL_LENGTH) {
                truncatedCommandDetail = commandDetail.substring(0, org.apache.ambari.server.orm.entities.HostRoleCommandEntity.MAX_COMMAND_DETAIL_LENGTH) + "...";
            }
        }
        this.commandDetail = truncatedCommandDetail;
    }

    public java.lang.String getCustomCommandName() {
        return customCommandName;
    }

    public void setCustomCommandName(java.lang.String customCommandName) {
        this.customCommandName = customCommandName;
    }

    public java.lang.String getOpsDisplayName() {
        return opsDisplayName;
    }

    public void setOpsDisplayName(java.lang.String opsDisplayName) {
        this.opsDisplayName = opsDisplayName;
    }

    public boolean isRetryAllowed() {
        return retryAllowed != 0;
    }

    public void setRetryAllowed(boolean enabled) {
        retryAllowed = (enabled) ? 1 : 0;
    }

    public boolean isFailureAutoSkipped() {
        return autoSkipOnFailure != 0;
    }

    public void setAutoSkipOnFailure(boolean skipFailures) {
        autoSkipOnFailure = (skipFailures) ? 1 : 0;
    }

    public void setBackgroundCommand(boolean runInBackground) {
        isBackgroundCommand = ((short) ((runInBackground) ? 1 : 0));
    }

    public boolean isBackgroundCommand() {
        return isBackgroundCommand == 0 ? false : true;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        org.apache.ambari.server.orm.entities.HostRoleCommandEntity that = ((org.apache.ambari.server.orm.entities.HostRoleCommandEntity) (o));
        if (attemptCount != null ? !attemptCount.equals(that.attemptCount) : that.attemptCount != null) {
            return false;
        }
        if (event != null ? !event.equals(that.event) : that.event != null) {
            return false;
        }
        if (exitcode != null ? !exitcode.equals(that.exitcode) : that.exitcode != null) {
            return false;
        }
        if (hostEntity != null ? !hostEntity.equals(that.hostEntity) : that.hostEntity != null) {
            return false;
        }
        if (lastAttemptTime != null ? !lastAttemptTime.equals(that.lastAttemptTime) : that.lastAttemptTime != null) {
            return false;
        }
        if (requestId != null ? !requestId.equals(that.requestId) : that.requestId != null) {
            return false;
        }
        if (role != null ? !role.equals(that.role) : that.role != null) {
            return false;
        }
        if (stageId != null ? !stageId.equals(that.stageId) : that.stageId != null) {
            return false;
        }
        if (startTime != null ? !startTime.equals(that.startTime) : that.startTime != null) {
            return false;
        }
        if (originalStartTime != null ? !originalStartTime.equals(that.originalStartTime) : that.originalStartTime != null) {
            return false;
        }
        if (status != null ? !status.equals(that.status) : that.status != null) {
            return false;
        }
        if (stdError != null ? !java.util.Arrays.equals(stdError, that.stdError) : that.stdError != null) {
            return false;
        }
        if (stdOut != null ? !java.util.Arrays.equals(stdOut, that.stdOut) : that.stdOut != null) {
            return false;
        }
        if (outputLog != null ? !outputLog.equals(that.outputLog) : that.outputLog != null) {
            return false;
        }
        if (errorLog != null ? !errorLog.equals(that.errorLog) : that.errorLog != null) {
            return false;
        }
        if (taskId != null ? !taskId.equals(that.taskId) : that.taskId != null) {
            return false;
        }
        if (structuredOut != null ? !java.util.Arrays.equals(structuredOut, that.structuredOut) : that.structuredOut != null) {
            return false;
        }
        if (endTime != null ? !endTime.equals(that.endTime) : that.endTime != null) {
            return false;
        }
        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (taskId != null) ? taskId.hashCode() : 0;
        result = (31 * result) + (requestId != null ? requestId.hashCode() : 0);
        result = (31 * result) + (stageId != null ? stageId.hashCode() : 0);
        result = (31 * result) + (hostEntity != null ? hostEntity.hashCode() : 0);
        result = (31 * result) + (role != null ? role.hashCode() : 0);
        result = (31 * result) + (event != null ? event.hashCode() : 0);
        result = (31 * result) + (exitcode != null ? exitcode.hashCode() : 0);
        result = (31 * result) + (status != null ? status.hashCode() : 0);
        result = (31 * result) + (stdError != null ? java.util.Arrays.hashCode(stdError) : 0);
        result = (31 * result) + (stdOut != null ? java.util.Arrays.hashCode(stdOut) : 0);
        result = (31 * result) + (outputLog != null ? outputLog.hashCode() : 0);
        result = (31 * result) + (errorLog != null ? errorLog.hashCode() : 0);
        result = (31 * result) + (startTime != null ? startTime.hashCode() : 0);
        result = (31 * result) + (originalStartTime != null ? originalStartTime.hashCode() : 0);
        result = (31 * result) + (lastAttemptTime != null ? lastAttemptTime.hashCode() : 0);
        result = (31 * result) + (attemptCount != null ? attemptCount.hashCode() : 0);
        result = (31 * result) + (endTime != null ? endTime.hashCode() : 0);
        result = (31 * result) + (structuredOut != null ? java.util.Arrays.hashCode(structuredOut) : 0);
        return result;
    }

    public org.apache.ambari.server.orm.entities.ExecutionCommandEntity getExecutionCommand() {
        return executionCommand;
    }

    public void setExecutionCommand(org.apache.ambari.server.orm.entities.ExecutionCommandEntity executionCommandsByTaskId) {
        executionCommand = executionCommandsByTaskId;
    }

    public org.apache.ambari.server.orm.entities.StageEntity getStage() {
        return stage;
    }

    public void setStage(org.apache.ambari.server.orm.entities.StageEntity stage) {
        this.stage = stage;
        if (null != stage) {
            if (null == stageId) {
                stageId = stage.getStageId();
            }
            if (null == requestId) {
                requestId = stage.getRequestId();
            }
        }
    }

    public org.apache.ambari.server.orm.entities.HostEntity getHostEntity() {
        return hostEntity;
    }

    public void setHostEntity(org.apache.ambari.server.orm.entities.HostEntity hostEntity) {
        this.hostEntity = hostEntity;
    }

    public org.apache.ambari.server.orm.entities.TopologyLogicalTaskEntity getTopologyLogicalTaskEntity() {
        return topologyLogicalTaskEntity;
    }

    public void setTopologyLogicalTaskEntity(org.apache.ambari.server.orm.entities.TopologyLogicalTaskEntity topologyLogicalTaskEntity) {
        this.topologyLogicalTaskEntity = topologyLogicalTaskEntity;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder buffer = new java.lang.StringBuilder("HostRoleCommandEntity{ ");
        buffer.append("taskId").append(taskId);
        buffer.append(", stageId=").append(stageId);
        buffer.append(", requestId=").append(requestId);
        buffer.append(", role=").append(role);
        buffer.append(", roleCommand=").append(roleCommand);
        buffer.append(", exitcode=").append(exitcode);
        buffer.append("}");
        return buffer.toString();
    }
}