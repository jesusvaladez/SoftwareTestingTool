package org.apache.ambari.server.view.configuration;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public class ParameterConfig {
    private java.lang.String name;

    private java.lang.String description;

    private java.lang.String label;

    private java.lang.String placeholder;

    @javax.xml.bind.annotation.XmlElement(name = "default-value")
    private java.lang.String defaultValue;

    @javax.xml.bind.annotation.XmlElement(name = "cluster-config")
    private java.lang.String clusterConfig;

    private boolean required;

    private boolean masked;

    public java.lang.String getName() {
        return name;
    }

    public java.lang.String getDescription() {
        return description;
    }

    public java.lang.String getLabel() {
        return label;
    }

    public java.lang.String getPlaceholder() {
        return placeholder;
    }

    public java.lang.String getDefaultValue() {
        return defaultValue;
    }

    public java.lang.String getClusterConfig() {
        return clusterConfig;
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isMasked() {
        return masked;
    }
}