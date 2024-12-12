package org.apache.ambari.server.controller.internal;
import static org.apache.commons.lang.StringUtils.isBlank;
public class UnitUpdater implements org.apache.ambari.server.controller.internal.BlueprintConfigurationProcessor.PropertyUpdater {
    private final java.lang.String serviceName;

    private final java.lang.String configType;

    public UnitUpdater(java.lang.String serviceName, java.lang.String configType) {
        this.serviceName = serviceName;
        this.configType = configType;
    }

    @java.lang.Override
    public java.lang.String updateForClusterCreate(java.lang.String propertyName, java.lang.String origValue, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties, org.apache.ambari.server.topology.ClusterTopology topology) {
        org.apache.ambari.server.controller.internal.Stack stack = topology.getBlueprint().getStack();
        return org.apache.ambari.server.controller.internal.UnitUpdater.updateForClusterCreate(stack, serviceName, configType, propertyName, origValue);
    }

    public static java.lang.String updateForClusterCreate(org.apache.ambari.server.controller.internal.Stack stack, java.lang.String serviceName, java.lang.String configType, java.lang.String propertyName, java.lang.String origValue) {
        org.apache.ambari.server.controller.internal.UnitUpdater.PropertyUnit stackUnit = org.apache.ambari.server.controller.internal.UnitUpdater.PropertyUnit.of(stack, serviceName, configType, propertyName);
        org.apache.ambari.server.controller.internal.UnitUpdater.PropertyValue value = org.apache.ambari.server.controller.internal.UnitUpdater.PropertyValue.of(propertyName, origValue);
        if (value.hasUnit(stackUnit)) {
            return value.toString();
        } else if (!value.hasAnyUnit()) {
            return value.withUnit(stackUnit);
        } else {
            throw new java.lang.IllegalArgumentException(((((("Property " + propertyName) + "=") + origValue) + " has an unsupported unit. Stack supported unit is: ") + stackUnit) + " or no unit");
        }
    }

    public static void updateUnits(org.apache.ambari.server.topology.Configuration configuration, org.apache.ambari.server.controller.internal.Stack stack) {
        org.apache.ambari.server.controller.internal.UnitUpdater.updateAllUnitValidatedProperties(configuration, (property, value) -> org.apache.ambari.server.controller.internal.UnitUpdater.updateForClusterCreate(stack, property.getServiceName(), property.getConfigType(), property.getPropertyName(), value));
    }

    public static void removeUnits(org.apache.ambari.server.topology.Configuration configuration, org.apache.ambari.server.controller.internal.Stack stack) {
        org.apache.ambari.server.controller.internal.UnitUpdater.updateAllUnitValidatedProperties(configuration, (property, value) -> org.apache.ambari.server.controller.internal.UnitUpdater.removeStackUnit(stack, property.getServiceName(), property.getConfigType(), property.getPropertyName(), value));
    }

    private static void updateAllUnitValidatedProperties(org.apache.ambari.server.topology.Configuration configuration, java.util.function.BiFunction<org.apache.ambari.server.topology.validators.UnitValidatedProperty, java.lang.String, java.lang.String> valueUpdater) {
        for (org.apache.ambari.server.topology.validators.UnitValidatedProperty p : org.apache.ambari.server.topology.validators.UnitValidatedProperty.ALL) {
            if (configuration.isPropertySet(p.getConfigType(), p.getPropertyName())) {
                java.lang.String value = configuration.getPropertyValue(p.getConfigType(), p.getPropertyName());
                java.lang.String updatedValue = valueUpdater.apply(p, value);
                if (!java.util.Objects.equals(value, updatedValue)) {
                    configuration.setProperty(p.getConfigType(), p.getPropertyName(), updatedValue);
                }
            }
        }
    }

    @java.lang.Override
    public java.lang.String updateForBlueprintExport(java.lang.String propertyName, java.lang.String origValue, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties, org.apache.ambari.server.topology.ClusterTopology topology) {
        return org.apache.ambari.server.controller.internal.UnitUpdater.removeStackUnit(topology.getBlueprint().getStack(), serviceName, configType, propertyName, origValue);
    }

