package org.apache.ambari.server.controller.internal;
public class SortRequestImpl implements org.apache.ambari.server.controller.spi.SortRequest {
    java.util.List<org.apache.ambari.server.controller.spi.SortRequestProperty> properties;

    public SortRequestImpl(java.util.List<org.apache.ambari.server.controller.spi.SortRequestProperty> properties) {
        this.properties = properties;
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.controller.spi.SortRequestProperty> getProperties() {
        return properties;
    }

    @java.lang.Override
    public java.util.List<java.lang.String> getPropertyIds() {
        java.util.List<java.lang.String> propertyIds = new java.util.ArrayList<>();
        for (org.apache.ambari.server.controller.spi.SortRequestProperty property : properties) {
            propertyIds.add(property.getPropertyId());
        }
        return propertyIds;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (("SortRequestImpl{" + "properties=") + properties) + '}';
    }
}