package org.apache.ambari.server.controller.internal;
public class TargetClusterResourceProvider extends org.apache.ambari.server.controller.internal.AbstractDRResourceProvider {
    protected static final java.lang.String CLUSTER_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Cluster", "name");

    protected static final java.lang.String CLUSTER_COLO_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Cluster", "colo");

    protected static final java.lang.String CLUSTER_INTERFACES_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Cluster", "interfaces");

    protected static final java.lang.String CLUSTER_LOCATIONS_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Cluster", "locations");

    protected static final java.lang.String CLUSTER_PROPERTIES_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Cluster", "properties");

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, org.apache.ambari.server.controller.internal.TargetClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID).build();

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.TargetClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.TargetClusterResourceProvider.CLUSTER_COLO_PROPERTY_ID, org.apache.ambari.server.controller.internal.TargetClusterResourceProvider.CLUSTER_INTERFACES_PROPERTY_ID, org.apache.ambari.server.controller.internal.TargetClusterResourceProvider.CLUSTER_LOCATIONS_PROPERTY_ID, org.apache.ambari.server.controller.internal.TargetClusterResourceProvider.CLUSTER_PROPERTIES_PROPERTY_ID);

    public TargetClusterResourceProvider(org.apache.ambari.server.controller.ivory.IvoryService ivoryService) {
        super(org.apache.ambari.server.controller.internal.TargetClusterResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.TargetClusterResourceProvider.keyPropertyIds, ivoryService);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        org.apache.ambari.server.controller.ivory.IvoryService service = getService();
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertiesSet = request.getProperties();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : propertiesSet) {
            service.submitCluster(org.apache.ambari.server.controller.internal.TargetClusterResourceProvider.getCluster(((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.TargetClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID))), propertyMap));
        }
        return new org.apache.ambari.server.controller.internal.RequestStatusImpl(null);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        org.apache.ambari.server.controller.ivory.IvoryService service = getService();
        java.util.List<java.lang.String> clusterNames = service.getClusterNames();
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        for (java.lang.String clusterName : clusterNames) {
            org.apache.ambari.server.controller.ivory.Cluster cluster = service.getCluster(clusterName);
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.DRTargetCluster);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.TargetClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID, cluster.getName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.TargetClusterResourceProvider.CLUSTER_COLO_PROPERTY_ID, cluster.getColo(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.TargetClusterResourceProvider.CLUSTER_INTERFACES_PROPERTY_ID, cluster.getInterfaces(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.TargetClusterResourceProvider.CLUSTER_LOCATIONS_PROPERTY_ID, cluster.getLocations(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.TargetClusterResourceProvider.CLUSTER_PROPERTIES_PROPERTY_ID, cluster.getProperties(), requestedIds);
            if ((predicate == null) || predicate.evaluate(resource)) {
                resources.add(resource);
            }
        }
        return resources;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        org.apache.ambari.server.controller.ivory.IvoryService service = getService();
        java.util.Iterator<java.util.Map<java.lang.String, java.lang.Object>> iterator = request.getProperties().iterator();
        if (iterator.hasNext()) {
            java.util.Map<java.lang.String, java.lang.Object> propertyMap = iterator.next();
            java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), predicate);
            for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
                service.updateCluster(org.apache.ambari.server.controller.internal.TargetClusterResourceProvider.getCluster(((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.TargetClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID))), propertyMap));
            }
        }
        return new org.apache.ambari.server.controller.internal.RequestStatusImpl(null);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        org.apache.ambari.server.controller.ivory.IvoryService service = getService();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), predicate);
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            service.deleteCluster(((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.internal.TargetClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID))));
        }
        return new org.apache.ambari.server.controller.internal.RequestStatusImpl(null);
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.TargetClusterResourceProvider.keyPropertyIds.values());
    }

    protected static org.apache.ambari.server.controller.ivory.Cluster getCluster(java.lang.String clusterName, java.util.Map<java.lang.String, java.lang.Object> propertyMap) {
        java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();
        for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : propertyMap.entrySet()) {
            java.lang.String property = entry.getKey();
            java.lang.String category = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(property);
            if (category.equals(org.apache.ambari.server.controller.internal.TargetClusterResourceProvider.CLUSTER_PROPERTIES_PROPERTY_ID)) {
                properties.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName(property), ((java.lang.String) (entry.getValue())));
            }
        }
        return new org.apache.ambari.server.controller.ivory.Cluster(clusterName, ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.TargetClusterResourceProvider.CLUSTER_COLO_PROPERTY_ID))), org.apache.ambari.server.controller.internal.TargetClusterResourceProvider.getInterfaces(((java.util.Set<java.util.Map<java.lang.String, java.lang.Object>>) (propertyMap.get(org.apache.ambari.server.controller.internal.TargetClusterResourceProvider.CLUSTER_INTERFACES_PROPERTY_ID)))), org.apache.ambari.server.controller.internal.TargetClusterResourceProvider.getLocations(((java.util.Set<java.util.Map<java.lang.String, java.lang.Object>>) (propertyMap.get(org.apache.ambari.server.controller.internal.TargetClusterResourceProvider.CLUSTER_LOCATIONS_PROPERTY_ID)))), properties);
    }

    protected static java.util.Set<org.apache.ambari.server.controller.ivory.Cluster.Interface> getInterfaces(java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> maps) {
        java.util.Set<org.apache.ambari.server.controller.ivory.Cluster.Interface> interfaces = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> map : maps) {
            interfaces.add(new org.apache.ambari.server.controller.ivory.Cluster.Interface(((java.lang.String) (map.get("type"))), ((java.lang.String) (map.get("endpoint"))), ((java.lang.String) (map.get("version")))));
        }
        return interfaces;
    }

    protected static java.util.Set<org.apache.ambari.server.controller.ivory.Cluster.Location> getLocations(java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> maps) {
        java.util.Set<org.apache.ambari.server.controller.ivory.Cluster.Location> locations = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> map : maps) {
            locations.add(new org.apache.ambari.server.controller.ivory.Cluster.Location(((java.lang.String) (map.get("name"))), ((java.lang.String) (map.get("path")))));
        }
        return locations;
    }
}