package org.apache.ambari.server.security.authentication.tproxy;
import org.springframework.security.core.AuthenticationException;
public class TrustedProxyAuthenticationNotAllowedException extends org.springframework.security.core.AuthenticationException {
    public TrustedProxyAuthenticationNotAllowedException(java.lang.String message) {
        super(message);
    }
}