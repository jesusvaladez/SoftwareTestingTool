package org.apache.ambari.server.checks;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
@com.google.inject.Singleton
@org.apache.ambari.annotations.UpgradeCheckInfo(group = org.apache.ambari.spi.upgrade.UpgradeCheckGroup.REPOSITORY_VERSION)
public class AmbariMetricsHadoopSinkVersionCompatibilityCheck extends org.apache.ambari.server.checks.ClusterCheck {
    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.RequestDAO requestDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAO;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.checks.AmbariMetricsHadoopSinkVersionCompatibilityCheck.class);

    private enum PreUpgradeCheckStatus {

        SUCCESS,
        FAILED,
        RUNNING;}

    static final java.lang.String HADOOP_SINK_VERSION_NOT_SPECIFIED = "hadoop-sink-version-not-specified";

    static final java.lang.String MIN_HADOOP_SINK_VERSION_PROPERTY_NAME = "min-hadoop-sink-version";

    static final java.lang.String RETRY_INTERVAL_PROPERTY_NAME = "request-status-check-retry-interval";

    static final java.lang.String NUM_TRIES_PROPERTY_NAME = "request-status-check-num-retries";

    private long retryInterval = 6000L;

    private int numTries = 20;

    static final org.apache.ambari.spi.upgrade.UpgradeCheckDescription AMS_HADOOP_SINK_VERSION_COMPATIBILITY = new org.apache.ambari.spi.upgrade.UpgradeCheckDescription("AMS_HADOOP_SINK_VERSION_COMPATIBILITY", org.apache.ambari.spi.upgrade.UpgradeCheckType.HOST, "Ambari Metrics Hadoop Sinks need to be compatible with the stack version. This check ensures that compatibility.", new com.google.common.collect.ImmutableMap.Builder<java.lang.String, java.lang.String>().put(org.apache.ambari.spi.upgrade.UpgradeCheckDescription.DEFAULT, "Hadoop Sink version check failed. " + "To fix this, please upgrade 'ambari-metrics-hadoop-sink' package to %s on all the failed hosts").put(org.apache.ambari.server.checks.AmbariMetricsHadoopSinkVersionCompatibilityCheck.HADOOP_SINK_VERSION_NOT_SPECIFIED, "Hadoop Sink version for pre-check not specified. " + "Please use 'min-hadoop-sink-version' property in upgrade pack to specify min hadoop sink version").build());

    public AmbariMetricsHadoopSinkVersionCompatibilityCheck() {
        super(org.apache.ambari.server.checks.AmbariMetricsHadoopSinkVersionCompatibilityCheck.AMS_HADOOP_SINK_VERSION_COMPATIBILITY);
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> getApplicableServices() {
        return com.google.common.collect.Sets.newHashSet("AMBARI_METRICS", "HDFS");
    }

    @java.lang.Override
    public org.apache.ambari.spi.upgrade.UpgradeCheckResult perform(org.apache.ambari.spi.upgrade.UpgradeCheckRequest prereqCheckRequest) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = new org.apache.ambari.spi.upgrade.UpgradeCheckResult(this);
        java.lang.String minHadoopSinkVersion = null;
        java.util.Map<java.lang.String, java.lang.String> checkProperties = prereqCheckRequest.getCheckConfigurations();
        if (checkProperties != null) {
            minHadoopSinkVersion = checkProperties.get(org.apache.ambari.server.checks.AmbariMetricsHadoopSinkVersionCompatibilityCheck.MIN_HADOOP_SINK_VERSION_PROPERTY_NAME);
            retryInterval = java.lang.Long.parseLong(checkProperties.getOrDefault(org.apache.ambari.server.checks.AmbariMetricsHadoopSinkVersionCompatibilityCheck.RETRY_INTERVAL_PROPERTY_NAME, "6000"));
            numTries = java.lang.Integer.parseInt(checkProperties.getOrDefault(org.apache.ambari.server.checks.AmbariMetricsHadoopSinkVersionCompatibilityCheck.NUM_TRIES_PROPERTY_NAME, "20"));
        }
        if (org.apache.commons.lang.StringUtils.isEmpty(minHadoopSinkVersion)) {
            org.apache.ambari.server.checks.AmbariMetricsHadoopSinkVersionCompatibilityCheck.LOG.debug("Hadoop Sink version for pre-check not specified.");
            result.setStatus(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL);
            result.setFailReason(getFailReason(org.apache.ambari.server.checks.AmbariMetricsHadoopSinkVersionCompatibilityCheck.HADOOP_SINK_VERSION_NOT_SPECIFIED, result, prereqCheckRequest));
            return result;
        }
        org.apache.ambari.server.checks.AmbariMetricsHadoopSinkVersionCompatibilityCheck.LOG.debug("Properties : Hadoop Sink Version = {} , retryInterval = {}, numTries = {}", minHadoopSinkVersion, retryInterval, numTries);
        org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = org.apache.ambari.server.controller.AmbariServer.getController();
        org.apache.ambari.server.controller.spi.ResourceProvider provider = org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Request, ambariManagementController);
        java.lang.String clusterName = prereqCheckRequest.getClusterName();
        java.util.Set<java.lang.String> hosts = ambariManagementController.getClusters().getCluster(clusterName).getHosts("AMBARI_METRICS", "METRICS_MONITOR");
        if (org.apache.commons.collections.CollectionUtils.isEmpty(hosts)) {
            org.apache.ambari.server.checks.AmbariMetricsHadoopSinkVersionCompatibilityCheck.LOG.warn("No hosts have the component METRICS_MONITOR.");
            result.setStatus(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS);
            return result;
        }
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertiesSet = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_CLUSTER_NAME_PROPERTY_ID, clusterName);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> filterSet = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> filterMap = new java.util.HashMap<>();
        filterMap.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.SERVICE_ID, "AMBARI_METRICS");
        filterMap.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.COMPONENT_ID, "METRICS_MONITOR");
        filterMap.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.HOSTS_ID, org.apache.commons.lang.StringUtils.join(hosts, ","));
        filterSet.add(filterMap);
        properties.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_RESOURCE_FILTER_ID, filterSet);
        propertiesSet.add(properties);
        java.util.Map<java.lang.String, java.lang.String> requestInfoProperties = new java.util.HashMap<>();
        requestInfoProperties.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.COMMAND_ID, "CHECK_HADOOP_SINK_VERSION");
        requestInfoProperties.put(org.apache.ambari.server.controller.internal.RequestResourceProvider.CONTEXT, "Pre Upgrade check for Hadoop Metric Sink version");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertiesSet, requestInfoProperties);
        try {
            org.apache.ambari.server.controller.spi.RequestStatus response = provider.createResources(request);
            org.apache.ambari.server.controller.spi.Resource responseResource = response.getRequestResource();
            java.lang.String requestIdProp = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Requests", "id");
            long requestId = ((long) (responseResource.getPropertyValue(requestIdProp)));
            org.apache.ambari.server.checks.AmbariMetricsHadoopSinkVersionCompatibilityCheck.LOG.debug("RequestId for AMS Hadoop Sink version compatibility pre check : " + requestId);
            java.lang.Thread.sleep(retryInterval);
            org.apache.ambari.server.checks.AmbariMetricsHadoopSinkVersionCompatibilityCheck.PreUpgradeCheckStatus status;
            int retry = 0;
            java.util.LinkedHashSet<java.lang.String> failedHosts = new java.util.LinkedHashSet<>();
            while ((status = pollRequestStatus(requestId, failedHosts)).equals(org.apache.ambari.server.checks.AmbariMetricsHadoopSinkVersionCompatibilityCheck.PreUpgradeCheckStatus.RUNNING) && ((retry++) < numTries)) {
                if (retry != numTries) {
                    java.lang.Thread.sleep(retryInterval);
                }
            } 
            if (status.equals(org.apache.ambari.server.checks.AmbariMetricsHadoopSinkVersionCompatibilityCheck.PreUpgradeCheckStatus.SUCCESS)) {
                result.setStatus(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS);
            } else {
                result.setStatus(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL);
                result.setFailReason(java.lang.String.format(getFailReason(result, prereqCheckRequest), minHadoopSinkVersion));
                result.setFailedOn(failedHosts);
            }
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.checks.AmbariMetricsHadoopSinkVersionCompatibilityCheck.LOG.error("Error running Pre Upgrade check for AMS Hadoop Sink compatibility. " + e);
            result.setStatus(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL);
        }
        return result;
    }

    private org.apache.ambari.server.checks.AmbariMetricsHadoopSinkVersionCompatibilityCheck.PreUpgradeCheckStatus pollRequestStatus(long requestId, java.util.Set<java.lang.String> failedHosts) throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.orm.entities.RequestEntity> requestEntities = requestDAO.findByPks(java.util.Collections.singleton(requestId), true);
        if ((requestEntities != null) && (requestEntities.size() > 0)) {
            org.apache.ambari.server.orm.entities.RequestEntity requestEntity = requestEntities.iterator().next();
            org.apache.ambari.server.actionmanager.HostRoleStatus requestStatus = requestEntity.getStatus();
            if (org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.equals(requestStatus)) {
                return org.apache.ambari.server.checks.AmbariMetricsHadoopSinkVersionCompatibilityCheck.PreUpgradeCheckStatus.SUCCESS;
            } else if (requestStatus.isFailedState()) {
                failedHosts.addAll(getPreUpgradeCheckFailedHosts(requestEntity));
                org.apache.ambari.server.checks.AmbariMetricsHadoopSinkVersionCompatibilityCheck.LOG.debug("Hadoop Sink version check failed on the following hosts : " + failedHosts.stream().collect(java.util.stream.Collectors.joining(",")));
                return org.apache.ambari.server.checks.AmbariMetricsHadoopSinkVersionCompatibilityCheck.PreUpgradeCheckStatus.FAILED;
            } else {
                return org.apache.ambari.server.checks.AmbariMetricsHadoopSinkVersionCompatibilityCheck.PreUpgradeCheckStatus.RUNNING;
            }
        } else {
            org.apache.ambari.server.checks.AmbariMetricsHadoopSinkVersionCompatibilityCheck.LOG.error("Unable to find RequestEntity for created request.");
        }
        return org.apache.ambari.server.checks.AmbariMetricsHadoopSinkVersionCompatibilityCheck.PreUpgradeCheckStatus.FAILED;
    }

    private java.util.Set<java.lang.String> getPreUpgradeCheckFailedHosts(org.apache.ambari.server.orm.entities.RequestEntity requestEntity) throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> hostRoleCommandEntities = hostRoleCommandDAO.findByRequest(requestEntity.getRequestId(), true);
        java.util.Set<java.lang.String> failedHosts = new java.util.LinkedHashSet<>();
        for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity hostRoleCommandEntity : hostRoleCommandEntities) {
            org.apache.ambari.server.actionmanager.HostRoleStatus status = hostRoleCommandEntity.getStatus();
            if (status.isFailedState()) {
                failedHosts.add(((hostRoleCommandEntity.getHostName() + "(") + status) + ")");
            }
        }
        return failedHosts;
    }
}