package org.apache.ambari.msi;
public abstract class AbstractResourceProvider implements org.apache.ambari.server.controller.spi.ResourceProvider {
    private final org.apache.ambari.msi.ClusterDefinition clusterDefinition;

    private final org.apache.ambari.server.controller.spi.Resource.Type type;

    private final java.util.Set<java.lang.String> propertyIds;

    public AbstractResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type type, org.apache.ambari.msi.ClusterDefinition clusterDefinition) {
        this.type = type;
        this.clusterDefinition = clusterDefinition;
        java.util.Set<java.lang.String> propertyIds = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyIds(type);
        this.propertyIds = new java.util.HashSet<java.lang.String>(propertyIds);
        this.propertyIds.addAll(org.apache.ambari.server.controller.utilities.PropertyHelper.getCategories(propertyIds));
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("Management operations are not supported");
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resultSet = new java.util.HashSet<org.apache.ambari.server.controller.spi.Resource>();
        for (org.apache.ambari.server.controller.spi.Resource resource : getResources()) {
            if ((predicate == null) || predicate.evaluate(resource)) {
                org.apache.ambari.server.controller.internal.ResourceImpl newResource = new org.apache.ambari.server.controller.internal.ResourceImpl(resource);
                updateProperties(newResource, request, predicate);
                resultSet.add(newResource);
            }
        }
        return resultSet;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = getResources(request, predicate);
        java.lang.Integer requestId = -1;
        java.util.Iterator<java.util.Map<java.lang.String, java.lang.Object>> iterator = request.getProperties().iterator();
        if (iterator.hasNext()) {
            java.util.Map<java.lang.String, java.lang.Object> properties = iterator.next();
            for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
                requestId = updateProperties(resource, properties);
            }
        }
        return getRequestStatus(requestId == (-1) ? null : requestId);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("Management operations are not supported");
    }

    @java.lang.Override
    public java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> getKeyPropertyIds() {
        return org.apache.ambari.server.controller.utilities.PropertyHelper.getKeyPropertyIds(type);
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> checkPropertyIds(java.util.Set<java.lang.String> propertyIds) {
        propertyIds = new java.util.HashSet<java.lang.String>(propertyIds);
        propertyIds.removeAll(this.propertyIds);
        return propertyIds;
    }

    protected abstract java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources();

    public abstract void updateProperties(org.apache.ambari.server.controller.spi.Resource resource, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate);

    public abstract int updateProperties(org.apache.ambari.server.controller.spi.Resource resource, java.util.Map<java.lang.String, java.lang.Object> properties);

    protected org.apache.ambari.server.controller.spi.RequestStatus getRequestStatus(java.lang.Integer requestId) {
        if (requestId != null) {
            org.apache.ambari.server.controller.spi.Resource requestResource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Request);
            requestResource.setProperty(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Requests", "id"), requestId);
            requestResource.setProperty(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Requests", "status"), "InProgress");
            return new org.apache.ambari.server.controller.internal.RequestStatusImpl(requestResource);
        }
        return new org.apache.ambari.server.controller.internal.RequestStatusImpl(null);
    }

    protected org.apache.ambari.msi.ClusterDefinition getClusterDefinition() {
        return clusterDefinition;
    }

    public org.apache.ambari.server.controller.spi.Resource.Type getType() {
        return type;
    }

    protected java.util.Set<java.lang.String> getRequestPropertyIds(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) {
        java.util.Set<java.lang.String> propertyIds = request.getPropertyIds();
        if ((propertyIds == null) || propertyIds.isEmpty()) {
            return new java.util.HashSet<java.lang.String>(this.propertyIds);
        }
        propertyIds = new java.util.HashSet<java.lang.String>(propertyIds);
        if (predicate != null) {
            propertyIds.addAll(org.apache.ambari.server.controller.utilities.PredicateHelper.getPropertyIds(predicate));
        }
        return propertyIds;
    }

    protected static boolean contains(java.util.Set<java.lang.String> ids, java.lang.String propertyId) {
        boolean contains = ids.contains(propertyId);
        if (!contains) {
            java.lang.String category = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(propertyId);
            while ((category != null) && (!contains)) {
                contains = ids.contains(category);
                category = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(category);
            } 
        }
        return contains;
    }

    public static org.apache.ambari.server.controller.spi.ResourceProvider getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type type, org.apache.ambari.msi.ClusterDefinition clusterDefinition) {
        if (type.equals(org.apache.ambari.server.controller.spi.Resource.Type.Cluster)) {
            return new org.apache.ambari.msi.ClusterProvider(clusterDefinition);
        } else if (type.equals(org.apache.ambari.server.controller.spi.Resource.Type.Service)) {
            return new org.apache.ambari.msi.ServiceProvider(clusterDefinition);
        } else if (type.equals(org.apache.ambari.server.controller.spi.Resource.Type.Component)) {
            return new org.apache.ambari.msi.ComponentProvider(clusterDefinition);
        } else if (type.equals(org.apache.ambari.server.controller.spi.Resource.Type.Host)) {
            return new org.apache.ambari.msi.HostProvider(clusterDefinition);
        } else if (type.equals(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent)) {
            return new org.apache.ambari.msi.HostComponentProvider(clusterDefinition);
        } else if (type.equals(org.apache.ambari.server.controller.spi.Resource.Type.Request)) {
            return new org.apache.ambari.msi.RequestProvider(clusterDefinition);
        } else if (type.equals(org.apache.ambari.server.controller.spi.Resource.Type.Task)) {
            return new org.apache.ambari.msi.TaskProvider(clusterDefinition);
        } else if (type.equals(org.apache.ambari.server.controller.spi.Resource.Type.Configuration)) {
            return new org.apache.ambari.msi.ConfigurationProvider(clusterDefinition);
        } else {
            return new org.apache.ambari.msi.NoOpProvider(type, clusterDefinition);
        }
    }
}