package org.apache.ambari.server.api.services.views;
public class ViewExternalSubResourceServiceTest {
    @org.junit.Test
    public void testAddResourceService() throws java.lang.Exception {
        org.apache.ambari.server.controller.spi.Resource.Type type = new org.apache.ambari.server.controller.spi.Resource.Type("resource");
        org.apache.ambari.server.orm.entities.ViewInstanceEntity definition = org.apache.ambari.server.orm.entities.ViewInstanceEntityTest.getViewInstanceEntity();
        org.apache.ambari.server.api.services.views.ViewExternalSubResourceService service = new org.apache.ambari.server.api.services.views.ViewExternalSubResourceService(type, definition);
        java.lang.Object fooService = new java.lang.Object();
        service.addResourceService("foo", fooService);
        org.junit.Assert.assertEquals(fooService, service.getResource("foo"));
        try {
            service.getResource("bar");
            org.junit.Assert.fail("Expected IllegalArgumentException for unknown service name.");
        } catch (java.lang.IllegalArgumentException e) {
        }
    }
}