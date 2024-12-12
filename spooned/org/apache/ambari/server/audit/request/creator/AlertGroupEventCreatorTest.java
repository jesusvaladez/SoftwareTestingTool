package org.apache.ambari.server.audit.request.creator;
public class AlertGroupEventCreatorTest extends org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase {
    @org.junit.Test
    public void postTest() {
        org.apache.ambari.server.audit.request.eventcreator.AlertGroupEventCreator creator = new org.apache.ambari.server.audit.request.eventcreator.AlertGroupEventCreator();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_NAME, "GroupName");
        properties.put(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_DEFINITIONS, java.util.Arrays.asList("D", "E", "F", "S"));
        properties.put(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_TARGETS, java.util.Arrays.asList("T", "G", "T", "S"));
        org.apache.ambari.server.api.services.Request request = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createRequest(org.apache.ambari.server.api.services.Request.Type.POST, org.apache.ambari.server.controller.spi.Resource.Type.AlertGroup, properties, null);
        org.apache.ambari.server.api.services.Result result = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createResult(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK));
        org.apache.ambari.server.audit.event.AuditEvent event = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.getEvent(creator, request, result);
        java.lang.String actual = event.getAuditMessage();
        java.lang.String expected = ("User(" + org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName) + "), RemoteIp(1.2.3.4), Operation(Alert group addition), RequestType(POST), url(http://example.com:8080/api/v1/test), ResultStatus(200 OK), Alert group name(GroupName), Definition IDs(D, E, F, S), Notification IDs(T, G, T, S)";
        junit.framework.Assert.assertTrue("Class mismatch", event instanceof org.apache.ambari.server.audit.event.request.AddAlertGroupRequestAuditEvent);
        junit.framework.Assert.assertEquals(expected, actual);
        junit.framework.Assert.assertTrue(actual.contains(org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName));
    }

    @org.junit.Test
    public void putTest() {
        org.apache.ambari.server.audit.request.eventcreator.AlertGroupEventCreator creator = new org.apache.ambari.server.audit.request.eventcreator.AlertGroupEventCreator();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_NAME, "GroupName");
        properties.put(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_DEFINITIONS, java.util.Arrays.asList("D", "E", "F", "S"));
        properties.put(org.apache.ambari.server.controller.internal.AlertGroupResourceProvider.ALERT_GROUP_TARGETS, java.util.Arrays.asList("T", "G", "T", "S"));
        org.apache.ambari.server.api.services.Request request = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createRequest(org.apache.ambari.server.api.services.Request.Type.PUT, org.apache.ambari.server.controller.spi.Resource.Type.AlertGroup, properties, null);
        org.apache.ambari.server.api.services.Result result = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createResult(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK));
        org.apache.ambari.server.audit.event.AuditEvent event = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.getEvent(creator, request, result);
        java.lang.String actual = event.getAuditMessage();
        java.lang.String expected = ("User(" + org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName) + "), RemoteIp(1.2.3.4), Operation(Alert group change), RequestType(PUT), url(http://example.com:8080/api/v1/test), ResultStatus(200 OK), Alert group name(GroupName), Definition IDs(D, E, F, S), Notification IDs(T, G, T, S)";
        junit.framework.Assert.assertTrue("Class mismatch", event instanceof org.apache.ambari.server.audit.event.request.ChangeAlertGroupRequestAuditEvent);
        junit.framework.Assert.assertEquals(expected, actual);
        junit.framework.Assert.assertTrue(actual.contains(org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName));
    }

    @org.junit.Test
    public void deleteTest() {
        org.apache.ambari.server.audit.request.eventcreator.AlertGroupEventCreator creator = new org.apache.ambari.server.audit.request.eventcreator.AlertGroupEventCreator();
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> resource = new java.util.HashMap<>();
        resource.put(org.apache.ambari.server.controller.spi.Resource.Type.AlertGroup, "999");
        org.apache.ambari.server.api.services.Request request = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createRequest(org.apache.ambari.server.api.services.Request.Type.DELETE, org.apache.ambari.server.controller.spi.Resource.Type.AlertGroup, null, resource);
        org.apache.ambari.server.api.services.Result result = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createResult(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK));
        org.apache.ambari.server.audit.event.AuditEvent event = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.getEvent(creator, request, result);
        java.lang.String actual = event.getAuditMessage();
        java.lang.String expected = ("User(" + org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName) + "), RemoteIp(1.2.3.4), Operation(Alert group removal), RequestType(DELETE), url(http://example.com:8080/api/v1/test), ResultStatus(200 OK), Alert group ID(999)";
        junit.framework.Assert.assertTrue("Class mismatch", event instanceof org.apache.ambari.server.audit.event.request.DeleteAlertGroupRequestAuditEvent);
        junit.framework.Assert.assertEquals(expected, actual);
        junit.framework.Assert.assertTrue(actual.contains(org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName));
    }
}