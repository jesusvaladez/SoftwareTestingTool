package org.apache.ambari.server.events;
public class AlertDefinitionDeleteEvent extends org.apache.ambari.server.events.ClusterEvent {
    private final org.apache.ambari.server.state.alert.AlertDefinition m_definition;

    public AlertDefinitionDeleteEvent(long clusterId, org.apache.ambari.server.state.alert.AlertDefinition definition) {
        super(org.apache.ambari.server.events.AmbariEvent.AmbariEventType.ALERT_DEFINITION_REMOVAL, clusterId);
        m_definition = definition;
    }

    public org.apache.ambari.server.state.alert.AlertDefinition getDefinition() {
        return m_definition;
    }
}