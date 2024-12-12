package org.apache.ambari.server.api.resources;
public class TargetClusterResourceDefinitionTest {
    @org.junit.Test
    public void testGetPluralName() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.TargetClusterResourceDefinition definition = new org.apache.ambari.server.api.resources.TargetClusterResourceDefinition();
        org.junit.Assert.assertEquals("targets", definition.getPluralName());
    }

    @org.junit.Test
    public void testGetSingularName() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.TargetClusterResourceDefinition definition = new org.apache.ambari.server.api.resources.TargetClusterResourceDefinition();
        org.junit.Assert.assertEquals("target", definition.getSingularName());
    }

    @org.junit.Test
    public void testGetSubResourceDefinitions() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.TargetClusterResourceDefinition definition = new org.apache.ambari.server.api.resources.TargetClusterResourceDefinition();
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResourceDefinitions = definition.getSubResourceDefinitions();
        org.junit.Assert.assertTrue(subResourceDefinitions.isEmpty());
    }
}