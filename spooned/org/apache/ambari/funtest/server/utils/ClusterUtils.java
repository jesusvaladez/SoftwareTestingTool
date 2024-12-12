package org.apache.ambari.funtest.server.utils;
public class ClusterUtils {
    private static org.apache.commons.logging.Log LOG = org.apache.commons.logging.LogFactory.getLog(org.apache.ambari.funtest.server.utils.ClusterUtils.class);

    @com.google.inject.Inject
    private com.google.inject.Injector injector;

    public void createSampleCluster(org.apache.ambari.funtest.server.ConnectionParams serverParams) throws java.lang.Exception {
        org.apache.ambari.funtest.server.WebResponse response = null;
        com.google.gson.JsonElement jsonResponse;
        java.lang.String clusterName = "c1";
        java.lang.String hostName = "host1";
        java.lang.String clusterVersion = "HDP-2.2.0";
        jsonResponse = org.apache.ambari.funtest.server.utils.RestApiUtils.executeRequest(new org.apache.ambari.funtest.server.api.cluster.CreateClusterWebRequest(serverParams, clusterName, clusterVersion));
        if (injector == null) {
            jsonResponse = org.apache.ambari.funtest.server.utils.RestApiUtils.executeRequest(new org.apache.ambari.funtest.server.api.host.RegisterHostWebRequest(serverParams, hostName));
        } else {
            org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
            clusters.addHost(hostName);
            org.apache.ambari.server.state.Host host1 = clusters.getHost(hostName);
            java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<java.lang.String, java.lang.String>();
            hostAttributes.put("os_family", "redhat");
            hostAttributes.put("os_release_version", "6.3");
            host1.setHostAttributes(hostAttributes);
        }
        jsonResponse = org.apache.ambari.funtest.server.utils.RestApiUtils.executeRequest(new org.apache.ambari.funtest.server.api.host.AddHostWebRequest(serverParams, clusterName, hostName));
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
        jsonResponse = org.apache.ambari.funtest.server.utils.RestApiUtils.executeRequest(new org.apache.ambari.funtest.server.api.cluster.CreateConfigurationWebRequest(serverParams, configParams));
        jsonResponse = org.apache.ambari.funtest.server.utils.RestApiUtils.executeRequest(new org.apache.ambari.funtest.server.api.cluster.AddDesiredConfigurationWebRequest(serverParams, configParams));
        java.lang.String serviceName = "HDFS";
        jsonResponse = org.apache.ambari.funtest.server.utils.RestApiUtils.executeRequest(new org.apache.ambari.funtest.server.api.service.AddServiceWebRequest(serverParams, clusterName, serviceName));
        java.lang.String[] componentNames = new java.lang.String[]{ "NAMENODE", "DATANODE", "SECONDARY_NAMENODE" };
        for (java.lang.String componentName : componentNames) {
            jsonResponse = org.apache.ambari.funtest.server.utils.RestApiUtils.executeRequest(new org.apache.ambari.funtest.server.api.servicecomponent.AddServiceComponentWebRequest(serverParams, clusterName, serviceName, componentName));
        }
        jsonResponse = org.apache.ambari.funtest.server.utils.RestApiUtils.executeRequest(new org.apache.ambari.funtest.server.api.service.InstallServiceWebRequest(serverParams, clusterName, serviceName));
        jsonResponse = org.apache.ambari.funtest.server.utils.RestApiUtils.executeRequest(new org.apache.ambari.funtest.server.api.servicecomponenthost.BulkAddServiceComponentHostsWebRequest(serverParams, clusterName, java.util.Arrays.asList(hostName), java.util.Arrays.asList(componentNames)));
        jsonResponse = org.apache.ambari.funtest.server.utils.RestApiUtils.executeRequest(new org.apache.ambari.funtest.server.api.servicecomponenthost.BulkSetServiceComponentHostStateWebRequest(serverParams, clusterName, org.apache.ambari.server.state.State.INIT, org.apache.ambari.server.state.State.INSTALLED));
        if (!jsonResponse.isJsonNull()) {
            int requestId = org.apache.ambari.funtest.server.utils.ClusterUtils.parseRequestId(jsonResponse);
            org.apache.ambari.funtest.server.utils.RequestStatusPoller.poll(serverParams, clusterName, requestId);
        }
        jsonResponse = org.apache.ambari.funtest.server.utils.RestApiUtils.executeRequest(new org.apache.ambari.funtest.server.api.servicecomponenthost.BulkSetServiceComponentHostStateWebRequest(serverParams, clusterName, org.apache.ambari.server.state.State.INSTALLED, org.apache.ambari.server.state.State.STARTED));
        if (!jsonResponse.isJsonNull()) {
            int requestId = org.apache.ambari.funtest.server.utils.ClusterUtils.parseRequestId(jsonResponse);
            org.apache.ambari.funtest.server.utils.RequestStatusPoller.poll(serverParams, clusterName, requestId);
        }
    }

