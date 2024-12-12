package org.apache.ambari.server.events.publishers;
@com.google.inject.Singleton
public class VersionEventPublisher {
    private final com.google.common.eventbus.EventBus m_eventBus;

    public VersionEventPublisher() {
        m_eventBus = new com.google.common.eventbus.EventBus("version-event-bus");
    }

    public void publish(org.apache.ambari.server.events.ClusterEvent event) {
        m_eventBus.post(event);
    }

    public void register(java.lang.Object object) {
        m_eventBus.register(object);
    }
}