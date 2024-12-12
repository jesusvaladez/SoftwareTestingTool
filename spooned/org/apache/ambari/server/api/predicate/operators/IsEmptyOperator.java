package org.apache.ambari.server.api.predicate.operators;
public class IsEmptyOperator extends org.apache.ambari.server.api.predicate.operators.AbstractOperator implements org.apache.ambari.server.api.predicate.operators.RelationalOperator {
    public IsEmptyOperator() {
        super(0);
    }

    @java.lang.Override
    public java.lang.String getName() {
        return "IsEmptyOperator";
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.Predicate toPredicate(java.lang.String prop, java.lang.String val) throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        if (val != null) {
            throw new org.apache.ambari.server.api.predicate.InvalidQueryException("'isEmpty' operator shouldn't have a right operand but one exists: " + val);
        }
        return new org.apache.ambari.server.controller.predicate.CategoryIsEmptyPredicate(prop);
    }

    @java.lang.Override
    public org.apache.ambari.server.api.predicate.operators.Operator.TYPE getType() {
        return org.apache.ambari.server.api.predicate.operators.Operator.TYPE.IS_EMPTY;
    }
}