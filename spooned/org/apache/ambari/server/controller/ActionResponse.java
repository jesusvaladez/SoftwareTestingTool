package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public class ActionResponse {
    private java.lang.String actionName;

    private java.lang.String actionType;

    private java.lang.String inputs;

    private java.lang.String targetService;

    private java.lang.String targetComponent;

    private java.lang.String description;

    private java.lang.String targetType;

    private java.lang.String defaultTimeout;

    public ActionResponse(java.lang.String actionName, java.lang.String actionType, java.lang.String inputs, java.lang.String targetService, java.lang.String targetComponent, java.lang.String description, java.lang.String targetType, java.lang.String defaultTimeout) {
        setActionName(actionName);
        setActionType(actionType);
        setInputs(inputs);
        setTargetService(targetService);
        setTargetComponent(targetComponent);
        setDescription(description);
        setTargetType(targetType);
        setDefaultTimeout(defaultTimeout);
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
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.controller.ActionResponse that = ((org.apache.ambari.server.controller.ActionResponse) (o));
        if (actionName != null ? !actionName.equals(that.actionName) : that.actionName != null) {
            return false;
        }
        if (actionType != null ? !actionType.equals(that.actionType) : that.actionType != null) {
            return false;
        }
        if (description != null ? !description.equals(that.description) : that.description != null) {
            return false;
        }
        if (inputs != null ? !inputs.equals(that.inputs) : that.inputs != null) {
            return false;
        }
        if (targetService != null ? !targetService.equals(that.targetService) : that.targetService != null) {
            return false;
        }
        if (targetComponent != null ? !targetComponent.equals(that.targetComponent) : that.targetComponent != null) {
            return false;
        }
        if (targetType != null ? !targetType.equals(that.targetType) : that.targetType != null) {
            return false;
        }
        if (defaultTimeout != null ? !defaultTimeout.equals(that.defaultTimeout) : that.defaultTimeout != null) {
            return false;
        }
        return true;
    }

    @java.lang.Override
    public int hashCode() {
        int result;
        result = 31 + (actionName != null ? actionName.hashCode() : 0);
        result = result + (actionType != null ? actionType.hashCode() : 0);
        result = result + (inputs != null ? inputs.hashCode() : 0);
        result = result + (description != null ? description.hashCode() : 0);
        result = result + (targetService != null ? targetService.hashCode() : 0);
        result = result + (targetComponent != null ? targetComponent.hashCode() : 0);
        result = result + (targetType != null ? targetType.hashCode() : 0);
        result = result + (defaultTimeout != null ? defaultTimeout.hashCode() : 0);
        return result;
    }

    public interface ActionResponseSwagger {
        @io.swagger.annotations.ApiModelProperty(name = "Actions")
        org.apache.ambari.server.controller.ActionResponse getActionResponse();
    }
}