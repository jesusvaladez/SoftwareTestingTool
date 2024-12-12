package org.apache.ambari.server.events;
import com.google.inject.persist.PersistService;
public class JpaInitializedEvent extends org.apache.ambari.server.events.AmbariEvent {
    public JpaInitializedEvent() {
        super(org.apache.ambari.server.events.AmbariEvent.AmbariEventType.JPA_INITIALIZED);
    }
}