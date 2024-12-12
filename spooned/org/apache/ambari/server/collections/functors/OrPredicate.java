package org.apache.ambari.server.collections.functors;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.functors.PredicateDecorator;
public class OrPredicate extends org.apache.ambari.server.collections.functors.DelegatedMultiplePredicateContainer {
    public static final java.lang.String NAME = "or";

    public static org.apache.ambari.server.collections.functors.OrPredicate fromMap(java.util.Map<java.lang.String, java.lang.Object> map) {
        java.lang.Object data = (map == null) ? null : map.get(org.apache.ambari.server.collections.functors.OrPredicate.NAME);
        if (data == null) {
            throw new java.lang.IllegalArgumentException(("Missing data for '" + org.apache.ambari.server.collections.functors.OrPredicate.NAME) + "' operation");
        } else if (data instanceof java.util.Collection) {
            java.util.Collection<?> collection = ((java.util.Collection) (data));
            if (collection.size() == 2) {
                java.util.Iterator<?> iterator = collection.iterator();
                java.lang.Object d1 = iterator.next();
                java.lang.Object d2 = iterator.next();
                if ((d1 instanceof java.util.Map) && (d2 instanceof java.util.Map)) {
                    return new org.apache.ambari.server.collections.functors.OrPredicate(org.apache.ambari.server.collections.PredicateUtils.fromMap(((java.util.Map) (d1))), org.apache.ambari.server.collections.PredicateUtils.fromMap(((java.util.Map) (d2))));
                } else {
                    throw new java.lang.IllegalArgumentException(java.lang.String.format("Unexpected data types for predicates: %s and %s", d1.getClass().getName(), d2.getClass().getName()));
                }
            } else {
                throw new java.lang.IllegalArgumentException(java.lang.String.format(("Missing data for '" + org.apache.ambari.server.collections.functors.OrPredicate.NAME) + "' operation - 2 predicates are needed, %d found", collection.size()));
            }
        } else {
            throw new java.lang.IllegalArgumentException(java.lang.String.format(("Unexpected data type for '" + org.apache.ambari.server.collections.functors.OrPredicate.NAME) + "' operation - %s", data.getClass().getName()));
        }
    }

    public OrPredicate(org.apache.commons.collections.Predicate predicate1, org.apache.commons.collections.Predicate predicate2) {
        super(org.apache.ambari.server.collections.functors.OrPredicate.NAME, ((org.apache.commons.collections.functors.PredicateDecorator) (org.apache.ambari.server.collections.functors.OrPredicate.getInstance(predicate1, predicate2))));
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