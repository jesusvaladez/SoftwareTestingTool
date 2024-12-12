package org.apache.ambari.server.controller.internal;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.exception.ResourceNotFoundException;
public class UserAuthenticationSourceResourceProvider extends org.apache.ambari.server.controller.internal.AbstractAuthorizedResourceProvider {
    public static final java.lang.String AUTHENTICATION_SOURCE_RESOURCE_CATEGORY = "AuthenticationSourceInfo";

    public static final java.lang.String AUTHENTICATION_SOURCE_ID_PROPERTY_ID = "source_id";

    public static final java.lang.String USER_NAME_PROPERTY_ID = "user_name";

    public static final java.lang.String AUTHENTICATION_TYPE_PROPERTY_ID = "authentication_type";

    public static final java.lang.String KEY_PROPERTY_ID = "key";

    public static final java.lang.String OLD_KEY_PROPERTY_ID = "old_key";

    public static final java.lang.String CREATED_PROPERTY_ID = "created";

    public static final java.lang.String UPDATED_PROPERTY_ID = "updated";

    public static final java.lang.String AUTHENTICATION_AUTHENTICATION_SOURCE_ID_PROPERTY_ID = (org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_SOURCE_RESOURCE_CATEGORY + "/") + org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_SOURCE_ID_PROPERTY_ID;

    public static final java.lang.String AUTHENTICATION_USER_NAME_PROPERTY_ID = (org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_SOURCE_RESOURCE_CATEGORY + "/") + org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.USER_NAME_PROPERTY_ID;

    public static final java.lang.String AUTHENTICATION_AUTHENTICATION_TYPE_PROPERTY_ID = (org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_SOURCE_RESOURCE_CATEGORY + "/") + org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_TYPE_PROPERTY_ID;

    public static final java.lang.String AUTHENTICATION_KEY_PROPERTY_ID = (org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_SOURCE_RESOURCE_CATEGORY + "/") + org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.KEY_PROPERTY_ID;

    public static final java.lang.String AUTHENTICATION_OLD_KEY_PROPERTY_ID = (org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_SOURCE_RESOURCE_CATEGORY + "/") + org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.OLD_KEY_PROPERTY_ID;

    public static final java.lang.String AUTHENTICATION_CREATED_PROPERTY_ID = (org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_SOURCE_RESOURCE_CATEGORY + "/") + org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.CREATED_PROPERTY_ID;

    public static final java.lang.String AUTHENTICATION_UPDATED_PROPERTY_ID = (org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_SOURCE_RESOURCE_CATEGORY + "/") + org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.UPDATED_PROPERTY_ID;

    private static final java.util.Set<java.lang.String> PK_PROPERTY_IDS = com.google.common.collect.ImmutableSet.of(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_AUTHENTICATION_SOURCE_ID_PROPERTY_ID);

    private static final java.util.Set<java.lang.String> PROPERTY_IDS = com.google.common.collect.ImmutableSet.of(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_AUTHENTICATION_SOURCE_ID_PROPERTY_ID, org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_USER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_AUTHENTICATION_TYPE_PROPERTY_ID, org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_KEY_PROPERTY_ID, org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_OLD_KEY_PROPERTY_ID, org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_CREATED_PROPERTY_ID, org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_UPDATED_PROPERTY_ID);

    private static final java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> KEY_PROPERTY_IDS = com.google.common.collect.ImmutableMap.of(org.apache.ambari.server.controller.spi.Resource.Type.User, org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_USER_NAME_PROPERTY_ID, org.apache.ambari.server.controller.spi.Resource.Type.UserAuthenticationSource, org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_AUTHENTICATION_SOURCE_ID_PROPERTY_ID);

    @com.google.inject.Inject
    private org.apache.ambari.server.security.authorization.Users users;

