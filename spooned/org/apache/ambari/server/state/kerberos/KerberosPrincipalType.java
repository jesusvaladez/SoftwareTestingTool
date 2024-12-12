package org.apache.ambari.server.state.kerberos;
public enum KerberosPrincipalType {

    USER,
    SERVICE;
    public static org.apache.ambari.server.state.kerberos.KerberosPrincipalType translate(java.lang.String string) {
        if (string == null)
            return null;
        else {
            string = string.trim();
            if (string.isEmpty())
                return null;
            else {
                return org.apache.ambari.server.state.kerberos.KerberosPrincipalType.valueOf(string.toUpperCase());
            }
        }
    }

    public static java.lang.String translate(org.apache.ambari.server.state.kerberos.KerberosPrincipalType type) {
        return type == null ? null : type.name().toLowerCase();
    }
}