package org.apache.oozie.ambari.view.workflowmanager;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
public class WorkflowsManagerResource {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.oozie.ambari.view.workflowmanager.WorkflowsManagerResource.class);

    private final org.apache.oozie.ambari.view.workflowmanager.WorkflowManagerService workflowManagerService;

    private final org.apache.ambari.view.ViewContext viewContext;

    public WorkflowsManagerResource(org.apache.ambari.view.ViewContext viewContext) {
        super();
        this.viewContext = viewContext;
        this.workflowManagerService = new org.apache.oozie.ambari.view.workflowmanager.WorkflowManagerService(viewContext);
    }

    @javax.ws.rs.GET
    public javax.ws.rs.core.Response getWorkflows() {
        try {
            java.util.HashMap<java.lang.String, java.lang.Object> result = new java.util.HashMap<>();
            result.put("wfprojects", workflowManagerService.getAllWorkflows(viewContext.getUsername()));
            return javax.ws.rs.core.Response.ok(result).build();
        } catch (java.lang.Exception ex) {
            org.apache.oozie.ambari.view.workflowmanager.WorkflowsManagerResource.LOGGER.error(ex.getMessage(), ex);
            throw new org.apache.oozie.ambari.view.exception.WfmWebException(ex);
        }
    }

    @javax.ws.rs.DELETE
    @javax.ws.rs.Path("/{projectId}")
    public javax.ws.rs.core.Response deleteWorkflow(@javax.ws.rs.PathParam("projectId")
    java.lang.String id, @javax.ws.rs.DefaultValue("false")
    @javax.ws.rs.QueryParam("deleteDefinition")
    java.lang.Boolean deleteDefinition) {
        try {
            workflowManagerService.deleteWorkflow(id, deleteDefinition);
            org.apache.oozie.ambari.view.model.APIResult result = new org.apache.oozie.ambari.view.model.APIResult();
            result.setStatus(org.apache.oozie.ambari.view.model.APIResult.Status.SUCCESS);
            return javax.ws.rs.core.Response.ok(result).build();
        } catch (java.lang.Exception ex) {
            org.apache.oozie.ambari.view.workflowmanager.WorkflowsManagerResource.LOGGER.error(ex.getMessage(), ex);
            throw new org.apache.oozie.ambari.view.exception.WfmWebException(ex);
        }
    }
}