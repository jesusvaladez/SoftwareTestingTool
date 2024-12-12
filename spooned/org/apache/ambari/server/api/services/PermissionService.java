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
@javax.ws.rs.Path("/permissions/")
public class PermissionService extends org.apache.ambari.server.api.services.BaseService {
    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{permissionId}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getPermission(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("permissionId")
    java.lang.String permissionId) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createPermissionResource(permissionId));
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getPermissions(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createPermissionResource(null));
    }

    @javax.ws.rs.POST
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{permissionId}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response createPermission(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("permissionId")
    java.lang.String permissionId) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.POST, createPermissionResource(permissionId));
    }

    @javax.ws.rs.PUT
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{permissionId}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response updatePermission(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("permissionId")
    java.lang.String permissionId) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.PUT, createPermissionResource(permissionId));
    }

    @javax.ws.rs.DELETE
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{permissionId}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response deletePermission(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("permissionId")
    java.lang.String permissionId) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.DELETE, createPermissionResource(permissionId));
    }

    @javax.ws.rs.Path("{permissionId}/authorizations")
    public org.apache.ambari.server.api.services.RoleAuthorizationService getRoleAuthorizations(@javax.ws.rs.core.Context
    javax.ws.rs.core.Request request, @javax.ws.rs.PathParam("permissionId")
    java.lang.String permissionId) {
        return new org.apache.ambari.server.api.services.RoleAuthorizationService(permissionId);
    }

    protected org.apache.ambari.server.api.resources.ResourceInstance createPermissionResource(java.lang.String permissionId) {
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.Permission, java.util.Collections.singletonMap(org.apache.ambari.server.controller.spi.Resource.Type.Permission, permissionId));
    }
}