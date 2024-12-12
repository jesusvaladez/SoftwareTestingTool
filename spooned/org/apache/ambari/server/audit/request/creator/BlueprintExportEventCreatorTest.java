package org.apache.ambari.server.audit.request.creator;
public class BlueprintExportEventCreatorTest extends org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase {
    @org.junit.Test
    public void getTest() {
        org.apache.ambari.server.audit.request.eventcreator.BlueprintExportEventCreator creator = new org.apache.ambari.server.audit.request.eventcreator.BlueprintExportEventCreator();
        org.apache.ambari.server.api.services.Request request = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createRequest(org.apache.ambari.server.api.services.Request.Type.GET, org.apache.ambari.server.controller.spi.Resource.Type.Cluster, null, null, "?format=blueprint");
        org.apache.ambari.server.api.services.Result result = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createResult(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK));
        org.apache.ambari.server.audit.event.AuditEvent event = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.getEvent(creator, request, result);
        java.lang.String actual = event.getAuditMessage();
        java.lang.String expected = ("User(" + org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName) + "), RemoteIp(1.2.3.4), Operation(Blueprint export), RequestType(GET), url(http://example.com:8080/api/v1/test?format=blueprint), ResultStatus(200 OK)";
        junit.framework.Assert.assertTrue("Class mismatch", event instanceof org.apache.ambari.server.audit.event.request.BlueprintExportRequestAuditEvent);
        junit.framework.Assert.assertEquals(expected, actual);
        junit.framework.Assert.assertTrue(actual.contains(org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName));
    }
}