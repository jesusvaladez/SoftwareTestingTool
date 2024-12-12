package org.apache.ambari.server.events;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
public class AlertDefinitionsAgentUpdateEvent extends org.apache.ambari.server.events.STOMPHostEvent implements org.apache.ambari.server.agent.stomp.dto.Hashable {
    private final java.util.Map<java.lang.Long, org.apache.ambari.server.agent.stomp.dto.AlertCluster> clusters;

    private final org.apache.ambari.server.events.AlertDefinitionEventType eventType;

    private final java.lang.String hostName;

    private final java.lang.Long hostId;

    private java.lang.String hash;

    public static org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent emptyEvent() {
        return new org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent(null, null, null, null);
    }

    public AlertDefinitionsAgentUpdateEvent(org.apache.ambari.server.events.AlertDefinitionEventType eventType, java.util.Map<java.lang.Long, org.apache.ambari.server.agent.stomp.dto.AlertCluster> clusters, java.lang.String hostName, java.lang.Long hostId) {
        super(org.apache.ambari.server.events.STOMPEvent.Type.ALERT_DEFINITIONS);
        this.eventType = eventType;
        this.clusters = (clusters != null) ? new java.util.HashMap<>(clusters) : null;
        this.hostName = hostName;
        this.hostId = hostId;
    }

    @java.lang.Override
    public java.lang.String getHash() {
        return hash;
    }

    @java.lang.Override
    @com.fasterxml.jackson.annotation.JsonProperty("hash")
    public void setHash(java.lang.String hash) {
        this.hash = hash;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("hostName")
    public java.lang.String getHostName() {
        return hostName;
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

        org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent other = ((org.apache.ambari.server.events.AlertDefinitionsAgentUpdateEvent) (o));
        return java.util.Objects.equals(eventType, other.eventType) && java.util.Objects.equals(clusters, other.clusters);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(eventType, clusters);
    }

    @java.lang.Override
    public java.lang.Long getHostId() {
        return hostId;
    }
}