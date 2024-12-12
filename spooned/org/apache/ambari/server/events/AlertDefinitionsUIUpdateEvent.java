package org.apache.ambari.server.events;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
public class AlertDefinitionsUIUpdateEvent extends org.apache.ambari.server.events.STOMPEvent {
    private final java.util.Map<java.lang.Long, org.apache.ambari.server.agent.stomp.dto.AlertCluster> clusters;

    private final org.apache.ambari.server.events.AlertDefinitionEventType eventType;

    protected AlertDefinitionsUIUpdateEvent(org.apache.ambari.server.events.STOMPEvent.Type type, org.apache.ambari.server.events.AlertDefinitionEventType eventType, java.util.Map<java.lang.Long, org.apache.ambari.server.agent.stomp.dto.AlertCluster> clusters) {
        super(type);
        this.eventType = eventType;
        this.clusters = (clusters != null) ? new java.util.HashMap<>(clusters) : null;
    }

    public AlertDefinitionsUIUpdateEvent(org.apache.ambari.server.events.AlertDefinitionEventType eventType, java.util.Map<java.lang.Long, org.apache.ambari.server.agent.stomp.dto.AlertCluster> clusters) {
        this(org.apache.ambari.server.events.STOMPEvent.Type.UI_ALERT_DEFINITIONS, eventType, clusters);
    }

    @com.fasterxml.jackson.annotation.JsonProperty("eventType")
    public org.apache.ambari.server.events.AlertDefinitionEventType getEventType() {
        return eventType;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("clusters")
    public java.util.Map<java.lang.Long, org.apache.ambari.server.agent.stomp.dto.AlertCluster> getClusters() {
        return clusters;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.events.AlertDefinitionsUIUpdateEvent that = ((org.apache.ambari.server.events.AlertDefinitionsUIUpdateEvent) (o));
        if (clusters != null ? !clusters.equals(that.clusters) : that.clusters != null)
            return false;

        return eventType == that.eventType;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (clusters != null) ? clusters.hashCode() : 0;
        result = (31 * result) + eventType.hashCode();
        return result;
    }
}