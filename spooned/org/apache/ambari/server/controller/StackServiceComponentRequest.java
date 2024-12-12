package org.apache.ambari.server.controller;
public class StackServiceComponentRequest extends org.apache.ambari.server.controller.StackServiceRequest {
    private java.lang.String componentName;

    private java.lang.String recoveryEnabled;

    public StackServiceComponentRequest(java.lang.String stackName, java.lang.String stackVersion, java.lang.String serviceName, java.lang.String componentName) {
        this(stackName, stackVersion, serviceName, componentName, null);
    }

    public StackServiceComponentRequest(java.lang.String stackName, java.lang.String stackVersion, java.lang.String serviceName, java.lang.String componentName, java.lang.String recoveryEnabled) {
        super(stackName, stackVersion, serviceName);
        setComponentName(componentName);
        setRecoveryEnabled(recoveryEnabled);
    }

    public java.lang.String getComponentName() {
        return componentName;
    }

    public void setComponentName(java.lang.String componentName) {
        this.componentName = componentName;
    }

    public java.lang.String getRecoveryEnabled() {
        return recoveryEnabled;
    }

    public void setRecoveryEnabled(java.lang.String recoveryEnabled) {
        this.recoveryEnabled = recoveryEnabled;
    }
}