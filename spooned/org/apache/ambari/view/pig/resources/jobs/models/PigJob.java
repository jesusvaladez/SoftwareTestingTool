package org.apache.ambari.view.pig.resources.jobs.models;
import org.apache.commons.beanutils.BeanUtils;
public class PigJob implements java.io.Serializable , org.apache.ambari.view.pig.persistence.utils.PersonalResource {
    public static final java.lang.String PIG_JOB_STATE_UNKNOWN = "UNKNOWN";

    public static final java.lang.String PIG_JOB_STATE_SUBMITTING = "SUBMITTING";

    public static final java.lang.String PIG_JOB_STATE_SUBMITTED = "SUBMITTED";

    public static final java.lang.String PIG_JOB_STATE_RUNNING = "RUNNING";

    public static final java.lang.String PIG_JOB_STATE_SUBMIT_FAILED = "SUBMIT_FAILED";

    public static final java.lang.String PIG_JOB_STATE_COMPLETED = "COMPLETED";

    public static final java.lang.String PIG_JOB_STATE_FAILED = "FAILED";

    public static final java.lang.String PIG_JOB_STATE_KILLED = "KILLED";

    public PigJob() {
    }

    public PigJob(java.util.Map<java.lang.String, java.lang.Object> stringObjectMap) throws java.lang.reflect.InvocationTargetException, java.lang.IllegalAccessException {
        org.apache.commons.beanutils.BeanUtils.populate(this, stringObjectMap);
    }

    private java.lang.String id = null;

    private java.lang.String scriptId = null;

    private java.lang.String pigScript = null;

    private java.lang.String pythonScript = null;

    private java.lang.String title = null;

    private java.lang.String templetonArguments = null;

    private java.lang.String owner;

    private java.lang.String forcedContent = null;

    private java.lang.String jobType = null;

    private java.lang.String sourceFile = null;

    private java.lang.String sourceFileContent = null;

    private java.lang.String statusDir;

    private java.lang.Long dateStarted = 0L;

    private java.lang.Long duration = 0L;

    private java.lang.String jobId = null;

    private java.lang.String status = org.apache.ambari.view.pig.resources.jobs.models.PigJob.PIG_JOB_STATE_UNKNOWN;

    private java.lang.Integer percentComplete = null;

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if (!(o instanceof org.apache.ambari.view.pig.resources.jobs.models.PigJob))
            return false;

        org.apache.ambari.view.pig.resources.jobs.models.PigJob pigScript = ((org.apache.ambari.view.pig.resources.jobs.models.PigJob) (o));
        return id.equals(pigScript.id);
    }

    @java.lang.Override
    public int hashCode() {
        return id.hashCode();
    }

    public boolean isInProgress() {
        return (status.equals(org.apache.ambari.view.pig.resources.jobs.models.PigJob.PIG_JOB_STATE_SUBMITTED) || status.equals(org.apache.ambari.view.pig.resources.jobs.models.PigJob.PIG_JOB_STATE_SUBMITTING)) || status.equals(org.apache.ambari.view.pig.resources.jobs.models.PigJob.PIG_JOB_STATE_RUNNING);
    }

    @java.lang.Override
    public java.lang.String getId() {
        return id;
    }

    @java.lang.Override
    public void setId(java.lang.String id) {
        this.id = id;
    }

    @java.lang.Override
    public java.lang.String getOwner() {
        return owner;
    }

    @java.lang.Override
    public void setOwner(java.lang.String owner) {
        this.owner = owner;
    }

    public java.lang.String getStatus() {
        return status;
    }

    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    public java.lang.String getScriptId() {
        return scriptId;
    }

    public void setScriptId(java.lang.String scriptId) {
        this.scriptId = scriptId;
    }

    public java.lang.String getTempletonArguments() {
        return templetonArguments;
    }

    public void setTempletonArguments(java.lang.String templetonArguments) {
        this.templetonArguments = templetonArguments;
    }

    public java.lang.String getPigScript() {
        return pigScript;
    }

    public void setPigScript(java.lang.String pigScript) {
        this.pigScript = pigScript;
    }

    public java.lang.String getJobId() {
        return jobId;
    }

    public void setJobId(java.lang.String jobId) {
        this.jobId = jobId;
    }

    public java.lang.String getTitle() {
        return title;
    }

    public void setTitle(java.lang.String title) {
        this.title = title;
    }

    public void setStatusDir(java.lang.String statusDir) {
        this.statusDir = statusDir;
    }

    public java.lang.String getStatusDir() {
        return statusDir;
    }

    public java.lang.Long getDateStarted() {
        return dateStarted;
    }

    public void setDateStarted(java.lang.Long dateStarted) {
        this.dateStarted = dateStarted;
    }

    public java.lang.Long getDuration() {
        return duration;
    }

    public void setDuration(java.lang.Long duration) {
        this.duration = duration;
    }

    public java.lang.Integer getPercentComplete() {
        return percentComplete;
    }

    public void setPercentComplete(java.lang.Integer percentComplete) {
        this.percentComplete = percentComplete;
    }

    public java.lang.String getPythonScript() {
        return pythonScript;
    }

    public void setPythonScript(java.lang.String pythonScript) {
        this.pythonScript = pythonScript;
    }

    public java.lang.String getForcedContent() {
        return forcedContent;
    }

    public void setForcedContent(java.lang.String forcedContent) {
        this.forcedContent = forcedContent;
    }

    public java.lang.String getJobType() {
        return jobType;
    }

    public void setJobType(java.lang.String jobType) {
        this.jobType = jobType;
    }

    public java.lang.String getSourceFileContent() {
        return sourceFileContent;
    }

    public void setSourceFileContent(java.lang.String sourceFileContent) {
        this.sourceFileContent = sourceFileContent;
    }

    public java.lang.String getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(java.lang.String sourceFile) {
        this.sourceFile = sourceFile;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return new java.lang.StringBuilder("PigJob{").append("id='").append(id).append(", scriptId='").append(scriptId).append(", owner='").append(owner).append(", jobId='").append(jobId).append('}').toString();
    }
}