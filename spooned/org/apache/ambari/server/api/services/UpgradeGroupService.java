package org.apache.ambari.server.api.services;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
public class UpgradeGroupService extends org.apache.ambari.server.api.services.BaseService {
    private java.lang.String m_clusterName = null;

    private java.lang.String m_upgradeId = null;

    UpgradeGroupService(java.lang.String clusterName, java.lang.String upgradeId) {
        m_clusterName = clusterName;
        m_upgradeId = upgradeId;
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getGroups(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createResourceInstance(null));
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{upgradeGroupId}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getGroup(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("upgradeGroupId")
    java.lang.Long id) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createResourceInstance(id));
    }

    @javax.ws.rs.Path("{upgradeGroupId}/upgrade_items")
    public org.apache.ambari.server.api.services.UpgradeItemService getUpgradeItemService(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.PathParam("upgradeGroupId")
    java.lang.Long groupId) {
        return new org.apache.ambari.server.api.services.UpgradeItemService(m_clusterName, m_upgradeId, groupId.toString());
    }

    private org.apache.ambari.server.api.resources.ResourceInstance createResourceInstance(java.lang.Long groupId) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, m_clusterName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Upgrade, m_upgradeId);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Request, m_upgradeId);
        if (null != groupId) {
            mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.UpgradeGroup, groupId.toString());
        }
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.UpgradeGroup, mapIds);
    }
}