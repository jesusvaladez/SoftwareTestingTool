package org.apache.ambari.server.controller.internal;
@org.apache.ambari.server.StaticallyInject
public class StackVersionResourceProvider extends org.apache.ambari.server.controller.internal.ReadOnlyResourceProvider {
    public static final java.lang.String RESPONSE_KEY = "Versions";

    public static final java.lang.String ALL_PROPERTIES = (org.apache.ambari.server.controller.internal.StackVersionResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + "*";

    public static final java.lang.String STACK_VERSION_PROPERTY_ID = (org.apache.ambari.server.controller.internal.StackVersionResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + "stack_version";

    public static final java.lang.String STACK_NAME_PROPERTY_ID = (org.apache.ambari.server.controller.internal.StackVersionResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + "stack_name";

    public static final java.lang.String STACK_MIN_VERSION_PROPERTY_ID = (org.apache.ambari.server.controller.internal.StackVersionResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + "min_upgrade_version";

    public static final java.lang.String STACK_ACTIVE_PROPERTY_ID = (org.apache.ambari.server.controller.internal.StackVersionResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + "active";

    public static final java.lang.String STACK_VALID_PROPERTY_ID = (org.apache.ambari.server.controller.internal.StackVersionResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + "valid";

    public static final java.lang.String STACK_ERROR_SET = (org.apache.ambari.server.controller.internal.StackVersionResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + "stack-errors";

    public static final java.lang.String STACK_CONFIG_TYPES = (org.apache.ambari.server.controller.internal.StackVersionResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + "config_types";

    public static final java.lang.String STACK_PARENT_PROPERTY_ID = (org.apache.ambari.server.controller.internal.StackVersionResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + "parent_stack_version";

    public static final java.lang.String UPGRADE_PACKS_PROPERTY_ID = (org.apache.ambari.server.controller.internal.StackVersionResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + "upgrade_packs";

    public static final java.lang.String STACK_MIN_JDK = (org.apache.ambari.server.controller.internal.StackVersionResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + "min_jdk";

    public static final java.lang.String STACK_MAX_JDK = (org.apache.ambari.server.controller.internal.StackVersionResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + "max_jdk";

    public static final java.lang.String MPACK_RESOURCE_ID = (org.apache.ambari.server.controller.internal.StackVersionResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + "id";

    public static final java.util.Set<java.lang.String> PROPERTY_IDS = new java.util.HashSet<>();

    @com.google.inject.Inject
    protected static org.apache.ambari.server.orm.dao.StackDAO stackDAO;

    private static java.util.Set<java.lang.String> pkPropertyIds = new java.util.HashSet<>(java.util.Arrays.asList(new java.lang.String[]{ org.apache.ambari.server.controller.internal.StackVersionResourceProvider.STACK_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackVersionResourceProvider.STACK_VERSION_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackVersionResourceProvider.MPACK_RESOURCE_ID }));

    public static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> KEY_PROPERTY_IDS = new java.util.HashMap<>();

    static {
        PROPERTY_IDS.add(MPACK_RESOURCE_ID);
        PROPERTY_IDS.add(STACK_VERSION_PROPERTY_ID);
        PROPERTY_IDS.add(STACK_NAME_PROPERTY_ID);
        PROPERTY_IDS.add(STACK_MIN_VERSION_PROPERTY_ID);
        PROPERTY_IDS.add(STACK_ACTIVE_PROPERTY_ID);
        PROPERTY_IDS.add(STACK_VALID_PROPERTY_ID);
        PROPERTY_IDS.add(STACK_ERROR_SET);
        PROPERTY_IDS.add(STACK_CONFIG_TYPES);
        PROPERTY_IDS.add(STACK_PARENT_PROPERTY_ID);
        PROPERTY_IDS.add(UPGRADE_PACKS_PROPERTY_ID);
        PROPERTY_IDS.add(STACK_MIN_JDK);
        PROPERTY_IDS.add(STACK_MAX_JDK);
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.Mpack, MPACK_RESOURCE_ID);
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, STACK_NAME_PROPERTY_ID);
        KEY_PROPERTY_IDS.put(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, STACK_VERSION_PROPERTY_ID);
    }

    StackVersionResourceProvider(org.apache.ambari.server.controller.AmbariManagementController controller) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, org.apache.ambari.server.controller.internal.StackVersionResourceProvider.PROPERTY_IDS, org.apache.ambari.server.controller.internal.StackVersionResourceProvider.KEY_PROPERTY_IDS, controller);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.StackVersionRequest> requests = new java.util.HashSet<>();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        if (predicate == null) {
            requests.add(getRequest(java.util.Collections.emptyMap()));
        } else {
            java.util.Map<java.lang.String, java.lang.Object> propertyMap = new java.util.HashMap<>(org.apache.ambari.server.controller.utilities.PredicateHelper.getProperties(predicate));
            if (propertyMap.containsKey(org.apache.ambari.server.controller.internal.StackVersionResourceProvider.MPACK_RESOURCE_ID)) {
                org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion);
                java.lang.Long mpackId = java.lang.Long.valueOf(((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.StackVersionResourceProvider.MPACK_RESOURCE_ID))));
                org.apache.ambari.server.orm.entities.StackEntity stackEntity = org.apache.ambari.server.controller.internal.StackVersionResourceProvider.stackDAO.findByMpack(mpackId);
                requests.add(new org.apache.ambari.server.controller.StackVersionRequest(stackEntity.getStackName(), stackEntity.getStackVersion()));
                resource.setProperty(org.apache.ambari.server.controller.internal.StackVersionResourceProvider.STACK_NAME_PROPERTY_ID, ((java.lang.String) (stackEntity.getStackName())));
                resource.setProperty(org.apache.ambari.server.controller.internal.StackVersionResourceProvider.STACK_VERSION_PROPERTY_ID, ((java.lang.String) (stackEntity.getStackVersion())));
                resource.setProperty(org.apache.ambari.server.controller.internal.StackVersionResourceProvider.MPACK_RESOURCE_ID, mpackId);
                resources.add(resource);
            } else {
                for (java.util.Map<java.lang.String, java.lang.Object> propertyMap1 : getPropertyMaps(predicate)) {
                    requests.add(getRequest(propertyMap1));
                }
                java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
                java.util.Set<org.apache.ambari.server.controller.StackVersionResponse> responses = getResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.util.Set<org.apache.ambari.server.controller.StackVersionResponse>>() {
                    @java.lang.Override
                    public java.util.Set<org.apache.ambari.server.controller.StackVersionResponse> invoke() throws org.apache.ambari.server.AmbariException {
                        return getManagementController().getStackVersions(requests);
                    }
                });
                for (org.apache.ambari.server.controller.StackVersionResponse response : responses) {
                    org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion);
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackVersionResourceProvider.STACK_NAME_PROPERTY_ID, response.getStackName(), requestedIds);
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackVersionResourceProvider.STACK_VERSION_PROPERTY_ID, response.getStackVersion(), requestedIds);
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackVersionResourceProvider.STACK_ACTIVE_PROPERTY_ID, response.isActive(), requestedIds);
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackVersionResourceProvider.STACK_VALID_PROPERTY_ID, response.isValid(), requestedIds);
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackVersionResourceProvider.STACK_ERROR_SET, response.getErrors(), requestedIds);
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackVersionResourceProvider.STACK_PARENT_PROPERTY_ID, response.getParentVersion(), requestedIds);
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackVersionResourceProvider.STACK_CONFIG_TYPES, response.getConfigTypes(), requestedIds);
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackVersionResourceProvider.UPGRADE_PACKS_PROPERTY_ID, response.getUpgradePacks(), requestedIds);
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackVersionResourceProvider.STACK_MIN_JDK, response.getMinJdk(), requestedIds);
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackVersionResourceProvider.STACK_MAX_JDK, response.getMaxJdk(), requestedIds);
                    resources.add(resource);
                }
            }
        }
        return resources;
    }

    private org.apache.ambari.server.controller.StackVersionRequest getRequest(java.util.Map<java.lang.String, java.lang.Object> properties) {
        return new org.apache.ambari.server.controller.StackVersionRequest(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.StackVersionResourceProvider.STACK_NAME_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.StackVersionResourceProvider.STACK_VERSION_PROPERTY_ID))));
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(keyPropertyIds.values());
    }
}