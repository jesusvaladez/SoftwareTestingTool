package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public interface UserAuthenticationSourceRequestCreateSwagger extends org.apache.ambari.server.controller.ApiModel {
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_SOURCE_RESOURCE_CATEGORY)
    org.apache.ambari.server.controller.UserAuthenticationSourceRequestCreateSwagger.CreateUserAuthenticationSourceInfo getCreateUserAuthenticationSourceRequest();

    interface CreateUserAuthenticationSourceInfo {
        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.AUTHENTICATION_TYPE_PROPERTY_ID, required = true)
        public org.apache.ambari.server.security.authorization.UserAuthenticationType getAuthenticationType();

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.UserAuthenticationSourceResourceProvider.KEY_PROPERTY_ID, required = true)
        public java.lang.String getKey();
    }
}