package org.apache.ambari.server.api.services;
import javax.ws.rs.DELETE;
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
public class AlertDefinitionService extends org.apache.ambari.server.api.services.BaseService {
    private java.lang.String clusterName = null;

    AlertDefinitionService(java.lang.String clusterName) {
        this.clusterName = clusterName;
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getDefinitions(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createResourceInstance(clusterName, null));
    }

    @javax.ws.rs.POST
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response createDefinition(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.POST, createResourceInstance(clusterName, null));
    }

    @javax.ws.rs.PUT
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{alertDefinitionId}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response updateDefinition(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("alertDefinitionId")
    java.lang.Long id) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.PUT, createResourceInstance(clusterName, id));
    }

    @javax.ws.rs.DELETE
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{alertDefinitionId}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response deleteDefinition(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("alertDefinitionId")
    java.lang.Long id) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.DELETE, createResourceInstance(clusterName, id));
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{alertDefinitionId}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getDefinitions(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("alertDefinitionId")
    java.lang.Long id) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createResourceInstance(clusterName, id));
    }

    private org.apache.ambari.server.api.resources.ResourceInstance createResourceInstance(java.lang.String clusterName, java.lang.Long definitionId) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, clusterName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.AlertDefinition, null == definitionId ? null : definitionId.toString());
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.AlertDefinition, mapIds);
    }
}