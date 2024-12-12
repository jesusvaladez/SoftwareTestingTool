package org.apache.ambari.server.topology.addservice;
public class StackAdvisorAdapter {
    @javax.inject.Inject
    private org.apache.ambari.server.controller.AmbariManagementController managementController;

    @javax.inject.Inject
    private org.apache.ambari.server.api.services.stackadvisor.StackAdvisorHelper stackAdvisorHelper;

    @javax.inject.Inject
    private org.apache.ambari.server.configuration.Configuration serverConfig;

    @javax.inject.Inject
    private com.google.inject.Injector injector;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorHelper.class);

    org.apache.ambari.server.topology.addservice.AddServiceInfo recommendLayout(org.apache.ambari.server.topology.addservice.AddServiceInfo info) {
        try {
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> allServices = getAllServices(info);
            java.util.Map<java.lang.String, java.util.Set<java.lang.String>> componentsToHosts = org.apache.ambari.server.topology.addservice.StackAdvisorAdapter.getComponentHostMap(allServices);
            java.util.Map<java.lang.String, java.util.Set<java.lang.String>> hostsToComponents = org.apache.ambari.server.topology.addservice.StackAdvisorAdapter.getHostComponentMap(componentsToHosts);
            java.util.List<java.lang.String> hosts = com.google.common.collect.ImmutableList.copyOf(getCluster(info).getHostNames());
            hosts.forEach(host -> hostsToComponents.putIfAbsent(host, new java.util.HashSet<>()));
            java.util.Map<java.lang.String, java.util.Set<java.lang.String>> hostGroups = getHostGroupStrategy().calculateHostGroups(hostsToComponents);
            org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest request = org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestBuilder.forStack(info.getStack().getStackId()).ofType(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType.HOST_GROUPS).forHosts(hosts).forServices(allServices.keySet()).forHostComponents(hostsToComponents).forHostsGroupBindings(hostGroups).withComponentHostsMap(componentsToHosts).withConfigurations(info.getConfig()).withGPLLicenseAccepted(serverConfig.getGplLicenseAccepted()).build();
            org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse response = stackAdvisorHelper.recommend(request);
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> recommendedLayout = org.apache.ambari.server.topology.addservice.StackAdvisorAdapter.getRecommendedLayout(response.getRecommendations().getBlueprintClusterBinding().getHostgroupHostMap(), response.getRecommendations().getBlueprint().getHostgroupComponentMap(), info.getStack()::getServiceForComponent);
            java.util.Map<java.lang.String, java.util.Set<java.lang.String>> recommendedComponentHosts = org.apache.ambari.server.topology.addservice.StackAdvisorAdapter.getComponentHostMap(recommendedLayout);
            org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest validationRequest = request.builder().forHostsGroupBindings(response.getRecommendations().getBlueprintClusterBinding().getHostgroupHostMap()).withComponentHostsMap(recommendedComponentHosts).forHostComponents(org.apache.ambari.server.topology.addservice.StackAdvisorAdapter.getHostComponentMap(recommendedComponentHosts)).build();
            validate(validationRequest);
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> newServiceRecommendations = org.apache.ambari.server.topology.addservice.StackAdvisorAdapter.keepNewServicesOnly(recommendedLayout, info.newServices());
            org.apache.ambari.server.topology.addservice.LayoutRecommendationInfo recommendationInfo = new org.apache.ambari.server.topology.addservice.LayoutRecommendationInfo(response.getRecommendations().getBlueprintClusterBinding().getHostgroupHostMap(), recommendedLayout);
            return info.withLayoutRecommendation(newServiceRecommendations, recommendationInfo);
        } catch (org.apache.ambari.server.AmbariException | org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException ex) {
            throw new java.lang.IllegalArgumentException("Layout recommendation failed.", ex);
        }
    }

    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> getAllServices(org.apache.ambari.server.topology.addservice.AddServiceInfo info) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.Cluster cluster = managementController.getClusters().getCluster(info.clusterName());
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> clusterServices = com.google.common.collect.Maps.transformValues(cluster.getServices(), service -> com.google.common.collect.Maps.transformValues(service.getServiceComponents(), component -> component.getServiceComponentsHosts()));
        return org.apache.ambari.server.topology.addservice.StackAdvisorAdapter.mergeDisjunctMaps(clusterServices, info.newServices());
    }

    private org.apache.ambari.server.state.Cluster getCluster(org.apache.ambari.server.topology.addservice.AddServiceInfo info) throws org.apache.ambari.server.AmbariException {
        return managementController.getClusters().getCluster(info.clusterName());
    }

    org.apache.ambari.server.topology.addservice.AddServiceInfo recommendConfigurations(org.apache.ambari.server.topology.addservice.AddServiceInfo info) {
        org.apache.ambari.server.topology.Configuration config = info.getConfig();
        if (info.getRequest().getRecommendationStrategy().shouldUseStackAdvisor()) {
            org.apache.ambari.server.topology.addservice.LayoutRecommendationInfo layoutInfo = getLayoutRecommendationInfo(info);
            java.util.Map<java.lang.String, java.util.Set<java.lang.String>> componentHostMap = org.apache.ambari.server.topology.addservice.StackAdvisorAdapter.getComponentHostMap(layoutInfo.getAllServiceLayouts());
            java.util.Map<java.lang.String, java.util.Set<java.lang.String>> hostComponentMap = org.apache.ambari.server.topology.addservice.StackAdvisorAdapter.getHostComponentMap(componentHostMap);
            org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest request = org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestBuilder.forStack(info.getStack().getStackId()).ofType(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType.CONFIGURATIONS).forHosts(layoutInfo.getHosts()).forServices(layoutInfo.getAllServiceLayouts().keySet()).forHostComponents(hostComponentMap).forHostsGroupBindings(layoutInfo.getHostGroups()).withComponentHostsMap(componentHostMap).withConfigurations(config).withGPLLicenseAccepted(serverConfig.getGplLicenseAccepted()).build();
            org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse response;
            try {
                response = stackAdvisorHelper.recommend(request);
            } catch (org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException | org.apache.ambari.server.AmbariException ex) {
                throw new java.lang.IllegalArgumentException("Configuration recommendation failed.", ex);
            }
            java.util.Map<java.lang.String, org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations> configRecommendations = response.getRecommendations().getBlueprint().getConfigurations();
            configRecommendations.keySet().removeIf(configType -> !info.newServices().containsKey(info.getStack().getServiceForConfigType(configType)));
            if (info.getRequest().getRecommendationStrategy() == org.apache.ambari.server.topology.ConfigRecommendationStrategy.ONLY_STACK_DEFAULTS_APPLY) {
                org.apache.ambari.server.topology.addservice.StackAdvisorAdapter.removeNonStackConfigRecommendations(info.getConfig().getParentConfiguration().getParentConfiguration(), configRecommendations);
            }
            org.apache.ambari.server.topology.Configuration recommendedConfig = org.apache.ambari.server.topology.addservice.StackAdvisorAdapter.toConfiguration(configRecommendations);
            org.apache.ambari.server.topology.Configuration userConfig = config;
            org.apache.ambari.server.topology.Configuration clusterAndStackConfig = userConfig.getParentConfiguration();
            if (info.getRequest().getRecommendationStrategy().shouldOverrideCustomValues()) {
                config = recommendedConfig;
                config.setParentConfiguration(userConfig);
            } else {
                config.setParentConfiguration(recommendedConfig);
                recommendedConfig.setParentConfiguration(clusterAndStackConfig);
            }
            org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest validationRequest = request.builder().withConfigurations(config).build();
            validate(validationRequest);
        }
        org.apache.ambari.server.controller.internal.UnitUpdater.updateUnits(config, info.getStack());
        return info.withConfig(config);
    }

    org.apache.ambari.server.topology.addservice.LayoutRecommendationInfo getLayoutRecommendationInfo(org.apache.ambari.server.topology.addservice.AddServiceInfo info) {
        if (info.getRecommendationInfo().isPresent()) {
            return info.getRecommendationInfo().get();
        }
        try {
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> allServices = getAllServices(info);
            java.util.Map<java.lang.String, java.util.Set<java.lang.String>> hostGroups = getHostGroupStrategy().calculateHostGroups(org.apache.ambari.server.topology.addservice.StackAdvisorAdapter.getHostComponentMap(org.apache.ambari.server.topology.addservice.StackAdvisorAdapter.getComponentHostMap(allServices)));
            return new org.apache.ambari.server.topology.addservice.LayoutRecommendationInfo(hostGroups, allServices);
        } catch (org.apache.ambari.server.AmbariException ex) {
            throw new java.lang.IllegalArgumentException("Error gathering host groups and services", ex);
        }
    }

    static void removeNonStackConfigRecommendations(org.apache.ambari.server.topology.Configuration stackConfig, java.util.Map<java.lang.String, org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations> configRecommendations) {
        configRecommendations.keySet().removeIf(configType -> !stackConfig.containsConfigType(configType));
        configRecommendations.entrySet().forEach(e -> {
            java.lang.String cfgType = e.getKey();
            org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations cfg = e.getValue();
            cfg.getProperties().keySet().removeIf(propName -> !stackConfig.containsConfig(cfgType, propName));
            if (null != cfg.getPropertyAttributes()) {
                cfg.getPropertyAttributes().keySet().removeIf(propName -> !stackConfig.containsConfig(cfgType, propName));
            }
        });
        configRecommendations.values().removeIf(cfg -> cfg.getProperties().isEmpty() && cfg.getPropertyAttributes().isEmpty());
    }

    private void validate(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest request) {
        try {
            java.util.Set<org.apache.ambari.server.api.services.stackadvisor.validations.ValidationResponse.ValidationItem> items = stackAdvisorHelper.validate(request).getItems();
            if (!items.isEmpty()) {
                org.apache.ambari.server.topology.addservice.StackAdvisorAdapter.LOG.warn("Issues found during recommended {} validation:\n{}", request.getRequestType(), com.google.common.base.Joiner.on('\n').join(items));
            }
        } catch (org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException ex) {
            org.apache.ambari.server.topology.addservice.StackAdvisorAdapter.LOG.error(request.getRequestType() + " validation failed", ex);
        }
    }

    static org.apache.ambari.server.topology.Configuration toConfiguration(java.util.Map<java.lang.String, org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations> configs) {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = configs.entrySet().stream().filter(e -> (e.getValue().getProperties() != null) && (!e.getValue().getProperties().isEmpty())).map(e -> org.apache.commons.lang3.tuple.Pair.of(e.getKey(), e.getValue().getProperties())).collect(java.util.stream.Collectors.toMap(org.apache.commons.lang3.tuple.Pair::getKey, org.apache.commons.lang3.tuple.Pair::getValue));
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> propertyAttributes = configs.entrySet().stream().filter(e -> (e.getValue().getPropertyAttributes() != null) && (!e.getValue().getPropertyAttributes().isEmpty())).map(e -> org.apache.commons.lang3.tuple.Pair.of(e.getKey(), e.getValue().getPropertyAttributesAsMap())).collect(java.util.stream.Collectors.toMap(org.apache.commons.lang3.tuple.Pair::getKey, org.apache.commons.lang3.tuple.Pair::getValue));
        return new org.apache.ambari.server.topology.Configuration(properties, propertyAttributes);
    }

    static java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> keepNewServicesOnly(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> recommendedLayout, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> newServices) {
        java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> newServiceRecommendations = new java.util.HashMap<>(recommendedLayout);
        newServiceRecommendations.keySet().retainAll(newServices.keySet());
        return newServiceRecommendations;
    }

    static java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> getRecommendedLayout(java.util.Map<java.lang.String, java.util.Set<java.lang.String>> hostGroupHosts, java.util.Map<java.lang.String, java.util.Set<java.lang.String>> hostGroupComponents, java.util.function.Function<java.lang.String, java.lang.String> componentToService) {
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> componentHostMap = hostGroupComponents.entrySet().stream().flatMap(entry -> entry.getValue().stream().map(comp -> org.apache.commons.lang3.tuple.Pair.of(comp, entry.getKey()))).flatMap(cmpHg -> hostGroupHosts.get(cmpHg.getValue()).stream().map(host -> org.apache.commons.lang3.tuple.Pair.of(cmpHg.getKey(), host))).collect(java.util.stream.Collectors.groupingBy(org.apache.commons.lang3.tuple.Pair::getKey, java.util.stream.Collectors.mapping(org.apache.commons.lang3.tuple.Pair::getValue, java.util.stream.Collectors.toSet())));
        return componentHostMap.entrySet().stream().collect(java.util.stream.Collectors.groupingBy(cmpHost -> componentToService.apply(cmpHost.getKey()), java.util.stream.Collectors.toMap(java.util.Map.Entry::getKey, java.util.Map.Entry::getValue)));
    }

    static java.util.Map<java.lang.String, java.util.Set<java.lang.String>> getHostComponentMap(java.util.Map<java.lang.String, java.util.Set<java.lang.String>> componentHostMap) {
        return componentHostMap.entrySet().stream().flatMap(compHosts -> compHosts.getValue().stream().map(host -> org.apache.commons.lang3.tuple.Pair.of(host, compHosts.getKey()))).collect(java.util.stream.Collectors.groupingBy(org.apache.commons.lang3.tuple.Pair::getKey, java.util.stream.Collectors.mapping(org.apache.commons.lang3.tuple.Pair::getValue, java.util.stream.Collectors.toSet())));
    }

    static java.util.Map<java.lang.String, java.util.Set<java.lang.String>> getComponentHostMap(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Set<java.lang.String>>> serviceComponentHostMap) {
        return serviceComponentHostMap.values().stream().reduce(org.apache.ambari.server.topology.addservice.StackAdvisorAdapter::mergeDisjunctMaps).orElse(new java.util.HashMap<>());
    }

    static <S, T> java.util.Map<S, T> mergeDisjunctMaps(java.util.Map<? extends S, ? extends T> map1, java.util.Map<? extends S, ? extends T> map2) {
        com.google.common.collect.Sets.SetView<? extends S> commonKeys = com.google.common.collect.Sets.intersection(map1.keySet(), map2.keySet());
        com.google.common.base.Preconditions.checkArgument(commonKeys.isEmpty(), "Maps must be disjunct. Common keys: %s", commonKeys);
        java.util.Map<S, T> merged = new java.util.HashMap<>(map1);
        merged.putAll(map2);
        return merged;
    }

    org.apache.ambari.server.topology.addservice.HostGroupStrategy getHostGroupStrategy() {
        try {
            return injector.getInstance(serverConfig.getAddServiceHostGroupStrategyClass());
        } catch (java.lang.ClassNotFoundException | java.lang.ClassCastException | com.google.inject.ConfigurationException | com.google.inject.ProvisionException ex) {
            throw new java.lang.IllegalStateException("Cannot load host group strategy", ex);
        }
    }
}