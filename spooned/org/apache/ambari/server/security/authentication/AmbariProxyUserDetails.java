package org.apache.ambari.server.security.authentication;
public interface AmbariProxyUserDetails {
    java.lang.String getUsername();

    org.apache.ambari.server.security.authorization.UserAuthenticationType getAuthenticationType();
}