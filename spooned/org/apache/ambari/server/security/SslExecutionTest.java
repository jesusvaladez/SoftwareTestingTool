package org.apache.ambari.server.security;
import static org.easymock.EasyMock.createNiceMock;
public class SslExecutionTest {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.security.SslExecutionTest.class);

    public org.junit.rules.TemporaryFolder temp = new org.junit.rules.TemporaryFolder();

    com.google.inject.Injector injector;

    private static org.apache.ambari.server.security.CertificateManager certMan;

    @com.google.inject.Inject
    static void init(org.apache.ambari.server.security.CertificateManager instance) {
        org.apache.ambari.server.security.SslExecutionTest.certMan = instance;
    }

    private class SecurityModule extends com.google.inject.AbstractModule {
        @java.lang.Override
        protected void configure() {
            bind(java.util.Properties.class).toInstance(buildTestProperties());
            bind(org.apache.ambari.server.configuration.Configuration.class).toConstructor(getConfigurationConstructor());
            bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
            requestStaticInjection(org.apache.ambari.server.security.SslExecutionTest.class);
        }
    }

    protected java.util.Properties buildTestProperties() {
        try {
            temp.create();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        java.util.Properties properties = new java.util.Properties();
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.SRVR_KSTR_DIR.getKey(), temp.getRoot().getAbsolutePath());
        return properties;
    }

    protected java.lang.reflect.Constructor<org.apache.ambari.server.configuration.Configuration> getConfigurationConstructor() {
        try {
            return org.apache.ambari.server.configuration.Configuration.class.getConstructor(java.util.Properties.class);
        } catch (java.lang.NoSuchMethodException e) {
            throw new java.lang.RuntimeException("Expected constructor not found in Configuration.java", e);
        }
    }

    @org.junit.Before
    public void setUp() throws java.io.IOException {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.security.SslExecutionTest.SecurityModule());
        org.apache.ambari.server.security.SslExecutionTest.certMan = injector.getInstance(org.apache.ambari.server.security.CertificateManager.class);
        org.apache.ambari.server.security.SslExecutionTest.certMan.initRootCert();
    }

    @org.junit.After
    public void tearDown() throws java.io.IOException {
        temp.delete();
    }

    @org.junit.Test
    public void testSslLogging() throws java.lang.Exception {
        org.apache.ambari.server.security.SslExecutionTest.LOG.info("Testing sign");
        org.apache.ambari.server.security.SslExecutionTest.certMan.configs.getConfigsMap().put(org.apache.ambari.server.configuration.Configuration.PASSPHRASE.getKey(), "123123");
        org.apache.ambari.server.security.SslExecutionTest.LOG.info("key dir = " + org.apache.ambari.server.security.SslExecutionTest.certMan.configs.getConfigsMap().get(org.apache.ambari.server.configuration.Configuration.SRVR_KSTR_DIR.getKey()));
        org.apache.ambari.server.security.SignCertResponse signAgentCrt = org.apache.ambari.server.security.SslExecutionTest.certMan.signAgentCrt("somehost", "gdfgdfg", "123123");
        org.apache.ambari.server.security.SslExecutionTest.LOG.info("-------------RESPONCE-------------");
        org.apache.ambari.server.security.SslExecutionTest.LOG.info("-------------MESSAGE--------------");
        org.apache.ambari.server.security.SslExecutionTest.LOG.info(signAgentCrt.getMessage());
        org.apache.ambari.server.security.SslExecutionTest.LOG.info("---------------------------------");
        org.apache.ambari.server.security.SslExecutionTest.LOG.info("-------------RESULT--------------");
        org.apache.ambari.server.security.SslExecutionTest.LOG.info(signAgentCrt.getResult());
        org.apache.ambari.server.security.SslExecutionTest.LOG.info("---------------------------------");
        org.junit.Assert.assertTrue(org.apache.ambari.server.security.SignCertResponse.ERROR_STATUS.equals(signAgentCrt.getResult()));
    }
}