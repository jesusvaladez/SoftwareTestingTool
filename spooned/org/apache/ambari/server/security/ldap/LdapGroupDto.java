package org.apache.ambari.server.security.ldap;
public class LdapGroupDto {
    private java.lang.String groupName;

    private java.util.Set<java.lang.String> memberAttributes = new java.util.HashSet<>();

    private boolean synced;

    public java.lang.String getGroupName() {
        return groupName;
    }

    public void setGroupName(java.lang.String groupName) {
        this.groupName = groupName;
    }

    public java.util.Set<java.lang.String> getMemberAttributes() {
        return memberAttributes;
    }

    public void setMemberAttributes(java.util.Set<java.lang.String> memberAttributes) {
        this.memberAttributes = memberAttributes;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (groupName != null) ? groupName.hashCode() : 0;
        return result;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.security.ldap.LdapGroupDto that = ((org.apache.ambari.server.security.ldap.LdapGroupDto) (o));
        if (groupName != null ? !groupName.equals(that.getGroupName()) : that.getGroupName() != null)
            return false;

        return true;
    }
}