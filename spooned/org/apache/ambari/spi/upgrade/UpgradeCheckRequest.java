package org.apache.ambari.spi.upgrade;
public class UpgradeCheckRequest {
    private final org.apache.ambari.spi.ClusterInformation m_clusterInformation;

    private final org.apache.ambari.spi.upgrade.UpgradeType m_upgradeType;

    private boolean m_revert = false;

    private final org.apache.ambari.spi.RepositoryVersion m_targetRepositoryVersion;

    private final java.util.Map<java.lang.String, java.lang.String> m_checkConfigurations;

    private final org.apache.ambari.spi.net.HttpURLConnectionProvider m_httpURLConnectionProvider;

    private java.util.Map<org.apache.ambari.spi.upgrade.UpgradeCheckDescription, org.apache.ambari.spi.upgrade.UpgradeCheckStatus> m_results = new java.util.HashMap<>();

    public UpgradeCheckRequest(org.apache.ambari.spi.ClusterInformation clusterInformation, org.apache.ambari.spi.upgrade.UpgradeType upgradeType, org.apache.ambari.spi.RepositoryVersion targetRepositoryVersion, java.util.Map<java.lang.String, java.lang.String> checkConfigurations, org.apache.ambari.spi.net.HttpURLConnectionProvider httpURLConnectionProvider) {
        m_clusterInformation = clusterInformation;
        m_upgradeType = upgradeType;
        m_targetRepositoryVersion = targetRepositoryVersion;
        m_checkConfigurations = checkConfigurations;
        m_httpURLConnectionProvider = httpURLConnectionProvider;
    }

    public org.apache.ambari.spi.ClusterInformation getClusterInformation() {
        return m_clusterInformation;
    }

    public java.lang.String getClusterName() {
        return m_clusterInformation.getClusterName();
    }

    public org.apache.ambari.spi.upgrade.UpgradeType getUpgradeType() {
        return m_upgradeType;
    }

    public org.apache.ambari.spi.RepositoryVersion getTargetRepositoryVersion() {
        return m_targetRepositoryVersion;
    }

    public void setRevert(boolean revert) {
        m_revert = revert;
    }

    public boolean isRevert() {
        return m_revert;
    }

    public java.util.Map<java.lang.String, java.lang.String> getCheckConfigurations() {
        return m_checkConfigurations;
    }

    public void addResult(org.apache.ambari.spi.upgrade.UpgradeCheckDescription description, org.apache.ambari.spi.upgrade.UpgradeCheckStatus status) {
        m_results.put(description, status);
    }

    public org.apache.ambari.spi.upgrade.UpgradeCheckStatus getResult(org.apache.ambari.spi.upgrade.UpgradeCheckDescription description) {
        return m_results.get(description);
    }

    public org.apache.ambari.spi.net.HttpURLConnectionProvider getHttpURLConnectionProvider() {
        return m_httpURLConnectionProvider;
    }
}