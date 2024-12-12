package org.apache.ambari.server.notifications;
public final class DispatchRunnable implements java.lang.Runnable {
    private final org.apache.ambari.server.notifications.NotificationDispatcher m_dispatcher;

    private final org.apache.ambari.server.notifications.Notification m_notification;

    public DispatchRunnable(org.apache.ambari.server.notifications.NotificationDispatcher dispatcher, org.apache.ambari.server.notifications.Notification notification) {
        m_dispatcher = dispatcher;
        m_notification = notification;
    }

    @java.lang.Override
    public void run() {
        m_dispatcher.dispatch(m_notification);
    }
}