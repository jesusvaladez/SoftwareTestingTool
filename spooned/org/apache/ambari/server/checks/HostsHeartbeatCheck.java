package org.apache.ambari.server.checks;
@com.google.inject.Singleton
@org.apache.ambari.annotations.UpgradeCheckInfo(group = org.apache.ambari.spi.upgrade.UpgradeCheckGroup.LIVELINESS, order = 1.0F, required = { org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.HOST_ORDERED })
public class HostsHeartbeatCheck extends org.apache.ambari.server.checks.ClusterCheck {
    static final org.apache.ambari.spi.upgrade.UpgradeCheckDescription HOSTS_HEARTBEAT = new org.apache.ambari.spi.upgrade.UpgradeCheckDescription("HOSTS_HEARTBEAT", org.apache.ambari.spi.upgrade.UpgradeCheckType.HOST, "All hosts must be communicating with Ambari. Hosts which are not reachable should be placed in Maintenance Mode.", new com.google.common.collect.ImmutableMap.Builder<java.lang.String, java.lang.String>().put(org.apache.ambari.spi.upgrade.UpgradeCheckDescription.DEFAULT, "There are hosts which are not communicating with Ambari.").build());

    public HostsHeartbeatCheck() {
        super(org.apache.ambari.server.checks.HostsHeartbeatCheck.HOSTS_HEARTBEAT);
    }

    @java.lang.Override
    public org.apache.ambari.spi.upgrade.UpgradeCheckResult perform(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = new org.apache.ambari.spi.upgrade.UpgradeCheckResult(this);
        final java.lang.String clusterName = request.getClusterName();
        final org.apache.ambari.server.state.Cluster cluster = clustersProvider.get().getCluster(clusterName);
        java.util.Collection<org.apache.ambari.server.state.Host> hosts = cluster.getHosts();
        for (org.apache.ambari.server.state.Host host : hosts) {
            org.apache.ambari.server.state.HostHealthStatus.HealthStatus hostHealth = host.getHealthStatus().getHealthStatus();
            org.apache.ambari.server.state.MaintenanceState maintenanceState = host.getMaintenanceState(cluster.getClusterId());
            switch (hostHealth) {
                case UNHEALTHY :
                case UNKNOWN :
                    if (maintenanceState == org.apache.ambari.server.state.MaintenanceState.OFF) {
                        result.getFailedOn().add(host.getHostName());
                        result.getFailedDetail().add(new org.apache.ambari.server.checks.ClusterCheck.HostDetail(host.getHostId(), host.getHostName()));
                    }
                    break;
                default :
                    break;
            }
        }
        if (!result.getFailedOn().isEmpty()) {
            result.setStatus(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL);
            result.setFailReason(getFailReason(result, request));
        }
        return result;
    }
}