package org.apache.ambari.server.topology;
public class ConfigurationTest {
    private static final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> EMPTY_PROPERTIES = new java.util.HashMap<>();

    private static final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> EMPTY_ATTRIBUTES = new java.util.HashMap<>();

    @org.junit.Test
    public void testGetProperties_noParent() {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProperties1 = new java.util.HashMap<>();
        typeProperties1.put("prop1", "val1");
        typeProperties1.put("prop2", "val2");
        java.util.Map<java.lang.String, java.lang.String> typeProperties2 = new java.util.HashMap<>();
        typeProperties2.put("prop1", "val1");
        typeProperties2.put("prop3", "val3");
        properties.put("type1", typeProperties1);
        properties.put("type2", typeProperties2);
        org.apache.ambari.server.topology.Configuration configuration = new org.apache.ambari.server.topology.Configuration(properties, org.apache.ambari.server.topology.ConfigurationTest.EMPTY_ATTRIBUTES);
        org.junit.Assert.assertEquals(properties, configuration.getProperties());
        org.junit.Assert.assertEquals(org.apache.ambari.server.topology.ConfigurationTest.EMPTY_ATTRIBUTES, configuration.getAttributes());
    }

    @org.junit.Test
    public void testGetFullProperties_noParent() {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProperties1 = new java.util.HashMap<>();
        typeProperties1.put("prop1", "val1");
        typeProperties1.put("prop2", "val2");
        java.util.Map<java.lang.String, java.lang.String> typeProperties2 = new java.util.HashMap<>();
        typeProperties2.put("prop1", "val1");
        typeProperties2.put("prop3", "val3");
        properties.put("type1", typeProperties1);
        properties.put("type2", typeProperties2);
        org.apache.ambari.server.topology.Configuration configuration = new org.apache.ambari.server.topology.Configuration(properties, org.apache.ambari.server.topology.ConfigurationTest.EMPTY_ATTRIBUTES);
        org.junit.Assert.assertEquals(properties, configuration.getFullProperties());
        org.junit.Assert.assertEquals(org.apache.ambari.server.topology.ConfigurationTest.EMPTY_ATTRIBUTES, configuration.getAttributes());
    }

    @org.junit.Test
    public void testGetProperties_withParent() {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProperties1 = new java.util.HashMap<>();
        typeProperties1.put("prop1", "val1");
        typeProperties1.put("prop2", "val2");
        java.util.Map<java.lang.String, java.lang.String> typeProperties2 = new java.util.HashMap<>();
        typeProperties2.put("prop1", "val1");
        typeProperties2.put("prop3", "val3");
        properties.put("type1", typeProperties1);
        properties.put("type2", typeProperties2);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> parentTypeProperties1 = new java.util.HashMap<>();
        parentTypeProperties1.put("prop5", "val5");
        java.util.Map<java.lang.String, java.lang.String> parentTypeProperties3 = new java.util.HashMap<>();
        parentTypeProperties3.put("prop6", "val6");
        parentProperties.put("type1", parentTypeProperties1);
        parentProperties.put("type3", parentTypeProperties3);
        org.apache.ambari.server.topology.Configuration parentConfiguration = new org.apache.ambari.server.topology.Configuration(parentProperties, org.apache.ambari.server.topology.ConfigurationTest.EMPTY_ATTRIBUTES);
        org.apache.ambari.server.topology.Configuration configuration = new org.apache.ambari.server.topology.Configuration(properties, org.apache.ambari.server.topology.ConfigurationTest.EMPTY_ATTRIBUTES, parentConfiguration);
        org.junit.Assert.assertEquals(properties, configuration.getProperties());
        org.junit.Assert.assertEquals(org.apache.ambari.server.topology.ConfigurationTest.EMPTY_ATTRIBUTES, configuration.getAttributes());
    }

