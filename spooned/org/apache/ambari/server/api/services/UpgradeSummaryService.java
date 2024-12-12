package org.apache.ambari.server.api.services;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
public class UpgradeSummaryService extends org.apache.ambari.server.api.services.BaseService {
    private java.lang.String m_clusterName = null;

    UpgradeSummaryService(java.lang.String clusterName) {
        m_clusterName = clusterName;
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getUpgradeSummaries(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createResourceInstance(null));
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{requestId}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getUpgradeSummary(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("requestId")
    java.lang.Long requestId) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createResourceInstance(requestId));
    }

    private org.apache.ambari.server.api.resources.ResourceInstance createResourceInstance(java.lang.Long requestId) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, m_clusterName);
        if (null != requestId) {
            mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.UpgradeSummary, requestId.toString());
        }
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.UpgradeSummary, mapIds);
    }
}