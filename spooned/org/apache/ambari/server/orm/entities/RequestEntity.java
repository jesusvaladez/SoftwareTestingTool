package org.apache.ambari.server.orm.entities;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
@javax.persistence.Table(name = "request")
@javax.persistence.Entity
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "RequestEntity.findRequestStageIdsInClusterBeforeDate", query = "SELECT NEW org.apache.ambari.server.orm.dao.RequestDAO.StageEntityPK(request.requestId, stage.stageId) FROM RequestEntity request JOIN StageEntity stage ON request.requestId = stage.requestId WHERE request.clusterId = :clusterId AND request.createTime <= :beforeDate"), @javax.persistence.NamedQuery(name = "RequestEntity.removeByRequestIds", query = "DELETE FROM RequestEntity request WHERE request.requestId IN :requestIds") })
public class RequestEntity {
    @javax.persistence.Column(name = "request_id")
    @javax.persistence.Id
    private java.lang.Long requestId;

    @javax.persistence.Column(name = "cluster_id", updatable = false, nullable = false)
    @javax.persistence.Basic
    private java.lang.Long clusterId;

    @javax.persistence.Column(name = "request_schedule_id", updatable = false, insertable = false, nullable = true)
    @javax.persistence.Basic
    private java.lang.Long requestScheduleId;

    @javax.persistence.Column(name = "request_context")
    @javax.persistence.Basic
    private java.lang.String requestContext;

    @javax.persistence.Column(name = "command_name")
    @javax.persistence.Basic
    private java.lang.String commandName;

    @javax.persistence.Column(name = "cluster_host_info")
    @javax.persistence.Basic(fetch = javax.persistence.FetchType.LAZY)
    private byte[] clusterHostInfo;

    @javax.persistence.Column(name = "inputs")
    @javax.persistence.Lob
    private byte[] inputs = new byte[0];

    @javax.persistence.Column(name = "request_type")
    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    private org.apache.ambari.server.actionmanager.RequestType requestType;

    @javax.persistence.Column(name = "status", nullable = false)
    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    private org.apache.ambari.server.actionmanager.HostRoleStatus status = org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING;

    @javax.persistence.Column(name = "display_status", nullable = false)
    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    private org.apache.ambari.server.actionmanager.HostRoleStatus displayStatus = org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING;

    @javax.persistence.Basic
    @javax.persistence.Column(name = "create_time", nullable = false)
    private java.lang.Long createTime = java.lang.System.currentTimeMillis();

    @javax.persistence.Basic
    @javax.persistence.Column(name = "start_time", nullable = false)
    private java.lang.Long startTime = -1L;

    @javax.persistence.Basic
    @javax.persistence.Column(name = "end_time", nullable = false)
    private java.lang.Long endTime = -1L;

    @javax.persistence.Basic
    @javax.persistence.Column(name = "exclusive_execution", insertable = true, updatable = true, nullable = false)
    private java.lang.Integer exclusive = 0;

    @javax.persistence.Column(name = "user_name")
    private java.lang.String userName;

    @javax.persistence.OneToMany(mappedBy = "request", cascade = javax.persistence.CascadeType.REMOVE)
    private java.util.Collection<org.apache.ambari.server.orm.entities.StageEntity> stages;

    @javax.persistence.OneToMany(mappedBy = "requestEntity", cascade = javax.persistence.CascadeType.ALL)
    private java.util.Collection<org.apache.ambari.server.orm.entities.RequestResourceFilterEntity> resourceFilterEntities;

    @javax.persistence.OneToOne(mappedBy = "requestEntity", cascade = { javax.persistence.CascadeType.ALL })
    private org.apache.ambari.server.orm.entities.RequestOperationLevelEntity requestOperationLevel;

    @javax.persistence.ManyToOne(cascade = { javax.persistence.CascadeType.MERGE })
    @javax.persistence.JoinColumn(name = "request_schedule_id", referencedColumnName = "schedule_id")
    private org.apache.ambari.server.orm.entities.RequestScheduleEntity requestScheduleEntity;

