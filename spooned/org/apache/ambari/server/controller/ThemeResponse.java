package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public interface ThemeResponse extends org.apache.ambari.server.controller.ApiModel {
    @io.swagger.annotations.ApiModelProperty(name = "ThemeInfo")
    org.apache.ambari.server.controller.ThemeResponse.ThemeInfoResponse getThemeInfo();

    interface ThemeInfoResponse {
        @io.swagger.annotations.ApiModelProperty(name = "default")
        boolean isDefault();

        @io.swagger.annotations.ApiModelProperty(name = "file_name")
        java.lang.String getFileName();

        @io.swagger.annotations.ApiModelProperty(name = "service_name")
        java.lang.String getServiceName();

        @io.swagger.annotations.ApiModelProperty(name = "stack_name")
        java.lang.String getStackName();

        @io.swagger.annotations.ApiModelProperty(name = "stack_version")
        java.lang.String getStackVersion();

        @io.swagger.annotations.ApiModelProperty(name = "theme_data")
        org.apache.ambari.server.state.theme.Theme getThemeData();
    }
}