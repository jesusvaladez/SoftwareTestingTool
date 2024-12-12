package org.apache.ambari.server.controller.predicate;
public class EqualsPredicate<T> extends org.apache.ambari.server.controller.predicate.ComparisonPredicate<T> {
    public EqualsPredicate(java.lang.String propertyId, java.lang.Comparable<T> value) {
        super(propertyId, value);
    }

    @java.lang.Override
    public boolean evaluate(org.apache.ambari.server.controller.spi.Resource resource) {
        java.lang.Object propertyValue = resource.getPropertyValue(getPropertyId());
        java.lang.Object predicateValue = getValue();
        return predicateValue == null ? propertyValue == null : (propertyValue != null) && (compareValueTo(propertyValue) == 0);
    }

    public boolean evaluateIgnoreCase(org.apache.ambari.server.controller.spi.Resource resource) {
        java.lang.Object propertyValue = resource.getPropertyValue(getPropertyId());
        java.lang.Object predicateValue = getValue();
        return predicateValue == null ? propertyValue == null : (propertyValue != null) && (compareValueToIgnoreCase(propertyValue) == 0);
    }

    @java.lang.Override
    public java.lang.String getOperator() {
        return "=";
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.predicate.ComparisonPredicate<T> copy(java.lang.String propertyId) {
        return new org.apache.ambari.server.controller.predicate.EqualsPredicate<>(propertyId, getValue());
    }
}