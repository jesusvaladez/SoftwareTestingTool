package org.apache.ambari.server.controller.internal;
public class ConfigurationResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider {
    private static final java.util.regex.Pattern PROPERTIES_ATTRIBUTES_PATTERN = java.util.regex.Pattern.compile("^" + org.apache.ambari.server.controller.internal.AbstractResourceProvider.PROPERTIES_ATTRIBUTES_REGEX);

    public static final java.lang.String CONFIG = "Config";

    public static final java.lang.String CLUSTER_NAME_PROPERTY_ID = "cluster_name";

    public static final java.lang.String STACK_ID_PROPERTY_ID = "stack_id";

    public static final java.lang.String TYPE_PROPERTY_ID = "type";

    public static final java.lang.String TAG_PROPERTY_ID = "tag";

    public static final java.lang.String VERSION_PROPERTY_ID = "version";

    public static final java.lang.String PROPERTIES_PROPERTY_ID = "properties";

    public static final java.lang.String PROPERTIES_ATTRIBUTES_PROPERTY_ID = "properties_attributes";

    public static final java.lang.String CLUSTER_NAME = (org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.CONFIG + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.CLUSTER_NAME_PROPERTY_ID;

    public static final java.lang.String STACK_ID = (org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.CONFIG + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.STACK_ID_PROPERTY_ID;

    public static final java.lang.String TYPE = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(null, org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.TYPE_PROPERTY_ID);

    public static final java.lang.String TAG = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(null, org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.TAG_PROPERTY_ID);

    public static final java.lang.String VERSION = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(null, org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.VERSION_PROPERTY_ID);

    private static final java.util.Set<java.lang.String> PROPERTY_IDS = new java.util.HashSet<>();

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> KEY_PROPERTY_IDS = new java.util.HashMap<>();

    static {
        PROPERTY_IDS.add(CLUSTER_NAME);
        PROPERTY_IDS.add(STACK_ID);
        PROPERTY_IDS.add(TYPE);
        PROPERTY_IDS.add(TAG);
        PROPERTY_IDS.add(VERSION);
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.Configuration, TYPE);
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, CLUSTER_NAME);
    }

    private static final java.util.Set<java.lang.String> pkPropertyIds = new java.util.HashSet<>(java.util.Arrays.asList(new java.lang.String[]{ org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.CLUSTER_NAME, org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.TYPE }));

    ConfigurationResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Configuration, org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.PROPERTY_IDS, org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.KEY_PROPERTY_IDS, managementController);
        setRequiredGetAuthorizations(java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_VIEW_CONFIGS));
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        for (java.util.Map<java.lang.String, java.lang.Object> map : request.getProperties()) {
            java.lang.String cluster = ((java.lang.String) (map.get(org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.CLUSTER_NAME)));
            java.lang.String type = ((java.lang.String) (map.get(org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.TYPE)));
            java.lang.String tag = ((java.lang.String) (map.get(org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.TAG)));
            java.util.Map<java.lang.String, java.lang.String> configMap = new java.util.HashMap<>();
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configAttributesMap = null;
            for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : map.entrySet()) {
                java.lang.String propertyCategory = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(entry.getKey());
                if (((propertyCategory != null) && propertyCategory.equals("properties")) && (null != entry.getValue())) {
                    configMap.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName(entry.getKey()), entry.getValue().toString());
                }
                if (((propertyCategory != null) && org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.PROPERTIES_ATTRIBUTES_PATTERN.matcher(propertyCategory).matches()) && (null != entry.getValue())) {
                    if (null == configAttributesMap) {
                        configAttributesMap = new java.util.HashMap<>();
                    }
                    java.lang.String attributeName = propertyCategory.substring(propertyCategory.lastIndexOf('/') + 1);
                    java.util.Map<java.lang.String, java.lang.String> attributesMap = configAttributesMap.get(attributeName);
                    if (attributesMap == null) {
                        attributesMap = new java.util.HashMap<>();
                        configAttributesMap.put(attributeName, attributesMap);
                    }
                    attributesMap.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName(entry.getKey()), entry.getValue().toString());
                }
            }
            final org.apache.ambari.server.controller.ConfigurationRequest configRequest = new org.apache.ambari.server.controller.ConfigurationRequest(cluster, type, tag, configMap, configAttributesMap);
            createResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
                @java.lang.Override
                public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
                    getManagementController().createConfiguration(configRequest);
                    return null;
                }
            });
        }
        return getRequestStatus(null);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.ConfigurationRequest> requests = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
            requests.add(getRequest(request, propertyMap));
        }
        java.util.Set<org.apache.ambari.server.controller.ConfigurationResponse> responses = getResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.util.Set<org.apache.ambari.server.controller.ConfigurationResponse>>() {
            @java.lang.Override
            public java.util.Set<org.apache.ambari.server.controller.ConfigurationResponse> invoke() throws org.apache.ambari.server.AmbariException {
                return getManagementController().getConfigurations(requests);
            }
        });
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.ConfigurationResponse response : responses) {
            java.lang.String stackId = response.getStackId().getStackId();
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Configuration);
            resource.setProperty(org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.CLUSTER_NAME, response.getClusterName());
            resource.setProperty(org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.STACK_ID, stackId);
            resource.setProperty(org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.TYPE, response.getType());
            resource.setProperty(org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.TAG, response.getVersionTag());
            resource.setProperty(org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.VERSION, response.getVersion());
            if ((null != response.getConfigs()) && (response.getConfigs().size() > 0)) {
                java.util.Map<java.lang.String, java.lang.String> configs = response.getConfigs();
                for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : configs.entrySet()) {
                    java.lang.String id = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("properties", entry.getKey());
                    resource.setProperty(id, entry.getValue());
                }
            }
            if ((null != response.getConfigAttributes()) && (response.getConfigAttributes().size() > 0)) {
                java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configAttributes = response.getConfigAttributes();
                for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configAttribute : configAttributes.entrySet()) {
                    java.lang.String id = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("properties_attributes", configAttribute.getKey());
                    resource.setProperty(id, configAttribute.getValue());
                }
            }
            resources.add(resource);
        }
        return resources;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("Cannot update a Configuration resource.");
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("Cannot delete a Configuration resource.");
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> checkPropertyIds(java.util.Set<java.lang.String> propertyIds) {
        propertyIds = super.checkPropertyIds(propertyIds);
        if (propertyIds.isEmpty()) {
            return propertyIds;
        }
        java.util.Set<java.lang.String> unsupportedProperties = new java.util.HashSet<>();
        for (java.lang.String propertyId : propertyIds) {
            if ((((((!propertyId.equals("tag")) && (!propertyId.equals("type"))) && (!propertyId.equals("/tag"))) && (!propertyId.equals("/type"))) && (!propertyId.equals("properties"))) && (!propertyId.equals("properties_attributes"))) {
                java.lang.String propertyCategory = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(propertyId);
                if ((propertyCategory == null) || (!(propertyCategory.equals("properties") || org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.PROPERTIES_ATTRIBUTES_PATTERN.matcher(propertyCategory).matches()))) {
                    unsupportedProperties.add(propertyId);
                }
            }
        }
        return unsupportedProperties;
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.pkPropertyIds;
    }

    private org.apache.ambari.server.controller.ConfigurationRequest getRequest(org.apache.ambari.server.controller.spi.Request request, java.util.Map<java.lang.String, java.lang.Object> properties) {
        java.lang.String type = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.TYPE)));
        java.lang.String tag = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.TAG)));
        org.apache.ambari.server.controller.ConfigurationRequest configRequest = new org.apache.ambari.server.controller.ConfigurationRequest(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ConfigurationResourceProvider.CLUSTER_NAME))), type, tag, new java.util.HashMap<>(), new java.util.HashMap<>());
        java.util.Set<java.lang.String> requestedIds = request.getPropertyIds();
        if (requestedIds.contains("properties") || requestedIds.contains("*")) {
            configRequest.setIncludeProperties(true);
        }
        return configRequest;
    }
}