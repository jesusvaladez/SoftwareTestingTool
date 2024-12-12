package org.apache.ambari.server.upgrade;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;
public class UpgradeCatalog260 extends org.apache.ambari.server.upgrade.AbstractUpgradeCatalog {
    public static final java.lang.String CLUSTER_CONFIG_MAPPING_TABLE = "clusterconfigmapping";

    public static final java.lang.String CLUSTER_VERSION_TABLE = "cluster_version";

    public static final java.lang.String CLUSTER_ID_COLUMN = "cluster_id";

    public static final java.lang.String STATE_COLUMN = "state";

    public static final java.lang.String CREATE_TIMESTAMP_COLUMN = "create_timestamp";

    public static final java.lang.String VERSION_TAG_COLUMN = "version_tag";

    public static final java.lang.String TYPE_NAME_COLUMN = "type_name";

    public static final java.lang.String CLUSTER_CONFIG_TABLE = "clusterconfig";

    public static final java.lang.String SELECTED_COLUMN = "selected";

    public static final java.lang.String SERVICE_DELETED_COLUMN = "service_deleted";

    public static final java.lang.String UNMAPPED_COLUMN = "unmapped";

    public static final java.lang.String SELECTED_TIMESTAMP_COLUMN = "selected_timestamp";

    public static final java.lang.String SERVICE_COMPONENT_DESIRED_STATE_TABLE = "servicecomponentdesiredstate";

    public static final java.lang.String DESIRED_STACK_ID_COLUMN = "desired_stack_id";

    public static final java.lang.String DESIRED_VERSION_COLUMN = "desired_version";

    public static final java.lang.String DESIRED_REPO_VERSION_ID_COLUMN = "desired_repo_version_id";

    public static final java.lang.String REPO_STATE_COLUMN = "repo_state";

    public static final java.lang.String FK_SCDS_DESIRED_STACK_ID = "FK_scds_desired_stack_id";

    public static final java.lang.String FK_SERVICECOMPONENTDESIREDSTATE_DESIRED_STACK_ID = "FK_servicecomponentdesiredstate_desired_stack_id";

    public static final java.lang.String FK_SCDS_DESIRED_REPO_ID = "FK_scds_desired_repo_id";

    public static final java.lang.String REPO_VERSION_TABLE = "repo_version";

    public static final java.lang.String REPO_VERSION_ID_COLUMN = "repo_version_id";

    public static final java.lang.String REPO_VERSION_RESOLVED_COLUMN = "resolved";

    public static final java.lang.String REPO_VERSION_HIDDEN_COLUMN = "hidden";

    public static final java.lang.String REPO_VERSION_LEGACY_COLUMN = "legacy";

    public static final java.lang.String HOST_COMPONENT_DESIRED_STATE_TABLE = "hostcomponentdesiredstate";

    public static final java.lang.String FK_HCDS_DESIRED_STACK_ID = "FK_hcds_desired_stack_id";

    public static final java.lang.String HOST_COMPONENT_STATE_TABLE = "hostcomponentstate";

    public static final java.lang.String CURRENT_STACK_ID_COLUMN = "current_stack_id";

    public static final java.lang.String FK_HCS_CURRENT_STACK_ID = "FK_hcs_current_stack_id";

    public static final java.lang.String HOST_VERSION_TABLE = "host_version";

    public static final java.lang.String UQ_HOST_REPO = "UQ_host_repo";

    public static final java.lang.String HOST_ID_COLUMN = "host_id";

    public static final java.lang.String SERVICE_DESIRED_STATE_TABLE = "servicedesiredstate";

    public static final java.lang.String FK_SDS_DESIRED_STACK_ID = "FK_sds_desired_stack_id";

    public static final java.lang.String FK_REPO_VERSION_ID = "FK_repo_version_id";

    public static final java.lang.String CLUSTERS_TABLE = "clusters";

    public static final java.lang.String UPGRADE_TABLE = "upgrade";

    public static final java.lang.String UPGRADE_GROUP_TABLE = "upgrade_group";

    public static final java.lang.String UPGRADE_ITEM_TABLE = "upgrade_item";

    public static final java.lang.String FROM_REPO_VERSION_ID_COLUMN = "from_repo_version_id";

    public static final java.lang.String TO_REPO_VERSION_ID_COLUMN = "to_repo_version_id";

    public static final java.lang.String ORCHESTRATION_COLUMN = "orchestration";

    public static final java.lang.String ALLOW_REVERT_COLUMN = "revert_allowed";

    public static final java.lang.String FK_UPGRADE_FROM_REPO_ID = "FK_upgrade_from_repo_id";

    public static final java.lang.String FK_UPGRADE_TO_REPO_ID = "FK_upgrade_to_repo_id";

    public static final java.lang.String FK_UPGRADE_REPO_VERSION_ID = "FK_upgrade_repo_version_id";

    public static final java.lang.String UPGRADE_ITEM_ITEM_TEXT = "item_text";

    public static final java.lang.String SERVICE_COMPONENT_HISTORY_TABLE = "servicecomponent_history";

    public static final java.lang.String UPGRADE_HISTORY_TABLE = "upgrade_history";

    public static final java.lang.String ID_COLUMN = "id";

    public static final java.lang.String UPGRADE_ID_COLUMN = "upgrade_id";

    public static final java.lang.String SERVICE_NAME_COLUMN = "service_name";

    public static final java.lang.String COMPONENT_NAME_COLUMN = "component_name";

    public static final java.lang.String TARGET_REPO_VERSION_ID_COLUMN = "target_repo_version_id";

    public static final java.lang.String PK_UPGRADE_HIST = "PK_upgrade_hist";

    public static final java.lang.String FK_UPGRADE_HIST_UPGRADE_ID = "FK_upgrade_hist_upgrade_id";

    public static final java.lang.String FK_UPGRADE_HIST_FROM_REPO = "FK_upgrade_hist_from_repo";

    public static final java.lang.String FK_UPGRADE_HIST_TARGET_REPO = "FK_upgrade_hist_target_repo";

    public static final java.lang.String UQ_UPGRADE_HIST = "UQ_upgrade_hist";

    public static final java.lang.String SERVICE_CONFIG_MAPPING_TABLE = "serviceconfigmapping";

    public static final java.lang.String SERVICE_COMPONENT_DESIRED_STATE = "servicecomponentdesiredstate";

    public static final java.lang.String HOST_COMPONENT_DESIRED_STATE = "hostcomponentdesiredstate";

    public static final java.lang.String HOST_COMPONENT_STATE = "hostcomponentstate";

