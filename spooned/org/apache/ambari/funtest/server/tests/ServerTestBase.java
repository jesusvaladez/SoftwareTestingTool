package org.apache.ambari.funtest.server.tests;
import com.google.inject.persist.PersistService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
public class ServerTestBase {
    private static org.apache.commons.logging.Log LOG = org.apache.commons.logging.LogFactory.getLog(org.apache.ambari.funtest.server.tests.ServerTestBase.class);

    protected static java.lang.Thread serverThread = null;

    protected static org.apache.ambari.funtest.server.LocalAmbariServer server = null;

    protected static int serverPort = 9995;

    protected static int serverAgentPort = 9000;

    protected static com.google.inject.Injector injector = null;

    protected static java.lang.String SERVER_URL_FORMAT = "http://localhost:%d";

    private static boolean isInitialized;

    @org.junit.BeforeClass
    public static void setupTest() throws java.lang.Exception {
        if (!org.apache.ambari.funtest.server.tests.ServerTestBase.isInitialized) {
            java.util.Properties properties = org.apache.ambari.funtest.server.tests.ServerTestBase.readConfigFile();
            properties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_PERSISTENCE_TYPE.getKey(), "remote");
            properties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_URL.getKey(), org.apache.ambari.server.configuration.Configuration.JDBC_IN_MEMORY_URL);
            properties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_DRIVER.getKey(), org.apache.ambari.server.configuration.Configuration.JDBC_IN_MEMORY_DRIVER);
            properties.setProperty(org.apache.ambari.server.configuration.Configuration.OS_VERSION.getKey(), "centos7");
            properties.setProperty(org.apache.ambari.server.configuration.Configuration.AGENT_USE_SSL.getKey(), "false");
            properties.setProperty(org.apache.ambari.server.configuration.Configuration.CLIENT_API_PORT.getKey(), java.lang.Integer.toString(org.apache.ambari.funtest.server.tests.ServerTestBase.serverPort));
            properties.setProperty(org.apache.ambari.server.configuration.Configuration.SRVR_ONE_WAY_SSL_PORT.getKey(), java.lang.Integer.toString(org.apache.ambari.funtest.server.tests.ServerTestBase.serverAgentPort));
            java.lang.String tmpDir = java.lang.System.getProperty("java.io.tmpdir");
            properties.setProperty(org.apache.ambari.server.configuration.Configuration.SRVR_KSTR_DIR.getKey(), tmpDir);
            org.apache.ambari.server.controller.ControllerModule testModule = new org.apache.ambari.server.controller.ControllerModule(properties);
            org.apache.ambari.funtest.server.tests.ServerTestBase.injector = com.google.inject.Guice.createInjector(testModule, new org.apache.ambari.server.audit.AuditLoggerModule(), new org.apache.ambari.server.ldap.LdapModule());
            org.apache.ambari.funtest.server.tests.ServerTestBase.injector.getInstance(com.google.inject.persist.PersistService.class).start();
            org.apache.ambari.funtest.server.tests.ServerTestBase.initDB();
            org.apache.ambari.funtest.server.tests.ServerTestBase.server = org.apache.ambari.funtest.server.tests.ServerTestBase.injector.getInstance(org.apache.ambari.funtest.server.LocalAmbariServer.class);
            org.apache.ambari.funtest.server.tests.ServerTestBase.serverThread = new java.lang.Thread(org.apache.ambari.funtest.server.tests.ServerTestBase.server);
            org.apache.ambari.funtest.server.tests.ServerTestBase.serverThread.start();
            org.apache.ambari.funtest.server.tests.ServerTestBase.waitForServer();
            org.apache.ambari.funtest.server.tests.ServerTestBase.isInitialized = true;
        }
    }

    protected static java.lang.String getBasicAdminAuthentication() {
        java.lang.String authString = (org.apache.ambari.funtest.server.tests.ServerTestBase.getAdminUserName() + ":") + org.apache.ambari.funtest.server.tests.ServerTestBase.getAdminPassword();
        byte[] authEncBytes = org.apache.commons.codec.binary.Base64.encodeBase64(authString.getBytes());
        java.lang.String authStringEnc = new java.lang.String(authEncBytes);
        return "Basic " + authStringEnc;
    }

    protected static void initDB() throws java.io.IOException, java.sql.SQLException {
        org.apache.ambari.funtest.server.tests.ServerTestBase.createSourceDatabase();
    }

    protected static void dropDatabase() throws java.lang.ClassNotFoundException, java.sql.SQLException {
        java.lang.String DROP_DERBY_URL = "jdbc:derby:memory:myDB/ambari;drop=true";
        java.lang.Class.forName(org.apache.ambari.server.configuration.Configuration.JDBC_IN_MEMORY_DRIVER);
        try {
            java.sql.DriverManager.getConnection(DROP_DERBY_URL);
        } catch (java.sql.SQLNonTransientConnectionException ignored) {
            org.apache.ambari.funtest.server.tests.ServerTestBase.LOG.info("Database dropped ", ignored);
        }
    }

    private static void createSourceDatabase() throws java.io.IOException, java.sql.SQLException {
        java.io.File projectDir = new java.io.File(java.lang.System.getProperty("user.dir"));
        java.io.File ddlFile = new java.io.File(projectDir.getParentFile(), "ambari-server/src/main/resources/Ambari-DDL-H2-CREATE.sql");
        java.lang.String ddlFilename = ddlFile.getPath();
        org.apache.ambari.server.orm.DBAccessor dbAccessor = org.apache.ambari.funtest.server.tests.ServerTestBase.injector.getInstance(org.apache.ambari.server.orm.DBAccessor.class);
        dbAccessor.executeScript(ddlFilename);
    }

    protected static java.lang.String getAdminUserName() {
        return "admin";
    }

    protected static java.lang.String getAdminPassword() {
        return "admin";
    }

    private static void waitForServer() throws java.lang.Exception {
        int count = 1;
        while (!org.apache.ambari.funtest.server.tests.ServerTestBase.isServerUp()) {
            org.apache.ambari.funtest.server.tests.ServerTestBase.serverThread.join(count * 10000);
        } 
    }

    private static boolean isServerUp() throws java.io.IOException {
        java.lang.String apiPath = "/api/v1/stacks";
        java.lang.String apiUrl = java.lang.String.format(org.apache.ambari.funtest.server.tests.ServerTestBase.SERVER_URL_FORMAT, org.apache.ambari.funtest.server.tests.ServerTestBase.serverPort) + apiPath;
        org.apache.http.impl.client.CloseableHttpClient httpClient = org.apache.http.impl.client.HttpClients.createDefault();
        try {
            org.apache.http.client.methods.HttpGet httpGet = new org.apache.http.client.methods.HttpGet(apiUrl);
            httpGet.addHeader("Authorization", org.apache.ambari.funtest.server.tests.ServerTestBase.getBasicAdminAuthentication());
            httpGet.addHeader("X-Requested-By", "ambari");
            org.apache.http.HttpResponse httpResponse = httpClient.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            org.apache.http.HttpEntity entity = httpResponse.getEntity();
            java.lang.String responseBody = (entity != null) ? org.apache.http.util.EntityUtils.toString(entity) : null;
            return true;
        } catch (java.io.IOException ex) {
        } finally {
            httpClient.close();
        }
        return false;
    }

    @org.junit.Before
    public void setup() throws java.lang.Exception {
    }

    @org.junit.After
    public void teardown() throws java.lang.Exception {
    }

    private static java.util.Properties readConfigFile() {
        java.util.Properties properties = new java.util.Properties();
        java.lang.String configFileName = "ambari.properties";
        java.io.InputStream inputStream = org.apache.ambari.server.configuration.Configuration.class.getClassLoader().getResourceAsStream(configFileName);
        if (inputStream == null) {
            throw new java.lang.RuntimeException(configFileName + " not found in classpath");
        }
        try {
            properties.load(new java.io.InputStreamReader(inputStream, com.google.common.base.Charsets.UTF_8));
            inputStream.close();
        } catch (java.io.FileNotFoundException fnf) {
            org.apache.ambari.funtest.server.tests.ServerTestBase.LOG.info(("No configuration file " + configFileName) + " found in classpath.", fnf);
        } catch (java.io.IOException ie) {
            throw new java.lang.IllegalArgumentException("Can't read configuration file " + configFileName, ie);
        }
        return properties;
    }
}