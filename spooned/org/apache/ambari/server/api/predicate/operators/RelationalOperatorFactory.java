package org.apache.ambari.server.api.predicate.operators;
public class RelationalOperatorFactory {
    public static org.apache.ambari.server.api.predicate.operators.RelationalOperator createOperator(java.lang.String operator) throws org.apache.ambari.server.api.predicate.InvalidQueryException {
        if ("!=".equals(operator)) {
            return new org.apache.ambari.server.api.predicate.operators.NotEqualsOperator();
        } else if ("=".equals(operator)) {
            return new org.apache.ambari.server.api.predicate.operators.EqualsOperator();
        } else if ("<=".equals(operator)) {
            return new org.apache.ambari.server.api.predicate.operators.LessEqualsOperator();
        } else if ("<".equals(operator)) {
            return new org.apache.ambari.server.api.predicate.operators.LessOperator();
        } else if (">=".equals(operator)) {
            return new org.apache.ambari.server.api.predicate.operators.GreaterEqualsOperator();
        } else if (">".equals(operator)) {
            return new org.apache.ambari.server.api.predicate.operators.GreaterOperator();
        } else if (".in(".equals(operator)) {
            return new org.apache.ambari.server.api.predicate.operators.InOperator();
        } else if (".isEmpty(".equals(operator)) {
            return new org.apache.ambari.server.api.predicate.operators.IsEmptyOperator();
        } else if (".matches(".equals(operator)) {
            return new org.apache.ambari.server.api.predicate.operators.FilterOperator();
        } else {
            throw new java.lang.RuntimeException("Invalid Operator Type: " + operator);
        }
    }
}