package org.apache.ambari.server.security.authentication;
public class TooManyLoginFailuresException extends org.apache.ambari.server.security.authentication.AmbariAuthenticationException {
    public TooManyLoginFailuresException(java.lang.String username) {
        super(username, "Too many authentication failures", false);
    }
}