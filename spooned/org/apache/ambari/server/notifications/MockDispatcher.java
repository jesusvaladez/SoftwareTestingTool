package org.apache.ambari.server.notifications;
public class MockDispatcher implements org.apache.ambari.server.notifications.NotificationDispatcher {
    public MockDispatcher() {
    }

    @java.lang.Override
    public java.lang.String getType() {
        return "MOCK";
    }

    @java.lang.Override
    public boolean isNotificationContentGenerationRequired() {
        return true;
    }

    @java.lang.Override
    public boolean isDigestSupported() {
        return true;
    }

    @java.lang.Override
    public void dispatch(org.apache.ambari.server.notifications.Notification notification) {
    }

    @java.lang.Override
    public org.apache.ambari.server.notifications.TargetConfigurationResult validateTargetConfig(java.util.Map<java.lang.String, java.lang.Object> properties) {
        return null;
    }
}