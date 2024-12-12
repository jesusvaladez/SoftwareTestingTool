package org.apache.ambari.server.view;
public class ViewExternalSubResourceProvider extends org.apache.ambari.server.controller.internal.AbstractResourceProvider {
    private static final java.lang.String VIEW_NAME_PROPERTY_ID = "view_name";

    private static final java.lang.String VIEW_VERSION_PROPERTY_ID = "version";

    private static final java.lang.String INSTANCE_NAME_PROPERTY_ID = "instance_name";

    private static final java.lang.String RESOURCE_NAME_PROPERTY_ID = "name";

    private final org.apache.ambari.server.controller.spi.Resource.Type type;

    private final java.util.Set<java.lang.String> resourceNames = new java.util.HashSet<>();

    private final java.util.Set<java.lang.String> pkPropertyIds;

    private final org.apache.ambari.server.orm.entities.ViewEntity viewDefinition;

    public ViewExternalSubResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type type, org.apache.ambari.server.orm.entities.ViewEntity viewDefinition) {
        super(org.apache.ambari.server.view.ViewExternalSubResourceProvider._getPropertyIds(), org.apache.ambari.server.view.ViewExternalSubResourceProvider._getKeyPropertyIds(type));
        this.type = type;
        this.pkPropertyIds = new java.util.HashSet<>(getKeyPropertyIds().values());
        this.viewDefinition = viewDefinition;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("Not supported!");
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resourceSet = new java.util.HashSet<>();
        java.util.Set<org.apache.ambari.server.orm.entities.ViewInstanceEntity> instanceDefinitions = new java.util.HashSet<>();
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertyMaps = getPropertyMaps(predicate);
        int size = propertyMaps.size();
        java.util.Collection<org.apache.ambari.server.orm.entities.ViewInstanceEntity> viewInstanceDefinitions = viewDefinition.getInstances();
        if (size == 0) {
            instanceDefinitions.addAll(viewInstanceDefinitions);
        } else {
            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : propertyMaps) {
                java.lang.String instanceName = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.view.ViewExternalSubResourceProvider.INSTANCE_NAME_PROPERTY_ID)));
                if (instanceName == null) {
                    instanceDefinitions.addAll(viewInstanceDefinitions);
                    break;
                } else {
                    instanceDefinitions.add(viewDefinition.getInstanceDefinition(instanceName));
                }
            }
        }
        for (org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceDefinition : instanceDefinitions) {
            for (java.lang.String resourceName : resourceNames) {
                org.apache.ambari.server.controller.internal.ResourceImpl resource = new org.apache.ambari.server.controller.internal.ResourceImpl(type);
                resource.setProperty(org.apache.ambari.server.view.ViewExternalSubResourceProvider.VIEW_NAME_PROPERTY_ID, viewDefinition.getCommonName());
                resource.setProperty(org.apache.ambari.server.view.ViewExternalSubResourceProvider.VIEW_VERSION_PROPERTY_ID, viewDefinition.getVersion());
                resource.setProperty(org.apache.ambari.server.view.ViewExternalSubResourceProvider.INSTANCE_NAME_PROPERTY_ID, viewInstanceDefinition.getName());
                resource.setProperty("name", resourceName);
                resourceSet.add(resource);
            }
        }
        return resourceSet;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("Not supported!");
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("Not supported!");
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return pkPropertyIds;
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> checkPropertyIds(java.util.Set<java.lang.String> propertyIds) {
        return java.util.Collections.emptySet();
    }

    public void addResourceName(java.lang.String resourceName) {
        resourceNames.add(resourceName);
    }

    private static java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> _getKeyPropertyIds(org.apache.ambari.server.controller.spi.Resource.Type type) {
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = new java.util.HashMap<>();
        keyPropertyIds.put(org.apache.ambari.server.controller.spi.Resource.Type.View, org.apache.ambari.server.view.ViewExternalSubResourceProvider.VIEW_NAME_PROPERTY_ID);
        keyPropertyIds.put(org.apache.ambari.server.controller.spi.Resource.Type.ViewVersion, org.apache.ambari.server.view.ViewExternalSubResourceProvider.VIEW_VERSION_PROPERTY_ID);
        keyPropertyIds.put(org.apache.ambari.server.controller.spi.Resource.Type.ViewInstance, org.apache.ambari.server.view.ViewExternalSubResourceProvider.INSTANCE_NAME_PROPERTY_ID);
        keyPropertyIds.put(type, org.apache.ambari.server.view.ViewExternalSubResourceProvider.RESOURCE_NAME_PROPERTY_ID);
        return keyPropertyIds;
    }

    private static java.util.Set<java.lang.String> _getPropertyIds() {
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.view.ViewExternalSubResourceProvider.INSTANCE_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.view.ViewExternalSubResourceProvider.VIEW_NAME_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.view.ViewExternalSubResourceProvider.VIEW_VERSION_PROPERTY_ID);
        propertyIds.add(org.apache.ambari.server.view.ViewExternalSubResourceProvider.RESOURCE_NAME_PROPERTY_ID);
        return propertyIds;
    }
}