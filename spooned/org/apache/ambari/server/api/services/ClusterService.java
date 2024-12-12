package org.apache.ambari.server.api.services;
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
@javax.ws.rs.Path("/clusters")
@io.swagger.annotations.Api(value = "Clusters", description = "Endpoint for cluster-specific operations")
public class ClusterService extends org.apache.ambari.server.api.services.BaseService {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.api.services.ClusterService.class);

    public static final java.lang.String INVALID_KERBEROS_CHAR = "^\"[=+@].*";

    private final com.google.gson.Gson gson = new com.google.gson.Gson();

    private static final java.lang.String CLUSTER_REQUEST_TYPE = "org.apache.ambari.server.api.services.ClusterRequestSwagger";

    private static final java.lang.String ARTIFACT_REQUEST_TYPE = "org.apache.ambari.server.controller.ClusterArtifactRequest";

    private final org.apache.ambari.server.state.Clusters clusters;

    public ClusterService() {
        clusters = org.apache.ambari.server.controller.AmbariServer.getController().getClusters();
    }

    protected ClusterService(org.apache.ambari.server.state.Clusters clusters) {
        this.clusters = clusters;
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{clusterName}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Returns information about a specific cluster", response = org.apache.ambari.server.controller.ClusterResponse.ClusterResponseWrapper.class)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = org.apache.ambari.server.api.services.BaseService.QUERY_FILTER_DESCRIPTION, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY, defaultValue = org.apache.ambari.server.controller.internal.ClusterResourceProvider.ALL_PROPERTIES) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response getCluster(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam(required = true)
    @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName) {
        org.apache.ambari.server.api.resources.ResourceInstance resource = createClusterResource(clusterName);
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, resource);
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Returns all clusters", response = org.apache.ambari.server.controller.ClusterResponse.ClusterResponseWrapper.class, responseContainer = org.apache.ambari.server.api.services.BaseService.RESPONSE_CONTAINER_LIST)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = org.apache.ambari.server.api.services.BaseService.QUERY_FILTER_DESCRIPTION, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_SORT, value = org.apache.ambari.server.api.services.BaseService.QUERY_SORT_DESCRIPTION, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_PAGE_SIZE, value = org.apache.ambari.server.api.services.BaseService.QUERY_PAGE_SIZE_DESCRIPTION, defaultValue = org.apache.ambari.server.api.services.BaseService.DEFAULT_PAGE_SIZE, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_INT, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FROM, value = org.apache.ambari.server.api.services.BaseService.QUERY_FROM_DESCRIPTION, allowableValues = org.apache.ambari.server.api.services.BaseService.QUERY_FROM_VALUES, defaultValue = org.apache.ambari.server.api.services.BaseService.DEFAULT_FROM, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_INT, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_TO, value = org.apache.ambari.server.api.services.BaseService.QUERY_TO_DESCRIPTION, allowableValues = org.apache.ambari.server.api.services.BaseService.QUERY_TO_VALUES, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_INT, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response getClusters(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        org.apache.ambari.server.api.resources.ResourceInstance resource = createClusterResource(null);
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, resource);
    }

    @javax.ws.rs.POST
    @javax.ws.rs.Path("{clusterName}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation("Creates a cluster")
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(dataType = org.apache.ambari.server.api.services.ClusterService.CLUSTER_REQUEST_TYPE, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_BODY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_CREATED, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_ACCEPTED, message = org.apache.ambari.server.api.services.BaseService.MSG_REQUEST_ACCEPTED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_BAD_REQUEST, message = org.apache.ambari.server.api.services.BaseService.MSG_INVALID_ARGUMENTS), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_CONFLICT, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_ALREADY_EXISTS), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response createCluster(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam(required = true)
    @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName) {
        org.apache.ambari.server.api.resources.ResourceInstance resource = createClusterResource(clusterName);
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.POST, resource);
    }

    @javax.ws.rs.PUT
    @javax.ws.rs.Path("{clusterName}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation("Updates a cluster")
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(dataType = org.apache.ambari.server.api.services.ClusterService.CLUSTER_REQUEST_TYPE, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_BODY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_ACCEPTED, message = org.apache.ambari.server.api.services.BaseService.MSG_REQUEST_ACCEPTED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_BAD_REQUEST, message = org.apache.ambari.server.api.services.BaseService.MSG_INVALID_ARGUMENTS), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response updateCluster(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam(required = true)
    @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName) {
        org.apache.ambari.server.api.resources.ResourceInstance resource = createClusterResource(clusterName);
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.PUT, resource);
    }

    @javax.ws.rs.DELETE
    @javax.ws.rs.Path("{clusterName}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation("Deletes a cluster")
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response deleteCluster(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam(required = true)
    @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName) {
        org.apache.ambari.server.api.resources.ResourceInstance resource = createClusterResource(clusterName);
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.DELETE, resource);
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{clusterName}/artifacts")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Returns all artifacts associated with the cluster", response = org.apache.ambari.server.controller.ClusterArtifactResponse.class, responseContainer = org.apache.ambari.server.api.services.BaseService.RESPONSE_CONTAINER_LIST)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = org.apache.ambari.server.api.services.BaseService.QUERY_FILTER_DESCRIPTION, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_SORT, value = org.apache.ambari.server.api.services.BaseService.QUERY_SORT_DESCRIPTION, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_PAGE_SIZE, value = org.apache.ambari.server.api.services.BaseService.QUERY_PAGE_SIZE_DESCRIPTION, defaultValue = org.apache.ambari.server.api.services.BaseService.DEFAULT_PAGE_SIZE, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_INT, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FROM, value = org.apache.ambari.server.api.services.BaseService.QUERY_FROM_DESCRIPTION, allowableValues = org.apache.ambari.server.api.services.BaseService.QUERY_FROM_VALUES, defaultValue = org.apache.ambari.server.api.services.BaseService.DEFAULT_FROM, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_INT, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_TO, value = org.apache.ambari.server.api.services.BaseService.QUERY_TO_DESCRIPTION, allowableValues = org.apache.ambari.server.api.services.BaseService.QUERY_TO_VALUES, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_INT, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response getClusterArtifacts(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam(required = true)
    @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName) {
        org.apache.ambari.server.api.resources.ResourceInstance resource = createArtifactResource(clusterName, null);
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, resource);
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{clusterName}/artifacts/{artifactName}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Get the details of a cluster artifact", response = org.apache.ambari.server.controller.ClusterArtifactResponse.class)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = org.apache.ambari.server.api.services.BaseService.QUERY_FILTER_DESCRIPTION, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_SORT, value = org.apache.ambari.server.api.services.BaseService.QUERY_SORT_DESCRIPTION, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_PAGE_SIZE, value = org.apache.ambari.server.api.services.BaseService.QUERY_PAGE_SIZE_DESCRIPTION, defaultValue = org.apache.ambari.server.api.services.BaseService.DEFAULT_PAGE_SIZE, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_INT, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FROM, value = org.apache.ambari.server.api.services.BaseService.QUERY_FROM_DESCRIPTION, defaultValue = org.apache.ambari.server.api.services.BaseService.DEFAULT_FROM, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY), @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_TO, value = org.apache.ambari.server.api.services.BaseService.QUERY_TO_DESCRIPTION, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response getClusterArtifact(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam(required = true)
    @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName, @io.swagger.annotations.ApiParam(required = true)
    @javax.ws.rs.PathParam("artifactName")
    java.lang.String artifactName) {
        org.apache.ambari.server.api.resources.ResourceInstance resource = createArtifactResource(clusterName, artifactName);
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, resource);
    }

    @javax.ws.rs.POST
    @javax.ws.rs.Path("{clusterName}/artifacts/{artifactName}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation("Creates a cluster artifact")
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(dataType = org.apache.ambari.server.api.services.ClusterService.ARTIFACT_REQUEST_TYPE, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_BODY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_CREATED, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_ACCEPTED, message = org.apache.ambari.server.api.services.BaseService.MSG_REQUEST_ACCEPTED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_BAD_REQUEST, message = org.apache.ambari.server.api.services.BaseService.MSG_INVALID_ARGUMENTS), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_CONFLICT, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_ALREADY_EXISTS), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response createClusterArtifact(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam(required = true)
    @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName, @io.swagger.annotations.ApiParam(required = true)
    @javax.ws.rs.PathParam("artifactName")
    java.lang.String artifactName) {
        org.apache.ambari.server.api.resources.ResourceInstance resource = createArtifactResource(clusterName, artifactName);
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.POST, resource);
    }

    @javax.ws.rs.PUT
    @javax.ws.rs.Path("{clusterName}/artifacts")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation("Updates multiple artifacts")
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(dataType = org.apache.ambari.server.api.services.ClusterService.ARTIFACT_REQUEST_TYPE, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_BODY, allowMultiple = true) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_ACCEPTED, message = org.apache.ambari.server.api.services.BaseService.MSG_REQUEST_ACCEPTED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_BAD_REQUEST, message = org.apache.ambari.server.api.services.BaseService.MSG_INVALID_ARGUMENTS), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response updateClusterArtifacts(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam(required = true)
    @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName) {
        org.apache.ambari.server.api.resources.ResourceInstance resource = createArtifactResource(clusterName, null);
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.PUT, resource);
    }

    @javax.ws.rs.PUT
    @javax.ws.rs.Path("{clusterName}/artifacts/{artifactName}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation("Updates a single artifact")
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(dataType = org.apache.ambari.server.api.services.ClusterService.ARTIFACT_REQUEST_TYPE, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_BODY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_ACCEPTED, message = org.apache.ambari.server.api.services.BaseService.MSG_REQUEST_ACCEPTED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_BAD_REQUEST, message = org.apache.ambari.server.api.services.BaseService.MSG_INVALID_ARGUMENTS), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response updateClusterArtifact(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam(required = true)
    @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName, @io.swagger.annotations.ApiParam(required = true)
    @javax.ws.rs.PathParam("artifactName")
    java.lang.String artifactName) throws org.apache.ambari.server.api.services.parsers.BodyParseException {
        if ("kerberos_descriptor".equals(artifactName)) {
            org.apache.ambari.server.api.services.ClusterService.LOG.info("Validating body For kerberos_descriptor");
            if (parseBody(body)) {
                throw new org.apache.ambari.server.api.services.parsers.BodyParseException("Bad request received");
            }
        }
        org.apache.ambari.server.api.resources.ResourceInstance resource = createArtifactResource(clusterName, artifactName);
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.PUT, resource);
    }

    @javax.ws.rs.DELETE
    @javax.ws.rs.Path("{clusterName}/artifacts/{artifactName}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation("Deletes a single artifact")
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response deleteClusterArtifact(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam(required = true)
    @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName, @io.swagger.annotations.ApiParam(required = true)
    @javax.ws.rs.PathParam("artifactName")
    java.lang.String artifactName) {
        org.apache.ambari.server.api.resources.ResourceInstance resource = createArtifactResource(clusterName, artifactName);
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.DELETE, resource);
    }

    @javax.ws.rs.DELETE
    @javax.ws.rs.Path("{clusterName}/artifacts")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation("Deletes all artifacts of a cluster that match the provided predicate")
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_RESOURCE_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response deleteClusterArtifacts(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @io.swagger.annotations.ApiParam(required = true)
    @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName) {
        org.apache.ambari.server.api.resources.ResourceInstance resource = createArtifactResource(clusterName, null);
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.DELETE, resource);
    }

    @javax.ws.rs.Path("{clusterName}/hosts")
    public org.apache.ambari.server.api.services.HostService getHostHandler(@javax.ws.rs.core.Context
    javax.ws.rs.core.Request request, @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName) {
        return new org.apache.ambari.server.api.services.HostService(clusterName);
    }

    @javax.ws.rs.Path("{clusterName}/services")
    public org.apache.ambari.server.api.services.ServiceService getServiceHandler(@javax.ws.rs.core.Context
    javax.ws.rs.core.Request request, @io.swagger.annotations.ApiParam
    @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName) {
        return new org.apache.ambari.server.api.services.ServiceService(clusterName);
    }

    @javax.ws.rs.Path("{clusterName}/configurations")
    public org.apache.ambari.server.api.services.ConfigurationService getConfigurationHandler(@javax.ws.rs.core.Context
    javax.ws.rs.core.Request request, @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName) {
        return new org.apache.ambari.server.api.services.ConfigurationService(clusterName);
    }

    @javax.ws.rs.Path("{clusterName}/requests")
    public org.apache.ambari.server.api.services.RequestService getRequestHandler(@javax.ws.rs.core.Context
    javax.ws.rs.core.Request request, @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName) {
        return new org.apache.ambari.server.api.services.RequestService(clusterName);
    }

    @javax.ws.rs.Path("{clusterName}/host_components")
    public org.apache.ambari.server.api.services.HostComponentService getHostComponentHandler(@javax.ws.rs.core.Context
    javax.ws.rs.core.Request request, @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName) {
        return new org.apache.ambari.server.api.services.HostComponentService(clusterName, null);
    }

    @javax.ws.rs.Path("{clusterName}/kerberos_identities")
    public org.apache.ambari.server.api.services.HostKerberosIdentityService getHostKerberosIdentityHandler(@javax.ws.rs.core.Context
    javax.ws.rs.core.Request request, @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName) {
        return new org.apache.ambari.server.api.services.HostKerberosIdentityService(clusterName, null);
    }

    @javax.ws.rs.Path("{clusterName}/components")
    public org.apache.ambari.server.api.services.ComponentService getComponentHandler(@javax.ws.rs.core.Context
    javax.ws.rs.core.Request request, @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName) {
        return new org.apache.ambari.server.api.services.ComponentService(clusterName, null);
    }

    @javax.ws.rs.Path("{clusterName}/workflows")
    public org.apache.ambari.server.api.services.WorkflowService getWorkflowHandler(@javax.ws.rs.core.Context
    javax.ws.rs.core.Request request, @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName) {
        return new org.apache.ambari.server.api.services.WorkflowService(clusterName);
    }

    @javax.ws.rs.Path("{clusterName}/config_groups")
    public org.apache.ambari.server.api.services.ConfigGroupService getConfigGroupService(@javax.ws.rs.core.Context
    javax.ws.rs.core.Request request, @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName) {
        return new org.apache.ambari.server.api.services.ConfigGroupService(clusterName);
    }

    @javax.ws.rs.Path("{clusterName}/request_schedules")
    public org.apache.ambari.server.api.services.RequestScheduleService getRequestScheduleService(@javax.ws.rs.core.Context
    javax.ws.rs.core.Request request, @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName) {
        return new org.apache.ambari.server.api.services.RequestScheduleService(clusterName);
    }

    @javax.ws.rs.Path("{clusterName}/alert_definitions")
    public org.apache.ambari.server.api.services.AlertDefinitionService getAlertDefinitionService(@javax.ws.rs.core.Context
    javax.ws.rs.core.Request request, @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName) {
        return new org.apache.ambari.server.api.services.AlertDefinitionService(clusterName);
    }

    @javax.ws.rs.Path("{clusterName}/alert_groups")
    public org.apache.ambari.server.api.services.AlertGroupService getAlertGroups(@javax.ws.rs.core.Context
    javax.ws.rs.core.Request request, @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName) {
        return new org.apache.ambari.server.api.services.AlertGroupService(clusterName);
    }

    @javax.ws.rs.Path("{clusterName}/privileges")
    public org.apache.ambari.server.api.services.PrivilegeService getPrivilegeService(@javax.ws.rs.core.Context
    javax.ws.rs.core.Request request, @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName) {
        return new org.apache.ambari.server.api.services.ClusterPrivilegeService(clusterName);
    }

    @javax.ws.rs.Path("{clusterName}/alerts")
    public org.apache.ambari.server.api.services.AlertService getAlertService(@javax.ws.rs.core.Context
    javax.ws.rs.core.Request request, @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName) {
        return new org.apache.ambari.server.api.services.AlertService(clusterName, null, null);
    }

    @javax.ws.rs.Path("{clusterName}/alert_history")
    public org.apache.ambari.server.api.services.AlertHistoryService getAlertHistoryService(@javax.ws.rs.core.Context
    javax.ws.rs.core.Request request, @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName) {
        return new org.apache.ambari.server.api.services.AlertHistoryService(clusterName, null, null);
    }

    @javax.ws.rs.Path("{clusterName}/alert_notices")
    public org.apache.ambari.server.api.services.AlertNoticeService getAlertNoticeService(@javax.ws.rs.core.Context
    javax.ws.rs.core.Request request, @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName) {
        return new org.apache.ambari.server.api.services.AlertNoticeService(clusterName);
    }

    @javax.ws.rs.Path("{clusterName}/stack_versions")
    public org.apache.ambari.server.api.services.ClusterStackVersionService getClusterStackVersionService(@javax.ws.rs.core.Context
    javax.ws.rs.core.Request request, @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName) {
        return new org.apache.ambari.server.api.services.ClusterStackVersionService(clusterName);
    }

    @javax.ws.rs.Path("{clusterName}/upgrades")
    public org.apache.ambari.server.api.services.UpgradeService getUpgradeService(@javax.ws.rs.core.Context
    javax.ws.rs.core.Request request, @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName) {
        return new org.apache.ambari.server.api.services.UpgradeService(clusterName);
    }

    @javax.ws.rs.Path("{clusterName}/upgrade_summary")
    public org.apache.ambari.server.api.services.UpgradeSummaryService getUpgradeSummaryService(@javax.ws.rs.core.Context
    javax.ws.rs.core.Request request, @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName) {
        return new org.apache.ambari.server.api.services.UpgradeSummaryService(clusterName);
    }

    @javax.ws.rs.Path("{clusterName}/rolling_upgrades_check")
    public org.apache.ambari.server.api.services.PreUpgradeCheckService getPreUpgradeCheckService(@javax.ws.rs.core.Context
    javax.ws.rs.core.Request request, @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName) {
        return new org.apache.ambari.server.api.services.PreUpgradeCheckService(clusterName);
    }

    @javax.ws.rs.Path("{clusterName}/widget_layouts")
    public org.apache.ambari.server.api.services.WidgetLayoutService getWidgetLayoutService(@javax.ws.rs.core.Context
    javax.ws.rs.core.Request request, @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName) {
        return new org.apache.ambari.server.api.services.WidgetLayoutService(clusterName);
    }

    @javax.ws.rs.Path("{clusterName}/widgets")
    public org.apache.ambari.server.api.services.WidgetService getWidgetService(@javax.ws.rs.core.Context
    javax.ws.rs.core.Request request, @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName) {
        return new org.apache.ambari.server.api.services.WidgetService(clusterName);
    }

    @javax.ws.rs.Path("{clusterName}/credentials")
    public org.apache.ambari.server.api.services.CredentialService getCredentials(@javax.ws.rs.core.Context
    javax.ws.rs.core.Request request, @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName) {
        return new org.apache.ambari.server.api.services.CredentialService(clusterName);
    }

    @javax.ws.rs.Path("{clusterName}/kerberos_descriptors")
    public org.apache.ambari.server.api.services.ClusterKerberosDescriptorService getCompositeKerberosDescriptor(@javax.ws.rs.core.Context
    javax.ws.rs.core.Request request, @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName) {
        return new org.apache.ambari.server.api.services.ClusterKerberosDescriptorService(clusterName);
    }

    @javax.ws.rs.Path("{clusterName}/logging")
    public org.apache.ambari.server.api.services.LoggingService getLogging(@javax.ws.rs.core.Context
    javax.ws.rs.core.Request request, @javax.ws.rs.PathParam("clusterName")
    java.lang.String clusterName) {
        return org.apache.ambari.server.controller.AmbariServer.getController().getLoggingService(clusterName);
    }

    org.apache.ambari.server.api.resources.ResourceInstance createClusterResource(java.lang.String clusterName) {
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, java.util.Collections.singletonMap(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, clusterName));
    }

    org.apache.ambari.server.api.resources.ResourceInstance createArtifactResource(java.lang.String clusterName, java.lang.String artifactName) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, clusterName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Artifact, artifactName);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.Artifact, mapIds);
    }

    private boolean parseBody(java.lang.String body) {
        com.google.gson.JsonObject jsonObject = gson.fromJson(body, com.google.gson.JsonObject.class);
        if (jsonObject == null) {
            return false;
        }
        com.google.gson.JsonElement artifact_data = jsonObject.get("artifact_data");
        if (artifact_data == null) {
            return false;
        }
        com.google.gson.JsonArray identities = artifact_data.getAsJsonObject().getAsJsonArray("identities");
        if (identities != null) {
            if (checkKeytabsPrincipal(identities)) {
                return true;
            }
        }
        com.google.gson.JsonArray services = artifact_data.getAsJsonObject().getAsJsonArray("services");
        for (int i = 0; i < services.size(); i++) {
            com.google.gson.JsonArray components = services.get(i).getAsJsonObject().get("components").getAsJsonArray();
            if (services.get(i).getAsJsonObject().get("identities") != null) {
                if (checkKeytabsPrincipal(((com.google.gson.JsonArray) (services.get(i).getAsJsonObject().get("identities"))))) {
                    return true;
                }
            }
            for (int ii = 0; ii < components.size(); ii++) {
                com.google.gson.JsonArray componentsIdentities = ((com.google.gson.JsonArray) (components.get(ii).getAsJsonObject().get("identities")));
                if (componentsIdentities != null) {
                    if (checkKeytabsPrincipal(componentsIdentities)) {
                        return true;
                    }
                }
            }
        }
        java.util.Set<java.util.Map.Entry<java.lang.String, com.google.gson.JsonElement>> properties = artifact_data.getAsJsonObject().getAsJsonObject("properties").entrySet();
        for (java.util.Map.Entry<java.lang.String, com.google.gson.JsonElement> entry : properties) {
            boolean res = validateValues(java.lang.String.valueOf(entry.getValue()));
            if (res) {
                return true;
            }
        }
        return false;
    }

    private boolean checkKeytabsPrincipal(com.google.gson.JsonArray identities) {
        java.lang.String keytabFile = "";
        java.lang.String principalValue = "";
        for (int i = 0; i < identities.size(); i++) {
            if (identities.get(i).getAsJsonObject().get("keytab") != null) {
                keytabFile = java.lang.String.valueOf(identities.get(i).getAsJsonObject().get("keytab").getAsJsonObject().get("file"));
            }
            if (identities.get(i).getAsJsonObject().get("principal") != null) {
                principalValue = java.lang.String.valueOf(identities.get(i).getAsJsonObject().get("principal").getAsJsonObject().get("value"));
            }
            if ((!keytabFile.isEmpty()) && (!"null".equals(keytabFile))) {
                if (validateValues(keytabFile)) {
                    return true;
                }
            }
            if ((!principalValue.isEmpty()) && (!"null".equals(principalValue))) {
                if (validateValues(principalValue)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean validateValues(java.lang.String values) {
        if (values.matches(org.apache.ambari.server.api.services.ClusterService.INVALID_KERBEROS_CHAR)) {
            return true;
        }
        return false;
    }
}