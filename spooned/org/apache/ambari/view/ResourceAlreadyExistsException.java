package org.apache.ambari.view;
public class ResourceAlreadyExistsException extends java.lang.Exception {
    private final java.lang.String resourceId;

    public ResourceAlreadyExistsException(java.lang.String resourceId) {
        super(("The resource " + resourceId) + " specified in the request already exists.");
        this.resourceId = resourceId;
    }

    public java.lang.String getResourceId() {
        return resourceId;
    }
}