package org.apache.ambari.server.controller;
public class ExecuteActionRequest {
    private final java.lang.String clusterName;

    private final java.lang.String commandName;

    private final java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> resourceFilters;

    private org.apache.ambari.server.controller.internal.RequestOperationLevel operationLevel = null;

    private java.lang.String actionName;

    private java.util.Map<java.lang.String, java.lang.String> parameters;

    private boolean exclusive;

    public ExecuteActionRequest(java.lang.String clusterName, java.lang.String commandName, java.lang.String actionName, java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> resourceFilters, org.apache.ambari.server.controller.internal.RequestOperationLevel operationLevel, java.util.Map<java.lang.String, java.lang.String> parameters, boolean exclusive) {
        this(clusterName, commandName, parameters, exclusive);
        this.actionName = actionName;
        if (resourceFilters != null) {
            this.resourceFilters.addAll(resourceFilters);
        }
        this.operationLevel = operationLevel;
    }

    public ExecuteActionRequest(java.lang.String clusterName, java.lang.String commandName, java.util.Map<java.lang.String, java.lang.String> parameters, boolean exclusive) {
        this.clusterName = clusterName;
        this.commandName = commandName;
        this.actionName = null;
        this.parameters = new java.util.HashMap<>();
        if (parameters != null) {
            this.parameters.putAll(parameters);
        }
        this.resourceFilters = new java.util.ArrayList<>();
        this.exclusive = exclusive;
    }

    public java.lang.String getClusterName() {
        return clusterName;
    }

    public java.lang.String getCommandName() {
        return commandName;
    }

    public java.lang.String getActionName() {
        return actionName;
    }

    public java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> getResourceFilters() {
        return resourceFilters;
    }

    public org.apache.ambari.server.controller.internal.RequestOperationLevel getOperationLevel() {
        return operationLevel;
    }

    public java.util.Map<java.lang.String, java.lang.String> getParameters() {
        return parameters;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public void setExclusive(boolean isExclusive) {
        this.exclusive = isExclusive;
    }

    public java.lang.Boolean isCommand() {
        return (actionName == null) || actionName.isEmpty();
    }

    @java.lang.Override
    public synchronized java.lang.String toString() {
        return new java.lang.StringBuilder().append("isCommand :").append(isCommand()).append(", action :").append(actionName).append(", command :").append(commandName).append(", inputs :").append(parameters).append(", resourceFilters: ").append(resourceFilters).append(", exclusive: ").append(exclusive).append(", clusterName :").append(clusterName).toString();
    }
}