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
public class ClusterStackVersionService extends org.apache.ambari.server.api.services.BaseService {
    private java.lang.String clusterName;

    public ClusterStackVersionService(java.lang.String clusterName) {
        this.clusterName = clusterName;
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getClusterStackVersions(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createResource(null));
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{stackVersionId}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getClusterStackVersion(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("stackVersionId")
    java.lang.String stackVersionId) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createResource(stackVersionId));
    }

    @javax.ws.rs.Path("{stackVersionId}/repository_versions")
    public org.apache.ambari.server.api.services.RepositoryVersionService getRepositoryVersionService(@javax.ws.rs.core.Context
    javax.ws.rs.core.Request request, @javax.ws.rs.PathParam("stackVersionId")
    java.lang.String stackVersion) {
        final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, clusterName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.ClusterStackVersion, stackVersion);
        return new org.apache.ambari.server.api.services.RepositoryVersionService(mapIds);
    }

    @javax.ws.rs.POST
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response createRequests(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.POST, createResource(null));
    }

    @javax.ws.rs.PUT
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response updateRequests(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.PUT, createResource(null));
    }

    private org.apache.ambari.server.api.resources.ResourceInstance createResource(java.lang.String stackVersionId) {
        final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, clusterName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.ClusterStackVersion, stackVersionId);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.ClusterStackVersion, mapIds);
    }
}