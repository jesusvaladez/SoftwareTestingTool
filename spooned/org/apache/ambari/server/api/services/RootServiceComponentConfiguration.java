package org.apache.ambari.server.api.services;
public class RootServiceComponentConfiguration {
    private final java.util.Map<java.lang.String, java.lang.String> properties;

    private final java.util.Map<java.lang.String, java.lang.String> propertyTypes;

    public RootServiceComponentConfiguration() {
        this(new java.util.TreeMap<>(), new java.util.TreeMap<>());
    }

    public RootServiceComponentConfiguration(java.util.Map<java.lang.String, java.lang.String> properties, java.util.Map<java.lang.String, java.lang.String> propertyTypes) {
        this.properties = (properties == null) ? new java.util.TreeMap<>() : properties;
        this.propertyTypes = (propertyTypes == null) ? new java.util.TreeMap<>() : propertyTypes;
    }

    public void addProperty(java.lang.String propertyName, java.lang.String propertyValue) {
        properties.put(propertyName, propertyValue);
    }

    public void addPropertyType(java.lang.String propertyName, java.lang.String propertyType) {
        propertyTypes.put(propertyName, propertyType);
    }

    public java.util.Map<java.lang.String, java.lang.String> getProperties() {
        return properties;
    }

    public java.util.Map<java.lang.String, java.lang.String> getPropertyTypes() {
        return propertyTypes;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        return org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals(this, obj);
    }

    @java.lang.Override
    public int hashCode() {
        return org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode(3, 19, this);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString(this, org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE);
    }
}