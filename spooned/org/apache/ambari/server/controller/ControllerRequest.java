package org.apache.ambari.server.controller;
public class ControllerRequest {
    private final java.lang.String name;

    private final java.util.Map<java.lang.String, java.lang.Object> propertyMap;

    public ControllerRequest(java.lang.String name, java.util.Map<java.lang.String, java.lang.Object> propertyMap) {
        this.name = name;
        this.propertyMap = propertyMap;
    }

    public java.lang.String getName() {
        return name;
    }

    public java.util.Map<java.lang.String, java.lang.Object> getPropertyMap() {
        return propertyMap;
    }
}