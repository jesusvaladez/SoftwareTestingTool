package org.apache.ambari.server.topology;
public interface TopologyValidator {
    void validate(org.apache.ambari.server.topology.ClusterTopology topology) throws org.apache.ambari.server.topology.InvalidTopologyException;
}