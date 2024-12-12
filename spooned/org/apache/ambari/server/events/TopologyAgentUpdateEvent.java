package org.apache.ambari.server.events;
@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY)
public class TopologyAgentUpdateEvent extends org.apache.ambari.server.events.TopologyUpdateEvent {
    public TopologyAgentUpdateEvent(java.util.SortedMap<java.lang.String, org.apache.ambari.server.agent.stomp.dto.TopologyCluster> clusters, java.lang.String hash, org.apache.ambari.server.events.UpdateEventType eventType) {
        super(org.apache.ambari.server.events.STOMPEvent.Type.AGENT_TOPOLOGY, clusters, hash, eventType);
    }
}