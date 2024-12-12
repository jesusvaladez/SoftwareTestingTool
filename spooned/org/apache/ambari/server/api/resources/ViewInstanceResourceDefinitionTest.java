package org.apache.ambari.server.api.resources;
public class ViewInstanceResourceDefinitionTest {
    @org.junit.Test
    public void testGetPluralName() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.ViewInstanceResourceDefinition viewInstanceResourceDefinition = org.apache.ambari.server.api.resources.ViewInstanceResourceDefinitionTest.getViewInstanceResourceDefinition();
        org.junit.Assert.assertEquals("instances", viewInstanceResourceDefinition.getPluralName());
    }

    @org.junit.Test
    public void testGetSingularName() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.ViewInstanceResourceDefinition viewInstanceResourceDefinition = org.apache.ambari.server.api.resources.ViewInstanceResourceDefinitionTest.getViewInstanceResourceDefinition();
        org.junit.Assert.assertEquals("instance", viewInstanceResourceDefinition.getSingularName());
    }

    @org.junit.Test
    public void testGetSubResourceDefinitions() throws java.lang.Exception {
        org.apache.ambari.server.api.resources.ViewInstanceResourceDefinition viewInstanceResourceDefinition = org.apache.ambari.server.api.resources.ViewInstanceResourceDefinitionTest.getViewInstanceResourceDefinition();
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResourceDefinitions = viewInstanceResourceDefinition.getSubResourceDefinitions();
        org.junit.Assert.assertEquals(3, subResourceDefinitions.size());
        for (org.apache.ambari.server.api.resources.SubResourceDefinition subResourceDefinition : subResourceDefinitions) {
            org.apache.ambari.server.controller.spi.Resource.Type type = subResourceDefinition.getType();
            org.junit.Assert.assertTrue((type.name().equals("sub1") || type.name().equals("sub2")) || type.equals(org.apache.ambari.server.controller.spi.Resource.Type.ViewPrivilege));
        }
    }

    public static org.apache.ambari.server.api.resources.ViewInstanceResourceDefinition getViewInstanceResourceDefinition() {
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResourceDefinitions = new java.util.HashSet<>();
        subResourceDefinitions.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(new org.apache.ambari.server.controller.spi.Resource.Type("sub1")));
        subResourceDefinitions.add(new org.apache.ambari.server.api.resources.SubResourceDefinition(new org.apache.ambari.server.controller.spi.Resource.Type("sub2")));
        return new org.apache.ambari.server.api.resources.ViewInstanceResourceDefinition(subResourceDefinitions);
    }
}