package org.apache.ambari.server.security.encryption;
public class CredentialProviderTest {
    @org.junit.Rule
    public org.junit.rules.TemporaryFolder tmpFolder = new org.junit.rules.TemporaryFolder();

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        tmpFolder.create();
    }

    private void createMasterKey() throws java.io.IOException {
        java.io.File f = tmpFolder.newFile(org.apache.ambari.server.configuration.Configuration.MASTER_KEY_FILENAME_DEFAULT);
        junit.framework.Assert.assertTrue(new org.apache.ambari.server.security.encryption.MasterKeyServiceImpl("dummyKey").initializeMasterKeyFile(f, "blahblah!"));
        org.apache.ambari.server.security.encryption.MasterKeyService ms = new org.apache.ambari.server.security.encryption.MasterKeyServiceImpl(f);
        if (!ms.isMasterKeyInitialized()) {
            throw new java.lang.ExceptionInInitializerError("Cannot create master key.");
        }
    }

    @org.junit.Test
    public void testInitialization() throws java.lang.Exception {
        org.apache.ambari.server.security.encryption.CredentialProvider cr;
        java.io.File msFile = tmpFolder.newFile(org.apache.ambari.server.configuration.Configuration.MASTER_KEY_FILENAME_DEFAULT);
        java.io.File mksFile = tmpFolder.newFile(org.apache.ambari.server.configuration.Configuration.MASTER_KEYSTORE_FILENAME_DEFAULT);
        org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration(new java.util.Properties());
        configuration.setProperty(org.apache.ambari.server.configuration.Configuration.MASTER_KEY_LOCATION, msFile.getParent());
        configuration.setProperty(org.apache.ambari.server.configuration.Configuration.MASTER_KEYSTORE_LOCATION, mksFile.getParent());
        createMasterKey();
        cr = new org.apache.ambari.server.security.encryption.CredentialProvider(null, configuration);
        junit.framework.Assert.assertNotNull(cr);
        junit.framework.Assert.assertNotNull(cr.getKeystoreService());
        msFile.delete();
        mksFile.delete();
        cr = new org.apache.ambari.server.security.encryption.CredentialProvider("blahblah!", configuration);
        junit.framework.Assert.assertNotNull(cr);
        junit.framework.Assert.assertNotNull(cr.getKeystoreService());
    }

    @org.junit.Test
    public void testIsAliasString() {
        java.lang.String test = "cassablanca";
        junit.framework.Assert.assertFalse(org.apache.ambari.server.security.encryption.CredentialProvider.isAliasString(test));
        test = "${}";
        junit.framework.Assert.assertFalse(org.apache.ambari.server.security.encryption.CredentialProvider.isAliasString(test));
        test = "{}";
        junit.framework.Assert.assertFalse(org.apache.ambari.server.security.encryption.CredentialProvider.isAliasString(test));
        test = "{cassablanca}";
        junit.framework.Assert.assertFalse(org.apache.ambari.server.security.encryption.CredentialProvider.isAliasString(test));
        test = "${cassablanca}";
        junit.framework.Assert.assertFalse(org.apache.ambari.server.security.encryption.CredentialProvider.isAliasString(test));
        test = "${alias=cassablanca}";
        junit.framework.Assert.assertTrue(org.apache.ambari.server.security.encryption.CredentialProvider.isAliasString(test));
    }

    @org.junit.Test
    public void testCredentialStore() throws java.lang.Exception {
        java.io.File msFile = tmpFolder.newFile(org.apache.ambari.server.configuration.Configuration.MASTER_KEY_FILENAME_DEFAULT);
        java.io.File mksFile = tmpFolder.newFile(org.apache.ambari.server.configuration.Configuration.MASTER_KEYSTORE_FILENAME_DEFAULT);
        org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration(new java.util.Properties());
        configuration.setProperty(org.apache.ambari.server.configuration.Configuration.MASTER_KEY_LOCATION, msFile.getParent());
        configuration.setProperty(org.apache.ambari.server.configuration.Configuration.MASTER_KEYSTORE_LOCATION, mksFile.getParent());
        createMasterKey();
        org.apache.ambari.server.security.encryption.CredentialProvider cr = new org.apache.ambari.server.security.encryption.CredentialProvider(null, configuration);
        junit.framework.Assert.assertNotNull(cr);
        junit.framework.Assert.assertNotNull(cr.getKeystoreService());
        try {
            cr.addAliasToCredentialStore("", "xyz");
            junit.framework.Assert.fail("Expected an exception");
        } catch (java.lang.Throwable t) {
            junit.framework.Assert.assertTrue(t instanceof java.lang.IllegalArgumentException);
        }
        try {
            cr.addAliasToCredentialStore("xyz", null);
            junit.framework.Assert.fail("Expected an exception");
        } catch (java.lang.Throwable t) {
            junit.framework.Assert.assertTrue(t instanceof java.lang.IllegalArgumentException);
        }
        cr.addAliasToCredentialStore("myalias", "mypassword");
        junit.framework.Assert.assertEquals("mypassword", new java.lang.String(cr.getPasswordForAlias("myalias")));
    }

    @org.junit.After
    public void tearDown() throws java.lang.Exception {
        tmpFolder.delete();
    }
}