package org.apache.ambari.server.topology.validators;
public class TopologyValidatorService {
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.topology.validators.TopologyValidatorService.class);

    @javax.inject.Inject
    private org.apache.ambari.server.topology.validators.TopologyValidatorFactory topologyValidatorFactory;

    public TopologyValidatorService() {
    }

    public void validateTopologyConfiguration(org.apache.ambari.server.topology.ClusterTopology clusterTopology) throws org.apache.ambari.server.topology.InvalidTopologyException {
        org.apache.ambari.server.topology.validators.TopologyValidatorService.LOGGER.info("Validating cluster topology: {}", clusterTopology);
        topologyValidatorFactory.createConfigurationValidatorChain().validate(clusterTopology);
    }
}