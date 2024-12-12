package org.apache.ambari.server.controller.internal;
@org.apache.ambari.server.StaticallyInject
public class ExtensionVersionResourceProvider extends org.apache.ambari.server.controller.internal.ReadOnlyResourceProvider {
    public static final java.lang.String EXTENSION_VERSION_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Versions", "extension_version");

    public static final java.lang.String EXTENSION_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Versions", "extension_name");

    public static final java.lang.String EXTENSION_VALID_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Versions", "valid");

    public static final java.lang.String EXTENSION_ERROR_SET = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Versions", "extension-errors");

    public static final java.lang.String EXTENSION_PARENT_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Versions", "parent_extension_version");

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.Extension, org.apache.ambari.server.controller.internal.ExtensionVersionResourceProvider.EXTENSION_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.ExtensionVersion, org.apache.ambari.server.controller.internal.ExtensionVersionResourceProvider.EXTENSION_VERSION_PROPERTY_ID).build();

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.ExtensionVersionResourceProvider.EXTENSION_VERSION_PROPERTY_ID, org.apache.ambari.server.controller.internal.ExtensionVersionResourceProvider.EXTENSION_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.ExtensionVersionResourceProvider.EXTENSION_VALID_PROPERTY_ID, org.apache.ambari.server.controller.internal.ExtensionVersionResourceProvider.EXTENSION_ERROR_SET, org.apache.ambari.server.controller.internal.ExtensionVersionResourceProvider.EXTENSION_PARENT_PROPERTY_ID);

    protected ExtensionVersionResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.ExtensionVersion, org.apache.ambari.server.controller.internal.ExtensionVersionResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.ExtensionVersionResourceProvider.keyPropertyIds, managementController);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.ExtensionVersionRequest> requests = new java.util.HashSet<>();
        if (predicate == null) {
            requests.add(getRequest(java.util.Collections.emptyMap()));
        } else {
            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
                requests.add(getRequest(propertyMap));
            }
        }
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        java.util.Set<org.apache.ambari.server.controller.ExtensionVersionResponse> responses = getResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.util.Set<org.apache.ambari.server.controller.ExtensionVersionResponse>>() {
            @java.lang.Override
            public java.util.Set<org.apache.ambari.server.controller.ExtensionVersionResponse> invoke() throws org.apache.ambari.server.AmbariException {
                return getManagementController().getExtensionVersions(requests);
            }
        });
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.ExtensionVersionResponse response : responses) {
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.ExtensionVersion);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ExtensionVersionResourceProvider.EXTENSION_NAME_PROPERTY_ID, response.getExtensionName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ExtensionVersionResourceProvider.EXTENSION_VERSION_PROPERTY_ID, response.getExtensionVersion(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ExtensionVersionResourceProvider.EXTENSION_VALID_PROPERTY_ID, response.isValid(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ExtensionVersionResourceProvider.EXTENSION_ERROR_SET, response.getErrors(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ExtensionVersionResourceProvider.EXTENSION_PARENT_PROPERTY_ID, response.getParentVersion(), requestedIds);
            resources.add(resource);
        }
        return resources;
    }

    private org.apache.ambari.server.controller.ExtensionVersionRequest getRequest(java.util.Map<java.lang.String, java.lang.Object> properties) {
        return new org.apache.ambari.server.controller.ExtensionVersionRequest(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ExtensionVersionResourceProvider.EXTENSION_NAME_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ExtensionVersionResourceProvider.EXTENSION_VERSION_PROPERTY_ID))));
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.ExtensionVersionResourceProvider.keyPropertyIds.values());
    }
}