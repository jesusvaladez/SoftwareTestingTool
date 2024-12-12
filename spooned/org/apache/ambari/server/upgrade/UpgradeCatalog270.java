package org.apache.ambari.server.upgrade;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
public class UpgradeCatalog270 extends org.apache.ambari.server.upgrade.AbstractUpgradeCatalog {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.upgrade.UpgradeCatalog270.class);

    protected static final java.lang.String STAGE_TABLE = "stage";

    protected static final java.lang.String STAGE_STATUS_COLUMN = "status";

    protected static final java.lang.String STAGE_DISPLAY_STATUS_COLUMN = "display_status";

    protected static final java.lang.String REQUEST_TABLE = "request";

    protected static final java.lang.String REQUEST_DISPLAY_STATUS_COLUMN = "display_status";

    protected static final java.lang.String REQUEST_USER_NAME_COLUMN = "user_name";

    protected static final java.lang.String HOST_ROLE_COMMAND_TABLE = "host_role_command";

    protected static final java.lang.String HRC_OPS_DISPLAY_NAME_COLUMN = "ops_display_name";

    protected static final java.lang.String COMPONENT_DESIRED_STATE_TABLE = "hostcomponentdesiredstate";

    protected static final java.lang.String COMPONENT_STATE_TABLE = "hostcomponentstate";

    protected static final java.lang.String COMPONENT_LAST_STATE_COLUMN = "last_live_state";

    protected static final java.lang.String SERVICE_DESIRED_STATE_TABLE = "servicedesiredstate";

    protected static final java.lang.String SECURITY_STATE_COLUMN = "security_state";

    protected static final java.lang.String AMBARI_SEQUENCES_TABLE = "ambari_sequences";

    protected static final java.lang.String AMBARI_SEQUENCES_SEQUENCE_NAME_COLUMN = "sequence_name";

    protected static final java.lang.String AMBARI_SEQUENCES_SEQUENCE_VALUE_COLUMN = "sequence_value";

    protected static final java.lang.String AMBARI_CONFIGURATION_TABLE = "ambari_configuration";

    protected static final java.lang.String AMBARI_CONFIGURATION_CATEGORY_NAME_COLUMN = "category_name";

    protected static final java.lang.String AMBARI_CONFIGURATION_PROPERTY_NAME_COLUMN = "property_name";

    protected static final java.lang.String AMBARI_CONFIGURATION_PROPERTY_VALUE_COLUMN = "property_value";

    protected static final java.lang.String USER_AUTHENTICATION_TABLE = "user_authentication";

    protected static final java.lang.String USER_AUTHENTICATION_USER_AUTHENTICATION_ID_COLUMN = "user_authentication_id";

    protected static final java.lang.String USER_AUTHENTICATION_USER_ID_COLUMN = "user_id";

    protected static final java.lang.String USER_AUTHENTICATION_AUTHENTICATION_TYPE_COLUMN = "authentication_type";

    protected static final java.lang.String USER_AUTHENTICATION_AUTHENTICATION_KEY_COLUMN = "authentication_key";

    protected static final java.lang.String USER_AUTHENTICATION_CREATE_TIME_COLUMN = "create_time";

    protected static final java.lang.String USER_AUTHENTICATION_UPDATE_TIME_COLUMN = "update_time";

    protected static final java.lang.String USER_AUTHENTICATION_PRIMARY_KEY = "PK_user_authentication";

    protected static final java.lang.String USER_AUTHENTICATION_USER_AUTHENTICATION_USER_ID_INDEX = "IDX_user_authentication_user_id";

    protected static final java.lang.String USER_AUTHENTICATION_USER_AUTHENTICATION_USERS_FOREIGN_KEY = "FK_user_authentication_users";

    protected static final java.lang.String USERS_TABLE = "users";

    protected static final java.lang.String USERS_USER_ID_COLUMN = "user_id";

    protected static final java.lang.String USERS_PRINCIPAL_ID_COLUMN = "principal_id";

    protected static final java.lang.String USERS_USER_TYPE_COLUMN = "user_type";

    protected static final java.lang.String USERS_USER_PASSWORD_COLUMN = "user_password";

    protected static final java.lang.String USERS_CREATE_TIME_COLUMN = "create_time";

    protected static final java.lang.String USERS_LDAP_USER_COLUMN = "ldap_user";

    protected static final java.lang.String USERS_CONSECUTIVE_FAILURES_COLUMN = "consecutive_failures";

    protected static final java.lang.String USERS_USER_NAME_COLUMN = "user_name";

    protected static final java.lang.String USERS_DISPLAY_NAME_COLUMN = "display_name";

    protected static final java.lang.String USERS_LOCAL_USERNAME_COLUMN = "local_username";

    protected static final java.lang.String USERS_VERSION_COLUMN = "version";

    protected static final java.lang.String UNIQUE_USERS_0_INDEX = "UNQ_users_0";

    protected static final java.lang.String MEMBERS_TABLE = "members";

    protected static final java.lang.String MEMBERS_MEMBER_ID_COLUMN = "member_id";

    protected static final java.lang.String MEMBERS_GROUP_ID_COLUMN = "group_id";

    protected static final java.lang.String MEMBERS_USER_ID_COLUMN = "user_id";

    protected static final java.lang.String ADMINPRIVILEGE_TABLE = "adminprivilege";

    protected static final java.lang.String ADMINPRIVILEGE_PRIVILEGE_ID_COLUMN = "privilege_id";

    protected static final java.lang.String ADMINPRIVILEGE_PERMISSION_ID_COLUMN = "permission_id";

    protected static final java.lang.String ADMINPRIVILEGE_RESOURCE_ID_COLUMN = "resource_id";

    protected static final java.lang.String ADMINPRIVILEGE_PRINCIPAL_ID_COLUMN = "principal_id";

    protected static final java.lang.String KERBEROS_KEYTAB_TABLE = "kerberos_keytab";

    protected static final java.lang.String KERBEROS_KEYTAB_PRINCIPAL_TABLE = "kerberos_keytab_principal";

    protected static final java.lang.String KKP_MAPPING_SERVICE_TABLE = "kkp_mapping_service";

    protected static final java.lang.String KEYTAB_PATH_FIELD = "keytab_path";

    protected static final java.lang.String OWNER_NAME_FIELD = "owner_name";

    protected static final java.lang.String OWNER_ACCESS_FIELD = "owner_access";

    protected static final java.lang.String GROUP_NAME_FIELD = "group_name";

    protected static final java.lang.String GROUP_ACCESS_FIELD = "group_access";

    protected static final java.lang.String IS_AMBARI_KEYTAB_FIELD = "is_ambari_keytab";

    protected static final java.lang.String WRITE_AMBARI_JAAS_FIELD = "write_ambari_jaas";

    protected static final java.lang.String PK_KERBEROS_KEYTAB = "PK_kerberos_keytab";

    protected static final java.lang.String KKP_ID_COLUMN = "kkp_id";

    protected static final java.lang.String PRINCIPAL_NAME_COLUMN = "principal_name";

    protected static final java.lang.String IS_DISTRIBUTED_COLUMN = "is_distributed";

    protected static final java.lang.String PK_KKP = "PK_kkp";

    protected static final java.lang.String UNI_KKP = "UNI_kkp";

    protected static final java.lang.String SERVICE_NAME_COLUMN = "service_name";

    protected static final java.lang.String COMPONENT_NAME_COLUMN = "component_name";

    protected static final java.lang.String PK_KKP_MAPPING_SERVICE = "PK_kkp_mapping_service";

    protected static final java.lang.String FK_KKP_KEYTAB_PATH = "FK_kkp_keytab_path";

    protected static final java.lang.String FK_KKP_HOST_ID = "FK_kkp_host_id";

    protected static final java.lang.String FK_KKP_PRINCIPAL_NAME = "FK_kkp_principal_name";

    protected static final java.lang.String HOSTS_TABLE = "hosts";

    protected static final java.lang.String KERBEROS_PRINCIPAL_TABLE = "kerberos_principal";

    protected static final java.lang.String FK_KKP_SERVICE_PRINCIPAL = "FK_kkp_service_principal";

    protected static final java.lang.String KKP_ID_SEQ_NAME = "kkp_id_seq";

    protected static final java.lang.String KERBEROS_PRINCIPAL_HOST_TABLE = "kerberos_principal_host";

    protected static final java.lang.String HOST_ID_COLUMN = "host_id";

    protected static final java.lang.String REPO_OS_TABLE = "repo_os";

    protected static final java.lang.String REPO_OS_ID_COLUMN = "id";

    protected static final java.lang.String REPO_OS_REPO_VERSION_ID_COLUMN = "repo_version_id";

    protected static final java.lang.String REPO_OS_FAMILY_COLUMN = "family";

    protected static final java.lang.String REPO_OS_AMBARI_MANAGED_COLUMN = "ambari_managed";

    protected static final java.lang.String REPO_OS_PRIMARY_KEY = "PK_repo_os_id";

    protected static final java.lang.String REPO_OS_FOREIGN_KEY = "FK_repo_os_id_repo_version_id";

    protected static final java.lang.String REPO_DEFINITION_TABLE = "repo_definition";

    protected static final java.lang.String REPO_DEFINITION_ID_COLUMN = "id";

    protected static final java.lang.String REPO_DEFINITION_REPO_OS_ID_COLUMN = "repo_os_id";

    protected static final java.lang.String REPO_DEFINITION_REPO_NAME_COLUMN = "repo_name";

    protected static final java.lang.String REPO_DEFINITION_REPO_ID_COLUMN = "repo_id";

    protected static final java.lang.String REPO_DEFINITION_BASE_URL_COLUMN = "base_url";

    protected static final java.lang.String REPO_DEFINITION_DISTRIBUTION_COLUMN = "distribution";

    protected static final java.lang.String REPO_DEFINITION_COMPONENTS_COLUMN = "components";

    protected static final java.lang.String REPO_DEFINITION_UNIQUE_REPO_COLUMN = "unique_repo";

    protected static final java.lang.String REPO_DEFINITION_MIRRORS_COLUMN = "mirrors";

    protected static final java.lang.String REPO_DEFINITION_PRIMARY_KEY = "PK_repo_definition_id";

    protected static final java.lang.String REPO_DEFINITION_FOREIGN_KEY = "FK_repo_definition_repo_os_id";

    protected static final java.lang.String REPO_TAGS_TABLE = "repo_tags";

    protected static final java.lang.String REPO_TAGS_REPO_DEFINITION_ID_COLUMN = "repo_definition_id";

    protected static final java.lang.String REPO_TAGS_TAG_COLUMN = "tag";

    protected static final java.lang.String REPO_TAGS_FOREIGN_KEY = "FK_repo_tag_definition_id";

    protected static final java.lang.String REPO_APPLICABLE_SERVICES_TABLE = "repo_applicable_services";

    protected static final java.lang.String REPO_APPLICABLE_SERVICES_REPO_DEFINITION_ID_COLUMN = "repo_definition_id";

    protected static final java.lang.String REPO_APPLICABLE_SERVICES_SERVICE_NAME_COLUMN = "service_name";

    protected static final java.lang.String REPO_APPLICABLE_SERVICES_FOREIGN_KEY = "FK_repo_app_service_def_id";

    protected static final java.lang.String REPO_VERSION_TABLE = "repo_version";

    protected static final java.lang.String REPO_VERSION_REPO_VERSION_ID_COLUMN = "repo_version_id";

    protected static final java.lang.String REPO_VERSION_REPOSITORIES_COLUMN = "repositories";

    protected static final java.lang.String WIDGET_TABLE = "widget";

    protected static final java.lang.String WIDGET_TAG_COLUMN = "tag";

    protected static final java.lang.String SERVICE_COMPONENT_DESIRED_STATE_TABLE = "servicecomponentdesiredstate";

    protected static final java.lang.String HIVE_SERVICE_COMPONENT_WEBHCAT_SERVER = "WEBHCAT_SERVER";

    protected static final java.lang.String CONFIGURATION_CORE_SITE = "core-site";

    protected static final java.lang.String CONFIGURATION_WEBHCAT_SITE = "webhcat-site";

    protected static final java.lang.String PROPERTY_HADOOP_PROXYUSER_HTTP_HOSTS = "hadoop.proxyuser.HTTP.hosts";

    protected static final java.lang.String PROPERTY_TEMPLETON_HIVE_PROPERTIES = "templeton.hive.properties";

    public static final java.lang.String AMBARI_INFRA_OLD_NAME = "AMBARI_INFRA";

    public static final java.lang.String AMBARI_INFRA_NEW_NAME = "AMBARI_INFRA_SOLR";

    public static final java.lang.String SERVICE_CONFIG_MAPPING_TABLE = "serviceconfigmapping";

    public static final java.lang.String CLUSTER_CONFIG_TABLE = "clusterconfig";

    public static final java.lang.String FK_HOSTCOMPONENTDESIREDSTATE_COMPONENT_NAME = "fk_hostcomponentdesiredstate_component_name";

    public static final java.lang.String FK_HOSTCOMPONENTSTATE_COMPONENT_NAME = "fk_hostcomponentstate_component_name";

    public static final java.lang.String FK_SERVICECOMPONENTDESIREDSTATE_SERVICE_NAME = "fk_servicecomponentdesiredstate_service_name";

    static final java.lang.String YARN_SERVICE = "YARN";

    @com.google.inject.Inject
    org.apache.ambari.server.orm.dao.DaoUtils daoUtils;

    @com.google.inject.Inject
    public UpgradeCatalog270(com.google.inject.Injector injector) {
        super(injector);
        daoUtils = injector.getInstance(org.apache.ambari.server.orm.dao.DaoUtils.class);
    }

    @java.lang.Override
    public java.lang.String getTargetVersion() {
        return "2.7.0";
    }

    @java.lang.Override
    public java.lang.String getSourceVersion() {
        return "2.6.2";
    }

    @java.lang.Override
    protected void executeDDLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        dropBrokenFKs();
        updateStageTable();
        updateRequestTable();
        addOpsDisplayNameColumnToHostRoleCommand();
        removeSecurityState();
        addAmbariConfigurationTable();
        addHostComponentLastStateTable();
        upgradeUserTables();
        upgradeKerberosTables();
        upgradeRepoTables();
        upgradeWidgetTable();
    }

    protected void upgradeUserTables() throws java.sql.SQLException {
        convertUserCreationTimeToLong();
        createUserAuthenticationTable();
        updateGroupMembershipRecords();
        updateAdminPrivilegeRecords();
        updateUsersTable();
    }

    protected void upgradeRepoTables() throws java.sql.SQLException {
        createRepoOsTable();
        createRepoDefinitionTable();
        createRepoTagsTable();
        createRepoApplicableServicesTable();
        migrateRepoData();
        updateRepoVersionTable();
    }

    private void createRepoOsTable() throws java.sql.SQLException {
        java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> columns = new java.util.ArrayList<>();
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_ID_COLUMN, java.lang.Long.class, null, null, false));
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_REPO_VERSION_ID_COLUMN, java.lang.Long.class, null, null, false));
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_FAMILY_COLUMN, java.lang.String.class, 255, null, false));
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_AMBARI_MANAGED_COLUMN, java.lang.Integer.class, null, 1, true));
        dbAccessor.createTable(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_TABLE, columns);
        dbAccessor.addPKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_PRIMARY_KEY, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_ID_COLUMN);
        dbAccessor.addFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_FOREIGN_KEY, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_REPO_VERSION_ID_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_VERSION_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_VERSION_REPO_VERSION_ID_COLUMN, false);
    }

    private void createRepoDefinitionTable() throws java.sql.SQLException {
        java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> columns = new java.util.ArrayList<>();
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_ID_COLUMN, java.lang.Long.class, null, null, false));
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_REPO_OS_ID_COLUMN, java.lang.Long.class, null, null, false));
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_REPO_NAME_COLUMN, java.lang.String.class, 255, null, false));
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_REPO_ID_COLUMN, java.lang.String.class, 255, null, false));
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_BASE_URL_COLUMN, java.lang.String.class, 2048, null, true));
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_DISTRIBUTION_COLUMN, java.lang.String.class, 2048, null, true));
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_COMPONENTS_COLUMN, java.lang.String.class, 2048, null, true));
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_UNIQUE_REPO_COLUMN, java.lang.Integer.class, 1, 1, true));
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_MIRRORS_COLUMN, java.lang.String.class, 2048, null, true));
        dbAccessor.createTable(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_TABLE, columns);
        dbAccessor.addPKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_PRIMARY_KEY, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_ID_COLUMN);
        dbAccessor.addFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_FOREIGN_KEY, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_REPO_OS_ID_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_ID_COLUMN, false);
    }

    private void createRepoTagsTable() throws java.sql.SQLException {
        java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> columns = new java.util.ArrayList<>();
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_TAGS_REPO_DEFINITION_ID_COLUMN, java.lang.Long.class, null, null, false));
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_TAGS_TAG_COLUMN, java.lang.String.class, 255, null, false));
        dbAccessor.createTable(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_TAGS_TABLE, columns);
        dbAccessor.addFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_TAGS_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_TAGS_FOREIGN_KEY, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_TAGS_REPO_DEFINITION_ID_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_ID_COLUMN, false);
    }

    private void createRepoApplicableServicesTable() throws java.sql.SQLException {
        java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> columns = new java.util.ArrayList<>();
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_APPLICABLE_SERVICES_REPO_DEFINITION_ID_COLUMN, java.lang.Long.class, null, null, false));
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_APPLICABLE_SERVICES_SERVICE_NAME_COLUMN, java.lang.String.class, 255, null, false));
        dbAccessor.createTable(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_APPLICABLE_SERVICES_TABLE, columns);
        dbAccessor.addFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_APPLICABLE_SERVICES_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_APPLICABLE_SERVICES_FOREIGN_KEY, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_APPLICABLE_SERVICES_REPO_DEFINITION_ID_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_ID_COLUMN, false);
    }

    private void migrateRepoData() throws java.sql.SQLException {
        if (dbAccessor.tableHasColumn(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_VERSION_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_VERSION_REPOSITORIES_COLUMN)) {
            int repoOsId = 0;
            int repoDefinitionId = 0;
            java.util.Map<java.lang.Long, java.lang.String> repoVersionData = dbAccessor.getKeyToStringColumnMap(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_VERSION_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_VERSION_REPO_VERSION_ID_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_VERSION_REPOSITORIES_COLUMN, null, null, true);
            if (repoVersionData != null) {
                for (java.util.Map.Entry<java.lang.Long, java.lang.String> entry : repoVersionData.entrySet()) {
                    java.lang.Long repoVersionId = entry.getKey();
                    java.lang.String repositoriesJson = entry.getValue();
                    if (!org.apache.commons.lang.StringUtils.isEmpty(repositoriesJson)) {
                        com.google.gson.JsonArray rootJson = new com.google.gson.JsonParser().parse(repositoriesJson).getAsJsonArray();
                        if (rootJson != null) {
                            for (com.google.gson.JsonElement rootElement : rootJson) {
                                com.google.gson.JsonObject rootObject = rootElement.getAsJsonObject();
                                if (rootObject != null) {
                                    com.google.gson.JsonPrimitive osType = rootObject.getAsJsonPrimitive("OperatingSystems/os_type");
                                    com.google.gson.JsonPrimitive ambariManaged = rootObject.getAsJsonPrimitive("OperatingSystems/ambari_managed_repositories");
                                    java.lang.String isAmbariManaged = (ambariManaged == null) ? "1" : ambariManaged.getAsBoolean() ? "1" : "0";
                                    com.google.gson.JsonArray repositories = rootObject.getAsJsonArray("repositories");
                                    dbAccessor.insertRowIfMissing(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_TABLE, new java.lang.String[]{ org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_ID_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_REPO_VERSION_ID_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_AMBARI_MANAGED_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_OS_FAMILY_COLUMN }, new java.lang.String[]{ java.lang.String.valueOf(++repoOsId), java.lang.String.valueOf(repoVersionId), isAmbariManaged, getFormattedJSONPrimitiveString(osType) }, false);
                                    if (repositories != null) {
                                        for (com.google.gson.JsonElement repositoryElement : repositories) {
                                            com.google.gson.JsonObject repositoryObject = repositoryElement.getAsJsonObject();
                                            if (repositoryObject != null) {
                                                com.google.gson.JsonPrimitive repoId = repositoryObject.getAsJsonPrimitive("Repositories/repo_id");
                                                com.google.gson.JsonPrimitive repoName = repositoryObject.getAsJsonPrimitive("Repositories/repo_name");
                                                com.google.gson.JsonPrimitive baseUrl = repositoryObject.getAsJsonPrimitive("Repositories/base_url");
                                                com.google.gson.JsonArray tags = repositoryObject.getAsJsonArray("Repositories/tags");
                                                dbAccessor.insertRowIfMissing(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_TABLE, new java.lang.String[]{ org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_ID_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_REPO_OS_ID_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_REPO_NAME_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_REPO_ID_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_DEFINITION_BASE_URL_COLUMN }, new java.lang.String[]{ java.lang.String.valueOf(++repoDefinitionId), java.lang.String.valueOf(repoOsId), getFormattedJSONPrimitiveString(repoName), getFormattedJSONPrimitiveString(repoId), getFormattedJSONPrimitiveString(baseUrl) }, false);
                                                if (tags != null) {
                                                    for (com.google.gson.JsonElement tagsElement : tags) {
                                                        com.google.gson.JsonPrimitive tag = tagsElement.getAsJsonPrimitive();
                                                        if (tag != null) {
                                                            dbAccessor.insertRowIfMissing(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_TAGS_TABLE, new java.lang.String[]{ org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_TAGS_REPO_DEFINITION_ID_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_TAGS_TAG_COLUMN }, new java.lang.String[]{ java.lang.String.valueOf(repoDefinitionId), getFormattedJSONPrimitiveString(tag) }, false);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            dbAccessor.insertRowIfMissing(org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_SEQUENCES_TABLE, new java.lang.String[]{ org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_SEQUENCES_SEQUENCE_NAME_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_SEQUENCES_SEQUENCE_VALUE_COLUMN }, new java.lang.String[]{ "'repo_os_id_seq'", java.lang.String.valueOf(++repoOsId) }, false);
            dbAccessor.insertRowIfMissing(org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_SEQUENCES_TABLE, new java.lang.String[]{ org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_SEQUENCES_SEQUENCE_NAME_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_SEQUENCES_SEQUENCE_VALUE_COLUMN }, new java.lang.String[]{ "'repo_definition_id_seq'", java.lang.String.valueOf(++repoDefinitionId) }, false);
        }
    }

    private java.lang.String getFormattedJSONPrimitiveString(com.google.gson.JsonPrimitive jsonValue) {
        return jsonValue == null ? null : java.lang.String.format("'%s'", jsonValue.getAsString());
    }

    private void updateRepoVersionTable() throws java.sql.SQLException {
        dbAccessor.dropColumn(org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_VERSION_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.REPO_VERSION_REPOSITORIES_COLUMN);
    }

    private void convertUserCreationTimeToLong() throws java.sql.SQLException {
        if (!isUserCreationTimeMigrated()) {
            org.apache.ambari.server.upgrade.UpgradeCatalog270.LOG.info("Converting user creation times...");
            final java.lang.String temporaryColumnName = org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_CREATE_TIME_COLUMN + "_numeric";
            if (!dbAccessor.tableHasColumn(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE, temporaryColumnName)) {
                final org.apache.ambari.server.orm.DBAccessor.DBColumnInfo tempColumnInfo = new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(temporaryColumnName, java.lang.Long.class);
                dbAccessor.addColumn(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE, tempColumnInfo);
            }
            if (dbAccessor.tableHasColumn(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_CREATE_TIME_COLUMN)) {
                final java.util.Map<java.lang.Integer, java.sql.Timestamp> currentUserCreateTimes = fetchCurrentUserCreateTimesNotYetMigrated(temporaryColumnName);
                for (java.util.Map.Entry<java.lang.Integer, java.sql.Timestamp> currentUserCreateTime : currentUserCreateTimes.entrySet()) {
                    dbAccessor.updateTable(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE, temporaryColumnName, currentUserCreateTime.getValue().getTime(), (("WHERE " + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_ID_COLUMN) + "=") + currentUserCreateTime.getKey());
                }
                dbAccessor.dropColumn(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_CREATE_TIME_COLUMN);
            }
            final org.apache.ambari.server.orm.DBAccessor.DBColumnInfo usersCreateTimeColumnInfo = new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_CREATE_TIME_COLUMN, java.lang.Long.class, null, null, false);
            dbAccessor.renameColumn(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE, temporaryColumnName, usersCreateTimeColumnInfo);
            org.apache.ambari.server.upgrade.UpgradeCatalog270.LOG.info("Converted user creation times");
        } else {
            org.apache.ambari.server.upgrade.UpgradeCatalog270.LOG.info("Already converted user creation timestamps to EPOCH representation");
        }
    }

    private boolean isUserCreationTimeMigrated() throws java.sql.SQLException {
        final int columnType = dbAccessor.getColumnType(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_CREATE_TIME_COLUMN);
        org.apache.ambari.server.upgrade.UpgradeCatalog270.LOG.info((((org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE + ".") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_CREATE_TIME_COLUMN) + "'s type = ") + columnType);
        return (columnType != java.sql.Types.DATE) && (columnType != java.sql.Types.TIMESTAMP);
    }

    private java.util.Map<java.lang.Integer, java.sql.Timestamp> fetchCurrentUserCreateTimesNotYetMigrated(java.lang.String temporaryColumnName) throws java.sql.SQLException {
        final java.util.Map<java.lang.Integer, java.sql.Timestamp> currentUserCreateTimes = new java.util.HashMap<>();
        try (java.sql.PreparedStatement pstmt = dbAccessor.getConnection().prepareStatement((((((((("SELECT " + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_ID_COLUMN) + ", ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_CREATE_TIME_COLUMN) + " FROM ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE) + " WHERE ") + temporaryColumnName) + " IS NULL ORDER BY ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_ID_COLUMN);java.sql.ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                currentUserCreateTimes.put(rs.getInt(1), rs.getTimestamp(2) == null ? new java.sql.Timestamp(java.lang.System.currentTimeMillis()) : rs.getTimestamp(2));
            } 
        }
        return currentUserCreateTimes;
    }

    private void createUserAuthenticationTable() throws java.sql.SQLException {
        if (!usersTableUpgraded()) {
            final java.lang.String temporaryTable = org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_TABLE + "_tmp";
            java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> columns = new java.util.ArrayList<>();
            columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_USER_AUTHENTICATION_ID_COLUMN, java.lang.Long.class, null, null, false));
            columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_USER_ID_COLUMN, java.lang.Integer.class, null, null, false));
            columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_AUTHENTICATION_TYPE_COLUMN, java.lang.String.class, 50, null, false));
            columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_AUTHENTICATION_KEY_COLUMN, java.lang.String.class, 2048, null, true));
            columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_CREATE_TIME_COLUMN, java.lang.Long.class, null, null, true));
            columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_UPDATE_TIME_COLUMN, java.lang.Long.class, null, null, true));
            dbAccessor.dropTable(temporaryTable);
            dbAccessor.createTable(temporaryTable, columns);
            dbAccessor.executeUpdate(((((((((((((((((((((((((((((((((((((((((((((((((((("insert into " + temporaryTable) + "(") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_USER_AUTHENTICATION_ID_COLUMN) + ", ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_USER_ID_COLUMN) + ", ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_AUTHENTICATION_TYPE_COLUMN) + ", ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_AUTHENTICATION_KEY_COLUMN) + ", ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_CREATE_TIME_COLUMN) + ", ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_UPDATE_TIME_COLUMN) + ")") + " select distinct") + "  u.") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_ID_COLUMN) + ",") + "  t.min_user_id,") + "  u.") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_TYPE_COLUMN) + ",") + "  u.") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_PASSWORD_COLUMN) + ",") + "  u.") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_CREATE_TIME_COLUMN) + ",") + "  u.") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_CREATE_TIME_COLUMN) + " from ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE) + " u inner join") + "   (select") + "     lower(") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_NAME_COLUMN) + ") as ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_NAME_COLUMN) + ",") + "     min(") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_ID_COLUMN) + ") as min_user_id") + "    from ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE) + "    group by lower(") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_NAME_COLUMN) + ")) t") + " on (lower(u.") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_NAME_COLUMN) + ") = lower(t.") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_NAME_COLUMN) + "))");
            dbAccessor.executeUpdate((((((((("update " + temporaryTable) + " set ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_AUTHENTICATION_KEY_COLUMN) + "=null") + " where ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_AUTHENTICATION_TYPE_COLUMN) + "!='") + org.apache.ambari.server.security.authorization.UserAuthenticationType.LOCAL.name()) + "'");
            dbAccessor.createTable(org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_TABLE, columns);
            dbAccessor.addPKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_PRIMARY_KEY, org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_USER_AUTHENTICATION_ID_COLUMN);
            dbAccessor.addFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_USER_AUTHENTICATION_USERS_FOREIGN_KEY, org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_USER_ID_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_ID_COLUMN, false);
            dbAccessor.executeUpdate(((((((((((((((((((((((((((("insert into " + org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_TABLE) + "(") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_USER_AUTHENTICATION_ID_COLUMN) + ", ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_USER_ID_COLUMN) + ", ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_AUTHENTICATION_TYPE_COLUMN) + ", ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_AUTHENTICATION_KEY_COLUMN) + ", ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_CREATE_TIME_COLUMN) + ", ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_UPDATE_TIME_COLUMN) + ")") + " select ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_USER_AUTHENTICATION_ID_COLUMN) + ", ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_USER_ID_COLUMN) + ", ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_AUTHENTICATION_TYPE_COLUMN) + ", ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_AUTHENTICATION_KEY_COLUMN) + ", ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_CREATE_TIME_COLUMN) + ", ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_UPDATE_TIME_COLUMN) + " from ") + temporaryTable);
            dbAccessor.dropTable(temporaryTable);
        }
    }

    private boolean usersTableUpgraded() {
        try {
            dbAccessor.getColumnType(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_TYPE_COLUMN);
            return false;
        } catch (java.sql.SQLException e) {
            return true;
        }
    }

    private void updateUsersTable() throws java.sql.SQLException {
        dbAccessor.executeUpdate(((((((("delete from " + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE) + " where ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_ID_COLUMN) + " not in (select ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_USER_ID_COLUMN) + " from ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_TABLE) + ")");
        dbAccessor.dropUniqueConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.UNIQUE_USERS_0_INDEX);
        dbAccessor.dropColumn(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_TYPE_COLUMN);
        dbAccessor.dropColumn(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_LDAP_USER_COLUMN);
        dbAccessor.dropColumn(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_PASSWORD_COLUMN);
        dbAccessor.addColumn(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_CONSECUTIVE_FAILURES_COLUMN, java.lang.Integer.class, null, 0, false));
        dbAccessor.addColumn(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_DISPLAY_NAME_COLUMN, java.lang.String.class, 255, null, true));
        dbAccessor.addColumn(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_LOCAL_USERNAME_COLUMN, java.lang.String.class, 255, null, true));
        dbAccessor.addColumn(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_VERSION_COLUMN, java.lang.Long.class, null, 0, false));
        dbAccessor.executeUpdate((((((((((((((("update " + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE) + " set ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_DISPLAY_NAME_COLUMN) + "=") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_NAME_COLUMN) + ", ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_LOCAL_USERNAME_COLUMN) + "= lower(") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_NAME_COLUMN) + ")") + ", ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_NAME_COLUMN) + "= lower(") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_NAME_COLUMN) + ")");
        dbAccessor.alterColumn(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_DISPLAY_NAME_COLUMN, java.lang.String.class, 255, null, false));
        dbAccessor.alterColumn(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_LOCAL_USERNAME_COLUMN, java.lang.String.class, 255, null, false));
        dbAccessor.addUniqueConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.UNIQUE_USERS_0_INDEX, org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_NAME_COLUMN);
    }

    private void updateGroupMembershipRecords() throws java.sql.SQLException {
        final java.lang.String temporaryTable = org.apache.ambari.server.upgrade.UpgradeCatalog270.MEMBERS_TABLE + "_tmp";
        dbAccessor.dropTable(temporaryTable);
        java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> columns = new java.util.ArrayList<>();
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.MEMBERS_MEMBER_ID_COLUMN, java.lang.Long.class, null, null, false));
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.MEMBERS_USER_ID_COLUMN, java.lang.Long.class, null, null, false));
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.MEMBERS_GROUP_ID_COLUMN, java.lang.Long.class, null, null, false));
        dbAccessor.createTable(temporaryTable, columns);
        dbAccessor.executeUpdate((((((((((((((((((((((((((((((((((((((((((((((((((((((("insert into " + temporaryTable) + " (") + org.apache.ambari.server.upgrade.UpgradeCatalog270.MEMBERS_MEMBER_ID_COLUMN) + ", ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.MEMBERS_USER_ID_COLUMN) + ", ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.MEMBERS_GROUP_ID_COLUMN) + ")") + "  select") + "    m.") + org.apache.ambari.server.upgrade.UpgradeCatalog270.MEMBERS_MEMBER_ID_COLUMN) + ",") + "    u.min_user_id,") + "    m.") + org.apache.ambari.server.upgrade.UpgradeCatalog270.MEMBERS_GROUP_ID_COLUMN) + "  from ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.MEMBERS_TABLE) + " m inner join") + "    (") + "      select") + "        iu.") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_NAME_COLUMN) + ",") + "        iu.") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_ID_COLUMN) + ",") + "        t.min_user_id") + "      from ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE) + " iu inner join") + "        (") + "          select") + "           lower(") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_NAME_COLUMN) + ") as ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_NAME_COLUMN) + ",") + "            min(") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_ID_COLUMN) + ") as min_user_id") + "          from ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE) + "          group by lower(") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_NAME_COLUMN) + ")") + "        ) t on (lower(t.") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_NAME_COLUMN) + ") = lower(iu.") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_NAME_COLUMN) + "))") + "    ) u on (m.") + org.apache.ambari.server.upgrade.UpgradeCatalog270.MEMBERS_USER_ID_COLUMN) + " = u.") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_ID_COLUMN) + ")");
        dbAccessor.truncateTable(org.apache.ambari.server.upgrade.UpgradeCatalog270.MEMBERS_TABLE);
        dbAccessor.executeUpdate((((((((((((((((((((((("insert into " + org.apache.ambari.server.upgrade.UpgradeCatalog270.MEMBERS_TABLE) + " (") + org.apache.ambari.server.upgrade.UpgradeCatalog270.MEMBERS_MEMBER_ID_COLUMN) + ", ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.MEMBERS_USER_ID_COLUMN) + ", ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.MEMBERS_GROUP_ID_COLUMN) + ")") + "  select ") + "    min(") + org.apache.ambari.server.upgrade.UpgradeCatalog270.MEMBERS_MEMBER_ID_COLUMN) + "),") + "    ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.MEMBERS_USER_ID_COLUMN) + ",") + "    ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.MEMBERS_GROUP_ID_COLUMN) + "  from ") + temporaryTable) + "  group by ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.MEMBERS_USER_ID_COLUMN) + ", ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.MEMBERS_GROUP_ID_COLUMN);
        dbAccessor.dropTable(temporaryTable);
    }

    private void updateAdminPrivilegeRecords() throws java.sql.SQLException {
        final java.lang.String temporaryTable = org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_TABLE + "_tmp";
        dbAccessor.dropTable(temporaryTable);
        java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> columns = new java.util.ArrayList<>();
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_PRIVILEGE_ID_COLUMN, java.lang.Long.class, null, null, false));
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_PERMISSION_ID_COLUMN, java.lang.Long.class, null, null, false));
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_RESOURCE_ID_COLUMN, java.lang.Long.class, null, null, false));
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_PRINCIPAL_ID_COLUMN, java.lang.Long.class, null, null, false));
        dbAccessor.createTable(temporaryTable, columns);
        dbAccessor.executeUpdate(((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((("insert into " + temporaryTable) + " (") + org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_PRIVILEGE_ID_COLUMN) + ", ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_PERMISSION_ID_COLUMN) + ", ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_RESOURCE_ID_COLUMN) + ", ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_PRINCIPAL_ID_COLUMN) + ")") + "  select") + "    ap.") + org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_PRIVILEGE_ID_COLUMN) + ",") + "    ap.") + org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_PERMISSION_ID_COLUMN) + ",") + "    ap.") + org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_RESOURCE_ID_COLUMN) + ",") + "    ap.") + org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_PRINCIPAL_ID_COLUMN) + "  from ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_TABLE) + " ap") + "  where ap.") + org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_PRINCIPAL_ID_COLUMN) + " not in") + "        (") + "          select ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_PRINCIPAL_ID_COLUMN) + "          from ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE) + "        )") + "  union") + "  select") + "    ap.") + org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_PRIVILEGE_ID_COLUMN) + ",") + "    ap.") + org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_PERMISSION_ID_COLUMN) + ",") + "    ap.") + org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_RESOURCE_ID_COLUMN) + ",") + "    t.new_principal_id") + "  from ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_TABLE) + " ap inner join") + "    (") + "      select") + "        u.") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_ID_COLUMN) + ",") + "        u.") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_NAME_COLUMN) + ",") + "        u.") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_PRINCIPAL_ID_COLUMN) + " as new_principal_id,") + "        t1.") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_PRINCIPAL_ID_COLUMN) + " as orig_principal_id") + "      from ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE) + " u inner join") + "        (") + "          select") + "            u1.") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_NAME_COLUMN) + ",") + "            u1.") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_PRINCIPAL_ID_COLUMN) + ",") + "            t2.min_user_id") + "          from ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE) + " u1 inner join") + "            (") + "              select") + "                lower(") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_NAME_COLUMN) + ") as ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_NAME_COLUMN) + ",") + "                min(") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_ID_COLUMN) + ") as min_user_id") + "              from ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_TABLE) + "              group by lower(") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_NAME_COLUMN) + ")") + "            ) t2 on (lower(u1.") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_NAME_COLUMN) + ") = lower(t2.") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_NAME_COLUMN) + "))") + "        ) t1 on (u.") + org.apache.ambari.server.upgrade.UpgradeCatalog270.USERS_USER_ID_COLUMN) + " = t1.min_user_id)") + "    ) t on (ap.") + org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_PRINCIPAL_ID_COLUMN) + " = t.orig_principal_id)");
        dbAccessor.truncateTable(org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_TABLE);
        dbAccessor.executeUpdate(((((((((((((((((((((((((((((("insert into " + org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_TABLE) + " (") + org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_PRIVILEGE_ID_COLUMN) + ", ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_PERMISSION_ID_COLUMN) + ", ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_RESOURCE_ID_COLUMN) + ", ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_PRINCIPAL_ID_COLUMN) + ")") + "  select ") + "    min(") + org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_PRIVILEGE_ID_COLUMN) + "),") + "    ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_PERMISSION_ID_COLUMN) + ",") + "    ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_RESOURCE_ID_COLUMN) + ",") + "    ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_PRINCIPAL_ID_COLUMN) + "  from ") + temporaryTable) + "  group by ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_PERMISSION_ID_COLUMN) + ", ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_RESOURCE_ID_COLUMN) + ", ") + org.apache.ambari.server.upgrade.UpgradeCatalog270.ADMINPRIVILEGE_PRINCIPAL_ID_COLUMN);
        dbAccessor.dropTable(temporaryTable);
    }

    private void dropBrokenFKs() throws java.sql.SQLException {
        dbAccessor.dropFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.COMPONENT_DESIRED_STATE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.FK_HOSTCOMPONENTDESIREDSTATE_COMPONENT_NAME);
        dbAccessor.dropFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.COMPONENT_STATE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.FK_HOSTCOMPONENTSTATE_COMPONENT_NAME);
        dbAccessor.dropFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.SERVICE_COMPONENT_DESIRED_STATE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.FK_SERVICECOMPONENTDESIREDSTATE_SERVICE_NAME);
    }

    protected void updateStageTable() throws java.sql.SQLException {
        dbAccessor.addColumn(org.apache.ambari.server.upgrade.UpgradeCatalog270.STAGE_TABLE, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.STAGE_STATUS_COLUMN, java.lang.String.class, 255, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, false));
        dbAccessor.addColumn(org.apache.ambari.server.upgrade.UpgradeCatalog270.STAGE_TABLE, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.STAGE_DISPLAY_STATUS_COLUMN, java.lang.String.class, 255, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, false));
        dbAccessor.addColumn(org.apache.ambari.server.upgrade.UpgradeCatalog270.REQUEST_TABLE, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.REQUEST_DISPLAY_STATUS_COLUMN, java.lang.String.class, 255, org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, false));
    }

    protected void updateRequestTable() throws java.sql.SQLException {
        dbAccessor.addColumn(org.apache.ambari.server.upgrade.UpgradeCatalog270.REQUEST_TABLE, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.REQUEST_USER_NAME_COLUMN, java.lang.String.class, 255));
    }

    protected void upgradeWidgetTable() throws java.sql.SQLException {
        dbAccessor.addColumn(org.apache.ambari.server.upgrade.UpgradeCatalog270.WIDGET_TABLE, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.WIDGET_TAG_COLUMN, java.lang.String.class, 255));
    }

    protected void addAmbariConfigurationTable() throws java.sql.SQLException {
        java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> columns = new java.util.ArrayList<>();
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_CONFIGURATION_CATEGORY_NAME_COLUMN, java.lang.String.class, 100, null, false));
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_CONFIGURATION_PROPERTY_NAME_COLUMN, java.lang.String.class, 100, null, false));
        columns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_CONFIGURATION_PROPERTY_VALUE_COLUMN, java.lang.String.class, 2048, null, true));
        dbAccessor.createTable(org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_CONFIGURATION_TABLE, columns);
        dbAccessor.addPKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_CONFIGURATION_TABLE, "PK_ambari_configuration", org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_CONFIGURATION_CATEGORY_NAME_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_CONFIGURATION_PROPERTY_NAME_COLUMN);
    }

    protected void addHostComponentLastStateTable() throws java.sql.SQLException {
        dbAccessor.addColumn(org.apache.ambari.server.upgrade.UpgradeCatalog270.COMPONENT_STATE_TABLE, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.COMPONENT_LAST_STATE_COLUMN, java.lang.String.class, 255, org.apache.ambari.server.state.State.UNKNOWN, true));
    }

    protected void upgradeKerberosTables() throws java.sql.SQLException {
        java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> kerberosKeytabColumns = new java.util.ArrayList<>();
        kerberosKeytabColumns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.KEYTAB_PATH_FIELD, java.lang.String.class, 255, null, false));
        kerberosKeytabColumns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.OWNER_NAME_FIELD, java.lang.String.class, 255, null, true));
        kerberosKeytabColumns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.OWNER_ACCESS_FIELD, java.lang.String.class, 255, null, true));
        kerberosKeytabColumns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.GROUP_NAME_FIELD, java.lang.String.class, 255, null, true));
        kerberosKeytabColumns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.GROUP_ACCESS_FIELD, java.lang.String.class, 255, null, true));
        kerberosKeytabColumns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.IS_AMBARI_KEYTAB_FIELD, java.lang.Integer.class, null, 0, false));
        kerberosKeytabColumns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.WRITE_AMBARI_JAAS_FIELD, java.lang.Integer.class, null, 0, false));
        dbAccessor.createTable(org.apache.ambari.server.upgrade.UpgradeCatalog270.KERBEROS_KEYTAB_TABLE, kerberosKeytabColumns);
        dbAccessor.addPKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.KERBEROS_KEYTAB_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.PK_KERBEROS_KEYTAB, org.apache.ambari.server.upgrade.UpgradeCatalog270.KEYTAB_PATH_FIELD);
        java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> kkpColumns = new java.util.ArrayList<>();
        kkpColumns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.KKP_ID_COLUMN, java.lang.Long.class, null, 0L, false));
        kkpColumns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.KEYTAB_PATH_FIELD, java.lang.String.class, 255, null, false));
        kkpColumns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.PRINCIPAL_NAME_COLUMN, java.lang.String.class, 255, null, false));
        kkpColumns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.HOST_ID_COLUMN, java.lang.Long.class, null, null, true));
        kkpColumns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.IS_DISTRIBUTED_COLUMN, java.lang.Integer.class, null, 0, false));
        dbAccessor.createTable(org.apache.ambari.server.upgrade.UpgradeCatalog270.KERBEROS_KEYTAB_PRINCIPAL_TABLE, kkpColumns);
        dbAccessor.addPKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.KERBEROS_KEYTAB_PRINCIPAL_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.PK_KKP, org.apache.ambari.server.upgrade.UpgradeCatalog270.KKP_ID_COLUMN);
        dbAccessor.addUniqueConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.KERBEROS_KEYTAB_PRINCIPAL_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.UNI_KKP, org.apache.ambari.server.upgrade.UpgradeCatalog270.KEYTAB_PATH_FIELD, org.apache.ambari.server.upgrade.UpgradeCatalog270.PRINCIPAL_NAME_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.HOST_ID_COLUMN);
        java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> kkpMappingColumns = new java.util.ArrayList<>();
        kkpMappingColumns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.KKP_ID_COLUMN, java.lang.Long.class, null, 0L, false));
        kkpMappingColumns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.SERVICE_NAME_COLUMN, java.lang.String.class, 255, null, false));
        kkpMappingColumns.add(new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.COMPONENT_NAME_COLUMN, java.lang.String.class, 255, null, false));
        dbAccessor.createTable(org.apache.ambari.server.upgrade.UpgradeCatalog270.KKP_MAPPING_SERVICE_TABLE, kkpMappingColumns);
        dbAccessor.addPKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.KKP_MAPPING_SERVICE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.PK_KKP_MAPPING_SERVICE, org.apache.ambari.server.upgrade.UpgradeCatalog270.KKP_ID_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.SERVICE_NAME_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.COMPONENT_NAME_COLUMN);
        dbAccessor.addFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.KERBEROS_KEYTAB_PRINCIPAL_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.FK_KKP_KEYTAB_PATH, org.apache.ambari.server.upgrade.UpgradeCatalog270.KEYTAB_PATH_FIELD, org.apache.ambari.server.upgrade.UpgradeCatalog270.KERBEROS_KEYTAB_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.KEYTAB_PATH_FIELD, false);
        dbAccessor.addFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.KERBEROS_KEYTAB_PRINCIPAL_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.FK_KKP_HOST_ID, org.apache.ambari.server.upgrade.UpgradeCatalog270.HOST_ID_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.HOSTS_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.HOST_ID_COLUMN, false);
        dbAccessor.addFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.KERBEROS_KEYTAB_PRINCIPAL_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.FK_KKP_PRINCIPAL_NAME, org.apache.ambari.server.upgrade.UpgradeCatalog270.PRINCIPAL_NAME_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.KERBEROS_PRINCIPAL_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.PRINCIPAL_NAME_COLUMN, false);
        dbAccessor.addFKConstraint(org.apache.ambari.server.upgrade.UpgradeCatalog270.KKP_MAPPING_SERVICE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.FK_KKP_SERVICE_PRINCIPAL, org.apache.ambari.server.upgrade.UpgradeCatalog270.KKP_ID_COLUMN, org.apache.ambari.server.upgrade.UpgradeCatalog270.KERBEROS_KEYTAB_PRINCIPAL_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.KKP_ID_COLUMN, false);
        addSequence(org.apache.ambari.server.upgrade.UpgradeCatalog270.KKP_ID_SEQ_NAME, 0L, false);
        dbAccessor.dropTable(org.apache.ambari.server.upgrade.UpgradeCatalog270.KERBEROS_PRINCIPAL_HOST_TABLE);
    }

    @java.lang.Override
    protected void executePreDMLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
    }

    @java.lang.Override
    protected void executeDMLUpdates() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        renameAmbariInfra();
        updateKerberosDescriptorArtifacts();
        addNewConfigurationsFromXml();
        showHcatDeletedUserMessage();
        setStatusOfStagesAndRequests();
        updateLogSearchConfigs();
        updateKerberosConfigurations();
        moveAmbariPropertiesToAmbariConfiguration();
        createRoleAuthorizations();
        addUserAuthenticationSequence();
        updateSolrConfigurations();
        updateAmsConfigs();
        updateStormConfigs();
        clearHadoopMetrics2Content();
    }

    protected void renameAmbariInfra() {
        org.apache.ambari.server.upgrade.UpgradeCatalog270.LOG.info("Renaming service AMBARI_INFRA to AMBARI_INFRA_SOLR");
        org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = ambariManagementController.getClusters();
        if (clusters == null)
            return;

        java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = clusters.getClusters();
        if (org.apache.commons.collections.MapUtils.isEmpty(clusterMap))
            return;

        javax.persistence.EntityManager entityManager = getEntityManagerProvider().get();
        org.apache.ambari.server.orm.dao.ClusterServiceDAO clusterServiceDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ClusterServiceDAO.class);
        org.apache.ambari.server.orm.dao.HostComponentStateDAO hostComponentStateDAO = injector.getInstance(org.apache.ambari.server.orm.dao.HostComponentStateDAO.class);
        org.apache.ambari.server.orm.dao.HostComponentDesiredStateDAO hostComponentDesiredStateDAO = injector.getInstance(org.apache.ambari.server.orm.dao.HostComponentDesiredStateDAO.class);
        org.apache.ambari.server.orm.dao.ServiceDesiredStateDAO serviceDesiredStateDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ServiceDesiredStateDAO.class);
        org.apache.ambari.server.orm.dao.ServiceComponentDesiredStateDAO serviceComponentDesiredStateDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ServiceComponentDesiredStateDAO.class);
        for (final org.apache.ambari.server.state.Cluster cluster : clusterMap.values()) {
            org.apache.ambari.server.orm.entities.ClusterServiceEntityPK clusterServiceEntityPK = new org.apache.ambari.server.orm.entities.ClusterServiceEntityPK();
            clusterServiceEntityPK.setClusterId(cluster.getClusterId());
            clusterServiceEntityPK.setServiceName(org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_INFRA_OLD_NAME);
            org.apache.ambari.server.orm.entities.ClusterServiceEntity clusterServiceEntity = clusterServiceDAO.findByPK(clusterServiceEntityPK);
            if (clusterServiceEntity == null)
                continue;

            java.util.List<org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity> serviceComponentDesiredStateEntities = new java.util.ArrayList<>(clusterServiceEntity.getServiceComponentDesiredStateEntities());
            org.apache.ambari.server.orm.entities.ServiceDesiredStateEntity serviceDesiredStateEntity = clusterServiceEntity.getServiceDesiredStateEntity();
            java.util.List<org.apache.ambari.server.orm.entities.HostComponentStateEntity> hostComponentStateEntities = hostComponentStateDAO.findByService(org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_INFRA_OLD_NAME);
            java.util.List<org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity> hostComponentDesiredStateEntities = new java.util.ArrayList<>();
            for (org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity serviceComponentDesiredStateEntity : clusterServiceEntity.getServiceComponentDesiredStateEntities()) {
                hostComponentDesiredStateEntities.addAll(hostComponentDesiredStateDAO.findByIndex(cluster.getClusterId(), org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_INFRA_OLD_NAME, serviceComponentDesiredStateEntity.getComponentName()));
            }
            for (org.apache.ambari.server.orm.entities.HostComponentStateEntity hostComponentStateEntity : hostComponentStateEntities) {
                hostComponentStateDAO.remove(hostComponentStateEntity);
                entityManager.detach(hostComponentStateEntity);
                hostComponentStateEntity.setServiceName(org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_INFRA_NEW_NAME);
            }
            for (org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity hostComponentDesiredStateEntity : hostComponentDesiredStateEntities) {
                hostComponentDesiredStateDAO.remove(hostComponentDesiredStateEntity);
                entityManager.detach(hostComponentDesiredStateEntity);
                hostComponentDesiredStateEntity.setServiceName(org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_INFRA_NEW_NAME);
                if ("INFRA_SOLR".equals(hostComponentDesiredStateEntity.getComponentName())) {
                    hostComponentDesiredStateEntity.setRestartRequired(true);
                }
            }
            clusterServiceEntity.getServiceComponentDesiredStateEntities().clear();
            for (org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity serviceComponentDesiredStateEntity : serviceComponentDesiredStateEntities) {
                serviceComponentDesiredStateDAO.remove(serviceComponentDesiredStateEntity);
                entityManager.detach(serviceComponentDesiredStateEntity);
                serviceComponentDesiredStateEntity.setServiceName(org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_INFRA_NEW_NAME);
            }
            if (serviceDesiredStateEntity != null) {
                clusterServiceEntity.setServiceDesiredStateEntity(null);
                serviceDesiredStateDAO.remove(serviceDesiredStateEntity);
                entityManager.detach(serviceDesiredStateEntity);
                serviceDesiredStateEntity.setServiceName(org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_INFRA_NEW_NAME);
            }
            clusterServiceDAO.remove(clusterServiceEntity);
            entityManager.detach(clusterServiceEntity);
            clusterServiceEntity.setServiceName(org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_INFRA_NEW_NAME);
            clusterServiceEntity.setServiceDesiredStateEntity(serviceDesiredStateEntity);
            clusterServiceDAO.create(clusterServiceEntity);
            for (org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity serviceComponentDesiredStateEntity : serviceComponentDesiredStateEntities)
                serviceComponentDesiredStateDAO.create(serviceComponentDesiredStateEntity);

            for (org.apache.ambari.server.orm.entities.HostComponentStateEntity hostComponentStateEntity : hostComponentStateEntities)
                hostComponentStateDAO.create(hostComponentStateEntity);

            for (org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity hostComponentDesiredStateEntity : hostComponentDesiredStateEntities)
                hostComponentDesiredStateDAO.create(hostComponentDesiredStateEntity);

        }
        executeInTransaction(() -> {
            javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ServiceConfigEntity> serviceConfigUpdate = entityManager.createQuery("UPDATE ServiceConfigEntity SET serviceName = :newServiceName WHERE serviceName = :oldServiceName", org.apache.ambari.server.orm.entities.ServiceConfigEntity.class);
            serviceConfigUpdate.setParameter("newServiceName", org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_INFRA_NEW_NAME);
            serviceConfigUpdate.setParameter("oldServiceName", org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_INFRA_OLD_NAME);
            serviceConfigUpdate.executeUpdate();
        });
        executeInTransaction(() -> {
            for (final org.apache.ambari.server.state.Cluster cluster : clusterMap.values()) {
                javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertDefinitionEntity> alertDefinitionUpdate = entityManager.createQuery("UPDATE AlertDefinitionEntity SET serviceName = :newServiceName WHERE serviceName = :oldServiceName AND clusterId = :clusterId", org.apache.ambari.server.orm.entities.AlertDefinitionEntity.class);
                alertDefinitionUpdate.setParameter("clusterId", cluster.getClusterId());
                alertDefinitionUpdate.setParameter("newServiceName", org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_INFRA_NEW_NAME);
                alertDefinitionUpdate.setParameter("oldServiceName", org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_INFRA_OLD_NAME);
                alertDefinitionUpdate.executeUpdate();
            }
        });
        executeInTransaction(() -> {
            javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertGroupEntity> alertGroupUpdate = entityManager.createQuery("UPDATE AlertGroupEntity SET serviceName = :newServiceName, groupName = :newServiceName WHERE serviceName = :oldServiceName", org.apache.ambari.server.orm.entities.AlertGroupEntity.class);
            alertGroupUpdate.setParameter("newServiceName", org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_INFRA_NEW_NAME);
            alertGroupUpdate.setParameter("oldServiceName", org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_INFRA_OLD_NAME);
            alertGroupUpdate.executeUpdate();
        });
        executeInTransaction(() -> {
            javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.AlertHistoryEntity> alertHistoryUpdate = entityManager.createQuery("UPDATE AlertHistoryEntity SET serviceName = :newServiceName WHERE serviceName = :oldServiceName", org.apache.ambari.server.orm.entities.AlertHistoryEntity.class);
            alertHistoryUpdate.setParameter("newServiceName", org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_INFRA_NEW_NAME);
            alertHistoryUpdate.setParameter("oldServiceName", org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_INFRA_OLD_NAME);
            alertHistoryUpdate.executeUpdate();
        });
        entityManager.getEntityManagerFactory().getCache().evictAll();
        clusters.invalidateAllClusters();
    }

    @java.lang.Override
    protected void updateKerberosDescriptorArtifact(org.apache.ambari.server.orm.dao.ArtifactDAO artifactDAO, org.apache.ambari.server.orm.entities.ArtifactEntity artifactEntity) throws org.apache.ambari.server.AmbariException {
        if (artifactEntity == null) {
            return;
        }
        java.util.Map<java.lang.String, java.lang.Object> data = artifactEntity.getArtifactData();
        if (data == null) {
            return;
        }
        final org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = new org.apache.ambari.server.state.kerberos.KerberosDescriptorFactory().createInstance(data);
        if (kerberosDescriptor == null) {
            return;
        }
        final boolean updateInfraKerberosDescriptor = updateInfraKerberosDescriptor(kerberosDescriptor);
        final boolean updateWebHCatHostKerberosDescriptor = updateWebHCatHostKerberosDescriptor(kerberosDescriptor);
        final boolean updateYarnKerberosDescriptor = updateYarnKerberosDescriptor(kerberosDescriptor);
        if ((updateInfraKerberosDescriptor || updateWebHCatHostKerberosDescriptor) || updateYarnKerberosDescriptor) {
            artifactEntity.setArtifactData(kerberosDescriptor.toMap());
            artifactDAO.merge(artifactEntity);
        }
    }

    private boolean updateYarnKerberosDescriptor(org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor) {
        boolean updated = false;
        org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor yarnServiceDescriptor = kerberosDescriptor.getServices().get(org.apache.ambari.server.upgrade.UpgradeCatalog270.YARN_SERVICE);
        if (yarnServiceDescriptor != null) {
            org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor coreSiteConfiguration = yarnServiceDescriptor.getConfiguration(org.apache.ambari.server.upgrade.UpgradeCatalog270.CONFIGURATION_CORE_SITE);
            if (coreSiteConfiguration != null) {
                java.util.Map<java.lang.String, java.lang.String> coreSiteProperties = coreSiteConfiguration.getProperties();
                if (coreSiteProperties != null) {
                    for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : coreSiteProperties.entrySet()) {
                        java.lang.String value = entry.getValue();
                        if (value.contains("rm_host")) {
                            java.lang.String newValue = value.replaceAll("rm_host", "resourcemanager_hosts");
                            if (!newValue.equals(value)) {
                                updated = true;
                                entry.setValue(newValue);
                            }
                        }
                    }
                    if (updated) {
                        coreSiteConfiguration.setProperties(coreSiteProperties);
                    }
                }
            }
        }
        return updated;
    }

    private boolean updateInfraKerberosDescriptor(org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor) {
        boolean updated = false;
        java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor> services = kerberosDescriptor.getServices();
        org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor ambariInfraService = services.get(org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_INFRA_OLD_NAME);
        if (ambariInfraService != null) {
            ambariInfraService.setName(org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_INFRA_NEW_NAME);
            services.remove(org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_INFRA_OLD_NAME);
            services.put(org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_INFRA_NEW_NAME, ambariInfraService);
            kerberosDescriptor.setServices(services);
            for (org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor : kerberosDescriptor.getServices().values()) {
                updateKerberosIdentities(serviceDescriptor);
                if (org.apache.commons.collections.MapUtils.isNotEmpty(serviceDescriptor.getComponents())) {
                    for (org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor componentDescriptor : serviceDescriptor.getComponents().values()) {
                        updateKerberosIdentities(componentDescriptor);
                    }
                }
            }
            updated = true;
        }
        return updated;
    }

    private boolean updateWebHCatHostKerberosDescriptor(org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor) {
        boolean updated = false;
        final org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor hiveService = kerberosDescriptor.getServices().get(org.apache.ambari.server.topology.validators.HiveServiceValidator.HIVE_SERVICE);
        if (hiveService != null) {
            final org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor webhcatServer = hiveService.getComponent(org.apache.ambari.server.upgrade.UpgradeCatalog270.HIVE_SERVICE_COMPONENT_WEBHCAT_SERVER);
            if (webhcatServer != null) {
                final org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor coreSiteConfiguration = webhcatServer.getConfiguration(org.apache.ambari.server.upgrade.UpgradeCatalog270.CONFIGURATION_CORE_SITE);
                if (coreSiteConfiguration != null) {
                    final java.lang.String currentHadoopProxyuserHttpHosts = coreSiteConfiguration.getProperty(org.apache.ambari.server.upgrade.UpgradeCatalog270.PROPERTY_HADOOP_PROXYUSER_HTTP_HOSTS);
                    if (org.apache.commons.lang.StringUtils.isNotBlank(currentHadoopProxyuserHttpHosts)) {
                        org.apache.ambari.server.upgrade.UpgradeCatalog270.LOG.info("Updating hadoop.proxyuser.HTTP.hosts...");
                        java.lang.String newValue = currentHadoopProxyuserHttpHosts.replace("webhcat_server_host|", "webhcat_server_hosts|");
                        newValue = newValue.replace("\\\\,", "\\,");
                        coreSiteConfiguration.putProperty(org.apache.ambari.server.upgrade.UpgradeCatalog270.PROPERTY_HADOOP_PROXYUSER_HTTP_HOSTS, newValue);
                        updated = true;
                    }
                }
                final org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor webhcatSiteConfiguration = webhcatServer.getConfiguration(org.apache.ambari.server.upgrade.UpgradeCatalog270.CONFIGURATION_WEBHCAT_SITE);
                if (webhcatSiteConfiguration != null) {
                    final java.lang.String currentTempletonHiveProperties = webhcatSiteConfiguration.getProperty(org.apache.ambari.server.upgrade.UpgradeCatalog270.PROPERTY_TEMPLETON_HIVE_PROPERTIES);
                    if (org.apache.commons.lang.StringUtils.isNotBlank(currentTempletonHiveProperties)) {
                        org.apache.ambari.server.upgrade.UpgradeCatalog270.LOG.info(("Updating " + org.apache.ambari.server.upgrade.UpgradeCatalog270.PROPERTY_TEMPLETON_HIVE_PROPERTIES) + "...");
                        java.lang.String newValue = currentTempletonHiveProperties.replace("hive_metastore_host|", "hive_metastore_hosts|");
                        newValue = newValue.replace("\\\\,", "\\,");
                        webhcatSiteConfiguration.putProperty(org.apache.ambari.server.upgrade.UpgradeCatalog270.PROPERTY_TEMPLETON_HIVE_PROPERTIES, newValue);
                        updated = true;
                    }
                }
            }
        }
        return updated;
    }

    protected void addUserAuthenticationSequence() throws java.sql.SQLException {
        final long maxUserAuthenticationId = fetchMaxId(org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.USER_AUTHENTICATION_USER_AUTHENTICATION_ID_COLUMN);
        org.apache.ambari.server.upgrade.UpgradeCatalog270.LOG.info("Maximum user authentication ID = " + maxUserAuthenticationId);
        addSequence("user_authentication_id_seq", maxUserAuthenticationId + 1, false);
    }

    protected void createRoleAuthorizations() throws java.sql.SQLException {
        addRoleAuthorization("AMBARI.MANAGE_CONFIGURATION", "Manage ambari configuration", java.util.Collections.singleton("AMBARI.ADMINISTRATOR:AMBARI"));
    }

    protected void showHcatDeletedUserMessage() {
        org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = ambariManagementController.getClusters();
        if (clusters != null) {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = getCheckedClusterMap(clusters);
            for (final org.apache.ambari.server.state.Cluster cluster : clusterMap.values()) {
                org.apache.ambari.server.state.Config hiveEnvConfig = cluster.getDesiredConfigByType("hive-env");
                if (hiveEnvConfig != null) {
                    java.util.Map<java.lang.String, java.lang.String> hiveEnvProperties = hiveEnvConfig.getProperties();
                    java.lang.String webhcatUser = hiveEnvProperties.get("webhcat_user");
                    java.lang.String hcatUser = hiveEnvProperties.get("hcat_user");
                    if (!org.apache.commons.lang.StringUtils.equals(webhcatUser, hcatUser)) {
                        java.lang.System.out.print("WARNING: In hive-env config, webhcat and hcat user are different. In current ambari release (3.0.0), hcat user was removed from stack, so potentially you could have some problems.");
                        org.apache.ambari.server.upgrade.UpgradeCatalog270.LOG.warn("In hive-env config, webhcat and hcat user are different. In current ambari release (3.0.0), hcat user was removed from stack, so potentially you could have some problems.");
                    }
                }
            }
        }
    }

    protected void setStatusOfStagesAndRequests() {
        executeInTransaction(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                try {
                    org.apache.ambari.server.orm.dao.RequestDAO requestDAO = injector.getInstance(org.apache.ambari.server.orm.dao.RequestDAO.class);
                    org.apache.ambari.server.actionmanager.StageFactory stageFactory = injector.getInstance(org.apache.ambari.server.actionmanager.StageFactory.class);
                    javax.persistence.EntityManager em = getEntityManagerProvider().get();
                    java.util.List<org.apache.ambari.server.orm.entities.RequestEntity> requestEntities = requestDAO.findAll();
                    for (org.apache.ambari.server.orm.entities.RequestEntity requestEntity : requestEntities) {
                        java.util.Collection<org.apache.ambari.server.orm.entities.StageEntity> stageEntities = requestEntity.getStages();
                        java.util.List<org.apache.ambari.server.actionmanager.HostRoleStatus> stageDisplayStatuses = new java.util.ArrayList<>();
                        java.util.List<org.apache.ambari.server.actionmanager.HostRoleStatus> stageStatuses = new java.util.ArrayList<>();
                        for (org.apache.ambari.server.orm.entities.StageEntity stageEntity : stageEntities) {
                            org.apache.ambari.server.actionmanager.Stage stage = stageFactory.createExisting(stageEntity);
                            java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommands = stage.getOrderedHostRoleCommands();
                            java.util.Map<org.apache.ambari.server.actionmanager.HostRoleStatus, java.lang.Integer> statusCount = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateStatusCountsForTasks(hostRoleCommands);
                            org.apache.ambari.server.actionmanager.HostRoleStatus stageDisplayStatus = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateSummaryDisplayStatus(statusCount, hostRoleCommands.size(), stage.isSkippable());
                            org.apache.ambari.server.actionmanager.HostRoleStatus stageStatus = org.apache.ambari.server.controller.internal.CalculatedStatus.calculateStageStatus(hostRoleCommands, statusCount, stage.getSuccessFactors(), stage.isSkippable());
                            stageEntity.setStatus(stageStatus);
                            stageStatuses.add(stageStatus);
                            stageEntity.setDisplayStatus(stageDisplayStatus);
                            stageDisplayStatuses.add(stageDisplayStatus);
                            em.merge(stageEntity);
                        }
                        org.apache.ambari.server.actionmanager.HostRoleStatus requestStatus = org.apache.ambari.server.controller.internal.CalculatedStatus.getOverallStatusForRequest(stageStatuses);
                        requestEntity.setStatus(requestStatus);
                        org.apache.ambari.server.actionmanager.HostRoleStatus requestDisplayStatus = org.apache.ambari.server.controller.internal.CalculatedStatus.getOverallDisplayStatusForRequest(stageDisplayStatuses);
                        requestEntity.setDisplayStatus(requestDisplayStatus);
                        em.merge(requestEntity);
                    }
                } catch (java.lang.Exception e) {
                    org.apache.ambari.server.upgrade.UpgradeCatalog270.LOG.warn("Setting status for stages and Requests threw exception. ", e);
                }
            }
        });
    }

    private void addOpsDisplayNameColumnToHostRoleCommand() throws java.sql.SQLException {
        dbAccessor.addColumn(org.apache.ambari.server.upgrade.UpgradeCatalog270.HOST_ROLE_COMMAND_TABLE, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(org.apache.ambari.server.upgrade.UpgradeCatalog270.HRC_OPS_DISPLAY_NAME_COLUMN, java.lang.String.class, 255, null, true));
    }

    private void removeSecurityState() throws java.sql.SQLException {
        dbAccessor.dropColumn(org.apache.ambari.server.upgrade.UpgradeCatalog270.COMPONENT_DESIRED_STATE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.SECURITY_STATE_COLUMN);
        dbAccessor.dropColumn(org.apache.ambari.server.upgrade.UpgradeCatalog270.COMPONENT_STATE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.SECURITY_STATE_COLUMN);
        dbAccessor.dropColumn(org.apache.ambari.server.upgrade.UpgradeCatalog270.SERVICE_DESIRED_STATE_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.SECURITY_STATE_COLUMN);
    }

    protected void updateLogSearchConfigs() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = ambariManagementController.getClusters();
        if (clusters != null) {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = clusters.getClusters();
            org.apache.ambari.server.state.ConfigHelper configHelper = injector.getInstance(org.apache.ambari.server.state.ConfigHelper.class);
            if ((clusterMap != null) && (!clusterMap.isEmpty())) {
                for (final org.apache.ambari.server.state.Cluster cluster : clusterMap.values()) {
                    org.apache.ambari.server.state.Config logSearchEnv = cluster.getDesiredConfigByType("logsearch-env");
                    java.lang.String oldProtocolProperty = null;
                    java.lang.String oldPortProperty = null;
                    if (logSearchEnv != null) {
                        oldPortProperty = logSearchEnv.getProperties().get("logsearch_ui_port");
                        oldProtocolProperty = logSearchEnv.getProperties().get("logsearch_ui_protocol");
                    }
                    org.apache.ambari.server.state.Config logSearchProperties = cluster.getDesiredConfigByType("logsearch-properties");
                    org.apache.ambari.server.state.Config logFeederProperties = cluster.getDesiredConfigByType("logfeeder-properties");
                    if ((logSearchProperties != null) && (logFeederProperties != null)) {
                        configHelper.createConfigType(cluster, cluster.getDesiredStackVersion(), ambariManagementController, "logsearch-common-properties", java.util.Collections.emptyMap(), "ambari-upgrade", java.lang.String.format("Updated logsearch-common-properties during Ambari Upgrade from %s to %s", getSourceVersion(), getTargetVersion()));
                        java.lang.String defaultLogLevels = logSearchProperties.getProperties().get("logsearch.logfeeder.include.default.level");
                        java.util.Set<java.lang.String> removeProperties = com.google.common.collect.Sets.newHashSet("logsearch.logfeeder.include.default.level");
                        removeConfigurationPropertiesFromCluster(cluster, "logsearch-properties", removeProperties);
                        java.util.Map<java.lang.String, java.lang.String> newLogSearchProperties = new java.util.HashMap<>();
                        if (oldProtocolProperty != null) {
                            newLogSearchProperties.put("logsearch.protocol", oldProtocolProperty);
                        }
                        if (oldPortProperty != null) {
                            newLogSearchProperties.put("logsearch.http.port", oldPortProperty);
                            newLogSearchProperties.put("logsearch.https.port", oldPortProperty);
                        }
                        if (!newLogSearchProperties.isEmpty()) {
                            updateConfigurationPropertiesForCluster(cluster, "logsearch-properties", newLogSearchProperties, true, true);
                        }
                        java.util.Map<java.lang.String, java.lang.String> newLogfeederProperties = new java.util.HashMap<>();
                        newLogfeederProperties.put("logfeeder.include.default.level", defaultLogLevels);
                        updateConfigurationPropertiesForCluster(cluster, "logfeeder-properties", newLogfeederProperties, true, true);
                    }
                    org.apache.ambari.server.state.Config logFeederLog4jProperties = cluster.getDesiredConfigByType("logfeeder-log4j");
                    if (logFeederLog4jProperties != null) {
                        java.lang.String content = logFeederLog4jProperties.getProperties().get("content");
                        if (content.contains("<!DOCTYPE log4j:configuration SYSTEM \"log4j.dtd\">")) {
                            content = content.replace("<!DOCTYPE log4j:configuration SYSTEM \"log4j.dtd\">", "<!DOCTYPE log4j:configuration SYSTEM \"http://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/xml/doc-files/log4j.dtd\">");
                            updateConfigurationPropertiesForCluster(cluster, "logfeeder-log4j", java.util.Collections.singletonMap("content", content), true, true);
                        }
                    }
                    org.apache.ambari.server.state.Config logSearchLog4jProperties = cluster.getDesiredConfigByType("logsearch-log4j");
                    if (logSearchLog4jProperties != null) {
                        java.lang.String content = logSearchLog4jProperties.getProperties().get("content");
                        if (content.contains("<!DOCTYPE log4j:configuration SYSTEM \"log4j.dtd\">")) {
                            content = content.replace("<!DOCTYPE log4j:configuration SYSTEM \"log4j.dtd\">", "<!DOCTYPE log4j:configuration SYSTEM \"http://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/xml/doc-files/log4j.dtd\">");
                            updateConfigurationPropertiesForCluster(cluster, "logsearch-log4j", java.util.Collections.singletonMap("content", content), true, true);
                        }
                    }
                    removeAdminHandlersFrom(cluster, "logsearch-service_logs-solrconfig");
                    removeAdminHandlersFrom(cluster, "logsearch-audit_logs-solrconfig");
                    org.apache.ambari.server.state.Config logFeederOutputConfig = cluster.getDesiredConfigByType("logfeeder-output-config");
                    if (logFeederOutputConfig != null) {
                        java.lang.String content = logFeederOutputConfig.getProperties().get("content");
                        content = content.replace("      \"collection\":\"{{logsearch_solr_collection_service_logs}}\",\n" + ("      \"number_of_shards\": \"{{logsearch_collection_service_logs_numshards}}\",\n" + "      \"splits_interval_mins\": \"{{logsearch_service_logs_split_interval_mins}}\",\n"), "      \"type\": \"service\",\n");
                        content = content.replace("      \"collection\":\"{{logsearch_solr_collection_audit_logs}}\",\n" + ("      \"number_of_shards\": \"{{logsearch_collection_audit_logs_numshards}}\",\n" + "      \"splits_interval_mins\": \"{{logsearch_audit_logs_split_interval_mins}}\",\n"), "      \"type\": \"audit\",\n");
                        updateConfigurationPropertiesForCluster(cluster, "logfeeder-output-config", java.util.Collections.singletonMap("content", content), true, true);
                    }
                    org.apache.ambari.server.orm.DBAccessor dba = (dbAccessor != null) ? dbAccessor : injector.getInstance(org.apache.ambari.server.orm.DBAccessor.class);
                    removeLogSearchPatternConfigs(dba);
                }
            }
        }
    }

    private void removeLogSearchPatternConfigs(org.apache.ambari.server.orm.DBAccessor dbAccessor) throws java.sql.SQLException {
        java.lang.String configSuffix = "-logsearch-conf";
        java.lang.String serviceConfigMappingRemoveSQL = java.lang.String.format("DELETE FROM %s WHERE config_id IN (SELECT config_id from %s where type_name like '%%%s')", org.apache.ambari.server.upgrade.UpgradeCatalog270.SERVICE_CONFIG_MAPPING_TABLE, org.apache.ambari.server.upgrade.UpgradeCatalog270.CLUSTER_CONFIG_TABLE, configSuffix);
        java.lang.String clusterConfigRemoveSQL = java.lang.String.format("DELETE FROM %s WHERE type_name like '%%%s'", org.apache.ambari.server.upgrade.UpgradeCatalog270.CLUSTER_CONFIG_TABLE, configSuffix);
        dbAccessor.executeQuery(serviceConfigMappingRemoveSQL);
        dbAccessor.executeQuery(clusterConfigRemoveSQL);
    }

    private void removeAdminHandlersFrom(org.apache.ambari.server.state.Cluster cluster, java.lang.String configType) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Config logSearchServiceLogsConfig = cluster.getDesiredConfigByType(configType);
        if (logSearchServiceLogsConfig != null) {
            java.lang.String content = logSearchServiceLogsConfig.getProperties().get("content");
            if (content.contains("class=\"solr.admin.AdminHandlers\"")) {
                content = removeAdminHandlers(content);
                updateConfigurationPropertiesForCluster(cluster, configType, java.util.Collections.singletonMap("content", content), true, true);
            }
        }
    }

    protected java.lang.String removeAdminHandlers(java.lang.String content) {
        return content.replaceAll("(?s)<requestHandler\\s+name=\"/admin/\"\\s+class=\"solr.admin.AdminHandlers\"\\s*/>", "");
    }

    private void updateKerberosIdentities(org.apache.ambari.server.state.kerberos.AbstractKerberosDescriptorContainer descriptorContainer) {
        if (descriptorContainer.getIdentities() == null)
            return;

        descriptorContainer.getIdentities().stream().filter(identityDescriptor -> (identityDescriptor.getReference() != null) && identityDescriptor.getReference().contains(org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_INFRA_OLD_NAME)).forEach(identityDescriptor -> identityDescriptor.setReference(identityDescriptor.getReference().replace(org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_INFRA_OLD_NAME, org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_INFRA_NEW_NAME)));
        descriptorContainer.getIdentities().stream().filter(identityDescriptor -> identityDescriptor.getWhen() != null).collect(java.util.stream.Collectors.toList()).forEach(identityDescriptor -> {
            java.util.Map<java.lang.String, java.lang.Object> whenMap = identityDescriptor.getWhen().toMap();
            if (whenMap.containsKey("contains")) {
                java.util.List<java.lang.String> serviceList = ((java.util.List<java.lang.String>) (whenMap.get("contains")));
                if (serviceList.contains(org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_INFRA_OLD_NAME)) {
                    serviceList.remove(org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_INFRA_OLD_NAME);
                    serviceList.add(org.apache.ambari.server.upgrade.UpgradeCatalog270.AMBARI_INFRA_NEW_NAME);
                    identityDescriptor.setWhen(org.apache.ambari.server.collections.PredicateUtils.fromMap(((java.util.Map<?, ?>) (whenMap))));
                }
            }
        });
    }

    protected org.apache.ambari.server.serveraction.kerberos.PrepareKerberosIdentitiesServerAction getPrepareIdentityServerAction() {
        return new org.apache.ambari.server.serveraction.kerberos.PrepareKerberosIdentitiesServerAction();
    }

    protected void updateKerberosConfigurations() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = ambariManagementController.getClusters();
        if (clusters != null) {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = clusters.getClusters();
            if (!org.apache.commons.collections.MapUtils.isEmpty(clusterMap)) {
                for (org.apache.ambari.server.state.Cluster cluster : clusterMap.values()) {
                    org.apache.ambari.server.state.Config config = cluster.getDesiredConfigByType("kerberos-env");
                    if (config != null) {
                        java.util.Map<java.lang.String, java.lang.String> properties = config.getProperties();
                        if (properties.containsKey("group")) {
                            updateConfigurationPropertiesForCluster(cluster, "kerberos-env", java.util.Collections.singletonMap("ipa_user_group", properties.get("group")), java.util.Collections.singleton("group"), true, false);
                        }
                    }
                    if (config != null) {
                        org.apache.ambari.server.serveraction.kerberos.PrepareKerberosIdentitiesServerAction prepareIdentities = getPrepareIdentityServerAction();
                        org.apache.ambari.server.agent.ExecutionCommand executionCommand = new org.apache.ambari.server.agent.ExecutionCommand();
                        executionCommand.setCommandParams(new java.util.HashMap<java.lang.String, java.lang.String>() {
                            {
                                put(org.apache.ambari.server.serveraction.kerberos.KerberosServerAction.DEFAULT_REALM, config.getProperties().get("realm"));
                            }
                        });
                        prepareIdentities.setExecutionCommand(executionCommand);
                        injector.injectMembers(prepareIdentities);
                        org.apache.ambari.server.controller.KerberosHelper kerberosHelper = injector.getInstance(org.apache.ambari.server.controller.KerberosHelper.class);
                        injector.getInstance(org.apache.ambari.server.controller.AmbariServer.class).performStaticInjection();
                        org.apache.ambari.server.controller.AmbariServer.setController(injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class));
                        org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = kerberosHelper.getKerberosDescriptor(cluster, false);
                        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> kerberosConfigurations = new java.util.HashMap<>();
                        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> propertiesToIgnore = new java.util.HashMap<>();
                        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> schToProcess = kerberosHelper.getServiceComponentHostsToProcess(cluster, kerberosDescriptor, null, null);
                        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations = kerberosHelper.calculateConfigurations(cluster, null, kerberosDescriptor, false, false, null);
                        boolean includeAmbariIdentity = true;
                        java.lang.String dataDirectory = kerberosHelper.createTemporaryDirectory().getAbsolutePath();
                        try {
                            executeInTransaction(new java.lang.Runnable() {
                                @java.lang.Override
                                public void run() {
                                    try {
                                        prepareIdentities.processServiceComponentHosts(cluster, kerberosDescriptor, schToProcess, null, dataDirectory, configurations, kerberosConfigurations, includeAmbariIdentity, propertiesToIgnore);
                                    } catch (org.apache.ambari.server.AmbariException e) {
                                        throw new java.lang.RuntimeException(e);
                                    }
                                }
                            });
                        } catch (java.lang.RuntimeException e) {
                            throw new org.apache.ambari.server.AmbariException("Failed to upgrade kerberos tables", e);
                        }
                    }
                }
            }
        }
    }

    protected void moveAmbariPropertiesToAmbariConfiguration() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.upgrade.UpgradeCatalog270.LOG.info("Moving LDAP and SSO related properties from ambari.properties to ambari_configuration DB table...");
        final org.apache.ambari.server.orm.dao.AmbariConfigurationDAO ambariConfigurationDAO = injector.getInstance(org.apache.ambari.server.orm.dao.AmbariConfigurationDAO.class);
        final java.util.Map<org.apache.ambari.server.configuration.AmbariServerConfigurationCategory, java.util.Map<java.lang.String, java.lang.String>> propertiesToBeMoved = new java.util.HashMap<>();
        final java.util.Map<org.apache.ambari.server.configuration.AmbariServerConfigurationKey, java.lang.String> configurationMap = getAmbariConfigurationMap();
        configurationMap.forEach((key, oldPropertyName) -> {
            java.lang.String propertyValue = configuration.getProperty(oldPropertyName);
            if (propertyValue != null) {
                if ((org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SERVER_HOST == key) || (org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SECONDARY_SERVER_HOST == key)) {
                    final com.google.common.net.HostAndPort hostAndPort = com.google.common.net.HostAndPort.fromString(propertyValue);
                    org.apache.ambari.server.configuration.AmbariServerConfigurationKey keyToBesaved = (org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SERVER_HOST == key) ? org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SERVER_HOST : org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SECONDARY_SERVER_HOST;
                    populateConfigurationToBeMoved(propertiesToBeMoved, oldPropertyName, keyToBesaved, hostAndPort.getHost());
                    keyToBesaved = (org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SERVER_HOST == key) ? org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SERVER_PORT : org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SECONDARY_SERVER_PORT;
                    populateConfigurationToBeMoved(propertiesToBeMoved, oldPropertyName, keyToBesaved, java.lang.String.valueOf(hostAndPort.getPort()));
                } else if (org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_PROVIDER_CERTIFICATE == key) {
                    java.lang.StringBuilder contentBuilder = new java.lang.StringBuilder();
                    try (java.util.stream.Stream<java.lang.String> stream = java.nio.file.Files.lines(java.nio.file.Paths.get(propertyValue), java.nio.charset.StandardCharsets.UTF_8)) {
                        stream.forEach(s -> contentBuilder.append(s).append("\n"));
                    } catch (java.io.IOException e) {
                        org.apache.ambari.server.upgrade.UpgradeCatalog270.LOG.error(java.lang.String.format("Failed to read the SSO provider's certificate file, %s: %s", propertyValue, e.getMessage()), e);
                    }
                    populateConfigurationToBeMoved(propertiesToBeMoved, oldPropertyName, key, contentBuilder.toString());
                } else if (org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_AUTHENTICATION_ENABLED == key) {
                    populateConfigurationToBeMoved(propertiesToBeMoved, oldPropertyName, key, propertyValue);
                    if ("true".equalsIgnoreCase(propertyValue)) {
                        populateConfigurationToBeMoved(propertiesToBeMoved, null, org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_MANAGE_SERVICES, "true");
                        populateConfigurationToBeMoved(propertiesToBeMoved, null, org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_ENABLED_SERVICES, "AMBARI");
                    }
                } else if (org.apache.ambari.server.configuration.AmbariServerConfigurationKey.LDAP_ENABLED == key) {
                    populateConfigurationToBeMoved(propertiesToBeMoved, oldPropertyName, key, propertyValue);
                    if ("true".equalsIgnoreCase(propertyValue)) {
                        populateConfigurationToBeMoved(propertiesToBeMoved, null, org.apache.ambari.server.configuration.AmbariServerConfigurationKey.AMBARI_MANAGES_LDAP_CONFIGURATION, "true");
                        populateConfigurationToBeMoved(propertiesToBeMoved, null, org.apache.ambari.server.configuration.AmbariServerConfigurationKey.LDAP_ENABLED_SERVICES, "AMBARI");
                    }
                } else {
                    populateConfigurationToBeMoved(propertiesToBeMoved, oldPropertyName, key, propertyValue);
                }
            }
        });
        if (propertiesToBeMoved.isEmpty()) {
            org.apache.ambari.server.upgrade.UpgradeCatalog270.LOG.info("There are no properties to be moved from ambari.properties to the Ambari DB; moved 0 elements");
        } else {
            for (java.util.Map.Entry<org.apache.ambari.server.configuration.AmbariServerConfigurationCategory, java.util.Map<java.lang.String, java.lang.String>> entry : propertiesToBeMoved.entrySet()) {
                java.util.Map<java.lang.String, java.lang.String> properties = entry.getValue();
                if (properties != null) {
                    java.lang.String categoryName = entry.getKey().getCategoryName();
                    ambariConfigurationDAO.reconcileCategory(categoryName, entry.getValue(), false);
                    org.apache.ambari.server.upgrade.UpgradeCatalog270.LOG.info("Moved {} properties to the {} Ambari Configuration category", properties.size(), categoryName);
                }
            }
            configuration.removePropertiesFromAmbariProperties(configurationMap.values());
        }
    }

    private void populateConfigurationToBeMoved(java.util.Map<org.apache.ambari.server.configuration.AmbariServerConfigurationCategory, java.util.Map<java.lang.String, java.lang.String>> propertiesToBeSaved, java.lang.String oldPropertyName, org.apache.ambari.server.configuration.AmbariServerConfigurationKey key, java.lang.String value) {
        org.apache.ambari.server.configuration.AmbariServerConfigurationCategory category = key.getConfigurationCategory();
        java.lang.String newPropertyName = key.key();
        java.util.Map<java.lang.String, java.lang.String> categoryProperties = propertiesToBeSaved.computeIfAbsent(category, k -> new java.util.HashMap<>());
        categoryProperties.put(newPropertyName, value);
        if (oldPropertyName != null) {
            org.apache.ambari.server.upgrade.UpgradeCatalog270.LOG.info("Upgrading '{}' to '{}'", oldPropertyName, newPropertyName);
        }
    }

    @java.lang.SuppressWarnings("serial")
    private java.util.Map<org.apache.ambari.server.configuration.AmbariServerConfigurationKey, java.lang.String> getAmbariConfigurationMap() {
        java.util.Map<org.apache.ambari.server.configuration.AmbariServerConfigurationKey, java.lang.String> map = new java.util.HashMap<>();
        map.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.LDAP_ENABLED, "ambari.ldap.isConfigured");
        map.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SERVER_HOST, "authentication.ldap.primaryUrl");
        map.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SECONDARY_SERVER_HOST, "authentication.ldap.secondaryUrl");
        map.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USE_SSL, "authentication.ldap.useSSL");
        map.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.ANONYMOUS_BIND, "authentication.ldap.bindAnonymously");
        map.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.BIND_DN, "authentication.ldap.managerDn");
        map.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.BIND_PASSWORD, "authentication.ldap.managerPassword");
        map.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.DN_ATTRIBUTE, "authentication.ldap.dnAttribute");
        map.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_OBJECT_CLASS, "authentication.ldap.userObjectClass");
        map.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_NAME_ATTRIBUTE, "authentication.ldap.usernameAttribute");
        map.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_SEARCH_BASE, "authentication.ldap.baseDn");
        map.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_BASE, "authentication.ldap.userBase");
        map.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_OBJECT_CLASS, "authentication.ldap.groupObjectClass");
        map.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_NAME_ATTRIBUTE, "authentication.ldap.groupNamingAttr");
        map.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_MEMBER_ATTRIBUTE, "authentication.ldap.groupMembershipAttr");
        map.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_SEARCH_BASE, "authentication.ldap.baseDn");
        map.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_BASE, "authentication.ldap.groupBase");
        map.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_SEARCH_FILTER, "authentication.ldap.userSearchFilter");
        map.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_MEMBER_REPLACE_PATTERN, "authentication.ldap.sync.userMemberReplacePattern");
        map.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.USER_MEMBER_FILTER, "authentication.ldap.sync.userMemberFilter");
        map.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.ALTERNATE_USER_SEARCH_ENABLED, "authentication.ldap.alternateUserSearchEnabled");
        map.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.ALTERNATE_USER_SEARCH_FILTER, "authentication.ldap.alternateUserSearchFilter");
        map.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_SEARCH_FILTER, "authorization.ldap.groupSearchFilter");
        map.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_MEMBER_REPLACE_PATTERN, "authentication.ldap.sync.groupMemberReplacePattern");
        map.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_MEMBER_FILTER, "authentication.ldap.sync.groupMemberFilter");
        map.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.GROUP_MAPPING_RULES, "authorization.ldap.adminGroupMappingRules");
        map.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.FORCE_LOWERCASE_USERNAMES, "authentication.ldap.username.forceLowercase");
        map.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.REFERRAL_HANDLING, "authentication.ldap.referral");
        map.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.PAGINATION_ENABLED, "authentication.ldap.pagination.enabled");
        map.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.COLLISION_BEHAVIOR, "ldap.sync.username.collision.behavior");
        map.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.DISABLE_ENDPOINT_IDENTIFICATION, "ldap.sync.disable.endpoint.identification");
        map.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_PROVIDER_URL, "authentication.jwt.providerUrl");
        map.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_PROVIDER_CERTIFICATE, "authentication.jwt.publicKey");
        map.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_PROVIDER_ORIGINAL_URL_PARAM_NAME, "authentication.jwt.originalUrlParamName");
        map.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_AUTHENTICATION_ENABLED, "authentication.jwt.enabled");
        map.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_JWT_AUDIENCES, "authentication.jwt.audiences");
        map.put(org.apache.ambari.server.configuration.AmbariServerConfigurationKey.SSO_JWT_COOKIE_NAME, "authentication.jwt.cookieName");
        return map;
    }

    protected void updateSolrConfigurations() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = ambariManagementController.getClusters();
        if (clusters == null)
            return;

        java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = clusters.getClusters();
        if ((clusterMap == null) || clusterMap.isEmpty()) {
            return;
        }
        for (final org.apache.ambari.server.state.Cluster cluster : clusterMap.values()) {
            updateConfig(cluster, "logsearch-service_logs-solrconfig", content -> {
                content = updateLuceneMatchVersion(content, "7.3.1");
                return updateMergeFactor(content, "logsearch_service_logs_merge_factor");
            });
            updateConfig(cluster, "logsearch-audit_logs-solrconfig", content -> {
                content = updateLuceneMatchVersion(content, "7.3.1");
                return updateMergeFactor(content, "logsearch_audit_logs_merge_factor");
            });
            updateConfig(cluster, "ranger-solr-configuration", content -> {
                content = updateLuceneMatchVersion(content, "6.6.0");
                return updateMergeFactor(content, "ranger_audit_logs_merge_factor");
            });
            updateConfig(cluster, "atlas-solrconfig", content -> updateLuceneMatchVersion(content, "6.6.0"));
            updateConfig(cluster, "infra-solr-env", this::updateInfraSolrEnv);
            updateConfig(cluster, "infra-solr-security-json", content -> content.replace("org.apache.ambari.infra.security.InfraRuleBasedAuthorizationPlugin", "org.apache.solr.security.InfraRuleBasedAuthorizationPlugin"));
        }
    }

    private void updateConfig(org.apache.ambari.server.state.Cluster cluster, java.lang.String configType, java.util.function.Function<java.lang.String, java.lang.String> contentUpdater) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Config config = cluster.getDesiredConfigByType(configType);
        if (config == null)
            return;

        if ((config.getProperties() == null) || (!config.getProperties().containsKey("content")))
            return;

        java.lang.String content = config.getProperties().get("content");
        content = contentUpdater.apply(content);
        updateConfigurationPropertiesForCluster(cluster, configType, java.util.Collections.singletonMap("content", content), true, true);
    }

    protected java.lang.String updateLuceneMatchVersion(java.lang.String content, java.lang.String newLuceneMatchVersion) {
        return content.replaceAll("<luceneMatchVersion>.*</luceneMatchVersion>", ("<luceneMatchVersion>" + newLuceneMatchVersion) + "</luceneMatchVersion>");
    }

    protected java.lang.String updateMergeFactor(java.lang.String content, java.lang.String variableName) {
        return content.replaceAll(("<mergeFactor>\\{\\{" + variableName) + "\\}\\}</mergeFactor>", (((((("<mergePolicyFactory class=\"org.apache.solr.index.TieredMergePolicyFactory\">\n" + "      <int name=\"maxMergeAtOnce\">{{") + variableName) + "}}</int>\n") + "      <int name=\"segmentsPerTier\">{{") + variableName) + "}}</int>\n") + "    </mergePolicyFactory>");
    }

    protected java.lang.String updateInfraSolrEnv(java.lang.String content) {
        return content.replaceAll("SOLR_KERB_NAME_RULES=\".*\"", "").replaceAll("#*SOLR_HOST=\".*\"", "SOLR_HOST=`hostname -f`").replaceAll("SOLR_AUTHENTICATION_CLIENT_CONFIGURER=\".*\"", "SOLR_AUTH_TYPE=\"kerberos\"");
    }

    protected void updateAmsConfigs() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = ambariManagementController.getClusters();
        if (clusters != null) {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = clusters.getClusters();
            if ((clusterMap != null) && (!clusterMap.isEmpty())) {
                for (final org.apache.ambari.server.state.Cluster cluster : clusterMap.values()) {
                    java.util.Map<java.lang.String, java.lang.String> newProperties = new java.util.HashMap<>();
                    org.apache.ambari.server.upgrade.UpgradeCatalog270.LOG.info("Updating ams-site:timeline.metrics.service.default.result.limit to 5760");
                    newProperties.put("timeline.metrics.service.default.result.limit", "5760");
                    org.apache.ambari.server.state.Config config = cluster.getDesiredConfigByType("ams-site");
                    if (config != null) {
                        java.util.Map<java.lang.String, java.lang.String> oldAmsSite = config.getProperties();
                        if (org.apache.commons.collections.MapUtils.isNotEmpty(oldAmsSite)) {
                            if (oldAmsSite.containsKey("timeline.container-metrics.ttl")) {
                                try {
                                    int oldTtl = java.lang.Integer.parseInt(oldAmsSite.get("timeline.container-metrics.ttl"));
                                    if (oldTtl > (14 * 86400)) {
                                        org.apache.ambari.server.upgrade.UpgradeCatalog270.LOG.info("Updating ams-site:timeline.container-metrics.ttl to 1209600");
                                        newProperties.put("timeline.container-metrics.ttl", "1209600");
                                    }
                                } catch (java.lang.Exception e) {
                                    org.apache.ambari.server.upgrade.UpgradeCatalog270.LOG.warn("Error updating Container metrics TTL for ams-site (AMBARI_METRICS)");
                                }
                            }
                            java.lang.String topnDownsamplerMetricPatternsKey = "timeline.metrics.downsampler.topn.metric.patterns";
                            if (oldAmsSite.containsKey(topnDownsamplerMetricPatternsKey) && org.apache.commons.lang.StringUtils.isNotEmpty(oldAmsSite.get(topnDownsamplerMetricPatternsKey))) {
                                org.apache.ambari.server.upgrade.UpgradeCatalog270.LOG.info("Updating ams-site:timeline.metrics.downsampler.topn.metric.patterns to empty.");
                                newProperties.put(topnDownsamplerMetricPatternsKey, "");
                            }
                        }
                    }
                    org.apache.ambari.server.upgrade.UpgradeCatalog270.LOG.info("Removing ams-site host and aggregate cluster split points.");
                    java.util.Set<java.lang.String> removeProperties = com.google.common.collect.Sets.newHashSet("timeline.metrics.host.aggregate.splitpoints", "timeline.metrics.cluster.aggregate.splitpoints");
                    updateConfigurationPropertiesForCluster(cluster, "ams-site", newProperties, removeProperties, true, true);
                    java.util.Map<java.lang.String, java.lang.String> newAmsHbaseSiteProperties = new java.util.HashMap<>();
                    org.apache.ambari.server.state.Config amsHBasiteSiteConfig = cluster.getDesiredConfigByType("ams-hbase-site");
                    if (amsHBasiteSiteConfig != null) {
                        java.util.Map<java.lang.String, java.lang.String> oldAmsHBaseSite = amsHBasiteSiteConfig.getProperties();
                        if (org.apache.commons.collections.MapUtils.isNotEmpty(oldAmsHBaseSite)) {
                            if (oldAmsHBaseSite.containsKey("hbase.snapshot.enabled")) {
                                try {
                                    java.lang.Boolean hbaseSnapshotEnabled = java.lang.Boolean.valueOf(oldAmsHBaseSite.get("hbase.snapshot.enabled"));
                                    if (!hbaseSnapshotEnabled) {
                                        org.apache.ambari.server.upgrade.UpgradeCatalog270.LOG.info("Updating ams-hbase-site:hbase.snapshot.enabled to true");
                                        newAmsHbaseSiteProperties.put("hbase.snapshot.enabled", "true");
                                    }
                                } catch (java.lang.Exception e) {
                                    org.apache.ambari.server.upgrade.UpgradeCatalog270.LOG.warn("Error updating ams-hbase-site:hbase.snapshot.enabled (AMBARI_METRICS)");
                                }
                            }
                        }
                        updateConfigurationPropertiesForCluster(cluster, "ams-hbase-site", newAmsHbaseSiteProperties, true, true);
                    }
                }
            }
        }
    }

    protected void updateStormConfigs() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = ambariManagementController.getClusters();
        if (clusters != null) {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = clusters.getClusters();
            if ((clusterMap != null) && (!clusterMap.isEmpty())) {
                java.util.Set<java.lang.String> removeProperties = com.google.common.collect.Sets.newHashSet("_storm.thrift.nonsecure.transport", "_storm.thrift.secure.transport");
                java.lang.String stormSecurityClassKey = "storm.thrift.transport";
                java.lang.String stormSecurityClassValue = "org.apache.storm.security.auth.SimpleTransportPlugin";
                java.lang.String stormSite = "storm-site";
                for (final org.apache.ambari.server.state.Cluster cluster : clusterMap.values()) {
                    org.apache.ambari.server.state.Config config = cluster.getDesiredConfigByType(stormSite);
                    if (config != null) {
                        java.util.Map<java.lang.String, java.lang.String> stormSiteProperties = config.getProperties();
                        if (stormSiteProperties.containsKey(stormSecurityClassKey)) {
                            org.apache.ambari.server.upgrade.UpgradeCatalog270.LOG.info("Updating " + stormSecurityClassKey);
                            if (cluster.getSecurityType() == org.apache.ambari.server.state.SecurityType.KERBEROS) {
                                stormSecurityClassValue = "org.apache.storm.security.auth.kerberos.KerberosSaslTransportPlugin";
                            }
                            java.util.Map<java.lang.String, java.lang.String> updateProperty = java.util.Collections.singletonMap(stormSecurityClassKey, stormSecurityClassValue);
                            updateConfigurationPropertiesForCluster(cluster, stormSite, updateProperty, removeProperties, true, false);
                        }
                    }
                }
            }
        }
    }

    protected void clearHadoopMetrics2Content() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.AmbariManagementController ambariManagementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = ambariManagementController.getClusters();
        if (clusters != null) {
            java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = clusters.getClusters();
            if ((clusterMap != null) && (!clusterMap.isEmpty())) {
                java.lang.String hadoopMetrics2ContentProperty = "content";
                java.lang.String hadoopMetrics2ContentValue = "";
                java.lang.String hadoopMetrics2ConfigType = "hadoop-metrics2.properties";
                for (final org.apache.ambari.server.state.Cluster cluster : clusterMap.values()) {
                    org.apache.ambari.server.state.Config config = cluster.getDesiredConfigByType(hadoopMetrics2ConfigType);
                    if (config != null) {
                        java.util.Map<java.lang.String, java.lang.String> hadoopMetrics2Configs = config.getProperties();
                        if (hadoopMetrics2Configs.containsKey(hadoopMetrics2ContentProperty)) {
                            org.apache.ambari.server.upgrade.UpgradeCatalog270.LOG.info("Updating " + hadoopMetrics2ContentProperty);
                            java.util.Map<java.lang.String, java.lang.String> updateProperty = java.util.Collections.singletonMap(hadoopMetrics2ContentProperty, hadoopMetrics2ContentValue);
                            updateConfigurationPropertiesForCluster(cluster, hadoopMetrics2ConfigType, updateProperty, java.util.Collections.EMPTY_SET, true, false);
                        }
                    }
                }
            }
        }
    }
}