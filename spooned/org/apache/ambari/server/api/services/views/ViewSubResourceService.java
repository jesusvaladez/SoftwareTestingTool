package org.apache.ambari.server.api.services.views;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
public class ViewSubResourceService extends org.apache.ambari.server.api.services.BaseService implements org.apache.ambari.view.ViewResourceHandler {
    private final org.apache.ambari.server.controller.spi.Resource.Type type;

    private final java.lang.String viewName;

    private final java.lang.String version;

    private final java.lang.String instanceName;

    public ViewSubResourceService(org.apache.ambari.server.controller.spi.Resource.Type type, org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition) {
        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = viewInstanceDefinition.getViewEntity();
        this.type = type;
        this.viewName = viewEntity.getCommonName();
        this.version = viewEntity.getVersion();
        this.instanceName = viewInstanceDefinition.getName();
    }

    @java.lang.Override
    public javax.ws.rs.core.Response handleRequest(javax.ws.rs.core.HttpHeaders headers, javax.ws.rs.core.UriInfo ui, org.apache.ambari.view.ViewResourceHandler.RequestType requestType, org.apache.ambari.view.ViewResourceHandler.MediaType mediaType, java.lang.String resourceId) {
        return handleRequest(headers, null, ui, getRequestType(requestType), getMediaType(mediaType), createResource(resourceId));
    }

    @java.lang.Override
    public javax.ws.rs.core.Response handleRequest(javax.ws.rs.core.HttpHeaders headers, javax.ws.rs.core.UriInfo ui, java.lang.String resourceId) {
        return handleRequest(headers, null, ui, org.apache.ambari.server.api.services.Request.Type.GET, createResource(resourceId));
    }

    protected org.apache.ambari.server.api.resources.ResourceInstance createResource(java.lang.String resourceId) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> mapIds = new java.util.HashMap<>();
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.View, viewName);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.ViewVersion, version);
        mapIds.put(org.apache.ambari.server.controller.spi.Resource.Type.ViewInstance, instanceName);
        if (resourceId != null) {
            mapIds.put(type, resourceId);
        }
        return super.createResource(type, mapIds);
    }

    private org.apache.ambari.server.api.services.Request.Type getRequestType(org.apache.ambari.view.ViewResourceHandler.RequestType type) {
        switch (type) {
            case GET :
                return org.apache.ambari.server.api.services.Request.Type.GET;
            case POST :
                return org.apache.ambari.server.api.services.Request.Type.POST;
            case PUT :
                return org.apache.ambari.server.api.services.Request.Type.PUT;
            case DELETE :
                return org.apache.ambari.server.api.services.Request.Type.DELETE;
            case QUERY_POST :
                return org.apache.ambari.server.api.services.Request.Type.QUERY_POST;
        }
        throw new java.lang.IllegalArgumentException("Unknown resource type " + type);
    }

    private javax.ws.rs.core.MediaType getMediaType(org.apache.ambari.view.ViewResourceHandler.MediaType type) {
        switch (type) {
            case TEXT_PLAIN :
                return org.apache.ambari.server.api.services.views.MediaType;
            case APPLICATION_JSON :
                return javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
        }
        throw new java.lang.IllegalArgumentException("Unknown media type " + type);
    }
}