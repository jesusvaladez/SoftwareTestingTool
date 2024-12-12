package org.apache.ambari.server.audit.request.creator;
public class UnauthorizedEventCreatorTest extends org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase {
    @org.junit.Test
    public void unauthorizedTest() {
        unauthorizedTest(org.apache.ambari.server.api.services.ResultStatus.STATUS.UNAUTHORIZED);
    }

    @org.junit.Test
    public void forbiddenTest() {
        unauthorizedTest(org.apache.ambari.server.api.services.ResultStatus.STATUS.FORBIDDEN);
    }

    private void unauthorizedTest(org.apache.ambari.server.api.services.ResultStatus.STATUS status) {
        org.apache.ambari.server.audit.request.eventcreator.UnauthorizedEventCreator creator = new org.apache.ambari.server.audit.request.eventcreator.UnauthorizedEventCreator();
        org.apache.ambari.server.api.services.Request request = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createRequest(org.apache.ambari.server.api.services.Request.Type.POST, org.apache.ambari.server.controller.spi.Resource.Type.Service, null, null);
        org.apache.ambari.server.api.services.Result result = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createResult(new org.apache.ambari.server.api.services.ResultStatus(status));
        org.apache.ambari.server.audit.event.AuditEvent event = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.getEvent(creator, request, result);
        java.lang.String actual = event.getAuditMessage();
        java.lang.String expected = ("User(" + org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName) + "), RemoteIp(1.2.3.4), Operation(null), ResourcePath(http://example.com:8080/api/v1/test), Status(Failed), Reason(Access not authorized)";
        junit.framework.Assert.assertTrue("Class mismatch", event instanceof org.apache.ambari.server.audit.event.AccessUnauthorizedAuditEvent);
        junit.framework.Assert.assertEquals(expected, actual);
        junit.framework.Assert.assertTrue(actual.contains(org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName));
    }
}