package org.apache.ambari.server.state;
class PropertyValueEquals extends org.apache.ambari.server.state.PropertyExists {
    protected java.lang.String propertyValue;

    @javax.xml.bind.annotation.XmlElement
    public java.lang.String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(java.lang.String propertyValue) {
        this.propertyValue = propertyValue;
    }

    @java.lang.Override
    public boolean isResolved(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties) {
        return super.isResolved(properties) && propertyValue.equals(properties.get(configType).get(property));
    }
}