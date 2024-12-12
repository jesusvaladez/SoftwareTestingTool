package org.apache.ambari.server.view;
public class ViewSubResourceDefinitionTest {
    @org.junit.Test
    public void testGetPluralName() throws java.lang.Exception {
        org.apache.ambari.server.view.ViewSubResourceDefinition viewSubResourceDefinition = org.apache.ambari.server.view.ViewSubResourceDefinitionTest.getViewSubResourceDefinition();
        org.junit.Assert.assertEquals("resources", viewSubResourceDefinition.getPluralName());
    }

    @org.junit.Test
    public void testGetSingularName() throws java.lang.Exception {
        org.apache.ambari.server.view.ViewSubResourceDefinition viewSubResourceDefinition = org.apache.ambari.server.view.ViewSubResourceDefinitionTest.getViewSubResourceDefinition();
        org.junit.Assert.assertEquals("resource", viewSubResourceDefinition.getSingularName());
    }

    @org.junit.Test
    public void testGetSubResourceDefinitions() throws java.lang.Exception {
        org.apache.ambari.server.view.ViewSubResourceDefinition viewSubResourceDefinition = org.apache.ambari.server.view.ViewSubResourceDefinitionTest.getViewSubResourceDefinition();
        new org.apache.ambari.server.controller.spi.Resource.Type("MY_VIEW{1.0.0}/resource");
        new org.apache.ambari.server.controller.spi.Resource.Type("MY_VIEW{1.0.0}/subresource");
        java.util.Set<org.apache.ambari.server.api.resources.SubResourceDefinition> subResourceDefinitions = viewSubResourceDefinition.getSubResourceDefinitions();
        org.junit.Assert.assertEquals(1, subResourceDefinitions.size());
        org.junit.Assert.assertEquals("MY_VIEW{1.0.0}/subresource", subResourceDefinitions.iterator().next().getType().name());
    }

    public static org.apache.ambari.server.view.ViewSubResourceDefinition getViewSubResourceDefinition() throws java.lang.Exception {
        org.apache.ambari.server.orm.entities.ViewEntity viewDefinition = org.apache.ambari.server.orm.entities.ViewEntityTest.getViewEntity();
        org.apache.ambari.server.view.configuration.ResourceConfig resourceConfig = org.apache.ambari.server.view.configuration.ResourceConfigTest.getResourceConfigs().get(0);
        return new org.apache.ambari.server.view.ViewSubResourceDefinition(viewDefinition, resourceConfig);
    }
}