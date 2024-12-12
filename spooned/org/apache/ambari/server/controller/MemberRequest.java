package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public class MemberRequest {
    private final java.lang.String groupName;

    private final java.lang.String userName;

    public MemberRequest(java.lang.String groupName, java.lang.String userName) {
        this.groupName = groupName;
        this.userName = userName;
    }

    @io.swagger.annotations.ApiModelProperty(name = "MemberInfo/group_name", required = true)
    public java.lang.String getGroupName() {
        return groupName;
    }

    @io.swagger.annotations.ApiModelProperty(name = "MemberInfo/user_name", required = true)
    public java.lang.String getUserName() {
        return userName;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((("MemberRequest [groupName=" + groupName) + ", userName=") + userName) + "]";
    }
}