package org.apache.ambari.server.audit.request.creator;
public class RepositoryEventCreatorTest extends org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase {
    @org.junit.Test
    public void postTest() {
        org.apache.ambari.server.audit.request.eventcreator.RepositoryEventCreator creator = new org.apache.ambari.server.audit.request.eventcreator.RepositoryEventCreator();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_REPO_ID_PROPERTY_ID, "Repo1");
        properties.put(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_STACK_NAME_PROPERTY_ID, "StackName");
        properties.put(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_STACK_VERSION_PROPERTY_ID, "1.2-56");
        properties.put(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_OS_TYPE_PROPERTY_ID, "redhat7");
        properties.put(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_BASE_URL_PROPERTY_ID, "http://example.com");
        org.apache.ambari.server.api.services.Request request = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createRequest(org.apache.ambari.server.api.services.Request.Type.POST, org.apache.ambari.server.controller.spi.Resource.Type.Repository, properties, null);
        org.apache.ambari.server.api.services.Result result = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createResult(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK));
        org.apache.ambari.server.audit.event.AuditEvent event = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.getEvent(creator, request, result);
        java.lang.String actual = event.getAuditMessage();
        java.lang.String expected = ("User(" + org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName) + "), RemoteIp(1.2.3.4), Operation(Repository addition), RequestType(POST), url(http://example.com:8080/api/v1/test), ResultStatus(200 OK), Stack(StackName), Stack version(1.2-56), OS(redhat7), Repo id(Repo1), Base URL(http://example.com)";
        junit.framework.Assert.assertTrue("Class mismatch", event instanceof org.apache.ambari.server.audit.event.request.AddRepositoryRequestAuditEvent);
        junit.framework.Assert.assertEquals(expected, actual);
        junit.framework.Assert.assertTrue(actual.contains(org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName));
    }

    @org.junit.Test
    public void putTest() {
        org.apache.ambari.server.audit.request.eventcreator.RepositoryEventCreator creator = new org.apache.ambari.server.audit.request.eventcreator.RepositoryEventCreator();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_REPO_ID_PROPERTY_ID, "Repo1");
        properties.put(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_STACK_NAME_PROPERTY_ID, "StackName");
        properties.put(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_STACK_VERSION_PROPERTY_ID, "1.2-56");
        properties.put(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_OS_TYPE_PROPERTY_ID, "redhat7");
        properties.put(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_BASE_URL_PROPERTY_ID, "http://example.com");
        org.apache.ambari.server.api.services.Request request = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createRequest(org.apache.ambari.server.api.services.Request.Type.PUT, org.apache.ambari.server.controller.spi.Resource.Type.Repository, properties, null);
        org.apache.ambari.server.api.services.Result result = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createResult(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK));
        org.apache.ambari.server.audit.event.AuditEvent event = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.getEvent(creator, request, result);
        java.lang.String actual = event.getAuditMessage();
        java.lang.String expected = ("User(" + org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName) + "), RemoteIp(1.2.3.4), Operation(Repository update), RequestType(PUT), url(http://example.com:8080/api/v1/test), ResultStatus(200 OK), Stack(StackName), Stack version(1.2-56), OS(redhat7), Repo id(Repo1), Base URL(http://example.com)";
        junit.framework.Assert.assertTrue("Class mismatch", event instanceof org.apache.ambari.server.audit.event.request.UpdateRepositoryRequestAuditEvent);
        junit.framework.Assert.assertEquals(expected, actual);
        junit.framework.Assert.assertTrue(actual.contains(org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName));
    }
}