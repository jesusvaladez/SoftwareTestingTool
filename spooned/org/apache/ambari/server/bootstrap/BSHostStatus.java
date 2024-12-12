package org.apache.ambari.server.bootstrap;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
@javax.xml.bind.annotation.XmlRootElement
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
@javax.xml.bind.annotation.XmlType(name = "", propOrder = {  })
public class BSHostStatus {
    @javax.xml.bind.annotation.XmlElement
    private java.lang.String hostName;

    @javax.xml.bind.annotation.XmlElement
    private java.lang.String status;

    @javax.xml.bind.annotation.XmlElement
    private java.lang.String statusCode;

    @javax.xml.bind.annotation.XmlElement
    private java.lang.String statusAction;

    @javax.xml.bind.annotation.XmlElement
    private java.lang.String log;

    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    public java.lang.String getStatus() {
        return this.status;
    }

    public void setHostName(java.lang.String hostName) {
        this.hostName = hostName;
    }

    public java.lang.String getHostName() {
        return this.hostName;
    }

    public java.lang.String getLog() {
        return this.log;
    }

    public void setLog(java.lang.String log) {
        this.log = log;
    }

    public java.lang.String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(java.lang.String code) {
        statusCode = code;
    }

    public java.lang.String getStatusAction() {
        return statusAction;
    }

    public void setStatusAction(java.lang.String action) {
        statusAction = action;
    }
}