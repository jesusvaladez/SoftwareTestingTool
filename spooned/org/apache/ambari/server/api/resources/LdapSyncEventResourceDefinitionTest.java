package org.apache.ambari.server.api.resources;
public class LdapSyncEventResourceDefinitionTest {
    @org.junit.Test
    public void testGetPluralName() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.LdapSyncEventResourceDefinition resourceDefinition = new org.apache.ambari.server.api.resources.LdapSyncEventResourceDefinition();
        org.junit.Assert.assertEquals("ldap_sync_events", resourceDefinition.getPluralName());
    }

    @org.junit.Test
    public void testGetSingularName() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.LdapSyncEventResourceDefinition resourceDefinition = new org.apache.ambari.server.api.resources.LdapSyncEventResourceDefinition();
        org.junit.Assert.assertEquals("ldap_sync_event", resourceDefinition.getSingularName());
    }

    @org.junit.Test
    public void testGetSubResourceDefinitions() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.LdapSyncEventResourceDefinition resourceDefinition = new org.apache.ambari.server.api.resources.LdapSyncEventResourceDefinition();
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResourceDefinitions = resourceDefinition.getSubResourceDefinitions();
        org.junit.Assert.assertEquals(0, subResourceDefinitions.size());
    }
}