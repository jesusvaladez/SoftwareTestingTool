package org.apache.ambari.server.checks;
import com.google.inject.persist.Transactional;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
public class DatabaseConsistencyCheckHelper {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.class);

    @com.google.inject.Inject
    private static com.google.inject.Injector injector;

    private static org.apache.ambari.server.orm.dao.MetainfoDAO metainfoDAO;

    private static org.apache.ambari.server.orm.dao.AlertDefinitionDAO alertDefinitionDAO;

    private static java.sql.Connection connection;

    private static org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo;

    private static org.apache.ambari.server.orm.DBAccessor dbAccessor;

    private static org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAO;

    private static org.apache.ambari.server.orm.dao.ExecutionCommandDAO executionCommandDAO;

    private static org.apache.ambari.server.orm.dao.StageDAO stageDAO;

    private static org.apache.ambari.server.checks.DatabaseConsistencyCheckResult checkResult = org.apache.ambari.server.checks.DatabaseConsistencyCheckResult.DB_CHECK_SUCCESS;

    public static final java.lang.String GET_CONFIGS_SELECTED_MORE_THAN_ONCE_QUERY = "select c.cluster_name, cc.type_name from clusterconfig cc " + (("join clusters c on cc.cluster_id=c.cluster_id " + "group by c.cluster_name, cc.type_name ") + "having sum(cc.selected) > 1");

    public static org.apache.ambari.server.checks.DatabaseConsistencyCheckResult getLastCheckResult() {
        return org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkResult;
    }

    public static void resetCheckResult() {
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkResult = org.apache.ambari.server.checks.DatabaseConsistencyCheckResult.DB_CHECK_SUCCESS;
    }

    private static void setCheckResult(org.apache.ambari.server.checks.DatabaseConsistencyCheckResult newResult) {
        if (newResult.ordinal() > org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkResult.ordinal()) {
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkResult = newResult;
        }
    }

    private static void warning(java.lang.String messageTemplate, java.lang.Object... messageParams) {
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.warn(messageTemplate, messageParams);
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.setCheckResult(org.apache.ambari.server.checks.DatabaseConsistencyCheckResult.DB_CHECK_WARNING);
    }

    private static void error(java.lang.String messageTemplate, java.lang.Object... messageParams) {
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.error(messageTemplate, messageParams);
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.setCheckResult(org.apache.ambari.server.checks.DatabaseConsistencyCheckResult.DB_CHECK_ERROR);
    }

    public static void setInjector(com.google.inject.Injector injector) {
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.injector = injector;
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.closeConnection();
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.connection = null;
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.metainfoDAO = null;
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.alertDefinitionDAO = null;
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.ambariMetaInfo = null;
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.dbAccessor = null;
    }

    public static void setConnection(java.sql.Connection connection) {
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.connection = connection;
    }

    public static void closeConnection() {
        try {
            if (org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.connection != null) {
                org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.connection.close();
            }
        } catch (java.sql.SQLException e) {
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.error("Exception occurred during connection close procedure: ", e);
        }
    }

    public static org.apache.ambari.server.checks.DatabaseConsistencyCheckResult runAllDBChecks(boolean fixIssues) throws java.lang.Throwable {
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.info("******************************* Check database started *******************************");
        try {
            if (fixIssues) {
                org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.fixHostComponentStatesCountEqualsHostComponentsDesiredStates();
                org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.fixClusterConfigsNotMappedToAnyService();
                org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.fixConfigGroupServiceNames();
                org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.fixConfigGroupHostMappings();
                org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.fixConfigGroupsForDeletedServices();
                org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.fixConfigsSelectedMoreThanOnce();
                org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.fixAlertsForDeletedServices();
            }
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkSchemaName();
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkMySQLEngine();
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkForConfigsNotMappedToService();
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkForConfigsSelectedMoreThanOnce();
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkForHostsWithoutState();
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkHostComponentStates();
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkServiceConfigs();
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkForLargeTables();
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkConfigGroupsHasServiceName();
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkConfigGroupHostMapping(true);
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkConfigGroupsForDeletedServices(true);
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkForStalealertdefs();
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.info("******************************* Check database completed *******************************");
            return org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkResult;
        } catch (java.lang.Throwable ex) {
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.error("An error occurred during database consistency check.", ex);
            throw ex;
        }
    }

    public static void checkDBVersionCompatible() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.info("Checking DB store version");
        if (org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.metainfoDAO == null) {
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.metainfoDAO = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.injector.getInstance(org.apache.ambari.server.orm.dao.MetainfoDAO.class);
        }
        if (org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.alertDefinitionDAO == null) {
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.alertDefinitionDAO = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.injector.getInstance(org.apache.ambari.server.orm.dao.AlertDefinitionDAO.class);
        }
        org.apache.ambari.server.orm.entities.MetainfoEntity schemaVersionEntity = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.metainfoDAO.findByKey(org.apache.ambari.server.configuration.Configuration.SERVER_VERSION_KEY);
        java.lang.String schemaVersion = null;
        if (schemaVersionEntity != null) {
            schemaVersion = schemaVersionEntity.getMetainfoValue();
        }
        org.apache.ambari.server.configuration.Configuration conf = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.injector.getInstance(org.apache.ambari.server.configuration.Configuration.class);
        java.io.File versionFile = new java.io.File(conf.getServerVersionFilePath());
        if (!versionFile.exists()) {
            throw new org.apache.ambari.server.AmbariException("Server version file does not exist.");
        }
        java.lang.String serverVersion = null;
        try (java.util.Scanner scanner = new java.util.Scanner(versionFile)) {
            serverVersion = scanner.useDelimiter("\\Z").next();
        } catch (java.io.IOException ioe) {
            throw new org.apache.ambari.server.AmbariException("Unable to read server version file.");
        }
        if ((schemaVersionEntity == null) || (org.apache.ambari.server.utils.VersionUtils.compareVersions(schemaVersion, serverVersion, 3) != 0)) {
            java.lang.String error = ((("Current database store version is not compatible with " + ("current server version" + ", serverVersion=")) + serverVersion) + ", schemaVersion=") + schemaVersion;
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.error(error);
            throw new org.apache.ambari.server.AmbariException(error);
        }
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.info("DB store version is compatible");
    }

    static void checkForLargeTables() {
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.info("Checking for tables with large physical size");
        if (org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.dbAccessor.getDbType() == org.apache.ambari.server.orm.DBAccessor.DbType.H2) {
            return;
        }
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.ensureConnection();
        org.apache.ambari.server.orm.DBAccessor.DbType dbType = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.dbAccessor.getDbType();
        java.lang.String schemaName = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.dbAccessor.getDbSchema();
        java.lang.String GET_TABLE_SIZE_IN_BYTES_POSTGRESQL = "SELECT pg_total_relation_size(\'%s\') \"Table Size\"";
        java.lang.String GET_TABLE_SIZE_IN_BYTES_MYSQL = ("SELECT (data_length + index_length) \"Table Size\" FROM information_schema.TABLES WHERE table_schema = \"" + schemaName) + "\" AND table_name =\"%s\"";
        java.lang.String GET_TABLE_SIZE_IN_BYTES_ORACLE = "SELECT bytes \"Table Size\" FROM user_segments WHERE segment_type=\'TABLE\' AND segment_name=\'%s\'";
        java.lang.String GET_ROW_COUNT_QUERY = "SELECT COUNT(*) FROM %s";
        java.util.Map<org.apache.ambari.server.orm.DBAccessor.DbType, java.lang.String> tableSizeQueryMap = new java.util.HashMap<>();
        tableSizeQueryMap.put(org.apache.ambari.server.orm.DBAccessor.DbType.POSTGRES, GET_TABLE_SIZE_IN_BYTES_POSTGRESQL);
        tableSizeQueryMap.put(org.apache.ambari.server.orm.DBAccessor.DbType.MYSQL, GET_TABLE_SIZE_IN_BYTES_MYSQL);
        tableSizeQueryMap.put(org.apache.ambari.server.orm.DBAccessor.DbType.ORACLE, GET_TABLE_SIZE_IN_BYTES_ORACLE);
        java.util.List<java.lang.String> tablesToCheck = java.util.Arrays.asList("host_role_command", "execution_command", "stage", "request", "alert_history");
        final double TABLE_SIZE_LIMIT_MB = 3000.0;
        final int TABLE_ROW_COUNT_LIMIT = 3000000;
        java.lang.String findTableSizeQuery = tableSizeQueryMap.get(dbType);
        if (dbType == org.apache.ambari.server.orm.DBAccessor.DbType.ORACLE) {
            for (int i = 0; i < tablesToCheck.size(); i++) {
                tablesToCheck.set(i, tablesToCheck.get(i).toUpperCase());
            }
        }
        for (java.lang.String tableName : tablesToCheck) {
            java.sql.ResultSet rs = null;
            java.sql.Statement statement = null;
            java.lang.Double tableSizeInMB = null;
            java.lang.Long tableSizeInBytes = null;
            int tableRowCount = -1;
            try {
                statement = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.connection.createStatement(java.sql.ResultSet.TYPE_SCROLL_SENSITIVE, java.sql.ResultSet.CONCUR_UPDATABLE);
                rs = statement.executeQuery(java.lang.String.format(findTableSizeQuery, tableName));
                if (rs != null) {
                    while (rs.next()) {
                        tableSizeInBytes = rs.getLong(1);
                        if (tableSizeInBytes != null) {
                            tableSizeInMB = (tableSizeInBytes / 1024.0) / 1024.0;
                        }
                    } 
                }
                if ((tableSizeInMB != null) && (tableSizeInMB > TABLE_SIZE_LIMIT_MB)) {
                    org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.warning("The database table {} is currently {} MB (limit is {}) and may impact performance. It is recommended " + "that you reduce its size by executing \"ambari-server db-purge-history\".", tableName, tableSizeInMB, TABLE_SIZE_LIMIT_MB);
                } else if ((tableSizeInMB != null) && (tableSizeInMB < TABLE_SIZE_LIMIT_MB)) {
                    org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.info(java.lang.String.format("The database table %s is currently %.3f MB and is within normal limits (%.3f)", tableName, tableSizeInMB, TABLE_SIZE_LIMIT_MB));
                } else {
                    throw new java.lang.Exception();
                }
            } catch (java.lang.Exception e) {
                org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.error(java.lang.String.format("Failed to get %s table size from database, will check row count: ", tableName), e);
                try {
                    rs = statement.executeQuery(java.lang.String.format(GET_ROW_COUNT_QUERY, tableName));
                    if (rs != null) {
                        while (rs.next()) {
                            tableRowCount = rs.getInt(1);
                        } 
                    }
                    if (tableRowCount > TABLE_ROW_COUNT_LIMIT) {
                        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.warning("The database table {} currently has {} rows (limit is {}) and may impact performance. It is " + "recommended that you reduce its size by executing \"ambari-server db-purge-history\".", tableName, tableRowCount, TABLE_ROW_COUNT_LIMIT);
                    } else if ((tableRowCount != (-1)) && (tableRowCount < TABLE_ROW_COUNT_LIMIT)) {
                        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.info(java.lang.String.format("The database table %s currently has %d rows and is within normal limits (%d)", tableName, tableRowCount, TABLE_ROW_COUNT_LIMIT));
                    } else {
                        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.warning("Unable to get size for table {}!", tableName);
                    }
                } catch (java.sql.SQLException ex) {
                    org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.warning(java.lang.String.format("Failed to get %s row count: ", tableName), e);
                }
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (java.sql.SQLException e) {
                        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.error("Exception occurred during result set closing procedure: ", e);
                    }
                }
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (java.sql.SQLException e) {
                        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.error("Exception occurred during statement closing procedure: ", e);
                    }
                }
            }
        }
    }

    static void checkForConfigsSelectedMoreThanOnce() {
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.info("Checking for more than 1 configuration of the same type being enabled.");
        java.lang.String GET_CONFIGS_SELECTED_MORE_THAN_ONCE_QUERY = "select c.cluster_name, cc.type_name from clusterconfig cc " + (("join clusters c on cc.cluster_id=c.cluster_id " + "group by c.cluster_name, cc.type_name ") + "having sum(selected) > 1");
        com.google.common.collect.Multimap<java.lang.String, java.lang.String> clusterConfigTypeMap = com.google.common.collect.HashMultimap.create();
        java.sql.ResultSet rs = null;
        java.sql.Statement statement = null;
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.ensureConnection();
        try {
            statement = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.connection.createStatement(java.sql.ResultSet.TYPE_SCROLL_SENSITIVE, java.sql.ResultSet.CONCUR_UPDATABLE);
            rs = statement.executeQuery(GET_CONFIGS_SELECTED_MORE_THAN_ONCE_QUERY);
            if (rs != null) {
                while (rs.next()) {
                    clusterConfigTypeMap.put(rs.getString("cluster_name"), rs.getString("type_name"));
                } 
                for (java.lang.String clusterName : clusterConfigTypeMap.keySet()) {
                    org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.error("You have config(s), in cluster {}, that is(are) selected more than once in clusterconfig table: {}", clusterName, org.apache.commons.lang.StringUtils.join(clusterConfigTypeMap.get(clusterName), ","));
                }
            }
        } catch (java.sql.SQLException e) {
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.warning("Exception occurred during check for config selected more than once procedure: ", e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (java.sql.SQLException e) {
                    org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.error("Exception occurred during result set closing procedure: ", e);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (java.sql.SQLException e) {
                    org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.error("Exception occurred during statement closing procedure: ", e);
                }
            }
        }
    }

    static void checkForHostsWithoutState() {
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.info("Checking for hosts without state");
        java.lang.String GET_HOSTS_WITHOUT_STATUS_QUERY = "select host_name from hosts where host_id not in (select host_id from hoststate)";
        java.util.Set<java.lang.String> hostsWithoutStatus = new java.util.HashSet<>();
        java.sql.ResultSet rs = null;
        java.sql.Statement statement = null;
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.ensureConnection();
        try {
            statement = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.connection.createStatement(java.sql.ResultSet.TYPE_SCROLL_SENSITIVE, java.sql.ResultSet.CONCUR_UPDATABLE);
            rs = statement.executeQuery(GET_HOSTS_WITHOUT_STATUS_QUERY);
            if (rs != null) {
                while (rs.next()) {
                    hostsWithoutStatus.add(rs.getString("host_name"));
                } 
                if (!hostsWithoutStatus.isEmpty()) {
                    org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.warning("You have host(s) without state (in hoststate table): " + org.apache.commons.lang.StringUtils.join(hostsWithoutStatus, ","));
                }
            }
        } catch (java.sql.SQLException e) {
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.warning("Exception occurred during check for host without state procedure: ", e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (java.sql.SQLException e) {
                    org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.error("Exception occurred during result set closing procedure: ", e);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (java.sql.SQLException e) {
                    org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.error("Exception occurred during statement closing procedure: ", e);
                }
            }
        }
    }

    private static int runQuery(java.sql.Statement statement, java.lang.String query) {
        java.sql.ResultSet rs = null;
        int result = 0;
        try {
            rs = statement.executeQuery(query);
            if (rs != null) {
                while (rs.next()) {
                    result = rs.getInt(1);
                } 
            }
        } catch (java.sql.SQLException e) {
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.warning("Exception occurred during topology request tables check: ", e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (java.sql.SQLException e) {
                    org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.error("Exception occurred during result set closing procedure: ", e);
                }
            }
        }
        return result;
    }

    static void checkHostComponentStates() {
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.info("Checking host component states count equals host component desired states count");
        java.lang.String GET_HOST_COMPONENT_STATE_COUNT_QUERY = "select count(*) from hostcomponentstate";
        java.lang.String GET_HOST_COMPONENT_DESIRED_STATE_COUNT_QUERY = "select count(*) from hostcomponentdesiredstate";
        java.lang.String GET_MERGED_TABLE_ROW_COUNT_QUERY = "select count(*) FROM hostcomponentstate hcs " + "JOIN hostcomponentdesiredstate hcds ON hcs.service_name=hcds.service_name AND hcs.component_name=hcds.component_name AND hcs.host_id=hcds.host_id";
        java.lang.String GET_HOST_COMPONENT_STATE_DUPLICATES_QUERY = "select component_name, host_id from hostcomponentstate group by component_name, host_id having count(component_name) > 1";
        int hostComponentStateCount = 0;
        int hostComponentDesiredStateCount = 0;
        java.util.Map<java.lang.String, java.lang.String> hostComponentStateDuplicates = new java.util.HashMap<>();
        int mergedCount = 0;
        java.sql.ResultSet rs = null;
        java.sql.Statement statement = null;
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.ensureConnection();
        try {
            statement = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.connection.createStatement(java.sql.ResultSet.TYPE_SCROLL_SENSITIVE, java.sql.ResultSet.CONCUR_UPDATABLE);
            rs = statement.executeQuery(GET_HOST_COMPONENT_STATE_COUNT_QUERY);
            if (rs != null) {
                while (rs.next()) {
                    hostComponentStateCount = rs.getInt(1);
                } 
            }
            rs = statement.executeQuery(GET_HOST_COMPONENT_DESIRED_STATE_COUNT_QUERY);
            if (rs != null) {
                while (rs.next()) {
                    hostComponentDesiredStateCount = rs.getInt(1);
                } 
            }
            rs = statement.executeQuery(GET_MERGED_TABLE_ROW_COUNT_QUERY);
            if (rs != null) {
                while (rs.next()) {
                    mergedCount = rs.getInt(1);
                } 
            }
            if ((hostComponentStateCount != hostComponentDesiredStateCount) || (hostComponentStateCount != mergedCount)) {
                org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.warning("Your host component states (hostcomponentstate table) count not equals host component desired states (hostcomponentdesiredstate table) count!");
            }
            rs = statement.executeQuery(GET_HOST_COMPONENT_STATE_DUPLICATES_QUERY);
            if (rs != null) {
                while (rs.next()) {
                    hostComponentStateDuplicates.put(rs.getString("component_name"), rs.getString("host_id"));
                } 
            }
            for (java.util.Map.Entry<java.lang.String, java.lang.String> component : hostComponentStateDuplicates.entrySet()) {
                org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.warning("Component {} on host with id {}, has more than one host component state (hostcomponentstate table)!", component.getKey(), component.getValue());
            }
        } catch (java.sql.SQLException e) {
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.warning("Exception occurred during check for same count of host component states and host component desired states: ", e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (java.sql.SQLException e) {
                    org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.error("Exception occurred during result set closing procedure: ", e);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (java.sql.SQLException e) {
                    org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.error("Exception occurred during statement closing procedure: ", e);
                }
            }
        }
    }

    @com.google.inject.persist.Transactional
    static void fixClusterConfigsNotMappedToAnyService() {
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.info("Checking for configs not mapped to any Service");
        org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.injector.getInstance(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity> notMappedClusterConfigs = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.getNotMappedClusterConfigsToService();
        for (org.apache.ambari.server.orm.entities.ClusterConfigEntity clusterConfigEntity : notMappedClusterConfigs) {
            if (clusterConfigEntity.isUnmapped()) {
                continue;
            }
            java.util.List<java.lang.String> types = new java.util.ArrayList<>();
            java.lang.String type = clusterConfigEntity.getType();
            types.add(type);
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.error("Removing config that is not mapped to any service", clusterConfigEntity);
            clusterDAO.removeConfig(clusterConfigEntity);
        }
    }

    private static java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity> getNotMappedClusterConfigsToService() {
        javax.inject.Provider<javax.persistence.EntityManager> entityManagerProvider = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.injector.getProvider(javax.persistence.EntityManager.class);
        javax.persistence.EntityManager entityManager = entityManagerProvider.get();
        java.lang.String queryName = "ClusterConfigEntity.findNotMappedClusterConfigsToService";
        javax.persistence.TypedQuery<org.apache.ambari.server.orm.entities.ClusterConfigEntity> query = entityManager.createNamedQuery(queryName, org.apache.ambari.server.orm.entities.ClusterConfigEntity.class);
        return query.getResultList();
    }

    static void checkForConfigsNotMappedToService() {
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.info("Checking for configs that are not mapped to any service");
        java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity> notMappedClasterConfigs = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.getNotMappedClusterConfigsToService();
        java.util.Set<java.lang.String> nonMappedConfigs = new java.util.HashSet<>();
        for (org.apache.ambari.server.orm.entities.ClusterConfigEntity clusterConfigEntity : notMappedClasterConfigs) {
            if (!clusterConfigEntity.isUnmapped()) {
                nonMappedConfigs.add((clusterConfigEntity.getType() + '-') + clusterConfigEntity.getTag());
            }
        }
        if (!nonMappedConfigs.isEmpty()) {
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.warning("You have config(s): {} that is(are) not mapped (in serviceconfigmapping table) to any service!", org.apache.commons.lang.StringUtils.join(nonMappedConfigs, ","));
        }
    }

    @com.google.inject.persist.Transactional
    static void fixHostComponentStatesCountEqualsHostComponentsDesiredStates() {
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.info("Checking that there are the same number of actual and desired host components");
        org.apache.ambari.server.orm.dao.HostComponentStateDAO hostComponentStateDAO = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.injector.getInstance(org.apache.ambari.server.orm.dao.HostComponentStateDAO.class);
        org.apache.ambari.server.orm.dao.HostComponentDesiredStateDAO hostComponentDesiredStateDAO = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.injector.getInstance(org.apache.ambari.server.orm.dao.HostComponentDesiredStateDAO.class);
        java.util.List<org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity> hostComponentDesiredStates = hostComponentDesiredStateDAO.findAll();
        java.util.List<org.apache.ambari.server.orm.entities.HostComponentStateEntity> hostComponentStates = hostComponentStateDAO.findAll();
        java.util.Set<org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity> missedHostComponentDesiredStates = new java.util.HashSet<>();
        missedHostComponentDesiredStates.addAll(hostComponentDesiredStates);
        java.util.Set<org.apache.ambari.server.orm.entities.HostComponentStateEntity> missedHostComponentStates = new java.util.HashSet<>();
        missedHostComponentStates.addAll(hostComponentStates);
        for (java.util.Iterator<org.apache.ambari.server.orm.entities.HostComponentStateEntity> stateIterator = missedHostComponentStates.iterator(); stateIterator.hasNext();) {
            org.apache.ambari.server.orm.entities.HostComponentStateEntity hostComponentStateEntity = stateIterator.next();
            for (java.util.Iterator<org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity> desiredStateIterator = missedHostComponentDesiredStates.iterator(); desiredStateIterator.hasNext();) {
                org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity hostComponentDesiredStateEntity = desiredStateIterator.next();
                if ((hostComponentStateEntity.getComponentName().equals(hostComponentDesiredStateEntity.getComponentName()) && hostComponentStateEntity.getServiceName().equals(hostComponentDesiredStateEntity.getServiceName())) && hostComponentStateEntity.getHostId().equals(hostComponentDesiredStateEntity.getHostId())) {
                    desiredStateIterator.remove();
                    stateIterator.remove();
                    break;
                }
            }
        }
        for (org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity hostComponentDesiredStateEntity : missedHostComponentDesiredStates) {
            org.apache.ambari.server.orm.entities.HostComponentStateEntity stateEntity = new org.apache.ambari.server.orm.entities.HostComponentStateEntity();
            stateEntity.setClusterId(hostComponentDesiredStateEntity.getClusterId());
            stateEntity.setComponentName(hostComponentDesiredStateEntity.getComponentName());
            stateEntity.setServiceName(hostComponentDesiredStateEntity.getServiceName());
            stateEntity.setVersion(org.apache.ambari.server.state.State.UNKNOWN.toString());
            stateEntity.setHostEntity(hostComponentDesiredStateEntity.getHostEntity());
            stateEntity.setCurrentState(org.apache.ambari.server.state.State.UNKNOWN);
            stateEntity.setUpgradeState(org.apache.ambari.server.state.UpgradeState.NONE);
            stateEntity.setServiceComponentDesiredStateEntity(hostComponentDesiredStateEntity.getServiceComponentDesiredStateEntity());
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.error("Trying to add missing record in hostcomponentstate: {}", stateEntity);
            hostComponentStateDAO.create(stateEntity);
        }
        for (org.apache.ambari.server.orm.entities.HostComponentStateEntity missedHostComponentState : missedHostComponentStates) {
            org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity stateEntity = new org.apache.ambari.server.orm.entities.HostComponentDesiredStateEntity();
            stateEntity.setClusterId(missedHostComponentState.getClusterId());
            stateEntity.setComponentName(missedHostComponentState.getComponentName());
            stateEntity.setServiceName(missedHostComponentState.getServiceName());
            stateEntity.setHostEntity(missedHostComponentState.getHostEntity());
            stateEntity.setDesiredState(org.apache.ambari.server.state.State.UNKNOWN);
            stateEntity.setServiceComponentDesiredStateEntity(missedHostComponentState.getServiceComponentDesiredStateEntity());
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.error("Trying to add missing record in hostcomponentdesiredstate: {}", stateEntity);
            hostComponentDesiredStateDAO.create(stateEntity);
        }
    }

    static void checkSchemaName() {
        org.apache.ambari.server.configuration.Configuration conf = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.injector.getInstance(org.apache.ambari.server.configuration.Configuration.class);
        if (conf.getDatabaseType() == org.apache.ambari.server.configuration.Configuration.DatabaseType.POSTGRES) {
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.info("Ensuring that the schema set for Postgres is correct");
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.ensureConnection();
            try (java.sql.ResultSet schemaRs = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.connection.getMetaData().getSchemas();java.sql.ResultSet searchPathRs = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.connection.createStatement().executeQuery("show search_path");java.sql.ResultSet ambariTablesRs = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.connection.createStatement().executeQuery("select table_schema from information_schema.tables where table_name = 'hostcomponentdesiredstate'")) {
                final boolean ambariSchemaExists = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.getResultSetColumn(schemaRs, "TABLE_SCHEM").contains(conf.getDatabaseSchema());
                if (!ambariSchemaExists) {
                    org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.warning("The schema [{}] defined for Ambari from ambari.properties has not been found in the database. " + "Storing Ambari tables under a different schema can lead to problems.", conf.getDatabaseSchema());
                }
                java.util.List<java.lang.Object> searchPathResultColumn = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.getResultSetColumn(searchPathRs, "search_path");
                java.util.List<java.lang.String> searchPath = (searchPathResultColumn.isEmpty()) ? com.google.common.collect.ImmutableList.of() : com.google.common.collect.ImmutableList.copyOf(com.google.common.base.Splitter.on(",").trimResults().split(java.lang.String.valueOf(searchPathResultColumn.get(0))));
                java.lang.String firstSearchPathItem = (searchPath.isEmpty()) ? null : searchPath.get(0);
                if (!java.util.Objects.equals(firstSearchPathItem, conf.getDatabaseSchema())) {
                    org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.warning("The schema [{}] defined for Ambari in ambari.properties is not first on the search path:" + " {}. This can lead to problems.", conf.getDatabaseSchema(), searchPath);
                }
                java.util.ArrayList<java.lang.Object> schemasWithAmbariTables = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.getResultSetColumn(ambariTablesRs, "table_schema");
                if (ambariSchemaExists && (!schemasWithAmbariTables.contains(conf.getDatabaseSchema()))) {
                    org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.warning("The schema [{}] defined for Ambari from ambari.properties does not contain the Ambari tables. " + "Storing Ambari tables under a different schema can lead to problems.", conf.getDatabaseSchema());
                }
                if (schemasWithAmbariTables.size() > 1) {
                    org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.warning("Multiple schemas contain the Ambari tables: {}. This can lead to problems.", schemasWithAmbariTables);
                }
            } catch (java.sql.SQLException e) {
                org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.warning("Exception occurred during checking db schema name: ", e);
            }
        }
    }

    private static java.util.ArrayList<java.lang.Object> getResultSetColumn(@javax.annotation.Nullable
    java.sql.ResultSet rs, java.lang.String columnName) throws java.sql.SQLException {
        java.util.ArrayList<java.lang.Object> values = new java.util.ArrayList<>();
        if (null != rs) {
            while (rs.next()) {
                values.add(rs.getObject(columnName));
            } 
        }
        return values;
    }

    static void checkMySQLEngine() {
        org.apache.ambari.server.configuration.Configuration conf = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.injector.getInstance(org.apache.ambari.server.configuration.Configuration.class);
        if (conf.getDatabaseType() != org.apache.ambari.server.configuration.Configuration.DatabaseType.MYSQL) {
            return;
        }
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.info("Checking to ensure that the MySQL DB engine type is set to InnoDB");
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.ensureConnection();
        java.lang.String GET_INNODB_ENGINE_SUPPORT = "select TABLE_NAME, ENGINE from information_schema.tables where TABLE_SCHEMA = '%s' and LOWER(ENGINE) != 'innodb';";
        java.sql.ResultSet rs = null;
        java.sql.Statement statement;
        try {
            statement = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.connection.createStatement(java.sql.ResultSet.TYPE_SCROLL_SENSITIVE, java.sql.ResultSet.CONCUR_UPDATABLE);
            rs = statement.executeQuery(java.lang.String.format(GET_INNODB_ENGINE_SUPPORT, conf.getDatabaseSchema()));
            if (rs != null) {
                java.util.List<java.lang.String> tablesInfo = new java.util.ArrayList<>();
                while (rs.next()) {
                    tablesInfo.add(rs.getString("TABLE_NAME"));
                } 
                if (!tablesInfo.isEmpty()) {
                    org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.warning("Found tables with engine type that is not InnoDB : {}", tablesInfo);
                }
            }
        } catch (java.sql.SQLException e) {
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.warning("Exception occurred during checking MySQL engine to be innodb: ", e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (java.sql.SQLException e) {
                    org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.error("Exception occurred during result set closing procedure: ", e);
                }
            }
        }
    }

    static java.util.Map<java.lang.String, java.lang.String> checkForStalealertdefs() {
        org.apache.ambari.server.configuration.Configuration conf = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.injector.getInstance(org.apache.ambari.server.configuration.Configuration.class);
        java.util.Map<java.lang.String, java.lang.String> alertInfo = new java.util.HashMap<>();
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.info("Checking to ensure there is no stale alert definitions");
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.ensureConnection();
        java.lang.String STALE_ALERT_DEFINITIONS = "select definition_name, service_name from alert_definition where service_name not in " + "(select service_name from clusterservices) and service_name not in ('AMBARI')";
        java.sql.ResultSet rs = null;
        java.sql.Statement statement;
        try {
            statement = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.connection.createStatement(java.sql.ResultSet.TYPE_SCROLL_SENSITIVE, java.sql.ResultSet.CONCUR_UPDATABLE);
            rs = statement.executeQuery(STALE_ALERT_DEFINITIONS);
            if (rs != null) {
                while (rs.next()) {
                    alertInfo.put(rs.getString("definition_name"), rs.getString("service_name"));
                } 
                if (!alertInfo.isEmpty()) {
                    java.lang.String alertInfoStr = "";
                    for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : alertInfo.entrySet()) {
                        alertInfoStr = ((entry.getKey() + "(") + entry.getValue()) + ")";
                    }
                    org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.warning("You have Alerts that are not mapped with any services : {}.Run --auto-fix-database to fix " + "this automatically. Please backup Ambari Server database before running --auto-fix-database.", alertInfoStr);
                }
            }
        } catch (java.sql.SQLException e) {
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.warning("Exception occurred during checking for stale alert definitions: ", e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (java.sql.SQLException e) {
                    org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.error("Exception occurred during  checking for stale alert definitions: ", e);
                }
            }
        }
        return alertInfo;
    }

    @com.google.inject.persist.Transactional
    static void fixConfigsSelectedMoreThanOnce() {
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.info("Fix configs selected more than once");
        org.apache.ambari.server.orm.dao.ClusterDAO clusterDAO = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.injector.getInstance(org.apache.ambari.server.orm.dao.ClusterDAO.class);
        org.apache.ambari.server.state.Clusters clusters = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        com.google.common.collect.Multimap<java.lang.String, java.lang.String> clusterConfigTypeMap = com.google.common.collect.HashMultimap.create();
        java.sql.ResultSet rs = null;
        java.sql.Statement statement = null;
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.ensureConnection();
        try {
            statement = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.connection.createStatement(java.sql.ResultSet.TYPE_SCROLL_SENSITIVE, java.sql.ResultSet.CONCUR_UPDATABLE);
            rs = statement.executeQuery(org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.GET_CONFIGS_SELECTED_MORE_THAN_ONCE_QUERY);
            if (rs != null) {
                while (rs.next()) {
                    clusterConfigTypeMap.put(rs.getString("cluster_name"), rs.getString("type_name"));
                } 
            }
        } catch (java.sql.SQLException e) {
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.warning("Exception occurred during check for config selected more than once procedure: ", e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (java.sql.SQLException e) {
                    org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.error("Exception occurred during result set closing procedure: ", e);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (java.sql.SQLException e) {
                    org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.error("Exception occurred during statement closing procedure: ", e);
                }
            }
        }
        for (java.lang.String clusterName : clusterConfigTypeMap.keySet()) {
            org.apache.ambari.server.state.Cluster cluster = null;
            try {
                cluster = clusters.getCluster(clusterName);
                java.util.Collection<java.lang.String> typesWithMultipleSelectedConfigs = clusterConfigTypeMap.get(clusterName);
                for (java.lang.String type : typesWithMultipleSelectedConfigs) {
                    java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity> enabledConfigsByType = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.getEnabledConfigsByType(cluster.getClusterId(), type);
                    org.apache.ambari.server.orm.entities.ClusterConfigEntity latestConfig = enabledConfigsByType.get(0);
                    for (org.apache.ambari.server.orm.entities.ClusterConfigEntity entity : enabledConfigsByType) {
                        entity.setSelected(false);
                        if (latestConfig.getSelectedTimestamp() < entity.getSelectedTimestamp()) {
                            latestConfig = entity;
                        }
                        clusterDAO.merge(entity, true);
                    }
                    latestConfig.setSelected(true);
                    clusterDAO.merge(latestConfig, true);
                }
            } catch (org.apache.ambari.server.AmbariException e) {
                org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.warning("Exception occurred during fix for config selected more than once procedure: ", e);
            }
        }
    }

    private static java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity> getEnabledConfigsByType(long clusterId, java.lang.String type) {
        javax.inject.Provider<javax.persistence.EntityManager> entityManagerProvider = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.injector.getProvider(javax.persistence.EntityManager.class);
        javax.persistence.EntityManager entityManager = entityManagerProvider.get();
        javax.persistence.Query query = entityManager.createNamedQuery("ClusterConfigEntity.findEnabledConfigByType", org.apache.ambari.server.orm.entities.ClusterConfigEntity.class);
        query.setParameter("clusterId", clusterId);
        query.setParameter("type", type);
        return ((java.util.List<org.apache.ambari.server.orm.entities.ClusterConfigEntity>) (query.getResultList()));
    }

    static void checkServiceConfigs() {
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.info("Checking services and their configs");
        java.lang.String GET_SERVICES_WITHOUT_CONFIGS_QUERY = "select c.cluster_name, service_name from clusterservices cs " + ("join clusters c on cs.cluster_id=c.cluster_id " + "where service_name not in (select service_name from serviceconfig sc where sc.cluster_id=cs.cluster_id and sc.service_name=cs.service_name and sc.group_id is null)");
        java.lang.String GET_SERVICE_CONFIG_WITHOUT_MAPPING_QUERY = "select c.cluster_name, sc.service_name, sc.version from serviceconfig sc " + ("join clusters c on sc.cluster_id=c.cluster_id " + "where service_config_id not in (select service_config_id from serviceconfigmapping) and group_id is null");
        java.lang.String GET_STACK_NAME_VERSION_QUERY = "select c.cluster_name, s.stack_name, s.stack_version from clusters c " + "join stack s on c.desired_stack_id = s.stack_id";
        java.lang.String GET_SERVICES_WITH_CONFIGS_QUERY = "select c.cluster_name, cs.service_name, cc.type_name, sc.version from clusterservices cs " + ((((("join serviceconfig sc on cs.service_name=sc.service_name and cs.cluster_id=sc.cluster_id " + "join serviceconfigmapping scm on sc.service_config_id=scm.service_config_id ") + "join clusterconfig cc on scm.config_id=cc.config_id and sc.cluster_id=cc.cluster_id ") + "join clusters c on cc.cluster_id=c.cluster_id and sc.stack_id=c.desired_stack_id ") + "where sc.group_id is null and sc.service_config_id=(select max(service_config_id) from serviceconfig sc2 where sc2.service_name=sc.service_name and sc2.cluster_id=sc.cluster_id) ") + "group by c.cluster_name, cs.service_name, cc.type_name, sc.version");
        java.lang.String GET_NOT_SELECTED_SERVICE_CONFIGS_QUERY = "select c.cluster_name, cs.service_name, cc.type_name from clusterservices cs " + (((((("join serviceconfig sc on cs.service_name=sc.service_name and cs.cluster_id=sc.cluster_id " + "join serviceconfigmapping scm on sc.service_config_id=scm.service_config_id ") + "join clusterconfig cc on scm.config_id=cc.config_id and cc.cluster_id=sc.cluster_id ") + "join clusters c on cc.cluster_id=c.cluster_id ") + "where sc.group_id is null and sc.service_config_id = (select max(service_config_id) from serviceconfig sc2 where sc2.service_name=sc.service_name and sc2.cluster_id=sc.cluster_id) ") + "group by c.cluster_name, cs.service_name, cc.type_name ") + "having sum(cc.selected) < 1");
        com.google.common.collect.Multimap<java.lang.String, java.lang.String> clusterServiceMap = com.google.common.collect.HashMultimap.create();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterStackInfo = new java.util.HashMap<>();
        java.util.Map<java.lang.String, com.google.common.collect.Multimap<java.lang.String, java.lang.String>> clusterServiceVersionMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, com.google.common.collect.Multimap<java.lang.String, java.lang.String>> clusterServiceConfigType = new java.util.HashMap<>();
        java.sql.ResultSet rs = null;
        java.sql.Statement statement = null;
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.ensureConnection();
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.info("Getting ambari metainfo instance");
        if (org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.ambariMetaInfo == null) {
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.ambariMetaInfo = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        }
        try {
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.info("Executing query 'GET_SERVICES_WITHOUT_CONFIGS'");
            statement = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.connection.createStatement(java.sql.ResultSet.TYPE_SCROLL_SENSITIVE, java.sql.ResultSet.CONCUR_UPDATABLE);
            rs = statement.executeQuery(GET_SERVICES_WITHOUT_CONFIGS_QUERY);
            if (rs != null) {
                while (rs.next()) {
                    clusterServiceMap.put(rs.getString("cluster_name"), rs.getString("service_name"));
                } 
                for (java.lang.String clusterName : clusterServiceMap.keySet()) {
                    org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.warning("Service(s): {}, from cluster {} has no config(s) in serviceconfig table!", org.apache.commons.lang.StringUtils.join(clusterServiceMap.get(clusterName), ","), clusterName);
                }
            }
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.info("Executing query 'GET_SERVICE_CONFIG_WITHOUT_MAPPING'");
            rs = statement.executeQuery(GET_SERVICE_CONFIG_WITHOUT_MAPPING_QUERY);
            if (rs != null) {
                java.lang.String serviceName = null;
                java.lang.String version = null;
                java.lang.String clusterName = null;
                while (rs.next()) {
                    serviceName = rs.getString("service_name");
                    clusterName = rs.getString("cluster_name");
                    version = rs.getString("version");
                    if (clusterServiceVersionMap.get(clusterName) != null) {
                        com.google.common.collect.Multimap<java.lang.String, java.lang.String> serviceVersion = clusterServiceVersionMap.get(clusterName);
                        serviceVersion.put(serviceName, version);
                    } else {
                        com.google.common.collect.Multimap<java.lang.String, java.lang.String> serviceVersion = com.google.common.collect.HashMultimap.create();
                        serviceVersion.put(serviceName, version);
                        clusterServiceVersionMap.put(clusterName, serviceVersion);
                    }
                } 
                for (java.lang.String clName : clusterServiceVersionMap.keySet()) {
                    com.google.common.collect.Multimap<java.lang.String, java.lang.String> serviceVersion = clusterServiceVersionMap.get(clName);
                    for (java.lang.String servName : serviceVersion.keySet()) {
                        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.warning("In cluster {}, service config mapping is unavailable (in table serviceconfigmapping) for service {} with version(s) {}! ", clName, servName, org.apache.commons.lang.StringUtils.join(serviceVersion.get(servName), ","));
                    }
                }
            }
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.info("Getting stack info from database");
            rs = statement.executeQuery(GET_STACK_NAME_VERSION_QUERY);
            if (rs != null) {
                while (rs.next()) {
                    java.util.Map<java.lang.String, java.lang.String> stackInfoMap = new java.util.HashMap<>();
                    stackInfoMap.put(rs.getString("stack_name"), rs.getString("stack_version"));
                    clusterStackInfo.put(rs.getString("cluster_name"), stackInfoMap);
                } 
            }
            java.util.Set<java.lang.String> serviceNames = new java.util.HashSet<>();
            java.util.Map<java.lang.String, java.util.Map<java.lang.Integer, com.google.common.collect.Multimap<java.lang.String, java.lang.String>>> dbClusterServiceVersionConfigs = new java.util.HashMap<>();
            com.google.common.collect.Multimap<java.lang.String, java.lang.String> stackServiceConfigs = com.google.common.collect.HashMultimap.create();
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.info("Executing query 'GET_SERVICES_WITH_CONFIGS'");
            rs = statement.executeQuery(GET_SERVICES_WITH_CONFIGS_QUERY);
            if (rs != null) {
                java.lang.String serviceName = null;
                java.lang.String configType = null;
                java.lang.String clusterName = null;
                java.lang.Integer serviceVersion = null;
                while (rs.next()) {
                    clusterName = rs.getString("cluster_name");
                    serviceName = rs.getString("service_name");
                    configType = rs.getString("type_name");
                    serviceVersion = rs.getInt("version");
                    serviceNames.add(serviceName);
                    if (dbClusterServiceVersionConfigs.get(clusterName) != null) {
                        java.util.Map<java.lang.Integer, com.google.common.collect.Multimap<java.lang.String, java.lang.String>> dbServiceVersionConfigs = dbClusterServiceVersionConfigs.get(clusterName);
                        if (dbServiceVersionConfigs.get(serviceVersion) != null) {
                            dbServiceVersionConfigs.get(serviceVersion).put(serviceName, configType);
                        } else {
                            com.google.common.collect.Multimap<java.lang.String, java.lang.String> dbServiceConfigs = com.google.common.collect.HashMultimap.create();
                            dbServiceConfigs.put(serviceName, configType);
                            dbServiceVersionConfigs.put(serviceVersion, dbServiceConfigs);
                        }
                    } else {
                        java.util.Map<java.lang.Integer, com.google.common.collect.Multimap<java.lang.String, java.lang.String>> dbServiceVersionConfigs = new java.util.HashMap<>();
                        com.google.common.collect.Multimap<java.lang.String, java.lang.String> dbServiceConfigs = com.google.common.collect.HashMultimap.create();
                        dbServiceConfigs.put(serviceName, configType);
                        dbServiceVersionConfigs.put(serviceVersion, dbServiceConfigs);
                        dbClusterServiceVersionConfigs.put(clusterName, dbServiceVersionConfigs);
                    }
                } 
            }
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.info("Comparing service configs from stack with configs that we got from db");
            for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterStackInfoEntry : clusterStackInfo.entrySet()) {
                java.lang.String clusterName = clusterStackInfoEntry.getKey();
                java.util.Map<java.lang.String, java.lang.String> stackInfo = clusterStackInfoEntry.getValue();
                java.lang.String stackName = stackInfo.keySet().iterator().next();
                java.lang.String stackVersion = stackInfo.get(stackName);
                org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.info("Getting services from metainfo");
                java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceInfo> serviceInfoMap = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.ambariMetaInfo.getServices(stackName, stackVersion);
                for (java.lang.String serviceName : serviceNames) {
                    org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.info("Processing {}-{} / {}", stackName, stackVersion, serviceName);
                    org.apache.ambari.server.state.ServiceInfo serviceInfo = serviceInfoMap.get(serviceName);
                    if (serviceInfo != null) {
                        java.util.Set<java.lang.String> configTypes = serviceInfo.getConfigTypeAttributes().keySet();
                        for (java.lang.String configType : configTypes) {
                            stackServiceConfigs.put(serviceName, configType);
                        }
                    } else {
                        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.warning("Service {} is not available for stack {} in cluster {}", serviceName, (stackName + "-") + stackVersion, clusterName);
                    }
                }
                org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.info("Comparing required service configs from stack with mapped service configs from db");
                java.util.Map<java.lang.Integer, com.google.common.collect.Multimap<java.lang.String, java.lang.String>> dbServiceVersionConfigs = dbClusterServiceVersionConfigs.get(clusterName);
                if (dbServiceVersionConfigs != null) {
                    for (java.lang.Integer serviceVersion : dbServiceVersionConfigs.keySet()) {
                        com.google.common.collect.Multimap<java.lang.String, java.lang.String> dbServiceConfigs = dbServiceVersionConfigs.get(serviceVersion);
                        if (dbServiceConfigs != null) {
                            for (java.lang.String serviceName : dbServiceConfigs.keySet()) {
                                org.apache.ambari.server.state.ServiceInfo serviceInfo = serviceInfoMap.get(serviceName);
                                java.util.Collection<java.lang.String> serviceConfigsFromStack = stackServiceConfigs.get(serviceName);
                                java.util.Collection<java.lang.String> serviceConfigsFromDB = dbServiceConfigs.get(serviceName);
                                if ((serviceConfigsFromDB != null) && (serviceConfigsFromStack != null)) {
                                    serviceConfigsFromStack.removeAll(serviceConfigsFromDB);
                                    if ((serviceInfo != null) && (serviceInfo.getComponents() != null)) {
                                        for (org.apache.ambari.server.state.ComponentInfo componentInfo : serviceInfo.getComponents()) {
                                            if (componentInfo.getClientConfigFiles() != null) {
                                                for (org.apache.ambari.server.state.ClientConfigFileDefinition clientConfigFileDefinition : componentInfo.getClientConfigFiles()) {
                                                    if (clientConfigFileDefinition.isOptional()) {
                                                        serviceConfigsFromStack.remove(clientConfigFileDefinition.getDictionaryName());
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    if (!dbServiceConfigs.containsKey("RANGER")) {
                                        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.removeStringsByRegexp(serviceConfigsFromStack, (("^ranger-" + serviceName.toLowerCase()) + "-") + "*");
                                    }
                                    if (!serviceConfigsFromStack.isEmpty()) {
                                        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.warning("Required config(s): {} is(are) not available for service {} with service config version {} in cluster {}", org.apache.commons.lang.StringUtils.join(serviceConfigsFromStack, ","), serviceName, java.lang.Integer.toString(serviceVersion), clusterName);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.info("Getting services which has mapped configs which are not selected in clusterconfig");
            rs = statement.executeQuery(GET_NOT_SELECTED_SERVICE_CONFIGS_QUERY);
            if (rs != null) {
                java.lang.String serviceName = null;
                java.lang.String configType = null;
                java.lang.String clusterName = null;
                while (rs.next()) {
                    clusterName = rs.getString("cluster_name");
                    serviceName = rs.getString("service_name");
                    configType = rs.getString("type_name");
                    if (clusterServiceConfigType.get(clusterName) != null) {
                        com.google.common.collect.Multimap<java.lang.String, java.lang.String> serviceConfigs = clusterServiceConfigType.get(clusterName);
                        serviceConfigs.put(serviceName, configType);
                    } else {
                        com.google.common.collect.Multimap<java.lang.String, java.lang.String> serviceConfigs = com.google.common.collect.HashMultimap.create();
                        serviceConfigs.put(serviceName, configType);
                        clusterServiceConfigType.put(clusterName, serviceConfigs);
                    }
                } 
            }
            for (java.lang.String clusterName : clusterServiceConfigType.keySet()) {
                com.google.common.collect.Multimap<java.lang.String, java.lang.String> serviceConfig = clusterServiceConfigType.get(clusterName);
                for (java.lang.String serviceName : serviceConfig.keySet()) {
                    org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.warning("You have non selected configs: {} for service {} from cluster {}!", org.apache.commons.lang.StringUtils.join(serviceConfig.get(serviceName), ","), serviceName, clusterName);
                }
            }
        } catch (java.sql.SQLException | org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.warning("Exception occurred during complex service check procedure: ", e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (java.sql.SQLException e) {
                    org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.error("Exception occurred during result set closing procedure: ", e);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (java.sql.SQLException e) {
                    org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.error("Exception occurred during statement closing procedure: ", e);
                }
            }
        }
    }

    static java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> collectConfigGroupsWithoutServiceName() {
        java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> configGroupMap = new java.util.HashMap<>();
        org.apache.ambari.server.state.Clusters clusters = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = clusters.getClusters();
        if (org.apache.commons.collections.MapUtils.isEmpty(clusterMap))
            return configGroupMap;

        for (org.apache.ambari.server.state.Cluster cluster : clusterMap.values()) {
            java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> configGroups = cluster.getConfigGroups();
            if (org.apache.commons.collections.MapUtils.isEmpty(configGroups)) {
                continue;
            }
            for (org.apache.ambari.server.state.configgroup.ConfigGroup configGroup : configGroups.values()) {
                if (org.apache.commons.lang.StringUtils.isEmpty(configGroup.getServiceName())) {
                    configGroupMap.put(configGroup.getId(), configGroup);
                }
            }
        }
        return configGroupMap;
    }

    static void checkConfigGroupsHasServiceName() {
        java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> configGroupMap = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.collectConfigGroupsWithoutServiceName();
        if (org.apache.commons.collections.MapUtils.isEmpty(configGroupMap))
            return;

        java.lang.String message = java.lang.String.join(" ), ( ", configGroupMap.values().stream().map(org.apache.ambari.server.state.configgroup.ConfigGroup::getName).collect(java.util.stream.Collectors.toList()));
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.warning("You have config groups present in the database with no " + ("service name, [(ConfigGroup) => ( {} )]. Run --auto-fix-database to fix " + "this automatically. Please backup Ambari Server database before running --auto-fix-database."), message);
    }

    @com.google.inject.persist.Transactional
    static void fixConfigGroupServiceNames() {
        java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> configGroupMap = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.collectConfigGroupsWithoutServiceName();
        if (org.apache.commons.collections.MapUtils.isEmpty(configGroupMap))
            return;

        org.apache.ambari.server.state.Clusters clusters = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        for (java.util.Map.Entry<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> configGroupEntry : configGroupMap.entrySet()) {
            org.apache.ambari.server.state.configgroup.ConfigGroup configGroup = configGroupEntry.getValue();
            try {
                org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(configGroup.getClusterName());
                java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> serviceMap = cluster.getServices();
                if (serviceMap.containsKey(configGroup.getTag())) {
                    org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.info("Setting service name of config group {} with id {} to {}", configGroup.getName(), configGroupEntry.getKey(), configGroup.getTag());
                    configGroup.setServiceName(configGroup.getTag());
                } else {
                    org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.info("Config group {} with id {} contains a tag {} which is not a service name in the cluster {}", configGroup.getName(), configGroupEntry.getKey(), configGroup.getTag(), cluster.getClusterName());
                }
            } catch (org.apache.ambari.server.AmbariException e) {
            }
        }
    }

    static java.util.Map<java.lang.Long, java.util.Set<java.lang.Long>> checkConfigGroupHostMapping(boolean warnIfFound) {
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.info("Checking config group host mappings");
        java.util.Map<java.lang.Long, java.util.Set<java.lang.Long>> nonMappedHostIds = new java.util.HashMap<>();
        org.apache.ambari.server.state.Clusters clusters = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = clusters.getClusters();
        java.lang.StringBuilder output = new java.lang.StringBuilder("[(ConfigGroup, Service, HostCount) => ");
        if (!org.apache.commons.collections.MapUtils.isEmpty(clusterMap)) {
            for (org.apache.ambari.server.state.Cluster cluster : clusterMap.values()) {
                java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> configGroups = cluster.getConfigGroups();
                java.util.Map<java.lang.String, org.apache.ambari.server.state.Host> clusterHosts = clusters.getHostsForCluster(cluster.getClusterName());
                if ((!org.apache.commons.collections.MapUtils.isEmpty(configGroups)) && (!org.apache.commons.collections.MapUtils.isEmpty(clusterHosts))) {
                    for (org.apache.ambari.server.state.configgroup.ConfigGroup configGroup : configGroups.values()) {
                        java.util.Map<java.lang.Long, org.apache.ambari.server.state.Host> hosts = configGroup.getHosts();
                        boolean addToOutput = false;
                        java.util.Set<java.lang.String> hostnames = new java.util.HashSet<>();
                        if (!org.apache.commons.collections.MapUtils.isEmpty(hosts)) {
                            for (org.apache.ambari.server.state.Host host : hosts.values()) {
                                if (!clusterHosts.containsKey(host.getHostName())) {
                                    java.util.Set<java.lang.Long> hostIds = nonMappedHostIds.computeIfAbsent(configGroup.getId(), configGroupId -> new java.util.HashSet<>());
                                    hostIds.add(host.getHostId());
                                    hostnames.add(host.getHostName());
                                    addToOutput = true;
                                }
                            }
                        }
                        if (addToOutput) {
                            output.append("( ");
                            output.append(configGroup.getName());
                            output.append(", ");
                            output.append(configGroup.getTag());
                            output.append(", ");
                            output.append(hostnames);
                            output.append(" ), ");
                        }
                    }
                }
            }
        }
        if ((!org.apache.commons.collections.MapUtils.isEmpty(nonMappedHostIds)) && warnIfFound) {
            output.replace(output.lastIndexOf(","), output.length(), "]");
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.warning("You have config group host mappings with hosts that are no " + (("longer associated with the cluster, {}. Run --auto-fix-database to " + "fix this automatically. Alternatively, you can remove this mapping ") + "from the UI. Please backup Ambari Server database before running --auto-fix-database."), output.toString());
        }
        return nonMappedHostIds;
    }

    static java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> checkConfigGroupsForDeletedServices(boolean warnIfFound) {
        java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> configGroupMap = new java.util.HashMap<>();
        org.apache.ambari.server.state.Clusters clusters = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = clusters.getClusters();
        java.lang.StringBuilder output = new java.lang.StringBuilder("[(ConfigGroup, Service) => ");
        if (!org.apache.commons.collections.MapUtils.isEmpty(clusterMap)) {
            for (org.apache.ambari.server.state.Cluster cluster : clusterMap.values()) {
                java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> configGroups = cluster.getConfigGroups();
                java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> services = cluster.getServices();
                if (!org.apache.commons.collections.MapUtils.isEmpty(configGroups)) {
                    for (org.apache.ambari.server.state.configgroup.ConfigGroup configGroup : configGroups.values()) {
                        if (!services.containsKey(configGroup.getServiceName())) {
                            configGroupMap.put(configGroup.getId(), configGroup);
                            output.append("( ");
                            output.append(configGroup.getName());
                            output.append(", ");
                            output.append(configGroup.getServiceName());
                            output.append(" ), ");
                        }
                    }
                }
            }
        }
        if (warnIfFound && (!configGroupMap.isEmpty())) {
            output.replace(output.lastIndexOf(","), output.length(), "]");
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.warning("You have config groups present in the database with no " + ("corresponding service found, {}. Run --auto-fix-database to fix " + "this automatically. Please backup Ambari Server database before running --auto-fix-database."), output.toString());
        }
        return configGroupMap;
    }

    @com.google.inject.persist.Transactional
    static void fixConfigGroupsForDeletedServices() {
        java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> configGroupMap = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkConfigGroupsForDeletedServices(false);
        org.apache.ambari.server.state.Clusters clusters = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        if (!org.apache.commons.collections.MapUtils.isEmpty(configGroupMap)) {
            for (java.util.Map.Entry<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> configGroupEntry : configGroupMap.entrySet()) {
                java.lang.Long id = configGroupEntry.getKey();
                org.apache.ambari.server.state.configgroup.ConfigGroup configGroup = configGroupEntry.getValue();
                if (!org.apache.commons.lang.StringUtils.isEmpty(configGroup.getServiceName())) {
                    org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.info("Deleting config group {} with id {} for deleted service {}", configGroup.getName(), id, configGroup.getServiceName());
                    try {
                        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(configGroup.getClusterName());
                        cluster.deleteConfigGroup(id);
                    } catch (org.apache.ambari.server.AmbariException e) {
                    }
                } else {
                    org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.warning("The config group {} with id {} can not be fixed automatically because service name is missing.", configGroup.getName(), id);
                }
            }
        }
    }

    @com.google.inject.persist.Transactional
    static void fixAlertsForDeletedServices() {
        org.apache.ambari.server.configuration.Configuration conf = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.injector.getInstance(org.apache.ambari.server.configuration.Configuration.class);
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.info("fixAlertsForDeletedServices stale alert definitions for deleted services");
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.ensureConnection();
        java.lang.String SELECT_STALE_ALERT_DEFINITIONS = "select definition_id from alert_definition where service_name not in " + "(select service_name from clusterservices) and service_name not in ('AMBARI')";
        int recordsCount = 0;
        java.sql.Statement statement = null;
        java.sql.ResultSet rs = null;
        java.util.List<java.lang.Integer> alertIds = new java.util.ArrayList<java.lang.Integer>();
        try {
            statement = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.connection.createStatement(java.sql.ResultSet.TYPE_SCROLL_SENSITIVE, java.sql.ResultSet.CONCUR_UPDATABLE);
            rs = statement.executeQuery(SELECT_STALE_ALERT_DEFINITIONS);
            while (rs.next()) {
                alertIds.add(rs.getInt("definition_id"));
            } 
        } catch (java.sql.SQLException e) {
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.warning("Exception occurred during fixing stale alert definitions: ", e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (java.sql.SQLException e) {
                    org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.error("Exception occurred during fixing stale alert definitions: ", e);
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (java.sql.SQLException e) {
                    org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.error("Exception occurred during fixing stale alert definitions: ", e);
                }
            }
        }
        for (java.lang.Integer alertId : alertIds) {
            final org.apache.ambari.server.orm.entities.AlertDefinitionEntity entity = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.alertDefinitionDAO.findById(alertId.intValue());
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.alertDefinitionDAO.remove(entity);
        }
        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.warning("fixAlertsForDeletedServices - {}  Stale alerts were deleted", alertIds.size());
    }

    @com.google.inject.persist.Transactional
    static void fixConfigGroupHostMappings() {
        java.util.Map<java.lang.Long, java.util.Set<java.lang.Long>> nonMappedHostIds = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.checkConfigGroupHostMapping(false);
        org.apache.ambari.server.state.Clusters clusters = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        if (!org.apache.commons.collections.MapUtils.isEmpty(nonMappedHostIds)) {
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.info("Fixing {} config groups with inconsistent host mappings", nonMappedHostIds.size());
            for (java.util.Map.Entry<java.lang.Long, java.util.Set<java.lang.Long>> nonMappedHostEntry : nonMappedHostIds.entrySet()) {
                if (!org.apache.commons.collections.MapUtils.isEmpty(clusters.getClusters())) {
                    for (org.apache.ambari.server.state.Cluster cluster : clusters.getClusters().values()) {
                        java.util.Map<java.lang.Long, org.apache.ambari.server.state.configgroup.ConfigGroup> configGroups = cluster.getConfigGroups();
                        if (!org.apache.commons.collections.MapUtils.isEmpty(configGroups)) {
                            org.apache.ambari.server.state.configgroup.ConfigGroup configGroup = configGroups.get(nonMappedHostEntry.getKey());
                            if (configGroup != null) {
                                for (java.lang.Long hostId : nonMappedHostEntry.getValue()) {
                                    try {
                                        configGroup.removeHost(hostId);
                                    } catch (org.apache.ambari.server.AmbariException e) {
                                        org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.warn("Unable to fix inconsistency by removing host " + "mapping for config group: {}, service: {}, hostId = {}", configGroup.getName(), configGroup.getTag(), hostId);
                                    }
                                }
                            } else {
                                org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.LOG.warn("Unable to find config group with id = {}", nonMappedHostEntry.getKey());
                            }
                        }
                    }
                }
            }
        }
    }

    private static void ensureConnection() {
        if (org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.connection == null) {
            if (org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.dbAccessor == null) {
                org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.dbAccessor = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.injector.getInstance(org.apache.ambari.server.orm.DBAccessor.class);
            }
            org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.connection = org.apache.ambari.server.checks.DatabaseConsistencyCheckHelper.dbAccessor.getConnection();
        }
    }

    private static void removeStringsByRegexp(java.util.Collection<java.lang.String> stringItems, java.lang.String regexp) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regexp);
        for (java.util.Iterator<java.lang.String> iterator = stringItems.iterator(); iterator.hasNext();) {
            java.lang.String stringItem = iterator.next();
            java.util.regex.Matcher matcher = pattern.matcher(stringItem);
            if (matcher.find()) {
                iterator.remove();
            }
        }
    }
}