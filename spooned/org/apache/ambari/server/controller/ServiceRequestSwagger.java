package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public interface ServiceRequestSwagger extends org.apache.ambari.server.controller.ApiModel {
    @io.swagger.annotations.ApiModelProperty(name = "ServiceInfo")
    public org.apache.ambari.server.controller.ServiceRequest getServiceRequest();
}