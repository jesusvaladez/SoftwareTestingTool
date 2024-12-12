package org.apache.oozie.ambari.view;
public class WorkflowFileInfo {
    private java.lang.String workflowPath;

    private java.lang.String draftPath;

    private java.lang.Boolean draftExists;

    private java.lang.Boolean isDraftCurrent = false;

    private java.lang.Boolean workflowDefinitionExists = false;

    public java.lang.Boolean getIsDraftCurrent() {
        return isDraftCurrent;
    }

    public void setIsDraftCurrent(java.lang.Boolean isDraftCurrent) {
        this.isDraftCurrent = isDraftCurrent;
    }

    private java.lang.Long workflowModificationTime;

    private java.lang.Long draftModificationTime;

    public java.lang.String getWorkflowPath() {
        return workflowPath;
    }

    public void setWorkflowPath(java.lang.String workflowPath) {
        this.workflowPath = workflowPath;
    }

    public java.lang.String getDraftPath() {
        return draftPath;
    }

    public void setDraftPath(java.lang.String draftPath) {
        this.draftPath = draftPath;
    }

    public java.lang.Boolean getDraftExists() {
        return draftExists;
    }

    public void setDraftExists(java.lang.Boolean draftExists) {
        this.draftExists = draftExists;
    }

    public void setWorkflowModificationTime(java.lang.Long modificationTime) {
        this.workflowModificationTime = modificationTime;
    }

    public void setDraftModificationTime(java.lang.Long modificationTime) {
        this.draftModificationTime = modificationTime;
    }

    public java.lang.Long getWorkflowModificationTime() {
        return workflowModificationTime;
    }

    public java.lang.Long getDraftModificationTime() {
        return draftModificationTime;
    }

    public void setWorkflowDefinitionExists(java.lang.Boolean workflowDefinitionExists) {
        this.workflowDefinitionExists = workflowDefinitionExists;
    }
}