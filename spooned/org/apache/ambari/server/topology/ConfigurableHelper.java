package org.apache.ambari.server.topology;
import javax.annotation.Nullable;
import static org.apache.ambari.server.controller.internal.BlueprintResourceProvider.PROPERTIES_ATTRIBUTES_PROPERTY_ID;
import static org.apache.ambari.server.controller.internal.BlueprintResourceProvider.PROPERTIES_PROPERTY_ID;
public class ConfigurableHelper {
    private static final com.google.common.collect.ImmutableSet<java.lang.String> PERMITTED_CONFIG_FIELDS = com.google.common.collect.ImmutableSet.of(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.PROPERTIES_PROPERTY_ID, org.apache.ambari.server.controller.internal.BlueprintResourceProvider.PROPERTIES_ATTRIBUTES_PROPERTY_ID);

    public static org.apache.ambari.server.topology.Configuration parseConfigs(@javax.annotation.Nullable
    java.util.Collection<? extends java.util.Map<java.lang.String, ?>> configs) {
        org.apache.ambari.server.topology.Configuration configuration;
        if ((null == configs) || configs.isEmpty()) {
            configuration = org.apache.ambari.server.topology.Configuration.newEmpty();
        } else if (configs.iterator().next().keySet().iterator().next().contains("/")) {
            org.apache.ambari.server.topology.ConfigurableHelper.checkFlattenedConfig(configs);
            configuration = new org.apache.ambari.server.topology.ConfigurationFactory().getConfiguration(((java.util.Collection<java.util.Map<java.lang.String, java.lang.String>>) (configs)));
        } else {
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> allProperties = new java.util.HashMap<>();
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> allAttributes = new java.util.HashMap<>();
            configs.forEach(item -> {
                com.google.common.base.Preconditions.checkArgument(item.size() == 1, "Each config object must have a single property which is the name of the config," + " e.g. \"cluster-env\" : {...}");
                java.util.Map.Entry<java.lang.String, ?> configEntry = item.entrySet().iterator().next();
                java.lang.String configName = item.keySet().iterator().next();
                com.google.common.base.Preconditions.checkArgument(configEntry.getValue() instanceof java.util.Map, "The value for %s must be a JSON object (found: %s)", configName, org.apache.ambari.server.topology.ConfigurableHelper.getClassName(configEntry.getValue()));
                java.util.Map<java.lang.String, ?> configData = ((java.util.Map<java.lang.String, ?>) (configEntry.getValue()));
                java.util.Set<java.lang.String> extraKeys = com.google.common.collect.Sets.difference(configData.keySet(), org.apache.ambari.server.topology.ConfigurableHelper.PERMITTED_CONFIG_FIELDS);
                boolean legacy = extraKeys.size() == configData.keySet().size();
                com.google.common.base.Preconditions.checkArgument(legacy || extraKeys.isEmpty(), "Invalid fields in %s configuration: %s", configName, extraKeys);
                if (legacy) {
                    org.apache.ambari.server.topology.ConfigurableHelper.checkMap("don't care", configData, java.lang.String.class);
                    java.util.Map<java.lang.String, java.lang.String> properties = ((java.util.Map<java.lang.String, java.lang.String>) (configData));
                    allProperties.put(configName, properties);
                } else {
                    if (configData.containsKey(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.PROPERTIES_PROPERTY_ID)) {
                        org.apache.ambari.server.topology.ConfigurableHelper.checkMap(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.PROPERTIES_PROPERTY_ID, configData.get(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.PROPERTIES_PROPERTY_ID), java.lang.String.class);
                        java.util.Map<java.lang.String, java.lang.String> properties = ((java.util.Map<java.lang.String, java.lang.String>) (configData.get(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.PROPERTIES_PROPERTY_ID)));
                        allProperties.put(configName, properties);
                    }
                    if (configData.containsKey(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.PROPERTIES_ATTRIBUTES_PROPERTY_ID)) {
                        org.apache.ambari.server.topology.ConfigurableHelper.checkMap(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.PROPERTIES_ATTRIBUTES_PROPERTY_ID, configData.get(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.PROPERTIES_ATTRIBUTES_PROPERTY_ID), java.util.Map.class);
                        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> attributes = ((java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>) (configData.get(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.PROPERTIES_ATTRIBUTES_PROPERTY_ID)));
                        attributes.forEach((key, value) -> org.apache.ambari.server.topology.ConfigurableHelper.checkMap(key, value, java.lang.String.class));
                        allAttributes.put(configName, attributes);
                    }
                }
            });
            configuration = new org.apache.ambari.server.topology.Configuration(allProperties, allAttributes);
        }
        return configuration;
    }

