package org.apache.ambari.server.orm.helpers.dbms;
import org.apache.commons.lang.StringUtils;
import org.eclipse.persistence.internal.databaseaccess.FieldTypeDefinition;
import org.eclipse.persistence.internal.databaseaccess.Platform;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.platform.database.DatabasePlatform;
import org.eclipse.persistence.tools.schemaframework.FieldDefinition;
import org.eclipse.persistence.tools.schemaframework.ForeignKeyConstraint;
import org.eclipse.persistence.tools.schemaframework.TableDefinition;
import org.eclipse.persistence.tools.schemaframework.UniqueKeyConstraint;
public class GenericDbmsHelper implements org.apache.ambari.server.orm.helpers.dbms.DbmsHelper {
    protected final org.eclipse.persistence.platform.database.DatabasePlatform databasePlatform;

    public GenericDbmsHelper(org.eclipse.persistence.platform.database.DatabasePlatform databasePlatform) {
        this.databasePlatform = databasePlatform;
    }

    @java.lang.Override
    public boolean supportsColumnTypeChange() {
        return false;
    }

    @java.lang.Override
    public java.lang.String quoteObjectName(java.lang.String name) {
        return ("\"" + name) + "\"";
    }

    @java.lang.Override
    public java.lang.String getRenameColumnStatement(java.lang.String tableName, java.lang.String oldName, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo columnInfo) {
        java.lang.StringBuilder stringBuilder = new java.lang.StringBuilder();
        writeAlterTableClause(stringBuilder, tableName);
        writeColumnRenameString(stringBuilder, oldName, columnInfo);
        return stringBuilder.toString();
    }

    @java.lang.Override
    public java.lang.String getAlterColumnStatement(java.lang.String tableName, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo columnInfo) {
        java.lang.StringBuilder stringBuilder = new java.lang.StringBuilder();
        writeAlterTableClause(stringBuilder, tableName);
        writeColumnModifyString(stringBuilder, columnInfo);
        return stringBuilder.toString();
    }

    @java.lang.Override
    public java.lang.String getSetNullableStatement(java.lang.String tableName, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo columnInfo, boolean nullable) {
        java.lang.StringBuilder stringBuilder = new java.lang.StringBuilder();
        writeAlterTableClause(stringBuilder, tableName);
        writeSetNullableString(stringBuilder, tableName, columnInfo, nullable);
        return stringBuilder.toString();
    }

    @java.lang.Override
    public java.lang.String getCopyColumnToAnotherTableStatement(java.lang.String sourceTable, java.lang.String sourceColumnName, java.lang.String sourceIDColumnName, java.lang.String targetTable, java.lang.String targetColumnName, java.lang.String targetIDColumnName) {
        throw new java.lang.UnsupportedOperationException("Column copy is not supported for generic DB");
    }

    @java.lang.Override
    public java.lang.String getCopyColumnToAnotherTableStatement(java.lang.String sourceTable, java.lang.String sourceColumnName, java.lang.String sourceIDColumnName1, java.lang.String sourceIDColumnName2, java.lang.String sourceIDColumnName3, java.lang.String targetTable, java.lang.String targetColumnName, java.lang.String targetIDColumnName1, java.lang.String targetIDColumnName2, java.lang.String targetIDColumnName3, java.lang.String sourceConditionFieldName, java.lang.String condition) {
        throw new java.lang.UnsupportedOperationException("Column copy is not supported for generic DB");
    }

    public java.lang.StringBuilder writeAlterTableClause(java.lang.StringBuilder builder, java.lang.String tableName) {
        builder.append("ALTER TABLE ").append(tableName).append(" ");
        return builder;
    }

    public java.lang.StringBuilder writeColumnModifyString(java.lang.StringBuilder builder, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo columnInfo) {
        throw new java.lang.UnsupportedOperationException("Column type modification not supported for generic DB");
    }

    public java.lang.StringBuilder writeColumnRenameString(java.lang.StringBuilder builder, java.lang.String oldName, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo newColumnInfo) {
        throw new java.lang.UnsupportedOperationException("Column rename not supported for generic DB");
    }

