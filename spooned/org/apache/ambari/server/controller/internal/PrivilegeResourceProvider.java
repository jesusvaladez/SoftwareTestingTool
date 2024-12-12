package org.apache.ambari.server.controller.internal;
import org.apache.commons.lang.StringUtils;
public abstract class PrivilegeResourceProvider<T> extends org.apache.ambari.server.controller.internal.AbstractAuthorizedResourceProvider {
    private static org.apache.ambari.server.orm.dao.PrivilegeDAO privilegeDAO;

    private static org.apache.ambari.server.orm.dao.UserDAO userDAO;

    private static org.apache.ambari.server.orm.dao.GroupDAO groupDAO;

    private static org.apache.ambari.server.orm.dao.PrincipalDAO principalDAO;

    protected static org.apache.ambari.server.orm.dao.PermissionDAO permissionDAO;

    private static org.apache.ambari.server.orm.dao.ResourceDAO resourceDAO;

    public static final java.lang.String PRIVILEGE_INFO = "PrivilegeInfo";

    public static final java.lang.String PRIVILEGE_ID_PROPERTY_ID = "privilege_id";

    public static final java.lang.String PERMISSION_NAME_PROPERTY_ID = "permission_name";

    public static final java.lang.String PERMISSION_LABEL_PROPERTY_ID = "permission_label";

    public static final java.lang.String PRINCIPAL_NAME_PROPERTY_ID = "principal_name";

    public static final java.lang.String PRINCIPAL_TYPE_PROPERTY_ID = "principal_type";

    public static final java.lang.String VERSION_PROPERTY_ID = "version";

    public static final java.lang.String TYPE_PROPERTY_ID = "type";