    @org.junit.Test
    public void testGetFullProperties_withParent() {
        org.apache.ambari.server.topology.Configuration configuration = createConfigurationWithParents_PropsOnly();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> fullProperties = configuration.getFullProperties();
        org.junit.Assert.assertEquals(4, fullProperties.size());
        java.util.Map<java.lang.String, java.lang.String> type1Props = fullProperties.get("type1");
        org.junit.Assert.assertEquals(5, type1Props.size());
        org.junit.Assert.assertEquals("val1.3", type1Props.get("prop1"));
        org.junit.Assert.assertEquals("val2.2", type1Props.get("prop2"));
        org.junit.Assert.assertEquals("val3.1", type1Props.get("prop3"));
        org.junit.Assert.assertEquals("val6.2", type1Props.get("prop6"));
        org.junit.Assert.assertEquals("val9.3", type1Props.get("prop9"));
        java.util.Map<java.lang.String, java.lang.String> type2Props = fullProperties.get("type2");
        org.junit.Assert.assertEquals(2, type2Props.size());
        org.junit.Assert.assertEquals("val4.3", type2Props.get("prop4"));
        org.junit.Assert.assertEquals("val5.1", type2Props.get("prop5"));
        java.util.Map<java.lang.String, java.lang.String> type3Props = fullProperties.get("type3");
        org.junit.Assert.assertEquals(2, type3Props.size());
        org.junit.Assert.assertEquals("val7.3", type3Props.get("prop7"));
        org.junit.Assert.assertEquals("val8.2", type3Props.get("prop8"));
        java.util.Map<java.lang.String, java.lang.String> type4Props = fullProperties.get("type4");
        org.junit.Assert.assertEquals(2, type4Props.size());
        org.junit.Assert.assertEquals("val10.3", type4Props.get("prop10"));
        org.junit.Assert.assertEquals("val11.3", type4Props.get("prop11"));
        org.apache.ambari.server.topology.Configuration expectedConfiguration = createConfigurationWithParents_PropsOnly();
        org.junit.Assert.assertEquals(expectedConfiguration.getProperties(), configuration.getProperties());
        org.junit.Assert.assertEquals(expectedConfiguration.getParentConfiguration().getProperties(), configuration.getParentConfiguration().getProperties());
        org.junit.Assert.assertEquals(expectedConfiguration.getParentConfiguration().getParentConfiguration().getProperties(), configuration.getParentConfiguration().getParentConfiguration().getProperties());
        org.junit.Assert.assertEquals(org.apache.ambari.server.topology.ConfigurationTest.EMPTY_ATTRIBUTES, configuration.getAttributes());
        java.util.Collection<java.lang.String> configTypes = configuration.getAllConfigTypes();
        org.junit.Assert.assertEquals(4, configTypes.size());
        org.junit.Assert.assertTrue(configTypes.containsAll(java.util.Arrays.asList("type1", "type2", "type3", "type4")));
    }

    @org.junit.Test
    public void containsConfigType() {
        org.apache.ambari.server.topology.Configuration configuration = createConfigurationWithParents_PropsOnly();
        org.junit.Assert.assertTrue(configuration.containsConfigType("type1"));
        org.junit.Assert.assertTrue(configuration.containsConfigType("type2"));
        org.junit.Assert.assertTrue(configuration.containsConfigType("type3"));
        org.junit.Assert.assertTrue(configuration.containsConfigType("type4"));
        org.junit.Assert.assertFalse(configuration.containsConfigType("type5"));
        configuration = createConfigurationWithParents_AttributesOnly();
        org.junit.Assert.assertTrue(configuration.containsConfigType("type1"));
        org.junit.Assert.assertTrue(configuration.containsConfigType("type2"));
        org.junit.Assert.assertFalse(configuration.containsConfigType("type3"));
    }

