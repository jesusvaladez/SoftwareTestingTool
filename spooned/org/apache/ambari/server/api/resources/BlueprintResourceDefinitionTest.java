package org.apache.ambari.server.api.resources;
public class BlueprintResourceDefinitionTest {
    @org.junit.Test
    public void testGetType() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.BlueprintResourceDefinition definition = new org.apache.ambari.server.api.resources.BlueprintResourceDefinition();
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Blueprint, definition.getType());
    }

    @org.junit.Test
    public void testGetPluralName() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.BlueprintResourceDefinition definition = new org.apache.ambari.server.api.resources.BlueprintResourceDefinition();
        org.junit.Assert.assertEquals("blueprints", definition.getPluralName());
    }

    @org.junit.Test
    public void testGetSingularName() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.BlueprintResourceDefinition definition = new org.apache.ambari.server.api.resources.BlueprintResourceDefinition();
        org.junit.Assert.assertEquals("blueprint", definition.getSingularName());
    }

    @org.junit.Test
    public void testGetSubResourceDefinitions() {
        org.apache.ambari.server.api.resources.BlueprintResourceDefinition definition = new org.apache.ambari.server.api.resources.BlueprintResourceDefinition();
        org.junit.Assert.assertTrue(definition.getSubResourceDefinitions().isEmpty());
    }

    @org.junit.Test
    public void testGetCreateDirectives() {
        org.apache.ambari.server.api.resources.BlueprintResourceDefinition definition = new org.apache.ambari.server.api.resources.BlueprintResourceDefinition();
        java.util.Collection<java.lang.String> directives = definition.getCreateDirectives();
        org.junit.Assert.assertEquals(1, directives.size());
        org.junit.Assert.assertTrue(directives.contains("validate_topology"));
    }
}