package org.apache.ambari.server.api.query;
public class ProcessingPredicateVisitor implements org.apache.ambari.server.controller.predicate.PredicateVisitor {
    private final org.apache.ambari.server.api.query.QueryImpl query;

    private org.apache.ambari.server.controller.spi.Predicate lastVisited = null;

    private final java.util.Set<java.lang.String> subResourceCategories = new java.util.HashSet<>();

    private final java.util.Set<java.lang.String> subResourceProperties = new java.util.HashSet<>();

    public ProcessingPredicateVisitor(org.apache.ambari.server.api.query.QueryImpl query) {
        this.query = query;
    }

    @java.lang.Override
    public void acceptComparisonPredicate(org.apache.ambari.server.controller.predicate.ComparisonPredicate predicate) {
        java.lang.String propertyId = predicate.getPropertyId();
        int index = propertyId.indexOf("/");
        java.lang.String category = (index == (-1)) ? propertyId : propertyId.substring(0, index);
        java.util.Map<java.lang.String, org.apache.ambari.server.api.query.QueryImpl> subResources = query.ensureSubResources();
        if (subResources.containsKey(category)) {
            subResourceCategories.add(category);
            subResourceProperties.add(propertyId);
            lastVisited = org.apache.ambari.server.controller.predicate.AlwaysPredicate.INSTANCE;
        } else {
            lastVisited = predicate;
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
        java.lang.String propertyId = predicate.getPropertyIds().iterator().next();
        int index = propertyId.indexOf("/");
        java.lang.String category = (index == (-1)) ? propertyId : propertyId.substring(0, index);
        java.util.Map<java.lang.String, org.apache.ambari.server.api.query.QueryImpl> subResources = query.ensureSubResources();
        if (subResources.containsKey(category)) {
            subResourceCategories.add(category);
            subResourceProperties.add(propertyId);
            lastVisited = org.apache.ambari.server.controller.predicate.AlwaysPredicate.INSTANCE;
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

    public org.apache.ambari.server.controller.spi.Predicate getProcessedPredicate() {
        return lastVisited;
    }

    public java.util.Set<java.lang.String> getSubResourceCategories() {
        return subResourceCategories;
    }

    public java.util.Set<java.lang.String> getSubResourceProperties() {
        return subResourceProperties;
    }
}