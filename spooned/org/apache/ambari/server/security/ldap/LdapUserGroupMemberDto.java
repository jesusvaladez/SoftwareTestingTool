package org.apache.ambari.server.security.ldap;
public class LdapUserGroupMemberDto {
    private final java.lang.String groupName;

    private final java.lang.String userName;

    public LdapUserGroupMemberDto(java.lang.String groupName, java.lang.String userName) {
        this.groupName = groupName;
        this.userName = userName;
    }

    public java.lang.String getGroupName() {
        return groupName;
    }

    public java.lang.String getUserName() {
        return userName;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.security.ldap.LdapUserGroupMemberDto that = ((org.apache.ambari.server.security.ldap.LdapUserGroupMemberDto) (o));
        if (userName != null ? !userName.equals(that.userName) : that.userName != null)
            return false;

        if (groupName != null ? !groupName.equals(that.groupName) : that.groupName != null)
            return false;

        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (userName != null) ? userName.hashCode() : 0;
        result = (31 * result) + (groupName != null ? groupName.hashCode() : 0);
        return result;
    }
}