    @org.junit.Test
    public void containsConfig() {
        org.apache.ambari.server.topology.Configuration configuration = createConfigurationWithParents_PropsOnly();
        org.junit.Assert.assertTrue(configuration.containsConfig("type1", "prop1"));
        org.junit.Assert.assertTrue(configuration.containsConfig("type1", "prop2"));
        org.junit.Assert.assertTrue(configuration.containsConfig("type1", "prop3"));
        org.junit.Assert.assertTrue(configuration.containsConfig("type2", "prop4"));
        org.junit.Assert.assertTrue(configuration.containsConfig("type2", "prop5"));
        org.junit.Assert.assertTrue(configuration.containsConfig("type1", "prop6"));
        org.junit.Assert.assertTrue(configuration.containsConfig("type1", "prop9"));
        org.junit.Assert.assertTrue(configuration.containsConfig("type3", "prop7"));
        org.junit.Assert.assertTrue(configuration.containsConfig("type3", "prop8"));
        org.junit.Assert.assertTrue(configuration.containsConfig("type4", "prop10"));
        org.junit.Assert.assertTrue(configuration.containsConfig("type4", "prop11"));
        org.junit.Assert.assertFalse(configuration.containsConfig("type1", "prop99"));
        org.junit.Assert.assertFalse(configuration.containsConfig("core-site", "io.file.buffer.size"));
        configuration = createConfigurationWithParents_AttributesOnly();
        org.junit.Assert.assertTrue(configuration.containsConfig("type1", "prop1"));
        org.junit.Assert.assertTrue(configuration.containsConfig("type1", "prop2"));
        org.junit.Assert.assertTrue(configuration.containsConfig("type1", "prop3"));
        org.junit.Assert.assertTrue(configuration.containsConfig("type1", "prop6"));
        org.junit.Assert.assertTrue(configuration.containsConfig("type1", "prop7"));
        org.junit.Assert.assertTrue(configuration.containsConfig("type1", "prop8"));
        org.junit.Assert.assertTrue(configuration.containsConfig("type1", "prop9"));
        org.junit.Assert.assertTrue(configuration.containsConfig("type1", "prop10"));
        org.junit.Assert.assertTrue(configuration.containsConfig("type1", "prop11"));
        org.junit.Assert.assertTrue(configuration.containsConfig("type2", "prop100"));
        org.junit.Assert.assertTrue(configuration.containsConfig("type2", "prop101"));
        org.junit.Assert.assertTrue(configuration.containsConfig("type2", "prop102"));
        org.junit.Assert.assertFalse(configuration.containsConfig("type1", "prop99"));
        org.junit.Assert.assertFalse(configuration.containsConfig("core-site", "io.file.buffer.size"));
    }

    @org.junit.Test
    public void testGetFullProperties_withParent_specifyDepth() {
        org.apache.ambari.server.topology.Configuration configuration = createConfigurationWithParents_PropsOnly();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> fullProperties = configuration.getFullProperties(1);
        org.junit.Assert.assertEquals(4, fullProperties.size());
        java.util.Map<java.lang.String, java.lang.String> type1Props = fullProperties.get("type1");
        org.junit.Assert.assertEquals(4, type1Props.size());
        org.junit.Assert.assertEquals("val1.3", type1Props.get("prop1"));
        org.junit.Assert.assertEquals("val2.2", type1Props.get("prop2"));
        org.junit.Assert.assertEquals("val6.2", type1Props.get("prop6"));
        org.junit.Assert.assertEquals("val9.3", type1Props.get("prop9"));
        java.util.Map<java.lang.String, java.lang.String> type2Props = fullProperties.get("type2");
        org.junit.Assert.assertEquals(1, type2Props.size());
        org.junit.Assert.assertEquals("val4.3", type2Props.get("prop4"));
        java.util.Map<java.lang.String, java.lang.String> type3Props = fullProperties.get("type3");
        org.junit.Assert.assertEquals(2, type3Props.size());
        org.junit.Assert.assertEquals("val7.3", type3Props.get("prop7"));
        org.junit.Assert.assertEquals("val8.2", type3Props.get("prop8"));
        java.util.Map<java.lang.String, java.lang.String> type4Props = fullProperties.get("type4");
        org.junit.Assert.assertEquals(2, type4Props.size());
        org.junit.Assert.assertEquals("val10.3", type4Props.get("prop10"));
        org.junit.Assert.assertEquals("val11.3", type4Props.get("prop11"));
        org.apache.ambari.server.topology.Configuration expectedConfiguration = createConfigurationWithParents_PropsOnly();
        org.junit.Assert.assertEquals(expectedConfiguration.getProperties(), configuration.getProperties());
        org.junit.Assert.assertEquals(expectedConfiguration.getParentConfiguration().getProperties(), configuration.getParentConfiguration().getProperties());
        org.junit.Assert.assertEquals(expectedConfiguration.getParentConfiguration().getParentConfiguration().getProperties(), configuration.getParentConfiguration().getParentConfiguration().getProperties());
        org.junit.Assert.assertEquals(org.apache.ambari.server.topology.ConfigurationTest.EMPTY_ATTRIBUTES, configuration.getAttributes());
    }

