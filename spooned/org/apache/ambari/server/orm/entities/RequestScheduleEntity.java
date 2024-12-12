package org.apache.ambari.server.orm.entities;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
@javax.persistence.Table(name = "requestschedule")
@javax.persistence.Entity
@javax.persistence.NamedQueries({ @javax.persistence.NamedQuery(name = "allReqSchedules", query = "SELECT reqSchedule FROM RequestScheduleEntity reqSchedule"), @javax.persistence.NamedQuery(name = "reqScheduleByStatus", query = "SELECT reqSchedule FROM RequestScheduleEntity reqSchedule " + "WHERE reqSchedule.status=:status") })
@javax.persistence.TableGenerator(name = "schedule_id_generator", table = "ambari_sequences", pkColumnName = "sequence_name", valueColumnName = "sequence_value", pkColumnValue = "requestschedule_id_seq", initialValue = 1)
public class RequestScheduleEntity {
    @javax.persistence.Id
    @javax.persistence.Column(name = "schedule_id", nullable = false, insertable = true, updatable = true)
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.TABLE, generator = "schedule_id_generator")
    private long scheduleId;

    @javax.persistence.Column(name = "cluster_id", insertable = false, updatable = false, nullable = false)
    private java.lang.Long clusterId;

    @javax.persistence.Column(name = "description")
    private java.lang.String description;

    @javax.persistence.Column(name = "status")
    private java.lang.String status;

    @javax.persistence.Column(name = "batch_separation_seconds")
    private java.lang.Integer batchSeparationInSeconds;

    @javax.persistence.Column(name = "batch_toleration_limit")
    private java.lang.Integer batchTolerationLimit;

    @javax.persistence.Column(name = "batch_toleration_limit_per_batch")
    private java.lang.Integer batchTolerationLimitPerBatch;

    @javax.persistence.Column(name = "pause_after_first_batch")
    private java.lang.Boolean pauseAfterFirstBatch;

    @javax.persistence.Column(name = "authenticated_user_id")
    private java.lang.Integer authenticatedUserId;

    @javax.persistence.Column(name = "create_user")
    private java.lang.String createUser;

    @javax.persistence.Column(name = "create_timestamp")
    private java.lang.Long createTimestamp;

    @javax.persistence.Column(name = "update_user")
    protected java.lang.String updateUser;

    @javax.persistence.Column(name = "update_timestamp")
    private java.lang.Long updateTimestamp;

    @javax.persistence.Column(name = "minutes")
    private java.lang.String minutes;

    @javax.persistence.Column(name = "hours")
    private java.lang.String hours;

    @javax.persistence.Column(name = "days_of_month")
    private java.lang.String daysOfMonth;

    @javax.persistence.Column(name = "month")
    private java.lang.String month;

    @javax.persistence.Column(name = "day_of_week")
    private java.lang.String dayOfWeek;

    @javax.persistence.Column(name = "yearToSchedule")
    private java.lang.String year;

    @javax.persistence.Column(name = "starttime")
    private java.lang.String startTime;

    @javax.persistence.Column(name = "endtime")
    private java.lang.String endTime;

    @javax.persistence.Column(name = "last_execution_status")
    private java.lang.String lastExecutionStatus;

    @javax.persistence.ManyToOne
    @javax.persistence.JoinColumn(name = "cluster_id", referencedColumnName = "cluster_id", nullable = false)
    private org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity;

    @javax.persistence.OneToMany(mappedBy = "requestScheduleEntity", cascade = javax.persistence.CascadeType.ALL)
    private java.util.Collection<org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity> requestScheduleBatchRequestEntities;

    @javax.persistence.OneToMany(mappedBy = "requestScheduleEntity")
    private java.util.List<org.apache.ambari.server.orm.entities.RequestEntity> requestEntities;

    public long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public java.lang.Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(java.lang.Long clusterId) {
        this.clusterId = clusterId;
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

    public java.lang.Integer getBatchSeparationInSeconds() {
        return batchSeparationInSeconds;
    }

    public void setBatchSeparationInSeconds(java.lang.Integer batchSeparationInSeconds) {
        this.batchSeparationInSeconds = batchSeparationInSeconds;
    }

    public java.lang.Integer getBatchTolerationLimit() {
        return batchTolerationLimit;
    }

    public void setBatchTolerationLimit(java.lang.Integer batchTolerationLimit) {
        this.batchTolerationLimit = batchTolerationLimit;
    }

    public java.lang.String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(java.lang.String createUser) {
        this.createUser = createUser;
    }

    public java.lang.Long getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(java.lang.Long createTimestamp) {
        this.createTimestamp = createTimestamp;
    }

    public java.lang.String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(java.lang.String updateUser) {
        this.updateUser = updateUser;
    }

    public java.lang.Long getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(java.lang.Long updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public java.lang.String getMinutes() {
        return minutes;
    }

    public void setMinutes(java.lang.String minutes) {
        this.minutes = minutes;
    }

    public java.lang.String getHours() {
        return hours;
    }

    public void setHours(java.lang.String hours) {
        this.hours = hours;
    }

    public java.lang.String getDaysOfMonth() {
        return daysOfMonth;
    }

    public void setDaysOfMonth(java.lang.String daysOfMonth) {
        this.daysOfMonth = daysOfMonth;
    }

    public java.lang.String getMonth() {
        return month;
    }

    public void setMonth(java.lang.String month) {
        this.month = month;
    }

    public java.lang.String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(java.lang.String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public java.lang.String getYear() {
        return year;
    }

    public void setYear(java.lang.String year) {
        this.year = year;
    }

    public java.lang.String getStartTime() {
        return startTime;
    }

    public void setStartTime(java.lang.String startTime) {
        this.startTime = startTime;
    }

    public java.lang.String getEndTime() {
        return endTime;
    }

    public void setEndTime(java.lang.String endTime) {
        this.endTime = endTime;
    }

    public java.lang.String getLastExecutionStatus() {
        return lastExecutionStatus;
    }

    public void setLastExecutionStatus(java.lang.String lastExecutionStatus) {
        this.lastExecutionStatus = lastExecutionStatus;
    }

    public org.apache.ambari.server.orm.entities.ClusterEntity getClusterEntity() {
        return clusterEntity;
    }

    public void setClusterEntity(org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity) {
        this.clusterEntity = clusterEntity;
    }

    public java.util.Collection<org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity> getRequestScheduleBatchRequestEntities() {
        return requestScheduleBatchRequestEntities;
    }

    public void setRequestScheduleBatchRequestEntities(java.util.Collection<org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity> requestScheduleBatchRequestEntities) {
        this.requestScheduleBatchRequestEntities = requestScheduleBatchRequestEntities;
    }

    public java.util.List<org.apache.ambari.server.orm.entities.RequestEntity> getRequestEntities() {
        return requestEntities;
    }

    public void setRequestEntities(java.util.List<org.apache.ambari.server.orm.entities.RequestEntity> requestEntities) {
        this.requestEntities = requestEntities;
    }

    public java.lang.Integer getAuthenticatedUserId() {
        return authenticatedUserId;
    }

    public void setAuthenticatedUserId(java.lang.Integer authenticatedUser) {
        this.authenticatedUserId = authenticatedUser;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.entities.RequestScheduleEntity that = ((org.apache.ambari.server.orm.entities.RequestScheduleEntity) (o));
        if (scheduleId != that.scheduleId)
            return false;

        if (!clusterId.equals(that.clusterId))
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = ((int) (scheduleId ^ (scheduleId >>> 32)));
        result = (31 * result) + clusterId.hashCode();
        return result;
    }

    public java.lang.Integer getBatchTolerationLimitPerBatch() {
        return batchTolerationLimitPerBatch;
    }

    public void setBatchTolerationLimitPerBatch(java.lang.Integer batchTolerationLimitPerBatch) {
        this.batchTolerationLimitPerBatch = batchTolerationLimitPerBatch;
    }

    public java.lang.Boolean isPauseAfterFirstBatch() {
        return pauseAfterFirstBatch;
    }

    public void setPauseAfterFirstBatch(java.lang.Boolean pauseAfterFirstBatch) {
        this.pauseAfterFirstBatch = pauseAfterFirstBatch;
    }
}