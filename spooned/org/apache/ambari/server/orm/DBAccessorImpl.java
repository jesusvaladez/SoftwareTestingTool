package org.apache.ambari.server.orm;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.persistence.internal.helper.DBPlatformHelper;
import org.eclipse.persistence.internal.sessions.DatabaseSessionImpl;
import org.eclipse.persistence.logging.AbstractSessionLog;
import org.eclipse.persistence.logging.SessionLogEntry;
import org.eclipse.persistence.platform.database.DatabasePlatform;
import org.eclipse.persistence.platform.database.DerbyPlatform;
import org.eclipse.persistence.platform.database.H2Platform;
import org.eclipse.persistence.platform.database.MySQLPlatform;
import org.eclipse.persistence.platform.database.OraclePlatform;
import org.eclipse.persistence.platform.database.PostgreSQLPlatform;
import org.eclipse.persistence.sessions.DatabaseLogin;
import org.eclipse.persistence.sessions.DatabaseSession;
import org.springframework.jdbc.support.JdbcUtils;
@com.google.inject.Singleton
public class DBAccessorImpl implements org.apache.ambari.server.orm.DBAccessor {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.orm.DBAccessorImpl.class);

    public static final java.lang.String USER = "user";

    public static final java.lang.String PASSWORD = "password";

    public static final java.lang.String NULL_CATALOG_MEANS_CURRENT = "nullCatalogMeansCurrent";

    public static final java.lang.String TRUE = "true";

    public static final int SUPPORT_CONNECTOR_VERSION = 5;

    private final org.eclipse.persistence.platform.database.DatabasePlatform databasePlatform;

    private final java.sql.Connection connection;

    private final org.apache.ambari.server.orm.helpers.dbms.DbmsHelper dbmsHelper;

    private org.apache.ambari.server.configuration.Configuration configuration;

    private java.sql.DatabaseMetaData databaseMetaData;

    private static final java.lang.String dbURLPatternString = "jdbc:(.*?):.*";

    private org.apache.ambari.server.orm.DBAccessor.DbType dbType;

    private final java.lang.String dbSchema;

    @com.google.inject.Inject
    public DBAccessorImpl(org.apache.ambari.server.configuration.Configuration configuration) {
        this.configuration = configuration;
        try {
            java.lang.Class.forName(configuration.getDatabaseDriver());
            connection = getNewConnection();
            connection.setAutoCommit(true);
            java.lang.String vendorName = connection.getMetaData().getDatabaseProductName() + connection.getMetaData().getDatabaseMajorVersion();
            java.lang.String dbPlatform = org.eclipse.persistence.internal.helper.DBPlatformHelper.getDBPlatform(vendorName, new org.eclipse.persistence.logging.AbstractSessionLog() {
                @java.lang.Override
                public void log(org.eclipse.persistence.logging.SessionLogEntry sessionLogEntry) {
                    org.apache.ambari.server.orm.DBAccessorImpl.LOG.debug(sessionLogEntry.getMessage());
                }
            });
            databasePlatform = ((org.eclipse.persistence.platform.database.DatabasePlatform) (java.lang.Class.forName(dbPlatform).newInstance()));
            dbmsHelper = loadHelper(databasePlatform);
            dbSchema = convertObjectName(configuration.getDatabaseSchema());
        } catch (java.lang.Exception e) {
            java.lang.String message = "";
            if (e instanceof java.lang.ClassNotFoundException) {
                message = "If you are using a non-default database for Ambari and a custom JDBC driver jar, you need to set property \"server.jdbc.driver.path={path/to/custom_jdbc_driver}\" " + "in ambari.properties config file, to include it in ambari-server classpath.";
            } else {
                message = "Error while creating database accessor ";
            }
            org.apache.ambari.server.orm.DBAccessorImpl.LOG.error(message, e);
            throw new java.lang.RuntimeException(message, e);
        }
    }

    protected org.apache.ambari.server.orm.helpers.dbms.DbmsHelper loadHelper(org.eclipse.persistence.platform.database.DatabasePlatform databasePlatform) {
        if (databasePlatform instanceof org.eclipse.persistence.platform.database.OraclePlatform) {
            dbType = org.apache.ambari.server.orm.DBAccessor.DbType.ORACLE;
            return new org.apache.ambari.server.orm.helpers.dbms.OracleHelper(databasePlatform);
        } else if (databasePlatform instanceof org.eclipse.persistence.platform.database.MySQLPlatform) {
            dbType = org.apache.ambari.server.orm.DBAccessor.DbType.MYSQL;
            return new org.apache.ambari.server.orm.helpers.dbms.MySqlHelper(databasePlatform);
        } else if (databasePlatform instanceof org.eclipse.persistence.platform.database.PostgreSQLPlatform) {
            dbType = org.apache.ambari.server.orm.DBAccessor.DbType.POSTGRES;
            return new org.apache.ambari.server.orm.helpers.dbms.PostgresHelper(databasePlatform);
        } else if (databasePlatform instanceof org.eclipse.persistence.platform.database.DerbyPlatform) {
            dbType = org.apache.ambari.server.orm.DBAccessor.DbType.DERBY;
            return new org.apache.ambari.server.orm.helpers.dbms.DerbyHelper(databasePlatform);
        } else if (databasePlatform instanceof org.eclipse.persistence.platform.database.H2Platform) {
            dbType = org.apache.ambari.server.orm.DBAccessor.DbType.H2;
            return new org.apache.ambari.server.orm.helpers.dbms.H2Helper(databasePlatform);
        } else {
            dbType = org.apache.ambari.server.orm.DBAccessor.DbType.UNKNOWN;
            return new org.apache.ambari.server.orm.helpers.dbms.GenericDbmsHelper(databasePlatform);
        }
    }

    private static java.lang.Class<?> fromSqlTypeToClass(int type) {
        switch (type) {
            case java.sql.Types.VARCHAR :
            case java.sql.Types.CHAR :
            case java.sql.Types.LONGVARCHAR :
                return java.lang.String.class;
            case java.sql.Types.NUMERIC :
            case java.sql.Types.DECIMAL :
                return java.math.BigDecimal.class;
            case java.sql.Types.BIT :
                return java.lang.Boolean.class;
            case java.sql.Types.TINYINT :
                return java.lang.Byte.class;
            case java.sql.Types.SMALLINT :
                return java.lang.Short.class;
            case java.sql.Types.INTEGER :
                return java.lang.Integer.class;
            case java.sql.Types.BIGINT :
                return java.lang.Long.class;
            case java.sql.Types.FLOAT :
            case java.sql.Types.REAL :
                return java.lang.Float.class;
            case java.sql.Types.DOUBLE :
                return java.lang.Double.class;
            case java.sql.Types.BINARY :
            case java.sql.Types.VARBINARY :
            case java.sql.Types.LONGVARBINARY :
                return java.lang.Byte[].class;
            case java.sql.Types.DATE :
                return java.sql.Date.class;
            case java.sql.Types.TIME :
                return java.sql.Timestamp.class;
            default :
                return null;
        }
    }

    @java.lang.Override
    public java.sql.Connection getConnection() {
        return connection;
    }

    @java.lang.Override
    public java.sql.Connection getNewConnection() {
        try {
            java.util.Properties properties = new java.util.Properties();
            properties.setProperty(org.apache.ambari.server.orm.DBAccessorImpl.USER, configuration.getDatabaseUser());
            properties.setProperty(org.apache.ambari.server.orm.DBAccessorImpl.PASSWORD, configuration.getDatabasePassword());
            if (configuration.getDatabaseUrl().contains("mysql") && (java.sql.DriverManager.getDriver(configuration.getDatabaseUrl()).getMajorVersion() > org.apache.ambari.server.orm.DBAccessorImpl.SUPPORT_CONNECTOR_VERSION)) {
                properties.setProperty(org.apache.ambari.server.orm.DBAccessorImpl.NULL_CATALOG_MEANS_CURRENT, org.apache.ambari.server.orm.DBAccessorImpl.TRUE);
            }
            return java.sql.DriverManager.getConnection(configuration.getDatabaseUrl(), properties);
        } catch (java.sql.SQLException e) {
            throw new java.lang.RuntimeException("Unable to connect to database", e);
        }
    }

    @java.lang.Override
    public java.lang.String quoteObjectName(java.lang.String name) {
        return dbmsHelper.quoteObjectName(name);
    }

    @java.lang.Override
    public void createTable(java.lang.String tableName, java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> columnInfo, java.lang.String... primaryKeyColumns) throws java.sql.SQLException {
        if (tableExists(tableName)) {
            return;
        }
        primaryKeyColumns = org.apache.commons.lang.ArrayUtils.nullToEmpty(primaryKeyColumns);
        java.lang.String query = dbmsHelper.getCreateTableStatement(tableName, columnInfo, java.util.Arrays.asList(primaryKeyColumns));
        executeQuery(query);
    }

    protected java.sql.DatabaseMetaData getDatabaseMetaData() throws java.sql.SQLException {
        if (databaseMetaData == null) {
            databaseMetaData = connection.getMetaData();
        }
        return databaseMetaData;
    }

    private java.lang.String convertObjectName(java.lang.String objectName) throws java.sql.SQLException {
        if (objectName == null) {
            return null;
        }
        java.sql.DatabaseMetaData metaData = getDatabaseMetaData();
        if (metaData.storesLowerCaseIdentifiers()) {
            return objectName.toLowerCase();
        } else if (metaData.storesUpperCaseIdentifiers()) {
            return objectName.toUpperCase();
        }
        return objectName;
    }

    private void setArgumentsForPreparedStatement(java.sql.PreparedStatement preparedStatement, java.lang.Object[] arguments) throws java.sql.SQLException {
        for (int i = 0; i < arguments.length; i++) {
            if (arguments[i] instanceof byte[]) {
                byte[] binaryData = ((byte[]) (arguments[i]));
                preparedStatement.setBinaryStream(i + 1, new java.io.ByteArrayInputStream(binaryData), binaryData.length);
            } else {
                preparedStatement.setObject(i + 1, arguments[i]);
            }
        }
    }

    @java.lang.Override
    public boolean tableExists(java.lang.String tableName) throws java.sql.SQLException {
        boolean result = false;
        java.sql.DatabaseMetaData metaData = getDatabaseMetaData();
        java.sql.ResultSet res = metaData.getTables(null, dbSchema, convertObjectName(tableName), new java.lang.String[]{ "TABLE" });
        if (res != null) {
            try {
                if (res.next()) {
                    result = (res.getString("TABLE_NAME") != null) && res.getString("TABLE_NAME").equalsIgnoreCase(tableName);
                }
                if (res.next()) {
                    throw new java.lang.IllegalStateException(java.lang.String.format("Request for table [%s] existing returned more than one results", tableName));
                }
            } finally {
                res.close();
            }
        }
        return result;
    }

    @java.lang.Override
    public org.apache.ambari.server.orm.DBAccessor.DbType getDbType() {
        return dbType;
    }

    @java.lang.Override
    public java.lang.String getDbSchema() {
        return dbSchema;
    }

    @java.lang.Override
    public boolean tableHasData(java.lang.String tableName) throws java.sql.SQLException {
        java.lang.String query = "SELECT count(*) from " + tableName;
        java.sql.Statement statement = getConnection().createStatement();
        boolean retVal = false;
        java.sql.ResultSet rs = null;
        try {
            rs = statement.executeQuery(query);
            if (rs != null) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.orm.DBAccessorImpl.LOG.error((("Unable to check if table " + tableName) + " has any data. Exception: ") + e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return retVal;
    }

    @java.lang.Override
    public boolean tableHasColumn(java.lang.String tableName, java.lang.String columnName) throws java.sql.SQLException {
        boolean result = false;
        java.sql.DatabaseMetaData metaData = getDatabaseMetaData();
        java.sql.ResultSet rs = metaData.getColumns(null, dbSchema, convertObjectName(tableName), convertObjectName(columnName));
        if (rs != null) {
            try {
                if (rs.next()) {
                    result = (rs.getString("COLUMN_NAME") != null) && rs.getString("COLUMN_NAME").equalsIgnoreCase(columnName);
                }
                if (rs.next()) {
                    throw new java.lang.IllegalStateException(java.lang.String.format("Request for column [%s] existing in table [%s] returned more than one results", columnName, tableName));
                }
            } finally {
                rs.close();
            }
        }
        return result;
    }

    @java.lang.Override
    public boolean tableHasColumn(java.lang.String tableName, java.lang.String... columnName) throws java.sql.SQLException {
        java.util.List<java.lang.String> columnsList = new java.util.ArrayList<>(java.util.Arrays.asList(columnName));
        java.sql.DatabaseMetaData metaData = getDatabaseMetaData();
        org.apache.ambari.server.utils.CustomStringUtils.toUpperCase(columnsList);
        java.util.Set<java.lang.String> columnsListToCheckCopies = new java.util.HashSet<>(columnsList);
        java.util.List<java.lang.String> duplicatedColumns = new java.util.ArrayList<>();
        java.sql.ResultSet rs = metaData.getColumns(null, dbSchema, convertObjectName(tableName), null);
        if (rs != null) {
            try {
                while (rs.next()) {
                    java.lang.String actualColumnName = rs.getString("COLUMN_NAME");
                    if (actualColumnName != null) {
                        boolean removingResult = columnsList.remove(actualColumnName.toUpperCase());
                        if ((!removingResult) && columnsListToCheckCopies.contains(actualColumnName.toUpperCase())) {
                            duplicatedColumns.add(actualColumnName.toUpperCase());
                        }
                    }
                } 
            } finally {
                rs.close();
            }
        }
        if (!duplicatedColumns.isEmpty()) {
            throw new java.lang.IllegalStateException(java.lang.String.format("Request for columns [%s] existing in table [%s] returned too many results [%s] for columns [%s]", java.util.Arrays.toString(columnName), tableName, duplicatedColumns.size(), duplicatedColumns.toString()));
        }
        return columnsList.size() == 0;
    }

    @java.lang.Override
    public boolean tableHasForeignKey(java.lang.String tableName, java.lang.String fkName) throws java.sql.SQLException {
        return getCheckedForeignKey(tableName, fkName) != null;
    }

    public java.lang.String getCheckedForeignKey(java.lang.String rawTableName, java.lang.String rawForeignKeyName) throws java.sql.SQLException {
        java.sql.DatabaseMetaData metaData = getDatabaseMetaData();
        java.lang.String tableName = convertObjectName(rawTableName);
        java.lang.String foreignKeyName = convertObjectName(rawForeignKeyName);
        try (java.sql.ResultSet rs = metaData.getImportedKeys(null, dbSchema, tableName)) {
            while (rs.next()) {
                java.lang.String foundName = rs.getString("FK_NAME");
                if (org.apache.commons.lang.StringUtils.equals(foreignKeyName, foundName)) {
                    return foundName;
                }
            } 
        }
        org.apache.ambari.server.configuration.Configuration.DatabaseType databaseType = configuration.getDatabaseType();
        if (databaseType == org.apache.ambari.server.configuration.Configuration.DatabaseType.ORACLE) {
            try (java.sql.PreparedStatement ps = getConnection().prepareStatement("SELECT constraint_name FROM user_constraints WHERE table_name = ? AND constraint_type = 'R' AND constraint_name = ?")) {
                ps.setString(1, tableName);
                ps.setString(2, foreignKeyName);
                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return foreignKeyName;
                    }
                }
            }
        }
        org.apache.ambari.server.orm.DBAccessorImpl.LOG.warn("FK {} not found for table {}", foreignKeyName, tableName);
        return null;
    }

    @java.lang.Override
    public boolean tableHasForeignKey(java.lang.String tableName, java.lang.String refTableName, java.lang.String columnName, java.lang.String refColumnName) throws java.sql.SQLException {
        return tableHasForeignKey(tableName, refTableName, new java.lang.String[]{ columnName }, new java.lang.String[]{ refColumnName });
    }

    @java.lang.Override
    public boolean tableHasForeignKey(java.lang.String tableName, java.lang.String referenceTableName, java.lang.String[] keyColumns, java.lang.String[] referenceColumns) throws java.sql.SQLException {
        java.sql.DatabaseMetaData metaData = getDatabaseMetaData();
        java.sql.ResultSet rs = metaData.getCrossReference(null, dbSchema, convertObjectName(referenceTableName), null, dbSchema, convertObjectName(tableName));
        java.util.List<java.lang.String> pkColumns = new java.util.ArrayList<>(referenceColumns.length);
        for (java.lang.String referenceColumn : referenceColumns) {
            pkColumns.add(convertObjectName(referenceColumn));
        }
        java.util.List<java.lang.String> fkColumns = new java.util.ArrayList<>(keyColumns.length);
        for (java.lang.String keyColumn : keyColumns) {
            fkColumns.add(convertObjectName(keyColumn));
        }
        if (rs != null) {
            try {
                while (rs.next()) {
                    java.lang.String pkColumn = rs.getString("PKCOLUMN_NAME");
                    java.lang.String fkColumn = rs.getString("FKCOLUMN_NAME");
                    int pkIndex = pkColumns.indexOf(pkColumn);
                    int fkIndex = fkColumns.indexOf(fkColumn);
                    if ((pkIndex != (-1)) && (fkIndex != (-1))) {
                        if (pkIndex != fkIndex) {
                            org.apache.ambari.server.orm.DBAccessorImpl.LOG.warn("Columns for FK constraint should be provided in exact order");
                        } else {
                            pkColumns.remove(pkIndex);
                            fkColumns.remove(fkIndex);
                        }
                    } else {
                        org.apache.ambari.server.orm.DBAccessorImpl.LOG.debug("pkCol={}, fkCol={} not found in provided column names, skipping", pkColumn, fkColumn);
                    }
                } 
                if (pkColumns.isEmpty() && fkColumns.isEmpty()) {
                    return true;
                }
            } finally {
                rs.close();
            }
        }
        return false;
    }

    @java.lang.Override
    public boolean tableHasIndex(java.lang.String tableName, boolean unique, java.lang.String indexName) throws java.sql.SQLException {
        if (tableExists(tableName)) {
            java.util.List<java.lang.String> indexList = getIndexesList(tableName, false);
            return org.apache.ambari.server.utils.CustomStringUtils.containsCaseInsensitive(indexName, indexList);
        }
        return false;
    }

    @java.lang.Override
    public void createIndex(java.lang.String indexName, java.lang.String tableName, java.lang.String... columnNames) throws java.sql.SQLException {
        createIndex(indexName, tableName, false, columnNames);
    }

    @java.lang.Override
    public void createIndex(java.lang.String indexName, java.lang.String tableName, boolean isUnique, java.lang.String... columnNames) throws java.sql.SQLException {
        if (!tableHasIndex(tableName, false, indexName)) {
            java.lang.String query = dbmsHelper.getCreateIndexStatement(indexName, tableName, isUnique, columnNames);
            executeQuery(query);
        } else {
            org.apache.ambari.server.orm.DBAccessorImpl.LOG.info("Index {} already exist, skipping creation, table = {}", indexName, tableName);
        }
    }

    @java.lang.Override
    public void addFKConstraint(java.lang.String tableName, java.lang.String constraintName, java.lang.String keyColumn, java.lang.String referenceTableName, java.lang.String referenceColumn, boolean ignoreFailure) throws java.sql.SQLException {
        addFKConstraint(tableName, constraintName, new java.lang.String[]{ keyColumn }, referenceTableName, new java.lang.String[]{ referenceColumn }, false, ignoreFailure);
    }

    @java.lang.Override
    public void addFKConstraint(java.lang.String tableName, java.lang.String constraintName, java.lang.String keyColumn, java.lang.String referenceTableName, java.lang.String referenceColumn, boolean shouldCascadeOnDelete, boolean ignoreFailure) throws java.sql.SQLException {
        addFKConstraint(tableName, constraintName, new java.lang.String[]{ keyColumn }, referenceTableName, new java.lang.String[]{ referenceColumn }, shouldCascadeOnDelete, ignoreFailure);
    }

    @java.lang.Override
    public void addFKConstraint(java.lang.String tableName, java.lang.String constraintName, java.lang.String[] keyColumns, java.lang.String referenceTableName, java.lang.String[] referenceColumns, boolean ignoreFailure) throws java.sql.SQLException {
        addFKConstraint(tableName, constraintName, keyColumns, referenceTableName, referenceColumns, false, ignoreFailure);
    }

    @java.lang.Override
    public void addFKConstraint(java.lang.String tableName, java.lang.String constraintName, java.lang.String[] keyColumns, java.lang.String referenceTableName, java.lang.String[] referenceColumns, boolean shouldCascadeOnDelete, boolean ignoreFailure) throws java.sql.SQLException {
        if (!tableHasForeignKey(tableName, referenceTableName, keyColumns, referenceColumns)) {
            java.lang.String query = dbmsHelper.getAddForeignKeyStatement(tableName, constraintName, java.util.Arrays.asList(keyColumns), referenceTableName, java.util.Arrays.asList(referenceColumns), shouldCascadeOnDelete);
            try {
                executeQuery(query, ignoreFailure);
            } catch (java.sql.SQLException e) {
                org.apache.ambari.server.orm.DBAccessorImpl.LOG.warn(((("Add FK constraint failed" + ", constraintName = ") + constraintName) + ", tableName = ") + tableName, e.getMessage());
                if (!ignoreFailure) {
                    throw e;
                }
            }
        } else {
            org.apache.ambari.server.orm.DBAccessorImpl.LOG.info("Foreign Key constraint {} already exists, skipping", constraintName);
        }
    }

    public boolean tableHasConstraint(java.lang.String tableName, java.lang.String constraintName) throws java.sql.SQLException {
        java.lang.String query = dbmsHelper.getTableConstraintsStatement(connection.getCatalog(), tableName);
        java.sql.Statement statement = null;
        java.sql.ResultSet rs = null;
        try {
            statement = getConnection().createStatement();
            rs = statement.executeQuery(query);
            if (rs != null) {
                while (rs.next()) {
                    if (rs.getString("CONSTRAINT_NAME").equalsIgnoreCase(constraintName)) {
                        return true;
                    }
                } 
            }
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return false;
    }

    @java.lang.Override
    public void addUniqueConstraint(java.lang.String tableName, java.lang.String constraintName, java.lang.String... columnNames) throws java.sql.SQLException {
        if ((!tableHasConstraint(tableName, constraintName)) && tableHasColumn(tableName, columnNames)) {
            java.lang.String query = dbmsHelper.getAddUniqueConstraintStatement(tableName, constraintName, columnNames);
            try {
                executeQuery(query);
            } catch (java.sql.SQLException e) {
                org.apache.ambari.server.orm.DBAccessorImpl.LOG.warn("Add unique constraint failed, constraintName={},tableName={}", constraintName, tableName);
                throw e;
            }
        } else {
            org.apache.ambari.server.orm.DBAccessorImpl.LOG.info("Unique constraint {} already exists or columns {} not found, skipping", constraintName, org.apache.commons.lang.StringUtils.join(columnNames, ", "));
        }
    }

    @java.lang.Override
    public void updateUniqueConstraint(java.lang.String tableName, java.lang.String constraintName, java.lang.String... columnNames) throws java.sql.SQLException {
        dropUniqueConstraint(tableName, constraintName);
        addUniqueConstraint(tableName, constraintName, columnNames);
    }

    @java.lang.Override
    public void addPKConstraint(java.lang.String tableName, java.lang.String constraintName, boolean ignoreErrors, java.lang.String... columnName) throws java.sql.SQLException {
        if ((!tableHasPrimaryKey(tableName, null)) && tableHasColumn(tableName, columnName)) {
            java.lang.String query = dbmsHelper.getAddPrimaryKeyConstraintStatement(tableName, constraintName, columnName);
            executeQuery(query, ignoreErrors);
        } else {
            org.apache.ambari.server.orm.DBAccessorImpl.LOG.warn("Primary constraint {} not altered to table {} as column {} not present or constraint already exists", constraintName, tableName, columnName);
        }
    }

    @java.lang.Override
    public void addPKConstraint(java.lang.String tableName, java.lang.String constraintName, java.lang.String... columnName) throws java.sql.SQLException {
        addPKConstraint(tableName, constraintName, false, columnName);
    }

    @java.lang.Override
    public void renameColumn(java.lang.String tableName, java.lang.String oldColumnName, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo columnInfo) throws java.sql.SQLException {
        java.lang.String renameColumnStatement = dbmsHelper.getRenameColumnStatement(tableName, oldColumnName, columnInfo);
        executeQuery(renameColumnStatement);
    }

    @java.lang.Override
    public void addColumn(java.lang.String tableName, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo columnInfo) throws java.sql.SQLException {
        if (tableHasColumn(tableName, columnInfo.getName())) {
            return;
        }
        org.apache.ambari.server.configuration.Configuration.DatabaseType databaseType = configuration.getDatabaseType();
        switch (databaseType) {
            case ORACLE :
                {
                    boolean originalNullable = columnInfo.isNullable();
                    if (columnInfo.getDefaultValue() != null) {
                        columnInfo.setNullable(true);
                    }
                    java.lang.String query = dbmsHelper.getAddColumnStatement(tableName, columnInfo);
                    executeQuery(query);
                    if (columnInfo.getDefaultValue() != null) {
                        updateTable(tableName, columnInfo.getName(), columnInfo.getDefaultValue(), "");
                        if (!originalNullable) {
                            setColumnNullable(tableName, columnInfo, originalNullable);
                        }
                        addDefaultConstraint(tableName, columnInfo);
                    }
                    break;
                }
            case DERBY :
            case MYSQL :
            case POSTGRES :
            case SQL_ANYWHERE :
            case SQL_SERVER :
            default :
                {
                    java.lang.String query = dbmsHelper.getAddColumnStatement(tableName, columnInfo);
                    executeQuery(query);
                    break;
                }
        }
    }

    @java.lang.Override
    public void alterColumn(java.lang.String tableName, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo columnInfo) throws java.sql.SQLException {
        if (dbmsHelper.supportsColumnTypeChange()) {
            java.lang.String statement = dbmsHelper.getAlterColumnStatement(tableName, columnInfo);
            executeQuery(statement);
        } else {
            org.apache.ambari.server.orm.DBAccessor.DBColumnInfo columnInfoTmp = new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(columnInfo.getName() + "_TMP", columnInfo.getType(), columnInfo.getLength());
            java.lang.String statement = dbmsHelper.getAddColumnStatement(tableName, columnInfoTmp);
            executeQuery(statement);
            updateTable(tableName, columnInfo, columnInfoTmp);
            dropColumn(tableName, columnInfo.getName());
            renameColumn(tableName, columnInfoTmp.getName(), columnInfo);
        }
        if (isColumnNullable(tableName, columnInfo.getName()) != columnInfo.isNullable()) {
            setColumnNullable(tableName, columnInfo, columnInfo.isNullable());
        }
    }

    @java.lang.Override
    public void updateTable(java.lang.String tableName, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo columnNameFrom, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo columnNameTo) throws java.sql.SQLException {
        org.apache.ambari.server.orm.DBAccessorImpl.LOG.info((((("Executing query: UPDATE TABLE " + tableName) + " SET ") + columnNameTo.getName()) + "=") + columnNameFrom.getName());
        java.lang.String statement = "SELECT * FROM " + tableName;
        int typeFrom = getColumnType(tableName, columnNameFrom.getName());
        int typeTo = getColumnType(tableName, columnNameTo.getName());
        java.sql.Statement dbStatement = null;
        java.sql.ResultSet rs = null;
        try {
            dbStatement = getConnection().createStatement(java.sql.ResultSet.TYPE_SCROLL_SENSITIVE, java.sql.ResultSet.CONCUR_UPDATABLE);
            rs = dbStatement.executeQuery(statement);
            while (rs.next()) {
                convertUpdateData(rs, columnNameFrom, typeFrom, columnNameTo, typeTo);
                rs.updateRow();
            } 
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (dbStatement != null) {
                dbStatement.close();
            }
        }
    }

    private void convertUpdateData(java.sql.ResultSet rs, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo columnNameFrom, int typeFrom, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo columnNameTo, int typeTo) throws java.sql.SQLException {
        if ((typeFrom == java.sql.Types.BLOB) && (typeTo == java.sql.Types.CLOB)) {
            java.sql.Blob data = rs.getBlob(columnNameFrom.getName());
            if (data != null) {
                rs.updateClob(columnNameTo.getName(), new java.io.BufferedReader(new java.io.InputStreamReader(data.getBinaryStream(), java.nio.charset.Charset.defaultCharset())));
            }
        } else {
            java.lang.Object data = rs.getObject(columnNameFrom.getName());
            rs.updateObject(columnNameTo.getName(), data);
        }
    }

    @java.lang.Override
    public boolean insertRow(java.lang.String tableName, java.lang.String[] columnNames, java.lang.String[] values, boolean ignoreFailure) throws java.sql.SQLException {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append("INSERT INTO ").append(tableName).append("(");
        if (columnNames.length != values.length) {
            throw new java.lang.IllegalArgumentException("number of columns should be equal to number of values");
        }
        for (int i = 0; i < columnNames.length; i++) {
            builder.append(columnNames[i]);
            if (i != (columnNames.length - 1)) {
                builder.append(",");
            }
        }
        builder.append(") VALUES(");
        for (int i = 0; i < values.length; i++) {
            builder.append(values[i]);
            if (i != (values.length - 1)) {
                builder.append(",");
            }
        }
        builder.append(")");
        java.sql.Statement statement = getConnection().createStatement();
        int rowsUpdated = 0;
        java.lang.String query = builder.toString();
        try {
            rowsUpdated = statement.executeUpdate(query);
        } catch (java.sql.SQLException e) {
            org.apache.ambari.server.orm.DBAccessorImpl.LOG.warn("Unable to execute query: " + query, e);
            if (!ignoreFailure) {
                throw e;
            }
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
        return rowsUpdated != 0;
    }

    @java.lang.Override
    public boolean insertRowIfMissing(java.lang.String tableName, java.lang.String[] columnNames, java.lang.String[] values, boolean ignoreFailure) throws java.sql.SQLException {
        if (columnNames.length == 0) {
            return false;
        }
        if (columnNames.length != values.length) {
            throw new java.lang.IllegalArgumentException("number of columns should be equal to number of values");
        }
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append("SELECT COUNT(*) FROM ").append(tableName);
        builder.append(" WHERE ").append(columnNames[0]).append("=").append(values[0]);
        for (int i = 1; i < columnNames.length; i++) {
            builder.append(" AND ").append(columnNames[i]).append("=").append(values[i]);
        }
        java.sql.Statement statement = getConnection().createStatement();
        java.sql.ResultSet resultSet = null;
        int count = -1;
        java.lang.String query = builder.toString();
        try {
            resultSet = statement.executeQuery(query);
            if ((resultSet != null) && resultSet.next()) {
                count = resultSet.getInt(1);
            }
        } catch (java.sql.SQLException e) {
            org.apache.ambari.server.orm.DBAccessorImpl.LOG.warn("Unable to execute query: " + query, e);
            if (!ignoreFailure) {
                throw e;
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
        return (count == 0) && insertRow(tableName, columnNames, values, ignoreFailure);
    }

    @java.lang.Override
    public int updateTable(java.lang.String tableName, java.lang.String columnName, java.lang.Object value, java.lang.String whereClause) throws java.sql.SQLException {
        java.lang.StringBuilder query = new java.lang.StringBuilder(java.lang.String.format("UPDATE %s SET %s = ", tableName, columnName));
        query.append(escapeParameter(value));
        query.append(" ");
        query.append(whereClause);
        java.sql.Statement statement = getConnection().createStatement();
        int res = -1;
        try {
            res = statement.executeUpdate(query.toString());
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
        return res;
    }

    @java.lang.Override
    public int executeUpdate(java.lang.String query) throws java.sql.SQLException {
        return executeUpdate(query, false);
    }

    @java.lang.Override
    public int executeUpdate(java.lang.String query, boolean ignoreErrors) throws java.sql.SQLException {
        java.sql.Statement statement = getConnection().createStatement();
        try {
            return statement.executeUpdate(query);
        } catch (java.sql.SQLException e) {
            org.apache.ambari.server.orm.DBAccessorImpl.LOG.warn(((((("Error executing query: " + query) + ", ") + "errorCode = ") + e.getErrorCode()) + ", message = ") + e.getMessage());
            if (!ignoreErrors) {
                throw e;
            }
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
        return 0;
    }

    @java.lang.Override
    public void executeQuery(java.lang.String query, java.lang.String tableName, java.lang.String hasColumnName) throws java.sql.SQLException {
        if (tableHasColumn(tableName, hasColumnName)) {
            executeQuery(query);
        }
    }

    @java.lang.Override
    public void executeQuery(java.lang.String query) throws java.sql.SQLException {
        executeQuery(query, false);
    }

    @java.lang.Override
    public void executeQuery(java.lang.String query, boolean ignoreFailure) throws java.sql.SQLException {
        org.apache.ambari.server.orm.DBAccessorImpl.LOG.info("Executing query: {}", query);
        java.sql.Statement statement = getConnection().createStatement();
        try {
            statement.execute(query);
        } catch (java.sql.SQLException e) {
            if (!ignoreFailure) {
                org.apache.ambari.server.orm.DBAccessorImpl.LOG.error("Error executing query: " + query, e);
                throw e;
            } else {
                org.apache.ambari.server.orm.DBAccessorImpl.LOG.warn(((((("Error executing query: " + query) + ", ") + "errorCode = ") + e.getErrorCode()) + ", message = ") + e.getMessage());
            }
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    @java.lang.Override
    public void executePreparedQuery(java.lang.String query, java.lang.Object... arguments) throws java.sql.SQLException {
        executePreparedQuery(query, false, arguments);
    }

    @java.lang.Override
    public void executePreparedQuery(java.lang.String query, boolean ignoreFailure, java.lang.Object... arguments) throws java.sql.SQLException {
        org.apache.ambari.server.orm.DBAccessorImpl.LOG.info("Executing prepared query: {}", query);
        java.sql.PreparedStatement preparedStatement = getConnection().prepareStatement(query);
        setArgumentsForPreparedStatement(preparedStatement, arguments);
        try {
            preparedStatement.execute();
        } catch (java.sql.SQLException e) {
            if (!ignoreFailure) {
                org.apache.ambari.server.orm.DBAccessorImpl.LOG.error("Error executing prepared query: {}", query, e);
                throw e;
            } else {
                org.apache.ambari.server.orm.DBAccessorImpl.LOG.warn("Error executing prepared query: {}, errorCode={}, message = {}", query, e.getErrorCode(), e.getMessage());
            }
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    @java.lang.Override
    public void executePreparedUpdate(java.lang.String query, java.lang.Object... arguments) throws java.sql.SQLException {
        executePreparedUpdate(query, false, arguments);
    }

    @java.lang.Override
    public void executePreparedUpdate(java.lang.String query, boolean ignoreFailure, java.lang.Object... arguments) throws java.sql.SQLException {
        org.apache.ambari.server.orm.DBAccessorImpl.LOG.info("Executing prepared query: {}", query);
        java.sql.PreparedStatement preparedStatement = getConnection().prepareStatement(query);
        setArgumentsForPreparedStatement(preparedStatement, arguments);
        try {
            preparedStatement.executeUpdate();
        } catch (java.sql.SQLException e) {
            if (!ignoreFailure) {
                org.apache.ambari.server.orm.DBAccessorImpl.LOG.error("Error executing prepared query: {}", query, e);
                throw e;
            } else {
                org.apache.ambari.server.orm.DBAccessorImpl.LOG.warn("Error executing prepared query: {}, errorCode={}, message = {}", query, e.getErrorCode(), e.getMessage());
            }
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    @java.lang.Override
    public void dropTable(java.lang.String tableName) throws java.sql.SQLException {
        if (tableExists(tableName)) {
            java.lang.String query = dbmsHelper.getDropTableStatement(tableName);
            executeQuery(query);
        } else {
            org.apache.ambari.server.orm.DBAccessorImpl.LOG.warn("{} table doesn't exists, skipping", tableName);
        }
    }

    @java.lang.Override
    public void truncateTable(java.lang.String tableName) throws java.sql.SQLException {
        java.lang.String query = "DELETE FROM " + tableName;
        executeQuery(query);
    }

    @java.lang.Override
    public void dropColumn(java.lang.String tableName, java.lang.String columnName) throws java.sql.SQLException {
        if (tableHasColumn(tableName, columnName)) {
            java.lang.String query = dbmsHelper.getDropTableColumnStatement(tableName, columnName);
            executeQuery(query);
        }
    }

    @java.lang.Override
    public void dropSequence(java.lang.String sequenceName) throws java.sql.SQLException {
        executeQuery(dbmsHelper.getDropSequenceStatement(sequenceName), true);
    }

    @java.lang.Override
    public void dropFKConstraint(java.lang.String tableName, java.lang.String constraintName) throws java.sql.SQLException {
        dropFKConstraint(tableName, constraintName, false);
    }

    @java.lang.Override
    public void dropFKConstraint(java.lang.String tableName, java.lang.String constraintName, boolean ignoreFailure) throws java.sql.SQLException {
        java.lang.String checkedConstraintName = getCheckedForeignKey(convertObjectName(tableName), constraintName);
        if (checkedConstraintName != null) {
            java.lang.String query = dbmsHelper.getDropFKConstraintStatement(tableName, checkedConstraintName);
            executeQuery(query, ignoreFailure);
        } else {
            org.apache.ambari.server.orm.DBAccessorImpl.LOG.warn("Foreign key {} from {} table does not exist and will not be dropped", constraintName, tableName);
        }
        org.apache.ambari.server.configuration.Configuration.DatabaseType databaseType = configuration.getDatabaseType();
        if ((databaseType == org.apache.ambari.server.configuration.Configuration.DatabaseType.MYSQL) && tableHasIndex(tableName, false, constraintName)) {
            java.lang.String query = dbmsHelper.getDropIndexStatement(constraintName, tableName);
            executeQuery(query, true);
        }
    }

    @java.lang.Override
    public void dropUniqueConstraint(java.lang.String tableName, java.lang.String constraintName, boolean ignoreFailure) throws java.sql.SQLException {
        if (tableHasConstraint(convertObjectName(tableName), convertObjectName(constraintName))) {
            java.lang.String query = dbmsHelper.getDropUniqueConstraintStatement(tableName, constraintName);
            executeQuery(query, ignoreFailure);
        } else {
            org.apache.ambari.server.orm.DBAccessorImpl.LOG.warn("Unique constraint {} from {} table not found, nothing to drop", constraintName, tableName);
        }
    }

    @java.lang.Override
    public void dropUniqueConstraint(java.lang.String tableName, java.lang.String constraintName) throws java.sql.SQLException {
        dropUniqueConstraint(tableName, constraintName, false);
    }

    @java.lang.Override
    public void dropPKConstraint(java.lang.String tableName, java.lang.String constraintName, java.lang.String columnName, boolean cascade) throws java.sql.SQLException {
        if (tableHasPrimaryKey(tableName, columnName)) {
            java.lang.String query = dbmsHelper.getDropPrimaryKeyStatement(convertObjectName(tableName), constraintName, cascade);
            executeQuery(query, false);
        } else {
            org.apache.ambari.server.orm.DBAccessorImpl.LOG.warn("Primary key doesn't exists for {} table, skipping", tableName);
        }
    }

    @java.lang.Override
    public void dropPKConstraint(java.lang.String tableName, java.lang.String constraintName, boolean ignoreFailure, boolean cascade) throws java.sql.SQLException {
        if (tableHasPrimaryKey(tableName, null)) {
            java.lang.String query = dbmsHelper.getDropPrimaryKeyStatement(convertObjectName(tableName), constraintName, cascade);
            executeQuery(query, ignoreFailure);
        } else {
            org.apache.ambari.server.orm.DBAccessorImpl.LOG.warn("Primary key doesn't exists for {} table, skipping", tableName);
        }
    }

    @java.lang.Override
    public void dropPKConstraint(java.lang.String tableName, java.lang.String constraintName, boolean cascade) throws java.sql.SQLException {
        dropPKConstraint(tableName, constraintName, false, cascade);
    }

    @java.lang.Override
    public void executeScript(java.lang.String filePath) throws java.sql.SQLException, java.io.IOException {
        java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(filePath));
        try {
            org.apache.ambari.server.orm.helpers.ScriptRunner scriptRunner = new org.apache.ambari.server.orm.helpers.ScriptRunner(getConnection(), false, false);
            scriptRunner.runScript(br);
        } finally {
            br.close();
        }
    }

    @java.lang.Override
    public org.eclipse.persistence.sessions.DatabaseSession getNewDatabaseSession() {
        org.eclipse.persistence.sessions.DatabaseLogin login = new org.eclipse.persistence.sessions.DatabaseLogin();
        login.setUserName(configuration.getDatabaseUser());
        login.setPassword(configuration.getDatabasePassword());
        login.setDatasourcePlatform(databasePlatform);
        login.setDatabaseURL(configuration.getDatabaseUrl());
        login.setDriverClassName(configuration.getDatabaseDriver());
        return new org.eclipse.persistence.internal.sessions.DatabaseSessionImpl(login);
    }

    @java.lang.Override
    public boolean tableHasPrimaryKey(java.lang.String tableName, java.lang.String columnName) throws java.sql.SQLException {
        java.sql.ResultSet rs = getDatabaseMetaData().getPrimaryKeys(null, dbSchema, convertObjectName(tableName));
        boolean res = false;
        try {
            if ((rs != null) && (columnName != null)) {
                while (rs.next()) {
                    if (rs.getString("COLUMN_NAME").equalsIgnoreCase(columnName)) {
                        res = true;
                        break;
                    }
                } 
            } else if (rs != null) {
                res = rs.next();
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return res;
    }

    @java.lang.Override
    public int getColumnType(java.lang.String tableName, java.lang.String columnName) throws java.sql.SQLException {
        int res;
        java.lang.String query;
        java.sql.Statement statement = null;
        java.sql.ResultSet rs = null;
        java.sql.ResultSetMetaData rsmd = null;
        try {
            query = java.lang.String.format("SELECT %s FROM %s WHERE 1=2", columnName, convertObjectName(tableName));
            statement = getConnection().createStatement();
            rs = statement.executeQuery(query);
            rsmd = rs.getMetaData();
            res = rsmd.getColumnType(1);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
        return res;
    }

    @java.lang.Override
    public java.lang.Class getColumnClass(java.lang.String tableName, java.lang.String columnName) throws java.sql.SQLException, java.lang.ClassNotFoundException {
        java.lang.String query = java.lang.String.format("SELECT %s FROM %s WHERE 1=2", convertObjectName(columnName), convertObjectName(tableName));
        java.sql.Statement statement = null;
        java.sql.ResultSet rs = null;
        try {
            statement = getConnection().createStatement();
            rs = statement.executeQuery(query);
            return java.lang.Class.forName(rs.getMetaData().getColumnClassName(1));
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    @java.lang.Override
    public boolean isColumnNullable(java.lang.String tableName, java.lang.String columnName) throws java.sql.SQLException {
        java.lang.String query = java.lang.String.format("SELECT %s FROM %s WHERE 1=2", convertObjectName(columnName), convertObjectName(tableName));
        java.sql.Statement statement = null;
        java.sql.ResultSet rs = null;
        try {
            statement = getConnection().createStatement();
            rs = statement.executeQuery(query);
            return !(rs.getMetaData().isNullable(1) == java.sql.ResultSetMetaData.columnNoNulls);
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    @java.lang.Override
    public void setColumnNullable(java.lang.String tableName, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo columnInfo, boolean nullable) throws java.sql.SQLException {
        java.lang.String columnName = columnInfo.getName();
        if (isColumnNullable(tableName, columnName) != nullable) {
            java.lang.String query = dbmsHelper.getSetNullableStatement(tableName, columnInfo, nullable);
            executeQuery(query);
        } else {
            org.apache.ambari.server.orm.DBAccessorImpl.LOG.info("Column nullability property is not changed due to {} column from {} table is already in {} state, skipping", columnName, tableName, nullable ? "nullable" : "not nullable");
        }
    }

    @java.lang.Override
    public void setColumnNullable(java.lang.String tableName, java.lang.String columnName, boolean nullable) throws java.sql.SQLException {
        try {
            java.lang.Class columnClass = getColumnClass(tableName, columnName);
            setColumnNullable(tableName, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(columnName, columnClass), nullable);
        } catch (java.lang.ClassNotFoundException e) {
            org.apache.ambari.server.orm.DBAccessorImpl.LOG.error("Could not modify table=[], column={}, error={}", tableName, columnName, e.getMessage());
        }
    }

    @java.lang.Override
    public void changeColumnType(java.lang.String tableName, java.lang.String columnName, java.lang.Class fromType, java.lang.Class toType) throws java.sql.SQLException {
        java.lang.String tempColumnName = columnName + "_temp";
        switch (configuration.getDatabaseType()) {
            case ORACLE :
                if ((java.lang.String.class.equals(fromType) && toType.equals(java.lang.Character[].class)) || toType.equals(char[].class)) {
                    addColumn(tableName, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(tempColumnName, toType));
                    executeUpdate(java.lang.String.format("UPDATE %s SET %s = %s", convertObjectName(tableName), convertObjectName(tempColumnName), convertObjectName(columnName)));
                    dropColumn(tableName, columnName);
                    renameColumn(tableName, tempColumnName, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(columnName, toType));
                    return;
                }
                break;
        }
        alterColumn(tableName, new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(columnName, toType, null));
    }

    @java.lang.Override
    public org.apache.ambari.server.orm.DBAccessor.DBColumnInfo getColumnInfo(java.lang.String tableName, java.lang.String columnName) {
        try {
            java.lang.String sqlQuery = java.lang.String.format("SELECT %s FROM %s WHERE 1=2", columnName, convertObjectName(tableName));
            try (java.sql.Statement statement = getConnection().createStatement();java.sql.ResultSet rs = statement.executeQuery(sqlQuery)) {
                java.sql.ResultSetMetaData rsmd = rs.getMetaData();
                return new org.apache.ambari.server.orm.DBAccessor.DBColumnInfo(rsmd.getColumnName(1), org.apache.ambari.server.orm.DBAccessorImpl.fromSqlTypeToClass(rsmd.getColumnType(1)), rsmd.getColumnDisplaySize(1), null, rsmd.isNullable(1) == java.sql.ResultSetMetaData.columnNullable);
            }
        } catch (java.sql.SQLException e) {
            return null;
        }
    }

    @java.lang.Override
    public java.util.List<java.lang.String> getIndexesList(java.lang.String tableName, boolean unique) throws java.sql.SQLException {
        java.sql.ResultSet rs = getDatabaseMetaData().getIndexInfo(null, dbSchema, convertObjectName(tableName), unique, false);
        java.util.List<java.lang.String> indexList = new java.util.ArrayList<>();
        if (rs != null) {
            try {
                while (rs.next()) {
                    java.lang.String indexName = rs.getString(convertObjectName("index_name"));
                    if (indexName != null) {
                        indexList.add(indexName);
                    }
                } 
            } finally {
                rs.close();
            }
        }
        return indexList;
    }

    @java.lang.Override
    public java.lang.String getPrimaryKeyConstraintName(java.lang.String tableName) throws java.sql.SQLException {
        java.lang.String primaryKeyConstraintName = null;
        java.sql.Statement statement = null;
        java.sql.ResultSet resultSet = null;
        org.apache.ambari.server.configuration.Configuration.DatabaseType databaseType = configuration.getDatabaseType();
        switch (databaseType) {
            case ORACLE :
                {
                    java.lang.String lookupPrimaryKeyNameSql = java.lang.String.format("SELECT constraint_name FROM all_constraints WHERE UPPER(table_name) = UPPER('%s') AND constraint_type = 'P'", tableName);
                    try {
                        statement = getConnection().createStatement();
                        resultSet = statement.executeQuery(lookupPrimaryKeyNameSql);
                        if (resultSet.next()) {
                            primaryKeyConstraintName = resultSet.getString("constraint_name");
                        }
                    } finally {
                        org.springframework.jdbc.support.JdbcUtils.closeResultSet(resultSet);
                        org.springframework.jdbc.support.JdbcUtils.closeStatement(statement);
                    }
                    break;
                }
            case SQL_SERVER :
                {
                    java.lang.String lookupPrimaryKeyNameSql = java.lang.String.format("SELECT constraint_name FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE OBJECTPROPERTY(OBJECT_ID(constraint_name), 'IsPrimaryKey') = 1 AND table_name = '%s'", tableName);
                    try {
                        statement = getConnection().createStatement();
                        resultSet = statement.executeQuery(lookupPrimaryKeyNameSql);
                        if (resultSet.next()) {
                            primaryKeyConstraintName = resultSet.getString("constraint_name");
                        }
                    } finally {
                        org.springframework.jdbc.support.JdbcUtils.closeResultSet(resultSet);
                        org.springframework.jdbc.support.JdbcUtils.closeStatement(statement);
                    }
                    break;
                }
            case MYSQL :
            case POSTGRES :
                {
                    java.lang.String lookupPrimaryKeyNameSql = java.lang.String.format("SELECT constraint_name FROM information_schema.table_constraints AS tc WHERE tc.constraint_type = 'PRIMARY KEY' AND table_name = '%s'", tableName);
                    try {
                        statement = getConnection().createStatement();
                        resultSet = statement.executeQuery(lookupPrimaryKeyNameSql);
                        if (resultSet.next()) {
                            primaryKeyConstraintName = resultSet.getString("constraint_name");
                        }
                    } finally {
                        org.springframework.jdbc.support.JdbcUtils.closeResultSet(resultSet);
                        org.springframework.jdbc.support.JdbcUtils.closeStatement(statement);
                    }
                    break;
                }
            default :
                break;
        }
        return primaryKeyConstraintName;
    }

    @java.lang.Override
    public void dropPKConstraint(java.lang.String tableName, java.lang.String defaultConstraintName) throws java.sql.SQLException {
        org.apache.ambari.server.configuration.Configuration.DatabaseType databaseType = configuration.getDatabaseType();
        if (databaseType == org.apache.ambari.server.configuration.Configuration.DatabaseType.MYSQL) {
            java.lang.String mysqlDropQuery = java.lang.String.format("ALTER TABLE %s DROP PRIMARY KEY", tableName);
            executeQuery(mysqlDropQuery, true);
            return;
        }
        java.lang.String primaryKeyConstraintName = getPrimaryKeyConstraintName(tableName);
        if (null == primaryKeyConstraintName) {
            primaryKeyConstraintName = defaultConstraintName;
            org.apache.ambari.server.orm.DBAccessorImpl.LOG.warn("Unable to dynamically determine the PK constraint name for {}, defaulting to {}", tableName, defaultConstraintName);
        }
        if (null == primaryKeyConstraintName) {
            org.apache.ambari.server.orm.DBAccessorImpl.LOG.warn("Unable to determine the primary key constraint name for {}", tableName);
        } else {
            dropPKConstraint(tableName, primaryKeyConstraintName, true);
        }
    }

    @java.lang.Override
    public void addDefaultConstraint(java.lang.String tableName, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo column) throws java.sql.SQLException {
        java.lang.String defaultValue = escapeParameter(column.getDefaultValue());
        java.lang.StringBuilder builder = new java.lang.StringBuilder(java.lang.String.format("ALTER TABLE %s ", tableName));
        org.apache.ambari.server.configuration.Configuration.DatabaseType databaseType = configuration.getDatabaseType();
        switch (databaseType) {
            case DERBY :
            case MYSQL :
            case POSTGRES :
            case SQL_ANYWHERE :
                builder.append(java.lang.String.format("ALTER %s SET DEFAULT %s", column.getName(), defaultValue));
                break;
            case ORACLE :
                builder.append(java.lang.String.format("MODIFY %s DEFAULT %s", column.getName(), defaultValue));
                break;
            case SQL_SERVER :
                builder.append(java.lang.String.format("ALTER COLUMN %s SET DEFAULT %s", column.getName(), defaultValue));
                break;
            default :
                builder.append(java.lang.String.format("ALTER %s SET DEFAULT %s", column.getName(), defaultValue));
                break;
        }
        executeQuery(builder.toString());
    }

    private java.lang.String escapeParameter(java.lang.Object value) {
        return org.apache.ambari.server.orm.DBAccessorImpl.escapeParameter(value, databasePlatform);
    }

    public static java.lang.String escapeParameter(java.lang.Object value, org.eclipse.persistence.platform.database.DatabasePlatform databasePlatform) {
        if (value == null) {
            return null;
        }
        if (value instanceof java.lang.Enum<?>) {
            value = ((java.lang.Enum) (value)).name();
        }
        java.lang.String valueString = value.toString();
        if ((value instanceof java.lang.String) || (databasePlatform.convertToDatabaseType(value) instanceof java.lang.String)) {
            valueString = ("'" + valueString) + "'";
        }
        return valueString;
    }

    @java.lang.Override
    public void copyColumnToAnotherTable(java.lang.String sourceTableName, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo sourceColumn, java.lang.String sourceIDFieldName1, java.lang.String sourceIDFieldName2, java.lang.String sourceIDFieldName3, java.lang.String targetTableName, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo targetColumn, java.lang.String targetIDFieldName1, java.lang.String targetIDFieldName2, java.lang.String targetIDFieldName3, java.lang.String sourceConditionFieldName, java.lang.String condition, java.lang.Object initialValue) throws java.sql.SQLException {
        if (((((((tableHasColumn(sourceTableName, sourceIDFieldName1) && tableHasColumn(sourceTableName, sourceIDFieldName2)) && tableHasColumn(sourceTableName, sourceIDFieldName3)) && tableHasColumn(sourceTableName, sourceColumn.getName())) && tableHasColumn(sourceTableName, sourceConditionFieldName)) && tableHasColumn(targetTableName, targetIDFieldName1)) && tableHasColumn(targetTableName, targetIDFieldName2)) && tableHasColumn(targetTableName, targetIDFieldName3)) {
            final java.lang.String moveSQL = dbmsHelper.getCopyColumnToAnotherTableStatement(sourceTableName, sourceColumn.getName(), sourceIDFieldName1, sourceIDFieldName2, sourceIDFieldName3, targetTableName, targetColumn.getName(), targetIDFieldName1, targetIDFieldName2, targetIDFieldName3, sourceConditionFieldName, condition);
            final boolean isTargetColumnNullable = targetColumn.isNullable();
            targetColumn.setNullable(true);
            addColumn(targetTableName, targetColumn);
            executeUpdate(moveSQL, false);
            if (initialValue != null) {
                java.lang.String updateSQL = dbmsHelper.getColumnUpdateStatementWhereColumnIsNull(convertObjectName(targetTableName), convertObjectName(targetColumn.getName()), convertObjectName(targetColumn.getName()));
                executePreparedUpdate(updateSQL, initialValue);
            }
            if (!isTargetColumnNullable) {
                setColumnNullable(targetTableName, targetColumn.getName(), false);
            }
        }
    }

    @java.lang.Override
    public java.util.List<java.lang.Integer> getIntColumnValues(java.lang.String tableName, java.lang.String columnName, java.lang.String[] conditionColumnNames, java.lang.String[] values, boolean ignoreFailure) throws java.sql.SQLException {
        return executeQuery(tableName, new java.lang.String[]{ columnName }, conditionColumnNames, values, ignoreFailure, new org.apache.ambari.server.orm.DBAccessorImpl.ResultGetter<java.util.List<java.lang.Integer>>() {
            private java.util.List<java.lang.Integer> results = new java.util.ArrayList<>();

            @java.lang.Override
            public void collect(java.sql.ResultSet resultSet) throws java.sql.SQLException {
                results.add(resultSet.getInt(1));
            }

            @java.lang.Override
            public java.util.List<java.lang.Integer> getResult() {
                return results;
            }
        });
    }

    @java.lang.Override
    public java.util.Map<java.lang.Long, java.lang.String> getKeyToStringColumnMap(java.lang.String tableName, java.lang.String keyColumnName, java.lang.String valueColumnName, java.lang.String[] conditionColumnNames, java.lang.String[] values, boolean ignoreFailure) throws java.sql.SQLException {
        return executeQuery(tableName, new java.lang.String[]{ keyColumnName, valueColumnName }, conditionColumnNames, values, ignoreFailure, new org.apache.ambari.server.orm.DBAccessorImpl.ResultGetter<java.util.Map<java.lang.Long, java.lang.String>>() {
            java.util.Map<java.lang.Long, java.lang.String> map = new java.util.HashMap<>();

            @java.lang.Override
            public void collect(java.sql.ResultSet resultSet) throws java.sql.SQLException {
                map.put(resultSet.getLong(1), resultSet.getString(2));
            }

            @java.lang.Override
            public java.util.Map<java.lang.Long, java.lang.String> getResult() {
                return map;
            }
        });
    }

    protected <T> T executeQuery(java.lang.String tableName, java.lang.String[] requestedColumnNames, java.lang.String[] conditionColumnNames, java.lang.String[] conditionValues, boolean ignoreFailure, org.apache.ambari.server.orm.DBAccessorImpl.ResultGetter<T> resultGetter) throws java.sql.SQLException {
        java.lang.String query = buildQuery(tableName, requestedColumnNames, conditionColumnNames, conditionValues);
        java.sql.Statement statement = getConnection().createStatement();
        java.sql.ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery(query);
            if (resultSet != null) {
                while (resultSet.next()) {
                    resultGetter.collect(resultSet);
                } 
            }
        } catch (java.sql.SQLException e) {
            org.apache.ambari.server.orm.DBAccessorImpl.LOG.warn("Unable to execute query: " + query, e);
            if (!ignoreFailure) {
                throw e;
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
        }
        return resultGetter.getResult();
    }

    protected java.lang.String buildQuery(java.lang.String tableName, java.lang.String[] requestedColumnNames, java.lang.String[] conditionColumnNames, java.lang.String[] conditionValues) throws java.sql.SQLException {
        if (!tableExists(tableName)) {
            throw new java.lang.IllegalArgumentException(java.lang.String.format("%s table does not exist", tableName));
        }
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append("SELECT ");
        if ((requestedColumnNames == null) || (requestedColumnNames.length == 0)) {
            throw new java.lang.IllegalArgumentException("no columns for the select have been set");
        }
        for (java.lang.String name : requestedColumnNames) {
            if (!tableHasColumn(tableName, name)) {
                throw new java.lang.IllegalArgumentException(java.lang.String.format("%s table does not contain %s column", tableName, name));
            }
        }
        builder.append(requestedColumnNames[0]);
        for (int i = 1; i < requestedColumnNames.length; i++) {
            builder.append(", ").append(requestedColumnNames[i]);
        }
        builder.append(" FROM ").append(tableName);
        if ((conditionColumnNames != null) && (conditionColumnNames.length > 0)) {
            for (java.lang.String name : conditionColumnNames) {
                if (!tableHasColumn(tableName, name)) {
                    throw new java.lang.IllegalArgumentException(java.lang.String.format("%s table does not contain %s column", tableName, name));
                }
            }
            if (conditionColumnNames.length != conditionValues.length) {
                throw new java.lang.IllegalArgumentException("number of columns should be equal to number of values");
            }
            builder.append(" WHERE ").append(conditionColumnNames[0]).append("='").append(conditionValues[0]).append("'");
            for (int i = 1; i < conditionColumnNames.length; i++) {
                builder.append(" AND ").append(conditionColumnNames[i]).append("='").append(conditionValues[i]).append("'");
            }
        }
        return builder.toString();
    }

    @java.lang.Override
    public void moveColumnToAnotherTable(java.lang.String sourceTableName, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo sourceColumn, java.lang.String sourceIDFieldName, java.lang.String targetTableName, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo targetColumn, java.lang.String targetIDFieldName, java.lang.Object initialValue) throws java.sql.SQLException {
        if ((tableHasColumn(sourceTableName, sourceIDFieldName) && tableHasColumn(sourceTableName, sourceColumn.getName())) && tableHasColumn(targetTableName, targetIDFieldName)) {
            final java.lang.String moveSQL = dbmsHelper.getCopyColumnToAnotherTableStatement(sourceTableName, sourceColumn.getName(), sourceIDFieldName, targetTableName, targetColumn.getName(), targetIDFieldName);
            final boolean isTargetColumnNullable = targetColumn.isNullable();
            targetColumn.setNullable(true);
            addColumn(targetTableName, targetColumn);
            executeUpdate(moveSQL, false);
            if (initialValue != null) {
                java.lang.String updateSQL = dbmsHelper.getColumnUpdateStatementWhereColumnIsNull(convertObjectName(targetTableName), convertObjectName(targetColumn.getName()), convertObjectName(targetColumn.getName()));
                executePreparedUpdate(updateSQL, initialValue);
            }
            if (!isTargetColumnNullable) {
                setColumnNullable(targetTableName, targetColumn.getName(), false);
            }
            dropColumn(sourceTableName, sourceColumn.getName());
        }
    }

    @java.lang.Override
    public void clearTable(java.lang.String tableName) throws java.sql.SQLException {
        if (tableExists(tableName)) {
            java.lang.String sqlQuery = "DELETE FROM " + convertObjectName(tableName);
            executeQuery(sqlQuery);
        } else {
            org.apache.ambari.server.orm.DBAccessorImpl.LOG.warn("{} table doesn't exists, skipping", tableName);
        }
    }

    @java.lang.Override
    public void clearTableColumn(java.lang.String tableName, java.lang.String columnName, java.lang.Object value) throws java.sql.SQLException {
        if (tableExists(tableName)) {
            java.lang.String sqlQuery = java.lang.String.format("UPDATE %s SET %s = ?", convertObjectName(tableName), convertObjectName(columnName));
            executePreparedUpdate(sqlQuery, value);
        } else {
            org.apache.ambari.server.orm.DBAccessorImpl.LOG.warn("{} table doesn't exists, skipping", tableName);
        }
    }

    private interface ResultGetter<T> {
        void collect(java.sql.ResultSet resultSet) throws java.sql.SQLException;

        T getResult();
    }
}