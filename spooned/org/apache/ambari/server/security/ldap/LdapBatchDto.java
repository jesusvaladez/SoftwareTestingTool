package org.apache.ambari.server.security.ldap;
public class LdapBatchDto {
    private final java.util.Set<org.apache.ambari.server.security.ldap.LdapGroupDto> groupsToBecomeLdap = new java.util.HashSet<>();

    private final java.util.Set<org.apache.ambari.server.security.ldap.LdapGroupDto> groupsToBeCreated = new java.util.HashSet<>();

    private final java.util.Set<org.apache.ambari.server.security.ldap.LdapGroupDto> groupsToBeRemoved = new java.util.HashSet<>();

    private final java.util.Set<org.apache.ambari.server.security.ldap.LdapGroupDto> groupsProcessedInternal = new java.util.HashSet<>();

    private final java.util.Set<org.apache.ambari.server.security.ldap.LdapUserDto> usersSkipped = new java.util.HashSet<>();

    private final java.util.Set<org.apache.ambari.server.security.ldap.LdapUserDto> usersIgnored = new java.util.HashSet<>();

    private final java.util.Set<org.apache.ambari.server.security.ldap.LdapUserDto> usersToBecomeLdap = new java.util.HashSet<>();

    private final java.util.Set<org.apache.ambari.server.security.ldap.LdapUserDto> usersToBeCreated = new java.util.HashSet<>();

    private final java.util.Set<org.apache.ambari.server.security.ldap.LdapUserDto> usersToBeRemoved = new java.util.HashSet<>();

    private final java.util.Set<org.apache.ambari.server.security.ldap.LdapUserGroupMemberDto> membershipToAdd = new java.util.HashSet<>();

    private final java.util.Set<org.apache.ambari.server.security.ldap.LdapUserGroupMemberDto> membershipToRemove = new java.util.HashSet<>();

    public java.util.Set<org.apache.ambari.server.security.ldap.LdapUserDto> getUsersSkipped() {
        return usersSkipped;
    }

    public java.util.Set<org.apache.ambari.server.security.ldap.LdapUserDto> getUsersIgnored() {
        return usersIgnored;
    }

    public java.util.Set<org.apache.ambari.server.security.ldap.LdapGroupDto> getGroupsToBecomeLdap() {
        return groupsToBecomeLdap;
    }

    public java.util.Set<org.apache.ambari.server.security.ldap.LdapGroupDto> getGroupsToBeCreated() {
        return groupsToBeCreated;
    }

    public java.util.Set<org.apache.ambari.server.security.ldap.LdapUserDto> getUsersToBecomeLdap() {
        return usersToBecomeLdap;
    }

    public java.util.Set<org.apache.ambari.server.security.ldap.LdapUserDto> getUsersToBeCreated() {
        return usersToBeCreated;
    }

    public java.util.Set<org.apache.ambari.server.security.ldap.LdapUserGroupMemberDto> getMembershipToAdd() {
        return membershipToAdd;
    }

    public java.util.Set<org.apache.ambari.server.security.ldap.LdapUserGroupMemberDto> getMembershipToRemove() {
        return membershipToRemove;
    }

    public java.util.Set<org.apache.ambari.server.security.ldap.LdapGroupDto> getGroupsToBeRemoved() {
        return groupsToBeRemoved;
    }

    public java.util.Set<org.apache.ambari.server.security.ldap.LdapUserDto> getUsersToBeRemoved() {
        return usersToBeRemoved;
    }

    public java.util.Set<org.apache.ambari.server.security.ldap.LdapGroupDto> getGroupsProcessedInternal() {
        return groupsProcessedInternal;
    }
}