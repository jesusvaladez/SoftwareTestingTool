package org.apache.oozie.ambari.view.workflowmanager;
public class WorkflowManagerService {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.oozie.ambari.view.workflowmanager.WorkflowManagerService.class);

    private final org.apache.oozie.ambari.view.workflowmanager.WorkflowsRepo workflowsRepository;

    private final org.apache.oozie.ambari.view.WorkflowFilesService workflowFilesService;

    public WorkflowManagerService(org.apache.ambari.view.ViewContext viewContext) {
        workflowsRepository = new org.apache.oozie.ambari.view.workflowmanager.WorkflowsRepo(viewContext.getDataStore());
        workflowFilesService = new org.apache.oozie.ambari.view.WorkflowFilesService(new org.apache.oozie.ambari.view.HDFSFileUtils(viewContext));
    }

    public void saveWorkflow(java.lang.String projectId, java.lang.String path, org.apache.oozie.ambari.view.JobType jobType, java.lang.String descripton, java.lang.String userName, java.lang.String name) {
        org.apache.oozie.ambari.view.workflowmanager.WorkflowManagerService.LOGGER.debug("save workflow called");
        if (projectId != null) {
            org.apache.oozie.ambari.view.workflowmanager.model.Workflow workflowById = workflowsRepository.findById(projectId);
            if (workflowById == null) {
                throw new java.lang.RuntimeException("could not find project with id :" + projectId);
            }
            setWorkflowAttributes(jobType, userName, name, workflowById);
            workflowsRepository.update(workflowById);
        } else {
            java.lang.String workflowFileName = workflowFilesService.getWorkflowFileName(path, jobType);
            org.apache.oozie.ambari.view.workflowmanager.model.Workflow workflowByPath = workflowsRepository.getWorkflowByPath(workflowFileName, userName);
            if (workflowByPath != null) {
                setWorkflowAttributes(jobType, userName, name, workflowByPath);
                workflowsRepository.update(workflowByPath);
            } else {
                org.apache.oozie.ambari.view.workflowmanager.model.Workflow wf = new org.apache.oozie.ambari.view.workflowmanager.model.Workflow();
                wf.setId(workflowsRepository.generateId());
                setWorkflowAttributes(jobType, userName, name, wf);
                wf.setWorkflowDefinitionPath(workflowFileName);
                workflowsRepository.create(wf);
            }
        }
    }

    private void setWorkflowAttributes(org.apache.oozie.ambari.view.JobType jobType, java.lang.String userName, java.lang.String name, org.apache.oozie.ambari.view.workflowmanager.model.Workflow wf) {
        wf.setOwner(userName);
        wf.setName(name);
        wf.setType(jobType.name());
    }

    public java.util.Collection<org.apache.oozie.ambari.view.workflowmanager.model.Workflow> getAllWorkflows(java.lang.String username) {
        return workflowsRepository.getWorkflows(username);
    }

    public void deleteWorkflow(java.lang.String projectId, java.lang.Boolean deleteDefinition) {
        org.apache.oozie.ambari.view.workflowmanager.model.Workflow workflow = workflowsRepository.findById(projectId);
        if (deleteDefinition) {
            workflowFilesService.deleteWorkflowFile(workflow.getWorkflowDefinitionPath());
        }
        workflowsRepository.delete(workflow);
    }
}