package org.apache.ambari.server.api.services;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
@io.swagger.annotations.Api(value = "Config Groups", description = "Endpoint for config-group-specific operations")
public class ConfigGroupService extends org.apache.ambari.server.api.services.BaseService {
    private static final java.lang.String CONFIG_GROUP_REQUEST_TYPE = "org.apache.ambari.server.controller.ConfigGroupRequest";

    private java.lang.String m_clusterName;

    public ConfigGroupService(java.lang.String m_clusterName) {
        this.m_clusterName = m_clusterName;
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Returns all config groups", response = org.apache.ambari.server.controller.ConfigGroupResponse.ConfigGroupWrapper.class, responseContainer = org.apache.ambari.server.api.services.BaseService.RESPONSE_CONTAINER_LIST)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = org.apache.ambari.server.api.services.BaseService.QUERY_FILTER_DESCRIPTION, defaultValue = "ConfigGroup/*", dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_SORT, value = org.apache.ambari.server.api.services.BaseService.QUERY_SORT_DESCRIPTION, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_PAGE_SIZE, value = org.apache.ambari.server.api.services.BaseService.QUERY_PAGE_SIZE_DESCRIPTION, defaultValue = org.apache.ambari.server.api.services.BaseService.DEFAULT_PAGE_SIZE, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_INT, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FROM, value = org.apache.ambari.server.api.services.BaseService.QUERY_FROM_DESCRIPTION, allowableValues = org.apache.ambari.server.api.services.BaseService.QUERY_FROM_VALUES, defaultValue = org.apache.ambari.server.api.services.BaseService.DEFAULT_FROM, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_INT, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_TO, value = org.apache.ambari.server.api.services.BaseService.QUERY_TO_DESCRIPTION, allowableValues = org.apache.ambari.server.api.services.BaseService.QUERY_TO_VALUES, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_INT, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_CLUSTER_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_BAD_REQUEST, message = org.apache.ambari.server.api.services.BaseService.MSG_INVALID_ARGUMENTS) })
    public javax.ws.rs.core.Response getConfigGroups(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createConfigGroupResource(m_clusterName, null));
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{groupId}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Returns a single config group", response = org.apache.ambari.server.controller.ConfigGroupResponse.ConfigGroupWrapper.class)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = org.apache.ambari.server.api.services.BaseService.QUERY_FILTER_DESCRIPTION, defaultValue = "ConfigGroup/*", dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_CLUSTER_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_BAD_REQUEST, message = org.apache.ambari.server.api.services.BaseService.MSG_INVALID_ARGUMENTS) })
    public javax.ws.rs.core.Response getConfigGroup(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("groupId")
    java.lang.String groupId) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createConfigGroupResource(m_clusterName, groupId));
    }

    @javax.ws.rs.POST
    @javax.ws.rs.Path("")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation("Creates a config group")
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(dataType = org.apache.ambari.server.api.services.ConfigGroupService.CONFIG_GROUP_REQUEST_TYPE, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_BODY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_CREATED, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_BAD_REQUEST, message = org.apache.ambari.server.api.services.BaseService.MSG_INVALID_ARGUMENTS), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response createConfigGroup(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.POST, createConfigGroupResource(m_clusterName, null));
    }

    @javax.ws.rs.PUT
    @javax.ws.rs.Path("{groupId}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation("Updates a config group")
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(dataType = org.apache.ambari.server.api.services.ConfigGroupService.CONFIG_GROUP_REQUEST_TYPE, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_BODY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_ACCEPTED, message = org.apache.ambari.server.api.services.BaseService.MSG_REQUEST_ACCEPTED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_BAD_REQUEST, message = org.apache.ambari.server.api.services.BaseService.MSG_INVALID_ARGUMENTS), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response updateConfigGroup(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("groupId")
    java.lang.String groupId) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.PUT, createConfigGroupResource(m_clusterName, groupId));
    }

    @javax.ws.rs.DELETE
    @javax.ws.rs.Path("{groupId}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation("Deletes a config group")
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response deleteConfigGroup(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("groupId")
    java.lang.String groupId) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.DELETE, createConfigGroupResource(m_clusterName, groupId));
    }

    org.apache.ambari.server.api.resources.ResourceInstance createConfigGroupResource(java.lang.String clusterName, java.lang.String groupId) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, clusterName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.ConfigGroup, groupId);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.ConfigGroup, mapIds);
    }
}