package org.apache.ambari.server.security.authorization;
import org.springframework.security.core.AuthenticationException;
public class DuplicateLdapUserFoundAuthenticationException extends org.springframework.security.core.AuthenticationException {
    public DuplicateLdapUserFoundAuthenticationException(java.lang.String msg) {
        super(msg);
    }

    public DuplicateLdapUserFoundAuthenticationException(java.lang.String msg, java.lang.Throwable t) {
        super(msg, t);
    }
}