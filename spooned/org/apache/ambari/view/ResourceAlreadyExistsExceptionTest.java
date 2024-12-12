package org.apache.ambari.view;
public class ResourceAlreadyExistsExceptionTest {
    @org.junit.Test
    public void testGetResourceId() throws java.lang.Exception {
        org.apache.ambari.view.ResourceAlreadyExistsException exception = new org.apache.ambari.view.ResourceAlreadyExistsException("id");
        org.junit.Assert.assertEquals("id", exception.getResourceId());
    }
}