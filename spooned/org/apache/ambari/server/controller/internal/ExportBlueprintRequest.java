package org.apache.ambari.server.controller.internal;
public class ExportBlueprintRequest implements org.apache.ambari.server.topology.TopologyRequest {
    private final org.apache.ambari.server.controller.AmbariManagementController controller;

    private final java.lang.String clusterName;

    private final java.lang.Long clusterId;

    private org.apache.ambari.server.topology.Blueprint blueprint;

    private final org.apache.ambari.server.topology.Configuration configuration;

    private final java.util.Map<java.lang.String, org.apache.ambari.server.topology.HostGroupInfo> hostGroupInfo = new java.util.HashMap<>();

    public ExportBlueprintRequest(org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> clusterNode, org.apache.ambari.server.controller.AmbariManagementController controller) throws org.apache.ambari.server.topology.InvalidTopologyTemplateException {
        this.controller = controller;
        org.apache.ambari.server.controller.spi.Resource clusterResource = clusterNode.getObject();
        org.apache.ambari.server.controller.internal.Stack stack = parseStack(clusterResource);
        clusterName = java.lang.String.valueOf(clusterResource.getPropertyValue(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_NAME_PROPERTY_ID));
        clusterId = java.lang.Long.valueOf(java.lang.String.valueOf(clusterResource.getPropertyValue(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_ID_PROPERTY_ID)));
        configuration = org.apache.ambari.server.controller.internal.ExportBlueprintRequest.createConfiguration(clusterNode);
        java.util.Collection<org.apache.ambari.server.controller.internal.ExportBlueprintRequest.ExportedHostGroup> exportedHostGroups = processHostGroups(clusterNode.getChild("hosts"));
        createHostGroupInfo(exportedHostGroups);
        createBlueprint(exportedHostGroups, stack);
    }

    public java.lang.String getClusterName() {
        return clusterName;
    }

    @java.lang.Override
    public java.lang.Long getClusterId() {
        return clusterId;
    }

    @java.lang.Override
    public org.apache.ambari.server.topology.TopologyRequest.Type getType() {
        return org.apache.ambari.server.topology.TopologyRequest.Type.EXPORT;
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
        return hostGroupInfo;
    }

    @java.lang.Override
    public java.lang.String getDescription() {
        return java.lang.String.format("Export Command For Cluster '%s'", clusterName);
    }

    private void createBlueprint(java.util.Collection<org.apache.ambari.server.controller.internal.ExportBlueprintRequest.ExportedHostGroup> exportedHostGroups, org.apache.ambari.server.controller.internal.Stack stack) {
        java.lang.String bpName = "exported-blueprint";
        java.util.Collection<org.apache.ambari.server.topology.HostGroup> hostGroups = new java.util.ArrayList<>();
        for (org.apache.ambari.server.controller.internal.ExportBlueprintRequest.ExportedHostGroup exportedHostGroup : exportedHostGroups) {
            java.util.List<org.apache.ambari.server.topology.Component> componentList = new java.util.ArrayList<>();
            for (java.lang.String component : exportedHostGroup.getComponents()) {
                componentList.add(new org.apache.ambari.server.topology.Component(component));
            }
            hostGroups.add(new org.apache.ambari.server.topology.HostGroupImpl(exportedHostGroup.getName(), bpName, stack, componentList, exportedHostGroup.getConfiguration(), java.lang.String.valueOf(exportedHostGroup.getCardinality())));
        }
        blueprint = new org.apache.ambari.server.topology.BlueprintImpl(bpName, hostGroups, stack, configuration, null);
    }

    private void createHostGroupInfo(java.util.Collection<org.apache.ambari.server.controller.internal.ExportBlueprintRequest.ExportedHostGroup> exportedHostGroups) {
        for (org.apache.ambari.server.controller.internal.ExportBlueprintRequest.ExportedHostGroup exportedGroup : exportedHostGroups) {
            org.apache.ambari.server.topology.HostGroupInfo groupInfo = new org.apache.ambari.server.topology.HostGroupInfo(exportedGroup.getName());
            groupInfo.addHosts(exportedGroup.getHostInfo());
            groupInfo.setConfiguration(exportedGroup.getConfiguration());
            hostGroupInfo.put(groupInfo.getHostGroupName(), groupInfo);
        }
    }

    private org.apache.ambari.server.controller.internal.Stack parseStack(org.apache.ambari.server.controller.spi.Resource clusterResource) throws org.apache.ambari.server.topology.InvalidTopologyTemplateException {
        java.lang.String[] stackTokens = java.lang.String.valueOf(clusterResource.getPropertyValue(org.apache.ambari.server.controller.internal.ClusterResourceProvider.CLUSTER_VERSION_PROPERTY_ID)).split("-");
        try {
            return new org.apache.ambari.server.controller.internal.Stack(stackTokens[0], stackTokens[1], controller);
        } catch (org.apache.ambari.server.AmbariException e) {
            throw new org.apache.ambari.server.topology.InvalidTopologyTemplateException(java.lang.String.format("The specified stack doesn't exist: name=%s version=%s", stackTokens[0], stackTokens[1]));
        }
    }

