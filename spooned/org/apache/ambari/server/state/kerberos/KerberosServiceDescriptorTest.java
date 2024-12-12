package org.apache.ambari.server.state.kerberos;
@org.junit.experimental.categories.Category({ category.KerberosTest.class })
public class KerberosServiceDescriptorTest {
    static final java.lang.String JSON_VALUE = ((((((((((((((((("{" + (("  \"name\": \"SERVICE_NAME\"," + "  \"preconfigure\": \"true\",") + "  \"identities\": [")) + org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptorTest.JSON_VALUE) + "],") + "  \"components\": [") + org.apache.ambari.server.state.kerberos.KerberosComponentDescriptorTest.JSON_VALUE) + "],") + "  \"auth_to_local_properties\": [") + "      service.name.rules1") + "    ],") + "  \"configurations\": [") + "    {") + "      \"service-site\": {") + "        \"service.property1\": \"value1\",") + "        \"service.property2\": \"value2\"") + "      }") + "    }") + "  ]") + "}";

    private static final java.lang.String JSON_VALUE_SERVICES = (((((((((((((((((((((((((((((((((((("{ " + (((("\"services\" : [" + "{") + "  \"name\": \"SERVICE_NAME\",") + "  \"preconfigure\": \"true\",") + "  \"identities\": [")) + org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptorTest.JSON_VALUE) + "],") + "  \"components\": [") + org.apache.ambari.server.state.kerberos.KerberosComponentDescriptorTest.JSON_VALUE) + "],") + "  \"auth_to_local_properties\": [") + "      service.name.rules1") + "    ],") + "  \"configurations\": [") + "    {") + "      \"service-site\": {") + "        \"service.property1\": \"value1\",") + "        \"service.property2\": \"value2\"") + "      }") + "    }") + "  ]") + "},") + "{") + "  \"name\": \"A_DIFFERENT_SERVICE_NAME\",") + "  \"identities\": [") + org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptorTest.JSON_VALUE) + "],") + "  \"components\": [") + org.apache.ambari.server.state.kerberos.KerberosComponentDescriptorTest.JSON_VALUE) + "],") + "  \"configurations\": [") + "    {") + "      \"service-site\": {") + "        \"service.property1\": \"value1\",") + "        \"service.property2\": \"value2\"") + "      }") + "    }") + "  ]") + "}") + "]") + "}";

    public static final java.util.Map<java.lang.String, java.lang.Object> MAP_VALUE;

    static {
        java.util.Map<java.lang.String, java.lang.Object> identitiesMap = new java.util.TreeMap<>();
        identitiesMap.put(((java.lang.String) (org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptorTest.MAP_VALUE.get("name"))), org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptorTest.MAP_VALUE);
        java.util.Map<java.lang.String, java.lang.Object> componentsMap = new java.util.TreeMap<>();
        componentsMap.put(((java.lang.String) (org.apache.ambari.server.state.kerberos.KerberosComponentDescriptorTest.MAP_VALUE.get("name"))), org.apache.ambari.server.state.kerberos.KerberosComponentDescriptorTest.MAP_VALUE);
        java.util.Map<java.lang.String, java.lang.Object> serviceSiteProperties = new java.util.TreeMap<>();
        serviceSiteProperties.put("service.property1", "red");
        serviceSiteProperties.put("service.property", "green");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> serviceSiteMap = new java.util.TreeMap<>();
        serviceSiteMap.put("service-site", serviceSiteProperties);
        java.util.TreeMap<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>>> configurationsMap = new java.util.TreeMap<>();
        configurationsMap.put("service-site", serviceSiteMap);
        java.util.Collection<java.lang.String> authToLocalRules = new java.util.ArrayList<>();
        authToLocalRules.add("service.name.rules2");
        MAP_VALUE = new java.util.TreeMap<>();
        MAP_VALUE.put("name", "A_DIFFERENT_SERVICE_NAME");
        MAP_VALUE.put(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor.KEY_IDENTITIES, identitiesMap.values());
        MAP_VALUE.put(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor.KEY_COMPONENTS, componentsMap.values());
        MAP_VALUE.put(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor.KEY_CONFIGURATIONS, configurationsMap.values());
        MAP_VALUE.put(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor.KEY_AUTH_TO_LOCAL_PROPERTIES, authToLocalRules);
    }

    private static final org.apache.ambari.server.state.kerberos.KerberosServiceDescriptorFactory KERBEROS_SERVICE_DESCRIPTOR_FACTORY = new org.apache.ambari.server.state.kerberos.KerberosServiceDescriptorFactory();

