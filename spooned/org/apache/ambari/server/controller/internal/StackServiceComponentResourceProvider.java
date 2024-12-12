package org.apache.ambari.server.controller.internal;
public class StackServiceComponentResourceProvider extends org.apache.ambari.server.controller.internal.ReadOnlyResourceProvider {
    private static final java.lang.String STACK_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServiceComponents", "stack_name");

    private static final java.lang.String STACK_VERSION_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServiceComponents", "stack_version");

    private static final java.lang.String SERVICE_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServiceComponents", "service_name");

    private static final java.lang.String COMPONENT_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServiceComponents", "component_name");

    private static final java.lang.String COMPONENT_DISPLAY_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServiceComponents", "display_name");

    private static final java.lang.String COMPONENT_CATEGORY_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServiceComponents", "component_category");

    private static final java.lang.String IS_CLIENT_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServiceComponents", "is_client");

    private static final java.lang.String IS_MASTER_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServiceComponents", "is_master");

    private static final java.lang.String CARDINALITY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServiceComponents", "cardinality");

    private static final java.lang.String ADVERTISE_VERSION_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServiceComponents", "advertise_version");

    private static final java.lang.String DECOMISSION_ALLOWED_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServiceComponents", "decommission_allowed");

    private static final java.lang.String REASSIGN_ALLOWED_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServiceComponents", "reassign_allowed");

    private static final java.lang.String CUSTOM_COMMANDS_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServiceComponents", "custom_commands");

    private static final java.lang.String HAS_BULK_COMMANDS_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServiceComponents", "has_bulk_commands_definition");

    private static final java.lang.String BULK_COMMANDS_DISPLAY_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServiceComponents", "bulk_commands_display_name");

    private static final java.lang.String BULK_COMMANDS_MASTER_COMPONENT_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServiceComponents", "bulk_commands_master_component_name");

    private static final java.lang.String RECOVERY_ENABLED = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServiceComponents", "recovery_enabled");

    private static final java.lang.String ROLLING_RESTART_SUPPORTED = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServiceComponents", "rolling_restart_supported");

    private static final java.lang.String AUTO_DEPLOY_ENABLED_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("auto_deploy", "enabled");

    private static final java.lang.String AUTO_DEPLOY_LOCATION_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("auto_deploy", "location");

    private static final java.lang.String COMPONENT_TYPE = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServiceComponents", "component_type");

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.STACK_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.STACK_VERSION_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.StackService, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.SERVICE_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.StackServiceComponent, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.COMPONENT_NAME_PROPERTY_ID).build();

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.STACK_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.STACK_VERSION_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.SERVICE_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.COMPONENT_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.COMPONENT_DISPLAY_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.COMPONENT_CATEGORY_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.IS_CLIENT_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.IS_MASTER_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.CARDINALITY_ID, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.ADVERTISE_VERSION_ID, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.DECOMISSION_ALLOWED_ID, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.REASSIGN_ALLOWED_ID, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.CUSTOM_COMMANDS_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.HAS_BULK_COMMANDS_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.BULK_COMMANDS_DISPLAY_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.BULK_COMMANDS_MASTER_COMPONENT_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.RECOVERY_ENABLED, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.ROLLING_RESTART_SUPPORTED, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.AUTO_DEPLOY_ENABLED_ID, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.AUTO_DEPLOY_LOCATION_ID, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.COMPONENT_TYPE);

    protected StackServiceComponentResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.StackServiceComponent, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.keyPropertyIds, managementController);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.StackServiceComponentRequest> requests = new java.util.HashSet<>();
        if (predicate == null) {
            requests.add(getRequest(java.util.Collections.emptyMap()));
        } else {
            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
                requests.add(getRequest(propertyMap));
            }
        }
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        java.util.Set<org.apache.ambari.server.controller.StackServiceComponentResponse> responses = getResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.util.Set<org.apache.ambari.server.controller.StackServiceComponentResponse>>() {
            @java.lang.Override
            public java.util.Set<org.apache.ambari.server.controller.StackServiceComponentResponse> invoke() throws org.apache.ambari.server.AmbariException {
                return getManagementController().getStackComponents(requests);
            }
        });
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.StackServiceComponentResponse response : responses) {
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.StackServiceComponent);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.STACK_NAME_PROPERTY_ID, response.getStackName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.STACK_VERSION_PROPERTY_ID, response.getStackVersion(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.SERVICE_NAME_PROPERTY_ID, response.getServiceName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.COMPONENT_NAME_PROPERTY_ID, response.getComponentName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.COMPONENT_DISPLAY_NAME_PROPERTY_ID, response.getComponentDisplayName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.COMPONENT_CATEGORY_PROPERTY_ID, response.getComponentCategory(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.IS_CLIENT_PROPERTY_ID, response.isClient(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.IS_MASTER_PROPERTY_ID, response.isMaster(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.CARDINALITY_ID, response.getCardinality(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.ADVERTISE_VERSION_ID, response.isVersionAdvertised(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.DECOMISSION_ALLOWED_ID, response.isDecommissionAlllowed(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.RECOVERY_ENABLED, response.isRecoveryEnabled(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.REASSIGN_ALLOWED_ID, response.isReassignAlllowed(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.CUSTOM_COMMANDS_PROPERTY_ID, response.getVisibleCustomCommands(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.BULK_COMMANDS_DISPLAY_NAME_PROPERTY_ID, response.getBulkCommandsDisplayName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.BULK_COMMANDS_MASTER_COMPONENT_NAME_PROPERTY_ID, response.getBulkCommandsMasterComponentName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.HAS_BULK_COMMANDS_PROPERTY_ID, response.hasBulkCommands(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.ROLLING_RESTART_SUPPORTED, response.isRollingRestartSupported(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.COMPONENT_TYPE, response.getComponentType(), requestedIds);
            org.apache.ambari.server.state.AutoDeployInfo autoDeployInfo = response.getAutoDeploy();
            if (autoDeployInfo != null) {
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.AUTO_DEPLOY_ENABLED_ID, autoDeployInfo.isEnabled(), requestedIds);
                if (autoDeployInfo.getCoLocate() != null) {
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.AUTO_DEPLOY_LOCATION_ID, autoDeployInfo.getCoLocate(), requestedIds);
                }
            }
            resources.add(resource);
        }
        return resources;
    }

    private org.apache.ambari.server.controller.StackServiceComponentRequest getRequest(java.util.Map<java.lang.String, java.lang.Object> properties) {
        return new org.apache.ambari.server.controller.StackServiceComponentRequest(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.STACK_NAME_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.STACK_VERSION_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.SERVICE_NAME_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.COMPONENT_NAME_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.RECOVERY_ENABLED))));
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.StackServiceComponentResourceProvider.keyPropertyIds.values());
    }
}