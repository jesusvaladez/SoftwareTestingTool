package org.apache.ambari.server.controller.utilities;
public class PredicateHelper {
    public static java.util.Set<java.lang.String> getPropertyIds(org.apache.ambari.server.controller.spi.Predicate predicate) {
        if (predicate instanceof org.apache.ambari.server.controller.predicate.BasePredicate) {
            return ((org.apache.ambari.server.controller.predicate.BasePredicate) (predicate)).getPropertyIds();
        }
        return java.util.Collections.emptySet();
    }

    public static void visit(org.apache.ambari.server.controller.spi.Predicate predicate, org.apache.ambari.server.controller.predicate.PredicateVisitor visitor) {
        if (predicate instanceof org.apache.ambari.server.controller.predicate.PredicateVisitorAcceptor) {
            ((org.apache.ambari.server.controller.predicate.PredicateVisitorAcceptor) (predicate)).accept(visitor);
        }
    }

    public static java.util.Map<java.lang.String, java.lang.Object> getProperties(org.apache.ambari.server.controller.spi.Predicate predicate) {
        if (predicate == null) {
            return java.util.Collections.emptyMap();
        }
        org.apache.ambari.server.controller.internal.PropertyPredicateVisitor visitor = new org.apache.ambari.server.controller.internal.PropertyPredicateVisitor();
        org.apache.ambari.server.controller.utilities.PredicateHelper.visit(predicate, visitor);
        return visitor.getProperties();
    }
}