package org.apache.ambari.server.controller.internal;
public class UserPrivilegeResourceProvider extends org.apache.ambari.server.controller.internal.ReadOnlyResourceProvider {
    protected static final java.lang.String USER_NAME_PROPERTY_ID = "user_name";

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

    protected static final java.lang.String USER_NAME = (org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRIVILEGE_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.USER_NAME_PROPERTY_ID;

    private static org.apache.ambari.server.orm.dao.UserDAO userDAO;

    private static org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO;

    private static org.apache.ambari.server.orm.dao.GroupDAO groupDAO;

    private static org.apache.ambari.server.orm.dao.ViewInstanceDAO viewInstanceDAO;

    private static org.apache.ambari.server.security.authorization.Users users;

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.PRIVILEGE_ID, org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.PERMISSION_NAME, org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.PERMISSION_LABEL, org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.PRINCIPAL_NAME, org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.PRINCIPAL_TYPE, org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.VIEW_NAME, org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.VIEW_VERSION, org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.INSTANCE_NAME, org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.CLUSTER_NAME, org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.TYPE, org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.USER_NAME);

    public static void init(org.apache.ambari.server.orm.dao.UserDAO userDAO, org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO, org.apache.ambari.server.orm.dao.GroupDAO groupDAO, org.apache.ambari.server.orm.dao.ViewInstanceDAO viewInstanceDAO, org.apache.ambari.server.security.authorization.Users users) {
        org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.userDAO = userDAO;
        org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.clusterDAO = clusterDAO;
        org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.groupDAO = groupDAO;
        org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.viewInstanceDAO = viewInstanceDAO;
        org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.users = users;
    }