    public java.lang.Long getRequestId() {
        return requestId;
    }

    public void setRequestId(java.lang.Long id) {
        this.requestId = id;
    }

    public java.lang.String getRequestContext() {
        return requestContext;
    }

    public void setRequestContext(java.lang.String request_context) {
        this.requestContext = request_context;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.StageEntity> getStages() {
        return stages;
    }

    public void setStages(java.util.Collection<org.apache.ambari.server.orm.entities.StageEntity> stages) {
        this.stages = stages;
    }

    public java.lang.String getClusterHostInfo() {
        return clusterHostInfo == null ? "{}" : new java.lang.String(clusterHostInfo);
    }

    public void setClusterHostInfo(java.lang.String clusterHostInfo) {
        this.clusterHostInfo = clusterHostInfo.getBytes();
    }

    public java.lang.Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(java.lang.Long createTime) {
        this.createTime = createTime;
    }

    public java.lang.Long getStartTime() {
        return startTime;
    }

    public void setStartTime(java.lang.Long startTime) {
        this.startTime = startTime;
    }

    public java.lang.Long getEndTime() {
        return endTime;
    }

    public void setEndTime(java.lang.Long endTime) {
        this.endTime = endTime;
    }

    public java.lang.Boolean isExclusive() {
        return exclusive == 0 ? false : true;
    }

    public void setExclusive(java.lang.Boolean exclusive) {
        this.exclusive = (exclusive == false) ? 0 : 1;
    }

    public java.lang.String getInputs() {
        return inputs != null ? new java.lang.String(inputs) : null;
    }

    public void setInputs(java.lang.String inputs) {
        this.inputs = (inputs != null) ? inputs.getBytes() : null;
    }

    public org.apache.ambari.server.actionmanager.RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(org.apache.ambari.server.actionmanager.RequestType requestType) {
        this.requestType = requestType;
    }

    public java.lang.Long getClusterId() {
        return clusterId;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.RequestResourceFilterEntity> getResourceFilterEntities() {
        return resourceFilterEntities;
    }

    public void setResourceFilterEntities(java.util.Collection<org.apache.ambari.server.orm.entities.RequestResourceFilterEntity> resourceFilterEntities) {
        this.resourceFilterEntities = resourceFilterEntities;
    }

    public org.apache.ambari.server.orm.entities.RequestOperationLevelEntity getRequestOperationLevel() {
        return requestOperationLevel;
    }

    public void setRequestOperationLevel(org.apache.ambari.server.orm.entities.RequestOperationLevelEntity operationLevel) {
        this.requestOperationLevel = operationLevel;
    }

    public void setClusterId(java.lang.Long clusterId) {
        this.clusterId = clusterId;
    }

    public java.lang.String getCommandName() {
        return commandName;
    }

    public void setCommandName(java.lang.String commandName) {
        this.commandName = commandName;
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

    public org.apache.ambari.server.orm.entities.RequestScheduleEntity getRequestScheduleEntity() {
        return requestScheduleEntity;
    }

    public void setRequestScheduleEntity(org.apache.ambari.server.orm.entities.RequestScheduleEntity requestScheduleEntity) {
        this.requestScheduleEntity = requestScheduleEntity;
    }

    public java.lang.Long getRequestScheduleId() {
        return requestScheduleId;
    }

    public void setRequestScheduleId(java.lang.Long scheduleId) {
        this.requestScheduleId = scheduleId;
    }

    public java.lang.String getUserName() {
        return userName;
    }

    public void setUserName(java.lang.String userName) {
        this.userName = userName;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.RequestEntity that = ((org.apache.ambari.server.orm.entities.RequestEntity) (o));
        if (requestId != null ? !requestId.equals(that.requestId) : that.requestId != null)
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        return requestId != null ? requestId.hashCode() : 0;
    }
}