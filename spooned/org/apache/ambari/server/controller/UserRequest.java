package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@org.apache.ambari.server.controller.ApiModel
public class UserRequest {
    private java.lang.String userName;

    private java.lang.String password;

    private java.lang.String oldPassword;

    private java.lang.Boolean active;

    private java.lang.Boolean admin;

    private java.lang.String displayName;

    private java.lang.String localUserName;

    private java.lang.Integer consecutiveFailures;

    public UserRequest(java.lang.String name) {
        this.userName = name;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserResourceProvider.USERNAME_PROPERTY_ID)
    public java.lang.String getUsername() {
        return userName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserResourceProvider.PASSWORD_PROPERTY_ID)
    public java.lang.String getPassword() {
        return password;
    }

    public void setPassword(java.lang.String userPass) {
        password = userPass;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserResourceProvider.OLD_PASSWORD_PROPERTY_ID)
    public java.lang.String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(java.lang.String oldUserPass) {
        oldPassword = oldUserPass;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserResourceProvider.ACTIVE_PROPERTY_ID)
    public java.lang.Boolean isActive() {
        return active;
    }

    public void setActive(java.lang.Boolean active) {
        this.active = active;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserResourceProvider.ADMIN_PROPERTY_ID)
    public java.lang.Boolean isAdmin() {
        return admin;
    }

    public void setAdmin(java.lang.Boolean admin) {
        this.admin = admin;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserResourceProvider.DISPLAY_NAME_PROPERTY_ID)
    public java.lang.String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(java.lang.String displayName) {
        this.displayName = displayName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserResourceProvider.LOCAL_USERNAME_PROPERTY_ID)
    public java.lang.String getLocalUserName() {
        return localUserName;
    }

    public void setLocalUserName(java.lang.String localUserName) {
        this.localUserName = localUserName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserResourceProvider.CONSECUTIVE_FAILURES_PROPERTY_ID)
    public java.lang.Integer getConsecutiveFailures() {
        return consecutiveFailures;
    }

    public void setConsecutiveFailures(java.lang.Integer consecutiveFailures) {
        this.consecutiveFailures = consecutiveFailures;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("User, username=").append(userName);
        return sb.toString();
    }
}