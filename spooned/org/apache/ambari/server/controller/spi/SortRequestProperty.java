package org.apache.ambari.server.controller.spi;
public class SortRequestProperty {
    private final java.lang.String propertyId;

    private final org.apache.ambari.server.controller.spi.SortRequest.Order order;

    public SortRequestProperty(java.lang.String propertyId, org.apache.ambari.server.controller.spi.SortRequest.Order order) {
        this.propertyId = propertyId;
        if (order == null) {
            this.order = org.apache.ambari.server.controller.spi.SortRequest.Order.ASC;
        } else {
            this.order = order;
        }
    }

    public java.lang.String getPropertyId() {
        return propertyId;
    }

    public org.apache.ambari.server.controller.spi.SortRequest.Order getOrder() {
        return order;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((("SortRequestProperty{" + "propertyId='") + propertyId) + '\'') + ", order=") + order) + '}';
    }
}