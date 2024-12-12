package org.apache.ambari.server.events;
public abstract class AlertEvent {
    protected long m_clusterId;

    protected org.apache.ambari.server.state.Alert m_alert;

    protected java.util.List<org.apache.ambari.server.state.Alert> m_alerts;

    public AlertEvent(long clusterId, org.apache.ambari.server.state.Alert alert) {
        m_clusterId = clusterId;
        m_alert = alert;
    }

    public AlertEvent(java.util.List<org.apache.ambari.server.state.Alert> m_alerts) {
        this.m_alerts = m_alerts;
    }

    public long getClusterId() {
        return m_clusterId;
    }

    public org.apache.ambari.server.state.Alert getAlert() {
        return m_alert;
    }

    public java.util.List<org.apache.ambari.server.state.Alert> getAlerts() {
        if (m_alerts != null) {
            return m_alerts;
        } else {
            return java.util.Collections.singletonList(m_alert);
        }
    }
}