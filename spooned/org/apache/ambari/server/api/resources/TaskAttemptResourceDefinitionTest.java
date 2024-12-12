package org.apache.ambari.server.api.resources;
public class TaskAttemptResourceDefinitionTest {
    @org.junit.Test
    public void testGetPluralName() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.TaskAttemptResourceDefinition definition = new org.apache.ambari.server.api.resources.TaskAttemptResourceDefinition();
        org.junit.Assert.assertEquals("taskattempts", definition.getPluralName());
    }

    @org.junit.Test
    public void testGetSingularName() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.TaskAttemptResourceDefinition definition = new org.apache.ambari.server.api.resources.TaskAttemptResourceDefinition();
        org.junit.Assert.assertEquals("taskattempt", definition.getSingularName());
    }

    @org.junit.Test
    public void testGetSubResourceDefinitions() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.TaskAttemptResourceDefinition definition = new org.apache.ambari.server.api.resources.TaskAttemptResourceDefinition();
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResourceDefinitions = definition.getSubResourceDefinitions();
        org.junit.Assert.assertEquals(0, subResourceDefinitions.size());
    }
}