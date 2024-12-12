package org.apache.ambari.server.controller.predicate;
public class NotPredicate extends org.apache.ambari.server.controller.predicate.UnaryPredicate {
    public NotPredicate(org.apache.ambari.server.controller.spi.Predicate predicate) {
        super(predicate);
    }

    @java.lang.Override
    public boolean evaluate(org.apache.ambari.server.controller.spi.Resource resource) {
        return !getPredicate().evaluate(resource);
    }

    @java.lang.Override
    public java.lang.String getOperator() {
        return "NOT";
    }
}