package org.apache.ambari.server.api.services.stackadvisor;
@com.google.inject.Singleton
public class StackAdvisorBlueprintProcessor {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorBlueprintProcessor.class);

    private static org.apache.ambari.server.api.services.stackadvisor.StackAdvisorHelper stackAdvisorHelper;

    static final java.lang.String RECOMMENDATION_FAILED = "Configuration recommendation failed.";

    static final java.lang.String INVALID_RESPONSE = "Configuration recommendation returned with invalid response.";

    public static void init(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorHelper instance) {
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorBlueprintProcessor.stackAdvisorHelper = instance;
    }

    private static final java.util.Map<java.lang.String, java.lang.String> userContext;

    static {
        userContext = new java.util.HashMap<>();
        userContext.put("operation", "ClusterCreate");
    }

    public void adviseConfiguration(org.apache.ambari.server.topology.ClusterTopology clusterTopology, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> userProvidedConfigurations) throws org.apache.ambari.server.controller.internal.ConfigurationTopologyException {
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest request = createStackAdvisorRequest(clusterTopology, org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType.CONFIGURATIONS);
        try {
            org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse response = org.apache.ambari.server.api.services.stackadvisor.StackAdvisorBlueprintProcessor.stackAdvisorHelper.recommend(request);
            addAdvisedConfigurationsToTopology(response, clusterTopology, userProvidedConfigurations);
        } catch (org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException | org.apache.ambari.server.AmbariException e) {
            throw new org.apache.ambari.server.controller.internal.ConfigurationTopologyException(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorBlueprintProcessor.RECOMMENDATION_FAILED, e);
        } catch (java.lang.IllegalArgumentException e) {
            throw new org.apache.ambari.server.controller.internal.ConfigurationTopologyException(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorBlueprintProcessor.INVALID_RESPONSE, e);
        }
    }

    private org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest createStackAdvisorRequest(org.apache.ambari.server.topology.ClusterTopology clusterTopology, org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType requestType) {
        org.apache.ambari.server.controller.internal.Stack stack = clusterTopology.getBlueprint().getStack();
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> hgComponentsMap = gatherHostGroupComponents(clusterTopology);
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> hgHostsMap = gatherHostGroupBindings(clusterTopology);
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> componentHostsMap = gatherComponentsHostsMap(hgComponentsMap, hgHostsMap);
        return org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestBuilder.forStack(stack.getName(), stack.getVersion()).forServices(new java.util.ArrayList<>(clusterTopology.getBlueprint().getServices())).forHosts(gatherHosts(clusterTopology)).forHostsGroupBindings(gatherHostGroupBindings(clusterTopology)).forHostComponents(gatherHostGroupComponents(clusterTopology)).withComponentHostsMap(componentHostsMap).withConfigurations(calculateConfigs(clusterTopology)).withUserContext(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorBlueprintProcessor.userContext).ofType(requestType).build();
    }

    private java.util.Map<java.lang.String, java.util.Set<java.lang.String>> gatherHostGroupBindings(org.apache.ambari.server.topology.ClusterTopology clusterTopology) {
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> hgBindngs = com.google.common.collect.Maps.newHashMap();
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> hgEnrty : clusterTopology.getHostGroupInfo().entrySet()) {
            hgBindngs.put(hgEnrty.getKey(), com.google.common.collect.Sets.newCopyOnWriteArraySet(hgEnrty.getValue().getHostNames()));
        }
        return hgBindngs;
    }

    private java.util.Map<java.lang.String, java.util.Set<java.lang.String>> gatherHostGroupComponents(org.apache.ambari.server.topology.ClusterTopology clusterTopology) {
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> hgComponentsMap = com.google.common.collect.Maps.newHashMap();
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.topology.HostGroup> hgEnrty : clusterTopology.getBlueprint().getHostGroups().entrySet()) {
            hgComponentsMap.put(hgEnrty.getKey(), com.google.common.collect.Sets.newCopyOnWriteArraySet(hgEnrty.getValue().getComponentNames()));
        }
        return hgComponentsMap;
    }

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> calculateConfigs(org.apache.ambari.server.topology.ClusterTopology clusterTopology) {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> result = com.google.common.collect.Maps.newHashMap();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> fullProperties = clusterTopology.getConfiguration().getFullProperties();
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> siteEntry : fullProperties.entrySet()) {
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> propsMap = com.google.common.collect.Maps.newHashMap();
            propsMap.put("properties", siteEntry.getValue());
            result.put(siteEntry.getKey(), propsMap);
        }
        return result;
    }

    private java.util.Map<java.lang.String, java.util.Set<java.lang.String>> gatherComponentsHostsMap(java.util.Map<java.lang.String, java.util.Set<java.lang.String>> hostGroups, java.util.Map<java.lang.String, java.util.Set<java.lang.String>> bindingHostGroups) {
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> componentHostsMap = new java.util.HashMap<>();
        if ((null != bindingHostGroups) && (null != hostGroups)) {
            for (java.util.Map.Entry<java.lang.String, java.util.Set<java.lang.String>> hgComponents : hostGroups.entrySet()) {
                java.lang.String hgName = hgComponents.getKey();
                java.util.Set<java.lang.String> components = hgComponents.getValue();
                java.util.Set<java.lang.String> hosts = bindingHostGroups.get(hgName);
                if (hosts != null) {
                    for (java.lang.String component : components) {
                        java.util.Set<java.lang.String> componentHosts = componentHostsMap.get(component);
                        if (componentHosts == null) {
                            componentHosts = new java.util.HashSet<>();
                            componentHostsMap.put(component, componentHosts);
                        }
                        componentHosts.addAll(hosts);
                    }
                }
            }
        }
        return componentHostsMap;
    }

    private java.util.List<java.lang.String> gatherHosts(org.apache.ambari.server.topology.ClusterTopology clusterTopology) {
        java.util.List<java.lang.String> hosts = com.google.common.collect.Lists.newArrayList();
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> entry : clusterTopology.getHostGroupInfo().entrySet()) {
            hosts.addAll(entry.getValue().getHostNames());
        }
        return hosts;
    }

    private void addAdvisedConfigurationsToTopology(org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse response, org.apache.ambari.server.topology.ClusterTopology topology, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> userProvidedConfigurations) {
        com.google.common.base.Preconditions.checkArgument(response.getRecommendations() != null, "Recommendation response is empty.");
        com.google.common.base.Preconditions.checkArgument(response.getRecommendations().getBlueprint() != null, "Blueprint field is missing from the recommendation response.");
        com.google.common.base.Preconditions.checkArgument(response.getRecommendations().getBlueprint().getConfigurations() != null, "Configurations are missing from the recommendation blueprint response.");
        java.util.Map<java.lang.String, org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations> recommendedConfigurations = response.getRecommendations().getBlueprint().getConfigurations();
        org.apache.ambari.server.topology.Blueprint blueprint = topology.getBlueprint();
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations> configEntry : recommendedConfigurations.entrySet()) {
            java.lang.String configType = configEntry.getKey();
            if (blueprint.isValidConfigType(configType)) {
                org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations blueprintConfig = filterBlueprintConfig(configType, configEntry.getValue(), userProvidedConfigurations, topology);
                topology.getAdvisedConfigurations().put(configType, new org.apache.ambari.server.topology.AdvisedConfiguration(blueprintConfig.getProperties(), blueprintConfig.getPropertyAttributes()));
            }
        }
    }

    private org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations filterBlueprintConfig(java.lang.String configType, org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations config, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> userProvidedConfigurations, org.apache.ambari.server.topology.ClusterTopology topology) {
        if ((topology.getConfigRecommendationStrategy() == org.apache.ambari.server.topology.ConfigRecommendationStrategy.ONLY_STACK_DEFAULTS_APPLY) || (topology.getConfigRecommendationStrategy() == org.apache.ambari.server.topology.ConfigRecommendationStrategy.ALWAYS_APPLY_DONT_OVERRIDE_CUSTOM_VALUES)) {
            if (userProvidedConfigurations.containsKey(configType)) {
                org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations newConfig = new org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations();
                java.util.Map<java.lang.String, java.lang.String> filteredProps = com.google.common.collect.Maps.filterKeys(config.getProperties(), com.google.common.base.Predicates.not(com.google.common.base.Predicates.in(userProvidedConfigurations.get(configType).keySet())));
                newConfig.setProperties(com.google.common.collect.Maps.newHashMap(filteredProps));
                if (config.getPropertyAttributes() != null) {
                    java.util.Map<java.lang.String, org.apache.ambari.server.state.ValueAttributesInfo> filteredAttributes = com.google.common.collect.Maps.filterKeys(config.getPropertyAttributes(), com.google.common.base.Predicates.not(com.google.common.base.Predicates.in(userProvidedConfigurations.get(configType).keySet())));
                    newConfig.setPropertyAttributes(com.google.common.collect.Maps.newHashMap(filteredAttributes));
                }
                return newConfig;
            }
        }
        return config;
    }
}