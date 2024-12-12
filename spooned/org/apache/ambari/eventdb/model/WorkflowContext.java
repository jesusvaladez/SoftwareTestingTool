package org.apache.ambari.eventdb.model;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
@javax.xml.bind.annotation.XmlRootElement
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public class WorkflowContext {
    private java.lang.String workflowId;

    private java.lang.String workflowName;

    private java.lang.String workflowEntityName;

    private org.apache.ambari.eventdb.model.WorkflowDag workflowDag;

    private org.apache.ambari.eventdb.model.WorkflowContext parentWorkflowContext;

    public WorkflowContext() {
    }

    public java.lang.String getWorkflowId() {
        return this.workflowId;
    }

    public java.lang.String getWorkflowName() {
        return this.workflowName;
    }

    public java.lang.String getWorkflowEntityName() {
        return this.workflowEntityName;
    }

    public org.apache.ambari.eventdb.model.WorkflowDag getWorkflowDag() {
        return this.workflowDag;
    }

    public org.apache.ambari.eventdb.model.WorkflowContext getParentWorkflowContext() {
        return this.parentWorkflowContext;
    }

    public void setWorkflowId(java.lang.String wfId) {
        this.workflowId = wfId;
    }

    public void setWorkflowName(java.lang.String wfName) {
        this.workflowName = wfName;
    }

    public void setWorkflowEntityName(java.lang.String wfEntityName) {
        this.workflowEntityName = wfEntityName;
    }

    public void setWorkflowDag(org.apache.ambari.eventdb.model.WorkflowDag wfDag) {
        this.workflowDag = wfDag;
    }

    public void setParentWorkflowContext(org.apache.ambari.eventdb.model.WorkflowContext pWfContext) {
        this.parentWorkflowContext = pWfContext;
    }
}