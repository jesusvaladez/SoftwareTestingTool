package org.apache.ambari.msi;
public class NoOpProviderTest {
    @org.junit.Test
    public void testGetKeyPropertyIds() throws java.lang.Exception {
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(new org.apache.ambari.msi.TestStateProvider(), new org.apache.ambari.scom.TestClusterDefinitionProvider(), new org.apache.ambari.scom.TestHostInfoProvider());
        org.apache.ambari.msi.NoOpProvider provider = new org.apache.ambari.msi.NoOpProvider(org.apache.ambari.server.controller.spi.Resource.Type.Workflow, clusterDefinition);
        junit.framework.Assert.assertNotNull(provider.getKeyPropertyIds());
    }

    @org.junit.Test
    public void testCheckPropertyIds() throws java.lang.Exception {
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(new org.apache.ambari.msi.TestStateProvider(), new org.apache.ambari.scom.TestClusterDefinitionProvider(), new org.apache.ambari.scom.TestHostInfoProvider());
        org.apache.ambari.msi.NoOpProvider provider = new org.apache.ambari.msi.NoOpProvider(org.apache.ambari.server.controller.spi.Resource.Type.Workflow, clusterDefinition);
        junit.framework.Assert.assertTrue(provider.checkPropertyIds(java.util.Collections.singleton("id")).isEmpty());
    }
}