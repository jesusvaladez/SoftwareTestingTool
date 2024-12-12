package org.apache.ambari.server.api.resources;
public class StackVersionResourceDefinitionTest {
    @org.junit.Test
    public void testDefinitionNames() {
        org.apache.ambari.server.api.resources.ResourceDefinition def = new org.apache.ambari.server.api.resources.StackVersionResourceDefinition();
        junit.framework.Assert.assertEquals("version", def.getSingularName());
        junit.framework.Assert.assertEquals("versions", def.getPluralName());
    }

    @org.junit.Test
    public void testGetSubResourceDefinitions() {
        org.apache.ambari.server.api.resources.ResourceDefinition def = new org.apache.ambari.server.api.resources.StackVersionResourceDefinition();
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResources = def.getSubResourceDefinitions();
        junit.framework.Assert.assertEquals(7, subResources.size());
        boolean operatingSystemFound = false;
        boolean serviceFound = false;
        boolean configFound = false;
        boolean repoFound = false;
        boolean artifactReturned = false;
        boolean compatibleFound = false;
        for (org.apache.ambari.server.api.resources.SubResourceDefinition subResource : subResources) {
            org.apache.ambari.server.controller.spi.Resource.Type type = subResource.getType();
            if (type.equals(org.apache.ambari.server.controller.spi.Resource.Type.OperatingSystem)) {
                operatingSystemFound = true;
            } else if (type.equals(org.apache.ambari.server.controller.spi.Resource.Type.StackService)) {
                serviceFound = true;
            } else if (type.equals(org.apache.ambari.server.controller.spi.Resource.Type.StackLevelConfiguration)) {
                configFound = true;
            } else if (type.equals(org.apache.ambari.server.controller.spi.Resource.Type.RepositoryVersion)) {
                repoFound = true;
            } else if (type.equals(org.apache.ambari.server.controller.spi.Resource.Type.StackArtifact)) {
                artifactReturned = true;
            } else if (type.equals(org.apache.ambari.server.controller.spi.Resource.Type.CompatibleRepositoryVersion)) {
                compatibleFound = true;
            }
        }
        junit.framework.Assert.assertTrue(operatingSystemFound);
        junit.framework.Assert.assertTrue(serviceFound);
        junit.framework.Assert.assertTrue(configFound);
        junit.framework.Assert.assertTrue(repoFound);
        junit.framework.Assert.assertTrue(artifactReturned);
        junit.framework.Assert.assertTrue(compatibleFound);
    }
}