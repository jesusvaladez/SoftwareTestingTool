package org.apache.ambari.server.customactions;
public class ActionDefinitionSpec {
    private java.lang.String actionName;

    private java.lang.String actionType;

    private java.lang.String inputs;

    private java.lang.String targetService;

    private java.lang.String targetComponent;

    private java.lang.String description;

    private java.lang.String targetType;

    private java.lang.String defaultTimeout;

    private java.lang.String permissions;

    public java.lang.String getTargetComponent() {
        return targetComponent;
    }

    public void setTargetComponent(java.lang.String targetComponent) {
        this.targetComponent = targetComponent;
    }

    public java.lang.String getTargetType() {
        return targetType;
    }

    public void setTargetType(java.lang.String targetType) {
        this.targetType = targetType;
    }

    public java.lang.String getDefaultTimeout() {
        return defaultTimeout;
    }

    public void setDefaultTimeout(java.lang.String defaultTimeout) {
        this.defaultTimeout = defaultTimeout;
    }

    public java.lang.String getActionName() {
        return actionName;
    }

    public void setActionName(java.lang.String actionName) {
        this.actionName = actionName;
    }

    public java.lang.String getActionType() {
        return actionType;
    }

    public void setActionType(java.lang.String actionType) {
        this.actionType = actionType;
    }

    public java.lang.String getDescription() {
        return description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public java.lang.String getInputs() {
        return inputs;
    }

    public void setInputs(java.lang.String inputs) {
        this.inputs = inputs;
    }

    public java.lang.String getTargetService() {
        return targetService;
    }

    public void setTargetService(java.lang.String targetService) {
        this.targetService = targetService;
    }

    public java.lang.String getPermissions() {
        return permissions;
    }

    public void setPermissions(java.lang.String permissions) {
        this.permissions = permissions;
    }

    @java.lang.Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + (actionName == null ? 0 : actionName.hashCode());
        return result;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        org.apache.ambari.server.customactions.ActionDefinitionSpec other = ((org.apache.ambari.server.customactions.ActionDefinitionSpec) (obj));
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }
        return true;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return new java.lang.StringBuilder().append("ActionDefinition:").append(" actionName: ").append(actionName).append(" actionType: ").append(actionType).append(" inputs: ").append(inputs).append(" description: ").append(description).append(" targetService: ").append(targetService).append(" targetComponent: ").append(targetComponent).append(" defaultTimeout: ").append(defaultTimeout).append(" targetType: ").append(targetType).append(" permissions: ").append(permissions).toString();
    }
}