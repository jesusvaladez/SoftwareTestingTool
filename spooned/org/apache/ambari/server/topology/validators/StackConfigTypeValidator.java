package org.apache.ambari.server.topology.validators;
public class StackConfigTypeValidator implements org.apache.ambari.server.topology.TopologyValidator {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.topology.validators.StackConfigTypeValidator.class);

    public StackConfigTypeValidator() {
    }

    @java.lang.Override
    public void validate(org.apache.ambari.server.topology.ClusterTopology topology) throws org.apache.ambari.server.topology.InvalidTopologyException {
        java.util.Set<java.lang.String> incomingConfigTypes = new java.util.HashSet<>(topology.getConfiguration().getAllConfigTypes());
        if (incomingConfigTypes.isEmpty()) {
            org.apache.ambari.server.topology.validators.StackConfigTypeValidator.LOGGER.debug("No config types to be checked.");
            return;
        }
        java.util.Set<java.lang.String> stackConfigTypes = new java.util.HashSet<>(topology.getBlueprint().getStack().getConfiguration().getAllConfigTypes());
        incomingConfigTypes.removeAll(stackConfigTypes);
        if (!incomingConfigTypes.isEmpty()) {
            java.lang.String message = java.lang.String.format("The following config types are not defined in the stack: %s ", incomingConfigTypes);
            org.apache.ambari.server.topology.validators.StackConfigTypeValidator.LOGGER.error(message);
            throw new org.apache.ambari.server.topology.InvalidTopologyException(message);
        }
    }
}