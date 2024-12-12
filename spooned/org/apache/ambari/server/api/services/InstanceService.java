package org.apache.ambari.server.api.services;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
@javax.ws.rs.Path("/instances/")
public class InstanceService extends org.apache.ambari.server.api.services.BaseService {
    private java.lang.String m_feedName;

    public InstanceService() {
    }

    public InstanceService(java.lang.String feedName) {
        m_feedName = feedName;
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{instanceID}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getInstance(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("instanceID")
    java.lang.String instanceID) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createInstanceResource(m_feedName, instanceID, ui));
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getInstances(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createInstanceResource(m_feedName, null, ui));
    }

    @javax.ws.rs.POST
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{instanceID}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response createInstance(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("instanceID")
    java.lang.String instanceID) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.POST, createInstanceResource(m_feedName, instanceID, ui));
    }

    @javax.ws.rs.PUT
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{instanceID}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response updateInstance(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("instanceID")
    java.lang.String instanceID) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.PUT, createInstanceResource(m_feedName, instanceID, ui));
    }

    @javax.ws.rs.DELETE
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Path("{instanceID}")
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response deleteInstance(@javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui, @javax.ws.rs.PathParam("instanceID")
    java.lang.String instanceID) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.DELETE, createInstanceResource(m_feedName, instanceID, ui));
    }

    org.apache.ambari.server.api.resources.ResourceInstance createInstanceResource(java.lang.String feedName, java.lang.String instanceID, javax.ws.rs.core.UriInfo ui) {
        boolean isAttached = ui.getRequestUri().toString().contains("/feeds/");
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.DRInstance, instanceID);
        if (isAttached) {
            mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.DRFeed, feedName);
        }
        return createResource(org.apache.ambari.server.controller.spi.Resource.Type.DRInstance, mapIds);
    }
}