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
@javax.ws.rs.Path("/targets/")
public class TargetClusterService extends org.apache.ambari.server.api.services.BaseService {
    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{targetName}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getTargetCluster(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("targetName")
    java.lang.String targetName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createTargetClusterResource(targetName));
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getTargetClusters(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createTargetClusterResource(null));
    }

    @javax.ws.rs.POST
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{targetName}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response createTargetCluster(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("targetName")
    java.lang.String targetName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.POST, createTargetClusterResource(targetName));
    }

    @javax.ws.rs.PUT
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{targetName}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response updateTargetCluster(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("targetName")
    java.lang.String targetName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.PUT, createTargetClusterResource(targetName));
    }

    @javax.ws.rs.DELETE
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{targetName}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response deleteTargetCluster(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("targetName")
    java.lang.String targetName) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.DELETE, createTargetClusterResource(targetName));
    }

    org.apache.ambari.server.api.resources.ResourceInstance createTargetClusterResource(java.lang.String targetName) {
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.DRTargetCluster, java.util.Collections.singletonMap(org.apache.ambari.server.controller.spi.Resource.Type.DRTargetCluster, targetName));
    }
}