    @org.junit.Test
    public void testGetAttributes_noParent() {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> attributes = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> attributeProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> properties1 = new java.util.HashMap<>();
        properties1.put("prop1", "val1");
        properties1.put("prop2", "val2");
        java.util.Map<java.lang.String, java.lang.String> properties2 = new java.util.HashMap<>();
        properties2.put("prop1", "val3");
        attributeProperties.put("attribute1", properties1);
        attributeProperties.put("attribute2", properties2);
        attributes.put("type1", attributeProperties);
        org.apache.ambari.server.topology.Configuration configuration = new org.apache.ambari.server.topology.Configuration(org.apache.ambari.server.topology.ConfigurationTest.EMPTY_PROPERTIES, attributes);
        org.junit.Assert.assertEquals(attributes, configuration.getAttributes());
        org.junit.Assert.assertEquals(org.apache.ambari.server.topology.ConfigurationTest.EMPTY_PROPERTIES, configuration.getProperties());
    }

    @org.junit.Test
    public void testGetFullAttributes_withParent() {
        org.apache.ambari.server.topology.Configuration configuration = createConfigurationWithParents_AttributesOnly();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> fullAttributes = configuration.getFullAttributes();
        org.junit.Assert.assertEquals(2, fullAttributes.size());
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> type1Attributes = fullAttributes.get("type1");
        org.junit.Assert.assertEquals(4, type1Attributes.size());
        java.util.Map<java.lang.String, java.lang.String> attribute1Properties = type1Attributes.get("attribute1");
        org.junit.Assert.assertEquals(5, attribute1Properties.size());
        org.junit.Assert.assertEquals("val1.3", attribute1Properties.get("prop1"));
        org.junit.Assert.assertEquals("val2.2", attribute1Properties.get("prop2"));
        org.junit.Assert.assertEquals("val3.1", attribute1Properties.get("prop3"));
        org.junit.Assert.assertEquals("val6.2", attribute1Properties.get("prop6"));
        org.junit.Assert.assertEquals("val9.3", attribute1Properties.get("prop9"));
        java.util.Map<java.lang.String, java.lang.String> attribute2Properties = type1Attributes.get("attribute2");
        org.junit.Assert.assertEquals(2, attribute2Properties.size());
        org.junit.Assert.assertEquals("val4.3", attribute2Properties.get("prop4"));
        org.junit.Assert.assertEquals("val5.1", attribute2Properties.get("prop5"));
        java.util.Map<java.lang.String, java.lang.String> attribute3Properties = type1Attributes.get("attribute3");
        org.junit.Assert.assertEquals(2, attribute3Properties.size());
        org.junit.Assert.assertEquals("val7.3", attribute3Properties.get("prop7"));
        org.junit.Assert.assertEquals("val8.2", attribute3Properties.get("prop8"));
        java.util.Map<java.lang.String, java.lang.String> attribute4Properties = type1Attributes.get("attribute4");
        org.junit.Assert.assertEquals(2, attribute4Properties.size());
        org.junit.Assert.assertEquals("val10.3", attribute4Properties.get("prop10"));
        org.junit.Assert.assertEquals("val11.3", attribute4Properties.get("prop11"));
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> type2Attributes = fullAttributes.get("type2");
        org.junit.Assert.assertEquals(2, type2Attributes.size());
        java.util.Map<java.lang.String, java.lang.String> attribute100Properties = type2Attributes.get("attribute100");
        org.junit.Assert.assertEquals(3, attribute100Properties.size());
        org.junit.Assert.assertEquals("val100.3", attribute100Properties.get("prop100"));
        org.junit.Assert.assertEquals("val101.1", attribute100Properties.get("prop101"));
        org.junit.Assert.assertEquals("val102.3", attribute100Properties.get("prop102"));
        java.util.Map<java.lang.String, java.lang.String> attribute101Properties = type2Attributes.get("attribute101");
        org.junit.Assert.assertEquals(2, attribute101Properties.size());
        org.junit.Assert.assertEquals("val100.2", attribute101Properties.get("prop100"));
        org.junit.Assert.assertEquals("val101.1", attribute101Properties.get("prop101"));
        org.apache.ambari.server.topology.Configuration expectedConfiguration = createConfigurationWithParents_AttributesOnly();
        org.junit.Assert.assertEquals(expectedConfiguration.getAttributes(), configuration.getAttributes());
        org.junit.Assert.assertEquals(expectedConfiguration.getParentConfiguration().getAttributes(), configuration.getParentConfiguration().getAttributes());
        org.junit.Assert.assertEquals(expectedConfiguration.getParentConfiguration().getParentConfiguration().getAttributes(), configuration.getParentConfiguration().getParentConfiguration().getAttributes());
        org.junit.Assert.assertEquals(org.apache.ambari.server.topology.ConfigurationTest.EMPTY_PROPERTIES, configuration.getProperties());
        java.util.Collection<java.lang.String> configTypes = configuration.getAllConfigTypes();
        org.junit.Assert.assertEquals(2, configTypes.size());
        org.junit.Assert.assertTrue(configTypes.containsAll(java.util.Arrays.asList("type1", "type2")));
    }

