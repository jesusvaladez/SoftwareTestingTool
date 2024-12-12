package org.apache.ambari.server.collections.functors;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.PredicateDecorator;
public class NotPredicate extends org.apache.ambari.server.collections.functors.DelegatedSinglePredicateContainer {
    public static final java.lang.String NAME = "not";

    public static org.apache.ambari.server.collections.functors.NotPredicate fromMap(java.util.Map<java.lang.String, java.lang.Object> map) {
        java.lang.Object data = (map == null) ? null : map.get(org.apache.ambari.server.collections.functors.NotPredicate.NAME);
        if (data == null) {
            throw new java.lang.IllegalArgumentException(("Missing data for '" + org.apache.ambari.server.collections.functors.NotPredicate.NAME) + "' operation");
        } else if (data instanceof java.util.Map) {
            return new org.apache.ambari.server.collections.functors.NotPredicate(org.apache.ambari.server.collections.PredicateUtils.fromMap(((java.util.Map) (data))));
        } else {
            throw new java.lang.IllegalArgumentException(("Missing data for '" + org.apache.ambari.server.collections.functors.NotPredicate.NAME) + "' operation");
        }
    }

    public NotPredicate(org.apache.commons.collections.Predicate predicate) {
        super(org.apache.ambari.server.collections.functors.NotPredicate.NAME, ((org.apache.commons.collections.functors.PredicateDecorator) (org.apache.ambari.server.collections.functors.NotPredicate.getInstance(predicate))));
    }

    @java.lang.Override
    public int hashCode() {
        return super.hashCode() + this.getClass().getName().hashCode();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        return (obj == this) || ((obj != null) && ((super.equals(obj) && this.getClass().isInstance(obj)) && (hashCode() == obj.hashCode())));
    }
}