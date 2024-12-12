package org.apache.ambari.server.events.publishers;
@org.apache.ambari.server.EagerSingleton
public class HostComponentUpdateEventPublisher extends org.apache.ambari.server.events.publishers.BufferedUpdateEventPublisher<org.apache.ambari.server.events.HostComponentsUpdateEvent> {
    @com.google.inject.Inject
    public HostComponentUpdateEventPublisher(org.apache.ambari.server.events.publishers.STOMPUpdatePublisher stompUpdatePublisher) {
        super(stompUpdatePublisher);
    }

    @java.lang.Override
    public org.apache.ambari.server.events.STOMPEvent.Type getType() {
        return org.apache.ambari.server.events.STOMPEvent.Type.HOSTCOMPONENT;
    }

    @java.lang.Override
    public void mergeBufferAndPost(java.util.List<org.apache.ambari.server.events.HostComponentsUpdateEvent> events, com.google.common.eventbus.EventBus m_eventBus) {
        java.util.List<org.apache.ambari.server.events.HostComponentUpdate> hostComponentUpdates = events.stream().flatMap(u -> u.getHostComponentUpdates().stream()).collect(java.util.stream.Collectors.toList());
        org.apache.ambari.server.events.HostComponentsUpdateEvent resultEvents = new org.apache.ambari.server.events.HostComponentsUpdateEvent(hostComponentUpdates);
        m_eventBus.post(resultEvents);
    }
}