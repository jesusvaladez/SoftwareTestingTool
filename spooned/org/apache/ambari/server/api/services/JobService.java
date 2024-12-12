package org.apache.ambari.server.api.services;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
public class JobService extends org.apache.ambari.server.api.services.BaseService {
    private java.lang.String workflowId;

    private java.lang.String clusterName;

    public JobService(java.lang.String clusterName, java.lang.String workflowId) {
        this.clusterName = clusterName;
        this.workflowId = workflowId;
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{jobId}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getJob(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("jobId")
    java.lang.String jobId) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createJobResource(clusterName, workflowId, jobId));
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getJobs(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createJobResource(clusterName, workflowId, null));
    }

    @javax.ws.rs.Path("{jobId}/taskattempts")
    public org.apache.ambari.server.api.services.TaskAttemptService getTaskAttemptHandler(@javax.ws.rs.PathParam("jobId")
    java.lang.String jobId) {
        return new org.apache.ambari.server.api.services.TaskAttemptService(clusterName, workflowId, jobId);
    }

    org.apache.ambari.server.api.resources.ResourceInstance createJobResource(java.lang.String clusterName, java.lang.String workflowId, java.lang.String jobId) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, clusterName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Workflow, workflowId);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Job, jobId);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.Job, mapIds);
    }
}