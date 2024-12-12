package org.apache.ambari.server.orm;
import com.google.inject.persist.PersistService;
@com.google.inject.Singleton
public class GuiceJpaInitializer {
    private final org.apache.ambari.server.events.publishers.AmbariEventPublisher publisher;

    @com.google.inject.Inject
    public GuiceJpaInitializer(com.google.inject.persist.PersistService service, org.apache.ambari.server.events.publishers.AmbariEventPublisher publisher) {
        this.publisher = publisher;
        service.start();
    }

    public void setInitialized() {
        publisher.publish(new org.apache.ambari.server.events.JpaInitializedEvent());
    }
}