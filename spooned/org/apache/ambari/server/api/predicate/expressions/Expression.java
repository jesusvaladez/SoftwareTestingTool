package org.apache.ambari.server.api.predicate.expressions;
public interface Expression<T> {
    java.util.List<org.apache.ambari.server.api.predicate.expressions.Expression> merge(org.apache.ambari.server.api.predicate.expressions.Expression left, org.apache.ambari.server.api.predicate.expressions.Expression right, int precedence);

    org.apache.ambari.server.controller.spi.Predicate toPredicate() throws org.apache.ambari.server.api.predicate.InvalidQueryException;

    void setLeftOperand(T left);

    void setRightOperand(T right);

    T getLeftOperand();

    T getRightOperand();

    org.apache.ambari.server.api.predicate.operators.Operator getOperator();
}