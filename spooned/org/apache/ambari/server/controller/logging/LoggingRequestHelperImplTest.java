package org.apache.ambari.server.controller.logging;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
public class LoggingRequestHelperImplTest {
    private static final java.lang.String TEST_JSON_INPUT_TWO_LIST_ENTRIES = "{" + ((((((((((((((((((((((((((((((((((((((((((((((((((("  \"startIndex\" : 0," + "  \"pageSize\" : 5,") + "  \"totalCount\" : 10452,") + "  \"resultSize\" : 5,") + "  \"queryTimeMS\" : 1458148754113,") + "  \"logList\" : [ {") + "    \"cluster\" : \"clusterone\",") + "    \"method\" : \"chooseUnderReplicatedBlocks\",") + "    \"level\" : \"INFO\",") + "    \"event_count\" : 1,") + "    \"ip\" : \"192.168.1.1\",") + "    \"type\" : \"hdfs_namenode\",") + "    \"seq_num\" : 10584,") + "    \"path\" : \"/var/log/hadoop/hdfs/hadoop-hdfs-namenode-c6401.ambari.apache.org.log\",") + "    \"file\" : \"UnderReplicatedBlocks.java\",") + "    \"line_number\" : 394,") + "    \"host\" : \"c6401.ambari.apache.org\",") + "    \"log_message\" : \"chooseUnderReplicatedBlocks selected 2 blocks at priority level 0;  Total=2 Reset bookmarks? false\",") + "    \"logger_name\" : \"BlockStateChange\",") + "    \"id\" : \"9c5562fb-123f-47c8-aaf5-b5e407326c08\",") + "    \"message_md5\" : \"-3892769501348410581\",") + "    \"logtime\" : 1458148749036,") + "    \"event_md5\" : \"1458148749036-2417481968206345035\",") + "    \"logfile_line_number\" : 2084,") + "    \"_ttl_\" : \"+7DAYS\",") + "    \"_expire_at_\" : 1458753550322,") + "    \"_version_\" : 1528979784023932928") + "  }, {") + "    \"cluster\" : \"clusterone\",") + "    \"method\" : \"putMetrics\",") + "    \"level\" : \"WARN\",") + "    \"event_count\" : 1,") + "    \"ip\" : \"192.168.1.1\",") + "    \"type\" : \"yarn_resourcemanager\",") + "    \"seq_num\" : 10583,") + "    \"path\" : \"/var/log/hadoop-yarn/yarn/yarn-yarn-resourcemanager-c6401.ambari.apache.org.log\",") + "    \"file\" : \"HadoopTimelineMetricsSink.java\",") + "    \"line_number\" : 262,") + "    \"host\" : \"c6401.ambari.apache.org\",") + "    \"log_message\" : \"Unable to send metrics to collector by address:http://c6401.ambari.apache.org:6188/ws/v1/timeline/metrics\",") + "    \"logger_name\" : \"timeline.HadoopTimelineMetricsSink\",") + "    \"id\" : \"8361c5a9-5b1c-4f44-bc8f-4c6f07d94228\",") + "    \"message_md5\" : \"5942185045779825717\",") + "    \"logtime\" : 1458148746937,") + "    \"event_md5\" : \"14581487469371427138486123628676\",") + "    \"logfile_line_number\" : 549,") + "    \"_ttl_\" : \"+7DAYS\",") + "    \"_expire_at_\" : 1458753550322,") + "    \"_version_\" : 1528979784022884357") + "  }") + "]") + "}");

    private static final java.lang.String TEST_JSON_INPUT_LOG_FILES_MAP = "{" + (((((("\"hostLogFiles\":{" + "\"hdfs_namenode\": [") + "\"/var/log/hadoop/hdfs/hadoop-hdfs-namenode-c6401.ambari.apache.org.log\"") + "],\"logsearch_app\": [") + "\"/var/log/ambari-logsearch-portal/logsearch.json\"") + "]") + "}}");

    private static final java.lang.String TEST_JSON_INPUT_LOG_LEVEL_QUERY = "{\"pageSize\":\"0\",\"queryTimeMS\":\"1459970731998\",\"resultSize\":\"6\",\"startIndex\":\"0\",\"totalCount\":\"0\"," + (("\"vNameValues\":[{\"name\":\"FATAL\",\"value\":\"0\"},{\"name\":\"ERROR\",\"value\":\"0\"}," + "{\"name\":\"WARN\",\"value\":\"41\"},{\"name\":\"INFO\",\"value\":\"186\"},{\"name\":\"DEBUG\",\"value\":\"0\"},") + "{\"name\":\"TRACE\",\"value\":\"0\"}]}");

    private static final java.lang.String TEST_JSON_INPUT_NULL_LOG_LIST = "{\"startIndex\":0,\"pageSize\":0,\"totalCount\":0,\"resultSize\":0,\"sortType\":null,\"sortBy\":null,\"queryTimeMS\":1479850014987,\"logList\":null,\"listSize\":0}";

