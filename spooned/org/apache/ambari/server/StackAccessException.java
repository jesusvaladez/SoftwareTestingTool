package org.apache.ambari.server;
@java.lang.SuppressWarnings("serial")
public class StackAccessException extends org.apache.ambari.server.ObjectNotFoundException {
    public StackAccessException(java.lang.String message) {
        super("Stack data, " + message);
    }
}