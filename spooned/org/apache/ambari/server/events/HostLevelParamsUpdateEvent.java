package org.apache.ambari.server.events;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
public class HostLevelParamsUpdateEvent extends org.apache.ambari.server.events.STOMPHostEvent implements org.apache.ambari.server.agent.stomp.dto.Hashable {
    private java.lang.String hash;

    private final java.lang.Long hostId;

    @com.fasterxml.jackson.annotation.JsonProperty("clusters")
    private final java.util.Map<java.lang.String, org.apache.ambari.server.agent.stomp.dto.HostLevelParamsCluster> hostLevelParamsClusters;

    public HostLevelParamsUpdateEvent(java.lang.Long hostId, java.util.Map<java.lang.String, org.apache.ambari.server.agent.stomp.dto.HostLevelParamsCluster> hostLevelParamsClusters) {
        super(org.apache.ambari.server.events.STOMPEvent.Type.HOSTLEVELPARAMS);
        this.hostId = hostId;
        this.hostLevelParamsClusters = hostLevelParamsClusters;
    }

    public HostLevelParamsUpdateEvent(java.lang.Long hostId, java.lang.String clusterId, org.apache.ambari.server.agent.stomp.dto.HostLevelParamsCluster hostLevelParamsCluster) {
        this(hostId, java.util.Collections.singletonMap(clusterId, hostLevelParamsCluster));
    }

    @java.lang.Override
    public java.lang.String getHash() {
        return hash;
    }

    @java.lang.Override
    public void setHash(java.lang.String hash) {
        this.hash = hash;
    }

    public static org.apache.ambari.server.events.HostLevelParamsUpdateEvent emptyUpdate() {
        return new org.apache.ambari.server.events.HostLevelParamsUpdateEvent(null, null);
    }

    @java.lang.Override
    public java.lang.Long getHostId() {
        return hostId;
    }

    public java.util.Map<java.lang.String, org.apache.ambari.server.agent.stomp.dto.HostLevelParamsCluster> getHostLevelParamsClusters() {
        return hostLevelParamsClusters == null ? null : java.util.Collections.unmodifiableMap(hostLevelParamsClusters);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.events.HostLevelParamsUpdateEvent that = ((org.apache.ambari.server.events.HostLevelParamsUpdateEvent) (o));
        return java.util.Objects.equals(hostId, that.hostId) && java.util.Objects.equals(hostLevelParamsClusters, that.hostLevelParamsClusters);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(hostId, hostLevelParamsClusters);
    }
}