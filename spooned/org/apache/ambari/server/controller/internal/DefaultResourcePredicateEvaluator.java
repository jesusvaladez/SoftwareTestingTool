package org.apache.ambari.server.controller.internal;
public class DefaultResourcePredicateEvaluator implements org.apache.ambari.server.controller.spi.ResourcePredicateEvaluator {
    @java.lang.Override
    public boolean evaluate(org.apache.ambari.server.controller.spi.Predicate predicate, org.apache.ambari.server.controller.spi.Resource resource) {
        return predicate.evaluate(resource);
    }
}