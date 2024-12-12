package org.apache.ambari.server.api.predicate.operators;
public class FilterOperator extends org.apache.ambari.server.api.predicate.operators.AbstractOperator implements org.apache.ambari.server.api.predicate.operators.RelationalOperator {
    public FilterOperator() {
        super(0);
    }

    @java.lang.Override
    public java.lang.String getName() {
        return "FilterOperator";
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.Predicate toPredicate(java.lang.String prop, java.lang.String val) throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        if (val == null) {
            throw new org.apache.ambari.server.api.predicate.InvalidQueryException("Filter operator is missing a required right operand.");
        }
        return new org.apache.ambari.server.controller.predicate.FilterPredicate(prop, val);
    }

    @java.lang.Override
    public org.apache.ambari.server.api.predicate.operators.Operator.TYPE getType() {
        return org.apache.ambari.server.api.predicate.operators.Operator.TYPE.FILTER;
    }
}