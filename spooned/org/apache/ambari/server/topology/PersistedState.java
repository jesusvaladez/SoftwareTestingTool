package org.apache.ambari.server.topology;
public interface PersistedState {
    org.apache.ambari.server.topology.PersistedTopologyRequest persistTopologyRequest(org.apache.ambari.server.controller.internal.BaseClusterRequest topologyRequest);

    void persistLogicalRequest(org.apache.ambari.server.topology.LogicalRequest logicalRequest, long topologyRequestId);

    void registerPhysicalTask(long logicalTaskId, long physicalTaskId);

    void registerHostName(long hostRequestId, java.lang.String hostName);

    java.util.Map<org.apache.ambari.server.topology.ClusterTopology, java.util.List<org.apache.ambari.server.topology.LogicalRequest>> getAllRequests();

    void registerInTopologyHostInfo(org.apache.ambari.server.state.Host host);

    org.apache.ambari.server.topology.LogicalRequest getProvisionRequest(long clusterId);

    void removeHostRequests(long logicalRequestId, java.util.Collection<org.apache.ambari.server.topology.HostRequest> hostRequests);

    void setHostRequestStatus(long hostRequestId, org.apache.ambari.server.actionmanager.HostRoleStatus status, java.lang.String message);
}