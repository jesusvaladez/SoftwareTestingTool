package org.apache.ambari.server.audit.request.creator;
public class ComponentEventCreatorTest extends org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase {
    @org.junit.Test
    public void deleteTest() {
        org.apache.ambari.server.audit.request.eventcreator.ComponentEventCreator creator = new org.apache.ambari.server.audit.request.eventcreator.ComponentEventCreator();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_NAME, "ambari1.example.com");
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> resource = new java.util.HashMap<>();
        resource.put(org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, "MyComponent");
        org.apache.ambari.server.api.services.Request request = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createRequest(org.apache.ambari.server.api.services.Request.Type.DELETE, org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, properties, resource);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultTree = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, null, null);
        addRequestId(resultTree, 1L);
        org.apache.ambari.server.api.services.Result result = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createResult(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK), resultTree);
        org.apache.ambari.server.audit.event.AuditEvent event = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.getEvent(creator, request, result);
        java.lang.String actual = event.getAuditMessage();
        java.lang.String expected = ("User(" + org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName) + "), RemoteIp(1.2.3.4), Operation(Delete component MyComponent), Host name(ambari1.example.com), RequestId(1), Status(Successfully queued)";
        junit.framework.Assert.assertTrue("Class mismatch", event instanceof org.apache.ambari.server.audit.event.request.StartOperationRequestAuditEvent);
        junit.framework.Assert.assertEquals(expected, actual);
        junit.framework.Assert.assertTrue(actual.contains(org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName));
    }

    @org.junit.Test
    public void putForAllHostsTest() {
        allHostsTest(org.apache.ambari.server.api.services.Request.Type.PUT);
    }

    @org.junit.Test
    public void postForAllHostsTest() {
        allHostsTest(org.apache.ambari.server.api.services.Request.Type.POST);
    }

    private void allHostsTest(org.apache.ambari.server.api.services.Request.Type type) {
        org.apache.ambari.server.audit.request.eventcreator.ComponentEventCreator creator = new org.apache.ambari.server.audit.request.eventcreator.ComponentEventCreator();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_NAME, "ambari1.example.com");
        properties.put(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.CLUSTER_NAME, "mycluster");
        properties.put(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.STATE, "STARTED");
        org.apache.ambari.server.api.services.Request request = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createRequest(type, org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, properties, null);
        request.getBody().addRequestInfoProperty(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_LEVEL_ID, "CLUSTER");
        request.getBody().addRequestInfoProperty(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_CLUSTER_ID, "mycluster");
        request.getBody().setQueryString("hostname.in(a,b,c)");
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultTree = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, null, null);
        addRequestId(resultTree, 1L);
        org.apache.ambari.server.api.services.Result result = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createResult(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK), resultTree);
        org.apache.ambari.server.audit.event.AuditEvent event = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.getEvent(creator, request, result);
        java.lang.String actual = event.getAuditMessage();
        java.lang.String expected = ("User(" + org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName) + "), RemoteIp(1.2.3.4), Operation(STARTED: all services on all hosts that matches hostname.in(a,b,c) (mycluster)), Host name(ambari1.example.com), RequestId(1), Status(Successfully queued)";
        junit.framework.Assert.assertTrue("Class mismatch", event instanceof org.apache.ambari.server.audit.event.request.StartOperationRequestAuditEvent);
        junit.framework.Assert.assertEquals(expected, actual);
        junit.framework.Assert.assertTrue(actual.contains(org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName));
    }

    @org.junit.Test
    public void hostTest() {
        org.apache.ambari.server.audit.request.eventcreator.ComponentEventCreator creator = new org.apache.ambari.server.audit.request.eventcreator.ComponentEventCreator();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_NAME, "ambari1.example.com");
        properties.put(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.CLUSTER_NAME, "mycluster");
        properties.put(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.STATE, "STARTED");
        org.apache.ambari.server.api.services.Request request = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createRequest(org.apache.ambari.server.api.services.Request.Type.PUT, org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, properties, null);
        request.getBody().addRequestInfoProperty(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_LEVEL_ID, "HOST");
        request.getBody().addRequestInfoProperty(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_HOST_NAME, "ambari1.example.com");
        request.getBody().addRequestInfoProperty(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_CLUSTER_ID, "mycluster");
        request.getBody().addRequestInfoProperty("query", "host_component.in(MYCOMPONENT,MYCOMPONENT2)");
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultTree = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, null, null);
        addRequestId(resultTree, 1L);
        org.apache.ambari.server.api.services.Result result = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createResult(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK), resultTree);
        org.apache.ambari.server.audit.event.AuditEvent event = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.getEvent(creator, request, result);
        java.lang.String actual = event.getAuditMessage();
        java.lang.String expected = ("User(" + org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName) + "), RemoteIp(1.2.3.4), Operation(STARTED: MYCOMPONENT,MYCOMPONENT2 on ambari1.example.com (mycluster)), Host name(ambari1.example.com), RequestId(1), Status(Successfully queued)";
        junit.framework.Assert.assertTrue("Class mismatch", event instanceof org.apache.ambari.server.audit.event.request.StartOperationRequestAuditEvent);
        junit.framework.Assert.assertEquals(expected, actual);
        junit.framework.Assert.assertTrue(actual.contains(org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName));
    }

    @org.junit.Test
    public void hostComponentTest() {
        org.apache.ambari.server.audit.request.eventcreator.ComponentEventCreator creator = new org.apache.ambari.server.audit.request.eventcreator.ComponentEventCreator();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_NAME, "ambari1.example.com");
        properties.put(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.CLUSTER_NAME, "mycluster");
        properties.put(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.STATE, "STARTED");
        properties.put(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.COMPONENT_NAME, "MYCOMPONENT");
        org.apache.ambari.server.api.services.Request request = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createRequest(org.apache.ambari.server.api.services.Request.Type.PUT, org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, properties, null);
        request.getBody().addRequestInfoProperty(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_LEVEL_ID, "HOST_COMPONENT");
        request.getBody().addRequestInfoProperty(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_SERVICE_ID, "MYSERVICE");
        request.getBody().addRequestInfoProperty(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_HOST_NAME, "ambari1.example.com");
        request.getBody().addRequestInfoProperty(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_CLUSTER_ID, "mycluster");
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultTree = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, null, null);
        addRequestId(resultTree, 1L);
        org.apache.ambari.server.api.services.Result result = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createResult(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK), resultTree);
        org.apache.ambari.server.audit.event.AuditEvent event = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.getEvent(creator, request, result);
        java.lang.String actual = event.getAuditMessage();
        java.lang.String expected = ("User(" + org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName) + "), RemoteIp(1.2.3.4), Operation(STARTED: MYCOMPONENT/MYSERVICE on ambari1.example.com (mycluster)), Host name(ambari1.example.com), RequestId(1), Status(Successfully queued)";
        junit.framework.Assert.assertTrue("Class mismatch", event instanceof org.apache.ambari.server.audit.event.request.StartOperationRequestAuditEvent);
        junit.framework.Assert.assertEquals(expected, actual);
        junit.framework.Assert.assertTrue(actual.contains(org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName));
    }

    @org.junit.Test
    public void maintenanceModeTest() {
        org.apache.ambari.server.audit.request.eventcreator.ComponentEventCreator creator = new org.apache.ambari.server.audit.request.eventcreator.ComponentEventCreator();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_NAME, "ambari1.example.com");
        properties.put(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.MAINTENANCE_STATE, "ON");
        properties.put(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.COMPONENT_NAME, "MYCOMPONENT");
        org.apache.ambari.server.api.services.Request request = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createRequest(org.apache.ambari.server.api.services.Request.Type.PUT, org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, properties, null);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultTree = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, null, null);
        addRequestId(resultTree, 1L);
        org.apache.ambari.server.api.services.Result result = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createResult(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK), resultTree);
        org.apache.ambari.server.audit.event.AuditEvent event = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.getEvent(creator, request, result);
        java.lang.String actual = event.getAuditMessage();
        java.lang.String expected = ("User(" + org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName) + "), RemoteIp(1.2.3.4), Operation(Turn ON Maintenance Mode for MYCOMPONENT), Host name(ambari1.example.com), RequestId(1), Status(Successfully queued)";
        junit.framework.Assert.assertTrue("Class mismatch", event instanceof org.apache.ambari.server.audit.event.request.StartOperationRequestAuditEvent);
        junit.framework.Assert.assertEquals(expected, actual);
        junit.framework.Assert.assertTrue(actual.contains(org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName));
    }

    @org.junit.Test
    public void failureTest() {
        org.apache.ambari.server.audit.request.eventcreator.ComponentEventCreator creator = new org.apache.ambari.server.audit.request.eventcreator.ComponentEventCreator();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.HOST_NAME, "ambari1.example.com");
        properties.put(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.MAINTENANCE_STATE, "ON");
        properties.put(org.apache.ambari.server.controller.internal.HostComponentResourceProvider.COMPONENT_NAME, "MYCOMPONENT");
        org.apache.ambari.server.api.services.Request request = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createRequest(org.apache.ambari.server.api.services.Request.Type.PUT, org.apache.ambari.server.controller.spi.Resource.Type.HostComponent, properties, null);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultTree = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, null, null);
        addRequestId(resultTree, 1L);
        org.apache.ambari.server.api.services.Result result = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createResult(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.BAD_REQUEST, "Failed for testing"), resultTree);
        org.apache.ambari.server.audit.event.AuditEvent event = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.getEvent(creator, request, result);
        java.lang.String actual = event.getAuditMessage();
        java.lang.String expected = ("User(" + org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName) + "), RemoteIp(1.2.3.4), Operation(Turn ON Maintenance Mode for MYCOMPONENT), Host name(ambari1.example.com), RequestId(1), Status(Failed to queue), Reason(Failed for testing)";
        junit.framework.Assert.assertTrue("Class mismatch", event instanceof org.apache.ambari.server.audit.event.request.StartOperationRequestAuditEvent);
        junit.framework.Assert.assertEquals(expected, actual);
        junit.framework.Assert.assertTrue(actual.contains(org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName));
    }

    private void addRequestId(org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultTree, java.lang.Long requestId) {
        org.apache.ambari.server.controller.spi.Resource resource = new org.apache.ambari.server.controller.internal.ResourceImpl(org.apache.ambari.server.controller.spi.Resource.Type.Request);
        resource.addCategory(org.apache.ambari.server.controller.utilities.PropertyHelper.getPropertyCategory(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID));
        resource.setProperty(org.apache.ambari.server.controller.internal.RequestResourceProvider.REQUEST_ID_PROPERTY_ID, requestId);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> requestNode = new org.apache.ambari.server.api.util.TreeNodeImpl<>(resultTree, resource, "request");
        resultTree.addChild(requestNode);
    }
}