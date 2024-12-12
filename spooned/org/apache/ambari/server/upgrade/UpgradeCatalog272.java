package org.apache.ambari.server.upgrade;
import static org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.LDAP_CONFIGURATION;
import static org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_VIEW_STATUS_INFO;
import static org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MANAGE_WIDGETS;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_CONFIGURATION_CATEGORY_NAME_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_CONFIGURATION_PROPERTY_NAME_COLUMN;
import static org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_CONFIGURATION_TABLE;
public class UpgradeCatalog272 extends org.apache.ambari.server.upgrade.AbstractUpgradeCatalog {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.upgrade.UpgradeCatalog272.class);

    private static final java.lang.String LDAP_CONFIGURATION_WRONG_COLLISION_BEHAVIOR_PROPERTY_NAME = "ambari.ldap.advance.collision_behavior";

    private static final java.lang.String LDAP_CONFIGURATION_CORRECT_COLLISION_BEHAVIOR_PROPERTY_NAME = "ambari.ldap.advanced.collision_behavior";

    static final java.lang.String RENAME_COLLISION_BEHAVIOR_PROPERTY_SQL = java.lang.String.format("UPDATE %s SET %s = '%s' WHERE %s = '%s' AND %s = '%s'", org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_CONFIGURATION_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_CONFIGURATION_PROPERTY_NAME_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog272.LDAP_CONFIGURATION_CORRECT_COLLISION_BEHAVIOR_PROPERTY_NAME, org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_CONFIGURATION_CATEGORY_NAME_COLUMN, org.apache.ambari.server.configuration.AmbariServerConfigurationCategory.LDAP_CONFIGURATION.getCategoryName(), org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_CONFIGURATION_PROPERTY_NAME_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog272.LDAP_CONFIGURATION_WRONG_COLLISION_BEHAVIOR_PROPERTY_NAME);

    protected static final java.lang.String HOST_COMPONENT_DESIRED_STATE_TABLE = "hostcomponentdesiredstate";

    protected static final java.lang.String CLUSTERS_TABLE = "clusters";

    protected static final java.lang.String BLUEPRINT_PROVISIONING_STATE_COLUMN = "blueprint_provisioning_state";

    @com.google.inject.Inject
    public UpgradeCatalog272(com.google.inject.Injector injector) {
        super(injector);
    }

    @java.lang.Override
    public java.lang.String getSourceVersion() {
        return "2.7.1";
    }

    @java.lang.Override
    public java.lang.String getTargetVersion() {
        return "2.7.2";
    }

    @java.lang.Override
    protected void executeDDLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        moveBlueprintProvisioningState();
    }

    @java.lang.Override
    protected void executePreDMLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
    }

    @java.lang.Override
    protected void executeDMLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        renameLdapSynchCollisionBehaviorValue();
        createRoleAuthorizations();
    }

    protected int renameLdapSynchCollisionBehaviorValue() throws java.sql.SQLException {
        int numberOfRecordsRenamed = 0;
        if (dbAccessor.tableExists(org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_CONFIGURATION_TABLE)) {
            org.apache.ambari.server.upgrade.UpgradeCatalog272.LOG.debug("Executing: {}", org.apache.ambari.server.upgrade.UpgradeCatalog272.RENAME_COLLISION_BEHAVIOR_PROPERTY_SQL);
            numberOfRecordsRenamed = dbAccessor.executeUpdate(org.apache.ambari.server.upgrade.UpgradeCatalog272.RENAME_COLLISION_BEHAVIOR_PROPERTY_SQL);
            org.apache.ambari.server.upgrade.UpgradeCatalog272.LOG.info("Renamed {} {} with incorrect LDAP configuration property name", numberOfRecordsRenamed, 1 >= numberOfRecordsRenamed ? "record" : "records");
        } else {
            org.apache.ambari.server.upgrade.UpgradeCatalog272.LOG.info("{} table does not exists; nothing to update", org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_CONFIGURATION_TABLE);
        }
        return numberOfRecordsRenamed;
    }

    protected void createRoleAuthorizations() throws java.sql.SQLException {
        addRoleAuthorization(org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_VIEW_STATUS_INFO.getId(), "View status information", java.util.Collections.singleton("AMBARI.ADMINISTRATOR:AMBARI"));
        org.apache.ambari.server.upgrade.UpgradeCatalog272.LOG.info("Added new role authorization {}", org.apache.ambari.server.security.authorization.RoleAuthorization.AMBARI_VIEW_STATUS_INFO.getId());
        addRoleAuthorization(org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MANAGE_WIDGETS.getId(), "Manage widgets", com.google.common.collect.Sets.newHashSet("AMBARI.ADMINISTRATOR:AMBARI", "CLUSTER.ADMINISTRATOR:CLUSTER", "CLUSTER.OPERATOR:CLUSTER"));
        org.apache.ambari.server.upgrade.UpgradeCatalog272.LOG.info("Added new role authorization {}", org.apache.ambari.server.security.authorization.RoleAuthorization.CLUSTER_MANAGE_WIDGETS.getId());
    }

    protected void moveBlueprintProvisioningState() throws java.sql.SQLException {
        dbAccessor.dropColumn(org.apache.ambari.server.upgrade.UpgradeCatalog272.CLUSTERS_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog272.BLUEPRINT_PROVISIONING_STATE_COLUMN);
        dbAccessor.addColumn(org.apache.ambari.server.upgrade.UpgradeCatalog272.HOST_COMPONENT_DESIRED_STATE_TABLE, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog272.BLUEPRINT_PROVISIONING_STATE_COLUMN, java.lang.String.class, 255, org.apache.ambari.server.state.BlueprintProvisioningState.NONE, true));
    }
}