package org.apache.ambari.server.controller.internal;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class RequestScheduleResourceProviderTest {
    org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider getResourceProvider(org.apache.ambari.server.controller.AmbariManagementController managementController) {
        org.apache.ambari.server.controller.spi.Resource.Type type = org.apache.ambari.server.controller.spi.Resource.Type.RequestSchedule;
        return ((org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider) (org.apache.ambari.server.controller.internal.AbstractControllerResourceProvider.getResourceProvider(type, managementController)));
    }

    @org.junit.Test
    public void testCreateRequestSchedule() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.RequestStatusResponse response = EasyMock.createNiceMock(org.apache.ambari.server.controller.RequestStatusResponse.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.scheduler.RequestExecutionFactory executionFactory = EasyMock.createNiceMock(org.apache.ambari.server.state.scheduler.RequestExecutionFactory.class);
        org.apache.ambari.server.state.scheduler.RequestExecution requestExecution = EasyMock.createNiceMock(org.apache.ambari.server.state.scheduler.RequestExecution.class);
        org.apache.ambari.server.scheduler.ExecutionScheduleManager executionScheduleManager = EasyMock.createNiceMock(org.apache.ambari.server.scheduler.ExecutionScheduleManager.class);
        EasyMock.expect(clusters.getCluster("Cluster100")).andReturn(cluster).anyTimes();
        EasyMock.expect(managementController.getClusters()).andReturn(clusters);
        EasyMock.expect(managementController.getExecutionScheduleManager()).andReturn(executionScheduleManager).anyTimes();
        EasyMock.expect(managementController.getRequestExecutionFactory()).andReturn(executionFactory);
        EasyMock.expect(managementController.getAuthName()).andReturn("admin").anyTimes();
        EasyMock.expect(managementController.getAuthId()).andReturn(1).anyTimes();
        org.easymock.Capture<org.apache.ambari.server.state.Cluster> clusterCapture = org.easymock.EasyMock.newCapture();
        org.easymock.Capture<org.apache.ambari.server.state.scheduler.Batch> batchCapture = org.easymock.EasyMock.newCapture();
        org.easymock.Capture<org.apache.ambari.server.state.scheduler.Schedule> scheduleCapture = org.easymock.EasyMock.newCapture();
        EasyMock.expect(executionFactory.createNew(EasyMock.capture(clusterCapture), EasyMock.capture(batchCapture), EasyMock.capture(scheduleCapture))).andReturn(requestExecution);
        EasyMock.replay(managementController, clusters, cluster, executionFactory, requestExecution, response, executionScheduleManager);
        org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider resourceProvider = getResourceProvider(managementController);
        java.util.Set<java.util.Map<java.lang.String, java.lang.Object>> propertySet = new java.util.LinkedHashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.CLUSTER_NAME, "Cluster100");
        properties.put(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.DESCRIPTION, "some description");
        properties.put(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.DAY_OF_WEEK, "MON");
        properties.put(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.MINUTES, "2");
        properties.put(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.END_TIME, "2013-11-18T14:29:29-08:00");
        properties.put(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.DAYS_OF_MONTH, "*");
        java.util.HashSet<java.util.Map<java.lang.String, java.lang.Object>> batch = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> batchSettings = new java.util.HashMap<>();
        batchSettings.put(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.BATCH_SEPARATION_IN_SECONDS, "15");
        java.util.Map<java.lang.String, java.lang.Object> batchRequests = new java.util.HashMap<>();
        java.util.HashSet<java.util.Map<java.lang.String, java.lang.Object>> requestSet = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> request1 = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.Object> request2 = new java.util.HashMap<>();
        request1.put(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.TYPE, org.apache.ambari.server.state.scheduler.BatchRequest.Type.PUT.name());
        request1.put(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.ORDER_ID, "20");
        request1.put(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.URI, "SomeUpdateUri");
        request1.put(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.BODY, "data1");
        request2.put(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.TYPE, org.apache.ambari.server.state.scheduler.BatchRequest.Type.DELETE.name());
        request2.put(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.ORDER_ID, "22");
        request2.put(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.URI, "SomeDeleteUri");
        requestSet.add(request1);
        requestSet.add(request2);
        batchRequests.put(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.REQUESTS, requestSet);
        batch.add(batchSettings);
        batch.add(batchRequests);
        properties.put(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.BATCH, batch);
        propertySet.add(properties);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getCreateRequest(propertySet, null);
        resourceProvider.createResources(request);
        EasyMock.verify(managementController, clusters, cluster, executionFactory, requestExecution, response, executionScheduleManager);
        java.util.List<org.apache.ambari.server.state.scheduler.BatchRequest> testRequests = batchCapture.getValue().getBatchRequests();
        junit.framework.Assert.assertNotNull(testRequests);
        org.apache.ambari.server.state.scheduler.BatchRequest deleteReq = null;
        org.apache.ambari.server.state.scheduler.BatchRequest putReq = null;
        for (org.apache.ambari.server.state.scheduler.BatchRequest testBatchRequest : testRequests) {
            if (testBatchRequest.getType().equals(org.apache.ambari.server.state.scheduler.BatchRequest.Type.DELETE.name())) {
                deleteReq = testBatchRequest;
            } else {
                putReq = testBatchRequest;
            }
        }
        junit.framework.Assert.assertNotNull(deleteReq);
        junit.framework.Assert.assertNotNull(putReq);
        junit.framework.Assert.assertEquals("data1", putReq.getBody());
        junit.framework.Assert.assertNull(deleteReq.getBody());
    }

    @org.junit.Test
    public void testUpdateRequestSchedule() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.RequestStatusResponse response = EasyMock.createNiceMock(org.apache.ambari.server.controller.RequestStatusResponse.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        final org.apache.ambari.server.state.scheduler.RequestExecution requestExecution = EasyMock.createNiceMock(org.apache.ambari.server.state.scheduler.RequestExecution.class);
        org.apache.ambari.server.controller.RequestScheduleResponse requestScheduleResponse = EasyMock.createNiceMock(org.apache.ambari.server.controller.RequestScheduleResponse.class);
        org.apache.ambari.server.scheduler.ExecutionScheduleManager executionScheduleManager = EasyMock.createNiceMock(org.apache.ambari.server.scheduler.ExecutionScheduleManager.class);
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(clusters.getCluster("Cluster100")).andReturn(cluster).anyTimes();
        EasyMock.expect(managementController.getAuthName()).andReturn("admin").anyTimes();
        EasyMock.expect(managementController.getAuthId()).andReturn(1).anyTimes();
        EasyMock.expect(managementController.getExecutionScheduleManager()).andReturn(executionScheduleManager).anyTimes();
        EasyMock.expect(requestExecution.getId()).andReturn(25L).anyTimes();
        EasyMock.expect(requestExecution.convertToResponse()).andReturn(requestScheduleResponse).anyTimes();
        EasyMock.expect(requestExecution.convertToResponseWithBody()).andReturn(requestScheduleResponse).anyTimes();
        EasyMock.expect(requestScheduleResponse.getId()).andReturn(25L).anyTimes();
        EasyMock.expect(requestScheduleResponse.getClusterName()).andReturn("Cluster100").anyTimes();
        EasyMock.expect(cluster.getAllRequestExecutions()).andStubAnswer(new org.easymock.IAnswer<java.util.Map<java.lang.Long, org.apache.ambari.server.state.scheduler.RequestExecution>>() {
            @java.lang.Override
            public java.util.Map<java.lang.Long, org.apache.ambari.server.state.scheduler.RequestExecution> answer() throws java.lang.Throwable {
                java.util.Map<java.lang.Long, org.apache.ambari.server.state.scheduler.RequestExecution> requestExecutionMap = new java.util.HashMap<>();
                requestExecutionMap.put(requestExecution.getId(), requestExecution);
                return requestExecutionMap;
            }
        });
        EasyMock.replay(managementController, clusters, cluster, requestExecution, response, requestScheduleResponse, executionScheduleManager);
        org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider resourceProvider = getResourceProvider(managementController);
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.CLUSTER_NAME, "Cluster100");
        properties.put(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.DESCRIPTION, "some description");
        properties.put(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.DAY_OF_WEEK, "MON");
        properties.put(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.MINUTES, "2");
        properties.put(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.END_TIME, "2013-11-18T14:29:29-08:00");
        properties.put(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.DAYS_OF_MONTH, "*");
        java.util.HashSet<java.util.Map<java.lang.String, java.lang.Object>> batch = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> batchSettings = new java.util.HashMap<>();
        batchSettings.put(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.BATCH_SEPARATION_IN_SECONDS, "15");
        java.util.Map<java.lang.String, java.lang.Object> batchRequests = new java.util.HashMap<>();
        java.util.HashSet<java.util.Map<java.lang.String, java.lang.Object>> requestSet = new java.util.HashSet<>();
        java.util.Map<java.lang.String, java.lang.Object> request1 = new java.util.HashMap<>();
        java.util.Map<java.lang.String, java.lang.Object> request2 = new java.util.HashMap<>();
        request1.put(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.TYPE, org.apache.ambari.server.state.scheduler.BatchRequest.Type.PUT.name());
        request1.put(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.ORDER_ID, "20");
        request1.put(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.URI, "SomeUpdateUri");
        request1.put(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.BODY, "data1");
        request2.put(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.TYPE, org.apache.ambari.server.state.scheduler.BatchRequest.Type.DELETE.name());
        request2.put(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.ORDER_ID, "22");
        request2.put(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.URI, "SomeDeleteUri");
        requestSet.add(request1);
        requestSet.add(request2);
        batchRequests.put(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.REQUESTS, requestSet);
        batch.add(batchSettings);
        batch.add(batchRequests);
        properties.put(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.BATCH, batch);
        java.util.Map<java.lang.String, java.lang.String> mapRequestProps = new java.util.HashMap<>();
        mapRequestProps.put("context", "Called from a test");
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getUpdateRequest(properties, mapRequestProps);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.CLUSTER_NAME).equals("Cluster100").and().property(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.ID).equals(25L).toPredicate();
        resourceProvider.updateResources(request, predicate);
        EasyMock.verify(managementController, clusters, cluster, requestExecution, response, requestScheduleResponse, executionScheduleManager);
    }

    @org.junit.Test
    public void testGetRequestSchedule() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.controller.RequestStatusResponse response = EasyMock.createNiceMock(org.apache.ambari.server.controller.RequestStatusResponse.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        final org.apache.ambari.server.state.scheduler.RequestExecution requestExecution = EasyMock.createNiceMock(org.apache.ambari.server.state.scheduler.RequestExecution.class);
        org.apache.ambari.server.controller.RequestScheduleResponse requestScheduleResponse = EasyMock.createNiceMock(org.apache.ambari.server.controller.RequestScheduleResponse.class);
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(clusters.getCluster("Cluster100")).andReturn(cluster).anyTimes();
        EasyMock.expect(managementController.getAuthName()).andReturn("admin").anyTimes();
        EasyMock.expect(requestExecution.getId()).andReturn(25L).anyTimes();
        EasyMock.expect(requestExecution.getStatus()).andReturn(org.apache.ambari.server.state.scheduler.RequestExecution.Status.SCHEDULED.name()).anyTimes();
        EasyMock.expect(requestExecution.convertToResponse()).andReturn(requestScheduleResponse).anyTimes();
        EasyMock.expect(requestExecution.convertToResponseWithBody()).andReturn(requestScheduleResponse).anyTimes();
        EasyMock.expect(requestScheduleResponse.getId()).andReturn(25L).anyTimes();
        EasyMock.expect(requestScheduleResponse.getClusterName()).andReturn("Cluster100").anyTimes();
        EasyMock.expect(cluster.getAllRequestExecutions()).andStubAnswer(new org.easymock.IAnswer<java.util.Map<java.lang.Long, org.apache.ambari.server.state.scheduler.RequestExecution>>() {
            @java.lang.Override
            public java.util.Map<java.lang.Long, org.apache.ambari.server.state.scheduler.RequestExecution> answer() throws java.lang.Throwable {
                java.util.Map<java.lang.Long, org.apache.ambari.server.state.scheduler.RequestExecution> requestExecutionMap = new java.util.HashMap<>();
                requestExecutionMap.put(requestExecution.getId(), requestExecution);
                return requestExecutionMap;
            }
        });
        EasyMock.replay(managementController, clusters, cluster, requestExecution, response, requestScheduleResponse);
        org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider resourceProvider = getResourceProvider(managementController);
        java.util.Map<java.lang.String, java.lang.Object> properties = new java.util.LinkedHashMap<>();
        properties.put(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.CLUSTER_NAME, "Cluster100");
        properties.put(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.DESCRIPTION, "some description");
        java.util.Set<java.lang.String> propertyIds = new java.util.HashSet<>();
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.CLUSTER_NAME);
        propertyIds.add(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.ID);
        org.apache.ambari.server.controller.spi.Request request = org.apache.ambari.server.controller.utilities.PropertyHelper.getReadRequest(propertyIds);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.CLUSTER_NAME).equals("Cluster100").and().property(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.ID).equals(25L).toPredicate();
        java.util.Set<org.apache.ambari.server.controller.spi.Resource> resources = resourceProvider.getResources(request, predicate);
        junit.framework.Assert.assertEquals(1, resources.size());
        junit.framework.Assert.assertEquals(25L, resources.iterator().next().getPropertyValue(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.ID));
        predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.CLUSTER_NAME).equals("Cluster100").toPredicate();
        resources = resourceProvider.getResources(request, predicate);
        junit.framework.Assert.assertEquals(1, resources.size());
        junit.framework.Assert.assertEquals(25L, resources.iterator().next().getPropertyValue(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.ID));
        EasyMock.verify(managementController, clusters, cluster, requestExecution, response, requestScheduleResponse);
    }

    @org.junit.Test
    public void testDeleteRequestSchedule() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariManagementController managementController = EasyMock.createMock(org.apache.ambari.server.controller.AmbariManagementController.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.scheduler.RequestExecution requestExecution = EasyMock.createNiceMock(org.apache.ambari.server.state.scheduler.RequestExecution.class);
        org.apache.ambari.server.scheduler.ExecutionScheduleManager executionScheduleManager = EasyMock.createNiceMock(org.apache.ambari.server.scheduler.ExecutionScheduleManager.class);
        java.util.Map<java.lang.Long, org.apache.ambari.server.state.scheduler.RequestExecution> requestExecutionMap = new java.util.HashMap<>();
        requestExecutionMap.put(1L, requestExecution);
        EasyMock.expect(managementController.getAuthName()).andReturn("admin").anyTimes();
        EasyMock.expect(managementController.getClusters()).andReturn(clusters).anyTimes();
        EasyMock.expect(managementController.getExecutionScheduleManager()).andReturn(executionScheduleManager).anyTimes();
        EasyMock.expect(clusters.getCluster("Cluster100")).andReturn(cluster).anyTimes();
        EasyMock.expect(cluster.getAllRequestExecutions()).andReturn(requestExecutionMap);
        EasyMock.replay(managementController, clusters, cluster, executionScheduleManager, requestExecution);
        org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider resourceProvider = getResourceProvider(managementController);
        org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver observer = new org.apache.ambari.server.controller.internal.AbstractResourceProviderTest.TestObserver();
        ((org.apache.ambari.server.controller.internal.ObservableResourceProvider) (resourceProvider)).addObserver(observer);
        org.apache.ambari.server.controller.spi.Predicate predicate = new org.apache.ambari.server.controller.utilities.PredicateBuilder().property(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.CLUSTER_NAME).equals("Cluster100").and().property(org.apache.ambari.server.controller.internal.RequestScheduleResourceProvider.ID).equals(1L).toPredicate();
        resourceProvider.deleteResources(new org.apache.ambari.server.controller.internal.RequestImpl(null, null, null, null), predicate);
        org.apache.ambari.server.controller.internal.ResourceProviderEvent lastEvent = observer.getLastEvent();
        junit.framework.Assert.assertNotNull(lastEvent);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.spi.Resource.Type.RequestSchedule, lastEvent.getResourceType());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.controller.internal.ResourceProviderEvent.Type.Delete, lastEvent.getType());
        junit.framework.Assert.assertEquals(predicate, lastEvent.getPredicate());
        junit.framework.Assert.assertNull(lastEvent.getRequest());
        EasyMock.verify(managementController, clusters, cluster, executionScheduleManager, requestExecution);
    }
}