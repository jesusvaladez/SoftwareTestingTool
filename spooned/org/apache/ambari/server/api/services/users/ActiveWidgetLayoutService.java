package org.apache.ambari.server.api.services.users;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.lang.StringUtils;
@javax.ws.rs.Path("/users/{userName}/activeWidgetLayouts")
@io.swagger.annotations.Api(value = "Users", description = "Endpoint for User specific operations")
public class ActiveWidgetLayoutService extends org.apache.ambari.server.api.services.BaseService {
    @javax.ws.rs.GET
    @javax.ws.rs.Produces("text/plain")
    @io.swagger.annotations.ApiOperation(value = "Get user widget layouts", nickname = "ActiveWidgetLayoutService#getServices", notes = "Returns all active widget layouts for user.", response = org.apache.ambari.server.controller.ActiveWidgetLayoutResponse.class, responseContainer = "List")
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = "fields", value = "Filter user layout details", defaultValue = "WidgetLayoutInfo/*", dataType = "string", paramType = "query"), @io.swagger.annotations.ApiImplicitParam(name = "sortBy", value = "Sort layouts (asc | desc)", defaultValue = "WidgetLayoutInfo/user_name.asc", dataType = "string", paramType = "query"), @io.swagger.annotations.ApiImplicitParam(name = "page_size", value = "The number of resources to be returned for the paged response.", defaultValue = "10", dataType = "integer", paramType = "query"), @io.swagger.annotations.ApiImplicitParam(name = "from", value = "The starting page resource (inclusive). Valid values are :offset | \"start\"", defaultValue = "0", dataType = "string", paramType = "query"), @io.swagger.annotations.ApiImplicitParam(name = "to", value = "The ending page resource (inclusive). Valid values are :offset | \"end\"", dataType = "string", paramType = "query") })
    public javax.ws.rs.core.Response getServices(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam(value = "user name", required = true)
    @javax.ws.rs.PathParam("userName")
    java.lang.String userName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createResource(userName));
    }

    @javax.ws.rs.PUT
    @javax.ws.rs.Produces("text/plain")
    @io.swagger.annotations.ApiOperation(value = "Update user widget layouts", nickname = "ActiveWidgetLayoutService#updateServices", notes = "Updates user widget layout.")
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = "body", value = "input parameters in json form", required = true, dataType = "org.apache.ambari.server.controller.ActiveWidgetLayoutRequest", paramType = "body") })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation"), @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error") })
    public javax.ws.rs.core.Response updateServices(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam(value = "user name", required = true)
    @javax.ws.rs.PathParam("userName")
    java.lang.String userName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.PUT, createResource(userName));
    }

    private org.apache.ambari.server.api.resources.ResourceInstance createResource(java.lang.String userName) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.User, org.apache.commons.lang.StringUtils.lowerCase(userName));
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.ActiveWidgetLayout, mapIds);
    }
}