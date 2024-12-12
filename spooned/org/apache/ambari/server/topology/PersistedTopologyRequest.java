package org.apache.ambari.server.topology;
public class PersistedTopologyRequest {
    private final long id;

    private final org.apache.ambari.server.topology.TopologyRequest request;

    public PersistedTopologyRequest(long id, org.apache.ambari.server.topology.TopologyRequest request) {
        this.id = id;
        this.request = request;
    }

    public long getId() {
        return id;
    }

    public org.apache.ambari.server.topology.TopologyRequest getRequest() {
        return request;
    }
}