package org.apache.ambari.spi.upgrade;
public class UpgradeCheckResult {
    private org.apache.ambari.spi.upgrade.UpgradeCheck m_upgradeCheck;

    private org.apache.ambari.spi.upgrade.UpgradeCheckStatus m_status = org.apache.ambari.spi.upgrade.UpgradeCheckStatus.PASS;

    private java.lang.String m_failReason = "";

    private java.util.LinkedHashSet<java.lang.String> m_failedOn = new java.util.LinkedHashSet<>();

    private java.util.List<java.lang.Object> m_failedDetail = new java.util.ArrayList<>();

    public UpgradeCheckResult(org.apache.ambari.spi.upgrade.UpgradeCheck check) {
        m_upgradeCheck = check;
    }

    public UpgradeCheckResult(org.apache.ambari.spi.upgrade.UpgradeCheck check, org.apache.ambari.spi.upgrade.UpgradeCheckStatus status) {
        m_upgradeCheck = check;
        m_status = status;
    }

    public java.lang.String getId() {
        return m_upgradeCheck.getCheckDescription().name();
    }

    public java.lang.String getDescription() {
        return m_upgradeCheck.getCheckDescription().getText();
    }

    public org.apache.ambari.spi.upgrade.UpgradeCheckStatus getStatus() {
        return m_status;
    }

    public void setStatus(org.apache.ambari.spi.upgrade.UpgradeCheckStatus status) {
        m_status = status;
    }

    public java.lang.String getFailReason() {
        return m_failReason;
    }

    public void setFailReason(java.lang.String failReason) {
        m_failReason = failReason;
    }

    public java.util.LinkedHashSet<java.lang.String> getFailedOn() {
        return m_failedOn;
    }

    public java.util.List<java.lang.Object> getFailedDetail() {
        return m_failedDetail;
    }

    public void setFailedOn(java.util.LinkedHashSet<java.lang.String> failedOn) {
        m_failedOn = failedOn;
    }

    public org.apache.ambari.spi.upgrade.UpgradeCheckType getType() {
        return m_upgradeCheck.getCheckDescription().getType();
    }
}