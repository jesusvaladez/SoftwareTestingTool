package org.apache.ambari.server.controller.internal;
public class RootServiceResourceProvider extends org.apache.ambari.server.controller.internal.ReadOnlyResourceProvider {
    public static final java.lang.String RESPONSE_KEY = "RootService";

    public static final java.lang.String SERVICE_NAME = "service_name";

    public static final java.lang.String SERVICE_NAME_PROPERTY_ID = (org.apache.ambari.server.controller.internal.RootServiceResourceProvider.RESPONSE_KEY + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.RootServiceResourceProvider.SERVICE_NAME;

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.RootService, org.apache.ambari.server.controller.internal.RootServiceResourceProvider.SERVICE_NAME_PROPERTY_ID).build();

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.RootServiceResourceProvider.SERVICE_NAME_PROPERTY_ID);

    protected RootServiceResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.RootService, org.apache.ambari.server.controller.internal.RootServiceResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.RootServiceResourceProvider.keyPropertyIds, managementController);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.RootServiceRequest> requests = new java.util.HashSet<>();
        if (predicate == null) {
            requests.add(getRequest(java.util.Collections.emptyMap()));
        } else {
            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
                requests.add(getRequest(propertyMap));
            }
        }
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        java.util.Set<org.apache.ambari.server.controller.RootServiceResponse> responses = getResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.util.Set<org.apache.ambari.server.controller.RootServiceResponse>>() {
            @java.lang.Override
            public java.util.Set<org.apache.ambari.server.controller.RootServiceResponse> invoke() throws org.apache.ambari.server.AmbariException {
                return getManagementController().getRootServices(requests);
            }
        });
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.RootServiceResponse response : responses) {
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.RootService);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RootServiceResourceProvider.SERVICE_NAME_PROPERTY_ID, response.getServiceName(), requestedIds);
            resources.add(resource);
        }
        return resources;
    }

    private org.apache.ambari.server.controller.RootServiceRequest getRequest(java.util.Map<java.lang.String, java.lang.Object> properties) {
        return new org.apache.ambari.server.controller.RootServiceRequest(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.RootServiceResourceProvider.SERVICE_NAME_PROPERTY_ID))));
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.RootServiceResourceProvider.keyPropertyIds.values());
    }
}