package org.apache.ambari.server.controller.jdbc;
public class SQLPredicateVisitor implements org.apache.ambari.server.controller.predicate.PredicateVisitor {
    private final java.lang.StringBuilder stringBuilder = new java.lang.StringBuilder();

    @java.lang.Override
    public void acceptComparisonPredicate(org.apache.ambari.server.controller.predicate.ComparisonPredicate predicate) {
        java.lang.String propertyId = predicate.getPropertyId();
        java.lang.String propertyCategory = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(propertyId);
        if (propertyCategory != null) {
            stringBuilder.append(propertyCategory).append(".");
        }
        stringBuilder.append(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyName(propertyId));
        stringBuilder.append(" ").append(predicate.getOperator()).append(" \"");
        stringBuilder.append(predicate.getValue());
        stringBuilder.append("\"");
    }

    @java.lang.Override
    public void acceptArrayPredicate(org.apache.ambari.server.controller.predicate.ArrayPredicate predicate) {
        org.apache.ambari.server.controller.spi.Predicate[] predicates = predicate.getPredicates();
        if (predicates.length > 0) {
            stringBuilder.append("(");
            for (int i = 0; i < predicates.length; i++) {
                if (i > 0) {
                    stringBuilder.append(" ").append(predicate.getOperator()).append(" ");
                }
                org.apache.ambari.server.controller.utilities.PredicateHelper.visit(predicates[i], this);
            }
            stringBuilder.append(")");
        }
    }

    @java.lang.Override
    public void acceptUnaryPredicate(org.apache.ambari.server.controller.predicate.UnaryPredicate predicate) {
        stringBuilder.append(predicate.getOperator()).append("(");
        org.apache.ambari.server.controller.utilities.PredicateHelper.visit(predicate.getPredicate(), this);
        stringBuilder.append(")");
    }

    @java.lang.Override
    public void acceptAlwaysPredicate(org.apache.ambari.server.controller.predicate.AlwaysPredicate predicate) {
        stringBuilder.append("TRUE");
    }

    @java.lang.Override
    public void acceptCategoryPredicate(org.apache.ambari.server.controller.predicate.CategoryPredicate predicate) {
    }

    public java.lang.String getSQL() {
        return stringBuilder.toString();
    }
}