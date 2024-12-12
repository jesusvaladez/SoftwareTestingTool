package org.apache.ambari.server.state;
class PropertyExists implements org.apache.ambari.server.state.DependencyConditionInfo {
    protected java.lang.String configType;

    protected java.lang.String property;

    protected java.lang.String type = this.getClass().getSimpleName();

    public java.lang.String getType() {
        return type;
    }

    public void setType(java.lang.String type) {
        this.type = type;
    }

    @javax.xml.bind.annotation.XmlElement
    public java.lang.String getProperty() {
        return property;
    }

    public void setProperty(java.lang.String property) {
        this.property = property;
    }

    @javax.xml.bind.annotation.XmlElement
    public java.lang.String getConfigType() {
        return configType;
    }

    public void setConfigType(java.lang.String configType) {
        this.configType = configType;
    }

    @java.lang.Override
    public boolean isResolved(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties) {
        return properties.get(configType).containsKey(property);
    }
}