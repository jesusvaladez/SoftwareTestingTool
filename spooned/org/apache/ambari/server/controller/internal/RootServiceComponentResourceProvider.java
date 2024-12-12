package org.apache.ambari.server.controller.internal;
public class RootServiceComponentResourceProvider extends org.apache.ambari.server.controller.internal.ReadOnlyResourceProvider {
    public static final java.lang.String RESPONSE_KEY = "RootServiceComponents";

    public static final java.lang.String ALL_PROPERTIES = (org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + "*";

    public static final java.lang.String SERVICE_NAME = "service_name";

    public static final java.lang.String COMPONENT_NAME = "component_name";

    public static final java.lang.String COMPONENT_VERSION = "component_version";

    public static final java.lang.String PROPERTIES = "properties";

    public static final java.lang.String SERVER_CLOCK = "server_clock";

    public static final java.lang.String SERVICE_NAME_PROPERTY_ID = (org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.SERVICE_NAME;

    public static final java.lang.String COMPONENT_NAME_PROPERTY_ID = (org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.COMPONENT_NAME;

    public static final java.lang.String COMPONENT_VERSION_PROPERTY_ID = (org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.COMPONENT_VERSION;

    public static final java.lang.String PROPERTIES_PROPERTY_ID = (org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.PROPERTIES;

    public static final java.lang.String SERVER_CLOCK_PROPERTY_ID = (org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.SERVER_CLOCK;

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.RootService, org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.SERVICE_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.RootServiceComponent, org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.COMPONENT_NAME_PROPERTY_ID).build();

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.SERVICE_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.COMPONENT_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.COMPONENT_VERSION_PROPERTY_ID, org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.PROPERTIES_PROPERTY_ID, org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.SERVER_CLOCK_PROPERTY_ID);

    protected RootServiceComponentResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.RootServiceComponent, org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.keyPropertyIds, managementController);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.RootServiceComponentRequest> requests = new java.util.HashSet<>();
        if (predicate == null) {
            requests.add(getRequest(java.util.Collections.emptyMap()));
        } else {
            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
                requests.add(getRequest(propertyMap));
            }
        }
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        java.util.Set<org.apache.ambari.server.controller.RootServiceComponentResponse> responses = getResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.util.Set<org.apache.ambari.server.controller.RootServiceComponentResponse>>() {
            @java.lang.Override
            public java.util.Set<org.apache.ambari.server.controller.RootServiceComponentResponse> invoke() throws org.apache.ambari.server.AmbariException {
                return getManagementController().getRootServiceComponents(requests);
            }
        });
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.RootServiceComponentResponse response : responses) {
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.RootServiceComponent);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.SERVICE_NAME_PROPERTY_ID, response.getServiceName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.COMPONENT_NAME_PROPERTY_ID, response.getComponentName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.PROPERTIES_PROPERTY_ID, response.getProperties(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.COMPONENT_VERSION_PROPERTY_ID, response.getComponentVersion(), requestedIds);
            if (org.apache.ambari.server.controller.RootComponent.AMBARI_SERVER.name().equals(response.getComponentName())) {
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.SERVER_CLOCK_PROPERTY_ID, response.getServerClock(), requestedIds);
            }
            resources.add(resource);
        }
        return resources;
    }

    private org.apache.ambari.server.controller.RootServiceComponentRequest getRequest(java.util.Map<java.lang.String, java.lang.Object> properties) {
        return new org.apache.ambari.server.controller.RootServiceComponentRequest(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.SERVICE_NAME_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.COMPONENT_NAME_PROPERTY_ID))));
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.RootServiceComponentResourceProvider.keyPropertyIds.values());
    }
}