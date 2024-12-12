package org.apache.ambari.server.controller.internal;
@org.apache.ambari.server.StaticallyInject
public class GroupPrivilegeResourceProvider extends org.apache.ambari.server.controller.internal.ReadOnlyResourceProvider {
    protected static final java.lang.String GROUP_NAME_PROPERTY_ID = "group_name";

    protected static final java.lang.String PRIVILEGE_ID = org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRIVILEGE_ID;

    protected static final java.lang.String PERMISSION_NAME = org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PERMISSION_NAME;

    protected static final java.lang.String PERMISSION_LABEL = org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PERMISSION_LABEL;

    protected static final java.lang.String PRINCIPAL_NAME = org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRINCIPAL_NAME;

    protected static final java.lang.String PRINCIPAL_TYPE = org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRINCIPAL_TYPE;

    protected static final java.lang.String VIEW_NAME = org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.VIEW_NAME;

    protected static final java.lang.String VIEW_VERSION = org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.VERSION;

    protected static final java.lang.String INSTANCE_NAME = org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.INSTANCE_NAME;

    protected static final java.lang.String CLUSTER_NAME = org.apache.ambari.server.controller.internal.ClusterPrivilegeResourceProvider.CLUSTER_NAME;

    protected static final java.lang.String TYPE = org.apache.ambari.server.controller.internal.AmbariPrivilegeResourceProvider.TYPE;

    protected static final java.lang.String GROUP_NAME = (org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRIVILEGE_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.GROUP_NAME_PROPERTY_ID;

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO;

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.GroupDAO groupDAO;

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.ViewInstanceDAO viewInstanceDAO;

    @com.google.inject.Inject
    private static org.apache.ambari.server.security.authorization.Users users;

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.PRIVILEGE_ID, org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.PERMISSION_NAME, org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.PERMISSION_LABEL, org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.PRINCIPAL_NAME, org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.PRINCIPAL_TYPE, org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.VIEW_NAME, org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.VIEW_VERSION, org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.INSTANCE_NAME, org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.CLUSTER_NAME, org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.TYPE, org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.GROUP_NAME);

    public static void init(org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO, org.apache.ambari.server.orm.dao.GroupDAO groupDAO, org.apache.ambari.server.orm.dao.ViewInstanceDAO viewInstanceDAO, org.apache.ambari.server.security.authorization.Users users) {
        org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.clusterDAO = clusterDAO;
        org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.groupDAO = groupDAO;
        org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.viewInstanceDAO = viewInstanceDAO;
        org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.users = users;
    }

