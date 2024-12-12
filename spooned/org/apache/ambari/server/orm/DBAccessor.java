package org.apache.ambari.server.orm;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.eclipse.persistence.internal.databaseaccess.FieldTypeDefinition;
import org.eclipse.persistence.sessions.DatabaseSession;
public interface DBAccessor {
    java.sql.Connection getConnection();

    java.sql.Connection getNewConnection();

    java.lang.String quoteObjectName(java.lang.String name);

    void createTable(java.lang.String tableName, java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> columnInfo, java.lang.String... primaryKeyColumns) throws java.sql.SQLException;

    void createIndex(java.lang.String indexName, java.lang.String tableName, java.lang.String... columnNames) throws java.sql.SQLException;

    void createIndex(java.lang.String indexName, java.lang.String tableName, boolean isUnique, java.lang.String... columnNames) throws java.sql.SQLException;

    void addFKConstraint(java.lang.String tableName, java.lang.String constraintName, java.lang.String keyColumn, java.lang.String referenceTableName, java.lang.String referenceColumn, boolean ignoreFailure) throws java.sql.SQLException;

    void addFKConstraint(java.lang.String tableName, java.lang.String constraintName, java.lang.String keyColumn, java.lang.String referenceTableName, java.lang.String referenceColumn, boolean shouldCascadeOnDelete, boolean ignoreFailure) throws java.sql.SQLException;

    void addFKConstraint(java.lang.String tableName, java.lang.String constraintName, java.lang.String[] keyColumns, java.lang.String referenceTableName, java.lang.String[] referenceColumns, boolean shouldCascadeOnDelete, boolean ignoreFailure) throws java.sql.SQLException;

    void addFKConstraint(java.lang.String tableName, java.lang.String constraintName, java.lang.String[] keyColumns, java.lang.String referenceTableName, java.lang.String[] referenceColumns, boolean ignoreFailure) throws java.sql.SQLException;

    void addColumn(java.lang.String tableName, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo columnInfo) throws java.sql.SQLException;

    void addUniqueConstraint(java.lang.String tableName, java.lang.String constraintName, java.lang.String... columnNames) throws java.sql.SQLException;

    void updateUniqueConstraint(java.lang.String tableName, java.lang.String constraintName, java.lang.String... columnNames) throws java.sql.SQLException;

    void addPKConstraint(java.lang.String tableName, java.lang.String constraintName, boolean ignoreErrors, java.lang.String... columnName) throws java.sql.SQLException;

    void addPKConstraint(java.lang.String tableName, java.lang.String constraintName, java.lang.String... columnName) throws java.sql.SQLException;

    void renameColumn(java.lang.String tableName, java.lang.String oldColumnName, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo columnInfo) throws java.sql.SQLException;

    void alterColumn(java.lang.String tableName, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo columnInfo) throws java.sql.SQLException;

    boolean insertRow(java.lang.String tableName, java.lang.String[] columnNames, java.lang.String[] values, boolean ignoreFailure) throws java.sql.SQLException;

    boolean insertRowIfMissing(java.lang.String tableName, java.lang.String[] columnNames, java.lang.String[] values, boolean ignoreFailure) throws java.sql.SQLException;

    int updateTable(java.lang.String tableName, java.lang.String columnName, java.lang.Object value, java.lang.String whereClause) throws java.sql.SQLException;

    void updateTable(java.lang.String tableName, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo columnNameSrc, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo columnNameTgt) throws java.sql.SQLException;

    void executeScript(java.lang.String filePath) throws java.sql.SQLException, java.io.IOException;

    int executeUpdate(java.lang.String query) throws java.sql.SQLException;

    int executeUpdate(java.lang.String query, boolean ignoreErrors) throws java.sql.SQLException;

    void executeQuery(java.lang.String query, java.lang.String tableName, java.lang.String hasColumnName) throws java.sql.SQLException;

    void executeQuery(java.lang.String query) throws java.sql.SQLException;

    void executeQuery(java.lang.String query, boolean ignoreFailure) throws java.sql.SQLException;

    void executePreparedQuery(java.lang.String query, java.lang.Object... arguments) throws java.sql.SQLException;

    void executePreparedQuery(java.lang.String query, boolean ignoreFailure, java.lang.Object... arguments) throws java.sql.SQLException;

    void executePreparedUpdate(java.lang.String query, java.lang.Object... arguments) throws java.sql.SQLException;

    void executePreparedUpdate(java.lang.String query, boolean ignoreFailure, java.lang.Object... arguments) throws java.sql.SQLException;

