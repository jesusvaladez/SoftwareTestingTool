package org.apache.ambari.server.orm.db;
class SimpleConstraint extends org.apache.ambari.server.orm.db.Constraint<java.util.Set<java.lang.String>> {
    final java.lang.String name;

    final java.lang.String type;

    final com.google.common.collect.ImmutableSet<java.lang.String> columns;

    SimpleConstraint(java.lang.String name, java.lang.String type, java.util.Collection<java.lang.String> columns) {
        this.name = name;
        this.type = type;
        this.columns = (columns instanceof com.google.common.collect.ImmutableSet) ? ((com.google.common.collect.ImmutableSet<java.lang.String>) (columns)) : com.google.common.collect.ImmutableSet.copyOf(columns);
    }

    public java.lang.String name() {
        return name;
    }

    public com.google.common.collect.ImmutableSet<java.lang.String> content() {
        return columns;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return java.lang.String.format("%s %s [%s]", type, name, com.google.common.base.Joiner.on(',').join(columns));
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.db.SimpleConstraint that = ((org.apache.ambari.server.orm.db.SimpleConstraint) (o));
        if (!name.equals(that.name))
            return false;

        if (!type.equals(that.type))
            return false;

        return columns.equals(that.columns);
    }

    @java.lang.Override
    public int hashCode() {
        int result = name.hashCode();
        result = (31 * result) + type.hashCode();
        result = (31 * result) + columns.hashCode();
        return result;
    }
}