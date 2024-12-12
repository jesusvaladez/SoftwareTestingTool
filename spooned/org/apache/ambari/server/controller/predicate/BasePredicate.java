package org.apache.ambari.server.controller.predicate;
public interface BasePredicate extends org.apache.ambari.server.controller.spi.Predicate , org.apache.ambari.server.controller.predicate.PredicateVisitorAcceptor {
    java.util.Set<java.lang.String> getPropertyIds();
}