package org.apache.ambari.server.security.authentication.kerberos;
public class AmbariProxyUserKerberosDetailsImpl extends org.apache.ambari.server.security.authentication.AmbariProxyUserDetailsImpl {
    private final java.lang.String principalName;

    public AmbariProxyUserKerberosDetailsImpl(java.lang.String principalName, java.lang.String localUsername) {
        super(localUsername, org.apache.ambari.server.security.authorization.UserAuthenticationType.KERBEROS);
        this.principalName = principalName;
    }

    public java.lang.String getPrincipalName() {
        return principalName;
    }
}