    private static org.apache.ambari.server.topology.Configuration createConfiguration(org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> clusterNode) {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> attributes = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.Object> desiredConfigMap = clusterNode.getObject().getPropertiesMap().get("Clusters/desired_configs");
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> configNode = clusterNode.getChild("configurations");
        for (org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> config : configNode.getChildren()) {
            org.apache.ambari.server.controller.internal.ExportBlueprintRequest.ExportedConfiguration configuration = new org.apache.ambari.server.controller.internal.ExportBlueprintRequest.ExportedConfiguration(config);
            org.apache.ambari.server.state.DesiredConfig desiredConfig = ((org.apache.ambari.server.state.DesiredConfig) (desiredConfigMap.get(configuration.getType())));
            if ((desiredConfig != null) && desiredConfig.getTag().equals(configuration.getTag())) {
                properties.put(configuration.getType(), configuration.getProperties());
                attributes.put(configuration.getType(), configuration.getPropertyAttributes());
            }
        }
        org.apache.ambari.server.topology.Configuration configuration = new org.apache.ambari.server.topology.Configuration(properties, attributes);
        configuration.setParentConfiguration(new org.apache.ambari.server.topology.Configuration(java.util.Collections.emptyMap(), java.util.Collections.emptyMap()));
        return configuration;
    }

