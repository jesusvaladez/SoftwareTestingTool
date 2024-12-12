package org.apache.ambari.server.checks;
@com.google.inject.Singleton
@org.apache.ambari.annotations.UpgradeCheckInfo(group = org.apache.ambari.spi.upgrade.UpgradeCheckGroup.DEFAULT, order = 4.0F, required = { org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.HOST_ORDERED })
public class PreviousUpgradeCompleted extends org.apache.ambari.server.checks.ClusterCheck {
    static final org.apache.ambari.spi.upgrade.UpgradeCheckDescription PREVIOUS_UPGRADE_COMPLETED = new org.apache.ambari.spi.upgrade.UpgradeCheckDescription("PREVIOUS_UPGRADE_COMPLETED", org.apache.ambari.spi.upgrade.UpgradeCheckType.CLUSTER, "A previous upgrade did not complete.", new com.google.common.collect.ImmutableMap.Builder<java.lang.String, java.lang.String>().put(org.apache.ambari.spi.upgrade.UpgradeCheckDescription.DEFAULT, "The last upgrade attempt did not complete. {{fails}}").build());

    public static final java.lang.String ERROR_MESSAGE = "There is an existing {0} {1} {2} which has not completed. This {3} must be completed before a new upgrade or downgrade can begin.";

    public PreviousUpgradeCompleted() {
        super(org.apache.ambari.server.checks.PreviousUpgradeCompleted.PREVIOUS_UPGRADE_COMPLETED);
    }

    @java.lang.Override
    public org.apache.ambari.spi.upgrade.UpgradeCheckResult perform(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = new org.apache.ambari.spi.upgrade.UpgradeCheckResult(this);
        final java.lang.String clusterName = request.getClusterName();
        final org.apache.ambari.server.state.Cluster cluster = clustersProvider.get().getCluster(clusterName);
        java.lang.String errorMessage = null;
        org.apache.ambari.server.orm.entities.UpgradeEntity upgradeInProgress = cluster.getUpgradeInProgress();
        if (null != upgradeInProgress) {
            org.apache.ambari.server.stack.upgrade.Direction direction = upgradeInProgress.getDirection();
            java.lang.String directionText = direction.getText(false);
            java.lang.String prepositionText = direction.getPreposition();
            errorMessage = java.text.MessageFormat.format(org.apache.ambari.server.checks.PreviousUpgradeCompleted.ERROR_MESSAGE, directionText, prepositionText, upgradeInProgress.getRepositoryVersion().getVersion(), directionText);
        }
        if (null != errorMessage) {
            java.util.LinkedHashSet<java.lang.String> failedOn = new java.util.LinkedHashSet<>();
            failedOn.add(cluster.getClusterName());
            result.setFailedOn(failedOn);
            result.setStatus(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL);
            result.setFailReason(errorMessage);
        }
        return result;
    }
}