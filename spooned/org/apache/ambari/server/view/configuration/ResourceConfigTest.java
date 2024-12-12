package org.apache.ambari.server.view.configuration;
import javax.xml.bind.JAXBException;
public class ResourceConfigTest {
    @org.junit.Test
    public void testGetName() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.view.configuration.ResourceConfig> resourceConfigs = org.apache.ambari.server.view.configuration.ResourceConfigTest.getResourceConfigs();
        org.junit.Assert.assertEquals(2, resourceConfigs.size());
        org.junit.Assert.assertEquals("resource", resourceConfigs.get(0).getName());
        org.junit.Assert.assertEquals("subresource", resourceConfigs.get(1).getName());
    }

    @org.junit.Test
    public void testGetPluralName() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.view.configuration.ResourceConfig> resourceConfigs = org.apache.ambari.server.view.configuration.ResourceConfigTest.getResourceConfigs();
        org.junit.Assert.assertEquals(2, resourceConfigs.size());
        org.junit.Assert.assertEquals("resources", resourceConfigs.get(0).getPluralName());
        org.junit.Assert.assertEquals("subresources", resourceConfigs.get(1).getPluralName());
    }

    @org.junit.Test
    public void testGetIdProperty() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.view.configuration.ResourceConfig> resourceConfigs = org.apache.ambari.server.view.configuration.ResourceConfigTest.getResourceConfigs();
        org.junit.Assert.assertEquals(2, resourceConfigs.size());
        org.junit.Assert.assertEquals("id", resourceConfigs.get(0).getIdProperty());
        org.junit.Assert.assertEquals("id", resourceConfigs.get(1).getIdProperty());
    }

    @org.junit.Test
    public void testGetSubResourceNames() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.view.configuration.ResourceConfig> resourceConfigs = org.apache.ambari.server.view.configuration.ResourceConfigTest.getResourceConfigs();
        org.junit.Assert.assertEquals(2, resourceConfigs.size());
        org.junit.Assert.assertEquals(1, resourceConfigs.get(0).getSubResourceNames().size());
        org.junit.Assert.assertEquals("subresource", resourceConfigs.get(0).getSubResourceNames().get(0));
        org.junit.Assert.assertEquals(0, resourceConfigs.get(1).getSubResourceNames().size());
    }

    @org.junit.Test
    public void testGetProviderClass() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.view.configuration.ResourceConfig> resourceConfigs = org.apache.ambari.server.view.configuration.ResourceConfigTest.getResourceConfigs();
        org.junit.Assert.assertEquals(2, resourceConfigs.size());
        org.junit.Assert.assertTrue(resourceConfigs.get(0).getProviderClass(getClass().getClassLoader()).equals(org.apache.ambari.server.view.configuration.ViewConfigTest.MyResourceProvider.class));
        org.junit.Assert.assertTrue(resourceConfigs.get(1).getProviderClass(getClass().getClassLoader()).equals(org.apache.ambari.server.view.configuration.ViewConfigTest.MyResourceProvider.class));
    }

    @org.junit.Test
    public void testGetServiceClass() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.view.configuration.ResourceConfig> resourceConfigs = org.apache.ambari.server.view.configuration.ResourceConfigTest.getResourceConfigs();
        org.junit.Assert.assertEquals(2, resourceConfigs.size());
        org.junit.Assert.assertTrue(resourceConfigs.get(0).getResourceClass(getClass().getClassLoader()).equals(org.apache.ambari.server.view.configuration.ViewConfigTest.MyResource.class));
        org.junit.Assert.assertTrue(resourceConfigs.get(1).getResourceClass(getClass().getClassLoader()).equals(org.apache.ambari.server.view.configuration.ViewConfigTest.MyResource.class));
    }

    @org.junit.Test
    public void testGetResourceClass() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.view.configuration.ResourceConfig> resourceConfigs = org.apache.ambari.server.view.configuration.ResourceConfigTest.getResourceConfigs();
        org.junit.Assert.assertEquals(2, resourceConfigs.size());
        org.junit.Assert.assertTrue(resourceConfigs.get(0).getServiceClass(getClass().getClassLoader()).equals(org.apache.ambari.server.view.configuration.ViewConfigTest.MyResourceService.class));
        org.junit.Assert.assertTrue(resourceConfigs.get(1).getServiceClass(getClass().getClassLoader()).equals(org.apache.ambari.server.view.configuration.ViewConfigTest.MyResourceService.class));
    }

    public static java.util.List<org.apache.ambari.server.view.configuration.ResourceConfig> getResourceConfigs() throws javax.xml.bind.JAXBException {
        org.apache.ambari.server.view.configuration.ViewConfig viewConfig = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig();
        return viewConfig.getResources();
    }
}