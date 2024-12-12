package org.apache.ambari.server.orm.db;
class DDL {
    final java.lang.String dbType;

    final java.util.Map<java.lang.String, org.apache.ambari.server.orm.db.Table> tables;

    final java.util.List<java.lang.String> alterTables;

    java.util.Set<java.lang.String> tableNames() {
        return tables.keySet();
    }

    DDL(java.lang.String dbType, java.util.Map<java.lang.String, org.apache.ambari.server.orm.db.Table> tables, java.util.List<java.lang.String> alterTables) {
        this.dbType = dbType;
        this.tables = tables;
        this.alterTables = alterTables;
    }
}