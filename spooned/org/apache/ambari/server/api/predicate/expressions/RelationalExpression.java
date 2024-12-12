package org.apache.ambari.server.api.predicate.expressions;
public class RelationalExpression extends org.apache.ambari.server.api.predicate.expressions.AbstractExpression<java.lang.String> {
    public RelationalExpression(org.apache.ambari.server.api.predicate.operators.RelationalOperator op) {
        super(op);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.Predicate toPredicate() throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        return ((org.apache.ambari.server.api.predicate.operators.RelationalOperator) (getOperator())).toPredicate(getLeftOperand(), getRightOperand());
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((("RelationalExpression{ property='" + getLeftOperand()) + "\', value=\'") + getRightOperand()) + "\', op=") + getOperator()) + " }";
    }
}