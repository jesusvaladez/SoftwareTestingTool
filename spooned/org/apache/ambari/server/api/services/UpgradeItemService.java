package org.apache.ambari.server.api.services;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
public class UpgradeItemService extends org.apache.ambari.server.api.services.BaseService {
    private java.lang.String m_clusterName = null;

    private java.lang.String m_upgradeId = null;

    private java.lang.String m_upgradeGroupId = null;

    UpgradeItemService(java.lang.String clusterName, java.lang.String upgradeId, java.lang.String upgradeGroupId) {
        m_clusterName = clusterName;
        m_upgradeId = upgradeId;
        m_upgradeGroupId = upgradeGroupId;
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getUpgrades(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createResourceInstance(null));
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{upgradeItemId}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getUpgradeItem(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("upgradeItemId")
    java.lang.Long id) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createResourceInstance(id));
    }

    @javax.ws.rs.PUT
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{upgradeItemId}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response updateUpgradeItem(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("upgradeItemId")
    java.lang.Long id) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.PUT, createResourceInstance(id));
    }

    @javax.ws.rs.Path("{upgradeItemId}/tasks")
    public org.apache.ambari.server.api.services.TaskService getTasks(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("upgradeItemId")
    java.lang.Long id) {
        return new org.apache.ambari.server.api.services.TaskService(m_clusterName, m_upgradeId, id.toString());
    }

    org.apache.ambari.server.api.resources.ResourceInstance createResourceInstance(java.lang.Long upgradeItemId) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, m_clusterName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Upgrade, m_upgradeId);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.UpgradeGroup, m_upgradeGroupId);
        if (null != upgradeItemId) {
            mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.UpgradeItem, upgradeItemId.toString());
        }
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.UpgradeItem, mapIds);
    }
}