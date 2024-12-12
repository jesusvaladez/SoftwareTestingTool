package org.apache.ambari.server.controller.internal;
public class OperatingSystemResourceProvider extends org.apache.ambari.server.controller.internal.ReadOnlyResourceProvider {
    public static final java.lang.String OPERATING_SYSTEM_STACK_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("OperatingSystems", "stack_name");

    public static final java.lang.String OPERATING_SYSTEM_STACK_VERSION_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("OperatingSystems", "stack_version");

    public static final java.lang.String OPERATING_SYSTEM_OS_TYPE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("OperatingSystems", "os_type");

    public static final java.lang.String OPERATING_SYSTEM_REPOSITORY_VERSION_ID_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("OperatingSystems", "repository_version_id");

    public static final java.lang.String OPERATING_SYSTEM_VERSION_DEFINITION_ID_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("OperatingSystems", "version_definition_id");

    public static final java.lang.String OPERATING_SYSTEM_AMBARI_MANAGED_REPOS = "OperatingSystems/ambari_managed_repositories";

    private static final java.util.Set<java.lang.String> pkPropertyIds = com.google.common.collect.ImmutableSet.of(org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.OPERATING_SYSTEM_OS_TYPE_PROPERTY_ID, org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.OPERATING_SYSTEM_STACK_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.OPERATING_SYSTEM_STACK_VERSION_PROPERTY_ID);

    public static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.ImmutableSet.of(org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.OPERATING_SYSTEM_OS_TYPE_PROPERTY_ID, org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.OPERATING_SYSTEM_STACK_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.OPERATING_SYSTEM_STACK_VERSION_PROPERTY_ID, org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.OPERATING_SYSTEM_REPOSITORY_VERSION_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.OPERATING_SYSTEM_VERSION_DEFINITION_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.OPERATING_SYSTEM_AMBARI_MANAGED_REPOS);

    public static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.OperatingSystem, org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.OPERATING_SYSTEM_OS_TYPE_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.OPERATING_SYSTEM_STACK_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.OPERATING_SYSTEM_STACK_VERSION_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.RepositoryVersion, org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.OPERATING_SYSTEM_REPOSITORY_VERSION_ID_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.CompatibleRepositoryVersion, org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.OPERATING_SYSTEM_REPOSITORY_VERSION_ID_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.VersionDefinition, org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.OPERATING_SYSTEM_VERSION_DEFINITION_ID_PROPERTY_ID).build();

    protected OperatingSystemResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.OperatingSystem, org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.keyPropertyIds, managementController);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.OperatingSystemRequest> requests = new java.util.HashSet<>();
        if (predicate == null) {
            requests.add(getRequest(java.util.Collections.emptyMap()));
        } else {
            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
                requests.add(getRequest(propertyMap));
            }
        }
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        java.util.Set<org.apache.ambari.server.controller.OperatingSystemResponse> responses = getResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.util.Set<org.apache.ambari.server.controller.OperatingSystemResponse>>() {
            @java.lang.Override
            public java.util.Set<org.apache.ambari.server.controller.OperatingSystemResponse> invoke() throws org.apache.ambari.server.AmbariException {
                return getManagementController().getOperatingSystems(requests);
            }
        });
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.OperatingSystemResponse response : responses) {
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.OperatingSystem);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.OPERATING_SYSTEM_STACK_NAME_PROPERTY_ID, response.getStackName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.OPERATING_SYSTEM_STACK_VERSION_PROPERTY_ID, response.getStackVersion(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.OPERATING_SYSTEM_OS_TYPE_PROPERTY_ID, response.getOsType(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.OPERATING_SYSTEM_AMBARI_MANAGED_REPOS, response.isAmbariManagedRepos(), requestedIds);
            if (response.getRepositoryVersionId() != null) {
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.OPERATING_SYSTEM_REPOSITORY_VERSION_ID_PROPERTY_ID, response.getRepositoryVersionId(), requestedIds);
            }
            if (response.getVersionDefinitionId() != null) {
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.OPERATING_SYSTEM_VERSION_DEFINITION_ID_PROPERTY_ID, response.getVersionDefinitionId(), requestedIds);
            }
            resources.add(resource);
        }
        return resources;
    }

    private org.apache.ambari.server.controller.OperatingSystemRequest getRequest(java.util.Map<java.lang.String, java.lang.Object> properties) {
        final org.apache.ambari.server.controller.OperatingSystemRequest request = new org.apache.ambari.server.controller.OperatingSystemRequest(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.OPERATING_SYSTEM_STACK_NAME_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.OPERATING_SYSTEM_STACK_VERSION_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.OPERATING_SYSTEM_OS_TYPE_PROPERTY_ID))));
        if (properties.containsKey(org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.OPERATING_SYSTEM_REPOSITORY_VERSION_ID_PROPERTY_ID)) {
            request.setRepositoryVersionId(java.lang.Long.parseLong(properties.get(org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.OPERATING_SYSTEM_REPOSITORY_VERSION_ID_PROPERTY_ID).toString()));
        }
        if (properties.containsKey(org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.OPERATING_SYSTEM_VERSION_DEFINITION_ID_PROPERTY_ID)) {
            request.setVersionDefinitionId(properties.get(org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.OPERATING_SYSTEM_VERSION_DEFINITION_ID_PROPERTY_ID).toString());
        }
        return request;
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.pkPropertyIds;
    }
}