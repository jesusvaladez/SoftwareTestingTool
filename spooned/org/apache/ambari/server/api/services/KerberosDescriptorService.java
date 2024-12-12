package org.apache.ambari.server.api.services;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
@javax.ws.rs.Path("/kerberos_descriptors/")
public class KerberosDescriptorService extends org.apache.ambari.server.api.services.BaseService {
    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getKerberosDescriptors(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createKerberosDescriptorResource(null));
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{kerberosDescriptorName}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getKerberosDescriptor(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("kerberosDescriptorName")
    java.lang.String kerberosDescriptorName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createKerberosDescriptorResource(kerberosDescriptorName));
    }

    @javax.ws.rs.POST
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{kerberosDescriptorName}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response createKerberosDescriptor(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("kerberosDescriptorName")
    java.lang.String kerberosDescriptorName) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.POST, createKerberosDescriptorResource(kerberosDescriptorName));
    }

    @javax.ws.rs.DELETE
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{kerberosDescriptorName}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response deleteKerberosDescriptor(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("kerberosDescriptorName")
    java.lang.String kerberosDescriptorName) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.DELETE, createKerberosDescriptorResource(kerberosDescriptorName));
    }

    private org.apache.ambari.server.api.resources.ResourceInstance createKerberosDescriptorResource(java.lang.String kerberosDescriptorName) {
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.KerberosDescriptor, java.util.Collections.singletonMap(org.apache.ambari.server.controller.spi.Resource.Type.KerberosDescriptor, kerberosDescriptorName));
    }
}