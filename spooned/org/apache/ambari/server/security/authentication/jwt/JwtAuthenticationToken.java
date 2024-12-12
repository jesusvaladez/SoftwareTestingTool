package org.apache.ambari.server.security.authentication.jwt;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
public class JwtAuthenticationToken extends org.springframework.security.authentication.AbstractAuthenticationToken {
    private final java.lang.String username;

    private final java.lang.String token;

    public JwtAuthenticationToken(java.lang.String username, java.lang.String token, java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> grantedAuthorities) {
        super(grantedAuthorities);
        this.username = username;
        this.token = token;
    }

    @java.lang.Override
    public java.lang.Object getCredentials() {
        return token;
    }

    @java.lang.Override
    public java.lang.Object getPrincipal() {
        return username;
    }
}