package org.apache.ambari.server.api.services;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiModelProperty;
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
@javax.ws.rs.Path("/services")
@io.swagger.annotations.Api(value = "Services", description = "Endpoint for querying root-level services, ie. Ambari Server and Ambari Agents")
public class RootServiceService extends org.apache.ambari.server.api.services.BaseService {
    private static final java.lang.String KEY_COMPONENTS = "components";

    private static final java.lang.String KEY_HOST_COMPONENTS = "hostComponents";

    private static final java.lang.String DEFAULT_FIELDS_ROOT_SERVICES = org.apache.ambari.server.controller.internal.RootServiceResourceProvider.SERVICE_NAME_PROPERTY_ID;

    private static final java.lang.String DEFAULT_FIELDS_ROOT_SERVICE = (((((((org.apache.ambari.server.controller.internal.RootServiceResourceProvider.SERVICE_NAME_PROPERTY_ID + org.apache.ambari.server.api.services.BaseService.FIELDS_SEPARATOR) + org.apache.ambari.server.api.services.RootServiceService.KEY_COMPONENTS) + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.COMPONENT_NAME_PROPERTY_ID) + org.apache.ambari.server.api.services.BaseService.FIELDS_SEPARATOR) + org.apache.ambari.server.api.services.RootServiceService.KEY_COMPONENTS) + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.SERVICE_NAME_PROPERTY_ID;

    private static final java.lang.String DEFAULT_FIELDS_ROOT_SERVICE_COMPONENTS = (org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.COMPONENT_NAME_PROPERTY_ID + org.apache.ambari.server.api.services.BaseService.FIELDS_SEPARATOR) + org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.SERVICE_NAME_PROPERTY_ID;

    private static final java.lang.String DEFAULT_FIELDS_ROOT_SERVICE_COMPONENT = (((((((((((org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.ALL_PROPERTIES + org.apache.ambari.server.api.services.BaseService.FIELDS_SEPARATOR) + org.apache.ambari.server.api.services.RootServiceService.KEY_HOST_COMPONENTS) + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.COMPONENT_NAME_PROPERTY_ID) + org.apache.ambari.server.api.services.BaseService.FIELDS_SEPARATOR) + org.apache.ambari.server.api.services.RootServiceService.KEY_HOST_COMPONENTS) + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.HOST_NAME_PROPERTY_ID) + org.apache.ambari.server.api.services.BaseService.FIELDS_SEPARATOR) + org.apache.ambari.server.api.services.RootServiceService.KEY_HOST_COMPONENTS) + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.SERVICE_NAME_PROPERTY_ID;

    private static final java.lang.String DEFAULT_FIELDS_ROOT_SERVICE_HOST_COMPONENT = (((org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.COMPONENT_NAME_PROPERTY_ID + org.apache.ambari.server.api.services.BaseService.FIELDS_SEPARATOR) + org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.HOST_NAME_PROPERTY_ID) + org.apache.ambari.server.api.services.BaseService.FIELDS_SEPARATOR) + org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.SERVICE_NAME_PROPERTY_ID;

    private static final java.lang.String DEFAULT_FIELDS_HOSTS = org.apache.ambari.server.controller.internal.HostResourceProvider.HOST_HOST_NAME_PROPERTY_ID;

    private static final java.lang.String DEFAULT_FIELDS_HOST = org.apache.ambari.server.controller.internal.HostResourceProvider.ALL_PROPERTIES;

    @javax.ws.rs.GET
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Returns the list of root-level services", response = org.apache.ambari.server.api.services.RootServiceService.RootServiceResponseWrapper.class, responseContainer = org.apache.ambari.server.api.services.BaseService.RESPONSE_CONTAINER_LIST)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = org.apache.ambari.server.api.services.BaseService.QUERY_FILTER_DESCRIPTION, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY, defaultValue = org.apache.ambari.server.api.services.RootServiceService.DEFAULT_FIELDS_ROOT_SERVICES) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response getRootServices(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        org.apache.ambari.server.api.resources.ResourceInstance resource = createServiceResource(null);
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, resource);
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{serviceName}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Returns information about the given root-level service, including a list of its components", response = org.apache.ambari.server.api.services.RootServiceService.RootServiceResponseWithComponentList.class)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = org.apache.ambari.server.api.services.BaseService.QUERY_FILTER_DESCRIPTION, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY, defaultValue = org.apache.ambari.server.api.services.RootServiceService.DEFAULT_FIELDS_ROOT_SERVICE) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response getRootService(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam(value = "service name", required = true)
    @javax.ws.rs.PathParam("serviceName")
    java.lang.String serviceName) {
        org.apache.ambari.server.api.resources.ResourceInstance resource = createServiceResource(serviceName);
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, resource);
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{serviceName}/hosts")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Returns the list of hosts for the given root-level service", response = org.apache.ambari.server.controller.HostResponse.HostResponseWrapper.class, responseContainer = org.apache.ambari.server.api.services.BaseService.RESPONSE_CONTAINER_LIST)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = org.apache.ambari.server.api.services.BaseService.QUERY_FILTER_DESCRIPTION, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY, defaultValue = org.apache.ambari.server.api.services.RootServiceService.DEFAULT_FIELDS_HOSTS) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response getRootHosts(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        org.apache.ambari.server.api.resources.ResourceInstance resource = createHostResource(null);
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, resource);
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{serviceName}/hosts/{hostName}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Returns information about the given host", response = org.apache.ambari.server.controller.HostResponse.HostResponseWrapper.class)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = org.apache.ambari.server.api.services.BaseService.QUERY_FILTER_DESCRIPTION, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY, defaultValue = org.apache.ambari.server.api.services.RootServiceService.DEFAULT_FIELDS_HOST) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response getRootHost(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam(value = "host name", required = true)
    @javax.ws.rs.PathParam("hostName")
    java.lang.String hostName) {
        org.apache.ambari.server.api.resources.ResourceInstance resource = createHostResource(hostName);
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, resource);
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{serviceName}/hosts/{hostName}/hostComponents")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Returns the list of components for the given root-level service on the given host", response = org.apache.ambari.server.api.services.RootServiceService.RootServiceHostComponentResponseWrapper.class, responseContainer = org.apache.ambari.server.api.services.BaseService.RESPONSE_CONTAINER_LIST)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = org.apache.ambari.server.api.services.BaseService.QUERY_FILTER_DESCRIPTION, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY, defaultValue = org.apache.ambari.server.api.services.RootServiceService.DEFAULT_FIELDS_ROOT_SERVICE_HOST_COMPONENT) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response getRootServiceHostComponents(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam(value = "service name", required = true)
    @javax.ws.rs.PathParam("serviceName")
    java.lang.String serviceName, @io.swagger.annotations.ApiParam(value = "host name", required = true)
    @javax.ws.rs.PathParam("hostName")
    java.lang.String hostName) {
        org.apache.ambari.server.api.resources.ResourceInstance resource = createHostComponentResource(serviceName, hostName, null);
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, resource);
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{serviceName}/hosts/{hostName}/hostComponents/{hostComponent}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Returns information about the given component for the given root-level service on the given host", response = org.apache.ambari.server.api.services.RootServiceService.RootServiceHostComponentResponseWrapper.class)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = org.apache.ambari.server.api.services.BaseService.QUERY_FILTER_DESCRIPTION, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY, defaultValue = org.apache.ambari.server.api.services.RootServiceService.DEFAULT_FIELDS_ROOT_SERVICE_HOST_COMPONENT) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response getRootServiceHostComponent(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam(value = "service name", required = true)
    @javax.ws.rs.PathParam("serviceName")
    java.lang.String serviceName, @io.swagger.annotations.ApiParam(value = "host name", required = true)
    @javax.ws.rs.PathParam("hostName")
    java.lang.String hostName, @io.swagger.annotations.ApiParam(value = "component name", required = true)
    @javax.ws.rs.PathParam("hostComponent")
    java.lang.String hostComponent) {
        org.apache.ambari.server.api.resources.ResourceInstance resource = createHostComponentResource(serviceName, hostName, hostComponent);
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, resource);
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{serviceName}/components")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Returns the list of components for the given root-level service", response = org.apache.ambari.server.api.services.RootServiceService.RootServiceComponentResponseWrapper.class, responseContainer = org.apache.ambari.server.api.services.BaseService.RESPONSE_CONTAINER_LIST)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = org.apache.ambari.server.api.services.BaseService.QUERY_FILTER_DESCRIPTION, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY, defaultValue = org.apache.ambari.server.api.services.RootServiceService.DEFAULT_FIELDS_ROOT_SERVICE_COMPONENTS) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response getRootServiceComponents(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam(value = "service name", required = true)
    @javax.ws.rs.PathParam("serviceName")
    java.lang.String serviceName) {
        org.apache.ambari.server.api.resources.ResourceInstance resource = createServiceComponentResource(serviceName, null);
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, resource);
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{serviceName}/components/{componentName}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Returns information about the given component for the given root-level service", response = org.apache.ambari.server.api.services.RootServiceService.RootServiceComponentWithHostComponentList.class)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = org.apache.ambari.server.api.services.BaseService.QUERY_FILTER_DESCRIPTION, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY, defaultValue = org.apache.ambari.server.api.services.RootServiceService.DEFAULT_FIELDS_ROOT_SERVICE_COMPONENT) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response getRootServiceComponent(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam(value = "service name", required = true)
    @javax.ws.rs.PathParam("serviceName")
    java.lang.String serviceName, @io.swagger.annotations.ApiParam(value = "component name", required = true)
    @javax.ws.rs.PathParam("componentName")
    java.lang.String componentName) {
        org.apache.ambari.server.api.resources.ResourceInstance resource = createServiceComponentResource(serviceName, componentName);
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, resource);
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{serviceName}/components/{componentName}/hostComponents")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Returns the list of hosts for the given root-level service component", response = org.apache.ambari.server.api.services.RootServiceService.RootServiceHostComponentResponseWrapper.class, responseContainer = org.apache.ambari.server.api.services.BaseService.RESPONSE_CONTAINER_LIST)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = org.apache.ambari.server.api.services.BaseService.QUERY_FILTER_DESCRIPTION, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY, defaultValue = org.apache.ambari.server.api.services.RootServiceService.DEFAULT_FIELDS_ROOT_SERVICE_HOST_COMPONENT) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response getRootServiceComponentHosts(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam(value = "service name", required = true)
    @javax.ws.rs.PathParam("serviceName")
    java.lang.String serviceName, @io.swagger.annotations.ApiParam(value = "component name", required = true)
    @javax.ws.rs.PathParam("componentName")
    java.lang.String componentName) {
        org.apache.ambari.server.api.resources.ResourceInstance resource = createHostComponentResource(serviceName, null, componentName);
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, resource);
    }

    @javax.ws.rs.Path("{serviceName}/components/{componentName}/configurations")
    public org.apache.ambari.server.api.services.RootServiceComponentConfigurationService getAmbariServerConfigurationHandler(@javax.ws.rs.core.Context
    javax.ws.rs.core.Request request, @javax.ws.rs.PathParam("serviceName")
    java.lang.String serviceName, @javax.ws.rs.PathParam("componentName")
    java.lang.String componentName) {
        return new org.apache.ambari.server.api.services.RootServiceComponentConfigurationService(serviceName, componentName);
    }

    protected org.apache.ambari.server.api.resources.ResourceInstance createServiceResource(java.lang.String serviceName) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = java.util.Collections.singletonMap(org.apache.ambari.server.controller.spi.Resource.Type.RootService, serviceName);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.RootService, mapIds);
    }

    protected org.apache.ambari.server.api.resources.ResourceInstance createServiceComponentResource(java.lang.String serviceName, java.lang.String componentName) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.RootService, serviceName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.RootServiceComponent, componentName);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.RootServiceComponent, mapIds);
    }

    protected org.apache.ambari.server.api.resources.ResourceInstance createHostResource(java.lang.String hostName) {
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.Host, java.util.Collections.singletonMap(org.apache.ambari.server.controller.spi.Resource.Type.Host, hostName));
    }

    protected org.apache.ambari.server.api.resources.ResourceInstance createHostComponentResource(java.lang.String serviceName, java.lang.String hostName, java.lang.String componentName) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.RootService, serviceName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Host, hostName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.RootServiceComponent, componentName);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.RootServiceHostComponent, mapIds);
    }

    private interface RootServiceResponseWrapper extends org.apache.ambari.server.controller.ApiModel {
        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RootServiceResourceProvider.RESPONSE_KEY)
        @java.lang.SuppressWarnings("unused")
        org.apache.ambari.server.controller.RootServiceResponse getRootServiceResponse();
    }

    private interface RootServiceResponseWithComponentList extends org.apache.ambari.server.api.services.RootServiceService.RootServiceResponseWrapper {
        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.api.services.RootServiceService.KEY_COMPONENTS)
        @java.lang.SuppressWarnings("unused")
        java.util.List<org.apache.ambari.server.api.services.RootServiceService.RootServiceComponentResponseWrapper> getComponents();
    }

    private interface RootServiceComponentResponseWrapper extends org.apache.ambari.server.controller.ApiModel {
        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.RESPONSE_KEY)
        @java.lang.SuppressWarnings("unused")
        org.apache.ambari.server.controller.RootServiceComponentResponse getRootServiceComponentResponse();
    }

    private interface RootServiceComponentWithHostComponentList extends org.apache.ambari.server.api.services.RootServiceService.RootServiceComponentResponseWrapper {
        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.api.services.RootServiceService.KEY_HOST_COMPONENTS)
        @java.lang.SuppressWarnings("unused")
        java.util.List<org.apache.ambari.server.api.services.RootServiceService.RootServiceHostComponentResponseWrapper> getHostComponents();
    }

    private interface RootServiceHostComponentResponseWrapper extends org.apache.ambari.server.controller.ApiModel {
        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.RESPONSE_KEY)
        @java.lang.SuppressWarnings("unused")
        org.apache.ambari.server.controller.RootServiceHostComponentResponse getRootServiceHostComponentResponse();
    }
}