package org.apache.ambari.server.controller.internal;
import org.apache.commons.lang.StringUtils;
@org.apache.ambari.server.StaticallyInject
public class RemoteClusterResourceProvider extends org.apache.ambari.server.controller.internal.AbstractAuthorizedResourceProvider {
    public static final java.lang.String CLUSTER_NAME_PROPERTY_ID = "ClusterInfo/name";

    public static final java.lang.String CLUSTER_ID_PROPERTY_ID = "ClusterInfo/cluster_id";

    public static final java.lang.String CLUSTER_URL_PROPERTY_ID = "ClusterInfo/url";

    public static final java.lang.String USERNAME_PROPERTY_ID = "ClusterInfo/username";

    public static final java.lang.String PASSWORD_PROPERTY_ID = "ClusterInfo/password";

    public static final java.lang.String SERVICES_PROPERTY_ID = "ClusterInfo/services";

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.RemoteClusterResourceProvider.class);

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.RemoteCluster, org.apache.ambari.server.controller.internal.RemoteClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID).build();

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.RemoteClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.RemoteClusterResourceProvider.CLUSTER_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.RemoteClusterResourceProvider.CLUSTER_URL_PROPERTY_ID, org.apache.ambari.server.controller.internal.RemoteClusterResourceProvider.USERNAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.RemoteClusterResourceProvider.PASSWORD_PROPERTY_ID, org.apache.ambari.server.controller.internal.RemoteClusterResourceProvider.SERVICES_PROPERTY_ID);

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.RemoteAmbariClusterDAO remoteAmbariClusterDAO;

    @com.google.inject.Inject
    private static org.apache.ambari.server.configuration.Configuration configuration;

    @com.google.inject.Inject
    private static org.apache.ambari.server.view.RemoteAmbariClusterRegistry remoteAmbariClusterRegistry;

    protected RemoteClusterResourceProvider() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.RemoteCluster, org.apache.ambari.server.controller.internal.RemoteClusterResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.RemoteClusterResourceProvider.keyPropertyIds);
        java.util.EnumSet<org.apache.ambari.server.security.authorization.RoleAuthorization> requiredAuthorizations = java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_ADD_DELETE_CLUSTERS);
        setRequiredCreateAuthorizations(requiredAuthorizations);
        setRequiredDeleteAuthorizations(requiredAuthorizations);
        setRequiredUpdateAuthorizations(requiredAuthorizations);
    }

    @java.lang.Override
    public java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> getKeyPropertyIds() {
        return org.apache.ambari.server.controller.internal.RemoteClusterResourceProvider.keyPropertyIds;
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.RemoteClusterResourceProvider.keyPropertyIds.values());
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        for (java.util.Map<java.lang.String, java.lang.Object> properties : request.getProperties()) {
            createResources(getCreateCommand(properties));
        }
        notifyCreate(org.apache.ambari.server.controller.spi.Resource.Type.RemoteCluster, request);
        return getRequestStatus(null);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertyMaps = getPropertyMaps(predicate);
        if (propertyMaps.isEmpty()) {
            propertyMaps.add(java.util.Collections.emptyMap());
        }
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : propertyMaps) {
            java.lang.String clusterName = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.RemoteClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID)));
            if (!com.google.common.base.Strings.isNullOrEmpty(clusterName)) {
                org.apache.ambari.server.orm.entities.RemoteAmbariClusterEntity cluster = org.apache.ambari.server.controller.internal.RemoteClusterResourceProvider.remoteAmbariClusterDAO.findByName(clusterName);
                if (cluster == null) {
                    throw new org.apache.ambari.server.controller.spi.NoSuchResourceException(java.lang.String.format("Cluster with name %s cannot be found", clusterName));
                }
                resources.add(toResource(requestedIds, cluster));
            } else {
                for (org.apache.ambari.server.orm.entities.RemoteAmbariClusterEntity cluster : org.apache.ambari.server.controller.internal.RemoteClusterResourceProvider.remoteAmbariClusterDAO.findAll()) {
                    org.apache.ambari.server.controller.spi.Resource resource = toResource(requestedIds, cluster);
                    resources.add(resource);
                }
            }
        }
        return resources;
    }

    protected org.apache.ambari.server.controller.spi.Resource toResource(java.util.Set<java.lang.String> requestedIds, org.apache.ambari.server.orm.entities.RemoteAmbariClusterEntity cluster) {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.RemoteCluster);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RemoteClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID, cluster.getName(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RemoteClusterResourceProvider.CLUSTER_ID_PROPERTY_ID, cluster.getId(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RemoteClusterResourceProvider.CLUSTER_URL_PROPERTY_ID, cluster.getUrl(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RemoteClusterResourceProvider.USERNAME_PROPERTY_ID, cluster.getUsername(), requestedIds);
        java.util.ArrayList<java.lang.String> services = new java.util.ArrayList<>();
        for (org.apache.ambari.server.orm.entities.RemoteAmbariClusterServiceEntity remoteClusterServiceEntity : cluster.getServices()) {
            services.add(remoteClusterServiceEntity.getServiceName());
        }
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.RemoteClusterResourceProvider.SERVICES_PROPERTY_ID, services, requestedIds);
        return resource;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Iterator<java.util.Map<java.lang.String, java.lang.Object>> iterator = request.getProperties().iterator();
        if (iterator.hasNext()) {
            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(iterator.next(), predicate)) {
                modifyResources(getUpdateCommand(propertyMap));
            }
        }
        notifyUpdate(org.apache.ambari.server.controller.spi.Resource.Type.RemoteCluster, request, predicate);
        return getRequestStatus(null);
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.RequestStatus deleteResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        modifyResources(getDeleteCommand(predicate));
        notifyDelete(org.apache.ambari.server.controller.spi.Resource.Type.ViewInstance, predicate);
        return getRequestStatus(null);
    }

    private org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void> getCreateCommand(final java.util.Map<java.lang.String, java.lang.Object> properties) {
        return new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                java.lang.String name = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.RemoteClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID)));
                if (org.apache.commons.lang.StringUtils.isEmpty(name)) {
                    throw new java.lang.IllegalArgumentException("Cluster Name cannot ne null or Empty");
                }
                if (org.apache.ambari.server.controller.internal.RemoteClusterResourceProvider.remoteAmbariClusterDAO.findByName(name) != null) {
                    throw new org.apache.ambari.server.DuplicateResourceException(java.lang.String.format("Remote cluster with name %s already exists", name));
                }
                saveOrUpdateRemoteAmbariClusterEntity(properties, false);
                return null;
            }
        };
    }

    private org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void> getUpdateCommand(final java.util.Map<java.lang.String, java.lang.Object> properties) {
        return new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                java.lang.String name = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.RemoteClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID)));
                if (org.apache.commons.lang.StringUtils.isEmpty(name)) {
                    throw new java.lang.IllegalArgumentException("Cluster Name cannot be null or Empty");
                }
                java.lang.String id = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.RemoteClusterResourceProvider.CLUSTER_ID_PROPERTY_ID)));
                if (org.apache.commons.lang.StringUtils.isEmpty(id)) {
                    throw new java.lang.IllegalArgumentException("Cluster Id cannot be null or Empty");
                }
                saveOrUpdateRemoteAmbariClusterEntity(properties, true);
                return null;
            }
        };
    }

    private void saveOrUpdateRemoteAmbariClusterEntity(java.util.Map<java.lang.String, java.lang.Object> properties, boolean update) throws org.apache.ambari.server.AmbariException {
        java.lang.String name = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.RemoteClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID)));
        java.lang.String url = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.RemoteClusterResourceProvider.CLUSTER_URL_PROPERTY_ID)));
        java.lang.String username = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.RemoteClusterResourceProvider.USERNAME_PROPERTY_ID)));
        java.lang.String password = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.RemoteClusterResourceProvider.PASSWORD_PROPERTY_ID)));
        if (org.apache.commons.lang.StringUtils.isEmpty(url) && org.apache.commons.lang.StringUtils.isEmpty(username)) {
            throw new java.lang.IllegalArgumentException("Url or username cannot be null");
        }
        org.apache.ambari.server.orm.entities.RemoteAmbariClusterEntity entity;
        if (update) {
            java.lang.Long id = java.lang.Long.valueOf(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.RemoteClusterResourceProvider.CLUSTER_ID_PROPERTY_ID))));
            entity = org.apache.ambari.server.controller.internal.RemoteClusterResourceProvider.remoteAmbariClusterDAO.findById(id);
            if (entity == null) {
                throw new java.lang.IllegalArgumentException(java.lang.String.format("Cannot find cluster with Id : \"%s\"", id));
            }
        } else {
            entity = org.apache.ambari.server.controller.internal.RemoteClusterResourceProvider.remoteAmbariClusterDAO.findByName(name);
            if (entity != null) {
                throw new org.apache.ambari.server.DuplicateResourceException(java.lang.String.format("Cluster with name : \"%s\" already exists", name));
            }
        }
        if (org.apache.commons.lang.StringUtils.isBlank(password) && (!update)) {
            throw new java.lang.IllegalArgumentException("Password cannot be null");
        } else if ((org.apache.commons.lang.StringUtils.isBlank(password) && update) && (!username.equals(entity.getUsername()))) {
            throw new java.lang.IllegalArgumentException("Failed to update. Username does not match.");
        }
        if (entity == null) {
            entity = new org.apache.ambari.server.orm.entities.RemoteAmbariClusterEntity();
        }
        entity.setName(name);
        entity.setUrl(url);
        try {
            if (password != null) {
                entity.setUsername(username);
                entity.setPassword(password);
            }
        } catch (org.apache.ambari.view.MaskException e) {
            throw new java.lang.IllegalArgumentException(("Failed to create new Remote Cluster " + name) + ". Illegal Password");
        }
        try {
            org.apache.ambari.server.controller.internal.RemoteClusterResourceProvider.remoteAmbariClusterRegistry.saveOrUpdate(entity, update);
        } catch (java.lang.Exception e) {
            throw new java.lang.IllegalArgumentException((("Failed to create new Remote Cluster " + name) + ". ") + e.getMessage(), e);
        }
    }

    private org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void> getDeleteCommand(final org.apache.ambari.server.controller.spi.Predicate predicate) {
        return new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                java.lang.Comparable deletedCluster = ((org.apache.ambari.server.controller.predicate.EqualsPredicate) (predicate)).getValue();
                java.lang.String toDelete = deletedCluster.toString();
                org.apache.ambari.server.orm.entities.RemoteAmbariClusterEntity clusterEntity = org.apache.ambari.server.controller.internal.RemoteClusterResourceProvider.remoteAmbariClusterDAO.findByName(toDelete);
                if (clusterEntity == null) {
                    throw new java.lang.IllegalArgumentException(("The Cluster " + toDelete) + " does not exist");
                }
                org.apache.ambari.server.controller.internal.RemoteClusterResourceProvider.remoteAmbariClusterRegistry.delete(clusterEntity);
                return null;
            }
        };
    }
}