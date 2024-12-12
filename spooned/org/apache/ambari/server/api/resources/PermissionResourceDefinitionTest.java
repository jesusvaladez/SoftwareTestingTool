package org.apache.ambari.server.api.resources;
public class PermissionResourceDefinitionTest {
    @org.junit.Test
    public void testGetPluralName() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.PermissionResourceDefinition permissionResourceDefinition = new org.apache.ambari.server.api.resources.PermissionResourceDefinition();
        org.junit.Assert.assertEquals("permissions", permissionResourceDefinition.getPluralName());
    }

    @org.junit.Test
    public void testGetSingularName() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.PermissionResourceDefinition permissionResourceDefinition = new org.apache.ambari.server.api.resources.PermissionResourceDefinition();
        org.junit.Assert.assertEquals("permission", permissionResourceDefinition.getSingularName());
    }

    @org.junit.Test
    public void testGetSubResourceDefinitions() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.PermissionResourceDefinition permissionResourceDefinition = new org.apache.ambari.server.api.resources.PermissionResourceDefinition();
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResourceDefinitions = permissionResourceDefinition.getSubResourceDefinitions();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource.Type> expectedSubTypes = new java.util.HashSet<>();
        expectedSubTypes.add(org.apache.ambari.server.controller.spi.Resource.Type.RoleAuthorization);
        org.junit.Assert.assertEquals(1, subResourceDefinitions.size());
        for (org.apache.ambari.server.api.resources.SubResourceDefinition subResourceDefinition : subResourceDefinitions) {
            org.junit.Assert.assertTrue(expectedSubTypes.contains(subResourceDefinition.getType()));
        }
    }
}