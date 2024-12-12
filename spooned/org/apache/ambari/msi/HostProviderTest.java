package org.apache.ambari.msi;
public class HostProviderTest {
    @org.junit.Test
    public void testGetResources() throws java.lang.Exception {
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(new org.apache.ambari.msi.TestStateProvider(), new org.apache.ambari.scom.TestClusterDefinitionProvider(), new org.apache.ambari.scom.TestHostInfoProvider());
        org.apache.ambari.msi.HostProvider provider = new org.apache.ambari.msi.HostProvider(clusterDefinition);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), null);
        junit.framework.Assert.assertEquals(13, resources.size());
    }

    @org.junit.Test
    public void testGetResourcesWithPredicate() throws java.lang.Exception {
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(new org.apache.ambari.msi.TestStateProvider(), new org.apache.ambari.scom.TestClusterDefinitionProvider(), new org.apache.ambari.scom.TestHostInfoProvider());
        org.apache.ambari.msi.HostProvider provider = new org.apache.ambari.msi.HostProvider(clusterDefinition);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.msi.HostProvider.HOST_NAME_PROPERTY_ID).equals("NAMENODE_MASTER.acme.com").toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), predicate);
        junit.framework.Assert.assertEquals(1, resources.size());
        predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.msi.HostProvider.HOST_NAME_PROPERTY_ID).equals("HBASE_MASTER.acme.com").or().property(org.apache.ambari.msi.HostProvider.HOST_NAME_PROPERTY_ID).equals("slave3.acme.com").toPredicate();
        resources = provider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), predicate);
        junit.framework.Assert.assertEquals(2, resources.size());
        predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.msi.HostProvider.HOST_NAME_PROPERTY_ID).equals("unknownHost").toPredicate();
        resources = provider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), predicate);
        junit.framework.Assert.assertTrue(resources.isEmpty());
    }

    @org.junit.Test
    public void testGetResourcesHostIP() throws java.lang.Exception {
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(new org.apache.ambari.msi.TestStateProvider(), new org.apache.ambari.scom.TestClusterDefinitionProvider(), new org.apache.ambari.scom.TestHostInfoProvider());
        org.apache.ambari.msi.HostProvider provider = new org.apache.ambari.msi.HostProvider(clusterDefinition);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.msi.HostProvider.HOST_NAME_PROPERTY_ID).equals("NAMENODE_MASTER.acme.com").toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), predicate);
        junit.framework.Assert.assertEquals(1, resources.size());
        org.apache.ambari.server.controller.spi.Resource resource = resources.iterator().next();
        java.lang.String ip = ((java.lang.String) (resource.getPropertyValue(org.apache.ambari.msi.HostProvider.HOST_IP_PROPERTY_ID)));
        junit.framework.Assert.assertEquals("127.0.0.1", ip);
    }

    @org.junit.Test
    public void testGetResourcesCheckState() throws java.lang.Exception {
        org.apache.ambari.msi.TestStateProvider stateProvider = new org.apache.ambari.msi.TestStateProvider();
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(stateProvider, new org.apache.ambari.scom.TestClusterDefinitionProvider(), new org.apache.ambari.scom.TestHostInfoProvider());
        org.apache.ambari.msi.HostProvider provider = new org.apache.ambari.msi.HostProvider(clusterDefinition);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.msi.HostProvider.HOST_NAME_PROPERTY_ID).equals("slave3.acme.com").toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), predicate);
        junit.framework.Assert.assertEquals(1, resources.size());
        org.apache.ambari.server.controller.spi.Resource resource = resources.iterator().next();
        junit.framework.Assert.assertEquals("HEALTHY", resource.getPropertyValue(org.apache.ambari.msi.HostProvider.HOST_STATE_PROPERTY_ID));
        stateProvider.setState(org.apache.ambari.msi.StateProvider.State.Unknown);
        resources = provider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), predicate);
        junit.framework.Assert.assertEquals(1, resources.size());
        resource = resources.iterator().next();
        junit.framework.Assert.assertEquals("UNHEALTHY", resource.getPropertyValue(org.apache.ambari.msi.HostProvider.HOST_STATE_PROPERTY_ID));
    }

    @org.junit.Test
    public void testGetResourcesCheckStateFromCategory() throws java.lang.Exception {
        org.apache.ambari.msi.TestStateProvider stateProvider = new org.apache.ambari.msi.TestStateProvider();
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(stateProvider, new org.apache.ambari.scom.TestClusterDefinitionProvider(), new org.apache.ambari.scom.TestHostInfoProvider());
        org.apache.ambari.msi.HostProvider provider = new org.apache.ambari.msi.HostProvider(clusterDefinition);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.msi.HostProvider.HOST_NAME_PROPERTY_ID).equals("slave3.acme.com").toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest("Hosts"), predicate);
        junit.framework.Assert.assertEquals(1, resources.size());
        org.apache.ambari.server.controller.spi.Resource resource = resources.iterator().next();
        junit.framework.Assert.assertEquals("HEALTHY", resource.getPropertyValue(org.apache.ambari.msi.HostProvider.HOST_STATE_PROPERTY_ID));
        stateProvider.setState(org.apache.ambari.msi.StateProvider.State.Unknown);
        resources = provider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), predicate);
        junit.framework.Assert.assertEquals(1, resources.size());
        resource = resources.iterator().next();
        junit.framework.Assert.assertEquals("UNHEALTHY", resource.getPropertyValue(org.apache.ambari.msi.HostProvider.HOST_STATE_PROPERTY_ID));
    }

    @org.junit.Test
    public void testCreateResources() throws java.lang.Exception {
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(new org.apache.ambari.msi.TestStateProvider(), new org.apache.ambari.scom.TestClusterDefinitionProvider(), new org.apache.ambari.scom.TestHostInfoProvider());
        org.apache.ambari.msi.HostProvider provider = new org.apache.ambari.msi.HostProvider(clusterDefinition);
        try {
            provider.createResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest());
            junit.framework.Assert.fail("Expected UnsupportedOperationException.");
        } catch (java.lang.UnsupportedOperationException e) {
        }
    }

    @org.junit.Test
    public void testUpdateResources() throws java.lang.Exception {
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(new org.apache.ambari.msi.TestStateProvider(), new org.apache.ambari.scom.TestClusterDefinitionProvider(), new org.apache.ambari.scom.TestHostInfoProvider());
        org.apache.ambari.msi.HostProvider provider = new org.apache.ambari.msi.HostProvider(clusterDefinition);
        provider.updateResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(new java.util.HashMap<java.lang.String, java.lang.Object>(), null), null);
    }

    @org.junit.Test
    public void testDeleteResources() throws java.lang.Exception {
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(new org.apache.ambari.msi.TestStateProvider(), new org.apache.ambari.scom.TestClusterDefinitionProvider(), new org.apache.ambari.scom.TestHostInfoProvider());
        org.apache.ambari.msi.HostProvider provider = new org.apache.ambari.msi.HostProvider(clusterDefinition);
        try {
            provider.deleteResources(null);
            junit.framework.Assert.fail("Expected UnsupportedOperationException.");
        } catch (java.lang.UnsupportedOperationException e) {
        }
    }
}