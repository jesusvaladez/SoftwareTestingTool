package org.apache.ambari.server.api.services;
import io.swagger.annotations.ApiModelProperty;
@java.lang.SuppressWarnings("unused")
public interface SettingRequestSwagger extends org.apache.ambari.server.controller.ApiModel {
    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.internal.SettingResourceProvider.RESPONSE_KEY)
    org.apache.ambari.server.controller.SettingRequest getSettingRequest();
}