package org.apache.ambari.server.api.query;
public class ExtendedResourcePredicateVisitor implements org.apache.ambari.server.controller.predicate.PredicateVisitor {
    private org.apache.ambari.server.controller.spi.Predicate lastVisited = null;

    private final java.util.Map<org.apache.ambari.server.controller.spi.Resource, java.util.Set<java.util.Map<java.lang.String, java.lang.Object>>> joinedResources;

    public ExtendedResourcePredicateVisitor(java.util.Map<org.apache.ambari.server.controller.spi.Resource, java.util.Set<java.util.Map<java.lang.String, java.lang.Object>>> extendedProperties) {
        this.joinedResources = extendedProperties;
    }

    @java.lang.Override
    public void acceptComparisonPredicate(org.apache.ambari.server.controller.predicate.ComparisonPredicate predicate) {
        lastVisited = new org.apache.ambari.server.api.query.ExtendedResourcePredicateVisitor.ExtendedResourcePredicate(predicate, joinedResources);
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
        lastVisited = new org.apache.ambari.server.api.query.ExtendedResourcePredicateVisitor.ExtendedResourcePredicate(predicate, joinedResources);
    }

    @java.lang.Override
    public void acceptAlwaysPredicate(org.apache.ambari.server.controller.predicate.AlwaysPredicate predicate) {
        lastVisited = predicate;
    }

    @java.lang.Override
    public void acceptCategoryPredicate(org.apache.ambari.server.controller.predicate.CategoryPredicate predicate) {
        lastVisited = new org.apache.ambari.server.api.query.ExtendedResourcePredicateVisitor.ExtendedResourcePredicate(predicate, joinedResources);
    }

    public org.apache.ambari.server.controller.spi.Predicate getExtendedPredicate() {
        return lastVisited;
    }

    private static class ExtendedResourcePredicate implements org.apache.ambari.server.controller.spi.Predicate {
        private final org.apache.ambari.server.controller.spi.Predicate predicate;

        private final java.util.Map<org.apache.ambari.server.controller.spi.Resource, java.util.Set<java.util.Map<java.lang.String, java.lang.Object>>> joinedResources;

        public ExtendedResourcePredicate(org.apache.ambari.server.controller.spi.Predicate predicate, java.util.Map<org.apache.ambari.server.controller.spi.Resource, java.util.Set<java.util.Map<java.lang.String, java.lang.Object>>> joinedResources) {
            this.predicate = predicate;
            this.joinedResources = joinedResources;
        }

        @java.lang.Override
        public boolean evaluate(org.apache.ambari.server.controller.spi.Resource resource) {
            java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> extendedPropertySet = joinedResources.get(resource);
            if (extendedPropertySet == null) {
                return predicate.evaluate(resource);
            }
            for (java.util.Map<java.lang.String, java.lang.Object> extendedProperties : extendedPropertySet) {
                org.apache.ambari.server.controller.spi.Resource extendedResource = new org.apache.ambari.server.api.query.ExtendedResourcePredicateVisitor.ExtendedResourceImpl(resource, extendedProperties);
                if (predicate.evaluate(extendedResource)) {
                    return true;
                }
            }
            return false;
        }
    }

    private static class ExtendedResourceImpl extends org.apache.ambari.server.controller.internal.ResourceImpl {
        public ExtendedResourceImpl(org.apache.ambari.server.controller.spi.Resource resource, java.util.Map<java.lang.String, java.lang.Object> extendedProperties) {
            super(resource);
            initProperties(extendedProperties);
        }

        private void initProperties(java.util.Map<java.lang.String, java.lang.Object> extendedProperties) {
            for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : extendedProperties.entrySet()) {
                setProperty(entry.getKey(), entry.getValue());
            }
        }
    }
}