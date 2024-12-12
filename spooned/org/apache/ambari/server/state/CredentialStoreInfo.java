package org.apache.ambari.server.state;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public class CredentialStoreInfo {
    @javax.xml.bind.annotation.XmlElement(name = "supported")
    private java.lang.Boolean supported = null;

    @javax.xml.bind.annotation.XmlElement(name = "required")
    private java.lang.Boolean required = null;

    @javax.xml.bind.annotation.XmlElement(name = "enabled")
    private java.lang.Boolean enabled = null;

    public CredentialStoreInfo() {
    }

    public CredentialStoreInfo(java.lang.Boolean supported, java.lang.Boolean enabled, java.lang.Boolean required) {
        this.supported = supported;
        this.enabled = enabled;
        this.required = required;
    }

    public java.lang.Boolean isSupported() {
        return supported;
    }

    public void setSupported(java.lang.Boolean supported) {
        this.supported = supported;
    }

    public java.lang.Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(java.lang.Boolean enabled) {
        this.enabled = enabled;
    }

    public java.lang.Boolean isRequired() {
        return required;
    }

    public void setRequired(java.lang.Boolean required) {
        this.required = required;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((((("CredentialStoreInfo{" + "supported=") + supported) + ", required=") + required) + ", enabled=") + enabled) + '}';
    }
}