package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public interface QuickLinksResponse extends org.apache.ambari.server.controller.ApiModel {
    @io.swagger.annotations.ApiModelProperty(name = "QuickLinkInfo")
    public org.apache.ambari.server.controller.QuickLinksResponse.QuickLinksResponseInfo getQuickLinksResponseInfo();

    public interface QuickLinksResponseInfo {
        @io.swagger.annotations.ApiModelProperty(name = "file_name")
        java.lang.String getFileName();

        @io.swagger.annotations.ApiModelProperty(name = "service_name")
        java.lang.String getServiceName();

        @io.swagger.annotations.ApiModelProperty(name = "stack_name")
        java.lang.String getStackName();

        @io.swagger.annotations.ApiModelProperty(name = "stack_version")
        java.lang.String getStackVersion();

        @io.swagger.annotations.ApiModelProperty(name = "default")
        boolean isDefault();

        @io.swagger.annotations.ApiModelProperty(name = "quicklink_data")
        org.apache.ambari.server.state.quicklinks.QuickLinksConfiguration getQuickLinkData();
    }
}