    public static java.util.Collection<java.util.Map<java.lang.String, java.util.Map<java.lang.String, ?>>> convertConfigToMap(org.apache.ambari.server.topology.Configuration configuration) {
        java.util.Collection<java.util.Map<java.lang.String, java.util.Map<java.lang.String, ?>>> configurations = new java.util.ArrayList<>();
        java.util.Set<java.lang.String> allConfigTypes = com.google.common.collect.Sets.union(configuration.getProperties().keySet(), configuration.getAttributes().keySet());
        for (java.lang.String configType : allConfigTypes) {
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, ?>> configData = new java.util.HashMap<>();
            if (configuration.getProperties().containsKey(configType)) {
                configData.put(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.PROPERTIES_PROPERTY_ID, configuration.getProperties().get(configType));
            }
            if (configuration.getAttributes().containsKey(configType)) {
                configData.put(org.apache.ambari.server.controller.internal.BlueprintResourceProvider.PROPERTIES_ATTRIBUTES_PROPERTY_ID, configuration.getAttributes().get(configType));
            }
            configurations.add(com.google.common.collect.ImmutableMap.of(configType, configData));
        }
        return configurations;
    }

    private static void checkFlattenedConfig(java.util.Collection<? extends java.util.Map<java.lang.String, ?>> configs) {
        configs.forEach(config -> {
            if (!config.isEmpty()) {
                java.util.List<java.lang.String> firstKey = org.apache.ambari.server.topology.ConfigurationFactory.splitConfigurationKey(config.keySet().iterator().next());
                java.lang.String configType = firstKey.get(0);
                boolean legacyConfig = org.apache.ambari.server.topology.ConfigurationFactory.isKeyInLegacyFormat(firstKey);
                config.keySet().forEach(key -> {
                    java.util.List<java.lang.String> keyParts = org.apache.ambari.server.topology.ConfigurationFactory.splitConfigurationKey(key);
                    com.google.common.base.Preconditions.checkArgument(java.util.Objects.equals(configType, keyParts.get(0)), "Invalid config type: %s. Should be: %s", keyParts.get(0), configType);
                    com.google.common.base.Preconditions.checkArgument((legacyConfig && org.apache.ambari.server.topology.ConfigurationFactory.isKeyInLegacyFormat(keyParts)) || ((!legacyConfig) && org.apache.ambari.server.topology.ConfigurationFactory.isKeyInNewFormat(keyParts)), "Expected key in %s format, found [%s]", legacyConfig ? "[config_type/property_name]" : "[config_type/properties/config_name] or [config_type/properties_attributes/attribute_name/property_name]", key);
                });
            }
        });
    }

    private static void checkMap(java.lang.String fieldName, java.lang.Object mapObj, java.lang.Class<?> valueType) {
        com.google.common.base.Preconditions.checkArgument(mapObj instanceof java.util.Map, "'%s' needs to be a JSON object. Found: %s", fieldName, org.apache.ambari.server.topology.ConfigurableHelper.getClassName(mapObj));
        java.util.Map<?, ?> map = ((java.util.Map<?, ?>) (mapObj));
        map.forEach((__, value) -> com.google.common.base.Preconditions.checkArgument(valueType.isInstance(value), "Expected %s as value type, found %s, type: %s", valueType.getName(), value, org.apache.ambari.server.topology.ConfigurableHelper.getClassName(value)));
    }

    private static java.lang.String getClassName(java.lang.Object object) {
        return null != object ? object.getClass().getName() : null;
    }

    public static java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> transformAttributesMap(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> input) {
        return input.entrySet().stream().flatMap(outer -> outer.getValue().entrySet().stream().map(inner -> org.apache.commons.lang3.tuple.Triple.of(outer.getKey(), inner.getKey(), inner.getValue()))).collect(java.util.stream.Collectors.groupingBy(org.apache.commons.lang3.tuple.Triple::getMiddle, java.util.stream.Collectors.toMap(org.apache.commons.lang3.tuple.Triple::getLeft, org.apache.commons.lang3.tuple.Triple::getRight)));
    }
}