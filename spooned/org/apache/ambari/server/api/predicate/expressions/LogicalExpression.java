package org.apache.ambari.server.api.predicate.expressions;
public class LogicalExpression extends org.apache.ambari.server.api.predicate.expressions.AbstractExpression<org.apache.ambari.server.api.predicate.expressions.Expression> {
    public LogicalExpression(org.apache.ambari.server.api.predicate.operators.LogicalOperator op) {
        super(op);
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.Predicate toPredicate() throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        return ((org.apache.ambari.server.api.predicate.operators.LogicalOperator) (getOperator())).toPredicate(getLeftOperand().toPredicate(), getRightOperand().toPredicate());
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.api.predicate.expressions.Expression> merge(org.apache.ambari.server.api.predicate.expressions.Expression left, org.apache.ambari.server.api.predicate.expressions.Expression right, int precedence) {
        if ((getOperator().getPrecedence() == precedence) && (getLeftOperand() == null)) {
            setLeftOperand(left);
            setRightOperand(right);
            return java.util.Collections.singletonList(this);
        } else {
            return defaultMerge(left, right);
        }
    }
}