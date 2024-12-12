package org.apache.ambari.funtest.server.tests;
import org.apache.http.HttpStatus;
public class RoleBasedAccessControlBasicTest extends org.apache.ambari.funtest.server.tests.ServerTestBase {
    private java.lang.String clusterName = "c1";

    private java.lang.String hostName = "host1";

    private java.lang.String clusterVersion = "HDP-2.2.0";

    private static org.apache.commons.logging.Log LOG = org.apache.commons.logging.LogFactory.getLog(org.apache.ambari.funtest.server.tests.RoleBasedAccessControlBasicTest.class);

    @java.lang.Override
    public void setup() throws java.lang.Exception {
        super.setup();
        setupCluster();
    }

    @java.lang.Override
    public void teardown() throws java.lang.Exception {
        teardownCluster();
        super.teardown();
    }

    @org.junit.Test
    public void testGetClustersAsAnonUser() throws java.lang.Exception {
        com.google.gson.JsonElement jsonResponse;
        org.apache.ambari.funtest.server.ConnectionParams adminConnectionParams = createAdminConnectionParams();
        java.lang.String anonUserName = "nothing";
        java.lang.String anonUserPwd = "nothing";
        org.apache.ambari.funtest.server.utils.ClusterUtils.createUser(adminConnectionParams, clusterName, anonUserName, anonUserPwd, org.apache.ambari.funtest.server.AmbariUserRole.NONE);
        org.apache.ambari.funtest.server.ConnectionParams anonUserParams = createConnectionParams(anonUserName, anonUserPwd);
        jsonResponse = org.apache.ambari.funtest.server.utils.RestApiUtils.executeRequest(new org.apache.ambari.funtest.server.api.cluster.GetAllClustersWebRequest(anonUserParams));
        org.junit.Assert.assertFalse(jsonResponse.isJsonNull());
        jsonResponse = org.apache.ambari.funtest.server.utils.RestApiUtils.executeRequest(new org.apache.ambari.funtest.server.api.user.DeleteUserWebRequest(adminConnectionParams, anonUserName));
        org.apache.ambari.funtest.server.tests.RoleBasedAccessControlBasicTest.LOG.info(jsonResponse);
    }

