package org.apache.ambari.server.audit.request.creator;
public class PrivilegeEventCreatorTest extends org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase {
    @org.junit.Test
    public void postTest() {
        org.apache.ambari.server.audit.request.eventcreator.PrivilegeEventCreator creator = new org.apache.ambari.server.audit.request.eventcreator.PrivilegeEventCreator();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRINCIPAL_TYPE, "USER");
        properties.put(org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PERMISSION_NAME, "Permission2");
        properties.put(org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRINCIPAL_NAME, org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName + "2");
        org.apache.ambari.server.api.services.NamedPropertySet nps = new org.apache.ambari.server.api.services.NamedPropertySet("1", properties);
        org.apache.ambari.server.api.services.Request request = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createRequest(org.apache.ambari.server.api.services.Request.Type.POST, org.apache.ambari.server.controller.spi.Resource.Type.ClusterPrivilege, null, null);
        request.getBody().addPropertySet(nps);
        org.apache.ambari.server.api.services.Result result = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createResult(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK));
        org.apache.ambari.server.audit.event.AuditEvent event = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.getEvent(creator, request, result);
        java.lang.String actual = event.getAuditMessage();
        java.lang.String expected = ((("User(" + org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName) + "), RemoteIp(1.2.3.4), Operation(User role change), RequestType(POST), url(http://example.com:8080/api/v1/test), ResultStatus(200 OK), Role(Permission2), User(") + org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName) + "2)";
        junit.framework.Assert.assertTrue("Class mismatch", event instanceof org.apache.ambari.server.audit.event.request.PrivilegeChangeRequestAuditEvent);
        junit.framework.Assert.assertEquals(expected, actual);
        junit.framework.Assert.assertTrue(actual.contains(org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName));
    }

    @org.junit.Test
    public void putTest() {
        org.apache.ambari.server.audit.request.eventcreator.PrivilegeEventCreator creator = new org.apache.ambari.server.audit.request.eventcreator.PrivilegeEventCreator();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRINCIPAL_TYPE, "USER");
        properties.put(org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PERMISSION_NAME, "Permission1");
        properties.put(org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRINCIPAL_NAME, org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName);
        java.util.Map<java.lang.String, java.lang.Object> properties2 = new java.util.HashMap<>();
        properties2.put(org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRINCIPAL_TYPE, "USER");
        properties2.put(org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PERMISSION_NAME, "Permission2");
        properties2.put(org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRINCIPAL_NAME, org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName + "2");
        java.util.Map<java.lang.String, java.lang.Object> properties3 = new java.util.HashMap<>();
        properties3.put(org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRINCIPAL_TYPE, "GROUP");
        properties3.put(org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PERMISSION_NAME, "Permission1");
        properties3.put(org.apache.ambari.server.controller.internal.PrivilegeResourceProvider.PRINCIPAL_NAME, "testgroup");
        org.apache.ambari.server.api.services.NamedPropertySet nps = new org.apache.ambari.server.api.services.NamedPropertySet("1", properties);
        org.apache.ambari.server.api.services.NamedPropertySet nps2 = new org.apache.ambari.server.api.services.NamedPropertySet("2", properties2);
        org.apache.ambari.server.api.services.NamedPropertySet nps3 = new org.apache.ambari.server.api.services.NamedPropertySet("3", properties3);
        org.apache.ambari.server.api.services.Request request = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createRequest(org.apache.ambari.server.api.services.Request.Type.PUT, org.apache.ambari.server.controller.spi.Resource.Type.ClusterPrivilege, null, null);
        org.apache.ambari.server.api.services.Result result = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createResult(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK));
        request.getBody().addPropertySet(nps);
        request.getBody().addPropertySet(nps2);
        request.getBody().addPropertySet(nps3);
        org.apache.ambari.server.audit.event.AuditEvent event = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.getEvent(creator, request, result);
        java.lang.String actual = event.getAuditMessage();
        java.lang.String expected = (((((((((("User(" + org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName) + "), RemoteIp(1.2.3.4), Operation(Role change), RequestType(PUT), url(http://example.com:8080/api/v1/test), ResultStatus(200 OK), Roles(") + "Permission1: [") + "Users: ") + org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName) + ";") + "Groups: testgroup]") + " Permission2: [") + "Users: ") + org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName) + "2] )";
        junit.framework.Assert.assertTrue("Class mismatch", event instanceof org.apache.ambari.server.audit.event.request.ClusterPrivilegeChangeRequestAuditEvent);
        junit.framework.Assert.assertEquals(expected, actual);
        junit.framework.Assert.assertTrue(actual.contains(org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName));
    }
}