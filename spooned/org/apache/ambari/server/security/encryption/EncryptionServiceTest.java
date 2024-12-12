package org.apache.ambari.server.security.encryption;
import org.easymock.EasyMock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;
@org.junit.runner.RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@org.powermock.core.classloader.annotations.PrepareForTest({ org.apache.ambari.server.security.encryption.MasterKeyServiceImpl.class })
@org.powermock.core.classloader.annotations.PowerMockIgnore({ "javax.crypto.*" })
public class EncryptionServiceTest {
    @org.junit.Rule
    private final org.junit.rules.TemporaryFolder tmpFolder = new org.junit.rules.TemporaryFolder();

    @org.junit.Test
    public void testEncryptAndDecryptUsingCustomKeyWithBase64Encoding() throws java.lang.Exception {
        testEncryptAndDecryptUsingCustomKey(org.apache.ambari.server.utils.TextEncoding.BASE_64);
    }

    @org.junit.Test
    public void testEncryptAndDecryptUsingCustomKeyWithBinHex64Encoding() throws java.lang.Exception {
        testEncryptAndDecryptUsingCustomKey(org.apache.ambari.server.utils.TextEncoding.BIN_HEX);
    }

    public void testEncryptAndDecryptUsingCustomKey(org.apache.ambari.server.utils.TextEncoding textEncoding) throws java.lang.Exception {
        final java.lang.String key = "mySuperS3cr3tMast3rKey!";
        final java.lang.String toBeEncrypted = "mySuperS3cr3tP4ssW0rD!";
        org.apache.ambari.server.security.encryption.EncryptionService encryptionService = new org.apache.ambari.server.security.encryption.AESEncryptionService();
        final java.lang.String encrypted = encryptionService.encrypt(toBeEncrypted, key, textEncoding);
        final java.lang.String decrypted = encryptionService.decrypt(encrypted, key, textEncoding);
        org.junit.Assert.assertEquals(toBeEncrypted, decrypted);
    }

    @org.junit.Test
    public void testEncryptAndDecryptUsingPersistedMasterKey() throws java.lang.Exception {
        final java.lang.String fileDir = tmpFolder.newFolder("keys").getAbsolutePath();
        final java.io.File masterKeyFile = new java.io.File(fileDir, "master");
        final java.lang.String masterKey = "mySuperS3cr3tMast3rKey!";
        final org.apache.ambari.server.security.encryption.MasterKeyServiceImpl ms = new org.apache.ambari.server.security.encryption.MasterKeyServiceImpl("dummyKey");
        junit.framework.Assert.assertTrue(ms.initializeMasterKeyFile(masterKeyFile, masterKey));
        final java.lang.String toBeEncrypted = "mySuperS3cr3tP4ssW0rD!";
        org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration(new java.util.Properties());
        configuration.setProperty(org.apache.ambari.server.configuration.Configuration.MASTER_KEY_LOCATION, masterKeyFile.getParent());
        com.google.inject.Injector injector = com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(org.apache.ambari.server.configuration.Configuration.class).toInstance(configuration);
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(org.easymock.EasyMock.createMock(org.apache.ambari.server.state.stack.OsFamily.class));
            }
        });
        org.apache.ambari.server.security.encryption.EncryptionService encryptionService = new org.apache.ambari.server.security.encryption.AESEncryptionService();
        injector.injectMembers(encryptionService);
        final java.lang.String encrypted = encryptionService.encrypt(toBeEncrypted);
        final java.lang.String decrypted = encryptionService.decrypt(encrypted);
        verifyAll();
        org.junit.Assert.assertEquals(toBeEncrypted, decrypted);
    }

    @org.junit.Test
    public void testEncryptAndDecryptUsingEnvDefinedMasterKey() throws java.lang.Exception {
        final java.lang.String fileDir = tmpFolder.newFolder("keys").getAbsolutePath();
        final java.io.File masterKeyFile = new java.io.File(fileDir, "master");
        final java.lang.String masterKey = "mySuperS3cr3tMast3rKey!";
        final org.apache.ambari.server.security.encryption.MasterKeyServiceImpl ms = new org.apache.ambari.server.security.encryption.MasterKeyServiceImpl("dummyKey");
        junit.framework.Assert.assertTrue(ms.initializeMasterKeyFile(masterKeyFile, masterKey));
        final java.lang.String toBeEncrypted = "mySuperS3cr3tP4ssW0rD!";
        setupEnvironmentVariableExpectations(masterKeyFile);
        org.apache.ambari.server.security.encryption.EncryptionService encryptionService = new org.apache.ambari.server.security.encryption.AESEncryptionService();
        final java.lang.String encrypted = encryptionService.encrypt(toBeEncrypted);
        final java.lang.String decrypted = encryptionService.decrypt(encrypted);
        verifyAll();
        org.junit.Assert.assertEquals(toBeEncrypted, decrypted);
    }

    @org.junit.Test(expected = java.lang.SecurityException.class)
    public void shouldThrowSecurityExceptionInCaseOfEncryptingWithNonExistingPersistedMasterKey() throws java.lang.Exception {
        final java.lang.String toBeEncrypted = "mySuperS3cr3tP4ssW0rD!";
        org.apache.ambari.server.security.encryption.EncryptionService encryptionService = new org.apache.ambari.server.security.encryption.AESEncryptionService();
        encryptionService.encrypt(toBeEncrypted);
    }

    private void setupEnvironmentVariableExpectations(final java.io.File masterKeyFile) {
        final java.util.Map<java.lang.String, java.lang.String> sysEnvironment = new java.util.HashMap<>();
        sysEnvironment.put(org.apache.ambari.server.configuration.Configuration.MASTER_KEY_LOCATION.getKey(), masterKeyFile.getAbsolutePath());
        mockStatic(java.lang.System.class);
        EasyMock.expect(java.lang.System.getenv()).andReturn(sysEnvironment);
        replayAll();
    }
}