    private final java.lang.String EXPECTED_HOST_NAME = "c6401.ambari.apache.org";

    private final java.lang.String EXPECTED_PORT_NUMBER = "61888";

    private static final java.lang.String EXPECTED_USER_NAME = "admin-user";

    private static final java.lang.String EXPECTED_ADMIN_PASSWORD = "admin-pwd";

    private static final java.lang.String EXPECTED_PROTOCOL = "http";

    private static final java.lang.String EXPECTED_ENCODED_CREDENTIALS = org.apache.commons.codec.binary.Base64.encodeBase64String(((org.apache.ambari.server.controller.logging.LoggingRequestHelperImplTest.EXPECTED_USER_NAME + ":") + org.apache.ambari.server.controller.logging.LoggingRequestHelperImplTest.EXPECTED_ADMIN_PASSWORD).getBytes());

    @org.junit.Test
    public void testLogQueryRequestBasic() throws java.lang.Exception {
        org.apache.ambari.server.controller.logging.LoggingCookieStore.INSTANCE.getCookiesMap().clear();
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.security.encryption.CredentialStoreService credentialStoreServiceMock = mockSupport.createMock(org.apache.ambari.server.security.encryption.CredentialStoreService.class);
        org.apache.ambari.server.state.Cluster clusterMock = mockSupport.createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.NetworkConnection networkConnectionMock = mockSupport.createMock(org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.NetworkConnection.class);
        org.apache.ambari.server.state.Config adminPropertiesConfigMock = mockSupport.createMock(org.apache.ambari.server.state.Config.class);
        java.util.Map<java.lang.String, java.lang.String> testConfigProperties = new java.util.HashMap<>();
        testConfigProperties.put("logsearch_admin_username", org.apache.ambari.server.controller.logging.LoggingRequestHelperImplTest.EXPECTED_USER_NAME);
        testConfigProperties.put("logsearch_admin_password", org.apache.ambari.server.controller.logging.LoggingRequestHelperImplTest.EXPECTED_ADMIN_PASSWORD);
        testConfigProperties = java.util.Collections.unmodifiableMap(testConfigProperties);
        org.easymock.Capture<java.net.HttpURLConnection> captureURLConnection = org.easymock.EasyMock.newCapture();
        org.easymock.Capture<java.net.HttpURLConnection> captureURLConnectionForAuthentication = org.easymock.EasyMock.newCapture();
        EasyMock.expect(clusterMock.getDesiredConfigByType("logsearch-admin-json")).andReturn(adminPropertiesConfigMock).atLeastOnce();
        EasyMock.expect(clusterMock.getClusterName()).andReturn("clusterone").atLeastOnce();
        EasyMock.expect(adminPropertiesConfigMock.getProperties()).andReturn(testConfigProperties).atLeastOnce();
        EasyMock.expect(networkConnectionMock.readQueryResponseFromServer(EasyMock.capture(captureURLConnection))).andReturn(new java.lang.StringBuffer(org.apache.ambari.server.controller.logging.LoggingRequestHelperImplTest.TEST_JSON_INPUT_TWO_LIST_ENTRIES)).atLeastOnce();
        networkConnectionMock.setupBasicAuthentication(EasyMock.capture(captureURLConnectionForAuthentication), EasyMock.eq(org.apache.ambari.server.controller.logging.LoggingRequestHelperImplTest.EXPECTED_ENCODED_CREDENTIALS));
        mockSupport.replayAll();
        org.apache.ambari.server.controller.logging.LoggingRequestHelper helper = new org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl(EXPECTED_HOST_NAME, EXPECTED_PORT_NUMBER, org.apache.ambari.server.controller.logging.LoggingRequestHelperImplTest.EXPECTED_PROTOCOL, credentialStoreServiceMock, clusterMock, null, networkConnectionMock);
        org.apache.ambari.server.controller.logging.LogQueryResponse result = helper.sendQueryRequest(java.util.Collections.emptyMap());
        java.net.HttpURLConnection httpURLConnection = captureURLConnection.getValue();
        org.junit.Assert.assertEquals("URLConnection did not have the correct hostname information", EXPECTED_HOST_NAME, httpURLConnection.getURL().getHost());
        org.junit.Assert.assertEquals("URLConnection did not have the correct port information", EXPECTED_PORT_NUMBER, httpURLConnection.getURL().getPort() + "");
        org.junit.Assert.assertEquals("URLConnection did not have the expected http protocol scheme", "http", httpURLConnection.getURL().getProtocol());
        org.junit.Assert.assertEquals("URLConnection did not have the expected method set", "GET", httpURLConnection.getRequestMethod());
        org.junit.Assert.assertTrue("URLConnection's URL did not have the expected query parameter string", httpURLConnection.getURL().getQuery().contains("clusters=clusterone"));
        org.junit.Assert.assertSame("HttpUrlConnection instances passed into NetworkConnection mock should have been the same instance", httpURLConnection, captureURLConnectionForAuthentication.getValue());
        org.junit.Assert.assertNotNull("Response object should not be null", result);
        org.junit.Assert.assertEquals("startIndex not parsed properly", "0", result.getStartIndex());
        org.junit.Assert.assertEquals("pageSize not parsed properly", "5", result.getPageSize());
        org.junit.Assert.assertEquals("totalCount not parsed properly", "10452", result.getTotalCount());
        org.junit.Assert.assertEquals("resultSize not parsed properly", "5", result.getResultSize());
        org.junit.Assert.assertEquals("queryTimeMS not parsed properly", "1458148754113", result.getQueryTimeMS());
        org.junit.Assert.assertEquals("incorrect number of LogLineResult items parsed", 2, result.getListOfResults().size());
        java.util.List<org.apache.ambari.server.controller.logging.LogLineResult> listOfLineResults = result.getListOfResults();
        org.apache.ambari.server.controller.logging.LoggingRequestHelperImplTest.verifyFirstLine(listOfLineResults);
        org.apache.ambari.server.controller.logging.LoggingRequestHelperImplTest.verifySecondLine(listOfLineResults);
        mockSupport.verifyAll();
    }

