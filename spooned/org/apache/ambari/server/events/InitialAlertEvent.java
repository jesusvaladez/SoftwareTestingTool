package org.apache.ambari.server.events;
public class InitialAlertEvent extends org.apache.ambari.server.events.AlertEvent {
    private final org.apache.ambari.server.orm.entities.AlertCurrentEntity m_currentAlert;

    private final org.apache.ambari.server.orm.entities.AlertHistoryEntity m_history;

    public InitialAlertEvent(long clusterId, org.apache.ambari.server.state.Alert alert, org.apache.ambari.server.orm.entities.AlertCurrentEntity currentAlert) {
        super(clusterId, alert);
        m_currentAlert = currentAlert;
        m_history = currentAlert.getAlertHistory();
    }

    public org.apache.ambari.server.orm.entities.AlertCurrentEntity getCurrentAlert() {
        return m_currentAlert;
    }

    public org.apache.ambari.server.orm.entities.AlertHistoryEntity getNewHistoricalEntry() {
        return m_history;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder buffer = new java.lang.StringBuilder("InitialAlertEvent{");
        buffer.append("cluserId=").append(m_clusterId);
        buffer.append(", alert=").append(m_alert);
        buffer.append("}");
        return buffer.toString();
    }
}