package org.apache.ambari.server.checks;
import org.apache.commons.lang.ArrayUtils;
public final class UpgradeTypeQualification implements org.apache.ambari.spi.upgrade.CheckQualification {
    private final java.lang.Class<? extends org.apache.ambari.spi.upgrade.UpgradeCheck> m_checkClass;

    public UpgradeTypeQualification(java.lang.Class<? extends org.apache.ambari.spi.upgrade.UpgradeCheck> checkClass) {
        m_checkClass = checkClass;
    }

    @java.lang.Override
    public boolean isApplicable(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.spi.upgrade.UpgradeType upgradeType = request.getUpgradeType();
        org.apache.ambari.annotations.UpgradeCheckInfo annotation = m_checkClass.getAnnotation(org.apache.ambari.annotations.UpgradeCheckInfo.class);
        if (null == annotation) {
            return true;
        }
        org.apache.ambari.spi.upgrade.UpgradeType[] upgradeTypes = annotation.required();
        return org.apache.commons.lang.ArrayUtils.isEmpty(upgradeTypes) || org.apache.commons.lang.ArrayUtils.contains(upgradeTypes, upgradeType);
    }
}