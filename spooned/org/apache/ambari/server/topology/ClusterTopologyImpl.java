package org.apache.ambari.server.topology;
import static org.apache.ambari.server.controller.internal.HostComponentResourceProvider.ALL_COMPONENTS;
import static org.apache.ambari.server.controller.internal.ProvisionAction.INSTALL_AND_START;
import static org.apache.ambari.server.controller.internal.ProvisionAction.INSTALL_ONLY;
import static org.apache.ambari.server.state.ServiceInfo.HADOOP_COMPATIBLE_FS;
public class ClusterTopologyImpl implements org.apache.ambari.server.topology.ClusterTopology {
    private java.lang.Long clusterId;

    private org.apache.ambari.server.topology.Blueprint blueprint;

    private org.apache.ambari.server.topology.Configuration configuration;

    private org.apache.ambari.server.topology.ConfigRecommendationStrategy configRecommendationStrategy;

    private org.apache.ambari.server.controller.internal.ProvisionAction provisionAction = org.apache.ambari.server.controller.internal.ProvisionAction.INSTALL_AND_START;

    private java.util.Map<java.lang.String, org.apache.ambari.server.topology.AdvisedConfiguration> advisedConfigurations = new java.util.HashMap<>();

    private final java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> hostGroupInfoMap = new java.util.HashMap<>();

    private final org.apache.ambari.server.topology.AmbariContext ambariContext;

