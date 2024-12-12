package org.apache.ambari.view;
public class NoSuchResourceExceptionTest {
    @org.junit.Test
    public void testGetResourceId() throws java.lang.Exception {
        org.apache.ambari.view.NoSuchResourceException exception = new org.apache.ambari.view.NoSuchResourceException("id");
        org.junit.Assert.assertEquals("id", exception.getResourceId());
    }
}