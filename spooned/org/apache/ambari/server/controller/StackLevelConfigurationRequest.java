package org.apache.ambari.server.controller;
public class StackLevelConfigurationRequest extends org.apache.ambari.server.controller.StackVersionRequest {
    private java.lang.String propertyName;

    public StackLevelConfigurationRequest(java.lang.String stackName, java.lang.String stackVersion, java.lang.String propertyName) {
        super(stackName, stackVersion);
        setPropertyName(propertyName);
    }

    public java.lang.String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(java.lang.String propertyName) {
        this.propertyName = propertyName;
    }
}