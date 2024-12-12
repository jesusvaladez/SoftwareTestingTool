package org.apache.ambari.server.events.publishers;
@com.google.inject.Singleton
public class TaskEventPublisher {
    private final com.google.common.eventbus.EventBus m_eventBus = new com.google.common.eventbus.EventBus("ambari-task-report-event-bus");

    public void publish(org.apache.ambari.server.events.TaskEvent event) {
        m_eventBus.post(event);
    }

    public void register(java.lang.Object object) {
        m_eventBus.register(object);
    }
}