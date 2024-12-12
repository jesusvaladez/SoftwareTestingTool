package org.apache.ambari.spi.upgrade;
public class PriorCheckQualification implements org.apache.ambari.spi.upgrade.CheckQualification {
    private final org.apache.ambari.spi.upgrade.UpgradeCheckDescription m_checkDescription;

    public PriorCheckQualification(org.apache.ambari.spi.upgrade.UpgradeCheckDescription checkDescription) {
        m_checkDescription = checkDescription;
    }

    @java.lang.Override
    public boolean isApplicable(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.spi.upgrade.UpgradeCheckStatus checkStatus = request.getResult(m_checkDescription);
        if ((null != checkStatus) && (checkStatus == org.apache.ambari.spi.upgrade.UpgradeCheckStatus.FAIL)) {
            return false;
        }
        return true;
    }
}