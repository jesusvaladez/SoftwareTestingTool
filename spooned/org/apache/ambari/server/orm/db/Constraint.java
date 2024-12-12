package org.apache.ambari.server.orm.db;
abstract class Constraint<ContentType> {
    abstract java.lang.String name();

    abstract ContentType content();

    static org.apache.ambari.server.orm.db.SimpleConstraint pk(java.lang.String name, java.util.Collection<java.lang.String> columns) {
        com.google.common.base.Preconditions.checkArgument(!columns.isEmpty(), "Columns must not be empty.");
        return new org.apache.ambari.server.orm.db.SimpleConstraint(name, "PK", columns);
    }

    static org.apache.ambari.server.orm.db.SimpleConstraint uq(java.lang.String name, java.util.Collection<java.lang.String> columns) {
        com.google.common.base.Preconditions.checkArgument(!columns.isEmpty(), "Columns must not be empty.");
        return new org.apache.ambari.server.orm.db.SimpleConstraint(name, "PK", columns);
    }

    static org.apache.ambari.server.orm.db.FKConstraint fk(java.lang.String name, java.util.Collection<java.lang.String> columns, java.lang.String referredTableName, java.util.Collection<java.lang.String> referredColumns) {
        com.google.common.base.Preconditions.checkArgument(!columns.isEmpty(), "Columns must not be empty.");
        com.google.common.base.Preconditions.checkArgument(!referredColumns.isEmpty(), "Referred columns must not be empty.");
        return new org.apache.ambari.server.orm.db.FKConstraint(name, columns, referredTableName, referredColumns);
    }
}