package org.apache.ambari.server.checks;
import org.apache.commons.lang.StringUtils;
@com.google.inject.Singleton
@org.apache.ambari.annotations.UpgradeCheckInfo(group = org.apache.ambari.spi.upgrade.UpgradeCheckGroup.DEFAULT, order = 3.0F, required = { org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.HOST_ORDERED })
public class InstallPackagesCheck extends org.apache.ambari.server.checks.ClusterCheck {
    static final org.apache.ambari.spi.upgrade.UpgradeCheckDescription INSTALL_PACKAGES_CHECK = new org.apache.ambari.spi.upgrade.UpgradeCheckDescription("INSTALL_PACKAGES_CHECK", org.apache.ambari.spi.upgrade.UpgradeCheckType.CLUSTER, "Install packages must be re-run", new com.google.common.collect.ImmutableMap.Builder<java.lang.String, java.lang.String>().put(org.apache.ambari.spi.upgrade.UpgradeCheckDescription.DEFAULT, "Re-run Install Packages before starting upgrade").build());

    public InstallPackagesCheck() {
        super(org.apache.ambari.server.checks.InstallPackagesCheck.INSTALL_PACKAGES_CHECK);
    }

    @java.lang.Override
    public org.apache.ambari.spi.upgrade.UpgradeCheckResult perform(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = new org.apache.ambari.spi.upgrade.UpgradeCheckResult(this);
        final java.lang.String clusterName = request.getClusterName();
        final org.apache.ambari.server.state.Cluster cluster = clustersProvider.get().getCluster(clusterName);
        org.apache.ambari.spi.RepositoryVersion repositoryVersion = request.getTargetRepositoryVersion();
        final org.apache.ambari.server.state.StackId targetStackId = new org.apache.ambari.server.state.StackId(repositoryVersion.getStackId());
        final java.util.Set<org.apache.ambari.server.checks.ClusterCheck.HostDetail> failedHosts = new java.util.TreeSet<>();
        for (org.apache.ambari.server.state.Host host : cluster.getHosts()) {
            if (host.getMaintenanceState(cluster.getClusterId()) != org.apache.ambari.server.state.MaintenanceState.ON) {
                for (org.apache.ambari.server.orm.entities.HostVersionEntity hve : hostVersionDaoProvider.get().findByHost(host.getHostName())) {
                    if (org.apache.commons.lang.StringUtils.equals(hve.getRepositoryVersion().getVersion(), repositoryVersion.getVersion()) && (hve.getState() == org.apache.ambari.server.state.RepositoryVersionState.INSTALL_FAILED)) {
                        failedHosts.add(new org.apache.ambari.server.checks.ClusterCheck.HostDetail(host.getHostId(), host.getHostName()));
                    }
                }
            }
        }
        if (!failedHosts.isEmpty()) {
            java.lang.String message = java.text.MessageFormat.format("Hosts in cluster [{0},{1},{2},{3}] are in INSTALL_FAILED state because " + ("Install Packages had failed. Please re-run Install Packages, if necessary place following hosts " + "in Maintenance mode: {4}"), cluster.getClusterName(), targetStackId.getStackName(), targetStackId.getStackVersion(), repositoryVersion.getVersion(), org.apache.commons.lang.StringUtils.join(failedHosts, ", "));
            java.util.LinkedHashSet<java.lang.String> failedHostNames = failedHosts.stream().map(failedHost -> failedHost.hostName).collect(java.util.stream.Collectors.toCollection(java.util.LinkedHashSet::new));
            result.setFailedOn(failedHostNames);
            result.setStatus(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL);
            result.setFailReason(message);
            result.getFailedDetail().addAll(failedHosts);
            return result;
        }
        result.setStatus(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS);
        return result;
    }
}