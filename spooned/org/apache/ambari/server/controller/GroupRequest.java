package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public class GroupRequest implements org.apache.ambari.server.controller.ApiModel {
    private final java.lang.String groupName;

    public GroupRequest(java.lang.String groupName) {
        this.groupName = groupName;
    }

    @io.swagger.annotations.ApiModelProperty(name = "Groups/group_name", required = true)
    public java.lang.String getGroupName() {
        return groupName;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ("GroupRequest [groupName=" + groupName) + "]";
    }
}