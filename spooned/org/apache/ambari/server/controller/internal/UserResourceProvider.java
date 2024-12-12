package org.apache.ambari.server.controller.internal;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import org.apache.commons.lang.StringUtils;
public class UserResourceProvider extends org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider implements org.apache.ambari.server.controller.spi.ResourcePredicateEvaluator {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.UserResourceProvider.class);

    public static final java.lang.String USER_RESOURCE_CATEGORY = "Users";

    public static final java.lang.String USERNAME_PROPERTY_ID = "user_name";

    public static final java.lang.String DISPLAY_NAME_PROPERTY_ID = "display_name";

    public static final java.lang.String LOCAL_USERNAME_PROPERTY_ID = "local_user_name";

    public static final java.lang.String ACTIVE_PROPERTY_ID = "active";

    public static final java.lang.String CREATE_TIME_PROPERTY_ID = "created";

    public static final java.lang.String CONSECUTIVE_FAILURES_PROPERTY_ID = "consecutive_failures";

    public static final java.lang.String ADMIN_PROPERTY_ID = "admin";

    public static final java.lang.String GROUPS_PROPERTY_ID = "groups";

    public static final java.lang.String USER_USERNAME_PROPERTY_ID = (org.apache.ambari.server.controller.internal.UserResourceProvider.USER_RESOURCE_CATEGORY + "/") + org.apache.ambari.server.controller.internal.UserResourceProvider.USERNAME_PROPERTY_ID;

    public static final java.lang.String USER_DISPLAY_NAME_PROPERTY_ID = (org.apache.ambari.server.controller.internal.UserResourceProvider.USER_RESOURCE_CATEGORY + "/") + org.apache.ambari.server.controller.internal.UserResourceProvider.DISPLAY_NAME_PROPERTY_ID;

    public static final java.lang.String USER_LOCAL_USERNAME_PROPERTY_ID = (org.apache.ambari.server.controller.internal.UserResourceProvider.USER_RESOURCE_CATEGORY + "/") + org.apache.ambari.server.controller.internal.UserResourceProvider.LOCAL_USERNAME_PROPERTY_ID;

    public static final java.lang.String USER_ACTIVE_PROPERTY_ID = (org.apache.ambari.server.controller.internal.UserResourceProvider.USER_RESOURCE_CATEGORY + "/") + org.apache.ambari.server.controller.internal.UserResourceProvider.ACTIVE_PROPERTY_ID;

    public static final java.lang.String USER_CREATE_TIME_PROPERTY_ID = (org.apache.ambari.server.controller.internal.UserResourceProvider.USER_RESOURCE_CATEGORY + "/") + org.apache.ambari.server.controller.internal.UserResourceProvider.CREATE_TIME_PROPERTY_ID;

    public static final java.lang.String USER_CONSECUTIVE_FAILURES_PROPERTY_ID = (org.apache.ambari.server.controller.internal.UserResourceProvider.USER_RESOURCE_CATEGORY + "/") + org.apache.ambari.server.controller.internal.UserResourceProvider.CONSECUTIVE_FAILURES_PROPERTY_ID;

    public static final java.lang.String USER_ADMIN_PROPERTY_ID = (org.apache.ambari.server.controller.internal.UserResourceProvider.USER_RESOURCE_CATEGORY + "/") + org.apache.ambari.server.controller.internal.UserResourceProvider.ADMIN_PROPERTY_ID;

    public static final java.lang.String USER_GROUPS_PROPERTY_ID = (org.apache.ambari.server.controller.internal.UserResourceProvider.USER_RESOURCE_CATEGORY + "/") + org.apache.ambari.server.controller.internal.UserResourceProvider.GROUPS_PROPERTY_ID;

    @java.lang.Deprecated
    public static final java.lang.String PASSWORD_PROPERTY_ID = "password";

    @java.lang.Deprecated
    public static final java.lang.String OLD_PASSWORD_PROPERTY_ID = "old_password";

    @java.lang.Deprecated
    public static final java.lang.String LDAP_USER_PROPERTY_ID = "ldap_user";

    @java.lang.Deprecated
    public static final java.lang.String USER_TYPE_PROPERTY_ID = "user_type";

    @java.lang.Deprecated
    public static final java.lang.String USER_PASSWORD_PROPERTY_ID = (org.apache.ambari.server.controller.internal.UserResourceProvider.USER_RESOURCE_CATEGORY + "/") + org.apache.ambari.server.controller.internal.UserResourceProvider.PASSWORD_PROPERTY_ID;

    @java.lang.Deprecated
    public static final java.lang.String USER_OLD_PASSWORD_PROPERTY_ID = (org.apache.ambari.server.controller.internal.UserResourceProvider.USER_RESOURCE_CATEGORY + "/") + org.apache.ambari.server.controller.internal.UserResourceProvider.OLD_PASSWORD_PROPERTY_ID;

    @java.lang.Deprecated
    public static final java.lang.String USER_LDAP_USER_PROPERTY_ID = (org.apache.ambari.server.controller.internal.UserResourceProvider.USER_RESOURCE_CATEGORY + "/") + org.apache.ambari.server.controller.internal.UserResourceProvider.LDAP_USER_PROPERTY_ID;

    @java.lang.Deprecated
    public static final java.lang.String USER_USER_TYPE_PROPERTY_ID = (org.apache.ambari.server.controller.internal.UserResourceProvider.USER_RESOURCE_CATEGORY + "/") + org.apache.ambari.server.controller.internal.UserResourceProvider.USER_TYPE_PROPERTY_ID;

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds = com.google.common.collect.ImmutableMap.<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String>builder().put(org.apache.ambari.server.controller.spi.Resource.Type.User, org.apache.ambari.server.controller.internal.UserResourceProvider.USER_USERNAME_PROPERTY_ID).build();

    private static final java.util.Set<java.lang.String> propertyIds = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_USERNAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.UserResourceProvider.USER_DISPLAY_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.UserResourceProvider.USER_LOCAL_USERNAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.UserResourceProvider.USER_ACTIVE_PROPERTY_ID, org.apache.ambari.server.controller.internal.UserResourceProvider.USER_CREATE_TIME_PROPERTY_ID, org.apache.ambari.server.controller.internal.UserResourceProvider.USER_CONSECUTIVE_FAILURES_PROPERTY_ID, org.apache.ambari.server.controller.internal.UserResourceProvider.USER_GROUPS_PROPERTY_ID, org.apache.ambari.server.controller.internal.UserResourceProvider.USER_PASSWORD_PROPERTY_ID, org.apache.ambari.server.controller.internal.UserResourceProvider.USER_OLD_PASSWORD_PROPERTY_ID, org.apache.ambari.server.controller.internal.UserResourceProvider.USER_LDAP_USER_PROPERTY_ID, org.apache.ambari.server.controller.internal.UserResourceProvider.USER_USER_TYPE_PROPERTY_ID, org.apache.ambari.server.controller.internal.UserResourceProvider.USER_ADMIN_PROPERTY_ID);

    @com.google.inject.Inject
    private org.apache.ambari.server.security.authorization.Users users;

    @com.google.inject.assistedinject.AssistedInject
    UserResourceProvider(@com.google.inject.assistedinject.Assisted
    org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(org.apache.ambari.server.controller.spi.Resource.Type.User, org.apache.ambari.server.controller.internal.UserResourceProvider.propertyIds, org.apache.ambari.server.controller.internal.UserResourceProvider.keyPropertyIds, managementController);
        setRequiredCreateAuthorizations(java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_USERS));
        setRequiredDeleteAuthorizations(java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_USERS));
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.UserRequest> requests = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : request.getProperties()) {
            requests.add(getRequest(propertyMap));
        }
        createResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                try {
                    createUsers(requests);
                } catch (org.apache.ambari.server.security.authorization.AuthorizationException e) {
                    throw new org.apache.ambari.server.AmbariException(e.getMessage(), e);
                }
                return null;
            }
        });
        return getRequestStatus(null);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.UserRequest> requests = new java.util.HashSet<>();
        if (predicate == null) {
            requests.add(getRequest(null));
        } else {
            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
                requests.add(getRequest(propertyMap));
            }
        }
        java.util.Set<org.apache.ambari.server.controller.UserResponse> responses = getResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.util.Set<org.apache.ambari.server.controller.UserResponse>>() {
            @java.lang.Override
            public java.util.Set<org.apache.ambari.server.controller.UserResponse> invoke() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
                return getUsers(requests);
            }
        });
        if (org.apache.ambari.server.controller.internal.UserResourceProvider.LOG.isDebugEnabled()) {
            org.apache.ambari.server.controller.internal.UserResourceProvider.LOG.debug("Found user responses matching get user request, userRequestSize={}, userResponseSize={}", requests.size(), responses.size());
        }
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.UserResponse userResponse : responses) {
            org.apache.ambari.server.controller.internal.ResourceImpl resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.User);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UserResourceProvider.USER_USERNAME_PROPERTY_ID, userResponse.getUsername(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UserResourceProvider.USER_DISPLAY_NAME_PROPERTY_ID, userResponse.getDisplayName(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UserResourceProvider.USER_LOCAL_USERNAME_PROPERTY_ID, userResponse.getLocalUsername(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UserResourceProvider.USER_LDAP_USER_PROPERTY_ID, userResponse.isLdapUser(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UserResourceProvider.USER_USER_TYPE_PROPERTY_ID, userResponse.getAuthenticationType(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UserResourceProvider.USER_ACTIVE_PROPERTY_ID, userResponse.isActive(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UserResourceProvider.USER_GROUPS_PROPERTY_ID, userResponse.getGroups(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UserResourceProvider.USER_ADMIN_PROPERTY_ID, userResponse.isAdmin(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UserResourceProvider.USER_CONSECUTIVE_FAILURES_PROPERTY_ID, userResponse.getConsecutiveFailures(), requestedIds);
            org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UserResourceProvider.USER_CREATE_TIME_PROPERTY_ID, userResponse.getCreateTime(), requestedIds);
            resources.add(resource);
        }
        return resources;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.UserRequest> requests = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(request.getProperties().iterator().next(), predicate)) {
            org.apache.ambari.server.controller.UserRequest req = getRequest(propertyMap);
            requests.add(req);
        }
        modifyResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
                updateUsers(requests);
                return null;
            }
        });
        return getRequestStatus(null);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.UserRequest> requests = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
            org.apache.ambari.server.controller.UserRequest req = getRequest(propertyMap);
            requests.add(req);
        }
        modifyResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                deleteUsers(requests);
                return null;
            }
        });
        return getRequestStatus(null);
    }

    @java.lang.Override
    public boolean evaluate(org.apache.ambari.server.controller.spi.Predicate predicate, org.apache.ambari.server.controller.spi.Resource resource) {
        if (predicate instanceof org.apache.ambari.server.controller.predicate.EqualsPredicate) {
            org.apache.ambari.server.controller.predicate.EqualsPredicate equalsPredicate = ((org.apache.ambari.server.controller.predicate.EqualsPredicate) (predicate));
            java.lang.String propertyId = equalsPredicate.getPropertyId();
            if (propertyId.equals(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_USERNAME_PROPERTY_ID)) {
                return equalsPredicate.evaluateIgnoreCase(resource);
            }
        }
        return predicate.evaluate(resource);
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return new java.util.HashSet<>(org.apache.ambari.server.controller.internal.UserResourceProvider.keyPropertyIds.values());
    }

    private org.apache.ambari.server.controller.UserRequest getRequest(java.util.Map<java.lang.String, java.lang.Object> properties) {
        if (properties == null) {
            return new org.apache.ambari.server.controller.UserRequest(null);
        }
        org.apache.ambari.server.controller.UserRequest request = new org.apache.ambari.server.controller.UserRequest(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_USERNAME_PROPERTY_ID))));
        request.setDisplayName(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_DISPLAY_NAME_PROPERTY_ID))));
        request.setLocalUserName(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_LOCAL_USERNAME_PROPERTY_ID))));
        request.setPassword(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_PASSWORD_PROPERTY_ID))));
        request.setOldPassword(((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_OLD_PASSWORD_PROPERTY_ID))));
        if (null != properties.get(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_ACTIVE_PROPERTY_ID)) {
            request.setActive(java.lang.Boolean.valueOf(properties.get(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_ACTIVE_PROPERTY_ID).toString()));
        }
        if (null != properties.get(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_ADMIN_PROPERTY_ID)) {
            request.setAdmin(java.lang.Boolean.valueOf(properties.get(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_ADMIN_PROPERTY_ID).toString()));
        }
        if (null != properties.get(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_CONSECUTIVE_FAILURES_PROPERTY_ID)) {
            request.setConsecutiveFailures(java.lang.Integer.parseInt(properties.get(org.apache.ambari.server.controller.internal.UserResourceProvider.USER_CONSECUTIVE_FAILURES_PROPERTY_ID).toString()));
        }
        return request;
    }

    private void createUsers(java.util.Set<org.apache.ambari.server.controller.UserRequest> requests) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        for (org.apache.ambari.server.controller.UserRequest request : requests) {
            java.lang.String username = request.getUsername();
            if (org.apache.commons.lang.StringUtils.isEmpty(username)) {
                throw new org.apache.ambari.server.AmbariException("Username must be supplied.");
            }
            if (users.getUser(username) != null) {
                java.lang.String message;
                if (requests.size() == 1) {
                    message = "The requested username already exists.";
                } else {
                    message = "One or more of the requested usernames already exists.";
                }
                throw new org.apache.ambari.server.DuplicateResourceException(message);
            }
        }
        for (org.apache.ambari.server.controller.UserRequest request : requests) {
            java.lang.String username = request.getUsername();
            java.lang.String displayName = org.apache.commons.lang.StringUtils.defaultIfEmpty(request.getDisplayName(), username);
            java.lang.String localUserName = org.apache.commons.lang.StringUtils.defaultIfEmpty(request.getLocalUserName(), username);
            java.lang.String password = request.getPassword();
            if (!org.apache.commons.lang.StringUtils.isEmpty(password)) {
                users.validatePassword(password, username);
            }
            org.apache.ambari.server.orm.entities.UserEntity userEntity = users.createUser(username, localUserName, displayName, request.isActive());
            if (userEntity != null) {
                if (java.lang.Boolean.TRUE.equals(request.isAdmin())) {
                    users.grantAdminPrivilege(userEntity);
                }
                if (!org.apache.commons.lang.StringUtils.isEmpty(password)) {
                    addOrUpdateLocalAuthenticationSource(true, userEntity, request.getPassword(), null);
                }
            }
        }
    }

    private void updateUsers(java.util.Set<org.apache.ambari.server.controller.UserRequest> requests) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        boolean asUserAdministrator = org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.AMBARI, null, org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_USERS);
        java.lang.String authenticatedUsername = org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthenticatedName();
        for (final org.apache.ambari.server.controller.UserRequest request : requests) {
            java.lang.String requestedUsername = request.getUsername();
            if ((!asUserAdministrator) && (!authenticatedUsername.equalsIgnoreCase(requestedUsername))) {
                throw new org.apache.ambari.server.security.authorization.AuthorizationException();
            }
            org.apache.ambari.server.orm.entities.UserEntity userEntity = users.getUserEntity(requestedUsername);
            if (null == userEntity) {
                continue;
            }
            boolean hasUpdates = false;
            if (isValueChanged(request.isActive(), userEntity.getActive())) {
                if (!asUserAdministrator) {
                    throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user is not authorized to update the requested user's active property");
                }
                hasUpdates = true;
            }
            if (isValueChanged(request.getLocalUserName(), userEntity.getLocalUsername())) {
                if (!asUserAdministrator) {
                    throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user is not authorized to update the requested user's local username property");
                }
                hasUpdates = true;
            }
            hasUpdates = hasUpdates || isValueChanged(request.getDisplayName(), userEntity.getDisplayName());
            if (hasUpdates) {
                users.safelyUpdateUserEntity(userEntity, new org.apache.ambari.server.security.authorization.Users.Command() {
                    @java.lang.Override
                    public void perform(org.apache.ambari.server.orm.entities.UserEntity userEntity) {
                        if (isValueChanged(request.isActive(), userEntity.getActive())) {
                            userEntity.setActive(request.isActive());
                        }
                        if (isValueChanged(request.getLocalUserName(), userEntity.getLocalUsername())) {
                            userEntity.setLocalUsername(request.getLocalUserName());
                        }
                        if (isValueChanged(request.getDisplayName(), userEntity.getDisplayName())) {
                            userEntity.setDisplayName(request.getDisplayName());
                        }
                    }
                });
            }
            if (null != request.isAdmin()) {
                if (!asUserAdministrator) {
                    throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user is not authorized to update the requested resource property");
                }
                if (request.isAdmin()) {
                    users.grantAdminPrivilege(userEntity);
                } else {
                    users.revokeAdminPrivilege(userEntity);
                }
            }
            if (request.getPassword() != null) {
                addOrUpdateLocalAuthenticationSource(asUserAdministrator, userEntity, request.getPassword(), request.getOldPassword());
            }
            if (request.getConsecutiveFailures() != null) {
                if (!asUserAdministrator) {
                    throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user is not authorized to update the requested resource property");
                }
                users.safelyUpdateUserEntity(userEntity, user -> user.setConsecutiveFailures(request.getConsecutiveFailures()));
            }
        }
    }

    private void addOrUpdateLocalAuthenticationSource(boolean asUserAdministrator, org.apache.ambari.server.orm.entities.UserEntity subjectUserEntity, java.lang.String password, java.lang.String oldPassword) throws org.apache.ambari.server.security.authorization.AuthorizationException, org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.UserAuthenticationSource, getManagementController());
        if (provider != null) {
            org.apache.ambari.server.orm.entities.UserAuthenticationEntity userAuthenticationEntity = null;
            java.util.List<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> authenticationEntities = subjectUserEntity.getAuthenticationEntities();
            for (org.apache.ambari.server.orm.entities.UserAuthenticationEntity authenticationEntity : authenticationEntities) {
                if (authenticationEntity.getAuthenticationType() == org.apache.ambari.server.security.authorization.UserAuthenticationType.LOCAL) {
                    userAuthenticationEntity = authenticationEntity;
                    break;
                }
            }
            if (userAuthenticationEntity == null) {
                if (!asUserAdministrator) {
                    throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user is not authorized to create a local authentication source.");
                } else {
                    java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertiesSet = new java.util.HashSet<>();
                    java.util.Map<java.lang.String, java.lang.Object> properties;
                    properties = new java.util.LinkedHashMap<>();
                    properties.put(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_USER_NAME_PROPERTY_ID, subjectUserEntity.getUserName());
                    properties.put(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_AUTHENTICATION_TYPE_PROPERTY_ID, org.apache.ambari.server.security.authorization.UserAuthenticationType.LOCAL.name());
                    properties.put(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_KEY_PROPERTY_ID, password);
                    propertiesSet.add(properties);
                    try {
                        provider.createResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertiesSet, null));
                    } catch (java.lang.Exception e) {
                        throw new org.apache.ambari.server.AmbariException(e.getMessage(), e);
                    }
                }
            } else {
                java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
                properties.put(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_OLD_KEY_PROPERTY_ID, oldPassword);
                properties.put(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_KEY_PROPERTY_ID, password);
                org.apache.ambari.server.controller.spi.Predicate predicate1 = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_USER_NAME_PROPERTY_ID).equals(subjectUserEntity.getUserName()).toPredicate();
                org.apache.ambari.server.controller.spi.Predicate predicate2 = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_AUTHENTICATION_SOURCE_ID_PROPERTY_ID).equals(convertIdToString(userAuthenticationEntity.getUserAuthenticationId())).toPredicate();
                try {
                    provider.updateResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(properties, null), new org.apache.ambari.server.controller.predicate.AndPredicate(predicate1, predicate2));
                } catch (java.lang.Exception e) {
                    throw new org.apache.ambari.server.AmbariException(e.getMessage(), e);
                }
            }
        }
    }

    private java.lang.String convertIdToString(java.lang.Long id) {
        if (id == null) {
            return null;
        } else {
            java.text.NumberFormat format = java.text.NumberFormat.getIntegerInstance();
            format.setGroupingUsed(false);
            return format.format(id);
        }
    }

    private boolean isValueChanged(java.lang.Object newValue, java.lang.Object currentValue) {
        return (newValue != null) && (!newValue.equals(currentValue));
    }

    private void deleteUsers(java.util.Set<org.apache.ambari.server.controller.UserRequest> requests) throws org.apache.ambari.server.AmbariException {
        for (org.apache.ambari.server.controller.UserRequest r : requests) {
            java.lang.String username = r.getUsername();
            if (!org.apache.commons.lang.StringUtils.isEmpty(username)) {
                if (org.apache.ambari.server.controller.internal.UserResourceProvider.LOG.isDebugEnabled()) {
                    org.apache.ambari.server.controller.internal.UserResourceProvider.LOG.debug("Received a delete user request, username= {}", username);
                }
                users.removeUser(users.getUserEntity(username));
            }
        }
    }

    private java.util.Set<org.apache.ambari.server.controller.UserResponse> getUsers(java.util.Set<org.apache.ambari.server.controller.UserRequest> requests) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.util.Set<org.apache.ambari.server.controller.UserResponse> responses = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.UserRequest r : requests) {
            if (org.apache.ambari.server.controller.internal.UserResourceProvider.LOG.isDebugEnabled()) {
                org.apache.ambari.server.controller.internal.UserResourceProvider.LOG.debug("Received a getUsers request, userRequest={}", r.toString());
            }
            java.lang.String requestedUsername = r.getUsername();
            java.lang.String authenticatedUsername = org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthenticatedName();
            if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.AMBARI, null, org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_USERS)) {
                if (null == requestedUsername) {
                    requestedUsername = authenticatedUsername;
                } else if (!requestedUsername.equalsIgnoreCase(authenticatedUsername)) {
                    throw new org.apache.ambari.server.security.authorization.AuthorizationException();
                }
            }
            if (null == requestedUsername) {
                for (org.apache.ambari.server.orm.entities.UserEntity u : users.getAllUserEntities()) {
                    responses.add(createUserResponse(u));
                }
            } else {
                org.apache.ambari.server.orm.entities.UserEntity u = users.getUserEntity(requestedUsername);
                if (null == u) {
                    if (requests.size() == 1) {
                        throw new org.apache.ambari.server.ObjectNotFoundException(("Cannot find user '" + requestedUsername) + "'");
                    }
                } else {
                    responses.add(createUserResponse(u));
                }
            }
        }
        return responses;
    }

    private org.apache.ambari.server.controller.UserResponse createUserResponse(org.apache.ambari.server.orm.entities.UserEntity userEntity) {
        java.util.List<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> authenticationEntities = userEntity.getAuthenticationEntities();
        boolean isLdapUser = false;
        org.apache.ambari.server.security.authorization.UserAuthenticationType userType = org.apache.ambari.server.security.authorization.UserAuthenticationType.LOCAL;
        for (org.apache.ambari.server.orm.entities.UserAuthenticationEntity authenticationEntity : authenticationEntities) {
            if (authenticationEntity.getAuthenticationType() == org.apache.ambari.server.security.authorization.UserAuthenticationType.LDAP) {
                isLdapUser = true;
                userType = org.apache.ambari.server.security.authorization.UserAuthenticationType.LDAP;
            } else if (authenticationEntity.getAuthenticationType() == org.apache.ambari.server.security.authorization.UserAuthenticationType.PAM) {
                userType = org.apache.ambari.server.security.authorization.UserAuthenticationType.PAM;
            }
        }
        java.util.Set<java.lang.String> groups = new java.util.HashSet<>();
        for (org.apache.ambari.server.orm.entities.MemberEntity memberEntity : userEntity.getMemberEntities()) {
            groups.add(memberEntity.getGroup().getGroupName());
        }
        boolean isAdmin = users.hasAdminPrivilege(userEntity);
        org.apache.ambari.server.controller.UserResponse userResponse = new org.apache.ambari.server.controller.UserResponse(userEntity.getUserName(), userEntity.getDisplayName(), userEntity.getLocalUsername(), userType, isLdapUser, userEntity.getActive(), isAdmin, userEntity.getConsecutiveFailures(), new java.util.Date(userEntity.getCreateTime()));
        userResponse.setGroups(groups);
        return userResponse;
    }
}