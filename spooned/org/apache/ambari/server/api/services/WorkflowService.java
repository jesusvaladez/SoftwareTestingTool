package org.apache.ambari.server.api.services;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
public class WorkflowService extends org.apache.ambari.server.api.services.BaseService {
    private java.lang.String clusterName;

    public WorkflowService(java.lang.String clusterName) {
        this.clusterName = clusterName;
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{workflowId}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getWorkflow(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("workflowId")
    java.lang.String workflowId) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createWorkflowResource(clusterName, workflowId));
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getWorkflows(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createWorkflowResource(clusterName, null));
    }

    @javax.ws.rs.Path("{workflowId}/jobs")
    public org.apache.ambari.server.api.services.JobService getJobHandler(@javax.ws.rs.PathParam("workflowId")
    java.lang.String workflowId) {
        return new org.apache.ambari.server.api.services.JobService(clusterName, workflowId);
    }

    org.apache.ambari.server.api.resources.ResourceInstance createWorkflowResource(java.lang.String clusterName, java.lang.String workflowId) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, clusterName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Workflow, workflowId);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.Workflow, mapIds);
    }
}