package org.apache.ambari.server.controller.internal;
public class Stack {
    private java.lang.String name;

    private java.lang.String version;

    private java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> serviceComponents = new java.util.HashMap<>();

    private java.util.Map<java.lang.String, java.lang.String> componentService = new java.util.HashMap<>();

    private java.util.Map<java.lang.String, java.util.Collection<org.apache.ambari.server.state.DependencyInfo>> dependencies = new java.util.HashMap<>();

    private java.util.Map<org.apache.ambari.server.state.DependencyInfo, java.lang.String> dependencyConditionalServiceMap = new java.util.HashMap<>();

    private java.util.Map<java.lang.String, java.lang.String> dbDependencyInfo = new java.util.HashMap<>();

    private java.util.Map<java.lang.String, java.lang.String> cardinalityRequirements = new java.util.HashMap<>();

    private java.util.Set<java.lang.String> masterComponents = new java.util.HashSet<>();

    private java.util.Map<java.lang.String, org.apache.ambari.server.state.AutoDeployInfo> componentAutoDeployInfo = new java.util.HashMap<>();

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.Stack.ConfigProperty>>> serviceConfigurations = new java.util.HashMap<>();

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.Stack.ConfigProperty>>> requiredServiceConfigurations = new java.util.HashMap<>();

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.Stack.ConfigProperty>> stackConfigurations = new java.util.HashMap<>();

    private java.util.Map<java.lang.String, java.util.Set<java.lang.String>> excludedConfigurationTypes = new java.util.HashMap<>();

    private final org.apache.ambari.server.controller.AmbariManagementController controller;

    public Stack(org.apache.ambari.server.orm.entities.StackEntity stack, org.apache.ambari.server.controller.AmbariManagementController ambariManagementController) throws org.apache.ambari.server.AmbariException {
        this(stack.getStackName(), stack.getStackVersion(), ambariManagementController);
    }

    public Stack(org.apache.ambari.server.state.StackId stackId, org.apache.ambari.server.controller.AmbariManagementController ambariManagementController) throws org.apache.ambari.server.AmbariException {
        this(stackId.getStackName(), stackId.getStackVersion(), ambariManagementController);
    }

    public Stack(java.lang.String name, java.lang.String version, org.apache.ambari.server.controller.AmbariManagementController controller) throws org.apache.ambari.server.AmbariException {
        this.name = name;
        this.version = version;
        this.controller = controller;
        java.util.Set<org.apache.ambari.server.controller.StackServiceResponse> stackServices = controller.getStackServices(java.util.Collections.singleton(new org.apache.ambari.server.controller.StackServiceRequest(name, version, null)));
        for (org.apache.ambari.server.controller.StackServiceResponse stackService : stackServices) {
            java.lang.String serviceName = stackService.getServiceName();
            parseComponents(serviceName);
            parseExcludedConfigurations(stackService);
            parseConfigurations(stackService);
            registerConditionalDependencies();
        }
        parseStackConfigurations();
    }

    public java.lang.String getName() {
        return name;
    }

    public java.lang.String getVersion() {
        return version;
    }

    public org.apache.ambari.server.state.StackId getStackId() {
        return new org.apache.ambari.server.state.StackId(name, version);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "stack " + getStackId();
    }

    java.util.Map<org.apache.ambari.server.state.DependencyInfo, java.lang.String> getDependencyConditionalServiceMap() {
        return dependencyConditionalServiceMap;
    }

    public java.util.Set<java.lang.String> getServices() {
        return serviceComponents.keySet();
    }

    public java.util.Collection<java.lang.String> getComponents(java.lang.String service) {
        return serviceComponents.get(service);
    }

    public java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> getComponents() {
        java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> serviceComponents = new java.util.HashMap<>();
        for (java.lang.String service : getServices()) {
            java.util.Collection<java.lang.String> components = new java.util.HashSet<>();
            components.addAll(getComponents(service));
            serviceComponents.put(service, components);
        }
        return serviceComponents;
    }

    public org.apache.ambari.server.state.ComponentInfo getComponentInfo(java.lang.String component) {
        org.apache.ambari.server.state.ComponentInfo componentInfo = null;
        java.lang.String service = getServiceForComponent(component);
        if (service != null) {
            try {
                componentInfo = controller.getAmbariMetaInfo().getComponent(getName(), getVersion(), service, component);
            } catch (org.apache.ambari.server.AmbariException e) {
            }
        }
        return componentInfo;
    }

