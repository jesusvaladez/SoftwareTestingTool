package org.apache.ambari.server.events;
public class AmbariConfigurationChangedEvent extends org.apache.ambari.server.events.AmbariEvent {
    private final java.lang.String categoryName;

    public AmbariConfigurationChangedEvent(java.lang.String categoryName) {
        super(org.apache.ambari.server.events.AmbariEvent.AmbariEventType.AMBARI_CONFIGURATION_CHANGED);
        this.categoryName = categoryName;
    }

    public java.lang.String getCategoryName() {
        return categoryName;
    }
}