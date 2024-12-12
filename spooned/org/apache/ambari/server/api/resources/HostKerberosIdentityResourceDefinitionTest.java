package org.apache.ambari.server.api.resources;
public class HostKerberosIdentityResourceDefinitionTest {
    @org.junit.Test
    public void testGetPluralName() throws java.lang.Exception {
        final org.apache.ambari.server.api.resources.HostKerberosIdentityResourceDefinition resourceDefinition = new org.apache.ambari.server.api.resources.HostKerberosIdentityResourceDefinition();
        org.junit.Assert.assertEquals("kerberos_identities", resourceDefinition.getPluralName());
    }

    @org.junit.Test
    public void testGetSingularName() throws java.lang.Exception {
        final org.apache.ambari.server.api.resources.HostKerberosIdentityResourceDefinition resourceDefinition = new org.apache.ambari.server.api.resources.HostKerberosIdentityResourceDefinition();
        org.junit.Assert.assertEquals("kerberos_identity", resourceDefinition.getSingularName());
    }

    @org.junit.Test
    public void testGetSubResourceDefinitions() throws java.lang.Exception {
        final org.apache.ambari.server.api.resources.HostKerberosIdentityResourceDefinition resourceDefinition = new org.apache.ambari.server.api.resources.HostKerberosIdentityResourceDefinition();
        final java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResourceDefinitions = resourceDefinition.getSubResourceDefinitions();
        org.junit.Assert.assertEquals(0, subResourceDefinitions.size());
    }
}