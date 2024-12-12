package org.apache.ambari.server.security.authorization;
public class AuthenticationMethod {
    private final org.apache.ambari.server.security.authorization.UserAuthenticationType authenticationType;

    private final java.lang.String authenticationKey;

    public AuthenticationMethod(org.apache.ambari.server.security.authorization.UserAuthenticationType authenticationType, java.lang.String authenticationKey) {
        this.authenticationType = authenticationType;
        this.authenticationKey = authenticationKey;
    }

    public org.apache.ambari.server.security.authorization.UserAuthenticationType getAuthenticationType() {
        return authenticationType;
    }

    public java.lang.String getAuthenticationKey() {
        return authenticationKey;
    }
}