package org.apache.ambari.server.topology;
public interface TopologyRequestFactory {
    org.apache.ambari.server.controller.internal.ProvisionClusterRequest createProvisionClusterRequest(java.util.Map<java.lang.String, java.lang.Object> properties, org.apache.ambari.server.topology.SecurityConfiguration securityConfiguration) throws org.apache.ambari.server.topology.InvalidTopologyTemplateException;
}