package org.apache.ambari.server.controller;
public class StackConfigurationDependencyRequest extends org.apache.ambari.server.controller.StackConfigurationRequest {
    private java.lang.String dependencyName;

    public StackConfigurationDependencyRequest(java.lang.String stackName, java.lang.String stackVersion, java.lang.String serviceName, java.lang.String propertyName, java.lang.String dependencyName) {
        super(stackName, stackVersion, serviceName, propertyName);
        this.dependencyName = dependencyName;
    }

    public java.lang.String getDependencyName() {
        return dependencyName;
    }

    public void setDependencyName(java.lang.String dependencyName) {
        this.dependencyName = dependencyName;
    }
}