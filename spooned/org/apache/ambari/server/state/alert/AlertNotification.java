package org.apache.ambari.server.state.alert;
public class AlertNotification extends org.apache.ambari.server.notifications.Notification {
    private org.apache.ambari.server.state.services.AlertNoticeDispatchService.AlertInfo m_alertInfo;

    @java.lang.Override
    public org.apache.ambari.server.notifications.Notification.Type getType() {
        return org.apache.ambari.server.notifications.Notification.Type.ALERT;
    }

    public org.apache.ambari.server.state.services.AlertNoticeDispatchService.AlertInfo getAlertInfo() {
        return m_alertInfo;
    }

    public void setAlertInfo(org.apache.ambari.server.state.services.AlertNoticeDispatchService.AlertInfo alertInfo) {
        m_alertInfo = alertInfo;
    }
}