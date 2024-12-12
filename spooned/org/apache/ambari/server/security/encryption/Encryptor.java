package org.apache.ambari.server.security.encryption;
public interface Encryptor<T> {
    static final java.lang.String ENCRYPTED_PROPERTY_PREFIX = "${enc=aes256_hex, value=";

    static final java.lang.String ENCRYPTED_PROPERTY_SCHEME = org.apache.ambari.server.security.encryption.Encryptor.ENCRYPTED_PROPERTY_PREFIX + "%s}";

    void encryptSensitiveData(T encryptible);

    void decryptSensitiveData(T decryptible);

    java.lang.String getEncryptionKey();

    org.apache.ambari.server.security.encryption.Encryptor NONE = new org.apache.ambari.server.security.encryption.Encryptor<java.lang.Object>() {
        @java.lang.Override
        public void encryptSensitiveData(java.lang.Object data) {
        }

        @java.lang.Override
        public void decryptSensitiveData(java.lang.Object decryptible) {
        }

        @java.lang.Override
        public java.lang.String getEncryptionKey() {
            return "";
        }
    };
}