package org.apache.ambari.server.controller;
public class ActionExecutionContext {
    private final java.lang.String clusterName;

    private final java.lang.String actionName;

    private java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> resourceFilters;

    private org.apache.ambari.server.controller.internal.RequestOperationLevel operationLevel;

    private java.util.Map<java.lang.String, java.lang.String> parameters;

    private org.apache.ambari.server.actionmanager.TargetHostType targetType;

    private java.lang.Integer timeout;

    private java.lang.String expectedServiceName;

    private java.lang.String expectedComponentName;

    private boolean hostsInMaintenanceModeExcluded = true;

    private boolean allowRetry = false;

    private org.apache.ambari.server.state.StackId stackId;

    private boolean isFutureCommand = false;

    private java.util.List<org.apache.ambari.server.controller.ActionExecutionContext.ExecutionCommandVisitor> m_visitors = new java.util.ArrayList<>();

    private boolean autoSkipFailures = false;

    public ActionExecutionContext(java.lang.String clusterName, java.lang.String actionName, java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> resourceFilters, java.util.Map<java.lang.String, java.lang.String> parameters, org.apache.ambari.server.actionmanager.TargetHostType targetType, java.lang.Integer timeout, java.lang.String expectedServiceName, java.lang.String expectedComponentName) {
        this.clusterName = clusterName;
        this.actionName = actionName;
        this.resourceFilters = resourceFilters;
        this.parameters = parameters;
        this.targetType = targetType;
        this.timeout = timeout;
        this.expectedServiceName = expectedServiceName;
        this.expectedComponentName = expectedComponentName;
    }

    public ActionExecutionContext(java.lang.String clusterName, java.lang.String actionName, java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> resourceFilters) {
        this.clusterName = clusterName;
        this.actionName = actionName;
        this.resourceFilters = resourceFilters;
    }

    public ActionExecutionContext(java.lang.String clusterName, java.lang.String commandName, java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> resourceFilters, java.util.Map<java.lang.String, java.lang.String> parameters) {
        this.clusterName = clusterName;
        actionName = commandName;
        this.resourceFilters = resourceFilters;
        this.parameters = parameters;
    }

    public java.lang.String getClusterName() {
        return clusterName;
    }

    public java.lang.String getActionName() {
        return actionName;
    }

    public java.util.Map<java.lang.String, java.lang.String> getParameters() {
        return parameters;
    }

    public org.apache.ambari.server.actionmanager.TargetHostType getTargetType() {
        return targetType;
    }

    public java.lang.Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(java.lang.Integer timeout) {
        this.timeout = timeout;
    }

    public java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> getResourceFilters() {
        return resourceFilters;
    }

    public org.apache.ambari.server.controller.internal.RequestOperationLevel getOperationLevel() {
        return operationLevel;
    }

    public void setOperationLevel(org.apache.ambari.server.controller.internal.RequestOperationLevel operationLevel) {
        this.operationLevel = operationLevel;
    }

    public java.lang.String getExpectedServiceName() {
        return expectedServiceName;
    }

    public java.lang.String getExpectedComponentName() {
        return expectedComponentName;
    }

    public boolean isRetryAllowed() {
        return allowRetry;
    }

    public void setRetryAllowed(boolean allowRetry) {
        this.allowRetry = allowRetry;
    }

    public boolean isFailureAutoSkipped() {
        return autoSkipFailures;
    }

    public void setAutoSkipFailures(boolean autoSkipFailures) {
        this.autoSkipFailures = autoSkipFailures;
    }

    public org.apache.ambari.server.state.StackId getStackId() {
        return stackId;
    }

    public void setStackId(org.apache.ambari.server.state.StackId stackId) {
        this.stackId = stackId;
    }

    public void addVisitor(org.apache.ambari.server.controller.ActionExecutionContext.ExecutionCommandVisitor visitor) {
        m_visitors.add(visitor);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((((((((((((((((((((("ActionExecutionContext{" + "clusterName='") + clusterName) + '\'') + ", actionName='") + actionName) + '\'') + ", resourceFilters=") + resourceFilters) + ", operationLevel=") + operationLevel) + ", parameters=") + parameters) + ", targetType=") + targetType) + ", timeout=") + timeout) + ", isMaintenanceModeHostExcluded=") + hostsInMaintenanceModeExcluded) + ", allowRetry=") + allowRetry) + ", autoSkipFailures=") + autoSkipFailures) + '}';
    }

    public boolean isMaintenanceModeHostExcluded() {
        return hostsInMaintenanceModeExcluded;
    }

    public void setMaintenanceModeHostExcluded(boolean excluded) {
        hostsInMaintenanceModeExcluded = excluded;
    }

    public void visitAll(org.apache.ambari.server.agent.ExecutionCommand command) {
        for (org.apache.ambari.server.controller.ActionExecutionContext.ExecutionCommandVisitor visitor : m_visitors) {
            visitor.visit(command);
        }
    }

    public interface ExecutionCommandVisitor {
        void visit(org.apache.ambari.server.agent.ExecutionCommand command);
    }

    public boolean isFutureCommand() {
        return isFutureCommand;
    }

    public void setIsFutureCommand(boolean isFutureCommand) {
        this.isFutureCommand = isFutureCommand;
    }
}