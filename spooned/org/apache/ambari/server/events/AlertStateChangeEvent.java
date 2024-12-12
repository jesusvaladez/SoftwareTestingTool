package org.apache.ambari.server.events;
public class AlertStateChangeEvent extends org.apache.ambari.server.events.AlertEvent {
    private final org.apache.ambari.server.state.AlertState m_fromState;

    private final org.apache.ambari.server.state.AlertFirmness m_fromFirmness;

    private final org.apache.ambari.server.orm.entities.AlertCurrentEntity m_currentAlert;

    private final org.apache.ambari.server.orm.entities.AlertHistoryEntity m_history;

    public AlertStateChangeEvent(long clusterId, org.apache.ambari.server.state.Alert alert, org.apache.ambari.server.orm.entities.AlertCurrentEntity currentAlert, org.apache.ambari.server.state.AlertState fromState, org.apache.ambari.server.state.AlertFirmness fromFirmness) {
        super(clusterId, alert);
        m_currentAlert = currentAlert;
        m_history = currentAlert.getAlertHistory();
        m_fromState = fromState;
        m_fromFirmness = fromFirmness;
    }

    public org.apache.ambari.server.orm.entities.AlertCurrentEntity getCurrentAlert() {
        return m_currentAlert;
    }

    public org.apache.ambari.server.orm.entities.AlertHistoryEntity getNewHistoricalEntry() {
        return m_history;
    }

    public org.apache.ambari.server.state.AlertState getFromState() {
        return m_fromState;
    }

    public org.apache.ambari.server.state.AlertFirmness getFromFirmness() {
        return m_fromFirmness;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder buffer = new java.lang.StringBuilder("AlertStateChangeEvent{");
        buffer.append("cluserId=").append(m_clusterId);
        buffer.append(", fromState=").append(m_fromState);
        buffer.append(", firmness=").append(m_currentAlert.getFirmness());
        buffer.append(", alert=").append(m_alert);
        buffer.append("}");
        return buffer.toString();
    }
}