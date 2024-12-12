package org.apache.ambari.server.topology;
public interface BlueprintValidator {
    void validateTopology() throws org.apache.ambari.server.topology.InvalidTopologyException;

    void validateRequiredProperties() throws org.apache.ambari.server.topology.InvalidTopologyException, org.apache.ambari.server.topology.GPLLicenseNotAcceptedException;
}