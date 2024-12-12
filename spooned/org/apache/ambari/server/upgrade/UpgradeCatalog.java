package org.apache.ambari.server.upgrade;
public interface UpgradeCatalog {
    void upgradeSchema() throws org.apache.ambari.server.AmbariException, java.sql.SQLException;

    void preUpgradeData() throws org.apache.ambari.server.AmbariException, java.sql.SQLException;

    void upgradeData() throws org.apache.ambari.server.AmbariException, java.sql.SQLException;

    void setConfigUpdatesFileName(java.lang.String ambariUpgradeConfigUpdatesFileName);

    boolean isFinal();

    void onPostUpgrade() throws org.apache.ambari.server.AmbariException, java.sql.SQLException;

    java.lang.String getTargetVersion();

    java.lang.String getSourceVersion();

    void updateDatabaseSchemaVersion();

    java.util.Map<java.lang.String, java.lang.String> getUpgradeJsonOutput();
}