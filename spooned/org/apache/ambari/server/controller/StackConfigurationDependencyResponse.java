package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public class StackConfigurationDependencyResponse {
    private java.lang.String stackName;

    private java.lang.String stackVersion;

    private java.lang.String serviceName;

    private java.lang.String propertyName;

    private java.lang.String dependencyName;

    private java.lang.String dependencyType;

    public StackConfigurationDependencyResponse(java.lang.String dependencyName) {
        this.dependencyName = dependencyName;
    }

    public StackConfigurationDependencyResponse(java.lang.String dependencyName, java.lang.String dependencyType) {
        this.dependencyName = dependencyName;
        this.dependencyType = dependencyType;
    }

    @io.swagger.annotations.ApiModelProperty(name = "stack_name")
    public java.lang.String getStackName() {
        return stackName;
    }

    public void setStackName(java.lang.String stackName) {
        this.stackName = stackName;
    }

    @io.swagger.annotations.ApiModelProperty(name = "stack_version")
    public java.lang.String getStackVersion() {
        return stackVersion;
    }

    public void setStackVersion(java.lang.String stackVersion) {
        this.stackVersion = stackVersion;
    }

    @io.swagger.annotations.ApiModelProperty(name = "service_name")
    public java.lang.String getServiceName() {
        return serviceName;
    }

    public void setServiceName(java.lang.String serviceName) {
        this.serviceName = serviceName;
    }

    @io.swagger.annotations.ApiModelProperty(name = "property_name")
    public java.lang.String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(java.lang.String propertyName) {
        this.propertyName = propertyName;
    }

    @io.swagger.annotations.ApiModelProperty(name = "dependency_name")
    public java.lang.String getDependencyName() {
        return dependencyName;
    }

    public void setDependencyName(java.lang.String dependencyName) {
        this.dependencyName = dependencyName;
    }

    @io.swagger.annotations.ApiModelProperty(name = "dependency_type")
    public java.lang.String getDependencyType() {
        return dependencyType;
    }

    public void setDependencyType(java.lang.String dependencyType) {
        this.dependencyType = dependencyType;
    }

    public interface StackConfigurationDependencyResponseSwagger extends org.apache.ambari.server.controller.ApiModel {
        @io.swagger.annotations.ApiModelProperty(name = "StackConfigurationDependency")
        public org.apache.ambari.server.controller.StackConfigurationDependencyResponse getStackConfigurationDependencyResponse();
    }
}