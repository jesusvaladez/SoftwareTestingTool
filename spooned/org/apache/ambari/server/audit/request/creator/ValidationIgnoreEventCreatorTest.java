package org.apache.ambari.server.audit.request.creator;
public class ValidationIgnoreEventCreatorTest extends org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase {
    @org.junit.Test(expected = java.lang.AssertionError.class)
    public void postTest() {
        org.apache.ambari.server.audit.request.eventcreator.ValidationIgnoreEventCreator creator = new org.apache.ambari.server.audit.request.eventcreator.ValidationIgnoreEventCreator();
        org.apache.ambari.server.api.services.Request request = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createRequest(org.apache.ambari.server.api.services.Request.Type.POST, org.apache.ambari.server.controller.spi.Resource.Type.Validation, null, null);
        org.apache.ambari.server.api.services.Result result = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createResult(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK));
        org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.getEvent(creator, request, result);
    }
}