package org.apache.ambari.server.state.kerberos;
import org.apache.commons.collections.map.HashedMap;
@org.junit.experimental.categories.Category({ category.KerberosTest.class })
public class KerberosDescriptorTest {
    private static final org.apache.ambari.server.state.kerberos.KerberosDescriptorFactory KERBEROS_DESCRIPTOR_FACTORY = new org.apache.ambari.server.state.kerberos.KerberosDescriptorFactory();

    private static final org.apache.ambari.server.state.kerberos.KerberosServiceDescriptorFactory KERBEROS_SERVICE_DESCRIPTOR_FACTORY = new org.apache.ambari.server.state.kerberos.KerberosServiceDescriptorFactory();

    private static final java.lang.String JSON_VALUE = ((("{" + ((((((("  \"properties\": {" + "      \"realm\": \"${cluster-env/kerberos_domain}\",") + "      \"keytab_dir\": \"/etc/security/keytabs\"") + "    },") + "  \"auth_to_local_properties\": [") + "      generic.name.rules") + "    ],") + "  \"services\": [")) + org.apache.ambari.server.state.kerberos.KerberosServiceDescriptorTest.JSON_VALUE) + "    ]") + "}";

    private static final java.lang.String JSON_VALUE_IDENTITY_REFERENCES = "{" + ((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((("  \"identities\": [" + "    {") + "      \"keytab\": {") + "        \"file\": \"${keytab_dir}/spnego.service.keytab\"") + "      },") + "      \"name\": \"spnego\",") + "      \"principal\": {") + "        \"type\": \"service\",") + "        \"value\": \"HTTP/_HOST@${realm}\"") + "      }") + "    }") + "  ],") + "  \"services\": [") + "    {") + "      \"identities\": [") + "        {") + "          \"name\": \"service1_spnego\",") + "          \"reference\": \"/spnego\"") + "        },") + "        {") + "          \"name\": \"service1_identity\"") + "        }") + "      ],") + "      \"name\": \"SERVICE1\"") + "    },") + "    {") + "      \"identities\": [") + "        {") + "          \"name\": \"/spnego\"") + "        },") + "        {") + "          \"name\": \"service2_identity\"") + "        }") + "      ],") + "      \"components\": [") + "        {") + "          \"identities\": [") + "            {") + "              \"name\": \"component1_identity\"") + "            },") + "            {") + "              \"name\": \"service2_component1_service1_identity\",") + "              \"reference\": \"/SERVICE1/service1_identity\"") + "            },") + "            {") + "              \"name\": \"service2_component1_component1_identity\",") + "              \"reference\": \"./component1_identity\"") + "            },") + "            {") + "              \"name\": \"service2_component1_service2_identity\",") + "              \"reference\": \"../service2_identity\"") + "            }") + "          ],") + "          \"name\": \"COMPONENT21\"") + "        },") + "        {") + "          \"identities\": [") + "            {") + "              \"name\": \"component2_identity\"") + "            }") + "          ],") + "          \"name\": \"COMPONENT22\"") + "        }") + "      ],") + "      \"name\": \"SERVICE2\"") + "    }") + "  ]") + "}");

    private static final java.util.Map<java.lang.String, java.lang.Object> MAP_VALUE;

    static {
        java.util.Map<java.lang.String, java.lang.Object> keytabOwnerMap = new java.util.TreeMap<>();
        keytabOwnerMap.put(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_ACL_NAME, "root");
        keytabOwnerMap.put(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_ACL_ACCESS, "rw");
        java.util.Map<java.lang.String, java.lang.Object> keytabGroupMap = new java.util.TreeMap<>();
        keytabGroupMap.put(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_ACL_NAME, "hadoop");
        keytabGroupMap.put(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_ACL_ACCESS, "r");
        java.util.Map<java.lang.String, java.lang.Object> keytabMap = new java.util.TreeMap<>();
        keytabMap.put(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_FILE, "/etc/security/keytabs/subject.service.keytab");
        keytabMap.put(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_OWNER, keytabOwnerMap);
        keytabMap.put(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_GROUP, keytabGroupMap);
        keytabMap.put(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_CONFIGURATION, "service-site/service2.component.keytab.file");
        java.util.Map<java.lang.String, java.lang.Object> sharedIdentityMap = new java.util.TreeMap<>();
        sharedIdentityMap.put(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.KEY_NAME, "shared");
        sharedIdentityMap.put(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.KEY_PRINCIPAL, org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptorTest.MAP_VALUE);
        sharedIdentityMap.put(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.KEY_KEYTAB, keytabMap);
        java.util.Map<java.lang.String, java.lang.Object> servicesMap = new java.util.TreeMap<>();
        servicesMap.put(((java.lang.String) (org.apache.ambari.server.state.kerberos.KerberosServiceDescriptorTest.MAP_VALUE.get(org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor.KEY_NAME))), org.apache.ambari.server.state.kerberos.KerberosServiceDescriptorTest.MAP_VALUE);
        java.util.Map<java.lang.String, java.lang.Object> identitiesMap = new java.util.TreeMap<>();
        identitiesMap.put("shared", sharedIdentityMap);
        java.util.Map<java.lang.String, java.lang.Object> clusterConfigProperties = new java.util.TreeMap<>();
        clusterConfigProperties.put("property1", "red");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>> clusterConfigMap = new java.util.TreeMap<>();
        clusterConfigMap.put("cluster-conf", clusterConfigProperties);
        java.util.TreeMap<java.lang.String, java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.Object>>> configurationsMap = new java.util.TreeMap<>();
        configurationsMap.put("cluster-conf", clusterConfigMap);
        java.util.Collection<java.lang.String> authToLocalRules = new java.util.ArrayList<>();
        authToLocalRules.add("global.name.rules");
        java.util.TreeMap<java.lang.String, java.lang.Object> properties = new java.util.TreeMap<>();
        properties.put("realm", "EXAMPLE.COM");
        properties.put("some.property", "Hello World");
        MAP_VALUE = new java.util.TreeMap<>();
        MAP_VALUE.put(org.apache.ambari.server.state.kerberos.KerberosDescriptor.KEY_PROPERTIES, properties);
        MAP_VALUE.put(org.apache.ambari.server.state.kerberos.KerberosDescriptor.KEY_AUTH_TO_LOCAL_PROPERTIES, authToLocalRules);
        MAP_VALUE.put(org.apache.ambari.server.state.kerberos.KerberosDescriptor.KEY_SERVICES, servicesMap.values());
        MAP_VALUE.put(org.apache.ambari.server.state.kerberos.KerberosDescriptor.KEY_CONFIGURATIONS, configurationsMap.values());
        MAP_VALUE.put(org.apache.ambari.server.state.kerberos.KerberosDescriptor.KEY_IDENTITIES, identitiesMap.values());
    }

    private static void validateFromJSON(org.apache.ambari.server.state.kerberos.KerberosDescriptor descriptor) {
        junit.framework.Assert.assertNotNull(descriptor);
        junit.framework.Assert.assertTrue(descriptor.isContainer());
        java.util.Map<java.lang.String, java.lang.String> properties = descriptor.getProperties();
        junit.framework.Assert.assertNotNull(properties);
        junit.framework.Assert.assertEquals(2, properties.size());
        junit.framework.Assert.assertEquals("${cluster-env/kerberos_domain}", properties.get("realm"));
        junit.framework.Assert.assertEquals("/etc/security/keytabs", properties.get("keytab_dir"));
        java.util.Set<java.lang.String> authToLocalProperties = descriptor.getAuthToLocalProperties();
        junit.framework.Assert.assertNotNull(authToLocalProperties);
        junit.framework.Assert.assertEquals(1, authToLocalProperties.size());
        junit.framework.Assert.assertTrue(authToLocalProperties.contains("generic.name.rules"));
        authToLocalProperties = descriptor.getAllAuthToLocalProperties();
        junit.framework.Assert.assertNotNull(authToLocalProperties);
        junit.framework.Assert.assertEquals(3, authToLocalProperties.size());
        junit.framework.Assert.assertTrue(authToLocalProperties.contains("component.name.rules1"));
        junit.framework.Assert.assertTrue(authToLocalProperties.contains("generic.name.rules"));
        junit.framework.Assert.assertTrue(authToLocalProperties.contains("service.name.rules1"));
        java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor> serviceDescriptors = descriptor.getServices();
        junit.framework.Assert.assertNotNull(serviceDescriptors);
        junit.framework.Assert.assertEquals(1, serviceDescriptors.size());
        for (org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor : serviceDescriptors.values()) {
            org.apache.ambari.server.state.kerberos.KerberosServiceDescriptorTest.validateFromJSON(serviceDescriptor);
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor> configurations = descriptor.getConfigurations();
        junit.framework.Assert.assertNull(configurations);
    }

    private static void validateFromMap(org.apache.ambari.server.state.kerberos.KerberosDescriptor descriptor) throws org.apache.ambari.server.AmbariException {
        junit.framework.Assert.assertNotNull(descriptor);
        junit.framework.Assert.assertTrue(descriptor.isContainer());
        java.util.Map<java.lang.String, java.lang.String> properties = descriptor.getProperties();
        junit.framework.Assert.assertNotNull(properties);
        junit.framework.Assert.assertEquals(2, properties.size());
        junit.framework.Assert.assertEquals("EXAMPLE.COM", properties.get("realm"));
        junit.framework.Assert.assertEquals("Hello World", properties.get("some.property"));
        java.util.Set<java.lang.String> authToLocalProperties = descriptor.getAuthToLocalProperties();
        junit.framework.Assert.assertNotNull(authToLocalProperties);
        junit.framework.Assert.assertEquals(1, authToLocalProperties.size());
        junit.framework.Assert.assertEquals("global.name.rules", authToLocalProperties.iterator().next());
        java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor> services = descriptor.getServices();
        junit.framework.Assert.assertNotNull(services);
        junit.framework.Assert.assertEquals(1, services.size());
        for (org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor service : services.values()) {
            org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor component = service.getComponent("A_DIFFERENT_COMPONENT_NAME");
            junit.framework.Assert.assertNotNull(component);
            java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> resolvedIdentities = component.getIdentities(true, null);
            org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor resolvedIdentity = null;
            junit.framework.Assert.assertNotNull(resolvedIdentities);
            junit.framework.Assert.assertEquals(3, resolvedIdentities.size());
            for (org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor item : resolvedIdentities) {
                if ("/shared".equals(item.getReference())) {
                    resolvedIdentity = item;
                    break;
                }
            }
            junit.framework.Assert.assertNotNull(resolvedIdentity);
            java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> identities = component.getIdentities(false, null);
            junit.framework.Assert.assertNotNull(identities);
            junit.framework.Assert.assertEquals(3, identities.size());
            org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityReference = component.getIdentity("shared_identity");
            junit.framework.Assert.assertNotNull(identityReference);
            org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor referencedIdentity = descriptor.getIdentity("shared");
            junit.framework.Assert.assertNotNull(referencedIdentity);
            junit.framework.Assert.assertEquals(identityReference.getKeytabDescriptor(), resolvedIdentity.getKeytabDescriptor());
            junit.framework.Assert.assertEquals(referencedIdentity.getPrincipalDescriptor(), resolvedIdentity.getPrincipalDescriptor());
            java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor> configurations = service.getConfigurations(true);
            junit.framework.Assert.assertNotNull(configurations);
            junit.framework.Assert.assertEquals(2, configurations.size());
            junit.framework.Assert.assertNotNull(configurations.get("service-site"));
            junit.framework.Assert.assertNotNull(configurations.get("cluster-conf"));
        }
        java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor> configurations = descriptor.getConfigurations();
        junit.framework.Assert.assertNotNull(configurations);
        junit.framework.Assert.assertEquals(1, configurations.size());
        org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor configuration = configurations.get("cluster-conf");
        junit.framework.Assert.assertNotNull(configuration);
        java.util.Map<java.lang.String, java.lang.String> configProperties = configuration.getProperties();
        junit.framework.Assert.assertEquals("cluster-conf", configuration.getType());
        junit.framework.Assert.assertNotNull(configProperties);
        junit.framework.Assert.assertEquals(1, configProperties.size());
        junit.framework.Assert.assertEquals("red", configProperties.get("property1"));
    }

    private void validateUpdatedData(org.apache.ambari.server.state.kerberos.KerberosDescriptor descriptor) {
        junit.framework.Assert.assertNotNull(descriptor);
        java.util.Map<java.lang.String, java.lang.String> properties = descriptor.getProperties();
        junit.framework.Assert.assertNotNull(properties);
        junit.framework.Assert.assertEquals(3, properties.size());
        junit.framework.Assert.assertEquals("EXAMPLE.COM", properties.get("realm"));
        junit.framework.Assert.assertEquals("/etc/security/keytabs", properties.get("keytab_dir"));
        junit.framework.Assert.assertEquals("Hello World", properties.get("some.property"));
        java.util.Set<java.lang.String> authToLocalProperties = descriptor.getAuthToLocalProperties();
        junit.framework.Assert.assertNotNull(authToLocalProperties);
        junit.framework.Assert.assertEquals(2, authToLocalProperties.size());
        java.util.Iterator<java.lang.String> iterator = new java.util.TreeSet<>(authToLocalProperties).iterator();
        junit.framework.Assert.assertEquals("generic.name.rules", iterator.next());
        junit.framework.Assert.assertEquals("global.name.rules", iterator.next());
        java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor> serviceDescriptors = descriptor.getServices();
        junit.framework.Assert.assertNotNull(serviceDescriptors);
        junit.framework.Assert.assertEquals(2, serviceDescriptors.size());
        org.apache.ambari.server.state.kerberos.KerberosServiceDescriptorTest.validateFromJSON(descriptor.getService("SERVICE_NAME"));
        org.apache.ambari.server.state.kerberos.KerberosServiceDescriptorTest.validateFromMap(descriptor.getService("A_DIFFERENT_SERVICE_NAME"));
        junit.framework.Assert.assertNull(descriptor.getService("invalid service"));
        java.util.Map<java.lang.String, org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor> configurations = descriptor.getConfigurations();
        junit.framework.Assert.assertNotNull(configurations);
        junit.framework.Assert.assertEquals(1, configurations.size());
        org.apache.ambari.server.state.kerberos.KerberosConfigurationDescriptor configuration = configurations.get("cluster-conf");
        junit.framework.Assert.assertNotNull(configuration);
        java.util.Map<java.lang.String, java.lang.String> configProperties = configuration.getProperties();
        junit.framework.Assert.assertEquals("cluster-conf", configuration.getType());
        junit.framework.Assert.assertNotNull(configProperties);
        junit.framework.Assert.assertEquals(1, configProperties.size());
        junit.framework.Assert.assertEquals("red", configProperties.get("property1"));
    }

    private org.apache.ambari.server.state.kerberos.KerberosDescriptor createFromJSON() throws org.apache.ambari.server.AmbariException {
        return org.apache.ambari.server.state.kerberos.KerberosDescriptorTest.KERBEROS_DESCRIPTOR_FACTORY.createInstance(org.apache.ambari.server.state.kerberos.KerberosDescriptorTest.JSON_VALUE);
    }

    private org.apache.ambari.server.state.kerberos.KerberosDescriptor createFromMap() throws org.apache.ambari.server.AmbariException {
        return new org.apache.ambari.server.state.kerberos.KerberosDescriptor(org.apache.ambari.server.state.kerberos.KerberosDescriptorTest.MAP_VALUE);
    }

    @org.junit.Test
    public void testFromMapViaGSON() throws org.apache.ambari.server.AmbariException {
        java.lang.Object data = new com.google.gson.Gson().fromJson(org.apache.ambari.server.state.kerberos.KerberosDescriptorTest.JSON_VALUE, java.lang.Object.class);
        junit.framework.Assert.assertNotNull(data);
        org.apache.ambari.server.state.kerberos.KerberosDescriptor descriptor = new org.apache.ambari.server.state.kerberos.KerberosDescriptor(((java.util.Map<?, ?>) (data)));
        org.apache.ambari.server.state.kerberos.KerberosDescriptorTest.validateFromJSON(descriptor);
    }

    @org.junit.Test
    public void testJSONDeserialize() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.kerberos.KerberosDescriptorTest.validateFromJSON(createFromJSON());
    }

    @org.junit.Test
    public void testMapDeserialize() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.kerberos.KerberosDescriptorTest.validateFromMap(createFromMap());
    }

    @org.junit.Test
    public void testInvalid() {
        try {
            org.apache.ambari.server.state.kerberos.KerberosDescriptorTest.KERBEROS_SERVICE_DESCRIPTOR_FACTORY.createInstances(org.apache.ambari.server.state.kerberos.KerberosDescriptorTest.JSON_VALUE + "erroneous text");
            junit.framework.Assert.fail("Should have thrown AmbariException.");
        } catch (org.apache.ambari.server.AmbariException e) {
        } catch (java.lang.Throwable t) {
            junit.framework.Assert.fail("Should have thrown AmbariException.");
        }
    }

    @org.junit.Test
    public void testEquals() throws org.apache.ambari.server.AmbariException {
        junit.framework.Assert.assertTrue(createFromJSON().equals(createFromJSON()));
        junit.framework.Assert.assertFalse(createFromJSON().equals(createFromMap()));
    }

    @org.junit.Test
    public void testToMap() throws org.apache.ambari.server.AmbariException {
        com.google.gson.Gson gson = new com.google.gson.Gson();
        org.apache.ambari.server.state.kerberos.KerberosDescriptor descriptor = createFromMap();
        junit.framework.Assert.assertNotNull(descriptor);
        junit.framework.Assert.assertEquals(gson.toJson(org.apache.ambari.server.state.kerberos.KerberosDescriptorTest.MAP_VALUE), gson.toJson(descriptor.toMap()));
    }

    @org.junit.Test
    public void testUpdate() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.kerberos.KerberosDescriptor descriptor = createFromJSON();
        org.apache.ambari.server.state.kerberos.KerberosDescriptor updatedDescriptor = createFromMap();
        junit.framework.Assert.assertNotNull(descriptor);
        junit.framework.Assert.assertNotNull(updatedDescriptor);
        descriptor.update(updatedDescriptor);
        validateUpdatedData(descriptor);
    }

    @org.junit.Test
    public void testGetReferencedIdentityDescriptor() throws java.io.IOException {
        java.net.URL systemResourceURL = java.lang.ClassLoader.getSystemResource("kerberos/test_get_referenced_identity_descriptor.json");
        junit.framework.Assert.assertNotNull(systemResourceURL);
        org.apache.ambari.server.state.kerberos.KerberosDescriptor descriptor = org.apache.ambari.server.state.kerberos.KerberosDescriptorTest.KERBEROS_DESCRIPTOR_FACTORY.createInstance(new java.io.File(systemResourceURL.getFile()));
        org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identity;
        identity = descriptor.getReferencedIdentityDescriptor("/stack_identity");
        junit.framework.Assert.assertNotNull(identity);
        junit.framework.Assert.assertEquals("stack_identity", identity.getName());
        identity = descriptor.getReferencedIdentityDescriptor("/SERVICE1/service1_identity");
        junit.framework.Assert.assertNotNull(identity);
        junit.framework.Assert.assertEquals("service1_identity", identity.getName());
        junit.framework.Assert.assertNotNull(identity.getParent());
        junit.framework.Assert.assertEquals("SERVICE1", identity.getParent().getName());
        identity = descriptor.getReferencedIdentityDescriptor("/SERVICE2/SERVICE2_COMPONENT1/service2_component1_identity");
        junit.framework.Assert.assertNotNull(identity);
        junit.framework.Assert.assertEquals("service2_component1_identity", identity.getName());
        junit.framework.Assert.assertNotNull(identity.getParent());
        junit.framework.Assert.assertEquals("SERVICE2_COMPONENT1", identity.getParent().getName());
        junit.framework.Assert.assertNotNull(identity.getParent().getParent());
        junit.framework.Assert.assertEquals("SERVICE2", identity.getParent().getParent().getName());
    }

    @org.junit.Test
    public void testGetReferencedIdentityDescriptor_NameCollisions() throws java.io.IOException {
        java.net.URL systemResourceURL = java.lang.ClassLoader.getSystemResource("kerberos/test_get_referenced_identity_descriptor.json");
        junit.framework.Assert.assertNotNull(systemResourceURL);
        org.apache.ambari.server.state.kerberos.KerberosDescriptor descriptor = org.apache.ambari.server.state.kerberos.KerberosDescriptorTest.KERBEROS_DESCRIPTOR_FACTORY.createInstance(new java.io.File(systemResourceURL.getFile()));
        org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identity;
        identity = descriptor.getReferencedIdentityDescriptor("/collision");
        junit.framework.Assert.assertNotNull(identity);
        junit.framework.Assert.assertEquals("collision", identity.getName());
        junit.framework.Assert.assertNotNull(identity.getParent());
        junit.framework.Assert.assertEquals(null, identity.getParent().getName());
        identity = descriptor.getReferencedIdentityDescriptor("/SERVICE1/collision");
        junit.framework.Assert.assertNotNull(identity);
        junit.framework.Assert.assertEquals("collision", identity.getName());
        junit.framework.Assert.assertNotNull(identity.getParent());
        junit.framework.Assert.assertEquals("SERVICE1", identity.getParent().getName());
        identity = descriptor.getReferencedIdentityDescriptor("/SERVICE2/SERVICE2_COMPONENT1/collision");
        junit.framework.Assert.assertNotNull(identity);
        junit.framework.Assert.assertEquals("collision", identity.getName());
        junit.framework.Assert.assertNotNull(identity.getParent());
        junit.framework.Assert.assertEquals("SERVICE2_COMPONENT1", identity.getParent().getName());
        junit.framework.Assert.assertNotNull(identity.getParent().getParent());
        junit.framework.Assert.assertEquals("SERVICE2", identity.getParent().getParent().getName());
    }

    @org.junit.Test
    public void testGetReferencedIdentityDescriptor_RelativePath() throws java.io.IOException {
        java.net.URL systemResourceURL = java.lang.ClassLoader.getSystemResource("kerberos/test_get_referenced_identity_descriptor.json");
        junit.framework.Assert.assertNotNull(systemResourceURL);
        org.apache.ambari.server.state.kerberos.KerberosDescriptor descriptor = org.apache.ambari.server.state.kerberos.KerberosDescriptorTest.KERBEROS_DESCRIPTOR_FACTORY.createInstance(new java.io.File(systemResourceURL.getFile()));
        junit.framework.Assert.assertNotNull(descriptor);
        org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor = descriptor.getService("SERVICE2");
        junit.framework.Assert.assertNotNull(serviceDescriptor);
        org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor componentDescriptor = serviceDescriptor.getComponent("SERVICE2_COMPONENT1");
        junit.framework.Assert.assertNotNull(componentDescriptor);
        org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identity;
        identity = componentDescriptor.getReferencedIdentityDescriptor("../service2_identity");
        junit.framework.Assert.assertNotNull(identity);
        junit.framework.Assert.assertEquals("service2_identity", identity.getName());
        junit.framework.Assert.assertEquals(serviceDescriptor, identity.getParent());
        identity = serviceDescriptor.getReferencedIdentityDescriptor("../service2_identity");
        junit.framework.Assert.assertNull(identity);
    }

    @org.junit.Test
    public void testGetReferencedIdentityDescriptor_Recursive() throws java.io.IOException {
        boolean identityFound;
        java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> identities;
        java.net.URL systemResourceURL = java.lang.ClassLoader.getSystemResource("kerberos/test_get_referenced_identity_descriptor.json");
        junit.framework.Assert.assertNotNull(systemResourceURL);
        org.apache.ambari.server.state.kerberos.KerberosDescriptor descriptor = org.apache.ambari.server.state.kerberos.KerberosDescriptorTest.KERBEROS_DESCRIPTOR_FACTORY.createInstance(new java.io.File(systemResourceURL.getFile()));
        junit.framework.Assert.assertNotNull(descriptor);
        org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor = descriptor.getService("SERVICE2");
        junit.framework.Assert.assertNotNull(serviceDescriptor);
        identities = serviceDescriptor.getIdentities(true, null);
        junit.framework.Assert.assertNotNull(identities);
        identityFound = false;
        for (org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identity : identities) {
            if ("service2_stack_reference".equals(identity.getName())) {
                junit.framework.Assert.assertEquals("stack@${realm}", identity.getPrincipalDescriptor().getValue());
                junit.framework.Assert.assertEquals("${keytab_dir}/service2_stack.keytab", identity.getKeytabDescriptor().getFile());
                junit.framework.Assert.assertEquals("/stack_identity", identity.getReference());
                junit.framework.Assert.assertEquals("service2/property1_principal", identity.getPrincipalDescriptor().getConfiguration());
                identityFound = true;
            }
        }
        junit.framework.Assert.assertTrue(identityFound);
        org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor componentDescriptor = serviceDescriptor.getComponent("SERVICE2_COMPONENT1");
        junit.framework.Assert.assertNotNull(componentDescriptor);
        identities = componentDescriptor.getIdentities(true, null);
        junit.framework.Assert.assertNotNull(identities);
        identityFound = false;
        for (org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identity : identities) {
            if ("component1_service2_stack_reference".equals(identity.getName())) {
                junit.framework.Assert.assertEquals("stack@${realm}", identity.getPrincipalDescriptor().getValue());
                junit.framework.Assert.assertEquals("${keytab_dir}/service2_stack.keytab", identity.getKeytabDescriptor().getFile());
                junit.framework.Assert.assertEquals("/SERVICE2/service2_stack_reference", identity.getReference());
                junit.framework.Assert.assertEquals("component1_service2/property1_principal", identity.getPrincipalDescriptor().getConfiguration());
                identityFound = true;
            }
        }
        junit.framework.Assert.assertTrue(identityFound);
    }

    @org.junit.Test
    public void testFiltersOutIdentitiesBasedonInstalledServices() throws java.io.IOException {
        java.net.URL systemResourceURL = java.lang.ClassLoader.getSystemResource("kerberos/test_filtering_identity_descriptor.json");
        org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor componentDescriptor = org.apache.ambari.server.state.kerberos.KerberosDescriptorTest.KERBEROS_DESCRIPTOR_FACTORY.createInstance(new java.io.File(systemResourceURL.getFile())).getService("SERVICE1").getComponent("SERVICE1_COMPONENT1");
        java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> identities = componentDescriptor.getIdentities(true, new org.apache.commons.collections.map.HashedMap() {
            {
                put("services", java.util.Collections.emptySet());
            }
        });
        junit.framework.Assert.assertEquals(0, identities.size());
        identities = componentDescriptor.getIdentities(true, new org.apache.commons.collections.map.HashedMap() {
            {
                put("services", java.util.Arrays.asList("REF_SERVICE1"));
            }
        });
        junit.framework.Assert.assertEquals(1, identities.size());
    }

    @org.junit.Test
    public void testCollectPrincipalNames() throws java.lang.Exception {
        java.net.URL systemResourceURL = java.lang.ClassLoader.getSystemResource("kerberos/test_get_referenced_identity_descriptor.json");
        org.apache.ambari.server.state.kerberos.KerberosDescriptor descriptor = org.apache.ambari.server.state.kerberos.KerberosDescriptorTest.KERBEROS_DESCRIPTOR_FACTORY.createInstance(new java.io.File(systemResourceURL.getFile()));
        java.util.Map<java.lang.String, java.lang.String> principalsPerComponent = descriptor.principals();
        junit.framework.Assert.assertEquals("service2_component1@${realm}", principalsPerComponent.get("SERVICE2/SERVICE2_COMPONENT1/service2_component1_identity"));
        junit.framework.Assert.assertEquals("service1@${realm}", principalsPerComponent.get("SERVICE1/service1_identity"));
    }

    @org.junit.Test
    public void testIdentityReferences() throws java.lang.Exception {
        org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = org.apache.ambari.server.state.kerberos.KerberosDescriptorTest.KERBEROS_DESCRIPTOR_FACTORY.createInstance(org.apache.ambari.server.state.kerberos.KerberosDescriptorTest.JSON_VALUE_IDENTITY_REFERENCES);
        org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor;
        java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> identities;
        serviceDescriptor = kerberosDescriptor.getService("SERVICE1");
        identities = serviceDescriptor.getIdentities(true, null);
        junit.framework.Assert.assertEquals(2, identities.size());
        for (org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identity : identities) {
            if (identity.isReference()) {
                junit.framework.Assert.assertEquals("service1_spnego", identity.getName());
                junit.framework.Assert.assertEquals("/spnego", identity.getReference());
            } else {
                junit.framework.Assert.assertEquals("service1_identity", identity.getName());
                junit.framework.Assert.assertNull(identity.getReference());
            }
        }
        junit.framework.Assert.assertEquals("service1_identity", identities.get(1).getName());
        junit.framework.Assert.assertNull(identities.get(1).getReference());
        serviceDescriptor = kerberosDescriptor.getService("SERVICE2");
        identities = serviceDescriptor.getIdentities(true, null);
        junit.framework.Assert.assertEquals(2, identities.size());
        for (org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identity : identities) {
            if (identity.isReference()) {
                junit.framework.Assert.assertEquals("/spnego", identity.getName());
                junit.framework.Assert.assertNull(identity.getReference());
            } else {
                junit.framework.Assert.assertEquals("service2_identity", identity.getName());
                junit.framework.Assert.assertNull(identity.getReference());
            }
        }
    }

    @org.junit.Test
    public void testGetPath() throws java.lang.Exception {
        org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor;
        org.apache.ambari.server.state.kerberos.KerberosServiceDescriptor serviceDescriptor;
        java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> identities;
        kerberosDescriptor = org.apache.ambari.server.state.kerberos.KerberosDescriptorTest.KERBEROS_DESCRIPTOR_FACTORY.createInstance(org.apache.ambari.server.state.kerberos.KerberosDescriptorTest.JSON_VALUE);
        serviceDescriptor = kerberosDescriptor.getService("SERVICE_NAME");
        identities = serviceDescriptor.getIdentities(false, null);
        junit.framework.Assert.assertEquals(1, identities.size());
        junit.framework.Assert.assertEquals("/SERVICE_NAME/identity_1", identities.get(0).getPath());
        org.apache.ambari.server.state.kerberos.KerberosComponentDescriptor componentDescriptor = serviceDescriptor.getComponent("COMPONENT_NAME");
        identities = componentDescriptor.getIdentities(false, null);
        junit.framework.Assert.assertEquals(1, identities.size());
        junit.framework.Assert.assertEquals("/SERVICE_NAME/COMPONENT_NAME/identity_1", identities.get(0).getPath());
        kerberosDescriptor = org.apache.ambari.server.state.kerberos.KerberosDescriptorTest.KERBEROS_DESCRIPTOR_FACTORY.createInstance(org.apache.ambari.server.state.kerberos.KerberosDescriptorTest.JSON_VALUE_IDENTITY_REFERENCES);
        serviceDescriptor = kerberosDescriptor.getService("SERVICE1");
        identities = serviceDescriptor.getIdentities(true, null);
        junit.framework.Assert.assertEquals(2, identities.size());
        junit.framework.Assert.assertEquals("/SERVICE1/service1_spnego", identities.get(0).getPath());
        junit.framework.Assert.assertEquals("/SERVICE1/service1_identity", identities.get(1).getPath());
    }

    @org.junit.Test
    public void testGetReferences() throws java.lang.Exception {
        org.apache.ambari.server.state.kerberos.KerberosDescriptor kerberosDescriptor = org.apache.ambari.server.state.kerberos.KerberosDescriptorTest.KERBEROS_DESCRIPTOR_FACTORY.createInstance(org.apache.ambari.server.state.kerberos.KerberosDescriptorTest.JSON_VALUE_IDENTITY_REFERENCES);
        org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identity;
        java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> references;
        java.util.Set<java.lang.String> paths;
        identity = kerberosDescriptor.getIdentity("spnego");
        references = identity.findReferences();
        junit.framework.Assert.assertNotNull(references);
        junit.framework.Assert.assertEquals(2, references.size());
        paths = collectPaths(references);
        junit.framework.Assert.assertTrue(paths.contains("/SERVICE1/service1_spnego"));
        junit.framework.Assert.assertTrue(paths.contains("/SERVICE2//spnego"));
        identity = kerberosDescriptor.getService("SERVICE1").getIdentity("service1_identity");
        references = identity.findReferences();
        junit.framework.Assert.assertNotNull(references);
        junit.framework.Assert.assertEquals(1, references.size());
        paths = collectPaths(references);
        junit.framework.Assert.assertTrue(paths.contains("/SERVICE2/COMPONENT21/service2_component1_service1_identity"));
        identity = kerberosDescriptor.getService("SERVICE2").getComponent("COMPONENT21").getIdentity("component1_identity");
        references = identity.findReferences();
        junit.framework.Assert.assertNotNull(references);
        junit.framework.Assert.assertEquals(1, references.size());
        paths = collectPaths(references);
        junit.framework.Assert.assertTrue(paths.contains("/SERVICE2/COMPONENT21/service2_component1_component1_identity"));
        identity = kerberosDescriptor.getService("SERVICE2").getIdentity("service2_identity");
        references = identity.findReferences();
        junit.framework.Assert.assertNotNull(references);
        junit.framework.Assert.assertEquals(1, references.size());
        paths = collectPaths(references);
        junit.framework.Assert.assertTrue(paths.contains("/SERVICE2/COMPONENT21/service2_component1_service2_identity"));
    }

    private java.util.Set<java.lang.String> collectPaths(java.util.List<org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor> identityDescriptors) {
        java.util.Set<java.lang.String> paths = new java.util.HashSet<>();
        for (org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptor : identityDescriptors) {
            paths.add(identityDescriptor.getPath());
        }
        return paths;
    }
}