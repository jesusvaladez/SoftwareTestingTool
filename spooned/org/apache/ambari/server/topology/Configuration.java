package org.apache.ambari.server.topology;
public class Configuration {
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.topology.Configuration.class);

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties;

    private java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> attributes;

    private org.apache.ambari.server.topology.Configuration parentConfiguration;

    public static org.apache.ambari.server.topology.Configuration newEmpty() {
        return new org.apache.ambari.server.topology.Configuration(new java.util.HashMap<>(), new java.util.HashMap<>());
    }

    public java.util.Set<java.lang.String> applyUpdatesToStackDefaultProperties(org.apache.ambari.server.topology.Configuration stackDefaultConfig, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> existingConfigurations, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> updatedConfigs) {
        java.util.Set<java.lang.String> updatedConfigTypes = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> stackDefaults = stackDefaultConfig.getProperties();
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configEntry : updatedConfigs.entrySet()) {
            java.lang.String configType = configEntry.getKey();
            java.util.Map<java.lang.String, java.lang.String> propertyMap = configEntry.getValue();
            java.util.Map<java.lang.String, java.lang.String> clusterConfigProperties = existingConfigurations.get(configType);
            java.util.Map<java.lang.String, java.lang.String> stackDefaultConfigProperties = stackDefaults.get(configType);
            for (java.util.Map.Entry<java.lang.String, java.lang.String> propertyEntry : propertyMap.entrySet()) {
                java.lang.String property = propertyEntry.getKey();
                java.lang.String newValue = propertyEntry.getValue();
                java.lang.String currentValue = getPropertyValue(configType, property);
                if ((!org.apache.ambari.server.topology.Configuration.propertyHasCustomValue(clusterConfigProperties, stackDefaultConfigProperties, property)) && (!java.util.Objects.equals(currentValue, newValue))) {
                    org.apache.ambari.server.topology.Configuration.LOG.debug("Update config property {}/{}: {} -> {}", configType, property, currentValue, newValue);
                    setProperty(configType, property, newValue);
                    updatedConfigTypes.add(configType);
                }
            }
        }
        return updatedConfigTypes;
    }

    private static boolean propertyHasCustomValue(java.util.Map<java.lang.String, java.lang.String> clusterConfigProperties, java.util.Map<java.lang.String, java.lang.String> stackDefaultConfigProperties, java.lang.String property) {
        boolean propertyHasCustomValue = false;
        if (clusterConfigProperties != null) {
            java.lang.String propertyValue = clusterConfigProperties.get(property);
            if (propertyValue != null) {
                if (stackDefaultConfigProperties != null) {
                    java.lang.String stackDefaultValue = stackDefaultConfigProperties.get(property);
                    if (stackDefaultValue != null) {
                        propertyHasCustomValue = !propertyValue.equals(stackDefaultValue);
                    } else {
                        propertyHasCustomValue = true;
                    }
                } else {
                    propertyHasCustomValue = true;
                }
            }
        }
        return propertyHasCustomValue;
    }

    public org.apache.ambari.server.topology.Configuration copy() {
        org.apache.ambari.server.topology.Configuration parent = parentConfiguration;
        parentConfiguration = null;
        org.apache.ambari.server.topology.Configuration copy = new org.apache.ambari.server.topology.Configuration(getFullProperties(), getFullAttributes());
        parentConfiguration = parent;
        return copy;
    }

    public Configuration(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> attributes, org.apache.ambari.server.topology.Configuration parentConfiguration) {
        this.properties = properties;
        this.attributes = attributes;
        this.parentConfiguration = parentConfiguration;
    }

    public Configuration(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> attributes) {
        this.properties = properties;
        this.attributes = attributes;
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getProperties() {
        return properties;
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getFullProperties() {
        return getFullProperties(java.lang.Integer.MAX_VALUE);
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> getFullProperties(int depthLimit) {
        if (depthLimit == 0) {
            java.util.HashMap<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> propertiesCopy = new java.util.HashMap<>();
            for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> typeProperties : properties.entrySet()) {
                propertiesCopy.put(typeProperties.getKey(), new java.util.HashMap<>(typeProperties.getValue()));
            }
            return propertiesCopy;
        }
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> mergedProperties = (parentConfiguration == null) ? new java.util.HashMap<>() : new java.util.HashMap<>(parentConfiguration.getFullProperties(--depthLimit));
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> entry : properties.entrySet()) {
            java.lang.String configType = entry.getKey();
            java.util.Map<java.lang.String, java.lang.String> typeProps = new java.util.HashMap<>(entry.getValue());
            if (mergedProperties.containsKey(configType)) {
                mergedProperties.get(configType).putAll(typeProps);
            } else {
                mergedProperties.put(configType, typeProps);
            }
        }
        return mergedProperties;
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> getAttributes() {
        return attributes;
    }

    public java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> getFullAttributes() {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> mergedAttributeMap = (parentConfiguration == null) ? new java.util.HashMap<>() : new java.util.HashMap<>(parentConfiguration.getFullAttributes());
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> typeEntry : attributes.entrySet()) {
            java.lang.String type = typeEntry.getKey();
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> typeAttributes = new java.util.HashMap<>();
            for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> attributeEntry : typeEntry.getValue().entrySet()) {
                typeAttributes.put(attributeEntry.getKey(), new java.util.HashMap<>(attributeEntry.getValue()));
            }
            if (!mergedAttributeMap.containsKey(type)) {
                mergedAttributeMap.put(type, typeAttributes);
            } else {
                java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> mergedAttributes = mergedAttributeMap.get(type);
                for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> attributeEntry : typeAttributes.entrySet()) {
                    java.lang.String attribute = attributeEntry.getKey();
                    if (!mergedAttributes.containsKey(attribute)) {
                        mergedAttributes.put(attribute, attributeEntry.getValue());
                    } else {
                        java.util.Map<java.lang.String, java.lang.String> mergedAttributeProps = mergedAttributes.get(attribute);
                        for (java.util.Map.Entry<java.lang.String, java.lang.String> propEntry : attributeEntry.getValue().entrySet()) {
                            mergedAttributeProps.put(propEntry.getKey(), propEntry.getValue());
                        }
                    }
                }
            }
        }
        return mergedAttributeMap;
    }

    public java.lang.String getPropertyValue(java.lang.String configType, java.lang.String propertyName) {
        java.lang.String value = null;
        if (properties.containsKey(configType) && properties.get(configType).containsKey(propertyName)) {
            value = properties.get(configType).get(propertyName);
        } else if (parentConfiguration != null) {
            value = parentConfiguration.getPropertyValue(configType, propertyName);
        }
        return value;
    }

    public java.lang.String getAttributeValue(java.lang.String configType, java.lang.String propertyName, java.lang.String attributeName) {
        java.lang.String value = null;
        if ((attributes.containsKey(configType) && attributes.get(configType).containsKey(attributeName)) && attributes.get(configType).get(attributeName).containsKey(propertyName)) {
            value = attributes.get(configType).get(attributeName).get(propertyName);
        } else if (parentConfiguration != null) {
            value = parentConfiguration.getAttributeValue(configType, propertyName, attributeName);
        }
        return value;
    }

    public java.lang.String setProperty(java.lang.String configType, java.lang.String propertyName, java.lang.String value) {
        java.lang.String previousValue = getPropertyValue(configType, propertyName);
        java.util.Map<java.lang.String, java.lang.String> typeProperties = properties.get(configType);
        if (typeProperties == null) {
            typeProperties = new java.util.HashMap<>();
            properties.put(configType, typeProperties);
        }
        typeProperties.put(propertyName, value);
        return previousValue;
    }

    public java.lang.String removeProperty(java.lang.String configType, java.lang.String propertyName) {
        java.lang.String previousValue = null;
        if (properties.containsKey(configType)) {
            previousValue = properties.get(configType).remove(propertyName);
        }
        if (parentConfiguration != null) {
            java.lang.String parentPreviousValue = parentConfiguration.removeProperty(configType, propertyName);
            if (previousValue == null) {
                previousValue = parentPreviousValue;
            }
        }
        return previousValue;
    }

    public java.util.Set<java.lang.String> moveProperties(java.lang.String sourceConfigType, java.lang.String targetConfigType, java.util.Set<java.lang.String> propertiesToMove) {
        java.util.Set<java.lang.String> moved = new java.util.HashSet<>();
        for (java.lang.String property : propertiesToMove) {
            if (isPropertySet(sourceConfigType, property)) {
                java.lang.String value = removeProperty(sourceConfigType, property);
                if (!isPropertySet(targetConfigType, property)) {
                    setProperty(targetConfigType, property, value);
                }
                moved.add(property);
            }
        }
        return moved;
    }

    public boolean isPropertySet(java.lang.String configType, java.lang.String propertyName) {
        return (properties.containsKey(configType) && properties.get(configType).containsKey(propertyName)) || ((parentConfiguration != null) && parentConfiguration.isPropertySet(configType, propertyName));
    }

    public java.lang.String setAttribute(java.lang.String configType, java.lang.String propertyName, java.lang.String attributeName, java.lang.String attributeValue) {
        java.lang.String previousValue = getAttributeValue(configType, propertyName, attributeName);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> typeAttributes = attributes.get(configType);
        if (typeAttributes == null) {
            typeAttributes = new java.util.HashMap<>();
            attributes.put(configType, typeAttributes);
        }
        java.util.Map<java.lang.String, java.lang.String> attributes = typeAttributes.get(attributeName);
        if (attributes == null) {
            attributes = new java.util.HashMap<>();
            typeAttributes.put(attributeName, attributes);
        }
        attributes.put(propertyName, attributeValue);
        return previousValue;
    }

    public java.util.Collection<java.lang.String> getAllConfigTypes() {
        java.util.Collection<java.lang.String> allTypes = new java.util.HashSet<>();
        allTypes.addAll(getFullProperties().keySet());
        allTypes.addAll(getFullAttributes().keySet());
        return allTypes;
    }

    public boolean containsConfigType(java.lang.String configType) {
        return (properties.containsKey(configType) || attributes.containsKey(configType)) || ((parentConfiguration != null) && parentConfiguration.containsConfigType(configType));
    }

    public boolean containsConfig(java.lang.String configType, java.lang.String propertyName) {
        return ((properties.containsKey(configType) && properties.get(configType).containsKey(propertyName)) || (attributes.containsKey(configType) && attributes.get(configType).values().stream().filter(map -> map.containsKey(propertyName)).findAny().isPresent())) || ((parentConfiguration != null) && parentConfiguration.containsConfig(configType, propertyName));
    }

    public org.apache.ambari.server.topology.Configuration getParentConfiguration() {
        return parentConfiguration;
    }

    public void setParentConfiguration(org.apache.ambari.server.topology.Configuration parent) {
        parentConfiguration = parent;
    }

    public void removeConfigType(java.lang.String configType) {
        if (properties != null) {
            properties.remove(configType);
        }
        if (attributes != null) {
            attributes.remove(configType);
        }
        if (parentConfiguration != null) {
            parentConfiguration.removeConfigType(configType);
        }
    }

    public static org.apache.ambari.server.topology.Configuration of(org.apache.commons.lang3.tuple.Pair<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>>> propertiesAndAttributes) {
        return new org.apache.ambari.server.topology.Configuration(propertiesAndAttributes.getLeft(), propertiesAndAttributes.getRight());
    }

    public org.apache.commons.lang3.tuple.Pair<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>>> asPair() {
        return org.apache.commons.lang3.tuple.Pair.of(properties, attributes);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        org.apache.ambari.server.topology.Configuration other = ((org.apache.ambari.server.topology.Configuration) (obj));
        return (java.util.Objects.equals(properties, other.properties) && java.util.Objects.equals(attributes, other.attributes)) && java.util.Objects.equals(parentConfiguration, other.parentConfiguration);
    }
}