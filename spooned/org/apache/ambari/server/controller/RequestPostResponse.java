package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public interface RequestPostResponse extends org.apache.ambari.server.controller.ApiModel {
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUESTS)
    org.apache.ambari.server.controller.RequestPostResponse.ShortRequestInfo getShortRequestInfo();

    interface ShortRequestInfo {
        @io.swagger.annotations.ApiModelProperty(name = "id")
        long getId();

        @io.swagger.annotations.ApiModelProperty(name = "status")
        java.lang.String getStatus();
    }
}