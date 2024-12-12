package org.apache.ambari.server.security.encryption;
public interface EncryptionService {
    java.lang.String encrypt(java.lang.String toBeEncrypted);

    java.lang.String encrypt(java.lang.String toBeEncrypted, org.apache.ambari.server.utils.TextEncoding textEncoding);

    java.lang.String encrypt(java.lang.String toBeEncrypted, java.lang.String key);

    java.lang.String encrypt(java.lang.String toBeEncrypted, java.lang.String key, org.apache.ambari.server.utils.TextEncoding textEncoding);

    java.lang.String getAmbariMasterKey();

    java.lang.String decrypt(java.lang.String toBeDecrypted);

    java.lang.String decrypt(java.lang.String toBeDecrypted, org.apache.ambari.server.utils.TextEncoding textEncoding);

    java.lang.String decrypt(java.lang.String toBeDecrypted, java.lang.String key);

    java.lang.String decrypt(java.lang.String toBeDecrypted, java.lang.String key, org.apache.ambari.server.utils.TextEncoding textEncoding);
}