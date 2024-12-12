package org.apache.ambari.server.orm.helpers.dbms;
import org.eclipse.persistence.platform.database.DatabasePlatform;
public class H2Helper extends org.apache.ambari.server.orm.helpers.dbms.GenericDbmsHelper {
    public H2Helper(org.eclipse.persistence.platform.database.DatabasePlatform databasePlatform) {
        super(databasePlatform);
    }

    @java.lang.Override
    public boolean supportsColumnTypeChange() {
        return false;
    }

    @java.lang.Override
    public java.lang.String getRenameColumnStatement(java.lang.String tableName, java.lang.String oldName, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo columnInfo) {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append("ALTER TABLE ").append(tableName).append(" ALTER COLUMN ").append(oldName);
        builder.append(" RENAME TO ").append(columnInfo.getName());
        return builder.toString();
    }

    @java.lang.Override
    public java.lang.StringBuilder writeColumnModifyString(java.lang.StringBuilder builder, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo columnInfo) {
        builder.append(" ALTER COLUMN ").append(columnInfo.getName()).append(" SET DATA TYPE ");
        writeColumnType(builder, columnInfo);
        return builder;
    }

    @java.lang.Override
    public java.lang.StringBuilder writeSetNullableString(java.lang.StringBuilder builder, java.lang.String tableName, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo columnInfo, boolean nullable) {
        builder.append(" ALTER COLUMN ").append(columnInfo.getName()).append(" SET");
        java.lang.String nullStatement = (nullable) ? " NULL" : " NOT NULL";
        builder.append(nullStatement);
        return builder;
    }

    @java.lang.Override
    public java.lang.String writeGetTableConstraints(java.lang.String databaseName, java.lang.String tableName) {
        java.lang.StringBuilder statement = new java.lang.StringBuilder().append("SELECT").append(" C.CONSTRAINTNAME AS CONSTRAINT_NAME,").append(" C.TYPE AS CONSTRAINT_TYPE").append(" FROM SYS.SYSCONSTRAINTS AS C, SYS.SYSTABLES AS T").append(" WHERE C.TABLEID = T.TABLEID AND T.TABLENAME = '").append(tableName).append("'");
        return statement.toString();
    }

    @java.lang.Override
    public java.lang.String getCopyColumnToAnotherTableStatement(java.lang.String sourceTable, java.lang.String sourceColumnName, java.lang.String sourceIDColumnName, java.lang.String targetTable, java.lang.String targetColumnName, java.lang.String targetIDColumnName) {
        return java.lang.String.format("UPDATE %1$s a SET %3$s = (SELECT b.%4$s FROM %2$s b WHERE b.%6$s = a.%5$s LIMIT 1)", targetTable, sourceTable, targetColumnName, sourceColumnName, targetIDColumnName, sourceIDColumnName);
    }

    @java.lang.Override
    public java.lang.String getCopyColumnToAnotherTableStatement(java.lang.String sourceTable, java.lang.String sourceColumnName, java.lang.String sourceIDColumnName1, java.lang.String sourceIDColumnName2, java.lang.String sourceIDColumnName3, java.lang.String targetTable, java.lang.String targetColumnName, java.lang.String targetIDColumnName1, java.lang.String targetIDColumnName2, java.lang.String targetIDColumnName3, java.lang.String sourceConditionFieldName, java.lang.String condition) {
        return java.lang.String.format("UPDATE %1$s a SET %3$s = (SELECT b.%4$s FROM %2$s b WHERE b.%8$s = a.%5$s AND b.%9$s = a.%6$s AND b.%10$s = a.%7$s AND b.%11$s = '%12$s'  LIMIT 1)", targetTable, sourceTable, targetColumnName, sourceColumnName, targetIDColumnName1, targetIDColumnName2, targetIDColumnName3, sourceIDColumnName1, sourceIDColumnName2, sourceIDColumnName3, sourceConditionFieldName, condition);
    }
}