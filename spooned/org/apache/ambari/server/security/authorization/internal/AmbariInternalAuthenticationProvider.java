package org.apache.ambari.server.security.authorization.internal;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
public class AmbariInternalAuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {
    private final org.apache.ambari.server.security.authorization.internal.InternalTokenStorage internalTokenStorage;

    @com.google.inject.Inject
    public AmbariInternalAuthenticationProvider(org.apache.ambari.server.security.authorization.internal.InternalTokenStorage internalTokenStorage) {
        this.internalTokenStorage = internalTokenStorage;
    }

    @java.lang.Override
    public org.springframework.security.core.Authentication authenticate(org.springframework.security.core.Authentication authentication) throws org.springframework.security.core.AuthenticationException {
        org.apache.ambari.server.security.authorization.internal.InternalAuthenticationToken token = ((org.apache.ambari.server.security.authorization.internal.InternalAuthenticationToken) (authentication));
        if (internalTokenStorage.isValidInternalToken(token.getCredentials())) {
            token.setAuthenticated(true);
        } else {
            throw new org.apache.ambari.server.security.authentication.InvalidUsernamePasswordCombinationException(null);
        }
        return token;
    }

    @java.lang.Override
    public boolean supports(java.lang.Class<?> authentication) {
        return org.apache.ambari.server.security.authorization.internal.InternalAuthenticationToken.class.isAssignableFrom(authentication);
    }
}