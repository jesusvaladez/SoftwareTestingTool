package org.apache.ambari.server.security;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
public class SecurityHelperImpl implements org.apache.ambari.server.security.SecurityHelper {
    private static final org.apache.ambari.server.security.SecurityHelper singleton = new org.apache.ambari.server.security.SecurityHelperImpl();

    private SecurityHelperImpl() {
    }

    public static org.apache.ambari.server.security.SecurityHelper getInstance() {
        return org.apache.ambari.server.security.SecurityHelperImpl.singleton;
    }

    @java.lang.Override
    public java.lang.String getCurrentUserName() {
        org.springframework.security.core.context.SecurityContext ctx = org.springframework.security.core.context.SecurityContextHolder.getContext();
        org.springframework.security.core.Authentication authentication = (ctx == null) ? null : ctx.getAuthentication();
        java.lang.Object principal = (authentication == null) ? null : authentication.getPrincipal();
        java.lang.String username;
        if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
            username = ((org.springframework.security.core.userdetails.UserDetails) (principal)).getUsername();
        } else if (principal instanceof org.apache.ambari.server.security.authorization.User) {
            username = ((org.apache.ambari.server.security.authorization.User) (principal)).getUserName();
        } else {
            username = (principal == null) ? "" : principal.toString();
        }
        return username;
    }

    @java.lang.Override
    public java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> getCurrentAuthorities() {
        org.springframework.security.core.context.SecurityContext context = org.springframework.security.core.context.SecurityContextHolder.getContext();
        org.springframework.security.core.Authentication authentication = context.getAuthentication();
        if ((context.getAuthentication() != null) && context.getAuthentication().isAuthenticated()) {
            return authentication.getAuthorities();
        }
        return java.util.Collections.emptyList();
    }
}