package org.apache.ambari.server.api.resources;
public class FeedResourceDefinitionTest {
    @org.junit.Test
    public void testGetPluralName() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.FeedResourceDefinition definition = new org.apache.ambari.server.api.resources.FeedResourceDefinition();
        org.junit.Assert.assertEquals("feeds", definition.getPluralName());
    }

    @org.junit.Test
    public void testGetSingularName() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.FeedResourceDefinition definition = new org.apache.ambari.server.api.resources.FeedResourceDefinition();
        org.junit.Assert.assertEquals("feed", definition.getSingularName());
    }

    @org.junit.Test
    public void testGetSubResourceDefinitions() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.FeedResourceDefinition definition = new org.apache.ambari.server.api.resources.FeedResourceDefinition();
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResourceDefinitions = definition.getSubResourceDefinitions();
        org.junit.Assert.assertEquals(1, subResourceDefinitions.size());
        org.apache.ambari.server.api.resources.SubResourceDefinition subResourceDefinition = subResourceDefinitions.iterator().next();
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.DRInstance, subResourceDefinition.getType());
    }
}