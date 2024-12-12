package org.apache.ambari.server.view.configuration;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public class InstanceConfig {
    private java.lang.String name;

    private java.lang.String label;

    private java.lang.String description;

    private java.lang.String shortUrl;

    private boolean visible = true;

    private java.lang.String icon;

    private java.lang.String icon64;

    @javax.xml.bind.annotation.XmlElement(name = "property")
    private java.util.List<org.apache.ambari.server.view.configuration.PropertyConfig> properties;

    public java.lang.String getName() {
        return name;
    }

    public java.lang.String getLabel() {
        return label;
    }

    public java.lang.String getDescription() {
        return description;
    }

    public boolean isVisible() {
        return visible;
    }

    public java.lang.String getIcon() {
        return icon;
    }

    public java.lang.String getIcon64() {
        return icon64;
    }

    public java.util.List<org.apache.ambari.server.view.configuration.PropertyConfig> getProperties() {
        return properties == null ? java.util.Collections.emptyList() : properties;
    }
}