package org.apache.ambari.server.controller.internal;
import org.springframework.security.core.Authentication;
public abstract class AbstractAuthorizedResourceProvider extends org.apache.ambari.server.controller.internal.AbstractResourceProvider {
    private java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> requiredCreateAuthorizations = java.util.Collections.emptySet();

    private java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> requiredGetAuthorizations = java.util.Collections.emptySet();

    private java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> requiredUpdateAuthorizations = java.util.Collections.emptySet();

    private java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> requiredDeleteAuthorizations = java.util.Collections.emptySet();

    AbstractAuthorizedResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type type, java.util.Set<java.lang.String> propertyIds, java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds) {
        super(propertyIds, keyPropertyIds);
        org.apache.ambari.server.controller.utilities.PropertyHelper.setPropertyIds(type, propertyIds);
        org.apache.ambari.server.controller.utilities.PropertyHelper.setKeyPropertyIds(type, keyPropertyIds);
    }

    public java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> getRequiredCreateAuthorizations() {
        return requiredCreateAuthorizations;
    }

    public void setRequiredCreateAuthorizations(java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> requiredCreateAuthorizations) {
        this.requiredCreateAuthorizations = createUnmodifiableSet(requiredCreateAuthorizations);
    }

    public java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> getRequiredGetAuthorizations() {
        return requiredGetAuthorizations;
    }

    public void setRequiredGetAuthorizations(java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> requiredGetAuthorizations) {
        this.requiredGetAuthorizations = createUnmodifiableSet(requiredGetAuthorizations);
    }

    public java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> getRequiredUpdateAuthorizations() {
        return requiredUpdateAuthorizations;
    }

    public void setRequiredUpdateAuthorizations(java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> requiredUpdateAuthorizations) {
        this.requiredUpdateAuthorizations = createUnmodifiableSet(requiredUpdateAuthorizations);
    }

    public java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> getRequiredDeleteAuthorizations() {
        return requiredDeleteAuthorizations;
    }

    public void setRequiredDeleteAuthorizations(java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> requiredDeleteAuthorizations) {
        this.requiredDeleteAuthorizations = createUnmodifiableSet(requiredDeleteAuthorizations);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus createResources(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        org.springframework.security.core.Authentication authentication = org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthentication();
        if ((authentication == null) || (!authentication.isAuthenticated())) {
            throw new org.apache.ambari.server.security.authorization.AuthorizationException("Authentication data is not available, authorization to perform the requested operation is not granted");
        } else if (!isAuthorizedToCreateResources(authentication, request)) {
            throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user does not have the appropriate authorizations to create the requested resource(s)");
        }
        return createResourcesAuthorized(request);
    }

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        org.springframework.security.core.Authentication authentication = org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthentication();
        if ((authentication == null) || (!authentication.isAuthenticated())) {
            throw new org.apache.ambari.server.security.authorization.AuthorizationException("Authentication data is not available, authorization to perform the requested operation is not granted");
        } else if (!isAuthorizedToGetResources(authentication, request, predicate)) {
            throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user does not have the appropriate authorizations to get the requested resource(s)");
        }
        return getResourcesAuthorized(request, predicate);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus updateResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        org.springframework.security.core.Authentication authentication = org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthentication();
        if ((authentication == null) || (!authentication.isAuthenticated())) {
            throw new org.apache.ambari.server.security.authorization.AuthorizationException("Authentication data is not available, authorization to perform the requested operation is not granted");
        } else if (!isAuthorizedToUpdateResources(authentication, request, predicate)) {
            throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user does not have the appropriate authorizations to update the requested resource(s)");
        }
        return updateResourcesAuthorized(request, predicate);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.RequestStatus deleteResources(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        org.springframework.security.core.Authentication authentication = org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthentication();
        if ((authentication == null) || (!authentication.isAuthenticated())) {
            throw new org.apache.ambari.server.security.authorization.AuthorizationException("Authentication data is not available, authorization to perform the requested operation is not granted");
        } else if (!isAuthorizedToDeleteResources(authentication, predicate)) {
            throw new org.apache.ambari.server.security.authorization.AuthorizationException("The authenticated user does not have the appropriate authorizations to delete the requested resource(s)");
        }
        return deleteResourcesAuthorized(request, predicate);
    }

    protected org.apache.ambari.server.controller.spi.RequestStatus createResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("If createResources is not overwritten, then createResourcesAuthorized must be overwritten");
    }

    protected boolean isAuthorizedToCreateResources(org.springframework.security.core.Authentication authentication, org.apache.ambari.server.controller.spi.Request request) throws org.apache.ambari.server.controller.spi.SystemException {
        return org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(authentication, getResourceType(request, null), getResourceId(request, null), requiredCreateAuthorizations);
    }

    protected java.util.Set<org.apache.ambari.server.controller.spi.Resource> getResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("If getResources is not overwritten, then getResourcesAuthorized must be overwritten");
    }

    protected boolean isAuthorizedToGetResources(org.springframework.security.core.Authentication authentication, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException {
        return org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(authentication, getResourceType(request, predicate), getResourceId(request, predicate), requiredGetAuthorizations);
    }

    protected org.apache.ambari.server.controller.spi.RequestStatus updateResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("If updateResources is not overwritten, then updateResourcesAuthorized must be overwritten");
    }

    protected boolean isAuthorizedToUpdateResources(org.springframework.security.core.Authentication authentication, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException {
        return org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(authentication, getResourceType(request, predicate), getResourceId(request, predicate), requiredUpdateAuthorizations);
    }

    protected org.apache.ambari.server.controller.spi.RequestStatus deleteResourcesAuthorized(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException, org.apache.ambari.server.controller.spi.UnsupportedPropertyException, org.apache.ambari.server.controller.spi.NoSuchResourceException, org.apache.ambari.server.controller.spi.NoSuchParentResourceException {
        throw new java.lang.UnsupportedOperationException("If deleteResources is not overwritten, then deleteResourcesAuthorized must be overwritten");
    }

    protected boolean isAuthorizedToDeleteResources(org.springframework.security.core.Authentication authentication, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException {
        return org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(authentication, getResourceType(null, predicate), getResourceId(null, predicate), requiredDeleteAuthorizations);
    }

    protected org.apache.ambari.server.security.authorization.ResourceType getResourceType(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) {
        return org.apache.ambari.server.security.authorization.ResourceType.CLUSTER;
    }

    protected java.lang.Long getResourceId(org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) {
        return null;
    }

    private java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> createUnmodifiableSet(java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> set) {
        return set == null ? java.util.Collections.emptySet() : java.util.Collections.unmodifiableSet(new java.util.HashSet<>(set));
    }
}