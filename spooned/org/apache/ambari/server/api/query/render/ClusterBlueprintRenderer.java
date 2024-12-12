package org.apache.ambari.server.api.query.render;
public class ClusterBlueprintRenderer extends org.apache.ambari.server.api.query.render.BaseRenderer implements org.apache.ambari.server.api.query.render.Renderer {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer.class);

    static final java.lang.String SERVICE_SETTINGS = "service_settings";

    static final java.lang.String COMPONENT_SETTINGS = "component_settings";

    static final java.lang.String CREDENTIAL_STORE_ENABLED = "credential_store_enabled";

    static final java.lang.String RECOVERY_ENABLED = "recovery_enabled";

    static final java.lang.String TRUE = java.lang.Boolean.TRUE.toString();

    static final java.lang.String FALSE = java.lang.Boolean.FALSE.toString();

    private final org.apache.ambari.server.controller.AmbariManagementController controller = org.apache.ambari.server.controller.AmbariServer.getController();

    private final org.apache.ambari.server.controller.internal.BlueprintExportType exportType;

    public ClusterBlueprintRenderer(org.apache.ambari.server.controller.internal.BlueprintExportType exportType) {
        this.exportType = exportType;
    }

    @java.lang.Override
    public org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> finalizeProperties(org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.api.query.QueryInfo> queryProperties, boolean isCollection) {
        java.util.Set<java.lang.String> properties = new java.util.HashSet<>(queryProperties.getObject().getProperties());
        org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> resultTree = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, properties, queryProperties.getName());
        copyPropertiesToResult(queryProperties, resultTree);
        java.lang.String configType = org.apache.ambari.server.controller.spi.Resource.Type.Configuration.name();
        if (resultTree.getChild(configType) == null) {
            resultTree.addChild(new java.util.HashSet<>(), configType);
        }
        java.lang.String serviceType = org.apache.ambari.server.controller.spi.Resource.Type.Service.name();
        if (resultTree.getChild(serviceType) == null) {
            resultTree.addChild(new java.util.HashSet<>(), serviceType);
        }
        org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> serviceNode = resultTree.getChild(serviceType);
        if (serviceNode == null) {
            serviceNode = resultTree.addChild(new java.util.HashSet<>(), serviceType);
        }
        java.lang.String serviceComponentType = org.apache.ambari.server.controller.spi.Resource.Type.Component.name();
        org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> serviceComponentNode = resultTree.getChild((serviceType + "/") + serviceComponentType);
        if (serviceComponentNode == null) {
            serviceComponentNode = serviceNode.addChild(new java.util.HashSet<>(), serviceComponentType);
        }
        serviceComponentNode.getObject().add("ServiceComponentInfo/cluster_name");
        serviceComponentNode.getObject().add("ServiceComponentInfo/service_name");
        serviceComponentNode.getObject().add("ServiceComponentInfo/component_name");
        serviceComponentNode.getObject().add("ServiceComponentInfo/recovery_enabled");
        java.lang.String hostType = org.apache.ambari.server.controller.spi.Resource.Type.Host.name();
        java.lang.String hostComponentType = org.apache.ambari.server.controller.spi.Resource.Type.HostComponent.name();
        org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> hostComponentNode = resultTree.getChild((hostType + "/") + hostComponentType);
        if (hostComponentNode == null) {
            org.apache.ambari.server.api.util.TreeNode<java.util.Set<java.lang.String>> hostNode = resultTree.getChild(hostType);
            if (hostNode == null) {
                hostNode = resultTree.addChild(new java.util.HashSet<>(), hostType);
            }
            hostComponentNode = hostNode.addChild(new java.util.HashSet<>(), hostComponentType);
        }
        resultTree.getChild(configType).getObject().add("properties");
        hostComponentNode.getObject().add("HostRoles/component_name");
        return resultTree;
    }

    @java.lang.Override
    public org.apache.ambari.server.api.services.Result finalizeResult(org.apache.ambari.server.api.services.Result queryResult) {
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultTree = queryResult.getResultTree();
        org.apache.ambari.server.api.services.Result result = new org.apache.ambari.server.api.services.ResultImpl(true);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> blueprintResultTree = result.getResultTree();
        if (isCollection(resultTree)) {
            blueprintResultTree.setProperty("isCollection", "true");
        }
        for (org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> node : resultTree.getChildren()) {
            org.apache.ambari.server.controller.spi.Resource blueprintResource = createBlueprintResource(node);
            blueprintResultTree.addChild(new org.apache.ambari.server.api.util.TreeNodeImpl<>(blueprintResultTree, blueprintResource, node.getName()));
        }
        return result;
    }

    @java.lang.Override
    public org.apache.ambari.server.api.services.ResultPostProcessor getResultPostProcessor(org.apache.ambari.server.api.services.Request request) {
        return new org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer.BlueprintPostProcessor(request);
    }

    @java.lang.Override
    public boolean requiresPropertyProviderInput() {
        return false;
    }

    private org.apache.ambari.server.controller.spi.Resource createBlueprintResource(org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> clusterNode) {
        org.apache.ambari.server.controller.spi.Resource blueprintResource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Cluster);
        org.apache.ambari.server.topology.ClusterTopology topology;
        try {
            topology = createClusterTopology(clusterNode);
        } catch (org.apache.ambari.server.topology.InvalidTopologyTemplateException | org.apache.ambari.server.topology.InvalidTopologyException e) {
            throw new java.lang.RuntimeException("Unable to process blueprint export request: " + e, e);
        }
        org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor configProcessor = new org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor(topology);
        configProcessor.doUpdateForBlueprintExport(exportType);
        org.apache.ambari.server.controller.internal.Stack stack = topology.getBlueprint().getStack();
        blueprintResource.setProperty("Blueprints/stack_name", stack.getName());
        blueprintResource.setProperty("Blueprints/stack_version", stack.getVersion());
        if (topology.isClusterKerberosEnabled()) {
            java.util.Map<java.lang.String, java.lang.Object> securityConfigMap = new java.util.LinkedHashMap<>();
            securityConfigMap.put(org.apache.ambari.server.topology.SecurityConfigurationFactory.TYPE_PROPERTY_ID, org.apache.ambari.server.state.SecurityType.KERBEROS.name());
            try {
                java.lang.String clusterName = topology.getAmbariContext().getClusterName(topology.getClusterId());
                java.util.Map<java.lang.String, java.lang.Object> kerberosDescriptor = org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer.getKerberosDescriptor(org.apache.ambari.server.topology.AmbariContext.getClusterController(), clusterName);
                if (kerberosDescriptor != null) {
                    securityConfigMap.put(org.apache.ambari.server.topology.SecurityConfigurationFactory.KERBEROS_DESCRIPTOR_PROPERTY_ID, kerberosDescriptor);
                }
            } catch (org.apache.ambari.server.AmbariException e) {
                org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer.LOG.info("Unable to retrieve kerberos_descriptor: ", e.getMessage());
            }
            blueprintResource.setProperty(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.BLUEPRINT_SECURITY_PROPERTY_ID, securityConfigMap);
        }
        java.util.List<java.util.Map<java.lang.String, java.lang.Object>> groupList = formatGroupsAsList(topology);
        blueprintResource.setProperty("host_groups", groupList);
        java.util.List<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, ?>>>> configurations = processConfigurations(topology);
        if (exportType.include(configurations)) {
            blueprintResource.setProperty("configurations", configurations);
        }
        java.util.Collection<java.util.Map<java.lang.String, java.lang.Object>> settings = getSettings(clusterNode, stack);
        if (exportType.include(settings)) {
            blueprintResource.setProperty("settings", settings);
        }
        return blueprintResource;
    }

    @com.google.common.annotations.VisibleForTesting
    java.util.Collection<java.util.Map<java.lang.String, java.lang.Object>> getSettings(org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> clusterNode, org.apache.ambari.server.controller.internal.Stack stack) {
        java.util.Collection<java.util.Map<java.lang.String, java.lang.Object>> blueprintSetting = new java.util.ArrayList<>();
        java.util.Set<java.util.Map<java.lang.String, java.lang.String>> serviceSettings = new java.util.HashSet<>();
        java.util.Set<java.util.Map<java.lang.String, java.lang.String>> componentSettings = new java.util.HashSet<>();
        java.util.Collection<org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource>> serviceChildren = clusterNode.getChild("services").getChildren();
        for (org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> serviceNode : serviceChildren) {
            org.apache.ambari.server.controller.internal.ResourceImpl service = ((org.apache.ambari.server.controller.internal.ResourceImpl) (serviceNode.getObject()));
            java.util.Map<java.lang.String, java.lang.Object> serviceInfoMap = service.getPropertiesMap().get("ServiceInfo");
            java.util.Map<java.lang.String, java.lang.String> serviceSetting = new java.util.HashMap<>();
            java.lang.String serviceName = serviceInfoMap.get("service_name").toString();
            org.apache.ambari.server.state.ServiceInfo serviceInfo = stack.getServiceInfo(serviceName).orElseThrow(java.lang.IllegalStateException::new);
            boolean serviceDefaultCredentialStoreEnabled = serviceInfo.isCredentialStoreEnabled();
            java.lang.String credentialStoreEnabledString = java.lang.String.valueOf(serviceInfoMap.get(org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer.CREDENTIAL_STORE_ENABLED));
            boolean credentialStoreEnabled = java.lang.Boolean.parseBoolean(credentialStoreEnabledString);
            if (credentialStoreEnabled != serviceDefaultCredentialStoreEnabled) {
                serviceSetting.put(org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer.CREDENTIAL_STORE_ENABLED, credentialStoreEnabledString);
                serviceSetting.put("name", serviceName);
                serviceSettings.add(serviceSetting);
            }
            java.util.Collection<org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource>> componentChildren = serviceNode.getChild("components").getChildren();
            for (org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> componentNode : componentChildren) {
                org.apache.ambari.server.controller.internal.ResourceImpl component = ((org.apache.ambari.server.controller.internal.ResourceImpl) (componentNode.getObject()));
                java.util.Map<java.lang.String, java.lang.Object> serviceComponentInfoMap = component.getPropertiesMap().get("ServiceComponentInfo");
                java.lang.String componentName = serviceComponentInfoMap.get("component_name").toString();
                boolean componentDefaultRecoveryEnabled = serviceInfo.getComponentByName(componentName).isRecoveryEnabled();
                if (serviceComponentInfoMap.containsKey(org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer.RECOVERY_ENABLED)) {
                    java.lang.String recoveryEnabledString = java.lang.String.valueOf(serviceComponentInfoMap.get(org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer.RECOVERY_ENABLED));
                    boolean recoveryEnabled = java.lang.Boolean.parseBoolean(recoveryEnabledString);
                    if (recoveryEnabled != componentDefaultRecoveryEnabled) {
                        componentSettings.add(com.google.common.collect.ImmutableMap.of("name", componentName, org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer.RECOVERY_ENABLED, recoveryEnabledString));
                    }
                }
            }
        }
        if (exportType.include(serviceSettings)) {
            blueprintSetting.add(com.google.common.collect.ImmutableMap.of(org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer.SERVICE_SETTINGS, serviceSettings));
        }
        if (exportType.include(componentSettings)) {
            blueprintSetting.add(com.google.common.collect.ImmutableMap.of(org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer.COMPONENT_SETTINGS, componentSettings));
        }
        return blueprintSetting;
    }

    private static java.util.Map<java.lang.String, java.lang.Object> getKerberosDescriptor(org.apache.ambari.server.controller.spi.ClusterController clusterController, java.lang.String clusterName) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.controller.spi.Predicate predicate = org.apache.ambari.server.topology.addservice.ResourceProviderAdapter.predicateForKerberosDescriptorArtifact(clusterName);
        org.apache.ambari.server.controller.spi.ResourceProvider artifactProvider = clusterController.ensureResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type.Artifact);
        org.apache.ambari.server.controller.spi.Request request = new org.apache.ambari.server.controller.internal.RequestImpl(java.util.Collections.emptySet(), java.util.Collections.emptySet(), java.util.Collections.emptyMap(), null);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> response;
        try {
            response = artifactProvider.getResources(request, predicate);
        } catch (org.apache.ambari.server.controller.spi.SystemException | org.apache.ambari.server.controller.spi.UnsupportedPropertyException | org.apache.ambari.server.controller.spi.NoSuchResourceException | org.apache.ambari.server.controller.spi.NoSuchParentResourceException e) {
            throw new org.apache.ambari.server.AmbariException("An unknown error occurred while trying to obtain the cluster kerberos descriptor", e);
        }
        if ((response != null) && (!response.isEmpty())) {
            org.apache.ambari.server.controller.spi.Resource descriptorResource = response.iterator().next();
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> propertyMap = descriptorResource.getPropertiesMap();
            if (propertyMap != null) {
                java.util.Map<java.lang.String, java.lang.Object> artifactData = propertyMap.get(org.apache.ambari.server.controller.internal.ArtifactResourceProvider.ARTIFACT_DATA_PROPERTY);
                java.util.Map<java.lang.String, java.lang.Object> artifactDataProperties = propertyMap.get(org.apache.ambari.server.controller.internal.ArtifactResourceProvider.ARTIFACT_DATA_PROPERTY + "/properties");
                java.util.HashMap<java.lang.String, java.lang.Object> data = new java.util.HashMap<>();
                if (artifactData != null) {
                    data.putAll(artifactData);
                }
                if (artifactDataProperties != null) {
                    data.put("properties", artifactDataProperties);
                }
                return data;
            }
        }
        return null;
    }

    private java.util.List<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, ?>>>> processConfigurations(org.apache.ambari.server.topology.ClusterTopology topology) {
        java.util.List<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, ?>>>> configList = new java.util.ArrayList<>();
        org.apache.ambari.server.topology.Configuration configuration = topology.getConfiguration();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> fullProperties = configuration.getFullProperties();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> fullAttributes = configuration.getFullAttributes();
        java.util.Collection<java.lang.String> allTypes = com.google.common.collect.ImmutableSet.<java.lang.String>builder().addAll(fullProperties.keySet()).addAll(fullAttributes.keySet()).build();
        for (java.lang.String type : allTypes) {
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, ?>> typeMap = new java.util.HashMap<>();
            java.util.Map<java.lang.String, java.lang.String> properties = fullProperties.get(type);
            if (exportType.include(properties)) {
                typeMap.put("properties", properties);
            }
            if (!fullAttributes.isEmpty()) {
                java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> attributes = fullAttributes.get(type);
                if (exportType.include(attributes)) {
                    typeMap.put("properties_attributes", attributes);
                }
            }
            configList.add(java.util.Collections.singletonMap(type, typeMap));
        }
        return configList;
    }

    private java.util.List<java.util.Map<java.lang.String, java.lang.Object>> formatGroupsAsList(org.apache.ambari.server.topology.ClusterTopology topology) {
        java.util.List<java.util.Map<java.lang.String, java.lang.Object>> listHostGroups = new java.util.ArrayList<>();
        for (org.apache.ambari.server.topology.HostGroupInfo group : topology.getHostGroupInfo().values()) {
            java.util.Map<java.lang.String, java.lang.Object> mapGroupProperties = new java.util.HashMap<>();
            listHostGroups.add(mapGroupProperties);
            java.lang.String name = group.getHostGroupName();
            mapGroupProperties.put("name", name);
            mapGroupProperties.put("cardinality", java.lang.String.valueOf(group.getHostNames().size()));
            mapGroupProperties.put("components", processHostGroupComponents(topology.getBlueprint().getHostGroup(name)));
            org.apache.ambari.server.topology.Configuration configuration = topology.getHostGroupInfo().get(name).getConfiguration();
            java.util.List<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> configList = new java.util.ArrayList<>();
            for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> typeEntry : configuration.getProperties().entrySet()) {
                java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> propertyMap = java.util.Collections.singletonMap(typeEntry.getKey(), typeEntry.getValue());
                configList.add(propertyMap);
            }
            if (exportType.include(configList)) {
                mapGroupProperties.put("configurations", configList);
            }
        }
        return listHostGroups;
    }

    private java.util.List<java.util.Map<java.lang.String, java.lang.String>> processHostGroupComponents(org.apache.ambari.server.topology.HostGroup group) {
        java.util.List<java.util.Map<java.lang.String, java.lang.String>> listHostGroupComponents = new java.util.ArrayList<>();
        for (org.apache.ambari.server.topology.Component component : group.getComponents()) {
            java.util.Map<java.lang.String, java.lang.String> mapComponentProperties = new java.util.HashMap<>();
            listHostGroupComponents.add(mapComponentProperties);
            mapComponentProperties.put("name", component.getName());
        }
        return listHostGroupComponents;
    }

    protected org.apache.ambari.server.topology.ClusterTopology createClusterTopology(org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> clusterNode) throws org.apache.ambari.server.topology.InvalidTopologyTemplateException, org.apache.ambari.server.topology.InvalidTopologyException {
        return new org.apache.ambari.server.topology.ClusterTopologyImpl(new org.apache.ambari.server.topology.AmbariContext(), new org.apache.ambari.server.controller.internal.ExportBlueprintRequest(clusterNode, controller));
    }

    private boolean isCollection(org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> node) {
        java.lang.String isCollection = node.getStringProperty("isCollection");
        return (isCollection != null) && isCollection.equals("true");
    }

    protected org.apache.ambari.server.controller.AmbariManagementController getController() {
        return controller;
    }

    private static class BlueprintPostProcessor extends org.apache.ambari.server.api.services.ResultPostProcessorImpl {
        private BlueprintPostProcessor(org.apache.ambari.server.api.services.Request request) {
            super(request);
        }

        @java.lang.Override
        protected void finalizeNode(org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> node) {
            node.removeProperty("href");
        }
    }
}