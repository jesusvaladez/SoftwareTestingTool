package org.apache.ambari.server.topology;
public class ConfigurationFactoryTest {
    @org.junit.Test
    public void testOldSyntax() throws java.lang.Exception {
        org.apache.ambari.server.topology.ConfigurationFactory factory = new org.apache.ambari.server.topology.ConfigurationFactory();
        org.apache.ambari.server.topology.Configuration configuration = factory.getConfiguration(getOldSyntaxConfigProps());
        org.junit.Assert.assertEquals(2, configuration.getProperties().size());
        java.util.Map<java.lang.String, java.lang.String> configProperties1 = configuration.getProperties().get("foo-type");
        org.junit.Assert.assertEquals(2, configProperties1.size());
        org.junit.Assert.assertEquals("prop1Value", configProperties1.get("prop1"));
        org.junit.Assert.assertEquals("prop2Value", configProperties1.get("prop2"));
        java.util.Map<java.lang.String, java.lang.String> configProperties2 = configuration.getProperties().get("bar-type");
        org.junit.Assert.assertEquals(1, configProperties2.size());
        org.junit.Assert.assertEquals("prop3Value", configProperties2.get("prop3"));
        org.junit.Assert.assertTrue(configuration.getAttributes().isEmpty());
    }

    @org.junit.Test
    public void testNewSyntax() throws java.lang.Exception {
        org.apache.ambari.server.topology.ConfigurationFactory factory = new org.apache.ambari.server.topology.ConfigurationFactory();
        org.apache.ambari.server.topology.Configuration configuration = factory.getConfiguration(getNewSyntaxConfigProps());
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> properties = configuration.getProperties();
        org.junit.Assert.assertEquals(2, properties.size());
        java.util.Map<java.lang.String, java.lang.String> configProperties1 = properties.get("foo-type");
        org.junit.Assert.assertEquals(2, configProperties1.size());
        org.junit.Assert.assertEquals("prop1Value", configProperties1.get("prop1"));
        org.junit.Assert.assertEquals("prop2Value", configProperties1.get("prop2"));
        java.util.Map<java.lang.String, java.lang.String> configProperties2 = properties.get("bar-type");
        org.junit.Assert.assertEquals(1, configProperties2.size());
        org.junit.Assert.assertEquals("prop3Value", configProperties2.get("prop3"));
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>>> attributes = configuration.getAttributes();
        org.junit.Assert.assertEquals(2, attributes.size());
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configType1Attributes = attributes.get("foo-type");
        org.junit.Assert.assertEquals(2, configType1Attributes.size());
        java.util.Map<java.lang.String, java.lang.String> configType1Prop1Attributes = configType1Attributes.get("attribute1");
        org.junit.Assert.assertEquals(3, configType1Prop1Attributes.size());
        org.junit.Assert.assertEquals("attribute1-prop1-value", configType1Prop1Attributes.get("prop1"));
        org.junit.Assert.assertEquals("attribute1-prop2-value", configType1Prop1Attributes.get("prop2"));
        org.junit.Assert.assertEquals("attribute1-prop3-value", configType1Prop1Attributes.get("prop3"));
        java.util.Map<java.lang.String, java.lang.String> configType1Prop2Attributes = configType1Attributes.get("attribute2");
        org.junit.Assert.assertEquals(1, configType1Prop2Attributes.size());
        org.junit.Assert.assertEquals("attribute2-prop1-value", configType1Prop2Attributes.get("prop1"));
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configType2Attributes = attributes.get("foobar-type");
        org.junit.Assert.assertEquals(2, configType2Attributes.size());
        java.util.Map<java.lang.String, java.lang.String> configType2Prop1Attributes = configType2Attributes.get("attribute1");
        org.junit.Assert.assertEquals(1, configType2Prop1Attributes.size());
        org.junit.Assert.assertEquals("attribute1-prop10-value", configType2Prop1Attributes.get("prop10"));
        java.util.Map<java.lang.String, java.lang.String> configType2Prop2Attributes = configType2Attributes.get("attribute10");
        org.junit.Assert.assertEquals(1, configType2Prop2Attributes.size());
        org.junit.Assert.assertEquals("attribute10-prop11-value", configType2Prop2Attributes.get("prop11"));
    }

    private java.util.Collection<java.util.Map<java.lang.String, java.lang.String>> getNewSyntaxConfigProps() {
        java.util.Collection<java.util.Map<java.lang.String, java.lang.String>> configurations = new java.util.ArrayList<>();
        java.util.Map<java.lang.String, java.lang.String> configProperties1 = new java.util.HashMap<>();
        configProperties1.put("foo-type/properties/prop1", "prop1Value");
        configProperties1.put("foo-type/properties/prop2", "prop2Value");
        configProperties1.put("foo-type/properties_attributes/attribute1/prop1", "attribute1-prop1-value");
        configProperties1.put("foo-type/properties_attributes/attribute1/prop2", "attribute1-prop2-value");
        configProperties1.put("foo-type/properties_attributes/attribute1/prop3", "attribute1-prop3-value");
        configProperties1.put("foo-type/properties_attributes/attribute2/prop1", "attribute2-prop1-value");
        configurations.add(configProperties1);
        java.util.Map<java.lang.String, java.lang.String> configProperties2 = new java.util.HashMap<>();
        configProperties2.put("bar-type/properties/prop3", "prop3Value");
        configurations.add(configProperties2);
        java.util.Map<java.lang.String, java.lang.String> configProperties3 = new java.util.HashMap<>();
        configProperties3.put("foobar-type/properties_attributes/attribute1/prop10", "attribute1-prop10-value");
        configProperties3.put("foobar-type/properties_attributes/attribute10/prop11", "attribute10-prop11-value");
        configurations.add(configProperties3);
        return configurations;
    }

    private java.util.Collection<java.util.Map<java.lang.String, java.lang.String>> getOldSyntaxConfigProps() {
        java.util.Collection<java.util.Map<java.lang.String, java.lang.String>> configurations = new java.util.ArrayList<>();
        java.util.Map<java.lang.String, java.lang.String> configProperties1 = new java.util.HashMap<>();
        configProperties1.put("foo-type/prop1", "prop1Value");
        configProperties1.put("foo-type/prop2", "prop2Value");
        configurations.add(configProperties1);
        java.util.Map<java.lang.String, java.lang.String> configProperties2 = new java.util.HashMap<>();
        configProperties2.put("bar-type/prop3", "prop3Value");
        configurations.add(configProperties2);
        return configurations;
    }
}