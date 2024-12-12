package org.apache.ambari.server.api.predicate.operators;
public abstract class AbstractOperator implements org.apache.ambari.server.api.predicate.operators.Operator {
    private final int m_ctxPrecedence;

    protected AbstractOperator(int ctxPrecedence) {
        m_ctxPrecedence = ctxPrecedence;
    }

    public int getBasePrecedence() {
        return -1;
    }

    @java.lang.Override
    public int getPrecedence() {
        return getBasePrecedence() + m_ctxPrecedence;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return getName();
    }

    public abstract java.lang.String getName();
}