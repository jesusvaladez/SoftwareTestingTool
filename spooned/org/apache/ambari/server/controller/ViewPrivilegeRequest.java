package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
public class ViewPrivilegeRequest extends org.apache.ambari.server.controller.ViewPrivilegeResponse implements org.apache.ambari.server.controller.ApiModel {
    @java.lang.Override
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRIVILEGE_ID_PROPERTY_ID, hidden = true)
    public java.lang.Integer getPrivilegeId() {
        return privilegeId;
    }

    @java.lang.Override
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PERMISSION_LABEL_PROPERTY_ID, hidden = true)
    public java.lang.String getPermissionLabel() {
        return permissionLabel;
    }

    @java.lang.Override
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.VIEW_NAME_PROPERTY_ID, hidden = true)
    public java.lang.String getViewName() {
        return viewName;
    }

    @java.lang.Override
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.VERSION_PROPERTY_ID, hidden = true)
    public java.lang.String getVersion() {
        return version;
    }

    @java.lang.Override
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.INSTANCE_NAME_PROPERTY_ID, hidden = true)
    public java.lang.String getInstanceName() {
        return instanceName;
    }
}