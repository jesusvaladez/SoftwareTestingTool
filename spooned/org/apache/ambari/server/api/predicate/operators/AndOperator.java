package org.apache.ambari.server.api.predicate.operators;
public class AndOperator extends org.apache.ambari.server.api.predicate.operators.AbstractOperator implements org.apache.ambari.server.api.predicate.operators.LogicalOperator {
    public AndOperator(int ctxPrecedence) {
        super(ctxPrecedence);
    }

    @java.lang.Override
    public org.apache.ambari.server.api.predicate.operators.Operator.TYPE getType() {
        return org.apache.ambari.server.api.predicate.operators.Operator.TYPE.AND;
    }

    @java.lang.Override
    public java.lang.String getName() {
        return "AndOperator";
    }

    @java.lang.Override
    public int getBasePrecedence() {
        return 2;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.Predicate toPredicate(org.apache.ambari.server.controller.spi.Predicate left, org.apache.ambari.server.controller.spi.Predicate right) {
        return new org.apache.ambari.server.controller.predicate.AndPredicate(left, right);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((getName() + "[precedence=") + getPrecedence()) + "]";
    }
}