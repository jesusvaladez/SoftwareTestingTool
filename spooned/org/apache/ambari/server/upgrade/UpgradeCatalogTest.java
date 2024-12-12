package org.apache.ambari.server.upgrade;
import com.google.inject.multibindings.Multibinder;
import org.springframework.security.core.context.SecurityContextHolder;
public class UpgradeCatalogTest {
    private com.google.inject.Injector injector;

    private org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo;

    private final java.lang.String DESIRED_STACK_VERSION = "{\"stackName\":\"HDP\"," + "\"stackVersion\":\"1.2.0\"}";

    private static class UpgradeCatalog201 extends org.apache.ambari.server.upgrade.AbstractUpgradeCatalog {
        @com.google.inject.Inject
        public UpgradeCatalog201(com.google.inject.Injector injector) {
            super(injector);
        }

        @java.lang.Override
        public void executeDDLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        }

        @java.lang.Override
        public void executePreDMLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        }

        @java.lang.Override
        public void executeDMLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        }

        @java.lang.Override
        public java.lang.String getTargetVersion() {
            return "2.0.1";
        }
    }

    private static class UpgradeHelperModuleTest extends org.apache.ambari.server.orm.InMemoryDefaultTestModule {
        @java.lang.Override
        protected void configure() {
            super.configure();
            com.google.inject.multibindings.Multibinder<org.apache.ambari.server.upgrade.UpgradeCatalog> catalogBinder = com.google.inject.multibindings.Multibinder.newSetBinder(binder(), org.apache.ambari.server.upgrade.UpgradeCatalog.class);
            catalogBinder.addBinding().to(org.apache.ambari.server.upgrade.UpgradeCatalogTest.UpgradeCatalog201.class);
            catalogBinder.addBinding().to(org.apache.ambari.server.upgrade.UpgradeCatalog251.class);
            catalogBinder.addBinding().to(org.apache.ambari.server.upgrade.UpgradeCatalog252.class);
            catalogBinder.addBinding().to(org.apache.ambari.server.upgrade.UpgradeCatalog270.class);
        }
    }

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.upgrade.UpgradeCatalogTest.UpgradeHelperModuleTest());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        metaInfo = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator("admin"));
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
    }

    @org.junit.Test
    public void testUpgradePath() throws java.lang.Exception {
        org.apache.ambari.server.upgrade.SchemaUpgradeHelper schemaUpgradeHelper = injector.getInstance(org.apache.ambari.server.upgrade.SchemaUpgradeHelper.class);
        java.util.Set<org.apache.ambari.server.upgrade.UpgradeCatalog> upgradeCatalogSet = schemaUpgradeHelper.getAllUpgradeCatalogs();
        junit.framework.Assert.assertNotNull(upgradeCatalogSet);
        junit.framework.Assert.assertEquals(4, upgradeCatalogSet.size());
        java.util.List<org.apache.ambari.server.upgrade.UpgradeCatalog> upgradeCatalogs = schemaUpgradeHelper.getUpgradePath(null, "2.5.1");
        junit.framework.Assert.assertNotNull(upgradeCatalogs);
        junit.framework.Assert.assertEquals(2, upgradeCatalogs.size());
        junit.framework.Assert.assertEquals("2.0.1", upgradeCatalogs.get(0).getTargetVersion());
        junit.framework.Assert.assertEquals("2.5.1", upgradeCatalogs.get(1).getTargetVersion());
    }
}