    private final java.lang.String defaultPassword;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.topology.ClusterTopologyImpl.class);

    public ClusterTopologyImpl(org.apache.ambari.server.topology.AmbariContext ambariContext, org.apache.ambari.server.topology.TopologyRequest topologyRequest) throws org.apache.ambari.server.topology.InvalidTopologyException {
        this.clusterId = topologyRequest.getClusterId();
        this.blueprint = topologyRequest.getBlueprint();
        this.configuration = topologyRequest.getConfiguration();
        if (topologyRequest instanceof org.apache.ambari.server.controller.internal.ProvisionClusterRequest) {
            this.defaultPassword = ((org.apache.ambari.server.controller.internal.ProvisionClusterRequest) (topologyRequest)).getDefaultPassword();
        } else {
            this.defaultPassword = null;
        }
        registerHostGroupInfo(topologyRequest.getHostGroupInfo());
        this.ambariContext = ambariContext;
    }

    @java.lang.Override
    public void update(org.apache.ambari.server.topology.TopologyRequest topologyRequest) throws org.apache.ambari.server.topology.InvalidTopologyException {
        registerHostGroupInfo(topologyRequest.getHostGroupInfo());
    }

    @java.lang.Override
    public java.lang.Long getClusterId() {
        return clusterId;
    }

    @java.lang.Override
    public void setClusterId(java.lang.Long clusterId) {
        this.clusterId = clusterId;
    }

    @java.lang.Override
    public org.apache.ambari.server.topology.Blueprint getBlueprint() {
        return blueprint;
    }

    @java.lang.Override
    public org.apache.ambari.server.topology.Configuration getConfiguration() {
        return configuration;
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> getHostGroupInfo() {
        return hostGroupInfoMap;
    }

    @java.lang.Override
    public java.util.Collection<java.lang.String> getHostGroupsForComponent(java.lang.String component) {
        java.util.Collection<java.lang.String> resultGroups = new java.util.ArrayList<>();
        for (org.apache.ambari.server.topology.HostGroup group : getBlueprint().getHostGroups().values()) {
            if (group.getComponentNames().contains(component)) {
                resultGroups.add(group.getName());
            }
        }
        return resultGroups;
    }

    @java.lang.Override
    public java.lang.String getHostGroupForHost(java.lang.String hostname) {
        for (org.apache.ambari.server.topology.HostGroupInfo groupInfo : hostGroupInfoMap.values()) {
            if (groupInfo.getHostNames().contains(hostname)) {
                return groupInfo.getHostGroupName();
            }
        }
        return null;
    }

    @java.lang.Override
    public void addHostToTopology(java.lang.String hostGroupName, java.lang.String host) throws org.apache.ambari.server.topology.InvalidTopologyException, org.apache.ambari.server.topology.NoSuchHostGroupException {
        if (blueprint.getHostGroup(hostGroupName) == null) {
            throw new org.apache.ambari.server.topology.NoSuchHostGroupException("Attempted to add host to non-existing host group: " + hostGroupName);
        }
        java.lang.String groupContainsHost = getHostGroupForHost(host);
        if ((groupContainsHost != null) && (!hostGroupName.equals(groupContainsHost))) {
            throw new org.apache.ambari.server.topology.InvalidTopologyException(java.lang.String.format("Attempted to add host '%s' to hostgroup '%s' but it is already associated with hostgroup '%s'.", host, hostGroupName, groupContainsHost));
        }
        synchronized(hostGroupInfoMap) {
            org.apache.ambari.server.topology.HostGroupInfo existingHostGroupInfo = hostGroupInfoMap.get(hostGroupName);
            if (existingHostGroupInfo == null) {
                throw new java.lang.RuntimeException(java.lang.String.format("An attempt was made to add host '%s' to an unregistered hostgroup '%s'", host, hostGroupName));
            }
            existingHostGroupInfo.addHost(host);
            org.apache.ambari.server.topology.ClusterTopologyImpl.LOG.info((("ClusterTopologyImpl.addHostTopology: added host = " + host) + " to host group = ") + existingHostGroupInfo.getHostGroupName());
        }
    }

    @java.lang.Override
    public java.util.Set<java.lang.String> getAllHosts() {
        return hostGroupInfoMap.values().stream().flatMap(hg -> hg.getHostNames().stream()).collect(java.util.stream.Collectors.toSet());
    }

    @java.lang.Override
    public java.util.Collection<java.lang.String> getHostAssignmentsForComponent(java.lang.String component) {
        java.util.Collection<java.lang.String> hosts = new java.util.ArrayList<>();
        java.util.Collection<java.lang.String> hostGroups = getHostGroupsForComponent(component);
        for (java.lang.String group : hostGroups) {
            org.apache.ambari.server.topology.HostGroupInfo hostGroupInfo = getHostGroupInfo().get(group);
            if (hostGroupInfo != null) {
                hosts.addAll(hostGroupInfo.getHostNames());
            } else {
                org.apache.ambari.server.topology.ClusterTopologyImpl.LOG.warn("HostGroup {} not found, when checking for hosts for component {}", group, component);
            }
        }
        return hosts;
    }

    @java.lang.Override
    public boolean isNameNodeHAEnabled() {
        return org.apache.ambari.server.topology.ClusterTopologyImpl.isNameNodeHAEnabled(configuration.getFullProperties());
    }

    public static boolean isNameNodeHAEnabled(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurationProperties) {
        return configurationProperties.containsKey("hdfs-site") && (configurationProperties.get("hdfs-site").containsKey("dfs.nameservices") || configurationProperties.get("hdfs-site").containsKey("dfs.internal.nameservices"));
    }

    @java.lang.Override
    public boolean isYarnResourceManagerHAEnabled() {
        return org.apache.ambari.server.topology.ClusterTopologyImpl.isYarnResourceManagerHAEnabled(configuration.getFullProperties());
    }

    static boolean isYarnResourceManagerHAEnabled(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties) {
        return (configProperties.containsKey("yarn-site") && configProperties.get("yarn-site").containsKey("yarn.resourcemanager.ha.enabled")) && configProperties.get("yarn-site").get("yarn.resourcemanager.ha.enabled").equals("true");
    }

    @java.lang.Override
    public boolean isClusterKerberosEnabled() {
        return ambariContext.isClusterKerberosEnabled(getClusterId());
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.RequestStatusResponse installHost(java.lang.String hostName, boolean skipInstallTaskCreate, boolean skipFailure) {
        try {
            java.lang.String hostGroupName = getHostGroupForHost(hostName);
            org.apache.ambari.server.topology.HostGroup hostGroup = this.blueprint.getHostGroup(hostGroupName);
            java.util.Collection<java.lang.String> skipInstallForComponents = new java.util.ArrayList<>();
            if (skipInstallTaskCreate) {
                skipInstallForComponents.add(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.ALL_COMPONENTS);
            } else {
                skipInstallForComponents.addAll(hostGroup.getComponentNames(org.apache.ambari.server.controller.internal.ProvisionAction.START_ONLY));
            }
            java.util.Collection<java.lang.String> dontSkipInstallForComponents = hostGroup.getComponentNames(org.apache.ambari.server.controller.internal.ProvisionAction.INSTALL_ONLY);
            dontSkipInstallForComponents.addAll(hostGroup.getComponentNames(org.apache.ambari.server.controller.internal.ProvisionAction.INSTALL_AND_START));
            return ambariContext.installHost(hostName, ambariContext.getClusterName(getClusterId()), skipInstallForComponents, dontSkipInstallForComponents, skipFailure);
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.topology.ClusterTopologyImpl.LOG.error("Cannot get cluster name for clusterId = " + getClusterId(), e);
            throw new java.lang.RuntimeException(e);
        }
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.RequestStatusResponse startHost(java.lang.String hostName, boolean skipFailure) {
        try {
            java.lang.String hostGroupName = getHostGroupForHost(hostName);
            org.apache.ambari.server.topology.HostGroup hostGroup = this.blueprint.getHostGroup(hostGroupName);
            java.util.Collection<java.lang.String> installOnlyComponents = hostGroup.getComponentNames(org.apache.ambari.server.controller.internal.ProvisionAction.INSTALL_ONLY);
            return ambariContext.startHost(hostName, ambariContext.getClusterName(getClusterId()), installOnlyComponents, skipFailure);
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.topology.ClusterTopologyImpl.LOG.error("Cannot get cluster name for clusterId = " + getClusterId(), e);
            throw new java.lang.RuntimeException(e);
        }
    }

    @java.lang.Override
    public void setConfigRecommendationStrategy(org.apache.ambari.server.topology.ConfigRecommendationStrategy strategy) {
        this.configRecommendationStrategy = strategy;
    }

    @java.lang.Override
    public org.apache.ambari.server.topology.ConfigRecommendationStrategy getConfigRecommendationStrategy() {
        return this.configRecommendationStrategy;
    }

    @java.lang.Override
    public org.apache.ambari.server.controller.internal.ProvisionAction getProvisionAction() {
        return provisionAction;
    }

    @java.lang.Override
    public void setProvisionAction(org.apache.ambari.server.controller.internal.ProvisionAction provisionAction) {
        this.provisionAction = provisionAction;
    }

    @java.lang.Override
    public java.util.Map<java.lang.String, org.apache.ambari.server.topology.AdvisedConfiguration> getAdvisedConfigurations() {
        return this.advisedConfigurations;
    }

    @java.lang.Override
    public org.apache.ambari.server.topology.AmbariContext getAmbariContext() {
        return ambariContext;
    }

    @java.lang.Override
    public void removeHost(java.lang.String hostname) {
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> entry : hostGroupInfoMap.entrySet()) {
            entry.getValue().removeHost(hostname);
        }
    }

    @java.lang.Override
    public java.lang.String getDefaultPassword() {
        return defaultPassword;
    }

    @java.lang.Override
    public boolean hasHadoopCompatibleService() {
        return blueprint.getServiceInfos().stream().anyMatch(service -> org.apache.ambari.server.state.ServiceInfo.HADOOP_COMPATIBLE_FS.equals(service.getServiceType()));
    }

    private void registerHostGroupInfo(java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> requestedHostGroupInfoMap) throws org.apache.ambari.server.topology.InvalidTopologyException {
        org.apache.ambari.server.topology.ClusterTopologyImpl.LOG.debug("Registering requested host group information for {} hostgroups", requestedHostGroupInfoMap.size());
        checkForDuplicateHosts(requestedHostGroupInfoMap);
        for (org.apache.ambari.server.topology.HostGroupInfo requestedHostGroupInfo : requestedHostGroupInfoMap.values()) {
            java.lang.String hostGroupName = requestedHostGroupInfo.getHostGroupName();
            org.apache.ambari.server.topology.HostGroup baseHostGroup = getBlueprint().getHostGroup(hostGroupName);
            if (baseHostGroup == null) {
                throw new java.lang.IllegalArgumentException(("Invalid host_group specified: " + hostGroupName) + ".  All request host groups must have a corresponding host group in the specified blueprint");
            }
            org.apache.ambari.server.topology.HostGroupInfo currentHostGroupInfo = hostGroupInfoMap.get(hostGroupName);
            if (currentHostGroupInfo == null) {
                org.apache.ambari.server.topology.Configuration bpHostGroupConfig = baseHostGroup.getConfiguration();
                org.apache.ambari.server.topology.Configuration parentConfiguration = new org.apache.ambari.server.topology.Configuration(bpHostGroupConfig.getProperties(), bpHostGroupConfig.getAttributes(), getConfiguration());
                requestedHostGroupInfo.getConfiguration().setParentConfiguration(parentConfiguration);
                hostGroupInfoMap.put(hostGroupName, requestedHostGroupInfo);
            } else if (!requestedHostGroupInfo.getHostNames().isEmpty()) {
                try {
                    addHostsToTopology(requestedHostGroupInfo);
                } catch (org.apache.ambari.server.topology.NoSuchHostGroupException e) {
                    throw new org.apache.ambari.server.topology.InvalidTopologyException("Attempted to add hosts to unknown host group: " + hostGroupName);
                }
            } else {
                currentHostGroupInfo.setRequestedCount(currentHostGroupInfo.getRequestedHostCount() + requestedHostGroupInfo.getRequestedHostCount());
            }
        }
    }

    private void addHostsToTopology(org.apache.ambari.server.topology.HostGroupInfo hostGroupInfo) throws org.apache.ambari.server.topology.InvalidTopologyException, org.apache.ambari.server.topology.NoSuchHostGroupException {
        for (java.lang.String host : hostGroupInfo.getHostNames()) {
            registerRackInfo(hostGroupInfo, host);
            addHostToTopology(hostGroupInfo.getHostGroupName(), host);
        }
    }

    private void registerRackInfo(org.apache.ambari.server.topology.HostGroupInfo hostGroupInfo, java.lang.String host) {
        synchronized(hostGroupInfoMap) {
            org.apache.ambari.server.topology.HostGroupInfo cachedHGI = hostGroupInfoMap.get(hostGroupInfo.getHostGroupName());
            if (null != cachedHGI) {
                cachedHGI.addHostRackInfo(host, hostGroupInfo.getHostRackInfo().get(host));
            }
        }
    }

    private void checkForDuplicateHosts(java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> groupInfoMap) throws org.apache.ambari.server.topology.InvalidTopologyException {
        java.util.Set<java.lang.String> hosts = new java.util.HashSet<>();
        java.util.Set<java.lang.String> duplicates = new java.util.HashSet<>();
        for (org.apache.ambari.server.topology.HostGroupInfo group : groupInfoMap.values()) {
            java.util.Collection<java.lang.String> groupHosts = group.getHostNames();
            java.util.Collection<java.lang.String> groupHostsCopy = new java.util.HashSet<>(group.getHostNames());
            groupHostsCopy.retainAll(hosts);
            duplicates.addAll(groupHostsCopy);
            hosts.addAll(groupHosts);
            for (java.lang.String host : groupHosts) {
                if (getHostGroupForHost(host) != null) {
                    duplicates.add(host);
                }
            }
        }
        if (!duplicates.isEmpty()) {
            throw new org.apache.ambari.server.topology.InvalidTopologyException((("The following hosts are mapped to multiple host groups: " + duplicates) + ".") + " Be aware that host names are converted to lowercase, case differences do not matter in Ambari deployments.");
        }
    }
}