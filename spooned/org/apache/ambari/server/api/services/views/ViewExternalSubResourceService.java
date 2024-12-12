package org.apache.ambari.server.api.services.views;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
public class ViewExternalSubResourceService extends org.apache.ambari.server.api.services.BaseService {
    private final org.apache.ambari.server.controller.spi.Resource.Type type;

    private final java.lang.String viewName;

    private final java.lang.String version;

    private final java.lang.String instanceName;

    private final java.util.Map<java.lang.String, java.lang.Object> resourceServiceMap = new java.util.HashMap<>();

    public ViewExternalSubResourceService(org.apache.ambari.server.controller.spi.Resource.Type type, org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition) {
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = viewInstanceDefinition.getViewEntity();
        this.type = type;
        this.viewName = viewEntity.getCommonName();
        this.version = viewEntity.getVersion();
        this.instanceName = viewInstanceDefinition.getName();
    }

    @javax.ws.rs.GET
    @org.apache.ambari.annotations.ApiIgnore
    @javax.ws.rs.Produces("text/plain")
    public javax.ws.rs.core.Response getResources(java.lang.String body, @javax.ws.rs.core.Context
    javax.ws.rs.core.HttpHeaders headers, @javax.ws.rs.core.Context
    javax.ws.rs.core.UriInfo ui) {
        return handleRequest(headers, body, ui, org.apache.ambari.server.api.services.Request.Type.GET, createResource(viewName, instanceName));
    }

    @javax.ws.rs.Path("{resourceName}")
    public java.lang.Object getResource(@javax.ws.rs.PathParam("resourceName")
    java.lang.String resourceName) throws java.io.IOException {
        java.lang.Object service = resourceServiceMap.get(resourceName);
        if (service == null) {
            throw new java.lang.IllegalArgumentException(((((("A resource type " + resourceName) + " for view instance ") + viewName) + "/") + instanceName) + " can not be found.");
        }
        return service;
    }

    public void addResourceService(java.lang.String resourceName, java.lang.Object service) {
        resourceServiceMap.put(resourceName, service);
    }

    private org.apache.ambari.server.api.resources.ResourceInstance createResource(java.lang.String viewName, java.lang.String instanceName) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.View, viewName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.ViewVersion, version);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.ViewInstance, instanceName);
        return createResource(type, mapIds);
    }
}