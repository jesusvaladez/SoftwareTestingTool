package org.apache.ambari.server.api.services.groups;
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
@javax.ws.rs.Path("/groups/{groupName}/privileges")
@io.swagger.annotations.Api(value = "Groups", description = "Endpoint for group specific operations")
public class GroupPrivilegeService extends org.apache.ambari.server.api.services.BaseService {
    @javax.ws.rs.GET
    @javax.ws.rs.Produces("text/plain")
    @io.swagger.annotations.ApiOperation(value = "Get all privileges", nickname = "GroupPrivilegeService#getPrivileges", notes = "Returns all privileges for group.", response = org.apache.ambari.server.controller.GroupPrivilegeResponse.class, responseContainer = "List")
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = "fields", value = "Filter user privileges", defaultValue = "PrivilegeInfo/*", dataType = "string", paramType = "query"), @io.swagger.annotations.ApiImplicitParam(name = "sortBy", value = "Sort user privileges (asc | desc)", defaultValue = "PrivilegeInfo/user_name.asc", dataType = "string", paramType = "query"), @io.swagger.annotations.ApiImplicitParam(name = "page_size", value = "The number of resources to be returned for the paged response.", defaultValue = "10", dataType = "integer", paramType = "query"), @io.swagger.annotations.ApiImplicitParam(name = "from", value = "The starting page resource (inclusive). Valid values are :offset | \"start\"", defaultValue = "0", dataType = "string", paramType = "query"), @io.swagger.annotations.ApiImplicitParam(name = "to", value = "The ending page resource (inclusive). Valid values are :offset | \"end\"", dataType = "string", paramType = "query") })
    public javax.ws.rs.core.Response getPrivileges(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam(value = "group name", required = true)
    @javax.ws.rs.PathParam("groupName")
    java.lang.String groupName) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createPrivilegeResource(groupName, null));
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{privilegeId}")
    @javax.ws.rs.Produces("text/plain")
    @io.swagger.annotations.ApiOperation(value = "Get group privilege", nickname = "GroupPrivilegeService#getPrivilege", notes = "Returns group privilege details.", response = org.apache.ambari.server.controller.GroupPrivilegeResponse.class)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = "fields", value = "Filter group privilege details", defaultValue = "PrivilegeInfo/*", dataType = "string", paramType = "query") })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = 200, message = "Successful operation", response = org.apache.ambari.server.controller.PrivilegeResponse.class) })
    public javax.ws.rs.core.Response getPrivilege(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam(value = "group name", required = true)
    @javax.ws.rs.PathParam("groupName")
    java.lang.String groupName, @io.swagger.annotations.ApiParam(value = "privilege id", required = true)
    @javax.ws.rs.PathParam("privilegeId")
    java.lang.String privilegeId) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createPrivilegeResource(groupName, privilegeId));
    }

    protected org.apache.ambari.server.api.resources.ResourceInstance createPrivilegeResource(java.lang.String groupName, java.lang.String privilegeId) {
        final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Group, groupName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.GroupPrivilege, privilegeId);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.GroupPrivilege, mapIds);
    }
}