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
public class UpgradeService extends org.apache.ambari.server.api.services.BaseService {
    private java.lang.String m_clusterName = null;

    UpgradeService(java.lang.String clusterName) {
        m_clusterName = clusterName;
    }

    @javax.ws.rs.POST
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response createUpgrade(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.POST, createResourceInstance(null));
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
    @javax.ws.rs.Path("{upgradeId}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getUpgradeItem(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("upgradeId")
    java.lang.Long id) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createResourceInstance(id));
    }

    @javax.ws.rs.PUT
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{upgradeId}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response updateUpgradeItem(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("upgradeId")
    java.lang.Long id) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.PUT, createResourceInstance(id));
    }

    @javax.ws.rs.Path("{upgradeId}/upgrade_groups")
    public org.apache.ambari.server.api.services.UpgradeGroupService getUpgradeGroupHandler(@javax.ws.rs.PathParam("upgradeId")
    java.lang.String upgradeId) {
        return new org.apache.ambari.server.api.services.UpgradeGroupService(m_clusterName, upgradeId);
    }

    private org.apache.ambari.server.api.resources.ResourceInstance createResourceInstance(java.lang.Long upgradeId) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, m_clusterName);
        if (null != upgradeId) {
            mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Upgrade, upgradeId.toString());
        }
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.Upgrade, mapIds);
    }
}