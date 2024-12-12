package org.apache.ambari.server.topology;
public interface ClusterTopology {
    java.lang.Long getClusterId();

    void setClusterId(java.lang.Long clusterId);

    org.apache.ambari.server.topology.Blueprint getBlueprint();

    org.apache.ambari.server.topology.Configuration getConfiguration();

    java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> getHostGroupInfo();

    java.util.Set<java.lang.String> getAllHosts();

    java.util.Collection<java.lang.String> getHostGroupsForComponent(java.lang.String component);

    java.lang.String getHostGroupForHost(java.lang.String hostname);

    java.util.Collection<java.lang.String> getHostAssignmentsForComponent(java.lang.String component);

    void update(org.apache.ambari.server.topology.TopologyRequest topologyRequest) throws org.apache.ambari.server.topology.InvalidTopologyException;

    void addHostToTopology(java.lang.String hostGroupName, java.lang.String host) throws org.apache.ambari.server.topology.InvalidTopologyException, org.apache.ambari.server.topology.NoSuchHostGroupException;

    boolean isNameNodeHAEnabled();

    boolean isYarnResourceManagerHAEnabled();

    boolean isClusterKerberosEnabled();

    org.apache.ambari.server.controller.RequestStatusResponse installHost(java.lang.String hostName, boolean skipInstallTaskCreate, boolean skipFailure);

    org.apache.ambari.server.controller.RequestStatusResponse startHost(java.lang.String hostName, boolean skipFailure);

    void setConfigRecommendationStrategy(org.apache.ambari.server.topology.ConfigRecommendationStrategy strategy);

    org.apache.ambari.server.topology.ConfigRecommendationStrategy getConfigRecommendationStrategy();

    void setProvisionAction(org.apache.ambari.server.controller.internal.ProvisionAction provisionAction);

    org.apache.ambari.server.controller.internal.ProvisionAction getProvisionAction();

    java.util.Map<java.lang.String, org.apache.ambari.server.topology.AdvisedConfiguration> getAdvisedConfigurations();

    org.apache.ambari.server.topology.AmbariContext getAmbariContext();

    void removeHost(java.lang.String hostname);

    java.lang.String getDefaultPassword();

    boolean hasHadoopCompatibleService();
}