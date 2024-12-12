package org.apache.ambari.server.state;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public class SingleSignOnInfo {
    @javax.xml.bind.annotation.XmlElement(name = "supported")
    private java.lang.Boolean supported = java.lang.Boolean.FALSE;

    @javax.xml.bind.annotation.XmlElement(name = "enabledConfiguration")
    private java.lang.String enabledConfiguration = null;

    @javax.xml.bind.annotation.XmlElement(name = "ssoEnabledTest")
    private java.lang.String ssoEnabledTest = null;

    @javax.xml.bind.annotation.XmlElement(name = "kerberosRequired")
    private java.lang.Boolean kerberosRequired = java.lang.Boolean.FALSE;

    public SingleSignOnInfo() {
    }

    public SingleSignOnInfo(java.lang.Boolean supported, java.lang.String enabledConfiguration, java.lang.Boolean kerberosRequired) {
        this.supported = supported;
        this.enabledConfiguration = enabledConfiguration;
        this.kerberosRequired = kerberosRequired;
    }

    public boolean isSupported() {
        return java.lang.Boolean.TRUE.equals(supported);
    }

    public java.lang.Boolean getSupported() {
        return supported;
    }

    public void setSupported(java.lang.Boolean supported) {
        this.supported = supported;
    }

    public java.lang.String getEnabledConfiguration() {
        return enabledConfiguration;
    }

    public void setEnabledConfiguration(java.lang.String enabledConfiguration) {
        this.enabledConfiguration = enabledConfiguration;
    }

    public java.lang.String getSsoEnabledTest() {
        return ssoEnabledTest;
    }

    public void setSsoEnabledTest(java.lang.String ssoEnabledTest) {
        this.ssoEnabledTest = ssoEnabledTest;
    }

    public boolean isKerberosRequired() {
        return java.lang.Boolean.TRUE.equals(kerberosRequired);
    }

    public void setKerberosRequired(java.lang.Boolean kerberosRequired) {
        this.kerberosRequired = kerberosRequired;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this).add("supported", supported).add("enabledConfiguration", enabledConfiguration).add("ssoEnabledTest", ssoEnabledTest).add("kerberosRequired", kerberosRequired).toString();
    }
}