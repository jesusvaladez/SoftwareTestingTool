package org.apache.ambari.server.api.services;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.http.HttpStatus;
@javax.ws.rs.Path("/stacks/")
@io.swagger.annotations.Api(value = "Stacks", description = "Endpoint for stack specific operations")
public class StacksService extends org.apache.ambari.server.api.services.BaseService {
    @javax.ws.rs.GET
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Get all stacks", nickname = "StacksService#getStacks", notes = "Returns all stacks.", response = org.apache.ambari.server.controller.StackResponse.StackResponseSwagger.class, responseContainer = org.apache.ambari.server.api.services.BaseService.RESPONSE_CONTAINER_LIST)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = "Filter stack details", defaultValue = "Stacks/stack_name", dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_SORT, value = "Sort stack privileges (asc | desc)", defaultValue = "Stacks/stack_name.asc", dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_PAGE_SIZE, value = org.apache.ambari.server.api.services.BaseService.QUERY_PAGE_SIZE_DESCRIPTION, defaultValue = org.apache.ambari.server.api.services.BaseService.DEFAULT_PAGE_SIZE, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_INT, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FROM, value = org.apache.ambari.server.api.services.BaseService.QUERY_FROM_DESCRIPTION, defaultValue = org.apache.ambari.server.api.services.BaseService.DEFAULT_FROM, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_TO, value = org.apache.ambari.server.api.services.BaseService.QUERY_TO_DESCRIPTION, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response getStacks(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createStackResource(null));
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{stackName}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Get a stack", nickname = "StacksService#getStack", notes = "Returns stack details.", response = org.apache.ambari.server.controller.StackResponse.StackResponseSwagger.class, responseContainer = org.apache.ambari.server.api.services.BaseService.RESPONSE_CONTAINER_LIST)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = "Filter stack details", defaultValue = "Stacks/*", dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response getStack(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackName")
    java.lang.String stackName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createStackResource(stackName));
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{stackName}/versions")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Get all versions for a stacks", nickname = "StacksService#getStackVersions", notes = "Returns all versions for a stack.", response = org.apache.ambari.server.controller.StackVersionResponse.StackVersionResponseSwagger.class, responseContainer = org.apache.ambari.server.api.services.BaseService.RESPONSE_CONTAINER_LIST)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = "Filter stack version details", defaultValue = "Versions/stack_name,Versions/stack_version", dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_SORT, value = "Sort stack privileges (asc | desc)", defaultValue = "Versions/stack_name.asc,Versions/stack_version.asc", dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_PAGE_SIZE, value = org.apache.ambari.server.api.services.BaseService.QUERY_PAGE_SIZE_DESCRIPTION, defaultValue = org.apache.ambari.server.api.services.BaseService.DEFAULT_PAGE_SIZE, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_INT, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FROM, value = org.apache.ambari.server.api.services.BaseService.QUERY_FROM_DESCRIPTION, defaultValue = org.apache.ambari.server.api.services.BaseService.DEFAULT_FROM, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_TO, value = org.apache.ambari.server.api.services.BaseService.QUERY_TO_DESCRIPTION, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response getStackVersions(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackName")
    java.lang.String stackName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createStackVersionResource(stackName, null));
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{stackName}/versions/{stackVersion}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Get details for a stack version", nickname = "StacksService#getStackVersion", notes = "Returns the details for a stack version.", response = org.apache.ambari.server.controller.StackVersionResponse.StackVersionResponseSwagger.class)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = "Filter stack version details", defaultValue = "Versions/*", dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response getStackVersion(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackName")
    java.lang.String stackName, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackVersion")
    java.lang.String stackVersion) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createStackVersionResource(stackName, stackVersion));
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{stackName}/versions/{stackVersion}/links")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Get extension links for a stack version", nickname = "StacksService#getStackVersionLinks", notes = "Returns the extension links for a stack version.", response = org.apache.ambari.server.controller.ExtensionLinkResponse.class, responseContainer = org.apache.ambari.server.api.services.BaseService.RESPONSE_CONTAINER_LIST)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = "Filter extension link attributes", defaultValue = "ExtensionLink/link_id," + ((("ExtensionLink/stack_name," + "ExtensionLink/stack_version,") + "ExtensionLink/extension_name,") + "ExtensionLink/extension_version"), dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_SORT, value = "Sort extension links (asc | desc)", defaultValue = "ExtensionLink/link_id.asc," + ((("ExtensionLink/stack_name.asc," + "ExtensionLink/stack_version.asc,") + "ExtensionLink/extension_name.asc,") + "ExtensionLink/extension_version.asc"), dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_PAGE_SIZE, value = org.apache.ambari.server.api.services.BaseService.QUERY_PAGE_SIZE_DESCRIPTION, defaultValue = org.apache.ambari.server.api.services.BaseService.DEFAULT_PAGE_SIZE, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_INT, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FROM, value = org.apache.ambari.server.api.services.BaseService.QUERY_FROM_DESCRIPTION, defaultValue = org.apache.ambari.server.api.services.BaseService.DEFAULT_FROM, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_TO, value = org.apache.ambari.server.api.services.BaseService.QUERY_TO_DESCRIPTION, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response getStackVersionLinks(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackName")
    java.lang.String stackName, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackVersion")
    java.lang.String stackVersion) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createExtensionLinkResource(stackName, stackVersion, null, null));
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{stackName}/versions/{stackVersion}/configurations")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Get all configurations for a stack version", nickname = "StacksService#getStackLevelConfigurations", notes = "Returns all configurations for a stack version.", response = org.apache.ambari.server.controller.StackConfigurationResponse.StackConfigurationResponseSwagger.class, responseContainer = org.apache.ambari.server.api.services.BaseService.RESPONSE_CONTAINER_LIST)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = "Filter returned attributes", defaultValue = "StackLevelConfigurations/stack_name," + ("StackLevelConfigurations/stack_version," + "StackLevelConfigurations/property_name"), dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_SORT, value = "Sort configuration (asc | desc)", defaultValue = "StackLevelConfigurations/stack_name.asc," + ("StackLevelConfigurations/stack_version.asc," + "StackLevelConfigurations/property_name.asc "), dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_PAGE_SIZE, value = org.apache.ambari.server.api.services.BaseService.QUERY_PAGE_SIZE_DESCRIPTION, defaultValue = org.apache.ambari.server.api.services.BaseService.DEFAULT_PAGE_SIZE, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_INT, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FROM, value = org.apache.ambari.server.api.services.BaseService.QUERY_FROM_DESCRIPTION, defaultValue = org.apache.ambari.server.api.services.BaseService.DEFAULT_FROM, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_TO, value = org.apache.ambari.server.api.services.BaseService.QUERY_TO_DESCRIPTION, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response getStackLevelConfigurations(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackName")
    java.lang.String stackName, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackVersion")
    java.lang.String stackVersion) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createStackLevelConfigurationsResource(stackName, stackVersion, null));
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{stackName}/versions/{stackVersion}/configurations/{propertyName}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Get configuration details for a given property", nickname = "StacksService#getStackLevelConfiguration", notes = "Returns the configuration details for a given property.", response = org.apache.ambari.server.controller.StackConfigurationResponse.StackConfigurationResponseSwagger.class)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = "Filter returned attributes", defaultValue = "StackLevelConfigurations/*", dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response getStackLevelConfiguration(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackName")
    java.lang.String stackName, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackVersion")
    java.lang.String stackVersion, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("serviceName")
    java.lang.String serviceName, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("propertyName")
    java.lang.String propertyName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createStackLevelConfigurationsResource(stackName, stackVersion, propertyName));
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{stackName}/versions/{stackVersion}/services")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Get all services for a stack version", nickname = "StacksService#getStackServices", notes = "Returns all services for a stack version.", response = org.apache.ambari.server.controller.StackServiceResponse.StackServiceResponseSwagger.class, responseContainer = org.apache.ambari.server.api.services.BaseService.RESPONSE_CONTAINER_LIST)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = "Filter returned attributes", defaultValue = "StackServices/stack_name," + ("StackServices/stack_version," + "StackServices/service_name"), dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_SORT, value = "Sort stack services (asc | desc)", defaultValue = "StackServices/stack_name.asc," + ("StackServices/stack_version.asc," + "StackServices/service_name.asc"), dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_PAGE_SIZE, value = org.apache.ambari.server.api.services.BaseService.QUERY_PAGE_SIZE_DESCRIPTION, defaultValue = org.apache.ambari.server.api.services.BaseService.DEFAULT_PAGE_SIZE, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_INT, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FROM, value = org.apache.ambari.server.api.services.BaseService.QUERY_FROM_DESCRIPTION, defaultValue = org.apache.ambari.server.api.services.BaseService.DEFAULT_FROM, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_TO, value = org.apache.ambari.server.api.services.BaseService.QUERY_TO_DESCRIPTION, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response getStackServices(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackName")
    java.lang.String stackName, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackVersion")
    java.lang.String stackVersion) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createStackServiceResource(stackName, stackVersion, null));
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{stackName}/versions/{stackVersion}/services/{serviceName}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Get stack service details", nickname = "StacksService#getStackService", notes = "Returns the details of a stack service.", response = org.apache.ambari.server.controller.StackServiceResponse.StackServiceResponseSwagger.class)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = "Filter returned attributes", defaultValue = "StackServices/*", dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response getStackService(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackName")
    java.lang.String stackName, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackVersion")
    java.lang.String stackVersion, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("serviceName")
    java.lang.String serviceName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createStackServiceResource(stackName, stackVersion, serviceName));
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{stackName}/versions/{stackVersion}/artifacts")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Get all stack artifacts", nickname = "StacksService#getStackArtifacts", notes = "Returns all stack artifacts (e.g: kerberos descriptor, metrics descriptor)", response = org.apache.ambari.server.controller.StackArtifactResponse.class, responseContainer = org.apache.ambari.server.api.services.BaseService.RESPONSE_CONTAINER_LIST)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = "Filter returned attributes", defaultValue = "Artifacts/artifact_name," + ("Artifacts/stack_name," + "Artifacts/stack_version"), dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response getStackArtifacts(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackName")
    java.lang.String stackName, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackVersion")
    java.lang.String stackVersion) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createStackArtifactsResource(stackName, stackVersion, null));
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{stackName}/versions/{stackVersion}/artifacts/{artifactName}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Get stack artifact details", nickname = "StacksService#getStackArtifact", notes = "Returns the details of a stack artifact", response = org.apache.ambari.server.controller.StackArtifactResponse.class)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = "Filter returned attributes", defaultValue = "Artifacts/*", dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response getStackArtifact(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("stackName")
    java.lang.String stackName, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackVersion")
    java.lang.String stackVersion, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("artifactName")
    java.lang.String artifactName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createStackArtifactsResource(stackName, stackVersion, artifactName));
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{stackName}/versions/{stackVersion}/services/{serviceName}/artifacts")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Get all artifacts for a stack service", nickname = "StacksService#getStackServiceArtifacts", notes = "Returns all stack service artifacts", response = org.apache.ambari.server.controller.StackServiceArtifactResponse.class, responseContainer = org.apache.ambari.server.api.services.BaseService.RESPONSE_CONTAINER_LIST)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = "Filter returned attributes", defaultValue = "Artifacts/artifact_name," + ("Artifacts/stack_name," + "Artifacts/stack_version"), dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_SORT, value = "Sort service artifacts (asc | desc)", defaultValue = "Artifacts/artifact_name.asc," + ("Artifacts/stack_name.asc," + "Artifacts/stack_version.asc"), dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_PAGE_SIZE, value = org.apache.ambari.server.api.services.BaseService.QUERY_PAGE_SIZE_DESCRIPTION, defaultValue = org.apache.ambari.server.api.services.BaseService.DEFAULT_PAGE_SIZE, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_INT, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FROM, value = org.apache.ambari.server.api.services.BaseService.QUERY_FROM_DESCRIPTION, defaultValue = org.apache.ambari.server.api.services.BaseService.DEFAULT_FROM, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_TO, value = org.apache.ambari.server.api.services.BaseService.QUERY_TO_DESCRIPTION, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response getStackServiceArtifacts(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackName")
    java.lang.String stackName, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackVersion")
    java.lang.String stackVersion, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("serviceName")
    java.lang.String serviceName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createStackServiceArtifactsResource(stackName, stackVersion, serviceName, null));
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{stackName}/versions/{stackVersion}/services/{serviceName}/themes")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Get all themes for a stack service", nickname = "StacksService#getStackServiceThemes", notes = "Returns all stack themes", response = org.apache.ambari.server.controller.ThemeResponse.class, responseContainer = org.apache.ambari.server.api.services.BaseService.RESPONSE_CONTAINER_LIST)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = "Filter returned attributes", defaultValue = "ThemeInfo/file_name," + (("ThemeInfo/service_name," + "ThemeInfo/stack_name,") + "ThemeInfo/stack_version"), dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_SORT, value = "Sort service artifacts (asc | desc)", defaultValue = "ThemeInfo/file_name.asc," + (("ThemeInfo/service_name.asc," + "ThemeInfo/stack_name.asc,") + "ThemeInfo/stack_version.asc"), dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_PAGE_SIZE, value = org.apache.ambari.server.api.services.BaseService.QUERY_PAGE_SIZE_DESCRIPTION, defaultValue = org.apache.ambari.server.api.services.BaseService.DEFAULT_PAGE_SIZE, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_INT, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FROM, value = org.apache.ambari.server.api.services.BaseService.QUERY_FROM_DESCRIPTION, defaultValue = org.apache.ambari.server.api.services.BaseService.DEFAULT_FROM, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_TO, value = org.apache.ambari.server.api.services.BaseService.QUERY_TO_DESCRIPTION, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response getStackServiceThemes(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackName")
    java.lang.String stackName, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackVersion")
    java.lang.String stackVersion, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("serviceName")
    java.lang.String serviceName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createStackServiceThemesResource(stackName, stackVersion, serviceName, null));
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{stackName}/versions/{stackVersion}/services/{serviceName}/themes/{themeName}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Get theme details for a stack service", nickname = "StacksService#getStackServiceTheme", notes = "Returns stack service theme details.", response = org.apache.ambari.server.controller.ThemeResponse.class)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = "Filter returned attributes", defaultValue = "ThemeInfo/*", dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response getStackServiceTheme(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackName")
    java.lang.String stackName, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackVersion")
    java.lang.String stackVersion, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("serviceName")
    java.lang.String serviceName, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("themeName")
    java.lang.String themeName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createStackServiceThemesResource(stackName, stackVersion, serviceName, themeName));
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{stackName}/versions/{stackVersion}/services/{serviceName}/quicklinks")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Get all quicklinks configurations for a stack service", nickname = "StacksService#getStackServiceQuickLinksConfigurations", notes = "Returns all quicklinks configurations for a stack service.", response = org.apache.ambari.server.controller.QuickLinksResponse.class, responseContainer = org.apache.ambari.server.api.services.BaseService.RESPONSE_CONTAINER_LIST)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = "Filter returned attributes", defaultValue = "QuickLinkInfo/file_name," + (("QuickLinkInfo/service_name," + "QuickLinkInfo/stack_name,") + "QuickLinkInfo/stack_version"), dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_SORT, value = "Sort quick links (asc | desc)", defaultValue = "QuickLinkInfo/file_name.asc," + (("QuickLinkInfo/service_name.asc," + "QuickLinkInfo/stack_name.asc,") + "QuickLinkInfo/stack_version.asc"), dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_PAGE_SIZE, value = org.apache.ambari.server.api.services.BaseService.QUERY_PAGE_SIZE_DESCRIPTION, defaultValue = org.apache.ambari.server.api.services.BaseService.DEFAULT_PAGE_SIZE, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_INT, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FROM, value = org.apache.ambari.server.api.services.BaseService.QUERY_FROM_DESCRIPTION, defaultValue = org.apache.ambari.server.api.services.BaseService.DEFAULT_FROM, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_TO, value = org.apache.ambari.server.api.services.BaseService.QUERY_TO_DESCRIPTION, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response getStackServiceQuickLinksConfigurations(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackName")
    java.lang.String stackName, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackVersion")
    java.lang.String stackVersion, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("serviceName")
    java.lang.String serviceName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createStackServiceQuickLinksResource(stackName, stackVersion, serviceName, null));
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{stackName}/versions/{stackVersion}/services/{serviceName}/quicklinks/{quickLinksConfigurationName}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Get quicklinks configuration details", nickname = "StacksService#getStackServiceQuickLinksConfiguration", notes = "Returns the details of a quicklinks configuration.", response = org.apache.ambari.server.controller.QuickLinksResponse.class, responseContainer = org.apache.ambari.server.api.services.BaseService.RESPONSE_CONTAINER_LIST)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = "Filter returned attributes", defaultValue = "QuickLinkInfo/*", dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response getStackServiceQuickLinksConfiguration(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackName")
    java.lang.String stackName, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackVersion")
    java.lang.String stackVersion, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("serviceName")
    java.lang.String serviceName, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("quickLinksConfigurationName")
    java.lang.String quickLinksConfigurationName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createStackServiceQuickLinksResource(stackName, stackVersion, serviceName, quickLinksConfigurationName));
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{stackName}/versions/{stackVersion}/services/{serviceName}/artifacts/{artifactName}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Get stack service artifact details", nickname = "StacksService#getStackServiceArtifact", notes = "Returns the details of a stack service artifact.", response = org.apache.ambari.server.controller.StackArtifactResponse.class)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = "Filter returned attributes", defaultValue = "Artifacts/*", dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response getStackServiceArtifact(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackName")
    java.lang.String stackName, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackVersion")
    java.lang.String stackVersion, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("serviceName")
    java.lang.String serviceName, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("artifactName")
    java.lang.String artifactName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createStackServiceArtifactsResource(stackName, stackVersion, serviceName, artifactName));
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{stackName}/versions/{stackVersion}/services/{serviceName}/configurations")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Get all configurations for a stack service", nickname = "StacksService#getStackConfigurations", notes = "Returns all configurations for a stack service.", response = org.apache.ambari.server.controller.StackConfigurationResponse.StackConfigurationResponseSwagger.class, responseContainer = org.apache.ambari.server.api.services.BaseService.RESPONSE_CONTAINER_LIST)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = "Filter returned attributes", defaultValue = "StackConfigurations/property_name," + (("StackConfigurations/service_name," + "StackConfigurations/stack_name") + "StackConfigurations/stack_version"), dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_SORT, value = "Sort service configurations (asc | desc)", defaultValue = "StackConfigurations/property_name.asc," + (("StackConfigurations/service_name.asc," + "StackConfigurations/stack_name.asc") + "StackConfigurations/stack_version.asc"), dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_PAGE_SIZE, value = org.apache.ambari.server.api.services.BaseService.QUERY_PAGE_SIZE_DESCRIPTION, defaultValue = org.apache.ambari.server.api.services.BaseService.DEFAULT_PAGE_SIZE, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_INT, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FROM, value = org.apache.ambari.server.api.services.BaseService.QUERY_FROM_DESCRIPTION, defaultValue = org.apache.ambari.server.api.services.BaseService.DEFAULT_FROM, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_TO, value = org.apache.ambari.server.api.services.BaseService.QUERY_TO_DESCRIPTION, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response getStackConfigurations(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackName")
    java.lang.String stackName, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackVersion")
    java.lang.String stackVersion, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("serviceName")
    java.lang.String serviceName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createStackConfigurationResource(stackName, stackVersion, serviceName, null));
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{stackName}/versions/{stackVersion}/services/{serviceName}/configurations/{propertyName}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Get stack service configuration details", nickname = "StacksService#getStackConfiguration", notes = "Returns the details of a stack service configuration.", response = org.apache.ambari.server.controller.StackConfigurationResponse.StackConfigurationResponseSwagger.class)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = "Filter returned attributes", defaultValue = "StackConfigurations/*", dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response getStackConfiguration(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackName")
    java.lang.String stackName, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackVersion")
    java.lang.String stackVersion, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("serviceName")
    java.lang.String serviceName, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("propertyName")
    java.lang.String propertyName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createStackConfigurationResource(stackName, stackVersion, serviceName, propertyName));
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{stackName}/versions/{stackVersion}/services/{serviceName}/configurations/{propertyName}/dependencies")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Get all dependencies for a stack service configuration", nickname = "StacksService#getStackConfigurationDependencies", notes = "Returns all dependencies for a stack service configuration.", response = org.apache.ambari.server.controller.StackConfigurationDependencyResponse.StackConfigurationDependencyResponseSwagger.class, responseContainer = org.apache.ambari.server.api.services.BaseService.RESPONSE_CONTAINER_LIST)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = "Filter returned attributes", defaultValue = "StackConfigurationDependency/stack_name," + ((("StackConfigurationDependency/stack_version," + "StackConfigurationDependency/service_name,") + "StackConfigurationDependency/property_name,") + "StackConfigurationDependency/dependency_name"), dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_SORT, value = "Sort configuration dependencies (asc | desc)", defaultValue = "StackConfigurationDependency/stack_name.asc," + ((("StackConfigurationDependency/stack_version.asc," + "StackConfigurationDependency/service_name.asc,") + "StackConfigurationDependency/property_name.asc,") + "StackConfigurationDependency/dependency_name.asc"), dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_PAGE_SIZE, value = org.apache.ambari.server.api.services.BaseService.QUERY_PAGE_SIZE_DESCRIPTION, defaultValue = org.apache.ambari.server.api.services.BaseService.DEFAULT_PAGE_SIZE, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_INT, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FROM, value = org.apache.ambari.server.api.services.BaseService.QUERY_FROM_DESCRIPTION, defaultValue = org.apache.ambari.server.api.services.BaseService.DEFAULT_FROM, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_TO, value = org.apache.ambari.server.api.services.BaseService.QUERY_TO_DESCRIPTION, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response getStackConfigurationDependencies(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackName")
    java.lang.String stackName, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackVersion")
    java.lang.String stackVersion, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("serviceName")
    java.lang.String serviceName, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("propertyName")
    java.lang.String propertyName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createStackConfigurationDependencyResource(stackName, stackVersion, serviceName, propertyName));
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{stackName}/versions/{stackVersion}/services/{serviceName}/components")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Get all components for a stack service", nickname = "StacksService#getServiceComponents", notes = "Returns all components for a stack service.", response = org.apache.ambari.server.controller.StackServiceComponentResponse.StackServiceComponentResponseSwagger.class, responseContainer = org.apache.ambari.server.api.services.BaseService.RESPONSE_CONTAINER_LIST)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = "Filter returned attributes", defaultValue = "StackServiceComponents/component_name," + (("StackServiceComponents/service_name," + "StackServiceComponents/stack_name,") + "StackServiceComponents/stack_version"), dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_SORT, value = "Sort service components (asc | desc)", defaultValue = "StackServiceComponents/component_name.asc," + (("StackServiceComponents/service_name.asc," + "StackServiceComponents/stack_name.asc,") + "StackServiceComponents/stack_version.asc"), dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_PAGE_SIZE, value = org.apache.ambari.server.api.services.BaseService.QUERY_PAGE_SIZE_DESCRIPTION, defaultValue = org.apache.ambari.server.api.services.BaseService.DEFAULT_PAGE_SIZE, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_INT, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FROM, value = org.apache.ambari.server.api.services.BaseService.QUERY_FROM_DESCRIPTION, defaultValue = org.apache.ambari.server.api.services.BaseService.DEFAULT_FROM, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_TO, value = org.apache.ambari.server.api.services.BaseService.QUERY_TO_DESCRIPTION, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response getServiceComponents(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackName")
    java.lang.String stackName, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackVersion")
    java.lang.String stackVersion, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("serviceName")
    java.lang.String serviceName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createStackServiceComponentResource(stackName, stackVersion, serviceName, null));
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{stackName}/versions/{stackVersion}/services/{serviceName}/components/{componentName}/dependencies")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Get all dependencies for a stack service component", nickname = "StacksService#getServiceComponentDependencies", notes = "Returns all dependencies for a stack service component.", response = org.apache.ambari.server.controller.ComponentDependencyResponse.class, responseContainer = org.apache.ambari.server.api.services.BaseService.RESPONSE_CONTAINER_LIST)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = "Filter returned attributes", defaultValue = "Dependencies/stack_name," + ((("Dependencies/stack_version," + "Dependencies/dependent_service_name,") + "Dependencies/dependent_component_name,") + "Dependencies/component_name"), dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_SORT, value = "Sort component dependencies (asc | desc)", defaultValue = "Dependencies/stack_name.asc," + ((("Dependencies/stack_version.asc," + "Dependencies/dependent_service_name.asc,") + "Dependencies/dependent_component_name.asc,") + "Dependencies/component_name.asc"), dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_PAGE_SIZE, value = org.apache.ambari.server.api.services.BaseService.QUERY_PAGE_SIZE_DESCRIPTION, defaultValue = org.apache.ambari.server.api.services.BaseService.DEFAULT_PAGE_SIZE, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_INT, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FROM, value = org.apache.ambari.server.api.services.BaseService.QUERY_FROM_DESCRIPTION, defaultValue = org.apache.ambari.server.api.services.BaseService.DEFAULT_FROM, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_TO, value = org.apache.ambari.server.api.services.BaseService.QUERY_TO_DESCRIPTION, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response getServiceComponentDependencies(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackName")
    java.lang.String stackName, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackVersion")
    java.lang.String stackVersion, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("serviceName")
    java.lang.String serviceName, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("componentName")
    java.lang.String componentName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createStackServiceComponentDependencyResource(stackName, stackVersion, serviceName, componentName, null));
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{stackName}/versions/{stackVersion}/services/{serviceName}/components/{componentName}/dependencies/{dependencyName}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Get a stack service component dependency", nickname = "StacksService#getServiceComponentDependency", notes = "Returns a stack service component dependency.", response = org.apache.ambari.server.controller.ComponentDependencyResponse.class)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = "Filter returned attributes", defaultValue = "Dependencies/*", dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response getServiceComponentDependency(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackName")
    java.lang.String stackName, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackVersion")
    java.lang.String stackVersion, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("serviceName")
    java.lang.String serviceName, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("componentName")
    java.lang.String componentName, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("dependencyName")
    java.lang.String dependencyName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createStackServiceComponentDependencyResource(stackName, stackVersion, serviceName, componentName, dependencyName));
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{stackName}/versions/{stackVersion}/services/{serviceName}/components/{componentName}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Get details for a stack service component", nickname = "StacksService#getServiceComponent", notes = "Returns details for a stack service component.", response = org.apache.ambari.server.controller.StackServiceComponentResponse.StackServiceComponentResponseSwagger.class)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = "Filter returned attributes", defaultValue = "StackServiceComponents/*", dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response getServiceComponent(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackName")
    java.lang.String stackName, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackVersion")
    java.lang.String stackVersion, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("serviceName")
    java.lang.String serviceName, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("componentName")
    java.lang.String componentName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createStackServiceComponentResource(stackName, stackVersion, serviceName, componentName));
    }

    @javax.ws.rs.Path("{stackName}/versions/{stackVersion}/operating_systems")
    public org.apache.ambari.server.api.services.OperatingSystemService getOperatingSystemsHandler(@io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackName")
    java.lang.String stackName, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackVersion")
    java.lang.String stackVersion) {
        final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> stackProperties = new java.util.HashMap<>();
        stackProperties.put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, stackName);
        stackProperties.put(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, stackVersion);
        return new org.apache.ambari.server.api.services.OperatingSystemService(stackProperties);
    }

    @javax.ws.rs.Path("{stackName}/versions/{stackVersion}/repository_versions")
    public org.apache.ambari.server.api.services.RepositoryVersionService getRepositoryVersionHandler(@io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackName")
    java.lang.String stackName, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackVersion")
    java.lang.String stackVersion) {
        final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> stackProperties = new java.util.HashMap<>();
        stackProperties.put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, stackName);
        stackProperties.put(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, stackVersion);
        return new org.apache.ambari.server.api.services.RepositoryVersionService(stackProperties);
    }

    @javax.ws.rs.Path("{stackName}/versions/{stackVersion}/compatible_repository_versions")
    public org.apache.ambari.server.api.services.CompatibleRepositoryVersionService getCompatibleRepositoryVersionHandler(@io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackName")
    java.lang.String stackName, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("stackVersion")
    java.lang.String stackVersion) {
        final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> stackProperties = new java.util.HashMap<>();
        stackProperties.put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, stackName);
        stackProperties.put(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, stackVersion);
        return new org.apache.ambari.server.api.services.CompatibleRepositoryVersionService(stackProperties);
    }

    org.apache.ambari.server.api.resources.ResourceInstance createStackServiceComponentResource(java.lang.String stackName, java.lang.String stackVersion, java.lang.String serviceName, java.lang.String componentName) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, stackName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, stackVersion);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.StackService, serviceName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.StackServiceComponent, componentName);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.StackServiceComponent, mapIds);
    }

    org.apache.ambari.server.api.resources.ResourceInstance createStackServiceComponentDependencyResource(java.lang.String stackName, java.lang.String stackVersion, java.lang.String serviceName, java.lang.String componentName, java.lang.String dependencyName) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, stackName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, stackVersion);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.StackService, serviceName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.StackServiceComponent, componentName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.StackServiceComponentDependency, dependencyName);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.StackServiceComponentDependency, mapIds);
    }

    org.apache.ambari.server.api.resources.ResourceInstance createStackConfigurationResource(java.lang.String stackName, java.lang.String stackVersion, java.lang.String serviceName, java.lang.String propertyName) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, stackName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, stackVersion);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.StackService, serviceName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.StackConfiguration, propertyName);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.StackConfiguration, mapIds);
    }

    org.apache.ambari.server.api.resources.ResourceInstance createStackConfigurationDependencyResource(java.lang.String stackName, java.lang.String stackVersion, java.lang.String serviceName, java.lang.String propertyName) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, stackName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, stackVersion);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.StackService, serviceName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.StackConfiguration, propertyName);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.StackConfigurationDependency, mapIds);
    }

    org.apache.ambari.server.api.resources.ResourceInstance createStackServiceResource(java.lang.String stackName, java.lang.String stackVersion, java.lang.String serviceName) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, stackName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, stackVersion);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.StackService, serviceName);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.StackService, mapIds);
    }

    org.apache.ambari.server.api.resources.ResourceInstance createStackVersionResource(java.lang.String stackName, java.lang.String stackVersion) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, stackName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, stackVersion);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, mapIds);
    }

    org.apache.ambari.server.api.resources.ResourceInstance createStackLevelConfigurationsResource(java.lang.String stackName, java.lang.String stackVersion, java.lang.String propertyName) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, stackName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, stackVersion);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.StackLevelConfiguration, propertyName);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.StackLevelConfiguration, mapIds);
    }

    org.apache.ambari.server.api.resources.ResourceInstance createStackArtifactsResource(java.lang.String stackName, java.lang.String stackVersion, java.lang.String artifactName) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, stackName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, stackVersion);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.StackArtifact, artifactName);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.StackArtifact, mapIds);
    }

    org.apache.ambari.server.api.resources.ResourceInstance createStackServiceArtifactsResource(java.lang.String stackName, java.lang.String stackVersion, java.lang.String serviceName, java.lang.String artifactName) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, stackName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, stackVersion);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.StackService, serviceName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.StackArtifact, artifactName);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.StackArtifact, mapIds);
    }

    org.apache.ambari.server.api.resources.ResourceInstance createStackServiceThemesResource(java.lang.String stackName, java.lang.String stackVersion, java.lang.String serviceName, java.lang.String themeName) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, stackName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, stackVersion);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.StackService, serviceName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Theme, themeName);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.Theme, mapIds);
    }

    org.apache.ambari.server.api.resources.ResourceInstance createStackServiceQuickLinksResource(java.lang.String stackName, java.lang.String stackVersion, java.lang.String serviceName, java.lang.String quickLinksConfigurationName) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, stackName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, stackVersion);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.StackService, serviceName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.QuickLink, quickLinksConfigurationName);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.QuickLink, mapIds);
    }

    org.apache.ambari.server.api.resources.ResourceInstance createExtensionLinkResource(java.lang.String stackName, java.lang.String stackVersion, java.lang.String extensionName, java.lang.String extensionVersion) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, stackName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, stackVersion);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Extension, extensionName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.ExtensionVersion, extensionVersion);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.ExtensionLink, mapIds);
    }

    org.apache.ambari.server.api.resources.ResourceInstance createStackResource(java.lang.String stackName) {
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.Stack, java.util.Collections.singletonMap(org.apache.ambari.server.controller.spi.Resource.Type.Stack, stackName));
    }
}