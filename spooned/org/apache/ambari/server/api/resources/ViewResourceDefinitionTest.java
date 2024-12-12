package org.apache.ambari.server.api.resources;
public class ViewResourceDefinitionTest {
    @org.junit.Test
    public void testGetPluralName() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.ViewResourceDefinition viewResourceDefinition = new org.apache.ambari.server.api.resources.ViewResourceDefinition();
        org.junit.Assert.assertEquals("views", viewResourceDefinition.getPluralName());
    }

    @org.junit.Test
    public void testGetSingularName() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.ViewResourceDefinition viewResourceDefinition = new org.apache.ambari.server.api.resources.ViewResourceDefinition();
        org.junit.Assert.assertEquals("view", viewResourceDefinition.getSingularName());
    }

    @org.junit.Test
    public void testGetSubResourceDefinitions() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.ViewResourceDefinition viewResourceDefinition = new org.apache.ambari.server.api.resources.ViewResourceDefinition();
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResourceDefinitions = viewResourceDefinition.getSubResourceDefinitions();
        org.junit.Assert.assertEquals(1, subResourceDefinitions.size());
        org.junit.Assert.assertEquals("ViewVersion", subResourceDefinitions.iterator().next().getType().name());
    }
}