package org.apache.oozie.ambari.view.workflowmanager.model;
public class Workflow extends org.apache.oozie.ambari.view.model.BaseModel implements org.apache.oozie.ambari.view.model.Indexed {
    private static final long serialVersionUID = 1L;

    private java.lang.String id = null;

    private java.lang.String name;

    private java.lang.String desciption;

    private java.lang.String workflowDefinitionPath;

    private java.lang.String type;

    private java.lang.String isDraft;

    private java.lang.String definitionMissing;

    public java.lang.String getType() {
        return type;
    }

    public void setType(java.lang.String type) {
        this.type = type;
    }

    public java.lang.String getId() {
        return id;
    }

    public void setId(java.lang.String id) {
        this.id = id;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getWorkflowDefinitionPath() {
        return workflowDefinitionPath;
    }

    public void setWorkflowDefinitionPath(java.lang.String workflowDefinitionPath) {
        this.workflowDefinitionPath = workflowDefinitionPath;
    }

    public java.lang.String getIsDraft() {
        return isDraft;
    }

    public void setIsDraft(java.lang.String isDraft) {
        this.isDraft = isDraft;
    }

    public java.lang.String getDesciption() {
        return desciption;
    }

    public void setDesciption(java.lang.String desciption) {
        this.desciption = desciption;
    }

    public java.lang.String getDefinitionMissing() {
        return definitionMissing;
    }

    public void setDefinitionMissing(java.lang.String definitionMissing) {
        this.definitionMissing = definitionMissing;
    }
}