    @org.junit.Test
    public void testLogLevelRequestBasic() throws java.lang.Exception {
        org.apache.ambari.server.controller.logging.LoggingCookieStore.INSTANCE.getCookiesMap().clear();
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.security.encryption.CredentialStoreService credentialStoreServiceMock = mockSupport.createMock(org.apache.ambari.server.security.encryption.CredentialStoreService.class);
        org.apache.ambari.server.state.Cluster clusterMock = mockSupport.createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.NetworkConnection networkConnectionMock = mockSupport.createMock(org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.NetworkConnection.class);
        org.apache.ambari.server.state.Config adminPropertiesConfigMock = mockSupport.createMock(org.apache.ambari.server.state.Config.class);
        java.util.Map<java.lang.String, java.lang.String> testConfigProperties = new java.util.HashMap<>();
        testConfigProperties.put("logsearch_admin_username", "admin-user");
        testConfigProperties.put("logsearch_admin_password", "admin-pwd");
        testConfigProperties = java.util.Collections.unmodifiableMap(testConfigProperties);
        org.easymock.Capture<java.net.HttpURLConnection> captureURLConnection = org.easymock.EasyMock.newCapture();
        org.easymock.Capture<java.net.HttpURLConnection> captureURLConnectionForAuthentication = org.easymock.EasyMock.newCapture();
        EasyMock.expect(clusterMock.getDesiredConfigByType("logsearch-admin-json")).andReturn(adminPropertiesConfigMock).atLeastOnce();
        EasyMock.expect(adminPropertiesConfigMock.getProperties()).andReturn(testConfigProperties).atLeastOnce();
        EasyMock.expect(networkConnectionMock.readQueryResponseFromServer(EasyMock.capture(captureURLConnection))).andReturn(new java.lang.StringBuffer(org.apache.ambari.server.controller.logging.LoggingRequestHelperImplTest.TEST_JSON_INPUT_LOG_LEVEL_QUERY)).atLeastOnce();
        networkConnectionMock.setupBasicAuthentication(EasyMock.capture(captureURLConnectionForAuthentication), EasyMock.eq(org.apache.ambari.server.controller.logging.LoggingRequestHelperImplTest.EXPECTED_ENCODED_CREDENTIALS));
        mockSupport.replayAll();
        org.apache.ambari.server.controller.logging.LoggingRequestHelper helper = new org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl(EXPECTED_HOST_NAME, EXPECTED_PORT_NUMBER, org.apache.ambari.server.controller.logging.LoggingRequestHelperImplTest.EXPECTED_PROTOCOL, credentialStoreServiceMock, clusterMock, null, networkConnectionMock);
        org.apache.ambari.server.controller.logging.LogLevelQueryResponse result = helper.sendLogLevelQueryRequest("hdfs_datanode", EXPECTED_HOST_NAME);
        java.net.HttpURLConnection httpURLConnection = captureURLConnection.getValue();
        org.junit.Assert.assertEquals("URLConnection did not have the correct hostname information", EXPECTED_HOST_NAME, httpURLConnection.getURL().getHost());
        org.junit.Assert.assertEquals("URLConnection did not have the correct port information", EXPECTED_PORT_NUMBER, httpURLConnection.getURL().getPort() + "");
        org.junit.Assert.assertEquals("URLConnection did not have the expected http protocol scheme", "http", httpURLConnection.getURL().getProtocol());
        org.junit.Assert.assertEquals("URLConnection did not have the expected method set", "GET", httpURLConnection.getRequestMethod());
        org.junit.Assert.assertSame("HttpUrlConnection instances passed into NetworkConnection mock should have been the same instance", httpURLConnection, captureURLConnectionForAuthentication.getValue());
        org.junit.Assert.assertNotNull("Response object should not be null", result);
        org.junit.Assert.assertEquals("startIndex not parsed properly", "0", result.getStartIndex());
        org.junit.Assert.assertEquals("pageSize not parsed properly", "0", result.getPageSize());
        org.junit.Assert.assertEquals("totalCount not parsed properly", "0", result.getTotalCount());
        org.junit.Assert.assertEquals("resultSize not parsed properly", "6", result.getResultSize());
        org.junit.Assert.assertEquals("queryTimeMS not parsed properly", "1459970731998", result.getQueryTimeMS());
        org.junit.Assert.assertEquals("Incorrect number of log level count items parsed", 6, result.getNameValueList().size());
        java.util.List<org.apache.ambari.server.controller.logging.NameValuePair> resultList = result.getNameValueList();
        org.apache.ambari.server.controller.logging.LoggingRequestHelperImplTest.assertNameValuePair("FATAL", "0", resultList.get(0));
        org.apache.ambari.server.controller.logging.LoggingRequestHelperImplTest.assertNameValuePair("ERROR", "0", resultList.get(1));
        org.apache.ambari.server.controller.logging.LoggingRequestHelperImplTest.assertNameValuePair("WARN", "41", resultList.get(2));
        org.apache.ambari.server.controller.logging.LoggingRequestHelperImplTest.assertNameValuePair("INFO", "186", resultList.get(3));
        org.apache.ambari.server.controller.logging.LoggingRequestHelperImplTest.assertNameValuePair("DEBUG", "0", resultList.get(4));
        org.apache.ambari.server.controller.logging.LoggingRequestHelperImplTest.assertNameValuePair("TRACE", "0", resultList.get(5));
        mockSupport.verifyAll();
    }

