package org.apache.ambari.server.api.services.users;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.lang.StringUtils;
@javax.ws.rs.Path("/users/{userName}/authorizations")
@io.swagger.annotations.Api(value = "Users", description = "Endpoint for user specific operations")
public class UserAuthorizationService extends org.apache.ambari.server.api.services.BaseService {
    @javax.ws.rs.GET
    @javax.ws.rs.Produces("text/plain")
    @io.swagger.annotations.ApiOperation(value = "Get all authorizations", nickname = "UserAuthorizationService#getAuthorizations", notes = "Returns all authorization for user.", response = org.apache.ambari.server.controller.UserAuthorizationResponse.class, responseContainer = "List")
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = "fields", value = "Filter user authorization details", defaultValue = "AuthorizationInfo/*", dataType = "string", paramType = "query"), @io.swagger.annotations.ApiImplicitParam(name = "sortBy", value = "Sort user authorizations (asc | desc)", defaultValue = "AuthorizationInfo/user_name.asc", dataType = "string", paramType = "query"), @io.swagger.annotations.ApiImplicitParam(name = "page_size", value = "The number of resources to be returned for the paged response.", defaultValue = "10", dataType = "integer", paramType = "query"), @io.swagger.annotations.ApiImplicitParam(name = "from", value = "The starting page resource (inclusive). Valid values are :offset | \"start\"", defaultValue = "0", dataType = "string", paramType = "query"), @io.swagger.annotations.ApiImplicitParam(name = "to", value = "The ending page resource (inclusive). Valid values are :offset | \"end\"", dataType = "string", paramType = "query") })
    public javax.ws.rs.core.Response getAuthorizations(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam(value = "user name", required = true)
    @javax.ws.rs.PathParam("userName")
    java.lang.String userName) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createAuthorizationResource(userName, null));
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{authorization_id}")
    @javax.ws.rs.Produces("text/plain")
    @io.swagger.annotations.ApiOperation(value = "Get user authorization", nickname = "UserAuthorizationService#getAuthorization", notes = "Returns user authorization details.", response = org.apache.ambari.server.controller.UserAuthorizationResponse.class)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = "fields", value = "Filter user authorization details", defaultValue = "AuthorizationInfo/*", dataType = "string", paramType = "query") })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = org.apache.ambari.server.controller.UserAuthorizationResponse.class) })
    public javax.ws.rs.core.Response getAuthorization(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam(value = "user name", required = true)
    @javax.ws.rs.PathParam("userName")
    java.lang.String userName, @io.swagger.annotations.ApiParam(value = "Authorization Id", required = true)
    @javax.ws.rs.PathParam("authorization_id")
    java.lang.String authorizationId) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createAuthorizationResource(userName, authorizationId));
    }

    protected org.apache.ambari.server.api.resources.ResourceInstance createAuthorizationResource(java.lang.String userName, java.lang.String authorizationId) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.User, org.apache.commons.lang.StringUtils.lowerCase(userName));
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.UserAuthorization, authorizationId);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.UserAuthorization, mapIds);
    }
}