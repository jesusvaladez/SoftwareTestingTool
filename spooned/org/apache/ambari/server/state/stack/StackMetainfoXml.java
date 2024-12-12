package org.apache.ambari.server.state.stack;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
@javax.xml.bind.annotation.XmlRootElement(name = "metainfo")
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public class StackMetainfoXml implements org.apache.ambari.server.stack.Validable {
    public java.lang.String getMinJdk() {
        return minJdk;
    }

    public java.lang.String getMaxJdk() {
        return maxJdk;
    }

    @javax.xml.bind.annotation.XmlElement(name = "minJdk")
    private java.lang.String minJdk = null;

    @javax.xml.bind.annotation.XmlElement(name = "maxJdk")
    private java.lang.String maxJdk = null;

    @javax.xml.bind.annotation.XmlElement(name = "extends")
    private java.lang.String extendsVersion = null;

    @javax.xml.bind.annotation.XmlElement(name = "versions")
    private org.apache.ambari.server.state.stack.StackMetainfoXml.Version version = new org.apache.ambari.server.state.stack.StackMetainfoXml.Version();

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

    public java.lang.String getExtends() {
        return extendsVersion;
    }

    public org.apache.ambari.server.state.stack.StackMetainfoXml.Version getVersion() {
        return version;
    }

    public void setVersion(org.apache.ambari.server.state.stack.StackMetainfoXml.Version version) {
        this.version = version;
    }

    public void setMinJdk(java.lang.String minJdk) {
        this.minJdk = minJdk;
    }

    public void setMaxJdk(java.lang.String maxJdk) {
        this.maxJdk = maxJdk;
    }

    @javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
    public static class Version {
        public Version() {
        }

        private boolean active = false;

        private java.lang.String stackReleaseVersion;

        public boolean isActive() {
            return active;
        }

        public java.lang.String getReleaseVersion() {
            return stackReleaseVersion;
        }

        public void setActive(boolean active) {
            this.active = active;
        }
    }
}