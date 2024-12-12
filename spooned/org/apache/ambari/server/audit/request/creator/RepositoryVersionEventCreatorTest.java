package org.apache.ambari.server.audit.request.creator;
public class RepositoryVersionEventCreatorTest extends org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase {
    @org.junit.Test
    public void postTest() {
        org.apache.ambari.server.audit.request.eventcreator.RepositoryVersionEventCreator creator = new org.apache.ambari.server.audit.request.eventcreator.RepositoryVersionEventCreator();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID, "StackName");
        properties.put(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID, "1.9");
        properties.put(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_DISPLAY_NAME_PROPERTY_ID, "MyStack");
        properties.put(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_REPOSITORY_VERSION_PROPERTY_ID, "1.2-56");
        properties.put("operating_systems", createOperatingSystems());
        org.apache.ambari.server.api.services.Request request = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createRequest(org.apache.ambari.server.api.services.Request.Type.POST, org.apache.ambari.server.controller.spi.Resource.Type.RepositoryVersion, properties, null);
        org.apache.ambari.server.api.services.Result result = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createResult(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK));
        org.apache.ambari.server.audit.event.AuditEvent event = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.getEvent(creator, request, result);
        java.lang.String actual = event.getAuditMessage();
        java.lang.String expected = (((((("User(" + org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName) + "), RemoteIp(1.2.3.4), Operation(Repository version addition), RequestType(POST), url(http://example.com:8080/api/v1/test), ResultStatus(200 OK), Stack(StackName), Stack version(1.9), Display name(MyStack), Repo version(1.2-56), Repositories(") + "Operating system: redhat6(") + "Repository ID(2), Repository name(MyRepo6), Base url(http://example6.com))") + "Operating system: redhat7(") + "Repository ID(1), Repository name(MyRepo), Base url(http://example.com))") + ")";
        junit.framework.Assert.assertTrue("Class mismatch", event instanceof org.apache.ambari.server.audit.event.request.AddRepositoryVersionRequestAuditEvent);
        junit.framework.Assert.assertEquals(expected, actual);
        junit.framework.Assert.assertTrue(actual.contains(org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName));
    }

    @org.junit.Test
    public void putTest() {
        org.apache.ambari.server.audit.request.eventcreator.RepositoryVersionEventCreator creator = new org.apache.ambari.server.audit.request.eventcreator.RepositoryVersionEventCreator();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_NAME_PROPERTY_ID, "StackName");
        properties.put(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_STACK_VERSION_PROPERTY_ID, "1.9");
        properties.put(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_DISPLAY_NAME_PROPERTY_ID, "MyStack");
        properties.put(org.apache.ambari.server.controller.internal.RepositoryVersionResourceProvider.REPOSITORY_VERSION_REPOSITORY_VERSION_PROPERTY_ID, "1.2-56");
        properties.put("operating_systems", createOperatingSystems());
        org.apache.ambari.server.api.services.Request request = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createRequest(org.apache.ambari.server.api.services.Request.Type.PUT, org.apache.ambari.server.controller.spi.Resource.Type.RepositoryVersion, properties, null);
        org.apache.ambari.server.api.services.Result result = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createResult(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK));
        org.apache.ambari.server.audit.event.AuditEvent event = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.getEvent(creator, request, result);
        java.lang.String actual = event.getAuditMessage();
        java.lang.String expected = (((((("User(" + org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName) + "), RemoteIp(1.2.3.4), Operation(Repository version change), RequestType(PUT), url(http://example.com:8080/api/v1/test), ResultStatus(200 OK), Stack(StackName), Stack version(1.9), Display name(MyStack), Repo version(1.2-56), Repositories(") + "Operating system: redhat6(") + "Repository ID(2), Repository name(MyRepo6), Base url(http://example6.com))") + "Operating system: redhat7(") + "Repository ID(1), Repository name(MyRepo), Base url(http://example.com))") + ")";
        junit.framework.Assert.assertTrue("Class mismatch", event instanceof org.apache.ambari.server.audit.event.request.ChangeRepositoryVersionRequestAuditEvent);
        junit.framework.Assert.assertEquals(expected, actual);
        junit.framework.Assert.assertTrue(actual.contains(org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName));
    }

    @org.junit.Test
    public void deleteTest() {
        org.apache.ambari.server.audit.request.eventcreator.RepositoryVersionEventCreator creator = new org.apache.ambari.server.audit.request.eventcreator.RepositoryVersionEventCreator();
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> resource = new java.util.HashMap<>();
        resource.put(org.apache.ambari.server.controller.spi.Resource.Type.Stack, "HDP");
        resource.put(org.apache.ambari.server.controller.spi.Resource.Type.StackVersion, "1.9");
        resource.put(org.apache.ambari.server.controller.spi.Resource.Type.RepositoryVersion, "1.2-56");
        org.apache.ambari.server.api.services.Request request = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createRequest(org.apache.ambari.server.api.services.Request.Type.DELETE, org.apache.ambari.server.controller.spi.Resource.Type.RepositoryVersion, null, resource);
        org.apache.ambari.server.api.services.Result result = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createResult(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK));
        org.apache.ambari.server.audit.event.AuditEvent event = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.getEvent(creator, request, result);
        java.lang.String actual = event.getAuditMessage();
        java.lang.String expected = ("User(" + org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName) + "), RemoteIp(1.2.3.4), Operation(Repository version removal), RequestType(DELETE), url(http://example.com:8080/api/v1/test), ResultStatus(200 OK), Stack(HDP), Stack version(1.9), Repo version ID(1.2-56)";
        junit.framework.Assert.assertTrue("Class mismatch", event instanceof org.apache.ambari.server.audit.event.request.DeleteRepositoryVersionRequestAuditEvent);
        junit.framework.Assert.assertEquals(expected, actual);
        junit.framework.Assert.assertTrue(actual.contains(org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName));
    }

    private java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> createOperatingSystems() {
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> operatingSystems = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> operatingSystem = new java.util.HashMap<>();
        operatingSystem.put(org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.OPERATING_SYSTEM_OS_TYPE_PROPERTY_ID, "redhat7");
        java.util.Set<java.util.Map<java.lang.String, java.lang.String>> repositories = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.String> repository = new java.util.HashMap<>();
        repository.put(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_REPO_ID_PROPERTY_ID, "1");
        repository.put(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_REPO_NAME_PROPERTY_ID, "MyRepo");
        repository.put(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_BASE_URL_PROPERTY_ID, "http://example.com");
        repositories.add(repository);
        operatingSystem.put("repositories", repositories);
        java.util.Map<java.lang.String, java.lang.Object> operatingSystem2 = new java.util.HashMap<>();
        operatingSystem2.put(org.apache.ambari.server.controller.internal.OperatingSystemResourceProvider.OPERATING_SYSTEM_OS_TYPE_PROPERTY_ID, "redhat6");
        java.util.Set<java.util.Map<java.lang.String, java.lang.String>> repositories2 = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.String> repository2 = new java.util.HashMap<>();
        repository2.put(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_REPO_ID_PROPERTY_ID, "2");
        repository2.put(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_REPO_NAME_PROPERTY_ID, "MyRepo6");
        repository2.put(org.apache.ambari.server.controller.internal.RepositoryResourceProvider.REPOSITORY_BASE_URL_PROPERTY_ID, "http://example6.com");
        repositories2.add(repository2);
        operatingSystem2.put("repositories", repositories2);
        operatingSystems.add(operatingSystem);
        operatingSystems.add(operatingSystem2);
        return operatingSystems;
    }
}