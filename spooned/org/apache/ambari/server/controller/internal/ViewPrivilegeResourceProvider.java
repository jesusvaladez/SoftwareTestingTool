package org.apache.ambari.server.controller.internal;
public class ViewPrivilegeResourceProvider extends org.apache.ambari.server.controller.internal.PrivilegeResourceProvider<org.apache.ambari.server.orm.entities.ViewInstanceEntity> {
    public static final java.lang.String VIEW_NAME_PROPERTY_ID = "view_name";

    public static final java.lang.String VERSION_PROPERTY_ID = "version";

    public static final java.lang.String INSTANCE_NAME_PROPERTY_ID = "instance_name";

    public static final java.lang.String VIEW_NAME = (org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRIVILEGE_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.VIEW_NAME_PROPERTY_ID;

    public static final java.lang.String VERSION = (org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRIVILEGE_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.VERSION_PROPERTY_ID;

    public static final java.lang.String INSTANCE_NAME = (org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRIVILEGE_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.INSTANCE_NAME_PROPERTY_ID;

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.VIEW_NAME, org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.VERSION, org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.INSTANCE_NAME, org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRIVILEGE_ID, org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PERMISSION_NAME, org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PERMISSION_LABEL, org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRINCIPAL_NAME, org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRINCIPAL_TYPE);

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.View, org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.VIEW_NAME).put(org.apache.ambari.server.controller.spi.Resource.Type.ViewVersion, org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.VERSION).put(org.apache.ambari.server.controller.spi.Resource.Type.ViewInstance, org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.INSTANCE_NAME).put(org.apache.ambari.server.controller.spi.Resource.Type.ViewPrivilege, org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRIVILEGE_ID).build();

    private final org.apache.ambari.server.orm.entities.PermissionEntity viewUsePermission;

    public ViewPrivilegeResourceProvider() {
        super(org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.keyPropertyIds, org.apache.ambari.server.controller.spi.Resource.Type.ViewPrivilege);
        viewUsePermission = org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.permissionDAO.findById(org.apache.ambari.server.orm.entities.PermissionEntity.VIEW_USER_PERMISSION);
        java.util.EnumSet<org.apache.ambari.server.security.authorization.RoleAuthorization> requiredAuthorizations = java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_VIEWS);
        setRequiredCreateAuthorizations(requiredAuthorizations);
        setRequiredDeleteAuthorizations(requiredAuthorizations);
        setRequiredGetAuthorizations(requiredAuthorizations);
        setRequiredUpdateAuthorizations(requiredAuthorizations);
    }

    @java.lang.Override
    public java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> getKeyPropertyIds() {
        return org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.keyPropertyIds;
    }

