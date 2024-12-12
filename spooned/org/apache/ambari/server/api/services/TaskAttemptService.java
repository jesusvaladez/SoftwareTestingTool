package org.apache.ambari.server.api.services;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
public class TaskAttemptService extends org.apache.ambari.server.api.services.BaseService {
    private java.lang.String jobId;

    private java.lang.String workflowId;

    private java.lang.String clusterName;

    public TaskAttemptService(java.lang.String clusterName, java.lang.String workflowId, java.lang.String jobId) {
        this.clusterName = clusterName;
        this.workflowId = workflowId;
        this.jobId = jobId;
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{taskAttemptId}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getTaskAttempt(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("taskAttemptId")
    java.lang.String taskAttemptId) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createTaskAttemptResource(clusterName, workflowId, jobId, taskAttemptId));
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getTaskAttempts(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createTaskAttemptResource(clusterName, workflowId, jobId, null));
    }

    org.apache.ambari.server.api.resources.ResourceInstance createTaskAttemptResource(java.lang.String clusterName, java.lang.String workflowId, java.lang.String jobId, java.lang.String taskAttemptId) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, clusterName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Workflow, workflowId);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Job, jobId);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.TaskAttempt, taskAttemptId);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.TaskAttempt, mapIds);
    }
}