    private static final java.lang.String CORE_SITE = "core-site";

    public static final java.lang.String AMS_SSL_CLIENT = "ams-ssl-client";

    public static final java.lang.String METRIC_TRUSTSTORE_ALIAS = "ssl.client.truststore.alias";

    private static final java.lang.String HIVE_INTERACTIVE_SITE = "hive-interactive-site";

    public static final java.lang.String HIVE_LLAP_DAEMON_KEYTAB_FILE = "hive.llap.daemon.keytab.file";

    public static final java.lang.String HIVE_LLAP_ZK_SM_KEYTAB_FILE = "hive.llap.zk.sm.keytab.file";

    public static final java.lang.String HIVE_LLAP_TASK_KEYTAB_FILE = "hive.llap.task.keytab.file";

    public static final java.lang.String HIVE_SERVER_KERBEROS_PREFIX = "/HIVE/HIVE_SERVER/";

    public static final java.lang.String YARN_LLAP_ZK_HIVE_KERBEROS_IDENTITY = "llap_zk_hive";

    public static final java.lang.String YARN_LLAP_TASK_HIVE_KERBEROS_IDENTITY = "llap_task_hive";

    public static final java.lang.String HIVE_SERVER_HIVE_KERBEROS_IDENTITY = "hive_server_hive";

