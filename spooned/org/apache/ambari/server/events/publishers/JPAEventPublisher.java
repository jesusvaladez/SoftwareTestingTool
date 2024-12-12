package org.apache.ambari.server.events.publishers;
@com.google.inject.Singleton
public class JPAEventPublisher {
    private final com.google.common.eventbus.EventBus m_eventBus = new com.google.common.eventbus.EventBus("ambari-jpa-event-bus");

    public void publish(org.apache.ambari.server.events.jpa.JPAEvent event) {
        m_eventBus.post(event);
    }

    public void register(java.lang.Object object) {
        m_eventBus.register(object);
    }
}