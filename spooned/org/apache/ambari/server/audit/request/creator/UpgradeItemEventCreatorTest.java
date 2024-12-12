package org.apache.ambari.server.audit.request.creator;
public class UpgradeItemEventCreatorTest extends org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase {
    @org.junit.Test
    public void postTest() {
        org.apache.ambari.server.audit.request.eventcreator.UpgradeItemEventCreator creator = new org.apache.ambari.server.audit.request.eventcreator.UpgradeItemEventCreator();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.UpgradeItemResourceProvider.UPGRADE_ITEM_STAGE_ID, "1");
        properties.put(org.apache.ambari.server.controller.internal.UpgradeItemResourceProvider.UPGRADE_REQUEST_ID, "9");
        properties.put(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyId("UpgradeItem", "status"), "Status");
        org.apache.ambari.server.api.services.Request request = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createRequest(org.apache.ambari.server.api.services.Request.Type.PUT, org.apache.ambari.server.controller.spi.Resource.Type.UpgradeItem, properties, null);
        org.apache.ambari.server.api.services.Result result = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createResult(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK));
        org.apache.ambari.server.audit.event.AuditEvent event = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.getEvent(creator, request, result);
        java.lang.String actual = event.getAuditMessage();
        java.lang.String expected = ("User(" + org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName) + "), RemoteIp(1.2.3.4), Operation(Action confirmation by the user), RequestType(PUT), url(http://example.com:8080/api/v1/test), ResultStatus(200 OK), Stage id(1), Status(Status), Request id(9)";
        junit.framework.Assert.assertTrue("Class mismatch", event instanceof org.apache.ambari.server.audit.event.request.UpdateUpgradeItemRequestAuditEvent);
        junit.framework.Assert.assertEquals(expected, actual);
        junit.framework.Assert.assertTrue(actual.contains(org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName));
    }
}