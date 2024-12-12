package org.apache.ambari.server.security.ldap;
public class LdapSyncDto {
    private java.util.Set<org.apache.ambari.server.security.ldap.LdapGroupDto> groups = new java.util.HashSet<>();

    private java.util.Set<org.apache.ambari.server.security.ldap.LdapUserDto> users = new java.util.HashSet<>();

    public java.util.Set<org.apache.ambari.server.security.ldap.LdapGroupDto> getGroups() {
        return groups;
    }

    public void setGroups(java.util.Set<org.apache.ambari.server.security.ldap.LdapGroupDto> groups) {
        this.groups = groups;
    }

    public java.util.Set<org.apache.ambari.server.security.ldap.LdapUserDto> getUsers() {
        return users;
    }

    public void setUsers(java.util.Set<org.apache.ambari.server.security.ldap.LdapUserDto> users) {
        this.users = users;
    }
}