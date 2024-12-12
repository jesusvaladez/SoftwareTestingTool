package org.apache.ambari.server.audit.request.creator;
public class AlertTargetEventCreatorTest extends org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase {
    @org.junit.Test
    public void postTest() {
        org.apache.ambari.server.audit.request.eventcreator.AlertTargetEventCreator creator = new org.apache.ambari.server.audit.request.eventcreator.AlertTargetEventCreator();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_DESCRIPTION, "Target description");
        properties.put(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_NAME, "Target name");
        properties.put(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_NOTIFICATION_TYPE, "NotifType");
        properties.put((org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_PROPERTIES + "/") + org.apache.ambari.server.notifications.dispatchers.EmailDispatcher.JAVAMAIL_FROM_PROPERTY, "email");
        properties.put(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_STATES, java.util.Arrays.asList("S", "T", "A", "T", "E", "S"));
        properties.put(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_GROUPS, java.util.Arrays.asList("G", "R", "P", "S"));
        properties.put((org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_PROPERTIES + "/") + org.apache.ambari.server.state.services.AlertNoticeDispatchService.AMBARI_DISPATCH_RECIPIENTS, java.util.Arrays.asList("a@a.com", "b@b.com", "c@c.com"));
        org.apache.ambari.server.api.services.Request request = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createRequest(org.apache.ambari.server.api.services.Request.Type.POST, org.apache.ambari.server.controller.spi.Resource.Type.AlertTarget, properties, null);
        org.apache.ambari.server.api.services.Result result = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createResult(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK));
        org.apache.ambari.server.audit.event.AuditEvent event = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.getEvent(creator, request, result);
        java.lang.String actual = event.getAuditMessage();
        java.lang.String expected = ("User(" + org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName) + "), RemoteIp(1.2.3.4), Operation(Notification addition), RequestType(POST), url(http://example.com:8080/api/v1/test), ResultStatus(200 OK), Notification name(Target name), Description(Target description), Notification type(NotifType), Group IDs(G, R, P, S), Email from(email), Email to(a@a.com, b@b.com, c@c.com), Alert states(S, T, A, T, E, S)";
        junit.framework.Assert.assertTrue("Class mismatch", event instanceof org.apache.ambari.server.audit.event.request.AddAlertTargetRequestAuditEvent);
        junit.framework.Assert.assertEquals(expected, actual);
        junit.framework.Assert.assertTrue(actual.contains(org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName));
    }

    @org.junit.Test
    public void putTest() {
        org.apache.ambari.server.audit.request.eventcreator.AlertTargetEventCreator creator = new org.apache.ambari.server.audit.request.eventcreator.AlertTargetEventCreator();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_DESCRIPTION, "Target description");
        properties.put(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_NAME, "Target name");
        properties.put(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_NOTIFICATION_TYPE, "NotifType");
        properties.put((org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_PROPERTIES + "/") + org.apache.ambari.server.notifications.dispatchers.EmailDispatcher.JAVAMAIL_FROM_PROPERTY, "email");
        properties.put(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_STATES, java.util.Arrays.asList("S", "T", "A", "T", "E", "S"));
        properties.put(org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_GROUPS, java.util.Arrays.asList("G", "R", "P", "S"));
        properties.put((org.apache.ambari.server.controller.internal.AlertTargetResourceProvider.ALERT_TARGET_PROPERTIES + "/") + org.apache.ambari.server.state.services.AlertNoticeDispatchService.AMBARI_DISPATCH_RECIPIENTS, java.util.Arrays.asList("a@a.com", "b@b.com", "c@c.com"));
        org.apache.ambari.server.api.services.Request request = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createRequest(org.apache.ambari.server.api.services.Request.Type.PUT, org.apache.ambari.server.controller.spi.Resource.Type.AlertTarget, properties, null);
        org.apache.ambari.server.api.services.Result result = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createResult(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK));
        org.apache.ambari.server.audit.event.AuditEvent event = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.getEvent(creator, request, result);
        java.lang.String actual = event.getAuditMessage();
        java.lang.String expected = ("User(" + org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName) + "), RemoteIp(1.2.3.4), Operation(Notification change), RequestType(PUT), url(http://example.com:8080/api/v1/test), ResultStatus(200 OK), Notification name(Target name), Description(Target description), Notification type(NotifType), Group IDs(G, R, P, S), Email from(email), Email to(a@a.com, b@b.com, c@c.com), Alert states(S, T, A, T, E, S)";
        junit.framework.Assert.assertTrue("Class mismatch", event instanceof org.apache.ambari.server.audit.event.request.ChangeAlertTargetRequestAuditEvent);
        junit.framework.Assert.assertEquals(expected, actual);
        junit.framework.Assert.assertTrue(actual.contains(org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName));
    }

    @org.junit.Test
    public void deleteTest() {
        org.apache.ambari.server.audit.request.eventcreator.AlertTargetEventCreator creator = new org.apache.ambari.server.audit.request.eventcreator.AlertTargetEventCreator();
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> resource = new java.util.HashMap<>();
        resource.put(org.apache.ambari.server.controller.spi.Resource.Type.AlertTarget, "888");
        org.apache.ambari.server.api.services.Request request = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createRequest(org.apache.ambari.server.api.services.Request.Type.DELETE, org.apache.ambari.server.controller.spi.Resource.Type.AlertTarget, null, resource);
        org.apache.ambari.server.api.services.Result result = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createResult(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK));
        org.apache.ambari.server.audit.event.AuditEvent event = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.getEvent(creator, request, result);
        java.lang.String actual = event.getAuditMessage();
        java.lang.String expected = ("User(" + org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName) + "), RemoteIp(1.2.3.4), Operation(Notification removal), RequestType(DELETE), url(http://example.com:8080/api/v1/test), ResultStatus(200 OK), Notification ID(888)";
        junit.framework.Assert.assertTrue("Class mismatch", event instanceof org.apache.ambari.server.audit.event.request.DeleteAlertTargetRequestAuditEvent);
        junit.framework.Assert.assertEquals(expected, actual);
        junit.framework.Assert.assertTrue(actual.contains(org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName));
    }
}