    private java.util.List<java.lang.String> yarnKerberosDescUpdatedList = new java.util.ArrayList<>();

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.upgrade.UpgradeCatalog260.class);

    public static final java.lang.String STANDARD = "STANDARD";

    public static final java.lang.String NOT_REQUIRED = "NOT_REQUIRED";

    public static final java.lang.String CURRENT = "CURRENT";

    public static final java.lang.String SELECTED = "1";

    public static final java.lang.String VIEWURL_TABLE = "viewurl";

    public static final java.lang.String VIEWINSTANCE_TABLE = "viewinstance";

    public static final java.lang.String PK_VIEWURL = "PK_viewurl";

    public static final java.lang.String URL_ID_COLUMN = "url_id";

    public static final java.lang.String STALE_POSTGRESS_VIEWURL_PKEY = "viewurl_pkey";

    public static final java.lang.String USERS_TABLE = "users";

    public static final java.lang.String STALE_POSTGRESS_USERS_LDAP_USER_KEY = "users_ldap_user_key";

    public static final java.lang.String SHORT_URL_COLUMN = "short_url";

    public static final java.lang.String FK_INSTANCE_URL_ID = "FK_instance_url_id";

    public static final java.lang.String FK_SERVICEDESIREDSTATE_DESIRED_STACK_ID = "FK_servicedesiredstate_desired_stack_id";

    public static final java.lang.String FK_HOSTCOMPONENTDESIREDSTATE_DESIRED_STACK_ID = "FK_hostcomponentdesiredstate_desired_stack_id";

    public static final java.lang.String FK_HOSTCOMPONENTSTATE_CURRENT_STACK_ID = "FK_hostcomponentstate_current_stack_id";

    public static final java.lang.String FK_UPGRADE_FROM_REPO_VERSION_ID = "FK_upgrade_from_repo_version_id";

    public static final java.lang.String FK_UPGRADE_TO_REPO_VERSION_ID = "FK_upgrade_to_repo_version_id";

    @com.google.inject.Inject
    public UpgradeCatalog260(com.google.inject.Injector injector) {
        super(injector);
    }

    @java.lang.Override
    public java.lang.String getSourceVersion() {
        return "2.5.2";
    }

    @java.lang.Override
    public java.lang.String getTargetVersion() {
        return "2.6.0";
    }

    @java.lang.Override
    protected void executeDDLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        java.lang.Integer currentVersionID = getCurrentVersionID();
        dropBrokenFK();
        updateServiceComponentDesiredStateTable(currentVersionID);
        updateServiceDesiredStateTable(currentVersionID);
        addSelectedCollumsToClusterconfigTable();
        updateHostComponentDesiredStateTable();
        updateHostComponentStateTable();
        dropStaleTables();
        updateUpgradeTable();
        createUpgradeHistoryTable();
        updateRepositoryVersionTable();
        renameServiceDeletedColumn();
        addLegacyColumn();
        expandUpgradeItemItemTextColumn();
        addViewUrlPKConstraint();
        removeStaleConstraints();
    }

    private void dropBrokenFK() throws java.sql.SQLException {
        dbAccessor.dropFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog260.UPGRADE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.FK_UPGRADE_FROM_REPO_VERSION_ID);
        dbAccessor.dropFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog260.UPGRADE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.FK_UPGRADE_TO_REPO_VERSION_ID);
        dbAccessor.dropFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog260.SERVICE_COMPONENT_DESIRED_STATE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.FK_SERVICECOMPONENTDESIREDSTATE_DESIRED_STACK_ID);
        dbAccessor.dropFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog260.SERVICE_DESIRED_STATE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.FK_SERVICEDESIREDSTATE_DESIRED_STACK_ID);
        dbAccessor.dropFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog260.HOST_COMPONENT_DESIRED_STATE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.FK_HOSTCOMPONENTDESIREDSTATE_DESIRED_STACK_ID);
        dbAccessor.dropFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog260.HOST_COMPONENT_STATE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.FK_HOSTCOMPONENTSTATE_CURRENT_STACK_ID);
    }

    private void addViewUrlPKConstraint() throws java.sql.SQLException {
        dbAccessor.dropFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog260.VIEWINSTANCE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.FK_INSTANCE_URL_ID);
        dbAccessor.dropPKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog260.VIEWURL_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.STALE_POSTGRESS_VIEWURL_PKEY);
        dbAccessor.addPKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog260.VIEWURL_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.PK_VIEWURL, org.apache.ambari.server.upgrade.UpgradeCatalog260.URL_ID_COLUMN);
        dbAccessor.addFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog260.VIEWINSTANCE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.FK_INSTANCE_URL_ID, org.apache.ambari.server.upgrade.UpgradeCatalog260.SHORT_URL_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog260.VIEWURL_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.URL_ID_COLUMN, false);
    }

    private void removeStaleConstraints() throws java.sql.SQLException {
        dbAccessor.dropUniqueConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog260.USERS_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.STALE_POSTGRESS_USERS_LDAP_USER_KEY);
    }

    private void expandUpgradeItemItemTextColumn() throws java.sql.SQLException {
        dbAccessor.changeColumnType(org.apache.ambari.server.upgrade.UpgradeCatalog260.UPGRADE_ITEM_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.UPGRADE_ITEM_ITEM_TEXT, java.lang.String.class, char[].class);
    }

    private void addLegacyColumn() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        java.lang.Boolean isLegacyColumnExists = dbAccessor.tableHasColumn(org.apache.ambari.server.upgrade.UpgradeCatalog260.REPO_VERSION_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.REPO_VERSION_LEGACY_COLUMN);
        if (!isLegacyColumnExists) {
            org.apache.ambari.server.orm.DBAccessor.DBColumnInfo legacyColumn = new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog260.REPO_VERSION_LEGACY_COLUMN, java.lang.Short.class, null, 1, false);
            dbAccessor.addColumn(org.apache.ambari.server.upgrade.UpgradeCatalog260.REPO_VERSION_TABLE, legacyColumn);
            legacyColumn.setDefaultValue(0);
            dbAccessor.alterColumn(org.apache.ambari.server.upgrade.UpgradeCatalog260.REPO_VERSION_TABLE, legacyColumn);
        }
    }

    private void renameServiceDeletedColumn() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        if (dbAccessor.tableHasColumn(org.apache.ambari.server.upgrade.UpgradeCatalog260.CLUSTER_CONFIG_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.SERVICE_DELETED_COLUMN)) {
            dbAccessor.renameColumn(org.apache.ambari.server.upgrade.UpgradeCatalog260.CLUSTER_CONFIG_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.SERVICE_DELETED_COLUMN, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog260.UNMAPPED_COLUMN, java.lang.Short.class, null, 0, false));
        }
    }

    private void setUnmappedForOrphanedConfigs() {
        executeInTransaction(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                javax.persistence.EntityManager entityManager = getEntityManagerProvider().get();
                javax.persistence.Query query = entityManager.createNamedQuery("ClusterConfigEntity.findNotMappedClusterConfigsToService", org.apache.ambari.server.orm.entities.ClusterConfigEntity.class);
                java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity> notMappedConfigs = query.getResultList();
                if (notMappedConfigs != null) {
                    for (org.apache.ambari.server.orm.entities.ClusterConfigEntity clusterConfigEntity : notMappedConfigs) {
                        clusterConfigEntity.setUnmapped(true);
                        entityManager.merge(clusterConfigEntity);
                    }
                }
            }
        });
    }

    private void createUpgradeHistoryTable() throws java.sql.SQLException {
        java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> columns = new java.util.ArrayList<>();
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog260.ID_COLUMN, java.lang.Long.class, null, null, false));
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog260.UPGRADE_ID_COLUMN, java.lang.Long.class, null, null, false));
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog260.SERVICE_NAME_COLUMN, java.lang.String.class, 255, null, false));
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog260.COMPONENT_NAME_COLUMN, java.lang.String.class, 255, null, false));
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog260.FROM_REPO_VERSION_ID_COLUMN, java.lang.Long.class, null, null, false));
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog260.TARGET_REPO_VERSION_ID_COLUMN, java.lang.Long.class, null, null, false));
        dbAccessor.createTable(org.apache.ambari.server.upgrade.UpgradeCatalog260.UPGRADE_HISTORY_TABLE, columns);
        dbAccessor.addPKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog260.UPGRADE_HISTORY_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.PK_UPGRADE_HIST, org.apache.ambari.server.upgrade.UpgradeCatalog260.ID_COLUMN);
        dbAccessor.addFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog260.UPGRADE_HISTORY_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.FK_UPGRADE_HIST_UPGRADE_ID, org.apache.ambari.server.upgrade.UpgradeCatalog260.UPGRADE_ID_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog260.UPGRADE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.UPGRADE_ID_COLUMN, false);
        dbAccessor.addFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog260.UPGRADE_HISTORY_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.FK_UPGRADE_HIST_FROM_REPO, org.apache.ambari.server.upgrade.UpgradeCatalog260.FROM_REPO_VERSION_ID_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog260.REPO_VERSION_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.REPO_VERSION_ID_COLUMN, false);
        dbAccessor.addFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog260.UPGRADE_HISTORY_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.FK_UPGRADE_HIST_TARGET_REPO, org.apache.ambari.server.upgrade.UpgradeCatalog260.TARGET_REPO_VERSION_ID_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog260.REPO_VERSION_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.REPO_VERSION_ID_COLUMN, false);
        dbAccessor.addUniqueConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog260.UPGRADE_HISTORY_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.UQ_UPGRADE_HIST, org.apache.ambari.server.upgrade.UpgradeCatalog260.UPGRADE_ID_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog260.COMPONENT_NAME_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog260.SERVICE_NAME_COLUMN);
        addSequence("upgrade_history_id_seq", 0L, false);
    }

    private void updateUpgradeTable() throws java.sql.SQLException {
        dbAccessor.clearTableColumn(org.apache.ambari.server.upgrade.UpgradeCatalog260.CLUSTERS_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.UPGRADE_ID_COLUMN, null);
        dbAccessor.clearTable(org.apache.ambari.server.upgrade.UpgradeCatalog260.UPGRADE_ITEM_TABLE);
        dbAccessor.clearTable(org.apache.ambari.server.upgrade.UpgradeCatalog260.UPGRADE_GROUP_TABLE);
        dbAccessor.clearTable(org.apache.ambari.server.upgrade.UpgradeCatalog260.UPGRADE_TABLE);
        dbAccessor.dropFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog260.UPGRADE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.FK_UPGRADE_FROM_REPO_ID);
        dbAccessor.dropFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog260.UPGRADE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.FK_UPGRADE_TO_REPO_ID);
        dbAccessor.dropColumn(org.apache.ambari.server.upgrade.UpgradeCatalog260.UPGRADE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.FROM_REPO_VERSION_ID_COLUMN);
        dbAccessor.dropColumn(org.apache.ambari.server.upgrade.UpgradeCatalog260.UPGRADE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.TO_REPO_VERSION_ID_COLUMN);
        dbAccessor.addColumn(org.apache.ambari.server.upgrade.UpgradeCatalog260.UPGRADE_TABLE, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog260.REPO_VERSION_ID_COLUMN, java.lang.Long.class, null, null, false));
        dbAccessor.addColumn(org.apache.ambari.server.upgrade.UpgradeCatalog260.UPGRADE_TABLE, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog260.ORCHESTRATION_COLUMN, java.lang.String.class, 255, org.apache.ambari.server.upgrade.UpgradeCatalog260.STANDARD, false));
        dbAccessor.addColumn(org.apache.ambari.server.upgrade.UpgradeCatalog260.UPGRADE_TABLE, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog260.ALLOW_REVERT_COLUMN, java.lang.Short.class, null, 0, false));
        dbAccessor.addFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog260.UPGRADE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.FK_UPGRADE_REPO_VERSION_ID, org.apache.ambari.server.upgrade.UpgradeCatalog260.REPO_VERSION_ID_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog260.REPO_VERSION_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.REPO_VERSION_ID_COLUMN, false);
    }

    private void updateServiceDesiredStateTable(java.lang.Integer currentRepoID) throws java.sql.SQLException {
        dbAccessor.addColumn(org.apache.ambari.server.upgrade.UpgradeCatalog260.SERVICE_DESIRED_STATE_TABLE, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog260.DESIRED_REPO_VERSION_ID_COLUMN, java.lang.Long.class, null, currentRepoID, false));
        dbAccessor.alterColumn(org.apache.ambari.server.upgrade.UpgradeCatalog260.SERVICE_DESIRED_STATE_TABLE, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog260.DESIRED_REPO_VERSION_ID_COLUMN, java.lang.Long.class, null, null, false));
        dbAccessor.addFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog260.SERVICE_DESIRED_STATE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.FK_REPO_VERSION_ID, org.apache.ambari.server.upgrade.UpgradeCatalog260.DESIRED_REPO_VERSION_ID_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog260.REPO_VERSION_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.REPO_VERSION_ID_COLUMN, false);
        dbAccessor.dropFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog260.SERVICE_DESIRED_STATE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.FK_SDS_DESIRED_STACK_ID);
        dbAccessor.dropColumn(org.apache.ambari.server.upgrade.UpgradeCatalog260.SERVICE_DESIRED_STATE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.DESIRED_STACK_ID_COLUMN);
    }

    private void dropStaleTables() throws java.sql.SQLException {
        dbAccessor.dropTable(org.apache.ambari.server.upgrade.UpgradeCatalog260.CLUSTER_CONFIG_MAPPING_TABLE);
        dbAccessor.dropTable(org.apache.ambari.server.upgrade.UpgradeCatalog260.CLUSTER_VERSION_TABLE);
        dbAccessor.dropTable(org.apache.ambari.server.upgrade.UpgradeCatalog260.SERVICE_COMPONENT_HISTORY_TABLE);
    }

    private void addSelectedCollumsToClusterconfigTable() throws java.sql.SQLException {
        org.apache.ambari.server.orm.DBAccessor.DBColumnInfo selectedColumnInfo = new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog260.SELECTED_COLUMN, java.lang.Short.class, null, 0, false);
        org.apache.ambari.server.orm.DBAccessor.DBColumnInfo selectedmappingColumnInfo = new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog260.SELECTED_COLUMN, java.lang.Integer.class, null, 0, false);
        org.apache.ambari.server.orm.DBAccessor.DBColumnInfo selectedTimestampColumnInfo = new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog260.SELECTED_TIMESTAMP_COLUMN, java.lang.Long.class, null, 0, false);
        org.apache.ambari.server.orm.DBAccessor.DBColumnInfo createTimestampColumnInfo = new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog260.CREATE_TIMESTAMP_COLUMN, java.lang.Long.class, null, null, false);
        dbAccessor.copyColumnToAnotherTable(org.apache.ambari.server.upgrade.UpgradeCatalog260.CLUSTER_CONFIG_MAPPING_TABLE, selectedmappingColumnInfo, org.apache.ambari.server.upgrade.UpgradeCatalog260.CLUSTER_ID_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog260.TYPE_NAME_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog260.VERSION_TAG_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog260.CLUSTER_CONFIG_TABLE, selectedColumnInfo, org.apache.ambari.server.upgrade.UpgradeCatalog260.CLUSTER_ID_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog260.TYPE_NAME_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog260.VERSION_TAG_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog260.SELECTED_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog260.SELECTED, 0);
        dbAccessor.copyColumnToAnotherTable(org.apache.ambari.server.upgrade.UpgradeCatalog260.CLUSTER_CONFIG_MAPPING_TABLE, createTimestampColumnInfo, org.apache.ambari.server.upgrade.UpgradeCatalog260.CLUSTER_ID_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog260.TYPE_NAME_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog260.VERSION_TAG_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog260.CLUSTER_CONFIG_TABLE, selectedTimestampColumnInfo, org.apache.ambari.server.upgrade.UpgradeCatalog260.CLUSTER_ID_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog260.TYPE_NAME_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog260.VERSION_TAG_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog260.SELECTED_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog260.SELECTED, 0);
    }

    private void updateServiceComponentDesiredStateTable(java.lang.Integer currentRepoID) throws java.sql.SQLException {
        dbAccessor.addColumn(org.apache.ambari.server.upgrade.UpgradeCatalog260.SERVICE_COMPONENT_DESIRED_STATE_TABLE, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog260.DESIRED_REPO_VERSION_ID_COLUMN, java.lang.Long.class, null, currentRepoID, false));
        dbAccessor.alterColumn(org.apache.ambari.server.upgrade.UpgradeCatalog260.SERVICE_COMPONENT_DESIRED_STATE_TABLE, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog260.DESIRED_REPO_VERSION_ID_COLUMN, java.lang.Long.class, null, null, false));
        dbAccessor.addColumn(org.apache.ambari.server.upgrade.UpgradeCatalog260.SERVICE_COMPONENT_DESIRED_STATE_TABLE, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog260.REPO_STATE_COLUMN, java.lang.String.class, 255, org.apache.ambari.server.upgrade.UpgradeCatalog260.CURRENT, false));
        dbAccessor.alterColumn(org.apache.ambari.server.upgrade.UpgradeCatalog260.SERVICE_COMPONENT_DESIRED_STATE_TABLE, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog260.REPO_STATE_COLUMN, java.lang.String.class, 255, org.apache.ambari.server.upgrade.UpgradeCatalog260.NOT_REQUIRED, false));
        dbAccessor.addFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog260.SERVICE_COMPONENT_DESIRED_STATE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.FK_SCDS_DESIRED_REPO_ID, org.apache.ambari.server.upgrade.UpgradeCatalog260.DESIRED_REPO_VERSION_ID_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog260.REPO_VERSION_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.REPO_VERSION_ID_COLUMN, false);
        dbAccessor.dropFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog260.SERVICE_COMPONENT_DESIRED_STATE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.FK_SCDS_DESIRED_STACK_ID);
        dbAccessor.dropColumn(org.apache.ambari.server.upgrade.UpgradeCatalog260.SERVICE_COMPONENT_DESIRED_STATE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.DESIRED_STACK_ID_COLUMN);
        dbAccessor.dropColumn(org.apache.ambari.server.upgrade.UpgradeCatalog260.SERVICE_COMPONENT_DESIRED_STATE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.DESIRED_VERSION_COLUMN);
    }

    private void updateHostComponentDesiredStateTable() throws java.sql.SQLException {
        dbAccessor.dropFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog260.HOST_COMPONENT_DESIRED_STATE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.FK_HCDS_DESIRED_STACK_ID);
        dbAccessor.dropColumn(org.apache.ambari.server.upgrade.UpgradeCatalog260.HOST_COMPONENT_DESIRED_STATE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.DESIRED_STACK_ID_COLUMN);
    }

    private void updateHostComponentStateTable() throws java.sql.SQLException {
        dbAccessor.dropFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog260.HOST_COMPONENT_STATE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.FK_HCS_CURRENT_STACK_ID);
        dbAccessor.dropColumn(org.apache.ambari.server.upgrade.UpgradeCatalog260.HOST_COMPONENT_STATE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.CURRENT_STACK_ID_COLUMN);
    }

    private void updateRepositoryVersionTable() throws java.sql.SQLException {
        dbAccessor.addColumn(org.apache.ambari.server.upgrade.UpgradeCatalog260.REPO_VERSION_TABLE, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog260.REPO_VERSION_HIDDEN_COLUMN, java.lang.Short.class, null, 0, false));
        dbAccessor.addColumn(org.apache.ambari.server.upgrade.UpgradeCatalog260.REPO_VERSION_TABLE, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog260.REPO_VERSION_RESOLVED_COLUMN, java.lang.Short.class, null, 0, false));
    }

    @java.lang.Override
    protected void executePreDMLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        removeSupersetFromDruid();
    }

    @java.lang.Override
    protected void executeDMLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        addNewConfigurationsFromXml();
        setUnmappedForOrphanedConfigs();
        ensureZeppelinProxyUserConfigs();
        updateKerberosDescriptorArtifacts();
        updateAmsConfigs();
        updateHiveConfigs();
        updateHDFSWidgetDefinition();
        updateExistingRepositoriesToBeResolved();
    }

    public java.lang.Integer getCurrentVersionID() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        if (!dbAccessor.tableExists(org.apache.ambari.server.upgrade.UpgradeCatalog260.CLUSTER_VERSION_TABLE)) {
            return null;
        }
        java.util.List<java.lang.Integer> currentVersionList = dbAccessor.getIntColumnValues(org.apache.ambari.server.upgrade.UpgradeCatalog260.CLUSTER_VERSION_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.REPO_VERSION_ID_COLUMN, new java.lang.String[]{ org.apache.ambari.server.upgrade.UpgradeCatalog260.STATE_COLUMN }, new java.lang.String[]{ org.apache.ambari.server.upgrade.UpgradeCatalog260.CURRENT }, false);
        if (currentVersionList.isEmpty()) {
            java.util.List<java.lang.Integer> allVersionList = dbAccessor.getIntColumnValues(org.apache.ambari.server.upgrade.UpgradeCatalog260.CLUSTER_VERSION_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.REPO_VERSION_ID_COLUMN, null, null, false);
            if (allVersionList.isEmpty()) {
                return null;
            } else {
                throw new org.apache.ambari.server.AmbariException("Unable to find any CURRENT repositories.");
            }
        } else if (currentVersionList.size() != 1) {
            throw new org.apache.ambari.server.AmbariException("The following repositories were found to be CURRENT: ".concat(org.apache.commons.lang.StringUtils.join(currentVersionList, ",")));
        }
        return currentVersionList.get(0);
    }

    protected void removeSupersetFromDruid() throws java.sql.SQLException {
        removeComponent("DRUID_SUPERSET", "druid-superset");
    }

    private void removeComponent(java.lang.String componentName, java.lang.String configPrefix) throws java.sql.SQLException {
        java.lang.String serviceConfigMappingRemoveSQL = java.lang.String.format("DELETE FROM %s WHERE config_id IN (SELECT config_id from %s where type_name like '%s%%')", org.apache.ambari.server.upgrade.UpgradeCatalog260.SERVICE_CONFIG_MAPPING_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog260.CLUSTER_CONFIG_TABLE, configPrefix);
        java.lang.String supersetConfigRemoveSQL = java.lang.String.format("DELETE FROM %s WHERE type_name like '%s%%'", org.apache.ambari.server.upgrade.UpgradeCatalog260.CLUSTER_CONFIG_TABLE, configPrefix);
        java.lang.String hostComponentDesiredStateRemoveSQL = java.lang.String.format("DELETE FROM %s WHERE component_name = '%s'", org.apache.ambari.server.upgrade.UpgradeCatalog260.HOST_COMPONENT_DESIRED_STATE, componentName);
        java.lang.String hostComponentStateRemoveSQL = java.lang.String.format("DELETE FROM %s WHERE component_name = '%s'", org.apache.ambari.server.upgrade.UpgradeCatalog260.HOST_COMPONENT_STATE, componentName);
        java.lang.String serviceComponentDesiredStateRemoveSQL = java.lang.String.format("DELETE FROM %s WHERE component_name = '%s'", org.apache.ambari.server.upgrade.UpgradeCatalog260.SERVICE_COMPONENT_DESIRED_STATE, componentName);
        dbAccessor.executeQuery(serviceConfigMappingRemoveSQL);
        dbAccessor.executeQuery(supersetConfigRemoveSQL);
        dbAccessor.executeQuery(hostComponentDesiredStateRemoveSQL);
        dbAccessor.executeQuery(hostComponentStateRemoveSQL);
        dbAccessor.executeQuery(serviceComponentDesiredStateRemoveSQL);
    }

    void ensureZeppelinProxyUserConfigs() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = getCheckedClusterMap(clusters);
        if ((clusterMap != null) && (!clusterMap.isEmpty())) {
            for (final org.apache.ambari.server.state.Cluster cluster : clusterMap.values()) {
                org.apache.ambari.server.state.Config zeppelinEnvConfig = cluster.getDesiredConfigByType("zeppelin-env");
                if (zeppelinEnvConfig != null) {
                    java.util.Map<java.lang.String, java.lang.String> zeppelinEnvProperties = zeppelinEnvConfig.getProperties();
                    java.lang.String zeppelinUser = null;
                    if (zeppelinEnvProperties != null) {
                        zeppelinUser = zeppelinEnvProperties.get("zeppelin_user");
                    }
                    if (!org.apache.commons.lang.StringUtils.isEmpty(zeppelinUser)) {
                        org.apache.ambari.server.state.Config coreSiteConfig = cluster.getDesiredConfigByType(org.apache.ambari.server.upgrade.UpgradeCatalog260.CORE_SITE);
                        if (coreSiteConfig != null) {
                            java.lang.String proxyUserHostsName = java.lang.String.format("hadoop.proxyuser.%s.hosts", zeppelinUser);
                            java.lang.String proxyUserGroupsName = java.lang.String.format("hadoop.proxyuser.%s.groups", zeppelinUser);
                            java.util.Map<java.lang.String, java.lang.String> proxyUserProperties = new java.util.HashMap<>();
                            proxyUserProperties.put(proxyUserHostsName, "*");
                            proxyUserProperties.put(proxyUserGroupsName, "*");
                            java.util.Map<java.lang.String, java.lang.String> coreSiteConfigProperties = coreSiteConfig.getProperties();
                            if (coreSiteConfigProperties != null) {
                                if (coreSiteConfigProperties.containsKey(proxyUserHostsName)) {
                                    proxyUserProperties.remove(proxyUserHostsName);
                                }
                                if (coreSiteConfigProperties.containsKey(proxyUserGroupsName)) {
                                    proxyUserProperties.remove(proxyUserGroupsName);
                                }
                            }
                            if (!proxyUserProperties.isEmpty()) {
                                updateConfigurationPropertiesForCluster(cluster, org.apache.ambari.server.upgrade.UpgradeCatalog260.CORE_SITE, proxyUserProperties, true, false);
                            }
                        }
                    }
                }
            }
        }
    }

    @java.lang.Override
    protected void updateKerberosDescriptorArtifact(org.apache.ambari.server.orm.dao.ArtifactDAO artifactDAO, org.apache.ambari.server.orm.entities.ArtifactEntity artifactEntity) throws org.apache.ambari.server.AmbariException {
        if (artifactEntity != null) {
            java.util.Map<java.lang.String, java.lang.Object> data = artifactEntity.getArtifactData();
            if (data != null) {
                final org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = new org.apache.ambari.server.state.kerberos.KerberosDescriptorFactory().createInstance(data);
                if (kerberosDescriptor != null) {
                    fixRangerKMSKerberosDescriptor(kerberosDescriptor);
                    fixIdentityReferences(getCluster(artifactEntity), kerberosDescriptor);
                    fixYarnHsiKerberosDescriptorAndSiteConfig(getCluster(artifactEntity), kerberosDescriptor);
                    artifactEntity.setArtifactData(kerberosDescriptor.toMap());
                    artifactDAO.merge(artifactEntity);
                }
            }
        }
    }

    protected void fixRangerKMSKerberosDescriptor(org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor) {
        org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor rangerKmsServiceDescriptor = kerberosDescriptor.getService("RANGER_KMS");
        if (rangerKmsServiceDescriptor != null) {
            org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor rangerKmsServiceIdentity = rangerKmsServiceDescriptor.getIdentity("/smokeuser");
            if (rangerKmsServiceIdentity != null) {
                rangerKmsServiceDescriptor.removeIdentity("/smokeuser");
            }
            org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor rangerKmscomponentDescriptor = rangerKmsServiceDescriptor.getComponent("RANGER_KMS_SERVER");
            if (rangerKmscomponentDescriptor != null) {
                org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor rangerKmsComponentIdentity = rangerKmscomponentDescriptor.getIdentity("/smokeuser");
                if (rangerKmsComponentIdentity != null) {
                    rangerKmscomponentDescriptor.removeIdentity("/smokeuser");
                }
            }
        }
    }

    protected void fixYarnHsiKerberosDescriptorAndSiteConfig(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor) {
        org.apache.ambari.server.upgrade.UpgradeCatalog260.LOG.info("Updating YARN's HSI Kerberos Descriptor ....");
        org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor hiveServiceDescriptor = kerberosDescriptor.getService("HIVE");
        org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor hsh_identityDescriptor = null;
        org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor hsh_principalDescriptor = null;
        org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor hsh_keytabDescriptor = null;
        if (hiveServiceDescriptor != null) {
            org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor hiveServerKerberosDescriptor = hiveServiceDescriptor.getComponent("HIVE_SERVER");
            if (hiveServerKerberosDescriptor != null) {
                hsh_identityDescriptor = hiveServerKerberosDescriptor.getIdentity(org.apache.ambari.server.upgrade.UpgradeCatalog260.HIVE_SERVER_HIVE_KERBEROS_IDENTITY);
                if (hsh_identityDescriptor != null) {
                    org.apache.ambari.server.upgrade.UpgradeCatalog260.LOG.info("  Retrieved HIVE->HIVE_SERVER kerberos descriptor. Name = " + hsh_identityDescriptor.getName());
                    hsh_principalDescriptor = hsh_identityDescriptor.getPrincipalDescriptor();
                    hsh_keytabDescriptor = hsh_identityDescriptor.getKeytabDescriptor();
                }
            }
            if ((hsh_principalDescriptor != null) && (hsh_keytabDescriptor != null)) {
                org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor yarnServiceDescriptor = kerberosDescriptor.getService("YARN");
                if (yarnServiceDescriptor != null) {
                    org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor yarnNmKerberosDescriptor = yarnServiceDescriptor.getComponent("NODEMANAGER");
                    if (yarnNmKerberosDescriptor != null) {
                        java.lang.String[] identities = new java.lang.String[]{ org.apache.ambari.server.upgrade.UpgradeCatalog260.YARN_LLAP_ZK_HIVE_KERBEROS_IDENTITY, org.apache.ambari.server.upgrade.UpgradeCatalog260.YARN_LLAP_TASK_HIVE_KERBEROS_IDENTITY };
                        for (java.lang.String identity : identities) {
                            org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptor = yarnNmKerberosDescriptor.getIdentity(identity);
                            org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principalDescriptor = null;
                            org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor keytabDescriptor = null;
                            if (identityDescriptor != null) {
                                org.apache.ambari.server.upgrade.UpgradeCatalog260.LOG.info("  Retrieved YARN->NODEMANAGER kerberos descriptor to be updated. Name = " + identityDescriptor.getName());
                                principalDescriptor = identityDescriptor.getPrincipalDescriptor();
                                keytabDescriptor = identityDescriptor.getKeytabDescriptor();
                                identityDescriptor.setReference(org.apache.ambari.server.upgrade.UpgradeCatalog260.HIVE_SERVER_KERBEROS_PREFIX + hsh_identityDescriptor.getName());
                                org.apache.ambari.server.upgrade.UpgradeCatalog260.LOG.info(((("    Updated '" + org.apache.ambari.server.upgrade.UpgradeCatalog260.YARN_LLAP_ZK_HIVE_KERBEROS_IDENTITY) + "' identity descriptor reference = '") + identityDescriptor.getReference()) + "'");
                                principalDescriptor.setValue(null);
                                org.apache.ambari.server.upgrade.UpgradeCatalog260.LOG.info(((("    Updated '" + org.apache.ambari.server.upgrade.UpgradeCatalog260.YARN_LLAP_ZK_HIVE_KERBEROS_IDENTITY) + "' principal descriptor value = '") + principalDescriptor.getValue()) + "'");
                                keytabDescriptor.setFile(null);
                                org.apache.ambari.server.upgrade.UpgradeCatalog260.LOG.info(((("    Updated '" + org.apache.ambari.server.upgrade.UpgradeCatalog260.YARN_LLAP_ZK_HIVE_KERBEROS_IDENTITY) + "' keytab descriptor file = '") + keytabDescriptor.getFile()) + "'");
                                keytabDescriptor.setOwnerName(null);
                                org.apache.ambari.server.upgrade.UpgradeCatalog260.LOG.info(((("    Updated '" + org.apache.ambari.server.upgrade.UpgradeCatalog260.YARN_LLAP_ZK_HIVE_KERBEROS_IDENTITY) + "' keytab descriptor owner name = '") + keytabDescriptor.getOwnerName()) + "'");
                                keytabDescriptor.setOwnerAccess(null);
                                org.apache.ambari.server.upgrade.UpgradeCatalog260.LOG.info(((("    Updated '" + org.apache.ambari.server.upgrade.UpgradeCatalog260.YARN_LLAP_ZK_HIVE_KERBEROS_IDENTITY) + "' keytab descriptor owner access = '") + keytabDescriptor.getOwnerAccess()) + "'");
                                keytabDescriptor.setGroupName(null);
                                org.apache.ambari.server.upgrade.UpgradeCatalog260.LOG.info(((("    Updated '" + org.apache.ambari.server.upgrade.UpgradeCatalog260.YARN_LLAP_ZK_HIVE_KERBEROS_IDENTITY) + "' keytab descriptor group name = '") + keytabDescriptor.getGroupName()) + "'");
                                keytabDescriptor.setGroupAccess(null);
                                org.apache.ambari.server.upgrade.UpgradeCatalog260.LOG.info(((("    Updated '" + org.apache.ambari.server.upgrade.UpgradeCatalog260.YARN_LLAP_ZK_HIVE_KERBEROS_IDENTITY) + "' keytab descriptor group access = '") + keytabDescriptor.getGroupAccess()) + "'");
                                java.lang.String[] splits = keytabDescriptor.getConfiguration().split("/");
                                if (splits.length == 2) {
                                    updateYarnKerberosDescUpdatedList(splits[1]);
                                    org.apache.ambari.server.upgrade.UpgradeCatalog260.LOG.info("    Updated 'yarnKerberosDescUpdatedList' = " + getYarnKerberosDescUpdatedList());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void updateYarnKerberosDescUpdatedList(java.lang.String val) {
        yarnKerberosDescUpdatedList.add(val);
    }

    public java.util.List<java.lang.String> getYarnKerberosDescUpdatedList() {
        return yarnKerberosDescUpdatedList;
    }

    protected void updateHiveConfigs() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = ambariManagementController.getClusters();
        if (clusters != null) {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = getCheckedClusterMap(clusters);
            if ((clusterMap != null) && (!clusterMap.isEmpty())) {
                for (final org.apache.ambari.server.state.Cluster cluster : clusterMap.values()) {
                    org.apache.ambari.server.state.Config hsiSiteConfig = cluster.getDesiredConfigByType(org.apache.ambari.server.upgrade.UpgradeCatalog260.HIVE_INTERACTIVE_SITE);
                    if (hsiSiteConfig != null) {
                        java.util.Map<java.lang.String, java.lang.String> hsiSiteConfigProperties = hsiSiteConfig.getProperties();
                        if ((hsiSiteConfigProperties != null) && hsiSiteConfigProperties.containsKey(org.apache.ambari.server.upgrade.UpgradeCatalog260.HIVE_LLAP_DAEMON_KEYTAB_FILE)) {
                            java.lang.String[] identities = new java.lang.String[]{ org.apache.ambari.server.upgrade.UpgradeCatalog260.HIVE_LLAP_ZK_SM_KEYTAB_FILE, org.apache.ambari.server.upgrade.UpgradeCatalog260.HIVE_LLAP_TASK_KEYTAB_FILE };
                            java.util.Map<java.lang.String, java.lang.String> newProperties = new java.util.HashMap<>();
                            for (java.lang.String identity : identities) {
                                if (getYarnKerberosDescUpdatedList().contains(identity) && hsiSiteConfigProperties.containsKey(identity)) {
                                    newProperties.put(identity, hsiSiteConfigProperties.get(org.apache.ambari.server.upgrade.UpgradeCatalog260.HIVE_LLAP_DAEMON_KEYTAB_FILE));
                                }
                            }
                            if (newProperties.size() > 0) {
                                try {
                                    updateConfigurationPropertiesForCluster(cluster, org.apache.ambari.server.upgrade.UpgradeCatalog260.HIVE_INTERACTIVE_SITE, newProperties, true, false);
                                    org.apache.ambari.server.upgrade.UpgradeCatalog260.LOG.info(((("Updated HSI config(s) : " + newProperties.keySet()) + " with value(s) = ") + newProperties.values()) + " respectively.");
                                } catch (org.apache.ambari.server.AmbariException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected void updateAmsConfigs() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = ambariManagementController.getClusters();
        if (clusters != null) {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = getCheckedClusterMap(clusters);
            if ((clusterMap != null) && (!clusterMap.isEmpty())) {
                for (final org.apache.ambari.server.state.Cluster cluster : clusterMap.values()) {
                    org.apache.ambari.server.state.Config amsSslClient = cluster.getDesiredConfigByType(org.apache.ambari.server.upgrade.UpgradeCatalog260.AMS_SSL_CLIENT);
                    if (amsSslClient != null) {
                        java.util.Map<java.lang.String, java.lang.String> amsSslClientProperties = amsSslClient.getProperties();
                        if (amsSslClientProperties.containsKey(org.apache.ambari.server.upgrade.UpgradeCatalog260.METRIC_TRUSTSTORE_ALIAS)) {
                            org.apache.ambari.server.upgrade.UpgradeCatalog260.LOG.info((("Removing " + org.apache.ambari.server.upgrade.UpgradeCatalog260.METRIC_TRUSTSTORE_ALIAS) + " from ") + org.apache.ambari.server.upgrade.UpgradeCatalog260.AMS_SSL_CLIENT);
                            removeConfigurationPropertiesFromCluster(cluster, org.apache.ambari.server.upgrade.UpgradeCatalog260.AMS_SSL_CLIENT, java.util.Collections.singleton(org.apache.ambari.server.upgrade.UpgradeCatalog260.METRIC_TRUSTSTORE_ALIAS));
                        }
                    }
                }
            }
        }
    }

    protected void updateHDFSWidgetDefinition() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.upgrade.UpgradeCatalog260.LOG.info("Updating HDFS widget definition.");
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> widgetMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> sectionLayoutMap = new java.util.HashMap<>();
        java.util.List<java.lang.String> hdfsHeatmapWidgets = new java.util.ArrayList<>(java.util.Arrays.asList("HDFS Bytes Read", "HDFS Bytes Written", "DataNode Process Disk I/O Utilization", "DataNode Process Network I/O Utilization"));
        widgetMap.put("HDFS_HEATMAPS", hdfsHeatmapWidgets);
        sectionLayoutMap.put("HDFS_HEATMAPS", "default_hdfs_heatmap");
        updateWidgetDefinitionsForService("HDFS", widgetMap, sectionLayoutMap);
    }

    private org.apache.ambari.server.state.Cluster getCluster(org.apache.ambari.server.orm.entities.ArtifactEntity artifactEntity) {
        if (artifactEntity != null) {
            java.util.Map<java.lang.String, java.lang.String> keys = artifactEntity.getForeignKeys();
            if (keys != null) {
                java.lang.String clusterId = keys.get("cluster");
                if (org.apache.commons.lang.StringUtils.isNumeric(clusterId)) {
                    org.apache.ambari.server.state.Clusters clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
                    try {
                        return clusters.getCluster(java.lang.Long.valueOf(clusterId));
                    } catch (org.apache.ambari.server.AmbariException e) {
                        org.apache.ambari.server.upgrade.UpgradeCatalog260.LOG.error(java.lang.String.format("Failed to obtain cluster using cluster id %s -  %s", clusterId, e.getMessage()), e);
                    }
                } else {
                    org.apache.ambari.server.upgrade.UpgradeCatalog260.LOG.error(java.lang.String.format("Failed to obtain cluster id from artifact entity with foreign keys: %s", keys));
                }
            }
        }
        return null;
    }

    private void fixIdentityReferences(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer container) throws org.apache.ambari.server.AmbariException {
        java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> identities = container.getIdentities();
        if (identities != null) {
            for (org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identity : identities) {
                java.lang.String name = identity.getName();
                if ((!org.apache.commons.lang.StringUtils.isEmpty(name)) && (name.startsWith("/") || name.startsWith("./"))) {
                    java.lang.String[] parts = name.split("/");
                    java.lang.String newName = buildName(identity.getParent(), parts[parts.length - 1]);
                    identity.setName(newName);
                    identity.setReference(name);
                }
                java.lang.String identityReference = identity.getReference();
                if (!org.apache.commons.lang.StringUtils.isEmpty(identityReference)) {
                    org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principal = identity.getPrincipalDescriptor();
                    if (principal != null) {
                        principal.setValue(null);
                        if (!org.apache.commons.lang.StringUtils.isEmpty(principal.getConfiguration())) {
                            java.lang.String referencedPrincipalName = getConfiguredPrincipalNameFromReference(cluster, container, identityReference);
                            if (!org.apache.commons.lang.StringUtils.isEmpty(referencedPrincipalName)) {
                                java.lang.String[] parts = principal.getConfiguration().split("/");
                                if (parts.length == 2) {
                                    java.lang.String type = parts[0];
                                    java.lang.String property = parts[1];
                                    updateConfigurationPropertiesForCluster(cluster, type, java.util.Collections.singletonMap(property, referencedPrincipalName), true, false);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (container instanceof org.apache.ambari.server.state.kerberos.KerberosDescriptor) {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor> services = ((org.apache.ambari.server.state.kerberos.KerberosDescriptor) (container)).getServices();
            if (services != null) {
                for (org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor : services.values()) {
                    fixIdentityReferences(cluster, serviceDescriptor);
                }
            }
        } else if (container instanceof org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor) {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor> components = ((org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor) (container)).getComponents();
            if (components != null) {
                for (org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor componentDescriptor : components.values()) {
                    fixIdentityReferences(cluster, componentDescriptor);
                }
            }
        }
    }

    private java.lang.String getConfiguredPrincipalNameFromReference(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer container, java.lang.String identityReference) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptor = container.getReferencedIdentityDescriptor(identityReference);
        if (identityDescriptor != null) {
            org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principal = identityDescriptor.getPrincipalDescriptor();
            if ((principal != null) && (!org.apache.commons.lang.StringUtils.isEmpty(principal.getConfiguration()))) {
                java.lang.String[] parts = principal.getConfiguration().split("/");
                if (parts.length == 2) {
                    java.lang.String type = parts[0];
                    java.lang.String property = parts[1];
                    org.apache.ambari.server.state.Config config = cluster.getDesiredConfigByType(type);
                    if (config != null) {
                        return config.getProperties().get(property);
                    }
                }
            }
        }
        return null;
    }

    private java.lang.String buildName(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptor container, java.lang.String identityName) {
        if (container instanceof org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor) {
            return (container.getName().toLowerCase() + "_") + identityName;
        } else if (container instanceof org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor) {
            return (((container.getParent().getName().toLowerCase() + "_") + container.getName().toLowerCase()) + "_") + identityName;
        } else {
            return identityName;
        }
    }

    protected void updateExistingRepositoriesToBeResolved() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO = injector.getInstance(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        java.util.List<org.apache.ambari.server.orm.entities.RepositoryVersionEntity> repositoryVersions = repositoryVersionDAO.findAll();
        for (org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion : repositoryVersions) {
            repositoryVersion.setResolved(true);
            repositoryVersionDAO.merge(repositoryVersion);
        }
    }
}