    @org.junit.Test
    public void testGetPropertyValue() {
        org.apache.ambari.server.topology.Configuration configuration = createConfigurationWithParents_PropsOnly();
        org.junit.Assert.assertEquals("val1.3", configuration.getPropertyValue("type1", "prop1"));
        org.junit.Assert.assertEquals("val2.2", configuration.getPropertyValue("type1", "prop2"));
        org.junit.Assert.assertEquals("val3.1", configuration.getPropertyValue("type1", "prop3"));
        org.junit.Assert.assertEquals("val4.3", configuration.getPropertyValue("type2", "prop4"));
        org.junit.Assert.assertEquals("val5.1", configuration.getPropertyValue("type2", "prop5"));
        org.junit.Assert.assertEquals("val6.2", configuration.getPropertyValue("type1", "prop6"));
        org.junit.Assert.assertEquals("val7.3", configuration.getPropertyValue("type3", "prop7"));
        org.junit.Assert.assertEquals("val8.2", configuration.getPropertyValue("type3", "prop8"));
        org.junit.Assert.assertEquals("val9.3", configuration.getPropertyValue("type1", "prop9"));
        org.junit.Assert.assertEquals("val10.3", configuration.getPropertyValue("type4", "prop10"));
        org.junit.Assert.assertEquals("val11.3", configuration.getPropertyValue("type4", "prop11"));
    }

    @org.junit.Test
    public void testGetAttributeValue() {
        org.apache.ambari.server.topology.Configuration configuration = createConfigurationWithParents_AttributesOnly();
        org.junit.Assert.assertEquals("val1.3", configuration.getAttributeValue("type1", "prop1", "attribute1"));
        org.junit.Assert.assertEquals("val2.2", configuration.getAttributeValue("type1", "prop2", "attribute1"));
        org.junit.Assert.assertEquals("val3.1", configuration.getAttributeValue("type1", "prop3", "attribute1"));
        org.junit.Assert.assertEquals("val4.3", configuration.getAttributeValue("type1", "prop4", "attribute2"));
        org.junit.Assert.assertEquals("val5.1", configuration.getAttributeValue("type1", "prop5", "attribute2"));
        org.junit.Assert.assertEquals("val6.2", configuration.getAttributeValue("type1", "prop6", "attribute1"));
        org.junit.Assert.assertEquals("val7.3", configuration.getAttributeValue("type1", "prop7", "attribute3"));
        org.junit.Assert.assertEquals("val8.2", configuration.getAttributeValue("type1", "prop8", "attribute3"));
        org.junit.Assert.assertEquals("val100.3", configuration.getAttributeValue("type2", "prop100", "attribute100"));
        org.junit.Assert.assertEquals("val101.1", configuration.getAttributeValue("type2", "prop101", "attribute100"));
        org.junit.Assert.assertEquals("val102.3", configuration.getAttributeValue("type2", "prop102", "attribute100"));
        org.junit.Assert.assertEquals("val100.2", configuration.getAttributeValue("type2", "prop100", "attribute101"));
        org.junit.Assert.assertEquals("val101.1", configuration.getAttributeValue("type2", "prop101", "attribute101"));
    }

