package org.apache.ambari.server.security.encryption;
public class EncryptionResult {
    public byte[] salt;

    public byte[] iv;

    public byte[] cipher;

    public EncryptionResult(byte[] salt, byte[] iv, byte[] cipher) {
        this.salt = salt;
        this.iv = iv;
        this.cipher = cipher;
    }
}