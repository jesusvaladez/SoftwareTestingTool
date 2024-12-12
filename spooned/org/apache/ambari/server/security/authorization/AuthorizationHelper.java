package org.apache.ambari.server.security.authorization;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
@com.google.inject.Singleton
public class AuthorizationHelper {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.security.authorization.AuthorizationHelper.class);

    @com.google.inject.Inject
    static com.google.inject.Provider<org.apache.ambari.server.orm.dao.PrivilegeDAO> privilegeDAOProvider;

    @com.google.inject.Inject
    static com.google.inject.Provider<org.apache.ambari.server.orm.dao.ViewInstanceDAO> viewInstanceDAOProvider;

    public static java.lang.String getProxyUserName(org.springframework.security.core.Authentication authentication) {
        if (authentication == null) {
            return null;
        }
        java.lang.Object userDetails = authentication.getPrincipal();
        if (userDetails instanceof org.apache.ambari.server.security.authentication.AmbariProxiedUserDetailsImpl) {
            org.apache.ambari.server.security.authentication.AmbariProxiedUserDetailsImpl ambariProxiedUserDetails = ((org.apache.ambari.server.security.authentication.AmbariProxiedUserDetailsImpl) (userDetails));
            return ambariProxiedUserDetails.getProxyUserDetails().getUsername();
        }
        return null;
    }

    public static java.lang.String getProxyUserName() {
        org.springframework.security.core.context.SecurityContext securityContext = org.springframework.security.core.context.SecurityContextHolder.getContext();
        org.springframework.security.core.Authentication auth = securityContext.getAuthentication();
        return org.apache.ambari.server.security.authorization.AuthorizationHelper.getProxyUserName(auth);
    }

    public java.util.Collection<org.springframework.security.core.GrantedAuthority> convertPrivilegesToAuthorities(java.util.Collection<org.apache.ambari.server.orm.entities.PrivilegeEntity> privilegeEntities) {
        java.util.Set<org.springframework.security.core.GrantedAuthority> authorities = new java.util.HashSet<>(privilegeEntities.size());
        for (org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity : privilegeEntities) {
            authorities.add(new org.apache.ambari.server.security.authorization.AmbariGrantedAuthority(privilegeEntity));
        }
        return authorities;
    }

    public static java.lang.String getAuthenticatedName() {
        return org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthenticatedName(null);
    }

    public static java.lang.String getAuthenticatedName(java.lang.String defaultUsername) {
        org.springframework.security.core.context.SecurityContext securityContext = org.springframework.security.core.context.SecurityContextHolder.getContext();
        org.springframework.security.core.Authentication auth = securityContext.getAuthentication();
        return null == auth ? defaultUsername : auth.getName();
    }

    public static int getAuthenticatedId() {
        org.springframework.security.core.context.SecurityContext securityContext = org.springframework.security.core.context.SecurityContextHolder.getContext();
        org.springframework.security.core.Authentication authentication = securityContext.getAuthentication();
        java.lang.Object principal = (authentication == null) ? null : authentication.getPrincipal();
        if (principal instanceof org.apache.ambari.server.security.authentication.AmbariUserDetails) {
            return ((org.apache.ambari.server.security.authentication.AmbariUserDetails) (principal)).getUserId();
        }
        return -1;
    }

    public static boolean isAuthorized(org.apache.ambari.server.security.authorization.ResourceType resourceType, java.lang.Long resourceId, org.apache.ambari.server.security.authorization.RoleAuthorization requiredAuthorization) {
        return org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthentication(), resourceType, resourceId, java.util.EnumSet.of(requiredAuthorization));
    }

    public static boolean isAuthorized(org.apache.ambari.server.security.authorization.ResourceType resourceType, java.lang.Long resourceId, java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> requiredAuthorizations) {
        return org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthentication(), resourceType, resourceId, requiredAuthorizations);
    }

    public static boolean isAuthorized(org.springframework.security.core.Authentication authentication, org.apache.ambari.server.security.authorization.ResourceType resourceType, java.lang.Long resourceId, org.apache.ambari.server.security.authorization.RoleAuthorization requiredAuthorization) {
        return org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(authentication, resourceType, resourceId, java.util.EnumSet.of(requiredAuthorization));
    }

    public static boolean isAuthorized(org.springframework.security.core.Authentication authentication, org.apache.ambari.server.security.authorization.ResourceType resourceType, java.lang.Long resourceId, java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> requiredAuthorizations) {
        if ((requiredAuthorizations == null) || requiredAuthorizations.isEmpty()) {
            return true;
        } else if (authentication == null) {
            return false;
        } else {
            for (org.springframework.security.core.GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                org.apache.ambari.server.security.authorization.AmbariGrantedAuthority ambariGrantedAuthority = ((org.apache.ambari.server.security.authorization.AmbariGrantedAuthority) (grantedAuthority));
                org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity = ambariGrantedAuthority.getPrivilegeEntity();
                org.apache.ambari.server.orm.entities.ResourceEntity privilegeResource = privilegeEntity.getResource();
                org.apache.ambari.server.security.authorization.ResourceType privilegeResourceType = org.apache.ambari.server.security.authorization.ResourceType.translate(privilegeResource.getResourceType().getName());
                boolean resourceOK;
                if (org.apache.ambari.server.security.authorization.ResourceType.AMBARI == privilegeResourceType) {
                    resourceOK = true;
                } else if ((resourceType == null) || (resourceType == privilegeResourceType)) {
                    resourceOK = (resourceId == null) || resourceId.equals(privilegeResource.getId());
                } else {
                    resourceOK = false;
                }
                if (resourceOK) {
                    org.apache.ambari.server.orm.entities.PermissionEntity permission = privilegeEntity.getPermission();
                    java.util.Collection<org.apache.ambari.server.orm.entities.RoleAuthorizationEntity> userAuthorizations = (permission == null) ? null : permission.getAuthorizations();
                    if (userAuthorizations != null) {
                        for (org.apache.ambari.server.orm.entities.RoleAuthorizationEntity userAuthorization : userAuthorizations) {
                            try {
                                if (requiredAuthorizations.contains(org.apache.ambari.server.security.authorization.RoleAuthorization.translate(userAuthorization.getAuthorizationId()))) {
                                    return true;
                                }
                            } catch (java.lang.IllegalArgumentException e) {
                                org.apache.ambari.server.security.authorization.AuthorizationHelper.LOG.warn("Invalid authorization name, '{}'... ignoring.", userAuthorization.getAuthorizationId());
                            }
                        }
                    }
                }
            }
            return false;
        }
    }

    public static void verifyAuthorization(org.apache.ambari.server.security.authorization.ResourceType resourceType, java.lang.Long resourceId, java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> requiredAuthorizations) throws org.apache.ambari.server.security.authorization.AuthorizationException {
        if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(resourceType, resourceId, requiredAuthorizations)) {
            throw new org.apache.ambari.server.security.authorization.AuthorizationException();
        }
    }

    public static void verifyAuthorization(org.springframework.security.core.Authentication authentication, org.apache.ambari.server.security.authorization.ResourceType resourceType, java.lang.Long resourceId, java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> requiredAuthorizations) throws org.apache.ambari.server.security.authorization.AuthorizationException {
        if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(authentication, resourceType, resourceId, requiredAuthorizations)) {
            throw new org.apache.ambari.server.security.authorization.AuthorizationException();
        }
    }

    public static org.springframework.security.core.Authentication getAuthentication() {
        org.springframework.security.core.context.SecurityContext context = org.springframework.security.core.context.SecurityContextHolder.getContext();
        return context == null ? null : context.getAuthentication();
    }

    public static void addLoginNameAlias(java.lang.String ambariUserName, java.lang.String loginAlias) {
        org.springframework.web.context.request.ServletRequestAttributes attr = ((org.springframework.web.context.request.ServletRequestAttributes) (org.springframework.web.context.request.RequestContextHolder.getRequestAttributes()));
        if (attr != null) {
            org.apache.ambari.server.security.authorization.AuthorizationHelper.LOG.info("Adding login alias '{}' for user name '{}'", loginAlias, ambariUserName);
            attr.setAttribute(loginAlias, ambariUserName, org.springframework.web.context.request.RequestAttributes.SCOPE_SESSION);
            attr.setAttribute(ambariUserName, loginAlias, org.springframework.web.context.request.RequestAttributes.SCOPE_SESSION);
        }
    }

    public static java.lang.String resolveLoginAliasToUserName(java.lang.String loginAlias) {
        org.springframework.web.context.request.ServletRequestAttributes attr = ((org.springframework.web.context.request.ServletRequestAttributes) (org.springframework.web.context.request.RequestContextHolder.getRequestAttributes()));
        if ((attr != null) && (attr.getAttribute(loginAlias, org.springframework.web.context.request.RequestAttributes.SCOPE_SESSION) != null)) {
            return ((java.lang.String) (attr.getAttribute(loginAlias, org.springframework.web.context.request.RequestAttributes.SCOPE_SESSION)));
        }
        return loginAlias;
    }

    public static java.util.List<java.lang.String> getAuthorizationNames(org.springframework.security.core.Authentication authentication) {
        java.util.List<java.lang.String> authorizationNames = com.google.common.collect.Lists.newArrayList();
        if (authentication.getAuthorities() != null) {
            for (org.springframework.security.core.GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                org.apache.ambari.server.security.authorization.AmbariGrantedAuthority ambariGrantedAuthority = ((org.apache.ambari.server.security.authorization.AmbariGrantedAuthority) (grantedAuthority));
                org.apache.ambari.server.orm.entities.PrivilegeEntity privilegeEntity = ambariGrantedAuthority.getPrivilegeEntity();
                java.util.Collection<org.apache.ambari.server.orm.entities.RoleAuthorizationEntity> roleAuthorizationEntities = privilegeEntity.getPermission().getAuthorizations();
                for (org.apache.ambari.server.orm.entities.RoleAuthorizationEntity entity : roleAuthorizationEntities) {
                    authorizationNames.add(entity.getAuthorizationName());
                }
            }
        }
        return authorizationNames;
    }
}