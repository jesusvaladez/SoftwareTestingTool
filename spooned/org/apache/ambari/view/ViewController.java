package org.apache.ambari.view;
public interface ViewController {
    public void fireEvent(java.lang.String eventId, java.util.Map<java.lang.String, java.lang.String> eventProperties);

    public void registerListener(org.apache.ambari.view.events.Listener listener, java.lang.String viewName);

    public void registerListener(org.apache.ambari.view.events.Listener listener, java.lang.String viewName, java.lang.String viewVersion);

    public void unregisterListener(org.apache.ambari.view.events.Listener listener, java.lang.String viewName);

    public void unregisterListener(org.apache.ambari.view.events.Listener listener, java.lang.String viewName, java.lang.String viewVersion);
}