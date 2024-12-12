package org.apache.ambari.server.controller.spi;
public class UnsupportedPropertyException extends java.lang.Exception {
    private final org.apache.ambari.server.controller.spi.Resource.Type type;

    private final java.util.Set<java.lang.String> propertyIds;

    public UnsupportedPropertyException(org.apache.ambari.server.controller.spi.Resource.Type type, java.util.Set<java.lang.String> propertyIds) {
        super(((("The properties " + propertyIds) + " specified in the request or predicate are not supported for the resource type ") + type) + ".");
        this.type = type;
        this.propertyIds = propertyIds;
    }

    public org.apache.ambari.server.controller.spi.Resource.Type getType() {
        return type;
    }

    public java.util.Set<java.lang.String> getPropertyIds() {
        return propertyIds;
    }
}