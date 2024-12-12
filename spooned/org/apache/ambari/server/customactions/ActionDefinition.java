package org.apache.ambari.server.customactions;
public class ActionDefinition {
    private java.lang.String actionName;

    private org.apache.ambari.server.actionmanager.ActionType actionType;

    private java.lang.String inputs;

    private java.lang.String targetService;

    private java.lang.String targetComponent;

    private java.lang.String description;

    private org.apache.ambari.server.actionmanager.TargetHostType targetType;

    private java.lang.Integer defaultTimeout;

    private java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> permissions;

    public ActionDefinition(java.lang.String actionName, org.apache.ambari.server.actionmanager.ActionType actionType, java.lang.String inputs, java.lang.String targetService, java.lang.String targetComponent, java.lang.String description, org.apache.ambari.server.actionmanager.TargetHostType targetType, java.lang.Integer defaultTimeout, java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> permissions) {
        setActionName(actionName);
        setActionType(actionType);
        setInputs(inputs);
        setTargetService(targetService);
        setTargetComponent(targetComponent);
        setDescription(description);
        setTargetType(targetType);
        setDefaultTimeout(defaultTimeout);
        setPermissions(permissions);
    }

    public java.lang.String getActionName() {
        return actionName;
    }

    public void setActionName(java.lang.String actionName) {
        this.actionName = actionName;
    }

    public org.apache.ambari.server.actionmanager.ActionType getActionType() {
        return actionType;
    }

    public void setActionType(org.apache.ambari.server.actionmanager.ActionType actionType) {
        this.actionType = actionType;
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

    public java.lang.String getTargetComponent() {
        return targetComponent;
    }

    public void setTargetComponent(java.lang.String targetComponent) {
        this.targetComponent = targetComponent;
    }

    public java.lang.String getDescription() {
        return description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public org.apache.ambari.server.actionmanager.TargetHostType getTargetType() {
        return targetType;
    }

    public void setTargetType(org.apache.ambari.server.actionmanager.TargetHostType targetType) {
        this.targetType = targetType;
    }

    public java.lang.Integer getDefaultTimeout() {
        return defaultTimeout;
    }

    public void setDefaultTimeout(java.lang.Integer defaultTimeout) {
        this.defaultTimeout = defaultTimeout;
    }

    public void setPermissions(java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> permissions) {
        this.permissions = permissions;
    }

    public java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> getPermissions() {
        return permissions;
    }

    public org.apache.ambari.server.controller.ActionResponse convertToResponse() {
        return new org.apache.ambari.server.controller.ActionResponse(getActionName(), getActionType().name(), getInputs(), getTargetService(), getTargetComponent(), getDescription(), getTargetType().name(), getDefaultTimeout().toString());
    }
}