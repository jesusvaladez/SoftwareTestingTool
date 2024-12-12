package org.apache.ambari.server.checks;
import org.apache.commons.lang.StringUtils;
@com.google.inject.Singleton
@org.apache.ambari.annotations.UpgradeCheckInfo(order = 99.0F, required = { org.apache.ambari.spi.upgrade.UpgradeType.ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.NON_ROLLING, org.apache.ambari.spi.upgrade.UpgradeType.HOST_ORDERED })
public class ConfigurationMergeCheck extends org.apache.ambari.server.checks.ClusterCheck {
    @com.google.inject.Inject
    org.apache.ambari.server.state.ConfigMergeHelper m_mergeHelper;

    static final org.apache.ambari.spi.upgrade.UpgradeCheckDescription CONFIG_MERGE = new org.apache.ambari.spi.upgrade.UpgradeCheckDescription("CONFIG_MERGE", org.apache.ambari.spi.upgrade.UpgradeCheckType.CLUSTER, "Configuration Merge Check", new com.google.common.collect.ImmutableMap.Builder<java.lang.String, java.lang.String>().put(org.apache.ambari.spi.upgrade.UpgradeCheckDescription.DEFAULT, "The following config types will have values overwritten: %s").build());

    public ConfigurationMergeCheck() {
        super(org.apache.ambari.server.checks.ConfigurationMergeCheck.CONFIG_MERGE);
    }

    @java.lang.Override
    public org.apache.ambari.spi.upgrade.UpgradeCheckResult perform(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = new org.apache.ambari.spi.upgrade.UpgradeCheckResult(this);
        org.apache.ambari.spi.RepositoryVersion repositoryVersion = request.getTargetRepositoryVersion();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.state.ConfigMergeHelper.ThreeWayValue>> changes = m_mergeHelper.getConflicts(request.getClusterName(), new org.apache.ambari.server.state.StackId(repositoryVersion.getStackId()));
        java.util.Set<java.lang.String> failedTypes = new java.util.HashSet<>();
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.state.ConfigMergeHelper.ThreeWayValue>> entry : changes.entrySet()) {
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.ConfigMergeHelper.ThreeWayValue> configEntry : entry.getValue().entrySet()) {
                org.apache.ambari.server.state.ConfigMergeHelper.ThreeWayValue twv = configEntry.getValue();
                if (null == twv.oldStackValue) {
                    failedTypes.add(entry.getKey());
                    result.getFailedOn().add((entry.getKey() + "/") + configEntry.getKey());
                    org.apache.ambari.server.checks.ConfigurationMergeCheck.MergeDetail md = new org.apache.ambari.server.checks.ConfigurationMergeCheck.MergeDetail();
                    md.type = entry.getKey();
                    md.property = configEntry.getKey();
                    md.current = twv.savedValue;
                    md.new_stack_value = twv.newStackValue;
                    md.result_value = md.current;
                    result.getFailedDetail().add(md);
                } else if (!twv.oldStackValue.equals(twv.savedValue)) {
                    if ((null == twv.newStackValue) || (!twv.oldStackValue.equals(twv.newStackValue))) {
                        failedTypes.add(entry.getKey());
                        result.getFailedOn().add((entry.getKey() + "/") + configEntry.getKey());
                        org.apache.ambari.server.checks.ConfigurationMergeCheck.MergeDetail md = new org.apache.ambari.server.checks.ConfigurationMergeCheck.MergeDetail();
                        md.type = entry.getKey();
                        md.property = configEntry.getKey();
                        md.current = twv.savedValue;
                        md.new_stack_value = twv.newStackValue;
                        md.result_value = md.current;
                        result.getFailedDetail().add(md);
                    }
                }
            }
        }
        if (result.getFailedOn().size() > 0) {
            result.setStatus(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.WARNING);
            java.lang.String failReason = getFailReason(result, request);
            result.setFailReason(java.lang.String.format(failReason, org.apache.commons.lang.StringUtils.join(failedTypes, ", ")));
        } else {
            result.setStatus(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS);
        }
        return result;
    }

    public static class MergeDetail {
        public java.lang.String type = null;

        public java.lang.String property = null;

        public java.lang.String current = null;

        public java.lang.String new_stack_value = null;

        public java.lang.String result_value = null;
    }
}