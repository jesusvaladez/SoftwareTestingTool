package org.apache.ambari.server.controller;
public class KerberosDetails {
    private java.lang.String defaultRealm;

    private org.apache.ambari.server.serveraction.kerberos.KDCType kdcType;

    private java.util.Map<java.lang.String, java.lang.String> kerberosEnvProperties;

    private org.apache.ambari.server.state.SecurityType securityType;

    private java.lang.Boolean manageIdentities;

    public void setDefaultRealm(java.lang.String defaultRealm) {
        this.defaultRealm = defaultRealm;
    }

    public java.lang.String getDefaultRealm() {
        return defaultRealm;
    }

    public void setKdcType(org.apache.ambari.server.serveraction.kerberos.KDCType kdcType) {
        this.kdcType = kdcType;
    }

    public org.apache.ambari.server.serveraction.kerberos.KDCType getKdcType() {
        return kdcType;
    }

    public void setKerberosEnvProperties(java.util.Map<java.lang.String, java.lang.String> kerberosEnvProperties) {
        this.kerberosEnvProperties = kerberosEnvProperties;
    }

    public java.util.Map<java.lang.String, java.lang.String> getKerberosEnvProperties() {
        return kerberosEnvProperties;
    }

    public void setSecurityType(org.apache.ambari.server.state.SecurityType securityType) {
        this.securityType = securityType;
    }

    public org.apache.ambari.server.state.SecurityType getSecurityType() {
        return securityType;
    }

    public boolean manageIdentities() {
        if (manageIdentities == null) {
            return (kerberosEnvProperties == null) || (!"false".equalsIgnoreCase(kerberosEnvProperties.get(org.apache.ambari.server.controller.KerberosHelper.MANAGE_IDENTITIES)));
        } else {
            return manageIdentities;
        }
    }

    public void setManageIdentities(java.lang.Boolean manageIdentities) {
        this.manageIdentities = manageIdentities;
    }

    public boolean createAmbariPrincipal() {
        return (kerberosEnvProperties == null) || (!"false".equalsIgnoreCase(kerberosEnvProperties.get(org.apache.ambari.server.controller.KerberosHelper.CREATE_AMBARI_PRINCIPAL)));
    }

    public java.lang.String getPreconfigureServices() {
        return kerberosEnvProperties == null ? "" : kerberosEnvProperties.get(org.apache.ambari.server.controller.KerberosHelper.PRECONFIGURE_SERVICES);
    }
}