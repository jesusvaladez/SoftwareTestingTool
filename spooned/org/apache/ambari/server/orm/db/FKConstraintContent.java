package org.apache.ambari.server.orm.db;
class FKConstraintContent {
    final com.google.common.collect.ImmutableSet<java.lang.String> columns;

    final java.lang.String referredTable;

    final com.google.common.collect.ImmutableSet<java.lang.String> referredColumns;

    public FKConstraintContent(java.util.Collection<java.lang.String> columns, java.lang.String referredTable, java.util.Collection<java.lang.String> referredColumns) {
        this.columns = (columns instanceof com.google.common.collect.ImmutableSet) ? ((com.google.common.collect.ImmutableSet<java.lang.String>) (columns)) : com.google.common.collect.ImmutableSet.copyOf(columns);
        this.referredTable = referredTable;
        this.referredColumns = (referredColumns instanceof com.google.common.collect.ImmutableSet) ? ((com.google.common.collect.ImmutableSet<java.lang.String>) (referredColumns)) : com.google.common.collect.ImmutableSet.copyOf(referredColumns);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.db.FKConstraintContent that = ((org.apache.ambari.server.orm.db.FKConstraintContent) (o));
        if (!columns.equals(that.columns))
            return false;

        if (!referredTable.equals(that.referredTable))
            return false;

        return referredColumns.equals(that.referredColumns);
    }

    @java.lang.Override
    public int hashCode() {
        int result = columns.hashCode();
        result = (31 * result) + referredTable.hashCode();
        result = (31 * result) + referredColumns.hashCode();
        return result;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return java.lang.String.format("[%s] --> %s [%s]", com.google.common.base.Joiner.on(',').join(columns), referredTable, com.google.common.base.Joiner.on(',').join(referredColumns));
    }
}