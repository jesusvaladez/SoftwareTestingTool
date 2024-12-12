package org.apache.ambari.server.controller;
public class ClusterRequestTest {
    @org.junit.Test
    public void testBasicGetAndSet() {
        java.lang.Long clusterId = new java.lang.Long(10);
        java.lang.String clusterName = "foo";
        java.lang.String provisioningState = org.apache.ambari.server.state.State.INIT.name();
        org.apache.ambari.server.state.SecurityType securityType = org.apache.ambari.server.state.SecurityType.NONE;
        org.apache.ambari.server.state.StackId stackVersion = new org.apache.ambari.server.state.StackId("HDP-1.0.1");
        java.util.Set<java.lang.String> hostNames = new java.util.HashSet<>();
        hostNames.add("h1");
        org.apache.ambari.server.controller.ClusterRequest r1 = new org.apache.ambari.server.controller.ClusterRequest(clusterId, clusterName, provisioningState, securityType, stackVersion.getStackId(), hostNames);
        org.junit.Assert.assertEquals(clusterId, r1.getClusterId());
        org.junit.Assert.assertEquals(clusterName, r1.getClusterName());
        org.junit.Assert.assertEquals(provisioningState, r1.getProvisioningState());
        org.junit.Assert.assertEquals(securityType, r1.getSecurityType());
        org.junit.Assert.assertEquals(stackVersion.getStackId(), r1.getStackVersion());
        org.junit.Assert.assertArrayEquals(hostNames.toArray(), r1.getHostNames().toArray());
    }

    @org.junit.Test
    public void testToString() {
        org.apache.ambari.server.controller.ClusterRequest r1 = new org.apache.ambari.server.controller.ClusterRequest(null, null, null, null, null, null);
        r1.toString();
    }
}