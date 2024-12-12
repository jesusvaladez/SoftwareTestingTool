package org.apache.ambari.server.api.resources;
public class ClusterResourceDefinitionTest {
    @org.junit.Test
    public void testGetPluralName() {
        org.junit.Assert.assertEquals("clusters", new org.apache.ambari.server.api.resources.ClusterResourceDefinition().getPluralName());
    }

    @org.junit.Test
    public void testGetSingularName() {
        org.junit.Assert.assertEquals("cluster", new org.apache.ambari.server.api.resources.ClusterResourceDefinition().getSingularName());
    }

    @org.junit.Test
    public void testGetSubResourceDefinitions() {
        org.apache.ambari.server.api.resources.ResourceDefinition resource = new org.apache.ambari.server.api.resources.ClusterResourceDefinition();
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResources = resource.getSubResourceDefinitions();
        org.junit.Assert.assertEquals(13, subResources.size());
        org.junit.Assert.assertTrue(includesType(subResources, org.apache.ambari.server.controller.spi.Resource.Type.Service));
        org.junit.Assert.assertTrue(includesType(subResources, org.apache.ambari.server.controller.spi.Resource.Type.Host));
        org.junit.Assert.assertTrue(includesType(subResources, org.apache.ambari.server.controller.spi.Resource.Type.Configuration));
        org.junit.Assert.assertTrue(includesType(subResources, org.apache.ambari.server.controller.spi.Resource.Type.Request));
        org.junit.Assert.assertTrue(includesType(subResources, org.apache.ambari.server.controller.spi.Resource.Type.Workflow));
        org.junit.Assert.assertTrue(includesType(subResources, org.apache.ambari.server.controller.spi.Resource.Type.ConfigGroup));
        org.junit.Assert.assertTrue(includesType(subResources, org.apache.ambari.server.controller.spi.Resource.Type.AlertDefinition));
        org.junit.Assert.assertTrue(includesType(subResources, org.apache.ambari.server.controller.spi.Resource.Type.ServiceConfigVersion));
        org.junit.Assert.assertTrue(includesType(subResources, org.apache.ambari.server.controller.spi.Resource.Type.ClusterPrivilege));
        org.junit.Assert.assertTrue(includesType(subResources, org.apache.ambari.server.controller.spi.Resource.Type.Alert));
        org.junit.Assert.assertTrue(includesType(subResources, org.apache.ambari.server.controller.spi.Resource.Type.ClusterStackVersion));
        org.junit.Assert.assertTrue(includesType(subResources, org.apache.ambari.server.controller.spi.Resource.Type.Artifact));
        org.junit.Assert.assertTrue(includesType(subResources, org.apache.ambari.server.controller.spi.Resource.Type.ClusterKerberosDescriptor));
    }

    @org.junit.Test
    public void testGetRenderer() {
        org.apache.ambari.server.api.resources.ResourceDefinition resource = new org.apache.ambari.server.api.resources.ClusterResourceDefinition();
        org.junit.Assert.assertTrue(resource.getRenderer(null) instanceof org.apache.ambari.server.api.query.render.DefaultRenderer);
        org.junit.Assert.assertTrue(resource.getRenderer("default") instanceof org.apache.ambari.server.api.query.render.DefaultRenderer);
        org.junit.Assert.assertTrue(resource.getRenderer("minimal") instanceof org.apache.ambari.server.api.query.render.MinimalRenderer);
        org.junit.Assert.assertTrue(resource.getRenderer("blueprint") instanceof org.apache.ambari.server.api.query.render.ClusterBlueprintRenderer);
        try {
            resource.getRenderer("foo");
            org.junit.Assert.fail("Should have thrown an exception due to invalid renderer type");
        } catch (java.lang.IllegalArgumentException e) {
        }
    }

    private boolean includesType(java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> resources, org.apache.ambari.server.controller.spi.Resource.Type type) {
        for (org.apache.ambari.server.api.resources.SubResourceDefinition subResource : resources) {
            if (subResource.getType() == type) {
                return true;
            }
        }
        return false;
    }
}