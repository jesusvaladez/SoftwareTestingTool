package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import static org.apache.commons.lang.StringUtils.defaultString;
@javax.persistence.Entity
@javax.persistence.Table(name = "stage")
@javax.persistence.IdClass(org.apache.ambari.server.orm.entities.StageEntityPK.class)
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "StageEntity.findFirstStageByStatus", query = "SELECT stage.requestId, MIN(stage.stageId) from StageEntity stage, HostRoleCommandEntity hrc WHERE hrc.status IN :statuses AND hrc.stageId = stage.stageId AND hrc.requestId = stage.requestId GROUP by stage.requestId ORDER BY stage.requestId"), @javax.persistence.NamedQuery(name = "StageEntity.findByRequestIdAndCommandStatuses", query = "SELECT stage from StageEntity stage WHERE stage.status IN :statuses AND stage.requestId = :requestId ORDER BY stage.stageId"), @javax.persistence.NamedQuery(name = "StageEntity.removeByRequestStageIds", query = "DELETE FROM StageEntity stage WHERE stage.stageId = :stageId AND stage.requestId = :requestId") })
public class StageEntity {
    @javax.persistence.Basic
    @javax.persistence.Column(name = "cluster_id", updatable = false, nullable = false)
    private java.lang.Long clusterId = java.lang.Long.valueOf(-1L);

    @javax.persistence.Id
    @javax.persistence.Column(name = "request_id", insertable = false, updatable = false, nullable = false)
    private java.lang.Long requestId;

    @javax.persistence.Id
    @javax.persistence.Column(name = "stage_id", insertable = true, updatable = false, nullable = false)
    private java.lang.Long stageId = 0L;

    @javax.persistence.Basic
    @javax.persistence.Column(name = "skippable", nullable = false)
    private java.lang.Integer skippable = java.lang.Integer.valueOf(0);

    @javax.persistence.Basic
    @javax.persistence.Column(name = "supports_auto_skip_failure", nullable = false)
    private java.lang.Integer supportsAutoSkipOnFailure = java.lang.Integer.valueOf(0);

    @javax.persistence.Basic
    @javax.persistence.Column(name = "log_info")
    private java.lang.String logInfo = "";

    @javax.persistence.Basic
    @javax.persistence.Column(name = "request_context")
    private java.lang.String requestContext = "";

    @javax.persistence.Basic
    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    @javax.persistence.Column(name = "command_execution_type", nullable = false)
    private org.apache.ambari.server.actionmanager.CommandExecutionType commandExecutionType = org.apache.ambari.server.actionmanager.CommandExecutionType.STAGE;

    @javax.persistence.Column(name = "command_params")
    @javax.persistence.Basic(fetch = javax.persistence.FetchType.LAZY)
    private byte[] commandParamsStage;

    @javax.persistence.Basic
    @javax.persistence.Column(name = "host_params")
    private byte[] hostParamsStage;

    @javax.persistence.Column(name = "status", nullable = false)
    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    private org.apache.ambari.server.actionmanager.HostRoleStatus status = org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING;

