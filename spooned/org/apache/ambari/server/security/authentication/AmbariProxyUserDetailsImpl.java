package org.apache.ambari.server.security.authentication;
public class AmbariProxyUserDetailsImpl implements org.apache.ambari.server.security.authentication.AmbariProxyUserDetails {
    private final java.lang.String username;

    private final org.apache.ambari.server.security.authorization.UserAuthenticationType authenticationType;

    public AmbariProxyUserDetailsImpl(java.lang.String username, org.apache.ambari.server.security.authorization.UserAuthenticationType authenticationType) {
        this.username = username;
        this.authenticationType = authenticationType;
    }

    @java.lang.Override
    public java.lang.String getUsername() {
        return username;
    }

    @java.lang.Override
    public org.apache.ambari.server.security.authorization.UserAuthenticationType getAuthenticationType() {
        return authenticationType;
    }
}