    public java.lang.StringBuilder writeColumnType(java.lang.StringBuilder builder, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo columnInfo) {
        org.eclipse.persistence.internal.databaseaccess.FieldTypeDefinition fieldType = columnInfo.getDbType();
        if (fieldType == null) {
            fieldType = databasePlatform.getFieldTypeDefinition(columnInfo.getType());
        }
        if (fieldType == null) {
            throw new java.lang.IllegalArgumentException("Unable to convert data type");
        }
        org.eclipse.persistence.tools.schemaframework.FieldDefinition definition = convertToFieldDefinition(columnInfo);
        java.io.StringWriter writer = new java.io.StringWriter();
        try {
            databasePlatform.printFieldTypeSize(writer, definition, fieldType, false);
        } catch (java.io.IOException ignored) {
        }
        builder.append(writer);
        return builder;
    }

    public java.lang.String writeGetTableConstraints(java.lang.String databaseName, java.lang.String tableName) {
        throw new java.lang.UnsupportedOperationException("List of table constraints is not supported for generic DB");
    }

    public java.lang.StringBuilder writeAddPrimaryKeyString(java.lang.StringBuilder builder, java.lang.String constraintName, java.lang.String... columnName) {
        builder.append("ADD CONSTRAINT ").append(constraintName).append(" PRIMARY KEY (").append(org.apache.commons.lang.StringUtils.join(columnName, ",")).append(")");
        return builder;
    }

    public java.lang.StringBuilder writeSetNullableString(java.lang.StringBuilder builder, java.lang.String tableName, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo columnInfo, boolean nullable) {
        throw new java.lang.UnsupportedOperationException("Column nullable modification not supported for generic DB");
    }

    public java.lang.StringBuilder writeDropTableColumnStatement(java.lang.StringBuilder builder, java.lang.String columnName) {
        builder.append("DROP COLUMN ").append(columnName);
        return builder;
    }

    public java.lang.StringBuilder writeDropPrimaryKeyStatement(java.lang.StringBuilder builder, java.lang.String constraintName, boolean cascade) {
        return builder.append("DROP PRIMARY KEY");
    }

    @java.lang.Override
    public java.lang.String getDropPrimaryKeyStatement(java.lang.String tableName, java.lang.String constraintName, boolean cascade) {
        java.lang.StringBuilder builder = writeAlterTableClause(new java.lang.StringBuilder(), tableName);
        return writeDropPrimaryKeyStatement(builder, constraintName, cascade).toString();
    }

    @java.lang.Override
    public java.lang.String getCreateTableStatement(java.lang.String tableName, java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> columns, java.util.List<java.lang.String> primaryKeyColumns) {
        java.io.Writer stringWriter = new java.io.StringWriter();
        writeCreateTableStatement(stringWriter, tableName, columns, primaryKeyColumns);
        return stringWriter.toString();
    }

    public java.io.Writer writeCreateTableStatement(java.io.Writer writer, java.lang.String tableName, java.util.List<org.apache.ambari.server.orm.DBAccessor.DBColumnInfo> columns, java.util.List<java.lang.String> primaryKeyColumns) {
        org.eclipse.persistence.tools.schemaframework.TableDefinition tableDefinition = new org.eclipse.persistence.tools.schemaframework.TableDefinition();
        tableDefinition.setName(tableName);
        for (org.apache.ambari.server.orm.DBAccessor.DBColumnInfo columnInfo : columns) {
            int length = (columnInfo.getLength() != null) ? columnInfo.getLength() : 0;
            if (primaryKeyColumns.contains(columnInfo.getName())) {
                tableDefinition.addIdentityField(columnInfo.getName(), columnInfo.getType(), length);
            } else {
                org.eclipse.persistence.tools.schemaframework.FieldDefinition fieldDefinition = convertToFieldDefinition(columnInfo);
                tableDefinition.addField(fieldDefinition);
            }
        }
        tableDefinition.buildCreationWriter(createStubAbstractSessionFromPlatform(databasePlatform), writer);
        return writer;
    }

    public org.eclipse.persistence.tools.schemaframework.FieldDefinition convertToFieldDefinition(org.apache.ambari.server.orm.DBAccessor.DBColumnInfo columnInfo) {
        int length = (columnInfo.getLength() != null) ? columnInfo.getLength() : 0;
        org.eclipse.persistence.tools.schemaframework.FieldDefinition fieldDefinition = new org.eclipse.persistence.tools.schemaframework.FieldDefinition(columnInfo.getName(), columnInfo.getType(), length);
        fieldDefinition.setShouldAllowNull(columnInfo.isNullable());
        if ((null != columnInfo.getDefaultValue()) && isConstraintSupportedAfterNullability()) {
            fieldDefinition.setConstraint("DEFAULT " + escapeParameter(columnInfo.getDefaultValue()));
        }
        return fieldDefinition;
    }

