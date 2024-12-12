package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public class UserPrivilegeResponse extends org.apache.ambari.server.controller.PrivilegeResponse implements org.apache.ambari.server.controller.ApiModel {
    private final java.lang.String userName;

    public UserPrivilegeResponse(java.lang.String userName, java.lang.String permissionLabel, java.lang.String permissionName, java.lang.Integer privilegeId, org.apache.ambari.server.orm.entities.PrincipalTypeEntity.PrincipalType principalType) {
        this.userName = userName;
        this.permissionLabel = permissionLabel;
        this.privilegeId = privilegeId;
        this.permissionName = permissionName;
        this.principalType = principalType;
    }

    @io.swagger.annotations.ApiModelProperty(name = "PrivilegeInfo/user_name", required = true)
    public java.lang.String getUserName() {
        return userName;
    }
}