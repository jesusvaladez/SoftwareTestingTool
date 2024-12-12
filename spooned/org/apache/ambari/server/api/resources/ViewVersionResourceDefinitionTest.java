package org.apache.ambari.server.api.resources;
public class ViewVersionResourceDefinitionTest {
    @org.junit.Test
    public void testGetPluralName() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.ViewVersionResourceDefinition viewVersionResourceDefinition = new org.apache.ambari.server.api.resources.ViewVersionResourceDefinition();
        org.junit.Assert.assertEquals("versions", viewVersionResourceDefinition.getPluralName());
    }

    @org.junit.Test
    public void testGetSingularName() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.ViewVersionResourceDefinition viewVersionResourceDefinition = new org.apache.ambari.server.api.resources.ViewVersionResourceDefinition();
        org.junit.Assert.assertEquals("version", viewVersionResourceDefinition.getSingularName());
    }

    @org.junit.Test
    public void testGetSubResourceDefinitions() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.ViewVersionResourceDefinition viewVersionResourceDefinition = new org.apache.ambari.server.api.resources.ViewVersionResourceDefinition();
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResourceDefinitions = viewVersionResourceDefinition.getSubResourceDefinitions();
        org.junit.Assert.assertEquals(2, subResourceDefinitions.size());
        for (org.apache.ambari.server.api.resources.SubResourceDefinition subResourceDefinition : subResourceDefinitions) {
            java.lang.String name = subResourceDefinition.getType().name();
            org.junit.Assert.assertTrue(name.equals("ViewInstance") || name.equals("ViewPermission"));
        }
    }
}