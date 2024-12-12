package org.apache.ambari.server.api.resources;
public class CredentialResourceDefinitionTest {
    @org.junit.Test
    public void testGetType() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.CredentialResourceDefinition definition = new org.apache.ambari.server.api.resources.CredentialResourceDefinition();
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Credential, definition.getType());
    }

    @org.junit.Test
    public void testGetPluralName() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.CredentialResourceDefinition definition = new org.apache.ambari.server.api.resources.CredentialResourceDefinition();
        org.junit.Assert.assertEquals("credentials", definition.getPluralName());
    }

    @org.junit.Test
    public void testGetSingularName() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.CredentialResourceDefinition definition = new org.apache.ambari.server.api.resources.CredentialResourceDefinition();
        org.junit.Assert.assertEquals("credential", definition.getSingularName());
    }

    @org.junit.Test
    public void testGetSubResourceDefinitions() {
        org.apache.ambari.server.api.resources.CredentialResourceDefinition definition = new org.apache.ambari.server.api.resources.CredentialResourceDefinition();
        org.junit.Assert.assertTrue(definition.getSubResourceDefinitions().isEmpty());
    }

    @org.junit.Test
    public void testGetCreateDirectives() {
        org.apache.ambari.server.api.resources.CredentialResourceDefinition definition = new org.apache.ambari.server.api.resources.CredentialResourceDefinition();
        java.util.Collection<java.lang.String> directives = definition.getCreateDirectives();
        org.junit.Assert.assertEquals(0, directives.size());
    }
}