package org.apache.ambari.server.controller.internal;
public class ClusterPrivilegeResourceProvider extends org.apache.ambari.server.controller.internal.PrivilegeResourceProvider<org.apache.ambari.server.orm.entities.ClusterEntity> {
    protected static org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO;

    protected static final java.lang.String CLUSTER_NAME_PROPERTY_ID = "cluster_name";

    protected static final java.lang.String CLUSTER_NAME = (org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRIVILEGE_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ClusterPrivilegeResourceProvider.CLUSTER_NAME_PROPERTY_ID;

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.ClusterPrivilegeResourceProvider.CLUSTER_NAME, org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRIVILEGE_ID, org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PERMISSION_NAME, org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PERMISSION_NAME, org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PERMISSION_LABEL, org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRINCIPAL_NAME, org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRINCIPAL_TYPE);

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.Cluster, org.apache.ambari.server.controller.internal.ClusterPrivilegeResourceProvider.CLUSTER_NAME).put(org.apache.ambari.server.controller.spi.Resource.Type.ClusterPrivilege, org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRIVILEGE_ID).build();

    public ClusterPrivilegeResourceProvider() {
        super(org.apache.ambari.server.controller.internal.ClusterPrivilegeResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.ClusterPrivilegeResourceProvider.keyPropertyIds, org.apache.ambari.server.controller.spi.Resource.Type.ClusterPrivilege);
        java.util.EnumSet<org.apache.ambari.server.security.authorization.RoleAuthorization> requiredAuthorizations = java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_ASSIGN_ROLES);
        setRequiredCreateAuthorizations(requiredAuthorizations);
        setRequiredDeleteAuthorizations(requiredAuthorizations);
        setRequiredGetAuthorizations(requiredAuthorizations);
        setRequiredUpdateAuthorizations(requiredAuthorizations);
    }

    public static void init(org.apache.ambari.server.orm.dao.ClusterDAO dao) {
        org.apache.ambari.server.controller.internal.ClusterPrivilegeResourceProvider.clusterDAO = dao;
    }

    @java.lang.Override
    public java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> getKeyPropertyIds() {
        return org.apache.ambari.server.controller.internal.ClusterPrivilegeResourceProvider.keyPropertyIds;
    }

    @java.lang.Override
    public java.util.Map<java.lang.Long, org.apache.ambari.server.orm.entities.ClusterEntity> getResourceEntities(java.util.Map<java.lang.String, java.lang.Object> properties) {
        java.lang.String clusterName = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ClusterPrivilegeResourceProvider.CLUSTER_NAME)));
        if (clusterName == null) {
            java.util.Map<java.lang.Long, org.apache.ambari.server.orm.entities.ClusterEntity> resourceEntities = new java.util.HashMap<>();
            java.util.List<org.apache.ambari.server.orm.entities.ClusterEntity> clusterEntities = org.apache.ambari.server.controller.internal.ClusterPrivilegeResourceProvider.clusterDAO.findAll();
            for (org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity : clusterEntities) {
                resourceEntities.put(clusterEntity.getResource().getId(), clusterEntity);
            }
            return resourceEntities;
        }
        org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = org.apache.ambari.server.controller.internal.ClusterPrivilegeResourceProvider.clusterDAO.findByName(clusterName);
        return java.util.Collections.singletonMap(clusterEntity.getResource().getId(), clusterEntity);
    }

    @java.lang.Override
    public java.lang.Long getResourceEntityId(org.apache.ambari.server.controller.spi.Predicate predicate) {
        final java.lang.String clusterName = org.apache.ambari.server.controller.internal.AbstractResourceProvider.getQueryParameterValue(org.apache.ambari.server.controller.internal.ClusterPrivilegeResourceProvider.CLUSTER_NAME, predicate).toString();
        final org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = org.apache.ambari.server.controller.internal.ClusterPrivilegeResourceProvider.clusterDAO.findByName(clusterName);
        return clusterEntity.getResource().getId();
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.Resource toResource(org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity, java.util.Map<java.lang.Long, org.apache.ambari.server.orm.entities.UserEntity> userEntities, java.util.Map<java.lang.Long, org.apache.ambari.server.orm.entities.GroupEntity> groupEntities, java.util.Map<java.lang.Long, org.apache.ambari.server.orm.entities.PermissionEntity> roleEntities, java.util.Map<java.lang.Long, org.apache.ambari.server.orm.entities.ClusterEntity> resourceEntities, java.util.Set<java.lang.String> requestedIds) {
        org.apache.ambari.server.controller.spi.Resource resource = super.toResource(privilegeEntity, userEntities, groupEntities, roleEntities, resourceEntities, requestedIds);
        if (resource != null) {
            org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = resourceEntities.get(privilegeEntity.getResource().getId());
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ClusterPrivilegeResourceProvider.CLUSTER_NAME, clusterEntity.getClusterName(), requestedIds);
        }
        return resource;
    }

    @java.lang.Override
    protected org.apache.ambari.server.orm.entities.PermissionEntity getPermission(java.lang.String permissionName, org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity) throws org.apache.ambari.server.AmbariException {
        return super.getPermission(permissionName, resourceEntity);
    }
}