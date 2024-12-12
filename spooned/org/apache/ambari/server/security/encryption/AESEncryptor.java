package org.apache.ambari.server.security.encryption;
public class AESEncryptor {
    private static final int ITERATION_COUNT = 65536;

    private static final int KEY_LENGTH = 128;

    private javax.crypto.Cipher ecipher;

    private javax.crypto.Cipher dcipher;

    private javax.crypto.SecretKey secret;

    private byte[] salt = null;

    private char[] passPhrase = null;

    public AESEncryptor(java.lang.String passPhrase) {
        try {
            this.passPhrase = passPhrase.toCharArray();
            salt = new byte[8];
            java.security.SecureRandom rnd = new java.security.SecureRandom();
            rnd.nextBytes(salt);
            javax.crypto.SecretKey tmp = getKeyFromPassword(passPhrase);
            secret = new javax.crypto.spec.SecretKeySpec(tmp.getEncoded(), "AES");
            ecipher = javax.crypto.Cipher.getInstance("AES/CBC/PKCS5Padding");
            ecipher.init(javax.crypto.Cipher.ENCRYPT_MODE, secret);
            dcipher = javax.crypto.Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] iv = ecipher.getParameters().getParameterSpec(javax.crypto.spec.IvParameterSpec.class).getIV();
            dcipher.init(javax.crypto.Cipher.DECRYPT_MODE, secret, new javax.crypto.spec.IvParameterSpec(iv));
        } catch (java.security.NoSuchAlgorithmException | java.security.InvalidAlgorithmParameterException | java.security.spec.InvalidParameterSpecException | java.security.InvalidKeyException | javax.crypto.NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    public javax.crypto.SecretKey getKeyFromPassword(java.lang.String passPhrase) {
        return getKeyFromPassword(passPhrase, salt);
    }

    public javax.crypto.SecretKey getKeyFromPassword(java.lang.String passPhrase, byte[] salt) {
        javax.crypto.SecretKeyFactory factory;
        javax.crypto.SecretKey key = null;
        try {
            factory = javax.crypto.SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            java.security.spec.KeySpec spec = new javax.crypto.spec.PBEKeySpec(passPhrase.toCharArray(), salt, org.apache.ambari.server.security.encryption.AESEncryptor.ITERATION_COUNT, org.apache.ambari.server.security.encryption.AESEncryptor.KEY_LENGTH);
            key = factory.generateSecret(spec);
        } catch (java.security.NoSuchAlgorithmException | java.security.spec.InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return key;
    }

    public org.apache.ambari.server.security.encryption.EncryptionResult encrypt(java.lang.String encrypt) {
        try {
            byte[] bytes = encrypt.getBytes("UTF8");
            org.apache.ambari.server.security.encryption.EncryptionResult atom = encrypt(bytes);
            return atom;
        } catch (java.io.IOException e) {
            throw new java.io.UncheckedIOException(e);
        }
    }

    public org.apache.ambari.server.security.encryption.EncryptionResult encrypt(byte[] plain) {
        try {
            return new org.apache.ambari.server.security.encryption.EncryptionResult(salt, ecipher.getParameters().getParameterSpec(javax.crypto.spec.IvParameterSpec.class).getIV(), ecipher.doFinal(plain));
        } catch (java.security.GeneralSecurityException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    public byte[] decrypt(byte[] salt, byte[] iv, byte[] encrypt) {
        try {
            javax.crypto.SecretKey tmp = getKeyFromPassword(new java.lang.String(passPhrase), salt);
            secret = new javax.crypto.spec.SecretKeySpec(tmp.getEncoded(), "AES");
            dcipher.init(javax.crypto.Cipher.DECRYPT_MODE, secret, new javax.crypto.spec.IvParameterSpec(iv));
            return dcipher.doFinal(encrypt);
        } catch (java.security.GeneralSecurityException e) {
            throw new java.lang.RuntimeException(e);
        }
    }
}