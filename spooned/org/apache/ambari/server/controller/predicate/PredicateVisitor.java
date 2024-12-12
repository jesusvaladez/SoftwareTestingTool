package org.apache.ambari.server.controller.predicate;
public interface PredicateVisitor {
    void acceptComparisonPredicate(org.apache.ambari.server.controller.predicate.ComparisonPredicate predicate);

    void acceptArrayPredicate(org.apache.ambari.server.controller.predicate.ArrayPredicate predicate);

    void acceptUnaryPredicate(org.apache.ambari.server.controller.predicate.UnaryPredicate predicate);

    void acceptAlwaysPredicate(org.apache.ambari.server.controller.predicate.AlwaysPredicate predicate);

    void acceptCategoryPredicate(org.apache.ambari.server.controller.predicate.CategoryPredicate predicate);
}