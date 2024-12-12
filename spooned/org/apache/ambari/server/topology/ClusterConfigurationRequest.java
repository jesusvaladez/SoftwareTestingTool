package org.apache.ambari.server.topology;
import org.apache.commons.collections.MapUtils;
public class ClusterConfigurationRequest {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.topology.ClusterConfigurationRequest.class);

    private static final java.util.regex.Pattern CLUSTER_HOST_INFO_PATTERN_VARIABLE = java.util.regex.Pattern.compile("\\$\\{clusterHostInfo/?([\\w\\-\\.]+)_host(?:\\s*\\|\\s*(.+?))?\\}");

    private org.apache.ambari.server.topology.AmbariContext ambariContext;

    private org.apache.ambari.server.topology.ClusterTopology clusterTopology;

    private org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configurationProcessor;

    private org.apache.ambari.server.api.services.stackadvisor.StackAdvisorBlueprintProcessor stackAdvisorBlueprintProcessor;

    private org.apache.ambari.server.controller.internal.Stack stack;

    private boolean configureSecurity = false;

    public ClusterConfigurationRequest(org.apache.ambari.server.topology.AmbariContext ambariContext, org.apache.ambari.server.topology.ClusterTopology topology, boolean setInitial, org.apache.ambari.server.api.services.stackadvisor.StackAdvisorBlueprintProcessor stackAdvisorBlueprintProcessor, boolean configureSecurity) {
        this(ambariContext, topology, setInitial, stackAdvisorBlueprintProcessor);
        this.configureSecurity = configureSecurity;
    }

    public ClusterConfigurationRequest(org.apache.ambari.server.topology.AmbariContext ambariContext, org.apache.ambari.server.topology.ClusterTopology clusterTopology, boolean setInitial, org.apache.ambari.server.api.services.stackadvisor.StackAdvisorBlueprintProcessor stackAdvisorBlueprintProcessor) {
        this.ambariContext = ambariContext;
        this.clusterTopology = clusterTopology;
        org.apache.ambari.server.topology.Blueprint blueprint = clusterTopology.getBlueprint();
        this.stack = blueprint.getStack();
        this.configurationProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(clusterTopology);
        this.stackAdvisorBlueprintProcessor = stackAdvisorBlueprintProcessor;
        removeOrphanConfigTypes();
        if (setInitial) {
            setConfigurationsOnCluster(clusterTopology, org.apache.ambari.server.topology.TopologyManager.INITIAL_CONFIG_TAG, java.util.Collections.emptySet());
        }
    }

    private void removeOrphanConfigTypes(org.apache.ambari.server.topology.Configuration configuration) {
        org.apache.ambari.server.topology.Blueprint blueprint = clusterTopology.getBlueprint();
        java.util.Collection<java.lang.String> configTypes = configuration.getAllConfigTypes();
        for (java.lang.String configType : configTypes) {
            if (!blueprint.isValidConfigType(configType)) {
                configuration.removeConfigType(configType);
                org.apache.ambari.server.topology.ClusterConfigurationRequest.LOG.info("Removing config type '{}' as related service is not present in either Blueprint or cluster creation template.", configType);
            }
        }
    }

    private void removeOrphanConfigTypes() {
        org.apache.ambari.server.topology.Configuration configuration = clusterTopology.getConfiguration();
        removeOrphanConfigTypes(configuration);
        java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> hostGroupInfoMap = clusterTopology.getHostGroupInfo();
        if (org.apache.commons.collections.MapUtils.isNotEmpty(hostGroupInfoMap)) {
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> hostGroupInfo : hostGroupInfoMap.entrySet()) {
                configuration = hostGroupInfo.getValue().getConfiguration();
                if (configuration != null) {
                    removeOrphanConfigTypes(configuration);
                }
            }
        }
    }

    public java.util.Collection<java.lang.String> getRequiredHostGroups() {
        java.util.Collection<java.lang.String> requiredHostGroups = new java.util.HashSet<>();
        requiredHostGroups.addAll(configurationProcessor.getRequiredHostGroups());
        if (configureSecurity) {
            requiredHostGroups.addAll(getRequiredHostgroupsForKerberosConfiguration());
        }
        return requiredHostGroups;
    }

    public void process() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.controller.internal.ConfigurationTopologyException {
        java.util.Set<java.lang.String> updatedConfigTypes = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> userProvidedConfigurations = clusterTopology.getConfiguration().getFullProperties(1);
        try {
            if (configureSecurity) {
                org.apache.ambari.server.topology.Configuration clusterConfiguration = clusterTopology.getConfiguration();
                java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> existingConfigurations = clusterConfiguration.getFullProperties();
                updatedConfigTypes.addAll(configureKerberos(clusterConfiguration, existingConfigurations));
            }
            if (!org.apache.ambari.server.topology.ConfigRecommendationStrategy.NEVER_APPLY.equals(this.clusterTopology.getConfigRecommendationStrategy())) {
                stackAdvisorBlueprintProcessor.adviseConfiguration(this.clusterTopology, userProvidedConfigurations);
            }
            updatedConfigTypes.addAll(configurationProcessor.doUpdateForClusterCreate());
        } catch (org.apache.ambari.server.controller.internal.ConfigurationTopologyException e) {
            org.apache.ambari.server.topology.ClusterConfigurationRequest.LOG.error("An exception occurred while doing configuration topology update: " + e, e);
        }
        setConfigurationsOnCluster(clusterTopology, org.apache.ambari.server.topology.TopologyManager.TOPOLOGY_RESOLVED_TAG, updatedConfigTypes);
    }

    private java.util.Set<java.lang.String> configureKerberos(org.apache.ambari.server.topology.Configuration clusterConfiguration, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> existingConfigurations) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Cluster cluster = getCluster();
        org.apache.ambari.server.topology.Blueprint blueprint = clusterTopology.getBlueprint();
        java.util.Set<java.lang.String> services = com.google.common.collect.ImmutableSet.copyOf(blueprint.getServices());
        org.apache.ambari.server.topology.Configuration stackDefaults = blueprint.getStack().getConfiguration(services);
        java.util.Map<java.lang.String, java.lang.String> componentHostsMap = createComponentHostMap(blueprint);
        existingConfigurations.put(org.apache.ambari.server.controller.KerberosHelper.CLUSTER_HOST_INFO, componentHostsMap);
        try {
            org.apache.ambari.server.controller.KerberosHelper kerberosHelper = org.apache.ambari.server.topology.AmbariContext.getController().getKerberosHelper();
            kerberosHelper.ensureHeadlessIdentities(cluster, existingConfigurations, services);
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> updatedConfigs = kerberosHelper.getServiceConfigurationUpdates(cluster, existingConfigurations, createServiceComponentMap(blueprint), null, null, true, false);
            updatedConfigs.computeIfAbsent(org.apache.ambari.server.state.ConfigHelper.CLUSTER_ENV, __ -> new java.util.HashMap<>()).put("security_enabled", "true");
            updatedConfigs.keySet().removeIf(configType -> !blueprint.isValidConfigType(configType));
            return clusterConfiguration.applyUpdatesToStackDefaultProperties(stackDefaults, existingConfigurations, updatedConfigs);
        } catch (org.apache.ambari.server.serveraction.kerberos.KerberosInvalidConfigurationException e) {
            org.apache.ambari.server.topology.ClusterConfigurationRequest.LOG.error("An exception occurred while doing Kerberos related configuration update: " + e, e);
        }
        return com.google.common.collect.ImmutableSet.of();
    }

    private java.util.Map<java.lang.String, java.util.Set<java.lang.String>> createServiceComponentMap(org.apache.ambari.server.topology.Blueprint blueprint) {
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> serviceComponents = new java.util.HashMap<>();
        java.util.Collection<java.lang.String> services = blueprint.getServices();
        if (services != null) {
            for (java.lang.String service : services) {
                java.util.Collection<java.lang.String> components = blueprint.getComponents(service);
                java.util.Set<java.lang.String> componentSet = (components == null) ? com.google.common.collect.ImmutableSet.of() : com.google.common.collect.ImmutableSet.copyOf(components);
                serviceComponents.put(service, componentSet);
            }
        }
        return serviceComponents;
    }

    private java.util.Map<java.lang.String, java.lang.String> createComponentHostMap(org.apache.ambari.server.topology.Blueprint blueprint) {
        return org.apache.ambari.server.utils.StageUtils.createComponentHostMap(blueprint.getServices(), blueprint::getComponents, (service, component) -> clusterTopology.getHostAssignmentsForComponent(component));
    }

    private java.util.Collection<java.lang.String> getRequiredHostgroupsForKerberosConfiguration() {
        java.util.Collection<java.lang.String> requiredHostGroups = new java.util.HashSet<>();
        try {
            org.apache.ambari.server.state.Cluster cluster = getCluster();
            org.apache.ambari.server.topology.Blueprint blueprint = clusterTopology.getBlueprint();
            org.apache.ambari.server.topology.Configuration clusterConfiguration = clusterTopology.getConfiguration();
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> existingConfigurations = clusterConfiguration.getFullProperties();
            existingConfigurations.put(org.apache.ambari.server.controller.KerberosHelper.CLUSTER_HOST_INFO, new java.util.HashMap<>());
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> updatedConfigs = org.apache.ambari.server.topology.AmbariContext.getController().getKerberosHelper().getServiceConfigurationUpdates(cluster, existingConfigurations, createServiceComponentMap(blueprint), null, null, true, false);
            for (java.lang.String configType : updatedConfigs.keySet()) {
                java.util.Map<java.lang.String, java.lang.String> propertyMap = updatedConfigs.get(configType);
                for (java.lang.String property : propertyMap.keySet()) {
                    java.lang.String propertyValue = propertyMap.get(property);
                    java.util.regex.Matcher matcher = org.apache.ambari.server.topology.ClusterConfigurationRequest.CLUSTER_HOST_INFO_PATTERN_VARIABLE.matcher(propertyValue);
                    while (matcher.find()) {
                        java.lang.String component = matcher.group(1).toUpperCase();
                        java.util.Collection<java.lang.String> hostGroups = clusterTopology.getHostGroupsForComponent(component);
                        if (hostGroups.isEmpty()) {
                            org.apache.ambari.server.topology.ClusterConfigurationRequest.LOG.warn("No matching hostgroup found for component: {} specified in Kerberos config type: {} property:" + (" " + "{}"), component, configType, property);
                        } else {
                            requiredHostGroups.addAll(hostGroups);
                        }
                    } 
                }
            }
        } catch (org.apache.ambari.server.serveraction.kerberos.KerberosInvalidConfigurationException | org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.topology.ClusterConfigurationRequest.LOG.error("An exception occurred while doing Kerberos related configuration update: " + e, e);
        }
        return requiredHostGroups;
    }

    private org.apache.ambari.server.state.Cluster getCluster() throws org.apache.ambari.server.AmbariException {
        java.lang.String clusterName = ambariContext.getClusterName(clusterTopology.getClusterId());
        return org.apache.ambari.server.topology.AmbariContext.getController().getClusters().getCluster(clusterName);
    }

    public void setConfigurationsOnCluster(org.apache.ambari.server.topology.ClusterTopology clusterTopology, java.lang.String tag, java.util.Set<java.lang.String> updatedConfigTypes) {
        java.util.List<org.apache.ambari.server.topology.ClusterConfigurationRequest.BlueprintServiceConfigRequest> configurationRequests = new java.util.LinkedList<>();
        org.apache.ambari.server.topology.Blueprint blueprint = clusterTopology.getBlueprint();
        org.apache.ambari.server.topology.Configuration clusterConfiguration = clusterTopology.getConfiguration();
        for (java.lang.String service : blueprint.getServices()) {
            org.apache.ambari.server.topology.ClusterConfigurationRequest.BlueprintServiceConfigRequest blueprintConfigRequest = new org.apache.ambari.server.topology.ClusterConfigurationRequest.BlueprintServiceConfigRequest(service);
            for (java.lang.String serviceConfigType : stack.getAllConfigurationTypes(service)) {
                java.util.Set<java.lang.String> excludedConfigTypes = stack.getExcludedConfigurationTypes(service);
                if (!excludedConfigTypes.contains(serviceConfigType)) {
                    if (!serviceConfigType.equals("cluster-env")) {
                        if (clusterConfiguration.getFullProperties().containsKey(serviceConfigType)) {
                            blueprintConfigRequest.addConfigElement(serviceConfigType, clusterConfiguration.getFullProperties().get(serviceConfigType), clusterConfiguration.getFullAttributes().get(serviceConfigType));
                        }
                    }
                }
            }
            configurationRequests.add(blueprintConfigRequest);
        }
        org.apache.ambari.server.topology.ClusterConfigurationRequest.BlueprintServiceConfigRequest globalConfigRequest = new org.apache.ambari.server.topology.ClusterConfigurationRequest.BlueprintServiceConfigRequest("GLOBAL-CONFIG");
        java.util.Map<java.lang.String, java.lang.String> clusterEnvProps = clusterConfiguration.getFullProperties().get("cluster-env");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> clusterEnvAttributes = clusterConfiguration.getFullAttributes().get("cluster-env");
        globalConfigRequest.addConfigElement("cluster-env", clusterEnvProps, clusterEnvAttributes);
        configurationRequests.add(globalConfigRequest);
        setConfigurationsOnCluster(configurationRequests, tag, updatedConfigTypes);
    }

    private void setConfigurationsOnCluster(java.util.List<org.apache.ambari.server.topology.ClusterConfigurationRequest.BlueprintServiceConfigRequest> configurationRequests, java.lang.String tag, java.util.Set<java.lang.String> updatedConfigTypes) {
        java.lang.String clusterName;
        org.apache.ambari.server.state.Cluster cluster;
        try {
            clusterName = ambariContext.getClusterName(clusterTopology.getClusterId());
            cluster = org.apache.ambari.server.topology.AmbariContext.getController().getClusters().getCluster(clusterName);
        } catch (org.apache.ambari.server.AmbariException e) {
            org.apache.ambari.server.topology.ClusterConfigurationRequest.LOG.error("Cannot get cluster name for clusterId = " + clusterTopology.getClusterId(), e);
            throw new java.lang.RuntimeException(e);
        }
        for (org.apache.ambari.server.topology.ClusterConfigurationRequest.BlueprintServiceConfigRequest blueprintConfigRequest : configurationRequests) {
            org.apache.ambari.server.controller.ClusterRequest clusterRequest = null;
            java.util.List<org.apache.ambari.server.controller.ConfigurationRequest> requestsPerService = new java.util.LinkedList<>();
            for (org.apache.ambari.server.topology.ClusterConfigurationRequest.BlueprintServiceConfigElement blueprintElement : blueprintConfigRequest.getConfigElements()) {
                java.util.Map<java.lang.String, java.lang.Object> clusterProperties = new java.util.HashMap<>();
                clusterProperties.put(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID, clusterName);
                clusterProperties.put(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_DESIRED_CONFIGS_PROPERTY_ID + "/type", blueprintElement.getTypeName());
                clusterProperties.put(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_DESIRED_CONFIGS_PROPERTY_ID + "/tag", tag);
                for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : blueprintElement.getConfiguration().entrySet()) {
                    clusterProperties.put((org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_DESIRED_CONFIGS_PROPERTY_ID + "/properties/") + entry.getKey(), entry.getValue());
                }
                if (blueprintElement.getAttributes() != null) {
                    for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> attribute : blueprintElement.getAttributes().entrySet()) {
                        java.lang.String attributeName = attribute.getKey();
                        for (java.util.Map.Entry<java.lang.String, java.lang.String> attributeOccurrence : attribute.getValue().entrySet()) {
                            clusterProperties.put((((org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_DESIRED_CONFIGS_PROPERTY_ID + "/properties_attributes/") + attributeName) + "/") + attributeOccurrence.getKey(), attributeOccurrence.getValue());
                        }
                    }
                }
                if (clusterRequest == null) {
                    org.apache.ambari.server.state.SecurityType securityType;
                    java.lang.String requestedSecurityType = ((java.lang.String) (clusterProperties.get(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_SECURITY_TYPE_PROPERTY_ID)));
                    if (requestedSecurityType == null)
                        securityType = null;
                    else {
                        try {
                            securityType = org.apache.ambari.server.state.SecurityType.valueOf(requestedSecurityType.toUpperCase());
                        } catch (java.lang.IllegalArgumentException e) {
                            throw new java.lang.IllegalArgumentException(java.lang.String.format("Cannot set cluster security type to invalid value: %s", requestedSecurityType));
                        }
                    }
                    clusterRequest = new org.apache.ambari.server.controller.ClusterRequest(((java.lang.Long) (clusterProperties.get(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_ID_PROPERTY_ID))), ((java.lang.String) (clusterProperties.get(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID))), ((java.lang.String) (clusterProperties.get(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_PROVISIONING_STATE_PROPERTY_ID))), securityType, ((java.lang.String) (clusterProperties.get(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_VERSION_PROPERTY_ID))), null);
                }
                java.util.List<org.apache.ambari.server.controller.ConfigurationRequest> listOfRequests = ambariContext.createConfigurationRequests(clusterProperties);
                requestsPerService.addAll(listOfRequests);
            }
            if (clusterRequest != null) {
                clusterRequest.setDesiredConfig(requestsPerService);
                org.apache.ambari.server.topology.ClusterConfigurationRequest.LOG.info("Sending cluster config update request for service = " + blueprintConfigRequest.getServiceName());
                ambariContext.setConfigurationOnCluster(clusterRequest);
            } else {
                org.apache.ambari.server.topology.ClusterConfigurationRequest.LOG.error("ClusterRequest should not be null for service = " + blueprintConfigRequest.getServiceName());
            }
        }
        cluster.refresh();
        ambariContext.notifyAgentsAboutConfigsChanges(clusterName);
        if (tag.equals(org.apache.ambari.server.topology.TopologyManager.TOPOLOGY_RESOLVED_TAG)) {
            try {
                ambariContext.waitForConfigurationResolution(clusterName, updatedConfigTypes);
            } catch (org.apache.ambari.server.AmbariException e) {
                org.apache.ambari.server.topology.ClusterConfigurationRequest.LOG.error("Error while attempting to wait for the cluster configuration to reach TOPOLOGY_RESOLVED state.", e);
            }
        }
    }

    private static class BlueprintServiceConfigRequest {
        private final java.lang.String serviceName;

        private java.util.List<org.apache.ambari.server.topology.ClusterConfigurationRequest.BlueprintServiceConfigElement> configElements = new java.util.LinkedList<>();

        BlueprintServiceConfigRequest(java.lang.String serviceName) {
            this.serviceName = serviceName;
        }

        void addConfigElement(java.lang.String type, java.util.Map<java.lang.String, java.lang.String> props, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> attributes) {
            if (props == null) {
                props = java.util.Collections.emptyMap();
            }
            if (attributes == null) {
                attributes = java.util.Collections.emptyMap();
            }
            configElements.add(new org.apache.ambari.server.topology.ClusterConfigurationRequest.BlueprintServiceConfigElement(type, props, attributes));
        }

        public java.lang.String getServiceName() {
            return serviceName;
        }

        java.util.List<org.apache.ambari.server.topology.ClusterConfigurationRequest.BlueprintServiceConfigElement> getConfigElements() {
            return configElements;
        }
    }

    private static class BlueprintServiceConfigElement {
        private final java.lang.String typeName;

        private final java.util.Map<java.lang.String, java.lang.String> configuration;

        private final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> attributes;

        BlueprintServiceConfigElement(java.lang.String type, java.util.Map<java.lang.String, java.lang.String> props, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> attributes) {
            this.typeName = type;
            this.configuration = props;
            this.attributes = attributes;
        }

        public java.lang.String getTypeName() {
            return typeName;
        }

        public java.util.Map<java.lang.String, java.lang.String> getConfiguration() {
            return configuration;
        }

        public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getAttributes() {
            return attributes;
        }
    }
}