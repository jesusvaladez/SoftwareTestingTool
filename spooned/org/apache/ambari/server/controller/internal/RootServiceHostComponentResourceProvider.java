package org.apache.ambari.server.controller.internal;
public class RootServiceHostComponentResourceProvider extends org.apache.ambari.server.controller.internal.ReadOnlyResourceProvider {
    public static final java.lang.String RESPONSE_KEY = "RootServiceHostComponents";

    public static final java.lang.String SERVICE_NAME = "service_name";

    public static final java.lang.String HOST_NAME = "host_name";

    public static final java.lang.String COMPONENT_NAME = "component_name";

    public static final java.lang.String COMPONENT_VERSION = "component_version";

    public static final java.lang.String COMPONENT_STATE = "component_state";

    public static final java.lang.String PROPERTIES = "properties";

    public static final java.lang.String SERVICE_NAME_PROPERTY_ID = (org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.SERVICE_NAME;

    public static final java.lang.String HOST_NAME_PROPERTY_ID = (org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.HOST_NAME;

    public static final java.lang.String COMPONENT_NAME_PROPERTY_ID = (org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.COMPONENT_NAME;

    public static final java.lang.String COMPONENT_VERSION_PROPERTY_ID = (org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.COMPONENT_VERSION;

    public static final java.lang.String COMPONENT_STATE_PROPERTY_ID = (org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.COMPONENT_STATE;

    public static final java.lang.String PROPERTIES_PROPERTY_ID = (org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.PROPERTIES;

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.RootService, org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.SERVICE_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.Host, org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.HOST_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.RootServiceComponent, org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.COMPONENT_NAME_PROPERTY_ID).put(org.apache.ambari.server.controller.spi.Resource.Type.RootServiceHostComponent, org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.COMPONENT_NAME_PROPERTY_ID).build();

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.SERVICE_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.HOST_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.COMPONENT_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.COMPONENT_VERSION_PROPERTY_ID, org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.COMPONENT_STATE_PROPERTY_ID, org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.PROPERTIES_PROPERTY_ID);

    public RootServiceHostComponentResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.RootServiceHostComponent, org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.keyPropertyIds, managementController);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.RootServiceHostComponentRequest> requests = new java.util.HashSet<>();
        if (predicate == null) {
            requests.add(getRequest(java.util.Collections.emptyMap()));
        } else {
            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
                requests.add(getRequest(propertyMap));
            }
        }
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        java.util.Set<org.apache.ambari.server.controller.RootServiceHostComponentResponse> responses = getResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.util.Set<org.apache.ambari.server.controller.RootServiceHostComponentResponse>>() {
            @java.lang.Override
            public java.util.Set<org.apache.ambari.server.controller.RootServiceHostComponentResponse> invoke() throws org.apache.ambari.server.AmbariException {
                return getRootServiceHostComponents(requests);
            }
        });
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.RootServiceHostComponentResponse response : responses) {
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.RootServiceHostComponent);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.SERVICE_NAME_PROPERTY_ID, response.getServiceName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.HOST_NAME_PROPERTY_ID, response.getHostName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.COMPONENT_NAME_PROPERTY_ID, response.getComponentName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.COMPONENT_STATE_PROPERTY_ID, response.getComponentState(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.COMPONENT_VERSION_PROPERTY_ID, response.getComponentVersion(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.PROPERTIES_PROPERTY_ID, response.getProperties(), requestedIds);
            resources.add(resource);
        }
        return resources;
    }

    private org.apache.ambari.server.controller.RootServiceHostComponentRequest getRequest(java.util.Map<java.lang.String, java.lang.Object> properties) {
        return new org.apache.ambari.server.controller.RootServiceHostComponentRequest(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.SERVICE_NAME_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.HOST_NAME_PROPERTY_ID))), ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.COMPONENT_NAME_PROPERTY_ID))));
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.RootServiceHostComponentResourceProvider.keyPropertyIds.values());
    }

    protected java.util.Set<org.apache.ambari.server.controller.RootServiceHostComponentResponse> getRootServiceHostComponents(java.util.Set<org.apache.ambari.server.controller.RootServiceHostComponentRequest> requests) throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.controller.RootServiceHostComponentResponse> response = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.RootServiceHostComponentRequest request : requests) {
            try {
                java.util.Set<org.apache.ambari.server.controller.RootServiceHostComponentResponse> rootServiceHostComponents = getRootServiceHostComponents(request);
                response.addAll(rootServiceHostComponents);
            } catch (org.apache.ambari.server.AmbariException e) {
                if (requests.size() == 1) {
                    throw e;
                }
            }
        }
        return response;
    }

    private java.util.Set<org.apache.ambari.server.controller.RootServiceHostComponentResponse> getRootServiceHostComponents(org.apache.ambari.server.controller.RootServiceHostComponentRequest request) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.AmbariManagementController controller = getManagementController();
        java.util.Set<org.apache.ambari.server.controller.HostResponse> hosts = org.apache.ambari.server.controller.internal.HostResourceProvider.getHosts(controller, new org.apache.ambari.server.controller.HostRequest(request.getHostName(), null), null);
        return controller.getRootServiceResponseFactory().getRootServiceHostComponent(request, hosts);
    }
}