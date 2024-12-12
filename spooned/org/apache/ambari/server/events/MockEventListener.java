package org.apache.ambari.server.events;
@com.google.inject.Singleton
public class MockEventListener {
    private final java.util.Map<java.lang.Class<?>, java.util.List<org.apache.ambari.server.events.AmbariEvent>> m_receivedAmbariEvents = new java.util.HashMap<>();

    private final java.util.Map<java.lang.Class<?>, java.util.List<org.apache.ambari.server.events.AlertEvent>> m_receivedAlertEvents = new java.util.HashMap<>();

    public void reset() {
        m_receivedAmbariEvents.clear();
        m_receivedAlertEvents.clear();
    }

    public boolean isAmbariEventReceived(java.lang.Class<? extends org.apache.ambari.server.events.AmbariEvent> clazz) {
        if (!m_receivedAmbariEvents.containsKey(clazz)) {
            return false;
        }
        return m_receivedAmbariEvents.get(clazz).size() > 0;
    }

    public boolean isAlertEventReceived(java.lang.Class<? extends org.apache.ambari.server.events.AlertEvent> clazz) {
        if (!m_receivedAlertEvents.containsKey(clazz)) {
            return false;
        }
        return m_receivedAlertEvents.get(clazz).size() > 0;
    }

    public int getAmbariEventReceivedCount(java.lang.Class<? extends org.apache.ambari.server.events.AmbariEvent> clazz) {
        if (!m_receivedAmbariEvents.containsKey(clazz)) {
            return 0;
        }
        return m_receivedAmbariEvents.get(clazz).size();
    }

    public int getAlertEventReceivedCount(java.lang.Class<? extends org.apache.ambari.server.events.AlertEvent> clazz) {
        if (!m_receivedAlertEvents.containsKey(clazz)) {
            return 0;
        }
        return m_receivedAlertEvents.get(clazz).size();
    }

    public java.util.List<org.apache.ambari.server.events.AmbariEvent> getAmbariEventInstances(java.lang.Class<? extends org.apache.ambari.server.events.AmbariEvent> clazz) {
        if (!m_receivedAmbariEvents.containsKey(clazz)) {
            return java.util.Collections.emptyList();
        }
        return m_receivedAmbariEvents.get(clazz);
    }

    public java.util.List<org.apache.ambari.server.events.AlertEvent> getAlertEventInstances(java.lang.Class<? extends org.apache.ambari.server.events.AlertEvent> clazz) {
        if (!m_receivedAlertEvents.containsKey(clazz)) {
            return java.util.Collections.emptyList();
        }
        return m_receivedAlertEvents.get(clazz);
    }

    @com.google.common.eventbus.Subscribe
    public void onAmbariEvent(org.apache.ambari.server.events.AmbariEvent event) {
        java.util.List<org.apache.ambari.server.events.AmbariEvent> events = m_receivedAmbariEvents.get(event.getClass());
        if (null == events) {
            events = new java.util.ArrayList<>();
            m_receivedAmbariEvents.put(event.getClass(), events);
        }
        events.add(event);
    }

    @com.google.common.eventbus.Subscribe
    public void onAlertEvent(org.apache.ambari.server.events.AlertEvent event) {
        java.util.List<org.apache.ambari.server.events.AlertEvent> events = m_receivedAlertEvents.get(event.getClass());
        if (null == events) {
            events = new java.util.ArrayList<>();
            m_receivedAlertEvents.put(event.getClass(), events);
        }
        events.add(event);
    }
}