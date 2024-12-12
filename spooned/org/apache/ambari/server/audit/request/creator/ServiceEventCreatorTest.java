package org.apache.ambari.server.audit.request.creator;
public class ServiceEventCreatorTest extends org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase {
    @org.junit.Test
    public void deleteTest() {
        org.apache.ambari.server.audit.request.eventcreator.ServiceEventCreator creator = new org.apache.ambari.server.audit.request.eventcreator.ServiceEventCreator();
        java.util.Map<org.apache.ambari.server.controller.spi.Resource.Type, java.lang.String> resource = new java.util.HashMap<>();
        resource.put(org.apache.ambari.server.controller.spi.Resource.Type.Service, "MyService");
        org.apache.ambari.server.api.services.Request request = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createRequest(org.apache.ambari.server.api.services.Request.Type.DELETE, org.apache.ambari.server.controller.spi.Resource.Type.Service, null, resource);
        org.apache.ambari.server.api.services.Result result = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createResult(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK));
        org.apache.ambari.server.audit.event.AuditEvent event = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.getEvent(creator, request, result);
        java.lang.String actual = event.getAuditMessage();
        java.lang.String expected = ("User(" + org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName) + "), RemoteIp(1.2.3.4), Operation(Service deletion), RequestType(DELETE), url(http://example.com:8080/api/v1/test), ResultStatus(200 OK), Service(MyService)";
        junit.framework.Assert.assertTrue("Class mismatch", event instanceof org.apache.ambari.server.audit.event.request.DeleteServiceRequestAuditEvent);
        junit.framework.Assert.assertEquals(expected, actual);
        junit.framework.Assert.assertTrue(actual.contains(org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName));
    }

    @org.junit.Test
    public void putForAllHostsTest() {
        clusterTest(org.apache.ambari.server.api.services.Request.Type.PUT);
    }

    @org.junit.Test
    public void postForAllHostsTest() {
        clusterTest(org.apache.ambari.server.api.services.Request.Type.POST);
    }

    private void clusterTest(org.apache.ambari.server.api.services.Request.Type type) {
        org.apache.ambari.server.audit.request.eventcreator.ServiceEventCreator creator = new org.apache.ambari.server.audit.request.eventcreator.ServiceEventCreator();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_STATE_PROPERTY_ID, "STARTED");
        org.apache.ambari.server.api.services.Request request = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createRequest(type, org.apache.ambari.server.controller.spi.Resource.Type.Service, properties, null);
        request.getBody().addRequestInfoProperty(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_LEVEL_ID, "CLUSTER");
        request.getBody().addRequestInfoProperty(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_CLUSTER_ID, "mycluster");
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultTree = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, null, null);
        addRequestId(resultTree, 1L);
        org.apache.ambari.server.api.services.Result result = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createResult(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK), resultTree);
        org.apache.ambari.server.audit.event.AuditEvent event = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.getEvent(creator, request, result);
        java.lang.String actual = event.getAuditMessage();
        java.lang.String expected = ("User(" + org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName) + "), RemoteIp(1.2.3.4), Operation(STARTED: all services (mycluster)), RequestId(1), Status(Successfully queued)";
        junit.framework.Assert.assertTrue("Class mismatch", event instanceof org.apache.ambari.server.audit.event.request.StartOperationRequestAuditEvent);
        junit.framework.Assert.assertEquals(expected, actual);
        junit.framework.Assert.assertTrue(actual.contains(org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName));
    }

    @org.junit.Test
    public void serviceTest() {
        org.apache.ambari.server.audit.request.eventcreator.ServiceEventCreator creator = new org.apache.ambari.server.audit.request.eventcreator.ServiceEventCreator();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_STATE_PROPERTY_ID, "STARTED");
        properties.put(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_NAME_PROPERTY_ID, "MyService");
        org.apache.ambari.server.api.services.Request request = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createRequest(org.apache.ambari.server.api.services.Request.Type.PUT, org.apache.ambari.server.controller.spi.Resource.Type.Service, properties, null);
        request.getBody().addRequestInfoProperty(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_LEVEL_ID, "SERVICE");
        request.getBody().addRequestInfoProperty(org.apache.ambari.server.controller.internal.RequestOperationLevel.OPERATION_CLUSTER_ID, "mycluster");
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultTree = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, null, null);
        addRequestId(resultTree, 1L);
        org.apache.ambari.server.api.services.Result result = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createResult(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK), resultTree);
        org.apache.ambari.server.audit.event.AuditEvent event = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.getEvent(creator, request, result);
        java.lang.String actual = event.getAuditMessage();
        java.lang.String expected = ("User(" + org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName) + "), RemoteIp(1.2.3.4), Operation(STARTED: MyService (mycluster)), RequestId(1), Status(Successfully queued)";
        junit.framework.Assert.assertTrue("Class mismatch", event instanceof org.apache.ambari.server.audit.event.request.StartOperationRequestAuditEvent);
        junit.framework.Assert.assertEquals(expected, actual);
        junit.framework.Assert.assertTrue(actual.contains(org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName));
    }

    @org.junit.Test
    public void maintenanceModeTest() {
        org.apache.ambari.server.audit.request.eventcreator.ServiceEventCreator creator = new org.apache.ambari.server.audit.request.eventcreator.ServiceEventCreator();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_MAINTENANCE_STATE_PROPERTY_ID, "ON");
        properties.put(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_NAME_PROPERTY_ID, "MyService");
        org.apache.ambari.server.api.services.Request request = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createRequest(org.apache.ambari.server.api.services.Request.Type.PUT, org.apache.ambari.server.controller.spi.Resource.Type.Service, properties, null);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultTree = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, null, null);
        addRequestId(resultTree, 1L);
        org.apache.ambari.server.api.services.Result result = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createResult(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.OK), resultTree);
        org.apache.ambari.server.audit.event.AuditEvent event = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.getEvent(creator, request, result);
        java.lang.String actual = event.getAuditMessage();
        java.lang.String expected = ("User(" + org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName) + "), RemoteIp(1.2.3.4), Operation(Turn ON Maintenance Mode for MyService), RequestId(1), Status(Successfully queued)";
        junit.framework.Assert.assertTrue("Class mismatch", event instanceof org.apache.ambari.server.audit.event.request.StartOperationRequestAuditEvent);
        junit.framework.Assert.assertEquals(expected, actual);
        junit.framework.Assert.assertTrue(actual.contains(org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName));
    }

    @org.junit.Test
    public void failureTest() {
        org.apache.ambari.server.audit.request.eventcreator.ServiceEventCreator creator = new org.apache.ambari.server.audit.request.eventcreator.ServiceEventCreator();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.HashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_MAINTENANCE_STATE_PROPERTY_ID, "ON");
        properties.put(org.apache.ambari.server.controller.internal.ServiceResourceProvider.SERVICE_SERVICE_NAME_PROPERTY_ID, "MyService");
        org.apache.ambari.server.api.services.Request request = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createRequest(org.apache.ambari.server.api.services.Request.Type.PUT, org.apache.ambari.server.controller.spi.Resource.Type.Service, properties, null);
        org.apache.ambari.server.api.util.TreeNode<org.apache.ambari.server.controller.spi.Resource> resultTree = new org.apache.ambari.server.api.util.TreeNodeImpl<>(null, null, null);
        addRequestId(resultTree, 1L);
        org.apache.ambari.server.api.services.Result result = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.createResult(new org.apache.ambari.server.api.services.ResultStatus(org.apache.ambari.server.api.services.ResultStatus.STATUS.BAD_REQUEST, "Failed for testing"), resultTree);
        org.apache.ambari.server.audit.event.AuditEvent event = org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestHelper.getEvent(creator, request, result);
        java.lang.String actual = event.getAuditMessage();
        java.lang.String expected = ("User(" + org.apache.ambari.server.audit.request.creator.AuditEventCreatorTestBase.userName) + "), RemoteIp(1.2.3.4), Operation(Turn ON Maintenance Mode for MyService), RequestId(1), Status(Failed to queue), Reason(Failed for testing)";
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