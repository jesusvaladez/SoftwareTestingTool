package org.apache.ambari.server.api.resources;
public class UpgradeResourceDefinitionTest {
    @org.junit.Test
    public void testGetSingularName() {
        org.apache.ambari.server.api.resources.ResourceDefinition resourceDefinition = new org.apache.ambari.server.api.resources.UpgradeResourceDefinition();
        org.junit.Assert.assertEquals("upgrade", resourceDefinition.getSingularName());
    }

    @org.junit.Test
    public void testGetPluralName() {
        org.apache.ambari.server.api.resources.ResourceDefinition resourceDefinition = new org.apache.ambari.server.api.resources.UpgradeResourceDefinition();
        org.junit.Assert.assertEquals("upgrades", resourceDefinition.getPluralName());
    }

    @org.junit.Test
    public void testGetType() {
        org.apache.ambari.server.api.resources.ResourceDefinition resourceDefinition = new org.apache.ambari.server.api.resources.UpgradeResourceDefinition();
        org.junit.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.Upgrade, resourceDefinition.getType());
    }

    @org.junit.Test
    public void testGetCreateDirectives() {
        org.apache.ambari.server.api.resources.ResourceDefinition resourceDefinition = new org.apache.ambari.server.api.resources.UpgradeResourceDefinition();
        org.junit.Assert.assertEquals(com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.api.resources.UpgradeResourceDefinition.SKIP_SERVICE_CHECKS_DIRECTIVE), resourceDefinition.getCreateDirectives());
    }
}