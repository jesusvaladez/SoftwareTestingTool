package org.apache.ambari.server.controller.spi;
public interface ResourcePredicateEvaluator {
    boolean evaluate(org.apache.ambari.server.controller.spi.Predicate predicate, org.apache.ambari.server.controller.spi.Resource resource);
}