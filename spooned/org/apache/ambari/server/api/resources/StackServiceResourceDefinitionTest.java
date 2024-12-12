package org.apache.ambari.server.api.resources;
public class StackServiceResourceDefinitionTest {
    @org.junit.Test
    public void testDefinitionNames() {
        org.apache.ambari.server.api.resources.ResourceDefinition def = new org.apache.ambari.server.api.resources.StackServiceResourceDefinition();
        junit.framework.Assert.assertEquals("service", def.getSingularName());
        junit.framework.Assert.assertEquals("services", def.getPluralName());
    }

    @org.junit.Test
    public void testGetSubResourceDefinitions() {
        org.apache.ambari.server.api.resources.ResourceDefinition def = new org.apache.ambari.server.api.resources.StackServiceResourceDefinition();
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResources = def.getSubResourceDefinitions();
        junit.framework.Assert.assertEquals(5, subResources.size());
        boolean configReturned = false;
        boolean componentReturned = false;
        boolean artifactReturned = false;
        boolean themesReturned = false;
        for (org.apache.ambari.server.api.resources.SubResourceDefinition subResource : subResources) {
            org.apache.ambari.server.controller.spi.Resource.Type type = subResource.getType();
            if (type.equals(org.apache.ambari.server.controller.spi.Resource.Type.StackConfiguration)) {
                configReturned = true;
            } else if (type.equals(org.apache.ambari.server.controller.spi.Resource.Type.StackServiceComponent)) {
                componentReturned = true;
            } else if (type.equals(org.apache.ambari.server.controller.spi.Resource.Type.StackArtifact)) {
                artifactReturned = true;
            } else if (type.equals(org.apache.ambari.server.controller.spi.Resource.Type.Theme)) {
                themesReturned = true;
            }
        }
        junit.framework.Assert.assertTrue(configReturned);
        junit.framework.Assert.assertTrue(componentReturned);
        junit.framework.Assert.assertTrue(artifactReturned);
        junit.framework.Assert.assertTrue(themesReturned);
    }
}