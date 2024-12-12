package org.apache.ambari.server.api.resources;
public class JobResourceDefinitionTest {
    @org.junit.Test
    public void testGetPluralName() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.JobResourceDefinition definition = new org.apache.ambari.server.api.resources.JobResourceDefinition();
        org.junit.Assert.assertEquals("jobs", definition.getPluralName());
    }

    @org.junit.Test
    public void testGetSingularName() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.JobResourceDefinition definition = new org.apache.ambari.server.api.resources.JobResourceDefinition();
        org.junit.Assert.assertEquals("job", definition.getSingularName());
    }

    @org.junit.Test
    public void testGetSubResourceDefinitions() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.JobResourceDefinition definition = new org.apache.ambari.server.api.resources.JobResourceDefinition();
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResourceDefinitions = definition.getSubResourceDefinitions();
        org.junit.Assert.assertEquals(1, subResourceDefinitions.size());
        org.apache.ambari.server.api.resources.SubResourceDefinition subResourceDefinition = subResourceDefinitions.iterator().next();
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.TaskAttempt, subResourceDefinition.getType());
    }
}