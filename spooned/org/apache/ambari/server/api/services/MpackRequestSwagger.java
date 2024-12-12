package org.apache.ambari.server.api.services;
import io.swagger.annotations.ApiModelProperty;
@java.lang.SuppressWarnings("unused")
public interface MpackRequestSwagger extends org.apache.ambari.server.controller.ApiModel {
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.MpackResourceProvider.RESPONSE_KEY)
    org.apache.ambari.server.controller.MpackRequest getMpackRequest();
}