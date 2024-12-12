package org.apache.ambari.server.security.authentication;
import org.springframework.security.core.AuthenticationException;
public class AuthenticationMethodNotAllowedException extends org.springframework.security.core.AuthenticationException {
    private final java.lang.String username;

    private final org.apache.ambari.server.security.authorization.UserAuthenticationType userAuthenticationType;

    public AuthenticationMethodNotAllowedException(java.lang.String username, org.apache.ambari.server.security.authorization.UserAuthenticationType authenticationType) {
        this(username, authenticationType, org.apache.ambari.server.security.authentication.AuthenticationMethodNotAllowedException.createDefaultMessage(username, authenticationType));
    }

    public AuthenticationMethodNotAllowedException(java.lang.String username, org.apache.ambari.server.security.authorization.UserAuthenticationType authenticationType, java.lang.Throwable cause) {
        this(username, authenticationType, org.apache.ambari.server.security.authentication.AuthenticationMethodNotAllowedException.createDefaultMessage(username, authenticationType), cause);
    }

    public AuthenticationMethodNotAllowedException(java.lang.String username, org.apache.ambari.server.security.authorization.UserAuthenticationType authenticationType, java.lang.String message) {
        super(message);
        this.username = username;
        this.userAuthenticationType = authenticationType;
    }

    public AuthenticationMethodNotAllowedException(java.lang.String username, org.apache.ambari.server.security.authorization.UserAuthenticationType authenticationType, java.lang.String message, java.lang.Throwable cause) {
        super(message, cause);
        this.username = username;
        this.userAuthenticationType = authenticationType;
    }

    public java.lang.String getUsername() {
        return username;
    }

    public org.apache.ambari.server.security.authorization.UserAuthenticationType getUserAuthenticationType() {
        return userAuthenticationType;
    }

    private static java.lang.String createDefaultMessage(java.lang.String username, org.apache.ambari.server.security.authorization.UserAuthenticationType authenticationType) {
        return java.lang.String.format("%s is not authorized to authenticate using %s", username, authenticationType == null ? "null" : authenticationType.name());
    }
}