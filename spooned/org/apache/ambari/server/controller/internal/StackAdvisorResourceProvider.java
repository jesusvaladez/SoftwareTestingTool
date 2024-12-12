package org.apache.ambari.server.controller.internal;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
public abstract class StackAdvisorResourceProvider extends org.apache.ambari.server.controller.internal.ReadOnlyResourceProvider {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.class);

    protected static final java.lang.String STACK_NAME_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Versions", "stack_name");

    protected static final java.lang.String STACK_VERSION_PROPERTY_ID = org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("Versions", "stack_version");

    private static final java.lang.String CLUSTER_ID_PROPERTY = "clusterId";

    private static final java.lang.String SERVICE_NAME_PROPERTY = "serviceName";

    private static final java.lang.String AUTO_COMPLETE_PROPERTY = "autoComplete";

    private static final java.lang.String CONFIGS_RESPONSE_PROPERTY = "configsResponse";

    private static final java.lang.String CONFIG_GROUPS_GROUP_ID_PROPERTY = "group_id";

    private static final java.lang.String HOST_PROPERTY = "hosts";

    private static final java.lang.String SERVICES_PROPERTY = "services";

    private static final java.lang.String CHANGED_CONFIGURATIONS_PROPERTY = "changed_configurations";

    private static final java.lang.String OPERATION_PROPERTY = "operation";

    private static final java.lang.String OPERATION_DETAILS_PROPERTY = "operation_details";

    private static final java.lang.String BLUEPRINT_HOST_GROUPS_PROPERTY = "recommendations/blueprint/host_groups";

    private static final java.lang.String BINDING_HOST_GROUPS_PROPERTY = "recommendations/blueprint_cluster_binding/host_groups";

    private static final java.lang.String BLUEPRINT_HOST_GROUPS_NAME_PROPERTY = "name";

    private static final java.lang.String BLUEPRINT_HOST_GROUPS_COMPONENTS_PROPERTY = "components";

    private static final java.lang.String BLUEPRINT_HOST_GROUPS_COMPONENTS_NAME_PROPERTY = "name";

    private static final java.lang.String BINDING_HOST_GROUPS_NAME_PROPERTY = "name";

    private static final java.lang.String BINDING_HOST_GROUPS_HOSTS_PROPERTY = "hosts";

    private static final java.lang.String BINDING_HOST_GROUPS_HOSTS_NAME_PROPERTY = "fqdn";

    private static final java.lang.String CONFIG_GROUPS_PROPERTY = "recommendations/config_groups";

    private static final java.lang.String CONFIG_GROUPS_CONFIGURATIONS_PROPERTY = "configurations";

    private static final java.lang.String CONFIG_GROUPS_HOSTS_PROPERTY = "hosts";

    protected static org.apache.ambari.server.api.services.stackadvisor.StackAdvisorHelper saHelper;

    private static org.apache.ambari.server.configuration.Configuration configuration;

    private static org.apache.ambari.server.state.Clusters clusters;

    private static org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo;

    protected static final java.lang.String USER_CONTEXT_OPERATION_PROPERTY = "user_context/operation";

    protected static final java.lang.String USER_CONTEXT_OPERATION_DETAILS_PROPERTY = "user_context/operation_details";

    @com.google.inject.Inject
    public static void init(org.apache.ambari.server.api.services.stackadvisor.StackAdvisorHelper instance, org.apache.ambari.server.configuration.Configuration serverConfig, org.apache.ambari.server.state.Clusters clusters, org.apache.ambari.server.api.services.AmbariMetaInfo ambariMetaInfo) {
        org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.saHelper = instance;
        org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.configuration = serverConfig;
        org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.clusters = clusters;
        org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.ambariMetaInfo = ambariMetaInfo;
    }

    protected StackAdvisorResourceProvider(org.apache.ambari.server.controller.spi.Resource.Type type, java.util.Set<java.lang.String> propertyIds, java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> keyPropertyIds, org.apache.ambari.server.controller.AmbariManagementController managementController) {
        super(type, propertyIds, keyPropertyIds, managementController);
    }

    protected abstract java.lang.String getRequestTypePropertyId();

    @java.lang.SuppressWarnings("unchecked")
    protected org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest prepareStackAdvisorRequest(org.apache.ambari.server.controller.spi.Request request) {
        try {
            java.lang.String clusterIdProperty = ((java.lang.String) (getRequestProperty(request, org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.CLUSTER_ID_PROPERTY)));
            java.lang.Long clusterId = (clusterIdProperty == null) ? null : java.lang.Long.valueOf(clusterIdProperty);
            java.lang.String serviceName = ((java.lang.String) (getRequestProperty(request, org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.SERVICE_NAME_PROPERTY)));
            java.lang.String autoCompleteProperty = ((java.lang.String) (getRequestProperty(request, org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.AUTO_COMPLETE_PROPERTY)));
            java.lang.Boolean autoComplete = (autoCompleteProperty == null) ? false : java.lang.Boolean.valueOf(autoCompleteProperty);
            java.lang.String stackName = ((java.lang.String) (getRequestProperty(request, org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.STACK_NAME_PROPERTY_ID)));
            java.lang.String stackVersion = ((java.lang.String) (getRequestProperty(request, org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.STACK_VERSION_PROPERTY_ID)));
            org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType requestType = org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType.fromString(((java.lang.String) (getRequestProperty(request, getRequestTypePropertyId()))));
            java.util.List<java.lang.String> hosts;
            java.util.List<java.lang.String> services;
            java.util.Map<java.lang.String, java.util.Set<java.lang.String>> hgComponentsMap;
            java.util.Map<java.lang.String, java.util.Set<java.lang.String>> hgHostsMap;
            java.util.Map<java.lang.String, java.util.Set<java.lang.String>> componentHostsMap;
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> configurations;
            java.util.Set<org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.ConfigGroup> configGroups;
            if (autoComplete) {
                if ((clusterId == null) || (serviceName == null)) {
                    throw new java.lang.Exception(java.lang.String.format("Incomplete request, clusterId and/or serviceName are not valid, clusterId=%s, serviceName=%s", clusterId, serviceName));
                }
                org.apache.ambari.server.state.Cluster cluster = org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.clusters.getCluster(clusterId);
                java.util.List<org.apache.ambari.server.state.Host> hostObjects = new java.util.ArrayList<>(cluster.getHosts());
                java.util.Map<java.lang.String, org.apache.ambari.server.state.Service> serviceObjects = cluster.getServices();
                hosts = hostObjects.stream().map(h -> h.getHostName()).collect(java.util.stream.Collectors.toList());
                services = new java.util.ArrayList<>(serviceObjects.keySet());
                hgComponentsMap = calculateHostGroupComponentsMap(cluster);
                hgHostsMap = calculateHostGroupHostsMap(cluster);
                componentHostsMap = calculateComponentHostsMap(cluster);
                configurations = calculateConfigurations(cluster, serviceName);
                configGroups = calculateConfigGroups(cluster, request);
            } else {
                java.lang.Object hostsObject = getRequestProperty(request, org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.HOST_PROPERTY);
                if (hostsObject instanceof java.util.LinkedHashSet) {
                    if (((java.util.LinkedHashSet) (hostsObject)).isEmpty()) {
                        throw new java.lang.Exception("Empty host list passed to recommendation service");
                    }
                }
                hosts = ((java.util.List<java.lang.String>) (hostsObject));
                java.lang.Object servicesObject = getRequestProperty(request, org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.SERVICES_PROPERTY);
                if (servicesObject instanceof java.util.LinkedHashSet) {
                    if (((java.util.LinkedHashSet) (servicesObject)).isEmpty()) {
                        throw new java.lang.Exception("Empty service list passed to recommendation service");
                    }
                }
                services = ((java.util.List<java.lang.String>) (servicesObject));
                hgComponentsMap = calculateHostGroupComponentsMap(request);
                hgHostsMap = calculateHostGroupHostsMap(request);
                componentHostsMap = calculateComponentHostsMap(hgComponentsMap, hgHostsMap);
                configurations = calculateConfigurations(request);
                configGroups = calculateConfigGroups(request);
            }
            java.util.Map<java.lang.String, java.lang.String> userContext = readUserContext(request);
            java.lang.Boolean gplLicenseAccepted = org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.configuration.getGplLicenseAccepted();
            java.util.List<org.apache.ambari.server.state.ChangedConfigInfo> changedConfigurations = (requestType == org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestType.CONFIGURATION_DEPENDENCIES) ? calculateChangedConfigurations(request) : java.util.Collections.emptyList();
            java.lang.String configsResponseProperty = ((java.lang.String) (getRequestProperty(request, org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.CONFIGS_RESPONSE_PROPERTY)));
            java.lang.Boolean configsResponse = (configsResponseProperty == null) ? false : java.lang.Boolean.valueOf(configsResponseProperty);
            return org.apache.ambari.server.api.services.stackadvisor.StackAdvisorRequest.StackAdvisorRequestBuilder.forStack(stackName, stackVersion).ofType(requestType).forHosts(hosts).forServices(services).forHostComponents(hgComponentsMap).forHostsGroupBindings(hgHostsMap).withComponentHostsMap(componentHostsMap).withConfigurations(configurations).withConfigGroups(configGroups).withChangedConfigurations(changedConfigurations).withUserContext(userContext).withGPLLicenseAccepted(gplLicenseAccepted).withClusterId(clusterId).withServiceName(serviceName).withConfigsResponse(configsResponse).build();
        } catch (java.lang.Exception e) {
            org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.LOG.warn("Error occurred during preparation of stack advisor request", e);
            javax.ws.rs.core.Response response = javax.ws.rs.core.Response.status(Status.BAD_REQUEST).entity(java.lang.String.format("Request body is not correct, error: %s", e.getMessage())).build();
            throw new javax.ws.rs.WebApplicationException(response);
        }
    }

    @java.lang.SuppressWarnings("unchecked")
    private java.util.Map<java.lang.String, java.util.Set<java.lang.String>> calculateHostGroupComponentsMap(org.apache.ambari.server.controller.spi.Request request) {
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> hostGroups = ((java.util.Set<java.util.Map<java.lang.String, java.lang.Object>>) (getRequestProperty(request, org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.BLUEPRINT_HOST_GROUPS_PROPERTY)));
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> map = new java.util.HashMap<>();
        if (hostGroups != null) {
            for (java.util.Map<java.lang.String, java.lang.Object> hostGroup : hostGroups) {
                java.lang.String hostGroupName = ((java.lang.String) (hostGroup.get(org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.BLUEPRINT_HOST_GROUPS_NAME_PROPERTY)));
                java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> componentsSet = ((java.util.Set<java.util.Map<java.lang.String, java.lang.Object>>) (hostGroup.get(org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.BLUEPRINT_HOST_GROUPS_COMPONENTS_PROPERTY)));
                java.util.Set<java.lang.String> components = new java.util.HashSet<>();
                for (java.util.Map<java.lang.String, java.lang.Object> component : componentsSet) {
                    components.add(((java.lang.String) (component.get(org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.BLUEPRINT_HOST_GROUPS_COMPONENTS_NAME_PROPERTY))));
                }
                map.put(hostGroupName, components);
            }
        }
        return map;
    }

    private java.util.Map<java.lang.String, java.util.Set<java.lang.String>> calculateHostGroupComponentsMap(org.apache.ambari.server.state.Cluster cluster) {
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> map = new java.util.HashMap<>();
        java.util.List<org.apache.ambari.server.state.Host> hosts = new java.util.ArrayList<>(cluster.getHosts());
        if (!hosts.isEmpty()) {
            for (org.apache.ambari.server.state.Host host : hosts) {
                java.lang.String hostGroupName = host.getHostName();
                java.util.Set<java.lang.String> components = new java.util.HashSet<>();
                for (org.apache.ambari.server.state.ServiceComponentHost sch : cluster.getServiceComponentHosts(host.getHostName())) {
                    components.add(sch.getServiceComponentName());
                }
                map.put(hostGroupName, components);
            }
        }
        return map;
    }

    @java.lang.SuppressWarnings("unchecked")
    private java.util.Map<java.lang.String, java.util.Set<java.lang.String>> calculateHostGroupHostsMap(org.apache.ambari.server.controller.spi.Request request) {
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> bindingHostGroups = ((java.util.Set<java.util.Map<java.lang.String, java.lang.Object>>) (getRequestProperty(request, org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.BINDING_HOST_GROUPS_PROPERTY)));
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> map = new java.util.HashMap<>();
        if (bindingHostGroups != null) {
            for (java.util.Map<java.lang.String, java.lang.Object> hostGroup : bindingHostGroups) {
                java.lang.String hostGroupName = ((java.lang.String) (hostGroup.get(org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.BINDING_HOST_GROUPS_NAME_PROPERTY)));
                java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> hostsSet = ((java.util.Set<java.util.Map<java.lang.String, java.lang.Object>>) (hostGroup.get(org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.BINDING_HOST_GROUPS_HOSTS_PROPERTY)));
                java.util.Set<java.lang.String> hosts = new java.util.HashSet<>();
                for (java.util.Map<java.lang.String, java.lang.Object> host : hostsSet) {
                    hosts.add(((java.lang.String) (host.get(org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.BINDING_HOST_GROUPS_HOSTS_NAME_PROPERTY))));
                }
                map.put(hostGroupName, hosts);
            }
        }
        return map;
    }

    private java.util.Map<java.lang.String, java.util.Set<java.lang.String>> calculateHostGroupHostsMap(org.apache.ambari.server.state.Cluster cluster) {
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> map = new java.util.HashMap<>();
        java.util.List<org.apache.ambari.server.state.Host> hosts = new java.util.ArrayList<>(cluster.getHosts());
        if (!hosts.isEmpty()) {
            for (org.apache.ambari.server.state.Host host : hosts) {
                map.put(host.getHostName(), java.util.Collections.singleton(host.getHostName()));
            }
        }
        return map;
    }

    protected java.util.List<org.apache.ambari.server.state.ChangedConfigInfo> calculateChangedConfigurations(org.apache.ambari.server.controller.spi.Request request) {
        java.util.List<org.apache.ambari.server.state.ChangedConfigInfo> configs = new java.util.LinkedList<>();
        java.util.HashSet<java.util.HashMap<java.lang.String, java.lang.String>> changedConfigs = ((java.util.HashSet<java.util.HashMap<java.lang.String, java.lang.String>>) (getRequestProperty(request, org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.CHANGED_CONFIGURATIONS_PROPERTY)));
        for (java.util.HashMap<java.lang.String, java.lang.String> props : changedConfigs) {
            configs.add(new org.apache.ambari.server.state.ChangedConfigInfo(props.get("type"), props.get("name"), props.get("old_value")));
        }
        return configs;
    }

    protected java.util.Set<org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.ConfigGroup> calculateConfigGroups(org.apache.ambari.server.controller.spi.Request request) {
        java.util.Set<org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.ConfigGroup> configGroups = new java.util.HashSet<>();
        java.util.Set<java.util.HashMap<java.lang.String, java.lang.Object>> configGroupsProperties = ((java.util.HashSet<java.util.HashMap<java.lang.String, java.lang.Object>>) (getRequestProperty(request, org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.CONFIG_GROUPS_PROPERTY)));
        if (configGroupsProperties != null) {
            for (java.util.HashMap<java.lang.String, java.lang.Object> props : configGroupsProperties) {
                org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.ConfigGroup configGroup = new org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.ConfigGroup();
                configGroup.setHosts(((java.util.List<java.lang.String>) (props.get(org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.CONFIG_GROUPS_HOSTS_PROPERTY))));
                for (java.util.Map<java.lang.String, java.lang.String> property : ((java.util.Set<java.util.Map<java.lang.String, java.lang.String>>) (props.get(org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.CONFIG_GROUPS_CONFIGURATIONS_PROPERTY)))) {
                    for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : property.entrySet()) {
                        java.lang.String[] propertyPath = entry.getKey().split("/");
                        java.lang.String siteName = propertyPath[0];
                        java.lang.String propertyName = propertyPath[2];
                        if (!configGroup.getConfigurations().containsKey(siteName)) {
                            org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations configurations = new org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations();
                            configGroup.getConfigurations().put(siteName, configurations);
                            configGroup.getConfigurations().get(siteName).setProperties(new java.util.HashMap<>());
                        }
                        configGroup.getConfigurations().get(siteName).getProperties().put(propertyName, entry.getValue());
                    }
                }
                configGroups.add(configGroup);
            }
        }
        return configGroups;
    }

    protected java.util.Set<org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.ConfigGroup> calculateConfigGroups(org.apache.ambari.server.state.Cluster cluster, org.apache.ambari.server.controller.spi.Request request) {
        java.util.Set<org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.ConfigGroup> configGroups = new java.util.HashSet<>();
        java.util.Set<java.util.HashMap<java.lang.String, java.lang.Object>> configGroupsProperties = ((java.util.HashSet<java.util.HashMap<java.lang.String, java.lang.Object>>) (getRequestProperty(request, org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.CONFIG_GROUPS_PROPERTY)));
        if (configGroupsProperties != null) {
            for (java.util.HashMap<java.lang.String, java.lang.Object> props : configGroupsProperties) {
                org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.ConfigGroup configGroup = new org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.ConfigGroup();
                java.lang.Object groupIdObject = props.get(org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.CONFIG_GROUPS_GROUP_ID_PROPERTY);
                if (groupIdObject != null) {
                    java.lang.Long groupId = java.lang.Long.valueOf(((java.lang.String) (groupIdObject)));
                    org.apache.ambari.server.state.configgroup.ConfigGroup clusterConfigGroup = cluster.getConfigGroupsById(groupId);
                    java.util.Map<java.lang.String, org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations> typedConfiguration = new java.util.HashMap<>();
                    for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.Config> config : clusterConfigGroup.getConfigurations().entrySet()) {
                        org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations blueprintConfiguration = new org.apache.ambari.server.api.services.stackadvisor.recommendations.RecommendationResponse.BlueprintConfigurations();
                        blueprintConfiguration.setProperties(config.getValue().getProperties());
                        typedConfiguration.put(config.getKey(), blueprintConfiguration);
                    }
                    configGroup.setConfigurations(typedConfiguration);
                    configGroup.setHosts(clusterConfigGroup.getHosts().values().stream().map(h -> h.getHostName()).collect(java.util.stream.Collectors.toList()));
                    configGroups.add(configGroup);
                }
            }
        }
        return configGroups;
    }

    protected java.util.Map<java.lang.String, java.lang.String> readUserContext(org.apache.ambari.server.controller.spi.Request request) {
        java.util.Map<java.lang.String, java.lang.String> userContext = new java.util.HashMap<>();
        if (null != getRequestProperty(request, org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.USER_CONTEXT_OPERATION_PROPERTY)) {
            userContext.put(org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.OPERATION_PROPERTY, ((java.lang.String) (getRequestProperty(request, org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.USER_CONTEXT_OPERATION_PROPERTY))));
        }
        if (null != getRequestProperty(request, org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.USER_CONTEXT_OPERATION_DETAILS_PROPERTY)) {
            userContext.put(org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.OPERATION_DETAILS_PROPERTY, ((java.lang.String) (getRequestProperty(request, org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.USER_CONTEXT_OPERATION_DETAILS_PROPERTY))));
        }
        return userContext;
    }

    protected static final java.lang.String CONFIGURATIONS_PROPERTY_ID = "recommendations/blueprint/configurations/";

    protected java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> calculateConfigurations(org.apache.ambari.server.controller.spi.Request request) {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> configurations = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.Object> properties = request.getProperties().iterator().next();
        for (java.lang.String property : properties.keySet()) {
            if (property.startsWith(org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.CONFIGURATIONS_PROPERTY_ID)) {
                try {
                    java.lang.String propertyEnd = property.substring(org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.CONFIGURATIONS_PROPERTY_ID.length());
                    java.lang.String[] propertyPath = propertyEnd.split("/");
                    java.lang.String siteName = propertyPath[0];
                    java.lang.String propertiesProperty = propertyPath[1];
                    java.lang.String propertyName = propertyPath[2];
                    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> siteMap = configurations.get(siteName);
                    if (siteMap == null) {
                        siteMap = new java.util.HashMap<>();
                        configurations.put(siteName, siteMap);
                    }
                    java.util.Map<java.lang.String, java.lang.String> propertiesMap = siteMap.get(propertiesProperty);
                    if (propertiesMap == null) {
                        propertiesMap = new java.util.HashMap<>();
                        siteMap.put(propertiesProperty, propertiesMap);
                    }
                    java.lang.Object propVal = properties.get(property);
                    if (propVal != null)
                        propertiesMap.put(propertyName, propVal.toString());
                    else
                        org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.LOG.info(java.lang.String.format("No value specified for configuration property, name = %s ", property));

                } catch (java.lang.Exception e) {
                    org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.LOG.debug(java.lang.String.format("Error handling configuration property, name = %s", property), e);
                }
            }
        }
        return configurations;
    }

    protected java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> calculateConfigurations(org.apache.ambari.server.state.Cluster cluster, java.lang.String serviceName) throws org.apache.ambari.server.AmbariException {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> configurations = new java.util.HashMap<>();
        org.apache.ambari.server.state.Service service = cluster.getService(serviceName);
        org.apache.ambari.server.state.StackId stackId = service.getDesiredStackId();
        org.apache.ambari.server.state.ServiceInfo serviceInfo = org.apache.ambari.server.controller.internal.StackAdvisorResourceProvider.ambariMetaInfo.getService(stackId.getStackName(), stackId.getStackVersion(), serviceName);
        java.util.List<java.lang.String> requiredConfigTypes = serviceInfo.getConfigDependenciesWithComponents();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigs = cluster.getDesiredConfigs();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> requiredDesiredConfigs = new java.util.HashMap<>();
        for (java.lang.String requiredConfigType : requiredConfigTypes) {
            if (desiredConfigs.containsKey(requiredConfigType)) {
                requiredDesiredConfigs.put(requiredConfigType, desiredConfigs.get(requiredConfigType));
            }
        }
        for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.state.DesiredConfig> requiredDesiredConfigEntry : requiredDesiredConfigs.entrySet()) {
            org.apache.ambari.server.state.Config config = cluster.getConfig(requiredDesiredConfigEntry.getKey(), requiredDesiredConfigEntry.getValue().getTag());
            configurations.put(requiredDesiredConfigEntry.getKey(), java.util.Collections.singletonMap("properties", config.getProperties()));
        }
        return configurations;
    }

    @java.lang.SuppressWarnings("unchecked")
    private java.util.Map<java.lang.String, java.util.Set<java.lang.String>> calculateComponentHostsMap(java.util.Map<java.lang.String, java.util.Set<java.lang.String>> hostGroups, java.util.Map<java.lang.String, java.util.Set<java.lang.String>> bindingHostGroups) {
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> componentHostsMap = new java.util.HashMap<>();
        if ((null != bindingHostGroups) && (null != hostGroups)) {
            for (java.util.Map.Entry<java.lang.String, java.util.Set<java.lang.String>> hgComponents : hostGroups.entrySet()) {
                java.lang.String hgName = hgComponents.getKey();
                java.util.Set<java.lang.String> components = hgComponents.getValue();
                java.util.Set<java.lang.String> hosts = bindingHostGroups.get(hgName);
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
        return componentHostsMap;
    }

    @java.lang.SuppressWarnings("unchecked")
    private java.util.Map<java.lang.String, java.util.Set<java.lang.String>> calculateComponentHostsMap(org.apache.ambari.server.state.Cluster cluster) {
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> componentHostsMap = new java.util.HashMap<>();
        java.util.List<org.apache.ambari.server.state.ServiceComponentHost> schs = cluster.getServiceComponentHosts();
        for (org.apache.ambari.server.state.ServiceComponentHost sch : schs) {
            componentHostsMap.putIfAbsent(sch.getServiceComponentName(), new java.util.HashSet<>());
            componentHostsMap.get(sch.getServiceComponentName()).add(sch.getHostName());
        }
        return componentHostsMap;
    }

    protected java.lang.Object getRequestProperty(org.apache.ambari.server.controller.spi.Request request, java.lang.String propertyName) {
        for (java.util.Map<java.lang.String, java.lang.Object> propertyMap : request.getProperties()) {
            if (propertyMap.containsKey(propertyName)) {
                return propertyMap.get(propertyName);
            }
        }
        return null;
    }
}