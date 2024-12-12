package org.apache.ambari.server.api.predicate.expressions;
public class LogicalExpressionFactory {
    public static org.apache.ambari.server.api.predicate.expressions.LogicalExpression createLogicalExpression(org.apache.ambari.server.api.predicate.operators.LogicalOperator op) throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        switch (op.getType()) {
            case AND :
            case OR :
                return new org.apache.ambari.server.api.predicate.expressions.LogicalExpression(op);
            case NOT :
                return new org.apache.ambari.server.api.predicate.expressions.NotLogicalExpression(op);
            default :
                throw new java.lang.RuntimeException("An invalid logical operator type was encountered: " + op);
        }
    }
}