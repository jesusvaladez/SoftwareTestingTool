package org.apache.ambari.server.security.authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
public class AmbariProxiedUserDetailsImpl implements org.apache.ambari.server.security.authentication.AmbariUserDetails {
    private final org.springframework.security.core.userdetails.UserDetails proxiedUserDetails;

    private final org.apache.ambari.server.security.authentication.AmbariProxyUserDetails proxyUserDetails;

    public AmbariProxiedUserDetailsImpl(org.springframework.security.core.userdetails.UserDetails proxiedUserDetails, org.apache.ambari.server.security.authentication.AmbariProxyUserDetails proxyUserDetails) {
        this.proxiedUserDetails = proxiedUserDetails;
        this.proxyUserDetails = proxyUserDetails;
    }

    @java.lang.Override
    public java.lang.Integer getUserId() {
        return proxiedUserDetails instanceof org.apache.ambari.server.security.authentication.AmbariUserDetails ? ((org.apache.ambari.server.security.authentication.AmbariUserDetails) (proxiedUserDetails)).getUserId() : null;
    }

    @java.lang.Override
    public java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> getAuthorities() {
        return proxiedUserDetails == null ? null : proxiedUserDetails.getAuthorities();
    }

    @java.lang.Override
    public java.lang.String getPassword() {
        return proxiedUserDetails == null ? null : proxiedUserDetails.getPassword();
    }

    @java.lang.Override
    public java.lang.String getUsername() {
        return proxiedUserDetails == null ? null : proxiedUserDetails.getUsername();
    }

    @java.lang.Override
    public boolean isAccountNonExpired() {
        return (proxiedUserDetails != null) && proxiedUserDetails.isAccountNonExpired();
    }

    @java.lang.Override
    public boolean isAccountNonLocked() {
        return (proxiedUserDetails != null) && proxiedUserDetails.isAccountNonLocked();
    }

    @java.lang.Override
    public boolean isCredentialsNonExpired() {
        return (proxiedUserDetails != null) && proxiedUserDetails.isCredentialsNonExpired();
    }

    @java.lang.Override
    public boolean isEnabled() {
        return (proxiedUserDetails != null) && proxiedUserDetails.isEnabled();
    }

    public org.apache.ambari.server.security.authentication.AmbariProxyUserDetails getProxyUserDetails() {
        return proxyUserDetails;
    }
}