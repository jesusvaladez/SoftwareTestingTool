package org.apache.ambari.server.api.resources;
public class StackConfigurationDefinitionTest {
    @org.junit.Test
    public void testGetPluralName() {
        org.junit.Assert.assertEquals("configurations", new org.apache.ambari.server.api.resources.StackConfigurationResourceDefinition().getPluralName());
    }

    @org.junit.Test
    public void testGetSingularName() {
        org.junit.Assert.assertEquals("configuration", new org.apache.ambari.server.api.resources.StackConfigurationResourceDefinition().getSingularName());
    }

    @org.junit.Test
    public void testGetSubResourceDefinitions() {
        org.apache.ambari.server.api.resources.ResourceDefinition resource = new org.apache.ambari.server.api.resources.StackConfigurationResourceDefinition();
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResources = resource.getSubResourceDefinitions();
        org.junit.Assert.assertEquals(1, subResources.size());
        org.junit.Assert.assertTrue(includesType(subResources, org.apache.ambari.server.controller.spi.Resource.Type.StackConfigurationDependency));
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