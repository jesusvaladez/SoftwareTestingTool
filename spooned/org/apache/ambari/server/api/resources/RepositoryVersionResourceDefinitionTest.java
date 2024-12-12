package org.apache.ambari.server.api.resources;
public class RepositoryVersionResourceDefinitionTest {
    @org.junit.Test
    public void testGetPluralName() throws java.lang.Exception {
        final org.apache.ambari.server.api.resources.RepositoryVersionResourceDefinition resourceDefinition = new org.apache.ambari.server.api.resources.RepositoryVersionResourceDefinition();
        org.junit.Assert.assertEquals("repository_versions", resourceDefinition.getPluralName());
    }

    @org.junit.Test
    public void testGetSingularName() throws java.lang.Exception {
        final org.apache.ambari.server.api.resources.RepositoryVersionResourceDefinition resourceDefinition = new org.apache.ambari.server.api.resources.RepositoryVersionResourceDefinition();
        org.junit.Assert.assertEquals("repository_version", resourceDefinition.getSingularName());
    }

    @org.junit.Test
    public void testGetSubResourceDefinitions() throws java.lang.Exception {
        final org.apache.ambari.server.api.resources.RepositoryVersionResourceDefinition resourceDefinition = new org.apache.ambari.server.api.resources.RepositoryVersionResourceDefinition();
        final java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResourceDefinitions = resourceDefinition.getSubResourceDefinitions();
        final java.util.Iterator<org.apache.ambari.server.api.resources.SubResourceDefinition> iterator = subResourceDefinitions.iterator();
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.OperatingSystem, iterator.next().getType());
        org.junit.Assert.assertEquals(1, subResourceDefinitions.size());
    }
}