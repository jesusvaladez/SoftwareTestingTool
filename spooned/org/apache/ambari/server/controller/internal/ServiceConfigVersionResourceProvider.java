package org.apache.ambari.server.controller.internal;
public class ServiceConfigVersionResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider {
    public static final java.lang.String CLUSTER_NAME_PROPERTY_ID = "cluster_name";

    public static final java.lang.String SERVICE_CONFIG_VERSION_PROPERTY_ID = "service_config_version";

    public static final java.lang.String SERVICE_NAME_PROPERTY_ID = "service_name";

    public static final java.lang.String CREATE_TIME_PROPERTY_ID = "createtime";

    public static final java.lang.String USER_PROPERTY_ID = "user";

    public static final java.lang.String SERVICE_CONFIG_VERSION_NOTE_PROPERTY_ID = "service_config_version_note";

    public static final java.lang.String GROUP_ID_PROPERTY_ID = "group_id";

    public static final java.lang.String GROUP_NAME_PROPERTY_ID = "group_name";

    public static final java.lang.String STACK_ID_PROPERTY_ID = "stack_id";

    public static final java.lang.String IS_CURRENT_PROPERTY_ID = "is_current";

    public static final java.lang.String IS_COMPATIBLE_PROPERTY_ID = "is_cluster_compatible";

    public static final java.lang.String HOSTS_PROPERTY_ID = "hosts";

    public static final java.lang.String CONFIGURATIONS_PROPERTY_ID = "configurations";

    public static final java.lang.String APPLIED_TIME_PROPERTY_ID = "appliedtime";

    private static final java.util.Set<java.lang.String> PROPERTY_IDS = new java.util.HashSet<>();

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> KEY_PROPERTY_IDS = new java.util.HashMap<>();

