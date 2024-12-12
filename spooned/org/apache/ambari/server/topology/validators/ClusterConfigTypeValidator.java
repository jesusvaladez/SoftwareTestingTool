package org.apache.ambari.server.topology.validators;
public class ClusterConfigTypeValidator implements org.apache.ambari.server.topology.TopologyValidator {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.topology.validators.ClusterConfigTypeValidator.class);

    @java.lang.Override
    public void validate(org.apache.ambari.server.topology.ClusterTopology topology) throws org.apache.ambari.server.topology.InvalidTopologyException {
        java.util.Set<java.lang.String> topologyClusterConfigTypes = new java.util.HashSet(topology.getConfiguration().getAllConfigTypes());
        org.apache.ambari.server.topology.validators.ClusterConfigTypeValidator.LOGGER.debug("Cluster config types: {}", topologyClusterConfigTypes);
        if (topologyClusterConfigTypes.isEmpty()) {
            org.apache.ambari.server.topology.validators.ClusterConfigTypeValidator.LOGGER.debug("No config types to be checked.");
            return;
        }
        java.util.Set<java.lang.String> stackServiceConfigTypes = new java.util.HashSet<>();
        for (java.lang.String serviceName : topology.getBlueprint().getServices()) {
            stackServiceConfigTypes.addAll(topology.getBlueprint().getStack().getConfigurationTypes(serviceName));
        }
        java.util.Set<java.lang.String> configTypeIntersection = new java.util.HashSet<>(topologyClusterConfigTypes);
        if (configTypeIntersection.retainAll(stackServiceConfigTypes)) {
            java.util.Set<java.lang.String> invalidConfigTypes = new java.util.HashSet<>(topologyClusterConfigTypes);
            invalidConfigTypes.removeAll(configTypeIntersection);
            org.apache.ambari.server.topology.validators.ClusterConfigTypeValidator.LOGGER.error("The following config typess are wrong: {}", invalidConfigTypes);
            throw new org.apache.ambari.server.topology.InvalidTopologyException("The following configuration types are invalid: " + invalidConfigTypes);
        }
    }
}