    java.util.List<java.lang.Integer> getIntColumnValues(java.lang.String tableName, java.lang.String columnName, java.lang.String[] conditionColumnNames, java.lang.String[] conditionValues, boolean ignoreFailure) throws java.sql.SQLException;

    java.util.Map<java.lang.Long, java.lang.String> getKeyToStringColumnMap(java.lang.String tableName, java.lang.String keyColumnName, java.lang.String valueColumnName, java.lang.String[] conditionColumnNames, java.lang.String[] conditionValues, boolean ignoreFailure) throws java.sql.SQLException;

    void dropTable(java.lang.String tableName) throws java.sql.SQLException;

    void truncateTable(java.lang.String tableName) throws java.sql.SQLException;

    void dropColumn(java.lang.String tableName, java.lang.String columnName) throws java.sql.SQLException;

    void dropSequence(java.lang.String sequenceName) throws java.sql.SQLException;

    void dropFKConstraint(java.lang.String tableName, java.lang.String constraintName) throws java.sql.SQLException;

    void dropPKConstraint(java.lang.String tableName, java.lang.String constraintName, boolean ignoreFailure, boolean cascade) throws java.sql.SQLException;

    void dropPKConstraint(java.lang.String tableName, java.lang.String constraintName, boolean cascade) throws java.sql.SQLException;

    void dropPKConstraint(java.lang.String tableName, java.lang.String constraintName, java.lang.String columnName, boolean cascade) throws java.sql.SQLException;

    void dropFKConstraint(java.lang.String tableName, java.lang.String constraintName, boolean ignoreFailure) throws java.sql.SQLException;

    void dropUniqueConstraint(java.lang.String tableName, java.lang.String constraintName, boolean ignoreFailure) throws java.sql.SQLException;

    void dropUniqueConstraint(java.lang.String tableName, java.lang.String constraintName) throws java.sql.SQLException;

    boolean tableExists(java.lang.String tableName) throws java.sql.SQLException;

    boolean tableHasData(java.lang.String tableName) throws java.sql.SQLException;

    boolean tableHasColumn(java.lang.String tableName, java.lang.String columnName) throws java.sql.SQLException;

    boolean tableHasColumn(java.lang.String tableName, java.lang.String... columnName) throws java.sql.SQLException;

    boolean tableHasForeignKey(java.lang.String tableName, java.lang.String fkName) throws java.sql.SQLException;

    boolean tableHasForeignKey(java.lang.String tableName, java.lang.String refTableName, java.lang.String columnName, java.lang.String refColumnName) throws java.sql.SQLException;

    boolean tableHasForeignKey(java.lang.String tableName, java.lang.String referenceTableName, java.lang.String[] keyColumns, java.lang.String[] referenceColumns) throws java.sql.SQLException;

    org.eclipse.persistence.sessions.DatabaseSession getNewDatabaseSession();

    boolean tableHasPrimaryKey(java.lang.String tableName, java.lang.String columnName) throws java.sql.SQLException;

    java.util.List<java.lang.String> getIndexesList(java.lang.String tableName, boolean unique) throws java.sql.SQLException;

    boolean tableHasIndex(java.lang.String tableName, boolean unique, java.lang.String indexName) throws java.sql.SQLException;

    int getColumnType(java.lang.String tableName, java.lang.String columnName) throws java.sql.SQLException;

    java.lang.Class getColumnClass(java.lang.String tableName, java.lang.String columnName) throws java.sql.SQLException, java.lang.ClassNotFoundException;

    boolean isColumnNullable(java.lang.String tableName, java.lang.String columnName) throws java.sql.SQLException;

    void setColumnNullable(java.lang.String tableName, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo columnInfo, boolean nullable) throws java.sql.SQLException;

    void setColumnNullable(java.lang.String tableName, java.lang.String columnName, boolean nullable) throws java.sql.SQLException;

    void changeColumnType(java.lang.String tableName, java.lang.String columnName, java.lang.Class fromType, java.lang.Class toType) throws java.sql.SQLException;

    org.apache.ambari.server.orm.DBAccessor.DBColumnInfo getColumnInfo(java.lang.String tableName, java.lang.String columnName) throws java.sql.SQLException;

    java.lang.String getPrimaryKeyConstraintName(java.lang.String tableName) throws java.sql.SQLException;

    void dropPKConstraint(java.lang.String tableName, java.lang.String defaultConstraintName) throws java.sql.SQLException;

