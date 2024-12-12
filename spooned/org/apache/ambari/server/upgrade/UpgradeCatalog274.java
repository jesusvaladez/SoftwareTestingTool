package org.apache.ambari.server.upgrade;
import javax.persistence.Table;
public class UpgradeCatalog274 extends org.apache.ambari.server.upgrade.AbstractUpgradeCatalog {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.upgrade.UpgradeCatalog274.class);

    static final java.lang.String AMBARI_CONFIGURATION_TABLE = org.apache.ambari.server.orm.entities.AmbariConfigurationEntity.class.getAnnotation(javax.persistence.Table.class).name();

    static final java.lang.String AMBARI_CONFIGURATION_PROPERTY_VALUE_COLUMN = org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_CONFIGURATION_PROPERTY_VALUE_COLUMN;

    static final java.lang.Integer AMBARI_CONFIGURATION_PROPERTY_VALUE_COLUMN_LEN = 4000;

    @com.google.inject.Inject
    public UpgradeCatalog274(com.google.inject.Injector injector) {
        super(injector);
    }

    @java.lang.Override
    public java.lang.String getSourceVersion() {
        return "2.7.2";
    }

    @java.lang.Override
    protected void executeDDLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        upgradeConfigurationTableValueMaxSize();
    }

    @java.lang.Override
    public java.lang.String getTargetVersion() {
        return "2.7.4";
    }

    @java.lang.Override
    protected void executePreDMLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
    }

    @java.lang.Override
    protected void executeDMLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
    }

    private void upgradeConfigurationTableValueMaxSize() throws java.sql.SQLException {
        org.apache.ambari.server.orm.DBAccessor.DBColumnInfo propertyColumn = dbAccessor.getColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog274.AMBARI_CONFIGURATION_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog274.AMBARI_CONFIGURATION_PROPERTY_VALUE_COLUMN);
        if (((propertyColumn != null) && (propertyColumn.getType() != null)) && (propertyColumn.getLength() < org.apache.ambari.server.upgrade.UpgradeCatalog274.AMBARI_CONFIGURATION_PROPERTY_VALUE_COLUMN_LEN)) {
            org.apache.ambari.server.upgrade.UpgradeCatalog274.LOG.info("Updating column max size to {} for {}.{}", org.apache.ambari.server.upgrade.UpgradeCatalog274.AMBARI_CONFIGURATION_PROPERTY_VALUE_COLUMN_LEN, org.apache.ambari.server.upgrade.UpgradeCatalog274.AMBARI_CONFIGURATION_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog274.AMBARI_CONFIGURATION_PROPERTY_VALUE_COLUMN);
            propertyColumn.setLength(org.apache.ambari.server.upgrade.UpgradeCatalog274.AMBARI_CONFIGURATION_PROPERTY_VALUE_COLUMN_LEN);
            dbAccessor.alterColumn(org.apache.ambari.server.upgrade.UpgradeCatalog274.AMBARI_CONFIGURATION_TABLE, propertyColumn);
        }
    }
}