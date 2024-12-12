package org.apache.ambari.server.topology;
public class AdvisedConfiguration {
    private final java.util.Map<java.lang.String, java.lang.String> properties;

    private final java.util.Map<java.lang.String, org.apache.ambari.server.state.ValueAttributesInfo> propertyValueAttributes;

    public AdvisedConfiguration(java.util.Map<java.lang.String, java.lang.String> properties, java.util.Map<java.lang.String, org.apache.ambari.server.state.ValueAttributesInfo> propertyValueAttributes) {
        this.properties = properties;
        this.propertyValueAttributes = propertyValueAttributes;
    }

    public java.util.Map<java.lang.String, java.lang.String> getProperties() {
        return properties;
    }

    public java.util.Map<java.lang.String, org.apache.ambari.server.state.ValueAttributesInfo> getPropertyValueAttributes() {
        return propertyValueAttributes;
    }
}