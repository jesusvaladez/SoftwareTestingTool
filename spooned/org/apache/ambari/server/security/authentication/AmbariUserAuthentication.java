package org.apache.ambari.server.security.authentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
public class AmbariUserAuthentication implements org.springframework.security.core.Authentication {
    private final java.lang.String serializedToken;

    private final org.apache.ambari.server.security.authentication.AmbariUserDetails userDetails;

    private boolean authenticated;

    public AmbariUserAuthentication(java.lang.String token, org.apache.ambari.server.security.authentication.AmbariUserDetails userDetails) {
        this(token, userDetails, false);
    }

    public AmbariUserAuthentication(java.lang.String token, org.apache.ambari.server.security.authentication.AmbariUserDetails userDetails, boolean authenticated) {
        this.serializedToken = token;
        this.userDetails = userDetails;
        this.authenticated = authenticated;
    }

    @java.lang.Override
    @com.fasterxml.jackson.annotation.JsonIgnore
    public java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> getAuthorities() {
        return userDetails == null ? null : userDetails.getAuthorities();
    }

    @java.lang.Override
    public java.lang.String getCredentials() {
        return serializedToken;
    }

    @java.lang.Override
    public java.lang.Object getDetails() {
        return null;
    }

    @java.lang.Override
    public org.apache.ambari.server.security.authentication.AmbariUserDetails getPrincipal() {
        return userDetails;
    }

    @java.lang.Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @java.lang.Override
    public void setAuthenticated(boolean authenticated) throws java.lang.IllegalArgumentException {
        this.authenticated = authenticated;
    }

    @java.lang.Override
    public java.lang.String getName() {
        return userDetails == null ? null : userDetails.getUsername();
    }

    public java.lang.Integer getUserId() {
        return userDetails == null ? null : userDetails.getUserId();
    }
}