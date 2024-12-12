package org.apache.ambari.server.api.services;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
public class AlertNoticeService extends org.apache.ambari.server.api.services.BaseService {
    private java.lang.String clusterName = null;

    AlertNoticeService(java.lang.String clusterName) {
        this.clusterName = clusterName;
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getNotices(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createResourceInstance(clusterName, null));
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{alertNoticeId}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getNotice(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("alertNoticeId")
    java.lang.Long id) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createResourceInstance(clusterName, id));
    }

    private org.apache.ambari.server.api.resources.ResourceInstance createResourceInstance(java.lang.String clusterName, java.lang.Long alertNoticeId) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, clusterName);
        if (null != alertNoticeId) {
            mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.AlertNotice, alertNoticeId.toString());
        }
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.AlertNotice, mapIds);
    }
}