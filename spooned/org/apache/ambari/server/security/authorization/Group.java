package org.apache.ambari.server.security.authorization;
public class Group {
    private final int groupId;

    private final java.lang.String groupName;

    private final boolean ldapGroup;

    private final org.apache.ambari.server.security.authorization.GroupType groupType;

    Group(org.apache.ambari.server.orm.entities.GroupEntity groupEntity) {
        this.groupId = groupEntity.getGroupId();
        this.groupName = groupEntity.getGroupName();
        this.ldapGroup = groupEntity.getLdapGroup();
        this.groupType = groupEntity.getGroupType();
    }

    public int getGroupId() {
        return groupId;
    }

    public java.lang.String getGroupName() {
        return groupName;
    }

    public boolean isLdapGroup() {
        return ldapGroup;
    }

    public org.apache.ambari.server.security.authorization.GroupType getGroupType() {
        return groupType;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((("Group [groupId=" + groupId) + ", groupName=") + groupName) + ", ldapGroup=") + ldapGroup) + "]";
    }
}