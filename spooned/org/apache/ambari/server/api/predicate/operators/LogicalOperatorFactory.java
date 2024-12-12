package org.apache.ambari.server.api.predicate.operators;
public class LogicalOperatorFactory {
    public static org.apache.ambari.server.api.predicate.operators.LogicalOperator createOperator(java.lang.String operator, int ctxPrecedence) throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        if ("&".equals(operator)) {
            return new org.apache.ambari.server.api.predicate.operators.AndOperator(ctxPrecedence);
        } else if ("|".equals(operator)) {
            return new org.apache.ambari.server.api.predicate.operators.OrOperator(ctxPrecedence);
        } else if ("!".equals(operator)) {
            return new org.apache.ambari.server.api.predicate.operators.NotOperator(ctxPrecedence);
        } else {
            throw new java.lang.RuntimeException("Invalid Logical Operator Type: " + operator);
        }
    }
}