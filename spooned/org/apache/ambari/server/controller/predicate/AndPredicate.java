package org.apache.ambari.server.controller.predicate;
public class AndPredicate extends org.apache.ambari.server.controller.predicate.ArrayPredicate {
    public AndPredicate(org.apache.ambari.server.controller.spi.Predicate... predicates) {
        super(predicates);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.Predicate create(org.apache.ambari.server.controller.spi.Predicate... predicates) {
        return org.apache.ambari.server.controller.predicate.AndPredicate.instance(predicates);
    }

    public static org.apache.ambari.server.controller.spi.Predicate instance(org.apache.ambari.server.controller.spi.Predicate... predicates) {
        java.util.List<org.apache.ambari.server.controller.spi.Predicate> predicateList = new java.util.LinkedList<>();
        for (org.apache.ambari.server.controller.spi.Predicate predicate : predicates) {
            if (!(predicate instanceof org.apache.ambari.server.controller.predicate.AlwaysPredicate)) {
                if (predicate instanceof org.apache.ambari.server.controller.predicate.AndPredicate) {
                    predicateList.addAll(java.util.Arrays.asList(((org.apache.ambari.server.controller.predicate.AndPredicate) (predicate)).getPredicates()));
                } else {
                    predicateList.add(predicate);
                }
            }
        }
        return predicateList.size() == 1 ? predicateList.get(0) : new org.apache.ambari.server.controller.predicate.AndPredicate(predicateList.toArray(new org.apache.ambari.server.controller.spi.Predicate[predicateList.size()]));
    }

    @java.lang.Override
    public boolean evaluate(org.apache.ambari.server.controller.spi.Resource resource) {
        org.apache.ambari.server.controller.spi.Predicate[] predicates = getPredicates();
        for (org.apache.ambari.server.controller.spi.Predicate predicate : predicates) {
            if (!predicate.evaluate(resource)) {
                return false;
            }
        }
        return true;
    }

    @java.lang.Override
    public java.lang.String getOperator() {
        return "AND";
    }
}