    @java.lang.Override
    public java.util.Map<java.lang.Long, org.apache.ambari.server.orm.entities.ViewInstanceEntity> getResourceEntities(java.util.Map<java.lang.String, java.lang.Object> properties) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.view.ViewRegistry viewRegistry = org.apache.ambari.server.view.ViewRegistry.getInstance();
        java.lang.String viewName = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.VIEW_NAME)));
        java.lang.String viewVersion = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.VERSION)));
        java.lang.String instanceName = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.INSTANCE_NAME)));
        if (((viewName != null) && (viewVersion != null)) && (instanceName != null)) {
            org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = viewRegistry.getInstanceDefinition(viewName, viewVersion, instanceName);
            if (viewInstanceEntity == null) {
                throw new org.apache.ambari.server.AmbariException((((("View instance " + instanceName) + " of ") + viewName) + viewVersion) + " was not found");
            }
            org.apache.ambari.server.orm.entities.ViewEntity view = viewInstanceEntity.getViewEntity();
            return view.isDeployed() ? java.util.Collections.singletonMap(viewInstanceEntity.getResource().getId(), viewInstanceEntity) : java.util.Collections.emptyMap();
        }
        java.util.Set<org.apache.ambari.server.orm.entities.ViewEntity> viewEntities = new java.util.HashSet<>();
        if (viewVersion != null) {
            org.apache.ambari.server.orm.entities.ViewEntity viewEntity = viewRegistry.getDefinition(viewName, viewVersion);
            if (viewEntity != null) {
                viewEntities.add(viewEntity);
            }
        } else {
            for (org.apache.ambari.server.orm.entities.ViewEntity viewEntity : viewRegistry.getDefinitions()) {
                if ((viewName == null) || viewEntity.getCommonName().equals(viewName)) {
                    viewEntities.add(viewEntity);
                }
            }
        }
        java.util.Map<java.lang.Long, org.apache.ambari.server.orm.entities.ViewInstanceEntity> resourceEntities = new java.util.HashMap<>();
        for (org.apache.ambari.server.orm.entities.ViewEntity viewEntity : viewEntities) {
            if (viewEntity.isDeployed()) {
                for (org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity : viewEntity.getInstances()) {
                    resourceEntities.put(viewInstanceEntity.getResource().getId(), viewInstanceEntity);
                }
            }
        }
        return resourceEntities;
    }

    @java.lang.Override
    public java.lang.Long getResourceEntityId(org.apache.ambari.server.controller.spi.Predicate predicate) {
        final org.apache.ambari.server.view.ViewRegistry viewRegistry = org.apache.ambari.server.view.ViewRegistry.getInstance();
        final java.lang.String viewName = org.apache.ambari.server.controller.internal.AbstractResourceProvider.getQueryParameterValue(org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.VIEW_NAME, predicate).toString();
        final java.lang.String viewVersion = org.apache.ambari.server.controller.internal.AbstractResourceProvider.getQueryParameterValue(org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.VERSION, predicate).toString();
        final java.lang.String instanceName = org.apache.ambari.server.controller.internal.AbstractResourceProvider.getQueryParameterValue(org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.INSTANCE_NAME, predicate).toString();
        final org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = viewRegistry.getInstanceDefinition(viewName, viewVersion, instanceName);
        if (viewInstanceEntity != null) {
            org.apache.ambari.server.orm.entities.ViewEntity view = viewInstanceEntity.getViewEntity();
            return view.isDeployed() ? viewInstanceEntity.getResource().getId() : null;
        }
        return null;
    }

    @java.lang.Override
    protected boolean checkResourceTypes(org.apache.ambari.server.orm.entities.PrivilegeEntity entity) throws org.apache.ambari.server.AmbariException {
        return super.checkResourceTypes(entity) || entity.getPermission().getResourceType().getId().equals(org.apache.ambari.server.security.authorization.ResourceType.VIEW.getId());
    }

    @java.lang.Override
    protected org.apache.ambari.server.controller.spi.Resource toResource(org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity, java.util.Map<java.lang.Long, org.apache.ambari.server.orm.entities.UserEntity> userEntities, java.util.Map<java.lang.Long, org.apache.ambari.server.orm.entities.GroupEntity> groupEntities, java.util.Map<java.lang.Long, org.apache.ambari.server.orm.entities.PermissionEntity> roleEntities, java.util.Map<java.lang.Long, org.apache.ambari.server.orm.entities.ViewInstanceEntity> resourceEntities, java.util.Set<java.lang.String> requestedIds) {
        org.apache.ambari.server.controller.spi.Resource resource = super.toResource(privilegeEntity, userEntities, groupEntities, roleEntities, resourceEntities, requestedIds);
        if (resource != null) {
            org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = resourceEntities.get(privilegeEntity.getResource().getId());
            org.apache.ambari.server.orm.entities.ViewEntity viewEntity = viewInstanceEntity.getViewEntity();
            if (!viewEntity.isDeployed()) {
                return null;
            }
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.VIEW_NAME, viewEntity.getCommonName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.VERSION, viewEntity.getVersion(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.INSTANCE_NAME, viewInstanceEntity.getName(), requestedIds);
        }
        return resource;
    }

    @java.lang.Override
    protected org.apache.ambari.server.orm.entities.PermissionEntity getPermission(java.lang.String permissionName, org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity) throws org.apache.ambari.server.AmbariException {
        return permissionName.equals(org.apache.ambari.server.orm.entities.PermissionEntity.VIEW_USER_PERMISSION_NAME) ? viewUsePermission : super.getPermission(permissionName, resourceEntity);
    }
}