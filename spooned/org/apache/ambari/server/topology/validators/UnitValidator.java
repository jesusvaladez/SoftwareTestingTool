package org.apache.ambari.server.topology.validators;
public class UnitValidator implements org.apache.ambari.server.topology.TopologyValidator {
    private final java.util.Set<org.apache.ambari.server.topology.validators.UnitValidatedProperty> relevantProps;

    public UnitValidator(java.util.Set<org.apache.ambari.server.topology.validators.UnitValidatedProperty> propertiesToBeValidated) {
        this.relevantProps = propertiesToBeValidated;
    }

    @java.lang.Override
    public void validate(org.apache.ambari.server.topology.ClusterTopology topology) throws org.apache.ambari.server.topology.InvalidTopologyException {
        org.apache.ambari.server.controller.internal.Stack stack = topology.getBlueprint().getStack();
        validateConfig(topology.getConfiguration().getFullProperties(), stack);
        for (org.apache.ambari.server.topology.HostGroupInfo hostGroup : topology.getHostGroupInfo().values()) {
            validateConfig(hostGroup.getConfiguration().getFullProperties(), stack);
        }
    }

    private void validateConfig(java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configuration, org.apache.ambari.server.controller.internal.Stack stack) {
        for (java.util.Map.Entry<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> each : configuration.entrySet()) {
            validateConfigType(each.getKey(), each.getValue(), stack);
        }
    }

    private void validateConfigType(java.lang.String configType, java.util.Map<java.lang.String, java.lang.String> config, org.apache.ambari.server.controller.internal.Stack stack) {
        for (java.lang.String propertyName : config.keySet()) {
            validateProperty(configType, config, propertyName, stack);
        }
    }

    private void validateProperty(java.lang.String configType, java.util.Map<java.lang.String, java.lang.String> config, java.lang.String propertyName, org.apache.ambari.server.controller.internal.Stack stack) {
        relevantProps.stream().filter(each -> each.hasTypeAndName(configType, propertyName)).findFirst().ifPresent(relevantProperty -> checkUnit(config, stack, relevantProperty));
    }

    private void checkUnit(java.util.Map<java.lang.String, java.lang.String> configToBeValidated, org.apache.ambari.server.controller.internal.Stack stack, org.apache.ambari.server.topology.validators.UnitValidatedProperty prop) {
        org.apache.ambari.server.controller.internal.UnitUpdater.PropertyUnit stackUnit = org.apache.ambari.server.controller.internal.UnitUpdater.PropertyUnit.of(stack, prop);
        org.apache.ambari.server.controller.internal.UnitUpdater.PropertyValue value = org.apache.ambari.server.controller.internal.UnitUpdater.PropertyValue.of(prop.getPropertyName(), configToBeValidated.get(prop.getPropertyName()));
        if (value.hasAnyUnit() && (!value.hasUnit(stackUnit))) {
            throw new java.lang.IllegalArgumentException(((((("Property " + prop.getPropertyName()) + "=") + value) + " has an unsupported unit. Stack supported unit is: ") + stackUnit) + " or no unit");
        }
    }
}