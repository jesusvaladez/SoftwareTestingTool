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
public class BSResponse {
    @javax.xml.bind.annotation.XmlType(name = "status")
    @javax.xml.bind.annotation.XmlEnum
    public enum BSRunStat {

        OK,
        ERROR;}

    @javax.xml.bind.annotation.XmlElement
    private org.apache.ambari.server.bootstrap.BSResponse.BSRunStat status;

    @javax.xml.bind.annotation.XmlElement
    private java.lang.String log;

    @javax.xml.bind.annotation.XmlElement
    private long requestId;

    public long getRequestId() {
        return this.requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public org.apache.ambari.server.bootstrap.BSResponse.BSRunStat getStatus() {
        return this.status;
    }

    public void setStatus(org.apache.ambari.server.bootstrap.BSResponse.BSRunStat status) {
        this.status = status;
    }

    public java.lang.String getLog() {
        return this.log;
    }

    public void setLog(java.lang.String log) {
        this.log = log;
    }
}