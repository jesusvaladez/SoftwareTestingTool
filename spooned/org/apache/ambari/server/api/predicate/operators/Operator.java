package org.apache.ambari.server.api.predicate.operators;
public interface Operator {
    enum TYPE {

        LESS,
        LESS_EQUAL,
        GREATER,
        GREATER_EQUAL,
        EQUAL,
        NOT_EQUAL,
        AND,
        OR,
        NOT,
        IN,
        IS_EMPTY,
        FILTER;}

    int MAX_OP_PRECEDENCE = 3;

    org.apache.ambari.server.api.predicate.operators.Operator.TYPE getType();

    int getPrecedence();
}