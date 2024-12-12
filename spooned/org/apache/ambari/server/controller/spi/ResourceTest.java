package org.apache.ambari.server.controller.spi;
public class ResourceTest {
    @org.junit.Test
    public void testResource() {
        for (org.apache.ambari.server.controller.spi.Resource.InternalType internalType : org.apache.ambari.server.controller.spi.Resource.InternalType.values()) {
            org.apache.ambari.server.controller.spi.Resource.Type type = null;
            try {
                type = org.apache.ambari.server.controller.spi.Resource.Type.valueOf(internalType.name());
            } catch (java.lang.IllegalArgumentException e) {
                org.junit.Assert.fail(("Resource.Type should be defined for internal type " + internalType.name()) + ".");
            }
            org.junit.Assert.assertEquals(type.name(), internalType.name());
            org.junit.Assert.assertEquals(type.ordinal(), internalType.ordinal());
        }
        org.apache.ambari.server.controller.spi.Resource.Type newType = new org.apache.ambari.server.controller.spi.Resource.Type("newType");
        org.apache.ambari.server.controller.spi.Resource.Type newType2 = new org.apache.ambari.server.controller.spi.Resource.Type("newType2");
        org.junit.Assert.assertFalse(newType.equals(newType2));
        org.junit.Assert.assertFalse(newType2.equals(newType));
        org.junit.Assert.assertEquals("newType", newType.name());
        org.junit.Assert.assertFalse(newType.isInternalType());
        try {
            newType.getInternalType();
            org.junit.Assert.fail("Can't get internal type for a extended resource.");
        } catch (java.lang.UnsupportedOperationException e) {
        }
        org.apache.ambari.server.controller.spi.Resource.Type t1 = org.apache.ambari.server.controller.spi.Resource.Type.valueOf("newType");
        org.apache.ambari.server.controller.spi.Resource.Type t2 = org.apache.ambari.server.controller.spi.Resource.Type.valueOf("newType2");
        org.junit.Assert.assertTrue(newType.equals(t1));
        org.junit.Assert.assertTrue(t1.equals(newType));
        org.junit.Assert.assertTrue(newType2.equals(t2));
        org.junit.Assert.assertTrue(t2.equals(newType2));
        org.junit.Assert.assertFalse(t1.equals(newType2));
        org.junit.Assert.assertFalse(t2.equals(newType));
        try {
            org.apache.ambari.server.controller.spi.Resource.Type.valueOf("badType");
            org.junit.Assert.fail("Expected IllegalArgumentException.");
        } catch (java.lang.IllegalArgumentException e) {
        }
    }
}