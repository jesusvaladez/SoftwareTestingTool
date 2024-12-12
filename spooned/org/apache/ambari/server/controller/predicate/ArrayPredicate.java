package org.apache.ambari.server.controller.predicate;
public abstract class ArrayPredicate implements org.apache.ambari.server.controller.predicate.BasePredicate {
    private final org.apache.ambari.server.controller.spi.Predicate[] predicates;

    private final java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();

    public ArrayPredicate(org.apache.ambari.server.controller.spi.Predicate... predicates) {
        this.predicates = predicates;
        for (org.apache.ambari.server.controller.spi.Predicate predicate : predicates) {
            propertyIds.addAll(org.apache.ambari.server.controller.utilities.PredicateHelper.getPropertyIds(predicate));
        }
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> getPropertyIds() {
        return propertyIds;
    }

    @java.lang.Override
    public void accept(org.apache.ambari.server.controller.predicate.PredicateVisitor visitor) {
        visitor.acceptArrayPredicate(this);
    }

    public abstract java.lang.String getOperator();

    public abstract org.apache.ambari.server.controller.spi.Predicate create(org.apache.ambari.server.controller.spi.Predicate... predicates);

    public org.apache.ambari.server.controller.spi.Predicate[] getPredicates() {
        return predicates;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if (!(o instanceof org.apache.ambari.server.controller.predicate.ArrayPredicate))
            return false;

        org.apache.ambari.server.controller.predicate.ArrayPredicate that = ((org.apache.ambari.server.controller.predicate.ArrayPredicate) (o));
        if (propertyIds != null ? !propertyIds.equals(that.propertyIds) : that.propertyIds != null)
            return false;

        java.util.Set<org.apache.ambari.server.controller.spi.Predicate> setThisPredicates = new java.util.HashSet<>(java.util.Arrays.asList(predicates));
        java.util.Set<org.apache.ambari.server.controller.spi.Predicate> setThatPredicates = new java.util.HashSet<>(java.util.Arrays.asList(that.predicates));
        return setThisPredicates.equals(setThatPredicates);
    }

    @java.lang.Override
    public int hashCode() {
        int result = (predicates != null) ? new java.util.HashSet<>(java.util.Arrays.asList(predicates)).hashCode() : 0;
        result = (31 * result) + (propertyIds != null ? propertyIds.hashCode() : 0);
        return result;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (org.apache.ambari.server.controller.spi.Predicate predicate : predicates) {
            boolean arrayPredicate = predicate instanceof org.apache.ambari.server.controller.predicate.ArrayPredicate;
            if (sb.length() > 0) {
                sb.append(" ").append(getOperator()).append(" ");
            }
            if (arrayPredicate) {
                sb.append("(").append(predicate).append(")");
            } else {
                sb.append(predicate);
            }
        }
        return sb.toString();
    }
}