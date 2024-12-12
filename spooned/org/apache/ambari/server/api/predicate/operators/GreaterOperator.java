package org.apache.ambari.server.api.predicate.operators;
public class GreaterOperator extends org.apache.ambari.server.api.predicate.operators.AbstractOperator implements org.apache.ambari.server.api.predicate.operators.RelationalOperator {
    public GreaterOperator() {
        super(0);
    }

    @java.lang.Override
    public org.apache.ambari.server.api.predicate.operators.Operator.TYPE getType() {
        return org.apache.ambari.server.api.predicate.operators.Operator.TYPE.GREATER;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.Predicate toPredicate(java.lang.String prop, java.lang.String val) {
        return new org.apache.ambari.server.controller.predicate.GreaterPredicate<>(prop, val);
    }

    @java.lang.Override
    public java.lang.String getName() {
        return "GreaterOperator";
    }
}