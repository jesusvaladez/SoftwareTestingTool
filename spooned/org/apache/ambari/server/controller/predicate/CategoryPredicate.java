package org.apache.ambari.server.controller.predicate;
public abstract class CategoryPredicate extends org.apache.ambari.server.controller.predicate.PropertyPredicate implements org.apache.ambari.server.controller.predicate.BasePredicate {
    public CategoryPredicate(java.lang.String propertyId) {
        super(propertyId);
    }

    @java.lang.Override
    public void accept(org.apache.ambari.server.controller.predicate.PredicateVisitor visitor) {
        visitor.acceptCategoryPredicate(this);
    }
}