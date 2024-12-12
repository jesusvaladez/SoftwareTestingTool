package org.apache.ambari.server.utils;
import org.easymock.EasyMockSupport;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
@org.junit.runner.RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@org.powermock.core.classloader.annotations.PrepareForTest(org.apache.ambari.server.utils.PasswordUtils.class)
public class PasswordUtilsTest extends org.easymock.EasyMockSupport {
    private static final java.lang.String CS_ALIAS = "${alias=testAlias}";

    private org.apache.ambari.server.utils.PasswordUtils passwordUtils;

    private com.google.inject.Injector injector;

    private org.apache.ambari.server.configuration.Configuration configuration;

    @org.junit.Before
    public void setUp() {
        injector = createInjector();
        configuration = injector.getInstance(org.apache.ambari.server.configuration.Configuration.class);
        passwordUtils = org.apache.ambari.server.utils.PasswordUtils.getInstance();
    }

    @org.junit.Test
    public void shouldReadPasswordFromCredentialStoreOfAnAlias() throws java.lang.Exception {
        final org.apache.ambari.server.security.encryption.CredentialProvider credentialProvider = org.powermock.api.easymock.PowerMock.createNiceMock(org.apache.ambari.server.security.encryption.CredentialProvider.class);
        setupBasicCredentialProviderExpectations(credentialProvider);
        credentialProvider.getPasswordForAlias(org.apache.ambari.server.utils.PasswordUtilsTest.CS_ALIAS);
        org.powermock.api.easymock.PowerMock.expectLastCall().andReturn("testPassword".toCharArray()).once();
        org.powermock.api.easymock.PowerMock.replay(credentialProvider, org.apache.ambari.server.security.encryption.CredentialProvider.class);
        replayAll();
        org.junit.Assert.assertEquals("testPassword", passwordUtils.readPassword(org.apache.ambari.server.utils.PasswordUtilsTest.CS_ALIAS, "testPassword"));
        verifyAll();
    }

    @org.junit.Test
    public void shouldReadPasswordFromFileIfPasswordPropertyIsPasswordFilePath() throws java.lang.Exception {
        final java.lang.String testPassword = "ambariTest";
        final java.io.File passwordFile = writeTestPasswordFile(testPassword);
        org.junit.Assert.assertEquals("ambariTest", passwordUtils.readPassword(passwordFile.getAbsolutePath(), "testPasswordDefault"));
    }

    @org.junit.Test
    public void shouldReadDefaultPasswordIfPasswordPropertyIsPasswordFilePathButItDoesNotExists() throws java.lang.Exception {
        final java.io.File passwordFile = new java.io.File("/my/test/password/file.dat");
        org.junit.Assert.assertEquals("testPasswordDefault", passwordUtils.readPassword(passwordFile.getAbsolutePath(), "testPasswordDefault"));
    }

    @org.junit.Test
    @org.junit.Ignore("until we fix fo for ANY kind of users including root")
    public void shouldReadDefaultPasswordIfPasswordPropertyIsPasswordFilePathButItIsNotReadable() throws java.lang.Exception {
        final java.lang.String testPassword = "ambariTest";
        final java.io.File passwordFile = writeTestPasswordFile(testPassword);
        passwordFile.setReadable(false);
        org.junit.Assert.assertEquals("testPasswordDefault", passwordUtils.readPassword(passwordFile.getAbsolutePath(), "testPasswordDefault"));
    }

    private java.io.File writeTestPasswordFile(final java.lang.String testPassword) throws java.io.IOException {
        final org.junit.rules.TemporaryFolder tempFolder = new org.junit.rules.TemporaryFolder();
        tempFolder.create();
        final java.io.File passwordFile = tempFolder.newFile();
        passwordFile.deleteOnExit();
        org.apache.commons.io.FileUtils.writeStringToFile(passwordFile, testPassword, java.nio.charset.Charset.defaultCharset());
        return passwordFile;
    }

    private void setupBasicCredentialProviderExpectations(org.apache.ambari.server.security.encryption.CredentialProvider credentialProvider) throws java.lang.Exception {
        org.powermock.api.easymock.PowerMock.expectNew(org.apache.ambari.server.security.encryption.CredentialProvider.class, null, null, configuration).andReturn(credentialProvider);
    }

    private com.google.inject.Injector createInjector() {
        return com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
                bind(org.apache.ambari.server.configuration.Configuration.class).toInstance(createNiceMock(org.apache.ambari.server.configuration.Configuration.class));
                binder().requestStaticInjection(org.apache.ambari.server.utils.PasswordUtils.class);
            }
        });
    }
}