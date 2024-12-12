package org.apache.ambari.server.events;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
public class MetadataUpdateEvent extends org.apache.ambari.server.events.STOMPEvent implements org.apache.ambari.server.agent.stomp.dto.Hashable {
    private static final java.lang.String AMBARI_LEVEL_CLUSTER_ID = "-1";

    private java.lang.String hash;

    private final org.apache.ambari.server.events.UpdateEventType eventType;

    @com.fasterxml.jackson.annotation.JsonProperty("clusters")
    private final java.util.SortedMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.MetadataCluster> metadataClusters;

    private MetadataUpdateEvent() {
        super(org.apache.ambari.server.events.STOMPEvent.Type.METADATA);
        metadataClusters = null;
        eventType = null;
    }

    public MetadataUpdateEvent(java.util.SortedMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.MetadataCluster> metadataClusters, java.util.SortedMap<java.lang.String, java.lang.String> ambariLevelParams, java.util.SortedMap<java.lang.String, java.util.SortedMap<java.lang.String, java.lang.String>> metadataAgentConfigs, org.apache.ambari.server.events.UpdateEventType eventType) {
        super(org.apache.ambari.server.events.STOMPEvent.Type.METADATA);
        this.metadataClusters = metadataClusters;
        if (ambariLevelParams != null) {
            this.metadataClusters.put(org.apache.ambari.server.events.MetadataUpdateEvent.AMBARI_LEVEL_CLUSTER_ID, new org.apache.ambari.server.agent.stomp.dto.MetadataCluster(null, null, false, ambariLevelParams, metadataAgentConfigs));
        }
        this.eventType = eventType;
    }

    public java.util.Map<java.lang.String, org.apache.ambari.server.agent.stomp.dto.MetadataCluster> getMetadataClusters() {
        return metadataClusters;
    }

    @java.lang.Override
    public java.lang.String getHash() {
        return hash;
    }

    @java.lang.Override
    public void setHash(java.lang.String hash) {
        this.hash = hash;
    }

    public org.apache.ambari.server.events.UpdateEventType getEventType() {
        return eventType;
    }

    public static org.apache.ambari.server.events.MetadataUpdateEvent emptyUpdate() {
        return new org.apache.ambari.server.events.MetadataUpdateEvent();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.events.MetadataUpdateEvent that = ((org.apache.ambari.server.events.MetadataUpdateEvent) (o));
        return metadataClusters != null ? metadataClusters.equals(that.metadataClusters) : that.metadataClusters == null;
    }

    @java.lang.Override
    public int hashCode() {
        return metadataClusters != null ? metadataClusters.hashCode() : 0;
    }
}