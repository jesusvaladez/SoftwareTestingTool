package org.apache.ambari.server.api.services.stackadvisor;
public class StackAdvisorExceptionTest {
    @org.junit.Test
    public void testCreateFromString() {
        java.lang.String message = "message";
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException e = new org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException(message);
        org.junit.Assert.assertEquals(message, e.getMessage());
    }

    @org.junit.Test
    public void testCreateFromException() {
        java.lang.String message = "message";
        java.lang.Exception e = new java.lang.Exception("another message");
        org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException sae = new org.apache.ambari.server.api.services.stackadvisor.StackAdvisorException(message, e);
        org.junit.Assert.assertEquals(message, sae.getMessage());
    }
}