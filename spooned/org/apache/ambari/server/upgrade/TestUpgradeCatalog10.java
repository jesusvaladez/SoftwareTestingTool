package org.apache.ambari.server.upgrade;
class TestUpgradeCatalog10 extends org.apache.ambari.server.upgrade.TestUpgradeCatalog {
    @com.google.inject.Inject
    public TestUpgradeCatalog10(com.google.inject.Injector injector) {
        super(injector);
    }

    @java.lang.Override
    public java.lang.String getTargetVersion() {
        return "0.1.0";
    }
}