    @org.junit.Test
    public void testLogFileNameRequestBasic() throws java.lang.Exception {
        org.apache.ambari.server.controller.logging.LoggingCookieStore.INSTANCE.getCookiesMap().clear();
        final java.lang.String expectedComponentName = "hdfs_namenode";
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.security.encryption.CredentialStoreService credentialStoreServiceMock = mockSupport.createMock(org.apache.ambari.server.security.encryption.CredentialStoreService.class);
        org.apache.ambari.server.state.Cluster clusterMock = mockSupport.createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.NetworkConnection networkConnectionMock = mockSupport.createMock(org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.NetworkConnection.class);
        org.apache.ambari.server.state.Config adminPropertiesConfigMock = mockSupport.createMock(org.apache.ambari.server.state.Config.class);
        java.util.Map<java.lang.String, java.lang.String> testConfigProperties = new java.util.HashMap<>();
        testConfigProperties.put("logsearch_admin_username", "admin-user");
        testConfigProperties.put("logsearch_admin_password", "admin-pwd");
        testConfigProperties = java.util.Collections.unmodifiableMap(testConfigProperties);
        org.easymock.Capture<java.net.HttpURLConnection> captureURLConnection = org.easymock.EasyMock.newCapture();
        org.easymock.Capture<java.net.HttpURLConnection> captureURLConnectionForAuthentication = org.easymock.EasyMock.newCapture();
        EasyMock.expect(clusterMock.getDesiredConfigByType("logsearch-admin-json")).andReturn(adminPropertiesConfigMock).atLeastOnce();
        EasyMock.expect(clusterMock.getClusterName()).andReturn("clusterone").atLeastOnce();
        EasyMock.expect(adminPropertiesConfigMock.getProperties()).andReturn(testConfigProperties).atLeastOnce();
        EasyMock.expect(networkConnectionMock.readQueryResponseFromServer(EasyMock.capture(captureURLConnection))).andReturn(new java.lang.StringBuffer(org.apache.ambari.server.controller.logging.LoggingRequestHelperImplTest.TEST_JSON_INPUT_LOG_FILES_MAP)).atLeastOnce();
        networkConnectionMock.setupBasicAuthentication(EasyMock.capture(captureURLConnectionForAuthentication), EasyMock.eq(org.apache.ambari.server.controller.logging.LoggingRequestHelperImplTest.EXPECTED_ENCODED_CREDENTIALS));
        mockSupport.replayAll();
        org.apache.ambari.server.controller.logging.LoggingRequestHelper helper = new org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl(EXPECTED_HOST_NAME, EXPECTED_PORT_NUMBER, org.apache.ambari.server.controller.logging.LoggingRequestHelperImplTest.EXPECTED_PROTOCOL, credentialStoreServiceMock, clusterMock, null, networkConnectionMock);
        org.apache.ambari.server.controller.logging.HostLogFilesResponse result = helper.sendGetLogFileNamesRequest(EXPECTED_HOST_NAME);
        java.net.HttpURLConnection httpURLConnection = captureURLConnection.getValue();
        org.junit.Assert.assertEquals("URLConnection did not have the correct hostname information", EXPECTED_HOST_NAME, httpURLConnection.getURL().getHost());
        org.junit.Assert.assertEquals("URLConnection did not have the correct port information", EXPECTED_PORT_NUMBER, httpURLConnection.getURL().getPort() + "");
        org.junit.Assert.assertEquals("URLConnection did not have the expected http protocol scheme", "http", httpURLConnection.getURL().getProtocol());
        org.junit.Assert.assertEquals("URLConnection did not have the expected method set", "GET", httpURLConnection.getRequestMethod());
        org.junit.Assert.assertTrue("URLConnection's URL did not have the expected query parameter string", httpURLConnection.getURL().getQuery().contains("clusters=clusterone"));
        org.junit.Assert.assertSame("HttpUrlConnection instances passed into NetworkConnection mock should have been the same instance", httpURLConnection, captureURLConnectionForAuthentication.getValue());
        final java.lang.String resultQuery = httpURLConnection.getURL().getQuery();
        org.junit.Assert.assertTrue("host_name parameter was not included in query", resultQuery.contains("host_name=c6401.ambari.apache.org"));
        org.junit.Assert.assertNotNull("Response object should not be null", result);
        org.junit.Assert.assertEquals("Response Set was not of the expected size", 2, result.getHostLogFiles().size());
        org.junit.Assert.assertEquals("Response did not include the expected file name", "/var/log/hadoop/hdfs/hadoop-hdfs-namenode-c6401.ambari.apache.org.log", result.getHostLogFiles().get(expectedComponentName).get(0));
        mockSupport.verifyAll();
    }

