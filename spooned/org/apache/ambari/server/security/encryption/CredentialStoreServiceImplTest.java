package org.apache.ambari.server.security.encryption;
import static org.easymock.EasyMock.createNiceMock;
public class CredentialStoreServiceImplTest {
    @org.junit.Rule
    public org.junit.rules.TemporaryFolder tmpFolder = new org.junit.rules.TemporaryFolder();

    private org.apache.ambari.server.security.encryption.CredentialStoreServiceImpl credentialStoreService;

    private static final java.lang.String CLUSTER_NAME = "C1";

    @org.junit.Before
    public void setUp() throws java.lang.Exception {
        tmpFolder.create();
        final java.io.File masterKeyFile = tmpFolder.newFile(org.apache.ambari.server.configuration.Configuration.MASTER_KEY_FILENAME_DEFAULT);
        junit.framework.Assert.assertTrue(new org.apache.ambari.server.security.encryption.MasterKeyServiceImpl("dummyKey").initializeMasterKeyFile(masterKeyFile, "secret"));
        com.google.inject.Injector injector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                java.util.Properties properties = new java.util.Properties();
                properties.setProperty(org.apache.ambari.server.configuration.Configuration.MASTER_KEY_LOCATION.getKey(), tmpFolder.getRoot().getAbsolutePath());
                properties.setProperty(org.apache.ambari.server.configuration.Configuration.MASTER_KEYSTORE_LOCATION.getKey(), tmpFolder.getRoot().getAbsolutePath());
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
                bind(org.apache.ambari.server.security.SecurePasswordHelper.class).toInstance(new org.apache.ambari.server.security.SecurePasswordHelper());
                bind(org.apache.ambari.server.configuration.Configuration.class).toInstance(new org.apache.ambari.server.configuration.Configuration(properties));
            }
        });
        org.apache.ambari.server.security.encryption.MasterKeyService masterKeyService = new org.apache.ambari.server.security.encryption.MasterKeyServiceImpl(masterKeyFile);
        junit.framework.Assert.assertTrue(masterKeyService.isMasterKeyInitialized());
        credentialStoreService = injector.getInstance(org.apache.ambari.server.security.encryption.CredentialStoreServiceImpl.class);
    }

    @org.junit.After
    public void tearDown() throws java.lang.Exception {
        credentialStoreService = null;
        tmpFolder.delete();
    }

    @org.junit.Test
    public void testSetAndGetCredential_Temporary() throws java.lang.Exception {
        org.apache.ambari.server.security.credential.PrincipalKeyCredential credential = new org.apache.ambari.server.security.credential.PrincipalKeyCredential("username", "password");
        credentialStoreService.setCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1", credential, org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY);
        junit.framework.Assert.assertEquals(credential, credentialStoreService.getCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1", org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY));
        junit.framework.Assert.assertEquals(credential, credentialStoreService.getCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1"));
        junit.framework.Assert.assertNull(credentialStoreService.getCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1", org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED));
    }

    @org.junit.Test
    public void testSetAndGetCredential_Persisted() throws java.lang.Exception {
        org.apache.ambari.server.security.credential.PrincipalKeyCredential credential = new org.apache.ambari.server.security.credential.PrincipalKeyCredential("username", "password");
        credentialStoreService.setCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1", credential, org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED);
        junit.framework.Assert.assertEquals(credential, credentialStoreService.getCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1", org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED));
        junit.framework.Assert.assertEquals(credential, credentialStoreService.getCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1"));
        junit.framework.Assert.assertNull(credentialStoreService.getCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1", org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY));
    }

    @org.junit.Test
    public void testRemoveCredential_Temporary() throws java.lang.Exception {
        org.apache.ambari.server.security.credential.PrincipalKeyCredential credential1 = new org.apache.ambari.server.security.credential.PrincipalKeyCredential("username1", "password1");
        org.apache.ambari.server.security.credential.PrincipalKeyCredential credential2 = new org.apache.ambari.server.security.credential.PrincipalKeyCredential("username2", "password2");
        credentialStoreService.setCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1", credential1, org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY);
        credentialStoreService.setCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test2", credential2, org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY);
        credentialStoreService.removeCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1", org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED);
        junit.framework.Assert.assertEquals(credential1, credentialStoreService.getCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1", org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY));
        junit.framework.Assert.assertEquals(credential1, credentialStoreService.getCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1"));
        credentialStoreService.removeCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1", org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY);
        junit.framework.Assert.assertNull(credentialStoreService.getCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1", org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY));
        junit.framework.Assert.assertNull(credentialStoreService.getCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1"));
        junit.framework.Assert.assertEquals(credential2, credentialStoreService.getCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test2"));
    }

    @org.junit.Test
    public void testRemoveCredential_Persisted() throws java.lang.Exception {
        org.apache.ambari.server.security.credential.PrincipalKeyCredential credential1 = new org.apache.ambari.server.security.credential.PrincipalKeyCredential("username1", "password1");
        org.apache.ambari.server.security.credential.PrincipalKeyCredential credential2 = new org.apache.ambari.server.security.credential.PrincipalKeyCredential("username2", "password2");
        credentialStoreService.setCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1", credential1, org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED);
        credentialStoreService.setCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test2", credential2, org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED);
        credentialStoreService.removeCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1", org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY);
        junit.framework.Assert.assertEquals(credential1, credentialStoreService.getCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1", org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED));
        junit.framework.Assert.assertEquals(credential1, credentialStoreService.getCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1"));
        credentialStoreService.removeCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1", org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED);
        junit.framework.Assert.assertNull(credentialStoreService.getCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1", org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED));
        junit.framework.Assert.assertNull(credentialStoreService.getCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1"));
        junit.framework.Assert.assertEquals(credential2, credentialStoreService.getCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test2"));
    }

    @org.junit.Test
    public void testRemoveCredential_Either() throws java.lang.Exception {
        org.apache.ambari.server.security.credential.PrincipalKeyCredential credential1 = new org.apache.ambari.server.security.credential.PrincipalKeyCredential("username1", "password1");
        org.apache.ambari.server.security.credential.PrincipalKeyCredential credential2 = new org.apache.ambari.server.security.credential.PrincipalKeyCredential("username2", "password2");
        org.apache.ambari.server.security.credential.PrincipalKeyCredential credential3 = new org.apache.ambari.server.security.credential.PrincipalKeyCredential("username3", "password3");
        org.apache.ambari.server.security.credential.PrincipalKeyCredential credential4 = new org.apache.ambari.server.security.credential.PrincipalKeyCredential("username4", "password4");
        credentialStoreService.setCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1", credential1, org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED);
        credentialStoreService.setCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test2", credential2, org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED);
        credentialStoreService.setCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test3", credential3, org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY);
        credentialStoreService.setCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test4", credential4, org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY);
        credentialStoreService.removeCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1");
        junit.framework.Assert.assertNull(credentialStoreService.getCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1", org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED));
        junit.framework.Assert.assertNull(credentialStoreService.getCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1"));
        junit.framework.Assert.assertEquals(credential2, credentialStoreService.getCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test2"));
        junit.framework.Assert.assertEquals(credential3, credentialStoreService.getCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test3"));
        junit.framework.Assert.assertEquals(credential4, credentialStoreService.getCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test4"));
        credentialStoreService.removeCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test3");
        junit.framework.Assert.assertNull(credentialStoreService.getCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1"));
        junit.framework.Assert.assertEquals(credential2, credentialStoreService.getCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test2"));
        junit.framework.Assert.assertNull(credentialStoreService.getCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test3"));
        junit.framework.Assert.assertEquals(credential4, credentialStoreService.getCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test4"));
    }

    @org.junit.Test
    public void testUpdateCredential() throws java.lang.Exception {
        org.apache.ambari.server.security.credential.PrincipalKeyCredential credential1 = new org.apache.ambari.server.security.credential.PrincipalKeyCredential("username1", "password1");
        org.apache.ambari.server.security.credential.PrincipalKeyCredential credential2 = new org.apache.ambari.server.security.credential.PrincipalKeyCredential("username2", "password2");
        credentialStoreService.setCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1", credential1, org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED);
        junit.framework.Assert.assertEquals(credential1, credentialStoreService.getCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1", org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED));
        junit.framework.Assert.assertNull(credentialStoreService.getCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1", org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY));
        junit.framework.Assert.assertEquals(credential1, credentialStoreService.getCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1"));
        credentialStoreService.setCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1", credential1, org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY);
        junit.framework.Assert.assertEquals(credential1, credentialStoreService.getCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1", org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY));
        junit.framework.Assert.assertNull(credentialStoreService.getCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1", org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED));
        junit.framework.Assert.assertEquals(credential1, credentialStoreService.getCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1"));
        credentialStoreService.setCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1", credential2, org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED);
        junit.framework.Assert.assertEquals(credential2, credentialStoreService.getCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1", org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED));
        junit.framework.Assert.assertNull(credentialStoreService.getCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1", org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY));
        junit.framework.Assert.assertEquals(credential2, credentialStoreService.getCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1"));
    }

    @org.junit.Test
    public void testContainsCredential() throws java.lang.Exception {
        org.apache.ambari.server.security.credential.PrincipalKeyCredential credential1 = new org.apache.ambari.server.security.credential.PrincipalKeyCredential("username1", "password1");
        org.apache.ambari.server.security.credential.PrincipalKeyCredential credential2 = new org.apache.ambari.server.security.credential.PrincipalKeyCredential("username2", "password2");
        org.apache.ambari.server.security.credential.PrincipalKeyCredential credential3 = new org.apache.ambari.server.security.credential.PrincipalKeyCredential("username3", "password3");
        org.apache.ambari.server.security.credential.PrincipalKeyCredential credential4 = new org.apache.ambari.server.security.credential.PrincipalKeyCredential("username4", "password4");
        credentialStoreService.setCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1", credential1, org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED);
        credentialStoreService.setCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test2", credential2, org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED);
        credentialStoreService.setCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test3", credential3, org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY);
        credentialStoreService.setCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test4", credential4, org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY);
        junit.framework.Assert.assertTrue(credentialStoreService.containsCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1", org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED));
        junit.framework.Assert.assertFalse(credentialStoreService.containsCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1", org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY));
        junit.framework.Assert.assertFalse(credentialStoreService.containsCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test3", org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED));
        junit.framework.Assert.assertFalse(credentialStoreService.containsCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1", org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY));
        junit.framework.Assert.assertFalse(credentialStoreService.containsCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test3", org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED));
        junit.framework.Assert.assertTrue(credentialStoreService.containsCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test3", org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY));
        junit.framework.Assert.assertTrue(credentialStoreService.containsCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1"));
        junit.framework.Assert.assertTrue(credentialStoreService.containsCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test3"));
    }

    @org.junit.Test
    public void testIsCredentialPersisted() throws java.lang.Exception {
        org.apache.ambari.server.security.credential.PrincipalKeyCredential credential1 = new org.apache.ambari.server.security.credential.PrincipalKeyCredential("username1", "password1");
        org.apache.ambari.server.security.credential.PrincipalKeyCredential credential2 = new org.apache.ambari.server.security.credential.PrincipalKeyCredential("username2", "password2");
        org.apache.ambari.server.security.credential.PrincipalKeyCredential credential3 = new org.apache.ambari.server.security.credential.PrincipalKeyCredential("username3", "password3");
        org.apache.ambari.server.security.credential.PrincipalKeyCredential credential4 = new org.apache.ambari.server.security.credential.PrincipalKeyCredential("username4", "password4");
        credentialStoreService.setCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1", credential1, org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED);
        credentialStoreService.setCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test2", credential2, org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED);
        credentialStoreService.setCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test3", credential3, org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY);
        credentialStoreService.setCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test4", credential4, org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED, credentialStoreService.getCredentialStoreType(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1"));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED, credentialStoreService.getCredentialStoreType(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test2"));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY, credentialStoreService.getCredentialStoreType(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test3"));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY, credentialStoreService.getCredentialStoreType(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test4"));
        try {
            credentialStoreService.getCredentialStoreType(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test5");
            junit.framework.Assert.fail("Expected AmbariException to be thrown");
        } catch (org.apache.ambari.server.AmbariException e) {
        }
    }

    @org.junit.Test
    public void testListCredentials() throws java.lang.Exception {
        org.apache.ambari.server.security.credential.PrincipalKeyCredential credential1 = new org.apache.ambari.server.security.credential.PrincipalKeyCredential("username1", "password1");
        org.apache.ambari.server.security.credential.PrincipalKeyCredential credential2 = new org.apache.ambari.server.security.credential.PrincipalKeyCredential("username2", "password2");
        org.apache.ambari.server.security.credential.PrincipalKeyCredential credential3 = new org.apache.ambari.server.security.credential.PrincipalKeyCredential("username3", "password3");
        org.apache.ambari.server.security.credential.PrincipalKeyCredential credential4 = new org.apache.ambari.server.security.credential.PrincipalKeyCredential("username4", "password4");
        credentialStoreService.setCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1", credential1, org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED);
        credentialStoreService.setCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test2", credential2, org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED);
        credentialStoreService.setCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test3", credential3, org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY);
        credentialStoreService.setCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test4", credential4, org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY);
        java.util.Map<java.lang.String, org.apache.ambari.server.security.encryption.CredentialStoreType> credentials = credentialStoreService.listCredentials(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME);
        junit.framework.Assert.assertNotNull(credentials);
        junit.framework.Assert.assertEquals(4, credentials.size());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED, credentials.get("test1"));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED, credentials.get("test2"));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY, credentials.get("test3"));
        junit.framework.Assert.assertEquals(org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY, credentials.get("test4"));
    }

    @org.junit.Test(expected = org.apache.ambari.server.AmbariException.class)
    public void testFailToReinitialize_Persisted() throws java.lang.Exception {
        credentialStoreService.initializePersistedCredentialStore(null, null);
    }

    @org.junit.Test(expected = org.apache.ambari.server.AmbariException.class)
    public void testFailToReinitialize_Temporary() throws java.lang.Exception {
        credentialStoreService.initializeTemporaryCredentialStore(1, java.util.concurrent.TimeUnit.MINUTES, false);
    }

    @org.junit.Test
    public void testFailNotInitialized() throws java.lang.Exception {
        org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration(new java.util.Properties());
        org.apache.ambari.server.security.encryption.CredentialStoreService uninitializedCredentialStoreService = new org.apache.ambari.server.security.encryption.CredentialStoreServiceImpl(configuration, new org.apache.ambari.server.security.SecurePasswordHelper());
        org.apache.ambari.server.security.credential.PrincipalKeyCredential credential1 = new org.apache.ambari.server.security.credential.PrincipalKeyCredential("username1", "password1");
        uninitializedCredentialStoreService.setCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1", credential1, org.apache.ambari.server.security.encryption.CredentialStoreType.TEMPORARY);
        try {
            uninitializedCredentialStoreService.setCredential(org.apache.ambari.server.security.encryption.CredentialStoreServiceImplTest.CLUSTER_NAME, "test1", credential1, org.apache.ambari.server.security.encryption.CredentialStoreType.PERSISTED);
            junit.framework.Assert.fail("AmbariException should have been thrown");
        } catch (org.apache.ambari.server.AmbariException e) {
        }
    }
}