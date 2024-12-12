package org.apache.ambari.server.controller.logging;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
public class LoggingRequestHelperFactoryImpl implements org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(org.apache.ambari.server.controller.logging.LoggingRequestHelperFactoryImpl.class);

    private static final java.lang.String LOGSEARCH_PROPERTIES_CONFIG_TYPE_NAME = "logsearch-properties";

    private static final java.lang.String LOGSEARCH_SERVICE_NAME = "LOGSEARCH";

    private static final java.lang.String LOGSEARCH_SERVER_COMPONENT_NAME = "LOGSEARCH_SERVER";

    private static final java.lang.String LOGSEARCH_HTTP_PORT_PROPERTY_NAME = "logsearch.http.port";

    private static final java.lang.String LOGSEARCH_HTTPS_PORT_PROPERTY_NAME = "logsearch.https.port";

    private static final java.lang.String LOGSEARCH_UI_PROTOCOL = "logsearch.protocol";

    private static final java.lang.String LOGSEARCH_HTTPS_PROTOCOL_VALUE = "https";

    @javax.inject.Inject
    private org.apache.ambari.server.configuration.Configuration ambariServerConfiguration;

    @java.lang.Override
    public org.apache.ambari.server.controller.logging.LoggingRequestHelper getHelper(org.apache.ambari.server.controller.AmbariManagementController ambariManagementController, java.lang.String clusterName) {
        if (ambariServerConfiguration == null) {
            org.apache.ambari.server.controller.logging.LoggingRequestHelperFactoryImpl.LOG.error("Ambari Server configuration object not available, cannot create request helper");
            return null;
        }
        org.apache.ambari.server.state.Clusters clusters = ambariManagementController.getClusters();
        try {
            final org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl loggingRequestHelper;
            org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
            if (cluster != null) {
                final java.lang.String logSearchHostName;
                final java.lang.String logSearchPortNumber;
                final java.lang.String logSearchProtocol;
                if (org.apache.commons.lang.StringUtils.isNotBlank(ambariServerConfiguration.getLogSearchPortalExternalAddress())) {
                    loggingRequestHelper = new org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl(null, null, null, ambariManagementController.getCredentialStoreService(), cluster, ambariServerConfiguration.getLogSearchPortalExternalAddress());
                } else {
                    boolean isLogSearchEnabled = cluster.getServices().containsKey(org.apache.ambari.server.controller.logging.LoggingRequestHelperFactoryImpl.LOGSEARCH_SERVICE_NAME);
                    if (!isLogSearchEnabled) {
                        return null;
                    }
                    org.apache.ambari.server.state.Config logSearchEnvConfig = cluster.getDesiredConfigByType(org.apache.ambari.server.controller.logging.LoggingRequestHelperFactoryImpl.LOGSEARCH_PROPERTIES_CONFIG_TYPE_NAME);
                    java.util.List<org.apache.ambari.server.state.ServiceComponentHost> listOfMatchingHosts = cluster.getServiceComponentHosts(org.apache.ambari.server.controller.logging.LoggingRequestHelperFactoryImpl.LOGSEARCH_SERVICE_NAME, org.apache.ambari.server.controller.logging.LoggingRequestHelperFactoryImpl.LOGSEARCH_SERVER_COMPONENT_NAME);
                    if (listOfMatchingHosts.size() == 0) {
                        org.apache.ambari.server.controller.logging.LoggingRequestHelperFactoryImpl.LOG.warn("No matching LOGSEARCH_SERVER instances found, this may indicate a deployment problem.  Please verify that LogSearch is deployed and running.");
                        return null;
                    }
                    if (listOfMatchingHosts.size() > 1) {
                        org.apache.ambari.server.controller.logging.LoggingRequestHelperFactoryImpl.LOG.warn("More than one LOGSEARCH_SERVER instance found, this may be a deployment error.  Only the first LOGSEARCH_SERVER instance will be used.");
                    }
                    org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost = listOfMatchingHosts.get(0);
                    if (serviceComponentHost.getState() != org.apache.ambari.server.state.State.STARTED) {
                        return null;
                    }
                    logSearchHostName = serviceComponentHost.getHostName();
                    logSearchProtocol = logSearchEnvConfig.getProperties().get(org.apache.ambari.server.controller.logging.LoggingRequestHelperFactoryImpl.LOGSEARCH_UI_PROTOCOL);
                    logSearchPortNumber = (org.apache.ambari.server.controller.logging.LoggingRequestHelperFactoryImpl.LOGSEARCH_HTTPS_PROTOCOL_VALUE.equalsIgnoreCase(logSearchProtocol)) ? logSearchEnvConfig.getProperties().get(org.apache.ambari.server.controller.logging.LoggingRequestHelperFactoryImpl.LOGSEARCH_HTTPS_PORT_PROPERTY_NAME) : logSearchEnvConfig.getProperties().get(org.apache.ambari.server.controller.logging.LoggingRequestHelperFactoryImpl.LOGSEARCH_HTTP_PORT_PROPERTY_NAME);
                    loggingRequestHelper = new org.apache.ambari.server.controller.logging.LoggingRequestHelperImpl(logSearchHostName, logSearchPortNumber, logSearchProtocol, ambariManagementController.getCredentialStoreService(), cluster, null);
                }
                loggingRequestHelper.setLogSearchConnectTimeoutInMilliseconds(ambariServerConfiguration.getLogSearchPortalConnectTimeout());
                loggingRequestHelper.setLogSearchReadTimeoutInMilliseconds(ambariServerConfiguration.getLogSearchPortalReadTimeout());
                return loggingRequestHelper;
            }
        } catch (org.apache.ambari.server.AmbariException ambariException) {
            org.apache.ambari.server.controller.logging.LoggingRequestHelperFactoryImpl.LOG.error("Error occurred while trying to obtain the cluster, cluster name = " + clusterName, ambariException);
        }
        return null;
    }

    void setAmbariServerConfiguration(org.apache.ambari.server.configuration.Configuration ambariServerConfiguration) {
        this.ambariServerConfiguration = ambariServerConfiguration;
    }
}