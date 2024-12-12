package org.apache.ambari.server.state.kerberos;
@org.junit.experimental.categories.Category({ category.KerberosTest.class })
public class KerberosPrincipalDescriptorTest {
    static final java.lang.String JSON_VALUE = "{" + (((("\"value\": \"service/_HOST@_REALM\"," + "\"configuration\": \"service-site/service.component.kerberos.principal\",") + "\"type\": \"service\",") + "\"local_username\": \"localUser\"") + "}");

    private static final java.lang.String JSON_VALUE_SPARSE = "{" + ("\"value\": \"serviceOther/_HOST@_REALM\"" + "}");

    public static final java.util.Map<java.lang.String, java.lang.Object> MAP_VALUE;

    private static final java.util.Map<java.lang.String, java.lang.Object> MAP_VALUE_SPARSE;

    static {
        MAP_VALUE = new java.util.TreeMap<>();
        MAP_VALUE.put(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor.KEY_VALUE, "user@_REALM");
        MAP_VALUE.put(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor.KEY_CONFIGURATION, "service-site/service.component.kerberos.https.principal");
        MAP_VALUE.put(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor.KEY_TYPE, "user");
        MAP_VALUE.put(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor.KEY_LOCAL_USERNAME, null);
        MAP_VALUE_SPARSE = new java.util.TreeMap<>();
        MAP_VALUE_SPARSE.put(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor.KEY_VALUE, "userOther@_REALM");
    }

    static void validateFromJSON(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principalDescriptor) {
        junit.framework.Assert.assertNotNull(principalDescriptor);
        junit.framework.Assert.assertFalse(principalDescriptor.isContainer());
        junit.framework.Assert.assertEquals("service/_HOST@_REALM", principalDescriptor.getValue());
        junit.framework.Assert.assertEquals("service-site/service.component.kerberos.principal", principalDescriptor.getConfiguration());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE, principalDescriptor.getType());
        junit.framework.Assert.assertEquals("localUser", principalDescriptor.getLocalUsername());
    }

    static void validateFromMap(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principalDescriptor) {
        junit.framework.Assert.assertNotNull(principalDescriptor);
        junit.framework.Assert.assertFalse(principalDescriptor.isContainer());
        junit.framework.Assert.assertEquals("user@_REALM", principalDescriptor.getValue());
        junit.framework.Assert.assertEquals("service-site/service.component.kerberos.https.principal", principalDescriptor.getConfiguration());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.kerberos.KerberosPrincipalType.USER, principalDescriptor.getType());
        junit.framework.Assert.assertNull(principalDescriptor.getLocalUsername());
    }

    static void validateUpdatedData(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principalDescriptor) {
        junit.framework.Assert.assertNotNull(principalDescriptor);
        junit.framework.Assert.assertEquals("user@_REALM", principalDescriptor.getValue());
        junit.framework.Assert.assertEquals("service-site/service.component.kerberos.https.principal", principalDescriptor.getConfiguration());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.kerberos.KerberosPrincipalType.USER, principalDescriptor.getType());
        junit.framework.Assert.assertEquals("localUser", principalDescriptor.getLocalUsername());
    }

    private static org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor createFromJSON() {
        java.util.Map<?, ?> map = new com.google.gson.Gson().fromJson(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptorTest.JSON_VALUE, new com.google.gson.reflect.TypeToken<java.util.Map<?, ?>>() {}.getType());
        return new org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor(map);
    }

    private static org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor createFromJSONSparse() {
        java.util.Map<?, ?> map = new com.google.gson.Gson().fromJson(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptorTest.JSON_VALUE_SPARSE, new com.google.gson.reflect.TypeToken<java.util.Map<?, ?>>() {}.getType());
        return new org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor(map);
    }

    private static org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor createFromMap() {
        return new org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptorTest.MAP_VALUE);
    }

    private static org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor createFromMapSparse() {
        return new org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptorTest.MAP_VALUE_SPARSE);
    }

    @org.junit.Test
    public void testJSONDeserialize() {
        org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptorTest.validateFromJSON(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptorTest.createFromJSON());
    }

    @org.junit.Test
    public void testMapDeserialize() {
        org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptorTest.validateFromMap(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptorTest.createFromMap());
    }

    @org.junit.Test
    public void testEquals() throws org.apache.ambari.server.AmbariException {
        junit.framework.Assert.assertTrue(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptorTest.createFromJSON().equals(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptorTest.createFromJSON()));
        junit.framework.Assert.assertFalse(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptorTest.createFromJSON().equals(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptorTest.createFromMap()));
    }

    @org.junit.Test
    public void testToMap() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor descriptor = org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptorTest.createFromMap();
        junit.framework.Assert.assertNotNull(descriptor);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptorTest.MAP_VALUE, descriptor.toMap());
    }

    @org.junit.Test
    public void testUpdate() {
        org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principalDescriptor = org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptorTest.createFromJSON();
        org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor updatedPrincipalDescriptor = org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptorTest.createFromMap();
        junit.framework.Assert.assertNotNull(principalDescriptor);
        junit.framework.Assert.assertNotNull(updatedPrincipalDescriptor);
        principalDescriptor.update(updatedPrincipalDescriptor);
        org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptorTest.validateUpdatedData(principalDescriptor);
    }

    @org.junit.Test
    public void testUpdateSparse() {
        org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor principalDescriptor;
        org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptor updatedPrincipalDescriptor;
        principalDescriptor = org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptorTest.createFromJSON();
        updatedPrincipalDescriptor = org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptorTest.createFromJSONSparse();
        junit.framework.Assert.assertNotNull(principalDescriptor);
        junit.framework.Assert.assertNotNull(updatedPrincipalDescriptor);
        junit.framework.Assert.assertEquals("service/_HOST@_REALM", principalDescriptor.getValue());
        junit.framework.Assert.assertEquals("service-site/service.component.kerberos.principal", principalDescriptor.getConfiguration());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE, principalDescriptor.getType());
        junit.framework.Assert.assertEquals("localUser", principalDescriptor.getLocalUsername());
        principalDescriptor.update(updatedPrincipalDescriptor);
        junit.framework.Assert.assertEquals("serviceOther/_HOST@_REALM", principalDescriptor.getValue());
        junit.framework.Assert.assertEquals("service-site/service.component.kerberos.principal", principalDescriptor.getConfiguration());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.kerberos.KerberosPrincipalType.SERVICE, principalDescriptor.getType());
        junit.framework.Assert.assertEquals("localUser", principalDescriptor.getLocalUsername());
        principalDescriptor = org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptorTest.createFromMap();
        updatedPrincipalDescriptor = org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptorTest.createFromMapSparse();
        junit.framework.Assert.assertNotNull(principalDescriptor);
        junit.framework.Assert.assertNotNull(updatedPrincipalDescriptor);
        junit.framework.Assert.assertEquals("user@_REALM", principalDescriptor.getValue());
        junit.framework.Assert.assertEquals("service-site/service.component.kerberos.https.principal", principalDescriptor.getConfiguration());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.kerberos.KerberosPrincipalType.USER, principalDescriptor.getType());
        junit.framework.Assert.assertNull(principalDescriptor.getLocalUsername());
        principalDescriptor.update(updatedPrincipalDescriptor);
        junit.framework.Assert.assertEquals("userOther@_REALM", principalDescriptor.getValue());
        junit.framework.Assert.assertEquals("service-site/service.component.kerberos.https.principal", principalDescriptor.getConfiguration());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.kerberos.KerberosPrincipalType.USER, principalDescriptor.getType());
        junit.framework.Assert.assertNull(principalDescriptor.getLocalUsername());
    }
}