package org.apache.ambari.server.orm.db;
class FKConstraint extends org.apache.ambari.server.orm.db.Constraint<org.apache.ambari.server.orm.db.FKConstraintContent> {
    final java.lang.String name;

    final org.apache.ambari.server.orm.db.FKConstraintContent content;

    FKConstraint(java.lang.String name, java.util.Collection<java.lang.String> columns, java.lang.String referredTable, java.util.Collection<java.lang.String> referredColumns) {
        this.name = name;
        this.content = new org.apache.ambari.server.orm.db.FKConstraintContent(columns, referredTable, referredColumns);
    }

    public java.lang.String name() {
        return name;
    }

    public org.apache.ambari.server.orm.db.FKConstraintContent content() {
        return content;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return java.lang.String.format("FK name:%s content: %s", name, content);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.orm.db.FKConstraint that = ((org.apache.ambari.server.orm.db.FKConstraint) (o));
        if (!name.equals(that.name))
            return false;

        return content.equals(that.content);
    }

    @java.lang.Override
    public int hashCode() {
        int result = name.hashCode();
        result = (31 * result) + content.hashCode();
        return result;
    }
}