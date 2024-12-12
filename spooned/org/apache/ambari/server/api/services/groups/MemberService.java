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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
@javax.ws.rs.Path("/groups/{groupName}/members")
@io.swagger.annotations.Api(value = "Groups", description = "Endpoint for group specific operations")
public class MemberService extends org.apache.ambari.server.api.services.BaseService {
    @javax.ws.rs.POST
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response createMember(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("groupName")
    java.lang.String groupName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.POST, createMemberResource(groupName, null));
    }

    @javax.ws.rs.POST
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{userName}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response createMember(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("groupName")
    java.lang.String groupName, @javax.ws.rs.PathParam("userName")
    java.lang.String userName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.POST, createMemberResource(groupName, userName));
    }

    @javax.ws.rs.DELETE
    @javax.ws.rs.Path("{userName}")
    @javax.ws.rs.Produces("text/plain")
    @io.swagger.annotations.ApiOperation(value = "Delete group member", nickname = "MemberService#deleteMember", notes = "Delete member resource.")
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation"), @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error") })
    public javax.ws.rs.core.Response deleteMember(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam(value = "group name", required = true)
    @javax.ws.rs.PathParam("groupName")
    java.lang.String groupName, @io.swagger.annotations.ApiParam(value = "user name", required = true)
    @javax.ws.rs.PathParam("userName")
    java.lang.String userName) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.DELETE, createMemberResource(groupName, userName));
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Produces("text/plain")
    @io.swagger.annotations.ApiOperation(value = "Get all group members", nickname = "MemberService#getMembers", notes = "Returns details of all members.", response = org.apache.ambari.server.controller.MemberResponse.class, responseContainer = "List")
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = "fields", value = "Filter member details", defaultValue = "MemberInfo/*", dataType = "string", paramType = "query"), @io.swagger.annotations.ApiImplicitParam(name = "sortBy", value = "Sort members (asc | desc)", defaultValue = "MemberInfo/user_name.asc", dataType = "string", paramType = "query"), @io.swagger.annotations.ApiImplicitParam(name = "page_size", value = "The number of resources to be returned for the paged response.", defaultValue = "10", dataType = "integer", paramType = "query"), @io.swagger.annotations.ApiImplicitParam(name = "from", value = "The starting page resource (inclusive). Valid values are :offset | \"start\"", defaultValue = "0", dataType = "string", paramType = "query"), @io.swagger.annotations.ApiImplicitParam(name = "to", value = "The ending page resource (inclusive). Valid values are :offset | \"end\"", dataType = "string", paramType = "query") })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = org.apache.ambari.server.controller.MemberResponse.class, responseContainer = "List") })
    public javax.ws.rs.core.Response getMembers(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam(value = "group name", required = true)
    @javax.ws.rs.PathParam("groupName")
    java.lang.String groupName) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createMemberResource(groupName, null));
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{userName}")
    @javax.ws.rs.Produces("text/plain")
    @io.swagger.annotations.ApiOperation(value = "Get group member", nickname = "MemberService#getMember", notes = "Returns member details.", response = org.apache.ambari.server.controller.MemberResponse.class)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = "fields", value = "Filter member details", defaultValue = "MemberInfo", dataType = "string", paramType = "query") })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = org.apache.ambari.server.controller.MemberResponse.class) })
    public javax.ws.rs.core.Response getMember(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam(value = "group name", required = true)
    @javax.ws.rs.PathParam("groupName")
    java.lang.String groupName, @io.swagger.annotations.ApiParam(value = "user name", required = true)
    @javax.ws.rs.PathParam("userName")
    java.lang.String userName) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createMemberResource(groupName, userName));
    }

    @javax.ws.rs.PUT
    @javax.ws.rs.Produces("text/plain")
    @io.swagger.annotations.ApiOperation(value = "Update group members", nickname = "MemberService#updateMembers", notes = "Updates group member resources.", responseContainer = "List")
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = "body", value = "input parameters in json form", required = true, dataType = "org.apache.ambari.server.controller.MemberRequest", paramType = "body") })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation"), @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error") })
    public javax.ws.rs.core.Response updateMembers(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam(value = "group name", required = true)
    @javax.ws.rs.PathParam("groupName")
    java.lang.String groupName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.PUT, createMemberResource(groupName, null));
    }

    private org.apache.ambari.server.api.resources.ResourceInstance createMemberResource(java.lang.String groupName, java.lang.String userName) {
        final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Group, groupName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Member, userName);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.Member, mapIds);
    }
}