package org.apache.ambari.server.checks;
import static org.apache.ambari.server.state.MaintenanceState.OFF;
@com.google.inject.Singleton
@org.apache.ambari.annotations.UpgradeCheckInfo(group = org.apache.ambari.spi.upgrade.UpgradeCheckGroup.REPOSITORY_VERSION, required = { org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.ROLLING })
public class MissingOsInRepoVersionCheck extends org.apache.ambari.server.checks.ClusterCheck {
    public static final java.lang.String SOURCE_OS = "source_os";

    public static final java.lang.String TARGET_OS = "target_os";

    static final org.apache.ambari.spi.upgrade.UpgradeCheckDescription MISSING_OS_IN_REPO_VERSION = new org.apache.ambari.spi.upgrade.UpgradeCheckDescription("MISSING_OS_IN_REPO_VERSION", org.apache.ambari.spi.upgrade.UpgradeCheckType.CLUSTER, "Missing OS in repository version.", new com.google.common.collect.ImmutableMap.Builder<java.lang.String, java.lang.String>().put(org.apache.ambari.server.checks.MissingOsInRepoVersionCheck.SOURCE_OS, "The source version must have an entry for each OS type in the cluster").put(org.apache.ambari.server.checks.MissingOsInRepoVersionCheck.TARGET_OS, "The target version must have an entry for each OS type in the cluster").build());

    public MissingOsInRepoVersionCheck() {
        super(org.apache.ambari.server.checks.MissingOsInRepoVersionCheck.MISSING_OS_IN_REPO_VERSION);
    }

    @java.lang.Override
    public org.apache.ambari.spi.upgrade.UpgradeCheckResult perform(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = new org.apache.ambari.spi.upgrade.UpgradeCheckResult(this);
        java.util.Set<java.lang.String> osFamiliesInCluster = osFamiliesInCluster(cluster(request));
        if (!targetOsFamilies(request).containsAll(osFamiliesInCluster)) {
            result.setFailReason(getFailReason(org.apache.ambari.server.checks.MissingOsInRepoVersionCheck.TARGET_OS, result, request));
            result.setStatus(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL);
            result.setFailedOn(new java.util.LinkedHashSet<>(osFamiliesInCluster));
        } else if (!sourceOsFamilies(request).containsAll(osFamiliesInCluster)) {
            result.setFailReason(getFailReason(org.apache.ambari.server.checks.MissingOsInRepoVersionCheck.SOURCE_OS, result, request));
            result.setStatus(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL);
            result.setFailedOn(new java.util.LinkedHashSet<>(osFamiliesInCluster));
        }
        return result;
    }

    private org.apache.ambari.server.state.Cluster cluster(org.apache.ambari.spi.upgrade.UpgradeCheckRequest prerequisiteCheck) throws org.apache.ambari.server.AmbariException {
        return clustersProvider.get().getCluster(prerequisiteCheck.getClusterName());
    }

    private java.util.Set<java.lang.String> osFamiliesInCluster(org.apache.ambari.server.state.Cluster cluster) {
        return cluster.getHosts().stream().filter(host -> host.getMaintenanceState(cluster.getClusterId()) == org.apache.ambari.server.state.MaintenanceState.OFF).map(org.apache.ambari.server.state.Host::getOsFamily).collect(java.util.stream.Collectors.toSet());
    }

    private java.util.Set<java.lang.String> sourceOsFamilies(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId(request.getTargetRepositoryVersion().getStackId());
        return ambariMetaInfo.get().getStack(stackId).getRepositoriesByOs().keySet();
    }

    private java.util.Set<java.lang.String> targetOsFamilies(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) {
        org.apache.ambari.spi.RepositoryVersion repositoryVersion = request.getTargetRepositoryVersion();
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity entity = repositoryVersionDaoProvider.get().findByPK(repositoryVersion.getId());
        return entity.getRepoOsEntities().stream().map(org.apache.ambari.server.orm.entities.RepoOsEntity::getFamily).collect(java.util.stream.Collectors.toSet());
    }
}