package org.apache.ambari.server.state;
public interface Clusters {
    void addCluster(java.lang.String clusterName, org.apache.ambari.server.state.StackId stackId) throws org.apache.ambari.server.AmbariException;

    void addCluster(java.lang.String clusterName, org.apache.ambari.server.state.StackId stackId, org.apache.ambari.server.state.SecurityType securityType) throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.state.Cluster getCluster(java.lang.String clusterName) throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.state.Cluster getCluster(java.lang.Long clusterId) throws org.apache.ambari.server.AmbariException;

    java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> getClusters();

    java.util.List<org.apache.ambari.server.state.Host> getHosts();

    java.util.Set<org.apache.ambari.server.state.Cluster> getClustersForHost(java.lang.String hostname) throws org.apache.ambari.server.AmbariException;

    org.apache.ambari.server.state.Host getHost(java.lang.String hostname) throws org.apache.ambari.server.AmbariException;

    boolean hostExists(java.lang.String hostname);

    boolean isHostMappedToCluster(long clusterId, java.lang.String hostName);

    org.apache.ambari.server.state.Host getHostById(java.lang.Long hostId) throws org.apache.ambari.server.AmbariException;

    void updateHostMappings(org.apache.ambari.server.state.Host host);

    void addHost(java.lang.String hostname) throws org.apache.ambari.server.AmbariException;

    void mapHostToCluster(java.lang.String hostname, java.lang.String clusterName) throws org.apache.ambari.server.AmbariException;

    void mapAndPublishHostsToCluster(java.util.Set<java.lang.String> hostnames, java.lang.String clusterName) throws org.apache.ambari.server.AmbariException;

    void updateClusterName(java.lang.String oldName, java.lang.String newName);

    org.apache.ambari.server.state.Cluster getClusterById(long id) throws org.apache.ambari.server.AmbariException;

    void debugDump(java.lang.StringBuilder sb);

    java.util.Map<java.lang.String, org.apache.ambari.server.state.Host> getHostsForCluster(java.lang.String clusterName);

    java.util.Map<java.lang.Long, org.apache.ambari.server.state.Host> getHostIdsForCluster(java.lang.String clusterName) throws org.apache.ambari.server.AmbariException;

    void deleteCluster(java.lang.String clusterName) throws org.apache.ambari.server.AmbariException;

    void updateHostWithClusterAndAttributes(java.util.Map<java.lang.String, java.util.Set<java.lang.String>> hostsClusters, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> hostAttributes) throws org.apache.ambari.server.AmbariException;

    void unmapHostFromCluster(java.lang.String hostname, java.lang.String clusterName) throws org.apache.ambari.server.AmbariException;

    void deleteHost(java.lang.String hostname) throws org.apache.ambari.server.AmbariException;

    void publishHostsDeletion(java.util.Set<java.lang.Long> hostIds, java.util.Set<java.lang.String> hostNames) throws org.apache.ambari.server.AmbariException;

    boolean checkPermission(java.lang.String clusterName, boolean readOnly);

    void addSessionAttributes(java.lang.String name, java.util.Map<java.lang.String, java.lang.Object> attributes);

    java.util.Map<java.lang.String, java.lang.Object> getSessionAttributes(java.lang.String name);

    int getClusterSize(java.lang.String clusterName);

    void invalidate(org.apache.ambari.server.state.Cluster cluster);

    void invalidateAllClusters();
}