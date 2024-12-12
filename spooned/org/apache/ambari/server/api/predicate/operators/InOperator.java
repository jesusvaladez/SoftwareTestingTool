package org.apache.ambari.server.api.predicate.operators;
public class InOperator extends org.apache.ambari.server.api.predicate.operators.AbstractOperator implements org.apache.ambari.server.api.predicate.operators.RelationalOperator {
    public InOperator() {
        super(0);
    }

    @java.lang.Override
    public java.lang.String getName() {
        return "InOperator";
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.Predicate toPredicate(java.lang.String prop, java.lang.String val) throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        if (val == null) {
            throw new org.apache.ambari.server.api.predicate.InvalidQueryException("IN operator is missing a required right operand for property " + prop);
        }
        java.lang.String[] tokens = val.split(",");
        java.util.List<org.apache.ambari.server.controller.predicate.EqualsPredicate> listPredicates = new java.util.ArrayList<>();
        for (java.lang.String token : tokens) {
            listPredicates.add(new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(prop, token.trim()));
        }
        return listPredicates.size() == 1 ? listPredicates.get(0) : buildOrPredicate(listPredicates);
    }

    private org.apache.ambari.server.controller.predicate.OrPredicate buildOrPredicate(java.util.List<org.apache.ambari.server.controller.predicate.EqualsPredicate> listPredicates) {
        return new org.apache.ambari.server.controller.predicate.OrPredicate(listPredicates.toArray(new org.apache.ambari.server.controller.spi.Predicate[listPredicates.size()]));
    }

    @java.lang.Override
    public org.apache.ambari.server.api.predicate.operators.Operator.TYPE getType() {
        return org.apache.ambari.server.api.predicate.operators.Operator.TYPE.IN;
    }
}