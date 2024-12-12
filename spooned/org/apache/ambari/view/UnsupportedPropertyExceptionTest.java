package org.apache.ambari.view;
public class UnsupportedPropertyExceptionTest {
    @org.junit.Test
    public void testGetType() throws java.lang.Exception {
        org.apache.ambari.view.UnsupportedPropertyException exception = new org.apache.ambari.view.UnsupportedPropertyException("type", java.util.Collections.singleton("p1"));
        org.junit.Assert.assertEquals("type", exception.getType());
    }

    @org.junit.Test
    public void testGetPropertyIds() throws java.lang.Exception {
        java.util.Set<java.lang.String> ids = new java.util.HashSet<java.lang.String>();
        ids.add("p1");
        ids.add("p2");
        org.apache.ambari.view.UnsupportedPropertyException exception = new org.apache.ambari.view.UnsupportedPropertyException("type", ids);
        org.junit.Assert.assertEquals(ids, exception.getPropertyIds());
    }
}