package org.apache.ambari.server.checks;
import javax.annotation.Nullable;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonProperty;
@com.google.inject.Singleton
@org.apache.ambari.annotations.UpgradeCheckInfo(group = org.apache.ambari.spi.upgrade.UpgradeCheckGroup.DEFAULT, required = { org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.HOST_ORDERED })
public class ServiceCheckValidityCheck extends org.apache.ambari.server.checks.ClusterCheck {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.checks.ServiceCheckValidityCheck.class);

    private static final java.text.SimpleDateFormat DATE_FORMAT = new java.text.SimpleDateFormat("MM-dd-yyyy hh:mm:ss");

    @com.google.inject.Inject
    com.google.inject.Provider<org.apache.ambari.server.orm.dao.ServiceConfigDAO> serviceConfigDAOProvider;

    @com.google.inject.Inject
    com.google.inject.Provider<org.apache.ambari.server.orm.dao.HostRoleCommandDAO> hostRoleCommandDAOProvider;

    @com.google.inject.Inject
    com.google.inject.Provider<org.apache.ambari.server.metadata.ActionMetadata> actionMetadataProvider;

    static final org.apache.ambari.spi.upgrade.UpgradeCheckDescription SERVICE_CHECK = new org.apache.ambari.spi.upgrade.UpgradeCheckDescription("SERVICE_CHECK", org.apache.ambari.spi.upgrade.UpgradeCheckType.SERVICE, "Last Service Check should be more recent than the last configuration change for the given service", new com.google.common.collect.ImmutableMap.Builder<java.lang.String, java.lang.String>().put(org.apache.ambari.spi.upgrade.UpgradeCheckDescription.DEFAULT, "The following service configurations have been updated and their Service Checks should be run again: %s").build());

    public ServiceCheckValidityCheck() {
        super(org.apache.ambari.server.checks.ServiceCheckValidityCheck.SERVICE_CHECK);
    }

    @java.lang.Override
    public org.apache.ambari.spi.upgrade.UpgradeCheckResult perform(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = new org.apache.ambari.spi.upgrade.UpgradeCheckResult(this);
        org.apache.ambari.server.orm.dao.ServiceConfigDAO serviceConfigDAO = serviceConfigDAOProvider.get();
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAO = hostRoleCommandDAOProvider.get();
        final java.lang.String clusterName = request.getClusterName();
        final org.apache.ambari.server.state.Cluster cluster = clustersProvider.get().getCluster(clusterName);
        long clusterId = cluster.getClusterId();
        java.util.Map<java.lang.String, java.lang.Long> lastServiceConfigUpdates = new java.util.HashMap<>();
        for (org.apache.ambari.server.state.Service service : cluster.getServices().values()) {
            if ((service.getMaintenanceState() != org.apache.ambari.server.state.MaintenanceState.OFF) || (!hasAtLeastOneComponentVersionAdvertised(service))) {
                continue;
            }
            org.apache.ambari.server.state.StackId stackId = service.getDesiredStackId();
            boolean isServiceWitNoConfigs = ambariMetaInfo.get().isServiceWithNoConfigs(stackId.getStackName(), stackId.getStackVersion(), service.getName());
            if (isServiceWitNoConfigs) {
                org.apache.ambari.server.checks.ServiceCheckValidityCheck.LOG.info(java.lang.String.format("%s in %s version %s does not have customizable configurations. Skip checking service configuration history.", service.getName(), stackId.getStackName(), stackId.getStackVersion()));
            } else {
                org.apache.ambari.server.checks.ServiceCheckValidityCheck.LOG.info(java.lang.String.format("%s in %s version %s has customizable configurations. Check service configuration history.", service.getName(), stackId.getStackName(), stackId.getStackVersion()));
                org.apache.ambari.server.orm.entities.ServiceConfigEntity lastServiceConfig = serviceConfigDAO.getLastServiceConfig(clusterId, service.getName());
                lastServiceConfigUpdates.put(service.getName(), lastServiceConfig.getCreateTimestamp());
            }
        }
        java.util.List<org.apache.ambari.server.orm.dao.HostRoleCommandDAO.LastServiceCheckDTO> lastServiceChecks = hostRoleCommandDAO.getLatestServiceChecksByRole(clusterId);
        java.util.Map<java.lang.String, java.lang.Long> lastServiceChecksByRole = new java.util.HashMap<>();
        for (org.apache.ambari.server.orm.dao.HostRoleCommandDAO.LastServiceCheckDTO lastServiceCheck : lastServiceChecks) {
            lastServiceChecksByRole.put(lastServiceCheck.role, lastServiceCheck.endTime);
        }
        java.util.LinkedHashSet<org.apache.ambari.server.checks.ServiceCheckValidityCheck.ServiceCheckConfigDetail> failures = new java.util.LinkedHashSet<>();
        for (java.util.Map.Entry<java.lang.String, java.lang.Long> entry : lastServiceConfigUpdates.entrySet()) {
            java.lang.String serviceName = entry.getKey();
            long configCreationTime = entry.getValue();
            java.lang.String role = actionMetadataProvider.get().getServiceCheckAction(serviceName);
            if (!lastServiceChecksByRole.containsKey(role)) {
                org.apache.ambari.server.checks.ServiceCheckValidityCheck.LOG.info("There was no service check found for service {} matching role {}", serviceName, role);
                failures.add(new org.apache.ambari.server.checks.ServiceCheckValidityCheck.ServiceCheckConfigDetail(serviceName, null, null));
                continue;
            }
            long lastServiceCheckTime = lastServiceChecksByRole.get(role);
            if (lastServiceCheckTime < configCreationTime) {
                failures.add(new org.apache.ambari.server.checks.ServiceCheckValidityCheck.ServiceCheckConfigDetail(serviceName, lastServiceCheckTime, configCreationTime));
                org.apache.ambari.server.checks.ServiceCheckValidityCheck.LOG.info("The {} service (role {}) had its configurations updated on {}, but the last service check was {}", serviceName, role, org.apache.ambari.server.checks.ServiceCheckValidityCheck.DATE_FORMAT.format(new java.util.Date(configCreationTime)), org.apache.ambari.server.checks.ServiceCheckValidityCheck.DATE_FORMAT.format(new java.util.Date(lastServiceCheckTime)));
            }
        }
        if (!failures.isEmpty()) {
            result.getFailedDetail().addAll(failures);
            java.util.LinkedHashSet<java.lang.String> failedServiceNames = failures.stream().map(failure -> failure.serviceName).collect(java.util.stream.Collectors.toCollection(java.util.LinkedHashSet::new));
            result.setFailedOn(failedServiceNames);
            result.setStatus(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL);
            java.lang.String failReason = getFailReason(result, request);
            result.setFailReason(java.lang.String.format(failReason, org.apache.commons.lang.StringUtils.join(failedServiceNames, ", ")));
        }
        return result;
    }

    private boolean hasAtLeastOneComponentVersionAdvertised(org.apache.ambari.server.state.Service service) {
        java.util.Collection<org.apache.ambari.server.state.ServiceComponent> components = service.getServiceComponents().values();
        for (org.apache.ambari.server.state.ServiceComponent component : components) {
            if (component.isVersionAdvertised()) {
                return true;
            }
        }
        return false;
    }

    static class ServiceCheckConfigDetail implements java.lang.Comparable<org.apache.ambari.server.checks.ServiceCheckValidityCheck.ServiceCheckConfigDetail> {
        @org.codehaus.jackson.annotate.JsonProperty("service_name")
        final java.lang.String serviceName;

        @org.codehaus.jackson.annotate.JsonProperty("service_check_date")
        final java.lang.Long serviceCheckDate;

        @org.codehaus.jackson.annotate.JsonProperty("configuration_date")
        final java.lang.Long configurationDate;

        ServiceCheckConfigDetail(java.lang.String serviceName, @javax.annotation.Nullable
        java.lang.Long serviceCheckDate, @javax.annotation.Nullable
        java.lang.Long configurationDate) {
            this.serviceName = serviceName;
            this.serviceCheckDate = serviceCheckDate;
            this.configurationDate = configurationDate;
        }

        @java.lang.Override
        public int hashCode() {
            return java.util.Objects.hash(serviceName, serviceCheckDate, configurationDate);
        }

        @java.lang.Override
        public boolean equals(java.lang.Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            org.apache.ambari.server.checks.ServiceCheckValidityCheck.ServiceCheckConfigDetail other = ((org.apache.ambari.server.checks.ServiceCheckValidityCheck.ServiceCheckConfigDetail) (obj));
            return (java.util.Objects.equals(serviceName, other.serviceName) && java.util.Objects.equals(serviceCheckDate, other.serviceCheckDate)) && java.util.Objects.equals(configurationDate, other.configurationDate);
        }

        @java.lang.Override
        public int compareTo(org.apache.ambari.server.checks.ServiceCheckValidityCheck.ServiceCheckConfigDetail other) {
            return serviceName.compareTo(other.serviceName);
        }
    }
}