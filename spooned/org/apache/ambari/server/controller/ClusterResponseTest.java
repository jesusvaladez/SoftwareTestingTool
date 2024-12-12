package org.apache.ambari.server.controller;
public class ClusterResponseTest {
    @org.junit.Test
    public void testBasicGetAndSet() {
        long clusterId = 10L;
        java.lang.String clusterName = "foo";
        org.apache.ambari.server.state.State provisioningState = org.apache.ambari.server.state.State.INSTALLED;
        org.apache.ambari.server.state.SecurityType securityType = org.apache.ambari.server.state.SecurityType.KERBEROS;
        java.util.Set<java.lang.String> hostNames = new java.util.HashSet<>();
        hostNames.add("h1");
        org.apache.ambari.server.controller.ClusterResponse r1 = new org.apache.ambari.server.controller.ClusterResponse(clusterId, clusterName, provisioningState, securityType, hostNames, hostNames.size(), "bar", null);
        org.junit.Assert.assertEquals(clusterId, r1.getClusterId());
        org.junit.Assert.assertEquals(clusterName, r1.getClusterName());
        org.junit.Assert.assertEquals(provisioningState, r1.getProvisioningState());
        org.junit.Assert.assertEquals(securityType, r1.getSecurityType());
        org.junit.Assert.assertEquals(1, r1.getTotalHosts());
        org.junit.Assert.assertEquals("bar", r1.getDesiredStackVersion());
    }

    @org.junit.Test
    public void testToString() {
        org.apache.ambari.server.controller.ClusterResponse r = new org.apache.ambari.server.controller.ClusterResponse(0, null, null, null, null, 0, null, null);
        r.toString();
    }
}