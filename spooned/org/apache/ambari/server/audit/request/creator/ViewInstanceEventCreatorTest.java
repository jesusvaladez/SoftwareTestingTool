package org.apache.ambari.server.audit.request.creator;
public class ViewInstanceEventCreatorTest extends org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase {
    @org.junit.Test
    public void postTest() {
        org.apache.ambari.server.audit.request.eventcreator.ViewInstanceEventCreator creator = new org.apache.ambari.server.audit.request.eventcreator.ViewInstanceEventCreator();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VIEW_NAME, "MyView");
        properties.put(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VERSION, "1.9");
        properties.put(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.INSTANCE_NAME, "MyViewInstance");
        properties.put(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.LABEL, "MyViewLabel");
        properties.put(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.DESCRIPTION, "Test view");
        org.apache.ambari.server.api.services.Request request = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createRequest(org.apache.ambari.server.api.services.Request.Type.POST, org.apache.ambari.server.controller.spi.Resource.Type.ViewInstance, properties, null);
        org.apache.ambari.server.api.services.Result result = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createResult(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK));
        org.apache.ambari.server.audit.event.AuditEvent event = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.getEvent(creator, request, result);
        java.lang.String actual = event.getAuditMessage();
        java.lang.String expected = ("User(" + org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName) + "), RemoteIp(1.2.3.4), Operation(View addition), RequestType(POST), url(http://example.com:8080/api/v1/test), ResultStatus(200 OK), Type(MyView), Version(1.9), Name(MyViewInstance), Display name(MyViewLabel), Description(Test view)";
        junit.framework.Assert.assertTrue("Class mismatch", event instanceof org.apache.ambari.server.audit.event.request.AddViewInstanceRequestAuditEvent);
        junit.framework.Assert.assertEquals(expected, actual);
        junit.framework.Assert.assertTrue(actual.contains(org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName));
    }

    @org.junit.Test
    public void putTest() {
        org.apache.ambari.server.audit.request.eventcreator.ViewInstanceEventCreator creator = new org.apache.ambari.server.audit.request.eventcreator.ViewInstanceEventCreator();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VIEW_NAME, "MyView");
        properties.put(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.VERSION, "1.9");
        properties.put(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.INSTANCE_NAME, "MyViewInstance");
        properties.put(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.LABEL, "MyViewLabel");
        properties.put(org.apache.ambari.server.controller.internal.ViewInstanceResourceProvider.DESCRIPTION, "Test view");
        org.apache.ambari.server.api.services.Request request = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createRequest(org.apache.ambari.server.api.services.Request.Type.PUT, org.apache.ambari.server.controller.spi.Resource.Type.ViewInstance, properties, null);
        org.apache.ambari.server.api.services.Result result = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createResult(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK));
        org.apache.ambari.server.audit.event.AuditEvent event = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.getEvent(creator, request, result);
        java.lang.String actual = event.getAuditMessage();
        java.lang.String expected = ("User(" + org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName) + "), RemoteIp(1.2.3.4), Operation(View change), RequestType(PUT), url(http://example.com:8080/api/v1/test), ResultStatus(200 OK), Type(MyView), Version(1.9), Name(MyViewInstance), Display name(MyViewLabel), Description(Test view)";
        junit.framework.Assert.assertTrue("Class mismatch", event instanceof org.apache.ambari.server.audit.event.request.ChangeViewInstanceRequestAuditEvent);
        junit.framework.Assert.assertEquals(expected, actual);
        junit.framework.Assert.assertTrue(actual.contains(org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName));
    }

    @org.junit.Test
    public void deleteTest() {
        org.apache.ambari.server.audit.request.eventcreator.ViewInstanceEventCreator creator = new org.apache.ambari.server.audit.request.eventcreator.ViewInstanceEventCreator();
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> resource = new java.util.HashMap<>();
        resource.put(org.apache.ambari.server.controller.spi.Resource.Type.View, "MyView");
        resource.put(org.apache.ambari.server.controller.spi.Resource.Type.ViewVersion, "1.2");
        resource.put(org.apache.ambari.server.controller.spi.Resource.Type.ViewInstance, "MyViewInstance");
        org.apache.ambari.server.api.services.Request request = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createRequest(org.apache.ambari.server.api.services.Request.Type.DELETE, org.apache.ambari.server.controller.spi.Resource.Type.ViewInstance, null, resource);
        org.apache.ambari.server.api.services.Result result = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createResult(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK));
        org.apache.ambari.server.audit.event.AuditEvent event = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.getEvent(creator, request, result);
        java.lang.String actual = event.getAuditMessage();
        java.lang.String expected = ("User(" + org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName) + "), RemoteIp(1.2.3.4), Operation(View deletion), RequestType(DELETE), url(http://example.com:8080/api/v1/test), ResultStatus(200 OK), Type(MyView), Version(1.2), Name(MyViewInstance)";
        junit.framework.Assert.assertTrue("Class mismatch", event instanceof org.apache.ambari.server.audit.event.request.DeleteViewInstanceRequestAuditEvent);
        junit.framework.Assert.assertEquals(expected, actual);
        junit.framework.Assert.assertTrue(actual.contains(org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName));
    }
}