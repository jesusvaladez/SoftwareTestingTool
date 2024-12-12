package org.apache.ambari.server.state.kerberos;
@org.junit.experimental.categories.Category({ category.KerberosTest.class })
public class KerberosIdentityDescriptorTest {
    static final java.lang.String JSON_VALUE = ((((((("{" + (("  \"name\": \"identity_1\"" + ",") + "  \"principal\":")) + org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptorTest.JSON_VALUE) + ",") + "  \"keytab\":") + org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptorTest.JSON_VALUE) + ",") + "  \"when\": {\"contains\" : [\"services\", \"HIVE\"]}") + "}";

    static final java.util.Map<java.lang.String, java.lang.Object> MAP_VALUE;

    static final java.util.Map<java.lang.String, java.lang.Object> MAP_VALUE_ALT;

    static final java.util.Map<java.lang.String, java.lang.Object> MAP_VALUE_REFERENCE;

    static {
        MAP_VALUE = new java.util.TreeMap<>();
        MAP_VALUE.put(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.KEY_NAME, "identity_1");
        MAP_VALUE.put(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.KEY_PRINCIPAL, org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptorTest.MAP_VALUE);
        MAP_VALUE.put(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.KEY_KEYTAB, org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptorTest.MAP_VALUE);
        MAP_VALUE_ALT = new java.util.TreeMap<>();
        MAP_VALUE_ALT.put(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.KEY_NAME, "identity_2");
        MAP_VALUE_ALT.put(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.KEY_PRINCIPAL, org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptorTest.MAP_VALUE);
        MAP_VALUE_ALT.put(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.KEY_KEYTAB, org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptorTest.MAP_VALUE);
        java.util.TreeMap<java.lang.String, java.lang.Object> ownerMap = new java.util.TreeMap<>();
        ownerMap.put(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_ACL_NAME, "me");
        ownerMap.put(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_ACL_ACCESS, "rw");
        java.util.TreeMap<java.lang.String, java.lang.Object> groupMap = new java.util.TreeMap<>();
        groupMap.put(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_ACL_NAME, "nobody");
        groupMap.put(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_ACL_ACCESS, "");
        java.util.TreeMap<java.lang.String, java.lang.Object> keytabMap = new java.util.TreeMap<>();
        keytabMap.put(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_FILE, "/home/user/me/subject.service.keytab");
        keytabMap.put(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_OWNER, ownerMap);
        keytabMap.put(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_GROUP, groupMap);
        keytabMap.put(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_CONFIGURATION, "service-site/me.component.keytab.file");
        MAP_VALUE_REFERENCE = new java.util.TreeMap<>();
        MAP_VALUE_REFERENCE.put(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.KEY_NAME, "shared_identity");
        MAP_VALUE_REFERENCE.put(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.KEY_REFERENCE, "/shared");
        MAP_VALUE_REFERENCE.put(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor.KEY_KEYTAB, keytabMap);
    }

    static void validateFromJSON(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptor) {
        junit.framework.Assert.assertNotNull(identityDescriptor);
        junit.framework.Assert.assertFalse(identityDescriptor.isContainer());
        org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptorTest.validateFromJSON(identityDescriptor.getPrincipalDescriptor());
        org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptorTest.validateFromJSON(identityDescriptor.getKeytabDescriptor());
    }

    static void validateFromMap(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptor) {
        junit.framework.Assert.assertNotNull(identityDescriptor);
        junit.framework.Assert.assertFalse(identityDescriptor.isContainer());
        org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptorTest.validateFromMap(identityDescriptor.getPrincipalDescriptor());
        org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptorTest.validateFromMap(identityDescriptor.getKeytabDescriptor());
    }

    static void validateUpdatedData(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptor) {
        junit.framework.Assert.assertNotNull(identityDescriptor);
        org.apache.ambari.server.state.kerberos.KerberosPrincipalDescriptorTest.validateUpdatedData(identityDescriptor.getPrincipalDescriptor());
        org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptorTest.validateUpdatedData(identityDescriptor.getKeytabDescriptor());
    }

    private static org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor createFromJSON() {
        java.util.Map<?, ?> map = new com.google.gson.Gson().fromJson(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptorTest.JSON_VALUE, new com.google.gson.reflect.TypeToken<java.util.Map<?, ?>>() {}.getType());
        return new org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor(map);
    }

    private static org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor createFromMap() {
        return new org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptorTest.MAP_VALUE);
    }

    @org.junit.Test
    public void testJSONDeserialize() {
        org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptorTest.validateFromJSON(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptorTest.createFromJSON());
    }

    @org.junit.Test
    public void testMapDeserialize() {
        org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptorTest.validateFromMap(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptorTest.createFromMap());
    }

    @org.junit.Test
    public void testEquals() throws org.apache.ambari.server.AmbariException {
        junit.framework.Assert.assertTrue(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptorTest.createFromJSON().equals(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptorTest.createFromJSON()));
        junit.framework.Assert.assertFalse(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptorTest.createFromJSON().equals(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptorTest.createFromMap()));
    }

    @org.junit.Test
    public void testToMap() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor descriptor = org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptorTest.createFromMap();
        junit.framework.Assert.assertNotNull(descriptor);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptorTest.MAP_VALUE, descriptor.toMap());
    }

    @org.junit.Test
    public void testUpdate() {
        org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptor = org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptorTest.createFromJSON();
        org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor updatedIdentityDescriptor = org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptorTest.createFromMap();
        junit.framework.Assert.assertNotNull(identityDescriptor);
        junit.framework.Assert.assertNotNull(updatedIdentityDescriptor);
        identityDescriptor.update(updatedIdentityDescriptor);
        org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptorTest.validateUpdatedData(identityDescriptor);
    }

    @org.junit.Test
    public void testShouldInclude() {
        org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptor identityDescriptor = org.apache.ambari.server.state.kerberos.KerberosIdentityDescriptorTest.createFromJSON();
        java.util.Map<java.lang.String, java.lang.Object> context = new java.util.TreeMap<>();
        context.put("services", new java.util.HashSet<>(java.util.Arrays.asList("HIVE", "HDFS", "ZOOKEEPER")));
        junit.framework.Assert.assertTrue(identityDescriptor.shouldInclude(context));
        context.put("services", new java.util.HashSet<>(java.util.Arrays.asList("NOT_HIVE", "HDFS", "ZOOKEEPER")));
        junit.framework.Assert.assertFalse(identityDescriptor.shouldInclude(context));
    }
}