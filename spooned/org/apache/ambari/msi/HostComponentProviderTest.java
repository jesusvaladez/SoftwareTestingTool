package org.apache.ambari.msi;
public class HostComponentProviderTest {
    @org.junit.Test
    public void testGetResources() throws java.lang.Exception {
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(new org.apache.ambari.msi.TestStateProvider(), new org.apache.ambari.scom.TestClusterDefinitionProvider(), new org.apache.ambari.scom.TestHostInfoProvider());
        org.apache.ambari.msi.HostComponentProvider provider = new org.apache.ambari.msi.HostComponentProvider(clusterDefinition);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), null);
        junit.framework.Assert.assertEquals(25, resources.size());
    }

    @org.junit.Test
    public void testGetResourcesWithPredicate() throws java.lang.Exception {
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(new org.apache.ambari.msi.TestStateProvider(), new org.apache.ambari.scom.TestClusterDefinitionProvider(), new org.apache.ambari.scom.TestHostInfoProvider());
        org.apache.ambari.msi.HostComponentProvider provider = new org.apache.ambari.msi.HostComponentProvider(clusterDefinition);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.msi.HostComponentProvider.HOST_COMPONENT_SERVICE_NAME_PROPERTY_ID).equals("MAPREDUCE").toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), predicate);
        junit.framework.Assert.assertEquals(5, resources.size());
        predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.msi.HostComponentProvider.HOST_COMPONENT_HOST_NAME_PROPERTY_ID).equals("UnknownHost").toPredicate();
        resources = provider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), predicate);
        junit.framework.Assert.assertTrue(resources.isEmpty());
    }

    @org.junit.Test
    public void testGetResourcesCheckState() throws java.lang.Exception {
        org.apache.ambari.msi.TestStateProvider stateProvider = new org.apache.ambari.msi.TestStateProvider();
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(stateProvider, new org.apache.ambari.scom.TestClusterDefinitionProvider(), new org.apache.ambari.scom.TestHostInfoProvider());
        org.apache.ambari.msi.HostComponentProvider provider = new org.apache.ambari.msi.HostComponentProvider(clusterDefinition);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.msi.HostComponentProvider.HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID).equals("HBASE_REGIONSERVER").toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), predicate);
        junit.framework.Assert.assertEquals(3, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            junit.framework.Assert.assertEquals("STARTED", resource.getPropertyValue(org.apache.ambari.msi.HostComponentProvider.HOST_COMPONENT_STATE_PROPERTY_ID));
        }
        stateProvider.setHealthy(false);
        resources = provider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), predicate);
        junit.framework.Assert.assertEquals(3, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            junit.framework.Assert.assertEquals("INSTALLED", resource.getPropertyValue(org.apache.ambari.msi.HostComponentProvider.HOST_COMPONENT_STATE_PROPERTY_ID));
        }
    }

    @org.junit.Test
    public void testGetResourcesCheckStateFromCategory() throws java.lang.Exception {
        org.apache.ambari.msi.TestStateProvider stateProvider = new org.apache.ambari.msi.TestStateProvider();
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(stateProvider, new org.apache.ambari.scom.TestClusterDefinitionProvider(), new org.apache.ambari.scom.TestHostInfoProvider());
        org.apache.ambari.msi.HostComponentProvider provider = new org.apache.ambari.msi.HostComponentProvider(clusterDefinition);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.msi.HostComponentProvider.HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID).equals("HBASE_REGIONSERVER").toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest("HostRoles"), predicate);
        junit.framework.Assert.assertEquals(3, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            junit.framework.Assert.assertEquals("STARTED", resource.getPropertyValue(org.apache.ambari.msi.HostComponentProvider.HOST_COMPONENT_STATE_PROPERTY_ID));
        }
        stateProvider.setHealthy(false);
        resources = provider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), predicate);
        junit.framework.Assert.assertEquals(3, resources.size());
        for (org.apache.ambari.server.controller.spi.Resource resource : resources) {
            junit.framework.Assert.assertEquals("INSTALLED", resource.getPropertyValue(org.apache.ambari.msi.HostComponentProvider.HOST_COMPONENT_STATE_PROPERTY_ID));
        }
    }

    @org.junit.Test
    public void testCreateResources() throws java.lang.Exception {
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(new org.apache.ambari.msi.TestStateProvider(), new org.apache.ambari.scom.TestClusterDefinitionProvider(), new org.apache.ambari.scom.TestHostInfoProvider());
        org.apache.ambari.msi.HostComponentProvider provider = new org.apache.ambari.msi.HostComponentProvider(clusterDefinition);
        try {
            provider.createResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest());
            junit.framework.Assert.fail("Expected UnsupportedOperationException.");
        } catch (java.lang.UnsupportedOperationException e) {
        }
    }

    @org.junit.Test
    public void testUpdateResources() throws java.lang.Exception {
        org.apache.ambari.msi.TestStateProvider stateProvider = new org.apache.ambari.msi.TestStateProvider();
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(stateProvider, new org.apache.ambari.scom.TestClusterDefinitionProvider(), new org.apache.ambari.scom.TestHostInfoProvider());
        org.apache.ambari.msi.HostComponentProvider provider = new org.apache.ambari.msi.HostComponentProvider(clusterDefinition);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.msi.HostComponentProvider.HOST_COMPONENT_COMPONENT_NAME_PROPERTY_ID).equals("HBASE_REGIONSERVER").toPredicate();
        java.util.HashMap<java.lang.String, java.lang.Object> properties = new java.util.HashMap<java.lang.String, java.lang.Object>();
        properties.put(org.apache.ambari.msi.HostComponentProvider.HOST_COMPONENT_STATE_PROPERTY_ID, "STARTED");
        org.apache.ambari.server.controller.spi.Request updateRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(properties, null);
        provider.updateResources(updateRequest, predicate);
        junit.framework.Assert.assertEquals(org.apache.ambari.msi.StateProvider.State.Running, stateProvider.getState());
        properties.put(org.apache.ambari.msi.HostComponentProvider.HOST_COMPONENT_STATE_PROPERTY_ID, "INSTALLED");
        updateRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(properties, null);
        provider.updateResources(updateRequest, predicate);
        junit.framework.Assert.assertEquals(org.apache.ambari.msi.StateProvider.State.Stopped, stateProvider.getState());
        properties.put(org.apache.ambari.msi.HostComponentProvider.HOST_COMPONENT_STATE_PROPERTY_ID, "STARTED");
        updateRequest = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(properties, null);
        provider.updateResources(updateRequest, predicate);
        junit.framework.Assert.assertEquals(org.apache.ambari.msi.StateProvider.State.Running, stateProvider.getState());
    }

    @org.junit.Test
    public void testDeleteResources() throws java.lang.Exception {
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(new org.apache.ambari.msi.TestStateProvider(), new org.apache.ambari.scom.TestClusterDefinitionProvider(), new org.apache.ambari.scom.TestHostInfoProvider());
        org.apache.ambari.msi.HostComponentProvider provider = new org.apache.ambari.msi.HostComponentProvider(clusterDefinition);
        try {
            provider.deleteResources(null);
            junit.framework.Assert.fail("Expected UnsupportedOperationException.");
        } catch (java.lang.UnsupportedOperationException e) {
        }
    }
}