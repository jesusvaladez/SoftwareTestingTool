package org.apache.ambari.server.security.authentication;
import org.springframework.security.core.AuthenticationException;
public class AmbariAuthenticationException extends org.springframework.security.core.AuthenticationException {
    private final java.lang.String username;

    private final boolean credentialFailure;

    public AmbariAuthenticationException(java.lang.String username, java.lang.String message, boolean credentialFailure) {
        super(message);
        this.username = username;
        this.credentialFailure = credentialFailure;
    }

    public AmbariAuthenticationException(java.lang.String username, java.lang.String message, boolean credentialFailure, java.lang.Throwable throwable) {
        super(message, throwable);
        this.username = username;
        this.credentialFailure = credentialFailure;
    }

    public java.lang.String getUsername() {
        return username;
    }

    public boolean isCredentialFailure() {
        return credentialFailure;
    }
}