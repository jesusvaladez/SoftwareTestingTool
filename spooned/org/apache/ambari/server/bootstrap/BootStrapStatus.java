package org.apache.ambari.server.bootstrap;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
@javax.xml.bind.annotation.XmlRootElement
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
@javax.xml.bind.annotation.XmlType(name = "", propOrder = {  })
public class BootStrapStatus {
    @javax.xml.bind.annotation.XmlType(name = "status")
    @javax.xml.bind.annotation.XmlEnum
    public enum BSStat {

        RUNNING,
        SUCCESS,
        ERROR;}

    @javax.xml.bind.annotation.XmlElement
    private org.apache.ambari.server.bootstrap.BootStrapStatus.BSStat status;

    @javax.xml.bind.annotation.XmlElement
    private java.util.List<org.apache.ambari.server.bootstrap.BSHostStatus> hostsStatus;

    @javax.xml.bind.annotation.XmlElement
    private java.lang.String log;

    public synchronized void setStatus(org.apache.ambari.server.bootstrap.BootStrapStatus.BSStat status) {
        this.status = status;
    }

    public synchronized org.apache.ambari.server.bootstrap.BootStrapStatus.BSStat getStatus() {
        return this.status;
    }

    public synchronized void setHostsStatus(java.util.List<org.apache.ambari.server.bootstrap.BSHostStatus> hostsStatus) {
        this.hostsStatus = hostsStatus;
    }

    public synchronized java.util.List<org.apache.ambari.server.bootstrap.BSHostStatus> getHostsStatus() {
        return this.hostsStatus;
    }

    public synchronized void setLog(java.lang.String log) {
        this.log = log;
    }

    public synchronized java.lang.String getLog() {
        return this.log;
    }
}