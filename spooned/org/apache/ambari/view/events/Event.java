package org.apache.ambari.view.events;
public interface Event {
    public java.lang.String getId();

    public java.util.Map<java.lang.String, java.lang.String> getProperties();

    public org.apache.ambari.view.ViewDefinition getViewSubject();

    public org.apache.ambari.view.ViewInstanceDefinition getViewInstanceSubject();
}