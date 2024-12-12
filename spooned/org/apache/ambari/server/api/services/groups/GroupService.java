package org.apache.ambari.server.api.services.groups;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
@javax.ws.rs.Path("/groups/")
@io.swagger.annotations.Api(value = "Groups", description = "Endpoint for group specific operations")
public class GroupService extends org.apache.ambari.server.api.services.BaseService {
    @javax.ws.rs.GET
    @javax.ws.rs.Produces("text/plain")
    @io.swagger.annotations.ApiOperation(value = "Get all groups", nickname = "GroupService#getGroups", notes = "Returns details of all groups.", response = org.apache.ambari.server.controller.GroupResponse.class, responseContainer = "List")
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = "fields", value = "Filter group details", defaultValue = "Groups/*", dataType = "string", paramType = "query"), @io.swagger.annotations.ApiImplicitParam(name = "sortBy", value = "Sort groups (asc | desc)", defaultValue = "Groups/group_name.asc", dataType = "string", paramType = "query"), @io.swagger.annotations.ApiImplicitParam(name = "page_size", value = "The number of resources to be returned for the paged response.", defaultValue = "10", dataType = "integer", paramType = "query"), @io.swagger.annotations.ApiImplicitParam(name = "from", value = "The starting page resource (inclusive). Valid values are :offset | \"start\"", defaultValue = "0", dataType = "string", paramType = "query"), @io.swagger.annotations.ApiImplicitParam(name = "to", value = "The ending page resource (inclusive). Valid values are :offset | \"end\"", dataType = "string", paramType = "query") })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = 200, message = "Successful retrieval of all group entries", response = org.apache.ambari.server.controller.GroupResponse.class, responseContainer = "List") })
    public javax.ws.rs.core.Response getGroups(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createGroupResource(null));
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{groupName}")
    @javax.ws.rs.Produces("text/plain")
    @io.swagger.annotations.ApiOperation(value = "Get group", nickname = "GroupService#getGroup", notes = "Returns group details.", response = org.apache.ambari.server.controller.GroupResponse.class)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = "fields", value = "Filter group details", defaultValue = "Groups", dataType = "string", paramType = "query") })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = 200, message = "Successful retrieval of group resource", response = org.apache.ambari.server.controller.GroupResponse.class) })
    public javax.ws.rs.core.Response getGroup(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam(value = "group name", required = true)
    @javax.ws.rs.PathParam("groupName")
    java.lang.String groupName) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createGroupResource(groupName));
    }

    @javax.ws.rs.POST
    @javax.ws.rs.Produces("text/plain")
    @io.swagger.annotations.ApiOperation(value = "Create new group", nickname = "GroupService#createGroup", notes = "Creates group resource.")
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = "body", value = "input parameters in json form", required = true, dataType = "org.apache.ambari.server.controller.GroupRequest", paramType = "body") })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = 200, message = "successful operation"), @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error") })
    public javax.ws.rs.core.Response createGroup(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.POST, createGroupResource(null));
    }

    @javax.ws.rs.POST
    @org.apache.ambari.annotations.ApiIgnore
    @java.lang.Deprecated
    @javax.ws.rs.Path("{groupName}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response createGroup(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("groupName")
    java.lang.String groupName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.POST, createGroupResource(groupName));
    }

    @javax.ws.rs.DELETE
    @javax.ws.rs.Path("{groupName}")
    @javax.ws.rs.Produces("text/plain")
    @io.swagger.annotations.ApiOperation(value = "Delete group", nickname = "GroupService#deleteGroup", notes = "Delete group resource.")
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation"), @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error") })
    public javax.ws.rs.core.Response deleteGroup(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam(value = "group name", required = true)
    @javax.ws.rs.PathParam("groupName")
    java.lang.String groupName) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.DELETE, createGroupResource(groupName));
    }

    private org.apache.ambari.server.api.resources.ResourceInstance createGroupResource(java.lang.String groupName) {
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.Group, java.util.Collections.singletonMap(org.apache.ambari.server.controller.spi.Resource.Type.Group, groupName));
    }
}