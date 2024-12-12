package org.apache.ambari.server.orm.helpers.dbms;
import org.eclipse.persistence.exceptions.ValidationException;
import org.eclipse.persistence.platform.database.DatabasePlatform;
public class MySqlHelper extends org.apache.ambari.server.orm.helpers.dbms.GenericDbmsHelper {
    public MySqlHelper(org.eclipse.persistence.platform.database.DatabasePlatform databasePlatform) {
        super(databasePlatform);
    }

    @java.lang.Override
    public boolean supportsColumnTypeChange() {
        return true;
    }

    @java.lang.Override
    public java.lang.String quoteObjectName(java.lang.String name) {
        return ("`" + name) + "`";
    }

    @java.lang.Override
    public java.lang.StringBuilder writeColumnRenameString(java.lang.StringBuilder builder, java.lang.String oldName, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo newColumnInfo) {
        builder.append(" CHANGE ").append(oldName).append(" ").append(newColumnInfo.getName()).append(" ");
        writeColumnType(builder, newColumnInfo);
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
        builder.append(" MODIFY ").append(columnInfo.getName()).append(" ");
        writeColumnType(builder, columnInfo);
        java.lang.String nullStatement = (nullable) ? " NULL" : " NOT NULL";
        builder.append(nullStatement);
        return builder;
    }

    @java.lang.Override
    public java.lang.String writeGetTableConstraints(java.lang.String databaseName, java.lang.String tableName) {
        java.lang.StringBuilder statement = new java.lang.StringBuilder().append("SELECT ").append("constraints.CONSTRAINT_NAME as CONSTRAINT_NAME,").append("constraints.CONSTRAINT_TYPE as CONSTRAINT_TYPE ").append("FROM information_schema.TABLE_CONSTRAINTS as constraints ").append("WHERE ").append("constraints.TABLE_SCHEMA = \"").append(databaseName).append("\" ").append("AND constraints.TABLE_NAME = \"").append(tableName).append("\"");
        return statement.toString();
    }

    @java.lang.Override
    public java.io.Writer writeCreateTableStatement(java.io.Writer writer, java.lang.String tableName, java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> columns, java.util.List<java.lang.String> primaryKeyColumns) {
        java.io.Writer defaultWriter = super.writeCreateTableStatement(writer, tableName, columns, primaryKeyColumns);
        try {
            defaultWriter.write(" ENGINE=INNODB");
        } catch (java.io.IOException e) {
            throw org.eclipse.persistence.exceptions.ValidationException.fileError(e);
        }
        return defaultWriter;
    }

    @java.lang.Override
    public java.lang.String getCopyColumnToAnotherTableStatement(java.lang.String sourceTable, java.lang.String sourceColumnName, java.lang.String sourceIDColumnName, java.lang.String targetTable, java.lang.String targetColumnName, java.lang.String targetIDColumnName) {
        return java.lang.String.format("UPDATE %1$s AS a INNER JOIN %2$s AS b ON a.%5$s = b.%6$s SET a.%3$s = b.%4$s", targetTable, sourceTable, targetColumnName, sourceColumnName, targetIDColumnName, sourceIDColumnName);
    }

    @java.lang.Override
    public java.lang.String getCopyColumnToAnotherTableStatement(java.lang.String sourceTable, java.lang.String sourceColumnName, java.lang.String sourceIDColumnName1, java.lang.String sourceIDColumnName2, java.lang.String sourceIDColumnName3, java.lang.String targetTable, java.lang.String targetColumnName, java.lang.String targetIDColumnName1, java.lang.String targetIDColumnName2, java.lang.String targetIDColumnName3, java.lang.String sourceConditionFieldName, java.lang.String condition) {
        return java.lang.String.format("UPDATE %1$s AS a INNER JOIN %2$s AS b ON a.%5$s = b.%8$s AND a.%6$s = b.%9$s AND a.%7$s = b.%10$s AND b.%11$s = '%12$s' SET a.%3$s = b.%4$s", targetTable, sourceTable, targetColumnName, sourceColumnName, targetIDColumnName1, targetIDColumnName2, targetIDColumnName3, sourceIDColumnName1, sourceIDColumnName2, sourceIDColumnName3, sourceConditionFieldName, condition);
    }
}