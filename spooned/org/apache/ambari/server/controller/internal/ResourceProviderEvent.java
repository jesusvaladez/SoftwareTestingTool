package org.apache.ambari.server.controller.internal;
public class ResourceProviderEvent {
    private final org.apache.ambari.server.controller.spi.Resource.Type resourceType;

    private final org.apache.ambari.server.controller.internal.ResourceProviderEvent.Type type;

    private final org.apache.ambari.server.controller.spi.Request request;

    private final org.apache.ambari.server.controller.spi.Predicate predicate;

    public ResourceProviderEvent(org.apache.ambari.server.controller.spi.Resource.Type resourceType, org.apache.ambari.server.controller.internal.ResourceProviderEvent.Type type, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) {
        this.resourceType = resourceType;
        this.type = type;
        this.request = request;
        this.predicate = predicate;
    }

    public org.apache.ambari.server.controller.spi.Resource.Type getResourceType() {
        return resourceType;
    }

    public org.apache.ambari.server.controller.internal.ResourceProviderEvent.Type getType() {
        return type;
    }

    public org.apache.ambari.server.controller.spi.Request getRequest() {
        return request;
    }

    public org.apache.ambari.server.controller.spi.Predicate getPredicate() {
        return predicate;
    }

    public enum Type {

        Create,
        Update,
        Delete;}
}