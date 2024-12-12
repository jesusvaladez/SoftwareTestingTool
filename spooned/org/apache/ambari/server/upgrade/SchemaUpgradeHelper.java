package org.apache.ambari.server.upgrade;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.persist.PersistService;
import org.springframework.jdbc.support.JdbcUtils;
public class SchemaUpgradeHelper {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.upgrade.SchemaUpgradeHelper.class);

    private java.util.Set<org.apache.ambari.server.upgrade.UpgradeCatalog> allUpgradeCatalogs;

    private com.google.inject.persist.PersistService persistService;

    private org.apache.ambari.server.orm.DBAccessor dbAccessor;

    private org.apache.ambari.server.configuration.Configuration configuration;

    private static final java.lang.String[] rcaTableNames = new java.lang.String[]{ "workflow", "job", "task", "taskAttempt", "hdfsEvent", "mapreduceEvent", "clusterEvent" };

    static final com.google.gson.Gson gson = new com.google.gson.GsonBuilder().create();

    @com.google.inject.Inject
    public SchemaUpgradeHelper(java.util.Set<org.apache.ambari.server.upgrade.UpgradeCatalog> allUpgradeCatalogs, com.google.inject.persist.PersistService persistService, org.apache.ambari.server.orm.DBAccessor dbAccessor, org.apache.ambari.server.configuration.Configuration configuration) {
        this.allUpgradeCatalogs = allUpgradeCatalogs;
        this.persistService = persistService;
        this.dbAccessor = dbAccessor;
        this.configuration = configuration;
    }

    public void startPersistenceService() {
        persistService.start();
    }

    public void stopPersistenceService() {
        persistService.stop();
    }

    public java.util.Set<org.apache.ambari.server.upgrade.UpgradeCatalog> getAllUpgradeCatalogs() {
        return allUpgradeCatalogs;
    }

    public java.lang.String readSourceVersion() {
        java.sql.Statement statement = null;
        java.sql.ResultSet rs = null;
        try {
            statement = dbAccessor.getConnection().createStatement();
            if (statement != null) {
                rs = statement.executeQuery(((("SELECT " + dbAccessor.quoteObjectName("metainfo_value")) + " from metainfo WHERE ") + dbAccessor.quoteObjectName("metainfo_key")) + "='version'");
                if ((rs != null) && rs.next()) {
                    return rs.getString(1);
                }
            }
        } catch (java.sql.SQLException e) {
            throw new java.lang.RuntimeException("Unable to read database version", e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (java.sql.SQLException e) {
                    throw new java.lang.RuntimeException("Cannot close result set");
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (java.sql.SQLException e) {
                    throw new java.lang.RuntimeException("Cannot close statement");
                }
            }
        }
        return "1.2.0";
    }

    protected java.lang.String getAmbariServerVersion() {
        return configuration.getServerVersion();
    }

    protected java.util.List<org.apache.ambari.server.upgrade.UpgradeCatalog> getUpgradePath(java.lang.String sourceVersion, java.lang.String targetVersion) throws org.apache.ambari.server.AmbariException {
        java.util.List<org.apache.ambari.server.upgrade.UpgradeCatalog> upgradeCatalogs = new java.util.ArrayList<>();
        java.util.List<org.apache.ambari.server.upgrade.UpgradeCatalog> candidateCatalogs = new java.util.ArrayList<>(allUpgradeCatalogs);
        java.util.Collections.sort(candidateCatalogs, new org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.VersionComparator());
        for (org.apache.ambari.server.upgrade.UpgradeCatalog upgradeCatalog : candidateCatalogs) {
            if ((sourceVersion == null) || (org.apache.ambari.server.utils.VersionUtils.compareVersions(sourceVersion, upgradeCatalog.getTargetVersion(), 4) < 0)) {
                if (org.apache.ambari.server.utils.VersionUtils.compareVersions(upgradeCatalog.getTargetVersion(), targetVersion, 4) <= 0) {
                    upgradeCatalogs.add(upgradeCatalog);
                }
            }
        }
        org.apache.ambari.server.upgrade.SchemaUpgradeHelper.LOG.info("Upgrade path: " + upgradeCatalogs);
        return upgradeCatalogs;
    }

    public static class UpgradeHelperModule extends org.apache.ambari.server.controller.ControllerModule {
        public UpgradeHelperModule() throws java.lang.Exception {
        }

        public UpgradeHelperModule(java.util.Properties properties) throws java.lang.Exception {
            super(properties);
        }

        @java.lang.Override
        protected void configure() {
            super.configure();
            com.google.inject.multibindings.Multibinder<org.apache.ambari.server.upgrade.UpgradeCatalog> catalogBinder = com.google.inject.multibindings.Multibinder.newSetBinder(binder(), org.apache.ambari.server.upgrade.UpgradeCatalog.class);
            catalogBinder.addBinding().to(org.apache.ambari.server.upgrade.UpgradeCatalog251.class);
            catalogBinder.addBinding().to(org.apache.ambari.server.upgrade.UpgradeCatalog252.class);
            catalogBinder.addBinding().to(org.apache.ambari.server.upgrade.UpgradeCatalog260.class);
            catalogBinder.addBinding().to(org.apache.ambari.server.upgrade.UpgradeCatalog261.class);
            catalogBinder.addBinding().to(org.apache.ambari.server.upgrade.UpgradeCatalog262.class);
            catalogBinder.addBinding().to(org.apache.ambari.server.upgrade.UpgradeCatalog270.class);
            catalogBinder.addBinding().to(org.apache.ambari.server.upgrade.UpgradeCatalog271.class);
            catalogBinder.addBinding().to(org.apache.ambari.server.upgrade.UpgradeCatalog272.class);
            catalogBinder.addBinding().to(org.apache.ambari.server.upgrade.UpgradeCatalog274.class);
            catalogBinder.addBinding().to(org.apache.ambari.server.upgrade.UpgradeCatalog275.class);
            catalogBinder.addBinding().to(org.apache.ambari.server.upgrade.UpgradeCatalog276.class);
            catalogBinder.addBinding().to(org.apache.ambari.server.upgrade.UpgradeCatalog280.class);
            catalogBinder.addBinding().to(org.apache.ambari.server.upgrade.UpdateAlertScriptPaths.class);
            catalogBinder.addBinding().to(org.apache.ambari.server.upgrade.FinalUpgradeCatalog.class);
            org.apache.ambari.server.utils.EventBusSynchronizer.synchronizeAmbariEventPublisher(binder());
        }
    }

    public void executeUpgrade(java.util.List<org.apache.ambari.server.upgrade.UpgradeCatalog> upgradeCatalogs) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.upgrade.SchemaUpgradeHelper.LOG.info("Executing DDL upgrade...");
        if ((upgradeCatalogs != null) && (!upgradeCatalogs.isEmpty())) {
            for (org.apache.ambari.server.upgrade.UpgradeCatalog upgradeCatalog : upgradeCatalogs) {
                try {
                    upgradeCatalog.upgradeSchema();
                } catch (java.lang.Exception e) {
                    org.apache.ambari.server.upgrade.SchemaUpgradeHelper.LOG.error("Upgrade failed. ", e);
                    throw new org.apache.ambari.server.AmbariException(e.getMessage(), e);
                }
            }
        }
    }

    public void executePreDMLUpdates(java.util.List<org.apache.ambari.server.upgrade.UpgradeCatalog> upgradeCatalogs) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.upgrade.SchemaUpgradeHelper.LOG.info("Executing Pre-DML changes.");
        if ((upgradeCatalogs != null) && (!upgradeCatalogs.isEmpty())) {
            for (org.apache.ambari.server.upgrade.UpgradeCatalog upgradeCatalog : upgradeCatalogs) {
                try {
                    upgradeCatalog.preUpgradeData();
                } catch (java.lang.Exception e) {
                    org.apache.ambari.server.upgrade.SchemaUpgradeHelper.LOG.error("Upgrade failed. ", e);
                    throw new org.apache.ambari.server.AmbariException(e.getMessage(), e);
                }
            }
        }
    }

    public void executeDMLUpdates(java.util.List<org.apache.ambari.server.upgrade.UpgradeCatalog> upgradeCatalogs, java.lang.String ambariUpgradeConfigUpdatesFileName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.upgrade.SchemaUpgradeHelper.LOG.info("Executing DML changes.");
        if ((upgradeCatalogs != null) && (!upgradeCatalogs.isEmpty())) {
            for (org.apache.ambari.server.upgrade.UpgradeCatalog upgradeCatalog : upgradeCatalogs) {
                try {
                    upgradeCatalog.setConfigUpdatesFileName(ambariUpgradeConfigUpdatesFileName);
                    upgradeCatalog.upgradeData();
                } catch (java.lang.Exception e) {
                    org.apache.ambari.server.upgrade.SchemaUpgradeHelper.LOG.error("Upgrade failed. ", e);
                    throw new org.apache.ambari.server.AmbariException(e.getMessage(), e);
                }
            }
        }
    }

    public void executeOnPostUpgrade(java.util.List<org.apache.ambari.server.upgrade.UpgradeCatalog> upgradeCatalogs) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.upgrade.SchemaUpgradeHelper.LOG.info("Finalizing catalog upgrade.");
        if ((upgradeCatalogs != null) && (!upgradeCatalogs.isEmpty())) {
            for (org.apache.ambari.server.upgrade.UpgradeCatalog upgradeCatalog : upgradeCatalogs) {
                try {
                    upgradeCatalog.onPostUpgrade();
                    upgradeCatalog.updateDatabaseSchemaVersion();
                } catch (java.lang.Exception e) {
                    org.apache.ambari.server.upgrade.SchemaUpgradeHelper.LOG.error("Upgrade failed. ", e);
                    throw new org.apache.ambari.server.AmbariException(e.getMessage(), e);
                }
            }
        }
    }

    public void outputUpgradeJsonOutput(java.util.List<org.apache.ambari.server.upgrade.UpgradeCatalog> upgradeCatalogs) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.upgrade.SchemaUpgradeHelper.LOG.info("Combining upgrade json output.");
        java.util.Map<java.lang.String, java.lang.String> combinedUpgradeJsonOutput = new java.util.HashMap<>();
        if ((upgradeCatalogs != null) && (!upgradeCatalogs.isEmpty())) {
            for (org.apache.ambari.server.upgrade.UpgradeCatalog upgradeCatalog : upgradeCatalogs) {
                try {
                    combinedUpgradeJsonOutput.putAll(upgradeCatalog.getUpgradeJsonOutput());
                } catch (java.lang.Exception e) {
                    org.apache.ambari.server.upgrade.SchemaUpgradeHelper.LOG.error("Upgrade failed. ", e);
                    throw new org.apache.ambari.server.AmbariException(e.getMessage(), e);
                }
            }
        }
        java.lang.String content = org.apache.ambari.server.upgrade.SchemaUpgradeHelper.gson.toJson(combinedUpgradeJsonOutput);
        java.lang.System.out.println(content);
    }

    public void resetUIState() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.upgrade.SchemaUpgradeHelper.LOG.info("Resetting UI state.");
        try {
            dbAccessor.updateTable("key_value_store", dbAccessor.quoteObjectName("value"), "{\"clusterState\":\"CLUSTER_STARTED_5\"}", ("where " + dbAccessor.quoteObjectName("key")) + "='CLUSTER_CURRENT_STATUS'");
        } catch (java.sql.SQLException e) {
            throw new org.apache.ambari.server.AmbariException("Unable to reset UI state", e);
        }
    }

    public void cleanUpRCATables() {
        org.apache.ambari.server.upgrade.SchemaUpgradeHelper.LOG.info("Cleaning up RCA tables.");
        for (java.lang.String tableName : org.apache.ambari.server.upgrade.SchemaUpgradeHelper.rcaTableNames) {
            try {
                if (dbAccessor.tableExists(tableName)) {
                    dbAccessor.truncateTable(tableName);
                }
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.upgrade.SchemaUpgradeHelper.LOG.warn("Error cleaning rca table " + tableName, e);
            }
        }
        try {
            cleanUpTablesFromRCADatabase();
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.upgrade.SchemaUpgradeHelper.LOG.warn("Error cleaning rca tables from ambarirca db", e);
        }
    }

    private void cleanUpTablesFromRCADatabase() throws java.lang.ClassNotFoundException, java.sql.SQLException {
        java.lang.String driverName = configuration.getRcaDatabaseDriver();
        java.lang.String connectionURL = configuration.getRcaDatabaseUrl();
        if (connectionURL.contains(org.apache.ambari.server.configuration.Configuration.HOSTNAME_MACRO)) {
            connectionURL = connectionURL.replace(org.apache.ambari.server.configuration.Configuration.HOSTNAME_MACRO, "localhost");
        }
        java.lang.String username = configuration.getRcaDatabaseUser();
        java.lang.String password = configuration.getRcaDatabasePassword();
        java.lang.Class.forName(driverName);
        try (java.sql.Connection connection = java.sql.DriverManager.getConnection(connectionURL, username, password)) {
            connection.setAutoCommit(true);
            for (java.lang.String tableName : org.apache.ambari.server.upgrade.SchemaUpgradeHelper.rcaTableNames) {
                java.lang.String query = "DELETE FROM " + tableName;
                try (java.sql.Statement statement = connection.createStatement()) {
                    statement.execute(query);
                } catch (java.lang.Exception e) {
                    org.apache.ambari.server.upgrade.SchemaUpgradeHelper.LOG.warn("Error while executing query: " + query, e);
                }
            }
        }
    }

    private java.lang.String getMinimalUpgradeCatalogVersion() {
        java.util.List<org.apache.ambari.server.upgrade.UpgradeCatalog> candidateCatalogs = new java.util.ArrayList<>(allUpgradeCatalogs);
        java.util.Collections.sort(candidateCatalogs, new org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.VersionComparator());
        if (candidateCatalogs.isEmpty()) {
            return null;
        }
        return candidateCatalogs.iterator().next().getTargetVersion();
    }

    private boolean verifyUpgradePath(java.lang.String minUpgradeVersion, java.lang.String sourceVersion) {
        if (null == minUpgradeVersion) {
            return false;
        }
        return org.apache.ambari.server.utils.VersionUtils.compareVersions(sourceVersion, minUpgradeVersion) >= 0;
    }

    private java.util.List<java.lang.String> getMyISAMTables() throws java.sql.SQLException {
        if (!configuration.getDatabaseType().equals(org.apache.ambari.server.configuration.Configuration.DatabaseType.MYSQL)) {
            return java.util.Collections.emptyList();
        }
        java.util.List<java.lang.String> myISAMTables = new java.util.ArrayList<>();
        java.lang.String query = java.lang.String.format("SELECT table_name FROM information_schema.tables WHERE table_schema = '%s' " + "AND engine = 'MyISAM' AND table_type = 'BASE TABLE'", configuration.getServerDBName());
        java.sql.Statement statement = null;
        java.sql.ResultSet rs = null;
        try {
            statement = dbAccessor.getConnection().createStatement();
            rs = statement.executeQuery(query);
            if (rs != null) {
                while (rs.next()) {
                    myISAMTables.add(rs.getString("table_name"));
                } 
            }
        } finally {
            org.springframework.jdbc.support.JdbcUtils.closeResultSet(rs);
            org.springframework.jdbc.support.JdbcUtils.closeStatement(statement);
        }
        return myISAMTables;
    }

    public static void main(java.lang.String[] args) throws java.lang.Exception {
        try {
            java.lang.String[] splittedJavaVersion = java.lang.System.getProperty("java.version").split("\\.");
            float javaVersion = java.lang.Float.parseFloat((splittedJavaVersion[0] + ".") + splittedJavaVersion[1]);
            if (javaVersion < org.apache.ambari.server.configuration.Configuration.JDK_MIN_VERSION) {
                org.apache.ambari.server.upgrade.SchemaUpgradeHelper.LOG.error(java.lang.String.format("Oracle JDK version is lower than %.1f It can cause problems during upgrade process. Please," + " use 'ambari-server setup' command to upgrade JDK!", org.apache.ambari.server.configuration.Configuration.JDK_MIN_VERSION));
                java.lang.System.exit(1);
            }
            com.google.inject.Injector injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.upgrade.SchemaUpgradeHelper.UpgradeHelperModule(), new org.apache.ambari.server.audit.AuditLoggerModule(), new org.apache.ambari.server.ldap.LdapModule());
            org.apache.ambari.server.orm.GuiceJpaInitializer jpaInitializer = injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
            org.apache.ambari.server.upgrade.SchemaUpgradeHelper schemaUpgradeHelper = injector.getInstance(org.apache.ambari.server.upgrade.SchemaUpgradeHelper.class);
            java.util.List<java.lang.String> myISAMTables = schemaUpgradeHelper.getMyISAMTables();
            if (!myISAMTables.isEmpty()) {
                java.lang.String errorMessage = java.lang.String.format("Unsupported MyISAM table %s detected. " + "For correct upgrade database should be migrated to InnoDB engine.", myISAMTables.get(0));
                org.apache.ambari.server.upgrade.SchemaUpgradeHelper.LOG.error(errorMessage);
                throw new org.apache.ambari.server.AmbariException(errorMessage);
            }
            java.lang.String targetVersion = schemaUpgradeHelper.getAmbariServerVersion();
            org.apache.ambari.server.upgrade.SchemaUpgradeHelper.LOG.info("Upgrading schema to target version = " + targetVersion);
            org.apache.ambari.server.upgrade.UpgradeCatalog targetUpgradeCatalog = org.apache.ambari.server.upgrade.AbstractUpgradeCatalog.getUpgradeCatalog(targetVersion);
            org.apache.ambari.server.upgrade.SchemaUpgradeHelper.LOG.debug("Target upgrade catalog. {}", targetUpgradeCatalog);
            java.lang.String sourceVersion = schemaUpgradeHelper.readSourceVersion();
            org.apache.ambari.server.upgrade.SchemaUpgradeHelper.LOG.info("Upgrading schema from source version = " + sourceVersion);
            java.lang.String minimalRequiredUpgradeVersion = schemaUpgradeHelper.getMinimalUpgradeCatalogVersion();
            if (!schemaUpgradeHelper.verifyUpgradePath(minimalRequiredUpgradeVersion, sourceVersion)) {
                throw new org.apache.ambari.server.AmbariException(java.lang.String.format("Database version does not meet minimal upgrade requirements. Expected version should be not less than %s, current version is %s", minimalRequiredUpgradeVersion, sourceVersion));
            }
            java.util.List<org.apache.ambari.server.upgrade.UpgradeCatalog> upgradeCatalogs = schemaUpgradeHelper.getUpgradePath(sourceVersion, targetVersion);
            java.lang.String date = new java.text.SimpleDateFormat("MM-dd-yyyy_HH:mm:ss").format(new java.util.Date());
            java.lang.String ambariUpgradeConfigUpdatesFileName = ("ambari_upgrade_config_changes_" + date) + ".json";
            schemaUpgradeHelper.executeUpgrade(upgradeCatalogs);
            jpaInitializer.setInitialized();
            schemaUpgradeHelper.executePreDMLUpdates(upgradeCatalogs);
            schemaUpgradeHelper.executeDMLUpdates(upgradeCatalogs, ambariUpgradeConfigUpdatesFileName);
            schemaUpgradeHelper.executeOnPostUpgrade(upgradeCatalogs);
            schemaUpgradeHelper.outputUpgradeJsonOutput(upgradeCatalogs);
            schemaUpgradeHelper.resetUIState();
            org.apache.ambari.server.upgrade.SchemaUpgradeHelper.LOG.info("Upgrade successful.");
            schemaUpgradeHelper.cleanUpRCATables();
            schemaUpgradeHelper.stopPersistenceService();
            java.lang.System.exit(0);
        } catch (java.lang.Throwable e) {
            if (e instanceof org.apache.ambari.server.AmbariException) {
                org.apache.ambari.server.upgrade.SchemaUpgradeHelper.LOG.error("Exception occurred during upgrade, failed", e);
                throw ((org.apache.ambari.server.AmbariException) (e));
            } else {
                org.apache.ambari.server.upgrade.SchemaUpgradeHelper.LOG.error("Unexpected error, upgrade failed", e);
                throw new java.lang.Exception("Unexpected error, upgrade failed", e);
            }
        }
    }
}