package org.apache.ambari.server.state.kerberos;
@org.junit.experimental.categories.Category({ category.KerberosTest.class })
public class KerberosComponentDescriptorTest {
    static final java.lang.String JSON_VALUE = ((((((((((((((" {" + ("  \"name\": \"COMPONENT_NAME\"," + "  \"identities\": [")) + org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptorTest.JSON_VALUE) + "],") + "  \"configurations\": [") + "    {") + "      \"service-site\": {") + "        \"service.component.property1\": \"value1\",") + "        \"service.component.property2\": \"value2\"") + "      }") + "    }") + "  ],") + "  \"auth_to_local_properties\": [") + "      component.name.rules1") + "    ]") + "}";

    static final java.util.Map<java.lang.String, java.lang.Object> MAP_VALUE;

    static {
        java.util.Map<java.lang.String, java.lang.Object> identitiesMap = new java.util.TreeMap<>();
        identitiesMap.put(((java.lang.String) (org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptorTest.MAP_VALUE.get(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.KEY_NAME))), org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptorTest.MAP_VALUE);
        identitiesMap.put(((java.lang.String) (org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptorTest.MAP_VALUE_ALT.get(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.KEY_NAME))), org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptorTest.MAP_VALUE_ALT);
        identitiesMap.put(((java.lang.String) (org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptorTest.MAP_VALUE_REFERENCE.get(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.KEY_NAME))), org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptorTest.MAP_VALUE_REFERENCE);
        java.util.Map<java.lang.String, java.lang.Object> serviceSiteProperties = new java.util.TreeMap<>();
        serviceSiteProperties.put("service.component.property1", "red");
        serviceSiteProperties.put("service.component.property", "green");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> serviceSiteMap = new java.util.TreeMap<>();
        serviceSiteMap.put("service-site", serviceSiteProperties);
        java.util.TreeMap<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>>> configurationsMap = new java.util.TreeMap<>();
        configurationsMap.put("service-site", serviceSiteMap);
        java.util.Collection<java.lang.String> authToLocalRules = new java.util.ArrayList<>();
        authToLocalRules.add("component.name.rules2");
        MAP_VALUE = new java.util.TreeMap<>();
        MAP_VALUE.put(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.KEY_NAME, "A_DIFFERENT_COMPONENT_NAME");
        MAP_VALUE.put(org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor.KEY_IDENTITIES, new java.util.ArrayList<>(identitiesMap.values()));
        MAP_VALUE.put(org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor.KEY_CONFIGURATIONS, configurationsMap.values());
        MAP_VALUE.put(org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor.KEY_AUTH_TO_LOCAL_PROPERTIES, authToLocalRules);
    }

    static void validateFromJSON(org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor componentDescriptor) {
        junit.framework.Assert.assertNotNull(componentDescriptor);
        junit.framework.Assert.assertTrue(componentDescriptor.isContainer());
        junit.framework.Assert.assertEquals("COMPONENT_NAME", componentDescriptor.getName());
        java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> identities = componentDescriptor.getIdentities();
        junit.framework.Assert.assertNotNull(identities);
        junit.framework.Assert.assertEquals(1, identities.size());
        java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor> configurations = componentDescriptor.getConfigurations();
        junit.framework.Assert.assertNotNull(configurations);
        junit.framework.Assert.assertEquals(1, configurations.size());
        org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor configuration = configurations.get("service-site");
        junit.framework.Assert.assertNotNull(configuration);
        java.util.Map<java.lang.String, java.lang.String> properties = configuration.getProperties();
        junit.framework.Assert.assertEquals("service-site", configuration.getType());
        junit.framework.Assert.assertNotNull(properties);
        junit.framework.Assert.assertEquals(2, properties.size());
        junit.framework.Assert.assertEquals("value1", properties.get("service.component.property1"));
        junit.framework.Assert.assertEquals("value2", properties.get("service.component.property2"));
        java.util.Set<java.lang.String> authToLocalProperties = componentDescriptor.getAuthToLocalProperties();
        junit.framework.Assert.assertNotNull(authToLocalProperties);
        junit.framework.Assert.assertEquals(1, authToLocalProperties.size());
        junit.framework.Assert.assertEquals("component.name.rules1", authToLocalProperties.iterator().next());
    }

    static void validateFromMap(org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor componentDescriptor) {
        junit.framework.Assert.assertNotNull(componentDescriptor);
        junit.framework.Assert.assertTrue(componentDescriptor.isContainer());
        junit.framework.Assert.assertEquals("A_DIFFERENT_COMPONENT_NAME", componentDescriptor.getName());
        java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> identities = componentDescriptor.getIdentities();
        junit.framework.Assert.assertNotNull(identities);
        junit.framework.Assert.assertEquals(3, identities.size());
        java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor> configurations = componentDescriptor.getConfigurations();
        junit.framework.Assert.assertNotNull(configurations);
        junit.framework.Assert.assertEquals(1, configurations.size());
        org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor configuration = configurations.get("service-site");
        junit.framework.Assert.assertNotNull(configuration);
        java.util.Map<java.lang.String, java.lang.String> properties = configuration.getProperties();
        junit.framework.Assert.assertEquals("service-site", configuration.getType());
        junit.framework.Assert.assertNotNull(properties);
        junit.framework.Assert.assertEquals(2, properties.size());
        junit.framework.Assert.assertEquals("red", properties.get("service.component.property1"));
        junit.framework.Assert.assertEquals("green", properties.get("service.component.property"));
        java.util.Set<java.lang.String> authToLocalProperties = componentDescriptor.getAuthToLocalProperties();
        junit.framework.Assert.assertNotNull(authToLocalProperties);
        junit.framework.Assert.assertEquals(1, authToLocalProperties.size());
        junit.framework.Assert.assertEquals("component.name.rules2", authToLocalProperties.iterator().next());
    }