    @org.junit.Test
    public void testRemoveProperty() {
        org.apache.ambari.server.topology.Configuration configuration = createConfigurationWithParents_PropsOnly();
        org.junit.Assert.assertEquals("val3.1", configuration.removeProperty("type1", "prop3"));
        org.junit.Assert.assertNull(configuration.getPropertyValue("type1", "prop3"));
        org.junit.Assert.assertEquals("val9.3", configuration.removeProperty("type1", "prop9"));
        org.junit.Assert.assertNull(configuration.getPropertyValue("type1", "prop9"));
        org.junit.Assert.assertEquals("val1.3", configuration.removeProperty("type1", "prop1"));
        org.junit.Assert.assertNull(configuration.getPropertyValue("type1", "prop1"));
        org.junit.Assert.assertEquals("val4.3", configuration.removeProperty("type2", "prop4"));
        org.junit.Assert.assertNull(configuration.getPropertyValue("type2", "prop4"));
        org.junit.Assert.assertEquals("val2.2", configuration.removeProperty("type1", "prop2"));
        org.junit.Assert.assertNull(configuration.getPropertyValue("type1", "prop2"));
        org.junit.Assert.assertNull(configuration.getPropertyValue("typeXXX", "XXXXX"));
        org.junit.Assert.assertNull(configuration.getPropertyValue("type1", "XXXXX"));
    }

    @org.junit.Test
    public void testRemoveConfigTypes() {
        org.apache.ambari.server.topology.Configuration configuration = createConfigurationWithParents_PropsOnly();
        configuration.removeConfigType("type1");
        org.junit.Assert.assertNull(configuration.getProperties().get("type1"));
    }

    @org.junit.Test
    public void testRemoveConfigTypesForAttributes() {
        org.apache.ambari.server.topology.Configuration configuration = createConfigurationWithParents_PropsOnly();
        configuration.removeConfigType("type1");
        org.junit.Assert.assertNull(configuration.getAttributes().get("type1"));
    }

    private org.apache.ambari.server.topology.Configuration createConfigurationWithParents_PropsOnly() {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentParentProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> parentParentTypeProperties1 = new java.util.HashMap<>();
        parentParentTypeProperties1.put("prop1", "val1.1");
        parentParentTypeProperties1.put("prop2", "val2.1");
        parentParentTypeProperties1.put("prop3", "val3.1");
        java.util.Map<java.lang.String, java.lang.String> parentParentTypeProperties2 = new java.util.HashMap<>();
        parentParentTypeProperties2.put("prop4", "val4.1");
        parentParentTypeProperties2.put("prop5", "val5.1");
        parentParentProperties.put("type1", parentParentTypeProperties1);
        parentParentProperties.put("type2", parentParentTypeProperties2);
        org.apache.ambari.server.topology.Configuration parentParentConfiguration = new org.apache.ambari.server.topology.Configuration(parentParentProperties, org.apache.ambari.server.topology.ConfigurationTest.EMPTY_ATTRIBUTES);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentProperties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> parentTypeProperties1 = new java.util.HashMap<>();
        parentTypeProperties1.put("prop1", "val1.2");
        parentTypeProperties1.put("prop2", "val2.2");
        parentTypeProperties1.put("prop6", "val6.2");
        java.util.Map<java.lang.String, java.lang.String> parentTypeProperties3 = new java.util.HashMap<>();
        parentTypeProperties3.put("prop7", "val7.2");
        parentTypeProperties3.put("prop8", "val8.2");
        parentProperties.put("type1", parentTypeProperties1);
        parentProperties.put("type3", parentTypeProperties3);
        org.apache.ambari.server.topology.Configuration parentConfiguration = new org.apache.ambari.server.topology.Configuration(parentProperties, org.apache.ambari.server.topology.ConfigurationTest.EMPTY_ATTRIBUTES, parentParentConfiguration);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.String> typeProperties1 = new java.util.HashMap<>();
        typeProperties1.put("prop1", "val1.3");
        typeProperties1.put("prop9", "val9.3");
        java.util.Map<java.lang.String, java.lang.String> typeProperties2 = new java.util.HashMap<>();
        typeProperties2.put("prop4", "val4.3");
        java.util.Map<java.lang.String, java.lang.String> typeProperties3 = new java.util.HashMap<>();
        typeProperties3.put("prop7", "val7.3");
        java.util.Map<java.lang.String, java.lang.String> typeProperties4 = new java.util.HashMap<>();
        typeProperties4.put("prop10", "val10.3");
        typeProperties4.put("prop11", "val11.3");
        properties.put("type1", typeProperties1);
        properties.put("type2", typeProperties2);
        properties.put("type3", typeProperties3);
        properties.put("type4", typeProperties4);
        return new org.apache.ambari.server.topology.Configuration(properties, org.apache.ambari.server.topology.ConfigurationTest.EMPTY_ATTRIBUTES, parentConfiguration);
    }

