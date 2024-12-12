package org.apache.ambari.server.controller.predicate;
import javax.annotation.Nonnull;
public class OrPredicate extends org.apache.ambari.server.controller.predicate.ArrayPredicate {
    public OrPredicate(org.apache.ambari.server.controller.spi.Predicate... predicates) {
        super(predicates);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.Predicate create(org.apache.ambari.server.controller.spi.Predicate... predicates) {
        return org.apache.ambari.server.controller.predicate.OrPredicate.instance(predicates);
    }

    public static org.apache.ambari.server.controller.spi.Predicate instance(org.apache.ambari.server.controller.spi.Predicate... predicates) {
        return org.apache.ambari.server.controller.predicate.OrPredicate.of(java.util.Arrays.asList(predicates));
    }

    public static org.apache.ambari.server.controller.spi.Predicate of(@javax.annotation.Nonnull
    java.lang.Iterable<? extends org.apache.ambari.server.controller.spi.Predicate> predicates) {
        java.util.List<org.apache.ambari.server.controller.spi.Predicate> predicateList = new java.util.LinkedList<>();
        for (org.apache.ambari.server.controller.spi.Predicate predicate : predicates) {
            if (predicate instanceof org.apache.ambari.server.controller.predicate.AlwaysPredicate) {
                return predicate;
            } else if (predicate instanceof org.apache.ambari.server.controller.predicate.OrPredicate) {
                predicateList.addAll(java.util.Arrays.asList(((org.apache.ambari.server.controller.predicate.OrPredicate) (predicate)).getPredicates()));
            } else {
                predicateList.add(predicate);
            }
        }
        return predicateList.size() == 1 ? predicateList.get(0) : new org.apache.ambari.server.controller.predicate.OrPredicate(predicateList.toArray(new org.apache.ambari.server.controller.spi.Predicate[predicateList.size()]));
    }

    @java.lang.Override
    public boolean evaluate(org.apache.ambari.server.controller.spi.Resource resource) {
        org.apache.ambari.server.controller.spi.Predicate[] predicates = getPredicates();
        for (org.apache.ambari.server.controller.spi.Predicate predicate : predicates) {
            if (predicate.evaluate(resource)) {
                return true;
            }
        }
        return false;
    }

    @java.lang.Override
    public java.lang.String getOperator() {
        return "OR";
    }
}