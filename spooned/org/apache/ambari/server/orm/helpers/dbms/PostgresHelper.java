package org.apache.ambari.server.orm.helpers.dbms;
import org.eclipse.persistence.platform.database.DatabasePlatform;
public class PostgresHelper extends org.apache.ambari.server.orm.helpers.dbms.GenericDbmsHelper {
    public PostgresHelper(org.eclipse.persistence.platform.database.DatabasePlatform databasePlatform) {
        super(databasePlatform);
    }

    @java.lang.Override
    public boolean supportsColumnTypeChange() {
        return true;
    }

    @java.lang.Override
    public java.lang.StringBuilder writeColumnRenameString(java.lang.StringBuilder builder, java.lang.String oldName, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo newColumnInfo) {
        builder.append(" RENAME COLUMN ").append(oldName).append(" TO ").append(newColumnInfo.getName());
        return builder;
    }

    @java.lang.Override
    public java.lang.StringBuilder writeColumnModifyString(java.lang.StringBuilder builder, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo columnInfo) {
        builder.append(" ALTER COLUMN ").append(columnInfo.getName()).append(" TYPE ");
        writeColumnType(builder, columnInfo);
        return builder;
    }

    @java.lang.Override
    public java.lang.String getCopyColumnToAnotherTableStatement(java.lang.String sourceTable, java.lang.String sourceColumnName, java.lang.String sourceIDColumnName, java.lang.String targetTable, java.lang.String targetColumnName, java.lang.String targetIDColumnName) {
        return java.lang.String.format("UPDATE %1$s AS a SET %3$s = b.%4$s FROM %2$s AS b WHERE a.%5$s = b.%6$s", targetTable, sourceTable, targetColumnName, sourceColumnName, targetIDColumnName, sourceIDColumnName);
    }

    @java.lang.Override
    public java.lang.String getCopyColumnToAnotherTableStatement(java.lang.String sourceTable, java.lang.String sourceColumnName, java.lang.String sourceIDColumnName1, java.lang.String sourceIDColumnName2, java.lang.String sourceIDColumnName3, java.lang.String targetTable, java.lang.String targetColumnName, java.lang.String targetIDColumnName1, java.lang.String targetIDColumnName2, java.lang.String targetIDColumnName3, java.lang.String sourceConditionFieldName, java.lang.String condition) {
        return java.lang.String.format("UPDATE %1$s AS a SET %3$s = b.%4$s FROM %2$s AS b WHERE a.%5$s = b.%8$s AND a.%6$s = b.%9$s AND a.%7$s = b.%10$s AND b.%11$s = '%12$s'", targetTable, sourceTable, targetColumnName, sourceColumnName, targetIDColumnName1, targetIDColumnName2, targetIDColumnName3, sourceIDColumnName1, sourceIDColumnName2, sourceIDColumnName3, sourceConditionFieldName, condition);
    }

    @java.lang.Override
    public java.lang.StringBuilder writeSetNullableString(java.lang.StringBuilder builder, java.lang.String tableName, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo columnInfo, boolean nullable) {
        builder.append(" ALTER COLUMN ").append(columnInfo.getName());
        java.lang.String nullStatement = (nullable) ? " DROP NOT NULL" : " SET NOT NULL";
        builder.append(nullStatement);
        return builder;
    }

    @java.lang.Override
    public java.lang.String writeGetTableConstraints(java.lang.String databaseName, java.lang.String tableName) {
        java.lang.StringBuilder statement = new java.lang.StringBuilder().append("SELECT ").append("c.conname as CONSTRAINT_NAME,").append("c.contype as CONSTRAINT_TYPE ").append("FROM pg_catalog.pg_constraint as c ").append("JOIN pg_catalog.pg_namespace as namespace ").append("on namespace.oid = c.connamespace ").append("JOIN pg_catalog.pg_class as class ").append("on class.oid = c.conrelid ").append("where (namespace.nspname='").append(databaseName).append("' or namespace.nspname='public')").append("and class.relname='").append(tableName).append("'");
        return statement.toString();
    }

    @java.lang.Override
    public java.lang.StringBuilder writeDropPrimaryKeyStatement(java.lang.StringBuilder builder, java.lang.String constraintName, boolean cascade) {
        return builder.append("DROP CONSTRAINT ").append(constraintName).append(cascade ? " CASCADE" : "");
    }
}