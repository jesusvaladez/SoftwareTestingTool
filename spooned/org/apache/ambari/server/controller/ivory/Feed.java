package org.apache.ambari.server.controller.ivory;
public class Feed {
    private final java.lang.String name;

    private final java.lang.String description;

    private final java.lang.String status;

    private final java.lang.String schedule;

    private final java.lang.String sourceClusterName;

    private final java.lang.String sourceClusterStart;

    private final java.lang.String sourceClusterEnd;

    private final java.lang.String sourceClusterLimit;

    private final java.lang.String sourceClusterAction;

    private final java.lang.String targetClusterName;

    private final java.lang.String targetClusterStart;

    private final java.lang.String targetClusterEnd;

    private final java.lang.String targetClusterLimit;

    private final java.lang.String targetClusterAction;

    private final java.util.Map<java.lang.String, java.lang.String> properties;

    public Feed(java.lang.String name, java.lang.String description, java.lang.String status, java.lang.String schedule, java.lang.String sourceClusterName, java.lang.String sourceClusterStart, java.lang.String sourceClusterEnd, java.lang.String sourceClusterLimit, java.lang.String sourceClusterAction, java.lang.String targetClusterName, java.lang.String targetClusterStart, java.lang.String targetClusterEnd, java.lang.String targetClusterLimit, java.lang.String targetClusterAction, java.util.Map<java.lang.String, java.lang.String> properties) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.schedule = schedule;
        this.sourceClusterName = sourceClusterName;
        this.sourceClusterStart = sourceClusterStart;
        this.sourceClusterEnd = sourceClusterEnd;
        this.sourceClusterLimit = sourceClusterLimit;
        this.sourceClusterAction = sourceClusterAction;
        this.targetClusterName = targetClusterName;
        this.targetClusterStart = targetClusterStart;
        this.targetClusterEnd = targetClusterEnd;
        this.targetClusterLimit = targetClusterLimit;
        this.targetClusterAction = targetClusterAction;
        this.properties = properties;
    }

    public java.lang.String getName() {
        return name;
    }

    public java.lang.String getDescription() {
        return description;
    }

    public java.lang.String getStatus() {
        return status;
    }

    public java.lang.String getSchedule() {
        return schedule;
    }

    public java.lang.String getSourceClusterName() {
        return sourceClusterName;
    }

    public java.lang.String getSourceClusterStart() {
        return sourceClusterStart;
    }

    public java.lang.String getSourceClusterEnd() {
        return sourceClusterEnd;
    }

    public java.lang.String getSourceClusterLimit() {
        return sourceClusterLimit;
    }

    public java.lang.String getSourceClusterAction() {
        return sourceClusterAction;
    }

    public java.lang.String getTargetClusterName() {
        return targetClusterName;
    }

    public java.lang.String getTargetClusterStart() {
        return targetClusterStart;
    }

    public java.lang.String getTargetClusterEnd() {
        return targetClusterEnd;
    }

    public java.lang.String getTargetClusterLimit() {
        return targetClusterLimit;
    }

    public java.lang.String getTargetClusterAction() {
        return targetClusterAction;
    }

    public java.util.Map<java.lang.String, java.lang.String> getProperties() {
        return properties;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.controller.ivory.Feed feed = ((org.apache.ambari.server.controller.ivory.Feed) (o));
        return (((((!(description != null ? !description.equals(feed.description) : feed.description != null)) && (!(name != null ? !name.equals(feed.name) : feed.name != null))) && (!(schedule != null ? !schedule.equals(feed.schedule) : feed.schedule != null))) && (!(sourceClusterName != null ? !sourceClusterName.equals(feed.sourceClusterName) : feed.sourceClusterName != null))) && (!(status != null ? !status.equals(feed.status) : feed.status != null))) && (!(targetClusterName != null ? !targetClusterName.equals(feed.targetClusterName) : feed.targetClusterName != null));
    }

    @java.lang.Override
    public int hashCode() {
        int result = (name != null) ? name.hashCode() : 0;
        result = (31 * result) + (description != null ? description.hashCode() : 0);
        result = (31 * result) + (status != null ? status.hashCode() : 0);
        result = (31 * result) + (schedule != null ? schedule.hashCode() : 0);
        result = (31 * result) + (sourceClusterName != null ? sourceClusterName.hashCode() : 0);
        result = (31 * result) + (targetClusterName != null ? targetClusterName.hashCode() : 0);
        return result;
    }
}