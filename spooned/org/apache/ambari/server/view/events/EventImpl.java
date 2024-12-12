package org.apache.ambari.server.view.events;
public class EventImpl implements org.apache.ambari.view.events.Event {
    private final java.lang.String id;

    private final java.util.Map<java.lang.String, java.lang.String> properties;

    private final org.apache.ambari.view.ViewDefinition viewSubject;

    private final org.apache.ambari.view.ViewInstanceDefinition viewInstanceSubject;

    public EventImpl(java.lang.String id, java.util.Map<java.lang.String, java.lang.String> properties, org.apache.ambari.view.ViewDefinition viewSubject) {
        this(id, properties, viewSubject, null);
    }

    public EventImpl(java.lang.String id, java.util.Map<java.lang.String, java.lang.String> properties, org.apache.ambari.view.ViewInstanceDefinition viewInstanceSubject) {
        this(id, properties, viewInstanceSubject.getViewDefinition(), viewInstanceSubject);
    }

    private EventImpl(java.lang.String id, java.util.Map<java.lang.String, java.lang.String> properties, org.apache.ambari.view.ViewDefinition viewSubject, org.apache.ambari.view.ViewInstanceDefinition viewInstanceSubject) {
        this.id = id;
        this.viewSubject = viewSubject;
        this.viewInstanceSubject = viewInstanceSubject;
        this.properties = (properties == null) ? java.util.Collections.emptyMap() : java.util.Collections.unmodifiableMap(properties);
    }

    @java.lang.Override
    public java.lang.String getId() {
        return id;
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, java.lang.String> getProperties() {
        return properties;
    }

    @java.lang.Override
    public org.apache.ambari.view.ViewDefinition getViewSubject() {
        return viewSubject;
    }

    @java.lang.Override
    public org.apache.ambari.view.ViewInstanceDefinition getViewInstanceSubject() {
        return viewInstanceSubject;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "View Event " + id;
    }
}