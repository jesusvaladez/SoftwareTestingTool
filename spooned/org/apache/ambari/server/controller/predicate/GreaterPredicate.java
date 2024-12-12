package org.apache.ambari.server.controller.predicate;
public class GreaterPredicate<T> extends org.apache.ambari.server.controller.predicate.ComparisonPredicate<T> {
    public GreaterPredicate(java.lang.String propertyId, java.lang.Comparable<T> value) {
        super(propertyId, value);
        if (value == null) {
            throw new java.lang.IllegalArgumentException("Value can't be null.");
        }
    }

    @java.lang.Override
    public boolean evaluate(org.apache.ambari.server.controller.spi.Resource resource) {
        java.lang.Object propertyValue = resource.getPropertyValue(getPropertyId());
        return (propertyValue != null) && (compareValueTo(propertyValue) < 0);
    }

    @java.lang.Override
    public java.lang.String getOperator() {
        return ">";
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.predicate.ComparisonPredicate<T> copy(java.lang.String propertyId) {
        return new org.apache.ambari.server.controller.predicate.GreaterPredicate<>(propertyId, getValue());
    }
}