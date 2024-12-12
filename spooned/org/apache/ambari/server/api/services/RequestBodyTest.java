package org.apache.ambari.server.api.services;
public class RequestBodyTest {
    @org.junit.Test
    public void testSetGetQueryString() {
        org.apache.ambari.server.api.services.RequestBody body = new org.apache.ambari.server.api.services.RequestBody();
        org.junit.Assert.assertNull(body.getQueryString());
        body.setQueryString("foo=bar");
        org.junit.Assert.assertEquals("foo=bar", body.getQueryString());
    }

    @org.junit.Test
    public void testSetGetPartialResponseFields() {
        org.apache.ambari.server.api.services.RequestBody body = new org.apache.ambari.server.api.services.RequestBody();
        org.junit.Assert.assertNull(body.getPartialResponseFields());
        body.setPartialResponseFields("foo,bar");
        org.junit.Assert.assertEquals("foo,bar", body.getPartialResponseFields());
    }

    @org.junit.Test
    public void testAddGetPropertySets() {
        org.apache.ambari.server.api.services.RequestBody body = new org.apache.ambari.server.api.services.RequestBody();
        org.junit.Assert.assertEquals(0, body.getNamedPropertySets().size());
        org.apache.ambari.server.api.services.NamedPropertySet ps = new org.apache.ambari.server.api.services.NamedPropertySet("foo", new java.util.HashMap<>());
        body.addPropertySet(ps);
        org.junit.Assert.assertEquals(1, body.getNamedPropertySets().size());
        org.junit.Assert.assertSame(ps, body.getNamedPropertySets().iterator().next());
    }

    @org.junit.Test
    public void testSetGetBody() {
        org.apache.ambari.server.api.services.RequestBody body = new org.apache.ambari.server.api.services.RequestBody();
        org.junit.Assert.assertNull(body.getBody());
        body.setBody("{\"foo\" : \"value\" }");
        org.junit.Assert.assertEquals("{\"foo\" : \"value\" }", body.getBody());
    }
}