    private static void validateFromJSON(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor[] serviceDescriptors) {
        junit.framework.Assert.assertNotNull(serviceDescriptors);
        junit.framework.Assert.assertEquals(2, serviceDescriptors.length);
        org.apache.ambari.server.state.kerberos.KerberosServiceDescriptorTest.validateFromJSON(serviceDescriptors[0]);
    }

    static void validateFromJSON(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor) {
        junit.framework.Assert.assertNotNull(serviceDescriptor);
        junit.framework.Assert.assertTrue(serviceDescriptor.isContainer());
        junit.framework.Assert.assertEquals("SERVICE_NAME", serviceDescriptor.getName());
        java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor> componentDescriptors = serviceDescriptor.getComponents();
        junit.framework.Assert.assertNotNull(componentDescriptors);
        junit.framework.Assert.assertEquals(1, componentDescriptors.size());
        for (org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor componentDescriptor : componentDescriptors.values()) {
            org.apache.ambari.server.state.kerberos.KerberosComponentDescriptorTest.validateFromJSON(componentDescriptor);
        }
        java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> identityDescriptors = serviceDescriptor.getIdentities();
        junit.framework.Assert.assertNotNull(identityDescriptors);
        junit.framework.Assert.assertEquals(1, identityDescriptors.size());
        for (org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptor : identityDescriptors) {
            org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptorTest.validateFromJSON(identityDescriptor);
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor> configurations = serviceDescriptor.getConfigurations();
        junit.framework.Assert.assertNotNull(configurations);
        junit.framework.Assert.assertEquals(1, configurations.size());
        org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor configuration = configurations.get("service-site");
        junit.framework.Assert.assertNotNull(configuration);
        java.util.Map<java.lang.String, java.lang.String> properties = configuration.getProperties();
        junit.framework.Assert.assertEquals("service-site", configuration.getType());
        junit.framework.Assert.assertNotNull(properties);
        junit.framework.Assert.assertEquals(2, properties.size());
        junit.framework.Assert.assertEquals("value1", properties.get("service.property1"));
        junit.framework.Assert.assertEquals("value2", properties.get("service.property2"));
        java.util.Set<java.lang.String> authToLocalProperties = serviceDescriptor.getAuthToLocalProperties();
        junit.framework.Assert.assertNotNull(authToLocalProperties);
        junit.framework.Assert.assertEquals(1, authToLocalProperties.size());
        junit.framework.Assert.assertEquals("service.name.rules1", authToLocalProperties.iterator().next());
    }

    static void validateFromMap(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor) {
        junit.framework.Assert.assertNotNull(serviceDescriptor);
        junit.framework.Assert.assertTrue(serviceDescriptor.isContainer());
        junit.framework.Assert.assertEquals("A_DIFFERENT_SERVICE_NAME", serviceDescriptor.getName());
        java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor> componentDescriptors = serviceDescriptor.getComponents();
        junit.framework.Assert.assertNotNull(componentDescriptors);
        junit.framework.Assert.assertEquals(1, componentDescriptors.size());
        for (org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor componentDescriptor : componentDescriptors.values()) {
            org.apache.ambari.server.state.kerberos.KerberosComponentDescriptorTest.validateFromMap(componentDescriptor);
        }
        java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> identityDescriptors = serviceDescriptor.getIdentities();
        junit.framework.Assert.assertNotNull(identityDescriptors);
        junit.framework.Assert.assertEquals(1, identityDescriptors.size());
        for (org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptor : identityDescriptors) {
            org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptorTest.validateFromMap(identityDescriptor);
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor> configurations = serviceDescriptor.getConfigurations();
        junit.framework.Assert.assertNotNull(configurations);
        junit.framework.Assert.assertEquals(1, configurations.size());
        org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor configuration = configurations.get("service-site");
        junit.framework.Assert.assertNotNull(configuration);
        java.util.Map<java.lang.String, java.lang.String> properties = configuration.getProperties();
        junit.framework.Assert.assertEquals("service-site", configuration.getType());
        junit.framework.Assert.assertNotNull(properties);
        junit.framework.Assert.assertEquals(2, properties.size());
        junit.framework.Assert.assertEquals("red", properties.get("service.property1"));
        junit.framework.Assert.assertEquals("green", properties.get("service.property"));
        java.util.Set<java.lang.String> authToLocalProperties = serviceDescriptor.getAuthToLocalProperties();
        junit.framework.Assert.assertNotNull(authToLocalProperties);
        junit.framework.Assert.assertEquals(1, authToLocalProperties.size());
        junit.framework.Assert.assertEquals("service.name.rules2", authToLocalProperties.iterator().next());
    }

    private void validateUpdatedData(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor) {
        junit.framework.Assert.assertNotNull(serviceDescriptor);
        junit.framework.Assert.assertEquals("A_DIFFERENT_SERVICE_NAME", serviceDescriptor.getName());
        java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor> componentDescriptors = serviceDescriptor.getComponents();
        junit.framework.Assert.assertNotNull(componentDescriptors);
        junit.framework.Assert.assertEquals(2, componentDescriptors.size());
        org.apache.ambari.server.state.kerberos.KerberosComponentDescriptorTest.validateFromJSON(serviceDescriptor.getComponent("COMPONENT_NAME"));
        org.apache.ambari.server.state.kerberos.KerberosComponentDescriptorTest.validateFromMap(serviceDescriptor.getComponent("A_DIFFERENT_COMPONENT_NAME"));
        java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> identityDescriptors = serviceDescriptor.getIdentities();
        junit.framework.Assert.assertNotNull(identityDescriptors);
        junit.framework.Assert.assertEquals(1, identityDescriptors.size());
        for (org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptor : identityDescriptors) {
            org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptorTest.validateUpdatedData(identityDescriptor);
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor> configurations = serviceDescriptor.getConfigurations();
        junit.framework.Assert.assertNotNull(configurations);
        junit.framework.Assert.assertEquals(1, configurations.size());
        org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor configuration = configurations.get("service-site");
        junit.framework.Assert.assertNotNull(configuration);
        java.util.Map<java.lang.String, java.lang.String> properties = configuration.getProperties();
        junit.framework.Assert.assertEquals("service-site", configuration.getType());
        junit.framework.Assert.assertNotNull(properties);
        junit.framework.Assert.assertEquals(3, properties.size());
        junit.framework.Assert.assertEquals("red", properties.get("service.property1"));
        junit.framework.Assert.assertEquals("value2", properties.get("service.property2"));
        junit.framework.Assert.assertEquals("green", properties.get("service.property"));
        java.util.Set<java.lang.String> authToLocalProperties = serviceDescriptor.getAuthToLocalProperties();
        junit.framework.Assert.assertNotNull(authToLocalProperties);
        junit.framework.Assert.assertEquals(2, authToLocalProperties.size());
        java.util.Iterator<java.lang.String> iterator = new java.util.TreeSet<>(authToLocalProperties).iterator();
        junit.framework.Assert.assertEquals("service.name.rules1", iterator.next());
        junit.framework.Assert.assertEquals("service.name.rules2", iterator.next());
    }

    private org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor createFromJSON() throws org.apache.ambari.server.AmbariException {
        return org.apache.ambari.server.state.kerberos.KerberosServiceDescriptorTest.KERBEROS_SERVICE_DESCRIPTOR_FACTORY.createInstance("SERVICE_NAME", org.apache.ambari.server.state.kerberos.KerberosServiceDescriptorTest.JSON_VALUE);
    }

    private org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor[] createMultipleFromJSON() throws org.apache.ambari.server.AmbariException {
        return org.apache.ambari.server.state.kerberos.KerberosServiceDescriptorTest.KERBEROS_SERVICE_DESCRIPTOR_FACTORY.createInstances(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptorTest.JSON_VALUE_SERVICES);
    }

    private org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor createFromMap() throws org.apache.ambari.server.AmbariException {
        return new org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptorTest.MAP_VALUE);
    }

    private org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor[] createFromFile() throws java.io.IOException {
        java.net.URL url = getClass().getClassLoader().getResource("service_level_kerberos.json");
        java.io.File file = (url == null) ? null : new java.io.File(url.getFile());
        return org.apache.ambari.server.state.kerberos.KerberosServiceDescriptorTest.KERBEROS_SERVICE_DESCRIPTOR_FACTORY.createInstances(file);
    }

    @org.junit.Test
    public void testJSONDeserialize() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.kerberos.KerberosServiceDescriptorTest.validateFromJSON(createFromJSON());
    }

    @org.junit.Test
    public void testJSONDeserializeMultiple() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.kerberos.KerberosServiceDescriptorTest.validateFromJSON(createMultipleFromJSON());
    }

