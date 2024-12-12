package org.apache.ambari.server.orm.helpers.dbms;
import org.eclipse.persistence.platform.database.DatabasePlatform;
public class DerbyHelper extends org.apache.ambari.server.orm.helpers.dbms.GenericDbmsHelper {
    public DerbyHelper(org.eclipse.persistence.platform.database.DatabasePlatform databasePlatform) {
        super(databasePlatform);
    }

    @java.lang.Override
    public boolean supportsColumnTypeChange() {
        return false;
    }

    @java.lang.Override
    public java.lang.String getRenameColumnStatement(java.lang.String tableName, java.lang.String oldName, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo columnInfo) {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append("RENAME COLUMN ").append(tableName).append(".").append(oldName);
        builder.append(" TO ").append(columnInfo.getName());
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
        builder.append(" ALTER COLUMN ").append(columnInfo.getName());
        java.lang.String nullStatement = (nullable) ? " NULL" : " NOT NULL";
        builder.append(nullStatement);
        return builder;
    }

    @java.lang.Override
    public java.lang.String writeGetTableConstraints(java.lang.String databaseName, java.lang.String tableName) {
        java.lang.StringBuilder statement = new java.lang.StringBuilder().append("SELECT").append(" C.CONSTRAINTNAME AS CONSTRAINT_NAME,").append(" C.TYPE AS CONSTRAINT_TYPE").append(" FROM SYS.SYSCONSTRAINTS AS C, SYS.SYSTABLES AS T").append(" WHERE C.TABLEID = T.TABLEID AND T.TABLENAME = '").append(tableName).append("'");
        return statement.toString();
    }
}