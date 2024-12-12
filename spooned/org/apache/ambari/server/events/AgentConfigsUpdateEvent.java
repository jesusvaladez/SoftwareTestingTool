package org.apache.ambari.server.events;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
public class AgentConfigsUpdateEvent extends org.apache.ambari.server.events.STOMPHostEvent implements org.apache.ambari.server.agent.stomp.dto.Hashable {
    private java.lang.String hash;

    private java.lang.Long timestamp;

    private final java.lang.Long hostId;

    @com.fasterxml.jackson.annotation.JsonProperty("clusters")
    private final java.util.SortedMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.ClusterConfigs> clustersConfigs;

    public AgentConfigsUpdateEvent(java.lang.Long hostId, java.util.SortedMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.ClusterConfigs> clustersConfigs) {
        super(org.apache.ambari.server.events.STOMPEvent.Type.AGENT_CONFIGS);
        this.hostId = hostId;
        this.clustersConfigs = clustersConfigs;
        this.timestamp = java.lang.System.currentTimeMillis();
    }

    @java.lang.Override
    public java.lang.String getHash() {
        return hash;
    }

    @java.lang.Override
    public void setHash(java.lang.String hash) {
        this.hash = hash;
    }

    public java.lang.Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(java.lang.Long timestamp) {
        this.timestamp = timestamp;
    }

    @java.lang.Override
    public java.lang.Long getHostId() {
        return hostId;
    }

    public java.util.SortedMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.ClusterConfigs> getClustersConfigs() {
        return clustersConfigs;
    }

    public static org.apache.ambari.server.events.AgentConfigsUpdateEvent emptyUpdate() {
        return new org.apache.ambari.server.events.AgentConfigsUpdateEvent(null, null);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.events.AgentConfigsUpdateEvent that = ((org.apache.ambari.server.events.AgentConfigsUpdateEvent) (o));
        return java.util.Objects.equals(hostId, that.hostId) && java.util.Objects.equals(clustersConfigs, that.clustersConfigs);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(hostId, clustersConfigs);
    }
}