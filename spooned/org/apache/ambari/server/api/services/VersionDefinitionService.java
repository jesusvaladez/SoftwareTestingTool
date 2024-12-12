package org.apache.ambari.server.api.services;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
@javax.ws.rs.Path("/version_definitions/")
public class VersionDefinitionService extends org.apache.ambari.server.api.services.BaseService {
    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public javax.ws.rs.core.Response getServices(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createResource(null));
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{versionId}")
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public javax.ws.rs.core.Response getService(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("versionId")
    java.lang.String versionId) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createResource(versionId));
    }

    @javax.ws.rs.Path("{versionNumber}/operating_systems")
    public org.apache.ambari.server.api.services.OperatingSystemService getOperatingSystemsHandler(@javax.ws.rs.PathParam("versionNumber")
    java.lang.String versionNumber) {
        final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.VersionDefinition, versionNumber);
        return new org.apache.ambari.server.api.services.OperatingSystemService(mapIds);
    }

    @javax.ws.rs.POST
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public javax.ws.rs.core.Response createVersion(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.POST, createResource(null));
    }

    @javax.ws.rs.POST
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Consumes({ javax.ws.rs.core.MediaType.TEXT_XML })
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.TEXT_PLAIN)
    public javax.ws.rs.core.Response createVersionByXml(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) throws java.lang.Exception {
        java.lang.String encoded = org.apache.commons.codec.binary.Base64.encodeBase64String(body.getBytes("UTF-8"));
        com.google.gson.JsonObject obj = new com.google.gson.JsonObject();
        obj.addProperty(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF_BASE64_PROPERTY, encoded);
        com.google.gson.JsonObject payload = new com.google.gson.JsonObject();
        payload.add(org.apache.ambari.server.controller.internal.VersionDefinitionResourceProvider.VERSION_DEF, obj);
        return handleRequest(headers, payload.toString(), ui, org.apache.ambari.server.api.services.Request.Type.POST, createResource(null));
    }

    protected org.apache.ambari.server.api.resources.ResourceInstance createResource(java.lang.String versionId) {
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.VersionDefinition, java.util.Collections.singletonMap(org.apache.ambari.server.controller.spi.Resource.Type.VersionDefinition, versionId));
    }
}