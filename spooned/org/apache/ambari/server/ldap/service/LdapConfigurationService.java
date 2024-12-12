package org.apache.ambari.server.ldap.service;
public interface LdapConfigurationService {
    void checkConnection(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration) throws org.apache.ambari.server.ldap.service.AmbariLdapException;

    java.lang.String checkUserAttributes(java.lang.String testUserName, java.lang.String testPassword, org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration configuration) throws org.apache.ambari.server.ldap.service.AmbariLdapException;

    java.util.Set<java.lang.String> checkGroupAttributes(java.lang.String userDn, org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ambariLdapConfiguration) throws org.apache.ambari.server.ldap.service.AmbariLdapException;
}