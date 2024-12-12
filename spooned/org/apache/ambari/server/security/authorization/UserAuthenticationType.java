package org.apache.ambari.server.security.authorization;
public enum UserAuthenticationType {

    LOCAL,
    LDAP,
    JWT,
    PAM,
    KERBEROS;}