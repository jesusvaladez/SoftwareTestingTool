package org.apache.ambari.server.view.configuration;
import javax.xml.bind.JAXBException;
public class InstanceConfigTest {
    private static java.lang.String xml_no_properties = "<view>\n" + ((((((((((((((("    <name>MY_VIEW</name>\n" + "    <label>My View!</label>\n") + "    <version>1.0.0</version>\n") + "    <resource>\n") + "        <name>resource</name>\n") + "        <plural-name>resources</plural-name>\n") + "        <id-property>id</id-property>\n") + "        <resource-class>org.apache.ambari.server.view.configuration.ViewConfigTest$MyResource</resource-class>\n") + "        <provider-class>org.apache.ambari.server.view.configuration.ViewConfigTest$MyResourceProvider</provider-class>\n") + "        <service-class>org.apache.ambari.server.view.configuration.ViewConfigTest$MyResourceService</service-class>\n") + "        <sub-resource-name>subresource</sub-resource-name>\n") + "    </resource>\n") + "    <instance>\n") + "        <name>INSTANCE1</name>\n") + "    </instance>\n") + "</view>");

    private static java.lang.String xml_visible = "<view>\n" + (((((((((((((("    <name>MY_VIEW</name>\n" + "    <label>My View!</label>\n") + "    <version>1.0.0</version>\n") + "    <instance>\n") + "        <name>INSTANCE1</name>\n") + "        <visible>true</visible>\n") + "    </instance>\n") + "    <instance>\n") + "        <name>INSTANCE2</name>\n") + "        <visible>false</visible>\n") + "    </instance>\n") + "    <instance>\n") + "        <name>INSTANCE3</name>\n") + "    </instance>\n") + "</view>");

    @org.junit.Test
    public void testGetName() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.view.configuration.InstanceConfig> instances = org.apache.ambari.server.view.configuration.InstanceConfigTest.getInstanceConfigs();
        org.junit.Assert.assertEquals(2, instances.size());
        org.junit.Assert.assertEquals("INSTANCE1", instances.get(0).getName());
        org.junit.Assert.assertEquals("INSTANCE2", instances.get(1).getName());
    }

    @org.junit.Test
    public void testGetLabel() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.view.configuration.InstanceConfig> instances = org.apache.ambari.server.view.configuration.InstanceConfigTest.getInstanceConfigs();
        org.junit.Assert.assertEquals(2, instances.size());
        org.junit.Assert.assertEquals("My Instance 1!", instances.get(0).getLabel());
        org.junit.Assert.assertEquals("My Instance 2!", instances.get(1).getLabel());
    }

    @org.junit.Test
    public void testGetDescription() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.view.configuration.InstanceConfig> instances = org.apache.ambari.server.view.configuration.InstanceConfigTest.getInstanceConfigs();
        org.junit.Assert.assertEquals(2, instances.size());
        org.junit.Assert.assertEquals("This is a description.", instances.get(0).getDescription());
        org.junit.Assert.assertNull(instances.get(1).getDescription());
    }

    @org.junit.Test
    public void testIsVisible() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.view.configuration.InstanceConfig> instances = org.apache.ambari.server.view.configuration.InstanceConfigTest.getInstanceConfigs(org.apache.ambari.server.view.configuration.InstanceConfigTest.xml_visible);
        org.junit.Assert.assertEquals(3, instances.size());
        org.junit.Assert.assertTrue(instances.get(0).isVisible());
        org.junit.Assert.assertFalse(instances.get(1).isVisible());
        org.junit.Assert.assertTrue(instances.get(2).isVisible());
    }

    @org.junit.Test
    public void testGetIcon() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.view.configuration.InstanceConfig> instances = org.apache.ambari.server.view.configuration.InstanceConfigTest.getInstanceConfigs();
        org.junit.Assert.assertEquals(2, instances.size());
        org.junit.Assert.assertEquals("/this/is/the/icon/url/instance_1_icon.png", instances.get(0).getIcon());
        org.junit.Assert.assertNull(instances.get(1).getIcon());
    }

    @org.junit.Test
    public void testGetIcon64() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.view.configuration.InstanceConfig> instances = org.apache.ambari.server.view.configuration.InstanceConfigTest.getInstanceConfigs();
        org.junit.Assert.assertEquals(2, instances.size());
        org.junit.Assert.assertEquals("/this/is/the/icon/url/instance_1_icon64.png", instances.get(0).getIcon64());
        org.junit.Assert.assertNull(instances.get(1).getIcon());
    }

    @org.junit.Test
    public void testGetProperties() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.view.configuration.InstanceConfig> instances = org.apache.ambari.server.view.configuration.InstanceConfigTest.getInstanceConfigs();
        org.junit.Assert.assertEquals(2, instances.size());
        java.util.List<org.apache.ambari.server.view.configuration.PropertyConfig> properties = instances.get(0).getProperties();
        org.junit.Assert.assertEquals(2, properties.size());
        properties = instances.get(1).getProperties();
        org.junit.Assert.assertEquals(1, properties.size());
        instances = org.apache.ambari.server.view.configuration.InstanceConfigTest.getInstanceConfigs(org.apache.ambari.server.view.configuration.InstanceConfigTest.xml_no_properties);
        org.junit.Assert.assertEquals(1, instances.size());
        properties = instances.get(0).getProperties();
        org.junit.Assert.assertNotNull(properties);
        org.junit.Assert.assertEquals(0, properties.size());
    }

    public static java.util.List<org.apache.ambari.server.view.configuration.InstanceConfig> getInstanceConfigs() throws javax.xml.bind.JAXBException {
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig();
        return config.getInstances();
    }

    public static java.util.List<org.apache.ambari.server.view.configuration.InstanceConfig> getInstanceConfigs(java.lang.String xml) throws javax.xml.bind.JAXBException {
        org.apache.ambari.server.view.configuration.ViewConfig config = org.apache.ambari.server.view.configuration.ViewConfigTest.getConfig(xml);
        return config.getInstances();
    }
}