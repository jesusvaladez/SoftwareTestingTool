package org.apache.ambari.view;
public class UnsupportedPropertyException extends java.lang.Exception {
    private final java.lang.String type;

    private final java.util.Set<java.lang.String> propertyIds;

    public UnsupportedPropertyException(java.lang.String type, java.util.Set<java.lang.String> propertyIds) {
        super(((("The properties " + propertyIds) + " specified in the request or predicate are not supported for the resource type ") + type) + ".");
        this.type = type;
        this.propertyIds = propertyIds;
    }

    public java.lang.String getType() {
        return type;
    }

    public java.util.Set<java.lang.String> getPropertyIds() {
        return propertyIds;
    }
}