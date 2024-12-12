package org.apache.ambari.server.api.resources;
public class StackConfigurationDependencyDefinitionTest {
    @org.junit.Test
    public void testGetPluralName() {
        org.junit.Assert.assertEquals("dependencies", new org.apache.ambari.server.api.resources.StackConfigurationDependencyResourceDefinition().getPluralName());
    }

    @org.junit.Test
    public void testGetSingularName() {
        org.junit.Assert.assertEquals("dependency", new org.apache.ambari.server.api.resources.StackConfigurationDependencyResourceDefinition().getSingularName());
    }

    @org.junit.Test
    public void testGetSubResourceDefinitions() {
        org.apache.ambari.server.api.resources.ResourceDefinition resource = new org.apache.ambari.server.api.resources.StackConfigurationDependencyResourceDefinition();
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResources = resource.getSubResourceDefinitions();
        org.junit.Assert.assertTrue(subResources.isEmpty());
    }
}