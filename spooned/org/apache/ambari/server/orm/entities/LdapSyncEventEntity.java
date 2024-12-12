package org.apache.ambari.server.orm.entities;
public class LdapSyncEventEntity {
    private final long id;

    private org.apache.ambari.server.orm.entities.LdapSyncEventEntity.Status status;

    private java.lang.String statusDetail;

    private long startTime;

    private long endTime;

    private java.lang.Integer usersCreated;

    private java.lang.Integer usersUpdated;

    private java.lang.Integer usersRemoved;

    private java.lang.Integer usersSkipped;

    private java.lang.Integer groupsCreated;

    private java.lang.Integer groupsUpdated;

    private java.lang.Integer groupsRemoved;

    private java.lang.Integer membershipsCreated;

    private java.lang.Integer membershipsRemoved;

    private java.util.List<org.apache.ambari.server.orm.entities.LdapSyncSpecEntity> specs;

    public LdapSyncEventEntity(long id) {
        this.id = id;
        this.status = org.apache.ambari.server.orm.entities.LdapSyncEventEntity.Status.PENDING;
    }

    public long getId() {
        return id;
    }

    public org.apache.ambari.server.orm.entities.LdapSyncEventEntity.Status getStatus() {
        return status;
    }

    public void setStatus(org.apache.ambari.server.orm.entities.LdapSyncEventEntity.Status status) {
        this.status = status;
    }

    public java.lang.String getStatusDetail() {
        return statusDetail;
    }

    public void setStatusDetail(java.lang.String statusDetail) {
        this.statusDetail = statusDetail;
    }

    public java.util.List<org.apache.ambari.server.orm.entities.LdapSyncSpecEntity> getSpecs() {
        return specs;
    }

    public void setSpecs(java.util.List<org.apache.ambari.server.orm.entities.LdapSyncSpecEntity> specs) {
        this.specs = specs;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public java.lang.Integer getUsersCreated() {
        return usersCreated;
    }

    public void setUsersCreated(java.lang.Integer usersCreated) {
        this.usersCreated = usersCreated;
    }

    public java.lang.Integer getUsersUpdated() {
        return usersUpdated;
    }

    public void setUsersUpdated(java.lang.Integer usersUpdated) {
        this.usersUpdated = usersUpdated;
    }

    public java.lang.Integer getUsersRemoved() {
        return usersRemoved;
    }

    public void setUsersRemoved(java.lang.Integer usersRemoved) {
        this.usersRemoved = usersRemoved;
    }

    public java.lang.Integer getGroupsCreated() {
        return groupsCreated;
    }

    public void setGroupsCreated(java.lang.Integer groupsCreated) {
        this.groupsCreated = groupsCreated;
    }

    public java.lang.Integer getGroupsUpdated() {
        return groupsUpdated;
    }

    public void setGroupsUpdated(java.lang.Integer groupsUpdated) {
        this.groupsUpdated = groupsUpdated;
    }

    public java.lang.Integer getGroupsRemoved() {
        return groupsRemoved;
    }

    public void setGroupsRemoved(java.lang.Integer groupsRemoved) {
        this.groupsRemoved = groupsRemoved;
    }

    public java.lang.Integer getMembershipsCreated() {
        return membershipsCreated;
    }

    public void setMembershipsCreated(java.lang.Integer membershipsCreated) {
        this.membershipsCreated = membershipsCreated;
    }

    public java.lang.Integer getMembershipsRemoved() {
        return membershipsRemoved;
    }

    public void setMembershipsRemoved(java.lang.Integer membershipsRemoved) {
        this.membershipsRemoved = membershipsRemoved;
    }

    public java.lang.Integer getUsersSkipped() {
        return usersSkipped;
    }

    public void setUsersSkipped(java.lang.Integer usersSkipped) {
        this.usersSkipped = usersSkipped;
    }

    public enum Status {

        PENDING,
        RUNNING,
        ERROR,
        COMPLETE;}
}