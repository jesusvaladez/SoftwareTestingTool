package org.apache.ambari.server.state;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
public class ServiceLdapInfo {
    @javax.xml.bind.annotation.XmlElement(name = "supported")
    private java.lang.Boolean supported = java.lang.Boolean.FALSE;

    @javax.xml.bind.annotation.XmlElement(name = "ldapEnabledTest")
    private java.lang.String ldapEnabledTest = null;

    public ServiceLdapInfo() {
        this(java.lang.Boolean.FALSE, null);
    }

    public ServiceLdapInfo(java.lang.Boolean supported, java.lang.String ldapEnabledTest) {
        this.supported = supported;
        this.ldapEnabledTest = ldapEnabledTest;
    }

    public java.lang.Boolean getSupported() {
        return supported;
    }

    public boolean isSupported() {
        return java.lang.Boolean.TRUE.equals(supported);
    }

    public void setSupported(java.lang.Boolean supported) {
        this.supported = supported;
    }

    public java.lang.String getLdapEnabledTest() {
        return ldapEnabledTest;
    }

    public void setLdapEnabledTest(java.lang.String ldapEnabledTest) {
        this.ldapEnabledTest = ldapEnabledTest;
    }

    @java.lang.Override
    public int hashCode() {
        return org.apache.commons.lang.builder.HashCodeBuilder.reflectionHashCode(this);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        return org.apache.commons.lang.builder.EqualsBuilder.reflectionEquals(this, obj);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return org.apache.commons.lang.builder.ToStringBuilder.reflectionToString(this);
    }
}