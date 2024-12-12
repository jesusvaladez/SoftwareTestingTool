package org.apache.ambari.server.upgrade;
public class UpgradeCatalog280 extends org.apache.ambari.server.upgrade.AbstractUpgradeCatalog {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.upgrade.UpgradeCatalog280.class);

    private static final java.lang.String REQUEST_SCHEDULE_TABLE_NAME = "requestschedule";

    protected static final java.lang.String HOST_COMPONENT_STATE_TABLE = "hostcomponentstate";

    private static final java.lang.String REQUEST_SCHEDULE_BATCH_TOLERATION_LIMIT_PER_BATCH_COLUMN_NAME = "batch_toleration_limit_per_batch";

    private static final java.lang.String REQUEST_SCHEDULE_PAUSE_AFTER_FIRST_BATCH_COLUMN_NAME = "pause_after_first_batch";

    protected static final java.lang.String LAST_LIVE_STATE_COLUMN = "last_live_state";

    private static final java.lang.String UPGRADE_TABLE = "upgrade";

    private static final java.lang.String UPGRADE_PACK_STACK_ID = "upgrade_pack_stack_id";

    protected static final java.lang.String AMBARI_CONFIGURATION_TABLE = "ambari_configuration";

    protected static final java.lang.String AMBARI_CONFIGURATION_PROPERTY_VALUE_COLUMN = "property_value";

    @com.google.inject.Inject
    public UpgradeCatalog280(com.google.inject.Injector injector) {
        super(injector);
    }

    @java.lang.Override
    public java.lang.String getSourceVersion() {
        return "2.7.2";
    }

    @java.lang.Override
    public java.lang.String getTargetVersion() {
        return "2.8.0";
    }

    @java.lang.Override
    protected void executeDDLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        removeLastValidState();
        addColumnsToRequestScheduleTable();
        addColumnsToUpgradeTable();
        modifyPropertyValueColumnInAmbariConfigurationTable();
    }

    private void modifyPropertyValueColumnInAmbariConfigurationTable() throws java.sql.SQLException {
        dbAccessor.alterColumn(org.apache.ambari.server.upgrade.UpgradeCatalog280.AMBARI_CONFIGURATION_TABLE, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog280.AMBARI_CONFIGURATION_PROPERTY_VALUE_COLUMN, java.lang.String.class, 4000, null, false));
        org.apache.ambari.server.upgrade.UpgradeCatalog280.LOG.info("Altered {}.{} to NOT NULL and extended its length to 4000", org.apache.ambari.server.upgrade.UpgradeCatalog280.AMBARI_CONFIGURATION_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog280.AMBARI_CONFIGURATION_PROPERTY_VALUE_COLUMN);
    }

    @java.lang.Override
    protected void executePreDMLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
    }

    @java.lang.Override
    protected void executeDMLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
    }

    protected void addColumnsToRequestScheduleTable() throws java.sql.SQLException {
        dbAccessor.addColumn(org.apache.ambari.server.upgrade.UpgradeCatalog280.REQUEST_SCHEDULE_TABLE_NAME, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog280.REQUEST_SCHEDULE_BATCH_TOLERATION_LIMIT_PER_BATCH_COLUMN_NAME, java.lang.Short.class, null, null, true));
        dbAccessor.addColumn(org.apache.ambari.server.upgrade.UpgradeCatalog280.REQUEST_SCHEDULE_TABLE_NAME, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog280.REQUEST_SCHEDULE_PAUSE_AFTER_FIRST_BATCH_COLUMN_NAME, java.lang.Boolean.class, null, null, true));
    }

    protected void addColumnsToUpgradeTable() throws java.sql.SQLException {
        dbAccessor.addColumn(org.apache.ambari.server.upgrade.UpgradeCatalog280.UPGRADE_TABLE, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog280.UPGRADE_PACK_STACK_ID, java.lang.String.class, 255, "", false));
    }

    protected void removeLastValidState() throws java.sql.SQLException {
        dbAccessor.dropColumn(org.apache.ambari.server.upgrade.UpgradeCatalog280.HOST_COMPONENT_STATE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog280.LAST_LIVE_STATE_COLUMN);
    }
}