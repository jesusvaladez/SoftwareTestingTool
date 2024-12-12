package org.apache.ambari.server.state.kerberos;
@org.junit.experimental.categories.Category({ category.KerberosTest.class })
public class KerberosKeytabDescriptorTest {
    static final java.lang.String JSON_VALUE = "{" + (((((((((("  \"file\": \"/etc/security/keytabs/${host}/subject.service.keytab\"," + "  \"owner\": {") + "      \"name\": \"subject\",") + "      \"access\": \"rw\"") + "  },") + "  \"group\": {") + "      \"name\": \"hadoop\",") + "      \"access\": \"r\"") + "  },") + "  \"configuration\": \"service-site/service.component.keytab.file\"") + "}");

    static final java.util.Map<java.lang.String, java.lang.Object> MAP_VALUE;

    static {
        java.util.TreeMap<java.lang.String, java.lang.Object> ownerMap = new java.util.TreeMap<>();
        ownerMap.put(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_ACL_NAME, "root");
        ownerMap.put(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_ACL_ACCESS, "rw");
        java.util.TreeMap<java.lang.String, java.lang.Object> groupMap = new java.util.TreeMap<>();
        groupMap.put(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_ACL_NAME, "hadoop");
        groupMap.put(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_ACL_ACCESS, "r");
        MAP_VALUE = new java.util.TreeMap<>();
        MAP_VALUE.put(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_FILE, "/etc/security/keytabs/subject.service.keytab");
        MAP_VALUE.put(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_OWNER, ownerMap);
        MAP_VALUE.put(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_GROUP, groupMap);
        MAP_VALUE.put(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor.KEY_CONFIGURATION, "service-site/service2.component.keytab.file");
    }

    static void validateFromJSON(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor keytabDescriptor) {
        junit.framework.Assert.assertNotNull(keytabDescriptor);
        junit.framework.Assert.assertFalse(keytabDescriptor.isContainer());
        junit.framework.Assert.assertEquals("/etc/security/keytabs/${host}/subject.service.keytab", keytabDescriptor.getFile());
        junit.framework.Assert.assertEquals("subject", keytabDescriptor.getOwnerName());
        junit.framework.Assert.assertEquals("rw", keytabDescriptor.getOwnerAccess());
        junit.framework.Assert.assertEquals("hadoop", keytabDescriptor.getGroupName());
        junit.framework.Assert.assertEquals("r", keytabDescriptor.getGroupAccess());
        junit.framework.Assert.assertEquals("service-site/service.component.keytab.file", keytabDescriptor.getConfiguration());
    }

    static void validateFromMap(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor keytabDescriptor) {
        junit.framework.Assert.assertNotNull(keytabDescriptor);
        junit.framework.Assert.assertFalse(keytabDescriptor.isContainer());
        junit.framework.Assert.assertEquals("/etc/security/keytabs/subject.service.keytab", keytabDescriptor.getFile());
        junit.framework.Assert.assertEquals("root", keytabDescriptor.getOwnerName());
        junit.framework.Assert.assertEquals("rw", keytabDescriptor.getOwnerAccess());
        junit.framework.Assert.assertEquals("hadoop", keytabDescriptor.getGroupName());
        junit.framework.Assert.assertEquals("r", keytabDescriptor.getGroupAccess());
        junit.framework.Assert.assertEquals("service-site/service2.component.keytab.file", keytabDescriptor.getConfiguration());
    }

    static void validateUpdatedData(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor keytabDescriptor) {
        junit.framework.Assert.assertNotNull(keytabDescriptor);
        junit.framework.Assert.assertEquals("/etc/security/keytabs/subject.service.keytab", keytabDescriptor.getFile());
        junit.framework.Assert.assertEquals("root", keytabDescriptor.getOwnerName());
        junit.framework.Assert.assertEquals("rw", keytabDescriptor.getOwnerAccess());
        junit.framework.Assert.assertEquals("hadoop", keytabDescriptor.getGroupName());
        junit.framework.Assert.assertEquals("r", keytabDescriptor.getGroupAccess());
        junit.framework.Assert.assertEquals("service-site/service2.component.keytab.file", keytabDescriptor.getConfiguration());
    }

    private static org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor createFromJSON() {
        java.util.Map<?, ?> map = new com.google.gson.Gson().fromJson(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptorTest.JSON_VALUE, new com.google.gson.reflect.TypeToken<java.util.Map<?, ?>>() {}.getType());
        return new org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor(map);
    }

    private static org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor createFromMap() {
        return new org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptorTest.MAP_VALUE);
    }

    @org.junit.Test
    public void testJSONDeserialize() {
        org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptorTest.validateFromJSON(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptorTest.createFromJSON());
    }

    @org.junit.Test
    public void testMapDeserialize() {
        org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptorTest.validateFromMap(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptorTest.createFromMap());
    }

    @org.junit.Test
    public void testEquals() throws org.apache.ambari.server.AmbariException {
        junit.framework.Assert.assertTrue(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptorTest.createFromJSON().equals(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptorTest.createFromJSON()));
        junit.framework.Assert.assertFalse(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptorTest.createFromJSON().equals(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptorTest.createFromMap()));
    }

    @org.junit.Test
    public void testToMap() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor descriptor = org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptorTest.createFromMap();
        junit.framework.Assert.assertNotNull(descriptor);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptorTest.MAP_VALUE, descriptor.toMap());
    }

    @org.junit.Test
    public void testUpdate() {
        org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor keytabDescriptor = org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptorTest.createFromJSON();
        org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptor updatedKeytabDescriptor = org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptorTest.createFromMap();
        junit.framework.Assert.assertNotNull(keytabDescriptor);
        junit.framework.Assert.assertNotNull(updatedKeytabDescriptor);
        keytabDescriptor.update(updatedKeytabDescriptor);
        org.apache.ambari.server.state.kerberos.KerberosKeytabDescriptorTest.validateUpdatedData(keytabDescriptor);
    }
}