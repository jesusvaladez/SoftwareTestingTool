package org.apache.ambari.server.state.kerberos;
@org.junit.experimental.categories.Category({ category.KerberosTest.class })
public class KerberosConfigurationDescriptorTest {
    private static final java.lang.String JSON_SINGLE_VALUE = "{ \"configuration-type\": {" + (("     \"property1\": \"${property-value1}\"," + "     \"property2\": \"${property.value2}\"") + "}}");

    private static final java.lang.String JSON_MULTIPLE_VALUE = "[" + ((((((((("{ \"configuration-type\": {" + "     \"property1\": \"value1\",") + "     \"property2\": \"value2\"") + "}},") + "{ \"configuration-type2\": {") + "     \"property1\": \"value1\",") + "     \"property3\": \"value3\",") + "     \"property2\": \"value2\"") + "}}") + "]");

    private static final java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> MAP_SINGLE_VALUE;

    private static final java.util.Collection<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>>> MAP_MULTIPLE_VALUES;

    static {
        java.util.TreeMap<java.lang.String, java.lang.Object> configuration_data = new java.util.TreeMap<>();
        configuration_data.put("property1", "black");
        configuration_data.put("property2", "white");
        MAP_SINGLE_VALUE = new java.util.TreeMap<>();
        MAP_SINGLE_VALUE.put("configuration-type", configuration_data);
        java.util.TreeMap<java.lang.String, java.lang.Object> configurationType2Properties = new java.util.TreeMap<>();
        configurationType2Properties.put("property1", "red");
        configurationType2Properties.put("property2", "yellow");
        configurationType2Properties.put("property3", "green");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> configurationType2 = new java.util.TreeMap<>();
        configurationType2.put("configuration-type2", configurationType2Properties);
        java.util.TreeMap<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>>> multipleValuesMap = new java.util.TreeMap<>();
        multipleValuesMap.put("configuration-type", MAP_SINGLE_VALUE);
        multipleValuesMap.put("configuration-type2", configurationType2);
        MAP_MULTIPLE_VALUES = multipleValuesMap.values();
    }

    @org.junit.Test
    public void testJSONDeserialize() {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> jsonData = new com.google.gson.Gson().fromJson(org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptorTest.JSON_SINGLE_VALUE, new com.google.gson.reflect.TypeToken<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>>>() {}.getType());
        org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor configuration = new org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor(jsonData);
        junit.framework.Assert.assertNotNull(configuration);
        junit.framework.Assert.assertFalse(configuration.isContainer());
        java.util.Map<java.lang.String, java.lang.String> properties = configuration.getProperties();
        junit.framework.Assert.assertEquals("configuration-type", configuration.getType());
        junit.framework.Assert.assertNotNull(properties);
        junit.framework.Assert.assertEquals(2, properties.size());
        junit.framework.Assert.assertEquals("${property-value1}", properties.get("property1"));
        junit.framework.Assert.assertEquals("${property.value2}", properties.get("property2"));
    }

    @org.junit.Test
    public void testJSONDeserializeMultiple() {
        java.util.List<java.util.Map<java.lang.String, java.lang.Object>> jsonData = new com.google.gson.Gson().fromJson(org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptorTest.JSON_MULTIPLE_VALUE, new com.google.gson.reflect.TypeToken<java.util.List<java.util.Map<java.lang.String, java.lang.Object>>>() {}.getType());
        java.util.List<org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor> configurations = new java.util.ArrayList<>();
        for (java.util.Map<java.lang.String, java.lang.Object> item : jsonData) {
            configurations.add(new org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor(item));
        }
        junit.framework.Assert.assertNotNull(configurations);
        junit.framework.Assert.assertEquals(2, configurations.size());
        for (org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor configuration : configurations) {
            junit.framework.Assert.assertFalse(configuration.isContainer());
            java.lang.String type = configuration.getType();
            junit.framework.Assert.assertEquals(2, configurations.size());
            java.util.Map<java.lang.String, java.lang.String> properties = configuration.getProperties();
            if ("configuration-type".equals(type)) {
                junit.framework.Assert.assertNotNull(properties);
                junit.framework.Assert.assertEquals(2, properties.size());
                junit.framework.Assert.assertEquals("value1", properties.get("property1"));
                junit.framework.Assert.assertEquals("value2", properties.get("property2"));
            } else if ("configuration-type2".equals(type)) {
                junit.framework.Assert.assertNotNull(properties);
                junit.framework.Assert.assertEquals(3, properties.size());
                junit.framework.Assert.assertEquals("value1", properties.get("property1"));
                junit.framework.Assert.assertEquals("value2", properties.get("property2"));
                junit.framework.Assert.assertEquals("value3", properties.get("property3"));
                junit.framework.Assert.assertEquals("value1", configuration.getProperty("property1"));
                junit.framework.Assert.assertEquals("value2", configuration.getProperty("property2"));
                junit.framework.Assert.assertEquals("value3", configuration.getProperty("property3"));
            } else {
                junit.framework.Assert.fail("Missing expected configuration type");
            }
        }
    }

