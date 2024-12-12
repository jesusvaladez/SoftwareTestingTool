package org.apache.ambari.server.state;
import com.google.inject.persist.Transactional;
public class RequestExecutionTest {
    private com.google.inject.Injector injector;

    private org.apache.ambari.server.state.Clusters clusters;

    private org.apache.ambari.server.state.Cluster cluster;

    private java.lang.String clusterName;

    private org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo;

    private org.apache.ambari.server.state.scheduler.RequestExecutionFactory requestExecutionFactory;

    private org.apache.ambari.server.orm.dao.RequestScheduleDAO requestScheduleDAO;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        metaInfo = injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        requestExecutionFactory = injector.getInstance(org.apache.ambari.server.state.scheduler.RequestExecutionFactory.class);
        requestScheduleDAO = injector.getInstance(org.apache.ambari.server.orm.dao.RequestScheduleDAO.class);
        clusterName = "foo";
        clusters.addCluster(clusterName, new org.apache.ambari.server.state.StackId("HDP-0.1"));
        cluster = clusters.getCluster(clusterName);
        junit.framework.Assert.assertNotNull(cluster);
        clusters.addHost("h1");
        clusters.addHost("h2");
        clusters.addHost("h3");
        junit.framework.Assert.assertNotNull(clusters.getHost("h1"));
        junit.framework.Assert.assertNotNull(clusters.getHost("h2"));
        junit.framework.Assert.assertNotNull(clusters.getHost("h3"));
    }

    @org.junit.After
    public void teardown() throws java.lang.Exception {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    @com.google.inject.persist.Transactional
    org.apache.ambari.server.state.scheduler.RequestExecution createRequestSchedule() throws java.lang.Exception {
        org.apache.ambari.server.state.scheduler.Batch batches = new org.apache.ambari.server.state.scheduler.Batch();
        org.apache.ambari.server.state.scheduler.Schedule schedule = new org.apache.ambari.server.state.scheduler.Schedule();
        org.apache.ambari.server.state.scheduler.BatchSettings batchSettings = new org.apache.ambari.server.state.scheduler.BatchSettings();
        batchSettings.setTaskFailureToleranceLimit(10);
        batchSettings.setTaskFailureToleranceLimitPerBatch(2);
        batches.setBatchSettings(batchSettings);
        java.util.List<org.apache.ambari.server.state.scheduler.BatchRequest> batchRequests = new java.util.ArrayList<>();
        org.apache.ambari.server.state.scheduler.BatchRequest batchRequest1 = new org.apache.ambari.server.state.scheduler.BatchRequest();
        batchRequest1.setOrderId(10L);
        batchRequest1.setType(org.apache.ambari.server.state.scheduler.BatchRequest.Type.DELETE);
        batchRequest1.setUri("testUri1");
        org.apache.ambari.server.state.scheduler.BatchRequest batchRequest2 = new org.apache.ambari.server.state.scheduler.BatchRequest();
        batchRequest2.setOrderId(12L);
        batchRequest2.setType(org.apache.ambari.server.state.scheduler.BatchRequest.Type.POST);
        batchRequest2.setUri("testUri2");
        batchRequest2.setBody("testBody");
        batchRequests.add(batchRequest1);
        batchRequests.add(batchRequest2);
        batches.getBatchRequests().addAll(batchRequests);
        schedule.setMinutes("10");
        schedule.setEndTime("2014-01-01 00:00:00");
        org.apache.ambari.server.state.scheduler.RequestExecution requestExecution = requestExecutionFactory.createNew(cluster, batches, schedule);
        requestExecution.setStatus(org.apache.ambari.server.state.scheduler.RequestExecution.Status.SCHEDULED);
        requestExecution.setDescription("Test Schedule");
        requestExecution.persist();
        cluster.addRequestExecution(requestExecution);
        return requestExecution;
    }

    @org.junit.Test
    public void testCreateRequestSchedule() throws java.lang.Exception {
        org.apache.ambari.server.state.scheduler.RequestExecution requestExecution = createRequestSchedule();
        junit.framework.Assert.assertNotNull(requestExecution);
        org.apache.ambari.server.orm.entities.RequestScheduleEntity scheduleEntity = requestScheduleDAO.findById(requestExecution.getId());
        junit.framework.Assert.assertNotNull(scheduleEntity);
        junit.framework.Assert.assertEquals(requestExecution.getBatch().getBatchSettings().getTaskFailureToleranceLimit(), scheduleEntity.getBatchTolerationLimit());
        junit.framework.Assert.assertEquals(requestExecution.getBatch().getBatchSettings().getTaskFailureToleranceLimitPerBatch(), scheduleEntity.getBatchTolerationLimitPerBatch());
        junit.framework.Assert.assertEquals(scheduleEntity.getRequestScheduleBatchRequestEntities().size(), 2);
        java.util.Collection<org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity> batchRequestEntities = scheduleEntity.getRequestScheduleBatchRequestEntities();
        junit.framework.Assert.assertNotNull(batchRequestEntities);
        org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity reqEntity1 = null;
        org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity reqEntity2 = null;
        for (org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity reqEntity : batchRequestEntities) {
            if (reqEntity.getRequestUri().equals("testUri1")) {
                reqEntity1 = reqEntity;
            } else if (reqEntity.getRequestUri().equals("testUri2")) {
                reqEntity2 = reqEntity;
            }
        }
        junit.framework.Assert.assertNotNull(reqEntity1);
        junit.framework.Assert.assertNotNull(reqEntity2);
        junit.framework.Assert.assertEquals(java.lang.Long.valueOf(10L), reqEntity1.getBatchId());
        junit.framework.Assert.assertEquals(java.lang.Long.valueOf(12L), reqEntity2.getBatchId());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.scheduler.BatchRequest.Type.DELETE.name(), reqEntity1.getRequestType());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.scheduler.BatchRequest.Type.POST.name(), reqEntity2.getRequestType());
        junit.framework.Assert.assertEquals(requestExecution.getSchedule().getMinutes(), scheduleEntity.getMinutes());
        junit.framework.Assert.assertEquals(requestExecution.getSchedule().getEndTime(), scheduleEntity.getEndTime());
    }

    @org.junit.Test
    public void testUpdateRequestSchedule() throws java.lang.Exception {
        org.apache.ambari.server.state.scheduler.RequestExecution requestExecution = createRequestSchedule();
        junit.framework.Assert.assertNotNull(requestExecution);
        java.lang.Long id = requestExecution.getId();
        org.apache.ambari.server.orm.entities.RequestScheduleEntity scheduleEntity = requestScheduleDAO.findById(id);
        junit.framework.Assert.assertNotNull(scheduleEntity);
        requestExecution = requestExecutionFactory.createExisting(cluster, scheduleEntity);
        org.apache.ambari.server.state.scheduler.Batch batches = new org.apache.ambari.server.state.scheduler.Batch();
        java.util.List<org.apache.ambari.server.state.scheduler.BatchRequest> batchRequests = new java.util.ArrayList<>();
        org.apache.ambari.server.state.scheduler.BatchRequest batchRequest1 = new org.apache.ambari.server.state.scheduler.BatchRequest();
        batchRequest1.setOrderId(10L);
        batchRequest1.setType(org.apache.ambari.server.state.scheduler.BatchRequest.Type.PUT);
        batchRequest1.setUri("testUri3");
        org.apache.ambari.server.state.scheduler.BatchRequest batchRequest2 = new org.apache.ambari.server.state.scheduler.BatchRequest();
        batchRequest2.setOrderId(12L);
        batchRequest2.setType(org.apache.ambari.server.state.scheduler.BatchRequest.Type.POST);
        batchRequest2.setUri("testUri4");
        batchRequest2.setBody("testBody");
        batchRequests.add(batchRequest1);
        batchRequests.add(batchRequest2);
        batches.getBatchRequests().addAll(batchRequests);
        requestExecution.setBatch(batches);
        requestExecution.getSchedule().setHours("11");
        requestExecution.persist();
        scheduleEntity = requestScheduleDAO.findById(id);
        junit.framework.Assert.assertNotNull(scheduleEntity);
        java.util.Collection<org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity> batchRequestEntities = scheduleEntity.getRequestScheduleBatchRequestEntities();
        junit.framework.Assert.assertNotNull(batchRequestEntities);
        org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity reqEntity1 = null;
        org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity reqEntity2 = null;
        for (org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity reqEntity : batchRequestEntities) {
            if (reqEntity.getRequestUri().equals("testUri3")) {
                reqEntity1 = reqEntity;
            } else if (reqEntity.getRequestUri().equals("testUri4")) {
                reqEntity2 = reqEntity;
            }
        }
        junit.framework.Assert.assertNotNull(reqEntity1);
        junit.framework.Assert.assertNotNull(reqEntity2);
        junit.framework.Assert.assertEquals(java.lang.Long.valueOf(10L), reqEntity1.getBatchId());
        junit.framework.Assert.assertEquals(java.lang.Long.valueOf(12L), reqEntity2.getBatchId());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.scheduler.BatchRequest.Type.PUT.name(), reqEntity1.getRequestType());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.scheduler.BatchRequest.Type.POST.name(), reqEntity2.getRequestType());
        junit.framework.Assert.assertEquals("11", scheduleEntity.getHours());
    }

    @org.junit.Test
    public void testGetRequestSchedule() throws java.lang.Exception {
        org.apache.ambari.server.state.scheduler.RequestExecution requestExecution = createRequestSchedule();
        junit.framework.Assert.assertNotNull(requestExecution);
        org.apache.ambari.server.orm.entities.RequestScheduleEntity scheduleEntity = requestScheduleDAO.findById(requestExecution.getId());
        junit.framework.Assert.assertNotNull(scheduleEntity);
        junit.framework.Assert.assertNotNull(cluster.getAllRequestExecutions().get(requestExecution.getId()));
        junit.framework.Assert.assertNotNull(scheduleEntity);
        junit.framework.Assert.assertEquals(requestExecution.getBatch().getBatchSettings().getTaskFailureToleranceLimit(), scheduleEntity.getBatchTolerationLimit());
        junit.framework.Assert.assertEquals(requestExecution.getBatch().getBatchSettings().getTaskFailureToleranceLimitPerBatch(), scheduleEntity.getBatchTolerationLimitPerBatch());
        junit.framework.Assert.assertEquals(scheduleEntity.getRequestScheduleBatchRequestEntities().size(), 2);
        java.util.Collection<org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity> batchRequestEntities = scheduleEntity.getRequestScheduleBatchRequestEntities();
        junit.framework.Assert.assertNotNull(batchRequestEntities);
        org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity reqEntity1 = null;
        org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity reqEntity2 = null;
        for (org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity reqEntity : batchRequestEntities) {
            if (reqEntity.getRequestUri().equals("testUri1")) {
                reqEntity1 = reqEntity;
            } else if (reqEntity.getRequestUri().equals("testUri2")) {
                reqEntity2 = reqEntity;
            }
        }
        junit.framework.Assert.assertNotNull(reqEntity1);
        junit.framework.Assert.assertNotNull(reqEntity2);
        junit.framework.Assert.assertEquals(java.lang.Long.valueOf(10L), reqEntity1.getBatchId());
        junit.framework.Assert.assertEquals(java.lang.Long.valueOf(12L), reqEntity2.getBatchId());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.scheduler.BatchRequest.Type.DELETE.name(), reqEntity1.getRequestType());
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.scheduler.BatchRequest.Type.POST.name(), reqEntity2.getRequestType());
        junit.framework.Assert.assertEquals(requestExecution.getSchedule().getMinutes(), scheduleEntity.getMinutes());
        junit.framework.Assert.assertEquals(requestExecution.getSchedule().getEndTime(), scheduleEntity.getEndTime());
    }

    @org.junit.Test
    public void testDeleteRequestSchedule() throws java.lang.Exception {
        org.apache.ambari.server.state.scheduler.RequestExecution requestExecution = createRequestSchedule();
        junit.framework.Assert.assertNotNull(requestExecution);
        java.lang.Long id = requestExecution.getId();
        cluster.deleteRequestExecution(id);
        junit.framework.Assert.assertNull(requestScheduleDAO.findById(id));
        junit.framework.Assert.assertNull(cluster.getAllRequestExecutions().get(id));
    }

    @org.junit.Test
    public void testGetRequestScheduleWithRequestBody() throws java.lang.Exception {
        org.apache.ambari.server.state.scheduler.RequestExecution requestExecution = createRequestSchedule();
        junit.framework.Assert.assertNotNull(requestExecution);
        junit.framework.Assert.assertNotNull(cluster.getAllRequestExecutions().get(requestExecution.getId()));
        org.apache.ambari.server.orm.entities.RequestScheduleEntity scheduleEntity = requestScheduleDAO.findById(requestExecution.getId());
        junit.framework.Assert.assertNotNull(scheduleEntity);
        requestExecution = requestExecutionFactory.createExisting(cluster, scheduleEntity);
        org.apache.ambari.server.state.scheduler.BatchRequest postBatchRequest = null;
        java.util.List<org.apache.ambari.server.state.scheduler.BatchRequest> batchRequests = requestExecution.getBatch().getBatchRequests();
        junit.framework.Assert.assertNotNull(batchRequests);
        for (org.apache.ambari.server.state.scheduler.BatchRequest batchRequest : batchRequests) {
            if (batchRequest.getType().equals(org.apache.ambari.server.state.scheduler.BatchRequest.Type.POST.name())) {
                postBatchRequest = batchRequest;
            }
        }
        junit.framework.Assert.assertNotNull(postBatchRequest);
        junit.framework.Assert.assertNotNull(postBatchRequest.getBody());
        junit.framework.Assert.assertEquals("testBody", postBatchRequest.getBody());
        org.apache.ambari.server.controller.RequestScheduleResponse requestScheduleResponse = requestExecution.convertToResponseWithBody();
        junit.framework.Assert.assertNotNull(requestScheduleResponse);
        batchRequests = requestExecution.getBatch().getBatchRequests();
        junit.framework.Assert.assertNotNull(batchRequests);
        for (org.apache.ambari.server.state.scheduler.BatchRequest batchRequest : batchRequests) {
            if (batchRequest.getType().equals(org.apache.ambari.server.state.scheduler.BatchRequest.Type.POST.name())) {
                postBatchRequest = batchRequest;
            }
        }
        junit.framework.Assert.assertNotNull(postBatchRequest);
        junit.framework.Assert.assertNotNull(postBatchRequest.getBody());
    }

    @org.junit.Test
    public void testUpdateStatus() throws java.lang.Exception {
        org.apache.ambari.server.state.scheduler.RequestExecution requestExecution = createRequestSchedule();
        junit.framework.Assert.assertNotNull(requestExecution);
        junit.framework.Assert.assertNotNull(cluster.getAllRequestExecutions().get(requestExecution.getId()));
        org.apache.ambari.server.orm.entities.RequestScheduleEntity scheduleEntity = requestScheduleDAO.findById(requestExecution.getId());
        junit.framework.Assert.assertNotNull(scheduleEntity);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.scheduler.RequestExecution.Status.SCHEDULED.name(), scheduleEntity.getStatus());
        requestExecution.updateStatus(org.apache.ambari.server.state.scheduler.RequestExecution.Status.COMPLETED);
        scheduleEntity = requestScheduleDAO.findById(requestExecution.getId());
        junit.framework.Assert.assertNotNull(scheduleEntity);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.scheduler.RequestExecution.Status.COMPLETED.name(), scheduleEntity.getStatus());
    }

    @org.junit.Test
    public void testUpdateBatchRequest() throws java.lang.Exception {
        org.apache.ambari.server.state.scheduler.RequestExecution requestExecution = createRequestSchedule();
        junit.framework.Assert.assertNotNull(requestExecution);
        junit.framework.Assert.assertNotNull(cluster.getAllRequestExecutions().get(requestExecution.getId()));
        org.apache.ambari.server.orm.entities.RequestScheduleEntity scheduleEntity = requestScheduleDAO.findById(requestExecution.getId());
        junit.framework.Assert.assertNotNull(scheduleEntity);
        junit.framework.Assert.assertEquals(org.apache.ambari.server.state.scheduler.RequestExecution.Status.SCHEDULED.name(), scheduleEntity.getStatus());
        java.util.Collection<org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity> batchRequestEntities = scheduleEntity.getRequestScheduleBatchRequestEntities();
        junit.framework.Assert.assertNotNull(batchRequestEntities);
        junit.framework.Assert.assertEquals(2, batchRequestEntities.size());
        org.apache.ambari.server.state.scheduler.BatchRequestResponse batchRequestResponse = new org.apache.ambari.server.state.scheduler.BatchRequestResponse();
        batchRequestResponse.setRequestId(1L);
        batchRequestResponse.setReturnCode(200);
        batchRequestResponse.setReturnMessage("test");
        batchRequestResponse.setStatus("IN_PROGRESS");
        requestExecution.updateBatchRequest(10L, batchRequestResponse, false);
        scheduleEntity = requestScheduleDAO.findById(requestExecution.getId());
        org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity testEntity = null;
        for (org.apache.ambari.server.orm.entities.RequestScheduleBatchRequestEntity entity : scheduleEntity.getRequestScheduleBatchRequestEntities()) {
            if (entity.getBatchId().equals(10L)) {
                testEntity = entity;
            }
        }
        junit.framework.Assert.assertNotNull(testEntity);
        junit.framework.Assert.assertEquals(200, testEntity.getReturnCode().intValue());
        junit.framework.Assert.assertEquals("test", testEntity.getReturnMessage());
        junit.framework.Assert.assertEquals("IN_PROGRESS", testEntity.getRequestStatus());
    }
}