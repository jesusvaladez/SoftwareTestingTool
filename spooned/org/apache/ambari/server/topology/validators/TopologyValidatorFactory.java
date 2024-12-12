package org.apache.ambari.server.topology.validators;
public class TopologyValidatorFactory {
    java.util.List<org.apache.ambari.server.topology.TopologyValidator> validators;

    public TopologyValidatorFactory() {
        validators = com.google.common.collect.ImmutableList.of(new org.apache.ambari.server.topology.validators.RequiredConfigPropertiesValidator(), new org.apache.ambari.server.topology.validators.RequiredPasswordValidator(), new org.apache.ambari.server.topology.validators.HiveServiceValidator(), new org.apache.ambari.server.topology.validators.StackConfigTypeValidator(), new org.apache.ambari.server.topology.validators.UnitValidator(org.apache.ambari.server.topology.validators.UnitValidatedProperty.ALL), new org.apache.ambari.server.topology.validators.NameNodeHaValidator());
    }

    public org.apache.ambari.server.topology.TopologyValidator createConfigurationValidatorChain() {
        return new org.apache.ambari.server.topology.validators.ChainedTopologyValidator(validators);
    }
}