package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public interface RequestPutRequest extends org.apache.ambari.server.controller.ApiModel {
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUESTS)
    org.apache.ambari.server.controller.RequestRequest getRequestRequest();
}