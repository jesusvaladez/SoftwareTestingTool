package org.apache.ambari.server.security.encryption;
public class CredentialStoreTest {
    @org.junit.Rule
    public org.junit.rules.TemporaryFolder tmpFolder = new org.junit.rules.TemporaryFolder();

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        tmpFolder.create();
    }

    @org.junit.After
    public void cleanUp() throws java.lang.Exception {
        tmpFolder.delete();
    }

    @org.junit.Test
    public void testFileBasedCredentialStoreService_AddCredentialToStoreWithPersistMaster() throws java.lang.Exception {
        addCredentialToStoreWithPersistMasterTest(new org.apache.ambari.server.security.encryption.CredentialStoreTest.FileBasedCredentialStoreServiceFactory(), new org.apache.ambari.server.security.encryption.CredentialStoreTest.DefaultMasterKeyServiceFactory());
    }

    @org.junit.Test
    public void testFileBasedCredentialStoreService_AddCredentialToStore() throws java.lang.Exception {
        addCredentialToStoreTest(new org.apache.ambari.server.security.encryption.CredentialStoreTest.FileBasedCredentialStoreServiceFactory(), new org.apache.ambari.server.security.encryption.CredentialStoreTest.DefaultMasterKeyServiceFactory());
    }

    @org.junit.Test
    public void testFileBasedCredentialStoreService_GetCredential() throws java.lang.Exception {
        getCredentialTest(new org.apache.ambari.server.security.encryption.CredentialStoreTest.FileBasedCredentialStoreServiceFactory(), new org.apache.ambari.server.security.encryption.CredentialStoreTest.DefaultMasterKeyServiceFactory());
    }

    @org.junit.Test
    public void testFileBasedCredentialStoreService_RemoveCredential() throws java.lang.Exception {
        removeCredentialTest(new org.apache.ambari.server.security.encryption.CredentialStoreTest.FileBasedCredentialStoreServiceFactory(), new org.apache.ambari.server.security.encryption.CredentialStoreTest.DefaultMasterKeyServiceFactory());
    }

    @org.junit.Test
    public void testInMemoryCredentialStoreService_AddCredentialToStoreWithPersistMaster() throws java.lang.Exception {
        addCredentialToStoreWithPersistMasterTest(new org.apache.ambari.server.security.encryption.CredentialStoreTest.InMemoryCredentialStoreServiceFactory(), new org.apache.ambari.server.security.encryption.CredentialStoreTest.DefaultMasterKeyServiceFactory());
    }

    @org.junit.Test
    public void testInMemoryCredentialStoreService_AddCredentialToStore() throws java.lang.Exception {
        addCredentialToStoreTest(new org.apache.ambari.server.security.encryption.CredentialStoreTest.InMemoryCredentialStoreServiceFactory(), new org.apache.ambari.server.security.encryption.CredentialStoreTest.DefaultMasterKeyServiceFactory());
    }

    @org.junit.Test
    public void testInMemoryCredentialStoreService_GetCredential() throws java.lang.Exception {
        getCredentialTest(new org.apache.ambari.server.security.encryption.CredentialStoreTest.InMemoryCredentialStoreServiceFactory(), new org.apache.ambari.server.security.encryption.CredentialStoreTest.DefaultMasterKeyServiceFactory());
    }

    @org.junit.Test
    public void testInMemoryCredentialStoreService_RemoveCredential() throws java.lang.Exception {
        removeCredentialTest(new org.apache.ambari.server.security.encryption.CredentialStoreTest.InMemoryCredentialStoreServiceFactory(), new org.apache.ambari.server.security.encryption.CredentialStoreTest.DefaultMasterKeyServiceFactory());
    }

    @org.junit.Test
    public void testInMemoryCredentialStoreService_CredentialExpired() throws java.lang.Exception {
        getExpiredCredentialTest(new org.apache.ambari.server.security.encryption.CredentialStoreTest.InMemoryCredentialStoreServiceFactory(), new org.apache.ambari.server.security.encryption.CredentialStoreTest.DefaultMasterKeyServiceFactory());
    }

    private void addCredentialToStoreWithPersistMasterTest(org.apache.ambari.server.security.encryption.CredentialStoreTest.CredentialStoreServiceFactory credentialStoreServiceFactory, org.apache.ambari.server.security.encryption.CredentialStoreTest.MasterKeyServiceFactory masterKeyServiceFactory) throws java.lang.Exception {
        java.io.File directory = tmpFolder.getRoot();
        java.lang.String masterKey = "ThisIsSomeSecretPassPhrase1234";
        java.io.File masterKeyFile = new java.io.File(directory, "master");
        org.apache.ambari.server.security.encryption.MasterKeyService masterKeyService = masterKeyServiceFactory.createPersisted(masterKeyFile, masterKey);
        org.apache.ambari.server.security.encryption.CredentialStore credentialStore = credentialStoreServiceFactory.create(directory, masterKeyService);
        java.lang.String password = "mypassword";
        credentialStore.addCredential("myalias", new org.apache.ambari.server.security.credential.GenericKeyCredential(password.toCharArray()));
        org.apache.ambari.server.security.credential.Credential credential = credentialStore.getCredential("myalias");
        junit.framework.Assert.assertEquals(password, new java.lang.String(credential.toValue()));
        junit.framework.Assert.assertTrue(masterKeyFile.exists());
    }

    private void addCredentialToStoreTest(org.apache.ambari.server.security.encryption.CredentialStoreTest.CredentialStoreServiceFactory credentialStoreServiceFactory, org.apache.ambari.server.security.encryption.CredentialStoreTest.MasterKeyServiceFactory masterKeyServiceFactory) throws java.lang.Exception {
        java.io.File directory = tmpFolder.getRoot();
        java.lang.String masterKey = "ThisIsSomeSecretPassPhrase1234";
        java.io.File masterKeyFile = new java.io.File(directory, "master");
        org.apache.ambari.server.security.encryption.MasterKeyService masterKeyService = masterKeyServiceFactory.create(masterKey);
        org.apache.ambari.server.security.encryption.CredentialStore credentialStore = credentialStoreServiceFactory.create(directory, masterKeyService);
        java.lang.String password = "mypassword";
        credentialStore.addCredential("password", new org.apache.ambari.server.security.credential.GenericKeyCredential(password.toCharArray()));
        org.apache.ambari.server.security.credential.Credential credential = credentialStore.getCredential("password");
        junit.framework.Assert.assertEquals(password, new java.lang.String(credential.toValue()));
        credentialStore.addCredential("null_password", null);
        junit.framework.Assert.assertNull(credentialStore.getCredential("null_password"));
        credentialStore.addCredential("empty_password", new org.apache.ambari.server.security.credential.GenericKeyCredential(new char[0]));
        junit.framework.Assert.assertNull(credentialStore.getCredential("empty_password"));
        junit.framework.Assert.assertFalse(masterKeyFile.exists());
    }

    private void getCredentialTest(org.apache.ambari.server.security.encryption.CredentialStoreTest.CredentialStoreServiceFactory credentialStoreServiceFactory, org.apache.ambari.server.security.encryption.CredentialStoreTest.MasterKeyServiceFactory masterKeyServiceFactory) throws java.lang.Exception {
        java.io.File directory = tmpFolder.getRoot();
        java.lang.String masterKey = "ThisIsSomeSecretPassPhrase1234";
        org.apache.ambari.server.security.encryption.MasterKeyService masterKeyService = masterKeyServiceFactory.create(masterKey);
        org.apache.ambari.server.security.encryption.CredentialStore credentialStore = credentialStoreServiceFactory.create(directory, masterKeyService);
        junit.framework.Assert.assertNull(credentialStore.getCredential(""));
        junit.framework.Assert.assertNull(credentialStore.getCredential(null));
        java.lang.String password = "mypassword";
        credentialStore.addCredential("myalias", new org.apache.ambari.server.security.credential.GenericKeyCredential(password.toCharArray()));
        org.apache.ambari.server.security.credential.Credential credential = credentialStore.getCredential("myalias");
        junit.framework.Assert.assertEquals(password, new java.lang.String(credential.toValue()));
        junit.framework.Assert.assertNull(credentialStore.getCredential("does_not_exist"));
    }

    private void getExpiredCredentialTest(org.apache.ambari.server.security.encryption.CredentialStoreTest.CredentialStoreServiceFactory credentialStoreServiceFactory, org.apache.ambari.server.security.encryption.CredentialStoreTest.MasterKeyServiceFactory masterKeyServiceFactory) throws java.lang.Exception {
        java.io.File directory = tmpFolder.getRoot();
        java.lang.String masterKey = "ThisIsSomeSecretPassPhrase1234";
        org.apache.ambari.server.security.encryption.MasterKeyService masterKeyService = masterKeyServiceFactory.create(masterKey);
        org.apache.ambari.server.security.encryption.CredentialStore credentialStore = credentialStoreServiceFactory.create(directory, masterKeyService);
        org.apache.ambari.server.security.encryption.CredentialStoreTest.TestTicker ticker = new org.apache.ambari.server.security.encryption.CredentialStoreTest.TestTicker(0);
        java.lang.reflect.Field cacheFiled = org.apache.ambari.server.security.encryption.InMemoryCredentialStore.class.getDeclaredField("cache");
        cacheFiled.setAccessible(true);
        java.lang.Object cache = cacheFiled.get(credentialStore);
        java.lang.Class localManualCacheClass = java.lang.Class.forName("com.google.common.cache.LocalCache$LocalManualCache");
        java.lang.reflect.Field localCacheField = localManualCacheClass.getDeclaredField("localCache");
        localCacheField.setAccessible(true);
        java.lang.Class localCacheClass = java.lang.Class.forName("com.google.common.cache.LocalCache");
        java.lang.Object localCache = localCacheField.get(cache);
        java.lang.reflect.Field tickerField = localCacheClass.getDeclaredField("ticker");
        tickerField.setAccessible(true);
        tickerField.set(localCache, ticker);
        java.lang.String password = "mypassword";
        credentialStore.addCredential("myalias", new org.apache.ambari.server.security.credential.GenericKeyCredential(password.toCharArray()));
        junit.framework.Assert.assertEquals(password, new java.lang.String(credentialStore.getCredential("myalias").toValue()));
        ticker.setCurrentNanos((250 * 1000) * 1000);
        junit.framework.Assert.assertEquals(password, new java.lang.String(credentialStore.getCredential("myalias").toValue()));
        ticker.setCurrentNanos((550 * 1000) * 1000);
        junit.framework.Assert.assertNull(password, credentialStore.getCredential("myalias"));
    }

    private void removeCredentialTest(org.apache.ambari.server.security.encryption.CredentialStoreTest.CredentialStoreServiceFactory credentialStoreServiceFactory, org.apache.ambari.server.security.encryption.CredentialStoreTest.MasterKeyServiceFactory masterKeyServiceFactory) throws java.lang.Exception {
        java.io.File directory = tmpFolder.getRoot();
        java.lang.String masterKey = "ThisIsSomeSecretPassPhrase1234";
        org.apache.ambari.server.security.encryption.MasterKeyService masterKeyService = masterKeyServiceFactory.create(masterKey);
        org.apache.ambari.server.security.encryption.CredentialStore credentialStore = credentialStoreServiceFactory.create(directory, masterKeyService);
        java.lang.String password = "mypassword";
        credentialStore.addCredential("myalias", new org.apache.ambari.server.security.credential.GenericKeyCredential(password.toCharArray()));
        org.apache.ambari.server.security.credential.Credential credential = credentialStore.getCredential("myalias");
        junit.framework.Assert.assertEquals(password, new java.lang.String(credential.toValue()));
        credentialStore.removeCredential("myalias");
        junit.framework.Assert.assertNull(credentialStore.getCredential("myalias"));
        credentialStore.addCredential("myalias", new org.apache.ambari.server.security.credential.GenericKeyCredential(password.toCharArray()));
        credential = credentialStore.getCredential("myalias");
        junit.framework.Assert.assertEquals(password, new java.lang.String(credential.toValue()));
        credentialStore = credentialStoreServiceFactory.create(directory, masterKeyService);
        credentialStore.setMasterKeyService(masterKeyService);
        credentialStore.removeCredential("myalias");
        junit.framework.Assert.assertNull(credentialStore.getCredential("myalias"));
        credentialStore.removeCredential("does_not_exist");
    }

    private interface CredentialStoreServiceFactory {
        org.apache.ambari.server.security.encryption.CredentialStore create(java.io.File path, org.apache.ambari.server.security.encryption.MasterKeyService masterKeyService);
    }

    private class FileBasedCredentialStoreServiceFactory implements org.apache.ambari.server.security.encryption.CredentialStoreTest.CredentialStoreServiceFactory {
        @java.lang.Override
        public org.apache.ambari.server.security.encryption.CredentialStore create(java.io.File path, org.apache.ambari.server.security.encryption.MasterKeyService masterKeyService) {
            org.apache.ambari.server.security.encryption.CredentialStore credentialStore = new org.apache.ambari.server.security.encryption.FileBasedCredentialStore(path);
            credentialStore.setMasterKeyService(masterKeyService);
            return credentialStore;
        }
    }

    private class InMemoryCredentialStoreServiceFactory implements org.apache.ambari.server.security.encryption.CredentialStoreTest.CredentialStoreServiceFactory {
        @java.lang.Override
        public org.apache.ambari.server.security.encryption.CredentialStore create(java.io.File path, org.apache.ambari.server.security.encryption.MasterKeyService masterKeyService) {
            org.apache.ambari.server.security.encryption.CredentialStore credentialStore = new org.apache.ambari.server.security.encryption.InMemoryCredentialStore(500, java.util.concurrent.TimeUnit.MILLISECONDS, false);
            credentialStore.setMasterKeyService(masterKeyService);
            return credentialStore;
        }
    }

    private interface MasterKeyServiceFactory {
        org.apache.ambari.server.security.encryption.MasterKeyService create(java.lang.String masterKey);

        org.apache.ambari.server.security.encryption.MasterKeyService createPersisted(java.io.File masterKeyFile, java.lang.String masterKey);
    }

    private class DefaultMasterKeyServiceFactory implements org.apache.ambari.server.security.encryption.CredentialStoreTest.MasterKeyServiceFactory {
        @java.lang.Override
        public org.apache.ambari.server.security.encryption.MasterKeyService create(java.lang.String masterKey) {
            return new org.apache.ambari.server.security.encryption.MasterKeyServiceImpl(masterKey);
        }

        @java.lang.Override
        public org.apache.ambari.server.security.encryption.MasterKeyService createPersisted(java.io.File masterKeyFile, java.lang.String masterKey) {
            new org.apache.ambari.server.security.encryption.MasterKeyServiceImpl("dummyKey").initializeMasterKeyFile(masterKeyFile, masterKey);
            return new org.apache.ambari.server.security.encryption.MasterKeyServiceImpl(masterKeyFile);
        }
    }

    private class TestTicker extends com.google.common.base.Ticker {
        private long currentNanos;

        public TestTicker(long currentNanos) {
            this.currentNanos = currentNanos;
        }

        @java.lang.Override
        public long read() {
            return currentNanos;
        }

        public void setCurrentNanos(long currentNanos) {
            this.currentNanos = currentNanos;
        }
    }
}