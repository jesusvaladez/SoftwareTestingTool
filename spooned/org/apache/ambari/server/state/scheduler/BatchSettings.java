package org.apache.ambari.server.state.scheduler;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
public class BatchSettings {
    private java.lang.Integer batchSeparationInSeconds;

    private java.lang.Integer taskFailureTolerance;

    private java.lang.Integer taskFailureTolerancePerBatch;

    private java.lang.Boolean pauseAfterFirstBatch = false;

    @org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
    @org.codehaus.jackson.annotate.JsonProperty("batch_separation_in_seconds")
    public java.lang.Integer getBatchSeparationInSeconds() {
        return batchSeparationInSeconds;
    }

    public void setBatchSeparationInSeconds(java.lang.Integer batchSeparationInSeconds) {
        this.batchSeparationInSeconds = batchSeparationInSeconds;
    }

    @org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
    @org.codehaus.jackson.annotate.JsonProperty("task_failure_tolerance_limit")
    public java.lang.Integer getTaskFailureToleranceLimit() {
        return taskFailureTolerance;
    }

    public void setTaskFailureToleranceLimit(java.lang.Integer taskFailureTolerance) {
        this.taskFailureTolerance = taskFailureTolerance;
    }

    @org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
    @org.codehaus.jackson.annotate.JsonProperty("task_failure_tolerance_limit_per_batch")
    public java.lang.Integer getTaskFailureToleranceLimitPerBatch() {
        return taskFailureTolerancePerBatch;
    }

    @org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
    @org.codehaus.jackson.annotate.JsonProperty("pause_after_first_batch")
    public java.lang.Boolean isPauseAfterFirstBatch() {
        return pauseAfterFirstBatch;
    }

    public void setPauseAfterFirstBatch(java.lang.Boolean pauseAfterFirstBatch) {
        this.pauseAfterFirstBatch = pauseAfterFirstBatch;
    }

    public void setTaskFailureToleranceLimitPerBatch(java.lang.Integer taskFailureTolerancePerBatch) {
        this.taskFailureTolerancePerBatch = taskFailureTolerancePerBatch;
    }
}