    @org.junit.Test
    public void testAddClusterConfigAsAnonUser() throws java.lang.Exception {
        org.apache.ambari.funtest.server.ConnectionParams adminConnectionParams = createAdminConnectionParams();
        java.lang.String anonUserName = "nothing";
        java.lang.String anonUserPwd = "nothing";
        org.apache.ambari.funtest.server.utils.ClusterUtils.createUser(adminConnectionParams, clusterName, anonUserName, anonUserPwd, org.apache.ambari.funtest.server.AmbariUserRole.NONE);
        java.lang.String configType = "test-hadoop-env";
        java.lang.String configTag = "version1";
        org.apache.ambari.funtest.server.ClusterConfigParams configParams = new org.apache.ambari.funtest.server.ClusterConfigParams();
        configParams.setClusterName(clusterName);
        configParams.setConfigType(configType);
        configParams.setConfigTag(configTag);
        configParams.setProperties(new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("fs.default.name", "localhost:9995");
            }
        });
        org.apache.ambari.funtest.server.ConnectionParams anonUserParams = createConnectionParams(anonUserName, anonUserPwd);
        org.apache.ambari.funtest.server.WebRequest webRequest = new org.apache.ambari.funtest.server.api.cluster.CreateConfigurationWebRequest(anonUserParams, configParams);
        org.apache.ambari.funtest.server.WebResponse webResponse = webRequest.getResponse();
        org.junit.Assert.assertEquals(HttpStatus.SC_FORBIDDEN, webResponse.getStatusCode());
        com.google.gson.JsonElement jsonResponse = org.apache.ambari.funtest.server.utils.RestApiUtils.executeRequest(new org.apache.ambari.funtest.server.api.user.DeleteUserWebRequest(adminConnectionParams, "nothing"));
        org.apache.ambari.funtest.server.tests.RoleBasedAccessControlBasicTest.LOG.info(jsonResponse);
    }

    @org.junit.Test
    public void testAddClusterConfigAsClusterAdmin() throws java.lang.Exception {
        org.apache.ambari.funtest.server.ConnectionParams adminConnectionParams = createAdminConnectionParams();
        java.lang.String clusterAdminName = "clusterAdmin";
        java.lang.String clusterAdminPwd = "clusterAdmin";
        org.apache.ambari.funtest.server.utils.ClusterUtils.createUserClusterAdministrator(adminConnectionParams, clusterName, clusterAdminName, clusterAdminPwd);
        java.lang.String configType = "test-hadoop-env";
        java.lang.String configTag = "version1";
        org.apache.ambari.funtest.server.ClusterConfigParams configParams = new org.apache.ambari.funtest.server.ClusterConfigParams();
        configParams.setClusterName(clusterName);
        configParams.setConfigType(configType);
        configParams.setConfigTag(configTag);
        configParams.setProperties(new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("fs.default.name", "localhost:9995");
            }
        });
        org.apache.ambari.funtest.server.ConnectionParams userConnectionParams = createConnectionParams(clusterAdminName, clusterAdminPwd);
        org.apache.ambari.funtest.server.WebRequest webRequest = new org.apache.ambari.funtest.server.api.cluster.CreateConfigurationWebRequest(userConnectionParams, configParams);
        org.apache.ambari.funtest.server.WebResponse webResponse = webRequest.getResponse();
        org.junit.Assert.assertEquals(HttpStatus.SC_CREATED, webResponse.getStatusCode());
        org.apache.ambari.funtest.server.utils.RestApiUtils.executeRequest(new org.apache.ambari.funtest.server.api.user.DeleteUserWebRequest(adminConnectionParams, clusterAdminName));
    }

    private void setupCluster() throws java.lang.Exception {
        com.google.gson.JsonElement jsonResponse;
        org.apache.ambari.funtest.server.ConnectionParams params = createAdminConnectionParams();
        jsonResponse = org.apache.ambari.funtest.server.utils.RestApiUtils.executeRequest(new org.apache.ambari.funtest.server.api.cluster.CreateClusterWebRequest(params, clusterName, clusterVersion));
        org.apache.ambari.funtest.server.tests.RoleBasedAccessControlBasicTest.LOG.info(jsonResponse);
    }

    private void teardownCluster() throws java.lang.Exception {
        com.google.gson.JsonElement jsonResponse;
        org.apache.ambari.funtest.server.ConnectionParams params = createAdminConnectionParams();
        jsonResponse = org.apache.ambari.funtest.server.utils.RestApiUtils.executeRequest(new org.apache.ambari.funtest.server.api.cluster.DeleteClusterWebRequest(params, clusterName));
        org.apache.ambari.funtest.server.tests.RoleBasedAccessControlBasicTest.LOG.info(jsonResponse);
    }

    private org.apache.ambari.funtest.server.ConnectionParams createAdminConnectionParams() {
        return createConnectionParams(org.apache.ambari.funtest.server.tests.ServerTestBase.getAdminUserName(), org.apache.ambari.funtest.server.tests.ServerTestBase.getAdminPassword());
    }

    private org.apache.ambari.funtest.server.ConnectionParams createConnectionParams(java.lang.String userName, java.lang.String password) {
        org.apache.ambari.funtest.server.ConnectionParams params = new org.apache.ambari.funtest.server.ConnectionParams();
        params.setServerName("localhost");
        params.setServerApiPort(org.apache.ambari.funtest.server.tests.ServerTestBase.serverPort);
        params.setServerAgentPort(org.apache.ambari.funtest.server.tests.ServerTestBase.serverAgentPort);
        params.setUserName(userName);
        params.setPassword(password);
        return params;
    }
}