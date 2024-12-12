package org.apache.ambari.server.sample.checks;
public class SampleServiceCheck extends org.apache.ambari.server.checks.ClusterCheck {
    public SampleServiceCheck() {
        super(new org.apache.ambari.spi.upgrade.UpgradeCheckDescription("SAMPLE_SERVICE_CHECK", org.apache.ambari.spi.upgrade.UpgradeCheckType.HOST, "Sample service check description.", new com.google.common.collect.ImmutableMap.Builder<java.lang.String, java.lang.String>().put(org.apache.ambari.spi.upgrade.UpgradeCheckDescription.DEFAULT, "Sample service check default property description.").build()));
    }

    @java.lang.Override
    public org.apache.ambari.spi.upgrade.UpgradeCheckResult perform(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = new org.apache.ambari.spi.upgrade.UpgradeCheckResult(this);
        result.setFailReason("Sample service check always fails.");
        result.setStatus(org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL);
        return result;
    }
}