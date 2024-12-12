package org.apache.ambari.server.security;
import org.easymock.EasyMockSupport;
import org.easymock.IAnswer;
import static org.easymock.EasyMock.expect;
public class CertificateManagerTest extends org.easymock.EasyMockSupport {
    @org.junit.Rule
    public org.junit.rules.TemporaryFolder folder = new org.junit.rules.TemporaryFolder();

    @org.junit.Test
    public void testSignAgentCrt() throws java.lang.Exception {
        com.google.inject.Injector injector = getInjector();
        java.io.File directory = folder.newFolder();
        java.lang.String hostname = "host1.example.com";
        java.util.Map<java.lang.String, java.lang.String> configurationMap = new java.util.HashMap<>();
        configurationMap.put(org.apache.ambari.server.configuration.Configuration.SRVR_KSTR_DIR.getKey(), directory.getAbsolutePath());
        configurationMap.put(org.apache.ambari.server.configuration.Configuration.SRVR_CRT_PASS.getKey(), "server_cert_pass");
        configurationMap.put(org.apache.ambari.server.configuration.Configuration.SRVR_CRT_NAME.getKey(), "server_cert_name");
        configurationMap.put(org.apache.ambari.server.configuration.Configuration.SRVR_KEY_NAME.getKey(), "server_key_name");
        configurationMap.put(org.apache.ambari.server.configuration.Configuration.PASSPHRASE.getKey(), "passphrase");
        org.apache.ambari.server.configuration.Configuration configuration = injector.getInstance(org.apache.ambari.server.configuration.Configuration.class);
        EasyMock.expect(configuration.validateAgentHostnames()).andReturn(true).once();
        EasyMock.expect(configuration.getConfigsMap()).andReturn(configurationMap).anyTimes();
        java.lang.reflect.Method runCommand = org.apache.ambari.server.security.CertificateManager.class.getDeclaredMethod("runCommand", java.lang.String.class);
        final java.io.File agentCrtFile = new java.io.File(directory, java.lang.String.format("%s.crt", hostname));
        java.lang.String expectedCommand = java.lang.String.format("openssl ca -config %s/ca.config -in %s/%s.csr -out %s -batch -passin pass:%s -keyfile %s/%s -cert %s/%s", directory.getAbsolutePath(), directory.getAbsolutePath(), hostname, agentCrtFile.getAbsolutePath(), configurationMap.get(org.apache.ambari.server.configuration.Configuration.SRVR_CRT_PASS.getKey()), directory.getAbsolutePath(), configurationMap.get(org.apache.ambari.server.configuration.Configuration.SRVR_KEY_NAME.getKey()), directory.getAbsolutePath(), configurationMap.get(org.apache.ambari.server.configuration.Configuration.SRVR_CRT_NAME.getKey()));
        org.apache.ambari.server.security.CertificateManager certificateManager = createMockBuilder(org.apache.ambari.server.security.CertificateManager.class).addMockedMethod(runCommand).createMock();
        EasyMock.expect(certificateManager.runCommand(expectedCommand)).andAnswer(new org.easymock.IAnswer<java.lang.Integer>() {
            @java.lang.Override
            public java.lang.Integer answer() throws java.lang.Throwable {
                return agentCrtFile.createNewFile() ? 0 : 1;
            }
        }).once();
        injector.injectMembers(certificateManager);
        replayAll();
        org.apache.ambari.server.security.SignCertResponse response = certificateManager.signAgentCrt(hostname, "crtContent", "passphrase");
        verifyAll();
        junit.framework.Assert.assertEquals(org.apache.ambari.server.security.SignCertResponse.OK_STATUS, response.getResult());
    }

    @org.junit.Test
    public void testSignAgentCrtInvalidHostname() throws java.lang.Exception {
        com.google.inject.Injector injector = getInjector();
        org.apache.ambari.server.configuration.Configuration configuration = injector.getInstance(org.apache.ambari.server.configuration.Configuration.class);
        EasyMock.expect(configuration.validateAgentHostnames()).andReturn(true).once();
        replayAll();
        org.apache.ambari.server.security.CertificateManager certificateManager = new org.apache.ambari.server.security.CertificateManager();
        injector.injectMembers(certificateManager);
        org.apache.ambari.server.security.SignCertResponse response = certificateManager.signAgentCrt("hostname; echo \"hello\" > /tmp/hello.txt;", "crtContent", "passphrase");
        verifyAll();
        junit.framework.Assert.assertEquals(org.apache.ambari.server.security.SignCertResponse.ERROR_STATUS, response.getResult());
        junit.framework.Assert.assertEquals("The agent hostname is not a valid hostname", response.getMessage());
    }

