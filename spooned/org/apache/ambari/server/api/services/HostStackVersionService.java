package org.apache.ambari.server.api.services;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
public class HostStackVersionService extends org.apache.ambari.server.api.services.BaseService {
    private java.lang.String hostName;

    private java.lang.String clusterName;

    public HostStackVersionService(java.lang.String hostName, java.lang.String clusterName) {
        this.hostName = hostName;
        this.clusterName = clusterName;
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getHostStackVersions(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createResource(ui, clusterName, hostName, null));
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{stackVersionId}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getHostStackVersion(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("stackVersionId")
    java.lang.String stackVersionId) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createResource(ui, clusterName, hostName, stackVersionId));
    }

    @javax.ws.rs.POST
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response createRequests(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.POST, createResource(ui, clusterName, hostName, null));
    }

    @javax.ws.rs.Path("{stackVersionId}/repository_versions")
    public org.apache.ambari.server.api.services.RepositoryVersionService getRepositoryVersionHandler(@javax.ws.rs.PathParam("stackVersionId")
    java.lang.String stackVersionId) {
        final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> stackVersionProperties = new java.util.HashMap<>();
        stackVersionProperties.put(org.apache.ambari.server.controller.spi.Resource.Type.Host, hostName);
        stackVersionProperties.put(org.apache.ambari.server.controller.spi.Resource.Type.HostStackVersion, stackVersionId);
        return new org.apache.ambari.server.api.services.RepositoryVersionService(stackVersionProperties);
    }

    private org.apache.ambari.server.api.resources.ResourceInstance createResource(javax.ws.rs.core.UriInfo ui, java.lang.String clusterName, java.lang.String hostName, java.lang.String stackVersionId) {
        final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        if (clusterName != null) {
            mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, clusterName);
        }
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Host, hostName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.HostStackVersion, stackVersionId);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.HostStackVersion, mapIds);
    }
}