package org.apache.ambari.server.ldap.service;
public interface LdapFacade {
    void checkConnection(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ambariLdapConfiguration) throws org.apache.ambari.server.ldap.service.AmbariLdapException;

    org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration detectAttributes(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ambariLdapConfiguration) throws org.apache.ambari.server.ldap.service.AmbariLdapException;

    java.util.Set<java.lang.String> checkLdapAttributes(java.util.Map<java.lang.String, java.lang.Object> parameters, org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ambariLdapConfiguration) throws org.apache.ambari.server.ldap.service.AmbariLdapException;
}