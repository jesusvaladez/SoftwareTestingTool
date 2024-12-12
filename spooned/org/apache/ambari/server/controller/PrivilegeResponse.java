package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public abstract class PrivilegeResponse implements org.apache.ambari.server.controller.ApiModel {
    protected java.lang.String permissionLabel;

    protected java.lang.Integer privilegeId;

    protected java.lang.String permissionName;

    protected org.apache.ambari.server.orm.entities.PrincipalTypeEntity.PrincipalType principalType;

    protected java.lang.String principalName;

    protected org.apache.ambari.server.security.authorization.ResourceType type;

    protected java.lang.String clusterName;

    protected java.lang.String viewName;

    protected java.lang.String version;

    protected java.lang.String instanceName;

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PERMISSION_LABEL_PROPERTY_ID)
    public java.lang.String getPermissionLabel() {
        return permissionLabel;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRINCIPAL_NAME_PROPERTY_ID)
    public java.lang.String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(java.lang.String principalName) {
        this.principalName = principalName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRIVILEGE_ID_PROPERTY_ID)
    public java.lang.Integer getPrivilegeId() {
        return privilegeId;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PERMISSION_NAME_PROPERTY_ID)
    public java.lang.String getPermissionName() {
        return permissionName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRINCIPAL_TYPE_PROPERTY_ID)
    public org.apache.ambari.server.orm.entities.PrincipalTypeEntity.PrincipalType getPrincipalType() {
        return principalType;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.TYPE_PROPERTY_ID)
    public org.apache.ambari.server.security.authorization.ResourceType getType() {
        return type;
    }

    public void setType(org.apache.ambari.server.security.authorization.ResourceType type) {
        this.type = type;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_NAME)
    public java.lang.String getClusterName() {
        return clusterName;
    }

    public void setClusterName(java.lang.String clusterName) {
        this.clusterName = clusterName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewResourceProvider.VIEW_NAME_PROPERTY_ID)
    public java.lang.String getViewName() {
        return viewName;
    }

    public void setViewName(java.lang.String viewName) {
        this.viewName = viewName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.VERSION_PROPERTY_ID)
    public java.lang.String getVersion() {
        return version;
    }

    public void setVersion(java.lang.String version) {
        this.version = version;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewPrivilegeResourceProvider.INSTANCE_NAME_PROPERTY_ID)
    public java.lang.String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(java.lang.String instanceName) {
        this.instanceName = instanceName;
    }
}