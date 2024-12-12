package org.apache.ambari.server.view;
public class IllegalClusterException extends java.lang.RuntimeException {
    public IllegalClusterException(java.lang.Throwable ex) {
        this("Failed to get cluster information associated with this view instance", ex);
    }

    public IllegalClusterException(java.lang.String message, java.lang.Throwable cause) {
        super(message, cause);
    }
}