package org.apache.ambari.server.state.stack;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
@javax.xml.bind.annotation.XmlRootElement(name = "metainfo")
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public class ServiceMetainfoXml implements org.apache.ambari.server.stack.Validable {
    private java.lang.String schemaVersion;

    @javax.xml.bind.annotation.XmlElementWrapper(name = "services")
    @javax.xml.bind.annotation.XmlElements(@javax.xml.bind.annotation.XmlElement(name = "service"))
    private java.util.List<org.apache.ambari.server.state.ServiceInfo> services;

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

    public java.util.List<org.apache.ambari.server.state.ServiceInfo> getServices() {
        return services;
    }

    public void setServices(java.util.List<org.apache.ambari.server.state.ServiceInfo> services) {
        this.services = services;
    }

    public java.lang.String getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(java.lang.String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }
}