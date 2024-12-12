package org.apache.ambari.server.security.authentication.kerberos;
public class AmbariKerberosAuthenticationProperties {
    private boolean kerberosAuthenticationEnabled = false;

    private java.lang.String spnegoPrincipalName = null;

    private java.lang.String spnegoKeytabFilePath = null;

    private java.util.List<org.apache.ambari.server.security.authorization.UserAuthenticationType> orderedUserTypes = java.util.Collections.emptyList();

    private java.lang.String authToLocalRules;

    public boolean isKerberosAuthenticationEnabled() {
        return kerberosAuthenticationEnabled;
    }

    public void setKerberosAuthenticationEnabled(boolean kerberosAuthenticationEnabled) {
        this.kerberosAuthenticationEnabled = kerberosAuthenticationEnabled;
    }

    public java.lang.String getSpnegoPrincipalName() {
        return spnegoPrincipalName;
    }

    public void setSpnegoPrincipalName(java.lang.String spnegoPrincipalName) {
        this.spnegoPrincipalName = spnegoPrincipalName;
    }

    public java.lang.String getSpnegoKeytabFilePath() {
        return spnegoKeytabFilePath;
    }

    public void setSpnegoKeytabFilePath(java.lang.String spnegoKeytabFilePath) {
        this.spnegoKeytabFilePath = spnegoKeytabFilePath;
    }

    public java.util.List<org.apache.ambari.server.security.authorization.UserAuthenticationType> getOrderedUserTypes() {
        return orderedUserTypes;
    }

    public java.lang.String getAuthToLocalRules() {
        return authToLocalRules;
    }

    public void setAuthToLocalRules(java.lang.String authToLocalRules) {
        this.authToLocalRules = authToLocalRules;
    }
}