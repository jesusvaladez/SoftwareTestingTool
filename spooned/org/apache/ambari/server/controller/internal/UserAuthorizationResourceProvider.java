package org.apache.ambari.server.controller.internal;
@org.apache.ambari.server.StaticallyInject
public class UserAuthorizationResourceProvider extends org.apache.ambari.server.controller.internal.ReadOnlyResourceProvider {
    public static final java.lang.String AUTHORIZATION_ID_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("AuthorizationInfo", "authorization_id");

    public static final java.lang.String USERNAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("AuthorizationInfo", "user_name");

    public static final java.lang.String AUTHORIZATION_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("AuthorizationInfo", "authorization_name");

    public static final java.lang.String AUTHORIZATION_RESOURCE_TYPE_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("AuthorizationInfo", "resource_type");

    public static final java.lang.String AUTHORIZATION_CLUSTER_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("AuthorizationInfo", "cluster_name");

    public static final java.lang.String AUTHORIZATION_VIEW_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("AuthorizationInfo", "view_name");

    public static final java.lang.String AUTHORIZATION_VIEW_VERSION_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("AuthorizationInfo", "view_version");

    public static final java.lang.String AUTHORIZATION_VIEW_INSTANCE_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("AuthorizationInfo", "view_instance_name");

    private static final java.util.Set<java.lang.String> PK_PROPERTY_IDS;

    private static final java.util.Set<java.lang.String> PROPERTY_IDS;

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> KEY_PROPERTY_IDS;

    static {
        java.util.Set<java.lang.String> set;
        set = new java.util.HashSet<>();
        set.add(AUTHORIZATION_ID_PROPERTY_ID);
        set.add(USERNAME_PROPERTY_ID);
        set.add(AUTHORIZATION_RESOURCE_TYPE_PROPERTY_ID);
        PK_PROPERTY_IDS = java.util.Collections.unmodifiableSet(set);
        set = new java.util.HashSet<>();
        set.add(AUTHORIZATION_ID_PROPERTY_ID);
        set.add(USERNAME_PROPERTY_ID);
        set.add(AUTHORIZATION_NAME_PROPERTY_ID);
        set.add(AUTHORIZATION_RESOURCE_TYPE_PROPERTY_ID);
        set.add(AUTHORIZATION_CLUSTER_NAME_PROPERTY_ID);
        set.add(AUTHORIZATION_VIEW_NAME_PROPERTY_ID);
        set.add(AUTHORIZATION_VIEW_VERSION_PROPERTY_ID);
        set.add(AUTHORIZATION_VIEW_INSTANCE_NAME_PROPERTY_ID);
        PROPERTY_IDS = java.util.Collections.unmodifiableSet(set);
        java.util.HashMap<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> map = new java.util.HashMap<>();
        map.put(org.apache.ambari.server.controller.spi.Resource.Type.User, USERNAME_PROPERTY_ID);
        map.put(org.apache.ambari.server.controller.spi.Resource.Type.UserAuthorization, AUTHORIZATION_ID_PROPERTY_ID);
        KEY_PROPERTY_IDS = java.util.Collections.unmodifiableMap(map);
    }

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.PermissionDAO permissionDAO;

    @com.google.inject.Inject
    private static org.apache.ambari.server.orm.dao.ResourceTypeDAO resourceTypeDAO;

    private final org.apache.ambari.server.controller.spi.ClusterController clusterController;

    public static void init(org.apache.ambari.server.orm.dao.PermissionDAO permissionDAO, org.apache.ambari.server.orm.dao.ResourceTypeDAO resourceTypeDAO) {
        org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.permissionDAO = permissionDAO;
        org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.resourceTypeDAO = resourceTypeDAO;
    }

