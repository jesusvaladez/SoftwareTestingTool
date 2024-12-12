package org.apache.ambari.server.api.predicate.expressions;
public abstract class AbstractExpression<T> implements org.apache.ambari.server.api.predicate.expressions.Expression<T> {
    private final org.apache.ambari.server.api.predicate.operators.Operator m_op;

    private T m_left = null;

    private T m_right = null;

    protected AbstractExpression(org.apache.ambari.server.api.predicate.operators.Operator op) {
        m_op = op;
    }

    @java.lang.Override
    public void setLeftOperand(T left) {
        m_left = left;
    }

    @java.lang.Override
    public void setRightOperand(T right) {
        m_right = right;
    }

    @java.lang.Override
    public T getLeftOperand() {
        return m_left;
    }

    @java.lang.Override
    public T getRightOperand() {
        return m_right;
    }

    @java.lang.Override
    public org.apache.ambari.server.api.predicate.operators.Operator getOperator() {
        return m_op;
    }

    @java.lang.Override
    public java.util.List<org.apache.ambari.server.api.predicate.expressions.Expression> merge(org.apache.ambari.server.api.predicate.expressions.Expression left, org.apache.ambari.server.api.predicate.expressions.Expression right, int precedence) {
        return defaultMerge(left, right);
    }

    protected java.util.List<org.apache.ambari.server.api.predicate.expressions.Expression> defaultMerge(org.apache.ambari.server.api.predicate.expressions.Expression left, org.apache.ambari.server.api.predicate.expressions.Expression right) {
        java.util.List<org.apache.ambari.server.api.predicate.expressions.Expression> listExpressions = new java.util.ArrayList<>();
        if (left != null) {
            listExpressions.add(left);
        }
        listExpressions.add(this);
        if (right != null) {
            listExpressions.add(right);
        }
        return listExpressions;
    }
}