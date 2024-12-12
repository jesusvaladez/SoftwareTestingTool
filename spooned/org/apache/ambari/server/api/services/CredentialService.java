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
public class CredentialService extends org.apache.ambari.server.api.services.BaseService {
    private final java.lang.String clusterName;

    public CredentialService(java.lang.String clusterName) {
        this.clusterName = clusterName;
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getCredentials(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createCredentialResource(null));
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{alias}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getCredential(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("alias")
    java.lang.String alias) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createCredentialResource(alias));
    }

    @javax.ws.rs.POST
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{alias}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response createCredential(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("alias")
    java.lang.String alias) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.POST, createCredentialResource(alias));
    }

    @javax.ws.rs.PUT
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{alias}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response updateCredential(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("alias")
    java.lang.String alias) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.PUT, createCredentialResource(alias));
    }

    @javax.ws.rs.DELETE
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{alias}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response deleteCredential(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("alias")
    java.lang.String alias) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.DELETE, createCredentialResource(alias));
    }

    org.apache.ambari.server.api.resources.ResourceInstance createCredentialResource(java.lang.String alias) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, this.clusterName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Credential, alias);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.Credential, mapIds);
    }
}