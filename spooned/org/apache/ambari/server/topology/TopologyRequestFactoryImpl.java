package org.apache.ambari.server.topology;
public class TopologyRequestFactoryImpl implements org.apache.ambari.server.topology.TopologyRequestFactory {
    @java.lang.Override
    public org.apache.ambari.server.controller.internal.ProvisionClusterRequest createProvisionClusterRequest(java.util.Map<java.lang.String, java.lang.Object> properties, org.apache.ambari.server.topology.SecurityConfiguration securityConfiguration) throws org.apache.ambari.server.topology.InvalidTopologyTemplateException {
        return new org.apache.ambari.server.controller.internal.ProvisionClusterRequest(properties, securityConfiguration);
    }
}