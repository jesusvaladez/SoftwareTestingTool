package org.apache.ambari.server.security.authentication;
public class AccountDisabledException extends org.apache.ambari.server.security.authentication.AmbariAuthenticationException {
    public AccountDisabledException(java.lang.String username) {
        super(username, "The account is disabled", false);
    }
}