    public static void createUser(org.apache.ambari.funtest.server.ConnectionParams connectionParams, java.lang.String clusterName, java.lang.String userName, java.lang.String password, org.apache.ambari.funtest.server.AmbariUserRole userRole) throws java.lang.Exception {
        com.google.gson.JsonElement jsonResponse;
        jsonResponse = org.apache.ambari.funtest.server.utils.RestApiUtils.executeRequest(new org.apache.ambari.funtest.server.api.user.CreateUserWebRequest(connectionParams, userName, password, org.apache.ambari.funtest.server.api.user.CreateUserWebRequest.ActiveUser.TRUE, org.apache.ambari.funtest.server.api.user.CreateUserWebRequest.AdminUser.FALSE));
        org.apache.ambari.funtest.server.utils.ClusterUtils.LOG.info(jsonResponse);
        if (userRole != org.apache.ambari.funtest.server.AmbariUserRole.NONE) {
            jsonResponse = org.apache.ambari.funtest.server.utils.RestApiUtils.executeRequest(new org.apache.ambari.funtest.server.api.cluster.SetUserPrivilegeWebRequest(connectionParams, clusterName, userName, userRole, org.apache.ambari.server.orm.entities.PrincipalTypeEntity.USER_PRINCIPAL_TYPE_NAME));
            org.apache.ambari.funtest.server.utils.ClusterUtils.LOG.info(jsonResponse);
        }
    }

    public static void createUserClusterUser(org.apache.ambari.funtest.server.ConnectionParams connectionParams, java.lang.String clusterName, java.lang.String userName, java.lang.String password) throws java.lang.Exception {
        org.apache.ambari.funtest.server.utils.ClusterUtils.createUser(connectionParams, clusterName, userName, password, org.apache.ambari.funtest.server.AmbariUserRole.CLUSTER_USER);
    }

    public static void createUserServiceOperator(org.apache.ambari.funtest.server.ConnectionParams connectionParams, java.lang.String clusterName, java.lang.String userName, java.lang.String password) throws java.lang.Exception {
        org.apache.ambari.funtest.server.utils.ClusterUtils.createUser(connectionParams, clusterName, userName, password, org.apache.ambari.funtest.server.AmbariUserRole.SERVICE_OPERATOR);
    }

    public static void createUserServiceAdministrator(org.apache.ambari.funtest.server.ConnectionParams connectionParams, java.lang.String clusterName, java.lang.String userName, java.lang.String password) throws java.lang.Exception {
        org.apache.ambari.funtest.server.utils.ClusterUtils.createUser(connectionParams, clusterName, userName, password, org.apache.ambari.funtest.server.AmbariUserRole.SERVICE_ADMINISTRATOR);
    }

    public static void createUserClusterOperator(org.apache.ambari.funtest.server.ConnectionParams connectionParams, java.lang.String clusterName, java.lang.String userName, java.lang.String password) throws java.lang.Exception {
        org.apache.ambari.funtest.server.utils.ClusterUtils.createUser(connectionParams, clusterName, userName, password, org.apache.ambari.funtest.server.AmbariUserRole.CLUSTER_OPERATOR);
    }

    public static void createUserClusterAdministrator(org.apache.ambari.funtest.server.ConnectionParams connectionParams, java.lang.String clusterName, java.lang.String userName, java.lang.String password) throws java.lang.Exception {
        org.apache.ambari.funtest.server.utils.ClusterUtils.createUser(connectionParams, clusterName, userName, password, org.apache.ambari.funtest.server.AmbariUserRole.CLUSTER_ADMINISTRATOR);
    }

    private static int parseRequestId(com.google.gson.JsonElement jsonResponse) throws java.lang.IllegalArgumentException {
        if (jsonResponse.isJsonNull()) {
            throw new java.lang.IllegalArgumentException("jsonResponse with request id expected.");
        }
        com.google.gson.JsonObject jsonObject = jsonResponse.getAsJsonObject();
        int requestId = jsonObject.get("Requests").getAsJsonObject().get("id").getAsInt();
        return requestId;
    }
}