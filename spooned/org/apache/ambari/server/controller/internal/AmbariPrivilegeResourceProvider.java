package org.apache.ambari.server.controller.internal;
import static org.apache.ambari.server.controller.internal.ClusterPrivilegeResourceProvider.CLUSTER_NAME;
import static org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.INSTANCE_NAME;
import static org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.VERSION;
import static org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.VIEW_NAME;
public class AmbariPrivilegeResourceProvider extends org.apache.ambari.server.controller.internal.PrivilegeResourceProvider<java.lang.Object> {
    public static final java.lang.String TYPE = (org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRIVILEGE_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.TYPE_PROPERTY_ID;

    protected static org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO;

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRIVILEGE_ID, org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PERMISSION_NAME, org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PERMISSION_LABEL, org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRINCIPAL_NAME, org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRINCIPAL_TYPE, org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.VIEW_NAME, org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.VERSION, org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.INSTANCE_NAME, org.apache.ambari.server.controller.internal.ClusterPrivilegeResourceProvider.CLUSTER_NAME, org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.TYPE);

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.AmbariPrivilege, org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRIVILEGE_ID).build();

    public AmbariPrivilegeResourceProvider() {
        super(org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.keyPropertyIds, org.apache.ambari.server.controller.spi.Resource.Type.AmbariPrivilege);
        java.util.EnumSet<org.apache.ambari.server.security.authorization.RoleAuthorization> requiredAuthorizations = java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_ASSIGN_ROLES);
        setRequiredCreateAuthorizations(requiredAuthorizations);
        setRequiredDeleteAuthorizations(requiredAuthorizations);
        setRequiredGetAuthorizations(requiredAuthorizations);
        setRequiredUpdateAuthorizations(requiredAuthorizations);
    }

    public static void init(org.apache.ambari.server.orm.dao.ClusterDAO clusterDao) {
        org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.clusterDAO = clusterDao;
    }

    @java.lang.Override
    public java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> getKeyPropertyIds() {
        return org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.keyPropertyIds;
    }

    @java.lang.Override
    public java.util.Map<java.lang.Long, java.lang.Object> getResourceEntities(java.util.Map<java.lang.String, java.lang.Object> properties) {
        java.util.Map<java.lang.Long, java.lang.Object> resourceEntities = new java.util.LinkedHashMap<>();
        resourceEntities.put(org.apache.ambari.server.orm.entities.ResourceEntity.AMBARI_RESOURCE_ID, null);
        java.util.List<org.apache.ambari.server.orm.entities.ClusterEntity> clusterEntities = org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.clusterDAO.findAll();
        if (clusterEntities != null) {
            for (org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity : clusterEntities) {
                resourceEntities.put(clusterEntity.getResource().getId(), clusterEntity);
            }
        }
        org.apache.ambari.server.view.ViewRegistry viewRegistry = org.apache.ambari.server.view.ViewRegistry.getInstance();
        for (org.apache.ambari.server.orm.entities.ViewEntity viewEntity : viewRegistry.getDefinitions()) {
            if (viewEntity.isDeployed()) {
                for (org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity : viewEntity.getInstances()) {
                    resourceEntities.put(viewInstanceEntity.getResource().getId(), viewInstanceEntity);
                }
            }
        }
        return resourceEntities;
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.Resource toResource(org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity, java.util.Map<java.lang.Long, org.apache.ambari.server.orm.entities.UserEntity> userEntities, java.util.Map<java.lang.Long, org.apache.ambari.server.orm.entities.GroupEntity> groupEntities, java.util.Map<java.lang.Long, org.apache.ambari.server.orm.entities.PermissionEntity> roleEntities, java.util.Map<java.lang.Long, java.lang.Object> resourceEntities, java.util.Set<java.lang.String> requestedIds) {
        org.apache.ambari.server.controller.spi.Resource resource = super.toResource(privilegeEntity, userEntities, groupEntities, roleEntities, resourceEntities, requestedIds);
        if (resource != null) {
            org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = privilegeEntity.getResource();
            org.apache.ambari.server.orm.entities.ResourceTypeEntity type = resourceEntity.getResourceType();
            java.lang.String typeName = type.getName();
            org.apache.ambari.server.security.authorization.ResourceType resourceType = org.apache.ambari.server.security.authorization.ResourceType.translate(typeName);
            if (resourceType != null) {
                switch (resourceType) {
                    case AMBARI :
                        break;
                    case CLUSTER :
                        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = ((org.apache.ambari.server.orm.entities.ClusterEntity) (resourceEntities.get(resourceEntity.getId())));
                        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ClusterPrivilegeResourceProvider.CLUSTER_NAME, clusterEntity.getClusterName(), requestedIds);
                        break;
                    case VIEW :
                        org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = ((org.apache.ambari.server.orm.entities.ViewInstanceEntity) (resourceEntities.get(resourceEntity.getId())));
                        org.apache.ambari.server.orm.entities.ViewEntity viewEntity = viewInstanceEntity.getViewEntity();
                        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.VIEW_NAME, viewEntity.getCommonName(), requestedIds);
                        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.VERSION, viewEntity.getVersion(), requestedIds);
                        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.INSTANCE_NAME, viewInstanceEntity.getName(), requestedIds);
                        break;
                }
                org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.TYPE, resourceType.name(), requestedIds);
            }
        }
        return resource;
    }

    @java.lang.Override
    public java.lang.Long getResourceEntityId(org.apache.ambari.server.controller.spi.Predicate predicate) {
        return org.apache.ambari.server.orm.entities.ResourceEntity.AMBARI_RESOURCE_ID;
    }
}