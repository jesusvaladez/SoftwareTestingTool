package org.apache.ambari.server.api.predicate.operators;
public interface LogicalOperator extends org.apache.ambari.server.api.predicate.operators.Operator {
    org.apache.ambari.server.controller.spi.Predicate toPredicate(org.apache.ambari.server.controller.spi.Predicate left, org.apache.ambari.server.controller.spi.Predicate right);
}