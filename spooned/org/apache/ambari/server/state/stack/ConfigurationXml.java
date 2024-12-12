package org.apache.ambari.server.state.stack;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
@javax.xml.bind.annotation.XmlRootElement(name = "configuration")
public class ConfigurationXml implements org.apache.ambari.server.stack.Validable {
    @javax.xml.bind.annotation.XmlAnyAttribute
    private java.util.Map<javax.xml.namespace.QName, java.lang.String> attributes = new java.util.HashMap<>();

    @javax.xml.bind.annotation.XmlElement(name = "property")
    private java.util.List<org.apache.ambari.server.state.PropertyInfo> properties = new java.util.ArrayList<>();

    @javax.xml.bind.annotation.XmlTransient
    private boolean valid = true;

    @java.lang.Override
    public boolean isValid() {
        return valid;
    }

    @java.lang.Override
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @javax.xml.bind.annotation.XmlTransient
    private java.util.Set<java.lang.String> errorSet = new java.util.HashSet<>();

    @java.lang.Override
    public void addError(java.lang.String error) {
        errorSet.add(error);
    }

    @java.lang.Override
    public java.util.Collection<java.lang.String> getErrors() {
        return errorSet;
    }

    @java.lang.Override
    public void addErrors(java.util.Collection<java.lang.String> errors) {
        this.errorSet.addAll(errors);
    }

    public java.util.List<org.apache.ambari.server.state.PropertyInfo> getProperties() {
        return properties;
    }

    public java.util.Map<javax.xml.namespace.QName, java.lang.String> getAttributes() {
        return attributes;
    }
}