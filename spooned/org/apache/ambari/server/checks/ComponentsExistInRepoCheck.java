package org.apache.ambari.server.checks;
@com.google.inject.Singleton
@org.apache.ambari.annotations.UpgradeCheckInfo(group = org.apache.ambari.spi.upgrade.UpgradeCheckGroup.INFORMATIONAL_WARNING, required = { org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING })
public class ComponentsExistInRepoCheck extends org.apache.ambari.server.checks.ClusterCheck {
    public static final java.lang.String AUTO_REMOVE = "auto_remove";

    public static final java.lang.String MANUAL_REMOVE = "manual_remove";

    @com.google.inject.Inject
    org.apache.ambari.server.state.ServiceComponentSupport serviceComponentSupport;

    @com.google.inject.Inject
    org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeHelper upgradeHelper;

    static final org.apache.ambari.spi.upgrade.UpgradeCheckDescription COMPONENTS_EXIST_IN_TARGET_REPO = new org.apache.ambari.spi.upgrade.UpgradeCheckDescription("COMPONENTS_EXIST_IN_TARGET_REPO", org.apache.ambari.spi.upgrade.UpgradeCheckType.CLUSTER, "Check installed services which are not supported in the installed stack", new com.google.common.collect.ImmutableMap.Builder<java.lang.String, java.lang.String>().put(org.apache.ambari.server.checks.ComponentsExistInRepoCheck.AUTO_REMOVE, "The following services and/or components do not exist in the target stack and will be automatically removed during the upgrade.").put(org.apache.ambari.server.checks.ComponentsExistInRepoCheck.MANUAL_REMOVE, "The following components do not exist in the target repository's stack. They must be removed from the cluster before upgrading.").build());

    public ComponentsExistInRepoCheck() {
        super(org.apache.ambari.server.checks.ComponentsExistInRepoCheck.COMPONENTS_EXIST_IN_TARGET_REPO);
    }

    @java.lang.Override
    public org.apache.ambari.spi.upgrade.UpgradeCheckResult perform(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = new org.apache.ambari.spi.upgrade.UpgradeCheckResult(this);
        org.apache.ambari.spi.RepositoryVersion repositoryVersion = request.getTargetRepositoryVersion();
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId(repositoryVersion.getStackId());
        org.apache.ambari.server.state.Cluster cluster = clustersProvider.get().getCluster(request.getClusterName());
        java.lang.String stackName = stackId.getStackName();
        java.lang.String stackVersion = stackId.getStackVersion();
        java.util.Collection<java.lang.String> allUnsupported = serviceComponentSupport.allUnsupported(cluster, stackName, stackVersion);
        report(result, request, allUnsupported);
        return result;
    }

    private void report(org.apache.ambari.spi.upgrade.UpgradeCheckResult result, org.apache.ambari.spi.upgrade.UpgradeCheckRequest request, java.util.Collection<java.lang.String> allUnsupported) throws org.apache.ambari.server.AmbariException {
        if (allUnsupported.isEmpty()) {
            result.setStatus(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS);
            return;
        }
        result.setFailedOn(new java.util.LinkedHashSet<>(allUnsupported));
        if (hasDeleteUnsupportedServicesAction(upgradePack(request))) {
            result.setStatus(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.WARNING);
            result.setFailReason(getFailReason(org.apache.ambari.server.checks.ComponentsExistInRepoCheck.AUTO_REMOVE, result, request));
        } else {
            result.setStatus(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL);
            result.setFailReason(getFailReason(org.apache.ambari.server.checks.ComponentsExistInRepoCheck.MANUAL_REMOVE, result, request));
        }
    }

    private org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Cluster cluster = clustersProvider.get().getCluster(request.getClusterName());
        return upgradeHelper.suggestUpgradePack(request.getClusterName(), cluster.getCurrentStackVersion(), new org.apache.ambari.server.state.StackId(request.getTargetRepositoryVersion().getStackId()), org.apache.ambari.server.stack.upgrade.Direction.UPGRADE, request.getUpgradeType(), null);
    }

    private boolean hasDeleteUnsupportedServicesAction(org.apache.ambari.server.stack.upgrade.UpgradePack upgradePack) {
        return upgradePack.getAllGroups().stream().filter(org.apache.ambari.server.stack.upgrade.ClusterGrouping.class::isInstance).flatMap(group -> ((org.apache.ambari.server.stack.upgrade.ClusterGrouping) (group)).executionStages.stream()).map(executeStage -> executeStage.task).anyMatch(this::isDeleteUnsupportedTask);
    }

    private boolean isDeleteUnsupportedTask(org.apache.ambari.server.stack.upgrade.Task task) {
        return (task instanceof org.apache.ambari.server.stack.upgrade.ServerActionTask) && org.apache.ambari.server.serveraction.upgrades.DeleteUnsupportedServicesAndComponents.class.getName().equals(((org.apache.ambari.server.stack.upgrade.ServerActionTask) (task)).getImplementationClass());
    }
}