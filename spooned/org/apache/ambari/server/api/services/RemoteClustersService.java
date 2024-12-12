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
@javax.ws.rs.Path("/remoteclusters")
public class RemoteClustersService extends org.apache.ambari.server.api.services.BaseService {
    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getRemoteClusters(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createRemoteClusterResource(null));
    }

    @javax.ws.rs.POST
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{clusterName}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response createRemoteCluster(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.POST, createRemoteClusterResource(clusterName));
    }

    @javax.ws.rs.PUT
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{clusterName}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response updateRemoteCluster(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.PUT, createRemoteClusterResource(clusterName));
    }

    @javax.ws.rs.DELETE
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{clusterName}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response deleteRemoteCluster(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.DELETE, createRemoteClusterResource(clusterName));
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{clusterName}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getRemoteCluster(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createRemoteClusterResource(clusterName));
    }

    private org.apache.ambari.server.api.resources.ResourceInstance createRemoteClusterResource(java.lang.String clusterName) {
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.RemoteCluster, java.util.Collections.singletonMap(org.apache.ambari.server.controller.spi.Resource.Type.RemoteCluster, clusterName));
    }
}