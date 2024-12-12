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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.http.HttpStatus;
@io.swagger.annotations.Api(value = "Host Components", description = "Endpoint for host component specific operations")
@org.apache.ambari.annotations.SwaggerPreferredParent(preferredParent = org.apache.ambari.server.api.services.ClusterService.class)
public class HostComponentService extends org.apache.ambari.server.api.services.BaseService {
    public static final java.lang.String HOST_ROLE_REQUEST_TYPE = "org.apache.ambari.server.controller.ServiceComponentHostResponse";

    private java.lang.String m_clusterName;

    private java.lang.String m_hostName;

    public HostComponentService(java.lang.String clusterName, java.lang.String hostName) {
        m_clusterName = clusterName;
        m_hostName = hostName;
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{hostComponentName}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Get single host component for a host", response = org.apache.ambari.server.controller.HostComponentSwagger.class)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = org.apache.ambari.server.api.services.BaseService.QUERY_FILTER_DESCRIPTION, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_CLUSTER_OR_HOST_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_BAD_REQUEST, message = org.apache.ambari.server.api.services.BaseService.MSG_INVALID_ARGUMENTS) })
    public javax.ws.rs.core.Response getHostComponent(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("hostComponentName")
    java.lang.String hostComponentName, @javax.ws.rs.QueryParam("format")
    java.lang.String format) {
        if (m_hostName == null) {
            java.lang.String s = "Invalid request. Must provide host information when requesting a host_resource instance resource.";
            return javax.ws.rs.core.Response.status(400).entity(s).build();
        }
        if ((format != null) && format.equals("client_config_tar")) {
            return createClientConfigResource(body, headers, ui, hostComponentName);
        }
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createHostComponentResource(m_clusterName, m_hostName, hostComponentName));
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Get all host components for a host", response = org.apache.ambari.server.controller.HostComponentSwagger.class, responseContainer = org.apache.ambari.server.api.services.BaseService.RESPONSE_CONTAINER_LIST)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = org.apache.ambari.server.api.services.BaseService.QUERY_FILTER_DESCRIPTION, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_CLUSTER_OR_HOST_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_BAD_REQUEST, message = org.apache.ambari.server.api.services.BaseService.MSG_INVALID_ARGUMENTS) })
    public javax.ws.rs.core.Response getHostComponents(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.QueryParam("format")
    java.lang.String format) {
        if ((format != null) && format.equals("client_config_tar")) {
            return createClientConfigResource(body, headers, ui, null);
        }
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createHostComponentResource(m_clusterName, m_hostName, null));
    }

