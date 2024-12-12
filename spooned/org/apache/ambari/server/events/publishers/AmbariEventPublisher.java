package org.apache.ambari.server.events.publishers;
@com.google.inject.Singleton
public class AmbariEventPublisher {
    private final com.google.common.eventbus.EventBus m_eventBus;

    public AmbariEventPublisher() {
        m_eventBus = new com.google.common.eventbus.AsyncEventBus("ambari-event-bus", java.util.concurrent.Executors.newSingleThreadExecutor());
    }

    public void publish(org.apache.ambari.server.events.AmbariEvent event) {
        m_eventBus.post(event);
    }

    public void register(java.lang.Object object) {
        m_eventBus.register(object);
    }
}