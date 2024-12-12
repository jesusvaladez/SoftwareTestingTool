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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.http.HttpStatus;
@javax.ws.rs.Path("/views/{viewName}/versions/{version}/instances")
@io.swagger.annotations.Api(tags = "Views", description = "Endpoint for view specific operations")
public class ViewInstanceService extends org.apache.ambari.server.api.services.BaseService {
    public static final java.lang.String VIEW_INSTANCE_REQUEST_TYPE = "org.apache.ambari.server.controller.ViewInstanceResponse";

    private final org.apache.ambari.server.view.ViewRegistry viewRegistry = org.apache.ambari.server.view.ViewRegistry.getInstance();

    @javax.ws.rs.GET
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Get all view instances", response = org.apache.ambari.server.controller.ViewInstanceResponse.class, responseContainer = "List")
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = org.apache.ambari.server.api.services.BaseService.QUERY_FILTER_DESCRIPTION, defaultValue = "ViewInstanceInfo/*", dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_SORT, value = org.apache.ambari.server.api.services.BaseService.QUERY_SORT_DESCRIPTION, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_PAGE_SIZE, value = org.apache.ambari.server.api.services.BaseService.QUERY_PAGE_SIZE_DESCRIPTION, defaultValue = org.apache.ambari.server.api.services.BaseService.DEFAULT_PAGE_SIZE, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_INT, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FROM, value = org.apache.ambari.server.api.services.BaseService.QUERY_FROM_DESCRIPTION, allowableValues = org.apache.ambari.server.api.services.BaseService.QUERY_FROM_VALUES, defaultValue = org.apache.ambari.server.api.services.BaseService.DEFAULT_FROM, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_INT, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_TO, value = org.apache.ambari.server.api.services.BaseService.QUERY_TO_DESCRIPTION, allowableValues = org.apache.ambari.server.api.services.BaseService.QUERY_TO_VALUES, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_INT, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_CLUSTER_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_BAD_REQUEST, message = org.apache.ambari.server.api.services.BaseService.MSG_INVALID_ARGUMENTS) })
    public javax.ws.rs.core.Response getServices(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("viewName")
    java.lang.String viewName, @javax.ws.rs.PathParam("version")
    java.lang.String version) throws org.apache.ambari.server.security.authorization.AuthorizationException {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createResource(viewName, version, null));
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{instanceName}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Get single view instance", response = org.apache.ambari.server.controller.ViewInstanceResponse.class)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = org.apache.ambari.server.api.services.BaseService.QUERY_FILTER_DESCRIPTION, defaultValue = "ViewInstanceInfo/*", dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_CLUSTER_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_BAD_REQUEST, message = org.apache.ambari.server.api.services.BaseService.MSG_INVALID_ARGUMENTS) })
    public javax.ws.rs.core.Response getService(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam("view name")
    @javax.ws.rs.PathParam("viewName")
    java.lang.String viewName, @javax.ws.rs.PathParam("version")
    java.lang.String version, @io.swagger.annotations.ApiParam("instance name")
    @javax.ws.rs.PathParam("instanceName")
    java.lang.String instanceName) throws org.apache.ambari.server.security.authorization.AuthorizationException {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createResource(viewName, version, instanceName));
    }

    @javax.ws.rs.POST
    @javax.ws.rs.Path("{instanceName}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation("Create view instance")
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(dataType = org.apache.ambari.server.api.services.views.ViewInstanceService.VIEW_INSTANCE_REQUEST_TYPE, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_BODY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_CREATED, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_ACCEPTED, message = org.apache.ambari.server.api.services.BaseService.MSG_REQUEST_ACCEPTED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_BAD_REQUEST, message = org.apache.ambari.server.api.services.BaseService.MSG_INVALID_ARGUMENTS), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_CLUSTER_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response createService(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam("view name")
    @javax.ws.rs.PathParam("viewName")
    java.lang.String viewName, @javax.ws.rs.PathParam("version")
    java.lang.String version, @io.swagger.annotations.ApiParam("instance name")
    @javax.ws.rs.PathParam("instanceName")
    java.lang.String instanceName) throws org.apache.ambari.server.security.authorization.AuthorizationException {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.POST, createResource(viewName, version, instanceName));
    }

    @javax.ws.rs.POST
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation("Create view instances")
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(dataType = org.apache.ambari.server.api.services.views.ViewInstanceService.VIEW_INSTANCE_REQUEST_TYPE, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_BODY, allowMultiple = true) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_CREATED, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_ACCEPTED, message = org.apache.ambari.server.api.services.BaseService.MSG_REQUEST_ACCEPTED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_BAD_REQUEST, message = org.apache.ambari.server.api.services.BaseService.MSG_INVALID_ARGUMENTS), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_CLUSTER_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response createServices(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("viewName")
    java.lang.String viewName, @javax.ws.rs.PathParam("version")
    java.lang.String version) throws org.apache.ambari.server.security.authorization.AuthorizationException {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.POST, createResource(viewName, version, null));
    }

    @javax.ws.rs.PUT
    @javax.ws.rs.Path("{instanceName}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation("Update view instance detail")
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(dataType = org.apache.ambari.server.api.services.views.ViewInstanceService.VIEW_INSTANCE_REQUEST_TYPE, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_BODY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_ACCEPTED, message = org.apache.ambari.server.api.services.BaseService.MSG_REQUEST_ACCEPTED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_BAD_REQUEST, message = org.apache.ambari.server.api.services.BaseService.MSG_INVALID_ARGUMENTS), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_CLUSTER_OR_HOST_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response updateService(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam("view name")
    @javax.ws.rs.PathParam("viewName")
    java.lang.String viewName, @javax.ws.rs.PathParam("version")
    java.lang.String version, @io.swagger.annotations.ApiParam("instance name")
    @javax.ws.rs.PathParam("instanceName")
    java.lang.String instanceName) throws org.apache.ambari.server.security.authorization.AuthorizationException {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.PUT, createResource(viewName, version, instanceName));
    }

    @javax.ws.rs.PUT
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation("Update multiple view instance detail")
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(dataType = org.apache.ambari.server.api.services.views.ViewInstanceService.VIEW_INSTANCE_REQUEST_TYPE, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_BODY, allowMultiple = true) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_ACCEPTED, message = org.apache.ambari.server.api.services.BaseService.MSG_REQUEST_ACCEPTED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_BAD_REQUEST, message = org.apache.ambari.server.api.services.BaseService.MSG_INVALID_ARGUMENTS), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_CLUSTER_OR_HOST_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response updateServices(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("viewName")
    java.lang.String viewName, @javax.ws.rs.PathParam("version")
    java.lang.String version) throws org.apache.ambari.server.security.authorization.AuthorizationException {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.PUT, createResource(viewName, version, null));
    }

    @javax.ws.rs.DELETE
    @javax.ws.rs.Path("{instanceName}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation("Delete view instance")
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_CLUSTER_OR_HOST_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response deleteService(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam("view name")
    @javax.ws.rs.PathParam("viewName")
    java.lang.String viewName, @javax.ws.rs.PathParam("version")
    java.lang.String version, @io.swagger.annotations.ApiParam("instance name")
    @javax.ws.rs.PathParam("instanceName")
    java.lang.String instanceName) throws org.apache.ambari.server.security.authorization.AuthorizationException {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.DELETE, createResource(viewName, version, instanceName));
    }

    @javax.ws.rs.Path("{instanceName}/{resources}")
    public java.lang.Object getResourceHandler(@javax.ws.rs.core.Context
    javax.ws.rs.core.Request request, @javax.ws.rs.PathParam("viewName")
    java.lang.String viewName, @javax.ws.rs.PathParam("version")
    java.lang.String version, @javax.ws.rs.PathParam("instanceName")
    java.lang.String instanceName, @javax.ws.rs.PathParam("resources")
    java.lang.String resources) {
        hasPermission(viewName, version, org.apache.ambari.server.api.services.Request.Type.valueOf(request.getMethod()), instanceName);
        org.apache.ambari.server.orm.entities.ViewInstanceEntity instanceDefinition = org.apache.ambari.server.view.ViewRegistry.getInstance().getInstanceDefinition(viewName, version, instanceName);
        if (instanceDefinition == null) {
            java.lang.String msg = ((("A view instance " + viewName) + "/") + instanceName) + " can not be found.";
            return new org.apache.ambari.server.api.services.views.ViewInstanceService.NotFoundResponse(msg);
        }
        java.lang.Object service = instanceDefinition.getService(resources);
        if (service == null) {
            java.lang.String msg = ((((("A resource type " + resources) + " for view instance ") + viewName) + "/") + instanceName) + " can not be found.";
            return new org.apache.ambari.server.api.services.views.ViewInstanceService.NotFoundResponse(msg);
        }
        return service;
    }

    @javax.ws.rs.Path("/")
    public class NotFoundResponse {
        java.lang.String msg;

        NotFoundResponse(java.lang.String msg) {
            this.msg = msg;
        }

        @javax.ws.rs.GET
        @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
        @io.swagger.annotations.ApiOperation("Handle GET resource with 404 response")
        public javax.ws.rs.core.Response get() {
            return getResponse();
        }

        @javax.ws.rs.POST
        @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
        @io.swagger.annotations.ApiOperation("Handle POST resource with 404 response")
        public javax.ws.rs.core.Response post() {
            return getResponse();
        }

        @javax.ws.rs.PUT
        @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
        @io.swagger.annotations.ApiOperation("Handle PUT resource with 404 response")
        public javax.ws.rs.core.Response put() {
            return getResponse();
        }

        @javax.ws.rs.DELETE
        @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
        @io.swagger.annotations.ApiOperation("Handle DELETE resource with 404 response")
        public javax.ws.rs.core.Response delete() {
            return getResponse();
        }

        @javax.ws.rs.GET
        @javax.ws.rs.Path("{path: .*}")
        @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
        @io.swagger.annotations.ApiOperation("Handle GET sub-resource with 404 response")
        public javax.ws.rs.core.Response getSub() {
            return getResponse();
        }

        @javax.ws.rs.POST
        @javax.ws.rs.Path("{path: .*}")
        @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
        @io.swagger.annotations.ApiOperation("Handle POST sub-resource with 404 response")
        public javax.ws.rs.core.Response postSub() {
            return getResponse();
        }

        @javax.ws.rs.PUT
        @javax.ws.rs.Path("{path: .*}")
        @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
        @io.swagger.annotations.ApiOperation("Handle PUT sub-resource with 404 response")
        public javax.ws.rs.core.Response putSub() {
            return getResponse();
        }

        @javax.ws.rs.DELETE
        @javax.ws.rs.Path("{path: .*}")
        @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
        @io.swagger.annotations.ApiOperation("Handle DELETE sub-resource with 404 response")
        public javax.ws.rs.core.Response deleteSub() {
            return getResponse();
        }

        public javax.ws.rs.core.Response getResponse() {
            org.apache.ambari.server.api.services.Result result = new org.apache.ambari.server.api.services.ResultImpl(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.NOT_FOUND, msg));
            javax.ws.rs.core.Response.ResponseBuilder builder = javax.ws.rs.core.Response.status(result.getStatus().getStatusCode()).entity(new org.apache.ambari.server.api.services.serializers.JsonSerializer().serialize(result));
            return builder.build();
        }
    }

    private org.apache.ambari.server.api.resources.ResourceInstance createResource(java.lang.String viewName, java.lang.String viewVersion, java.lang.String instanceName) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.View, viewName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.ViewVersion, viewVersion);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.ViewInstance, instanceName);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.ViewInstance, mapIds);
    }

    private void hasPermission(java.lang.String viewName, java.lang.String version, org.apache.ambari.server.api.services.Request.Type requestType, java.lang.String instanceName) {
        if (!viewRegistry.checkPermission(viewName, version, instanceName, requestType == org.apache.ambari.server.api.services.Request.Type.GET)) {
            throw new javax.ws.rs.WebApplicationException(Response.Status.FORBIDDEN);
        }
    }
}