    public UserAuthorizationResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.UserAuthorization, org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.PROPERTY_IDS, org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.KEY_PROPERTY_IDS, managementController);
        clusterController = org.apache.ambari.server.controller.utilities.ClusterControllerHelper.getClusterController();
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        org.apache.ambari.server.controller.spi.ResourceProvider userPrivilegeProvider = clusterController.ensureResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.UserPrivilege);
        boolean isUserAdministrator = org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.AMBARI, null, org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_USERS);
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
            java.lang.String username = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.USERNAME_PROPERTY_ID)));
            if ((!isUserAdministrator) && (!org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthenticatedName().equalsIgnoreCase(username))) {
                throw new org.apache.ambari.server.security.authorization.AuthorizationException();
            }
            org.apache.ambari.server.controller.spi.Request internalRequest = createUserPrivilegeRequest();
            org.apache.ambari.server.controller.spi.Predicate internalPredicate = createUserPrivilegePredicate(username);
            java.util.Set<org.apache.ambari.server.controller.spi.Resource> internalResources = userPrivilegeProvider.getResources(internalRequest, internalPredicate);
            if (internalResources != null) {
                for (org.apache.ambari.server.controller.spi.Resource internalResource : internalResources) {
                    java.lang.String permissionName = ((java.lang.String) (internalResource.getPropertyValue(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.PERMISSION_NAME)));
                    java.lang.String resourceType = ((java.lang.String) (internalResource.getPropertyValue(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.TYPE)));
                    java.util.Collection<org.apache.ambari.server.orm.entities.RoleAuthorizationEntity> authorizationEntities;
                    org.apache.ambari.server.orm.entities.ResourceTypeEntity resourceTypeEntity = org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.resourceTypeDAO.findByName(resourceType);
                    if (resourceTypeEntity != null) {
                        org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.permissionDAO.findPermissionByNameAndType(permissionName, resourceTypeEntity);
                        if (permissionEntity == null) {
                            authorizationEntities = null;
                        } else {
                            authorizationEntities = permissionEntity.getAuthorizations();
                        }
                        if (authorizationEntities != null) {
                            if ("VIEW".equals(resourceType)) {
                                addViewResources(resources, username, resourceType, internalResource, authorizationEntities, requestedIds);
                            } else {
                                addClusterResources(resources, username, resourceType, internalResource, authorizationEntities, requestedIds);
                            }
                        }
                    }
                }
            }
        }
        return resources;
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.PK_PROPERTY_IDS;
    }

    private org.apache.ambari.server.controller.spi.Predicate createUserPrivilegePredicate(java.lang.String username) {
        return new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.USER_NAME, username);
    }

    private org.apache.ambari.server.controller.spi.Request createUserPrivilegeRequest() {
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.PRIVILEGE_ID);
        propertyIds.add(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.PERMISSION_NAME);
        propertyIds.add(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.TYPE);
        propertyIds.add(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.CLUSTER_NAME);
        propertyIds.add(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.VIEW_NAME);
        propertyIds.add(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.VIEW_VERSION);
        propertyIds.add(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.INSTANCE_NAME);
        return new org.apache.ambari.server.controller.internal.RequestImpl(propertyIds, null, null, null);
    }

    private void addClusterResources(java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources, java.lang.String username, java.lang.String resourceType, org.apache.ambari.server.controller.spi.Resource privilegeResource, java.util.Collection<org.apache.ambari.server.orm.entities.RoleAuthorizationEntity> authorizationEntities, java.util.Set<java.lang.String> requestedIds) {
        for (org.apache.ambari.server.orm.entities.RoleAuthorizationEntity entity : authorizationEntities) {
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.UserAuthorization);
            java.lang.String clusterName = ((java.lang.String) (privilegeResource.getPropertyValue(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.CLUSTER_NAME)));
            org.apache.ambari.server.controller.UserAuthorizationResponse userAuthorizationResponse = getResponse(entity.getAuthorizationId(), entity.getAuthorizationName(), clusterName, resourceType, username);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.AUTHORIZATION_ID_PROPERTY_ID, userAuthorizationResponse.getAuthorizationId(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.USERNAME_PROPERTY_ID, username, requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.AUTHORIZATION_NAME_PROPERTY_ID, entity.getAuthorizationName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.AUTHORIZATION_RESOURCE_TYPE_PROPERTY_ID, resourceType, requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.AUTHORIZATION_CLUSTER_NAME_PROPERTY_ID, privilegeResource.getPropertyValue(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.CLUSTER_NAME), requestedIds);
            resources.add(resource);
        }
    }

    private void addViewResources(java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources, java.lang.String username, java.lang.String resourceType, org.apache.ambari.server.controller.spi.Resource privilegeResource, java.util.Collection<org.apache.ambari.server.orm.entities.RoleAuthorizationEntity> authorizationEntities, java.util.Set<java.lang.String> requestedIds) {
        for (org.apache.ambari.server.orm.entities.RoleAuthorizationEntity entity : authorizationEntities) {
            org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.UserAuthorization);
            java.lang.String viewName = ((java.lang.String) (privilegeResource.getPropertyValue(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.VIEW_NAME)));
            java.lang.String viewVersion = ((java.lang.String) (privilegeResource.getPropertyValue(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.VIEW_VERSION)));
            java.lang.String viewInstanceName = ((java.lang.String) (privilegeResource.getPropertyValue(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.INSTANCE_NAME)));
            org.apache.ambari.server.controller.UserAuthorizationResponse userAuthorizationResponse = getResponse(entity.getAuthorizationId(), entity.getAuthorizationName(), resourceType, username, viewName, viewVersion, viewInstanceName);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.AUTHORIZATION_ID_PROPERTY_ID, userAuthorizationResponse.getAuthorizationId(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.USERNAME_PROPERTY_ID, userAuthorizationResponse.getUserName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.AUTHORIZATION_NAME_PROPERTY_ID, userAuthorizationResponse.getAuthorizationName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.AUTHORIZATION_RESOURCE_TYPE_PROPERTY_ID, userAuthorizationResponse.getResourceType(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.AUTHORIZATION_VIEW_NAME_PROPERTY_ID, userAuthorizationResponse.getViewName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.AUTHORIZATION_VIEW_VERSION_PROPERTY_ID, userAuthorizationResponse.getViewVersion(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UserAuthorizationResourceProvider.AUTHORIZATION_VIEW_INSTANCE_NAME_PROPERTY_ID, userAuthorizationResponse.getViewInstanceName(), requestedIds);
            resources.add(resource);
        }
    }

    private org.apache.ambari.server.controller.UserAuthorizationResponse getResponse(java.lang.String authorizationId, java.lang.String authorizationName, java.lang.String clusterName, java.lang.String resourceType, java.lang.String userName) {
        return new org.apache.ambari.server.controller.UserAuthorizationResponse(authorizationId, authorizationName, clusterName, resourceType, userName);
    }

    private org.apache.ambari.server.controller.UserAuthorizationResponse getResponse(java.lang.String authorizationId, java.lang.String authorizationName, java.lang.String resourceType, java.lang.String userName, java.lang.String viewName, java.lang.String viewVersion, java.lang.String viewInstanceName) {
        return new org.apache.ambari.server.controller.UserAuthorizationResponse(authorizationId, authorizationName, resourceType, userName, viewName, viewVersion, viewInstanceName);
    }
}