    @java.lang.SuppressWarnings("serial")
    private static final java.util.Set<java.lang.String> pkPropertyIds = com.google.common.collect.ImmutableSet.<java.lang.String>builder().add(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.PRIVILEGE_ID).build();

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.User, org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.USER_NAME).put(org.apache.ambari.server.controller.spi.Resource.Type.UserPrivilege, org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.PRIVILEGE_ID).build();

    private java.lang.ThreadLocal<com.google.common.cache.LoadingCache<java.lang.Long, org.apache.ambari.server.orm.entities.ClusterEntity>> clusterCache = new java.lang.ThreadLocal<com.google.common.cache.LoadingCache<java.lang.Long, org.apache.ambari.server.orm.entities.ClusterEntity>>() {
        @java.lang.Override
        protected com.google.common.cache.LoadingCache<java.lang.Long, org.apache.ambari.server.orm.entities.ClusterEntity> initialValue() {
            com.google.common.cache.CacheLoader<java.lang.Long, org.apache.ambari.server.orm.entities.ClusterEntity> loader = new com.google.common.cache.CacheLoader<java.lang.Long, org.apache.ambari.server.orm.entities.ClusterEntity>() {
                @java.lang.Override
                public org.apache.ambari.server.orm.entities.ClusterEntity load(java.lang.Long key) throws java.lang.Exception {
                    return org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.clusterDAO.findByResourceId(key);
                }
            };
            return com.google.common.cache.CacheBuilder.newBuilder().expireAfterWrite(20, java.util.concurrent.TimeUnit.SECONDS).build(loader);
        }
    };

    private java.lang.ThreadLocal<com.google.common.cache.LoadingCache<java.lang.Long, org.apache.ambari.server.orm.entities.ViewInstanceEntity>> viewInstanceCache = new java.lang.ThreadLocal<com.google.common.cache.LoadingCache<java.lang.Long, org.apache.ambari.server.orm.entities.ViewInstanceEntity>>() {
        @java.lang.Override
        protected com.google.common.cache.LoadingCache<java.lang.Long, org.apache.ambari.server.orm.entities.ViewInstanceEntity> initialValue() {
            com.google.common.cache.CacheLoader<java.lang.Long, org.apache.ambari.server.orm.entities.ViewInstanceEntity> loader = new com.google.common.cache.CacheLoader<java.lang.Long, org.apache.ambari.server.orm.entities.ViewInstanceEntity>() {
                @java.lang.Override
                public org.apache.ambari.server.orm.entities.ViewInstanceEntity load(java.lang.Long key) throws java.lang.Exception {
                    return org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.viewInstanceDAO.findByResourceId(key);
                }
            };
            return com.google.common.cache.CacheBuilder.newBuilder().expireAfterWrite(20, java.util.concurrent.TimeUnit.SECONDS).build(loader);
        }
    };

    private java.lang.ThreadLocal<com.google.common.cache.LoadingCache<java.lang.String, org.apache.ambari.server.orm.entities.UserEntity>> usersCache = new java.lang.ThreadLocal<com.google.common.cache.LoadingCache<java.lang.String, org.apache.ambari.server.orm.entities.UserEntity>>() {
        @java.lang.Override
        protected com.google.common.cache.LoadingCache<java.lang.String, org.apache.ambari.server.orm.entities.UserEntity> initialValue() {
            com.google.common.cache.CacheLoader<java.lang.String, org.apache.ambari.server.orm.entities.UserEntity> loader = new com.google.common.cache.CacheLoader<java.lang.String, org.apache.ambari.server.orm.entities.UserEntity>() {
                @java.lang.Override
                public org.apache.ambari.server.orm.entities.UserEntity load(java.lang.String key) throws java.lang.Exception {
                    return org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.userDAO.findUserByName(key);
                }
            };
            return com.google.common.cache.CacheBuilder.newBuilder().expireAfterWrite(20, java.util.concurrent.TimeUnit.SECONDS).build(loader);
        }
    };

    private java.lang.ThreadLocal<com.google.common.cache.LoadingCache<org.apache.ambari.server.orm.entities.PrincipalEntity, org.apache.ambari.server.orm.entities.GroupEntity>> groupsCache = new java.lang.ThreadLocal<com.google.common.cache.LoadingCache<org.apache.ambari.server.orm.entities.PrincipalEntity, org.apache.ambari.server.orm.entities.GroupEntity>>() {
        @java.lang.Override
        protected com.google.common.cache.LoadingCache<org.apache.ambari.server.orm.entities.PrincipalEntity, org.apache.ambari.server.orm.entities.GroupEntity> initialValue() {
            com.google.common.cache.CacheLoader<org.apache.ambari.server.orm.entities.PrincipalEntity, org.apache.ambari.server.orm.entities.GroupEntity> loader = new com.google.common.cache.CacheLoader<org.apache.ambari.server.orm.entities.PrincipalEntity, org.apache.ambari.server.orm.entities.GroupEntity>() {
                @java.lang.Override
                public org.apache.ambari.server.orm.entities.GroupEntity load(org.apache.ambari.server.orm.entities.PrincipalEntity key) throws java.lang.Exception {
                    return org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.groupDAO.findGroupByPrincipal(key);
                }
            };
            return com.google.common.cache.CacheBuilder.newBuilder().expireAfterWrite(20, java.util.concurrent.TimeUnit.SECONDS).build(loader);
        }
    };

    private org.apache.ambari.server.orm.entities.GroupEntity getCachedGroupByPrincipal(org.apache.ambari.server.orm.entities.PrincipalEntity principalEntity) {
        org.apache.ambari.server.orm.entities.GroupEntity entity = groupsCache.get().getIfPresent(principalEntity);
        if (entity == null) {
            for (org.apache.ambari.server.orm.entities.GroupEntity groupEntity : org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.groupDAO.findAll()) {
                groupsCache.get().put(groupEntity.getPrincipal(), groupEntity);
            }
            entity = groupsCache.get().getUnchecked(principalEntity);
        }
        return entity;
    }

    public UserPrivilegeResourceProvider() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.UserPrivilege, org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.keyPropertyIds, null);
        java.util.EnumSet<org.apache.ambari.server.security.authorization.RoleAuthorization> requiredAuthorizations = java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_ASSIGN_ROLES);
        setRequiredCreateAuthorizations(requiredAuthorizations);
        setRequiredDeleteAuthorizations(requiredAuthorizations);
        setRequiredGetAuthorizations(requiredAuthorizations);
        setRequiredUpdateAuthorizations(requiredAuthorizations);
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.pkPropertyIds;
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        final java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        boolean isUserAdministrator = org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.AMBARI, null, org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_USERS);
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
            final java.lang.String userName = ((java.lang.String) (propertyMap.get(org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.USER_NAME)));
            if ((!isUserAdministrator) && (!org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthenticatedName().equalsIgnoreCase(userName))) {
                throw new org.apache.ambari.server.security.authorization.AuthorizationException();
            }
            if (userName != null) {
                org.apache.ambari.server.orm.entities.UserEntity userEntity = usersCache.get().getIfPresent(userName);
                if (userEntity == null) {
                    java.util.Map<java.lang.String, org.apache.ambari.server.orm.entities.UserEntity> userNames = new java.util.TreeMap<>();
                    for (org.apache.ambari.server.orm.entities.UserEntity entity : org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.userDAO.findAll()) {
                        org.apache.ambari.server.orm.entities.UserEntity existing = userNames.get(entity.getUserName());
                        if (existing == null) {
                            userNames.put(entity.getUserName(), entity);
                        }
                    }
                    usersCache.get().putAll(userNames);
                    userEntity = usersCache.get().getIfPresent(userName);
                }
                if (userEntity == null) {
                    userEntity = org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.userDAO.findUserByName(userName);
                }
                if (userEntity == null) {
                    throw new org.apache.ambari.server.controller.spi.NoSuchParentResourceException("User was not found");
                }
                final java.util.Collection<org.apache.ambari.server.orm.entities.PrivilegeEntity> privileges = org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.users.getUserPrivileges(userEntity);
                for (org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity : privileges) {
                    org.apache.ambari.server.controller.UserPrivilegeResponse response = getResponse(privilegeEntity, userName);
                    resources.add(toResource(response, requestedIds));
                }
            }
        }
        return resources;
    }

    protected org.apache.ambari.server.controller.UserPrivilegeResponse getResponse(org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity, java.lang.String userName) {
        java.lang.String permissionLabel = privilegeEntity.getPermission().getPermissionLabel();
        java.lang.String permissionName = privilegeEntity.getPermission().getPermissionName();
        java.lang.String principalTypeName = privilegeEntity.getPrincipal().getPrincipalType().getName();
        org.apache.ambari.server.controller.UserPrivilegeResponse userPrivilegeResponse = new org.apache.ambari.server.controller.UserPrivilegeResponse(userName, permissionLabel, permissionName, privilegeEntity.getId(), org.apache.ambari.server.orm.entities.PrincipalTypeEntity.PrincipalType.valueOf(principalTypeName));
        if (principalTypeName.equals(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.USER_PRINCIPAL_TYPE_NAME)) {
            final org.apache.ambari.server.orm.entities.UserEntity user = org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.userDAO.findUserByPrincipal(privilegeEntity.getPrincipal());
            userPrivilegeResponse.setPrincipalName(user.getUserName());
        } else if (principalTypeName.equals(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.GROUP_PRINCIPAL_TYPE_NAME)) {
            final org.apache.ambari.server.orm.entities.GroupEntity groupEntity = getCachedGroupByPrincipal(privilegeEntity.getPrincipal());
            userPrivilegeResponse.setPrincipalName(groupEntity.getGroupName());
        }
        java.lang.String typeName = privilegeEntity.getResource().getResourceType().getName();
        org.apache.ambari.server.security.authorization.ResourceType resourceType = org.apache.ambari.server.security.authorization.ResourceType.translate(typeName);
        if (resourceType != null) {
            switch (resourceType) {
                case AMBARI :
                    break;
                case CLUSTER :
                    final org.apache.ambari.server.orm.entities.ClusterEntity clusterEntity = clusterCache.get().getUnchecked(privilegeEntity.getResource().getId());
                    userPrivilegeResponse.setClusterName(clusterEntity.getClusterName());
                    break;
                case VIEW :
                    final org.apache.ambari.server.orm.entities.ViewInstanceEntity viewInstanceEntity = viewInstanceCache.get().getUnchecked(privilegeEntity.getResource().getId());
                    final org.apache.ambari.server.orm.entities.ViewEntity viewEntity = viewInstanceEntity.getViewEntity();
                    userPrivilegeResponse.setViewName(viewEntity.getCommonName());
                    userPrivilegeResponse.setVersion(viewEntity.getVersion());
                    userPrivilegeResponse.setInstanceName(viewInstanceEntity.getName());
                    break;
            }
            userPrivilegeResponse.setType(resourceType);
        }
        return userPrivilegeResponse;
    }

    protected org.apache.ambari.server.controller.spi.Resource toResource(org.apache.ambari.server.controller.UserPrivilegeResponse response, java.util.Set<java.lang.String> requestedIds) {
        final org.apache.ambari.server.controller.internal.ResourceImpl resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.UserPrivilege);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.USER_NAME, response.getUserName(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.PRIVILEGE_ID, response.getPrivilegeId(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.PERMISSION_NAME, response.getPermissionName(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.PERMISSION_LABEL, response.getPermissionLabel(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.PRINCIPAL_TYPE, response.getPrincipalType().name(), requestedIds);
        if (response.getPrincipalName() != null) {
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.PRINCIPAL_NAME, response.getPrincipalName(), requestedIds);
        }
        if (response.getType() != null) {
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.TYPE, response.getType().name(), requestedIds);
            switch (response.getType()) {
                case CLUSTER :
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.CLUSTER_NAME, response.getClusterName(), requestedIds);
                    break;
                case VIEW :
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.VIEW_NAME, response.getViewName(), requestedIds);
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.VIEW_VERSION, response.getVersion(), requestedIds);
                    org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UserPrivilegeResourceProvider.INSTANCE_NAME, response.getInstanceName(), requestedIds);
                    break;
            }
        }
        return resource;
    }
}