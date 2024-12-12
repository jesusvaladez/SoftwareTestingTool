package org.apache.ambari.server.controller.predicate;
public class AlwaysPredicate implements org.apache.ambari.server.controller.predicate.BasePredicate {
    public static final org.apache.ambari.server.controller.predicate.AlwaysPredicate INSTANCE = new org.apache.ambari.server.controller.predicate.AlwaysPredicate();

    @java.lang.Override
    public boolean evaluate(org.apache.ambari.server.controller.spi.Resource resource) {
        return true;
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> getPropertyIds() {
        return java.util.Collections.emptySet();
    }

    @java.lang.Override
    public void accept(org.apache.ambari.server.controller.predicate.PredicateVisitor visitor) {
        visitor.acceptAlwaysPredicate(this);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "TRUE";
    }
}