package org.apache.ambari.server.checks;
import org.apache.commons.lang.StringUtils;
@com.google.inject.Singleton
@org.apache.ambari.annotations.UpgradeCheckInfo(order = 98.0F, required = { org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.HOST_ORDERED })
public class HardcodedStackVersionPropertiesCheck extends org.apache.ambari.server.checks.ClusterCheck {
    static final org.apache.ambari.spi.upgrade.UpgradeCheckDescription HARDCODED_STACK_VERSION_PROPERTIES_CHECK = new org.apache.ambari.spi.upgrade.UpgradeCheckDescription("HARDCODED_STACK_VERSION_PROPERTIES_CHECK", org.apache.ambari.spi.upgrade.UpgradeCheckType.CLUSTER, "Found hardcoded stack version in property value.", new com.google.common.collect.ImmutableMap.Builder<java.lang.String, java.lang.String>().put(org.apache.ambari.spi.upgrade.UpgradeCheckDescription.DEFAULT, "Some properties seem to contain hardcoded stack version string \"%s\"." + " That is a potential problem when doing stack update.").build());

    public HardcodedStackVersionPropertiesCheck() {
        super(org.apache.ambari.server.checks.HardcodedStackVersionPropertiesCheck.HARDCODED_STACK_VERSION_PROPERTIES_CHECK);
    }

    @java.lang.Override
    public org.apache.ambari.spi.upgrade.UpgradeCheckResult perform(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = new org.apache.ambari.spi.upgrade.UpgradeCheckResult(this);
        org.apache.ambari.server.state.Cluster cluster = clustersProvider.get().getCluster(request.getClusterName());
        java.util.Set<java.lang.String> versions = new java.util.HashSet<>();
        java.util.Set<java.lang.String> failures = new java.util.HashSet<>();
        java.util.Set<java.lang.String> failedVersions = new java.util.HashSet<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs = cluster.getDesiredConfigs();
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.DesiredConfig> configEntry : desiredConfigs.entrySet()) {
            java.lang.String configType = configEntry.getKey();
            org.apache.ambari.server.state.DesiredConfig desiredConfig = configEntry.getValue();
            final org.apache.ambari.server.state.Config config = cluster.getConfig(configType, desiredConfig.getTag());
            java.util.Map<java.lang.String, java.lang.String> properties = config.getProperties();
            for (java.util.Map.Entry<java.lang.String, java.lang.String> property : properties.entrySet()) {
                for (java.lang.String version : versions) {
                    java.util.regex.Pattern searchPattern = org.apache.ambari.server.checks.HardcodedStackVersionPropertiesCheck.getHardcodeSearchPattern(version);
                    if (org.apache.ambari.server.checks.HardcodedStackVersionPropertiesCheck.stringContainsVersionHardcode(property.getValue(), searchPattern)) {
                        failedVersions.add(version);
                        failures.add(java.lang.String.format("%s/%s found a hardcoded value %s", configType, property.getKey(), version));
                    }
                }
            }
        }
        if (failures.size() > 0) {
            result.setStatus(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.WARNING);
            java.lang.String failReason = getFailReason(result, request);
            result.setFailReason(java.lang.String.format(failReason, org.apache.commons.lang.StringUtils.join(failedVersions, ',')));
            result.setFailedOn(new java.util.LinkedHashSet<>(failures));
        } else {
            result.setStatus(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS);
        }
        return result;
    }

    public static java.util.regex.Pattern getHardcodeSearchPattern(java.lang.String hdpVersion) {
        return java.util.regex.Pattern.compile("(?<!-Dhdp\\.version=)" + hdpVersion.replace(".", "\\."));
    }

    public static boolean stringContainsVersionHardcode(java.lang.String string, java.util.regex.Pattern searchPattern) {
        java.util.regex.Matcher matcher = searchPattern.matcher(string);
        return matcher.find();
    }
}