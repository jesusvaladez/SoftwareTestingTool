package org.apache.ambari.server.controller.internal;
public enum BlueprintExportType {

    FULL() {
        @java.lang.Override
        public org.apache.ambari.server.topology.Configuration filter(org.apache.ambari.server.topology.Configuration actualConfig, org.apache.ambari.server.topology.Configuration defaultConfig) {
            return actualConfig;
        }

        @java.lang.Override
        public boolean include(java.lang.String value, java.lang.String defaultValue) {
            return true;
        }

        @java.lang.Override
        public boolean include(java.util.Collection<?> collection) {
            return true;
        }

        @java.lang.Override
        public boolean include(java.util.Map<?, ?> map) {
            return true;
        }
    },
    MINIMAL() {
        @java.lang.Override
        public org.apache.ambari.server.topology.Configuration filter(org.apache.ambari.server.topology.Configuration actualConfig, org.apache.ambari.server.topology.Configuration defaultConfig) {
            for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configTypeEntry : com.google.common.collect.ImmutableSet.copyOf(actualConfig.getProperties().entrySet())) {
                java.lang.String configType = configTypeEntry.getKey();
                java.util.Map<java.lang.String, java.lang.String> properties = configTypeEntry.getValue();
                for (java.util.Map.Entry<java.lang.String, java.lang.String> propertyEntry : com.google.common.collect.ImmutableSet.copyOf(properties.entrySet())) {
                    java.lang.String propertyName = propertyEntry.getKey();
                    java.lang.String propertyValue = propertyEntry.getValue();
                    java.lang.String defaultValue = defaultConfig.getPropertyValue(configType, propertyName);
                    if (include(propertyValue, defaultValue)) {
                        org.apache.ambari.server.controller.internal.BlueprintExportType.LOG.debug("Including {}/{} in exported blueprint, as default value and actual value differ:\n{}\nvs\n{}", configType, propertyName, defaultValue, propertyValue);
                    } else {
                        org.apache.ambari.server.controller.internal.BlueprintExportType.LOG.debug("Omitting {}/{} from exported blueprint, as it has the default value of {}", configType, propertyName, propertyValue);
                        actualConfig.removeProperty(configType, propertyName);
                    }
                }
                if (properties.isEmpty()) {
                    actualConfig.getProperties().remove(configType);
                }
            }
            for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> configTypeEntry : com.google.common.collect.ImmutableSet.copyOf(actualConfig.getAttributes().entrySet())) {
                java.lang.String configType = configTypeEntry.getKey();
                java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> attributes = configTypeEntry.getValue();
                for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> attributeEntry : com.google.common.collect.ImmutableSet.copyOf(attributes.entrySet())) {
                    java.lang.String attributeName = attributeEntry.getKey();
                    java.util.Map<java.lang.String, java.lang.String> properties = attributeEntry.getValue();
                    for (java.util.Map.Entry<java.lang.String, java.lang.String> propertyEntry : com.google.common.collect.ImmutableSet.copyOf(properties.entrySet())) {
                        java.lang.String propertyName = propertyEntry.getKey();
                        java.lang.String attributeValue = propertyEntry.getValue();
                        java.lang.String defaultValue = defaultConfig.getAttributeValue(configType, propertyName, attributeName);
                        if (include(attributeValue, defaultValue)) {
                            org.apache.ambari.server.controller.internal.BlueprintExportType.LOG.debug("Including {}/{}/{} in exported blueprint, as default value and actual value differ:\n{}\nvs\n{}", configType, attributeName, propertyName, defaultValue, attributeValue);
                        } else {
                            org.apache.ambari.server.controller.internal.BlueprintExportType.LOG.debug("Omitting {}/{}/{} from exported blueprint, as it has the default value of {}", configType, attributeName, propertyName, attributeValue);
                            properties.remove(propertyName);
                        }
                    }
                    if (properties.isEmpty()) {
                        attributes.remove(attributeName);
                    }
                }
                if (attributes.isEmpty()) {
                    actualConfig.getAttributes().remove(configType);
                }
            }
            return actualConfig;
        }

        @java.lang.Override
        public boolean include(java.lang.String value, java.lang.String defaultValue) {
            return (value != null) && ((defaultValue == null) || (!java.util.Objects.equals(org.apache.commons.lang3.StringUtils.trim(defaultValue), org.apache.commons.lang3.StringUtils.trim(value))));
        }

        @java.lang.Override
        public boolean include(java.util.Collection<?> collection) {
            return (collection != null) && (!collection.isEmpty());
        }

        @java.lang.Override
        public boolean include(java.util.Map<?, ?> map) {
            return (map != null) && (!map.isEmpty());
        }
    };
    public abstract org.apache.ambari.server.topology.Configuration filter(org.apache.ambari.server.topology.Configuration actualConfig, org.apache.ambari.server.topology.Configuration defaultConfig);

    public abstract boolean include(java.lang.String value, java.lang.String defaultValue);

    public abstract boolean include(java.util.Collection<?> collection);

    public abstract boolean include(java.util.Map<?, ?> map);

    public static final org.apache.ambari.server.controller.internal.BlueprintExportType DEFAULT = org.apache.ambari.server.controller.internal.BlueprintExportType.MINIMAL;

    public static final java.lang.String PREFIX = "blueprint";

    private static final java.lang.String SEPARATOR = "_";

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.controller.internal.BlueprintExportType.class);

    public static java.util.Optional<org.apache.ambari.server.controller.internal.BlueprintExportType> parse(java.lang.String input) {
        if ((input == null) || (!input.startsWith(org.apache.ambari.server.controller.internal.BlueprintExportType.PREFIX))) {
            return java.util.Optional.empty();
        }
        int separatorPos = input.indexOf(org.apache.ambari.server.controller.internal.BlueprintExportType.SEPARATOR);
        if ((separatorPos == (-1)) || ((separatorPos + 1) == input.length())) {
            return java.util.Optional.of(org.apache.ambari.server.controller.internal.BlueprintExportType.DEFAULT);
        }
        switch (input.substring(separatorPos + 1)) {
            case "full" :
                return java.util.Optional.of(org.apache.ambari.server.controller.internal.BlueprintExportType.FULL);
            case "minimal" :
                return java.util.Optional.of(org.apache.ambari.server.controller.internal.BlueprintExportType.MINIMAL);
            default :
                return java.util.Optional.of(org.apache.ambari.server.controller.internal.BlueprintExportType.DEFAULT);
        }
    }
}