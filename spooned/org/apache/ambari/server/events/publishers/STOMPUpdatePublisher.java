package org.apache.ambari.server.events.publishers;
@com.google.inject.Singleton
@java.lang.SuppressWarnings({ "UnstableApiUsage", "rawtypes" })
public class STOMPUpdatePublisher {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.events.publishers.STOMPUpdatePublisher.class);

    private final com.google.common.eventbus.EventBus agentEventBus;

    private final com.google.common.eventbus.EventBus apiEventBus;

    private final java.util.List<org.apache.ambari.server.events.publishers.BufferedUpdateEventPublisher> publishers = new java.util.ArrayList<>();

    public STOMPUpdatePublisher() {
        agentEventBus = new com.google.common.eventbus.AsyncEventBus("agent-update-bus", org.apache.ambari.server.utils.ThreadPools.getSingleThreadedExecutor("stomp-agent-bus"));
        apiEventBus = new com.google.common.eventbus.AsyncEventBus("api-update-bus", org.apache.ambari.server.utils.ThreadPools.getSingleThreadedExecutor("stomp-api-bus"));
    }

    public void registerPublisher(org.apache.ambari.server.events.publishers.BufferedUpdateEventPublisher publisher) {
        if (publishers.contains(publisher)) {
            org.apache.ambari.server.events.publishers.STOMPUpdatePublisher.LOG.error("Publisher for type {} is already in use", publisher.getType());
        } else {
            publishers.add(publisher);
        }
    }

    public void publish(org.apache.ambari.server.events.STOMPEvent event) {
        if (org.apache.ambari.server.events.DefaultMessageEmitter.DEFAULT_AGENT_EVENT_TYPES.contains(event.getType())) {
            publishAgent(event);
        } else if (org.apache.ambari.server.events.DefaultMessageEmitter.DEFAULT_API_EVENT_TYPES.contains(event.getType())) {
            publishAPI(event);
        } else {
            throw new org.apache.ambari.server.AmbariRuntimeException(("Event with type {" + event.getType()) + "} can not be published.");
        }
    }

    @java.lang.SuppressWarnings("unchecked")
    private void publishAPI(org.apache.ambari.server.events.STOMPEvent event) {
        boolean published = false;
        for (org.apache.ambari.server.events.publishers.BufferedUpdateEventPublisher publisher : publishers) {
            if (publisher.getType().equals(event.getType())) {
                publisher.publish(event, apiEventBus);
                published = true;
            }
        }
        if (!published) {
            apiEventBus.post(event);
        }
    }

    private void publishAgent(org.apache.ambari.server.events.STOMPEvent event) {
        agentEventBus.post(event);
    }

    public void registerAgent(java.lang.Object object) {
        agentEventBus.register(object);
    }

    public void registerAPI(java.lang.Object object) {
        apiEventBus.register(object);
    }
}