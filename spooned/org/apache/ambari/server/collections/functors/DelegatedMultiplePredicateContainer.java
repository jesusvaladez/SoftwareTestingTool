package org.apache.ambari.server.collections.functors;
import org.apache.commons.collections.functors.PredicateDecorator;
abstract class DelegatedMultiplePredicateContainer extends org.apache.ambari.server.collections.Predicate implements org.apache.commons.collections.functors.PredicateDecorator {
    private final org.apache.commons.collections.functors.PredicateDecorator delegate;

    DelegatedMultiplePredicateContainer(java.lang.String name, org.apache.commons.collections.functors.PredicateDecorator delegate) {
        super(name);
        this.delegate = delegate;
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.lang.Object> toMap() {
        return java.util.Collections.singletonMap(getName(), containedPredicatesToMaps());
    }

    @java.lang.Override
    public boolean evaluate(java.lang.Object o) {
        return delegate.evaluate(o);
    }

    @java.lang.Override
    public org.apache.ambari.server.collections.Predicate[] getPredicates() {
        return delegate.getPredicates();
    }

    @java.lang.Override
    public int hashCode() {
        return super.hashCode() + (delegate == null ? 0 : delegate.hashCode());
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (obj == this) {
            return true;
        } else if (obj == null) {
            return false;
        } else if ((super.equals(obj) && (obj instanceof org.apache.ambari.server.collections.functors.DelegatedMultiplePredicateContainer)) && (hashCode() == obj.hashCode())) {
            org.apache.ambari.server.collections.functors.DelegatedMultiplePredicateContainer p = ((org.apache.ambari.server.collections.functors.DelegatedMultiplePredicateContainer) (obj));
            return delegate == null ? p.delegate == null : delegate.equals(p.delegate);
        } else {
            return false;
        }
    }

    private java.util.List<java.util.Map<java.lang.String, java.lang.Object>> containedPredicatesToMaps() {
        java.util.List<java.util.Map<java.lang.String, java.lang.Object>> list = new java.util.ArrayList<>();
        if (delegate != null) {
            org.apache.ambari.server.collections.functors.collections[] predicates = delegate.getPredicates();
            if (predicates != null) {
                for (org.apache.commons.collections.Predicate p : predicates) {
                    if (p instanceof org.apache.ambari.server.collections.Predicate) {
                        list.add(((org.apache.ambari.server.collections.Predicate) (p)).toMap());
                    } else {
                        throw new java.lang.UnsupportedOperationException(java.lang.String.format("Cannot convert a %s to a Map", p.getClass().getName()));
                    }
                }
            }
        }
        return list;
    }
}