    @org.junit.Test
    public void testLogFileNameRequestWithNullLogList() throws java.lang.Exception {
        final java.lang.String expectedComponentName = "hdfs_namenode";
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.security.encryption.CredentialStoreService credentialStoreServiceMock = mockSupport.createMock(org.apache.ambari.server.security.encryption.CredentialStoreService.class);
        org.apache.ambari.server.state.Cluster clusterMock = mockSupport.createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.NetworkConnection networkConnectionMock = mockSupport.createMock(org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.NetworkConnection.class);
        org.apache.ambari.server.state.Config adminPropertiesConfigMock = mockSupport.createMock(org.apache.ambari.server.state.Config.class);
        java.util.Map<java.lang.String, java.lang.String> testConfigProperties = new java.util.HashMap<>();
        testConfigProperties.put("logsearch_admin_username", "admin-user");
        testConfigProperties.put("logsearch_admin_password", "admin-pwd");
        testConfigProperties = java.util.Collections.unmodifiableMap(testConfigProperties);
        org.easymock.Capture<java.net.HttpURLConnection> captureURLConnection = new org.easymock.Capture<>();
        org.easymock.Capture<java.net.HttpURLConnection> captureURLConnectionForAuthentication = new org.easymock.Capture<>();
        EasyMock.expect(clusterMock.getDesiredConfigByType("logsearch-admin-json")).andReturn(adminPropertiesConfigMock).atLeastOnce();
        EasyMock.expect(clusterMock.getClusterName()).andReturn("clusterone").atLeastOnce();
        EasyMock.expect(adminPropertiesConfigMock.getProperties()).andReturn(testConfigProperties).atLeastOnce();
        EasyMock.expect(networkConnectionMock.readQueryResponseFromServer(EasyMock.capture(captureURLConnection))).andReturn(new java.lang.StringBuffer(org.apache.ambari.server.controller.logging.LoggingRequestHelperImplTest.TEST_JSON_INPUT_NULL_LOG_LIST)).atLeastOnce();
        networkConnectionMock.setupBasicAuthentication(EasyMock.capture(captureURLConnectionForAuthentication), EasyMock.eq(org.apache.ambari.server.controller.logging.LoggingRequestHelperImplTest.EXPECTED_ENCODED_CREDENTIALS));
        mockSupport.replayAll();
        org.apache.ambari.server.controller.logging.LoggingRequestHelper helper = new org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl(EXPECTED_HOST_NAME, EXPECTED_PORT_NUMBER, org.apache.ambari.server.controller.logging.LoggingRequestHelperImplTest.EXPECTED_PROTOCOL, credentialStoreServiceMock, clusterMock, null, networkConnectionMock);
        org.apache.ambari.server.controller.logging.HostLogFilesResponse result = helper.sendGetLogFileNamesRequest(EXPECTED_HOST_NAME);
        java.net.HttpURLConnection httpURLConnection = captureURLConnection.getValue();
        org.junit.Assert.assertEquals("URLConnection did not have the correct hostname information", EXPECTED_HOST_NAME, httpURLConnection.getURL().getHost());
        org.junit.Assert.assertEquals("URLConnection did not have the correct port information", EXPECTED_PORT_NUMBER, httpURLConnection.getURL().getPort() + "");
        org.junit.Assert.assertEquals("URLConnection did not have the expected http protocol scheme", "http", httpURLConnection.getURL().getProtocol());
        org.junit.Assert.assertEquals("URLConnection did not have the expected method set", "GET", httpURLConnection.getRequestMethod());
        org.junit.Assert.assertTrue("URLConnection's URL did not have the expected query parameter string", httpURLConnection.getURL().getQuery().contains("clusters=clusterone"));
        org.junit.Assert.assertSame("HttpUrlConnection instances passed into NetworkConnection mock should have been the same instance", httpURLConnection, captureURLConnectionForAuthentication.getValue());
        final java.lang.String resultQuery = httpURLConnection.getURL().getQuery();
        org.junit.Assert.assertTrue("host_name parameter was not included in query", resultQuery.contains("host_name=c6401.ambari.apache.org"));
        org.junit.Assert.assertNotNull("Response object should not be null", result);
        org.junit.Assert.assertNull("Response Map should be null", result.getHostLogFiles());
        mockSupport.verifyAll();
    }

