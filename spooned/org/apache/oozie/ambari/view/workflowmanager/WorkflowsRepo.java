package org.apache.oozie.ambari.view.workflowmanager;
public class WorkflowsRepo extends org.apache.oozie.ambari.view.repo.BaseRepo<org.apache.oozie.ambari.view.workflowmanager.model.Workflow> {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.oozie.ambari.view.workflowmanager.WorkflowsRepo.class);

    public WorkflowsRepo(org.apache.ambari.view.DataStore dataStore) {
        super(org.apache.oozie.ambari.view.workflowmanager.model.Workflow.class, dataStore);
    }

    public java.util.Collection<org.apache.oozie.ambari.view.workflowmanager.model.Workflow> getWorkflows(java.lang.String userName) {
        try {
            java.util.Collection<org.apache.oozie.ambari.view.workflowmanager.model.Workflow> workflows = this.dataStore.findAll(org.apache.oozie.ambari.view.workflowmanager.model.Workflow.class, ("owner='" + userName) + "'");
            return workflows;
        } catch (org.apache.ambari.view.PersistenceException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    public org.apache.oozie.ambari.view.workflowmanager.model.Workflow getWorkflowByPath(java.lang.String path, java.lang.String userName) {
        try {
            java.util.Collection<org.apache.oozie.ambari.view.workflowmanager.model.Workflow> workflows = this.dataStore.findAll(org.apache.oozie.ambari.view.workflowmanager.model.Workflow.class, ("workflowDefinitionPath='" + path) + "'");
            if ((workflows == null) || workflows.isEmpty()) {
                return null;
            } else {
                java.util.List<org.apache.oozie.ambari.view.workflowmanager.model.Workflow> myWorkflows = filterWorkflows(workflows, userName, true);
                if (myWorkflows.isEmpty()) {
                    return null;
                } else if (myWorkflows.size() == 1) {
                    return myWorkflows.get(0);
                } else {
                    org.apache.oozie.ambari.view.workflowmanager.WorkflowsRepo.LOGGER.error("Duplicate workflows found having same path");
                    throw new java.lang.RuntimeException("Duplicate workflows. Remove one in Recent Workflows Manager");
                }
            }
        } catch (org.apache.ambari.view.PersistenceException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    private java.util.List<org.apache.oozie.ambari.view.workflowmanager.model.Workflow> filterWorkflows(java.util.Collection<org.apache.oozie.ambari.view.workflowmanager.model.Workflow> workflows, java.lang.String userName, boolean matches) {
        java.util.List<org.apache.oozie.ambari.view.workflowmanager.model.Workflow> filteredWorkflows = new java.util.ArrayList<>();
        for (org.apache.oozie.ambari.view.workflowmanager.model.Workflow wf : workflows) {
            if (matches && userName.equals(wf.getOwner())) {
                filteredWorkflows.add(wf);
            } else if ((!matches) && (!userName.equals(wf.getOwner()))) {
                filteredWorkflows.add(wf);
            }
        }
        return filteredWorkflows;
    }
}