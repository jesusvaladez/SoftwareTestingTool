package org.apache.ambari.server.api.services;
public class NamedPropertySetTest {
    @org.junit.Test
    public void testGetters() {
        java.util.Map<java.lang.String, java.lang.Object> mapProps = new java.util.HashMap<>();
        mapProps.put("foo", "bar");
        org.apache.ambari.server.api.services.NamedPropertySet propertySet = new org.apache.ambari.server.api.services.NamedPropertySet("foo", mapProps);
        org.junit.Assert.assertEquals("foo", propertySet.getName());
        org.junit.Assert.assertEquals(mapProps, propertySet.getProperties());
    }

    @org.junit.Test
    public void testEquals() {
        java.util.Map<java.lang.String, java.lang.Object> mapProps = new java.util.HashMap<>();
        mapProps.put("foo", "bar");
        org.apache.ambari.server.api.services.NamedPropertySet propertySet = new org.apache.ambari.server.api.services.NamedPropertySet("foo", mapProps);
        org.apache.ambari.server.api.services.NamedPropertySet propertySet2 = new org.apache.ambari.server.api.services.NamedPropertySet("foo", mapProps);
        org.junit.Assert.assertEquals(propertySet, propertySet2);
        org.apache.ambari.server.api.services.NamedPropertySet propertySet3 = new org.apache.ambari.server.api.services.NamedPropertySet("bar", mapProps);
        org.junit.Assert.assertFalse(propertySet.equals(propertySet3));
        org.apache.ambari.server.api.services.NamedPropertySet propertySet4 = new org.apache.ambari.server.api.services.NamedPropertySet("foo", new java.util.HashMap<>());
        org.junit.Assert.assertFalse(propertySet.equals(propertySet4));
    }

    @org.junit.Test
    public void testHashCode() {
        java.util.Map<java.lang.String, java.lang.Object> mapProps = new java.util.HashMap<>();
        org.apache.ambari.server.api.services.NamedPropertySet propertySet = new org.apache.ambari.server.api.services.NamedPropertySet("foo", mapProps);
        org.apache.ambari.server.api.services.NamedPropertySet propertySet2 = new org.apache.ambari.server.api.services.NamedPropertySet("foo", mapProps);
        org.junit.Assert.assertEquals(propertySet.hashCode(), propertySet2.hashCode());
    }
}