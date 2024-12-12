package org.apache.ambari.server.api.services;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
@javax.ws.rs.Path("/authorizations/")
public class RoleAuthorizationService extends org.apache.ambari.server.api.services.BaseService {
    private java.lang.String permissionId;

    public RoleAuthorizationService() {
        this(null);
    }

    public RoleAuthorizationService(java.lang.String permissionId) {
        this.permissionId = permissionId;
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getAuthorizations(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createAuthorizationResource(null));
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{authorization_id}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getAuthorization(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("authorization_id")
    java.lang.String authorizationId) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createAuthorizationResource(authorizationId));
    }

    protected org.apache.ambari.server.api.resources.ResourceInstance createAuthorizationResource(java.lang.String authorizationId) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Permission, permissionId);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.RoleAuthorization, authorizationId);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.RoleAuthorization, mapIds);
    }
}