package org.apache.ambari.server.api.resources;
public class PrivilegeResourceDefinitionTest {
    @org.junit.Test
    public void testGetPluralName() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.PrivilegeResourceDefinition privilegeResourceDefinition = new org.apache.ambari.server.api.resources.PrivilegeResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.AmbariPrivilege);
        org.junit.Assert.assertEquals("privileges", privilegeResourceDefinition.getPluralName());
    }

    @org.junit.Test
    public void testGetSingularName() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.PrivilegeResourceDefinition privilegeResourceDefinition = new org.apache.ambari.server.api.resources.PrivilegeResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.AmbariPrivilege);
        org.junit.Assert.assertEquals("privilege", privilegeResourceDefinition.getSingularName());
    }

    @org.junit.Test
    public void testGetSubResourceDefinitions() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.PrivilegeResourceDefinition privilegeResourceDefinition = new org.apache.ambari.server.api.resources.PrivilegeResourceDefinition(org.apache.ambari.server.controller.spi.Resource.Type.AmbariPrivilege);
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResourceDefinitions = privilegeResourceDefinition.getSubResourceDefinitions();
        org.junit.Assert.assertEquals(0, subResourceDefinitions.size());
    }
}