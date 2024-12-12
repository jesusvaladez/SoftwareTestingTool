package org.apache.ambari.server.controller;
public class StackConfigurationRequest extends org.apache.ambari.server.controller.StackServiceRequest {
    private java.lang.String propertyName;

    public StackConfigurationRequest(java.lang.String stackName, java.lang.String stackVersion, java.lang.String serviceName, java.lang.String propertyName) {
        super(stackName, stackVersion, serviceName);
        setPropertyName(propertyName);
    }

    public java.lang.String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(java.lang.String propertyName) {
        this.propertyName = propertyName;
    }
}