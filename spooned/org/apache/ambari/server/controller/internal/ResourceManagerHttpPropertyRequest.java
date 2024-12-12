package org.apache.ambari.server.controller.internal;
public class ResourceManagerHttpPropertyRequest extends org.apache.ambari.server.controller.internal.JsonHttpPropertyRequest {
    private static final java.lang.String CONFIG_YARN_SITE = "yarn-site";

    private static final java.lang.String CONFIG_CORE_SITE = "core-site";

    private static final java.lang.String PROPERTY_YARN_HTTP_POLICY = "yarn.http.policy";

    private static final java.lang.String PROPERTY_HADOOP_SSL_ENABLED = "hadoop.ssl.enabled";

    private static final java.lang.String PROPERTY_YARN_HTTP_POLICY_VALUE_HTTPS_ONLY = "HTTPS_ONLY";

    private static final java.lang.String PROPERTY_HADOOP_SSL_ENABLED_VALUE_TRUE = "true";

    private static final java.lang.String PROPERTY_WEBAPP_ADDRESS = "yarn.resourcemanager.webapp.address";

    private static final java.lang.String PROPERTY_WEBAPP_HTTPS_ADDRESS = "yarn.resourcemanager.webapp.https.address";

    private static final java.lang.String PROPERTY_HA_RM_IDS = "yarn.resourcemanager.ha.rm-ids";

    private static final java.lang.String PROPERTY_HOSTNAME_TEMPLATE = "yarn.resourcemanager.hostname.%s";

    private static final java.lang.String PROPERTY_WEBAPP_ADDRESS_TEMPLATE = "yarn.resourcemanager.webapp.address.%s";

    private static final java.lang.String PROPERTY_WEBAPP_HTTPS_ADDRESS_TEMPLATE = "yarn.resourcemanager.webapp.https.address.%s";

    private static final java.lang.String URL_TEMPLATE = "%s://%s:%s/ws/v1/cluster/info";

    private static final java.util.Map<java.lang.String, java.lang.String> PROPERTY_MAPPINGS = java.util.Collections.singletonMap("clusterInfo/haState", "HostRoles/ha_state");

    public ResourceManagerHttpPropertyRequest() {
        super(org.apache.ambari.server.controller.internal.ResourceManagerHttpPropertyRequest.PROPERTY_MAPPINGS);
    }

    @java.lang.Override
    public java.lang.String getUrl(org.apache.ambari.server.state.Cluster cluster, java.lang.String hostName) throws org.apache.ambari.server.controller.spi.SystemException {
        java.util.Map<java.lang.String, java.lang.String> yarnConfig = cluster.getDesiredConfigByType(org.apache.ambari.server.controller.internal.ResourceManagerHttpPropertyRequest.CONFIG_YARN_SITE).getProperties();
        java.util.Map<java.lang.String, java.lang.String> coreConfig = cluster.getDesiredConfigByType(org.apache.ambari.server.controller.internal.ResourceManagerHttpPropertyRequest.CONFIG_CORE_SITE).getProperties();
        java.lang.String yarnHttpPolicy = yarnConfig.get(org.apache.ambari.server.controller.internal.ResourceManagerHttpPropertyRequest.PROPERTY_YARN_HTTP_POLICY);
        java.lang.String hadoopSslEnabled = coreConfig.get(org.apache.ambari.server.controller.internal.ResourceManagerHttpPropertyRequest.PROPERTY_HADOOP_SSL_ENABLED);
        boolean useHttps = ((yarnHttpPolicy != null) && yarnHttpPolicy.equals(org.apache.ambari.server.controller.internal.ResourceManagerHttpPropertyRequest.PROPERTY_YARN_HTTP_POLICY_VALUE_HTTPS_ONLY)) || ((hadoopSslEnabled != null) && hadoopSslEnabled.equals(org.apache.ambari.server.controller.internal.ResourceManagerHttpPropertyRequest.PROPERTY_HADOOP_SSL_ENABLED_VALUE_TRUE));
        return java.lang.String.format(org.apache.ambari.server.controller.internal.ResourceManagerHttpPropertyRequest.URL_TEMPLATE, getProtocol(useHttps), hostName, getPort(hostName, yarnConfig, useHttps));
    }

    private java.lang.String getProtocol(boolean useHttps) {
        return useHttps ? "https" : "http";
    }

    private java.lang.String getPort(java.lang.String hostName, java.util.Map<java.lang.String, java.lang.String> yarnConfig, boolean useHttps) {
        if (yarnConfig.containsKey(org.apache.ambari.server.controller.internal.ResourceManagerHttpPropertyRequest.PROPERTY_HA_RM_IDS)) {
            java.lang.String rmId = getConfigResourceManagerId(yarnConfig, hostName);
            return useHttps ? getConfigPortValue(yarnConfig, java.lang.String.format(org.apache.ambari.server.controller.internal.ResourceManagerHttpPropertyRequest.PROPERTY_WEBAPP_HTTPS_ADDRESS_TEMPLATE, rmId), "8090") : getConfigPortValue(yarnConfig, java.lang.String.format(org.apache.ambari.server.controller.internal.ResourceManagerHttpPropertyRequest.PROPERTY_WEBAPP_ADDRESS_TEMPLATE, rmId), "8088");
        }
        return useHttps ? getConfigPortValue(yarnConfig, org.apache.ambari.server.controller.internal.ResourceManagerHttpPropertyRequest.PROPERTY_WEBAPP_HTTPS_ADDRESS, "8090") : getConfigPortValue(yarnConfig, org.apache.ambari.server.controller.internal.ResourceManagerHttpPropertyRequest.PROPERTY_WEBAPP_ADDRESS, "8088");
    }

    private java.lang.String getConfigResourceManagerId(java.util.Map<java.lang.String, java.lang.String> yarnConfig, java.lang.String hostName) {
        for (java.lang.String id : yarnConfig.get(org.apache.ambari.server.controller.internal.ResourceManagerHttpPropertyRequest.PROPERTY_HA_RM_IDS).split(",")) {
            java.lang.String hostNameProperty = java.lang.String.format(org.apache.ambari.server.controller.internal.ResourceManagerHttpPropertyRequest.PROPERTY_HOSTNAME_TEMPLATE, id);
            java.lang.String hostNameById = yarnConfig.get(hostNameProperty);
            if (hostNameById.equals(hostName)) {
                return id;
            }
        }
        return null;
    }

    private java.lang.String getConfigPortValue(java.util.Map<java.lang.String, java.lang.String> yarnConfig, java.lang.String property, java.lang.String defaultValue) {
        return yarnConfig.containsKey(property) ? yarnConfig.get(property).split(":")[1] : defaultValue;
    }
}