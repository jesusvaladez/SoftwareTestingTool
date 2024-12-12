package org.apache.ambari.server.bootstrap;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
@javax.xml.bind.annotation.XmlRootElement
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
@javax.xml.bind.annotation.XmlType(name = "", propOrder = {  })
public class SshHostInfo {
    @javax.xml.bind.annotation.XmlElement
    private java.lang.String sshKey;

    @javax.xml.bind.annotation.XmlElement
    private java.util.List<java.lang.String> hosts = new java.util.ArrayList<>();

    @javax.xml.bind.annotation.XmlElement
    private boolean verbose = false;

    @javax.xml.bind.annotation.XmlElement
    private java.lang.String user;

    @javax.xml.bind.annotation.XmlElement
    private java.lang.String sshPort;

    @javax.xml.bind.annotation.XmlElement
    private java.lang.String password;

    @javax.xml.bind.annotation.XmlElement
    private java.lang.String userRunAs;

    public java.lang.String getSshKey() {
        return sshKey;
    }

    public void setSshKey(java.lang.String sshKey) {
        this.sshKey = sshKey;
    }

    public void setHosts(java.util.List<java.lang.String> hosts) {
        this.hosts = hosts;
    }

    public java.util.List<java.lang.String> getHosts() {
        return this.hosts;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    public java.lang.String getUser() {
        return user;
    }

    public void setUser(java.lang.String user) {
        this.user = user;
    }

    public java.lang.String getSshPort() {
        return sshPort;
    }

    public void setSshPort(java.lang.String sshPort) {
        this.sshPort = sshPort;
    }

    public java.lang.String getPassword() {
        return password;
    }

    public void setPassword(java.lang.String password) {
        this.password = password;
    }

    public java.lang.String getUserRunAs() {
        return userRunAs;
    }

    public void setUserRunAs(java.lang.String userRunAs) {
        this.userRunAs = userRunAs;
    }

    public java.lang.String hostListAsString() {
        java.lang.StringBuilder ret = new java.lang.StringBuilder();
        if (this.hosts == null) {
            return "";
        }
        for (java.lang.String host : this.hosts) {
            ret.append(host).append(":");
        }
        return ret.toString();
    }
}