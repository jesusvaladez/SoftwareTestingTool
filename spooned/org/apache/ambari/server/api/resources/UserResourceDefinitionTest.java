package org.apache.ambari.server.api.resources;
public class UserResourceDefinitionTest {
    @org.junit.Test
    public void testGetPluralName() throws java.lang.Exception {
        final org.apache.ambari.server.api.resources.UserResourceDefinition userResourceDefinition = new org.apache.ambari.server.api.resources.UserResourceDefinition();
        org.junit.Assert.assertEquals("users", userResourceDefinition.getPluralName());
    }

    @org.junit.Test
    public void testGetSingularName() throws java.lang.Exception {
        final org.apache.ambari.server.api.resources.UserResourceDefinition userResourceDefinition = new org.apache.ambari.server.api.resources.UserResourceDefinition();
        org.junit.Assert.assertEquals("user", userResourceDefinition.getSingularName());
    }

    @org.junit.Test
    public void testGetSubResourceDefinitions() throws java.lang.Exception {
        java.util.Set<org.apache.ambari.server.controller.spi.Resource.Type> expectedSubResourceDefinitionTypes = new java.util.HashSet<>();
        expectedSubResourceDefinitionTypes.add(org.apache.ambari.server.controller.spi.Resource.Type.UserAuthenticationSource);
        expectedSubResourceDefinitionTypes.add(org.apache.ambari.server.controller.spi.Resource.Type.UserPrivilege);
        expectedSubResourceDefinitionTypes.add(org.apache.ambari.server.controller.spi.Resource.Type.ActiveWidgetLayout);
        final org.apache.ambari.server.api.resources.UserResourceDefinition userResourceDefinition = new org.apache.ambari.server.api.resources.UserResourceDefinition();
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResourceDefinitions = userResourceDefinition.getSubResourceDefinitions();
        org.junit.Assert.assertEquals(expectedSubResourceDefinitionTypes.size(), subResourceDefinitions.size());
        for (org.apache.ambari.server.api.resources.SubResourceDefinition subResourceDefinition : subResourceDefinitions) {
            org.junit.Assert.assertTrue(expectedSubResourceDefinitionTypes.contains(subResourceDefinition.getType()));
        }
    }
}