package org.apache.ambari.server.api.services;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
public class StageService extends org.apache.ambari.server.api.services.BaseService {
    private java.lang.String m_clusterName;

    private java.lang.String m_requestId;

    public StageService(java.lang.String clusterName, java.lang.String requestId) {
        m_clusterName = clusterName;
        m_requestId = requestId;
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{stageId}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getStage(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("stageId")
    java.lang.String stageId) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createStageResource(m_clusterName, m_requestId, stageId));
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getStages(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createStageResource(m_clusterName, m_requestId, null));
    }

    @javax.ws.rs.Path("{stageId}/tasks")
    public org.apache.ambari.server.api.services.TaskService getTaskHandler(@javax.ws.rs.PathParam("stageId")
    java.lang.String stageId) {
        return new org.apache.ambari.server.api.services.TaskService(m_clusterName, m_requestId, stageId);
    }

    @javax.ws.rs.PUT
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{stageId}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response updateStages(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("stageId")
    java.lang.String stageId) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.PUT, createStageResource(m_clusterName, m_requestId, stageId));
    }

    @javax.ws.rs.POST
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response createStages(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.POST, createStageResource(m_clusterName, m_requestId, null));
    }

    org.apache.ambari.server.api.resources.ResourceInstance createStageResource(java.lang.String clusterName, java.lang.String requestId, java.lang.String stageId) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        if (clusterName != null) {
            mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, clusterName);
        }
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Request, requestId);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Stage, stageId);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.Stage, mapIds);
    }
}