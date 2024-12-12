package org.apache.ambari.server.controller.internal;
import org.apache.commons.lang.StringUtils;
public class StackLevelConfigurationResourceProvider extends org.apache.ambari.server.controller.internal.ReadOnlyResourceProvider {
    public static final java.lang.String STACK_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackLevelConfigurations", "stack_name");

    public static final java.lang.String STACK_VERSION_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackLevelConfigurations", "stack_version");

    public static final java.lang.String PROPERTY_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackLevelConfigurations", "property_name");

    public static final java.lang.String PROPERTY_DISPLAY_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackLevelConfigurations", "property_display_name");

    public static final java.lang.String PROPERTY_VALUE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackLevelConfigurations", "property_value");

    public static final java.lang.String PROPERTY_VALUE_ATTRIBUTES_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackLevelConfigurations", "property_value_attributes");

    public static final java.lang.String DEPENDS_ON_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackLevelConfigurations", "property_depends_on");

    public static final java.lang.String PROPERTY_DESCRIPTION_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackLevelConfigurations", "property_description");

    public static final java.lang.String PROPERTY_PROPERTY_TYPE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackLevelConfigurations", "property_type");

    public static final java.lang.String PROPERTY_TYPE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackLevelConfigurations", "type");

    public static final java.lang.String PROPERTY_FINAL_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackLevelConfigurations", "final");

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.STACK_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.STACK_VERSION_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.StackLevelConfiguration, org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.PROPERTY_NAME_PROPERTY_ID).build();

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.STACK_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.STACK_VERSION_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.PROPERTY_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.PROPERTY_DISPLAY_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.PROPERTY_VALUE_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.PROPERTY_VALUE_ATTRIBUTES_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.DEPENDS_ON_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.PROPERTY_DESCRIPTION_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.PROPERTY_PROPERTY_TYPE_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.PROPERTY_TYPE_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.PROPERTY_FINAL_PROPERTY_ID);

    protected StackLevelConfigurationResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.StackLevelConfiguration, org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.keyPropertyIds, managementController);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.StackLevelConfigurationRequest> requests = new java.util.HashSet<>();
        if (predicate == null) {
            requests.add(getRequest(java.util.Collections.emptyMap()));
        } else {
            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
                requests.add(getRequest(propertyMap));
            }
        }
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        java.util.Set<org.apache.ambari.server.controller.StackConfigurationResponse> responses = getResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.util.Set<org.apache.ambari.server.controller.StackConfigurationResponse>>() {
            @java.lang.Override
            public java.util.Set<org.apache.ambari.server.controller.StackConfigurationResponse> invoke() throws org.apache.ambari.server.AmbariException {
                return getManagementController().getStackLevelConfigurations(requests);
            }
        });
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.StackConfigurationResponse response : responses) {
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.StackLevelConfiguration);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.STACK_NAME_PROPERTY_ID, response.getStackName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.STACK_VERSION_PROPERTY_ID, response.getStackVersion(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.PROPERTY_NAME_PROPERTY_ID, response.getPropertyName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.PROPERTY_VALUE_PROPERTY_ID, response.getPropertyValue(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.PROPERTY_VALUE_ATTRIBUTES_PROPERTY_ID, response.getPropertyValueAttributes(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.DEPENDS_ON_PROPERTY_ID, response.getDependsOnProperties(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.PROPERTY_DESCRIPTION_PROPERTY_ID, response.getPropertyDescription(), requestedIds);
            if (org.apache.commons.lang.StringUtils.isNotEmpty(response.getPropertyDisplayName())) {
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.PROPERTY_DISPLAY_NAME_PROPERTY_ID, response.getPropertyDisplayName(), requestedIds);
            }
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.PROPERTY_PROPERTY_TYPE_PROPERTY_ID, response.getPropertyType(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.PROPERTY_TYPE_PROPERTY_ID, response.getType(), requestedIds);
            setDefaultPropertiesAttributes(resource, requestedIds);
            for (java.util.Map.Entry<java.lang.String, java.lang.String> attribute : response.getPropertyAttributes().entrySet()) {
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackLevelConfigurations", attribute.getKey()), attribute.getValue(), requestedIds);
            }
            resources.add(resource);
        }
        return resources;
    }

    private void setDefaultPropertiesAttributes(org.apache.ambari.server.controller.spi.Resource resource, java.util.Set<java.lang.String> requestedIds) {
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.PROPERTY_FINAL_PROPERTY_ID, "false", requestedIds);
    }

    private org.apache.ambari.server.controller.StackLevelConfigurationRequest getRequest(java.util.Map<java.lang.String, java.lang.Object> properties) {
        return new org.apache.ambari.server.controller.StackLevelConfigurationRequest(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.STACK_NAME_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.STACK_VERSION_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.PROPERTY_NAME_PROPERTY_ID))));
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.StackLevelConfigurationResourceProvider.keyPropertyIds.values());
    }
}