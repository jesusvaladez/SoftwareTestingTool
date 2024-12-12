package org.apache.ambari.server.api.services;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
public class AlertHistoryService extends org.apache.ambari.server.api.services.BaseService {
    private java.lang.String clusterName = null;

    private java.lang.String serviceName = null;

    private java.lang.String hostName = null;

    AlertHistoryService(java.lang.String clusterName, java.lang.String serviceName, java.lang.String hostName) {
        this.clusterName = clusterName;
        this.serviceName = serviceName;
        this.hostName = hostName;
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getHistories(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createResourceInstance(clusterName, null));
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{alertHistoryId}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getHistory(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("alertHistoryId")
    java.lang.Long id) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createResourceInstance(clusterName, id));
    }

    private org.apache.ambari.server.api.resources.ResourceInstance createResourceInstance(java.lang.String clusterName, java.lang.Long historyId) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, clusterName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Service, serviceName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Host, hostName);
        if (null != historyId) {
            mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.AlertHistory, historyId.toString());
        }
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.AlertHistory, mapIds);
    }
}