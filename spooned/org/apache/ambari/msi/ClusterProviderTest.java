package org.apache.ambari.msi;
public class ClusterProviderTest {
    @org.junit.Test
    public void testGetResources() throws java.lang.Exception {
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(new org.apache.ambari.msi.TestStateProvider(), new org.apache.ambari.scom.TestClusterDefinitionProvider(), new org.apache.ambari.scom.TestHostInfoProvider());
        org.apache.ambari.msi.ClusterProvider provider = new org.apache.ambari.msi.ClusterProvider(clusterDefinition);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), null);
        junit.framework.Assert.assertEquals(1, resources.size());
        junit.framework.Assert.assertEquals("myCluster", resources.iterator().next().getPropertyValue(org.apache.ambari.msi.ClusterProvider.CLUSTER_NAME_PROPERTY_ID));
    }

    @org.junit.Test
    public void testGetResourcesWithPredicate() throws java.lang.Exception {
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(new org.apache.ambari.msi.TestStateProvider(), new org.apache.ambari.scom.TestClusterDefinitionProvider(), new org.apache.ambari.scom.TestHostInfoProvider());
        org.apache.ambari.msi.ClusterProvider provider = new org.apache.ambari.msi.ClusterProvider(clusterDefinition);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.msi.ClusterProvider.CLUSTER_NAME_PROPERTY_ID).equals("myCluster").toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), predicate);
        junit.framework.Assert.assertEquals(1, resources.size());
        org.apache.ambari.server.controller.spi.Resource next = resources.iterator().next();
        junit.framework.Assert.assertEquals("myCluster", next.getPropertyValue(org.apache.ambari.msi.ClusterProvider.CLUSTER_NAME_PROPERTY_ID));
        junit.framework.Assert.assertEquals("HDP-1.2.9", next.getPropertyValue(org.apache.ambari.msi.ClusterProvider.CLUSTER_VERSION_PROPERTY_ID));
        predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.msi.ClusterProvider.CLUSTER_NAME_PROPERTY_ID).equals("non-existent Cluster").toPredicate();
        resources = provider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), predicate);
        junit.framework.Assert.assertTrue(resources.isEmpty());
    }

    @org.junit.Test
    public void testCreateResources() throws java.lang.Exception {
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(new org.apache.ambari.msi.TestStateProvider(), new org.apache.ambari.scom.TestClusterDefinitionProvider(), new org.apache.ambari.scom.TestHostInfoProvider());
        org.apache.ambari.msi.ClusterProvider provider = new org.apache.ambari.msi.ClusterProvider(clusterDefinition);
        try {
            provider.createResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest());
            junit.framework.Assert.fail("Expected UnsupportedOperationException.");
        } catch (java.lang.UnsupportedOperationException e) {
        }
    }

    @org.junit.Test
    public void testUpdateResources() throws java.lang.Exception {
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(new org.apache.ambari.msi.TestStateProvider(), new org.apache.ambari.scom.TestClusterDefinitionProvider(), new org.apache.ambari.scom.TestHostInfoProvider());
        org.apache.ambari.msi.ClusterProvider provider = new org.apache.ambari.msi.ClusterProvider(clusterDefinition);
        provider.updateResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(new java.util.HashMap<java.lang.String, java.lang.Object>(), null), null);
    }

    @org.junit.Test
    public void testDeleteResources() throws java.lang.Exception {
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(new org.apache.ambari.msi.TestStateProvider(), new org.apache.ambari.scom.TestClusterDefinitionProvider(), new org.apache.ambari.scom.TestHostInfoProvider());
        org.apache.ambari.msi.ClusterProvider provider = new org.apache.ambari.msi.ClusterProvider(clusterDefinition);
        try {
            provider.deleteResources(null);
            junit.framework.Assert.fail("Expected UnsupportedOperationException.");
        } catch (java.lang.UnsupportedOperationException e) {
        }
    }
}