    private java.util.Collection<org.apache.ambari.server.controller.internal.ExportBlueprintRequest.ExportedHostGroup> processHostGroups(org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> hostNode) {
        java.util.Map<org.apache.ambari.server.controller.internal.ExportBlueprintRequest.ExportedHostGroup, org.apache.ambari.server.controller.internal.ExportBlueprintRequest.ExportedHostGroup> mapHostGroups = new java.util.HashMap<>();
        int count = 1;
        for (org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> host : hostNode.getChildren()) {
            org.apache.ambari.server.controller.internal.ExportBlueprintRequest.ExportedHostGroup group = new org.apache.ambari.server.controller.internal.ExportBlueprintRequest.ExportedHostGroup(host);
            java.lang.String hostName = ((java.lang.String) (host.getObject().getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name"))));
            if (mapHostGroups.containsKey(group)) {
                org.apache.ambari.server.controller.internal.ExportBlueprintRequest.ExportedHostGroup hostGroup = mapHostGroups.get(group);
                hostGroup.incrementCardinality();
                hostGroup.addHost(hostName);
            } else {
                mapHostGroups.put(group, group);
                group.setName("host_group_" + (count++));
                group.addHost(hostName);
            }
        }
        return mapHostGroups.values();
    }

    public class ExportedHostGroup {
        private java.lang.String name;

        private final java.util.Set<java.lang.String> components = new java.util.HashSet<>();

        private final java.util.Collection<org.apache.ambari.server.controller.internal.ExportBlueprintRequest.ExportedConfiguration> configurations = new java.util.HashSet<>();

        private int m_cardinality = 1;

        private final java.util.Collection<java.lang.String> hosts = new java.util.HashSet<>();

        public ExportedHostGroup(org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> host) {
            org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> components = host.getChild("host_components");
            for (org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> component : components.getChildren()) {
                getComponents().add(((java.lang.String) (component.getObject().getPropertyValue("HostRoles/component_name"))));
            }
            addAmbariComponentIfLocalhost(((java.lang.String) (host.getObject().getPropertyValue(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Hosts", "host_name")))));
            processGroupConfiguration(host);
        }

        public org.apache.ambari.server.topology.Configuration getConfiguration() {
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configProperties = new java.util.HashMap<>();
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> configAttributes = new java.util.HashMap<>();
            for (org.apache.ambari.server.controller.internal.ExportBlueprintRequest.ExportedConfiguration config : configurations) {
                configProperties.put(config.getType(), config.getProperties());
                configAttributes.put(config.getType(), config.getPropertyAttributes());
            }
            return new org.apache.ambari.server.topology.Configuration(configProperties, configAttributes);
        }

        private void processGroupConfiguration(org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> host) {
            java.util.Map<java.lang.String, java.lang.Object> desiredConfigMap = host.getObject().getPropertiesMap().get("Hosts/desired_configs");
            if (desiredConfigMap != null) {
                for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : desiredConfigMap.entrySet()) {
                    java.lang.String type = entry.getKey();
                    org.apache.ambari.server.state.HostConfig hostConfig = ((org.apache.ambari.server.state.HostConfig) (entry.getValue()));
                    java.util.Map<java.lang.Long, java.lang.String> overrides = hostConfig.getConfigGroupOverrides();
                    if ((overrides != null) && (!overrides.isEmpty())) {
                        java.lang.Long version = java.util.Collections.max(overrides.keySet());
                        java.lang.String tag = overrides.get(version);
                        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> clusterNode = host.getParent().getParent();
                        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> configNode = clusterNode.getChild("configurations");
                        for (org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> config : configNode.getChildren()) {
                            org.apache.ambari.server.controller.internal.ExportBlueprintRequest.ExportedConfiguration configuration = new org.apache.ambari.server.controller.internal.ExportBlueprintRequest.ExportedConfiguration(config);
                            if (type.equals(configuration.getType()) && tag.equals(configuration.getTag())) {
                                getConfigurations().add(configuration);
                                break;
                            }
                        }
                    }
                }
            }
        }

        public java.lang.String getName() {
            return name;
        }

        public java.util.Set<java.lang.String> getComponents() {
            return components;
        }

        public java.util.Collection<java.lang.String> getHostInfo() {
            return hosts;
        }

        public void setName(java.lang.String name) {
            this.name = name;
        }

        public void addHost(java.lang.String host) {
            hosts.add(host);
        }

        public java.util.Collection<org.apache.ambari.server.controller.internal.ExportBlueprintRequest.ExportedConfiguration> getConfigurations() {
            return configurations;
        }

        public int getCardinality() {
            return m_cardinality;
        }

        public void incrementCardinality() {
            m_cardinality += 1;
        }

        private void addAmbariComponentIfLocalhost(java.lang.String hostname) {
            try {
                java.net.InetAddress hostAddress = java.net.InetAddress.getByName(hostname);
                try {
                    if (hostAddress.equals(java.net.InetAddress.getLocalHost())) {
                        getComponents().add("AMBARI_SERVER");
                    }
                } catch (java.net.UnknownHostException e) {
                    throw new java.lang.RuntimeException("Unable to obtain local host name", e);
                }
            } catch (java.net.UnknownHostException e) {
            }
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o)
                return true;

            if ((o == null) || (getClass() != o.getClass()))
                return false;

            org.apache.ambari.server.controller.internal.ExportBlueprintRequest.ExportedHostGroup hostGroup = ((org.apache.ambari.server.controller.internal.ExportBlueprintRequest.ExportedHostGroup) (o));
            return components.equals(hostGroup.components) && configurations.equals(hostGroup.configurations);
        }

        @java.lang.Override
        public int hashCode() {
            int result = components.hashCode();
            result = (31 * result) + configurations.hashCode();
            return result;
        }
    }

    private static class ExportedConfiguration {
        private java.lang.String type;

        private java.lang.String tag;

        private java.util.Map<java.lang.String, java.lang.String> properties = new java.util.HashMap<>();

        private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> propertyAttributes = new java.util.HashMap<>();

        @java.lang.SuppressWarnings("unchecked")
        public ExportedConfiguration(org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> configNode) {
            org.apache.ambari.server.controller.spi.Resource configResource = configNode.getObject();
            type = ((java.lang.String) (configResource.getPropertyValue("type")));
            tag = ((java.lang.String) (configResource.getPropertyValue("tag")));
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> propertiesMap = configNode.getObject().getPropertiesMap();
            if (propertiesMap.containsKey("properties")) {
                properties = ((java.util.Map) (propertiesMap.get("properties")));
            }
            if (propertiesMap.containsKey("properties_attributes")) {
                propertyAttributes = ((java.util.Map) (propertiesMap.get("properties_attributes")));
            }
        }

        public java.lang.String getType() {
            return type;
        }

        public java.lang.String getTag() {
            return tag;
        }

        public java.util.Map<java.lang.String, java.lang.String> getProperties() {
            return properties;
        }

        public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getPropertyAttributes() {
            return propertyAttributes;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o)
                return true;

            if ((o == null) || (getClass() != o.getClass()))
                return false;

            org.apache.ambari.server.controller.internal.ExportBlueprintRequest.ExportedConfiguration that = ((org.apache.ambari.server.controller.internal.ExportBlueprintRequest.ExportedConfiguration) (o));
            return ((tag.equals(that.tag) && type.equals(that.type)) && properties.equals(that.properties)) && propertyAttributes.equals(that.propertyAttributes);
        }

        @java.lang.Override
        public int hashCode() {
            int result = type.hashCode();
            result = (31 * result) + tag.hashCode();
            result = (31 * result) + properties.hashCode();
            result = (31 * result) + propertyAttributes.hashCode();
            return result;
        }
    }
}