package org.apache.ambari.server.topology.validators;
public class RequiredConfigPropertiesValidator implements org.apache.ambari.server.topology.TopologyValidator {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.topology.validators.RequiredConfigPropertiesValidator.class);

    @java.lang.Override
    public void validate(org.apache.ambari.server.topology.ClusterTopology topology) throws org.apache.ambari.server.topology.InvalidTopologyException {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Collection<java.lang.String>>> requiredPropertiesByService = getRequiredPropertiesByService(topology.getBlueprint());
        java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> missingProperties = new java.util.TreeMap<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> topologyConfiguration = new java.util.HashMap<>(topology.getConfiguration().getFullProperties(1));
        for (org.apache.ambari.server.topology.HostGroup hostGroup : topology.getBlueprint().getHostGroups().values()) {
            org.apache.ambari.server.topology.validators.RequiredConfigPropertiesValidator.LOGGER.debug("Processing hostgroup configurations for hostgroup: {}", hostGroup.getName());
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> operationalConfigurations = new java.util.HashMap<>(topologyConfiguration);
            for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> hostgroupConfigEntry : hostGroup.getConfiguration().getProperties().entrySet()) {
                if (operationalConfigurations.containsKey(hostgroupConfigEntry.getKey())) {
                    operationalConfigurations.get(hostgroupConfigEntry.getKey()).putAll(hostgroupConfigEntry.getValue());
                } else {
                    operationalConfigurations.put(hostgroupConfigEntry.getKey(), hostgroupConfigEntry.getValue());
                }
            }
            for (java.lang.String hostGroupService : hostGroup.getServices()) {
                if (!requiredPropertiesByService.containsKey(hostGroupService)) {
                    org.apache.ambari.server.topology.validators.RequiredConfigPropertiesValidator.LOGGER.debug("There are no required properties found for hostgroup/service: [{}/{}]", hostGroup.getName(), hostGroupService);
                    continue;
                }
                java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> requiredPropertiesByType = requiredPropertiesByService.get(hostGroupService);
                for (java.lang.String configType : requiredPropertiesByType.keySet()) {
                    java.util.Collection<java.lang.String> requiredPropertiesForType = new java.util.HashSet(requiredPropertiesByType.get(configType));
                    if (!operationalConfigurations.containsKey(configType)) {
                        missingProperties = addTomissingProperties(missingProperties, hostGroup.getName(), requiredPropertiesForType);
                        continue;
                    }
                    java.util.Collection<java.lang.String> operationalConfigsForType = operationalConfigurations.get(configType).keySet();
                    requiredPropertiesForType.removeAll(operationalConfigsForType);
                    if (!requiredPropertiesForType.isEmpty()) {
                        org.apache.ambari.server.topology.validators.RequiredConfigPropertiesValidator.LOGGER.info("Found missing properties in hostgroup: {}, config type: {}, mising properties: {}", hostGroup.getName(), configType, requiredPropertiesForType);
                        missingProperties = addTomissingProperties(missingProperties, hostGroup.getName(), requiredPropertiesForType);
                    }
                }
            }
        }
        if (!missingProperties.isEmpty()) {
            throw new org.apache.ambari.server.topology.InvalidTopologyException(("Missing required properties.  Specify a value for these " + "properties in the blueprint or cluster creation template configuration. ") + missingProperties);
        }
    }

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Collection<java.lang.String>>> getRequiredPropertiesByService(org.apache.ambari.server.topology.Blueprint blueprint) {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Collection<java.lang.String>>> requiredPropertiesForServiceByType = new java.util.HashMap<>();
        for (java.lang.String bpService : blueprint.getServices()) {
            org.apache.ambari.server.topology.validators.RequiredConfigPropertiesValidator.LOGGER.debug("Collecting required properties for the service: {}", bpService);
            java.util.Collection<org.apache.ambari.server.controller.internal.Stack.ConfigProperty> requiredConfigsForService = blueprint.getStack().getRequiredConfigurationProperties(bpService);
            java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> requiredPropertiesByConfigType = new java.util.HashMap<>();
            for (org.apache.ambari.server.controller.internal.Stack.ConfigProperty configProperty : requiredConfigsForService) {
                if ((configProperty.getPropertyTypes() != null) && configProperty.getPropertyTypes().contains(org.apache.ambari.server.state.PropertyInfo.PropertyType.PASSWORD)) {
                    org.apache.ambari.server.topology.validators.RequiredConfigPropertiesValidator.LOGGER.debug("Skipping required property validation for password type: {}", configProperty.getName());
                    continue;
                }
                if (requiredPropertiesForServiceByType.containsKey(bpService)) {
                    requiredPropertiesByConfigType = requiredPropertiesForServiceByType.get(bpService);
                } else {
                    org.apache.ambari.server.topology.validators.RequiredConfigPropertiesValidator.LOGGER.debug("Adding required properties entry for service: {}", bpService);
                    requiredPropertiesForServiceByType.put(bpService, requiredPropertiesByConfigType);
                }
                java.util.Collection<java.lang.String> requiredPropsForType = new java.util.HashSet<>();
                if (requiredPropertiesByConfigType.containsKey(configProperty.getType())) {
                    requiredPropsForType = requiredPropertiesByConfigType.get(configProperty.getType());
                } else {
                    org.apache.ambari.server.topology.validators.RequiredConfigPropertiesValidator.LOGGER.debug("Adding required properties entry for configuration type: {}", configProperty.getType());
                    requiredPropertiesByConfigType.put(configProperty.getType(), requiredPropsForType);
                }
                requiredPropsForType.add(configProperty.getName());
                org.apache.ambari.server.topology.validators.RequiredConfigPropertiesValidator.LOGGER.debug("Added required property for service; {}, configuration type: {}, property: {}", bpService, configProperty.getType(), configProperty.getName());
            }
        }
        org.apache.ambari.server.topology.validators.RequiredConfigPropertiesValidator.LOGGER.info("Identified required properties for blueprint services: {}", requiredPropertiesForServiceByType);
        return requiredPropertiesForServiceByType;
    }

    private java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> addTomissingProperties(java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> missingProperties, java.lang.String hostGroup, java.util.Collection<java.lang.String> values) {
        java.util.Map<java.lang.String, java.util.Collection<java.lang.String>> missing;
        if (missingProperties == null) {
            missing = new java.util.TreeMap<>();
        } else {
            missing = new java.util.TreeMap<>(missingProperties);
        }
        if (!missing.containsKey(hostGroup)) {
            missing.put(hostGroup, new java.util.TreeSet<>());
        }
        missing.get(hostGroup).addAll(values);
        return missing;
    }
}