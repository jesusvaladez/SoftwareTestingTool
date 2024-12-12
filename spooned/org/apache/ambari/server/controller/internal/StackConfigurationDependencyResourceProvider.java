package org.apache.ambari.server.controller.internal;
public class StackConfigurationDependencyResourceProvider extends org.apache.ambari.server.controller.internal.ReadOnlyResourceProvider {
    public static final java.lang.String STACK_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackConfigurationDependency", "stack_name");

    public static final java.lang.String STACK_VERSION_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackConfigurationDependency", "stack_version");

    public static final java.lang.String SERVICE_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackConfigurationDependency", "service_name");

    public static final java.lang.String PROPERTY_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackConfigurationDependency", "property_name");

    public static final java.lang.String DEPENDENCY_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackConfigurationDependency", "dependency_name");

    public static final java.lang.String DEPENDENCY_TYPE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackConfigurationDependency", "dependency_type");

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, org.apache.ambari.server.controller.internal.StackConfigurationDependencyResourceProvider.STACK_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, org.apache.ambari.server.controller.internal.StackConfigurationDependencyResourceProvider.STACK_VERSION_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.StackService, org.apache.ambari.server.controller.internal.StackConfigurationDependencyResourceProvider.SERVICE_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.StackConfiguration, org.apache.ambari.server.controller.internal.StackConfigurationDependencyResourceProvider.PROPERTY_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.StackLevelConfiguration, org.apache.ambari.server.controller.internal.StackConfigurationDependencyResourceProvider.PROPERTY_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.StackConfigurationDependency, org.apache.ambari.server.controller.internal.StackConfigurationDependencyResourceProvider.DEPENDENCY_NAME_PROPERTY_ID).build();

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.StackConfigurationDependencyResourceProvider.STACK_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackConfigurationDependencyResourceProvider.STACK_VERSION_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackConfigurationDependencyResourceProvider.SERVICE_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackConfigurationDependencyResourceProvider.PROPERTY_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackConfigurationDependencyResourceProvider.DEPENDENCY_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackConfigurationDependencyResourceProvider.DEPENDENCY_TYPE_PROPERTY_ID);

    protected StackConfigurationDependencyResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.StackConfigurationDependency, org.apache.ambari.server.controller.internal.StackConfigurationDependencyResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.StackConfigurationDependencyResourceProvider.keyPropertyIds, managementController);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(final org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.StackConfigurationDependencyRequest> requests = new java.util.HashSet<>();
        if (predicate == null) {
            requests.add(getRequest(java.util.Collections.emptyMap()));
        } else {
            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
                requests.add(getRequest(propertyMap));
            }
        }
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        java.util.Set<org.apache.ambari.server.controller.StackConfigurationDependencyResponse> responses = getResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.util.Set<org.apache.ambari.server.controller.StackConfigurationDependencyResponse>>() {
            @java.lang.Override
            public java.util.Set<org.apache.ambari.server.controller.StackConfigurationDependencyResponse> invoke() throws org.apache.ambari.server.AmbariException {
                return getManagementController().getStackConfigurationDependencies(requests);
            }
        });
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.StackConfigurationDependencyResponse response : responses) {
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.StackConfigurationDependency);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackConfigurationDependencyResourceProvider.STACK_NAME_PROPERTY_ID, response.getStackName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackConfigurationDependencyResourceProvider.STACK_VERSION_PROPERTY_ID, response.getStackVersion(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackConfigurationDependencyResourceProvider.SERVICE_NAME_PROPERTY_ID, response.getServiceName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackConfigurationDependencyResourceProvider.PROPERTY_NAME_PROPERTY_ID, response.getPropertyName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackConfigurationDependencyResourceProvider.DEPENDENCY_NAME_PROPERTY_ID, response.getDependencyName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackConfigurationDependencyResourceProvider.DEPENDENCY_TYPE_PROPERTY_ID, response.getDependencyType(), requestedIds);
            resources.add(resource);
        }
        return resources;
    }

    private org.apache.ambari.server.controller.StackConfigurationDependencyRequest getRequest(java.util.Map<java.lang.String, java.lang.Object> properties) {
        return new org.apache.ambari.server.controller.StackConfigurationDependencyRequest(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.StackConfigurationDependencyResourceProvider.STACK_NAME_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.StackConfigurationDependencyResourceProvider.STACK_VERSION_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.StackConfigurationDependencyResourceProvider.SERVICE_NAME_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.StackConfigurationDependencyResourceProvider.PROPERTY_NAME_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.StackConfigurationDependencyResourceProvider.DEPENDENCY_NAME_PROPERTY_ID))));
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.StackConfigurationDependencyResourceProvider.keyPropertyIds.values());
    }
}