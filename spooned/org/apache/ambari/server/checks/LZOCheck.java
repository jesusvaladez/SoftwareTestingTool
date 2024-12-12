package org.apache.ambari.server.checks;
import org.apache.commons.lang.StringUtils;
@com.google.inject.Singleton
@org.apache.ambari.annotations.UpgradeCheckInfo(group = org.apache.ambari.spi.upgrade.UpgradeCheckGroup.INFORMATIONAL_WARNING)
public class LZOCheck extends org.apache.ambari.server.checks.ClusterCheck {
    static final java.lang.String IO_COMPRESSION_CODECS = "io.compression.codecs";

    static final java.lang.String LZO_ENABLE_KEY = "io.compression.codec.lzo.class";

    static final java.lang.String LZO_ENABLE_VALUE = "com.hadoop.compression.lzo.LzoCodec";

    static final org.apache.ambari.spi.upgrade.UpgradeCheckDescription LZO_CONFIG_CHECK = new org.apache.ambari.spi.upgrade.UpgradeCheckDescription("LZO_CONFIG_CHECK", org.apache.ambari.spi.upgrade.UpgradeCheckType.CLUSTER, "LZO Codec Check", new com.google.common.collect.ImmutableMap.Builder<java.lang.String, java.lang.String>().put(org.apache.ambari.spi.upgrade.UpgradeCheckDescription.DEFAULT, "You have LZO codec enabled in the core-site config of your cluster. LZO is no longer installed automatically. " + ("If any hosts require LZO, it should be installed before starting the upgrade. " + "Consult Ambari documentation for instructions on how to do this.")).build());

    public LZOCheck() {
        super(org.apache.ambari.server.checks.LZOCheck.LZO_CONFIG_CHECK);
    }

    @java.lang.Override
    public org.apache.ambari.spi.upgrade.UpgradeCheckResult perform(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.spi.upgrade.UpgradeCheckResult result = new org.apache.ambari.spi.upgrade.UpgradeCheckResult(this);
        if (config.getGplLicenseAccepted()) {
            return result;
        }
        java.util.List<java.lang.String> errorMessages = new java.util.ArrayList<>();
        org.apache.ambari.spi.upgrade.UpgradeCheckStatus checkStatus = org.apache.ambari.spi.upgrade.UpgradeCheckStatus.WARNING;
        java.lang.String codecs = getProperty(request, "core-site", org.apache.ambari.server.checks.LZOCheck.IO_COMPRESSION_CODECS);
        if ((codecs != null) && codecs.contains(org.apache.ambari.server.checks.LZOCheck.LZO_ENABLE_VALUE)) {
            errorMessages.add(getFailReason(org.apache.ambari.server.checks.LZOCheck.IO_COMPRESSION_CODECS, result, request));
        }
        java.lang.String classValue = getProperty(request, "core-site", org.apache.ambari.server.checks.LZOCheck.LZO_ENABLE_KEY);
        if (org.apache.ambari.server.checks.LZOCheck.LZO_ENABLE_VALUE.equals(classValue)) {
            errorMessages.add(getFailReason(org.apache.ambari.server.checks.LZOCheck.LZO_ENABLE_KEY, result, request));
        }
        if (!errorMessages.isEmpty()) {
            result.setFailReason(org.apache.commons.lang.StringUtils.join(errorMessages, "You have LZO codec enabled in the core-site config of your cluster. " + (("You have to accept GPL license during ambari-server setup to have LZO installed automatically. " + "If any hosts require LZO, it should be installed before starting the upgrade. ") + "Consult Ambari documentation for instructions on how to do this.")));
            result.getFailedOn().add("LZO");
            result.setStatus(checkStatus);
        }
        return result;
    }
}