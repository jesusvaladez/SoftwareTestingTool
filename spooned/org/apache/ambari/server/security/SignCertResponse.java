package org.apache.ambari.server.security;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
@javax.xml.bind.annotation.XmlRootElement
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
@javax.xml.bind.annotation.XmlType(name = "", propOrder = {  })
public class SignCertResponse {
    public static final java.lang.String ERROR_STATUS = "ERROR";

    public static final java.lang.String OK_STATUS = "OK";

    @javax.xml.bind.annotation.XmlElement
    private java.lang.String result;

    @javax.xml.bind.annotation.XmlElement
    private java.lang.String signedCa;

    @javax.xml.bind.annotation.XmlElement
    private java.lang.String message;

    public java.lang.String getResult() {
        return result;
    }

    public void setResult(java.lang.String result) {
        this.result = result;
    }

    public java.lang.String getSignedCa() {
        return signedCa;
    }

    public void setSignedCa(java.lang.String signedCa) {
        this.signedCa = signedCa;
    }

    public java.lang.String getMessage() {
        return message;
    }

    public void setMessage(java.lang.String message) {
        this.message = message;
    }
}