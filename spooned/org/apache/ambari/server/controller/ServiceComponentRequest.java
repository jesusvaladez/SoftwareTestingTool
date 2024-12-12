package org.apache.ambari.server.controller;
public class ServiceComponentRequest {
    private java.lang.String clusterName;

    private java.lang.String serviceName;

    private java.lang.String componentName;

    private java.lang.String desiredState;

    private java.lang.String componentCategory;

    private java.lang.String recoveryEnabled;

    public ServiceComponentRequest(java.lang.String clusterName, java.lang.String serviceName, java.lang.String componentName, java.lang.String desiredState) {
        this(clusterName, serviceName, componentName, desiredState, null, null);
    }

    public ServiceComponentRequest(java.lang.String clusterName, java.lang.String serviceName, java.lang.String componentName, java.lang.String desiredState, java.lang.String recoveryEnabled) {
        this(clusterName, serviceName, componentName, desiredState, recoveryEnabled, null);
    }

    public ServiceComponentRequest(java.lang.String clusterName, java.lang.String serviceName, java.lang.String componentName, java.lang.String desiredState, java.lang.String recoveryEnabled, java.lang.String componentCategory) {
        this.clusterName = clusterName;
        this.serviceName = serviceName;
        this.componentName = componentName;
        this.desiredState = desiredState;
        this.recoveryEnabled = recoveryEnabled;
        this.componentCategory = componentCategory;
    }

    public java.lang.String getServiceName() {
        return serviceName;
    }

    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }

    public java.lang.String getComponentName() {
        return componentName;
    }

    public void setComponentName(java.lang.String componentName) {
        this.componentName = componentName;
    }

    public java.lang.String getDesiredState() {
        return desiredState;
    }

    public void setDesiredState(java.lang.String desiredState) {
        this.desiredState = desiredState;
    }

    public java.lang.String getClusterName() {
        return clusterName;
    }

    public void setClusterName(java.lang.String clusterName) {
        this.clusterName = clusterName;
    }

    public java.lang.String getRecoveryEnabled() {
        return recoveryEnabled;
    }

    public void setRecoveryEnabled(java.lang.String recoveryEnabled) {
        this.recoveryEnabled = recoveryEnabled;
    }

    public java.lang.String getComponentCategory() {
        return componentCategory;
    }

    public void setComponentCategory(java.lang.String componentCategory) {
        this.componentCategory = componentCategory;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return java.lang.String.format("[clusterName=%s, serviceName=%s, componentName=%s, desiredState=%s, recoveryEnabled=%s, componentCategory=%s]", clusterName, serviceName, clusterName, desiredState, recoveryEnabled, componentCategory);
    }
}