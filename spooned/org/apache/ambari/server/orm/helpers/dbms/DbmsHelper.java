package org.apache.ambari.server.orm.helpers.dbms;
public interface DbmsHelper {
    boolean supportsColumnTypeChange();

    java.lang.String quoteObjectName(java.lang.String name);

    java.lang.String getRenameColumnStatement(java.lang.String tableName, java.lang.String oldName, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo columnInfo);

    java.lang.String getAlterColumnStatement(java.lang.String tableName, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo columnInfo);

    java.lang.String getCreateTableStatement(java.lang.String tableName, java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> columns, java.util.List<java.lang.String> primaryKeyColumns);

    java.lang.String getTableConstraintsStatement(java.lang.String databaseName, java.lang.String tablename);

    java.lang.String getCreateIndexStatement(java.lang.String indexName, java.lang.String tableName, java.lang.String... columnNames);

    java.lang.String getCreateIndexStatement(java.lang.String indexName, java.lang.String tableName, boolean isUnique, java.lang.String... columnNames);

    java.lang.String getColumnUpdateStatementWhereColumnIsNull(java.lang.String tableName, java.lang.String setColumnName, java.lang.String conditionColumnName);

    java.lang.String getDropIndexStatement(java.lang.String indexName, java.lang.String tableName);

    java.lang.String getAddUniqueConstraintStatement(java.lang.String tableName, java.lang.String constraintName, java.lang.String... columnNames);

    java.lang.String getAddPrimaryKeyConstraintStatement(java.lang.String tableName, java.lang.String constraintName, java.lang.String... columnName);

    java.lang.String getAddForeignKeyStatement(java.lang.String tableName, java.lang.String constraintName, java.util.List<java.lang.String> keyColumns, java.lang.String referenceTableName, java.util.List<java.lang.String> referenceColumns, boolean shouldCascadeOnDelete);

    java.lang.String getAddColumnStatement(java.lang.String tableName, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo columnInfo);

    java.lang.String getDropTableColumnStatement(java.lang.String tableName, java.lang.String columnName);

    java.lang.String getRenameColumnStatement(java.lang.String tableName, java.lang.String oldColumnName, java.lang.String newColumnName);

    java.lang.String getDropTableStatement(java.lang.String tableName);

    java.lang.String getDropFKConstraintStatement(java.lang.String tableName, java.lang.String constraintName);

    java.lang.String getDropUniqueConstraintStatement(java.lang.String tableName, java.lang.String constraintName);

    java.lang.String getDropSequenceStatement(java.lang.String sequenceName);

    java.lang.String getDropPrimaryKeyStatement(java.lang.String tableName, java.lang.String constraintName, boolean cascade);

    java.lang.String getSetNullableStatement(java.lang.String tableName, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo columnInfo, boolean nullable);

    java.lang.String getCopyColumnToAnotherTableStatement(java.lang.String sourceTable, java.lang.String sourceColumnName, java.lang.String sourceIDColumnName, java.lang.String targetTable, java.lang.String targetColumnName, java.lang.String targetIDColumnName);

    java.lang.String getCopyColumnToAnotherTableStatement(java.lang.String sourceTable, java.lang.String sourceColumnName, java.lang.String sourceIDColumnName1, java.lang.String sourceIDColumnName2, java.lang.String sourceIDColumnName3, java.lang.String targetTable, java.lang.String targetColumnName, java.lang.String targetIDColumnName1, java.lang.String targetIDColumnName2, java.lang.String targetIDColumnName3, java.lang.String sourceConditionFieldName, java.lang.String condition);

    boolean isConstraintSupportedAfterNullability();
}