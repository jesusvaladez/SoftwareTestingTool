package org.apache.ambari.server.api.services;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
public class OperatingSystemService extends org.apache.ambari.server.api.services.BaseService {
    private java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> parentKeyProperties;

    public OperatingSystemService(java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> parentKeyProperties) {
        this.parentKeyProperties = parentKeyProperties;
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getOperatingSystems(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createResource(null));
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{osType}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getOperatingSystem(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("osType")
    java.lang.String osType) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createResource(osType));
    }

    @javax.ws.rs.Path("{osType}/repositories")
    public org.apache.ambari.server.api.services.RepositoryService getOperatingSystemsHandler(@javax.ws.rs.PathParam("osType")
    java.lang.String osType) {
        final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.putAll(parentKeyProperties);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.OperatingSystem, osType);
        return new org.apache.ambari.server.api.services.RepositoryService(mapIds);
    }

    private org.apache.ambari.server.api.resources.ResourceInstance createResource(java.lang.String osType) {
        final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.putAll(parentKeyProperties);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.OperatingSystem, osType);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.OperatingSystem, mapIds);
    }
}