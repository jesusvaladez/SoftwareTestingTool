package org.apache.ambari.server.orm.db;
class Table {
    final java.lang.String name;

    final com.google.common.collect.ImmutableSet<java.lang.String> columns;

    final com.google.common.base.Optional<org.apache.ambari.server.orm.db.SimpleConstraint> primaryKey;

    final com.google.common.collect.ImmutableSet<org.apache.ambari.server.orm.db.FKConstraint> foreignKeys;

    final com.google.common.collect.ImmutableSet<org.apache.ambari.server.orm.db.SimpleConstraint> uniqueConstraints;

    Table(java.lang.String name, java.util.Set<java.lang.String> columns, com.google.common.base.Optional<org.apache.ambari.server.orm.db.SimpleConstraint> primaryKey, java.util.Set<org.apache.ambari.server.orm.db.FKConstraint> foreignKeys, java.util.Set<org.apache.ambari.server.orm.db.SimpleConstraint> uniqueConstraints) {
        this.name = name;
        this.columns = (columns instanceof com.google.common.collect.ImmutableSet) ? ((com.google.common.collect.ImmutableSet<java.lang.String>) (columns)) : com.google.common.collect.ImmutableSet.copyOf(columns);
        this.primaryKey = primaryKey;
        this.foreignKeys = (foreignKeys instanceof com.google.common.collect.ImmutableSet) ? ((com.google.common.collect.ImmutableSet<org.apache.ambari.server.orm.db.FKConstraint>) (foreignKeys)) : com.google.common.collect.ImmutableSet.copyOf(foreignKeys);
        this.uniqueConstraints = (uniqueConstraints instanceof com.google.common.collect.ImmutableSet) ? ((com.google.common.collect.ImmutableSet<org.apache.ambari.server.orm.db.SimpleConstraint>) (uniqueConstraints)) : com.google.common.collect.ImmutableSet.copyOf(uniqueConstraints);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return java.lang.String.format("TABLE name: %s, columns: %s, pk: %s, fks: %s, uqs: %s", name, com.google.common.collect.Iterables.toString(columns), primaryKey, com.google.common.collect.Iterables.toString(foreignKeys), com.google.common.collect.Iterables.toString(uniqueConstraints));
    }
}