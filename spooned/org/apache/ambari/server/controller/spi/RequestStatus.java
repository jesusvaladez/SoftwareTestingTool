package org.apache.ambari.server.controller.spi;
public interface RequestStatus {
    java.util.Set<org.apache.ambari.server.controller.spi.Resource> getAssociatedResources();

    org.apache.ambari.server.controller.spi.Resource getRequestResource();

    org.apache.ambari.server.controller.spi.RequestStatus.Status getStatus();

    org.apache.ambari.server.controller.spi.RequestStatusMetaData getStatusMetadata();

    enum Status {

        Accepted,
        InProgress,
        Complete;}
}