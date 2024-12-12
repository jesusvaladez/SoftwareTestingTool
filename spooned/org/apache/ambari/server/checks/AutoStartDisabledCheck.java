package org.apache.ambari.server.checks;
import org.apache.commons.lang.StringUtils;
@com.google.inject.Singleton
@org.apache.ambari.annotations.UpgradeCheckInfo(group = org.apache.ambari.spi.upgrade.UpgradeCheckGroup.CONFIGURATION_WARNING, required = { org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.HOST_ORDERED })
public class AutoStartDisabledCheck extends org.apache.ambari.server.checks.ClusterCheck {
    static final org.apache.ambari.spi.upgrade.UpgradeCheckDescription AUTO_START_DISABLED = new org.apache.ambari.spi.upgrade.UpgradeCheckDescription("AUTO_START_DISABLED", org.apache.ambari.spi.upgrade.UpgradeCheckType.CLUSTER, "Auto-Start Disabled Check", new com.google.common.collect.ImmutableMap.Builder<java.lang.String, java.lang.String>().put(org.apache.ambari.spi.upgrade.UpgradeCheckDescription.DEFAULT, "Auto Start must be disabled before performing an Upgrade. To disable Auto Start, navigate to " + "Admin > Service Auto Start. Turn the toggle switch off to Disabled and hit Save.").build());

    static final java.lang.String CLUSTER_ENV_TYPE = "cluster-env";

    static final java.lang.String RECOVERY_ENABLED_KEY = "recovery_enabled";

    static final java.lang.String RECOVERY_TYPE_KEY = "recovery_type";

    static final java.lang.String RECOVERY_AUTO_START = "AUTO_START";

    public AutoStartDisabledCheck() {
        super(org.apache.ambari.server.checks.AutoStartDisabledCheck.AUTO_START_DISABLED);
    }

    @java.lang.Override
    public org.apache.ambari.spi.upgrade.UpgradeCheckResult perform(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = new org.apache.ambari.spi.upgrade.UpgradeCheckResult(this, org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS);
        java.lang.String autoStartEnabled = getProperty(request, org.apache.ambari.server.checks.AutoStartDisabledCheck.CLUSTER_ENV_TYPE, org.apache.ambari.server.checks.AutoStartDisabledCheck.RECOVERY_ENABLED_KEY);
        if (!java.lang.Boolean.valueOf(autoStartEnabled)) {
            return result;
        }
        java.lang.String recoveryType = getProperty(request, org.apache.ambari.server.checks.AutoStartDisabledCheck.CLUSTER_ENV_TYPE, org.apache.ambari.server.checks.AutoStartDisabledCheck.RECOVERY_TYPE_KEY);
        if (org.apache.commons.lang.StringUtils.equals(recoveryType, org.apache.ambari.server.checks.AutoStartDisabledCheck.RECOVERY_AUTO_START)) {
            result.setFailReason(getFailReason(result, request));
            result.setStatus(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL);
            result.getFailedOn().add(request.getClusterInformation().getClusterName());
        }
        return result;
    }
}