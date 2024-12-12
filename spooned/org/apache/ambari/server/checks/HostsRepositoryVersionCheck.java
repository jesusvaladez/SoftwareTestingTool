package org.apache.ambari.server.checks;
@com.google.inject.Singleton
@org.apache.ambari.annotations.UpgradeCheckInfo(group = org.apache.ambari.spi.upgrade.UpgradeCheckGroup.REPOSITORY_VERSION, required = { org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.HOST_ORDERED })
public class HostsRepositoryVersionCheck extends org.apache.ambari.server.checks.ClusterCheck {
    static final org.apache.ambari.spi.upgrade.UpgradeCheckDescription HOSTS_REPOSITORY_VERSION = new org.apache.ambari.spi.upgrade.UpgradeCheckDescription("HOSTS_REPOSITORY_VERSION", org.apache.ambari.spi.upgrade.UpgradeCheckType.HOST, "All hosts should have target version installed", new com.google.common.collect.ImmutableMap.Builder<java.lang.String, java.lang.String>().put(org.apache.ambari.spi.upgrade.UpgradeCheckDescription.DEFAULT, "The following hosts must have version {{version}} installed: {{fails}}.").build());

    public HostsRepositoryVersionCheck() {
        super(org.apache.ambari.server.checks.HostsRepositoryVersionCheck.HOSTS_REPOSITORY_VERSION);
    }

    @java.lang.Override
    public org.apache.ambari.spi.upgrade.UpgradeCheckResult perform(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = new org.apache.ambari.spi.upgrade.UpgradeCheckResult(this);
        final java.lang.String clusterName = request.getClusterName();
        final org.apache.ambari.server.state.Cluster cluster = clustersProvider.get().getCluster(clusterName);
        final java.util.Map<java.lang.String, org.apache.ambari.server.state.Host> clusterHosts = clustersProvider.get().getHostsForCluster(clusterName);
        for (org.apache.ambari.server.state.Host host : clusterHosts.values()) {
            org.apache.ambari.server.state.MaintenanceState maintenanceState = host.getMaintenanceState(cluster.getClusterId());
            if (maintenanceState != org.apache.ambari.server.state.MaintenanceState.OFF) {
                continue;
            }
            org.apache.ambari.spi.RepositoryVersion repositoryVersion = request.getTargetRepositoryVersion();
            org.apache.ambari.server.state.StackId repositoryStackId = new org.apache.ambari.server.state.StackId(repositoryVersion.getStackId());
            final org.apache.ambari.server.orm.entities.HostVersionEntity hostVersion = hostVersionDaoProvider.get().findByClusterStackVersionAndHost(clusterName, repositoryStackId, repositoryVersion.getVersion(), host.getHostName());
            java.util.Set<org.apache.ambari.server.state.RepositoryVersionState> okStates = java.util.EnumSet.of(org.apache.ambari.server.state.RepositoryVersionState.INSTALLED, org.apache.ambari.server.state.RepositoryVersionState.NOT_REQUIRED);
            if ((hostVersion == null) || (!okStates.contains(hostVersion.getState()))) {
                result.getFailedOn().add(host.getHostName());
                result.getFailedDetail().add(new org.apache.ambari.server.checks.ClusterCheck.HostDetail(host.getHostId(), host.getHostName()));
            }
        }
        if (!result.getFailedOn().isEmpty()) {
            result.setStatus(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL);
            result.setFailReason(getFailReason(result, request));
        }
        return result;
    }
}