package org.apache.ambari.server.configuration;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.support.membermodification.MemberMatcher;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.mockito.Mockito.spy;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;
@org.junit.runner.RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@org.powermock.core.classloader.annotations.PrepareForTest({ org.apache.ambari.server.configuration.Configuration.class, org.apache.ambari.server.utils.PasswordUtils.class })
@org.powermock.core.classloader.annotations.PowerMockIgnore({ "javax.management.*", "javax.crypto.*" })
public class ConfigurationTest {
    public org.junit.rules.TemporaryFolder temp = new org.junit.rules.TemporaryFolder();

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        temp.create();
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException {
        temp.delete();
    }

    @org.junit.Test
    public void testValidateAgentHostnames() {
        junit.framework.Assert.assertTrue(new org.apache.ambari.server.configuration.Configuration().validateAgentHostnames());
    }

    @org.junit.Test
    public void testValidateAgentHostnamesOn() {
        java.util.Properties ambariProperties = new java.util.Properties();
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.SRVR_AGENT_HOSTNAME_VALIDATE.getKey(), "true");
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        junit.framework.Assert.assertTrue(conf.validateAgentHostnames());
        junit.framework.Assert.assertEquals("true", conf.getConfigsMap().get(org.apache.ambari.server.configuration.Configuration.SRVR_AGENT_HOSTNAME_VALIDATE.getKey()));
    }

    @org.junit.Test
    public void testValidateAgentHostnamesOff() {
        java.util.Properties ambariProperties = new java.util.Properties();
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.SRVR_AGENT_HOSTNAME_VALIDATE.getKey(), "false");
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        junit.framework.Assert.assertFalse(conf.validateAgentHostnames());
        junit.framework.Assert.assertEquals("false", conf.getConfigsMap().get(org.apache.ambari.server.configuration.Configuration.SRVR_AGENT_HOSTNAME_VALIDATE.getKey()));
    }

    @org.junit.Test
    public void testDefaultTwoWayAuthNotSet() throws java.lang.Exception {
        junit.framework.Assert.assertFalse(new org.apache.ambari.server.configuration.Configuration().isTwoWaySsl());
    }

    @org.junit.Test
    public void testTwoWayAuthTurnedOn() throws java.lang.Exception {
        java.util.Properties ambariProperties = new java.util.Properties();
        ambariProperties.setProperty("security.server.two_way_ssl", "true");
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        junit.framework.Assert.assertTrue(conf.isTwoWaySsl());
    }

    @org.junit.Test
    public void testTwoWayAuthTurnedOff() throws java.lang.Exception {
        java.util.Properties ambariProperties = new java.util.Properties();
        ambariProperties.setProperty("security.server.two_way_ssl", "false");
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        junit.framework.Assert.assertFalse(conf.isTwoWaySsl());
    }

    @org.junit.Test
    public void testGetClientSSLApiPort() throws java.lang.Exception {
        java.util.Properties ambariProperties = new java.util.Properties();
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_PORT.getKey(), "6666");
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        junit.framework.Assert.assertEquals(6666, conf.getClientSSLApiPort());
        conf = new org.apache.ambari.server.configuration.Configuration();
        junit.framework.Assert.assertEquals(8443, conf.getClientSSLApiPort());
    }

    @org.junit.Test
    public void testGetMpacksV2StagingPath() {
        java.util.Properties ambariProperties = new java.util.Properties();
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.MPACKS_V2_STAGING_DIR_PATH.getKey(), "/var/lib/ambari-server/resources/mpacks-v2/");
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        junit.framework.Assert.assertEquals("/var/lib/ambari-server/resources/mpacks-v2/", conf.getMpacksV2StagingPath());
        conf = new org.apache.ambari.server.configuration.Configuration();
        junit.framework.Assert.assertEquals(null, conf.getMpacksV2StagingPath());
    }

    @org.junit.Test
    public void testGetClientHTTPSSettings() throws java.io.IOException {
        java.io.File passFile = java.io.File.createTempFile("https.pass.", "txt");
        passFile.deleteOnExit();
        java.lang.String password = "pass12345";
        org.apache.commons.io.FileUtils.writeStringToFile(passFile, password, java.nio.charset.Charset.defaultCharset());
        java.util.Properties ambariProperties = new java.util.Properties();
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.API_USE_SSL.getKey(), "true");
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_KSTR_DIR_NAME.getKey(), passFile.getParent());
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_CRT_PASS_FILE_NAME.getKey(), passFile.getName());
        java.lang.String oneWayPort = org.apache.commons.lang.RandomStringUtils.randomNumeric(4);
        java.lang.String twoWayPort = org.apache.commons.lang.RandomStringUtils.randomNumeric(4);
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.SRVR_TWO_WAY_SSL_PORT.getKey(), twoWayPort.toString());
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.SRVR_ONE_WAY_SSL_PORT.getKey(), oneWayPort.toString());
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        junit.framework.Assert.assertTrue(conf.getApiSSLAuthentication());
        junit.framework.Assert.assertFalse(conf.getConfigsMap().get(org.apache.ambari.server.configuration.Configuration.KSTR_NAME.getKey()).equals(conf.getConfigsMap().get(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_KSTR_NAME.getKey())));
        junit.framework.Assert.assertEquals("keystore.p12", conf.getConfigsMap().get(org.apache.ambari.server.configuration.Configuration.KSTR_NAME.getKey()));
        junit.framework.Assert.assertEquals("PKCS12", conf.getConfigsMap().get(org.apache.ambari.server.configuration.Configuration.KSTR_TYPE.getKey()));
        junit.framework.Assert.assertEquals("keystore.p12", conf.getConfigsMap().get(org.apache.ambari.server.configuration.Configuration.TSTR_NAME.getKey()));
        junit.framework.Assert.assertEquals("PKCS12", conf.getConfigsMap().get(org.apache.ambari.server.configuration.Configuration.TSTR_TYPE.getKey()));
        junit.framework.Assert.assertEquals("https.keystore.p12", conf.getConfigsMap().get(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_KSTR_NAME.getKey()));
        junit.framework.Assert.assertEquals("PKCS12", conf.getConfigsMap().get(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_KSTR_TYPE.getKey()));
        junit.framework.Assert.assertEquals("https.keystore.p12", conf.getConfigsMap().get(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_TSTR_NAME.getKey()));
        junit.framework.Assert.assertEquals("PKCS12", conf.getConfigsMap().get(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_TSTR_TYPE.getKey()));
        junit.framework.Assert.assertEquals(passFile.getName(), conf.getConfigsMap().get(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_CRT_PASS_FILE_NAME.getKey()));
        junit.framework.Assert.assertEquals(password, conf.getConfigsMap().get(org.apache.ambari.server.configuration.Configuration.CLIENT_API_SSL_CRT_PASS.getKey()));
        junit.framework.Assert.assertEquals(java.lang.Integer.parseInt(twoWayPort), conf.getTwoWayAuthPort());
        junit.framework.Assert.assertEquals(java.lang.Integer.parseInt(oneWayPort), conf.getOneWayAuthPort());
    }

    @org.junit.Test
    public void testLoadSSLParams_unencrypted() throws java.io.IOException {
        java.util.Properties ambariProperties = new java.util.Properties();
        java.lang.String unencrypted = "fake-unencrypted-password";
        java.lang.String encrypted = "fake-encrypted-password";
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.SSL_TRUSTSTORE_PASSWORD.getKey(), unencrypted);
        org.apache.ambari.server.configuration.Configuration conf = Mockito.spy(new org.apache.ambari.server.configuration.Configuration(ambariProperties));
        org.powermock.api.easymock.PowerMock.stub(org.powermock.api.easymock.PowerMock.method(org.apache.ambari.server.utils.PasswordUtils.class, "readPasswordFromStore", java.lang.String.class, org.apache.ambari.server.configuration.Configuration.class)).toReturn(null);
        conf.loadSSLParams();
        junit.framework.Assert.assertEquals(java.lang.System.getProperty(org.apache.ambari.server.configuration.Configuration.JAVAX_SSL_TRUSTSTORE_PASSWORD, "unknown"), unencrypted);
    }

    @org.junit.Test
    public void testLoadSSLParams_encrypted() throws java.io.IOException {
        java.util.Properties ambariProperties = new java.util.Properties();
        java.lang.String unencrypted = "fake-unencrypted-password";
        java.lang.String encrypted = "fake-encrypted-password";
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.SSL_TRUSTSTORE_PASSWORD.getKey(), unencrypted);
        org.apache.ambari.server.configuration.Configuration conf = Mockito.spy(new org.apache.ambari.server.configuration.Configuration(ambariProperties));
        org.powermock.api.easymock.PowerMock.stub(org.powermock.api.easymock.PowerMock.method(org.apache.ambari.server.utils.PasswordUtils.class, "readPasswordFromStore", java.lang.String.class, org.apache.ambari.server.configuration.Configuration.class)).toReturn(encrypted);
        conf.loadSSLParams();
        junit.framework.Assert.assertEquals(java.lang.System.getProperty(org.apache.ambari.server.configuration.Configuration.JAVAX_SSL_TRUSTSTORE_PASSWORD, "unknown"), encrypted);
    }

    @org.junit.Test
    public void testGetRcaDatabasePassword_fromStore() {
        java.lang.String serverJdbcRcaUserPasswdKey = "key";
        java.lang.String encrypted = "password";
        java.util.Properties properties = new java.util.Properties();
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_RCA_USER_PASSWD.getKey(), serverJdbcRcaUserPasswdKey);
        org.apache.ambari.server.configuration.Configuration conf = Mockito.spy(new org.apache.ambari.server.configuration.Configuration(properties));
        org.powermock.api.easymock.PowerMock.stub(org.powermock.api.easymock.PowerMock.method(org.apache.ambari.server.utils.PasswordUtils.class, "readPassword")).toReturn(encrypted);
        junit.framework.Assert.assertEquals(encrypted, conf.getRcaDatabasePassword());
    }

    @org.junit.Test
    public void testGetRcaDatabasePassword_fromFile() {
        org.apache.ambari.server.configuration.Configuration conf = Mockito.spy(new org.apache.ambari.server.configuration.Configuration(new java.util.Properties()));
        junit.framework.Assert.assertEquals("mapred", conf.getRcaDatabasePassword());
    }

    @org.junit.Test
    public void testGetLocalDatabaseUrl() {
        java.util.Properties ambariProperties = new java.util.Properties();
        ambariProperties.setProperty("server.jdbc.database_name", "ambaritestdatabase");
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        junit.framework.Assert.assertEquals(conf.getLocalDatabaseUrl(), org.apache.ambari.server.configuration.Configuration.JDBC_LOCAL_URL.concat("ambaritestdatabase"));
    }

    @org.junit.Test
    public void testNoNewlineInPassword() throws java.lang.Exception {
        java.util.Properties ambariProperties = new java.util.Properties();
        java.io.File f = temp.newFile("password.dat");
        java.io.FileOutputStream fos = new java.io.FileOutputStream(f);
        fos.write("ambaritest\r\n".getBytes());
        fos.close();
        java.lang.String passwordFile = (temp.getRoot().getAbsolutePath() + java.lang.System.getProperty("file.separator")) + "password.dat";
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_USER_PASSWD.getKey(), passwordFile);
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        org.powermock.api.easymock.PowerMock.stub(org.powermock.api.easymock.PowerMock.method(org.apache.ambari.server.utils.PasswordUtils.class, "readPasswordFromStore", java.lang.String.class, org.apache.ambari.server.configuration.Configuration.class)).toReturn(null);
        junit.framework.Assert.assertEquals("ambaritest", conf.getDatabasePassword());
    }

    @org.junit.Test
    public void testGetAmbariProperties() throws java.lang.Exception {
        java.util.Properties ambariProperties = new java.util.Properties();
        ambariProperties.setProperty("name", "value");
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        mockStatic(org.apache.ambari.server.configuration.Configuration.class);
        java.lang.reflect.Method[] methods = org.powermock.api.support.membermodification.MemberMatcher.methods(org.apache.ambari.server.configuration.Configuration.class, "readConfigFile");
        org.powermock.api.easymock.PowerMock.expectPrivate(org.apache.ambari.server.configuration.Configuration.class, methods[0]).andReturn(ambariProperties);
        replayAll();
        java.util.Map<java.lang.String, java.lang.String> props = conf.getAmbariProperties();
        verifyAll();
        junit.framework.Assert.assertEquals("value", props.get("name"));
    }

    @org.junit.Test
    public void testGetAmbariBlacklistFile() {
        java.util.Properties ambariProperties = new java.util.Properties();
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        junit.framework.Assert.assertEquals(null, conf.getAmbariBlacklistFile());
        ambariProperties = new java.util.Properties();
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.PROPERTY_MASK_FILE.getKey(), "ambari-blacklist.properties");
        conf = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        junit.framework.Assert.assertEquals("ambari-blacklist.properties", conf.getAmbariBlacklistFile());
    }

    @org.junit.Rule
    public org.junit.rules.ExpectedException exception = org.junit.rules.ExpectedException.none();

    @org.junit.Test
    public void testGetLocalDatabaseUrlThrowException() {
        java.util.Properties ambariProperties = new java.util.Properties();
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        exception.expect(java.lang.RuntimeException.class);
        exception.expectMessage("Server DB Name is not configured!");
        conf.getLocalDatabaseUrl();
    }

    @org.junit.Test
    public void testServerPoolSizes() {
        java.util.Properties ambariProperties = new java.util.Properties();
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        junit.framework.Assert.assertEquals(25, conf.getClientThreadPoolSize());
        junit.framework.Assert.assertEquals(25, conf.getAgentThreadPoolSize());
        junit.framework.Assert.assertEquals(10, conf.getViewExtractionThreadPoolCoreSize());
        junit.framework.Assert.assertEquals(20, conf.getViewExtractionThreadPoolMaxSize());
        junit.framework.Assert.assertEquals(100000L, conf.getViewExtractionThreadPoolTimeout());
        ambariProperties = new java.util.Properties();
        ambariProperties.setProperty("client.threadpool.size.max", "4");
        ambariProperties.setProperty("agent.threadpool.size.max", "82");
        ambariProperties.setProperty("view.extraction.threadpool.size.core", "83");
        ambariProperties.setProperty("view.extraction.threadpool.size.max", "56");
        ambariProperties.setProperty("view.extraction.threadpool.timeout", "6000");
        conf = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        junit.framework.Assert.assertEquals(4, conf.getClientThreadPoolSize());
        junit.framework.Assert.assertEquals(82, conf.getAgentThreadPoolSize());
        junit.framework.Assert.assertEquals(83, conf.getViewExtractionThreadPoolCoreSize());
        junit.framework.Assert.assertEquals(56, conf.getViewExtractionThreadPoolMaxSize());
        junit.framework.Assert.assertEquals(6000L, conf.getViewExtractionThreadPoolTimeout());
    }

    @org.junit.Test
    public void testGetDefaultAgentTaskTimeout() {
        java.util.Properties ambariProperties = new java.util.Properties();
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        junit.framework.Assert.assertEquals("900", conf.getDefaultAgentTaskTimeout(false));
        junit.framework.Assert.assertEquals("1800", conf.getDefaultAgentTaskTimeout(true));
        ambariProperties = new java.util.Properties();
        ambariProperties.setProperty("agent.task.timeout", "4");
        ambariProperties.setProperty("agent.package.install.task.timeout", "82");
        conf = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        junit.framework.Assert.assertEquals("4", conf.getDefaultAgentTaskTimeout(false));
        junit.framework.Assert.assertEquals("82", conf.getDefaultAgentTaskTimeout(true));
    }

    @org.junit.Test
    public void testGetDefaultServerTaskTimeout() {
        java.util.Properties ambariProperties = new java.util.Properties();
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        junit.framework.Assert.assertEquals(java.lang.Integer.valueOf(1200), conf.getDefaultServerTaskTimeout());
        ambariProperties = new java.util.Properties();
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_TASK_TIMEOUT.getKey(), "3600");
        conf = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        junit.framework.Assert.assertEquals(java.lang.Integer.valueOf(3600), conf.getDefaultServerTaskTimeout());
    }

    @org.junit.Test
    public void testIsViewValidationEnabled() throws java.lang.Exception {
        final java.util.Properties ambariProperties = new java.util.Properties();
        org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        junit.framework.Assert.assertFalse(configuration.isViewValidationEnabled());
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.VIEWS_VALIDATE.getKey(), "false");
        configuration = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        junit.framework.Assert.assertFalse(configuration.isViewValidationEnabled());
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.VIEWS_VALIDATE.getKey(), "true");
        configuration = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        junit.framework.Assert.assertTrue(configuration.isViewValidationEnabled());
    }

    @org.junit.Test
    public void testIsViewRemoveUndeployedEnabled() throws java.lang.Exception {
        final java.util.Properties ambariProperties = new java.util.Properties();
        org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        junit.framework.Assert.assertFalse(configuration.isViewRemoveUndeployedEnabled());
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.VIEWS_REMOVE_UNDEPLOYED.getKey(), "false");
        configuration = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        junit.framework.Assert.assertFalse(configuration.isViewRemoveUndeployedEnabled());
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.VIEWS_REMOVE_UNDEPLOYED.getKey(), "true");
        configuration = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        junit.framework.Assert.assertTrue(configuration.isViewRemoveUndeployedEnabled());
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.VIEWS_REMOVE_UNDEPLOYED.getKey(), org.apache.ambari.server.configuration.Configuration.VIEWS_REMOVE_UNDEPLOYED.getDefaultValue());
        configuration = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        junit.framework.Assert.assertFalse(configuration.isViewRemoveUndeployedEnabled());
    }

    @org.junit.Test
    public void testConnectionPoolingProperties() throws java.lang.Exception {
        final java.util.Properties ambariProperties = new java.util.Properties();
        final org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.configuration.Configuration.ConnectionPoolType.INTERNAL, configuration.getConnectionPoolType());
        junit.framework.Assert.assertEquals(5, configuration.getConnectionPoolAcquisitionSize());
        junit.framework.Assert.assertEquals(7200, configuration.getConnectionPoolIdleTestInternval());
        junit.framework.Assert.assertEquals(0, configuration.getConnectionPoolMaximumAge());
        junit.framework.Assert.assertEquals(0, configuration.getConnectionPoolMaximumExcessIdle());
        junit.framework.Assert.assertEquals(14400, configuration.getConnectionPoolMaximumIdle());
        junit.framework.Assert.assertEquals(32, configuration.getConnectionPoolMaximumSize());
        junit.framework.Assert.assertEquals(5, configuration.getConnectionPoolMinimumSize());
        junit.framework.Assert.assertEquals(30, configuration.getConnectionPoolAcquisitionRetryAttempts());
        junit.framework.Assert.assertEquals(1000, configuration.getConnectionPoolAcquisitionRetryDelay());
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_CONNECTION_POOL.getKey(), org.apache.ambari.server.configuration.Configuration.ConnectionPoolType.C3P0.getName());
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_CONNECTION_POOL_MIN_SIZE.getKey(), "1");
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_CONNECTION_POOL_MAX_SIZE.getKey(), "2");
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_CONNECTION_POOL_AQUISITION_SIZE.getKey(), "3");
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_CONNECTION_POOL_MAX_AGE.getKey(), "4");
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_CONNECTION_POOL_MAX_IDLE_TIME.getKey(), "5");
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_CONNECTION_POOL_MAX_IDLE_TIME_EXCESS.getKey(), "6");
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_CONNECTION_POOL_IDLE_TEST_INTERVAL.getKey(), "7");
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_CONNECTION_POOL_ACQUISITION_RETRY_ATTEMPTS.getKey(), "8");
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_CONNECTION_POOL_ACQUISITION_RETRY_DELAY.getKey(), "9");
        junit.framework.Assert.assertEquals(org.apache.ambari.server.configuration.Configuration.ConnectionPoolType.C3P0, configuration.getConnectionPoolType());
        junit.framework.Assert.assertEquals(3, configuration.getConnectionPoolAcquisitionSize());
        junit.framework.Assert.assertEquals(7, configuration.getConnectionPoolIdleTestInternval());
        junit.framework.Assert.assertEquals(4, configuration.getConnectionPoolMaximumAge());
        junit.framework.Assert.assertEquals(6, configuration.getConnectionPoolMaximumExcessIdle());
        junit.framework.Assert.assertEquals(5, configuration.getConnectionPoolMaximumIdle());
        junit.framework.Assert.assertEquals(2, configuration.getConnectionPoolMaximumSize());
        junit.framework.Assert.assertEquals(1, configuration.getConnectionPoolMinimumSize());
        junit.framework.Assert.assertEquals(8, configuration.getConnectionPoolAcquisitionRetryAttempts());
        junit.framework.Assert.assertEquals(9, configuration.getConnectionPoolAcquisitionRetryDelay());
    }

    @org.junit.Test
    public void testDatabaseType() throws java.lang.Exception {
        final java.util.Properties ambariProperties = new java.util.Properties();
        final org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_URL.getKey(), "jdbc:oracle://server");
        junit.framework.Assert.assertEquals(org.apache.ambari.server.configuration.Configuration.DatabaseType.ORACLE, configuration.getDatabaseType());
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_URL.getKey(), "jdbc:postgres://server");
        junit.framework.Assert.assertEquals(org.apache.ambari.server.configuration.Configuration.DatabaseType.POSTGRES, configuration.getDatabaseType());
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_URL.getKey(), "jdbc:mysql://server");
        junit.framework.Assert.assertEquals(org.apache.ambari.server.configuration.Configuration.DatabaseType.MYSQL, configuration.getDatabaseType());
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_URL.getKey(), "jdbc:derby://server");
        junit.framework.Assert.assertEquals(org.apache.ambari.server.configuration.Configuration.DatabaseType.DERBY, configuration.getDatabaseType());
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_URL.getKey(), "jdbc:sqlserver://server");
        junit.framework.Assert.assertEquals(org.apache.ambari.server.configuration.Configuration.DatabaseType.SQL_SERVER, configuration.getDatabaseType());
    }

    @org.junit.Test
    public void testGetAgentPackageParallelCommandsLimit() throws java.lang.Exception {
        final java.util.Properties ambariProperties = new java.util.Properties();
        final org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        junit.framework.Assert.assertEquals(100, configuration.getAgentPackageParallelCommandsLimit());
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.AGENT_PACKAGE_PARALLEL_COMMANDS_LIMIT.getKey(), "5");
        junit.framework.Assert.assertEquals(5, configuration.getAgentPackageParallelCommandsLimit());
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.AGENT_PACKAGE_PARALLEL_COMMANDS_LIMIT.getKey(), "0");
        junit.framework.Assert.assertEquals(1, configuration.getAgentPackageParallelCommandsLimit());
    }

    @org.junit.Test
    public void testGetExecutionSchedulerWait() throws java.lang.Exception {
        final java.util.Properties ambariProperties = new java.util.Properties();
        final org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        junit.framework.Assert.assertEquals(new java.lang.Long(1000L), configuration.getExecutionSchedulerWait());
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.EXECUTION_SCHEDULER_WAIT.getKey(), "5");
        junit.framework.Assert.assertEquals(new java.lang.Long(5000L), configuration.getExecutionSchedulerWait());
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.EXECUTION_SCHEDULER_WAIT.getKey(), "100");
        junit.framework.Assert.assertEquals(new java.lang.Long(60000L), configuration.getExecutionSchedulerWait());
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.EXECUTION_SCHEDULER_WAIT.getKey(), "100m");
        junit.framework.Assert.assertEquals(new java.lang.Long(1000L), configuration.getExecutionSchedulerWait());
    }

    @org.junit.Test
    public void testServerLocksProfilingEnabled() throws java.lang.Exception {
        final java.util.Properties ambariProperties = new java.util.Properties();
        final org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        junit.framework.Assert.assertFalse(configuration.isServerLocksProfilingEnabled());
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_LOCKS_PROFILING.getKey(), java.lang.Boolean.TRUE.toString());
        junit.framework.Assert.assertTrue(configuration.isServerLocksProfilingEnabled());
    }

    @org.junit.Test
    public void testAlertCaching() throws java.lang.Exception {
        final java.util.Properties ambariProperties = new java.util.Properties();
        final org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        junit.framework.Assert.assertFalse(configuration.isAlertCacheEnabled());
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.ALERTS_CACHE_ENABLED.getKey(), java.lang.Boolean.TRUE.toString());
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.ALERTS_CACHE_FLUSH_INTERVAL.getKey(), "60");
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.ALERTS_CACHE_SIZE.getKey(), "1000");
        junit.framework.Assert.assertTrue(configuration.isAlertCacheEnabled());
        junit.framework.Assert.assertEquals(60, configuration.getAlertCacheFlushInterval());
        junit.framework.Assert.assertEquals(1000, configuration.getAlertCacheSize());
    }

    @org.junit.Test
    public void testPropertyProviderThreadPoolSizes() throws java.lang.Exception {
        final java.util.Properties ambariProperties = new java.util.Properties();
        final org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        junit.framework.Assert.assertEquals(2 * java.lang.Runtime.getRuntime().availableProcessors(), configuration.getPropertyProvidersThreadPoolCoreSize());
        junit.framework.Assert.assertEquals(4 * java.lang.Runtime.getRuntime().availableProcessors(), configuration.getPropertyProvidersThreadPoolMaxSize());
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.PROPERTY_PROVIDER_THREADPOOL_MAX_SIZE.getKey(), "44");
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.PROPERTY_PROVIDER_THREADPOOL_CORE_SIZE.getKey(), "22");
        junit.framework.Assert.assertEquals(22, configuration.getPropertyProvidersThreadPoolCoreSize());
        junit.framework.Assert.assertEquals(44, configuration.getPropertyProvidersThreadPoolMaxSize());
    }

    public void testGetHostRoleCommandStatusSummaryCacheSize() throws java.lang.Exception {
        final java.util.Properties ambariProperties = new java.util.Properties();
        final org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_HRC_STATUS_SUMMARY_CACHE_SIZE.getKey(), "3000");
        long actualCacheSize = configuration.getHostRoleCommandStatusSummaryCacheSize();
        junit.framework.Assert.assertEquals(actualCacheSize, 3000L);
    }

    @org.junit.Test
    public void testGetHostRoleCommandStatusSummaryCacheSizeDefault() throws java.lang.Exception {
        final java.util.Properties ambariProperties = new java.util.Properties();
        final org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        java.lang.Long actualCacheSize = configuration.getHostRoleCommandStatusSummaryCacheSize();
        junit.framework.Assert.assertEquals(actualCacheSize, org.apache.ambari.server.configuration.Configuration.SERVER_HRC_STATUS_SUMMARY_CACHE_SIZE.getDefaultValue());
    }

    @org.junit.Test
    public void testGetHostRoleCommandStatusSummaryCacheExpiryDuration() throws java.lang.Exception {
        final java.util.Properties ambariProperties = new java.util.Properties();
        final org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_HRC_STATUS_SUMMARY_CACHE_EXPIRY_DURATION.getKey(), "60");
        long actualCacheExpiryDuration = configuration.getHostRoleCommandStatusSummaryCacheExpiryDuration();
        junit.framework.Assert.assertEquals(actualCacheExpiryDuration, 60L);
    }

    @org.junit.Test
    public void testGetHostRoleCommandStatusSummaryCacheExpiryDurationDefault() throws java.lang.Exception {
        final java.util.Properties ambariProperties = new java.util.Properties();
        final org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        java.lang.Long actualCacheExpiryDuration = configuration.getHostRoleCommandStatusSummaryCacheExpiryDuration();
        junit.framework.Assert.assertEquals(actualCacheExpiryDuration, org.apache.ambari.server.configuration.Configuration.SERVER_HRC_STATUS_SUMMARY_CACHE_EXPIRY_DURATION.getDefaultValue());
    }

    @org.junit.Test
    public void testGetHostRoleCommandStatusSummaryCacheEnabled() throws java.lang.Exception {
        final java.util.Properties ambariProperties = new java.util.Properties();
        final org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_HRC_STATUS_SUMMARY_CACHE_ENABLED.getKey(), "true");
        boolean actualCacheEnabledConfig = configuration.getHostRoleCommandStatusSummaryCacheEnabled();
        junit.framework.Assert.assertEquals(actualCacheEnabledConfig, true);
    }

    @org.junit.Test
    public void testGetHostRoleCommandStatusSummaryCacheDisabled() throws java.lang.Exception {
        final java.util.Properties ambariProperties = new java.util.Properties();
        final org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_HRC_STATUS_SUMMARY_CACHE_ENABLED.getKey(), "false");
        boolean actualCacheEnabledConfig = configuration.getHostRoleCommandStatusSummaryCacheEnabled();
        junit.framework.Assert.assertEquals(actualCacheEnabledConfig, false);
    }

    @org.junit.Test
    public void testGetHostRoleCommandStatusSummaryCacheEnabledDefault() throws java.lang.Exception {
        final java.util.Properties ambariProperties = new java.util.Properties();
        final org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        java.lang.Boolean actualCacheEnabledConfig = configuration.getHostRoleCommandStatusSummaryCacheEnabled();
        junit.framework.Assert.assertEquals(actualCacheEnabledConfig, org.apache.ambari.server.configuration.Configuration.SERVER_HRC_STATUS_SUMMARY_CACHE_ENABLED.getDefaultValue());
    }

    @org.junit.Test
    public void testCustomDatabaseProperties() throws java.lang.Exception {
        final java.util.Properties ambariProperties = new java.util.Properties();
        final org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        ambariProperties.setProperty("server.jdbc.properties.foo", "fooValue");
        ambariProperties.setProperty("server.jdbc.properties.bar", "barValue");
        java.util.Properties properties = configuration.getDatabaseCustomProperties();
        junit.framework.Assert.assertEquals(2, properties.size());
        junit.framework.Assert.assertEquals("fooValue", properties.getProperty("eclipselink.jdbc.property.foo"));
        junit.framework.Assert.assertEquals("barValue", properties.getProperty("eclipselink.jdbc.property.bar"));
    }

    @org.junit.Test
    public void testCustomPersistenceProperties() throws java.lang.Exception {
        final java.util.Properties ambariProperties = new java.util.Properties();
        final org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        ambariProperties.setProperty("server.persistence.properties.eclipselink.cache.coordination.channel", "FooChannel");
        ambariProperties.setProperty("server.persistence.properties.eclipselink.persistence-context.flush-mode", "commit");
        java.util.Properties properties = configuration.getPersistenceCustomProperties();
        junit.framework.Assert.assertEquals(2, properties.size());
        junit.framework.Assert.assertEquals("FooChannel", properties.getProperty("eclipselink.cache.coordination.channel"));
        junit.framework.Assert.assertEquals("commit", properties.getProperty("eclipselink.persistence-context.flush-mode"));
    }

    @org.junit.Test
    public void testThreadPoolEnabledPropertyProviderDefaults() throws java.lang.Exception {
        final int SMALLEST_COMPLETION_SERIVCE_TIMEOUT_MS = 1000;
        final int LARGEST_COMPLETION_SERIVCE_TIMEOUT_MS = 5000;
        int processorCount = java.lang.Runtime.getRuntime().availableProcessors();
        final java.util.Properties ambariProperties = new java.util.Properties();
        final org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        long completionServiceTimeout = configuration.getPropertyProvidersCompletionServiceTimeout();
        int corePoolSize = configuration.getPropertyProvidersThreadPoolCoreSize();
        int maxPoolSize = configuration.getPropertyProvidersThreadPoolMaxSize();
        int workerQueueSize = configuration.getPropertyProvidersWorkerQueueSize();
        junit.framework.Assert.assertEquals(5000, completionServiceTimeout);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.configuration.Configuration.PROCESSOR_BASED_THREADPOOL_CORE_SIZE_DEFAULT, corePoolSize);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.configuration.Configuration.PROCESSOR_BASED_THREADPOOL_MAX_SIZE_DEFAULT, maxPoolSize);
        junit.framework.Assert.assertEquals(java.lang.Integer.MAX_VALUE, workerQueueSize);
        junit.framework.Assert.assertTrue(completionServiceTimeout >= SMALLEST_COMPLETION_SERIVCE_TIMEOUT_MS);
        junit.framework.Assert.assertTrue(completionServiceTimeout <= LARGEST_COMPLETION_SERIVCE_TIMEOUT_MS);
        junit.framework.Assert.assertTrue(corePoolSize <= maxPoolSize);
        junit.framework.Assert.assertTrue((corePoolSize > 2) && (corePoolSize <= 128));
        junit.framework.Assert.assertTrue((maxPoolSize > 2) && (maxPoolSize <= (processorCount * 4)));
        junit.framework.Assert.assertTrue(workerQueueSize > (processorCount * 10));
    }

    @org.junit.Test
    public void testKerberosAuthenticationEnabled() throws java.io.IOException {
        java.io.File keytabFile = temp.newFile("spnego.service.keytab");
        java.util.Properties properties = new java.util.Properties();
        properties.put(org.apache.ambari.server.configuration.Configuration.KERBEROS_AUTH_ENABLED.getKey(), "true");
        properties.put(org.apache.ambari.server.configuration.Configuration.KERBEROS_AUTH_SPNEGO_KEYTAB_FILE.getKey(), keytabFile.getAbsolutePath());
        properties.put(org.apache.ambari.server.configuration.Configuration.KERBEROS_AUTH_SPNEGO_PRINCIPAL.getKey(), "spnego/principal@REALM");
        properties.put(org.apache.ambari.server.configuration.Configuration.KERBEROS_AUTH_AUTH_TO_LOCAL_RULES.getKey(), "DEFAULT");
        org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.security.authentication.kerberos.AmbariKerberosAuthenticationProperties kerberosAuthenticationProperties = configuration.getKerberosAuthenticationProperties();
        junit.framework.Assert.assertTrue(kerberosAuthenticationProperties.isKerberosAuthenticationEnabled());
        junit.framework.Assert.assertEquals(keytabFile.getAbsolutePath(), kerberosAuthenticationProperties.getSpnegoKeytabFilePath());
        junit.framework.Assert.assertEquals("spnego/principal@REALM", kerberosAuthenticationProperties.getSpnegoPrincipalName());
        junit.framework.Assert.assertEquals("DEFAULT", kerberosAuthenticationProperties.getAuthToLocalRules());
    }

    @org.junit.Test
    public void testKerberosAuthenticationEnabledUsingDefaults() throws java.io.IOException {
        java.io.File keytabFile = temp.newFile("spnego.service.keytab");
        java.util.Properties properties = new java.util.Properties();
        properties.put(org.apache.ambari.server.configuration.Configuration.KERBEROS_AUTH_ENABLED.getKey(), "true");
        properties.put(org.apache.ambari.server.configuration.Configuration.KERBEROS_AUTH_SPNEGO_KEYTAB_FILE.getKey(), keytabFile.getAbsolutePath());
        org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.security.authentication.kerberos.AmbariKerberosAuthenticationProperties kerberosAuthenticationProperties = configuration.getKerberosAuthenticationProperties();
        junit.framework.Assert.assertTrue(kerberosAuthenticationProperties.isKerberosAuthenticationEnabled());
        junit.framework.Assert.assertEquals(keytabFile.getAbsolutePath(), kerberosAuthenticationProperties.getSpnegoKeytabFilePath());
        junit.framework.Assert.assertEquals("HTTP/" + org.apache.ambari.server.utils.StageUtils.getHostName(), kerberosAuthenticationProperties.getSpnegoPrincipalName());
        junit.framework.Assert.assertEquals("DEFAULT", kerberosAuthenticationProperties.getAuthToLocalRules());
    }

    @org.junit.Test
    public void testKerberosAuthenticationDisabled() {
        java.util.Properties properties = new java.util.Properties();
        properties.put(org.apache.ambari.server.configuration.Configuration.KERBEROS_AUTH_ENABLED.getKey(), "false");
        org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.security.authentication.kerberos.AmbariKerberosAuthenticationProperties kerberosAuthenticationProperties = configuration.getKerberosAuthenticationProperties();
        junit.framework.Assert.assertFalse(kerberosAuthenticationProperties.isKerberosAuthenticationEnabled());
        junit.framework.Assert.assertNull(kerberosAuthenticationProperties.getSpnegoKeytabFilePath());
        junit.framework.Assert.assertNull(kerberosAuthenticationProperties.getSpnegoPrincipalName());
        junit.framework.Assert.assertNull(kerberosAuthenticationProperties.getAuthToLocalRules());
    }

    @org.junit.Test
    public void testKerberosAuthenticationDisabledWithValuesSet() {
        java.util.Properties properties = new java.util.Properties();
        properties.put(org.apache.ambari.server.configuration.Configuration.KERBEROS_AUTH_ENABLED.getKey(), "false");
        properties.put(org.apache.ambari.server.configuration.Configuration.KERBEROS_AUTH_SPNEGO_KEYTAB_FILE.getKey(), "/path/to/spnego/keytab/file");
        properties.put(org.apache.ambari.server.configuration.Configuration.KERBEROS_AUTH_SPNEGO_PRINCIPAL.getKey(), "spnego/principal@REALM");
        properties.put(org.apache.ambari.server.configuration.Configuration.KERBEROS_AUTH_AUTH_TO_LOCAL_RULES.getKey(), "DEFAULT");
        org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.security.authentication.kerberos.AmbariKerberosAuthenticationProperties kerberosAuthenticationProperties = configuration.getKerberosAuthenticationProperties();
        junit.framework.Assert.assertFalse(kerberosAuthenticationProperties.isKerberosAuthenticationEnabled());
        junit.framework.Assert.assertNull(kerberosAuthenticationProperties.getSpnegoKeytabFilePath());
        junit.framework.Assert.assertNull(kerberosAuthenticationProperties.getSpnegoPrincipalName());
        junit.framework.Assert.assertNull(kerberosAuthenticationProperties.getAuthToLocalRules());
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testKerberosAuthenticationEmptySPNEGOPrincipalName() throws java.io.IOException {
        java.io.File keytabFile = temp.newFile("spnego.service.keytab");
        java.util.Properties properties = new java.util.Properties();
        properties.put(org.apache.ambari.server.configuration.Configuration.KERBEROS_AUTH_ENABLED.getKey(), "true");
        properties.put(org.apache.ambari.server.configuration.Configuration.KERBEROS_AUTH_SPNEGO_KEYTAB_FILE.getKey(), keytabFile.getAbsolutePath());
        properties.put(org.apache.ambari.server.configuration.Configuration.KERBEROS_AUTH_SPNEGO_PRINCIPAL.getKey(), "");
        properties.put(org.apache.ambari.server.configuration.Configuration.KERBEROS_AUTH_AUTH_TO_LOCAL_RULES.getKey(), "DEFAULT");
        new org.apache.ambari.server.configuration.Configuration(properties);
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testKerberosAuthenticationEmptySPNEGOKeytabFile() {
        java.util.Properties properties = new java.util.Properties();
        properties.put(org.apache.ambari.server.configuration.Configuration.KERBEROS_AUTH_ENABLED.getKey(), "true");
        properties.put(org.apache.ambari.server.configuration.Configuration.KERBEROS_AUTH_SPNEGO_KEYTAB_FILE.getKey(), "");
        properties.put(org.apache.ambari.server.configuration.Configuration.KERBEROS_AUTH_SPNEGO_PRINCIPAL.getKey(), "spnego/principal@REALM");
        properties.put(org.apache.ambari.server.configuration.Configuration.KERBEROS_AUTH_AUTH_TO_LOCAL_RULES.getKey(), "DEFAULT");
        new org.apache.ambari.server.configuration.Configuration(properties);
    }

    @org.junit.Test
    public void testMetricsRetrieveServiceDefaults() throws java.lang.Exception {
        final int LOWEST_CACHE_TIMEOUT_MINUTES = 30;
        int processorCount = java.lang.Runtime.getRuntime().availableProcessors();
        final java.util.Properties ambariProperties = new java.util.Properties();
        final org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        int priority = configuration.getMetricsServiceThreadPriority();
        int cacheTimeout = configuration.getMetricsServiceCacheTimeout();
        int corePoolSize = configuration.getMetricsServiceThreadPoolCoreSize();
        int maxPoolSize = configuration.getMetricsServiceThreadPoolMaxSize();
        int workerQueueSize = configuration.getMetricsServiceWorkerQueueSize();
        junit.framework.Assert.assertEquals(java.lang.Thread.NORM_PRIORITY, priority);
        junit.framework.Assert.assertEquals(LOWEST_CACHE_TIMEOUT_MINUTES, cacheTimeout);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.configuration.Configuration.PROCESSOR_BASED_THREADPOOL_CORE_SIZE_DEFAULT, corePoolSize);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.configuration.Configuration.PROCESSOR_BASED_THREADPOOL_MAX_SIZE_DEFAULT, maxPoolSize);
        junit.framework.Assert.assertEquals(maxPoolSize * 10, workerQueueSize);
        junit.framework.Assert.assertTrue(priority <= java.lang.Thread.NORM_PRIORITY);
        junit.framework.Assert.assertTrue(priority > java.lang.Thread.MIN_PRIORITY);
        junit.framework.Assert.assertTrue(cacheTimeout >= LOWEST_CACHE_TIMEOUT_MINUTES);
        junit.framework.Assert.assertTrue((corePoolSize > 2) && (corePoolSize <= 128));
        junit.framework.Assert.assertTrue((maxPoolSize > 2) && (maxPoolSize <= (processorCount * 4)));
        junit.framework.Assert.assertTrue(workerQueueSize >= (processorCount * 10));
    }

    @org.junit.Test
    public void testAllPropertiesHaveMarkdownDescriptions() throws java.lang.Exception {
        java.lang.reflect.Field[] fields = org.apache.ambari.server.configuration.Configuration.class.getDeclaredFields();
        for (java.lang.reflect.Field field : fields) {
            if (field.getType() != org.apache.ambari.server.configuration.Configuration.ConfigurationProperty.class) {
                continue;
            }
            org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<?> configurationProperty = ((org.apache.ambari.server.configuration.Configuration.ConfigurationProperty<?>) (field.get(null)));
            org.apache.ambari.annotations.Markdown markdown = field.getAnnotation(org.apache.ambari.annotations.Markdown.class);
            if (null == markdown) {
                org.apache.ambari.server.configuration.Configuration.ConfigurationMarkdown configMarkdown = field.getAnnotation(org.apache.ambari.server.configuration.Configuration.ConfigurationMarkdown.class);
                markdown = (configMarkdown != null) ? configMarkdown.markdown() : null;
            }
            junit.framework.Assert.assertNotNull(("The configuration property " + configurationProperty.getKey()) + " is missing the Markdown annotation", markdown);
            junit.framework.Assert.assertFalse(("The configuration property " + configurationProperty.getKey()) + " has a Markdown annotation with no description", org.apache.commons.lang.StringUtils.isEmpty(markdown.description()));
        }
    }

    @org.junit.Test(expected = java.lang.IllegalArgumentException.class)
    public void testRejectsInvalidDtKeySize() {
        java.util.Properties properties = new java.util.Properties();
        properties.put(org.apache.ambari.server.configuration.Configuration.TLS_EPHEMERAL_DH_KEY_SIZE.getKey(), "invalid");
        new org.apache.ambari.server.configuration.Configuration(properties).getTlsEphemeralDhKeySize();
    }

    @org.junit.Test
    public void testDefaultDhKeySizeIs2048() {
        java.util.Properties properties = new java.util.Properties();
        junit.framework.Assert.assertEquals(2048, new org.apache.ambari.server.configuration.Configuration(properties).getTlsEphemeralDhKeySize());
    }

    @org.junit.Test
    public void testOverridingDhtKeySize() {
        java.util.Properties properties = new java.util.Properties();
        properties.put(org.apache.ambari.server.configuration.Configuration.TLS_EPHEMERAL_DH_KEY_SIZE.getKey(), "1024");
        junit.framework.Assert.assertEquals(1024, new org.apache.ambari.server.configuration.Configuration(properties).getTlsEphemeralDhKeySize());
    }

    @org.junit.Test
    public void canReadNonLatin1Properties() {
        junit.framework.Assert.assertEquals("árvíztűrő tükörfúrógép", new org.apache.ambari.server.configuration.Configuration().getProperty("encoding.test"));
    }

    @org.junit.Test
    public void testRemovingAmbariProperties() throws java.lang.Exception {
        final java.io.File ambariPropertiesFile = new java.io.File(org.apache.ambari.server.configuration.Configuration.class.getClassLoader().getResource("ambari.properties").getPath());
        final java.lang.String originalContent = org.apache.commons.io.FileUtils.readFileToString(ambariPropertiesFile, com.google.common.base.Charsets.UTF_8);
        org.junit.Assert.assertTrue(originalContent.indexOf("testPropertyName") == (-1));
        try {
            final java.lang.String testambariProperties = "\ntestPropertyName1=testValue1\ntestPropertyName2=testValue2\ntestPropertyName3=testValue3";
            org.apache.commons.io.FileUtils.writeStringToFile(ambariPropertiesFile, testambariProperties, com.google.common.base.Charsets.UTF_8, true);
            org.junit.Assert.assertTrue(org.apache.commons.io.FileUtils.readFileToString(ambariPropertiesFile, com.google.common.base.Charsets.UTF_8).indexOf("testPropertyName1") > (-1));
            org.junit.Assert.assertTrue(org.apache.commons.io.FileUtils.readFileToString(ambariPropertiesFile, com.google.common.base.Charsets.UTF_8).indexOf("testPropertyName2") > (-1));
            org.junit.Assert.assertTrue(org.apache.commons.io.FileUtils.readFileToString(ambariPropertiesFile, com.google.common.base.Charsets.UTF_8).indexOf("testPropertyName3") > (-1));
            final org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration();
            configuration.removePropertiesFromAmbariProperties(java.util.Arrays.asList("testPropertyName2"));
            org.junit.Assert.assertTrue(org.apache.commons.io.FileUtils.readFileToString(ambariPropertiesFile, com.google.common.base.Charsets.UTF_8).indexOf("testPropertyName1") > (-1));
            org.junit.Assert.assertTrue(org.apache.commons.io.FileUtils.readFileToString(ambariPropertiesFile, com.google.common.base.Charsets.UTF_8).indexOf("testPropertyName2") == (-1));
            org.junit.Assert.assertTrue(org.apache.commons.io.FileUtils.readFileToString(ambariPropertiesFile, com.google.common.base.Charsets.UTF_8).indexOf("testPropertyName3") > (-1));
            configuration.removePropertiesFromAmbariProperties(java.util.Arrays.asList("testPropertyName3"));
            org.junit.Assert.assertTrue(org.apache.commons.io.FileUtils.readFileToString(ambariPropertiesFile, com.google.common.base.Charsets.UTF_8).indexOf("testPropertyName1") > (-1));
            org.junit.Assert.assertTrue(org.apache.commons.io.FileUtils.readFileToString(ambariPropertiesFile, com.google.common.base.Charsets.UTF_8).indexOf("testPropertyName2") == (-1));
            org.junit.Assert.assertTrue(org.apache.commons.io.FileUtils.readFileToString(ambariPropertiesFile, com.google.common.base.Charsets.UTF_8).indexOf("testPropertyName3") == (-1));
            configuration.removePropertiesFromAmbariProperties(java.util.Arrays.asList("testPropertyName1"));
            org.junit.Assert.assertTrue(org.apache.commons.io.FileUtils.readFileToString(ambariPropertiesFile, com.google.common.base.Charsets.UTF_8).indexOf("testPropertyName1") == (-1));
            org.junit.Assert.assertTrue(org.apache.commons.io.FileUtils.readFileToString(ambariPropertiesFile, com.google.common.base.Charsets.UTF_8).indexOf("testPropertyName2") == (-1));
            org.junit.Assert.assertTrue(org.apache.commons.io.FileUtils.readFileToString(ambariPropertiesFile, com.google.common.base.Charsets.UTF_8).indexOf("testPropertyName3") == (-1));
        } finally {
            org.apache.commons.io.FileUtils.writeStringToFile(ambariPropertiesFile, originalContent, com.google.common.base.Charsets.UTF_8);
        }
    }

    @org.junit.Test
    public void testMaxAuthenticationFailureConfiguration() {
        org.apache.ambari.server.configuration.Configuration configuration;
        configuration = new org.apache.ambari.server.configuration.Configuration();
        org.junit.Assert.assertEquals(0, configuration.getMaxAuthenticationFailures());
        java.util.Properties properties = new java.util.Properties();
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.MAX_LOCAL_AUTHENTICATION_FAILURES.getKey(), "10");
        configuration = new org.apache.ambari.server.configuration.Configuration(properties);
        org.junit.Assert.assertEquals(10, configuration.getMaxAuthenticationFailures());
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.MAX_LOCAL_AUTHENTICATION_FAILURES.getKey(), "not a number");
        configuration = new org.apache.ambari.server.configuration.Configuration(properties);
        try {
            configuration.getMaxAuthenticationFailures();
            junit.framework.Assert.fail("Expected NumberFormatException");
        } catch (java.lang.NumberFormatException e) {
        }
    }

    @org.junit.Test
    public void testServerShowErrorStacksEnabled() throws java.lang.Exception {
        final java.util.Properties ambariProperties = new java.util.Properties();
        ambariProperties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_SHOW_ERROR_STACKS.getKey(), "true");
        final org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        boolean result = configuration.isServerShowErrorStacks();
        junit.framework.Assert.assertTrue(result);
    }

    @org.junit.Test
    public void testServerShowErrorStacksDefault() throws java.lang.Exception {
        final java.util.Properties ambariProperties = new java.util.Properties();
        final org.apache.ambari.server.configuration.Configuration configuration = new org.apache.ambari.server.configuration.Configuration(ambariProperties);
        boolean result = configuration.isServerShowErrorStacks();
        junit.framework.Assert.assertEquals(result, java.lang.Boolean.parseBoolean(org.apache.ambari.server.configuration.Configuration.SERVER_SHOW_ERROR_STACKS.getDefaultValue()));
    }
}