    @org.junit.Test
    public void testInvalid() {
        try {
            org.apache.ambari.server.state.kerberos.KerberosServiceDescriptorTest.KERBEROS_SERVICE_DESCRIPTOR_FACTORY.createInstances(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptorTest.JSON_VALUE_SERVICES + "erroneous text");
            junit.framework.Assert.fail("Should have thrown AmbariException.");
        } catch (org.apache.ambari.server.AmbariException e) {
        } catch (java.lang.Throwable t) {
            junit.framework.Assert.fail("Should have thrown AmbariException.");
        }
        try {
            org.apache.ambari.server.state.kerberos.KerberosServiceDescriptorTest.KERBEROS_SERVICE_DESCRIPTOR_FACTORY.createInstances(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptorTest.JSON_VALUE);
            junit.framework.Assert.fail("Should have thrown AmbariException.");
        } catch (org.apache.ambari.server.AmbariException e) {
        } catch (java.lang.Throwable t) {
            junit.framework.Assert.fail("Should have thrown AmbariException.");
        }
        java.net.URL url = getClass().getClassLoader().getResource("service_level_kerberos_invalid.json");
        java.io.File file = (url == null) ? null : new java.io.File(url.getFile());
        try {
            org.apache.ambari.server.state.kerberos.KerberosServiceDescriptorTest.KERBEROS_SERVICE_DESCRIPTOR_FACTORY.createInstances(file);
            junit.framework.Assert.fail("Should have thrown AmbariException.");
        } catch (org.apache.ambari.server.AmbariException e) {
        } catch (java.lang.Throwable t) {
            junit.framework.Assert.fail("Should have thrown AmbariException.");
        }
    }

