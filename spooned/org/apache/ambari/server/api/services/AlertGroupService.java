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
public class AlertGroupService extends org.apache.ambari.server.api.services.BaseService {
    private java.lang.String m_clusterName = null;

    AlertGroupService(java.lang.String clusterName) {
        m_clusterName = clusterName;
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getGroups(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createAlertGroupResource(m_clusterName, null));
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    @javax.ws.rs.Path("{groupId}")
    public javax.ws.rs.core.Response getGroup(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("groupId")
    java.lang.Long groupId) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createAlertGroupResource(m_clusterName, groupId));
    }

    @javax.ws.rs.POST
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response createGroup(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.POST, createAlertGroupResource(m_clusterName, null));
    }

    @javax.ws.rs.PUT
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    @javax.ws.rs.Path("{groupId}")
    public javax.ws.rs.core.Response updateGroup(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("groupId")
    java.lang.Long groupId) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.PUT, createAlertGroupResource(m_clusterName, groupId));
    }

    @javax.ws.rs.DELETE
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    @javax.ws.rs.Path("{groupId}")
    public javax.ws.rs.core.Response deleteGroup(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("groupId")
    java.lang.Long groupId) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.DELETE, createAlertGroupResource(m_clusterName, groupId));
    }

    private org.apache.ambari.server.api.resources.ResourceInstance createAlertGroupResource(java.lang.String clusterName, java.lang.Long groupId) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, m_clusterName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.AlertGroup, null == groupId ? null : groupId.toString());
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.AlertGroup, mapIds);
    }
}