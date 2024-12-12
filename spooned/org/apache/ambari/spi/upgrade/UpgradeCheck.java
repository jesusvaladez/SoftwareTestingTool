package org.apache.ambari.spi.upgrade;
public interface UpgradeCheck {
    java.util.Set<java.lang.String> getApplicableServices();

    java.util.List<org.apache.ambari.spi.upgrade.CheckQualification> getQualifications();

    org.apache.ambari.spi.upgrade.UpgradeCheckResult perform(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.spi.upgrade.UpgradeCheckDescription getCheckDescription();
}