package org.apache.ambari.server.upgrade;
class UpgradeHelperTestModule extends org.apache.ambari.server.orm.InMemoryDefaultTestModule {
    UpgradeHelperTestModule() {
    }

    @java.lang.Override
    protected void configure() {
        super.configure();
        com.google.inject.multibindings.Multibinder<org.apache.ambari.server.upgrade.UpgradeCatalog> catalogBinder = com.google.inject.multibindings.Multibinder.newSetBinder(binder(), org.apache.ambari.server.upgrade.UpgradeCatalog.class);
        catalogBinder.addBinding().to(org.apache.ambari.server.upgrade.TestUpgradeCatalog10.class);
        catalogBinder.addBinding().to(org.apache.ambari.server.upgrade.TestUpgradeCatalog20.class);
        catalogBinder.addBinding().to(org.apache.ambari.server.upgrade.TestUpgradeCatalog30.class);
        org.apache.ambari.server.utils.EventBusSynchronizer.synchronizeAmbariEventPublisher(binder());
    }
}