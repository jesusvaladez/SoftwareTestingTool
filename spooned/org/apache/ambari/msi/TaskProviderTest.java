package org.apache.ambari.msi;
public class TaskProviderTest {
    @org.junit.Test
    public void testGetResources() throws java.lang.Exception {
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(new org.apache.ambari.msi.TestStateProvider(), new org.apache.ambari.scom.TestClusterDefinitionProvider(), new org.apache.ambari.scom.TestHostInfoProvider());
        clusterDefinition.getTaskResources().add(new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Task));
        org.apache.ambari.msi.TaskProvider provider = new org.apache.ambari.msi.TaskProvider(clusterDefinition);
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = provider.getResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(), null);
        junit.framework.Assert.assertEquals(1, resources.size());
    }

    @org.junit.Test
    public void testCreateResources() throws java.lang.Exception {
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(new org.apache.ambari.msi.TestStateProvider(), new org.apache.ambari.scom.TestClusterDefinitionProvider(), new org.apache.ambari.scom.TestHostInfoProvider());
        org.apache.ambari.msi.TaskProvider provider = new org.apache.ambari.msi.TaskProvider(clusterDefinition);
        try {
            provider.createResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest());
            junit.framework.Assert.fail("Expected UnsupportedOperationException.");
        } catch (java.lang.UnsupportedOperationException e) {
        }
    }

    @org.junit.Test
    public void testUpdateResources() throws java.lang.Exception {
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(new org.apache.ambari.msi.TestStateProvider(), new org.apache.ambari.scom.TestClusterDefinitionProvider(), new org.apache.ambari.scom.TestHostInfoProvider());
        org.apache.ambari.msi.TaskProvider provider = new org.apache.ambari.msi.TaskProvider(clusterDefinition);
        provider.updateResources(org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(new java.util.HashMap<java.lang.String, java.lang.Object>(), null), null);
    }

    @org.junit.Test
    public void testDeleteResources() throws java.lang.Exception {
        org.apache.ambari.msi.ClusterDefinition clusterDefinition = new org.apache.ambari.msi.ClusterDefinition(new org.apache.ambari.msi.TestStateProvider(), new org.apache.ambari.scom.TestClusterDefinitionProvider(), new org.apache.ambari.scom.TestHostInfoProvider());
        org.apache.ambari.msi.TaskProvider provider = new org.apache.ambari.msi.TaskProvider(clusterDefinition);
        try {
            provider.deleteResources(null);
            junit.framework.Assert.fail("Expected UnsupportedOperationException.");
        } catch (java.lang.UnsupportedOperationException e) {
        }
    }
}