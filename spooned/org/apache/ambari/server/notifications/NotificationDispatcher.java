package org.apache.ambari.server.notifications;
public interface NotificationDispatcher {
    java.lang.String getType();

    void dispatch(org.apache.ambari.server.notifications.Notification notification);

    boolean isDigestSupported();

    org.apache.ambari.server.notifications.TargetConfigurationResult validateTargetConfig(java.util.Map<java.lang.String, java.lang.Object> properties);

    boolean isNotificationContentGenerationRequired();
}