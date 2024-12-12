package org.apache.ambari.server;
@java.lang.SuppressWarnings("serial")
public class MessageDestinationIsNotDefinedException extends org.apache.ambari.server.ObjectNotFoundException {
    public MessageDestinationIsNotDefinedException(org.apache.ambari.server.events.STOMPEvent.Type eventType) {
        super(java.lang.String.format("No destination defined for message with %s type", eventType));
    }
}