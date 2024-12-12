package org.apache.ambari.server.security.encryption;
public interface CredentialStore {
    void addCredential(java.lang.String alias, org.apache.ambari.server.security.credential.Credential credential) throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.security.credential.Credential getCredential(java.lang.String alias) throws org.apache.ambari.server.AmbariException;

    void removeCredential(java.lang.String alias) throws org.apache.ambari.server.AmbariException;

    java.util.Set<java.lang.String> listCredentials() throws org.apache.ambari.server.AmbariException;

    boolean containsCredential(java.lang.String alias) throws org.apache.ambari.server.AmbariException;

    void setMasterKeyService(org.apache.ambari.server.security.encryption.MasterKeyService masterKeyService);
}