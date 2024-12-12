package org.apache.ambari.server.controller.predicate;
public abstract class PropertyPredicate implements org.apache.ambari.server.controller.predicate.BasePredicate {
    private final java.lang.String propertyId;

    public PropertyPredicate(java.lang.String propertyId) {
        assert propertyId != null;
        this.propertyId = propertyId;
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> getPropertyIds() {
        return java.util.Collections.singleton(propertyId);
    }

    public java.lang.String getPropertyId() {
        return propertyId;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof org.apache.ambari.server.controller.predicate.PropertyPredicate)) {
            return false;
        }
        org.apache.ambari.server.controller.predicate.PropertyPredicate that = ((org.apache.ambari.server.controller.predicate.PropertyPredicate) (o));
        return propertyId == null ? that.propertyId == null : propertyId.equals(that.propertyId);
    }

    @java.lang.Override
    public int hashCode() {
        return propertyId != null ? propertyId.hashCode() : 0;
    }
}