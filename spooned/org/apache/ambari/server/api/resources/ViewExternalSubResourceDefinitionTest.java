package org.apache.ambari.server.api.resources;
public class ViewExternalSubResourceDefinitionTest {
    @org.junit.Test
    public void testGetPluralName() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = new org.apache.ambari.server.controller.spi.Resource.Type("resource");
        org.apache.ambari.server.api.resources.ViewExternalSubResourceDefinition definition = new org.apache.ambari.server.api.resources.ViewExternalSubResourceDefinition(type);
        org.junit.Assert.assertEquals("resources", definition.getPluralName());
    }

    @org.junit.Test
    public void testGetSingularName() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = new org.apache.ambari.server.controller.spi.Resource.Type("resource");
        org.apache.ambari.server.api.resources.ViewExternalSubResourceDefinition definition = new org.apache.ambari.server.api.resources.ViewExternalSubResourceDefinition(type);
        org.junit.Assert.assertEquals("resource", definition.getSingularName());
    }

    @org.junit.Test
    public void testGetSubResourceDefinitions() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = new org.apache.ambari.server.controller.spi.Resource.Type("resource");
        org.apache.ambari.server.api.resources.ViewExternalSubResourceDefinition definition = new org.apache.ambari.server.api.resources.ViewExternalSubResourceDefinition(type);
        org.junit.Assert.assertEquals(java.util.Collections.emptySet(), definition.getSubResourceDefinitions());
    }
}