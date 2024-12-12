package org.apache.ambari.server.ldap.service;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;
public interface LdapConnectionConfigService {
    org.apache.directory.ldap.client.api.LdapConnectionConfig createLdapConnectionConfig(org.apache.ambari.server.ldap.domain.AmbariLdapConfiguration ambariLdapConfiguration) throws org.apache.ambari.server.ldap.service.AmbariLdapException;
}