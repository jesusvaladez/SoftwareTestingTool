package org.apache.ambari.server.upgrade;
import com.google.inject.persist.PersistService;
import org.easymock.EasyMock;
@org.junit.runner.RunWith(org.junit.runners.Parameterized.class)
public class UpgradeTest {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.upgrade.UpgradeTest.class);

    private static java.lang.String DDL_PATTERN = "ddl-scripts/Ambari-DDL-Derby-%s.sql";

    private static java.util.List<java.lang.String> VERSIONS = java.util.Arrays.asList("1.4.4", "1.4.3", "1.4.2", "1.4.1", "1.4.0", "1.2.5", "1.2.4", "1.2.3");

    private static java.lang.String DROP_DERBY_URL = "jdbc:derby:memory:myDB/ambari;drop=true";

    private final java.lang.String sourceVersion;

    private java.util.Properties properties = new java.util.Properties();

    private com.google.inject.Injector injector;

    public UpgradeTest(java.lang.String sourceVersion) {
        this.sourceVersion = sourceVersion;
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_PERSISTENCE_TYPE.getKey(), "remote");
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_URL.getKey(), org.apache.ambari.server.configuration.Configuration.JDBC_IN_MEMORY_URL);
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_JDBC_DRIVER.getKey(), org.apache.ambari.server.configuration.Configuration.JDBC_IN_MEMORY_DRIVER);
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.METADATA_DIR_PATH.getKey(), "src/test/resources/stacks");
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.SERVER_VERSION_FILE.getKey(), "src/test/resources/version");
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.OS_VERSION.getKey(), "centos5");
        properties.setProperty(org.apache.ambari.server.configuration.Configuration.SHARED_RESOURCES_DIR.getKey(), "src/test/resources/");
    }

    @org.junit.Test
    @org.junit.Ignore
    public void testUpgrade() throws java.lang.Exception {
        try {
            dropDatabase();
        } catch (java.sql.SQLException ignored) {
        }
        java.lang.String targetVersion = getLastVersion();
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.controller.ControllerModule(properties));
        org.apache.ambari.server.upgrade.UpgradeTest.LOG.info("Testing upgrade from version {} to {}", sourceVersion, targetVersion);
        createSourceDatabase(sourceVersion);
        performUpgrade(targetVersion);
        testUpgradedSchema();
        dropDatabase();
    }

    private void dropDatabase() throws java.lang.ClassNotFoundException, java.sql.SQLException {
        java.lang.Class.forName(org.apache.ambari.server.configuration.Configuration.JDBC_IN_MEMORY_DRIVER);
        try {
            java.sql.DriverManager.getConnection(org.apache.ambari.server.upgrade.UpgradeTest.DROP_DERBY_URL);
        } catch (java.sql.SQLNonTransientConnectionException ignored) {
            org.apache.ambari.server.upgrade.UpgradeTest.LOG.info("Database dropped ", ignored);
        }
    }

    private void testUpgradedSchema() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.controller.ControllerModule(properties));
        injector.getInstance(com.google.inject.persist.PersistService.class).start();
        org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        clusterDAO.findAll();
        org.apache.ambari.server.orm.dao.BlueprintDAO blueprintDAO = injector.getInstance(org.apache.ambari.server.orm.dao.BlueprintDAO.class);
        blueprintDAO.findAll();
        org.apache.ambari.server.orm.dao.ClusterServiceDAO clusterServiceDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ClusterServiceDAO.class);
        clusterServiceDAO.findAll();
        injector.getInstance(org.apache.ambari.server.orm.dao.ClusterStateDAO.class).findAll();
        injector.getInstance(org.apache.ambari.server.orm.dao.ConfigGroupConfigMappingDAO.class).findAll();
        injector.getInstance(org.apache.ambari.server.orm.dao.ConfigGroupDAO.class).findAll();
        injector.getInstance(org.apache.ambari.server.orm.dao.ConfigGroupHostMappingDAO.class).findAll();
        injector.getInstance(org.apache.ambari.server.orm.dao.ExecutionCommandDAO.class).findAll();
        injector.getInstance(org.apache.ambari.server.orm.dao.HostComponentDesiredStateDAO.class).findAll();
        injector.getInstance(org.apache.ambari.server.orm.dao.HostComponentStateDAO.class).findAll();
        injector.getInstance(org.apache.ambari.server.orm.dao.HostConfigMappingDAO.class).findAll();
        injector.getInstance(org.apache.ambari.server.orm.dao.HostDAO.class).findAll();
        injector.getInstance(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class).findAll();
        injector.getInstance(org.apache.ambari.server.orm.dao.HostStateDAO.class).findAll();
        injector.getInstance(org.apache.ambari.server.orm.dao.KeyValueDAO.class).findAll();
        injector.getInstance(org.apache.ambari.server.orm.dao.MetainfoDAO.class).findAll();
        org.apache.ambari.server.orm.dao.RequestDAO requestDAO = injector.getInstance(org.apache.ambari.server.orm.dao.RequestDAO.class);
        requestDAO.findAll();
        requestDAO.findAllResourceFilters();
        injector.getInstance(org.apache.ambari.server.orm.dao.RequestScheduleBatchRequestDAO.class).findAll();
        injector.getInstance(org.apache.ambari.server.orm.dao.RequestScheduleDAO.class).findAll();
        injector.getInstance(org.apache.ambari.server.orm.dao.RoleSuccessCriteriaDAO.class).findAll();
        injector.getInstance(org.apache.ambari.server.orm.dao.ServiceComponentDesiredStateDAO.class).findAll();
        injector.getInstance(org.apache.ambari.server.orm.dao.ServiceDesiredStateDAO.class).findAll();
        injector.getInstance(org.apache.ambari.server.orm.dao.StageDAO.class).findAll();
        injector.getInstance(org.apache.ambari.server.orm.dao.UserDAO.class).findAll();
        injector.getInstance(org.apache.ambari.server.orm.dao.ViewDAO.class).findAll();
        injector.getInstance(org.apache.ambari.server.orm.dao.ViewInstanceDAO.class).findAll();
        injector.getInstance(com.google.inject.persist.PersistService.class).stop();
    }

    private void performUpgrade(java.lang.String targetVersion) throws java.lang.Exception {
        com.google.inject.Injector injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.upgrade.SchemaUpgradeHelper.UpgradeHelperModule(properties) {
            @java.lang.Override
            protected void configure() {
                super.configure();
                org.apache.ambari.server.view.ViewRegistry viewRegistryMock = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.view.ViewRegistry.class);
                bind(org.apache.ambari.server.view.ViewRegistry.class).toInstance(viewRegistryMock);
            }
        });
        org.apache.ambari.server.upgrade.SchemaUpgradeHelper schemaUpgradeHelper = injector.getInstance(org.apache.ambari.server.upgrade.SchemaUpgradeHelper.class);
        org.apache.ambari.server.upgrade.UpgradeTest.LOG.info("Upgrading schema to target version = " + targetVersion);
        org.apache.ambari.server.upgrade.UpgradeCatalog targetUpgradeCatalog = org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.getUpgradeCatalog(targetVersion);
        org.apache.ambari.server.upgrade.UpgradeTest.LOG.debug("Target upgrade catalog. {}", targetUpgradeCatalog);
        java.lang.String sourceVersion = schemaUpgradeHelper.readSourceVersion();
        org.apache.ambari.server.upgrade.UpgradeTest.LOG.info("Upgrading schema from source version = " + sourceVersion);
        java.util.List<org.apache.ambari.server.upgrade.UpgradeCatalog> upgradeCatalogs = schemaUpgradeHelper.getUpgradePath(sourceVersion, targetVersion);
        org.junit.Assert.assertTrue("Final Upgrade Catalog should be run last", (!upgradeCatalogs.isEmpty()) && upgradeCatalogs.get(upgradeCatalogs.size() - 1).isFinal());
        try {
            schemaUpgradeHelper.executeUpgrade(upgradeCatalogs);
        } catch (java.lang.Exception e) {
            if (e.getMessage().contains("Column 'T.HOST_NAME' is either not in any table in the FROM list") || e.getMessage().contains("Column 'T.HOSTNAME' is either not in any table in the FROM list")) {
                java.lang.System.out.println("Ignoring on purpose, " + e.getMessage());
            } else {
                throw e;
            }
        }
        schemaUpgradeHelper.executePreDMLUpdates(upgradeCatalogs);
        schemaUpgradeHelper.executeDMLUpdates(upgradeCatalogs, "test");
        schemaUpgradeHelper.executeOnPostUpgrade(upgradeCatalogs);
        org.apache.ambari.server.upgrade.UpgradeTest.LOG.info("Upgrade successful.");
    }

    private java.lang.String getLastVersion() throws java.lang.Exception {
        com.google.inject.Injector injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.upgrade.SchemaUpgradeHelper.UpgradeHelperModule(properties));
        java.util.Set<org.apache.ambari.server.upgrade.UpgradeCatalog> upgradeCatalogs = injector.getInstance(com.google.inject.Key.get(new com.google.inject.TypeLiteral<java.util.Set<org.apache.ambari.server.upgrade.UpgradeCatalog>>() {}));
        java.lang.String maxVersion = "1.2";
        for (org.apache.ambari.server.upgrade.UpgradeCatalog upgradeCatalog : upgradeCatalogs) {
            java.lang.String targetVersion = upgradeCatalog.getTargetVersion();
            if (org.apache.ambari.server.utils.VersionUtils.compareVersions(maxVersion, targetVersion) < 0) {
                maxVersion = targetVersion;
            }
        }
        return maxVersion;
    }

    private void createSourceDatabase(java.lang.String version) throws java.io.IOException, java.sql.SQLException {
        java.lang.String fileName = java.lang.String.format(org.apache.ambari.server.upgrade.UpgradeTest.DDL_PATTERN, version);
        fileName = this.getClass().getClassLoader().getResource(fileName).getFile();
        org.apache.ambari.server.orm.DBAccessor dbAccessor = injector.getInstance(org.apache.ambari.server.orm.DBAccessor.class);
        dbAccessor.executeScript(fileName);
    }

    @org.junit.runners.Parameterized.Parameters
    public static java.util.Collection<java.lang.Object[]> data() {
        java.util.Collection<java.lang.Object[]> data = new java.util.ArrayList<>();
        for (java.lang.String s : org.apache.ambari.server.upgrade.UpgradeTest.VERSIONS) {
            data.add(new java.lang.Object[]{ s });
        }
        return data;
    }
}