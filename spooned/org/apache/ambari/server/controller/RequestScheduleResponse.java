package org.apache.ambari.server.controller;
public class RequestScheduleResponse {
    private java.lang.Long id;

    private java.lang.String clusterName;

    private java.lang.String description;

    private java.lang.String status;

    private java.lang.String lastExecutionStatus;

    private org.apache.ambari.server.state.scheduler.Batch batch;

    private org.apache.ambari.server.state.scheduler.Schedule schedule;

    private java.lang.String createUser;

    private java.lang.String createTime;

    private java.lang.String updateUser;

    private java.lang.String updateTime;

    private java.lang.Integer authenticatedUserId;

    public RequestScheduleResponse(java.lang.Long id, java.lang.String clusterName, java.lang.String description, java.lang.String status, java.lang.String lastExecutionStatus, org.apache.ambari.server.state.scheduler.Batch batch, org.apache.ambari.server.state.scheduler.Schedule schedule, java.lang.String createUser, java.lang.String createTime, java.lang.String updateUser, java.lang.String updateTime, java.lang.Integer authenticatedUserId) {
        this.id = id;
        this.clusterName = clusterName;
        this.description = description;
        this.status = status;
        this.lastExecutionStatus = lastExecutionStatus;
        this.batch = batch;
        this.schedule = schedule;
        this.createUser = createUser;
        this.createTime = createTime;
        this.updateUser = updateUser;
        this.updateTime = updateTime;
        this.authenticatedUserId = authenticatedUserId;
    }

    public java.lang.Long getId() {
        return id;
    }

    public void setId(java.lang.Long id) {
        this.id = id;
    }

    public java.lang.String getClusterName() {
        return clusterName;
    }

    public void setClusterName(java.lang.String clusterName) {
        this.clusterName = clusterName;
    }

    public java.lang.String getDescription() {
        return description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public java.lang.String getStatus() {
        return status;
    }

    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    public org.apache.ambari.server.state.scheduler.Batch getBatch() {
        return batch;
    }

    public void setBatch(org.apache.ambari.server.state.scheduler.Batch batch) {
        this.batch = batch;
    }

    public org.apache.ambari.server.state.scheduler.Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(org.apache.ambari.server.state.scheduler.Schedule schedule) {
        this.schedule = schedule;
    }

    public java.lang.String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(java.lang.String createUser) {
        this.createUser = createUser;
    }

    public java.lang.String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(java.lang.String createTime) {
        this.createTime = createTime;
    }

    public java.lang.String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(java.lang.String updateUser) {
        this.updateUser = updateUser;
    }

    public java.lang.String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(java.lang.String updateTime) {
        this.updateTime = updateTime;
    }

    public java.lang.String getLastExecutionStatus() {
        return lastExecutionStatus;
    }

    public void setLastExecutionStatus(java.lang.String lastExecutionStatus) {
        this.lastExecutionStatus = lastExecutionStatus;
    }

    public java.lang.Integer getAuthenticatedUserId() {
        return authenticatedUserId;
    }

    public void setAuthenticatedUserId(java.lang.Integer authenticatedUserId) {
        this.authenticatedUserId = authenticatedUserId;
    }
}