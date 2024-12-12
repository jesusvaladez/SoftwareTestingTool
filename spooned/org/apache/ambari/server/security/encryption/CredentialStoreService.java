package org.apache.ambari.server.security.encryption;
public interface CredentialStoreService {
    void setCredential(java.lang.String clusterName, java.lang.String alias, org.apache.ambari.server.security.credential.Credential credential, org.apache.ambari.server.security.encryption.CredentialStoreType credentialStoreType) throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.security.credential.Credential getCredential(java.lang.String clusterName, java.lang.String alias) throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.security.credential.Credential getCredential(java.lang.String clusterName, java.lang.String alias, org.apache.ambari.server.security.encryption.CredentialStoreType credentialStoreType) throws org.apache.ambari.server.AmbariException;

    void removeCredential(java.lang.String clusterName, java.lang.String alias) throws org.apache.ambari.server.AmbariException;

    void removeCredential(java.lang.String clusterName, java.lang.String alias, org.apache.ambari.server.security.encryption.CredentialStoreType credentialStoreType) throws org.apache.ambari.server.AmbariException;

    boolean containsCredential(java.lang.String clusterName, java.lang.String alias) throws org.apache.ambari.server.AmbariException;

    boolean containsCredential(java.lang.String clusterName, java.lang.String alias, org.apache.ambari.server.security.encryption.CredentialStoreType credentialStoreType) throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.security.encryption.CredentialStoreType getCredentialStoreType(java.lang.String clusterName, java.lang.String alias) throws org.apache.ambari.server.AmbariException;

    java.util.Map<java.lang.String, org.apache.ambari.server.security.encryption.CredentialStoreType> listCredentials(java.lang.String clusterName) throws org.apache.ambari.server.AmbariException;

    boolean isInitialized();

    boolean isInitialized(org.apache.ambari.server.security.encryption.CredentialStoreType credentialStoreType);
}