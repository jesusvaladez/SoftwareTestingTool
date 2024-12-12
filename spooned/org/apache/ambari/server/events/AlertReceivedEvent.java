package org.apache.ambari.server.events;
public final class AlertReceivedEvent extends org.apache.ambari.server.events.AlertEvent {
    public AlertReceivedEvent(long clusterId, org.apache.ambari.server.state.Alert alert) {
        super(clusterId, alert);
    }

    public AlertReceivedEvent(java.util.List<org.apache.ambari.server.state.Alert> alerts) {
        super(alerts);
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder buffer = new java.lang.StringBuilder("AlertReceivedEvent{");
        buffer.append("cluserId=").append(m_clusterId);
        buffer.append(", alerts=").append(getAlerts());
        buffer.append("}");
        return buffer.toString();
    }
}