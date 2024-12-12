package org.apache.ambari.server.security;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
@javax.xml.bind.annotation.XmlRootElement
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
@javax.xml.bind.annotation.XmlType(name = "", propOrder = {  })
public class SignMessage {
    @javax.xml.bind.annotation.XmlElement
    private java.lang.String csr;

    @javax.xml.bind.annotation.XmlElement
    private java.lang.String passphrase;

    public java.lang.String getCsr() {
        return csr;
    }

    public void setCsr(java.lang.String csr) {
        this.csr = csr;
    }

    public java.lang.String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(java.lang.String passphrase) {
        this.passphrase = passphrase;
    }
}