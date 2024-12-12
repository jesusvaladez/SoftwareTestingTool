package org.apache.ambari.server.controller.internal;
public abstract class ReadOnlyResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider {
    private static final java.lang.String READ_ONLY_MSG = "Read-only resource";

    ReadOnlyResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type type, java.util.Set<java.lang.String> propertyIds, java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds, org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(type, propertyIds, keyPropertyIds, managementController);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new org.apache.ambari.server.controller.spi.SystemException(org.apache.ambari.server.controller.internal.ReadOnlyResourceProvider.READ_ONLY_MSG, null);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new org.apache.ambari.server.controller.spi.SystemException(org.apache.ambari.server.controller.internal.ReadOnlyResourceProvider.READ_ONLY_MSG, null);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new org.apache.ambari.server.controller.spi.SystemException(org.apache.ambari.server.controller.internal.ReadOnlyResourceProvider.READ_ONLY_MSG, null);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new org.apache.ambari.server.controller.spi.SystemException(org.apache.ambari.server.controller.internal.ReadOnlyResourceProvider.READ_ONLY_MSG, null);
    }

    public org.apache.ambari.server.controller.spi.Predicate amendPredicate(org.apache.ambari.server.controller.spi.Predicate predicate) {
        return null;
    }
}