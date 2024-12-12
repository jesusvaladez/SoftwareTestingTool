package org.apache.ambari.server.controller.ivory;
public class Instance {
    private final java.lang.String feedName;

    private final java.lang.String id;

    private final java.lang.String status;

    private final java.lang.String startTime;

    private final java.lang.String endTime;

    private final java.lang.String details;

    private final java.lang.String log;

    public Instance(java.lang.String feedName, java.lang.String id, java.lang.String status, java.lang.String startTime, java.lang.String endTime, java.lang.String details, java.lang.String log) {
        this.feedName = feedName;
        this.id = id;
        this.status = status;
        this.startTime = startTime;
        this.endTime = endTime;
        this.details = details;
        this.log = log;
    }

    public java.lang.String getFeedName() {
        return feedName;
    }

    public java.lang.String getId() {
        return id;
    }

    public java.lang.String getStatus() {
        return status;
    }

    public java.lang.String getStartTime() {
        return startTime;
    }

    public java.lang.String getEndTime() {
        return endTime;
    }

    public java.lang.String getDetails() {
        return details;
    }

    public java.lang.String getLog() {
        return log;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.controller.ivory.Instance instance = ((org.apache.ambari.server.controller.ivory.Instance) (o));
        return ((((((!(details != null ? !details.equals(instance.details) : instance.details != null)) && (!(endTime != null ? !endTime.equals(instance.endTime) : instance.endTime != null))) && (!(feedName != null ? !feedName.equals(instance.feedName) : instance.feedName != null))) && (!(id != null ? !id.equals(instance.id) : instance.id != null))) && (!(log != null ? !log.equals(instance.log) : instance.log != null))) && (!(startTime != null ? !startTime.equals(instance.startTime) : instance.startTime != null))) && (!(status != null ? !status.equals(instance.status) : instance.status != null));
    }

    @java.lang.Override
    public int hashCode() {
        int result = (feedName != null) ? feedName.hashCode() : 0;
        result = (31 * result) + (id != null ? id.hashCode() : 0);
        result = (31 * result) + (status != null ? status.hashCode() : 0);
        result = (31 * result) + (startTime != null ? startTime.hashCode() : 0);
        result = (31 * result) + (endTime != null ? endTime.hashCode() : 0);
        result = (31 * result) + (details != null ? details.hashCode() : 0);
        result = (31 * result) + (log != null ? log.hashCode() : 0);
        return result;
    }
}