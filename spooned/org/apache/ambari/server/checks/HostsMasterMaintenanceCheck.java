package org.apache.ambari.server.checks;
@com.google.inject.Singleton
@org.apache.ambari.annotations.UpgradeCheckInfo(group = org.apache.ambari.spi.upgrade.UpgradeCheckGroup.MAINTENANCE_MODE, order = 5.0F, required = { org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.HOST_ORDERED })
public class HostsMasterMaintenanceCheck extends org.apache.ambari.server.checks.ClusterCheck {
    static final java.lang.String KEY_NO_UPGRADE_NAME = "no_upgrade_name";

    static final java.lang.String KEY_NO_UPGRADE_PACK = "no_upgrade_pack";

    static final org.apache.ambari.spi.upgrade.UpgradeCheckDescription HOSTS_MASTER_MAINTENANCE = new org.apache.ambari.spi.upgrade.UpgradeCheckDescription("HOSTS_MASTER_MAINTENANCE", org.apache.ambari.spi.upgrade.UpgradeCheckType.HOST, "Hosts in Maintenance Mode must not have any master components", new com.google.common.collect.ImmutableMap.Builder<java.lang.String, java.lang.String>().put(org.apache.ambari.spi.upgrade.UpgradeCheckDescription.DEFAULT, "The following hosts must not be in in Maintenance Mode since they host Master components: {{fails}}.").put(org.apache.ambari.server.checks.HostsMasterMaintenanceCheck.KEY_NO_UPGRADE_NAME, "Could not find suitable upgrade pack for %s %s to version {{version}}.").put(org.apache.ambari.server.checks.HostsMasterMaintenanceCheck.KEY_NO_UPGRADE_PACK, "Could not find upgrade pack named %s.").build());

    public HostsMasterMaintenanceCheck() {
        super(org.apache.ambari.server.checks.HostsMasterMaintenanceCheck.HOSTS_MASTER_MAINTENANCE);
    }

    @java.lang.Override
    public org.apache.ambari.spi.upgrade.UpgradeCheckResult perform(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = new org.apache.ambari.spi.upgrade.UpgradeCheckResult(this);
        final java.lang.String clusterName = request.getClusterName();
        final org.apache.ambari.server.state.Cluster cluster = clustersProvider.get().getCluster(clusterName);
        org.apache.ambari.spi.RepositoryVersion repositoryVersion = request.getTargetRepositoryVersion();
        final org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId(repositoryVersion.getStackId());
        final java.util.Set<java.lang.String> hostsWithMasterComponent = new java.util.HashSet<>();
        final java.lang.String upgradePackName = repositoryVersionHelper.get().getUpgradePackageName(stackId.getStackName(), stackId.getStackVersion(), repositoryVersion.getVersion(), null);
        if (upgradePackName == null) {
            result.setStatus(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL);
            java.lang.String fail = getFailReason(org.apache.ambari.server.checks.HostsMasterMaintenanceCheck.KEY_NO_UPGRADE_NAME, result, request);
            result.setFailReason(java.lang.String.format(fail, stackId.getStackName(), stackId.getStackVersion()));
            return result;
        }
        final org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack = ambariMetaInfo.get().getUpgradePacks(stackId.getStackName(), stackId.getStackVersion()).get(upgradePackName);
        if (upgradePack == null) {
            result.setStatus(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL);
            java.lang.String fail = getFailReason(org.apache.ambari.server.checks.HostsMasterMaintenanceCheck.KEY_NO_UPGRADE_PACK, result, request);
            result.setFailReason(java.lang.String.format(fail, upgradePackName));
            return result;
        }
        final java.util.Set<java.lang.String> componentsFromUpgradePack = new java.util.HashSet<>();
        for (java.util.Map<java.lang.String, org.apache.ambari.server.stack.upgrade.UpgradePack.ProcessingComponent> task : upgradePack.getTasks().values()) {
            componentsFromUpgradePack.addAll(task.keySet());
        }
        for (org.apache.ambari.server.state.Service service : cluster.getServices().values()) {
            for (org.apache.ambari.server.state.ServiceComponent serviceComponent : service.getServiceComponents().values()) {
                if (serviceComponent.isMasterComponent() && componentsFromUpgradePack.contains(serviceComponent.getName())) {
                    hostsWithMasterComponent.addAll(serviceComponent.getServiceComponentHosts().keySet());
                }
            }
        }
        final java.util.Map<java.lang.String, org.apache.ambari.server.state.Host> clusterHosts = clustersProvider.get().getHostsForCluster(clusterName);
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.Host> hostEntry : clusterHosts.entrySet()) {
            final org.apache.ambari.server.state.Host host = hostEntry.getValue();
            if ((host.getMaintenanceState(cluster.getClusterId()) == org.apache.ambari.server.state.MaintenanceState.ON) && hostsWithMasterComponent.contains(host.getHostName())) {
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