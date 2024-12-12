package org.apache.ambari.server.controller.internal;
public class ViewResourceProvider extends org.apache.ambari.server.controller.internal.AbstractAuthorizedResourceProvider {
    public static final java.lang.String VIEW_INFO = "ViewInfo";

    public static final java.lang.String VIEW_NAME_PROPERTY_ID = "view_name";

    public static final java.lang.String VIEW_NAME = (org.apache.ambari.server.controller.internal.ViewResourceProvider.VIEW_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewResourceProvider.VIEW_NAME_PROPERTY_ID;

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.View, org.apache.ambari.server.controller.internal.ViewResourceProvider.VIEW_NAME).build();

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.ViewResourceProvider.VIEW_NAME);

    public ViewResourceProvider() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.View, org.apache.ambari.server.controller.internal.ViewResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.ViewResourceProvider.keyPropertyIds);
        java.util.EnumSet<org.apache.ambari.server.security.authorization.RoleAuthorization> requiredAuthorizations = java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_VIEWS);
        setRequiredCreateAuthorizations(requiredAuthorizations);
        setRequiredDeleteAuthorizations(requiredAuthorizations);
        setRequiredUpdateAuthorizations(requiredAuthorizations);
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus createResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("Not yet supported.");
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        org.apache.ambari.server.view.ViewRegistry viewRegistry = org.apache.ambari.server.view.ViewRegistry.getInstance();
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertyMaps = getPropertyMaps(predicate);
        if (propertyMaps.isEmpty()) {
            propertyMaps.add(java.util.Collections.emptyMap());
        }
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : propertyMaps) {
            java.lang.String viewName = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.ViewResourceProvider.VIEW_NAME)));
            for (org.apache.ambari.server.orm.entities.ViewEntity viewDefinition : viewRegistry.getDefinitions()) {
                if ((viewName == null) || viewName.equals(viewDefinition.getCommonName())) {
                    if (viewRegistry.includeDefinition(viewDefinition)) {
                        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.View);
                        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewResourceProvider.VIEW_NAME, viewDefinition.getCommonName(), requestedIds);
                        resources.add(resource);
                    }
                }
            }
        }
        return resources;
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus updateResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("Not yet supported.");
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus deleteResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("Not yet supported.");
    }

    @java.lang.Override
    public java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> getKeyPropertyIds() {
        return org.apache.ambari.server.controller.internal.ViewResourceProvider.keyPropertyIds;
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.ViewResourceProvider.keyPropertyIds.values());
    }
}