package org.apache.ambari.server.api.resources;
public class InstanceResourceDefinitionTest {
    @org.junit.Test
    public void testGetPluralName() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.InstanceResourceDefinition definition = new org.apache.ambari.server.api.resources.InstanceResourceDefinition();
        org.junit.Assert.assertEquals("instances", definition.getPluralName());
    }

    @org.junit.Test
    public void testGetSingularName() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.InstanceResourceDefinition definition = new org.apache.ambari.server.api.resources.InstanceResourceDefinition();
        org.junit.Assert.assertEquals("instance", definition.getSingularName());
    }

    @org.junit.Test
    public void testGetSubResourceDefinitions() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.InstanceResourceDefinition definition = new org.apache.ambari.server.api.resources.InstanceResourceDefinition();
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResourceDefinitions = definition.getSubResourceDefinitions();
        org.junit.Assert.assertTrue(subResourceDefinitions.isEmpty());
    }
}