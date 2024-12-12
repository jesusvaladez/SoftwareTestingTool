package org.apache.ambari.server.api.services;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
public class TaskService extends org.apache.ambari.server.api.services.BaseService {
    private java.lang.String m_clusterName;

    private java.lang.String m_requestId;

    private java.lang.String m_stageId;

    public TaskService(java.lang.String clusterName, java.lang.String requestId, java.lang.String stageId) {
        m_clusterName = clusterName;
        m_requestId = requestId;
        m_stageId = stageId;
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{taskId}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getTask(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("taskId")
    java.lang.String taskId) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createTaskResource(m_clusterName, m_requestId, m_stageId, taskId));
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getComponents(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createTaskResource(m_clusterName, m_requestId, m_stageId, null));
    }

    org.apache.ambari.server.api.resources.ResourceInstance createTaskResource(java.lang.String clusterName, java.lang.String requestId, java.lang.String stageId, java.lang.String taskId) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, clusterName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Request, requestId);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Stage, stageId);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Task, taskId);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.Task, mapIds);
    }
}