package org.apache.ambari.server.security.authorization.internal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
public class InternalAuthenticationToken implements org.springframework.security.core.Authentication {
    private static final long serialVersionUID = 1L;

    private static final java.lang.String INTERNAL_NAME = "internal";

    private static final org.apache.ambari.server.orm.entities.PrivilegeEntity ADMIN_PRIV_ENTITY = new org.apache.ambari.server.orm.entities.PrivilegeEntity();

    static {
        createAdminPrivilegeEntity(ADMIN_PRIV_ENTITY);
    }

    private static final java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> AUTHORITIES = java.util.Collections.singleton(new org.apache.ambari.server.security.authorization.AmbariGrantedAuthority(org.apache.ambari.server.security.authorization.internal.InternalAuthenticationToken.ADMIN_PRIV_ENTITY));

    private static final org.springframework.security.core.userdetails.User INTERNAL_USER = new org.springframework.security.core.userdetails.User(org.apache.ambari.server.security.authorization.internal.InternalAuthenticationToken.INTERNAL_NAME, "empty", org.apache.ambari.server.security.authorization.internal.InternalAuthenticationToken.AUTHORITIES);

    private java.lang.String token;

    private boolean authenticated = false;

    private static void createAdminPrivilegeEntity(org.apache.ambari.server.orm.entities.PrivilegeEntity entity) {
        org.apache.ambari.server.orm.entities.PermissionEntity pe = new org.apache.ambari.server.orm.entities.PermissionEntity();
        pe.setId(org.apache.ambari.server.orm.entities.PermissionEntity.AMBARI_ADMINISTRATOR_PERMISSION);
        pe.setPermissionName(org.apache.ambari.server.orm.entities.PermissionEntity.AMBARI_ADMINISTRATOR_PERMISSION_NAME);
        pe.addAuthorizations(java.util.EnumSet.allOf(org.apache.ambari.server.security.authorization.RoleAuthorization.class));
        entity.setPermission(pe);
        org.apache.ambari.server.orm.entities.ResourceEntity resource = new org.apache.ambari.server.orm.entities.ResourceEntity();
        resource.setId(1L);
        org.apache.ambari.server.orm.entities.ResourceTypeEntity rte = new org.apache.ambari.server.orm.entities.ResourceTypeEntity();
        rte.setId(org.apache.ambari.server.security.authorization.ResourceType.AMBARI.getId());
        rte.setName(org.apache.ambari.server.security.authorization.ResourceType.AMBARI.name());
        resource.setResourceType(rte);
        entity.setResource(resource);
    }

    public InternalAuthenticationToken(java.lang.String tokenString) {
        this.token = tokenString;
    }

    @java.lang.Override
    public java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> getAuthorities() {
        return org.apache.ambari.server.security.authorization.internal.InternalAuthenticationToken.AUTHORITIES;
    }

    @java.lang.Override
    public java.lang.String getCredentials() {
        return token;
    }

    @java.lang.Override
    public java.lang.Object getDetails() {
        return null;
    }

    @java.lang.Override
    public java.lang.Object getPrincipal() {
        return org.apache.ambari.server.security.authorization.internal.InternalAuthenticationToken.INTERNAL_USER;
    }

    @java.lang.Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @java.lang.Override
    public void setAuthenticated(boolean isAuthenticated) throws java.lang.IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @java.lang.Override
    public java.lang.String getName() {
        return org.apache.ambari.server.security.authorization.internal.InternalAuthenticationToken.INTERNAL_NAME;
    }
}