    @java.lang.SuppressWarnings("serial")
    private static final java.util.Set<java.lang.String> pkPropertyIds = com.google.common.collect.ImmutableSet.<java.lang.String>builder().add(org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.PRIVILEGE_ID).build();

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.Group, org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.GROUP_NAME).put(org.apache.ambari.server.controller.spi.Resource.Type.GroupPrivilege, org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.PRIVILEGE_ID).build();

    public GroupPrivilegeResourceProvider() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.GroupPrivilege, org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.keyPropertyIds, null);
        java.util.EnumSet<org.apache.ambari.server.security.authorization.RoleAuthorization> requiredAuthorizations = java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_ASSIGN_ROLES);
        setRequiredCreateAuthorizations(requiredAuthorizations);
        setRequiredDeleteAuthorizations(requiredAuthorizations);
        setRequiredGetAuthorizations(requiredAuthorizations);
        setRequiredUpdateAuthorizations(requiredAuthorizations);
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.pkPropertyIds;
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        final java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.AMBARI, null, org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_GROUPS)) {
            throw new org.apache.ambari.server.security.authorization.AuthorizationException();
        }
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
            final java.lang.String groupName = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.GROUP_NAME)));
            if (groupName != null) {
                org.apache.ambari.server.orm.entities.GroupEntity groupEntity = org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.groupDAO.findGroupByName(groupName);
                if (groupEntity == null) {
                    throw new org.apache.ambari.server.controller.spi.SystemException(("Group " + groupName) + " was not found");
                }
                final java.util.Collection<org.apache.ambari.server.orm.entities.PrivilegeEntity> privileges = org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.users.getGroupPrivileges(groupEntity);
                for (org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity : privileges) {
                    org.apache.ambari.server.controller.GroupPrivilegeResponse response = getResponse(privilegeEntity, groupName);
                    resources.add(toResource(response, requestedIds));
                }
            }
        }
        return resources;
    }

    protected org.apache.ambari.server.controller.GroupPrivilegeResponse getResponse(org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity, java.lang.String groupName) {
        java.lang.String permissionLabel = privilegeEntity.getPermission().getPermissionLabel();
        java.lang.String permissionName = privilegeEntity.getPermission().getPermissionName();
        java.lang.String principalTypeName = privilegeEntity.getPrincipal().getPrincipalType().getName();
        org.apache.ambari.server.controller.GroupPrivilegeResponse groupPrivilegeResponse = new org.apache.ambari.server.controller.GroupPrivilegeResponse(groupName, permissionLabel, permissionName, privilegeEntity.getId(), org.apache.ambari.server.orm.entities.PrincipalTypeEntity.PrincipalType.valueOf(principalTypeName));
        if (principalTypeName.equals(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.GROUP_PRINCIPAL_TYPE_NAME)) {
            final org.apache.ambari.server.orm.entities.GroupEntity groupEntity = org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.groupDAO.findGroupByPrincipal(privilegeEntity.getPrincipal());
            groupPrivilegeResponse.setPrincipalName(groupEntity.getGroupName());
        }
        java.lang.String typeName = privilegeEntity.getResource().getResourceType().getName();
        org.apache.ambari.server.security.authorization.ResourceType resourceType = org.apache.ambari.server.security.authorization.ResourceType.translate(typeName);
        if (resourceType != null) {
            switch (resourceType) {
                case AMBARI :
                    break;
                case CLUSTER :
                    final org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.clusterDAO.findByResourceId(privilegeEntity.getResource().getId());
                    groupPrivilegeResponse.setClusterName(clusterEntity.getClusterName());
                    break;
                case VIEW :
                    final org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.viewInstanceDAO.findByResourceId(privilegeEntity.getResource().getId());
                    final org.apache.ambari.server.orm.entities.ViewEntity viewEntity = viewInstanceEntity.getViewEntity();
                    groupPrivilegeResponse.setViewName(viewEntity.getCommonName());
                    groupPrivilegeResponse.setVersion(viewEntity.getVersion());
                    groupPrivilegeResponse.setInstanceName(viewInstanceEntity.getName());
                    break;
            }
            groupPrivilegeResponse.setType(resourceType);
        }
        return groupPrivilegeResponse;
    }

    protected org.apache.ambari.server.controller.spi.Resource toResource(org.apache.ambari.server.controller.GroupPrivilegeResponse response, java.util.Set<java.lang.String> requestedIds) {
        final org.apache.ambari.server.controller.internal.ResourceImpl resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.GroupPrivilege);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.GROUP_NAME, response.getGroupName(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.PRIVILEGE_ID, response.getPrivilegeId(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.PERMISSION_NAME, response.getPermissionName(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.PERMISSION_LABEL, response.getPermissionLabel(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.PRINCIPAL_TYPE, response.getPrincipalType().name(), requestedIds);
        if (response.getPrincipalName() != null) {
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.PRINCIPAL_NAME, response.getPrincipalName(), requestedIds);
        }
        if (response.getType() != null) {
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.TYPE, response.getType().name(), requestedIds);
            switch (response.getType()) {
                case CLUSTER :
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.CLUSTER_NAME, response.getClusterName(), requestedIds);
                    break;
                case VIEW :
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.VIEW_NAME, response.getViewName(), requestedIds);
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.VIEW_VERSION, response.getVersion(), requestedIds);
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.GroupPrivilegeResourceProvider.INSTANCE_NAME, response.getInstanceName(), requestedIds);
                    break;
            }
        }
        return resource;
    }
}