    @org.junit.Test
    public void testSignAgentCrtBadPassphrase() throws java.lang.Exception {
        com.google.inject.Injector injector = getInjector();
        org.apache.ambari.server.configuration.Configuration configuration = injector.getInstance(org.apache.ambari.server.configuration.Configuration.class);
        EasyMock.expect(configuration.validateAgentHostnames()).andReturn(true).once();
        EasyMock.expect(configuration.getConfigsMap()).andReturn(java.util.Collections.singletonMap(org.apache.ambari.server.configuration.Configuration.PASSPHRASE.getKey(), "some_passphrase")).once();
        replayAll();
        org.apache.ambari.server.security.CertificateManager certificateManager = new org.apache.ambari.server.security.CertificateManager();
        injector.injectMembers(certificateManager);
        org.apache.ambari.server.security.SignCertResponse response = certificateManager.signAgentCrt("host1.example.com", "crtContent", "passphrase");
        verifyAll();
        junit.framework.Assert.assertEquals(org.apache.ambari.server.security.SignCertResponse.ERROR_STATUS, response.getResult());
        junit.framework.Assert.assertEquals("Incorrect passphrase from the agent", response.getMessage());
    }

    @org.junit.Test
    public void testSignAgentCrtInvalidHostnameIgnoreBadPassphrase() throws java.lang.Exception {
        com.google.inject.Injector injector = getInjector();
        org.apache.ambari.server.configuration.Configuration configuration = injector.getInstance(org.apache.ambari.server.configuration.Configuration.class);
        EasyMock.expect(configuration.validateAgentHostnames()).andReturn(false).once();
        EasyMock.expect(configuration.getConfigsMap()).andReturn(java.util.Collections.singletonMap(org.apache.ambari.server.configuration.Configuration.PASSPHRASE.getKey(), "some_passphrase")).once();
        replayAll();
        org.apache.ambari.server.security.CertificateManager certificateManager = new org.apache.ambari.server.security.CertificateManager();
        injector.injectMembers(certificateManager);
        org.apache.ambari.server.security.SignCertResponse response = certificateManager.signAgentCrt("hostname; echo \"hello\" > /tmp/hello.txt;", "crtContent", "passphrase");
        verifyAll();
        junit.framework.Assert.assertEquals(org.apache.ambari.server.security.SignCertResponse.ERROR_STATUS, response.getResult());
        junit.framework.Assert.assertEquals("Incorrect passphrase from the agent", response.getMessage());
    }

    @org.junit.Test
    public void testGetCACertificateChain() throws java.io.IOException {
        com.google.inject.Injector injector = getInjector();
        java.io.File directory = folder.newFolder();
        java.lang.String caCertFileName = "myca.crt";
        java.lang.String caCertChainFileName = "myca_chain.pem";
        org.apache.ambari.server.configuration.Configuration configuration = injector.getInstance(org.apache.ambari.server.configuration.Configuration.class);
        EasyMock.expect(configuration.getProperty(org.apache.ambari.server.configuration.Configuration.SRVR_KSTR_DIR)).andReturn(directory.getAbsolutePath()).anyTimes();
        EasyMock.expect(configuration.getProperty(org.apache.ambari.server.configuration.Configuration.SRVR_CRT_NAME)).andReturn(caCertFileName).anyTimes();
        EasyMock.expect(configuration.getProperty(org.apache.ambari.server.configuration.Configuration.SRVR_CRT_CHAIN_NAME)).andReturn(caCertChainFileName).anyTimes();
        final java.io.File caCertFile = new java.io.File(directory, caCertFileName);
        final java.io.File caCertChainFile = new java.io.File(directory, caCertChainFileName);
        org.apache.ambari.server.security.CertificateManager certificateManager = new org.apache.ambari.server.security.CertificateManager();
        injector.injectMembers(certificateManager);
        replayAll();
        java.lang.String content;
        java.nio.file.Files.write(caCertFile.toPath(), java.util.Collections.singleton(caCertFile.getAbsolutePath()));
        content = certificateManager.getCACertificateChainContent();
        junit.framework.Assert.assertEquals(caCertFile.getAbsolutePath(), content.trim());
        java.nio.file.Files.write(caCertChainFile.toPath(), java.util.Collections.singleton(caCertChainFile.getAbsolutePath()));
        content = certificateManager.getCACertificateChainContent();
        junit.framework.Assert.assertEquals(caCertChainFile.getAbsolutePath(), content.trim());
        verifyAll();
    }

    private com.google.inject.Injector getInjector() {
        return com.google.inject.Guice.createInjector(new com.google.inject.AbstractModule() {
            @java.lang.Override
            protected void configure() {
                bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
                bind(org.apache.ambari.server.configuration.Configuration.class).toInstance(createMock(org.apache.ambari.server.configuration.Configuration.class));
            }
        });
    }
}