    @java.lang.Override
    public java.lang.String getDropUniqueConstraintStatement(java.lang.String tableName, java.lang.String constraintName) {
        org.eclipse.persistence.tools.schemaframework.UniqueKeyConstraint uniqueKeyConstraint = new org.eclipse.persistence.tools.schemaframework.UniqueKeyConstraint();
        uniqueKeyConstraint.setName(constraintName);
        java.io.Writer writer = new java.io.StringWriter();
        org.eclipse.persistence.tools.schemaframework.TableDefinition tableDefinition = new org.eclipse.persistence.tools.schemaframework.TableDefinition();
        tableDefinition.setName(tableName);
        tableDefinition.buildUniqueConstraintDeletionWriter(createStubAbstractSessionFromPlatform(databasePlatform), uniqueKeyConstraint, writer);
        return writer.toString();
    }

    @java.lang.Override
    public java.lang.String getTableConstraintsStatement(java.lang.String databaseName, java.lang.String tablename) {
        return writeGetTableConstraints(databaseName, tablename);
    }

    @java.lang.Override
    public java.lang.String getCreateIndexStatement(java.lang.String indexName, java.lang.String tableName, java.lang.String... columnNames) {
        return getCreateIndexStatement(indexName, tableName, false, columnNames);
    }

    @java.lang.Override
    public java.lang.String getCreateIndexStatement(java.lang.String indexName, java.lang.String tableName, boolean isUnique, java.lang.String... columnNames) {
        java.lang.String createIndex = databasePlatform.buildCreateIndex(tableName, indexName, "", isUnique, columnNames);
        return createIndex;
    }

    @java.lang.Override
    public java.lang.String getColumnUpdateStatementWhereColumnIsNull(java.lang.String tableName, java.lang.String setColumnName, java.lang.String conditionColumnName) {
        return ((((("UPDATE " + tableName) + " SET ") + setColumnName) + "=? WHERE ") + conditionColumnName) + " IS NULL";
    }

    @java.lang.Override
    public java.lang.String getDropIndexStatement(java.lang.String indexName, java.lang.String tableName) {
        java.lang.String dropIndex = databasePlatform.buildDropIndex(tableName, indexName);
        return dropIndex;
    }

    @java.lang.Override
    public java.lang.String getAddUniqueConstraintStatement(java.lang.String tableName, java.lang.String constraintName, java.lang.String... columnNames) {
        org.eclipse.persistence.tools.schemaframework.UniqueKeyConstraint uniqueKeyConstraint = new org.eclipse.persistence.tools.schemaframework.UniqueKeyConstraint();
        uniqueKeyConstraint.setName(constraintName);
        for (java.lang.String columnName : columnNames) {
            uniqueKeyConstraint.addSourceField(columnName);
        }
        org.eclipse.persistence.tools.schemaframework.TableDefinition tableDefinition = new org.eclipse.persistence.tools.schemaframework.TableDefinition();
        tableDefinition.setName(tableName);
        java.io.Writer writer = new java.io.StringWriter();
        tableDefinition.buildUniqueConstraintCreationWriter(createStubAbstractSessionFromPlatform(databasePlatform), uniqueKeyConstraint, writer);
        return writer.toString();
    }

    @java.lang.Override
    public java.lang.String getAddPrimaryKeyConstraintStatement(java.lang.String tableName, java.lang.String constraintName, java.lang.String... columnName) {
        java.lang.StringBuilder builder = writeAlterTableClause(new java.lang.StringBuilder(), tableName);
        builder = writeAddPrimaryKeyString(builder, constraintName, columnName);
        return builder.toString();
    }

    @java.lang.Override
    public java.lang.String getAddForeignKeyStatement(java.lang.String tableName, java.lang.String constraintName, java.util.List<java.lang.String> keyColumns, java.lang.String referenceTableName, java.util.List<java.lang.String> referenceColumns, boolean shouldCascadeOnDelete) {
        org.eclipse.persistence.tools.schemaframework.ForeignKeyConstraint foreignKeyConstraint = new org.eclipse.persistence.tools.schemaframework.ForeignKeyConstraint();
        foreignKeyConstraint.setName(constraintName);
        foreignKeyConstraint.setTargetTable(referenceTableName);
        foreignKeyConstraint.setSourceFields(keyColumns);
        foreignKeyConstraint.setTargetFields(referenceColumns);
        foreignKeyConstraint.setShouldCascadeOnDelete(shouldCascadeOnDelete);
        org.eclipse.persistence.tools.schemaframework.TableDefinition tableDefinition = new org.eclipse.persistence.tools.schemaframework.TableDefinition();
        tableDefinition.setName(tableName);
        java.io.Writer writer = new java.io.StringWriter();
        tableDefinition.buildConstraintCreationWriter(createStubAbstractSessionFromPlatform(databasePlatform), foreignKeyConstraint, writer);
        return writer.toString();
    }