    private static void validateUpdatedData(org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor componentDescriptor) {
        junit.framework.Assert.assertNotNull(componentDescriptor);
        junit.framework.Assert.assertEquals("A_DIFFERENT_COMPONENT_NAME", componentDescriptor.getName());
        java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> identities = componentDescriptor.getIdentities();
        junit.framework.Assert.assertNotNull(identities);
        junit.framework.Assert.assertEquals(3, identities.size());
        java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor> configurations = componentDescriptor.getConfigurations();
        junit.framework.Assert.assertNotNull(configurations);
        junit.framework.Assert.assertEquals(1, configurations.size());
        org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor configuration = configurations.get("service-site");
        junit.framework.Assert.assertNotNull(configuration);
        java.util.Map<java.lang.String, java.lang.String> properties = configuration.getProperties();
        junit.framework.Assert.assertEquals("service-site", configuration.getType());
        junit.framework.Assert.assertNotNull(properties);
        junit.framework.Assert.assertEquals(3, properties.size());
        junit.framework.Assert.assertEquals("red", properties.get("service.component.property1"));
        junit.framework.Assert.assertEquals("value2", properties.get("service.component.property2"));
        junit.framework.Assert.assertEquals("green", properties.get("service.component.property"));
        java.util.Set<java.lang.String> authToLocalProperties = componentDescriptor.getAuthToLocalProperties();
        junit.framework.Assert.assertNotNull(authToLocalProperties);
        junit.framework.Assert.assertEquals(2, authToLocalProperties.size());
        java.util.Iterator<java.lang.String> iterator = new java.util.TreeSet<>(authToLocalProperties).iterator();
        junit.framework.Assert.assertEquals("component.name.rules1", iterator.next());
        junit.framework.Assert.assertEquals("component.name.rules2", iterator.next());
    }

    private static org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor createFromJSON() {
        java.util.Map<java.lang.Object, java.lang.Object> map = new com.google.gson.Gson().fromJson(org.apache.ambari.server.state.kerberos.KerberosComponentDescriptorTest.JSON_VALUE, new com.google.gson.reflect.TypeToken<java.util.Map<java.lang.Object, java.lang.Object>>() {}.getType());
        return new org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor(map);
    }

    private static org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor createFromMap() throws org.apache.ambari.server.AmbariException {
        return new org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor(org.apache.ambari.server.state.kerberos.KerberosComponentDescriptorTest.MAP_VALUE);
    }

    @org.junit.Test
    public void testJSONDeserialize() {
        org.apache.ambari.server.state.kerberos.KerberosComponentDescriptorTest.validateFromJSON(org.apache.ambari.server.state.kerberos.KerberosComponentDescriptorTest.createFromJSON());
    }

    @org.junit.Test
    public void testMapDeserialize() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.kerberos.KerberosComponentDescriptorTest.validateFromMap(org.apache.ambari.server.state.kerberos.KerberosComponentDescriptorTest.createFromMap());
    }

    @org.junit.Test
    public void testEquals() throws org.apache.ambari.server.AmbariException {
        junit.framework.Assert.assertTrue(org.apache.ambari.server.state.kerberos.KerberosComponentDescriptorTest.createFromJSON().equals(org.apache.ambari.server.state.kerberos.KerberosComponentDescriptorTest.createFromJSON()));
        junit.framework.Assert.assertFalse(org.apache.ambari.server.state.kerberos.KerberosComponentDescriptorTest.createFromJSON().equals(org.apache.ambari.server.state.kerberos.KerberosComponentDescriptorTest.createFromMap()));
    }

    @org.junit.Test
    public void testToMap() throws org.apache.ambari.server.AmbariException {
        com.google.gson.Gson gson = new com.google.gson.Gson();
        org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor descriptor = org.apache.ambari.server.state.kerberos.KerberosComponentDescriptorTest.createFromMap();
        junit.framework.Assert.assertNotNull(descriptor);
        junit.framework.Assert.assertEquals(gson.toJson(org.apache.ambari.server.state.kerberos.KerberosComponentDescriptorTest.MAP_VALUE), gson.toJson(descriptor.toMap()));
    }

    @org.junit.Test
    public void testUpdate() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor componentDescriptor = org.apache.ambari.server.state.kerberos.KerberosComponentDescriptorTest.createFromJSON();
        org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor updatedComponentDescriptor = org.apache.ambari.server.state.kerberos.KerberosComponentDescriptorTest.createFromMap();
        junit.framework.Assert.assertNotNull(componentDescriptor);
        junit.framework.Assert.assertNotNull(updatedComponentDescriptor);
        componentDescriptor.update(updatedComponentDescriptor);
        org.apache.ambari.server.state.kerberos.KerberosComponentDescriptorTest.validateUpdatedData(componentDescriptor);
    }
}