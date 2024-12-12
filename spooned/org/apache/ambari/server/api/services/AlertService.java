package org.apache.ambari.server.api.services;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
public class AlertService extends org.apache.ambari.server.api.services.BaseService {
    private java.lang.String clusterName = null;

    private java.lang.String serviceName = null;

    private java.lang.String hostName = null;

    AlertService(java.lang.String clusterName, java.lang.String serviceName, java.lang.String hostName) {
        this.clusterName = clusterName;
        this.serviceName = serviceName;
        this.hostName = hostName;
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getAlerts(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createResourceInstance(null));
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{alertId}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getAlert(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("alertId")
    java.lang.Long id) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createResourceInstance(id));
    }

    private org.apache.ambari.server.api.resources.ResourceInstance createResourceInstance(java.lang.Long alertId) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, clusterName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Service, serviceName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Host, hostName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Alert, null == alertId ? null : alertId.toString());
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.Alert, mapIds);
    }
}