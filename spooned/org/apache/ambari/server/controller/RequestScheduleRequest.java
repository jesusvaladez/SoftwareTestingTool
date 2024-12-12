package org.apache.ambari.server.controller;
public class RequestScheduleRequest {
    private java.lang.Long id;

    private java.lang.String clusterName;

    private java.lang.String description;

    private java.lang.String status;

    private org.apache.ambari.server.state.scheduler.Batch batch;

    private org.apache.ambari.server.state.scheduler.Schedule schedule;

    public RequestScheduleRequest(java.lang.Long id, java.lang.String clusterName, java.lang.String description, java.lang.String status, org.apache.ambari.server.state.scheduler.Batch batch, org.apache.ambari.server.state.scheduler.Schedule schedule) {
        this.id = id;
        this.clusterName = clusterName;
        this.description = description;
        this.status = status;
        this.batch = batch;
        this.schedule = schedule;
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
}