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
public class RepositoryVersionService extends org.apache.ambari.server.api.services.BaseService {
    private java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> parentKeyProperties;

    public RepositoryVersionService(java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> parentKeyProperties) {
        this.parentKeyProperties = parentKeyProperties;
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getRepositoryVersions(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createResource(null));
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{repositoryVersionId}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getRepositoryVersion(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("repositoryVersionId")
    java.lang.String repositoryVersionId) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createResource(repositoryVersionId));
    }

    @javax.ws.rs.POST
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response createRepositoryVersion(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.POST, createResource(null));
    }

    @javax.ws.rs.DELETE
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{repositoryVersionId}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response deleteRepositoryVersion(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("repositoryVersionId")
    java.lang.String repositoryVersionId) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.DELETE, createResource(repositoryVersionId));
    }

    @javax.ws.rs.PUT
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{repositoryVersionId}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response updateRepositoryVersion(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("repositoryVersionId")
    java.lang.String repositoryVersionId) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.PUT, createResource(repositoryVersionId));
    }

    @javax.ws.rs.Path("{repositoryVersionId}/operating_systems")
    public org.apache.ambari.server.api.services.OperatingSystemService getOperatingSystemsHandler(@javax.ws.rs.PathParam("repositoryVersionId")
    java.lang.String repositoryVersionId) {
        final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.putAll(parentKeyProperties);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.RepositoryVersion, repositoryVersionId);
        return new org.apache.ambari.server.api.services.OperatingSystemService(mapIds);
    }

    private org.apache.ambari.server.api.resources.ResourceInstance createResource(java.lang.String repositoryVersionId) {
        final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.putAll(parentKeyProperties);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.RepositoryVersion, repositoryVersionId);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.RepositoryVersion, mapIds);
    }
}