package org.apache.ambari.server.security.encryption;
public class AESEncryptorTest extends junit.framework.TestCase {
    @org.junit.Test
    public void testEncryptionDecryption() throws java.lang.Exception {
        org.apache.ambari.server.security.encryption.AESEncryptor aes = new org.apache.ambari.server.security.encryption.AESEncryptor("AESPassPhrase");
        java.lang.String masterKey = "Thisisusermasterkey";
        org.apache.ambari.server.security.encryption.EncryptionResult res = aes.encrypt(masterKey.getBytes());
        junit.framework.Assert.assertNotNull(res);
        junit.framework.Assert.assertNotNull(res.cipher);
        junit.framework.Assert.assertNotNull(res.iv);
        junit.framework.Assert.assertNotNull(res.salt);
        byte[] descryptedKey = aes.decrypt(res.salt, res.iv, res.cipher);
        junit.framework.Assert.assertEquals(masterKey, new java.lang.String(descryptedKey, "UTF8"));
        junit.framework.Assert.assertEquals(masterKey.getBytes().length, descryptedKey.length);
        junit.framework.Assert.assertEquals(masterKey.getBytes("UTF8").length, new java.lang.String(descryptedKey, "UTF8").toCharArray().length);
    }

    @org.junit.Test
    public void testDecryptionWithDiffEncryptors() throws java.lang.Exception {
        org.apache.ambari.server.security.encryption.AESEncryptor aes1 = new org.apache.ambari.server.security.encryption.AESEncryptor("Test");
        org.apache.ambari.server.security.encryption.AESEncryptor aes2 = new org.apache.ambari.server.security.encryption.AESEncryptor("Test");
        org.apache.ambari.server.security.encryption.EncryptionResult res = aes1.encrypt("mastersecret".getBytes("UTF8"));
        byte[] decrypted = aes1.decrypt(res.salt, res.iv, res.cipher);
        junit.framework.Assert.assertEquals("mastersecret", new java.lang.String(decrypted, "UTF8"));
        decrypted = aes2.decrypt(res.salt, res.iv, res.cipher);
        junit.framework.Assert.assertEquals("mastersecret", new java.lang.String(decrypted, "UTF8"));
    }
}