package org.apache.ambari.server.api.services.parsers;
public class BodyParseExceptionTest {
    @org.junit.Test
    public void testCreateFromString() {
        java.lang.String msg = "some msg";
        org.apache.ambari.server.api.services.parsers.BodyParseException e = new org.apache.ambari.server.api.services.parsers.BodyParseException(msg);
        org.junit.Assert.assertEquals(msg, e.getMessage());
    }

    @org.junit.Test
    public void testCreateFromException() {
        java.lang.Exception e = new java.lang.Exception("test error msg");
        org.apache.ambari.server.api.services.parsers.BodyParseException bpe = new org.apache.ambari.server.api.services.parsers.BodyParseException(e);
        org.junit.Assert.assertEquals("Invalid Request: Malformed Request Body.  An exception occurred parsing the request body: " + e.getMessage(), bpe.getMessage());
    }
}