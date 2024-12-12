package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public class MemberResponse implements org.apache.ambari.server.controller.ApiModel {
    private final java.lang.String groupName;

    private final java.lang.String userName;

    public MemberResponse(java.lang.String groupName, java.lang.String userName) {
        this.groupName = groupName;
        this.userName = userName;
    }

    @io.swagger.annotations.ApiModelProperty(name = "MemberInfo/group_name")
    public java.lang.String getGroupName() {
        return groupName;
    }

    @io.swagger.annotations.ApiModelProperty(name = "MemberInfo/user_name")
    public java.lang.String getUserName() {
        return userName;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.controller.MemberResponse that = ((org.apache.ambari.server.controller.MemberResponse) (o));
        if (groupName != null ? !groupName.equals(that.groupName) : that.groupName != null) {
            return false;
        }
        if (userName != null ? !userName.equals(that.userName) : that.userName != null) {
            return false;
        }
        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (groupName != null) ? groupName.hashCode() : 0;
        result = (31 * result) + (userName != null ? userName.hashCode() : 0);
        return result;
    }
}