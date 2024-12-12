package org.apache.ambari.server.api.services;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
public class ClusterKerberosDescriptorService extends org.apache.ambari.server.api.services.BaseService {
    private java.lang.String clusterName;

    public ClusterKerberosDescriptorService(java.lang.String clusterName) {
        this.clusterName = clusterName;
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getKerberosDescriptors(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createResource(null));
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{type}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getKerberosDescriptor(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("type")
    java.lang.String type) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createResource(type));
    }

    org.apache.ambari.server.api.resources.ResourceInstance createResource(java.lang.String type) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, clusterName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.ClusterKerberosDescriptor, type);
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.ClusterKerberosDescriptor, mapIds);
    }
}