package org.apache.ambari.spi.upgrade;
public class UpgradeInformation {
    private final boolean m_isUpgrade;

    private final org.apache.ambari.spi.upgrade.UpgradeType m_upgradeType;

    private final org.apache.ambari.spi.RepositoryVersion m_targetVersion;

    private final java.util.Map<java.lang.String, org.apache.ambari.spi.RepositoryVersion> m_sourceVersions;

    private final java.util.Map<java.lang.String, org.apache.ambari.spi.RepositoryVersion> m_targetVersions;

    public UpgradeInformation(boolean isUpgrade, org.apache.ambari.spi.upgrade.UpgradeType upgradeType, org.apache.ambari.spi.RepositoryVersion targetVersion, java.util.Map<java.lang.String, org.apache.ambari.spi.RepositoryVersion> sourceVersions, java.util.Map<java.lang.String, org.apache.ambari.spi.RepositoryVersion> targetVersions) {
        m_isUpgrade = isUpgrade;
        m_upgradeType = upgradeType;
        m_targetVersion = targetVersion;
        m_sourceVersions = sourceVersions;
        m_targetVersions = targetVersions;
    }

    public boolean isUpgrade() {
        return m_isUpgrade;
    }

    public org.apache.ambari.spi.upgrade.UpgradeType getUpgradeType() {
        return m_upgradeType;
    }

    public org.apache.ambari.spi.RepositoryVersion getRepositoryVersion() {
        return m_targetVersion;
    }

    public java.util.Map<java.lang.String, org.apache.ambari.spi.RepositoryVersion> getSourceVersions() {
        return m_sourceVersions;
    }

    public java.util.Map<java.lang.String, org.apache.ambari.spi.RepositoryVersion> getTargetVersions() {
        return m_targetVersions;
    }
}