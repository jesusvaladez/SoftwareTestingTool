package org.apache.ambari.server.security.credential;
public class CredentialFactory {
    public static org.apache.ambari.server.security.credential.Credential createCredential(char[] value) throws org.apache.ambari.server.security.credential.InvalidCredentialValueException {
        if (value == null) {
            return null;
        } else {
            java.lang.String valueString = java.lang.String.valueOf(value);
            if (org.apache.ambari.server.security.credential.PrincipalKeyCredential.isValidValue(valueString)) {
                return org.apache.ambari.server.security.credential.PrincipalKeyCredential.fromValue(valueString);
            } else {
                return org.apache.ambari.server.security.credential.GenericKeyCredential.fromValue(valueString);
            }
        }
    }
}