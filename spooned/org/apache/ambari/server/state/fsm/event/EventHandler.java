package org.apache.ambari.server.state.fsm.event;
public interface EventHandler<T extends org.apache.ambari.server.state.fsm.event.Event<?>> {
    void handle(T event);
}