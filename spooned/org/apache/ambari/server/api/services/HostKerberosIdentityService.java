package org.apache.ambari.server.api.services;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
public class HostKerberosIdentityService extends org.apache.ambari.server.api.services.BaseService {
    private java.lang.String clusterName;

    private java.lang.String hostName;

    public HostKerberosIdentityService(java.lang.String clusterName, java.lang.String hostName) {
        this.clusterName = clusterName;
        this.hostName = hostName;
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{kerberosIdentityID}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getKerberosIdentity(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("kerberosIdentityID")
    java.lang.String identityID, @javax.ws.rs.QueryParam("format")
    java.lang.String format) {
        javax.ws.rs.core.MediaType mediaType;
        if ("csv".equalsIgnoreCase(format)) {
            mediaType = org.apache.ambari.server.api.services.BaseService.MEDIA_TYPE_TEXT_CSV_TYPE;
        } else {
            mediaType = null;
        }
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, mediaType, createResource(clusterName, hostName, identityID));
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getKerberosIdentities(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.QueryParam("format")
    java.lang.String format) {
        return getKerberosIdentity(body, headers, ui, null, format);
    }

    org.apache.ambari.server.api.resources.ResourceInstance createResource(java.lang.String clusterName, java.lang.String hostName, java.lang.String identityId) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, clusterName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Host, hostName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.HostKerberosIdentity, identityId);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.HostKerberosIdentity, mapIds);
    }
}