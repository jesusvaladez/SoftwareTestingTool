package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public interface ActionRequestSwagger {
    @io.swagger.annotations.ApiModelProperty(name = "Actions")
    public org.apache.ambari.server.controller.ActionRequest getActionRequest();
}