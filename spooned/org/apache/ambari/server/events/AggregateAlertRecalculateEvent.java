package org.apache.ambari.server.events;
public class AggregateAlertRecalculateEvent extends org.apache.ambari.server.events.AlertEvent {
    public AggregateAlertRecalculateEvent(long clusterId) {
        super(clusterId, null);
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder buffer = new java.lang.StringBuilder("AggregateAlertRecalculateEvent{");
        buffer.append("cluserId=").append(m_clusterId);
        buffer.append("}");
        return buffer.toString();
    }
}