package org.apache.ambari.server.controller.internal;
public class ValidationResourceProvider extends org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.ValidationResourceProvider.class);

    protected static final java.lang.String VALIDATION_ID_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Validation", "id");

    protected static final java.lang.String VALIDATE_PROPERTY_ID = "validate";

    protected static final java.lang.String ITEMS_PROPERTY_ID = "items";

    protected static final java.lang.String TYPE_PROPERTY_ID = "type";

    protected static final java.lang.String LEVE_PROPERTY_ID = "level";

    protected static final java.lang.String MESSAGE_PROPERTY_ID = "message";

    protected static final java.lang.String COMPONENT_NAME_PROPERTY_ID = "component-name";

    protected static final java.lang.String HOST_PROPERTY_ID = "host";

    protected static final java.lang.String CONFIG_TYPE_PROPERTY_ID = "config-type";

    protected static final java.lang.String CONFIG_NAME_PROPERTY_ID = "config-name";

    protected static final java.lang.String HOST_GROUP_PROPERTY_ID = "host-group";

    protected static final java.lang.String HOSTS_PROPERTY_ID = "hosts";

    protected static final java.lang.String SERVICES_PROPERTY_ID = "services";

    protected static final java.lang.String RECOMMENDATIONS_PROPERTY_ID = "recommendations";

    protected static final java.lang.String ITEMS_TYPE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.ValidationResourceProvider.ITEMS_PROPERTY_ID, org.apache.ambari.server.controller.internal.ValidationResourceProvider.TYPE_PROPERTY_ID);

    protected static final java.lang.String ITEMS_LEVE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.ValidationResourceProvider.ITEMS_PROPERTY_ID, org.apache.ambari.server.controller.internal.ValidationResourceProvider.LEVE_PROPERTY_ID);

    protected static final java.lang.String ITEMS_MESSAGE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.ValidationResourceProvider.ITEMS_PROPERTY_ID, org.apache.ambari.server.controller.internal.ValidationResourceProvider.MESSAGE_PROPERTY_ID);

    protected static final java.lang.String ITEMS_COMPONENT_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.ValidationResourceProvider.ITEMS_PROPERTY_ID, org.apache.ambari.server.controller.internal.ValidationResourceProvider.COMPONENT_NAME_PROPERTY_ID);

    protected static final java.lang.String ITEMS_HOST_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.ValidationResourceProvider.ITEMS_PROPERTY_ID, org.apache.ambari.server.controller.internal.ValidationResourceProvider.HOST_PROPERTY_ID);

    protected static final java.lang.String ITEMS_CONFIG_TYPE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.ValidationResourceProvider.ITEMS_PROPERTY_ID, org.apache.ambari.server.controller.internal.ValidationResourceProvider.CONFIG_TYPE_PROPERTY_ID);

    protected static final java.lang.String ITEMS_CONFIG_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.ValidationResourceProvider.ITEMS_PROPERTY_ID, org.apache.ambari.server.controller.internal.ValidationResourceProvider.CONFIG_NAME_PROPERTY_ID);

    protected static final java.lang.String ITEMS_HOST_GROUP_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId(org.apache.ambari.server.controller.internal.ValidationResourceProvider.ITEMS_PROPERTY_ID, org.apache.ambari.server.controller.internal.ValidationResourceProvider.HOST_GROUP_PROPERTY_ID);

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.Validation, org.apache.ambari.server.controller.internal.ValidationResourceProvider.VALIDATION_ID_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.STACK_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.STACK_VERSION_PROPERTY_ID).build();

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.ValidationResourceProvider.VALIDATION_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.ValidationResourceProvider.VALIDATE_PROPERTY_ID, org.apache.ambari.server.controller.internal.ValidationResourceProvider.ITEMS_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.STACK_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.STACK_VERSION_PROPERTY_ID, org.apache.ambari.server.controller.internal.ValidationResourceProvider.ITEMS_TYPE_PROPERTY_ID, org.apache.ambari.server.controller.internal.ValidationResourceProvider.ITEMS_LEVE_PROPERTY_ID, org.apache.ambari.server.controller.internal.ValidationResourceProvider.ITEMS_MESSAGE_PROPERTY_ID, org.apache.ambari.server.controller.internal.ValidationResourceProvider.ITEMS_COMPONENT_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.ValidationResourceProvider.ITEMS_HOST_PROPERTY_ID, org.apache.ambari.server.controller.internal.ValidationResourceProvider.ITEMS_CONFIG_TYPE_PROPERTY_ID, org.apache.ambari.server.controller.internal.ValidationResourceProvider.ITEMS_CONFIG_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.ValidationResourceProvider.ITEMS_HOST_GROUP_PROPERTY_ID, org.apache.ambari.server.controller.internal.ValidationResourceProvider.HOSTS_PROPERTY_ID, org.apache.ambari.server.controller.internal.ValidationResourceProvider.SERVICES_PROPERTY_ID, org.apache.ambari.server.controller.internal.ValidationResourceProvider.RECOMMENDATIONS_PROPERTY_ID);

    protected ValidationResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Validation, org.apache.ambari.server.controller.internal.ValidationResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.ValidationResourceProvider.keyPropertyIds, managementController);
    }

    @java.lang.Override
    protected java.lang.String getRequestTypePropertyId() {
        return org.apache.ambari.server.controller.internal.ValidationResourceProvider.VALIDATE_PROPERTY_ID;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(final org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest validationRequest = prepareStackAdvisorRequest(request);
        final org.apache.ambari.server.api.services.stackadvisor.validations.ValidationResponse response;
        try {
            response = org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.saHelper.validate(validationRequest);
        } catch (org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequestException e) {
            org.apache.ambari.server.controller.internal.ValidationResourceProvider.LOG.warn("Error occurred during validation", e);
            throw new java.lang.IllegalArgumentException(e.getMessage(), e);
        } catch (org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException e) {
            org.apache.ambari.server.controller.internal.ValidationResourceProvider.LOG.warn("Error occurred during validation", e);
            throw new org.apache.ambari.server.controller.spi.SystemException(e.getMessage(), e);
        }
        org.apache.ambari.server.controller.spi.Resource validation = createResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<org.apache.ambari.server.controller.spi.Resource>() {
            @java.lang.Override
            public org.apache.ambari.server.controller.spi.Resource invoke() throws org.apache.ambari.server.AmbariException {
                org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Validation);
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ValidationResourceProvider.VALIDATION_ID_PROPERTY_ID, response.getId(), getPropertyIds());
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.STACK_NAME_PROPERTY_ID, response.getVersion().getStackName(), getPropertyIds());
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.STACK_VERSION_PROPERTY_ID, response.getVersion().getStackVersion(), getPropertyIds());
                java.util.List<java.util.Map<java.lang.String, java.lang.Object>> listItemProps = new java.util.ArrayList<>();
                java.util.Set<org.apache.ambari.server.api.services.stackadvisor.validations.ValidationResponse.ValidationItem> items = response.getItems();
                for (org.apache.ambari.server.api.services.stackadvisor.validations.ValidationResponse.ValidationItem item : items) {
                    java.util.Map<java.lang.String, java.lang.Object> mapItemProps = new java.util.HashMap<>();
                    mapItemProps.put(org.apache.ambari.server.controller.internal.ValidationResourceProvider.TYPE_PROPERTY_ID, item.getType());
                    mapItemProps.put(org.apache.ambari.server.controller.internal.ValidationResourceProvider.LEVE_PROPERTY_ID, item.getLevel());
                    mapItemProps.put(org.apache.ambari.server.controller.internal.ValidationResourceProvider.MESSAGE_PROPERTY_ID, item.getMessage());
                    if (item.getComponentName() != null) {
                        mapItemProps.put(org.apache.ambari.server.controller.internal.ValidationResourceProvider.COMPONENT_NAME_PROPERTY_ID, item.getComponentName());
                    }
                    if (item.getHost() != null) {
                        mapItemProps.put(org.apache.ambari.server.controller.internal.ValidationResourceProvider.HOST_PROPERTY_ID, item.getHost());
                    }
                    if (item.getConfigType() != null) {
                        mapItemProps.put(org.apache.ambari.server.controller.internal.ValidationResourceProvider.CONFIG_TYPE_PROPERTY_ID, item.getConfigType());
                        mapItemProps.put(org.apache.ambari.server.controller.internal.ValidationResourceProvider.CONFIG_NAME_PROPERTY_ID, item.getConfigName());
                    }
                    listItemProps.add(mapItemProps);
                }
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ValidationResourceProvider.ITEMS_PROPERTY_ID, listItemProps, getPropertyIds());
                return resource;
            }
        });
        notifyCreate(org.apache.ambari.server.controller.spi.Resource.Type.Validation, request);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>(java.util.Arrays.asList(validation));
        return new org.apache.ambari.server.controller.internal.RequestStatusImpl(null, resources);
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.ValidationResourceProvider.keyPropertyIds.values());
    }
}