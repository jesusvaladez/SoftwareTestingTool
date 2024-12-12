package org.apache.ambari.server.controller.internal;
public class SimplifyingPredicateVisitor implements org.apache.ambari.server.controller.predicate.PredicateVisitor {
    private org.apache.ambari.server.controller.spi.ResourceProvider resourceProvider;

    private org.apache.ambari.server.controller.spi.Predicate lastVisited = null;

    public SimplifyingPredicateVisitor(org.apache.ambari.server.controller.spi.ResourceProvider provider) {
        resourceProvider = provider;
    }

    public java.util.List<org.apache.ambari.server.controller.spi.Predicate> getSimplifiedPredicates() {
        if (lastVisited == null) {
            return java.util.Collections.emptyList();
        }
        if (lastVisited instanceof org.apache.ambari.server.controller.predicate.OrPredicate) {
            return java.util.Arrays.asList(((org.apache.ambari.server.controller.predicate.OrPredicate) (lastVisited)).getPredicates());
        }
        return java.util.Collections.singletonList(lastVisited);
    }

    @java.lang.Override
    public void acceptComparisonPredicate(org.apache.ambari.server.controller.predicate.ComparisonPredicate predicate) {
        if ((predicate instanceof org.apache.ambari.server.controller.predicate.EqualsPredicate) && resourceProvider.checkPropertyIds(java.util.Collections.singleton(predicate.getPropertyId())).isEmpty()) {
            lastVisited = predicate;
        } else {
            lastVisited = org.apache.ambari.server.controller.predicate.AlwaysPredicate.INSTANCE;
        }
    }

    @java.lang.Override
    public void acceptArrayPredicate(org.apache.ambari.server.controller.predicate.ArrayPredicate arrayPredicate) {
        java.util.List<org.apache.ambari.server.controller.spi.Predicate> predicateList = new java.util.LinkedList<>();
        boolean hasOrs = false;
        org.apache.ambari.server.controller.spi.Predicate[] predicates = arrayPredicate.getPredicates();
        if (predicates.length > 0) {
            for (org.apache.ambari.server.controller.spi.Predicate predicate : predicates) {
                org.apache.ambari.server.controller.utilities.PredicateHelper.visit(predicate, this);
                predicateList.add(lastVisited);
                if (lastVisited instanceof org.apache.ambari.server.controller.predicate.OrPredicate) {
                    hasOrs = true;
                }
            }
        }
        if (hasOrs && (arrayPredicate instanceof org.apache.ambari.server.controller.predicate.AndPredicate)) {
            int size = predicateList.size();
            java.util.List<org.apache.ambari.server.controller.spi.Predicate> andPredicateList = new java.util.LinkedList<>();
            for (int i = 0; i < size; ++i) {
                for (int j = i + 1; j < size; ++j) {
                    andPredicateList.addAll(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitor.distribute(predicateList.get(i), predicateList.get(j)));
                }
            }
            lastVisited = org.apache.ambari.server.controller.predicate.OrPredicate.instance(andPredicateList.toArray(new org.apache.ambari.server.controller.spi.Predicate[andPredicateList.size()]));
        } else {
            lastVisited = arrayPredicate.create(predicateList.toArray(new org.apache.ambari.server.controller.spi.Predicate[predicateList.size()]));
        }
    }

    @java.lang.Override
    public void acceptUnaryPredicate(org.apache.ambari.server.controller.predicate.UnaryPredicate predicate) {
        lastVisited = predicate;
    }

    @java.lang.Override
    public void acceptAlwaysPredicate(org.apache.ambari.server.controller.predicate.AlwaysPredicate predicate) {
        lastVisited = predicate;
    }

    private static java.util.List<org.apache.ambari.server.controller.spi.Predicate> distribute(org.apache.ambari.server.controller.spi.Predicate left, org.apache.ambari.server.controller.spi.Predicate right) {
        if (left instanceof org.apache.ambari.server.controller.predicate.OrPredicate) {
            return org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitor.distributeOr(((org.apache.ambari.server.controller.predicate.OrPredicate) (left)), right);
        }
        if (right instanceof org.apache.ambari.server.controller.predicate.OrPredicate) {
            return org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitor.distributeOr(((org.apache.ambari.server.controller.predicate.OrPredicate) (right)), left);
        }
        return java.util.Collections.singletonList(left.equals(right) ? left : org.apache.ambari.server.controller.predicate.AndPredicate.instance(left, right));
    }

    private static java.util.List<org.apache.ambari.server.controller.spi.Predicate> distributeOr(org.apache.ambari.server.controller.predicate.OrPredicate orPredicate, org.apache.ambari.server.controller.spi.Predicate other) {
        java.util.List<org.apache.ambari.server.controller.spi.Predicate> andPredicateList = new java.util.LinkedList<>();
        org.apache.ambari.server.controller.predicate.OrPredicate otherOr = null;
        if (other instanceof org.apache.ambari.server.controller.predicate.OrPredicate) {
            otherOr = ((org.apache.ambari.server.controller.predicate.OrPredicate) (other));
        }
        for (org.apache.ambari.server.controller.spi.Predicate basePredicate : orPredicate.getPredicates()) {
            if (otherOr != null) {
                andPredicateList.addAll(org.apache.ambari.server.controller.internal.SimplifyingPredicateVisitor.distributeOr(otherOr, basePredicate));
            } else {
                andPredicateList.add(basePredicate.equals(other) ? basePredicate : org.apache.ambari.server.controller.predicate.AndPredicate.instance(basePredicate, other));
            }
        }
        return andPredicateList;
    }

    @java.lang.Override
    public void acceptCategoryPredicate(org.apache.ambari.server.controller.predicate.CategoryPredicate predicate) {
        lastVisited = predicate;
    }
}