    @org.junit.Test
    public void testMapDeserialize() {
        org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor configuration = new org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor(org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptorTest.MAP_SINGLE_VALUE);
        java.util.Map<java.lang.String, java.lang.String> properties = configuration.getProperties();
        junit.framework.Assert.assertNotNull(configuration);
        junit.framework.Assert.assertFalse(configuration.isContainer());
        junit.framework.Assert.assertEquals("configuration-type", configuration.getType());
        junit.framework.Assert.assertNotNull(properties);
        junit.framework.Assert.assertEquals(2, properties.size());
        junit.framework.Assert.assertEquals("black", properties.get("property1"));
        junit.framework.Assert.assertEquals("white", properties.get("property2"));
    }

    @org.junit.Test
    public void testMapDeserializeMultiple() {
        java.util.List<org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor> configurations = new java.util.ArrayList<>();
        for (java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> item : org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptorTest.MAP_MULTIPLE_VALUES) {
            configurations.add(new org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor(item));
        }
        junit.framework.Assert.assertNotNull(configurations);
        junit.framework.Assert.assertEquals(2, configurations.size());
        for (org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor configuration : configurations) {
            junit.framework.Assert.assertFalse(configuration.isContainer());
            java.lang.String type = configuration.getType();
            java.util.Map<java.lang.String, java.lang.String> properties = configuration.getProperties();
            if ("configuration-type".equals(type)) {
                junit.framework.Assert.assertNotNull(properties);
                junit.framework.Assert.assertEquals(2, properties.size());
                junit.framework.Assert.assertEquals("black", properties.get("property1"));
                junit.framework.Assert.assertEquals("white", properties.get("property2"));
                junit.framework.Assert.assertEquals("black", configuration.getProperty("property1"));
                junit.framework.Assert.assertEquals("white", configuration.getProperty("property2"));
            } else if ("configuration-type2".equals(type)) {
                junit.framework.Assert.assertNotNull(properties);
                junit.framework.Assert.assertEquals(3, properties.size());
                junit.framework.Assert.assertEquals("red", properties.get("property1"));
                junit.framework.Assert.assertEquals("yellow", properties.get("property2"));
                junit.framework.Assert.assertEquals("green", properties.get("property3"));
                junit.framework.Assert.assertEquals("red", configuration.getProperty("property1"));
                junit.framework.Assert.assertEquals("yellow", configuration.getProperty("property2"));
                junit.framework.Assert.assertEquals("green", configuration.getProperty("property3"));
            } else {
                junit.framework.Assert.fail("Missing expected configuration type");
            }
        }
    }

    @org.junit.Test
    public void testToMap() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor descriptor = new org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor(org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptorTest.MAP_SINGLE_VALUE);
        junit.framework.Assert.assertNotNull(descriptor);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptorTest.MAP_SINGLE_VALUE, descriptor.toMap());
    }

    @org.junit.Test
    public void testUpdate() {
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> jsonData = new com.google.gson.Gson().fromJson(org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptorTest.JSON_SINGLE_VALUE, new com.google.gson.reflect.TypeToken<java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>>>() {}.getType());
        org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor configuration = new org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor(jsonData);
        org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor updatedConfiguration = new org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor(org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptorTest.MAP_SINGLE_VALUE);
        java.util.Map<java.lang.String, java.lang.String> properties;
        properties = configuration.getProperties();
        junit.framework.Assert.assertEquals("configuration-type", configuration.getType());
        junit.framework.Assert.assertNotNull(properties);
        junit.framework.Assert.assertEquals(2, properties.size());
        junit.framework.Assert.assertEquals("${property-value1}", properties.get("property1"));
        junit.framework.Assert.assertEquals("${property.value2}", properties.get("property2"));
        configuration.update(updatedConfiguration);
        properties = configuration.getProperties();
        junit.framework.Assert.assertEquals("configuration-type", configuration.getType());
        junit.framework.Assert.assertNotNull(properties);
        junit.framework.Assert.assertEquals(2, properties.size());
        junit.framework.Assert.assertEquals("black", properties.get("property1"));
        junit.framework.Assert.assertEquals("white", properties.get("property2"));
        updatedConfiguration.setType("updated-type");
        configuration.update(updatedConfiguration);
        junit.framework.Assert.assertEquals("updated-type", configuration.getType());
        junit.framework.Assert.assertNotNull(properties);
        junit.framework.Assert.assertEquals(2, properties.size());
        junit.framework.Assert.assertEquals("black", properties.get("property1"));
        junit.framework.Assert.assertEquals("white", properties.get("property2"));
    }
}