package org.apache.ambari.server.controller.predicate;
public abstract class UnaryPredicate implements org.apache.ambari.server.controller.predicate.BasePredicate {
    private final org.apache.ambari.server.controller.spi.Predicate predicate;

    public UnaryPredicate(org.apache.ambari.server.controller.spi.Predicate predicate) {
        assert predicate != null;
        this.predicate = predicate;
    }

    public org.apache.ambari.server.controller.spi.Predicate getPredicate() {
        return predicate;
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> getPropertyIds() {
        return org.apache.ambari.server.controller.utilities.PredicateHelper.getPropertyIds(predicate);
    }

    @java.lang.Override
    public void accept(org.apache.ambari.server.controller.predicate.PredicateVisitor visitor) {
        visitor.acceptUnaryPredicate(this);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if (!(o instanceof org.apache.ambari.server.controller.predicate.UnaryPredicate))
            return false;

        org.apache.ambari.server.controller.predicate.UnaryPredicate that = ((org.apache.ambari.server.controller.predicate.UnaryPredicate) (o));
        return predicate.equals(that.predicate);
    }

    @java.lang.Override
    public int hashCode() {
        return predicate.hashCode();
    }

    public abstract java.lang.String getOperator();

    @java.lang.Override
    public java.lang.String toString() {
        return ((getOperator() + "(") + getPredicate()) + ")";
    }
}