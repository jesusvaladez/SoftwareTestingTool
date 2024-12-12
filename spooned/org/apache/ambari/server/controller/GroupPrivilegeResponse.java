package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public class GroupPrivilegeResponse extends org.apache.ambari.server.controller.PrivilegeResponse implements org.apache.ambari.server.controller.ApiModel {
    private java.lang.String groupName;

    public GroupPrivilegeResponse(java.lang.String groupName, java.lang.String permissionLabel, java.lang.String permissionName, java.lang.Integer privilegeId, org.apache.ambari.server.orm.entities.PrincipalTypeEntity.PrincipalType principalType) {
        this.groupName = groupName;
        this.permissionLabel = permissionLabel;
        this.privilegeId = privilegeId;
        this.permissionName = permissionName;
        this.principalType = principalType;
    }

    @io.swagger.annotations.ApiModelProperty(name = "PrivilegeInfo/group_name", required = true)
    public java.lang.String getGroupName() {
        return groupName;
    }
}