    static {
        PROPERTY_IDS.add(CLUSTER_NAME_PROPERTY_ID);
        PROPERTY_IDS.add(SERVICE_CONFIG_VERSION_PROPERTY_ID);
        PROPERTY_IDS.add(SERVICE_NAME_PROPERTY_ID);
        PROPERTY_IDS.add(CREATE_TIME_PROPERTY_ID);
        PROPERTY_IDS.add(USER_PROPERTY_ID);
        PROPERTY_IDS.add(SERVICE_CONFIG_VERSION_NOTE_PROPERTY_ID);
        PROPERTY_IDS.add(GROUP_ID_PROPERTY_ID);
        PROPERTY_IDS.add(GROUP_NAME_PROPERTY_ID);
        PROPERTY_IDS.add(STACK_ID_PROPERTY_ID);
        PROPERTY_IDS.add(IS_CURRENT_PROPERTY_ID);
        PROPERTY_IDS.add(HOSTS_PROPERTY_ID);
        PROPERTY_IDS.add(CONFIGURATIONS_PROPERTY_ID);
        PROPERTY_IDS.add(IS_COMPATIBLE_PROPERTY_ID);
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.Service, SERVICE_NAME_PROPERTY_ID);
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, CLUSTER_NAME_PROPERTY_ID);
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.ServiceConfigVersion, SERVICE_CONFIG_VERSION_PROPERTY_ID);
    }

    private static final java.util.Set<java.lang.String> pkPropertyIds = new java.util.HashSet<>(java.util.Arrays.asList(new java.lang.String[]{ org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.SERVICE_NAME_PROPERTY_ID }));

    ServiceConfigVersionResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.ServiceConfigVersion, org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.PROPERTY_IDS, org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.KEY_PROPERTY_IDS, managementController);
        setRequiredGetAuthorizations(java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_CONFIGS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_VIEW_CONFIGS, org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_COMPARE_CONFIGS));
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.pkPropertyIds;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("Cannot explicitly create service config version");
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.ServiceConfigVersionRequest> requests = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> properties : getPropertyMaps(predicate)) {
            requests.add(createRequest(properties));
        }
        java.util.Set<org.apache.ambari.server.controller.ServiceConfigVersionResponse> responses = getResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.util.Set<org.apache.ambari.server.controller.ServiceConfigVersionResponse>>() {
            @java.lang.Override
            public java.util.Set<org.apache.ambari.server.controller.ServiceConfigVersionResponse> invoke() throws org.apache.ambari.server.AmbariException {
                return getManagementController().getServiceConfigVersions(requests);
            }
        });
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.ServiceConfigVersionResponse response : responses) {
            java.lang.String clusterName = response.getClusterName();
            java.util.List<org.apache.ambari.server.controller.ConfigurationResponse> configurationResponses = response.getConfigurations();
            java.util.List<java.util.Map<java.lang.String, java.lang.Object>> configVersionConfigurations = convertToSubResources(clusterName, configurationResponses);
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.ServiceConfigVersion);
            resource.setProperty(org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.CLUSTER_NAME_PROPERTY_ID, clusterName);
            resource.setProperty(org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.SERVICE_NAME_PROPERTY_ID, response.getServiceName());
            resource.setProperty(org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.USER_PROPERTY_ID, response.getUserName());
            resource.setProperty(org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.SERVICE_CONFIG_VERSION_PROPERTY_ID, response.getVersion());
            resource.setProperty(org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.CREATE_TIME_PROPERTY_ID, response.getCreateTime());
            resource.setProperty(org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.CONFIGURATIONS_PROPERTY_ID, configVersionConfigurations);
            resource.setProperty(org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.SERVICE_CONFIG_VERSION_NOTE_PROPERTY_ID, response.getNote());
            resource.setProperty(org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.GROUP_ID_PROPERTY_ID, response.getGroupId());
            resource.setProperty(org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.GROUP_NAME_PROPERTY_ID, response.getGroupName());
            resource.setProperty(org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.HOSTS_PROPERTY_ID, response.getHosts());
            resource.setProperty(org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.STACK_ID_PROPERTY_ID, response.getStackId());
            resource.setProperty(org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.IS_CURRENT_PROPERTY_ID, response.getIsCurrent());
            resource.setProperty(org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.IS_COMPATIBLE_PROPERTY_ID, response.isCompatibleWithCurrentStack());
            resources.add(resource);
        }
        return resources;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("Cannot update service config version");
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("Cannot delete service config version");
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> checkPropertyIds(java.util.Set<java.lang.String> propertyIds) {
        propertyIds = super.checkPropertyIds(propertyIds);
        if (propertyIds.isEmpty()) {
            return propertyIds;
        }
        java.util.Set<java.lang.String> unsupportedProperties = new java.util.HashSet<>();
        for (java.lang.String propertyId : propertyIds) {
            if (((((((((((((!propertyId.equals("cluster_name")) && (!propertyId.equals("service_config_version"))) && (!propertyId.equals("service_name"))) && (!propertyId.equals("createtime"))) && (!propertyId.equals("appliedtime"))) && (!propertyId.equals("user"))) && (!propertyId.equals("service_config_version_note"))) && (!propertyId.equals("group_id"))) && (!propertyId.equals("group_name"))) && (!propertyId.equals("stack_id"))) && (!propertyId.equals("is_current"))) && (!propertyId.equals("is_cluster_compatible"))) && (!propertyId.equals("hosts"))) {
                unsupportedProperties.add(propertyId);
            }
        }
        return unsupportedProperties;
    }

    private org.apache.ambari.server.controller.ServiceConfigVersionRequest createRequest(java.util.Map<java.lang.String, java.lang.Object> properties) {
        java.lang.String clusterName = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.CLUSTER_NAME_PROPERTY_ID)));
        java.lang.String serviceName = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.SERVICE_NAME_PROPERTY_ID)));
        java.lang.String user = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.USER_PROPERTY_ID)));
        java.lang.Boolean isCurrent = java.lang.Boolean.valueOf(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.IS_CURRENT_PROPERTY_ID))));
        java.lang.Object versionObject = properties.get(org.apache.ambari.server.controller.internal.ServiceConfigVersionResourceProvider.SERVICE_CONFIG_VERSION_PROPERTY_ID);
        java.lang.Long version = (versionObject == null) ? null : java.lang.Long.valueOf(versionObject.toString());
        return new org.apache.ambari.server.controller.ServiceConfigVersionRequest(clusterName, serviceName, version, null, null, user, isCurrent);
    }

    private java.util.List<java.util.Map<java.lang.String, java.lang.Object>> convertToSubResources(final java.lang.String clusterName, java.util.List<org.apache.ambari.server.controller.ConfigurationResponse> configs) {
        java.util.List<java.util.Map<java.lang.String, java.lang.Object>> result = new java.util.ArrayList<>();
        for (final org.apache.ambari.server.controller.ConfigurationResponse config : configs) {
            java.util.Map<java.lang.String, java.lang.Object> subResourceMap = new java.util.LinkedHashMap<>();
            java.util.Map<java.lang.String, java.lang.String> configMap = new java.util.HashMap<>();
            java.lang.String stackId = config.getStackId().getStackId();
            configMap.put("cluster_name", clusterName);
            configMap.put("stack_id", stackId);
            subResourceMap.put("Config", configMap);
            subResourceMap.put("type", config.getType());
            subResourceMap.put("tag", config.getVersionTag());
            subResourceMap.put("version", config.getVersion());
            subResourceMap.put("properties", new java.util.TreeMap<>(config.getConfigs()));
            subResourceMap.put("properties_attributes", config.getConfigAttributes());
            result.add(subResourceMap);
        }
        return result;
    }
}