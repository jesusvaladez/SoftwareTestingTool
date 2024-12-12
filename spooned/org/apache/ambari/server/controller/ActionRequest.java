package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public class ActionRequest {
    static final java.lang.String ACTION_NAME = "action_name";

    static final java.lang.String ACTION_TYPE = "action_type";

    static final java.lang.String INPUTS = "inputs";

    static final java.lang.String TARGET_SERVICE = "target_service";

    static final java.lang.String TARGET_COMPONENT = "target_component";

    static final java.lang.String DESCRIPTION = "description";

    static final java.lang.String TARGET_TYPE = "target_type";

    static final java.lang.String DEFAULT_TIMEOUT = "default_timeout";

    private java.lang.String actionName;

    private java.lang.String actionType;

    private java.lang.String inputs;

    private java.lang.String targetService;

    private java.lang.String targetComponent;

    private java.lang.String description;

    private java.lang.String targetType;

    private java.lang.String defaultTimeout;

    public ActionRequest(java.lang.String actionName, java.lang.String actionType, java.lang.String inputs, java.lang.String targetService, java.lang.String targetComponent, java.lang.String description, java.lang.String targetType, java.lang.String defaultTimeout) {
        setActionName(actionName);
        setActionType(actionType);
        setInputs(inputs);
        setTargetService(targetService);
        setTargetComponent(targetComponent);
        setDescription(description);
        setTargetType(targetType);
        setDefaultTimeout(defaultTimeout);
    }

    public static org.apache.ambari.server.controller.ActionRequest getAllRequest() {
        return new org.apache.ambari.server.controller.ActionRequest(null, null, null, null, null, null, null, null);
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.ActionRequest.ACTION_NAME)
    public java.lang.String getActionName() {
        return actionName;
    }

    public void setActionName(java.lang.String actionName) {
        this.actionName = actionName;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.ActionRequest.ACTION_TYPE)
    public java.lang.String getActionType() {
        return actionType;
    }

    public void setActionType(java.lang.String actionType) {
        this.actionType = actionType;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.ActionRequest.INPUTS)
    public java.lang.String getInputs() {
        return inputs;
    }

    public void setInputs(java.lang.String inputs) {
        this.inputs = inputs;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.ActionRequest.TARGET_SERVICE)
    public java.lang.String getTargetService() {
        return targetService;
    }

    public void setTargetService(java.lang.String targetService) {
        this.targetService = targetService;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.ActionRequest.TARGET_COMPONENT)
    public java.lang.String getTargetComponent() {
        return targetComponent;
    }

    public void setTargetComponent(java.lang.String targetComponent) {
        this.targetComponent = targetComponent;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.ActionRequest.DESCRIPTION)
    public java.lang.String getDescription() {
        return description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.ActionRequest.TARGET_TYPE)
    public java.lang.String getTargetType() {
        return targetType;
    }

    public void setTargetType(java.lang.String targetType) {
        this.targetType = targetType;
    }

    @io.swagger.annotations.ApiModelProperty(name = org.apache.ambari.server.controller.ActionRequest.DEFAULT_TIMEOUT)
    public java.lang.String getDefaultTimeout() {
        return defaultTimeout;
    }

    public void setDefaultTimeout(java.lang.String defaultTimeout) {
        this.defaultTimeout = defaultTimeout;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return new java.lang.StringBuilder().append("actionName :").append(actionName).append(", actionType :").append(actionType).append(", inputs :").append(inputs).append(", targetService :").append(targetService).append(", targetComponent :").append(targetComponent).append(", description :").append(description).append(", targetType :").append(targetType).append(", defaultTimeout :").append(defaultTimeout).toString();
    }
}