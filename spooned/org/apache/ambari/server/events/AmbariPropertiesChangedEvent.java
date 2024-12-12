package org.apache.ambari.server.events;
public class AmbariPropertiesChangedEvent extends org.apache.ambari.server.events.AmbariEvent {
    public AmbariPropertiesChangedEvent() {
        super(org.apache.ambari.server.events.AmbariEvent.AmbariEventType.AMBARI_CONFIGURATION_CHANGED);
    }
}