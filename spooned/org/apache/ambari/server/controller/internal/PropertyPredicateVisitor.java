package org.apache.ambari.server.controller.internal;
public class PropertyPredicateVisitor implements org.apache.ambari.server.controller.predicate.PredicateVisitor {
    private final java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();

    @java.lang.Override
    public void acceptComparisonPredicate(org.apache.ambari.server.controller.predicate.ComparisonPredicate predicate) {
        properties.put(predicate.getPropertyId(), predicate.getValue());
    }

    @java.lang.Override
    public void acceptArrayPredicate(org.apache.ambari.server.controller.predicate.ArrayPredicate predicate) {
        org.apache.ambari.server.controller.spi.Predicate[] predicates = predicate.getPredicates();
        for (org.apache.ambari.server.controller.spi.Predicate predicate1 : predicates) {
            org.apache.ambari.server.controller.utilities.PredicateHelper.visit(predicate1, this);
        }
    }

    @java.lang.Override
    public void acceptUnaryPredicate(org.apache.ambari.server.controller.predicate.UnaryPredicate predicate) {
    }

    @java.lang.Override
    public void acceptAlwaysPredicate(org.apache.ambari.server.controller.predicate.AlwaysPredicate predicate) {
    }

    @java.lang.Override
    public void acceptCategoryPredicate(org.apache.ambari.server.controller.predicate.CategoryPredicate predicate) {
    }

    public java.util.Map<java.lang.String, java.lang.Object> getProperties() {
        return properties;
    }
}