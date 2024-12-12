package org.apache.ambari.server.scheduler;
import com.google.inject.persist.Transactional;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class ExecutionScheduleManagerTest {
    private static org.apache.ambari.server.state.Clusters clusters;

    private static org.apache.ambari.server.state.Cluster cluster;

    private static java.lang.String clusterName;

    private static com.google.inject.Injector injector;

    private static org.apache.ambari.server.api.services.AmbariMetaInfo metaInfo;

    private static org.apache.ambari.server.scheduler.ExecutionScheduleManager executionScheduleManager;

    private static org.apache.ambari.server.state.scheduler.RequestExecutionFactory requestExecutionFactory;

    private static org.apache.ambari.server.scheduler.ExecutionScheduler executionScheduler;

    private static org.quartz.Scheduler scheduler;

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.class);

    @org.junit.BeforeClass
    public static void setup() throws java.lang.Exception {
        org.apache.ambari.server.orm.InMemoryDefaultTestModule defaultTestModule = new org.apache.ambari.server.orm.InMemoryDefaultTestModule();
        org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.injector = com.google.inject.Guice.createInjector(com.google.inject.util.Modules.override(defaultTestModule).with(new org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.ExecutionSchedulerTestModule()));
        org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.clusters = org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.metaInfo = org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.executionScheduleManager = org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.injector.getInstance(org.apache.ambari.server.scheduler.ExecutionScheduleManager.class);
        org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.executionScheduler = org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.injector.getInstance(org.apache.ambari.server.scheduler.ExecutionScheduler.class);
        org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.requestExecutionFactory = org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.injector.getInstance(org.apache.ambari.server.state.scheduler.RequestExecutionFactory.class);
        org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.clusterName = "c1";
        org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.clusters.addCluster(org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.clusterName, new org.apache.ambari.server.state.StackId("HDP-0.1"));
        org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.cluster = org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.clusters.getCluster(org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.clusterName);
        junit.framework.Assert.assertNotNull(org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.cluster);
        org.junit.Assert.assertThat(org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.executionScheduler, org.hamcrest.CoreMatchers.instanceOf(org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.TestExecutionScheduler.class));
        org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.TestExecutionScheduler testExecutionScheduler = ((org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.TestExecutionScheduler) (org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.executionScheduler));
        org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.scheduler = testExecutionScheduler.getScheduler();
        junit.framework.Assert.assertNotNull(org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.scheduler);
        org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.executionScheduleManager.start();
    }

    @org.junit.AfterClass
    public static void teardown() throws java.lang.Exception {
        org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.executionScheduleManager.stop();
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.injector);
    }

    public static class TestExecutionScheduler extends org.apache.ambari.server.scheduler.ExecutionSchedulerImpl {
        @com.google.inject.Inject
        public TestExecutionScheduler(com.google.inject.Injector injector) {
            super(injector);
            try {
                org.quartz.impl.StdSchedulerFactory factory = new org.quartz.impl.StdSchedulerFactory();
                scheduler = factory.getScheduler();
                org.apache.ambari.server.scheduler.ExecutionSchedulerImpl.isInitialized = true;
            } catch (org.quartz.SchedulerException e) {
                e.printStackTrace();
                throw new java.lang.ExceptionInInitializerError("Unable to instantiate " + "scheduler");
            }
        }

        public org.quartz.Scheduler getScheduler() {
            return scheduler;
        }
    }

    public static class ExecutionSchedulerTestModule implements com.google.inject.Module {
        @java.lang.Override
        public void configure(com.google.inject.Binder binder) {
            binder.bind(org.apache.ambari.server.scheduler.ExecutionScheduler.class).to(org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.TestExecutionScheduler.class);
        }
    }

    @com.google.inject.persist.Transactional
    org.apache.ambari.server.state.scheduler.RequestExecution createRequestExecution(boolean addSchedule) throws java.lang.Exception {
        org.apache.ambari.server.state.scheduler.Batch batches = new org.apache.ambari.server.state.scheduler.Batch();
        org.apache.ambari.server.state.scheduler.Schedule schedule = new org.apache.ambari.server.state.scheduler.Schedule();
        org.apache.ambari.server.state.scheduler.BatchSettings batchSettings = new org.apache.ambari.server.state.scheduler.BatchSettings();
        batchSettings.setTaskFailureToleranceLimit(10);
        batchSettings.setTaskFailureToleranceLimitPerBatch(1);
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
        schedule.setHours("2");
        schedule.setMonth("*");
        schedule.setDaysOfMonth("*");
        schedule.setDayOfWeek("?");
        if (!addSchedule) {
            schedule = null;
        }
        org.apache.ambari.server.state.scheduler.RequestExecution requestExecution = org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.requestExecutionFactory.createNew(org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.cluster, batches, schedule);
        requestExecution.setDescription("Test Schedule");
        requestExecution.persist();
        return requestExecution;
    }

    @org.junit.Test
    public void testScheduleBatch() throws java.lang.Exception {
        org.apache.ambari.server.state.scheduler.RequestExecution requestExecution = createRequestExecution(true);
        junit.framework.Assert.assertNotNull(requestExecution);
        org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.executionScheduleManager.scheduleAllBatches(requestExecution);
        java.lang.String jobName1 = org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.executionScheduleManager.getJobName(requestExecution.getId(), 10L);
        java.lang.String jobName2 = org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.executionScheduleManager.getJobName(requestExecution.getId(), 12L);
        org.quartz.JobDetail jobDetail1 = null;
        org.quartz.JobDetail jobDetail2 = null;
        org.quartz.Trigger trigger1 = null;
        org.quartz.Trigger trigger2 = null;
        for (java.lang.String group : org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.scheduler.getJobGroupNames()) {
            for (org.quartz.JobKey jobKey : org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.scheduler.getJobKeys(org.quartz.impl.matchers.GroupMatcher.jobGroupEquals(org.apache.ambari.server.scheduler.ExecutionJob.LINEAR_EXECUTION_JOB_GROUP))) {
                org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.LOG.info("Found job identified by: " + jobKey);
                java.lang.String jobName = jobKey.getName();
                java.lang.String jobGroup = jobKey.getGroup();
                java.util.List<org.quartz.Trigger> triggers = ((java.util.List<org.quartz.Trigger>) (org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.scheduler.getTriggersOfJob(jobKey)));
                org.quartz.Trigger trigger = ((triggers != null) && (!triggers.isEmpty())) ? triggers.get(0) : null;
                java.util.Date nextFireTime = (trigger != null) ? trigger.getNextFireTime() : null;
                org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.LOG.info((((("[jobName] : " + jobName) + " [groupName] : ") + jobGroup) + " - ") + nextFireTime);
                if (jobName.equals(jobName1)) {
                    jobDetail1 = org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.scheduler.getJobDetail(jobKey);
                    trigger1 = trigger;
                } else if (jobName.equals(jobName2)) {
                    jobDetail2 = org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.scheduler.getJobDetail(jobKey);
                    trigger2 = trigger;
                }
            }
        }
        junit.framework.Assert.assertNotNull(jobDetail1);
        junit.framework.Assert.assertNotNull(trigger1);
        junit.framework.Assert.assertNotNull(jobDetail2);
        junit.framework.Assert.assertNull(trigger2);
        org.quartz.CronTrigger cronTrigger = ((org.quartz.CronTrigger) (trigger1));
        org.apache.ambari.server.state.scheduler.Schedule schedule = new org.apache.ambari.server.state.scheduler.Schedule();
        schedule.setMinutes("10");
        schedule.setHours("2");
        schedule.setMonth("*");
        schedule.setDaysOfMonth("*");
        schedule.setDayOfWeek("?");
        junit.framework.Assert.assertEquals(schedule.getScheduleExpression(), cronTrigger.getCronExpression());
        junit.framework.Assert.assertEquals(jobName1, jobDetail1.getKey().getName());
        junit.framework.Assert.assertEquals(jobName2, jobDetail2.getKey().getName());
    }

    @org.junit.Test
    public void testDeleteAllJobs() throws java.lang.Exception {
        org.apache.ambari.server.state.scheduler.RequestExecution requestExecution = createRequestExecution(true);
        junit.framework.Assert.assertNotNull(requestExecution);
        org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.executionScheduleManager.scheduleAllBatches(requestExecution);
        java.lang.String jobName1 = org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.executionScheduleManager.getJobName(requestExecution.getId(), 10L);
        java.lang.String jobName2 = org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.executionScheduleManager.getJobName(requestExecution.getId(), 12L);
        org.quartz.JobDetail jobDetail1 = org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.scheduler.getJobDetail(org.quartz.JobKey.jobKey(jobName1, org.apache.ambari.server.scheduler.ExecutionJob.LINEAR_EXECUTION_JOB_GROUP));
        org.quartz.JobDetail jobDetail2 = org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.scheduler.getJobDetail(org.quartz.JobKey.jobKey(jobName2, org.apache.ambari.server.scheduler.ExecutionJob.LINEAR_EXECUTION_JOB_GROUP));
        junit.framework.Assert.assertNotNull(jobDetail1);
        junit.framework.Assert.assertNotNull(jobDetail2);
        junit.framework.Assert.assertTrue(!org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.scheduler.getTriggersOfJob(org.quartz.JobKey.jobKey(jobName1, org.apache.ambari.server.scheduler.ExecutionJob.LINEAR_EXECUTION_JOB_GROUP)).isEmpty());
        org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.executionScheduleManager.deleteAllJobs(requestExecution);
        junit.framework.Assert.assertTrue(org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.scheduler.getTriggersOfJob(org.quartz.JobKey.jobKey(jobName1, org.apache.ambari.server.scheduler.ExecutionJob.LINEAR_EXECUTION_JOB_GROUP)).isEmpty());
    }

    @org.junit.Test
    public void testPointInTimeExecutionJob() throws java.lang.Exception {
        org.apache.ambari.server.state.scheduler.RequestExecution requestExecution = createRequestExecution(false);
        junit.framework.Assert.assertNotNull(requestExecution);
        org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.executionScheduleManager.scheduleAllBatches(requestExecution);
        java.lang.String jobName1 = org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.executionScheduleManager.getJobName(requestExecution.getId(), 10L);
        java.lang.String jobName2 = org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.executionScheduleManager.getJobName(requestExecution.getId(), 12L);
        org.quartz.JobDetail jobDetail1 = org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.scheduler.getJobDetail(org.quartz.JobKey.jobKey(jobName1, org.apache.ambari.server.scheduler.ExecutionJob.LINEAR_EXECUTION_JOB_GROUP));
        org.quartz.JobDetail jobDetail2 = org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.scheduler.getJobDetail(org.quartz.JobKey.jobKey(jobName2, org.apache.ambari.server.scheduler.ExecutionJob.LINEAR_EXECUTION_JOB_GROUP));
        junit.framework.Assert.assertNotNull(jobDetail1);
        junit.framework.Assert.assertNotNull(jobDetail2);
        java.util.List<? extends org.quartz.Trigger> triggers = org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.scheduler.getTriggersOfJob(org.quartz.JobKey.jobKey(jobName1, org.apache.ambari.server.scheduler.ExecutionJob.LINEAR_EXECUTION_JOB_GROUP));
        junit.framework.Assert.assertNotNull(triggers);
        junit.framework.Assert.assertEquals(1, triggers.size());
        org.junit.Assert.assertThat(triggers.get(0), org.hamcrest.CoreMatchers.instanceOf(org.quartz.SimpleTrigger.class));
        junit.framework.Assert.assertNull(jobDetail2.getJobDataMap().getString(org.apache.ambari.server.scheduler.ExecutionJob.NEXT_EXECUTION_JOB_NAME_KEY));
        int waitCount = 0;
        while ((org.apache.ambari.server.scheduler.ExecutionScheduleManagerTest.scheduler.getCurrentlyExecutingJobs().size() != 0) && (waitCount < 10)) {
            java.lang.Thread.sleep(100);
            waitCount++;
        } 
    }

    @org.junit.Test
    public void testExecuteBatchRequest() throws java.lang.Exception {
        org.apache.ambari.server.state.Clusters clustersMock = EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster clusterMock = EasyMock.createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.scheduler.RequestExecution requestExecutionMock = EasyMock.createMock(org.apache.ambari.server.state.scheduler.RequestExecution.class);
        org.apache.ambari.server.configuration.Configuration configurationMock = EasyMock.createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
        org.apache.ambari.server.scheduler.ExecutionScheduler executionSchedulerMock = EasyMock.createMock(org.apache.ambari.server.scheduler.ExecutionScheduler.class);
        org.apache.ambari.server.security.authorization.internal.InternalTokenStorage tokenStorageMock = EasyMock.createMock(org.apache.ambari.server.security.authorization.internal.InternalTokenStorage.class);
        org.apache.ambari.server.actionmanager.ActionDBAccessor actionDBAccessorMock = EasyMock.createMock(org.apache.ambari.server.actionmanager.ActionDBAccessor.class);
        com.google.gson.Gson gson = new com.google.gson.Gson();
        org.apache.ambari.server.state.scheduler.BatchRequest batchRequestMock = EasyMock.createMock(org.apache.ambari.server.state.scheduler.BatchRequest.class);
        long executionId = 11L;
        long batchId = 1L;
        long requestId = 5L;
        java.lang.String clusterName = "mycluster";
        java.lang.String uri = "clusters";
        java.lang.String type = "post";
        java.lang.String body = "body";
        java.lang.Integer userId = 1;
        java.util.Map<java.lang.Long, org.apache.ambari.server.state.scheduler.RequestExecution> executionMap = new java.util.HashMap<>();
        executionMap.put(executionId, requestExecutionMock);
        org.apache.ambari.server.state.scheduler.BatchRequestResponse batchRequestResponse = new org.apache.ambari.server.state.scheduler.BatchRequestResponse();
        batchRequestResponse.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS.toString());
        batchRequestResponse.setRequestId(requestId);
        batchRequestResponse.setReturnCode(202);
        org.easymock.EasyMock.expect(configurationMock.getApiSSLAuthentication()).andReturn(java.lang.Boolean.FALSE);
        org.easymock.EasyMock.replay(configurationMock);
        org.apache.ambari.server.scheduler.ExecutionScheduleManager scheduleManager = EasyMock.createMockBuilder(org.apache.ambari.server.scheduler.ExecutionScheduleManager.class).withConstructor(configurationMock, executionSchedulerMock, tokenStorageMock, clustersMock, actionDBAccessorMock, gson).addMockedMethods("performApiRequest", "updateBatchRequest").createNiceMock();
        EasyMock.expect(clustersMock.getCluster(clusterName)).andReturn(clusterMock).anyTimes();
        EasyMock.expect(clusterMock.getAllRequestExecutions()).andReturn(executionMap).anyTimes();
        EasyMock.expect(requestExecutionMock.getBatchRequest(EasyMock.eq(batchId))).andReturn(batchRequestMock).once();
        EasyMock.expect(requestExecutionMock.getRequestBody(EasyMock.eq(batchId))).andReturn(body).once();
        EasyMock.expect(requestExecutionMock.getAuthenticatedUserId()).andReturn(userId).once();
        EasyMock.expect(batchRequestMock.getUri()).andReturn(uri).once();
        EasyMock.expect(batchRequestMock.getType()).andReturn(type).once();
        batchRequestMock.setRequestId(5L);
        EasyMock.expectLastCall().once();
        EasyMock.expect(scheduleManager.performApiRequest(EasyMock.eq(uri), EasyMock.eq(body), EasyMock.eq(type), EasyMock.eq(userId))).andReturn(batchRequestResponse).once();
        scheduleManager.updateBatchRequest(EasyMock.eq(executionId), EasyMock.eq(batchId), EasyMock.eq(clusterName), EasyMock.eq(batchRequestResponse), EasyMock.eq(false));
        EasyMock.expectLastCall().once();
        actionDBAccessorMock.setSourceScheduleForRequest(EasyMock.eq(requestId), EasyMock.eq(executionId));
        EasyMock.expectLastCall().once();
        EasyMock.replay(clusterMock, clustersMock, requestExecutionMock, executionSchedulerMock, tokenStorageMock, batchRequestMock, scheduleManager, actionDBAccessorMock);
        scheduleManager.executeBatchRequest(executionId, batchId, clusterName);
        EasyMock.verify(clusterMock, clustersMock, configurationMock, requestExecutionMock, executionSchedulerMock, tokenStorageMock, batchRequestMock, scheduleManager, actionDBAccessorMock);
    }

    @org.junit.Test
    public void testUpdateBatchRequest() throws java.lang.Exception {
        org.apache.ambari.server.state.Clusters clustersMock = EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster clusterMock = EasyMock.createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.scheduler.RequestExecution requestExecutionMock = EasyMock.createMock(org.apache.ambari.server.state.scheduler.RequestExecution.class);
        org.apache.ambari.server.configuration.Configuration configurationMock = EasyMock.createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
        org.apache.ambari.server.scheduler.ExecutionScheduler executionSchedulerMock = EasyMock.createMock(org.apache.ambari.server.scheduler.ExecutionScheduler.class);
        org.apache.ambari.server.security.authorization.internal.InternalTokenStorage tokenStorageMock = EasyMock.createMock(org.apache.ambari.server.security.authorization.internal.InternalTokenStorage.class);
        org.apache.ambari.server.actionmanager.ActionDBAccessor actionDBAccessorMock = EasyMock.createMock(org.apache.ambari.server.actionmanager.ActionDBAccessor.class);
        com.google.gson.Gson gson = new com.google.gson.Gson();
        org.apache.ambari.server.state.scheduler.BatchRequest batchRequestMock = EasyMock.createMock(org.apache.ambari.server.state.scheduler.BatchRequest.class);
        long executionId = 11L;
        long batchId = 1L;
        long requestId = 5L;
        java.lang.String clusterName = "mycluster";
        java.util.Map<java.lang.Long, org.apache.ambari.server.state.scheduler.RequestExecution> executionMap = new java.util.HashMap<>();
        executionMap.put(executionId, requestExecutionMock);
        org.apache.ambari.server.state.scheduler.BatchRequestResponse batchRequestResponse = new org.apache.ambari.server.state.scheduler.BatchRequestResponse();
        batchRequestResponse.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS.toString());
        batchRequestResponse.setRequestId(requestId);
        batchRequestResponse.setReturnCode(202);
        org.easymock.EasyMock.expect(configurationMock.getApiSSLAuthentication()).andReturn(java.lang.Boolean.FALSE);
        org.easymock.EasyMock.replay(configurationMock);
        org.apache.ambari.server.scheduler.ExecutionScheduleManager scheduleManager = EasyMock.createMockBuilder(org.apache.ambari.server.scheduler.ExecutionScheduleManager.class).withConstructor(configurationMock, executionSchedulerMock, tokenStorageMock, clustersMock, actionDBAccessorMock, gson).addMockedMethods("performApiRequest").createNiceMock();
        EasyMock.expect(clustersMock.getCluster(clusterName)).andReturn(clusterMock).anyTimes();
        EasyMock.expect(clusterMock.getAllRequestExecutions()).andReturn(executionMap).anyTimes();
        requestExecutionMock.updateBatchRequest(EasyMock.eq(batchId), EasyMock.eq(batchRequestResponse), EasyMock.eq(true));
        EasyMock.expectLastCall().once();
        EasyMock.replay(clusterMock, clustersMock, requestExecutionMock, executionSchedulerMock, tokenStorageMock, batchRequestMock, scheduleManager);
        scheduleManager.updateBatchRequest(executionId, batchId, clusterName, batchRequestResponse, true);
        EasyMock.verify(clusterMock, clustersMock, configurationMock, requestExecutionMock, executionSchedulerMock, tokenStorageMock, batchRequestMock, scheduleManager);
    }

    @org.junit.Test
    public void testGetBatchRequestResponse() throws java.lang.Exception {
        org.apache.ambari.server.state.Clusters clustersMock = EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster clusterMock = EasyMock.createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.configuration.Configuration configurationMock = EasyMock.createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
        org.apache.ambari.server.scheduler.ExecutionScheduler executionSchedulerMock = EasyMock.createMock(org.apache.ambari.server.scheduler.ExecutionScheduler.class);
        org.apache.ambari.server.security.authorization.internal.InternalTokenStorage tokenStorageMock = EasyMock.createMock(org.apache.ambari.server.security.authorization.internal.InternalTokenStorage.class);
        org.apache.ambari.server.actionmanager.ActionDBAccessor actionDBAccessorMock = EasyMock.createMock(org.apache.ambari.server.actionmanager.ActionDBAccessor.class);
        com.google.gson.Gson gson = new com.google.gson.Gson();
        long requestId = 5L;
        java.lang.String clusterName = "mycluster";
        java.lang.String apiUri = "api/v1/clusters/mycluster/requests/5";
        org.easymock.Capture<java.lang.String> uriCapture = org.easymock.EasyMock.newCapture();
        org.apache.ambari.server.state.scheduler.BatchRequestResponse batchRequestResponse = new org.apache.ambari.server.state.scheduler.BatchRequestResponse();
        batchRequestResponse.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS.toString());
        batchRequestResponse.setRequestId(requestId);
        batchRequestResponse.setReturnCode(202);
        org.easymock.EasyMock.expect(configurationMock.getApiSSLAuthentication()).andReturn(java.lang.Boolean.FALSE);
        org.easymock.EasyMock.replay(configurationMock);
        org.apache.ambari.server.scheduler.ExecutionScheduleManager scheduleManager = EasyMock.createMockBuilder(org.apache.ambari.server.scheduler.ExecutionScheduleManager.class).withConstructor(configurationMock, executionSchedulerMock, tokenStorageMock, clustersMock, actionDBAccessorMock, gson).addMockedMethods("performApiGetRequest").createNiceMock();
        EasyMock.expect(scheduleManager.performApiGetRequest(EasyMock.capture(uriCapture), EasyMock.eq(true))).andReturn(batchRequestResponse).once();
        EasyMock.replay(clusterMock, clustersMock, executionSchedulerMock, tokenStorageMock, scheduleManager);
        scheduleManager.getBatchRequestResponse(requestId, clusterName);
        EasyMock.verify(clusterMock, clustersMock, configurationMock, executionSchedulerMock, tokenStorageMock, scheduleManager);
        org.junit.Assert.assertEquals(apiUri, uriCapture.getValue());
    }

    @org.junit.Test
    public void testHasToleranceThresholdExceeded() throws java.lang.Exception {
        org.apache.ambari.server.state.Clusters clustersMock = EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster clusterMock = EasyMock.createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.configuration.Configuration configurationMock = EasyMock.createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
        org.apache.ambari.server.scheduler.ExecutionScheduler executionSchedulerMock = EasyMock.createMock(org.apache.ambari.server.scheduler.ExecutionScheduler.class);
        org.apache.ambari.server.security.authorization.internal.InternalTokenStorage tokenStorageMock = EasyMock.createMock(org.apache.ambari.server.security.authorization.internal.InternalTokenStorage.class);
        org.apache.ambari.server.actionmanager.ActionDBAccessor actionDBAccessorMock = EasyMock.createMock(org.apache.ambari.server.actionmanager.ActionDBAccessor.class);
        com.google.gson.Gson gson = new com.google.gson.Gson();
        org.apache.ambari.server.state.scheduler.RequestExecution requestExecutionMock = EasyMock.createMock(org.apache.ambari.server.state.scheduler.RequestExecution.class);
        org.apache.ambari.server.state.scheduler.Batch batchMock = EasyMock.createMock(org.apache.ambari.server.state.scheduler.Batch.class);
        long executionId = 11L;
        java.lang.String clusterName = "c1";
        org.apache.ambari.server.state.scheduler.BatchSettings batchSettings = new org.apache.ambari.server.state.scheduler.BatchSettings();
        batchSettings.setTaskFailureToleranceLimit(1);
        batchSettings.setTaskFailureToleranceLimitPerBatch(1);
        java.util.Map<java.lang.Long, org.apache.ambari.server.state.scheduler.RequestExecution> executionMap = new java.util.HashMap<>();
        executionMap.put(executionId, requestExecutionMock);
        EasyMock.expect(clustersMock.getCluster(clusterName)).andReturn(clusterMock).anyTimes();
        EasyMock.expect(clusterMock.getAllRequestExecutions()).andReturn(executionMap).anyTimes();
        EasyMock.expect(requestExecutionMock.getBatch()).andReturn(batchMock).anyTimes();
        EasyMock.expect(batchMock.getBatchSettings()).andReturn(batchSettings).anyTimes();
        EasyMock.replay(clustersMock, clusterMock, configurationMock, requestExecutionMock, executionSchedulerMock, batchMock);
        org.apache.ambari.server.scheduler.ExecutionScheduleManager scheduleManager = new org.apache.ambari.server.scheduler.ExecutionScheduleManager(configurationMock, executionSchedulerMock, tokenStorageMock, clustersMock, actionDBAccessorMock, gson);
        java.util.HashMap<java.lang.String, java.lang.Integer> taskCounts = new java.util.HashMap<java.lang.String, java.lang.Integer>() {
            {
                put(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_FAILED_TASKS_KEY, 2);
                put(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_TOTAL_TASKS_KEY, 10);
            }
        };
        boolean exceeded = scheduleManager.hasToleranceThresholdExceeded(executionId, clusterName, taskCounts);
        junit.framework.Assert.assertTrue(exceeded);
        EasyMock.verify(clustersMock, clusterMock, configurationMock, requestExecutionMock, executionSchedulerMock, batchMock);
    }

    @org.junit.Test
    public void testHasToleranceThresholdPerBatchExceeded() throws java.lang.Exception {
        org.apache.ambari.server.state.Clusters clustersMock = EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster clusterMock = EasyMock.createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.configuration.Configuration configurationMock = EasyMock.createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
        org.apache.ambari.server.scheduler.ExecutionScheduler executionSchedulerMock = EasyMock.createMock(org.apache.ambari.server.scheduler.ExecutionScheduler.class);
        org.apache.ambari.server.security.authorization.internal.InternalTokenStorage tokenStorageMock = EasyMock.createMock(org.apache.ambari.server.security.authorization.internal.InternalTokenStorage.class);
        org.apache.ambari.server.actionmanager.ActionDBAccessor actionDBAccessorMock = EasyMock.createMock(org.apache.ambari.server.actionmanager.ActionDBAccessor.class);
        com.google.gson.Gson gson = new com.google.gson.Gson();
        org.apache.ambari.server.state.scheduler.RequestExecution requestExecutionMock = EasyMock.createMock(org.apache.ambari.server.state.scheduler.RequestExecution.class);
        org.apache.ambari.server.state.scheduler.Batch batchMock = EasyMock.createMock(org.apache.ambari.server.state.scheduler.Batch.class);
        long executionId = 11L;
        java.lang.String clusterName = "c1";
        org.apache.ambari.server.state.scheduler.BatchSettings batchSettings = new org.apache.ambari.server.state.scheduler.BatchSettings();
        batchSettings.setTaskFailureToleranceLimitPerBatch(1);
        java.util.Map<java.lang.Long, org.apache.ambari.server.state.scheduler.RequestExecution> executionMap = new java.util.HashMap<>();
        executionMap.put(executionId, requestExecutionMock);
        EasyMock.expect(clustersMock.getCluster(clusterName)).andReturn(clusterMock).anyTimes();
        EasyMock.expect(clusterMock.getAllRequestExecutions()).andReturn(executionMap).anyTimes();
        EasyMock.expect(requestExecutionMock.getBatch()).andReturn(batchMock).anyTimes();
        EasyMock.expect(batchMock.getBatchSettings()).andReturn(batchSettings).anyTimes();
        EasyMock.replay(clustersMock, clusterMock, configurationMock, requestExecutionMock, executionSchedulerMock, batchMock);
        org.apache.ambari.server.scheduler.ExecutionScheduleManager scheduleManager = new org.apache.ambari.server.scheduler.ExecutionScheduleManager(configurationMock, executionSchedulerMock, tokenStorageMock, clustersMock, actionDBAccessorMock, gson);
        java.util.HashMap<java.lang.String, java.lang.Integer> taskCounts = new java.util.HashMap<java.lang.String, java.lang.Integer>() {
            {
                put(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_FAILED_TASKS_IN_CURRENT_BATCH_KEY, 2);
                put(org.apache.ambari.server.state.scheduler.BatchRequestJob.BATCH_REQUEST_TOTAL_TASKS_KEY, 10);
            }
        };
        boolean exceeded = scheduleManager.hasToleranceThresholdExceeded(executionId, clusterName, taskCounts);
        junit.framework.Assert.assertTrue(exceeded);
        EasyMock.verify(clustersMock, clusterMock, configurationMock, requestExecutionMock, executionSchedulerMock, batchMock);
    }

    @java.lang.SuppressWarnings("unchecked")
    @org.junit.Test
    public void testFinalizeBatch() throws java.lang.Exception {
        org.apache.ambari.server.state.Clusters clustersMock = EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster clusterMock = EasyMock.createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.configuration.Configuration configurationMock = EasyMock.createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
        org.apache.ambari.server.scheduler.ExecutionScheduler executionSchedulerMock = EasyMock.createMock(org.apache.ambari.server.scheduler.ExecutionScheduler.class);
        org.apache.ambari.server.security.authorization.internal.InternalTokenStorage tokenStorageMock = EasyMock.createMock(org.apache.ambari.server.security.authorization.internal.InternalTokenStorage.class);
        org.apache.ambari.server.actionmanager.ActionDBAccessor actionDBAccessorMock = EasyMock.createMock(org.apache.ambari.server.actionmanager.ActionDBAccessor.class);
        com.google.gson.Gson gson = new com.google.gson.Gson();
        org.apache.ambari.server.state.scheduler.RequestExecution requestExecutionMock = EasyMock.createMock(org.apache.ambari.server.state.scheduler.RequestExecution.class);
        org.apache.ambari.server.state.scheduler.Batch batchMock = EasyMock.createMock(org.apache.ambari.server.state.scheduler.Batch.class);
        org.quartz.JobDetail jobDetailMock = EasyMock.createMock(org.quartz.JobDetail.class);
        final org.apache.ambari.server.state.scheduler.BatchRequest batchRequestMock = EasyMock.createMock(org.apache.ambari.server.state.scheduler.BatchRequest.class);
        final org.quartz.Trigger triggerMock = EasyMock.createNiceMock(org.quartz.Trigger.class);
        final java.util.List<org.quartz.Trigger> triggers = new java.util.ArrayList<org.quartz.Trigger>() {
            {
                add(triggerMock);
            }
        };
        long executionId = 11L;
        java.lang.String clusterName = "c1";
        java.util.Date pastDate = new java.util.Date(new java.util.Date().getTime() - 2);
        java.util.Map<java.lang.Long, org.apache.ambari.server.state.scheduler.RequestExecution> executionMap = new java.util.HashMap<>();
        executionMap.put(executionId, requestExecutionMock);
        org.easymock.EasyMock.expect(configurationMock.getApiSSLAuthentication()).andReturn(java.lang.Boolean.FALSE);
        org.easymock.EasyMock.replay(configurationMock);
        org.apache.ambari.server.scheduler.ExecutionScheduleManager scheduleManager = EasyMock.createMockBuilder(org.apache.ambari.server.scheduler.ExecutionScheduleManager.class).withConstructor(configurationMock, executionSchedulerMock, tokenStorageMock, clustersMock, actionDBAccessorMock, gson).createMock();
        EasyMock.expect(clustersMock.getCluster(clusterName)).andReturn(clusterMock).anyTimes();
        EasyMock.expect(clusterMock.getAllRequestExecutions()).andReturn(executionMap).anyTimes();
        EasyMock.expect(requestExecutionMock.getBatch()).andReturn(batchMock).anyTimes();
        EasyMock.expect(batchMock.getBatchRequests()).andReturn(new java.util.ArrayList<org.apache.ambari.server.state.scheduler.BatchRequest>() {
            {
                add(batchRequestMock);
            }
        });
        EasyMock.expect(batchRequestMock.getOrderId()).andReturn(1L).anyTimes();
        EasyMock.expect(executionSchedulerMock.getJobDetail(((org.quartz.JobKey) (EasyMock.anyObject())))).andReturn(jobDetailMock).anyTimes();
        EasyMock.expect(((java.util.List<org.quartz.Trigger>) (executionSchedulerMock.getTriggersForJob(((org.quartz.JobKey) (EasyMock.anyObject())))))).andReturn(triggers).anyTimes();
        EasyMock.expect(triggerMock.mayFireAgain()).andReturn(true).anyTimes();
        EasyMock.expect(triggerMock.getFinalFireTime()).andReturn(pastDate).anyTimes();
        requestExecutionMock.updateStatus(org.apache.ambari.server.state.scheduler.RequestExecution.Status.COMPLETED);
        EasyMock.expectLastCall();
        EasyMock.replay(clustersMock, clusterMock, requestExecutionMock, executionSchedulerMock, scheduleManager, batchMock, batchRequestMock, triggerMock, jobDetailMock, actionDBAccessorMock);
        scheduleManager.finalizeBatch(executionId, clusterName);
        EasyMock.verify(clustersMock, clusterMock, configurationMock, requestExecutionMock, executionSchedulerMock, scheduleManager, batchMock, batchRequestMock, triggerMock, jobDetailMock, actionDBAccessorMock);
    }

    @org.junit.Test
    public void testFinalizeBeforeExit() throws java.lang.Exception {
        org.apache.ambari.server.scheduler.ExecutionScheduleManager scheduleManagerMock = EasyMock.createMock(org.apache.ambari.server.scheduler.ExecutionScheduleManager.class);
        org.apache.ambari.server.scheduler.AbstractLinearExecutionJob executionJob = EasyMock.createMockBuilder(org.apache.ambari.server.scheduler.AbstractLinearExecutionJob.class).addMockedMethods("finalizeExecution", "doWork").withConstructor(scheduleManagerMock).createMock();
        org.quartz.JobExecutionContext context = EasyMock.createMock(org.quartz.JobExecutionContext.class);
        org.quartz.JobDetail jobDetail = EasyMock.createMock(org.quartz.JobDetail.class);
        org.quartz.JobDataMap jobDataMap = EasyMock.createMock(org.quartz.JobDataMap.class);
        EasyMock.expect(context.getJobDetail()).andReturn(jobDetail).anyTimes();
        EasyMock.expect(context.getMergedJobDataMap()).andReturn(jobDataMap).anyTimes();
        EasyMock.expect(jobDetail.getKey()).andReturn(new org.quartz.JobKey("TestJob"));
        EasyMock.expect(jobDataMap.getWrappedMap()).andReturn(new java.util.HashMap<>());
        EasyMock.expect(scheduleManagerMock.continueOnMisfire(context)).andReturn(true);
        executionJob.doWork(org.easymock.EasyMock.anyObject());
        EasyMock.expectLastCall().andThrow(new org.apache.ambari.server.AmbariException("Test Exception")).anyTimes();
        executionJob.finalizeExecution(org.easymock.EasyMock.anyObject());
        EasyMock.expectLastCall().once();
        EasyMock.replay(scheduleManagerMock, executionJob, context, jobDataMap, jobDetail);
        try {
            executionJob.execute(context);
        } catch (java.lang.Exception ae) {
            org.junit.Assert.assertThat(ae, org.hamcrest.CoreMatchers.instanceOf(org.quartz.JobExecutionException.class));
            org.quartz.JobExecutionException je = ((org.quartz.JobExecutionException) (ae));
            junit.framework.Assert.assertEquals("Test Exception", je.getUnderlyingException().getMessage());
        }
        EasyMock.verify(scheduleManagerMock, executionJob, context, jobDataMap, jobDetail);
    }

    @org.junit.Test
    public void testExtendApiResource() throws java.lang.NoSuchMethodException, java.lang.reflect.InvocationTargetException, java.lang.IllegalAccessException {
        com.sun.jersey.api.client.WebResource webResource = com.sun.jersey.api.client.Client.create().resource("http://localhost:8080/");
        java.lang.String clustersEndpoint = "http://localhost:8080/api/v1/clusters";
        org.apache.ambari.server.state.Clusters clustersMock = EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.configuration.Configuration configurationMock = EasyMock.createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
        org.apache.ambari.server.scheduler.ExecutionScheduler executionSchedulerMock = EasyMock.createMock(org.apache.ambari.server.scheduler.ExecutionScheduler.class);
        org.apache.ambari.server.security.authorization.internal.InternalTokenStorage tokenStorageMock = EasyMock.createMock(org.apache.ambari.server.security.authorization.internal.InternalTokenStorage.class);
        org.apache.ambari.server.actionmanager.ActionDBAccessor actionDBAccessorMock = EasyMock.createMock(org.apache.ambari.server.actionmanager.ActionDBAccessor.class);
        com.google.gson.Gson gson = new com.google.gson.Gson();
        EasyMock.replay(clustersMock, configurationMock, executionSchedulerMock, tokenStorageMock, actionDBAccessorMock);
        org.apache.ambari.server.scheduler.ExecutionScheduleManager scheduleManager = new org.apache.ambari.server.scheduler.ExecutionScheduleManager(configurationMock, executionSchedulerMock, tokenStorageMock, clustersMock, actionDBAccessorMock, gson);
        org.junit.Assert.assertEquals(clustersEndpoint, scheduleManager.extendApiResource(webResource, "clusters").getURI().toString());
        org.junit.Assert.assertEquals(clustersEndpoint, scheduleManager.extendApiResource(webResource, "/clusters").getURI().toString());
        org.junit.Assert.assertEquals(clustersEndpoint, scheduleManager.extendApiResource(webResource, "/api/v1/clusters").getURI().toString());
        org.junit.Assert.assertEquals(clustersEndpoint, scheduleManager.extendApiResource(webResource, "api/v1/clusters").getURI().toString());
        org.junit.Assert.assertEquals("http://localhost:8080/", scheduleManager.extendApiResource(webResource, "").getURI().toString());
    }

    @org.junit.Test
    public void testUpdateBatchSchedulePause() throws java.lang.Exception {
        org.apache.ambari.server.state.Clusters clustersMock = EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster clusterMock = EasyMock.createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.configuration.Configuration configurationMock = EasyMock.createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
        org.apache.ambari.server.scheduler.ExecutionScheduler executionSchedulerMock = EasyMock.createMock(org.apache.ambari.server.scheduler.ExecutionScheduler.class);
        org.apache.ambari.server.security.authorization.internal.InternalTokenStorage tokenStorageMock = EasyMock.createMock(org.apache.ambari.server.security.authorization.internal.InternalTokenStorage.class);
        org.apache.ambari.server.actionmanager.ActionDBAccessor actionDBAccessorMock = EasyMock.createMock(org.apache.ambari.server.actionmanager.ActionDBAccessor.class);
        com.google.gson.Gson gson = new com.google.gson.Gson();
        org.apache.ambari.server.state.scheduler.RequestExecution requestExecutionMock = EasyMock.createMock(org.apache.ambari.server.state.scheduler.RequestExecution.class);
        org.apache.ambari.server.state.scheduler.Batch batchMock = EasyMock.createMock(org.apache.ambari.server.state.scheduler.Batch.class);
        org.quartz.JobDetail jobDetailMock = EasyMock.createMock(org.quartz.JobDetail.class);
        final org.apache.ambari.server.state.scheduler.BatchRequest batchRequestMock1 = EasyMock.createMock(org.apache.ambari.server.state.scheduler.BatchRequest.class);
        final org.apache.ambari.server.state.scheduler.BatchRequest batchRequestMock2 = EasyMock.createMock(org.apache.ambari.server.state.scheduler.BatchRequest.class);
        final org.quartz.Trigger triggerMock = EasyMock.createNiceMock(org.quartz.Trigger.class);
        final java.util.List<org.quartz.Trigger> triggers = new java.util.ArrayList<org.quartz.Trigger>() {
            {
                add(triggerMock);
            }
        };
        long executionId = 11L;
        java.lang.String clusterName = "c1";
        java.util.Date pastDate = new java.util.Date(new java.util.Date().getTime() - 2);
        java.util.Map<java.lang.Long, org.apache.ambari.server.state.scheduler.RequestExecution> executionMap = new java.util.HashMap<>();
        executionMap.put(executionId, requestExecutionMock);
        org.easymock.EasyMock.expect(configurationMock.getApiSSLAuthentication()).andReturn(java.lang.Boolean.FALSE);
        org.easymock.EasyMock.replay(configurationMock);
        org.apache.ambari.server.scheduler.ExecutionScheduleManager scheduleManager = EasyMock.createMockBuilder(org.apache.ambari.server.scheduler.ExecutionScheduleManager.class).withConstructor(configurationMock, executionSchedulerMock, tokenStorageMock, clustersMock, actionDBAccessorMock, gson).addMockedMethods("deleteJobs", "abortRequestById").createMock();
        EasyMock.expect(clustersMock.getCluster(clusterName)).andReturn(clusterMock).anyTimes();
        EasyMock.expect(clusterMock.getAllRequestExecutions()).andReturn(executionMap).anyTimes();
        EasyMock.expect(requestExecutionMock.getBatch()).andReturn(batchMock).anyTimes();
        EasyMock.expect(batchMock.getBatchRequests()).andReturn(new java.util.ArrayList<org.apache.ambari.server.state.scheduler.BatchRequest>() {
            {
                add(batchRequestMock1);
                add(batchRequestMock2);
            }
        });
        EasyMock.expect(batchRequestMock1.getOrderId()).andReturn(1L).anyTimes();
        EasyMock.expect(batchRequestMock1.getStatus()).andReturn(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.name()).anyTimes();
        EasyMock.expect(batchRequestMock1.compareTo(batchRequestMock2)).andReturn(-1).anyTimes();
        EasyMock.expect(batchRequestMock2.compareTo(batchRequestMock1)).andReturn(1).anyTimes();
        EasyMock.expect(batchRequestMock2.getOrderId()).andReturn(3L).anyTimes();
        EasyMock.expect(batchRequestMock2.getStatus()).andReturn(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS.name()).anyTimes();
        EasyMock.expect(executionSchedulerMock.getJobDetail(((org.quartz.JobKey) (EasyMock.anyObject())))).andReturn(jobDetailMock).anyTimes();
        EasyMock.expect(((java.util.List<org.quartz.Trigger>) (executionSchedulerMock.getTriggersForJob(((org.quartz.JobKey) (EasyMock.anyObject())))))).andReturn(triggers).anyTimes();
        EasyMock.expect(triggerMock.mayFireAgain()).andReturn(true).anyTimes();
        EasyMock.expect(triggerMock.getFinalFireTime()).andReturn(pastDate).anyTimes();
        EasyMock.expect(requestExecutionMock.getStatus()).andReturn(org.apache.ambari.server.state.scheduler.RequestExecution.Status.PAUSED.name()).anyTimes();
        EasyMock.expect(requestExecutionMock.getId()).andReturn(executionId).anyTimes();
        EasyMock.expect(requestExecutionMock.getBatchRequestRequestsIDs(3L)).andReturn(java.util.Collections.singleton(5L)).anyTimes();
        scheduleManager.deleteJobs(EasyMock.eq(requestExecutionMock), EasyMock.eq(3L));
        EasyMock.expectLastCall().once();
        EasyMock.expect(scheduleManager.abortRequestById(requestExecutionMock, 5L)).andReturn(null).once();
        EasyMock.replay(clustersMock, clusterMock, requestExecutionMock, executionSchedulerMock, scheduleManager, batchMock, batchRequestMock1, batchRequestMock2, triggerMock, jobDetailMock, actionDBAccessorMock);
        scheduleManager.updateBatchSchedule(requestExecutionMock);
        EasyMock.verify(clustersMock, clusterMock, configurationMock, requestExecutionMock, executionSchedulerMock, scheduleManager, batchMock, batchRequestMock1, batchRequestMock2, triggerMock, jobDetailMock, actionDBAccessorMock);
    }

    @org.junit.Test
    public void testUpdateBatchScheduleUnpause() throws java.lang.Exception {
        org.apache.ambari.server.state.Clusters clustersMock = EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster clusterMock = EasyMock.createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.configuration.Configuration configurationMock = EasyMock.createNiceMock(org.apache.ambari.server.configuration.Configuration.class);
        org.apache.ambari.server.scheduler.ExecutionScheduler executionSchedulerMock = EasyMock.createMock(org.apache.ambari.server.scheduler.ExecutionScheduler.class);
        org.apache.ambari.server.security.authorization.internal.InternalTokenStorage tokenStorageMock = EasyMock.createMock(org.apache.ambari.server.security.authorization.internal.InternalTokenStorage.class);
        org.apache.ambari.server.actionmanager.ActionDBAccessor actionDBAccessorMock = EasyMock.createMock(org.apache.ambari.server.actionmanager.ActionDBAccessor.class);
        com.google.gson.Gson gson = new com.google.gson.Gson();
        org.apache.ambari.server.state.scheduler.RequestExecution requestExecutionMock = EasyMock.createMock(org.apache.ambari.server.state.scheduler.RequestExecution.class);
        org.apache.ambari.server.state.scheduler.Batch batchMock = EasyMock.createMock(org.apache.ambari.server.state.scheduler.Batch.class);
        org.quartz.JobDetail jobDetailMock = EasyMock.createMock(org.quartz.JobDetail.class);
        final org.apache.ambari.server.state.scheduler.BatchRequest batchRequestMock1 = EasyMock.createMock(org.apache.ambari.server.state.scheduler.BatchRequest.class);
        final org.apache.ambari.server.state.scheduler.BatchRequest batchRequestMock2 = EasyMock.createMock(org.apache.ambari.server.state.scheduler.BatchRequest.class);
        final org.quartz.Trigger triggerMock = EasyMock.createNiceMock(org.quartz.Trigger.class);
        final java.util.List<org.quartz.Trigger> triggers = new java.util.ArrayList<org.quartz.Trigger>() {
            {
                add(triggerMock);
            }
        };
        long executionId = 11L;
        java.lang.String clusterName = "c1";
        java.util.Date pastDate = new java.util.Date(new java.util.Date().getTime() - 2);
        java.util.Map<java.lang.Long, org.apache.ambari.server.state.scheduler.RequestExecution> executionMap = new java.util.HashMap<>();
        executionMap.put(executionId, requestExecutionMock);
        org.easymock.EasyMock.expect(configurationMock.getApiSSLAuthentication()).andReturn(java.lang.Boolean.FALSE);
        org.easymock.EasyMock.replay(configurationMock);
        org.apache.ambari.server.scheduler.ExecutionScheduleManager scheduleManager = EasyMock.createMockBuilder(org.apache.ambari.server.scheduler.ExecutionScheduleManager.class).withConstructor(configurationMock, executionSchedulerMock, tokenStorageMock, clustersMock, actionDBAccessorMock, gson).addMockedMethods("scheduleBatch").createMock();
        EasyMock.expect(clustersMock.getCluster(clusterName)).andReturn(clusterMock).anyTimes();
        EasyMock.expect(clusterMock.getAllRequestExecutions()).andReturn(executionMap).anyTimes();
        EasyMock.expect(requestExecutionMock.getBatch()).andReturn(batchMock).anyTimes();
        EasyMock.expect(batchMock.getBatchRequests()).andReturn(new java.util.ArrayList<org.apache.ambari.server.state.scheduler.BatchRequest>() {
            {
                add(batchRequestMock1);
                add(batchRequestMock2);
            }
        });
        EasyMock.expect(batchRequestMock1.getOrderId()).andReturn(1L).anyTimes();
        EasyMock.expect(batchRequestMock1.getStatus()).andReturn(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED.name()).anyTimes();
        EasyMock.expect(batchRequestMock1.compareTo(batchRequestMock2)).andReturn(-1).anyTimes();
        EasyMock.expect(batchRequestMock2.compareTo(batchRequestMock1)).andReturn(1).anyTimes();
        EasyMock.expect(batchRequestMock2.getOrderId()).andReturn(3L).anyTimes();
        EasyMock.expect(batchRequestMock2.getStatus()).andReturn(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING.name()).anyTimes();
        EasyMock.expect(executionSchedulerMock.getJobDetail(((org.quartz.JobKey) (EasyMock.anyObject())))).andReturn(jobDetailMock).anyTimes();
        EasyMock.expect(((java.util.List<org.quartz.Trigger>) (executionSchedulerMock.getTriggersForJob(((org.quartz.JobKey) (EasyMock.anyObject())))))).andReturn(triggers).anyTimes();
        EasyMock.expect(triggerMock.mayFireAgain()).andReturn(true).anyTimes();
        EasyMock.expect(triggerMock.getFinalFireTime()).andReturn(pastDate).anyTimes();
        EasyMock.expect(requestExecutionMock.getStatus()).andReturn(org.apache.ambari.server.state.scheduler.RequestExecution.Status.SCHEDULED.name()).anyTimes();
        EasyMock.expect(requestExecutionMock.getId()).andReturn(executionId).anyTimes();
        EasyMock.expect(requestExecutionMock.getBatchRequestRequestsIDs(3L)).andReturn(java.util.Collections.singleton(5L)).anyTimes();
        scheduleManager.scheduleBatch(EasyMock.eq(requestExecutionMock), EasyMock.eq(3L));
        EasyMock.expectLastCall().once();
        EasyMock.replay(clustersMock, clusterMock, requestExecutionMock, executionSchedulerMock, scheduleManager, batchMock, batchRequestMock1, batchRequestMock2, triggerMock, jobDetailMock, actionDBAccessorMock);
        scheduleManager.updateBatchSchedule(requestExecutionMock);
        EasyMock.verify(clustersMock, clusterMock, configurationMock, requestExecutionMock, executionSchedulerMock, scheduleManager, batchMock, batchRequestMock1, batchRequestMock2, triggerMock, jobDetailMock, actionDBAccessorMock);
    }
}