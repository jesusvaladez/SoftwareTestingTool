package org.apache.ambari.server.api.services;
import io.swagger.annotations.ApiModelProperty;
public interface RootServiceComponentConfigurationResponseSwagger extends org.apache.ambari.server.controller.ApiModel {
    @io.swagger.annotations.ApiModelProperty(name = "Configuration")
    org.apache.ambari.server.api.services.RootServiceComponentConfigurationResponseSwagger.RootServiceComponentConfigurationResponseInfo getRootServiceComponentConfigurationResponseInfo();

    interface RootServiceComponentConfigurationResponseInfo {
        @io.swagger.annotations.ApiModelProperty
        java.lang.String getServiceName();

        @io.swagger.annotations.ApiModelProperty
        java.lang.String getComponentName();

        @io.swagger.annotations.ApiModelProperty
        java.lang.String getCategoryName();

        @io.swagger.annotations.ApiModelProperty
        java.util.Map<java.lang.String, java.lang.Object> getProperties();
    }
}