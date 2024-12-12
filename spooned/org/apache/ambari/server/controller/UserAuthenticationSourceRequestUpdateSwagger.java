package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public interface UserAuthenticationSourceRequestUpdateSwagger extends org.apache.ambari.server.controller.ApiModel {
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_SOURCE_RESOURCE_CATEGORY)
    org.apache.ambari.server.controller.UserAuthenticationSourceRequestUpdateSwagger.UserAuthenticationSourceRequestUpdateInfo getUpdateUserAuthenticationSourceRequest();

    interface UserAuthenticationSourceRequestUpdateInfo {
        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.KEY_PROPERTY_ID, required = true)
        public java.lang.String getKey();

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.OLD_KEY_PROPERTY_ID, required = false)
        public java.lang.String getOldKey();
    }
}