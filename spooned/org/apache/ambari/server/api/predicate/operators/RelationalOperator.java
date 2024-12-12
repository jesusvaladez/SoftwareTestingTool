package org.apache.ambari.server.api.predicate.operators;
public interface RelationalOperator extends org.apache.ambari.server.api.predicate.operators.Operator {
    org.apache.ambari.server.controller.spi.Predicate toPredicate(java.lang.String prop, java.lang.String val) throws org.apache.ambari.server.api.predicate.InvalidQueryException;
}