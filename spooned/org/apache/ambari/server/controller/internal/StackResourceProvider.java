package org.apache.ambari.server.controller.internal;
public class StackResourceProvider extends org.apache.ambari.server.controller.internal.ReadOnlyResourceProvider {
    public static final java.lang.String STACK_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Stacks", "stack_name");

    protected static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, org.apache.ambari.server.controller.internal.StackResourceProvider.STACK_NAME_PROPERTY_ID).build();

    protected static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.ImmutableSet.of(org.apache.ambari.server.controller.internal.StackResourceProvider.STACK_NAME_PROPERTY_ID);

    protected StackResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.Stack, org.apache.ambari.server.controller.internal.StackResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.StackResourceProvider.keyPropertyIds, managementController);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.StackRequest> requests = new java.util.HashSet<>();
        if (predicate == null) {
            requests.add(getRequest(java.util.Collections.emptyMap()));
        } else {
            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
                requests.add(getRequest(propertyMap));
            }
        }
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        java.util.Set<org.apache.ambari.server.controller.StackResponse> responses = getResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.util.Set<org.apache.ambari.server.controller.StackResponse>>() {
            @java.lang.Override
            public java.util.Set<org.apache.ambari.server.controller.StackResponse> invoke() throws org.apache.ambari.server.AmbariException {
                return getManagementController().getStacks(requests);
            }
        });
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.StackResponse response : responses) {
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Stack);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.StackResourceProvider.STACK_NAME_PROPERTY_ID, response.getStackName(), requestedIds);
            resource.setProperty(org.apache.ambari.server.controller.internal.StackResourceProvider.STACK_NAME_PROPERTY_ID, response.getStackName());
            resources.add(resource);
        }
        return resources;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        org.apache.ambari.server.controller.RequestStatusResponse response = modifyResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<org.apache.ambari.server.controller.RequestStatusResponse>() {
            @java.lang.Override
            public org.apache.ambari.server.controller.RequestStatusResponse invoke() throws org.apache.ambari.server.AmbariException {
                return getManagementController().updateStacks();
            }
        });
        notifyUpdate(org.apache.ambari.server.controller.spi.Resource.Type.Stack, request, predicate);
        return getRequestStatus(response);
    }

    private org.apache.ambari.server.controller.StackRequest getRequest(java.util.Map<java.lang.String, java.lang.Object> properties) {
        return new org.apache.ambari.server.controller.StackRequest(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.StackResourceProvider.STACK_NAME_PROPERTY_ID))));
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.StackResourceProvider.keyPropertyIds.values());
    }
}