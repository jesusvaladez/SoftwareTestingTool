package org.apache.ambari.server.api.services.stackadvisor.recommendations;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
public class RecommendationResponse extends org.apache.ambari.server.api.services.stackadvisor.StackAdvisorResponse {
    @org.codehaus.jackson.annotate.JsonProperty
    private java.util.Set<java.lang.String> hosts;

    @org.codehaus.jackson.annotate.JsonProperty
    private java.util.Set<java.lang.String> services;

    @org.codehaus.jackson.annotate.JsonProperty
    private org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.Recommendation recommendations;

    public java.util.Set<java.lang.String> getHosts() {
        return hosts;
    }

    public void setHosts(java.util.Set<java.lang.String> hosts) {
        this.hosts = hosts;
    }

    public java.util.Set<java.lang.String> getServices() {
        return services;
    }

    public void setServices(java.util.Set<java.lang.String> services) {
        this.services = services;
    }

    public org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.Recommendation getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.Recommendation recommendations) {
        this.recommendations = recommendations;
    }

    public static class Recommendation {
        @org.codehaus.jackson.annotate.JsonProperty
        private org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.Blueprint blueprint;

        @org.codehaus.jackson.annotate.JsonProperty("blueprint_cluster_binding")
        private org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintClusterBinding blueprintClusterBinding;

        @org.codehaus.jackson.annotate.JsonProperty("config-groups")
        @org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
        private java.util.Set<org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.ConfigGroup> configGroups;

        public org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.Blueprint getBlueprint() {
            return blueprint;
        }

        public void setBlueprint(org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.Blueprint blueprint) {
            this.blueprint = blueprint;
        }

        public org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintClusterBinding getBlueprintClusterBinding() {
            return blueprintClusterBinding;
        }

        public void setBlueprintClusterBinding(org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintClusterBinding blueprintClusterBinding) {
            this.blueprintClusterBinding = blueprintClusterBinding;
        }

        public java.util.Set<org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.ConfigGroup> getConfigGroups() {
            return configGroups;
        }

        public void setConfigGroups(java.util.Set<org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.ConfigGroup> configGroups) {
            this.configGroups = configGroups;
        }
    }

    public static class Blueprint {
        @org.codehaus.jackson.annotate.JsonProperty
        private java.util.Map<java.lang.String, org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations> configurations;

        @org.codehaus.jackson.annotate.JsonProperty("host_groups")
        private java.util.Set<org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.HostGroup> hostGroups;

        public java.util.Map<java.lang.String, org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations> getConfigurations() {
            return configurations;
        }

        public void setConfigurations(java.util.Map<java.lang.String, org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations> configurations) {
            this.configurations = configurations;
        }

        public java.util.Set<org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.HostGroup> getHostGroups() {
            return hostGroups;
        }

        public void setHostGroups(java.util.Set<org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.HostGroup> hostGroups) {
            this.hostGroups = hostGroups;
        }

        public java.util.Map<java.lang.String, java.util.Set<java.lang.String>> getHostgroupComponentMap() {
            return hostGroups.stream().flatMap(hg -> hg.getComponentNames().stream().map(comp -> org.apache.commons.lang3.tuple.Pair.of(hg.getName(), comp))).collect(java.util.stream.Collectors.groupingBy(org.apache.commons.lang3.tuple.Pair::getKey, java.util.stream.Collectors.mapping(org.apache.commons.lang3.tuple.Pair::getValue, java.util.stream.Collectors.toSet())));
        }
    }

    public static class BlueprintConfigurations {
        @org.codehaus.jackson.annotate.JsonProperty
        private final java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();

        @org.codehaus.jackson.annotate.JsonProperty("property_attributes")
        @org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
        private java.util.Map<java.lang.String, org.apache.ambari.server.state.ValueAttributesInfo> propertyAttributes = null;

        public static org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations create(java.util.Map<java.lang.String, java.lang.String> properties, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> attributes) {
            org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations config = new org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations();
            config.setProperties(properties);
            if (attributes != null) {
                java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> transformedAttributes = org.apache.ambari.server.topology.ConfigurableHelper.transformAttributesMap(attributes);
                org.codehaus.jackson.map.ObjectMapper mapper = new org.codehaus.jackson.map.ObjectMapper();
                config.setPropertyAttributes(new java.util.HashMap<>(com.google.common.collect.Maps.transformValues(transformedAttributes, attr -> org.apache.ambari.server.state.ValueAttributesInfo.fromMap(attr, java.util.Optional.of(mapper)))));
            }
            return config;
        }

        public BlueprintConfigurations() {
        }

        public java.util.Map<java.lang.String, java.lang.String> getProperties() {
            return properties;
        }

        public void setProperties(java.util.Map<java.lang.String, java.lang.String> properties) {
            this.properties.clear();
            if (properties != null) {
                this.properties.putAll(properties);
            }
        }

        public java.util.Map<java.lang.String, org.apache.ambari.server.state.ValueAttributesInfo> getPropertyAttributes() {
            return propertyAttributes;
        }

        @org.codehaus.jackson.annotate.JsonIgnore
        public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getPropertyAttributesAsMap() {
            org.codehaus.jackson.map.ObjectMapper mapper = new org.codehaus.jackson.map.ObjectMapper();
            return null == propertyAttributes ? null : org.apache.ambari.server.topology.ConfigurableHelper.transformAttributesMap(com.google.common.collect.Maps.transformValues(propertyAttributes, vaInfo -> vaInfo.toMap(java.util.Optional.of(mapper))));
        }

        public void setPropertyAttributes(java.util.Map<java.lang.String, org.apache.ambari.server.state.ValueAttributesInfo> propertyAttributes) {
            this.propertyAttributes = propertyAttributes;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o)
                return true;

            if ((o == null) || (getClass() != o.getClass()))
                return false;

            org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations that = ((org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations) (o));
            return java.util.Objects.equals(properties, that.properties) && java.util.Objects.equals(propertyAttributes, that.propertyAttributes);
        }

        @java.lang.Override
        public int hashCode() {
            return java.util.Objects.hash(properties, propertyAttributes);
        }
    }

    public static class HostGroup {
        @org.codehaus.jackson.annotate.JsonProperty
        private java.lang.String name;

        @org.codehaus.jackson.annotate.JsonProperty
        private java.util.Set<java.util.Map<java.lang.String, java.lang.String>> components;

        public java.lang.String getName() {
            return name;
        }

        public void setName(java.lang.String name) {
            this.name = name;
        }

        public java.util.Set<java.util.Map<java.lang.String, java.lang.String>> getComponents() {
            return components;
        }

        public void setComponents(java.util.Set<java.util.Map<java.lang.String, java.lang.String>> components) {
            this.components = components;
        }

        @org.codehaus.jackson.annotate.JsonIgnore
        public java.util.Set<java.lang.String> getComponentNames() {
            return components.stream().map(comp -> comp.get("name")).collect(java.util.stream.Collectors.toSet());
        }

        public static java.util.Set<org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.HostGroup> fromHostGroupComponents(java.util.Map<java.lang.String, java.util.Set<java.lang.String>> hostGroupComponents) {
            return hostGroupComponents.entrySet().stream().map(entry -> org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.HostGroup.create(entry.getKey(), entry.getValue())).collect(java.util.stream.Collectors.toSet());
        }

        public static org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.HostGroup create(java.lang.String name, java.util.Set<java.lang.String> componentNames) {
            org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.HostGroup group = new org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.HostGroup();
            group.setName(name);
            java.util.Set<java.util.Map<java.lang.String, java.lang.String>> components = componentNames.stream().map(comp -> com.google.common.collect.ImmutableMap.of("name", comp)).collect(java.util.stream.Collectors.toSet());
            group.setComponents(components);
            return group;
        }
    }

    public static class BlueprintClusterBinding {
        @org.codehaus.jackson.annotate.JsonProperty("host_groups")
        private java.util.Set<org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BindingHostGroup> hostGroups;

        public java.util.Set<org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BindingHostGroup> getHostGroups() {
            return hostGroups;
        }

        public void setHostGroups(java.util.Set<org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BindingHostGroup> hostGroups) {
            this.hostGroups = hostGroups;
        }

        @org.codehaus.jackson.annotate.JsonIgnore
        public java.util.Map<java.lang.String, java.util.Set<java.lang.String>> getHostgroupHostMap() {
            return hostGroups.stream().collect(java.util.stream.Collectors.toMap(org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BindingHostGroup::getName, org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BindingHostGroup::getHostNames));
        }

        public static org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintClusterBinding fromHostGroupHostMap(java.util.Map<java.lang.String, java.util.Set<java.lang.String>> hostGroupHosts) {
            java.util.Set<org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BindingHostGroup> hostGroups = hostGroupHosts.entrySet().stream().map(entry -> org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BindingHostGroup.create(entry.getKey(), entry.getValue())).collect(java.util.stream.Collectors.toSet());
            org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintClusterBinding binding = new org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintClusterBinding();
            binding.setHostGroups(hostGroups);
            return binding;
        }
    }

    public static class BindingHostGroup {
        @org.codehaus.jackson.annotate.JsonProperty
        private java.lang.String name;

        @org.codehaus.jackson.annotate.JsonProperty
        private java.util.Set<java.util.Map<java.lang.String, java.lang.String>> hosts;

        public java.lang.String getName() {
            return name;
        }

        public void setName(java.lang.String name) {
            this.name = name;
        }

        public java.util.Set<java.util.Map<java.lang.String, java.lang.String>> getHosts() {
            return hosts;
        }

        public void setHosts(java.util.Set<java.util.Map<java.lang.String, java.lang.String>> hosts) {
            this.hosts = hosts;
        }

        @org.codehaus.jackson.annotate.JsonIgnore
        public java.util.Set<java.lang.String> getHostNames() {
            return hosts.stream().map(host -> host.get("fqdn")).collect(java.util.stream.Collectors.toSet());
        }

        public static org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BindingHostGroup create(java.lang.String name, java.util.Set<java.lang.String> hostNames) {
            org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BindingHostGroup hostGroup = new org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BindingHostGroup();
            hostGroup.setName(name);
            java.util.Set<java.util.Map<java.lang.String, java.lang.String>> hosts = hostNames.stream().map(hostName -> com.google.common.collect.ImmutableMap.of("fqdn", hostName)).collect(java.util.stream.Collectors.toSet());
            hostGroup.setHosts(hosts);
            return hostGroup;
        }
    }

    public static class ConfigGroup {
        @org.codehaus.jackson.annotate.JsonProperty
        private java.util.List<java.lang.String> hosts;

        @org.codehaus.jackson.annotate.JsonProperty
        @org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
        private java.util.Map<java.lang.String, org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations> configurations = new java.util.HashMap<>();

        @org.codehaus.jackson.annotate.JsonProperty("dependent_configurations")
        @org.codehaus.jackson.map.annotate.JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
        private java.util.Map<java.lang.String, org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations> dependentConfigurations = new java.util.HashMap<>();

        public ConfigGroup() {
        }

        public java.util.List<java.lang.String> getHosts() {
            return hosts;
        }

        public void setHosts(java.util.List<java.lang.String> hosts) {
            this.hosts = hosts;
        }

        public java.util.Map<java.lang.String, org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations> getConfigurations() {
            return configurations;
        }

        public void setConfigurations(java.util.Map<java.lang.String, org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations> configurations) {
            this.configurations = configurations;
        }

        public java.util.Map<java.lang.String, org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations> getDependentConfigurations() {
            return dependentConfigurations;
        }

        public void setDependentConfigurations(java.util.Map<java.lang.String, org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations> dependentConfigurations) {
            this.dependentConfigurations = dependentConfigurations;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o)
                return true;

            if ((o == null) || (getClass() != o.getClass()))
                return false;

            org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.ConfigGroup that = ((org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.ConfigGroup) (o));
            return (java.util.Objects.equals(hosts, that.hosts) && java.util.Objects.equals(configurations, that.configurations)) && java.util.Objects.equals(dependentConfigurations, that.dependentConfigurations);
        }

        @java.lang.Override
        public int hashCode() {
            return java.util.Objects.hash(hosts, configurations, dependentConfigurations);
        }
    }
}