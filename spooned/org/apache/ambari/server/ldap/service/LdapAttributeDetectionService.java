package org.apache.ambari.server.ldap.service;
public interface LdapAttributeDetectionService {
    org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration detectLdapUserAttributes(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ambariLdapConfiguration) throws org.apache.ambari.server.ldap.service.AmbariLdapException;

    org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration detectLdapGroupAttributes(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ambariLdapConfiguration) throws org.apache.ambari.server.ldap.service.AmbariLdapException;
}