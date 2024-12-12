package org.apache.ambari.server.upgrade;
class TestUpgradeCatalog30 extends org.apache.ambari.server.upgrade.TestUpgradeCatalog {
    @com.google.inject.Inject
    public TestUpgradeCatalog30(com.google.inject.Injector injector) {
        super(injector);
    }

    @java.lang.Override
    public java.lang.String getTargetVersion() {
        return "0.3.0";
    }
}