package org.apache.ambari.server.orm.helpers.dbms;
import org.eclipse.persistence.platform.database.DatabasePlatform;
public class OracleHelper extends org.apache.ambari.server.orm.helpers.dbms.GenericDbmsHelper {
    public OracleHelper(org.eclipse.persistence.platform.database.DatabasePlatform databasePlatform) {
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
        builder.append(" MODIFY ").append(columnInfo.getName()).append(" ");
        writeColumnType(builder, columnInfo);
        return builder;
    }

    @java.lang.Override
    public java.lang.StringBuilder writeSetNullableString(java.lang.StringBuilder builder, java.lang.String tableName, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo columnInfo, boolean nullable) {
        builder.append(" MODIFY ").append(columnInfo.getName());
        java.lang.String nullStatement = (nullable) ? " NULL" : " NOT NULL";
        builder.append(nullStatement);
        return builder;
    }

    @java.lang.Override
    public java.lang.String writeGetTableConstraints(java.lang.String databaseName, java.lang.String tableName) {
        java.lang.StringBuilder statement = new java.lang.StringBuilder().append("SELECT CONSTRAINT_NAME as constraint_name, CONSTRAINT_TYPE as constraint_type ").append("FROM USER_CONSTRAINTS ").append("WHERE ").append("USER_CONSTRAINTS.TABLE_NAME='").append(tableName.toUpperCase()).append("'");
        return statement.toString();
    }

    @java.lang.Override
    public boolean isConstraintSupportedAfterNullability() {
        return false;
    }

    @java.lang.Override
    public java.lang.String getCopyColumnToAnotherTableStatement(java.lang.String sourceTable, java.lang.String sourceColumnName, java.lang.String sourceIDColumnName, java.lang.String targetTable, java.lang.String targetColumnName, java.lang.String targetIDColumnName) {
        return java.lang.String.format("UPDATE %1$s a SET (a.%3$s) = (SELECT b.%4$s FROM %2$s b WHERE b.%6$s = a.%5$s and ROWNUM < 2)", targetTable, sourceTable, targetColumnName, sourceColumnName, targetIDColumnName, sourceIDColumnName);
    }

    @java.lang.Override
    public java.lang.String getCopyColumnToAnotherTableStatement(java.lang.String sourceTable, java.lang.String sourceColumnName, java.lang.String sourceIDColumnName1, java.lang.String sourceIDColumnName2, java.lang.String sourceIDColumnName3, java.lang.String targetTable, java.lang.String targetColumnName, java.lang.String targetIDColumnName1, java.lang.String targetIDColumnName2, java.lang.String targetIDColumnName3, java.lang.String sourceConditionFieldName, java.lang.String condition) {
        return java.lang.String.format("UPDATE %1$s a SET (a.%3$s) = (SELECT b.%4$s FROM %2$s b WHERE b.%8$s = a.%5$s AND b.%9$s = a.%6$s AND b.%10$s = a.%7$s AND b.%11$s = '%12$s' AND ROWNUM < 2)", targetTable, sourceTable, targetColumnName, sourceColumnName, targetIDColumnName1, targetIDColumnName2, targetIDColumnName3, sourceIDColumnName1, sourceIDColumnName2, sourceIDColumnName3, sourceConditionFieldName, condition);
    }
}