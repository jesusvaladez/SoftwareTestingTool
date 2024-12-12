package org.apache.ambari.server.notifications;
@com.google.inject.Singleton
public class DispatchFactory {
    private static final org.apache.ambari.server.notifications.DispatchFactory s_instance = new org.apache.ambari.server.notifications.DispatchFactory();

    private final java.util.Map<java.lang.String, org.apache.ambari.server.notifications.NotificationDispatcher> m_dispatchers = new java.util.HashMap<>();

    private DispatchFactory() {
    }

    public static org.apache.ambari.server.notifications.DispatchFactory getInstance() {
        return org.apache.ambari.server.notifications.DispatchFactory.s_instance;
    }

    public void register(java.lang.String type, org.apache.ambari.server.notifications.NotificationDispatcher dispatcher) {
        if (null == dispatcher) {
            m_dispatchers.remove(type);
        } else {
            m_dispatchers.put(type, dispatcher);
        }
    }

    public org.apache.ambari.server.notifications.NotificationDispatcher getDispatcher(java.lang.String type) {
        return m_dispatchers.get(type);
    }
}