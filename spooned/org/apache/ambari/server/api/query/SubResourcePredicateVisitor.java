package org.apache.ambari.server.api.query;
public class SubResourcePredicateVisitor implements org.apache.ambari.server.controller.predicate.PredicateVisitor {
    private org.apache.ambari.server.controller.spi.Predicate lastVisited = null;

    private final java.lang.String category;

    public SubResourcePredicateVisitor(java.lang.String category) {
        this.category = category;
    }

    @java.lang.Override
    public void acceptComparisonPredicate(org.apache.ambari.server.controller.predicate.ComparisonPredicate predicate) {
        java.lang.String propertyId = predicate.getPropertyId();
        int index = propertyId.indexOf("/");
        java.lang.String category = (index == (-1)) ? propertyId : propertyId.substring(0, index);
        if ((index > (-1)) && category.equals(this.category)) {
            lastVisited = predicate.copy(propertyId.substring(index + 1));
        } else {
            lastVisited = org.apache.ambari.server.controller.predicate.AlwaysPredicate.INSTANCE;
        }
    }

    @java.lang.Override
    public void acceptArrayPredicate(org.apache.ambari.server.controller.predicate.ArrayPredicate arrayPredicate) {
        java.util.List<org.apache.ambari.server.controller.spi.Predicate> predicateList = new java.util.LinkedList<>();
        org.apache.ambari.server.controller.spi.Predicate[] predicates = arrayPredicate.getPredicates();
        if (predicates.length > 0) {
            for (org.apache.ambari.server.controller.spi.Predicate predicate : predicates) {
                org.apache.ambari.server.controller.utilities.PredicateHelper.visit(predicate, this);
                predicateList.add(lastVisited);
            }
        }
        lastVisited = arrayPredicate.create(predicateList.toArray(new org.apache.ambari.server.controller.spi.Predicate[predicateList.size()]));
    }

    @java.lang.Override
    public void acceptUnaryPredicate(org.apache.ambari.server.controller.predicate.UnaryPredicate predicate) {
        if (predicate.getPredicate() instanceof org.apache.ambari.server.controller.predicate.ComparisonPredicate) {
            org.apache.ambari.server.controller.predicate.ComparisonPredicate innerPredicate = ((org.apache.ambari.server.controller.predicate.ComparisonPredicate) (predicate.getPredicate()));
            java.lang.String propertyId = innerPredicate.getPropertyId();
            int index = propertyId.indexOf("/");
            java.lang.String category = (index == (-1)) ? propertyId : propertyId.substring(0, index);
            if ((index > (-1)) && category.equals(this.category)) {
                lastVisited = new org.apache.ambari.server.controller.predicate.NotPredicate(innerPredicate.copy(propertyId.substring(index + 1)));
            } else {
                lastVisited = org.apache.ambari.server.controller.predicate.AlwaysPredicate.INSTANCE;
            }
        } else {
            lastVisited = predicate;
        }
    }

    @java.lang.Override
    public void acceptAlwaysPredicate(org.apache.ambari.server.controller.predicate.AlwaysPredicate predicate) {
        lastVisited = predicate;
    }

    @java.lang.Override
    public void acceptCategoryPredicate(org.apache.ambari.server.controller.predicate.CategoryPredicate predicate) {
        lastVisited = predicate;
    }

    public org.apache.ambari.server.controller.spi.Predicate getSubResourcePredicate() {
        return lastVisited;
    }
}