    public java.util.Optional<org.apache.ambari.server.state.ServiceInfo> getServiceInfo(java.lang.String serviceName) {
        try {
            return java.util.Optional.of(controller.getAmbariMetaInfo().getService(getName(), getVersion(), serviceName));
        } catch (org.apache.ambari.server.AmbariException e) {
            return java.util.Optional.empty();
        }
    }

    public java.util.Collection<java.lang.String> getAllConfigurationTypes(java.lang.String service) {
        return serviceConfigurations.get(service).keySet();
    }

    public java.util.Collection<java.lang.String> getConfigurationTypes(java.lang.String service) {
        java.util.Set<java.lang.String> serviceTypes = new java.util.HashSet<>(serviceConfigurations.get(service).keySet());
        serviceTypes.removeAll(getExcludedConfigurationTypes(service));
        return serviceTypes;
    }

    public java.util.Set<java.lang.String> getExcludedConfigurationTypes(java.lang.String service) {
        return excludedConfigurationTypes.containsKey(service) ? excludedConfigurationTypes.get(service) : java.util.Collections.emptySet();
    }

    public java.util.Map<java.lang.String, java.lang.String> getConfigurationProperties(java.lang.String service, java.lang.String type) {
        java.util.Map<java.lang.String, java.lang.String> configMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.Stack.ConfigProperty> configProperties = serviceConfigurations.get(service).get(type);
        if (configProperties != null) {
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.controller.internal.Stack.ConfigProperty> configProperty : configProperties.entrySet()) {
                configMap.put(configProperty.getKey(), configProperty.getValue().getValue());
            }
        }
        return configMap;
    }

    public java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.Stack.ConfigProperty> getConfigurationPropertiesWithMetadata(java.lang.String service, java.lang.String type) {
        return serviceConfigurations.get(service).get(type);
    }

    public java.util.Collection<org.apache.ambari.server.controller.internal.Stack.ConfigProperty> getRequiredConfigurationProperties(java.lang.String service) {
        java.util.Collection<org.apache.ambari.server.controller.internal.Stack.ConfigProperty> requiredConfigProperties = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.Stack.ConfigProperty>> serviceProperties = requiredServiceConfigurations.get(service);
        if (serviceProperties != null) {
            for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.Stack.ConfigProperty>> typePropertiesEntry : serviceProperties.entrySet()) {
                requiredConfigProperties.addAll(typePropertiesEntry.getValue().values());
            }
        }
        return requiredConfigProperties;
    }

    public java.util.Collection<org.apache.ambari.server.controller.internal.Stack.ConfigProperty> getRequiredConfigurationProperties(java.lang.String service, org.apache.ambari.server.state.PropertyInfo.PropertyType propertyType) {
        java.util.Collection<org.apache.ambari.server.controller.internal.Stack.ConfigProperty> matchingProperties = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.Stack.ConfigProperty>> requiredProperties = requiredServiceConfigurations.get(service);
        if (requiredProperties != null) {
            for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.Stack.ConfigProperty>> typePropertiesEntry : requiredProperties.entrySet()) {
                for (org.apache.ambari.server.controller.internal.Stack.ConfigProperty configProperty : typePropertiesEntry.getValue().values()) {
                    if (configProperty.getPropertyTypes().contains(propertyType)) {
                        matchingProperties.add(configProperty);
                    }
                }
            }
        }
        return matchingProperties;
    }

    public boolean isPasswordProperty(java.lang.String service, java.lang.String type, java.lang.String propertyName) {
        return ((serviceConfigurations.containsKey(service) && serviceConfigurations.get(service).containsKey(type)) && serviceConfigurations.get(service).get(type).containsKey(propertyName)) && serviceConfigurations.get(service).get(type).get(propertyName).getPropertyTypes().contains(org.apache.ambari.server.state.PropertyInfo.PropertyType.PASSWORD);
    }

    public java.util.Map<java.lang.String, java.lang.String> getStackConfigurationProperties(java.lang.String type) {
        java.util.Map<java.lang.String, java.lang.String> configMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.Stack.ConfigProperty> configProperties = stackConfigurations.get(type);
        if (configProperties != null) {
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.controller.internal.Stack.ConfigProperty> configProperty : configProperties.entrySet()) {
                configMap.put(configProperty.getKey(), configProperty.getValue().getValue());
            }
        }
        return configMap;
    }

    public boolean isKerberosPrincipalNameProperty(java.lang.String service, java.lang.String type, java.lang.String propertyName) {
        return ((serviceConfigurations.containsKey(service) && serviceConfigurations.get(service).containsKey(type)) && serviceConfigurations.get(service).get(type).containsKey(propertyName)) && serviceConfigurations.get(service).get(type).get(propertyName).getPropertyTypes().contains(org.apache.ambari.server.state.PropertyInfo.PropertyType.KERBEROS_PRINCIPAL);
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getConfigurationAttributes(java.lang.String service, java.lang.String type) {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> attributesMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.Stack.ConfigProperty> configProperties = serviceConfigurations.get(service).get(type);
        if (configProperties != null) {
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.controller.internal.Stack.ConfigProperty> configProperty : configProperties.entrySet()) {
                java.lang.String propertyName = configProperty.getKey();
                java.util.Map<java.lang.String, java.lang.String> propertyAttributes = configProperty.getValue().getAttributes();
                if (propertyAttributes != null) {
                    for (java.util.Map.Entry<java.lang.String, java.lang.String> propertyAttribute : propertyAttributes.entrySet()) {
                        java.lang.String attributeName = propertyAttribute.getKey();
                        java.lang.String attributeValue = propertyAttribute.getValue();
                        if (attributeValue != null) {
                            java.util.Map<java.lang.String, java.lang.String> attributes = attributesMap.get(attributeName);
                            if (attributes == null) {
                                attributes = new java.util.HashMap<>();
                                attributesMap.put(attributeName, attributes);
                            }
                            attributes.put(propertyName, attributeValue);
                        }
                    }
                }
            }
        }
        return attributesMap;
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getStackConfigurationAttributes(java.lang.String type) {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> attributesMap = new java.util.HashMap<>();
        java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.Stack.ConfigProperty> configProperties = stackConfigurations.get(type);
        if (configProperties != null) {
            for (java.util.Map.Entry<java.lang.String, org.apache.ambari.server.controller.internal.Stack.ConfigProperty> configProperty : configProperties.entrySet()) {
                java.lang.String propertyName = configProperty.getKey();
                java.util.Map<java.lang.String, java.lang.String> propertyAttributes = configProperty.getValue().getAttributes();
                if (propertyAttributes != null) {
                    for (java.util.Map.Entry<java.lang.String, java.lang.String> propertyAttribute : propertyAttributes.entrySet()) {
                        java.lang.String attributeName = propertyAttribute.getKey();
                        java.lang.String attributeValue = propertyAttribute.getValue();
                        java.util.Map<java.lang.String, java.lang.String> attributes = attributesMap.get(attributeName);
                        if (attributes == null) {
                            attributes = new java.util.HashMap<>();
                            attributesMap.put(attributeName, attributes);
                        }
                        attributes.put(propertyName, attributeValue);
                    }
                }
            }
        }
        return attributesMap;
    }

    public java.lang.String getServiceForComponent(java.lang.String component) {
        return componentService.get(component);
    }

    public java.util.Collection<java.lang.String> getServicesForComponents(java.util.Collection<java.lang.String> components) {
        java.util.Set<java.lang.String> services = new java.util.HashSet<>();
        for (java.lang.String component : components) {
            services.add(getServiceForComponent(component));
        }
        return services;
    }

    public java.lang.String getServiceForConfigType(java.lang.String config) {
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.Stack.ConfigProperty>>> entry : serviceConfigurations.entrySet()) {
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.Stack.ConfigProperty>> typeMap = entry.getValue();
            java.lang.String serviceName = entry.getKey();
            if (typeMap.containsKey(config) && (!getExcludedConfigurationTypes(serviceName).contains(config))) {
                return serviceName;
            }
        }
        throw new java.lang.IllegalArgumentException("Specified configuration type is not associated with any service: " + config);
    }

    public java.util.List<java.lang.String> getServicesForConfigType(java.lang.String config) {
        java.util.List<java.lang.String> serviceNames = new java.util.ArrayList<>();
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.Stack.ConfigProperty>>> entry : serviceConfigurations.entrySet()) {
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.Stack.ConfigProperty>> typeMap = entry.getValue();
            java.lang.String serviceName = entry.getKey();
            if (typeMap.containsKey(config) && (!getExcludedConfigurationTypes(serviceName).contains(config))) {
                serviceNames.add(serviceName);
            }
        }
        return serviceNames;
    }

    public java.util.Collection<org.apache.ambari.server.state.DependencyInfo> getDependenciesForComponent(java.lang.String component) {
        return dependencies.containsKey(component) ? dependencies.get(component) : java.util.Collections.emptySet();
    }

    public java.lang.String getConditionalServiceForDependency(org.apache.ambari.server.state.DependencyInfo dependency) {
        return dependencyConditionalServiceMap.get(dependency);
    }

    public java.lang.String getExternalComponentConfig(java.lang.String component) {
        return dbDependencyInfo.get(component);
    }

    public org.apache.ambari.server.topology.Cardinality getCardinality(java.lang.String component) {
        return new org.apache.ambari.server.topology.Cardinality(cardinalityRequirements.get(component));
    }

    public org.apache.ambari.server.state.AutoDeployInfo getAutoDeployInfo(java.lang.String component) {
        return componentAutoDeployInfo.get(component);
    }

    public boolean isMasterComponent(java.lang.String component) {
        return masterComponents.contains(component);
    }

    public org.apache.ambari.server.topology.Configuration getConfiguration(java.util.Collection<java.lang.String> services) {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> attributes = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        for (java.lang.String service : services) {
            java.util.Collection<java.lang.String> serviceConfigTypes = getConfigurationTypes(service);
            for (java.lang.String type : serviceConfigTypes) {
                java.util.Map<java.lang.String, java.lang.String> typeProps = properties.get(type);
                if (typeProps == null) {
                    typeProps = new java.util.HashMap<>();
                    properties.put(type, typeProps);
                }
                typeProps.putAll(getConfigurationProperties(service, type));
                java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> stackTypeAttributes = getConfigurationAttributes(service, type);
                if (!stackTypeAttributes.isEmpty()) {
                    if (!attributes.containsKey(type)) {
                        attributes.put(type, new java.util.HashMap<>());
                    }
                    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> typeAttributes = attributes.get(type);
                    for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> attribute : stackTypeAttributes.entrySet()) {
                        java.lang.String attributeName = attribute.getKey();
                        java.util.Map<java.lang.String, java.lang.String> attributeProps = typeAttributes.get(attributeName);
                        if (attributeProps == null) {
                            attributeProps = new java.util.HashMap<>();
                            typeAttributes.put(attributeName, attributeProps);
                        }
                        attributeProps.putAll(attribute.getValue());
                    }
                }
            }
        }
        return new org.apache.ambari.server.topology.Configuration(properties, attributes);
    }

    public org.apache.ambari.server.topology.Configuration getConfiguration() {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> stackAttributes = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> stackConfigs = new java.util.HashMap<>();
        for (java.lang.String service : getServices()) {
            for (java.lang.String type : getAllConfigurationTypes(service)) {
                java.util.Map<java.lang.String, java.lang.String> typeProps = stackConfigs.get(type);
                if (typeProps == null) {
                    typeProps = new java.util.HashMap<>();
                    stackConfigs.put(type, typeProps);
                }
                typeProps.putAll(getConfigurationProperties(service, type));
                java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> stackTypeAttributes = getConfigurationAttributes(service, type);
                if (!stackTypeAttributes.isEmpty()) {
                    if (!stackAttributes.containsKey(type)) {
                        stackAttributes.put(type, new java.util.HashMap<>());
                    }
                    java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> typeAttrs = stackAttributes.get(type);
                    for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> attribute : stackTypeAttributes.entrySet()) {
                        java.lang.String attributeName = attribute.getKey();
                        java.util.Map<java.lang.String, java.lang.String> attributes = typeAttrs.get(attributeName);
                        if (attributes == null) {
                            attributes = new java.util.HashMap<>();
                            typeAttrs.put(attributeName, attributes);
                        }
                        attributes.putAll(attribute.getValue());
                    }
                }
            }
        }
        return new org.apache.ambari.server.topology.Configuration(stackConfigs, stackAttributes);
    }

    public org.apache.ambari.server.topology.Configuration getValidDefaultConfig() {
        org.apache.ambari.server.topology.Configuration config = getDefaultConfig();
        org.apache.ambari.server.controller.internal.UnitUpdater.updateUnits(config, this);
        return config;
    }

    public org.apache.ambari.server.topology.Configuration getDefaultConfig() {
        org.apache.ambari.server.topology.Configuration config = getConfiguration();
        config.getProperties().values().forEach(each -> each.values().removeIf(java.util.Objects::isNull));
        return config;
    }

    private void parseComponents(java.lang.String service) throws org.apache.ambari.server.AmbariException {
        java.util.Collection<java.lang.String> componentSet = new java.util.HashSet<>();
        java.util.Set<org.apache.ambari.server.controller.StackServiceComponentResponse> components = controller.getStackComponents(java.util.Collections.singleton(new org.apache.ambari.server.controller.StackServiceComponentRequest(name, version, service, null)));
        for (org.apache.ambari.server.controller.StackServiceComponentResponse component : components) {
            java.lang.String componentName = component.getComponentName();
            componentSet.add(componentName);
            componentService.put(componentName, service);
            java.lang.String cardinality = component.getCardinality();
            if (cardinality != null) {
                cardinalityRequirements.put(componentName, cardinality);
            }
            org.apache.ambari.server.state.AutoDeployInfo autoDeploy = component.getAutoDeploy();
            if (autoDeploy != null) {
                componentAutoDeployInfo.put(componentName, autoDeploy);
            }
            java.util.Collection<org.apache.ambari.server.state.DependencyInfo> componentDependencies = controller.getAmbariMetaInfo().getComponentDependencies(name, version, service, componentName);
            if ((componentDependencies != null) && (!componentDependencies.isEmpty())) {
                dependencies.put(componentName, componentDependencies);
            }
            if (component.isMaster()) {
                masterComponents.add(componentName);
            }
        }
        serviceComponents.put(service, componentSet);
    }

    private void parseConfigurations(org.apache.ambari.server.controller.StackServiceResponse stackService) throws org.apache.ambari.server.AmbariException {
        java.lang.String service = stackService.getServiceName();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.Stack.ConfigProperty>> mapServiceConfig = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.Stack.ConfigProperty>> mapRequiredServiceConfig = new java.util.HashMap<>();
        serviceConfigurations.put(service, mapServiceConfig);
        requiredServiceConfigurations.put(service, mapRequiredServiceConfig);
        java.util.Set<org.apache.ambari.server.controller.StackConfigurationResponse> serviceConfigs = controller.getStackConfigurations(java.util.Collections.singleton(new org.apache.ambari.server.controller.StackConfigurationRequest(name, version, service, null)));
        java.util.Set<org.apache.ambari.server.controller.StackConfigurationResponse> stackLevelConfigs = controller.getStackLevelConfigurations(java.util.Collections.singleton(new org.apache.ambari.server.controller.StackLevelConfigurationRequest(name, version, null)));
        serviceConfigs.addAll(stackLevelConfigs);
        for (org.apache.ambari.server.controller.StackConfigurationResponse config : serviceConfigs) {
            org.apache.ambari.server.controller.internal.Stack.ConfigProperty configProperty = new org.apache.ambari.server.controller.internal.Stack.ConfigProperty(config);
            java.lang.String type = configProperty.getType();
            java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.Stack.ConfigProperty> mapTypeConfig = mapServiceConfig.get(type);
            if (mapTypeConfig == null) {
                mapTypeConfig = new java.util.HashMap<>();
                mapServiceConfig.put(type, mapTypeConfig);
            }
            mapTypeConfig.put(config.getPropertyName(), configProperty);
            if (config.isRequired()) {
                java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.Stack.ConfigProperty> requiredTypeConfig = mapRequiredServiceConfig.get(type);
                if (requiredTypeConfig == null) {
                    requiredTypeConfig = new java.util.HashMap<>();
                    mapRequiredServiceConfig.put(type, requiredTypeConfig);
                }
                requiredTypeConfig.put(config.getPropertyName(), configProperty);
            }
        }
        java.util.Set<java.lang.String> configTypes = stackService.getConfigTypes().keySet();
        for (java.lang.String configType : configTypes) {
            if (!mapServiceConfig.containsKey(configType)) {
                mapServiceConfig.put(configType, java.util.Collections.emptyMap());
            }
        }
    }

    private void parseStackConfigurations() throws org.apache.ambari.server.AmbariException {
        java.util.Set<org.apache.ambari.server.controller.StackConfigurationResponse> stackLevelConfigs = controller.getStackLevelConfigurations(java.util.Collections.singleton(new org.apache.ambari.server.controller.StackLevelConfigurationRequest(name, version, null)));
        for (org.apache.ambari.server.controller.StackConfigurationResponse config : stackLevelConfigs) {
            org.apache.ambari.server.controller.internal.Stack.ConfigProperty configProperty = new org.apache.ambari.server.controller.internal.Stack.ConfigProperty(config);
            java.lang.String type = configProperty.getType();
            java.util.Map<java.lang.String, org.apache.ambari.server.controller.internal.Stack.ConfigProperty> mapTypeConfig = stackConfigurations.get(type);
            if (mapTypeConfig == null) {
                mapTypeConfig = new java.util.HashMap<>();
                stackConfigurations.put(type, mapTypeConfig);
            }
            mapTypeConfig.put(config.getPropertyName(), configProperty);
        }
    }

    private void parseExcludedConfigurations(org.apache.ambari.server.controller.StackServiceResponse stackServiceResponse) {
        excludedConfigurationTypes.put(stackServiceResponse.getServiceName(), stackServiceResponse.getExcludedConfigTypes());
    }

    void registerConditionalDependencies() {
        dbDependencyInfo.put("MYSQL_SERVER", "global/hive_database");
    }

    public static class ConfigProperty {
        private org.apache.ambari.server.state.ValueAttributesInfo propertyValueAttributes = null;

        private java.lang.String name;

        private java.lang.String value;

        private java.util.Map<java.lang.String, java.lang.String> attributes;

        private java.util.Set<org.apache.ambari.server.state.PropertyInfo.PropertyType> propertyTypes;

        private java.lang.String type;

        private java.util.Set<org.apache.ambari.server.state.PropertyDependencyInfo> dependsOnProperties = java.util.Collections.emptySet();

        public ConfigProperty(org.apache.ambari.server.controller.StackConfigurationResponse config) {
            this.name = config.getPropertyName();
            this.value = config.getPropertyValue();
            this.attributes = config.getPropertyAttributes();
            this.propertyTypes = config.getPropertyType();
            this.type = normalizeType(config.getType());
            this.dependsOnProperties = config.getDependsOnProperties();
            this.propertyValueAttributes = config.getPropertyValueAttributes();
        }

        public ConfigProperty(java.lang.String type, java.lang.String name, java.lang.String value) {
            this.type = type;
            this.name = name;
            this.value = value;
        }

        public java.lang.String getName() {
            return name;
        }

        public java.lang.String getValue() {
            return value;
        }

        public void setValue(java.lang.String value) {
            this.value = value;
        }

        public java.lang.String getType() {
            return type;
        }

        public java.util.Set<org.apache.ambari.server.state.PropertyInfo.PropertyType> getPropertyTypes() {
            return propertyTypes;
        }

        public void setPropertyTypes(java.util.Set<org.apache.ambari.server.state.PropertyInfo.PropertyType> propertyTypes) {
            this.propertyTypes = propertyTypes;
        }

        public java.util.Map<java.lang.String, java.lang.String> getAttributes() {
            return attributes;
        }

        public void setAttributes(java.util.Map<java.lang.String, java.lang.String> attributes) {
            this.attributes = attributes;
        }

        java.util.Set<org.apache.ambari.server.state.PropertyDependencyInfo> getDependsOnProperties() {
            return this.dependsOnProperties;
        }

        private java.lang.String normalizeType(java.lang.String type) {
            if (type.endsWith(".xml")) {
                type = type.substring(0, type.length() - 4);
            }
            return type;
        }

        public org.apache.ambari.server.state.ValueAttributesInfo getPropertyValueAttributes() {
            return propertyValueAttributes;
        }
    }
}