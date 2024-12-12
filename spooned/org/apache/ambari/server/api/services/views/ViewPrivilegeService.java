package org.apache.ambari.server.api.services.views;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.http.HttpStatus;
@javax.ws.rs.Path("/views/{viewName}/versions/{version}/instances/{instanceName}/privileges")
@io.swagger.annotations.Api(tags = "Views", description = "Endpoint for view specific operations")
public class ViewPrivilegeService extends org.apache.ambari.server.api.services.BaseService {
    public static final java.lang.String PRIVILEGE_INFO_REQUEST_TYPE = "org.apache.ambari.server.controller.ViewPrivilegeResponse.ViewPrivilegeResponseWrapper";

    @javax.ws.rs.GET
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Get all view instance privileges", response = org.apache.ambari.server.controller.ViewPrivilegeResponse.ViewPrivilegeResponseWrapper.class, responseContainer = "List")
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = org.apache.ambari.server.api.services.BaseService.QUERY_FILTER_DESCRIPTION, defaultValue = "PrivilegeInfo/*", dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_SORT, value = org.apache.ambari.server.api.services.BaseService.QUERY_SORT_DESCRIPTION, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_PAGE_SIZE, value = org.apache.ambari.server.api.services.BaseService.QUERY_PAGE_SIZE_DESCRIPTION, defaultValue = org.apache.ambari.server.api.services.BaseService.DEFAULT_PAGE_SIZE, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_INT, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FROM, value = org.apache.ambari.server.api.services.BaseService.QUERY_FROM_DESCRIPTION, allowableValues = org.apache.ambari.server.api.services.BaseService.QUERY_FROM_VALUES, defaultValue = org.apache.ambari.server.api.services.BaseService.DEFAULT_FROM, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_INT, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_TO, value = org.apache.ambari.server.api.services.BaseService.QUERY_TO_DESCRIPTION, allowableValues = org.apache.ambari.server.api.services.BaseService.QUERY_TO_VALUES, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_INT, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_CLUSTER_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_BAD_REQUEST, message = org.apache.ambari.server.api.services.BaseService.MSG_INVALID_ARGUMENTS) })
    public javax.ws.rs.core.Response getPrivileges(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam("view name")
    @javax.ws.rs.PathParam("viewName")
    java.lang.String viewName, @io.swagger.annotations.ApiParam("view version")
    @javax.ws.rs.PathParam("version")
    java.lang.String version, @io.swagger.annotations.ApiParam("instance name")
    @javax.ws.rs.PathParam("instanceName")
    java.lang.String instanceName) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createPrivilegeResource(viewName, version, instanceName, null));
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/{privilegeId}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Get single view instance privilege", response = org.apache.ambari.server.controller.ViewPrivilegeResponse.ViewPrivilegeResponseWrapper.class)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = org.apache.ambari.server.api.services.BaseService.QUERY_FILTER_DESCRIPTION, defaultValue = "PrivilegeInfo/*", dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_CLUSTER_OR_HOST_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_BAD_REQUEST, message = org.apache.ambari.server.api.services.BaseService.MSG_INVALID_ARGUMENTS) })
    public javax.ws.rs.core.Response getPrivilege(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam("view name")
    @javax.ws.rs.PathParam("viewName")
    java.lang.String viewName, @io.swagger.annotations.ApiParam("view version")
    @javax.ws.rs.PathParam("version")
    java.lang.String version, @io.swagger.annotations.ApiParam("instance name")
    @javax.ws.rs.PathParam("instanceName")
    java.lang.String instanceName, @io.swagger.annotations.ApiParam(value = "privilege id", required = true)
    @javax.ws.rs.PathParam("privilegeId")
    java.lang.String privilegeId) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createPrivilegeResource(viewName, version, instanceName, privilegeId));
    }

    @javax.ws.rs.POST
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation("Create view instance privilege")
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(dataType = org.apache.ambari.server.api.services.views.ViewPrivilegeService.PRIVILEGE_INFO_REQUEST_TYPE, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_BODY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_CREATED, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_BAD_REQUEST, message = org.apache.ambari.server.api.services.BaseService.MSG_INVALID_ARGUMENTS), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response createPrivilege(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam("view name")
    @javax.ws.rs.PathParam("viewName")
    java.lang.String viewName, @io.swagger.annotations.ApiParam("view version")
    @javax.ws.rs.PathParam("version")
    java.lang.String version, @io.swagger.annotations.ApiParam("instance name")
    @javax.ws.rs.PathParam("instanceName")
    java.lang.String instanceName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.POST, createPrivilegeResource(viewName, version, instanceName, null));
    }

    @javax.ws.rs.PUT
    @org.apache.ambari.annotations.ApiIgnore
    public javax.ws.rs.core.Response updatePrivilege(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam("view name")
    @javax.ws.rs.PathParam("viewName")
    java.lang.String viewName, @io.swagger.annotations.ApiParam("view version")
    @javax.ws.rs.PathParam("version")
    java.lang.String version, @io.swagger.annotations.ApiParam("instance name")
    @javax.ws.rs.PathParam("instanceName")
    java.lang.String instanceName, @io.swagger.annotations.ApiParam("privilege id")
    @javax.ws.rs.PathParam("privilegeId")
    java.lang.String privilegeId) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.PUT, createPrivilegeResource(viewName, version, instanceName, privilegeId));
    }

    @javax.ws.rs.PUT
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation("Update view instance privilege")
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(dataType = org.apache.ambari.server.api.services.views.ViewPrivilegeService.PRIVILEGE_INFO_REQUEST_TYPE, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_BODY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_ACCEPTED, message = org.apache.ambari.server.api.services.BaseService.MSG_REQUEST_ACCEPTED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_BAD_REQUEST, message = org.apache.ambari.server.api.services.BaseService.MSG_INVALID_ARGUMENTS), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_CLUSTER_OR_HOST_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response updatePrivileges(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam("view name")
    @javax.ws.rs.PathParam("viewName")
    java.lang.String viewName, @io.swagger.annotations.ApiParam("view version")
    @javax.ws.rs.PathParam("version")
    java.lang.String version, @io.swagger.annotations.ApiParam("instance name")
    @javax.ws.rs.PathParam("instanceName")
    java.lang.String instanceName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.PUT, createPrivilegeResource(viewName, version, instanceName, null));
    }

    @javax.ws.rs.DELETE
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation("Delete view instance privileges")
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_CLUSTER_OR_HOST_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response deletePrivileges(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam("view name")
    @javax.ws.rs.PathParam("viewName")
    java.lang.String viewName, @io.swagger.annotations.ApiParam("view version")
    @javax.ws.rs.PathParam("viewVersion")
    java.lang.String version, @io.swagger.annotations.ApiParam("instance name")
    @javax.ws.rs.PathParam("instanceName")
    java.lang.String instanceName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.DELETE, createPrivilegeResource(viewName, version, instanceName, null));
    }

    @javax.ws.rs.DELETE
    @javax.ws.rs.Path("{privilegeId}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation("Delete privileges")
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_CLUSTER_OR_HOST_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response deletePrivilege(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam("view name")
    @javax.ws.rs.PathParam("viewName")
    java.lang.String viewName, @io.swagger.annotations.ApiParam("view version")
    @javax.ws.rs.PathParam("version")
    java.lang.String version, @io.swagger.annotations.ApiParam("instance name")
    @javax.ws.rs.PathParam("instanceName")
    java.lang.String instanceName, @io.swagger.annotations.ApiParam("privilege id")
    @javax.ws.rs.PathParam("privilegeId")
    java.lang.String privilegeId) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.DELETE, createPrivilegeResource(viewName, version, instanceName, privilegeId));
    }

    protected org.apache.ambari.server.api.resources.ResourceInstance createPrivilegeResource(java.lang.String viewName, java.lang.String viewVersion, java.lang.String instanceName, java.lang.String privilegeId) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.View, viewName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.ViewVersion, viewVersion);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.ViewInstance, instanceName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.ViewPrivilege, privilegeId);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.ViewPrivilege, mapIds);
    }
}