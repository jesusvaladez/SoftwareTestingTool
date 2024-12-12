package org.apache.ambari.server.controller;
public class StackLevelConfigurationResponse extends org.apache.ambari.server.controller.StackConfigurationResponse {
    public StackLevelConfigurationResponse(java.lang.String propertyName, java.lang.String propertyValue, java.lang.String propertyDescription, java.lang.String propertyDisplayName, java.lang.String type, java.lang.Boolean isRequired, java.util.Set<org.apache.ambari.server.state.PropertyInfo.PropertyType> propertyTypes, java.util.Map<java.lang.String, java.lang.String> propertyAttributes, org.apache.ambari.server.state.ValueAttributesInfo propertyValueAttributes, java.util.Set<org.apache.ambari.server.state.PropertyDependencyInfo> dependsOnProperties) {
        super(propertyName, propertyValue, propertyDescription, propertyDisplayName, type, isRequired, propertyTypes, propertyAttributes, propertyValueAttributes, dependsOnProperties);
    }

    public StackLevelConfigurationResponse(java.lang.String propertyName, java.lang.String propertyValue, java.lang.String propertyDescription, java.lang.String type, java.util.Map<java.lang.String, java.lang.String> propertyAttributes) {
        super(propertyName, propertyValue, propertyDescription, type, propertyAttributes);
    }
}