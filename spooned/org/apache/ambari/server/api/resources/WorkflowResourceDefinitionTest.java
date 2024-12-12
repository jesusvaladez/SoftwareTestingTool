package org.apache.ambari.server.api.resources;
public class WorkflowResourceDefinitionTest {
    @org.junit.Test
    public void testGetPluralName() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.WorkflowResourceDefinition definition = new org.apache.ambari.server.api.resources.WorkflowResourceDefinition();
        org.junit.Assert.assertEquals("workflows", definition.getPluralName());
    }

    @org.junit.Test
    public void testGetSingularName() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.WorkflowResourceDefinition definition = new org.apache.ambari.server.api.resources.WorkflowResourceDefinition();
        org.junit.Assert.assertEquals("workflow", definition.getSingularName());
    }

    @org.junit.Test
    public void testGetSubResourceDefinitions() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.WorkflowResourceDefinition definition = new org.apache.ambari.server.api.resources.WorkflowResourceDefinition();
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResourceDefinitions = definition.getSubResourceDefinitions();
        org.junit.Assert.assertEquals(1, subResourceDefinitions.size());
        org.apache.ambari.server.api.resources.SubResourceDefinition subResourceDefinition = subResourceDefinitions.iterator().next();
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Job, subResourceDefinition.getType());
    }
}