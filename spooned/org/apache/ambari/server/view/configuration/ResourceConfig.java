package org.apache.ambari.server.view.configuration;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public class ResourceConfig {
    public static final java.lang.String EXTERNAL_RESOURCE_PLURAL_NAME = "resources";

    private java.lang.String name;

    @javax.xml.bind.annotation.XmlElement(name = "plural-name")
    private java.lang.String pluralName;

    @javax.xml.bind.annotation.XmlElement(name = "id-property")
    private java.lang.String idProperty;

    @javax.xml.bind.annotation.XmlElement(name = "sub-resource-name")
    private java.util.List<java.lang.String> subResourceNames;

    @javax.xml.bind.annotation.XmlElement(name = "provider-class")
    private java.lang.String provider;

    private java.lang.Class<? extends org.apache.ambari.view.ResourceProvider> providerClass = null;

    @javax.xml.bind.annotation.XmlElement(name = "service-class")
    private java.lang.String service;

    private java.lang.Class<?> serviceClass = null;

    @javax.xml.bind.annotation.XmlElement(name = "resource-class")
    private java.lang.String resource;

    private java.lang.Class<?> resourceClass = null;

    public java.lang.String getName() {
        return name;
    }

    public java.lang.String getPluralName() {
        return pluralName;
    }

    public java.lang.String getIdProperty() {
        return idProperty;
    }

    public java.util.List<java.lang.String> getSubResourceNames() {
        return subResourceNames == null ? java.util.Collections.emptyList() : subResourceNames;
    }

    public java.lang.String getProvider() {
        return provider;
    }

    public java.lang.String getService() {
        return service;
    }

    public java.lang.String getResource() {
        return resource;
    }

    public java.lang.Class<? extends org.apache.ambari.view.ResourceProvider> getProviderClass(java.lang.ClassLoader cl) throws java.lang.ClassNotFoundException {
        if (providerClass == null) {
            providerClass = cl.loadClass(provider).asSubclass(org.apache.ambari.view.ResourceProvider.class);
        }
        return providerClass;
    }

    public java.lang.Class<?> getServiceClass(java.lang.ClassLoader cl) throws java.lang.ClassNotFoundException {
        if (serviceClass == null) {
            serviceClass = cl.loadClass(service);
        }
        return serviceClass;
    }

    public java.lang.Class<?> getResourceClass(java.lang.ClassLoader cl) throws java.lang.ClassNotFoundException {
        if (resourceClass == null) {
            resourceClass = cl.loadClass(resource);
        }
        return resourceClass;
    }

    public boolean isExternal() {
        return (resource == null) || (provider == null);
    }
}