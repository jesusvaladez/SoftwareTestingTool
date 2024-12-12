package org.apache.ambari.server.controller.logging;
import org.apache.commons.lang.StringUtils;
public class LoggingSearchPropertyProvider implements org.apache.ambari.server.controller.spi.PropertyProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.logging.LoggingSearchPropertyProvider.class);

    private static final java.lang.String CLUSTERS_PATH = "/api/v1/clusters";

    private static final java.lang.String PATH_TO_SEARCH_ENGINE = "/logging/searchEngine";

    private static final java.util.Set<org.apache.ambari.server.security.authorization.RoleAuthorization> REQUIRED_AUTHORIZATIONS = java.util.EnumSet.of(org.apache.ambari.server.security.authorization.RoleAuthorization.SERVICE_VIEW_OPERATIONAL_LOGS);

    private static java.util.concurrent.atomic.AtomicInteger errorLogCounterForLogSearchConnectionExceptions = new java.util.concurrent.atomic.AtomicInteger(0);

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.AmbariManagementController ambariManagementController;

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService logSearchDataRetrievalService;

    @com.google.inject.Inject
    private org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory loggingRequestHelperFactory;

    @java.lang.Override
    public java.util.Set<org.apache.ambari.server.controller.spi.Resource> populateResources(java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources, org.apache.ambari.server.controller.spi.Request request, org.apache.ambari.server.controller.spi.Predicate predicate) throws org.apache.ambari.server.controller.spi.SystemException {
        java.util.Map<java.lang.String, java.lang.Boolean> isLogSearchRunning = new java.util.HashMap<>();
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            final java.lang.String componentName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "component_name"))));
            final java.lang.String hostName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "host_name"))));
            final java.lang.String clusterName = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("HostRoles", "cluster_name"))));
            if (!org.apache.ambari.server.security.authorization.AuthorizationHelper.isAuthorized(org.apache.ambari.server.security.authorization.ResourceType.CLUSTER, getClusterResourceID(clusterName), org.apache.ambari.server.controller.logging.LoggingSearchPropertyProvider.REQUIRED_AUTHORIZATIONS)) {
                if (org.apache.ambari.server.controller.logging.LoggingSearchPropertyProvider.LOG.isDebugEnabled()) {
                    org.apache.ambari.server.controller.logging.LoggingSearchPropertyProvider.LOG.debug("The authenticated user ({}) is not authorized to access LogSearch data for the cluster named {}", org.apache.ambari.server.security.authorization.AuthorizationHelper.getAuthenticatedName(), clusterName);
                }
                continue;
            }
            java.lang.Boolean isLogSearchRunningForSpecifiedCluster = isLogSearchRunning.get(clusterName);
            if (isLogSearchRunningForSpecifiedCluster == null) {
                isLogSearchRunningForSpecifiedCluster = logSearchServerRunning(clusterName);
                isLogSearchRunning.put(clusterName, isLogSearchRunningForSpecifiedCluster);
            }
            if (!isLogSearchRunningForSpecifiedCluster) {
                continue;
            }
            final java.lang.String mappedComponentNameForLogSearch = getMappedComponentNameForSearch(clusterName, componentName, ambariManagementController);
            if (mappedComponentNameForLogSearch != null) {
                java.util.Set<java.lang.String> logFileNames = logSearchDataRetrievalService.getLogFileNames(mappedComponentNameForLogSearch, hostName, clusterName);
                if ((logFileNames != null) && (!logFileNames.isEmpty())) {
                    org.apache.ambari.server.controller.logging.HostComponentLoggingInfo loggingInfo = new org.apache.ambari.server.controller.logging.HostComponentLoggingInfo();
                    loggingInfo.setComponentName(mappedComponentNameForLogSearch);
                    java.util.List<org.apache.ambari.server.controller.logging.LogFileDefinitionInfo> listOfFileDefinitions = new java.util.LinkedList<>();
                    for (java.lang.String fileName : logFileNames) {
                        final java.lang.String searchEngineURI = ambariManagementController.getAmbariServerURI(getFullPathToSearchEngine(clusterName));
                        final java.lang.String logFileTailURI = logSearchDataRetrievalService.getLogFileTailURI(searchEngineURI, mappedComponentNameForLogSearch, hostName, clusterName);
                        if (logFileTailURI != null) {
                            listOfFileDefinitions.add(new org.apache.ambari.server.controller.logging.LogFileDefinitionInfo(fileName, org.apache.ambari.server.controller.logging.LogFileType.SERVICE, searchEngineURI, logFileTailURI));
                        }
                    }
                    loggingInfo.setListOfLogFileDefinitions(listOfFileDefinitions);
                    org.apache.ambari.server.controller.logging.LoggingSearchPropertyProvider.LOG.debug((("Adding logging info for component name = " + componentName) + " on host name = ") + hostName);
                    resource.setProperty("logging", loggingInfo);
                } else if (org.apache.ambari.server.controller.logging.LoggingSearchPropertyProvider.LOG.isDebugEnabled()) {
                    java.lang.String debugMessage = java.lang.String.format("Error occurred while making request to LogSearch service, unable to populate logging properties on this resource, component = %s, host = %s", componentName, hostName);
                    org.apache.ambari.server.controller.logging.Utils.logDebugMessageWithCounter(org.apache.ambari.server.controller.logging.LoggingSearchPropertyProvider.LOG, org.apache.ambari.server.controller.logging.LoggingSearchPropertyProvider.errorLogCounterForLogSearchConnectionExceptions, debugMessage);
                }
            }
        }
        return resources;
    }

    private java.lang.Long getClusterResourceID(java.lang.String clusterName) {
        java.lang.Long clusterResourceId = null;
        if (!org.apache.commons.lang.StringUtils.isEmpty(clusterName)) {
            try {
                org.apache.ambari.server.state.Cluster cluster = ambariManagementController.getClusters().getCluster(clusterName);
                if (cluster == null) {
                    org.apache.ambari.server.controller.logging.LoggingSearchPropertyProvider.LOG.warn(java.lang.String.format("No cluster found with the name %s, assuming null resource id", clusterName));
                } else {
                    clusterResourceId = cluster.getResourceId();
                }
            } catch (org.apache.ambari.server.AmbariException e) {
                org.apache.ambari.server.controller.logging.LoggingSearchPropertyProvider.LOG.warn(java.lang.String.format("An exception occurred looking up the cluster named %s, assuming null resource id: %s", clusterName, e.getLocalizedMessage()));
            }
        } else {
            org.apache.ambari.server.controller.logging.LoggingSearchPropertyProvider.LOG.debug("The cluster name is not set, assuming null resource id");
        }
        return clusterResourceId;
    }

    private boolean logSearchServerRunning(java.lang.String clusterName) {
        return loggingRequestHelperFactory.getHelper(ambariManagementController, clusterName) != null;
    }

    private java.lang.String getMappedComponentNameForSearch(java.lang.String clusterName, java.lang.String componentName, org.apache.ambari.server.controller.AmbariManagementController controller) {
        try {
            org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo = controller.getAmbariMetaInfo();
            org.apache.ambari.server.state.Cluster cluster = controller.getClusters().getCluster(clusterName);
            java.lang.String serviceName = controller.findServiceName(cluster, componentName);
            org.apache.ambari.server.state.Service service = cluster.getService(serviceName);
            org.apache.ambari.server.state.StackId stackId = service.getDesiredStackId();
            final java.lang.String stackName = stackId.getStackName();
            final java.lang.String stackVersion = stackId.getStackVersion();
            org.apache.ambari.server.state.ComponentInfo componentInfo = metaInfo.getComponent(stackName, stackVersion, serviceName, componentName);
            if (componentInfo != null) {
                java.util.List<org.apache.ambari.server.state.LogDefinition> listOfLogs = componentInfo.getLogs();
                if ((listOfLogs != null) && (!listOfLogs.isEmpty())) {
                    org.apache.ambari.server.state.LogDefinition definition = listOfLogs.get(0);
                    return definition.getLogId();
                }
            }
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.controller.logging.LoggingSearchPropertyProvider.LOG.error("Error occurred while attempting to locate the log component name for component = " + componentName, e);
        }
        return null;
    }

    private java.lang.String getFullPathToSearchEngine(java.lang.String clusterName) {
        return ((org.apache.ambari.server.controller.logging.LoggingSearchPropertyProvider.CLUSTERS_PATH + "/") + clusterName) + org.apache.ambari.server.controller.logging.LoggingSearchPropertyProvider.PATH_TO_SEARCH_ENGINE;
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> checkPropertyIds(java.util.Set<java.lang.String> propertyIds) {
        return java.util.Collections.emptySet();
    }

    protected void setAmbariManagementController(org.apache.ambari.server.controller.AmbariManagementController ambariManagementController) {
        this.ambariManagementController = ambariManagementController;
    }

    void setLogSearchDataRetrievalService(org.apache.ambari.server.controller.logging.LogSearchDataRetrievalService logSearchDataRetrievalService) {
        this.logSearchDataRetrievalService = logSearchDataRetrievalService;
    }

    void setLoggingRequestHelperFactory(org.apache.ambari.server.controller.logging.LoggingRequestHelperFactory loggingRequestHelperFactory) {
        this.loggingRequestHelperFactory = loggingRequestHelperFactory;
    }
}