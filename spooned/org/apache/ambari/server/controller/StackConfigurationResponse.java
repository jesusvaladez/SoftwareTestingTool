package org.apache.ambari.server.controller;
import io.swagger.annotations.ApiModelProperty;
public class StackConfigurationResponse {
    public StackConfigurationResponse(java.lang.String propertyName, java.lang.String propertyValue, java.lang.String propertyDescription, java.lang.String type, java.util.Map<java.lang.String, java.lang.String> propertyAttributes) {
        setPropertyName(propertyName);
        setPropertyValue(propertyValue);
        setPropertyDescription(propertyDescription);
        setType(type);
        setPropertyAttributes(propertyAttributes);
    }

    public StackConfigurationResponse(java.lang.String propertyName, java.lang.String propertyValue, java.lang.String propertyDescription, java.lang.String propertyDisplayName, java.lang.String type, java.lang.Boolean isRequired, java.util.Set<org.apache.ambari.server.state.PropertyInfo.PropertyType> propertyTypes, java.util.Map<java.lang.String, java.lang.String> propertyAttributes, org.apache.ambari.server.state.ValueAttributesInfo propertyValueAttributes, java.util.Set<org.apache.ambari.server.state.PropertyDependencyInfo> dependsOnProperties) {
        setPropertyName(propertyName);
        setPropertyValue(propertyValue);
        setPropertyDescription(propertyDescription);
        setPropertyDisplayName(propertyDisplayName);
        setType(type);
        setRequired(isRequired);
        setPropertyType(propertyTypes);
        setPropertyAttributes(propertyAttributes);
        setPropertyValueAttributes(propertyValueAttributes);
        setDependsOnProperties(dependsOnProperties);
    }

    private java.lang.String stackName;

    private java.lang.String stackVersion;

    private java.lang.String serviceName;

    private java.lang.String propertyName;

    private java.lang.String propertyValue;

    private java.lang.String propertyDescription;

    private java.lang.String propertyDisplayName;

    private java.lang.String type;

    private java.util.Map<java.lang.String, java.lang.String> propertyAttributes;

    private org.apache.ambari.server.state.ValueAttributesInfo propertyValueAttributes;

    private java.util.Set<org.apache.ambari.server.state.PropertyDependencyInfo> dependsOnProperties;

    private java.lang.Boolean isRequired;

    private java.util.Set<org.apache.ambari.server.state.PropertyInfo.PropertyType> propertyTypes;

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

    @io.swagger.annotations.ApiModelProperty(name = "property_value")
    public java.lang.String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(java.lang.String propertyValue) {
        this.propertyValue = propertyValue;
    }

    @io.swagger.annotations.ApiModelProperty(name = "property_description")
    public java.lang.String getPropertyDescription() {
        return propertyDescription;
    }

    public void setPropertyDescription(java.lang.String propertyDescription) {
        this.propertyDescription = propertyDescription;
    }

    @io.swagger.annotations.ApiModelProperty(name = "property_display_name")
    public java.lang.String getPropertyDisplayName() {
        return propertyDisplayName;
    }

    public void setPropertyDisplayName(java.lang.String propertyDisplayName) {
        this.propertyDisplayName = propertyDisplayName;
    }

    public java.lang.String getType() {
        return type;
    }

    @io.swagger.annotations.ApiModelProperty(name = "type")
    public void setType(java.lang.String type) {
        this.type = type;
    }

    @io.swagger.annotations.ApiModelProperty(hidden = true)
    public java.util.Map<java.lang.String, java.lang.String> getPropertyAttributes() {
        return propertyAttributes;
    }

    public void setPropertyAttributes(java.util.Map<java.lang.String, java.lang.String> propertyAttributes) {
        this.propertyAttributes = propertyAttributes;
    }

    @io.swagger.annotations.ApiModelProperty(name = "property_value_attributes")
    public org.apache.ambari.server.state.ValueAttributesInfo getPropertyValueAttributes() {
        return propertyValueAttributes;
    }

    public void setPropertyValueAttributes(org.apache.ambari.server.state.ValueAttributesInfo propertyValueAttributes) {
        this.propertyValueAttributes = propertyValueAttributes;
    }

    @io.swagger.annotations.ApiModelProperty(name = "dependencies")
    public java.util.Set<org.apache.ambari.server.state.PropertyDependencyInfo> getDependsOnProperties() {
        return dependsOnProperties;
    }

    public void setDependsOnProperties(java.util.Set<org.apache.ambari.server.state.PropertyDependencyInfo> dependsOnProperties) {
        this.dependsOnProperties = dependsOnProperties;
    }

    @io.swagger.annotations.ApiModelProperty(hidden = true)
    public java.lang.Boolean isRequired() {
        return isRequired;
    }

    public void setRequired(java.lang.Boolean required) {
        this.isRequired = required;
    }

    @io.swagger.annotations.ApiModelProperty(name = "property_type")
    public java.util.Set<org.apache.ambari.server.state.PropertyInfo.PropertyType> getPropertyType() {
        return propertyTypes;
    }

    public void setPropertyType(java.util.Set<org.apache.ambari.server.state.PropertyInfo.PropertyType> propertyTypes) {
        this.propertyTypes = propertyTypes;
    }

    public interface StackConfigurationResponseSwagger extends org.apache.ambari.server.controller.ApiModel {
        @io.swagger.annotations.ApiModelProperty(name = "StackConfigurations")
        public org.apache.ambari.server.controller.StackConfigurationResponse getStackConfigurationResponse();
    }
}