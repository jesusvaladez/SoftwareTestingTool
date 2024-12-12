package org.apache.ambari.server.api.services;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
public class ComponentService extends org.apache.ambari.server.api.services.BaseService {
    private java.lang.String m_clusterName;

    private java.lang.String m_serviceName;

    public ComponentService(java.lang.String clusterName, java.lang.String serviceName) {
        m_clusterName = clusterName;
        m_serviceName = serviceName;
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{componentName}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getComponent(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("componentName")
    java.lang.String componentName, @javax.ws.rs.QueryParam("format")
    java.lang.String format) {
        if ((format != null) && format.equals("client_config_tar")) {
            return createClientConfigResource(body, headers, ui, componentName);
        }
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createComponentResource(m_clusterName, m_serviceName, componentName));
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getComponents(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.QueryParam("format")
    java.lang.String format) {
        if ((format != null) && format.equals("client_config_tar")) {
            return createClientConfigResource(body, headers, ui, null);
        }
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createComponentResource(m_clusterName, m_serviceName, null));
    }

    @javax.ws.rs.POST
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response createComponents(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.POST, createComponentResource(m_clusterName, m_serviceName, null));
    }

    @javax.ws.rs.POST
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{componentName}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response createComponent(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("componentName")
    java.lang.String componentName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.POST, createComponentResource(m_clusterName, m_serviceName, componentName));
    }

    @javax.ws.rs.PUT
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{componentName}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response updateComponent(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("componentName")
    java.lang.String componentName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.PUT, createComponentResource(m_clusterName, m_serviceName, componentName));
    }

    @javax.ws.rs.PUT
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response updateComponents(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.PUT, createComponentResource(m_clusterName, m_serviceName, null));
    }

    @javax.ws.rs.DELETE
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{componentName}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response deleteComponent(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("componentName")
    java.lang.String componentName) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.DELETE, createComponentResource(m_clusterName, m_serviceName, componentName));
    }

    org.apache.ambari.server.api.resources.ResourceInstance createComponentResource(java.lang.String clusterName, java.lang.String serviceName, java.lang.String componentName) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, clusterName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Service, serviceName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Component, componentName);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.Component, mapIds);
    }

    private javax.ws.rs.core.Response createClientConfigResource(java.lang.String body, javax.ws.rs.core.HttpHeaders headers, javax.ws.rs.core.UriInfo ui, java.lang.String componentName) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, m_clusterName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Service, m_serviceName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Component, componentName);
        java.lang.String filePrefixName;
        if (org.apache.commons.lang.StringUtils.isEmpty(componentName)) {
            if (org.apache.commons.lang.StringUtils.isEmpty(m_serviceName)) {
                filePrefixName = ((m_clusterName + "(") + org.apache.ambari.server.controller.spi.Resource.InternalType.Cluster.toString().toUpperCase()) + ")";
            } else {
                filePrefixName = ((m_serviceName + "(") + org.apache.ambari.server.controller.spi.Resource.InternalType.Service.toString().toUpperCase()) + ")";
            }
        } else {
            filePrefixName = componentName;
        }
        org.apache.commons.lang.Validate.notNull(filePrefixName, "compressed config file name should not be null");
        java.lang.String fileName = (filePrefixName + "-configs") + org.apache.ambari.server.configuration.Configuration.DEF_ARCHIVE_EXTENSION;
        javax.ws.rs.core.Response response = handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createResource(org.apache.ambari.server.controller.spi.Resource.Type.ClientConfig, mapIds));
        if (response.getStatus() != 200) {
            return response;
        }
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