    void addDefaultConstraint(java.lang.String tableName, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo column) throws java.sql.SQLException;

    void moveColumnToAnotherTable(java.lang.String sourceTableName, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo sourceColumn, java.lang.String sourceIDFieldName, java.lang.String targetTableName, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo targetColumn, java.lang.String targetIDFieldName, java.lang.Object initialValue) throws java.sql.SQLException;

    void copyColumnToAnotherTable(java.lang.String sourceTableName, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo sourceColumn, java.lang.String sourceIDFieldName1, java.lang.String sourceIDFieldName2, java.lang.String sourceIDFieldName3, java.lang.String targetTableName, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo targetColumn, java.lang.String targetIDFieldName1, java.lang.String targetIDFieldName2, java.lang.String targetIDFieldName3, java.lang.String sourceConditionFieldName, java.lang.String condition, java.lang.Object initialValue) throws java.sql.SQLException;

    void clearTable(java.lang.String tableName) throws java.sql.SQLException;

    void clearTableColumn(java.lang.String tableName, java.lang.String columnName, java.lang.Object value) throws java.sql.SQLException;

    enum DbType {

        ORACLE,
        MYSQL,
        POSTGRES,
        DERBY,
        H2,
        UNKNOWN;}

    org.apache.ambari.server.orm.DBAccessor.DbType getDbType();

    java.lang.String getDbSchema();

    class DBColumnInfo {
        private java.lang.String name;

        private java.lang.Class type;

        private java.lang.Integer length;

        private java.lang.Object defaultValue;

        private boolean isNullable;

        private org.eclipse.persistence.internal.databaseaccess.FieldTypeDefinition dbType = null;

        public DBColumnInfo(java.lang.String name, java.lang.Class type) {
            this(name, type, null, null, true);
        }

        public DBColumnInfo(java.lang.String name, java.lang.Class type, java.lang.Integer length) {
            this(name, type, length, null, true);
        }

        public DBColumnInfo(java.lang.String name, java.lang.Class type, java.lang.Integer length, java.lang.Object defaultValue, boolean nullable) {
            this.name = name;
            this.type = type;
            this.length = length;
            this.defaultValue = defaultValue;
            isNullable = nullable;
        }

        public DBColumnInfo(java.lang.String name, org.eclipse.persistence.internal.databaseaccess.FieldTypeDefinition dbType, java.lang.Integer length, java.lang.Object defaultValue, boolean isNullable) {
            this.name = name;
            this.length = length;
            this.isNullable = isNullable;
            this.defaultValue = defaultValue;
            this.dbType = dbType;
        }

        public DBColumnInfo(java.lang.String name, org.eclipse.persistence.internal.databaseaccess.FieldTypeDefinition dbType, java.lang.Integer length) {
            this(name, dbType, length, null, true);
        }

        public java.lang.String getName() {
            return name;
        }

        public void setName(java.lang.String name) {
            this.name = name;
        }

        public java.lang.Class getType() {
            return type;
        }

        public void setType(java.lang.Class type) {
            this.type = type;
        }

        public java.lang.Integer getLength() {
            return length;
        }

        public void setLength(java.lang.Integer length) {
            this.length = length;
        }

        public java.lang.Object getDefaultValue() {
            return defaultValue;
        }

        public void setDefaultValue(java.lang.Object defaultValue) {
            this.defaultValue = defaultValue;
        }

        public boolean isNullable() {
            return isNullable;
        }

        public void setNullable(boolean nullable) {
            isNullable = nullable;
        }

        public org.eclipse.persistence.internal.databaseaccess.FieldTypeDefinition getDbType() {
            return dbType;
        }

        public void setDbType(org.eclipse.persistence.internal.databaseaccess.FieldTypeDefinition dbType) {
            this.dbType = dbType;
        }

        @java.lang.Override
        public int hashCode() {
            return new org.apache.commons.lang.builder.HashCodeBuilder(17, 37).append(name).append(type).append(length).append(isNullable).append(defaultValue).append(dbType).toHashCode();
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o)
                return true;

            if ((o == null) || (getClass() != o.getClass()))
                return false;

            org.apache.ambari.server.orm.DBAccessor.DBColumnInfo that = ((org.apache.ambari.server.orm.DBAccessor.DBColumnInfo) (o));
            return new org.apache.commons.lang.builder.EqualsBuilder().append(name, that.name).append(type, that.type).append(length, that.length).append(isNullable, that.isNullable).append(defaultValue, that.defaultValue).append(dbType, that.dbType).isEquals();
        }
    }
}