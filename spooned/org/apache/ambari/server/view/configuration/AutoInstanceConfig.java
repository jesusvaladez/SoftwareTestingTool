package org.apache.ambari.server.view.configuration;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public class AutoInstanceConfig extends org.apache.ambari.server.view.configuration.InstanceConfig {
    @javax.xml.bind.annotation.XmlElement(name = "stack-id")
    private java.lang.String stackId;

    @javax.xml.bind.annotation.XmlElementWrapper
    @javax.xml.bind.annotation.XmlElement(name = "service")
    @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(javax.xml.bind.annotation.adapters.CollapsedStringAdapter.class)
    private java.util.List<java.lang.String> services;

    @javax.xml.bind.annotation.XmlElementWrapper
    @javax.xml.bind.annotation.XmlElement(name = "role")
    @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(javax.xml.bind.annotation.adapters.CollapsedStringAdapter.class)
    private java.util.Set<java.lang.String> roles;

    public java.lang.String getStackId() {
        return stackId;
    }

    public java.util.List<java.lang.String> getServices() {
        return services;
    }

    public java.util.Set<java.lang.String> getRoles() {
        return roles;
    }
}