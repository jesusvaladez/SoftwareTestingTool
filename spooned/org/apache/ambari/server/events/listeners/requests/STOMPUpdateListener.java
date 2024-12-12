package org.apache.ambari.server.events.listeners.requests;
public class STOMPUpdateListener {
    @org.springframework.beans.factory.annotation.Autowired
    private org.apache.ambari.server.events.DefaultMessageEmitter defaultMessageEmitter;

    private final java.util.Set<org.apache.ambari.server.events.STOMPEvent.Type> typesToProcess;

    public STOMPUpdateListener(com.google.inject.Injector injector, java.util.Set<org.apache.ambari.server.events.STOMPEvent.Type> typesToProcess) {
        org.apache.ambari.server.events.publishers.STOMPUpdatePublisher STOMPUpdatePublisher = injector.getInstance(org.apache.ambari.server.events.publishers.STOMPUpdatePublisher.class);
        STOMPUpdatePublisher.registerAgent(this);
        STOMPUpdatePublisher.registerAPI(this);
        this.typesToProcess = (typesToProcess == null) ? java.util.Collections.emptySet() : typesToProcess;
    }

    @com.google.common.eventbus.Subscribe
    @com.google.common.eventbus.AllowConcurrentEvents
    public void onUpdateEvent(org.apache.ambari.server.events.STOMPEvent event) throws org.apache.ambari.server.AmbariException, java.lang.InterruptedException {
        if (typesToProcess.contains(event.getType())) {
            defaultMessageEmitter.emitMessage(event);
        }
    }
}