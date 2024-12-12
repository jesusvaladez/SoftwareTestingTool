package org.apache.ambari.server.api.services.users;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.http.HttpStatus;
@javax.ws.rs.Path("/auth")
@io.swagger.annotations.Api(value = "Auth", description = "Endpoint for authentication operations")
public class AuthService extends org.apache.ambari.server.api.services.BaseService {
    private static final java.lang.String AUTH_USERS_REQUEST_TYPE = "org.apache.ambari.server.controller.AuthRequestCreateAuthSwagger";

    @javax.ws.rs.POST
    @javax.ws.rs.Produces("text/plain")
    @io.swagger.annotations.ApiOperation("User authorization request")
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(dataType = org.apache.ambari.server.api.services.users.AuthService.AUTH_USERS_REQUEST_TYPE, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_BODY, allowMultiple = true) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_BAD_REQUEST, message = org.apache.ambari.server.api.services.BaseService.MSG_INVALID_ARGUMENTS), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response getUsersViaPost(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.POST, createAuthResource());
    }

    private org.apache.ambari.server.api.resources.ResourceInstance createAuthResource() {
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.Auth, java.util.Collections.EMPTY_MAP);
    }
}