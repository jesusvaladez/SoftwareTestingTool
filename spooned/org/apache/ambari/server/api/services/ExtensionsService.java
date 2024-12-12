package org.apache.ambari.server.api.services;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
@javax.ws.rs.Path("/extensions/")
public class ExtensionsService extends org.apache.ambari.server.api.services.BaseService {
    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getExtensions(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createExtensionResource(null));
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{extensionName}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getExtension(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("extensionName")
    java.lang.String extensionName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createExtensionResource(extensionName));
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{extensionName}/versions")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getExtensionVersions(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("extensionName")
    java.lang.String extensionName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createExtensionVersionResource(extensionName, null));
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{extensionName}/versions/{extensionVersion}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getExtensionVersion(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("extensionName")
    java.lang.String extensionName, @javax.ws.rs.PathParam("extensionVersion")
    java.lang.String extensionVersion) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createExtensionVersionResource(extensionName, extensionVersion));
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{extensionName}/versions/{extensionVersion}/links")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getExtensionVersionLinks(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("extensionName")
    java.lang.String extensionName, @javax.ws.rs.PathParam("extensionVersion")
    java.lang.String extensionVersion) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createExtensionLinkResource(null, null, extensionName, extensionVersion));
    }

    org.apache.ambari.server.api.resources.ResourceInstance createExtensionVersionResource(java.lang.String extensionName, java.lang.String extensionVersion) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Extension, extensionName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.ExtensionVersion, extensionVersion);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.ExtensionVersion, mapIds);
    }

    org.apache.ambari.server.api.resources.ResourceInstance createExtensionLinkResource(java.lang.String stackName, java.lang.String stackVersion, java.lang.String extensionName, java.lang.String extensionVersion) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, stackName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, stackVersion);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Extension, extensionName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.ExtensionVersion, extensionVersion);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.ExtensionLink, mapIds);
    }

    org.apache.ambari.server.api.resources.ResourceInstance createExtensionResource(java.lang.String extensionName) {
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.Extension, java.util.Collections.singletonMap(org.apache.ambari.server.controller.spi.Resource.Type.Extension, extensionName));
    }
}