    @org.junit.Test
    public void testLogQueryRequestBasicCredentialsNotInConfig() throws java.lang.Exception {
        org.apache.ambari.server.controller.logging.LoggingCookieStore.INSTANCE.getCookiesMap().clear();
        final java.lang.String expectedClusterName = "my-test-cluster";
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.security.encryption.CredentialStoreService credentialStoreServiceMock = mockSupport.createMock(org.apache.ambari.server.security.encryption.CredentialStoreService.class);
        org.apache.ambari.server.state.Cluster clusterMock = mockSupport.createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.NetworkConnection networkConnectionMock = mockSupport.createMock(org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.NetworkConnection.class);
        org.apache.ambari.server.state.Config adminPropertiesConfigMock = mockSupport.createMock(org.apache.ambari.server.state.Config.class);
        org.easymock.Capture<java.net.HttpURLConnection> captureURLConnection = org.easymock.EasyMock.newCapture();
        org.easymock.Capture<java.net.HttpURLConnection> captureURLConnectionForAuthentication = org.easymock.EasyMock.newCapture();
        EasyMock.expect(clusterMock.getDesiredConfigByType("logsearch-admin-json")).andReturn(adminPropertiesConfigMock).atLeastOnce();
        EasyMock.expect(clusterMock.getClusterName()).andReturn(expectedClusterName).atLeastOnce();
        EasyMock.expect(adminPropertiesConfigMock.getProperties()).andReturn(java.util.Collections.emptyMap()).atLeastOnce();
        EasyMock.expect(networkConnectionMock.readQueryResponseFromServer(EasyMock.capture(captureURLConnection))).andReturn(new java.lang.StringBuffer(org.apache.ambari.server.controller.logging.LoggingRequestHelperImplTest.TEST_JSON_INPUT_TWO_LIST_ENTRIES)).atLeastOnce();
        EasyMock.expect(credentialStoreServiceMock.getCredential(expectedClusterName, "logsearch.admin.credential")).andReturn(new org.apache.ambari.server.security.credential.PrincipalKeyCredential(org.apache.ambari.server.controller.logging.LoggingRequestHelperImplTest.EXPECTED_USER_NAME, org.apache.ambari.server.controller.logging.LoggingRequestHelperImplTest.EXPECTED_ADMIN_PASSWORD)).atLeastOnce();
        networkConnectionMock.setupBasicAuthentication(EasyMock.capture(captureURLConnectionForAuthentication), EasyMock.eq(org.apache.ambari.server.controller.logging.LoggingRequestHelperImplTest.EXPECTED_ENCODED_CREDENTIALS));
        mockSupport.replayAll();
        org.apache.ambari.server.controller.logging.LoggingRequestHelper helper = new org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl(EXPECTED_HOST_NAME, EXPECTED_PORT_NUMBER, org.apache.ambari.server.controller.logging.LoggingRequestHelperImplTest.EXPECTED_PROTOCOL, credentialStoreServiceMock, clusterMock, null, networkConnectionMock);
        org.apache.ambari.server.controller.logging.LogQueryResponse result = helper.sendQueryRequest(java.util.Collections.emptyMap());
        java.net.HttpURLConnection httpURLConnection = captureURLConnection.getValue();
        org.junit.Assert.assertEquals("URLConnection did not have the correct hostname information", EXPECTED_HOST_NAME, httpURLConnection.getURL().getHost());
        org.junit.Assert.assertEquals("URLConnection did not have the correct port information", EXPECTED_PORT_NUMBER, httpURLConnection.getURL().getPort() + "");
        org.junit.Assert.assertEquals("URLConnection did not have the expected http protocol scheme", "http", httpURLConnection.getURL().getProtocol());
        org.junit.Assert.assertEquals("URLConnection did not have the expected method set", "GET", httpURLConnection.getRequestMethod());
        org.junit.Assert.assertSame("HttpUrlConnection instances passed into NetworkConnection mock should have been the same instance", httpURLConnection, captureURLConnectionForAuthentication.getValue());
        org.junit.Assert.assertNotNull("Response object should not be null", result);
        org.junit.Assert.assertEquals("startIndex not parsed properly", "0", result.getStartIndex());
        org.junit.Assert.assertEquals("pageSize not parsed properly", "5", result.getPageSize());
        org.junit.Assert.assertEquals("totalCount not parsed properly", "10452", result.getTotalCount());
        org.junit.Assert.assertEquals("resultSize not parsed properly", "5", result.getResultSize());
        org.junit.Assert.assertEquals("queryTimeMS not parsed properly", "1458148754113", result.getQueryTimeMS());
        org.junit.Assert.assertEquals("incorrect number of LogLineResult items parsed", 2, result.getListOfResults().size());
        java.util.List<org.apache.ambari.server.controller.logging.LogLineResult> listOfLineResults = result.getListOfResults();
        org.apache.ambari.server.controller.logging.LoggingRequestHelperImplTest.verifyFirstLine(listOfLineResults);
        org.apache.ambari.server.controller.logging.LoggingRequestHelperImplTest.verifySecondLine(listOfLineResults);
        mockSupport.verifyAll();
    }

