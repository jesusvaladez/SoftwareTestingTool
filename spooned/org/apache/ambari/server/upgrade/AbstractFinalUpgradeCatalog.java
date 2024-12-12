package org.apache.ambari.server.upgrade;
public abstract class AbstractFinalUpgradeCatalog extends org.apache.ambari.server.upgrade.AbstractUpgradeCatalog {
    AbstractFinalUpgradeCatalog(com.google.inject.Injector injector) {
        super(injector);
    }

    @java.lang.Override
    protected void executeDDLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
    }

    @java.lang.Override
    protected void executePreDMLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
    }

    @java.lang.Override
    public java.lang.String getTargetVersion() {
        return getFinalVersion();
    }

    @java.lang.Override
    public boolean isFinal() {
        return true;
    }

    private java.lang.String getFinalVersion() {
        return org.apache.ambari.server.utils.VersionUtils.getVersionSubstring(configuration.getServerVersion());
    }
}