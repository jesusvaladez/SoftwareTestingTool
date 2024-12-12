package org.apache.ambari.server.api.services;
import io.swagger.annotations.ApiModelProperty;
public interface RootServiceComponentConfigurationRequestSwagger extends org.apache.ambari.server.controller.ApiModel {
    @io.swagger.annotations.ApiModelProperty(name = "Configuration")
    org.apache.ambari.server.api.services.RootServiceComponentConfigurationRequestSwagger.RootServiceComponentConfigurationRequestInfo getRootServiceComponentConfigurationRequestInfo();

    interface RootServiceComponentConfigurationRequestInfo {
        @io.swagger.annotations.ApiModelProperty
        java.lang.String getServiceName();

        @io.swagger.annotations.ApiModelProperty
        java.lang.String getComponentName();

        @io.swagger.annotations.ApiModelProperty
        java.lang.String getCategoryName();

        @io.swagger.annotations.ApiModelProperty
        java.util.Map<java.lang.String, java.lang.String> getProperties();
    }
}