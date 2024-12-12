package org.apache.ambari.server.controller.internal;
@org.apache.ambari.server.StaticallyInject
public class StackServiceResourceProvider extends org.apache.ambari.server.controller.internal.ReadOnlyResourceProvider {
    protected static final java.lang.String SERVICE_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServices", "service_name");

    protected static final java.lang.String SERVICE_TYPE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServices", "service_type");

    public static final java.lang.String STACK_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServices", "stack_name");

    public static final java.lang.String STACK_VERSION_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServices", "stack_version");

    private static final java.lang.String SERVICE_DISPLAY_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServices", "display_name");

    private static final java.lang.String USER_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServices", "user_name");

    private static final java.lang.String COMMENTS_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServices", "comments");

    private static final java.lang.String SELECTION_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServices", "selection");

    private static final java.lang.String MAINTAINER_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServices", "maintainer");

    private static final java.lang.String VERSION_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServices", "service_version");

    private static final java.lang.String CONFIG_TYPES = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServices", "config_types");

    private static final java.lang.String REQUIRED_SERVICES_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServices", "required_services");

    private static final java.lang.String SERVICE_CHECK_SUPPORTED_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServices", "service_check_supported");

    private static final java.lang.String CUSTOM_COMMANDS_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServices", "custom_commands");

    private static final java.lang.String SERVICE_PROPERTIES_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServices", "properties");

    private static final java.lang.String CREDENTIAL_STORE_SUPPORTED = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServices", "credential_store_supported");

    private static final java.lang.String CREDENTIAL_STORE_REQUIRED = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServices", "credential_store_required");

    private static final java.lang.String CREDENTIAL_STORE_ENABLED = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServices", "credential_store_enabled");

    private static final java.lang.String SUPPORT_DELETE_VIA_UI = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServices", "support_delete_via_ui");

    private static final java.lang.String SSO_INTEGRATION_SUPPORTED_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServices", "sso_integration_supported");

    private static final java.lang.String SSO_INTEGRATION_REQUIRES_KERBEROS_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServices", "sso_integration_requires_kerberos");

    private static final java.lang.String LDAP_INTEGRATION_SUPPORTED_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServices", "ldap_integration_supported");

    private static final java.lang.String ROLLING_RESTART_SUPPORTED_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("StackServices", "rolling_restart_supported");

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.STACK_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.STACK_VERSION_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.StackService, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.SERVICE_NAME_PROPERTY_ID).build();

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.StackServiceResourceProvider.SERVICE_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.SERVICE_TYPE_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.STACK_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.STACK_VERSION_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.SERVICE_DISPLAY_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.USER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.COMMENTS_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.SELECTION_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.MAINTAINER_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.VERSION_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.CONFIG_TYPES, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.REQUIRED_SERVICES_ID, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.SERVICE_CHECK_SUPPORTED_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.CUSTOM_COMMANDS_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.SERVICE_PROPERTIES_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.CREDENTIAL_STORE_SUPPORTED, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.CREDENTIAL_STORE_REQUIRED, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.CREDENTIAL_STORE_ENABLED, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.SUPPORT_DELETE_VIA_UI, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.SSO_INTEGRATION_SUPPORTED_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.SSO_INTEGRATION_REQUIRES_KERBEROS_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.LDAP_INTEGRATION_SUPPORTED_PROPERTY_ID, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.ROLLING_RESTART_SUPPORTED_PROPERTY_ID);

    @com.google.inject.Inject
    private static org.apache.ambari.server.state.kerberos.KerberosServiceDescriptorFactory kerberosServiceDescriptorFactory;

    protected StackServiceResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.StackService, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.keyPropertyIds, managementController);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.StackServiceRequest> requests = new java.util.HashSet<>();
        if (predicate == null) {
            requests.add(getRequest(java.util.Collections.emptyMap()));
        } else {
            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
                requests.add(getRequest(propertyMap));
            }
        }
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        java.util.Set<org.apache.ambari.server.controller.StackServiceResponse> responses = getResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.util.Set<org.apache.ambari.server.controller.StackServiceResponse>>() {
            @java.lang.Override
            public java.util.Set<org.apache.ambari.server.controller.StackServiceResponse> invoke() throws org.apache.ambari.server.AmbariException {
                return getManagementController().getStackServices(requests);
            }
        });
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.StackServiceResponse response : responses) {
            org.apache.ambari.server.controller.spi.Resource resource = createResource(response, requestedIds);
            resources.add(resource);
        }
        return resources;
    }

    private org.apache.ambari.server.controller.spi.Resource createResource(org.apache.ambari.server.controller.StackServiceResponse response, java.util.Set<java.lang.String> requestedIds) {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.StackService);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.STACK_NAME_PROPERTY_ID, response.getStackName(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.SERVICE_NAME_PROPERTY_ID, response.getServiceName(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.SERVICE_TYPE_PROPERTY_ID, response.getServiceType(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.STACK_VERSION_PROPERTY_ID, response.getStackVersion(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.SERVICE_NAME_PROPERTY_ID, response.getServiceName(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.SERVICE_DISPLAY_NAME_PROPERTY_ID, response.getServiceDisplayName(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.USER_NAME_PROPERTY_ID, response.getUserName(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.COMMENTS_PROPERTY_ID, response.getComments(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.VERSION_PROPERTY_ID, response.getServiceVersion(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.SELECTION_PROPERTY_ID, response.getSelection(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.MAINTAINER_PROPERTY_ID, response.getMaintainer(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.CONFIG_TYPES, response.getConfigTypes(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.REQUIRED_SERVICES_ID, response.getRequiredServices(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.SERVICE_CHECK_SUPPORTED_PROPERTY_ID, response.isServiceCheckSupported(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.CUSTOM_COMMANDS_PROPERTY_ID, response.getCustomCommands(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.SERVICE_PROPERTIES_PROPERTY_ID, response.getServiceProperties(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.CREDENTIAL_STORE_SUPPORTED, response.isCredentialStoreSupported(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.CREDENTIAL_STORE_REQUIRED, response.isCredentialStoreRequired(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.CREDENTIAL_STORE_ENABLED, response.isCredentialStoreEnabled(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.SUPPORT_DELETE_VIA_UI, response.isSupportDeleteViaUI(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.ROLLING_RESTART_SUPPORTED_PROPERTY_ID, response.isRollingRestartSupported(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.SSO_INTEGRATION_SUPPORTED_PROPERTY_ID, response.isSsoIntegrationSupported(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.SSO_INTEGRATION_REQUIRES_KERBEROS_PROPERTY_ID, response.isSsoIntegrationRequiresKerberos(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackServiceResourceProvider.LDAP_INTEGRATION_SUPPORTED_PROPERTY_ID, response.isLdapIntegrationSupported(), requestedIds);
        return resource;
    }

    private org.apache.ambari.server.controller.StackServiceRequest getRequest(java.util.Map<java.lang.String, java.lang.Object> properties) {
        return new org.apache.ambari.server.controller.StackServiceRequest(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.StackServiceResourceProvider.STACK_NAME_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.StackServiceResourceProvider.STACK_VERSION_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.StackServiceResourceProvider.SERVICE_NAME_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.StackServiceResourceProvider.CREDENTIAL_STORE_SUPPORTED))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.StackServiceResourceProvider.CREDENTIAL_STORE_ENABLED))));
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.StackServiceResourceProvider.keyPropertyIds.values());
    }
}