package org.apache.ambari.server.api.resources;
public class RepositoryResourceDefinitionTest {
    @org.junit.Test
    public void testGetPluralName() throws java.lang.Exception {
        final org.apache.ambari.server.api.resources.RepositoryResourceDefinition resourceDefinition = new org.apache.ambari.server.api.resources.RepositoryResourceDefinition();
        org.junit.Assert.assertEquals("repositories", resourceDefinition.getPluralName());
    }

    @org.junit.Test
    public void testGetSingularName() throws java.lang.Exception {
        final org.apache.ambari.server.api.resources.RepositoryResourceDefinition resourceDefinition = new org.apache.ambari.server.api.resources.RepositoryResourceDefinition();
        org.junit.Assert.assertEquals("repository", resourceDefinition.getSingularName());
    }

    @org.junit.Test
    public void testGetSubResourceDefinitions() throws java.lang.Exception {
        final org.apache.ambari.server.api.resources.RepositoryResourceDefinition resourceDefinition = new org.apache.ambari.server.api.resources.RepositoryResourceDefinition();
        final java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResourceDefinitions = resourceDefinition.getSubResourceDefinitions();
        org.junit.Assert.assertEquals(0, subResourceDefinitions.size());
    }

    @org.junit.Test
    public void testGetCreateDirectives() throws java.lang.Exception {
        final org.apache.ambari.server.api.resources.RepositoryResourceDefinition resourceDefinition = new org.apache.ambari.server.api.resources.RepositoryResourceDefinition();
        final java.util.Collection<java.lang.String> createDirectives = resourceDefinition.getCreateDirectives();
        org.junit.Assert.assertEquals(1, createDirectives.size());
        org.junit.Assert.assertEquals(org.apache.ambari.server.api.resources.RepositoryResourceDefinition.VALIDATE_ONLY_DIRECTIVE, createDirectives.iterator().next());
    }
}