    public static final java.lang.String PRIVILEGE_ID = (org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRIVILEGE_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRIVILEGE_ID_PROPERTY_ID;

    public static final java.lang.String PERMISSION_NAME = (org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRIVILEGE_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PERMISSION_NAME_PROPERTY_ID;

    public static final java.lang.String PERMISSION_LABEL = (org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRIVILEGE_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PERMISSION_LABEL_PROPERTY_ID;

    public static final java.lang.String PRINCIPAL_NAME = (org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRIVILEGE_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRINCIPAL_NAME_PROPERTY_ID;

    public static final java.lang.String PRINCIPAL_TYPE = (org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRIVILEGE_INFO + org.apache.ambari.server.controller.utilities.PropertyHelper.EXTERNAL_PATH_SEP) + org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRINCIPAL_TYPE_PROPERTY_ID;

    private final org.apache.ambari.server.controller.spi.Resource.Type resourceType;

    public PrivilegeResourceProvider(java.util.Set<java.lang.String> propertyIds, java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds, org.apache.ambari.server.controller.spi.Resource.Type resourceType) {
        super(resourceType, propertyIds, keyPropertyIds);
        this.resourceType = resourceType;
    }

    public static void init(org.apache.ambari.server.orm.dao.PrivilegeDAO privDAO, org.apache.ambari.server.orm.dao.UserDAO usrDAO, org.apache.ambari.server.orm.dao.GroupDAO grpDAO, org.apache.ambari.server.orm.dao.PrincipalDAO prinDAO, org.apache.ambari.server.orm.dao.PermissionDAO permDAO, org.apache.ambari.server.orm.dao.ResourceDAO resDAO) {
        org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.privilegeDAO = privDAO;
        org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.userDAO = usrDAO;
        org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.groupDAO = grpDAO;
        org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.principalDAO = prinDAO;
        org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.permissionDAO = permDAO;
        org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.resourceDAO = resDAO;
    }

    public abstract java.util.Map<java.lang.Long, T> getResourceEntities(java.util.Map<java.lang.String, java.lang.Object> properties) throws org.apache.ambari.server.AmbariException;

    public abstract java.lang.Long getResourceEntityId(org.apache.ambari.server.controller.spi.Predicate predicate);

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        for (java.util.Map<java.lang.String, java.lang.Object> properties : request.getProperties()) {
            createResources(getCreateCommand(properties));
        }
        notifyCreate(resourceType, request);
        return getRequestStatus(null);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        java.util.Set<java.lang.Long> resourceIds = new java.util.HashSet<>();
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertyMaps = getPropertyMaps(predicate);
        if (propertyMaps.isEmpty()) {
            propertyMaps.add(java.util.Collections.emptyMap());
        }
        for (java.util.Map<java.lang.String, java.lang.Object> properties : propertyMaps) {
            java.util.Map<java.lang.Long, T> resourceEntities;
            try {
                resourceEntities = getResourceEntities(properties);
            } catch (org.apache.ambari.server.AmbariException e) {
                throw new org.apache.ambari.server.controller.spi.SystemException("Could not get resource list from request", e);
            }
            resourceIds.addAll(resourceEntities.keySet());
            java.util.Set<org.apache.ambari.server.orm.entities.PrivilegeEntity> entitySet = new java.util.HashSet<>();
            java.util.List<org.apache.ambari.server.orm.entities.PrincipalEntity> userPrincipals = new java.util.LinkedList<>();
            java.util.List<org.apache.ambari.server.orm.entities.PrincipalEntity> groupPrincipals = new java.util.LinkedList<>();
            java.util.List<org.apache.ambari.server.orm.entities.PrincipalEntity> rolePrincipals = new java.util.LinkedList<>();
            java.util.List<org.apache.ambari.server.orm.entities.PrivilegeEntity> entities = org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.privilegeDAO.findAll();
            for (org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity : entities) {
                if (resourceIds.contains(privilegeEntity.getResource().getId())) {
                    org.apache.ambari.server.orm.entities.PrincipalEntity principal = privilegeEntity.getPrincipal();
                    java.lang.String principalType = principal.getPrincipalType().getName();
                    entitySet.add(privilegeEntity);
                    if (org.apache.ambari.server.orm.entities.PrincipalTypeEntity.USER_PRINCIPAL_TYPE_NAME.equals(principalType)) {
                        userPrincipals.add(principal);
                    } else if (org.apache.ambari.server.orm.entities.PrincipalTypeEntity.GROUP_PRINCIPAL_TYPE_NAME.equals(principalType)) {
                        groupPrincipals.add(principal);
                    } else if (org.apache.ambari.server.orm.entities.PrincipalTypeEntity.ROLE_PRINCIPAL_TYPE_NAME.equals(principalType)) {
                        rolePrincipals.add(principal);
                    }
                }
            }
            java.util.Map<java.lang.Long, org.apache.ambari.server.orm.entities.UserEntity> userEntities = new java.util.HashMap<>();
            if (!userPrincipals.isEmpty()) {
                java.util.List<org.apache.ambari.server.orm.entities.UserEntity> userList = org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.userDAO.findUsersByPrincipal(userPrincipals);
                for (org.apache.ambari.server.orm.entities.UserEntity userEntity : userList) {
                    userEntities.put(userEntity.getPrincipal().getId(), userEntity);
                }
            }
            java.util.Map<java.lang.Long, org.apache.ambari.server.orm.entities.GroupEntity> groupEntities = new java.util.HashMap<>();
            if (!groupPrincipals.isEmpty()) {
                java.util.List<org.apache.ambari.server.orm.entities.GroupEntity> groupList = org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.groupDAO.findGroupsByPrincipal(groupPrincipals);
                for (org.apache.ambari.server.orm.entities.GroupEntity groupEntity : groupList) {
                    groupEntities.put(groupEntity.getPrincipal().getId(), groupEntity);
                }
            }
            java.util.Map<java.lang.Long, org.apache.ambari.server.orm.entities.PermissionEntity> roleEntities = new java.util.HashMap<>();
            if (!rolePrincipals.isEmpty()) {
                java.util.List<org.apache.ambari.server.orm.entities.PermissionEntity> roleList = org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.permissionDAO.findPermissionsByPrincipal(rolePrincipals);
                for (org.apache.ambari.server.orm.entities.PermissionEntity roleEntity : roleList) {
                    roleEntities.put(roleEntity.getPrincipal().getId(), roleEntity);
                }
            }
            for (org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity : entitySet) {
                org.apache.ambari.server.controller.spi.Resource resource = toResource(privilegeEntity, userEntities, groupEntities, roleEntities, resourceEntities, requestedIds);
                if ((resource != null) && ((predicate == null) || predicate.evaluate(resource))) {
                    resources.add(resource);
                }
            }
        }
        return resources;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        modifyResources(getUpdateCommand(request, predicate));
        notifyUpdate(resourceType, request, predicate);
        return getRequestStatus(null);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        modifyResources(getDeleteCommand(predicate));
        notifyDelete(resourceType, predicate);
        return getRequestStatus(null);
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(getKeyPropertyIds().values());
    }

    protected boolean checkResourceTypes(org.apache.ambari.server.orm.entities.PrivilegeEntity entity) throws org.apache.ambari.server.AmbariException {
        java.lang.Integer resourceType = entity.getResource().getResourceType().getId();
        java.lang.Integer permissionResourceType = entity.getPermission().getResourceType().getId();
        return resourceType.equals(permissionResourceType);
    }

    protected org.apache.ambari.server.controller.spi.Resource toResource(org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity, java.util.Map<java.lang.Long, org.apache.ambari.server.orm.entities.UserEntity> userEntities, java.util.Map<java.lang.Long, org.apache.ambari.server.orm.entities.GroupEntity> groupEntities, java.util.Map<java.lang.Long, org.apache.ambari.server.orm.entities.PermissionEntity> roleEntities, java.util.Map<java.lang.Long, T> resourceEntities, java.util.Set<java.lang.String> requestedIds) {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(resourceType);
        org.apache.ambari.server.orm.entities.PrincipalEntity principal = privilegeEntity.getPrincipal();
        java.lang.String principalTypeName = null;
        java.lang.String resourcePropertyName = null;
        if (principal != null) {
            org.apache.ambari.server.orm.entities.PrincipalTypeEntity principalType = principal.getPrincipalType();
            if (principalType != null) {
                java.lang.Long principalId = principal.getId();
                principalTypeName = principalType.getName();
                if (org.apache.commons.lang.StringUtils.equalsIgnoreCase(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.GROUP_PRINCIPAL_TYPE_NAME, principalTypeName)) {
                    org.apache.ambari.server.orm.entities.GroupEntity groupEntity = groupEntities.get(principalId);
                    if (groupEntity != null) {
                        resourcePropertyName = groupEntity.getGroupName();
                    }
                } else if (org.apache.commons.lang.StringUtils.equalsIgnoreCase(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.ROLE_PRINCIPAL_TYPE_NAME, principalTypeName)) {
                    org.apache.ambari.server.orm.entities.PermissionEntity roleEntity = roleEntities.get(principalId);
                    if (roleEntity != null) {
                        resourcePropertyName = roleEntity.getPermissionName();
                    }
                } else if (org.apache.commons.lang.StringUtils.equalsIgnoreCase(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.USER_PRINCIPAL_TYPE_NAME, principalTypeName)) {
                    org.apache.ambari.server.orm.entities.UserEntity userEntity = userEntities.get(principalId);
                    if (userEntity != null) {
                        resourcePropertyName = userEntity.getUserName();
                    }
                }
            }
        }
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRIVILEGE_ID, privilegeEntity.getId(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PERMISSION_NAME, privilegeEntity.getPermission().getPermissionName(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PERMISSION_LABEL, privilegeEntity.getPermission().getPermissionLabel(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRINCIPAL_NAME, resourcePropertyName, requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRINCIPAL_TYPE, principalTypeName, requestedIds);
        return resource;
    }

    protected org.apache.ambari.server.orm.entities.PrivilegeEntity toEntity(java.util.Map<java.lang.String, java.lang.Object> properties, java.lang.Long resourceId) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.entities.PrivilegeEntity entity = new org.apache.ambari.server.orm.entities.PrivilegeEntity();
        java.lang.String permissionName = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PERMISSION_NAME)));
        org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity = org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.resourceDAO.findById(resourceId);
        org.apache.ambari.server.orm.entities.PermissionEntity permission = getPermission(permissionName, resourceEntity);
        if (permission == null) {
            throw new org.apache.ambari.server.AmbariException(("Can't find a permission named " + permissionName) + " for the resource.");
        }
        entity.setPermission(permission);
        entity.setResource(resourceEntity);
        java.lang.String principalName = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRINCIPAL_NAME)));
        java.lang.String principalType = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRINCIPAL_TYPE)));
        if (org.apache.commons.lang.StringUtils.equalsIgnoreCase(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.GROUP_PRINCIPAL_TYPE_NAME, principalType)) {
            org.apache.ambari.server.orm.entities.GroupEntity groupEntity = org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.groupDAO.findGroupByName(principalName);
            if (groupEntity != null) {
                entity.setPrincipal(org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.principalDAO.findById(groupEntity.getPrincipal().getId()));
            }
        } else if (org.apache.commons.lang.StringUtils.equalsIgnoreCase(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.ROLE_PRINCIPAL_TYPE_NAME, principalType)) {
            org.apache.ambari.server.orm.entities.PermissionEntity permissionEntity = org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.permissionDAO.findByName(principalName);
            if (permissionEntity != null) {
                entity.setPrincipal(org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.principalDAO.findById(permissionEntity.getPrincipal().getId()));
            }
        } else if (org.apache.commons.lang.StringUtils.equalsIgnoreCase(org.apache.ambari.server.orm.entities.PrincipalTypeEntity.USER_PRINCIPAL_TYPE_NAME, principalType)) {
            org.apache.ambari.server.orm.entities.UserEntity userEntity = org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.userDAO.findUserByName(principalName);
            if (userEntity != null) {
                entity.setPrincipal(org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.principalDAO.findById(userEntity.getPrincipal().getId()));
            }
        } else {
            throw new org.apache.ambari.server.AmbariException("Unknown principal type " + principalType);
        }
        if (entity.getPrincipal() == null) {
            throw new org.apache.ambari.server.AmbariException((("Could not find " + principalType) + " named ") + principalName);
        }
        return entity;
    }

    protected org.apache.ambari.server.orm.entities.PermissionEntity getPermission(java.lang.String permissionName, org.apache.ambari.server.orm.entities.ResourceEntity resourceEntity) throws org.apache.ambari.server.AmbariException {
        return org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.permissionDAO.findPermissionByNameAndType(permissionName, resourceEntity.getResourceType());
    }

    private org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void> getCreateCommand(final java.util.Map<java.lang.String, java.lang.Object> properties) {
        return new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                java.util.Set<java.lang.Long> resourceIds = getResourceEntities(properties).keySet();
                java.lang.Long resourceId = resourceIds.iterator().next();
                org.apache.ambari.server.orm.entities.PrivilegeEntity entity = toEntity(properties, resourceId);
                if (entity.getPrincipal() == null) {
                    throw new org.apache.ambari.server.AmbariException(((("Can't find principal " + properties.get(org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRINCIPAL_TYPE)) + " ") + properties.get(org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRINCIPAL_NAME)) + " for privilege.");
                }
                if (org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.privilegeDAO.exists(entity)) {
                    throw new org.apache.ambari.server.DuplicateResourceException("The privilege already exists.");
                }
                if (!checkResourceTypes(entity)) {
                    throw new org.apache.ambari.server.AmbariException(((("Can't grant " + entity.getPermission().getResourceType().getName()) + " permission on a ") + entity.getResource().getResourceType().getName()) + " resource.");
                }
                org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.privilegeDAO.create(entity);
                entity.getPrincipal().getPrivileges().add(entity);
                org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.principalDAO.merge(entity.getPrincipal());
                return null;
            }
        };
    }

    private org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void> getDeleteCommand(final org.apache.ambari.server.controller.spi.Predicate predicate) {
        return new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                try {
                    for (java.util.Map<java.lang.String, java.lang.Object> resource : getPropertyMaps(predicate)) {
                        if (resource.get(org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRIVILEGE_ID) == null) {
                            throw new org.apache.ambari.server.AmbariException("Privilege ID should be provided for this request");
                        }
                        org.apache.ambari.server.orm.entities.PrivilegeEntity entity = org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.privilegeDAO.findById(java.lang.Integer.valueOf(resource.get(org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRIVILEGE_ID).toString()));
                        if (entity != null) {
                            if (!checkResourceTypes(entity)) {
                                throw new org.apache.ambari.server.AmbariException(((("Can't remove " + entity.getPermission().getResourceType().getName()) + " permission from a ") + entity.getResource().getResourceType().getName()) + " resource.");
                            }
                            entity.getPrincipal().getPrivileges().remove(entity);
                            org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.principalDAO.merge(entity.getPrincipal());
                            org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.privilegeDAO.remove(entity);
                        }
                    }
                } catch (java.lang.Exception e) {
                    throw new org.apache.ambari.server.AmbariException("Caught exception deleting privilege.", e);
                }
                return null;
            }
        };
    }

    private org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void> getUpdateCommand(final org.apache.ambari.server.controller.spi.Request request, final org.apache.ambari.server.controller.spi.Predicate predicate) {
        return new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                java.lang.Long resource = null;
                final java.util.List<org.apache.ambari.server.orm.entities.PrivilegeEntity> requiredEntities = new java.util.ArrayList<>();
                for (java.util.Map<java.lang.String, java.lang.Object> properties : request.getProperties()) {
                    java.util.Set<java.lang.Long> resourceIds = getResourceEntities(properties).keySet();
                    java.lang.Long resourceId = resourceIds.iterator().next();
                    if ((resource != null) && (!resourceId.equals(resource))) {
                        throw new org.apache.ambari.server.AmbariException("Can't update privileges of multiple resources in one request");
                    }
                    resource = resourceId;
                    org.apache.ambari.server.orm.entities.PrivilegeEntity entity = toEntity(properties, resourceId);
                    requiredEntities.add(entity);
                }
                if (resource == null) {
                    resource = getResourceEntityId(predicate);
                    if (resource == null) {
                        return null;
                    }
                }
                final java.util.List<org.apache.ambari.server.orm.entities.PrivilegeEntity> currentPrivileges = org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.privilegeDAO.findByResourceId(resource);
                for (org.apache.ambari.server.orm.entities.PrivilegeEntity requiredPrivilege : requiredEntities) {
                    boolean isInBothLists = false;
                    for (org.apache.ambari.server.orm.entities.PrivilegeEntity currentPrivilege : currentPrivileges) {
                        if (requiredPrivilege.getPermission().getPermissionName().equals(currentPrivilege.getPermission().getPermissionName()) && requiredPrivilege.getPrincipal().getId().equals(currentPrivilege.getPrincipal().getId())) {
                            isInBothLists = true;
                            break;
                        }
                    }
                    if (!isInBothLists) {
                        if (!checkResourceTypes(requiredPrivilege)) {
                            throw new org.apache.ambari.server.AmbariException(((("Can't grant " + requiredPrivilege.getPermission().getResourceType().getName()) + " permission on a ") + requiredPrivilege.getResource().getResourceType().getName()) + " resource.");
                        }
                        org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.privilegeDAO.create(requiredPrivilege);
                        requiredPrivilege.getPrincipal().getPrivileges().add(requiredPrivilege);
                        org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.principalDAO.merge(requiredPrivilege.getPrincipal());
                    }
                }
                for (org.apache.ambari.server.orm.entities.PrivilegeEntity currentPrivilege : currentPrivileges) {
                    boolean isInBothLists = false;
                    for (org.apache.ambari.server.orm.entities.PrivilegeEntity requiredPrivilege : requiredEntities) {
                        if (requiredPrivilege.getPermission().getPermissionName().equals(currentPrivilege.getPermission().getPermissionName()) && requiredPrivilege.getPrincipal().getId().equals(currentPrivilege.getPrincipal().getId())) {
                            isInBothLists = true;
                            break;
                        }
                    }
                    if (!isInBothLists) {
                        if (!checkResourceTypes(currentPrivilege)) {
                            throw new org.apache.ambari.server.AmbariException(((("Can't remove " + currentPrivilege.getPermission().getResourceType().getName()) + " permission from a ") + currentPrivilege.getResource().getResourceType().getName()) + " resource.");
                        }
                        currentPrivilege.getPrincipal().getPrivileges().remove(currentPrivilege);
                        org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.principalDAO.merge(currentPrivilege.getPrincipal());
                        org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.privilegeDAO.remove(currentPrivilege);
                    }
                }
                return null;
            }
        };
    }
}