    @javax.persistence.Column(name = "display_status", nullable = false)
    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    private org.apache.ambari.server.actionmanager.HostRoleStatus displayStatus = org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "request_id", referencedColumnName = "request_id", nullable = false)
    private org.apache.ambari.server.orm.entities.RequestEntity request;

    @javax.persistence.OneToMany(mappedBy = "stage", cascade = javax.persistence.CascadeType.REMOVE, fetch = javax.persistence.FetchType.LAZY)
    private java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> hostRoleCommands;

    @javax.persistence.OneToMany(mappedBy = "stage", cascade = javax.persistence.CascadeType.REMOVE)
    private java.util.Collection<org.apache.ambari.server.orm.entities.RoleSuccessCriteriaEntity> roleSuccessCriterias;

    public java.lang.Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(java.lang.Long clusterId) {
        this.clusterId = clusterId;
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

    public java.lang.String getLogInfo() {
        return StringUtils.defaultString(logInfo);
    }

    public void setLogInfo(java.lang.String logInfo) {
        this.logInfo = logInfo;
    }

    public java.lang.String getRequestContext() {
        return StringUtils.defaultString(requestContext);
    }

    public java.lang.String getCommandParamsStage() {
        return commandParamsStage == null ? new java.lang.String() : new java.lang.String(commandParamsStage);
    }

    public void setCommandParamsStage(java.lang.String commandParamsStage) {
        this.commandParamsStage = commandParamsStage.getBytes();
    }

    public java.lang.String getHostParamsStage() {
        return hostParamsStage == null ? new java.lang.String() : new java.lang.String(hostParamsStage);
    }

    public void setHostParamsStage(java.lang.String hostParamsStage) {
        this.hostParamsStage = hostParamsStage.getBytes();
    }

    public void setRequestContext(java.lang.String requestContext) {
        if (requestContext != null) {
            this.requestContext = requestContext;
        }
    }

    public org.apache.ambari.server.actionmanager.CommandExecutionType getCommandExecutionType() {
        return commandExecutionType;
    }

    public void setCommandExecutionType(org.apache.ambari.server.actionmanager.CommandExecutionType commandExecutionType) {
        this.commandExecutionType = commandExecutionType;
    }

    public org.apache.ambari.server.actionmanager.HostRoleStatus getStatus() {
        return status;
    }

    public void setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus status) {
        this.status = status;
    }

    public org.apache.ambari.server.actionmanager.HostRoleStatus getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(org.apache.ambari.server.actionmanager.HostRoleStatus displayStatus) {
        this.displayStatus = displayStatus;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        org.apache.ambari.server.orm.entities.StageEntity that = ((org.apache.ambari.server.orm.entities.StageEntity) (o));
        if (clusterId != null ? !clusterId.equals(that.clusterId) : that.clusterId != null) {
            return false;
        }
        if (requestId != null ? !requestId.equals(that.requestId) : that.requestId != null) {
            return false;
        }
        if (stageId != null ? !stageId.equals(that.stageId) : that.stageId != null) {
            return false;
        }
        return !(requestContext != null ? !requestContext.equals(that.requestContext) : that.requestContext != null);
    }

    @java.lang.Override
    public int hashCode() {
        int result = (clusterId != null) ? clusterId.hashCode() : 0;
        result = (31 * result) + (requestId != null ? requestId.hashCode() : 0);
        result = (31 * result) + (stageId != null ? stageId.hashCode() : 0);
        result = (31 * result) + (requestContext != null ? requestContext.hashCode() : 0);
        return result;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> getHostRoleCommands() {
        return hostRoleCommands;
    }

    public void setHostRoleCommands(java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> hostRoleCommands) {
        this.hostRoleCommands = hostRoleCommands;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.RoleSuccessCriteriaEntity> getRoleSuccessCriterias() {
        return roleSuccessCriterias;
    }

    public void setRoleSuccessCriterias(java.util.Collection<org.apache.ambari.server.orm.entities.RoleSuccessCriteriaEntity> roleSuccessCriterias) {
        this.roleSuccessCriterias = roleSuccessCriterias;
    }

    public org.apache.ambari.server.orm.entities.RequestEntity getRequest() {
        return request;
    }

    public void setRequest(org.apache.ambari.server.orm.entities.RequestEntity request) {
        this.request = request;
    }

    public boolean isSkippable() {
        return skippable != 0;
    }

    public void setSkippable(boolean skippable) {
        this.skippable = (skippable) ? 1 : 0;
    }

    public boolean isAutoSkipOnFailureSupported() {
        return supportsAutoSkipOnFailure != 0;
    }

    public void setAutoSkipFailureSupported(boolean supportsAutoSkipOnFailure) {
        this.supportsAutoSkipOnFailure = (supportsAutoSkipOnFailure) ? 1 : 0;
    }
}