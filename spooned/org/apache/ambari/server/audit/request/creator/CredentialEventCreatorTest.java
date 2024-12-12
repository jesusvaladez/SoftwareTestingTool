package org.apache.ambari.server.audit.request.creator;
public class CredentialEventCreatorTest extends org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase {
    @org.junit.Test
    public void postTest() {
        org.apache.ambari.server.audit.request.eventcreator.CredentialEventCreator creator = new org.apache.ambari.server.audit.request.eventcreator.CredentialEventCreator();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_CLUSTER_NAME_PROPERTY_ID, "mycluster");
        properties.put(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_TYPE_PROPERTY_ID, "USER");
        properties.put(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_ALIAS_PROPERTY_ID, "Alias");
        properties.put(org.apache.ambari.server.controller.internal.CredentialResourceProvider.CREDENTIAL_PRINCIPAL_PROPERTY_ID, "newuser");
        org.apache.ambari.server.api.services.Request request = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createRequest(org.apache.ambari.server.api.services.Request.Type.POST, org.apache.ambari.server.controller.spi.Resource.Type.Credential, properties, null);
        org.apache.ambari.server.api.services.Result result = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createResult(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK));
        org.apache.ambari.server.audit.event.AuditEvent event = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.getEvent(creator, request, result);
        java.lang.String actual = event.getAuditMessage();
        java.lang.String expected = ("User(" + org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName) + "), RemoteIp(1.2.3.4), Operation(Credential addition), RequestType(POST), url(http://example.com:8080/api/v1/test), ResultStatus(200 OK), Type(USER), Principal(newuser), Alias(Alias), Cluster name(mycluster)";
        junit.framework.Assert.assertTrue("Class mismatch", event instanceof org.apache.ambari.server.audit.event.request.AddCredentialRequestAuditEvent);
        junit.framework.Assert.assertEquals(expected, actual);
        junit.framework.Assert.assertTrue(actual.contains(org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName));
    }
}