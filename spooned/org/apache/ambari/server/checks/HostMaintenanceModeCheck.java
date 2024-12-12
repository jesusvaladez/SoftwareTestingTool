package org.apache.ambari.server.checks;
@com.google.inject.Singleton
@org.apache.ambari.annotations.UpgradeCheckInfo(group = org.apache.ambari.spi.upgrade.UpgradeCheckGroup.MAINTENANCE_MODE, order = 7.0F, required = { org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.HOST_ORDERED })
public class HostMaintenanceModeCheck extends org.apache.ambari.server.checks.ClusterCheck {
    public static final java.lang.String KEY_CANNOT_START_HOST_ORDERED = "cannot_upgrade_mm_hosts";

    static final org.apache.ambari.spi.upgrade.UpgradeCheckDescription HOSTS_MAINTENANCE_MODE = new org.apache.ambari.spi.upgrade.UpgradeCheckDescription("HOSTS_MAINTENANCE_MODE", org.apache.ambari.spi.upgrade.UpgradeCheckType.HOST, "Hosts in Maintenance Mode will be excluded from the upgrade.", new com.google.common.collect.ImmutableMap.Builder<java.lang.String, java.lang.String>().put(org.apache.ambari.spi.upgrade.UpgradeCheckDescription.DEFAULT, "There are hosts in Maintenance Mode which excludes them from being upgraded.").put(org.apache.ambari.server.checks.HostMaintenanceModeCheck.KEY_CANNOT_START_HOST_ORDERED, "The following hosts cannot be in Maintenance Mode: {{fails}}.").build());

    public HostMaintenanceModeCheck() {
        super(org.apache.ambari.server.checks.HostMaintenanceModeCheck.HOSTS_MAINTENANCE_MODE);
    }

    @java.lang.Override
    public org.apache.ambari.spi.upgrade.UpgradeCheckResult perform(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = new org.apache.ambari.spi.upgrade.UpgradeCheckResult(this);
        final java.lang.String clusterName = request.getClusterName();
        final org.apache.ambari.server.state.Cluster cluster = clustersProvider.get().getCluster(clusterName);
        java.util.Collection<org.apache.ambari.server.state.Host> hosts = cluster.getHosts();
        for (org.apache.ambari.server.state.Host host : hosts) {
            org.apache.ambari.server.state.MaintenanceState maintenanceState = host.getMaintenanceState(cluster.getClusterId());
            if (maintenanceState != org.apache.ambari.server.state.MaintenanceState.OFF) {
                result.getFailedOn().add(host.getHostName());
                result.getFailedDetail().add(new org.apache.ambari.server.checks.ClusterCheck.HostDetail(host.getHostId(), host.getHostName()));
            }
        }
        if (!result.getFailedOn().isEmpty()) {
            org.apache.ambari.spi.upgrade.UpgradeCheckStatus status = (request.getUpgradeType() == org.apache.ambari.spi.upgrade.UpgradeType.HOST_ORDERED) ? org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL : org.apache.ambari.spi.upgrade.UpgradeCheckStatus.WARNING;
            result.setStatus(status);
            java.lang.String failReason = (request.getUpgradeType() == org.apache.ambari.spi.upgrade.UpgradeType.HOST_ORDERED) ? getFailReason(org.apache.ambari.server.checks.HostMaintenanceModeCheck.KEY_CANNOT_START_HOST_ORDERED, result, request) : getFailReason(result, request);
            result.setFailReason(failReason);
        }
        return result;
    }
}