package org.apache.ambari.server.security;
public enum ClientSecurityType {

    LOCAL("local"),
    LDAP("ldap"),
    PAM("pam");
    private java.lang.String value;

    ClientSecurityType(java.lang.String value) {
        this.value = value;
    }

    public static org.apache.ambari.server.security.ClientSecurityType fromString(java.lang.String value) {
        for (org.apache.ambari.server.security.ClientSecurityType securityType : org.apache.ambari.server.security.ClientSecurityType.values()) {
            if (securityType.toString().equalsIgnoreCase(value)) {
                return securityType;
            }
        }
        return org.apache.ambari.server.security.ClientSecurityType.LOCAL;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return value;
    }
}