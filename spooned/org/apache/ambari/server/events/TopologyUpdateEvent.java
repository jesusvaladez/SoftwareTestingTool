package org.apache.ambari.server.events;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
public class TopologyUpdateEvent extends org.apache.ambari.server.events.STOMPEvent implements org.apache.ambari.server.agent.stomp.dto.Hashable {
    @com.fasterxml.jackson.annotation.JsonProperty("clusters")
    private final java.util.SortedMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.TopologyCluster> clusters;

    private java.lang.String hash;

    private final org.apache.ambari.server.events.UpdateEventType eventType;

    public TopologyUpdateEvent(java.util.SortedMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.TopologyCluster> clusters, org.apache.ambari.server.events.UpdateEventType eventType) {
        this(org.apache.ambari.server.events.STOMPEvent.Type.UI_TOPOLOGY, clusters, null, eventType);
    }

    public TopologyUpdateEvent(org.apache.ambari.server.events.STOMPEvent.Type type, java.util.SortedMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.TopologyCluster> clusters, java.lang.String hash, org.apache.ambari.server.events.UpdateEventType eventType) {
        super(type);
        this.clusters = clusters;
        this.hash = hash;
        this.eventType = eventType;
    }

    public java.util.SortedMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.TopologyCluster> getClusters() {
        return clusters;
    }

    public org.apache.ambari.server.events.TopologyUpdateEvent deepCopy() {
        java.util.SortedMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.TopologyCluster> copiedClusters = new java.util.TreeMap<>();
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.agent.stomp.dto.TopologyCluster> topologyClusterEntry : getClusters().entrySet()) {
            copiedClusters.put(topologyClusterEntry.getKey(), topologyClusterEntry.getValue().deepCopyCluster());
        }
        org.apache.ambari.server.events.TopologyUpdateEvent copiedEvent = new org.apache.ambari.server.events.TopologyUpdateEvent(copiedClusters, getEventType());
        copiedEvent.setHash(getHash());
        return copiedEvent;
    }

    public org.apache.ambari.server.events.UpdateEventType getEventType() {
        return eventType;
    }

    @java.lang.Override
    public java.lang.String getHash() {
        return hash;
    }

    @java.lang.Override
    public void setHash(java.lang.String hash) {
        this.hash = hash;
    }

    public static org.apache.ambari.server.events.TopologyUpdateEvent emptyUpdate() {
        return new org.apache.ambari.server.events.TopologyUpdateEvent(null, null);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.events.TopologyUpdateEvent that = ((org.apache.ambari.server.events.TopologyUpdateEvent) (o));
        return java.util.Objects.equals(eventType, that.eventType) && java.util.Objects.equals(clusters, that.clusters);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(clusters, eventType);
    }
}