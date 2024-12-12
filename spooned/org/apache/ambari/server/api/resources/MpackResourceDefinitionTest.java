package org.apache.ambari.server.api.resources;
public class MpackResourceDefinitionTest {
    @org.junit.Test
    public void testDefinitionNames() {
        org.apache.ambari.server.api.resources.ResourceDefinition def = new org.apache.ambari.server.api.resources.MpackResourceDefinition();
        junit.framework.Assert.assertEquals("mpack", def.getSingularName());
        junit.framework.Assert.assertEquals("mpacks", def.getPluralName());
    }
}