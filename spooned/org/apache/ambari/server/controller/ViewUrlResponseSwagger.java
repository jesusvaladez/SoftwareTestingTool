package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public interface ViewUrlResponseSwagger extends org.apache.ambari.server.controller.ApiModel {
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewURLResourceProvider.VIEW_URL_INFO)
    org.apache.ambari.server.controller.ViewUrlResponseSwagger.ViewUrlInfo getViewUrlInfo();

    interface ViewUrlInfo {
        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewURLResourceProvider.URL_NAME_PROPERTY_ID)
        java.lang.String getUrlName();

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewURLResourceProvider.URL_SUFFIX_PROPERTY_ID)
        java.lang.String getUrlSuffix();

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewURLResourceProvider.VIEW_INSTANCE_COMMON_NAME_PROPERTY_ID)
        java.lang.String getViewInstanceCommonName();

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewURLResourceProvider.VIEW_INSTANCE_NAME_PROPERTY_ID)
        java.lang.String getViewInstanceName();

        @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.ViewURLResourceProvider.VIEW_INSTANCE_VERSION_PROPERTY_ID)
        java.lang.String getViewInstanceVersion();
    }
}