package org.apache.ambari.server.security;
import org.apache.commons.lang.RandomStringUtils;
import static org.easymock.EasyMock.createNiceMock;
public class CertGenerationTest {
    private static final int PASS_FILE_NAME_LEN = 20;

    private static final float MAX_PASS_LEN = 100;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.security.CertGenerationTest.class);

    public static org.junit.rules.TemporaryFolder temp = new org.junit.rules.TemporaryFolder();

    private static com.google.inject.Injector injector;

    private static org.apache.ambari.server.security.CertificateManager certMan;

    private static java.lang.String passFileName;

    private static int passLen;

    @com.google.inject.Inject
    static void init(org.apache.ambari.server.security.CertificateManager instance) {
        org.apache.ambari.server.security.CertGenerationTest.certMan = instance;
    }

    private static class SecurityModule extends com.google.inject.AbstractModule {
        @java.lang.Override
        protected void configure() {
            bind(java.util.Properties.class).toInstance(org.apache.ambari.server.security.CertGenerationTest.buildTestProperties());
            bind(org.apache.ambari.server.configuration.Configuration.class).toConstructor(org.apache.ambari.server.security.CertGenerationTest.getConfigurationConstructor());
            bind(org.apache.ambari.server.state.stack.OsFamily.class).toInstance(EasyMock.createNiceMock(org.apache.ambari.server.state.stack.OsFamily.class));
            requestStaticInjection(org.apache.ambari.server.security.CertGenerationTest.class);
        }
    }

    protected static java.util.Properties buildTestProperties() {
        try {
            org.apache.ambari.server.security.CertGenerationTest.temp.create();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        java.util.Properties properties = new java.util.Properties();
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.SRVR_KSTR_DIR.getKey(), org.apache.ambari.server.security.CertGenerationTest.temp.getRoot().getAbsolutePath());
        org.apache.ambari.server.security.CertGenerationTest.passLen = ((int) (java.lang.Math.abs(new java.util.Random().nextFloat() * org.apache.ambari.server.security.CertGenerationTest.MAX_PASS_LEN)));
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.SRVR_CRT_PASS_LEN.getKey(), java.lang.String.valueOf(org.apache.ambari.server.security.CertGenerationTest.passLen));
        org.apache.ambari.server.security.CertGenerationTest.passFileName = org.apache.commons.lang.RandomStringUtils.randomAlphabetic(org.apache.ambari.server.security.CertGenerationTest.PASS_FILE_NAME_LEN);
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.SRVR_CRT_PASS_FILE.getKey(), org.apache.ambari.server.security.CertGenerationTest.passFileName);
        return properties;
    }

    protected static java.lang.reflect.Constructor<org.apache.ambari.server.configuration.Configuration> getConfigurationConstructor() {
        try {
            return org.apache.ambari.server.configuration.Configuration.class.getConstructor(java.util.Properties.class);
        } catch (java.lang.NoSuchMethodException e) {
            throw new java.lang.RuntimeException("Expected constructor not found in Configuration.java", e);
        }
    }

    @org.junit.BeforeClass
    public static void setUpBeforeClass() throws java.io.IOException {
        org.apache.ambari.server.security.CertGenerationTest.injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.security.CertGenerationTest.SecurityModule());
        org.apache.ambari.server.security.CertGenerationTest.certMan = org.apache.ambari.server.security.CertGenerationTest.injector.getInstance(org.apache.ambari.server.security.CertificateManager.class);
        try {
            java.io.File caConfig = new java.io.File("conf/unix/ca.config");
            if (java.lang.System.getProperty("os.name").contains("Windows")) {
                caConfig = new java.io.File(new java.io.File(java.lang.ClassLoader.getSystemClassLoader().getResource("").getPath()).getParentFile().getParentFile(), "conf\\windows\\ca.config");
            }
            java.io.File caConfigTest = new java.io.File(org.apache.ambari.server.security.CertGenerationTest.temp.getRoot().getAbsolutePath(), "ca.config");
            java.io.File newCertsDir = new java.io.File(org.apache.ambari.server.security.CertGenerationTest.temp.getRoot().getAbsolutePath(), "newcerts");
            newCertsDir.mkdirs();
            java.io.File indexTxt = new java.io.File(org.apache.ambari.server.security.CertGenerationTest.temp.getRoot().getAbsolutePath(), "index.txt");
            indexTxt.createNewFile();
            java.lang.String content = org.apache.commons.io.IOUtils.toString(new java.io.FileInputStream(caConfig));
            if (java.lang.System.getProperty("os.name").contains("Windows")) {
                content = content.replace("keystore\\\\db", org.apache.ambari.server.security.CertGenerationTest.temp.getRoot().getAbsolutePath().replace("\\", "\\\\"));
            } else {
                content = content.replaceAll("/var/lib/ambari-server/keys/db", org.apache.ambari.server.security.CertGenerationTest.temp.getRoot().getAbsolutePath());
            }
            org.apache.commons.io.IOUtils.write(content, new java.io.FileOutputStream(caConfigTest));
        } catch (java.io.IOException e) {
            e.printStackTrace();
            junit.framework.TestCase.fail();
        }
        org.apache.ambari.server.security.CertGenerationTest.certMan.initRootCert();
    }

    @org.junit.AfterClass
    public static void tearDownAfterClass() throws java.io.IOException {
        org.apache.ambari.server.security.CertGenerationTest.temp.delete();
    }

    @org.junit.Ignore
    @org.junit.Test
    public void testServerCertGen() throws java.lang.Exception {
        java.io.File serverCrt = new java.io.File((org.apache.ambari.server.security.CertGenerationTest.temp.getRoot().getAbsoluteFile() + java.io.File.separator) + org.apache.ambari.server.configuration.Configuration.SRVR_CRT_NAME.getDefaultValue());
        org.junit.Assert.assertTrue(serverCrt.exists());
    }

    @org.junit.Test
    public void testServerKeyGen() throws java.lang.Exception {
        java.io.File serverKey = new java.io.File((org.apache.ambari.server.security.CertGenerationTest.temp.getRoot().getAbsoluteFile() + java.io.File.separator) + org.apache.ambari.server.configuration.Configuration.SRVR_KEY_NAME.getDefaultValue());
        org.junit.Assert.assertTrue(serverKey.exists());
    }

    @org.junit.Ignore
    @org.junit.Test
    public void testServerKeystoreGen() throws java.lang.Exception {
        java.io.File serverKeyStrore = new java.io.File((org.apache.ambari.server.security.CertGenerationTest.temp.getRoot().getAbsoluteFile() + java.io.File.separator) + org.apache.ambari.server.configuration.Configuration.KSTR_NAME.getDefaultValue());
        org.junit.Assert.assertTrue(serverKeyStrore.exists());
    }

    @org.junit.Ignore
    @org.junit.Test
    public void testRevokeExistingAgentCert() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> config = org.apache.ambari.server.security.CertGenerationTest.certMan.configs.getConfigsMap();
        config.put(org.apache.ambari.server.configuration.Configuration.PASSPHRASE.getKey(), "passphrase");
        java.lang.String agentHostname = "agent_hostname";
        org.apache.ambari.server.security.SignCertResponse scr = org.apache.ambari.server.security.CertGenerationTest.certMan.signAgentCrt(agentHostname, "incorrect_agentCrtReqContent", "passphrase");
        org.junit.Assert.assertFalse(scr.getMessage().contains("-revoke"));
        java.io.File fakeAgentCertFile = new java.io.File(((org.apache.ambari.server.security.CertGenerationTest.temp.getRoot().getAbsoluteFile() + java.io.File.separator) + agentHostname) + ".crt");
        org.junit.Assert.assertTrue(fakeAgentCertFile.exists());
        scr = org.apache.ambari.server.security.CertGenerationTest.certMan.signAgentCrt(agentHostname, "incorrect_agentCrtReqContent", "passphrase");
        org.junit.Assert.assertTrue(scr.getMessage().contains("-revoke"));
    }

    @org.junit.Test
    public void testPassFileGen() throws java.lang.Exception {
        java.io.File passFile = new java.io.File((org.apache.ambari.server.security.CertGenerationTest.temp.getRoot().getAbsolutePath() + java.io.File.separator) + org.apache.ambari.server.security.CertGenerationTest.passFileName);
        org.junit.Assert.assertTrue(passFile.exists());
        java.lang.String pass = org.apache.commons.io.FileUtils.readFileToString(passFile, java.nio.charset.Charset.defaultCharset());
        org.junit.Assert.assertEquals(pass.length(), org.apache.ambari.server.security.CertGenerationTest.passLen);
        if (org.apache.ambari.server.utils.ShellCommandUtil.LINUX) {
            java.lang.String permissions = org.apache.ambari.server.utils.ShellCommandUtil.getUnixFilePermissions(passFile.getAbsolutePath());
            org.junit.Assert.assertEquals(org.apache.ambari.server.utils.ShellCommandUtil.MASK_OWNER_ONLY_RW, permissions);
        }
    }
}