    static java.lang.String removeStackUnit(org.apache.ambari.server.controller.internal.Stack stack, java.lang.String serviceName, java.lang.String configType, java.lang.String propertyName, java.lang.String origValue) {
        org.apache.ambari.server.controller.internal.UnitUpdater.PropertyUnit stackUnit = org.apache.ambari.server.controller.internal.UnitUpdater.PropertyUnit.of(stack, serviceName, configType, propertyName);
        org.apache.ambari.server.controller.internal.UnitUpdater.PropertyValue value = org.apache.ambari.server.controller.internal.UnitUpdater.PropertyValue.of(propertyName, origValue);
        return value.withoutUnit(stackUnit);
    }

    @java.lang.Override
    public java.util.Collection<java.lang.String> getRequiredHostGroups(java.lang.String propertyName, java.lang.String origValue, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties, org.apache.ambari.server.topology.ClusterTopology topology) {
        return java.util.Collections.emptySet();
    }

    public static class PropertyUnit {
        private static final java.lang.String DEFAULT_UNIT = "m";

        private final java.lang.String unit;

        public static org.apache.ambari.server.controller.internal.UnitUpdater.PropertyUnit of(org.apache.ambari.server.controller.internal.Stack stack, org.apache.ambari.server.topology.validators.UnitValidatedProperty property) {
            return org.apache.ambari.server.controller.internal.UnitUpdater.PropertyUnit.of(stack, property.getServiceName(), property.getConfigType(), property.getPropertyName());
        }

        public static org.apache.ambari.server.controller.internal.UnitUpdater.PropertyUnit of(org.apache.ambari.server.controller.internal.Stack stack, java.lang.String serviceName, java.lang.String configType, java.lang.String propertyName) {
            return new org.apache.ambari.server.controller.internal.UnitUpdater.PropertyUnit(org.apache.ambari.server.controller.internal.UnitUpdater.PropertyUnit.stackUnit(stack, serviceName, configType, propertyName).map(org.apache.ambari.server.controller.internal.UnitUpdater.PropertyUnit::toJvmUnit).orElse(org.apache.ambari.server.controller.internal.UnitUpdater.PropertyUnit.DEFAULT_UNIT));
        }

        private static java.util.Optional<java.lang.String> stackUnit(org.apache.ambari.server.controller.internal.Stack stack, java.lang.String serviceName, java.lang.String configType, java.lang.String propertyName) {
            try {
                return java.util.Optional.ofNullable(stack.getConfigurationPropertiesWithMetadata(serviceName, configType).get(propertyName).getPropertyValueAttributes().getUnit());
            } catch (java.lang.NullPointerException e) {
                return java.util.Optional.empty();
            }
        }

        private static java.lang.String toJvmUnit(java.lang.String stackUnit) {
            switch (stackUnit.toLowerCase()) {
                case "mb" :
                    return "m";
                case "gb" :
                    return "g";
                case "b" :
                case "bytes" :
                    return "";
                default :
                    throw new java.lang.IllegalArgumentException("Unsupported stack unit: " + stackUnit);
            }
        }

        private PropertyUnit(java.lang.String unit) {
            this.unit = unit;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return unit;
        }
    }

    public static class PropertyValue {
        private final java.lang.String value;

        public static org.apache.ambari.server.controller.internal.UnitUpdater.PropertyValue of(java.lang.String name, java.lang.String value) {
            return new org.apache.ambari.server.controller.internal.UnitUpdater.PropertyValue(org.apache.ambari.server.controller.internal.UnitUpdater.PropertyValue.normalized(name, value));
        }

        private static java.lang.String normalized(java.lang.String name, java.lang.String value) {
            if (StringUtils.isBlank(value)) {
                throw new java.lang.IllegalArgumentException("Missing property value " + name);
            }
            return value.trim().toLowerCase();
        }

        private PropertyValue(java.lang.String value) {
            this.value = value;
        }

        public boolean hasUnit(org.apache.ambari.server.controller.internal.UnitUpdater.PropertyUnit unit) {
            return value.endsWith(unit.toString());
        }

        public boolean hasAnyUnit() {
            return org.apache.ambari.server.controller.internal.UnitUpdater.PropertyValue.hasAnyUnit(value);
        }

        static boolean hasAnyUnit(java.lang.String value) {
            return !java.lang.Character.isDigit(value.charAt(value.length() - 1));
        }

        public java.lang.String withUnit(org.apache.ambari.server.controller.internal.UnitUpdater.PropertyUnit unit) {
            return value + unit;
        }

        public java.lang.String withoutUnit(org.apache.ambari.server.controller.internal.UnitUpdater.PropertyUnit unit) {
            return hasUnit(unit) ? value.substring(0, value.length() - unit.toString().length()) : value;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return value;
        }
    }
}