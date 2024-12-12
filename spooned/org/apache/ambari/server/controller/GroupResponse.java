package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public class GroupResponse implements org.apache.ambari.server.controller.ApiModel {
    private final java.lang.String groupName;

    private final boolean ldapGroup;

    private final org.apache.ambari.server.security.authorization.GroupType groupType;

    public GroupResponse(java.lang.String groupName, boolean ldapGroup, org.apache.ambari.server.security.authorization.GroupType groupType) {
        this.groupName = groupName;
        this.ldapGroup = ldapGroup;
        this.groupType = groupType;
    }

    public GroupResponse(java.lang.String groupName, boolean ldapGroup) {
        this.groupName = groupName;
        this.ldapGroup = ldapGroup;
        this.groupType = org.apache.ambari.server.security.authorization.GroupType.LOCAL;
    }

    @io.swagger.annotations.ApiModelProperty(name = "Groups/group_name")
    public java.lang.String getGroupName() {
        return groupName;
    }

    @io.swagger.annotations.ApiModelProperty(name = "Groups/ldap_group")
    public boolean isLdapGroup() {
        return ldapGroup;
    }

    @io.swagger.annotations.ApiModelProperty(name = "Groups/group_type")
    public org.apache.ambari.server.security.authorization.GroupType getGroupType() {
        return groupType;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.controller.GroupResponse that = ((org.apache.ambari.server.controller.GroupResponse) (o));
        if (groupName != null ? !groupName.equals(that.groupName) : that.groupName != null) {
            return false;
        }
        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (groupName != null) ? groupName.hashCode() : 0;
        return result;
    }
}