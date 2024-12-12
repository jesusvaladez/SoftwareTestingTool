package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public class UserResponse implements org.apache.ambari.server.controller.ApiModel {
    private final java.lang.String userName;

    private final java.lang.String displayName;

    private final java.lang.String localUserName;

    private final org.apache.ambari.server.security.authorization.UserAuthenticationType authenticationType;

    private final boolean isLdapUser;

    private final boolean isActive;

    private final boolean isAdmin;

    private java.util.Set<java.lang.String> groups = java.util.Collections.emptySet();

    private final java.util.Date createTime;

    private final java.lang.Integer consecutiveFailures;

    public UserResponse(java.lang.String userName, java.lang.String displayName, java.lang.String localUserName, org.apache.ambari.server.security.authorization.UserAuthenticationType userType, boolean isLdapUser, boolean isActive, boolean isAdmin, java.lang.Integer consecutiveFailures, java.util.Date createTime) {
        this.userName = userName;
        this.displayName = displayName;
        this.localUserName = localUserName;
        this.authenticationType = userType;
        this.isLdapUser = isLdapUser;
        this.isActive = isActive;
        this.isAdmin = isAdmin;
        this.consecutiveFailures = consecutiveFailures;
        this.createTime = createTime;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserResourceProvider.USERNAME_PROPERTY_ID)
    public java.lang.String getUsername() {
        return userName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserResourceProvider.DISPLAY_NAME_PROPERTY_ID)
    public java.lang.String getDisplayName() {
        return displayName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserResourceProvider.LOCAL_USERNAME_PROPERTY_ID)
    public java.lang.String getLocalUsername() {
        return localUserName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserResourceProvider.GROUPS_PROPERTY_ID)
    public java.util.Set<java.lang.String> getGroups() {
        return groups;
    }

    public void setGroups(java.util.Set<java.lang.String> groups) {
        this.groups = groups;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserResourceProvider.LDAP_USER_PROPERTY_ID)
    public boolean isLdapUser() {
        return isLdapUser;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserResourceProvider.ACTIVE_PROPERTY_ID)
    public boolean isActive() {
        return isActive;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserResourceProvider.ADMIN_PROPERTY_ID)
    public boolean isAdmin() {
        return isAdmin;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserResourceProvider.USER_TYPE_PROPERTY_ID)
    public org.apache.ambari.server.security.authorization.UserAuthenticationType getAuthenticationType() {
        return authenticationType;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserResourceProvider.CONSECUTIVE_FAILURES_PROPERTY_ID)
    public java.lang.Integer getConsecutiveFailures() {
        return consecutiveFailures;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserResourceProvider.CREATE_TIME_PROPERTY_ID)
    public java.util.Date getCreateTime() {
        return createTime;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        org.apache.ambari.server.controller.UserResponse that = ((org.apache.ambari.server.controller.UserResponse) (o));
        if (userName != null ? !userName.equals(that.userName) : that.userName != null) {
            return false;
        }
        return authenticationType == that.authenticationType;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (userName != null) ? userName.hashCode() : 0;
        result = (31 * result) + (authenticationType != null ? authenticationType.hashCode() : 0);
        return result;
    }

    public interface UserResponseSwagger {
        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserResourceProvider.USER_RESOURCE_CATEGORY)
        org.apache.ambari.server.controller.UserResponse getUserResponse();
    }
}