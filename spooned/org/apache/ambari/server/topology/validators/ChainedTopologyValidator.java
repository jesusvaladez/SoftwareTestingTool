package org.apache.ambari.server.topology.validators;
public class ChainedTopologyValidator implements org.apache.ambari.server.topology.TopologyValidator {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.topology.validators.ChainedTopologyValidator.class);

    private java.util.List<org.apache.ambari.server.topology.TopologyValidator> validators;

    public ChainedTopologyValidator(java.util.List<org.apache.ambari.server.topology.TopologyValidator> validators) {
        this.validators = validators;
    }

    @java.lang.Override
    public void validate(org.apache.ambari.server.topology.ClusterTopology topology) throws org.apache.ambari.server.topology.InvalidTopologyException {
        for (org.apache.ambari.server.topology.TopologyValidator validator : validators) {
            org.apache.ambari.server.topology.validators.ChainedTopologyValidator.LOGGER.info("Performing topology validation: {}", validator.getClass());
            validator.validate(topology);
        }
    }
}