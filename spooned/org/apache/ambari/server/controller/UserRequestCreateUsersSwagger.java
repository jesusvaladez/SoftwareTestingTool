package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public interface UserRequestCreateUsersSwagger extends org.apache.ambari.server.controller.ApiModel {
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserResourceProvider.USER_RESOURCE_CATEGORY)
    org.apache.ambari.server.controller.UserRequestCreateUsersSwagger.CreateUsersInfo getCreateUsersRequest();

    interface CreateUsersInfo {
        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserResourceProvider.USERNAME_PROPERTY_ID)
        java.lang.String getUsername();

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserResourceProvider.PASSWORD_PROPERTY_ID)
        java.lang.String getPassword();

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserResourceProvider.ACTIVE_PROPERTY_ID)
        java.lang.Boolean isActive();

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserResourceProvider.ADMIN_PROPERTY_ID)
        java.lang.Boolean isAdmin();

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserResourceProvider.DISPLAY_NAME_PROPERTY_ID)
        java.lang.String getDisplayName();

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserResourceProvider.LOCAL_USERNAME_PROPERTY_ID)
        java.lang.String getLocalUserName();
    }
}