    private org.apache.ambari.server.topology.Configuration createConfigurationWithParents_AttributesOnly() {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> parentParentAttributes = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentParentTypeAttributes1 = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentParentTypeAttributes2 = new java.util.HashMap<>();
        parentParentAttributes.put("type1", parentParentTypeAttributes1);
        parentParentAttributes.put("type2", parentParentTypeAttributes2);
        java.util.Map<java.lang.String, java.lang.String> parentParentAttributeProperties1 = new java.util.HashMap<>();
        parentParentAttributeProperties1.put("prop1", "val1.1");
        parentParentAttributeProperties1.put("prop2", "val2.1");
        parentParentAttributeProperties1.put("prop3", "val3.1");
        java.util.Map<java.lang.String, java.lang.String> parentParentAttributeProperties2 = new java.util.HashMap<>();
        parentParentAttributeProperties2.put("prop4", "val4.1");
        parentParentAttributeProperties2.put("prop5", "val5.1");
        parentParentTypeAttributes1.put("attribute1", parentParentAttributeProperties1);
        parentParentTypeAttributes1.put("attribute2", parentParentAttributeProperties2);
        java.util.Map<java.lang.String, java.lang.String> parentParentAttributeProperties100 = new java.util.HashMap<>();
        parentParentAttributeProperties100.put("prop100", "val100.1");
        parentParentAttributeProperties100.put("prop101", "val101.1");
        java.util.Map<java.lang.String, java.lang.String> parentParentAttributeProperties101 = new java.util.HashMap<>();
        parentParentAttributeProperties101.put("prop100", "val100.1");
        parentParentAttributeProperties101.put("prop101", "val101.1");
        parentParentTypeAttributes2.put("attribute100", parentParentAttributeProperties100);
        parentParentTypeAttributes2.put("attribute101", parentParentAttributeProperties101);
        org.apache.ambari.server.topology.Configuration parentParentConfiguration = new org.apache.ambari.server.topology.Configuration(org.apache.ambari.server.topology.ConfigurationTest.EMPTY_PROPERTIES, new java.util.HashMap<>(parentParentAttributes));
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> parentAttributes = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentTypeAttributes1 = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> parentTypeAttributes2 = new java.util.HashMap<>();
        parentAttributes.put("type1", parentTypeAttributes1);
        parentAttributes.put("type2", parentTypeAttributes2);
        java.util.Map<java.lang.String, java.lang.String> parentAttributeProperties1 = new java.util.HashMap<>();
        parentAttributeProperties1.put("prop1", "val1.2");
        parentAttributeProperties1.put("prop2", "val2.2");
        parentAttributeProperties1.put("prop6", "val6.2");
        java.util.Map<java.lang.String, java.lang.String> parentAttributeProperties3 = new java.util.HashMap<>();
        parentAttributeProperties3.put("prop7", "val7.2");
        parentAttributeProperties3.put("prop8", "val8.2");
        parentTypeAttributes1.put("attribute1", parentAttributeProperties1);
        parentTypeAttributes1.put("attribute3", parentAttributeProperties3);
        java.util.Map<java.lang.String, java.lang.String> parentAttributeProperties101 = new java.util.HashMap<>();
        parentAttributeProperties101.put("prop100", "val100.2");
        parentTypeAttributes2.put("attribute101", parentAttributeProperties101);
        org.apache.ambari.server.topology.Configuration parentConfiguration = new org.apache.ambari.server.topology.Configuration(org.apache.ambari.server.topology.ConfigurationTest.EMPTY_PROPERTIES, new java.util.HashMap<>(parentAttributes), parentParentConfiguration);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> attributes = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> typeAttributes1 = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> typeAttributes2 = new java.util.HashMap<>();
        attributes.put("type1", typeAttributes1);
        attributes.put("type2", typeAttributes2);
        java.util.Map<java.lang.String, java.lang.String> attributeProperties1 = new java.util.HashMap<>();
        attributeProperties1.put("prop1", "val1.3");
        attributeProperties1.put("prop9", "val9.3");
        java.util.Map<java.lang.String, java.lang.String> attributeProperties2 = new java.util.HashMap<>();
        attributeProperties2.put("prop4", "val4.3");
        java.util.Map<java.lang.String, java.lang.String> attributeProperties3 = new java.util.HashMap<>();
        attributeProperties3.put("prop7", "val7.3");
        java.util.Map<java.lang.String, java.lang.String> attributeProperties4 = new java.util.HashMap<>();
        attributeProperties4.put("prop10", "val10.3");
        attributeProperties4.put("prop11", "val11.3");
        typeAttributes1.put("attribute1", attributeProperties1);
        typeAttributes1.put("attribute2", attributeProperties2);
        typeAttributes1.put("attribute3", attributeProperties3);
        typeAttributes1.put("attribute4", attributeProperties4);
        java.util.Map<java.lang.String, java.lang.String> attributeProperties100 = new java.util.HashMap<>();
        attributeProperties100.put("prop100", "val100.3");
        attributeProperties100.put("prop102", "val102.3");
        typeAttributes1.put("attribute1", attributeProperties1);
        typeAttributes2.put("attribute100", attributeProperties100);
        return new org.apache.ambari.server.topology.Configuration(org.apache.ambari.server.topology.ConfigurationTest.EMPTY_PROPERTIES, new java.util.HashMap<>(attributes), parentConfiguration);
    }

