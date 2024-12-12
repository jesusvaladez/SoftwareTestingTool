package org.apache.ambari.server.security.credential;
public class GenericKeyCredential implements org.apache.ambari.server.security.credential.Credential {
    private char[] key = null;

    public GenericKeyCredential() {
    }

    public GenericKeyCredential(char[] key) {
        this.key = key;
    }

    public char[] getKey() {
        return key;
    }

    public void setKey(char[] key) {
        this.key = key;
    }

    @java.lang.Override
    public char[] toValue() {
        return this.key;
    }

    public static org.apache.ambari.server.security.credential.GenericKeyCredential fromValue(java.lang.String value) throws org.apache.ambari.server.security.credential.InvalidCredentialValueException {
        return new org.apache.ambari.server.security.credential.GenericKeyCredential(value == null ? null : value.toCharArray());
    }
}