    @org.junit.Test
    public void testFileDeserialize() throws java.io.IOException {
        org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor[] descriptors = createFromFile();
        junit.framework.Assert.assertNotNull(descriptors);
        junit.framework.Assert.assertEquals(2, descriptors.length);
    }

    @org.junit.Test
    public void testMapDeserialize() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.kerberos.KerberosServiceDescriptorTest.validateFromMap(createFromMap());
    }

    @org.junit.Test
    public void testEquals() throws org.apache.ambari.server.AmbariException {
        junit.framework.Assert.assertTrue(createFromJSON().equals(createFromJSON()));
        junit.framework.Assert.assertFalse(createFromJSON().equals(createFromMap()));
    }

    @org.junit.Test
    public void testToMap() throws org.apache.ambari.server.AmbariException {
        com.google.gson.Gson gson = new com.google.gson.Gson();
        org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor descriptor = createFromMap();
        junit.framework.Assert.assertNotNull(descriptor);
        junit.framework.Assert.assertEquals(gson.toJson(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptorTest.MAP_VALUE), gson.toJson(descriptor.toMap()));
    }

    @org.junit.Test
    public void testUpdate() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor = createFromJSON();
        org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor updatedServiceDescriptor = createFromMap();
        junit.framework.Assert.assertNotNull(serviceDescriptor);
        junit.framework.Assert.assertNotNull(updatedServiceDescriptor);
        serviceDescriptor.update(updatedServiceDescriptor);
        validateUpdatedData(serviceDescriptor);
    }

    @org.junit.Test
    public void testJSONWithOnlyServiceNameAndConfigurations() throws org.apache.ambari.server.AmbariException {
        java.lang.String JSON_VALUE_ONLY_NAME_AND_CONFIGS = "{" + ((((((((("  \"name\": \"SERVICE_NAME\"," + "  \"configurations\": [") + "    {") + "      \"service-site\": {") + "        \"service.property1\": \"value1\",") + "        \"service.property2\": \"value2\"") + "      }") + "    }") + "  ]") + "}");
        java.util.TreeMap<java.lang.String, java.lang.Object> CHANGE_NAME = new java.util.TreeMap<java.lang.String, java.lang.Object>() {
            {
                put("name", "A_DIFFERENT_SERVICE_NAME");
            }
        };
        org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor = org.apache.ambari.server.state.kerberos.KerberosServiceDescriptorTest.KERBEROS_SERVICE_DESCRIPTOR_FACTORY.createInstance("SERVICE_NAME", JSON_VALUE_ONLY_NAME_AND_CONFIGS);
        org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor updatedServiceDescriptor = new org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor(CHANGE_NAME);
        junit.framework.Assert.assertNotNull(serviceDescriptor);
        junit.framework.Assert.assertNotNull(updatedServiceDescriptor);
        serviceDescriptor.update(updatedServiceDescriptor);
        junit.framework.Assert.assertNotNull(serviceDescriptor);
        junit.framework.Assert.assertEquals("A_DIFFERENT_SERVICE_NAME", serviceDescriptor.getName());
    }
}