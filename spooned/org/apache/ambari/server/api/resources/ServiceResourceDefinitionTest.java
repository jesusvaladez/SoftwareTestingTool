package org.apache.ambari.server.api.resources;
public class ServiceResourceDefinitionTest {
    @org.junit.Test
    public void testGetPluralName() {
        org.junit.Assert.assertEquals("services", new org.apache.ambari.server.api.resources.ServiceResourceDefinition().getPluralName());
    }

    @org.junit.Test
    public void testGetSingularName() {
        org.junit.Assert.assertEquals("service", new org.apache.ambari.server.api.resources.ServiceResourceDefinition().getSingularName());
    }

    @org.junit.Test
    public void testGetSubResourceDefinitions() {
        org.apache.ambari.server.api.resources.ResourceDefinition resource = new org.apache.ambari.server.api.resources.ServiceResourceDefinition();
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResources = resource.getSubResourceDefinitions();
        org.junit.Assert.assertEquals(3, subResources.size());
        org.junit.Assert.assertTrue(includesType(subResources, org.apache.ambari.server.controller.spi.Resource.Type.Component));
        org.junit.Assert.assertTrue(includesType(subResources, org.apache.ambari.server.controller.spi.Resource.Type.Alert));
        org.junit.Assert.assertTrue(includesType(subResources, org.apache.ambari.server.controller.spi.Resource.Type.Artifact));
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