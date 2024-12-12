package org.apache.ambari.spi.upgrade;
public interface UpgradeAction {
    org.apache.ambari.spi.upgrade.UpgradeActionOperations getOperations(org.apache.ambari.spi.ClusterInformation clusterInformation, org.apache.ambari.spi.upgrade.UpgradeInformation upgradeInformation) throws org.apache.ambari.spi.exceptions.UpgradeActionException;
}