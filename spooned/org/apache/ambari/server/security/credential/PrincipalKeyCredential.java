package org.apache.ambari.server.security.credential;
public class PrincipalKeyCredential implements org.apache.ambari.server.security.credential.Credential {
    private static final java.lang.String VALUE_PREFIX = "PrincipalKeyCredential";

    private java.lang.String principal;

    private char[] key;

    public PrincipalKeyCredential() {
        this(null, ((char[]) (null)));
    }

    public PrincipalKeyCredential(java.lang.String principal, java.lang.String key) {
        this(principal, key == null ? null : key.toCharArray());
    }

    public PrincipalKeyCredential(java.lang.String principal, char[] key) {
        this.principal = principal;
        this.key = key;
    }

    public java.lang.String getPrincipal() {
        return principal;
    }

    public void setPrincipal(java.lang.String principal) {
        this.principal = principal;
    }

    public char[] getKey() {
        return key;
    }

    public void setKey(char[] key) {
        this.key = key;
    }

    @java.lang.Override
    public char[] toValue() {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append("PrincipalKeyCredential");
        builder.append(new com.google.gson.Gson().toJson(this));
        return builder.toString().toCharArray();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (obj == null) {
            return false;
        } else if (this == obj) {
            return true;
        } else if (obj.getClass() == this.getClass()) {
            org.apache.ambari.server.security.credential.PrincipalKeyCredential other = ((org.apache.ambari.server.security.credential.PrincipalKeyCredential) (obj));
            return (this.principal == null ? other.principal == null : this.principal.equals(other.principal)) && (this.key == null ? other.key == null : java.util.Arrays.equals(this.key, other.key));
        } else {
            return false;
        }
    }

    @java.lang.Override
    public int hashCode() {
        return (principal == null ? 0 : principal.hashCode()) + (key == null ? 0 : java.util.Arrays.hashCode(key));
    }

    public static org.apache.ambari.server.security.credential.PrincipalKeyCredential fromValue(java.lang.String value) throws org.apache.ambari.server.security.credential.InvalidCredentialValueException {
        if (org.apache.ambari.server.security.credential.PrincipalKeyCredential.isValidValue(value)) {
            value = value.substring(org.apache.ambari.server.security.credential.PrincipalKeyCredential.VALUE_PREFIX.length());
            try {
                return value.isEmpty() ? null : new com.google.gson.Gson().fromJson(value, org.apache.ambari.server.security.credential.PrincipalKeyCredential.class);
            } catch (com.google.gson.JsonSyntaxException e) {
                throw new org.apache.ambari.server.security.credential.InvalidCredentialValueException("The value does not represent a PrincipalKeyCredential", e);
            }
        } else {
            throw new org.apache.ambari.server.security.credential.InvalidCredentialValueException("The value does not represent a PrincipalKeyCredential");
        }
    }

    public static boolean isValidValue(java.lang.String value) {
        return (value != null) && value.startsWith(org.apache.ambari.server.security.credential.PrincipalKeyCredential.VALUE_PREFIX);
    }
}