package org.apache.ambari.server.security.authentication;
import org.springframework.security.core.GrantedAuthority;
public class AmbariUserDetailsImpl implements org.apache.ambari.server.security.authentication.AmbariUserDetails {
    private final org.apache.ambari.server.security.authorization.User user;

    private final java.lang.String password;

    private final java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> grantedAuthorities;

    public AmbariUserDetailsImpl(org.apache.ambari.server.security.authorization.User user, java.lang.String password, java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> grantedAuthorities) {
        this.user = user;
        this.password = password;
        this.grantedAuthorities = (grantedAuthorities == null) ? java.util.Collections.emptyList() : java.util.Collections.unmodifiableCollection(new java.util.ArrayList<>(grantedAuthorities));
    }

    @java.lang.Override
    public java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @java.lang.Override
    public java.lang.String getPassword() {
        return password;
    }

    @java.lang.Override
    public java.lang.String getUsername() {
        return user == null ? null : user.getUserName();
    }

    @java.lang.Override
    public java.lang.Integer getUserId() {
        return user == null ? null : user.getUserId();
    }

    @java.lang.Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @java.lang.Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @java.lang.Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @java.lang.Override
    public boolean isEnabled() {
        return (user != null) && user.isActive();
    }
}