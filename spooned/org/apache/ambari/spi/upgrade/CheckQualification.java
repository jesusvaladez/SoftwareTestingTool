package org.apache.ambari.spi.upgrade;
public interface CheckQualification {
    boolean isApplicable(org.apache.ambari.spi.upgrade.UpgradeCheckRequest request) throws org.apache.ambari.server.AmbariException;
}