    @java.lang.Override
    public java.lang.String getAddColumnStatement(java.lang.String tableName, org.apache.ambari.server.orm.DBAccessor.DBColumnInfo columnInfo) {
        java.io.Writer writer = new java.io.StringWriter();
        org.eclipse.persistence.tools.schemaframework.TableDefinition tableDefinition = new org.eclipse.persistence.tools.schemaframework.TableDefinition();
        tableDefinition.setName(tableName);
        tableDefinition.buildAddFieldWriter(createStubAbstractSessionFromPlatform(databasePlatform), convertToFieldDefinition(columnInfo), writer);
        return writer.toString();
    }

    @java.lang.Override
    public java.lang.String getDropTableColumnStatement(java.lang.String tableName, java.lang.String columnName) {
        java.lang.StringBuilder builder = writeAlterTableClause(new java.lang.StringBuilder(), tableName);
        return writeDropTableColumnStatement(builder, columnName).toString();
    }

    @java.lang.Override
    public java.lang.String getRenameColumnStatement(java.lang.String tableName, java.lang.String oldColumnName, java.lang.String newColumnName) {
        throw new java.lang.UnsupportedOperationException("Rename operation not supported.");
    }

    @java.lang.Override
    public java.lang.String getDropTableStatement(java.lang.String tableName) {
        java.io.Writer writer = new java.io.StringWriter();
        org.eclipse.persistence.tools.schemaframework.TableDefinition tableDefinition = new org.eclipse.persistence.tools.schemaframework.TableDefinition();
        tableDefinition.setName(tableName);
        tableDefinition.buildDeletionWriter(createStubAbstractSessionFromPlatform(databasePlatform), writer);
        return writer.toString();
    }

    @java.lang.Override
    public java.lang.String getDropFKConstraintStatement(java.lang.String tableName, java.lang.String constraintName) {
        java.io.Writer writer = new java.io.StringWriter();
        org.eclipse.persistence.tools.schemaframework.ForeignKeyConstraint foreignKeyConstraint = new org.eclipse.persistence.tools.schemaframework.ForeignKeyConstraint();
        foreignKeyConstraint.setName(constraintName);
        foreignKeyConstraint.setTargetTable(tableName);
        org.eclipse.persistence.tools.schemaframework.TableDefinition tableDefinition = new org.eclipse.persistence.tools.schemaframework.TableDefinition();
        tableDefinition.setName(tableName);
        tableDefinition.buildConstraintDeletionWriter(createStubAbstractSessionFromPlatform(databasePlatform), foreignKeyConstraint, writer);
        return writer.toString();
    }

    @java.lang.Override
    public java.lang.String getDropSequenceStatement(java.lang.String sequenceName) {
        java.io.StringWriter writer = new java.io.StringWriter();
        java.lang.String defaultStmt = java.lang.String.format("DROP sequence %s", sequenceName);
        try {
            java.io.Writer w = databasePlatform.buildSequenceObjectDeletionWriter(writer, sequenceName);
            return w != null ? w.toString() : defaultStmt;
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return defaultStmt;
    }

    public org.eclipse.persistence.internal.sessions.AbstractSession createStubAbstractSessionFromPlatform(final org.eclipse.persistence.platform.database.DatabasePlatform databasePlatform) {
        return new org.eclipse.persistence.internal.sessions.AbstractSession() {
            @java.lang.Override
            public org.eclipse.persistence.internal.databaseaccess.Platform getDatasourcePlatform() {
                return databasePlatform;
            }

            @java.lang.Override
            public org.eclipse.persistence.platform.database.DatabasePlatform getPlatform() {
                return databasePlatform;
            }
        };
    }

    @java.lang.Override
    public boolean isConstraintSupportedAfterNullability() {
        return true;
    }

    private java.lang.String escapeParameter(java.lang.Object value) {
        return org.apache.ambari.server.orm.DBAccessorImpl.escapeParameter(value, databasePlatform);
    }
}