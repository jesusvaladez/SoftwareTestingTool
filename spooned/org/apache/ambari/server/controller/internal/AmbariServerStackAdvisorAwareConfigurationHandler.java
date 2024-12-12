package org.apache.ambari.server.controller.internal;
class AmbariServerStackAdvisorAwareConfigurationHandler extends org.apache.ambari.server.controller.internal.AmbariServerConfigurationHandler {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.AmbariServerStackAdvisorAwareConfigurationHandler.class);

    private final org.apache.ambari.server.state.Clusters clusters;

    private final org.apache.ambari.server.state.ConfigHelper configHelper;

    private final org.apache.ambari.server.controller.AmbariManagementController managementController;

    private final org.apache.ambari.server.api.services.stackadvisor.StackAdvisorHelper stackAdvisorHelper;

    AmbariServerStackAdvisorAwareConfigurationHandler(org.apache.ambari.server.orm.dao.AmbariConfigurationDAO ambariConfigurationDAO, org.apache.ambari.server.events.publishers.AmbariEventPublisher publisher, org.apache.ambari.server.state.Clusters clusters, org.apache.ambari.server.state.ConfigHelper configHelper, org.apache.ambari.server.controller.AmbariManagementController managementController, org.apache.ambari.server.api.services.stackadvisor.StackAdvisorHelper stackAdvisorHelper) {
        super(ambariConfigurationDAO, publisher);
        this.clusters = clusters;
        this.configHelper = configHelper;
        this.managementController = managementController;
        this.stackAdvisorHelper = stackAdvisorHelper;
    }

    protected void processClusters(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType stackAdvisorRequestType) {
        final java.util.Map<java.lang.String, org.apache.ambari.server.state.Cluster> clusterMap = clusters.getClusters();
        if (clusterMap != null) {
            for (org.apache.ambari.server.state.Cluster cluster : clusterMap.values()) {
                try {
                    org.apache.ambari.server.controller.internal.AmbariServerStackAdvisorAwareConfigurationHandler.LOGGER.info("Managing the {} configuration for the cluster named '{}'", stackAdvisorRequestType.toString(), cluster.getClusterName());
                    processCluster(cluster, stackAdvisorRequestType);
                } catch (org.apache.ambari.server.AmbariException | org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException e) {
                    org.apache.ambari.server.controller.internal.AmbariServerStackAdvisorAwareConfigurationHandler.LOGGER.warn("Failed to update the {} for the cluster named '{}': ", stackAdvisorRequestType.toString(), cluster.getClusterName(), e);
                }
            }
        }
    }

    protected void processCluster(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType stackAdvisorRequestType) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException {
        final org.apache.ambari.server.state.StackId stackVersion = cluster.getCurrentStackVersion();
        final java.util.List<java.lang.String> hosts = cluster.getHosts().stream().map(org.apache.ambari.server.state.Host::getHostName).collect(java.util.stream.Collectors.toList());
        final java.util.Set<java.lang.String> serviceNames = cluster.getServices().values().stream().map(org.apache.ambari.server.state.Service::getName).collect(java.util.stream.Collectors.toSet());
        final org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest request = org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestBuilder.forStack(stackVersion.getStackName(), stackVersion.getStackVersion()).ofType(stackAdvisorRequestType).forHosts(hosts).forServices(serviceNames).withComponentHostsMap(cluster.getServiceComponentHostMap(null, null)).withConfigurations(calculateExistingConfigurations(cluster)).build();
        final org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse response = stackAdvisorHelper.recommend(request);
        final org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.Recommendation recommendation = (response == null) ? null : response.getRecommendations();
        final org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.Blueprint blueprint = (recommendation == null) ? null : recommendation.getBlueprint();
        final java.util.Map<java.lang.String, org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations> configurations = (blueprint == null) ? null : blueprint.getConfigurations();
        if (configurations != null) {
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations> configuration : configurations.entrySet()) {
                processConfigurationType(cluster, configuration.getKey(), configuration.getValue());
            }
        }
    }

    private void processConfigurationType(org.apache.ambari.server.state.Cluster cluster, java.lang.String configType, org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations configurations) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.lang.String> updates = new java.util.HashMap<>();
        java.util.Collection<java.lang.String> removals = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.String> recommendedConfigProperties = configurations.getProperties();
        if (recommendedConfigProperties != null) {
            updates.putAll(recommendedConfigProperties);
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ValueAttributesInfo> recommendedConfigPropertyAttributes = configurations.getPropertyAttributes();
        if (recommendedConfigPropertyAttributes != null) {
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.ValueAttributesInfo> entry : recommendedConfigPropertyAttributes.entrySet()) {
                org.apache.ambari.server.state.ValueAttributesInfo info = entry.getValue();
                if ((info != null) && "true".equalsIgnoreCase(info.getDelete())) {
                    updates.remove(entry.getKey());
                    removals.add(entry.getKey());
                }
            }
        }
        configHelper.updateConfigType(cluster, cluster.getCurrentStackVersion(), managementController, configType, updates, removals, "internal", getServiceVersionNote());
    }

    protected java.lang.String getServiceVersionNote() {
        return "Ambari-managed configuration change";
    }

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> calculateExistingConfigurations(org.apache.ambari.server.state.Cluster cluster) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurationTags = configHelper.getEffectiveDesiredTags(cluster, null);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> effectiveConfigs = configHelper.getEffectiveConfigProperties(cluster, configurationTags);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> requestConfigurations = new java.util.HashMap<>();
        if (effectiveConfigs != null) {
            for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configuration : effectiveConfigs.entrySet()) {
                java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
                java.lang.String configType = configuration.getKey();
                java.util.Map<java.lang.String, java.lang.String> configurationProperties = new java.util.HashMap<>(configuration.getValue());
                properties.put("properties", configurationProperties);
                requestConfigurations.put(configType, properties);
            }
        }
        return requestConfigurations;
    }
}