    public UserAuthenticationSourceResourceProvider() {
        super(org.apache.ambari.server.controller.spi.Resource.Type.UserAuthenticationSource, org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.PROPERTY_IDS, org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.KEY_PROPERTY_IDS);
        java.util.EnumSet<org.apache.ambari.server.security.authorization.RoleAuthorization> requiredAuthorizations = java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_USERS);
        setRequiredCreateAuthorizations(requiredAuthorizations);
        setRequiredDeleteAuthorizations(requiredAuthorizations);
        setRequiredGetAuthorizations(requiredAuthorizations);
        setRequiredUpdateAuthorizations(requiredAuthorizations);
    }

    @java.lang.Override
    protected java.util.Set<java.lang.String> getPKPropertyIds() {
        return org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.PK_PROPERTY_IDS;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.UserAuthenticationSourceRequest> requests = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : request.getProperties()) {
            requests.add(getRequest(propertyMap));
        }
        createResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException {
                createUserAuthenticationSources(requests);
                return null;
            }
        });
        return getRequestStatus(null);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.UserAuthenticationSourceRequest> requests = new java.util.HashSet<>();
        if (predicate == null) {
            requests.add(getRequest(null));
        } else {
            for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
                requests.add(getRequest(propertyMap));
            }
        }
        java.util.Set<org.apache.ambari.server.controller.UserAuthenticationSourceResponse> responses = getResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.util.Set<org.apache.ambari.server.controller.UserAuthenticationSourceResponse>>() {
            @java.lang.Override
            public java.util.Set<org.apache.ambari.server.controller.UserAuthenticationSourceResponse> invoke() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
                return getUserAuthenticationSources(requests);
            }
        });
        java.util.Set<java.lang.String> requestedIds = getRequestPropertyIds(request, predicate);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.UserAuthenticationSourceResponse response : responses) {
            resources.add(toResource(response, requestedIds));
        }
        return resources;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.UserAuthenticationSourceRequest> requests = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(request.getProperties().iterator().next(), predicate)) {
            requests.add(getRequest(propertyMap));
        }
        modifyResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
                updateUserAuthenticationSources(requests);
                return null;
            }
        });
        return getRequestStatus(null);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        final java.util.Set<org.apache.ambari.server.controller.UserAuthenticationSourceRequest> requests = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : getPropertyMaps(predicate)) {
            requests.add(getRequest(propertyMap));
        }
        modifyResources(new org.apache.ambari.server.controller.internal.AbstractResourceProvider.Command<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void invoke() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
                deleteUserAuthenticationSources(requests);
                return null;
            }
        });
        return getRequestStatus(null);
    }

    private org.apache.ambari.server.controller.UserAuthenticationSourceRequest getRequest(java.util.Map<java.lang.String, java.lang.Object> properties) {
        java.lang.String username;
        java.lang.Long sourceId;
        org.apache.ambari.server.security.authorization.UserAuthenticationType authenticationType;
        java.lang.String key;
        java.lang.String oldKey;
        if (properties == null) {
            username = null;
            sourceId = null;
            authenticationType = null;
            key = null;
            oldKey = null;
        } else {
            java.lang.String tmp;
            username = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_USER_NAME_PROPERTY_ID)));
            key = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_KEY_PROPERTY_ID)));
            oldKey = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_OLD_KEY_PROPERTY_ID)));
            tmp = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_AUTHENTICATION_SOURCE_ID_PROPERTY_ID)));
            if (org.apache.commons.lang.StringUtils.isEmpty(tmp)) {
                sourceId = null;
            } else {
                sourceId = java.lang.Long.parseLong(tmp);
            }
            tmp = ((java.lang.String) (properties.get(org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_AUTHENTICATION_TYPE_PROPERTY_ID)));
            if (org.apache.commons.lang.StringUtils.isEmpty(tmp)) {
                authenticationType = null;
            } else {
                authenticationType = org.apache.ambari.server.security.authorization.UserAuthenticationType.valueOf(tmp.trim().toUpperCase());
            }
        }
        return new org.apache.ambari.server.controller.UserAuthenticationSourceRequest(username, sourceId, authenticationType, key, oldKey);
    }

    private void createUserAuthenticationSources(java.util.Set<org.apache.ambari.server.controller.UserAuthenticationSourceRequest> requests) throws org.apache.ambari.server.AmbariException {
        for (org.apache.ambari.server.controller.UserAuthenticationSourceRequest request : requests) {
            java.lang.String username = request.getUsername();
            if (org.apache.commons.lang.StringUtils.isEmpty(username)) {
                throw new org.apache.ambari.server.AmbariException("Username must be supplied.");
            }
            org.apache.ambari.server.security.authorization.UserAuthenticationType authenticationType = request.getAuthenticationType();
            if (authenticationType == null) {
                throw new org.apache.ambari.server.AmbariException("A value authentication type must be supplied.");
            }
            org.apache.ambari.server.orm.entities.UserEntity userEntity = users.getUserEntity(username);
            if (userEntity == null) {
                throw new org.apache.ambari.server.AmbariException("There is no user with the supplied username");
            }
            users.addAuthentication(userEntity, authenticationType, request.getKey());
        }
    }

    private java.util.Set<org.apache.ambari.server.controller.UserAuthenticationSourceResponse> getUserAuthenticationSources(java.util.Set<org.apache.ambari.server.controller.UserAuthenticationSourceRequest> requests) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.util.Set<org.apache.ambari.server.controller.UserAuthenticationSourceResponse> responses = new java.util.HashSet<>();
        for (org.apache.ambari.server.controller.UserAuthenticationSourceRequest request : requests) {
            java.lang.String requestedUsername = request.getUsername();
            java.lang.String authenticatedUsername = org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthenticatedName();
            if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.AMBARI, null, org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_USERS)) {
                if (null == requestedUsername) {
                    requestedUsername = authenticatedUsername;
                } else if (!requestedUsername.equalsIgnoreCase(authenticatedUsername)) {
                    throw new org.apache.ambari.server.security.authorization.AuthorizationException();
                }
            }
            java.util.Collection<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> authenticationEntities = users.getUserAuthenticationEntities(requestedUsername, request.getAuthenticationType());
            if (authenticationEntities != null) {
                for (org.apache.ambari.server.orm.entities.UserAuthenticationEntity authenticationEntity : authenticationEntities) {
                    responses.add(createUserAuthenticationSourceResponse(authenticationEntity));
                }
            }
        }
        return responses;
    }

    private void deleteUserAuthenticationSources(java.util.Set<org.apache.ambari.server.controller.UserAuthenticationSourceRequest> requests) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        for (org.apache.ambari.server.controller.UserAuthenticationSourceRequest r : requests) {
            java.lang.String username = r.getUsername();
            java.lang.Long sourceId = r.getSourceId();
            if ((!org.apache.commons.lang.StringUtils.isEmpty(username)) && (sourceId != null)) {
                users.removeAuthentication(username, sourceId);
            }
        }
    }

    private void updateUserAuthenticationSources(java.util.Set<org.apache.ambari.server.controller.UserAuthenticationSourceRequest> requests) throws org.apache.ambari.server.security.authorization.AuthorizationException, org.apache.ambari.server.AmbariException {
        java.lang.Integer authenticatedUserId = org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthenticatedId();
        for (org.apache.ambari.server.controller.UserAuthenticationSourceRequest request : requests) {
            java.lang.String requestedUsername = request.getUsername();
            org.apache.ambari.server.orm.entities.UserEntity userEntity = users.getUserEntity(requestedUsername);
            if (null == userEntity) {
                continue;
            }
            boolean isSelf = authenticatedUserId.equals(userEntity.getUserId());
            if ((!isSelf) && (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.AMBARI, null, org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_MANAGE_USERS))) {
                throw new org.apache.ambari.server.security.authorization.AuthorizationException("You are not authorized perform this operation");
            }
            org.apache.ambari.server.orm.entities.UserAuthenticationEntity userAuthenticationEntity = null;
            java.lang.Long sourceId = request.getSourceId();
            if (sourceId != null) {
                java.util.List<org.apache.ambari.server.orm.entities.UserAuthenticationEntity> authenticationEntities = userEntity.getAuthenticationEntities();
                for (org.apache.ambari.server.orm.entities.UserAuthenticationEntity authenticationEntity : authenticationEntities) {
                    if (sourceId.equals(authenticationEntity.getUserAuthenticationId())) {
                        userAuthenticationEntity = authenticationEntity;
                        break;
                    }
                }
            }
            if (userAuthenticationEntity == null) {
                throw new org.apache.velocity.exception.ResourceNotFoundException("The requested authentication source was not found.");
            }
            if ((request.getAuthenticationType() != null) && (request.getAuthenticationType() != userAuthenticationEntity.getAuthenticationType())) {
                throw new org.apache.velocity.exception.ResourceNotFoundException("The requested authentication source was not found - mismatch on authentication type");
            }
            users.modifyAuthentication(userAuthenticationEntity, request.getOldKey(), request.getKey(), isSelf);
        }
    }

    private org.apache.ambari.server.controller.UserAuthenticationSourceResponse createUserAuthenticationSourceResponse(org.apache.ambari.server.orm.entities.UserAuthenticationEntity entity) {
        return new org.apache.ambari.server.controller.UserAuthenticationSourceResponse(entity.getUser().getUserName(), entity.getUserAuthenticationId(), entity.getAuthenticationType(), entity.getAuthenticationKey(), new java.util.Date(entity.getCreateTime()), new java.util.Date(entity.getUpdateTime()));
    }

    private org.apache.ambari.server.controller.spi.Resource toResource(org.apache.ambari.server.controller.UserAuthenticationSourceResponse response, java.util.Set<java.lang.String> requestedIds) {
        final org.apache.ambari.server.controller.internal.ResourceImpl resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.UserAuthenticationSource);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_USER_NAME_PROPERTY_ID, response.getUserName(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_AUTHENTICATION_SOURCE_ID_PROPERTY_ID, response.getSourceId(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_AUTHENTICATION_TYPE_PROPERTY_ID, response.getAuthenticationType().name(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_CREATED_PROPERTY_ID, response.getCreateTime(), requestedIds);
        org.apache.ambari.server.controller.internal.BaseProvider.setResourceProperty(resource, org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_UPDATED_PROPERTY_ID, response.getUpdateTime(), requestedIds);
        return resource;
    }
}