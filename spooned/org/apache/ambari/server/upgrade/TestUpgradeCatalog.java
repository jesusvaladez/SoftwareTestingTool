package org.apache.ambari.server.upgrade;
abstract class TestUpgradeCatalog extends org.apache.ambari.server.upgrade.AbstractUpgradeCatalog {
    @com.google.inject.Inject
    public TestUpgradeCatalog(com.google.inject.Injector injector) {
        super(injector);
    }

    @java.lang.Override
    protected void executeDDLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
    }

    @java.lang.Override
    protected void executePreDMLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
    }

    @java.lang.Override
    protected void executeDMLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
    }
}