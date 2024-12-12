package org.apache.ambari.server.api.predicate.expressions;
public class NotLogicalExpression extends org.apache.ambari.server.api.predicate.expressions.LogicalExpression {
    public NotLogicalExpression(org.apache.ambari.server.api.predicate.operators.LogicalOperator op) {
        super(op);
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.api.predicate.expressions.Expression> merge(org.apache.ambari.server.api.predicate.expressions.Expression left, org.apache.ambari.server.api.predicate.expressions.Expression right, int precedence) {
        if ((getOperator().getPrecedence() == precedence) && (getRightOperand() == null)) {
            java.util.List<org.apache.ambari.server.api.predicate.expressions.Expression> listExpressions = new java.util.ArrayList<>();
            if (left != null) {
                listExpressions.add(left);
            }
            setRightOperand(right);
            listExpressions.add(this);
            return listExpressions;
        } else {
            return defaultMerge(left, right);
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.spi.Predicate toPredicate() throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        return new org.apache.ambari.server.controller.predicate.NotPredicate(getRightOperand().toPredicate());
    }
}