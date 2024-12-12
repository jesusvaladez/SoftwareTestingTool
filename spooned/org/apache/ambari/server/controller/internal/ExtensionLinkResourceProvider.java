package org.apache.ambari.server.controller.internal;
@org.apache.ambari.server.StaticallyInject
public class ExtensionLinkResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider {
    public static final java.lang.String LINK_ID_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ExtensionLink", "link_id");

    public static final java.lang.String STACK_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ExtensionLink", "stack_name");

    public static final java.lang.String STACK_VERSION_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ExtensionLink", "stack_version");

    public static final java.lang.String EXTENSION_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ExtensionLink", "extension_name");

    public static final java.lang.String EXTENSION_VERSION_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("ExtensionLink", "extension_version");

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.ExtensionLink, org.apache.ambari.server.controller.internal.ExtensionLinkResourceProvider.LINK_ID_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, org.apache.ambari.server.controller.internal.ExtensionLinkResourceProvider.STACK_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, org.apache.ambari.server.controller.internal.ExtensionLinkResourceProvider.STACK_VERSION_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.Extension, org.apache.ambari.server.controller.internal.ExtensionLinkResourceProvider.EXTENSION_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.ExtensionVersion, org.apache.ambari.server.controller.internal.ExtensionLinkResourceProvider.EXTENSION_VERSION_PROPERTY_ID).build();

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.ExtensionLinkResourceProvider.LINK_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.ExtensionLinkResourceProvider.STACK_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.ExtensionLinkResourceProvider.STACK_VERSION_PROPERTY_ID, org.apache.ambari.server.controller.internal.ExtensionLinkResourceProvider.EXTENSION_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.ExtensionLinkResourceProvider.EXTENSION_VERSION_PROPERTY_ID);

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.ExtensionLinkDAO dao;

    protected ExtensionLinkResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.ExtensionLink, org.apache.ambari.server.controller.internal.ExtensionLinkResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.ExtensionLinkResourceProvider.keyPropertyIds, managementController);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException {
        final java.util.Set<org.apache.ambari.server.controller.ExtensionLinkRequest> requests = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : request.getProperties()) {
            requests.add(getRequest(propertyMap));
        }
        createResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                for (org.apache.ambari.server.controller.ExtensionLinkRequest extensionLinkRequest : requests) {
                    getManagementController().createExtensionLink(extensionLinkRequest);
                }
                return null;
            }
        });
        if (requests.size() > 0) {
            try {
                getManagementController().updateStacks();
            } catch (org.apache.ambari.server.AmbariException e) {
                throw new org.apache.ambari.server.controller.spi.SystemException(e.getMessage(), e);
            }
            notifyCreate(org.apache.ambari.server.controller.spi.Resource.Type.ExtensionLink, request);
        }
        return getRequestStatus(null);
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus deleteResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.ExtensionLinkRequest> requests = new java.util.HashSet<>();
        if (predicate == null) {
            requests.add(getRequest(java.util.Collections.emptyMap()));
        } else {
            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
                requests.add(getRequest(propertyMap));
            }
        }
        org.apache.ambari.server.controller.RequestStatusResponse response = modifyResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<org.apache.ambari.server.controller.RequestStatusResponse>() {
            @java.lang.Override
            public org.apache.ambari.server.controller.RequestStatusResponse invoke() throws org.apache.ambari.server.AmbariException {
                for (org.apache.ambari.server.controller.ExtensionLinkRequest extensionLinkRequest : requests) {
                    getManagementController().deleteExtensionLink(extensionLinkRequest);
                }
                return null;
            }
        });
        try {
            getManagementController().updateStacks();
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new org.apache.ambari.server.controller.spi.SystemException(e.getMessage(), e);
        }
        notifyDelete(org.apache.ambari.server.controller.spi.Resource.Type.ExtensionLink, predicate);
        return getRequestStatus(response);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        final java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        final java.util.Set<org.apache.ambari.server.controller.ExtensionLinkRequest> requests = new java.util.HashSet<>();
        if (predicate == null) {
            requests.add(getRequest(java.util.Collections.emptyMap()));
        } else {
            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
                requests.add(getRequest(propertyMap));
            }
        }
        java.util.Set<org.apache.ambari.server.orm.entities.ExtensionLinkEntity> entities = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.ExtensionLinkRequest extensionLinkRequest : requests) {
            verifyStackAndExtensionExist(extensionLinkRequest);
            entities.addAll(org.apache.ambari.server.controller.internal.ExtensionLinkResourceProvider.dao.find(extensionLinkRequest));
        }
        for (org.apache.ambari.server.orm.entities.ExtensionLinkEntity entity : entities) {
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.ExtensionLink);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ExtensionLinkResourceProvider.LINK_ID_PROPERTY_ID, entity.getLinkId(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ExtensionLinkResourceProvider.STACK_NAME_PROPERTY_ID, entity.getStack().getStackName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ExtensionLinkResourceProvider.STACK_VERSION_PROPERTY_ID, entity.getStack().getStackVersion(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ExtensionLinkResourceProvider.EXTENSION_NAME_PROPERTY_ID, entity.getExtension().getExtensionName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ExtensionLinkResourceProvider.EXTENSION_VERSION_PROPERTY_ID, entity.getExtension().getExtensionVersion(), requestedIds);
            resources.add(resource);
        }
        return resources;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.ExtensionLinkRequest> requests = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : request.getProperties()) {
            requests.add(getRequest(propertyMap));
        }
        modifyResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<org.apache.ambari.server.controller.RequestStatusResponse>() {
            @java.lang.Override
            public org.apache.ambari.server.controller.RequestStatusResponse invoke() throws org.apache.ambari.server.AmbariException {
                for (org.apache.ambari.server.controller.ExtensionLinkRequest extensionLinkRequest : requests) {
                    getManagementController().updateExtensionLink(extensionLinkRequest);
                }
                return null;
            }
        });
        try {
            getManagementController().updateStacks();
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new org.apache.ambari.server.controller.spi.SystemException(e.getMessage(), e);
        }
        notifyUpdate(org.apache.ambari.server.controller.spi.Resource.Type.ExtensionLink, request, predicate);
        return getRequestStatus(null);
    }

    private void verifyStackAndExtensionExist(org.apache.ambari.server.controller.ExtensionLinkRequest request) throws org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        try {
            if ((request.getStackName() != null) && (request.getStackVersion() != null)) {
                getManagementController().getAmbariMetaInfo().getStack(request.getStackName(), request.getStackVersion());
            }
            if ((request.getExtensionName() != null) && (request.getExtensionVersion() != null)) {
                getManagementController().getAmbariMetaInfo().getExtension(request.getExtensionName(), request.getExtensionVersion());
            }
        } catch (org.apache.ambari.server.AmbariException ambariException) {
            throw new org.apache.ambari.server.controller.spi.NoSuchParentResourceException(ambariException.getMessage());
        }
    }

    private org.apache.ambari.server.controller.ExtensionLinkRequest getRequest(java.util.Map<java.lang.String, java.lang.Object> properties) {
        return new org.apache.ambari.server.controller.ExtensionLinkRequest(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ExtensionLinkResourceProvider.LINK_ID_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ExtensionLinkResourceProvider.STACK_NAME_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ExtensionLinkResourceProvider.STACK_VERSION_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ExtensionLinkResourceProvider.EXTENSION_NAME_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ExtensionLinkResourceProvider.EXTENSION_VERSION_PROPERTY_ID))));
    }

    private org.apache.ambari.server.controller.ExtensionLinkRequest createExtensionLinkRequest(org.apache.ambari.server.orm.entities.ExtensionLinkEntity entity) {
        if (entity == null) {
            return null;
        }
        return new org.apache.ambari.server.controller.ExtensionLinkRequest(java.lang.String.valueOf(entity.getLinkId()), entity.getStack().getStackName(), entity.getStack().getStackVersion(), entity.getExtension().getExtensionName(), entity.getExtension().getExtensionVersion());
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.ExtensionLinkResourceProvider.keyPropertyIds.values());
    }
}