    private static void verifyFirstLine(java.util.List<org.apache.ambari.server.controller.logging.LogLineResult> listOfLineResults) {
        org.apache.ambari.server.controller.logging.LogLineResult resultOne = listOfLineResults.get(0);
        org.junit.Assert.assertEquals("Cluster name not parsed properly", "clusterone", resultOne.getClusterName());
        org.junit.Assert.assertEquals("Method Name not parsed properly", "chooseUnderReplicatedBlocks", resultOne.getLogMethod());
        org.junit.Assert.assertEquals("Log Level not parsed properly", "INFO", resultOne.getLogLevel());
        org.junit.Assert.assertEquals("event_count not parsed properly", "1", resultOne.getEventCount());
        org.junit.Assert.assertEquals("ip address not parsed properly", "192.168.1.1", resultOne.getIpAddress());
        org.junit.Assert.assertEquals("component type not parsed properly", "hdfs_namenode", resultOne.getComponentType());
        org.junit.Assert.assertEquals("sequence number not parsed properly", "10584", resultOne.getSequenceNumber());
        org.junit.Assert.assertEquals("log file path not parsed properly", "/var/log/hadoop/hdfs/hadoop-hdfs-namenode-c6401.ambari.apache.org.log", resultOne.getLogFilePath());
        org.junit.Assert.assertEquals("log src file name not parsed properly", "UnderReplicatedBlocks.java", resultOne.getSourceFile());
        org.junit.Assert.assertEquals("log src line number not parsed properly", "394", resultOne.getSourceFileLineNumber());
        org.junit.Assert.assertEquals("host name not parsed properly", "c6401.ambari.apache.org", resultOne.getHostName());
        org.junit.Assert.assertEquals("log message not parsed properly", "chooseUnderReplicatedBlocks selected 2 blocks at priority level 0;  Total=2 Reset bookmarks? false", resultOne.getLogMessage());
        org.junit.Assert.assertEquals("logger name not parsed properly", "BlockStateChange", resultOne.getLoggerName());
        org.junit.Assert.assertEquals("id not parsed properly", "9c5562fb-123f-47c8-aaf5-b5e407326c08", resultOne.getId());
        org.junit.Assert.assertEquals("message MD5 not parsed properly", "-3892769501348410581", resultOne.getMessageMD5());
        org.junit.Assert.assertEquals("log time not parsed properly", "1458148749036", resultOne.getLogTime());
        org.junit.Assert.assertEquals("event MD5 not parsed properly", "1458148749036-2417481968206345035", resultOne.getEventMD5());
        org.junit.Assert.assertEquals("logfile line number not parsed properly", "2084", resultOne.getLogFileLineNumber());
        org.junit.Assert.assertEquals("ttl not parsed properly", "+7DAYS", resultOne.getTtl());
        org.junit.Assert.assertEquals("expire at not parsed properly", "1458753550322", resultOne.getExpirationTime());
        org.junit.Assert.assertEquals("version not parsed properly", "1528979784023932928", resultOne.getVersion());
    }

