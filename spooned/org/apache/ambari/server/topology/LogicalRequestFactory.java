package org.apache.ambari.server.topology;
public class LogicalRequestFactory {
    public org.apache.ambari.server.topology.LogicalRequest createRequest(java.lang.Long id, org.apache.ambari.server.topology.TopologyRequest topologyRequest, org.apache.ambari.server.topology.ClusterTopology topology) throws org.apache.ambari.server.AmbariException {
        return new org.apache.ambari.server.topology.LogicalRequest(id, topologyRequest, topology);
    }

    public org.apache.ambari.server.topology.LogicalRequest createRequest(java.lang.Long id, org.apache.ambari.server.topology.TopologyRequest topologyRequest, org.apache.ambari.server.topology.ClusterTopology topology, org.apache.ambari.server.orm.entities.TopologyLogicalRequestEntity requestEntity) throws org.apache.ambari.server.AmbariException {
        return new org.apache.ambari.server.topology.LogicalRequest(id, topologyRequest, topology, requestEntity);
    }
}