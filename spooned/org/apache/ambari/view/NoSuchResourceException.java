package org.apache.ambari.view;
public class NoSuchResourceException extends java.lang.Exception {
    private final java.lang.String resourceId;

    public NoSuchResourceException(java.lang.String resourceId) {
        super(("The resource " + resourceId) + " specified in the request does not exist.");
        this.resourceId = resourceId;
    }

    public java.lang.String getResourceId() {
        return resourceId;
    }
}