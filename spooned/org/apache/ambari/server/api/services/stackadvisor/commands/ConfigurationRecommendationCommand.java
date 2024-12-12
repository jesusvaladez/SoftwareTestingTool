package org.apache.ambari.server.api.services.stackadvisor.commands;
import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.JsonNode;
public class ConfigurationRecommendationCommand extends org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommand<org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse> {
    private final org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandType commandType;

    public ConfigurationRecommendationCommand(org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandType commandType, java.io.File recommendationsDir, java.lang.String recommendationsArtifactsLifetime, org.apache.ambari.server.state.ServiceInfo.ServiceAdvisorType serviceAdvisorType, int requestId, org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRunner saRunner, org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo, org.apache.ambari.server.controller.internal.AmbariServerConfigurationHandler ambariServerConfigurationHandler, java.util.Map<java.lang.String, org.codehaus.jackson.JsonNode> hostInfoCache) {
        super(recommendationsDir, recommendationsArtifactsLifetime, serviceAdvisorType, requestId, saRunner, metaInfo, ambariServerConfigurationHandler, hostInfoCache);
        this.commandType = commandType;
    }

    @java.lang.Override
    public org.apache.ambari.server.api.services.stackadvisor.commands.StackAdvisorCommandType getCommandType() {
        return commandType;
    }

    @java.lang.Override
    protected void validate(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest request) throws org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException {
        if (org.apache.commons.collections.CollectionUtils.isEmpty(request.getHosts()) || org.apache.commons.collections.CollectionUtils.isEmpty(request.getServices())) {
            throw new org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException("Hosts and services must not be empty");
        }
    }

    @java.lang.Override
    protected org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse updateResponse(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest request, org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse response) {
        response.getRecommendations().getBlueprint().setHostGroups(processHostGroups(request));
        response.getRecommendations().getBlueprintClusterBinding().setHostGroups(processHostGroupBindings(request));
        return response;
    }

    protected java.util.Set<org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.HostGroup> processHostGroups(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest request) {
        java.util.Set<org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.HostGroup> resultSet = new java.util.HashSet<>();
        for (java.util.Map.Entry<java.lang.String, java.util.Set<java.lang.String>> componentHost : request.getHostComponents().entrySet()) {
            java.lang.String hostGroupName = componentHost.getKey();
            java.util.Set<java.lang.String> components = componentHost.getValue();
            if ((hostGroupName != null) && (components != null)) {
                org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.HostGroup hostGroup = new org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.HostGroup();
                java.util.Set<java.util.Map<java.lang.String, java.lang.String>> componentsSet = new java.util.HashSet<>();
                for (java.lang.String component : components) {
                    java.util.Map<java.lang.String, java.lang.String> componentMap = new java.util.HashMap<>();
                    componentMap.put("name", component);
                    componentsSet.add(componentMap);
                }
                hostGroup.setComponents(componentsSet);
                hostGroup.setName(hostGroupName);
                resultSet.add(hostGroup);
            }
        }
        return resultSet;
    }

    private java.util.Set<org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BindingHostGroup> processHostGroupBindings(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest request) {
        java.util.Set<org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BindingHostGroup> resultSet = new java.util.HashSet<>();
        for (java.util.Map.Entry<java.lang.String, java.util.Set<java.lang.String>> hostBinding : request.getHostGroupBindings().entrySet()) {
            java.lang.String hostGroupName = hostBinding.getKey();
            java.util.Set<java.lang.String> hosts = hostBinding.getValue();
            if ((hostGroupName != null) && (hosts != null)) {
                org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BindingHostGroup bindingHostGroup = new org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BindingHostGroup();
                java.util.Set<java.util.Map<java.lang.String, java.lang.String>> hostsSet = new java.util.HashSet<>();
                for (java.lang.String host : hosts) {
                    java.util.Map<java.lang.String, java.lang.String> hostMap = new java.util.HashMap<>();
                    hostMap.put("name", host);
                    hostsSet.add(hostMap);
                }
                bindingHostGroup.setHosts(hostsSet);
                bindingHostGroup.setName(hostGroupName);
                resultSet.add(bindingHostGroup);
            }
        }
        return resultSet;
    }

    @java.lang.Override
    protected java.lang.String getResultFileName() {
        return "configurations.json";
    }
}