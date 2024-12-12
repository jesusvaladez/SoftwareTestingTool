package org.apache.ambari.server.events;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
public class ExecutionCommandEvent extends org.apache.ambari.server.events.STOMPHostEvent {
    private final java.lang.Long hostId;

    @com.fasterxml.jackson.annotation.JsonProperty("requiredConfigTimestamp")
    private java.lang.Long requiredConfigTimestamp;

    @com.fasterxml.jackson.annotation.JsonProperty("clusters")
    private java.util.TreeMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.ExecutionCommandsCluster> clusters;

    public ExecutionCommandEvent(java.lang.Long hostId, java.lang.Long requiredConfigTimestamp, java.util.TreeMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.ExecutionCommandsCluster> clusters) {
        super(org.apache.ambari.server.events.STOMPEvent.Type.COMMAND);
        this.hostId = hostId;
        this.requiredConfigTimestamp = requiredConfigTimestamp;
        this.clusters = clusters;
    }

    public java.util.TreeMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.ExecutionCommandsCluster> getClusters() {
        return clusters;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        org.apache.ambari.server.events.ExecutionCommandEvent that = ((org.apache.ambari.server.events.ExecutionCommandEvent) (o));
        if (hostId != null ? !hostId.equals(that.hostId) : that.hostId != null)
            return false;

        return clusters != null ? clusters.equals(that.clusters) : that.clusters == null;
    }

    @java.lang.Override
    public int hashCode() {
        int result = (hostId != null) ? hostId.hashCode() : 0;
        result = (31 * result) + (clusters != null ? clusters.hashCode() : 0);
        return result;
    }

    @java.lang.Override
    public java.lang.Long getHostId() {
        return hostId;
    }

    public java.lang.Long getRequiredConfigTimestamp() {
        return requiredConfigTimestamp;
    }
}