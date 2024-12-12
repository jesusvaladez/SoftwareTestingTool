package org.apache.ambari.server.api.resources;
public class ComponentStackVersionResourceDefinitionTest {
    @org.junit.Test
    public void testGetPluralName() throws java.lang.Exception {
        final org.apache.ambari.server.api.resources.ComponentStackVersionResourceDefinition resourceDefinition = new org.apache.ambari.server.api.resources.ComponentStackVersionResourceDefinition(null);
        org.junit.Assert.assertEquals("stack_versions", resourceDefinition.getPluralName());
    }

    @org.junit.Test
    public void testGetSingularName() throws java.lang.Exception {
        final org.apache.ambari.server.api.resources.ComponentStackVersionResourceDefinition resourceDefinition = new org.apache.ambari.server.api.resources.ComponentStackVersionResourceDefinition(null);
        org.junit.Assert.assertEquals("stack_version", resourceDefinition.getSingularName());
    }

    @org.junit.Test
    public void testGetSubResourceDefinitions() throws java.lang.Exception {
        final org.apache.ambari.server.api.resources.ComponentStackVersionResourceDefinition resourceDefinition = new org.apache.ambari.server.api.resources.ComponentStackVersionResourceDefinition(null);
        final java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResourceDefinitions = resourceDefinition.getSubResourceDefinitions();
        org.junit.Assert.assertEquals(1, subResourceDefinitions.size());
    }
}