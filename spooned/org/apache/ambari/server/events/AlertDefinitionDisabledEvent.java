package org.apache.ambari.server.events;
public class AlertDefinitionDisabledEvent extends org.apache.ambari.server.events.ClusterEvent {
    private final long m_definitionId;

    private final java.lang.String definitionName;

    public AlertDefinitionDisabledEvent(long clusterId, long definitionId, java.lang.String definitionName) {
        super(org.apache.ambari.server.events.AmbariEvent.AmbariEventType.ALERT_DEFINITION_DISABLED, clusterId);
        m_definitionId = definitionId;
        this.definitionName = definitionName;
    }

    public long getDefinitionId() {
        return m_definitionId;
    }

    public java.lang.String getDefinitionName() {
        return definitionName;
    }
}