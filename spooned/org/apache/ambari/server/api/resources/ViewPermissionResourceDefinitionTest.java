package org.apache.ambari.server.api.resources;
public class ViewPermissionResourceDefinitionTest {
    @org.junit.Test
    public void testGetPluralName() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.ViewPermissionResourceDefinition ViewPermissionResourceDefinition = new org.apache.ambari.server.api.resources.ViewPermissionResourceDefinition();
        org.junit.Assert.assertEquals("permissions", ViewPermissionResourceDefinition.getPluralName());
    }

    @org.junit.Test
    public void testGetSingularName() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.ViewPermissionResourceDefinition ViewPermissionResourceDefinition = new org.apache.ambari.server.api.resources.ViewPermissionResourceDefinition();
        org.junit.Assert.assertEquals("permission", ViewPermissionResourceDefinition.getSingularName());
    }

    @org.junit.Test
    public void testGetSubResourceDefinitions() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.ViewPermissionResourceDefinition ViewPermissionResourceDefinition = new org.apache.ambari.server.api.resources.ViewPermissionResourceDefinition();
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResourceDefinitions = ViewPermissionResourceDefinition.getSubResourceDefinitions();
        org.junit.Assert.assertTrue(subResourceDefinitions.isEmpty());
    }
}