    @javax.ws.rs.POST
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation("Create new host components")
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(dataType = org.apache.ambari.server.api.services.HostComponentService.HOST_ROLE_REQUEST_TYPE, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_BODY, allowMultiple = true) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_CREATED, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_ACCEPTED, message = org.apache.ambari.server.api.services.BaseService.MSG_REQUEST_ACCEPTED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_BAD_REQUEST, message = org.apache.ambari.server.api.services.BaseService.MSG_INVALID_ARGUMENTS), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_CLUSTER_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response createHostComponents(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.POST, createHostComponentResource(m_clusterName, m_hostName, null));
    }

    @javax.ws.rs.POST
    @javax.ws.rs.Path("{hostComponentName}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation("Create new host component")
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(dataType = org.apache.ambari.server.api.services.HostComponentService.HOST_ROLE_REQUEST_TYPE, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_BODY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_CREATED, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_ACCEPTED, message = org.apache.ambari.server.api.services.BaseService.MSG_REQUEST_ACCEPTED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_BAD_REQUEST, message = org.apache.ambari.server.api.services.BaseService.MSG_INVALID_ARGUMENTS), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_CLUSTER_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response createHostComponent(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("hostComponentName")
    java.lang.String hostComponentName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.POST, createHostComponentResource(m_clusterName, m_hostName, hostComponentName));
    }

    @javax.ws.rs.PUT
    @javax.ws.rs.Path("{hostComponentName}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation("Update host component detail")
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(dataType = org.apache.ambari.server.api.services.HostComponentService.HOST_ROLE_REQUEST_TYPE, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_BODY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_ACCEPTED, message = org.apache.ambari.server.api.services.BaseService.MSG_REQUEST_ACCEPTED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_BAD_REQUEST, message = org.apache.ambari.server.api.services.BaseService.MSG_INVALID_ARGUMENTS), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_CLUSTER_OR_HOST_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response updateHostComponent(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("hostComponentName")
    java.lang.String hostComponentName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.PUT, createHostComponentResource(m_clusterName, m_hostName, hostComponentName));
    }

    @javax.ws.rs.PUT
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation("Update multiple host component details")
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(dataType = org.apache.ambari.server.api.services.HostComponentService.HOST_ROLE_REQUEST_TYPE, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_BODY, allowMultiple = true) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_ACCEPTED, message = org.apache.ambari.server.api.services.BaseService.MSG_REQUEST_ACCEPTED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_BAD_REQUEST, message = org.apache.ambari.server.api.services.BaseService.MSG_INVALID_ARGUMENTS), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_CLUSTER_OR_HOST_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response updateHostComponents(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.PUT, createHostComponentResource(m_clusterName, m_hostName, null));
    }

    @javax.ws.rs.DELETE
    @javax.ws.rs.Path("{hostComponentName}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation("Delete host component")
    @io.swagger.annotations.ApiImplicitParams({  })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_CLUSTER_OR_HOST_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response deleteHostComponent(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("hostComponentName")
    java.lang.String hostComponentName) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.DELETE, createHostComponentResource(m_clusterName, m_hostName, hostComponentName));
    }

    @javax.ws.rs.DELETE
    @javax.ws.rs.Path("")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation("Delete host components")
    @io.swagger.annotations.ApiImplicitParams({  })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_CLUSTER_OR_HOST_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR) })
    public javax.ws.rs.core.Response deleteHostComponents(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.DELETE, createHostComponentResource(m_clusterName, m_hostName, null));
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("{hostComponentName}/processes")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    @io.swagger.annotations.ApiOperation(value = "Get processes of a specific host component", response = org.apache.ambari.server.controller.HostComponentProcessResponse.class)
    @io.swagger.annotations.ApiImplicitParams({ @io.swagger.annotations.ApiImplicitParam(name = org.apache.ambari.server.api.services.BaseService.QUERY_FIELDS, value = org.apache.ambari.server.api.services.BaseService.QUERY_FILTER_DESCRIPTION, dataType = org.apache.ambari.server.api.services.BaseService.DATA_TYPE_STRING, paramType = org.apache.ambari.server.api.services.BaseService.PARAM_TYPE_QUERY) })
    @io.swagger.annotations.ApiResponses({ @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_OK, message = org.apache.ambari.server.api.services.BaseService.MSG_SUCCESSFUL_OPERATION), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_NOT_FOUND, message = org.apache.ambari.server.api.services.BaseService.MSG_CLUSTER_OR_HOST_NOT_FOUND), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_UNAUTHORIZED, message = org.apache.ambari.server.api.services.BaseService.MSG_NOT_AUTHENTICATED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_FORBIDDEN, message = org.apache.ambari.server.api.services.BaseService.MSG_PERMISSION_DENIED), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = org.apache.ambari.server.api.services.BaseService.MSG_SERVER_ERROR), @io.swagger.annotations.ApiResponse(code = org.apache.http.HttpStatus.SC_BAD_REQUEST, message = org.apache.ambari.server.api.services.BaseService.MSG_INVALID_ARGUMENTS) })
    public javax.ws.rs.core.Response getProcesses(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("hostComponentName")
    java.lang.String hostComponentName) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, m_clusterName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Host, m_hostName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, hostComponentName);
        org.apache.ambari.server.api.resources.ResourceInstance ri = createResource(org.apache.ambari.server.controller.spi.Resource.Type.HostComponentProcess, mapIds);
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, ri);
    }

    org.apache.ambari.server.api.resources.ResourceInstance createHostComponentResource(java.lang.String clusterName, java.lang.String hostName, java.lang.String hostComponentName) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, clusterName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Host, hostName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, hostComponentName);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, mapIds);
    }

    private javax.ws.rs.core.Response createClientConfigResource(java.lang.String body, javax.ws.rs.core.HttpHeaders headers, javax.ws.rs.core.UriInfo ui, java.lang.String hostComponentName) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, m_clusterName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Host, m_hostName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Component, hostComponentName);
        javax.ws.rs.core.Response response = handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createResource(org.apache.ambari.server.controller.spi.Resource.Type.ClientConfig, mapIds));
        if (response.getStatus() != 200) {
            return response;
        }
        java.lang.String filePrefixName;
        if (org.apache.commons.lang.StringUtils.isEmpty(hostComponentName)) {
            filePrefixName = ((m_hostName + "(") + org.apache.ambari.server.controller.spi.Resource.InternalType.Host.toString().toUpperCase()) + ")";
        } else {
            filePrefixName = hostComponentName;
        }
        org.apache.commons.lang.Validate.notNull(filePrefixName, "compressed config file name should not be null");
        java.lang.String fileName = (filePrefixName + "-configs") + org.apache.ambari.server.configuration.Configuration.DEF_ARCHIVE_EXTENSION;
        javax.ws.rs.core.Response.ResponseBuilder rb = javax.ws.rs.core.Response.status(Response.Status.OK);
        org.apache.ambari.server.configuration.Configuration configs = new org.apache.ambari.server.configuration.Configuration();
        java.lang.String tmpDir = configs.getProperty(org.apache.ambari.server.configuration.Configuration.SERVER_TMP_DIR.getKey());
        java.io.File file = new java.io.File(tmpDir, fileName);
        java.io.InputStream resultInputStream = null;
        try {
            resultInputStream = new java.io.FileInputStream(file);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        java.lang.String contentType = org.apache.ambari.server.configuration.Configuration.DEF_ARCHIVE_CONTENT_TYPE;
        rb.header("Content-Disposition", ("attachment; filename=\"" + fileName) + "\"");
        rb.entity(resultInputStream);
        return rb.type(contentType).build();
    }
}