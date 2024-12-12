package org.apache.ambari.server.controller.predicate;
public class CategoryIsEmptyPredicate extends org.apache.ambari.server.controller.predicate.CategoryPredicate {
    public CategoryIsEmptyPredicate(java.lang.String propertyId) {
        super(propertyId);
    }

    @java.lang.Override
    public boolean evaluate(org.apache.ambari.server.controller.spi.Resource resource) {
        java.lang.String propertyId = getPropertyId();
        java.lang.Object value = resource.getPropertyValue(propertyId);
        if (value instanceof java.util.Map) {
            java.util.Map<?, ?> mapValue = ((java.util.Map) (value));
            return mapValue.isEmpty();
        }
        java.util.Map<java.lang.String, java.lang.Object> properties = resource.getPropertiesMap().get(propertyId);
        return properties == null ? true : properties.isEmpty();
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ("isEmpty(" + getPropertyId()) + ")";
    }
}