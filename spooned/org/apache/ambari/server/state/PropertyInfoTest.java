package org.apache.ambari.server.state;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
public class PropertyInfoTest {
    @org.junit.Test
    public void testProperty() {
        org.apache.ambari.server.state.PropertyInfo property = new org.apache.ambari.server.state.PropertyInfo();
        property.setName("name");
        property.setValue("value");
        property.setDescription("desc");
        property.setFilename("filename");
        org.apache.ambari.server.state.PropertyDependencyInfo pdi = new org.apache.ambari.server.state.PropertyDependencyInfo("type", "name");
        property.getDependsOnProperties().add(pdi);
        org.junit.Assert.assertEquals("name", property.getName());
        org.junit.Assert.assertEquals("value", property.getValue());
        org.junit.Assert.assertEquals("desc", property.getDescription());
        org.junit.Assert.assertEquals("filename", property.getFilename());
        org.junit.Assert.assertEquals(1, property.getDependsOnProperties().size());
        org.junit.Assert.assertTrue(property.getDependsOnProperties().contains(pdi));
    }

    @org.junit.Test
    public void testAttributes() throws java.lang.Exception {
        org.apache.ambari.server.state.PropertyInfo property = new org.apache.ambari.server.state.PropertyInfo();
        java.util.List<org.w3c.dom.Element> elements = new java.util.ArrayList<>();
        org.w3c.dom.Element e1 = EasyMock.createNiceMock(org.w3c.dom.Element.class);
        org.w3c.dom.Element e2 = EasyMock.createNiceMock(org.w3c.dom.Element.class);
        org.w3c.dom.Node n1 = EasyMock.createNiceMock(org.w3c.dom.Node.class);
        org.w3c.dom.Node n2 = EasyMock.createNiceMock(org.w3c.dom.Node.class);
        elements.add(e1);
        elements.add(e2);
        EasyMock.expect(e1.getTagName()).andReturn("foo").anyTimes();
        EasyMock.expect(e1.getFirstChild()).andReturn(n1).anyTimes();
        EasyMock.expect(n1.getNodeValue()).andReturn("value1").anyTimes();
        EasyMock.expect(e2.getTagName()).andReturn("bar").anyTimes();
        EasyMock.expect(e2.getFirstChild()).andReturn(n2).anyTimes();
        EasyMock.expect(n2.getNodeValue()).andReturn("value2").anyTimes();
        EasyMock.replay(e1, e2, n1, n2);
        java.lang.reflect.Field f = property.getClass().getDeclaredField("propertyAttributes");
        f.setAccessible(true);
        f.set(property, elements);
        java.util.Map<java.lang.String, java.lang.String> attributes = property.getAttributesMap();
        org.junit.Assert.assertEquals(2, attributes.size());
        org.junit.Assert.assertEquals("value1", attributes.get("foo"));
        org.junit.Assert.assertEquals("value2", attributes.get("bar"));
    }

    @org.junit.Test
    public void testUpgradeBehaviorTag() throws javax.xml.bind.JAXBException {
        java.lang.String xml = "<property>\n" + ((("  <name>prop_name</name>\n" + "  <value>prop_val</value>\n") + "  <on-ambari-upgrade add=\"true\" update=\"true\" delete=\"true\"/>\n") + "</property>");
        org.apache.ambari.server.state.PropertyInfo propertyInfo = org.apache.ambari.server.state.PropertyInfoTest.propertyInfoFrom(xml);
        org.junit.Assert.assertTrue(propertyInfo.getPropertyAmbariUpgradeBehavior().isAdd());
        org.junit.Assert.assertTrue(propertyInfo.getPropertyAmbariUpgradeBehavior().isUpdate());
        org.junit.Assert.assertTrue(propertyInfo.getPropertyAmbariUpgradeBehavior().isDelete());
    }

    @org.junit.Test
    public void testBehaviorWithoutUpgradeTags() throws javax.xml.bind.JAXBException {
        java.lang.String xml = "<property>\n" + (("  <name>prop_name</name>\n" + "  <value>prop_val</value>\n") + "</property>");
        org.apache.ambari.server.state.PropertyInfo propertyInfo = org.apache.ambari.server.state.PropertyInfoTest.propertyInfoFrom(xml);
        org.junit.Assert.assertTrue(propertyInfo.getPropertyAmbariUpgradeBehavior().isAdd());
        org.junit.Assert.assertFalse(propertyInfo.getPropertyAmbariUpgradeBehavior().isUpdate());
        org.junit.Assert.assertFalse(propertyInfo.getPropertyAmbariUpgradeBehavior().isDelete());
    }

    @org.junit.Test
    public void testBehaviorWithSupportedRefreshCommandsTags() throws javax.xml.bind.JAXBException {
        java.lang.String xml = "<property>\n" + (((((" <name>prop_name</name>\n" + " <value>prop_val</value>\n") + " <supported-refresh-commands>\n") + "   <refresh-command componentName=\"NAMENODE\" command=\"reload_configs\" />\n") + " </supported-refresh-commands>\n") + "</property>");
        org.apache.ambari.server.state.PropertyInfo propertyInfo = org.apache.ambari.server.state.PropertyInfoTest.propertyInfoFrom(xml);
        org.junit.Assert.assertEquals(propertyInfo.getSupportedRefreshCommands().iterator().next().getCommand(), "reload_configs");
        org.junit.Assert.assertEquals(propertyInfo.getSupportedRefreshCommands().iterator().next().getComponentName(), "NAMENODE");
    }

    @org.junit.Test
    public void testUnknownPropertyType() throws java.lang.Exception {
        java.lang.String xml = "<property>\n" + (((("  <name>prop_name</name>\n" + "  <value>prop_val</value>\n") + "  <property-type>PASSWORD USER UNRECOGNIZED_TYPE</property-type>\n") + "  <description>test description</description>\n") + "</property>");
        org.apache.ambari.server.state.PropertyInfo propertyInfo = org.apache.ambari.server.state.PropertyInfoTest.propertyInfoFrom(xml);
        java.util.Set<org.apache.ambari.server.state.PropertyInfo.PropertyType> expectedPropertyTypes = com.google.common.collect.Sets.newHashSet(org.apache.ambari.server.state.PropertyInfo.PropertyType.PASSWORD, org.apache.ambari.server.state.PropertyInfo.PropertyType.USER);
        org.junit.Assert.assertEquals(expectedPropertyTypes, propertyInfo.getPropertyTypes());
    }

    public static org.apache.ambari.server.state.PropertyInfo propertyInfoFrom(java.lang.String xml) throws javax.xml.bind.JAXBException {
        javax.xml.bind.JAXBContext jaxbCtx = javax.xml.bind.JAXBContext.newInstance(org.apache.ambari.server.state.PropertyInfo.class, org.apache.ambari.server.state.PropertyUpgradeBehavior.class);
        javax.xml.bind.Unmarshaller unmarshaller = jaxbCtx.createUnmarshaller();
        return unmarshaller.unmarshal(new javax.xml.transform.stream.StreamSource(new java.io.ByteArrayInputStream(xml.getBytes())), org.apache.ambari.server.state.PropertyInfo.class).getValue();
    }
}