    @org.junit.Test
    public void moveProperties() {
        java.lang.String sourceType = "source";
        java.lang.String targetType = "target";
        java.lang.String sourceValue = "source value";
        java.lang.String targetValue = "target value";
        java.util.Map<java.lang.String, java.lang.String> keepers = com.google.common.collect.ImmutableMap.of("keep1", "v1", "keep2", "v3");
        java.util.Map<java.lang.String, java.lang.String> movers = com.google.common.collect.ImmutableMap.of("move1", "v2", "move2", "v4");
        java.util.Set<java.lang.String> common = com.google.common.collect.ImmutableSet.of("common1", "common2");
        org.apache.ambari.server.topology.Configuration config = new org.apache.ambari.server.topology.Configuration(new java.util.HashMap<>(), new java.util.HashMap<>());
        for (java.util.Map.Entry<java.lang.String, java.lang.String> e : keepers.entrySet()) {
            config.setProperty(sourceType, e.getKey(), e.getValue());
        }
        for (java.util.Map.Entry<java.lang.String, java.lang.String> e : movers.entrySet()) {
            config.setProperty(sourceType, e.getKey(), e.getValue());
        }
        for (java.lang.String key : common) {
            config.setProperty(sourceType, key, sourceValue);
            config.setProperty(targetType, key, targetValue);
        }
        com.google.common.collect.Sets.SetView<java.lang.String> propertiesToMove = com.google.common.collect.Sets.union(movers.keySet(), common);
        java.util.Set<java.lang.String> moved = config.moveProperties(sourceType, targetType, propertiesToMove);
        for (java.util.Map.Entry<java.lang.String, java.lang.String> e : keepers.entrySet()) {
            org.junit.Assert.assertEquals(e.getValue(), config.getPropertyValue(sourceType, e.getKey()));
        }
        for (java.util.Map.Entry<java.lang.String, java.lang.String> e : movers.entrySet()) {
            org.junit.Assert.assertEquals(e.getValue(), config.getPropertyValue(targetType, e.getKey()));
            org.junit.Assert.assertFalse(config.isPropertySet(sourceType, e.getKey()));
        }
        for (java.lang.String key : common) {
            org.junit.Assert.assertEquals(targetValue, config.getPropertyValue(targetType, key));
            org.junit.Assert.assertFalse(config.isPropertySet(sourceType, key));
        }
        org.junit.Assert.assertEquals(propertiesToMove, moved);
    }
}