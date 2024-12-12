package org.apache.ambari.server.controller.internal;
public class RequestStatusImpl implements org.apache.ambari.server.controller.spi.RequestStatus {
    private final org.apache.ambari.server.controller.spi.Resource requestResource;

    private final java.util.Set<org.apache.ambari.server.controller.spi.Resource> associatedResources;

    private final org.apache.ambari.server.controller.spi.RequestStatusMetaData requestStatusMetaData;

    public RequestStatusImpl(org.apache.ambari.server.controller.spi.Resource requestResource) {
        this(requestResource, null, null);
    }

    public RequestStatusImpl(org.apache.ambari.server.controller.spi.Resource requestResource, java.util.Set<org.apache.ambari.server.controller.spi.Resource> associatedResources) {
        this(requestResource, associatedResources, null);
    }

    public RequestStatusImpl(org.apache.ambari.server.controller.spi.Resource requestResource, java.util.Set<org.apache.ambari.server.controller.spi.Resource> associatedResources, org.apache.ambari.server.controller.spi.RequestStatusMetaData requestStatusMetaData) {
        this.requestResource = requestResource;
        this.associatedResources = (associatedResources == null) ? java.util.Collections.emptySet() : associatedResources;
        this.requestStatusMetaData = requestStatusMetaData;
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getAssociatedResources() {
        return associatedResources;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.Resource getRequestResource() {
        return requestResource;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus.Status getStatus() {
        return requestResource == null ? org.apache.ambari.server.controller.spi.RequestStatus.Status.Complete : org.apache.ambari.server.controller.spi.RequestStatus.Status.valueOf(((java.lang.String) (requestResource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Requests", "status")))));
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatusMetaData getStatusMetadata() {
        return requestStatusMetaData;
    }
}