    private static void verifySecondLine(java.util.List<org.apache.ambari.server.controller.logging.LogLineResult> listOfLineResults) {
        org.apache.ambari.server.controller.logging.LogLineResult resultTwo = listOfLineResults.get(1);
        org.junit.Assert.assertEquals("Cluster name not parsed properly", "clusterone", resultTwo.getClusterName());
        org.junit.Assert.assertEquals("Method Name not parsed properly", "putMetrics", resultTwo.getLogMethod());
        org.junit.Assert.assertEquals("Log Level not parsed properly", "WARN", resultTwo.getLogLevel());
        org.junit.Assert.assertEquals("event_count not parsed properly", "1", resultTwo.getEventCount());
        org.junit.Assert.assertEquals("ip address not parsed properly", "192.168.1.1", resultTwo.getIpAddress());
        org.junit.Assert.assertEquals("component type not parsed properly", "yarn_resourcemanager", resultTwo.getComponentType());
        org.junit.Assert.assertEquals("sequence number not parsed properly", "10583", resultTwo.getSequenceNumber());
        org.junit.Assert.assertEquals("log file path not parsed properly", "/var/log/hadoop-yarn/yarn/yarn-yarn-resourcemanager-c6401.ambari.apache.org.log", resultTwo.getLogFilePath());
        org.junit.Assert.assertEquals("log src file name not parsed properly", "HadoopTimelineMetricsSink.java", resultTwo.getSourceFile());
        org.junit.Assert.assertEquals("log src line number not parsed properly", "262", resultTwo.getSourceFileLineNumber());
        org.junit.Assert.assertEquals("host name not parsed properly", "c6401.ambari.apache.org", resultTwo.getHostName());
        org.junit.Assert.assertEquals("log message not parsed properly", "Unable to send metrics to collector by address:http://c6401.ambari.apache.org:6188/ws/v1/timeline/metrics", resultTwo.getLogMessage());
        org.junit.Assert.assertEquals("logger name not parsed properly", "timeline.HadoopTimelineMetricsSink", resultTwo.getLoggerName());
        org.junit.Assert.assertEquals("id not parsed properly", "8361c5a9-5b1c-4f44-bc8f-4c6f07d94228", resultTwo.getId());
        org.junit.Assert.assertEquals("message MD5 not parsed properly", "5942185045779825717", resultTwo.getMessageMD5());
        org.junit.Assert.assertEquals("log time not parsed properly", "1458148746937", resultTwo.getLogTime());
        org.junit.Assert.assertEquals("event MD5 not parsed properly", "14581487469371427138486123628676", resultTwo.getEventMD5());
        org.junit.Assert.assertEquals("logfile line number not parsed properly", "549", resultTwo.getLogFileLineNumber());
        org.junit.Assert.assertEquals("ttl not parsed properly", "+7DAYS", resultTwo.getTtl());
        org.junit.Assert.assertEquals("expire at not parsed properly", "1458753550322", resultTwo.getExpirationTime());
        org.junit.Assert.assertEquals("version not parsed properly", "1528979784022884357", resultTwo.getVersion());
    }

    @org.junit.Test
    public void testCreateLogFileTailURI() throws java.lang.Exception {
        org.apache.ambari.server.controller.logging.LoggingCookieStore.INSTANCE.getCookiesMap().clear();
        final java.lang.String expectedHostName = "c6401.ambari.apache.org";
        final java.lang.String expectedPort = "61888";
        final java.lang.String expectedComponentName = "hdfs_namenode";
        final java.lang.String expectedBaseURI = ((("http://" + expectedHostName) + ":") + expectedPort) + "/api/v1/clusters/clusterone/logging/searchEngine";
        final java.lang.String expectedTailFileURI = ((((expectedBaseURI + "?component_name=") + expectedComponentName) + "&host_name=") + expectedHostName) + "&pageSize=50";
        org.easymock.EasyMockSupport mockSupport = new org.easymock.EasyMockSupport();
        org.apache.ambari.server.security.encryption.CredentialStoreService credentialStoreServiceMock = mockSupport.createMock(org.apache.ambari.server.security.encryption.CredentialStoreService.class);
        org.apache.ambari.server.state.Cluster clusterMock = mockSupport.createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.NetworkConnection networkConnectionMock = mockSupport.createMock(org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl.NetworkConnection.class);
        mockSupport.replayAll();
        org.apache.ambari.server.controller.logging.LoggingRequestHelper helper = new org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl("c6401.ambari.apache.org", "61888", "http", credentialStoreServiceMock, clusterMock, null, networkConnectionMock);
        java.lang.String result = helper.createLogFileTailURI(expectedBaseURI, expectedComponentName, expectedHostName);
        org.junit.Assert.assertEquals("LogFile Tail URI was not generated as expected", expectedTailFileURI, result);
        mockSupport.verifyAll();
    }

    static void assertNameValuePair(java.lang.String expectedName, java.lang.String expectedValue, org.apache.ambari.server.controller.logging.NameValuePair nameValuePair) {
        org.junit.Assert.assertEquals("Unexpected name found in this pair", expectedName, nameValuePair.getName());
        org.junit.Assert.assertEquals("Unexpected value found in this pair", expectedValue, nameValuePair.getValue());
    }
}