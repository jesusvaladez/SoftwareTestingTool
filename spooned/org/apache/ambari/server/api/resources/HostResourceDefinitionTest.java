package org.apache.ambari.server.api.resources;
public class HostResourceDefinitionTest {
    @org.junit.Test
    public void testGetPluralName() {
        org.junit.Assert.assertEquals("hosts", new org.apache.ambari.server.api.resources.HostResourceDefinition().getPluralName());
    }

    @org.junit.Test
    public void testGetSingularName() {
        org.junit.Assert.assertEquals("host", new org.apache.ambari.server.api.resources.HostResourceDefinition().getSingularName());
    }

    @org.junit.Test
    public void testGetSubResourceDefinitions() {
        final org.apache.ambari.server.api.resources.ResourceDefinition resource = new org.apache.ambari.server.api.resources.HostResourceDefinition();
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResources = resource.getSubResourceDefinitions();
        org.junit.Assert.assertEquals(4, subResources.size());
        org.junit.Assert.assertTrue(includesType(subResources, org.apache.ambari.server.controller.spi.Resource.Type.HostComponent));
        org.junit.Assert.assertTrue(includesType(subResources, org.apache.ambari.server.controller.spi.Resource.Type.Alert));
        org.junit.Assert.assertTrue(includesType(subResources, org.apache.ambari.server.controller.spi.Resource.Type.HostStackVersion));
        org.junit.Assert.assertTrue(includesType(subResources, org.apache.ambari.server.controller.spi.Resource.Type.HostKerberosIdentity));
    }

    private boolean includesType(java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> resources, org.apache.ambari.server.controller.spi.Resource.Type type) {
        for (org.apache.ambari.server.api.resources.SubResourceDefinition subResource : resources) {
            if (subResource.getType() == type) {
                return true;
            }
        }
        return false;
    }
}