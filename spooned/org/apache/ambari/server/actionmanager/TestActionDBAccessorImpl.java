package org.apache.ambari.server.actionmanager;
import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import org.easymock.EasyMock;
import static org.apache.ambari.server.orm.DBAccessor.DbType.ORACLE;
public class TestActionDBAccessorImpl {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.actionmanager.TestActionDBAccessorImpl.class);

    private long requestId = 23;

    private long stageId = 31;

    private java.lang.String hostName = "host1";

    private java.lang.String clusterName = "cluster1";

    private java.lang.String actionName = "validate_kerberos";

    private java.lang.String serverHostName = org.apache.ambari.server.utils.StageUtils.getHostName();

    private java.lang.String serverActionName = org.apache.ambari.server.serveraction.MockServerAction.class.getName();

    private com.google.inject.Injector injector;

    org.apache.ambari.server.actionmanager.ActionDBAccessor db;

    org.apache.ambari.server.actionmanager.ActionManager am;

    @com.google.inject.Inject
    private org.apache.ambari.server.state.Clusters clusters;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.ExecutionCommandDAO executionCommandDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.actionmanager.StageFactory stageFactory;

    @org.junit.Before
    public void setup() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.orm.InMemoryDefaultTestModule defaultTestModule = new org.apache.ambari.server.orm.InMemoryDefaultTestModule();
        injector = com.google.inject.Guice.createInjector(com.google.inject.util.Modules.override(defaultTestModule).with(new org.apache.ambari.server.actionmanager.TestActionDBAccessorImpl.TestActionDBAccessorModule()));
        org.apache.ambari.server.H2DatabaseCleaner.resetSequences(injector);
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        injector.getInstance(org.apache.ambari.server.api.services.AmbariMetaInfo.class);
        injector.injectMembers(this);
        clusters.addHost(serverHostName);
        clusters.addHost(hostName);
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-0.1");
        clusters.addCluster(clusterName, stackId);
        db = injector.getInstance(org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.class);
        am = injector.getInstance(org.apache.ambari.server.actionmanager.ActionManager.class);
        org.easymock.EasyMock.replay(injector.getInstance(org.apache.ambari.server.audit.AuditLogger.class));
    }

    @org.junit.After
    public void tearDown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    @org.junit.Test
    public void testActionResponse() throws org.apache.ambari.server.AmbariException {
        java.lang.String hostname = "host1";
        populateActionDB(db, hostname, requestId, stageId, false);
        org.apache.ambari.server.actionmanager.Stage stage = db.getAllStages(requestId).get(0);
        org.junit.Assert.assertEquals(stageId, stage.getStageId());
        stage.setHostRoleStatus(hostname, "HBASE_MASTER", org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED);
        db.hostRoleScheduled(stage, hostname, "HBASE_MASTER");
        java.util.List<org.apache.ambari.server.agent.CommandReport> reports = new java.util.ArrayList<>();
        org.apache.ambari.server.agent.CommandReport cr = new org.apache.ambari.server.agent.CommandReport();
        cr.setTaskId(1);
        cr.setActionId(org.apache.ambari.server.utils.StageUtils.getActionId(requestId, stageId));
        cr.setRole("HBASE_MASTER");
        cr.setStatus("COMPLETED");
        cr.setStdErr("");
        cr.setStdOut("");
        cr.setExitCode(215);
        reports.add(cr);
        am.processTaskResponse(hostname, reports, org.apache.ambari.server.utils.CommandUtils.convertToTaskIdCommandMap(stage.getOrderedHostRoleCommands()));
        am.processTaskResponse(hostname, reports, org.apache.ambari.server.utils.CommandUtils.convertToTaskIdCommandMap(stage.getOrderedHostRoleCommands()));
        org.junit.Assert.assertEquals(215, am.getAction(requestId, stageId).getExitCode(hostname, "HBASE_MASTER"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, am.getAction(requestId, stageId).getHostRoleStatus(hostname, "HBASE_MASTER"));
        org.apache.ambari.server.actionmanager.Stage s = db.getAllStages(requestId).get(0);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, s.getHostRoleStatus(hostname, "HBASE_MASTER"));
    }

    @org.junit.Test
    public void testCancelCommandReport() throws org.apache.ambari.server.AmbariException {
        java.lang.String hostname = "host1";
        populateActionDB(db, hostname, requestId, stageId, false);
        org.apache.ambari.server.actionmanager.Stage stage = db.getAllStages(requestId).get(0);
        org.junit.Assert.assertEquals(stageId, stage.getStageId());
        stage.setHostRoleStatus(hostname, "HBASE_MASTER", org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED);
        db.hostRoleScheduled(stage, hostname, "HBASE_MASTER");
        java.util.List<org.apache.ambari.server.agent.CommandReport> reports = new java.util.ArrayList<>();
        org.apache.ambari.server.agent.CommandReport cr = new org.apache.ambari.server.agent.CommandReport();
        cr.setTaskId(1);
        cr.setActionId(org.apache.ambari.server.utils.StageUtils.getActionId(requestId, stageId));
        cr.setRole("HBASE_MASTER");
        cr.setStatus("COMPLETED");
        cr.setStdErr("");
        cr.setStdOut("");
        cr.setExitCode(0);
        reports.add(cr);
        am.processTaskResponse(hostname, reports, org.apache.ambari.server.utils.CommandUtils.convertToTaskIdCommandMap(stage.getOrderedHostRoleCommands()));
        org.junit.Assert.assertEquals(0, am.getAction(requestId, stageId).getExitCode(hostname, "HBASE_MASTER"));
        org.junit.Assert.assertEquals("HostRoleStatus should remain ABORTED " + "(command report status should be ignored)", org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, am.getAction(requestId, stageId).getHostRoleStatus(hostname, "HBASE_MASTER"));
        org.apache.ambari.server.actionmanager.Stage s = db.getAllStages(requestId).get(0);
        org.junit.Assert.assertEquals("HostRoleStatus should remain ABORTED " + "(command report status should be ignored)", org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, s.getHostRoleStatus(hostname, "HBASE_MASTER"));
    }

    @org.junit.Test
    public void testGetStagesInProgress() throws org.apache.ambari.server.AmbariException {
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = new java.util.ArrayList<>();
        stages.add(createStubStage(hostName, requestId, stageId, false));
        stages.add(createStubStage(hostName, requestId, stageId + 1, false));
        org.apache.ambari.server.actionmanager.Request request = new org.apache.ambari.server.actionmanager.Request(stages, "", clusters);
        db.persistActions(request);
        org.junit.Assert.assertEquals(2, stages.size());
    }

    @org.junit.Test
    public void testGetStagesInProgressWithFailures() throws org.apache.ambari.server.AmbariException {
        populateActionDB(db, hostName, requestId, stageId, false);
        populateActionDB(db, hostName, requestId + 1, stageId, false);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = db.getFirstStageInProgressPerRequest();
        org.junit.Assert.assertEquals(2, stages.size());
        db.abortOperation(requestId);
        stages = db.getFirstStageInProgressPerRequest();
        org.junit.Assert.assertEquals(1, stages.size());
        org.junit.Assert.assertEquals(requestId + 1, stages.get(0).getRequestId());
    }

    @org.junit.Test
    public void testGetStagesInProgressWithManyStages() throws org.apache.ambari.server.AmbariException {
        populateActionDBMultipleStages(3, db, hostName, requestId, stageId);
        populateActionDBMultipleStages(3, db, hostName, requestId + 1, stageId + 3);
        populateActionDBMultipleStages(3, db, hostName, requestId + 2, stageId + 3);
        int commandsInProgressCount = db.getCommandsInProgressCount();
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = db.getFirstStageInProgressPerRequest();
        org.junit.Assert.assertEquals(18, commandsInProgressCount);
        org.junit.Assert.assertEquals(3, stages.size());
        long lastRequestId = java.lang.Integer.MIN_VALUE;
        for (org.apache.ambari.server.actionmanager.Stage stage : stages) {
            org.junit.Assert.assertTrue(stage.getRequestId() >= lastRequestId);
            lastRequestId = stage.getRequestId();
        }
        db.abortOperation(requestId);
        commandsInProgressCount = db.getCommandsInProgressCount();
        stages = db.getFirstStageInProgressPerRequest();
        org.junit.Assert.assertEquals(12, commandsInProgressCount);
        org.junit.Assert.assertEquals(2, stages.size());
        stages.get(0).setHostRoleStatus(hostName, org.apache.ambari.server.Role.HBASE_MASTER.toString(), org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
        db.hostRoleScheduled(stages.get(0), hostName, org.apache.ambari.server.Role.HBASE_MASTER.toString());
        commandsInProgressCount = db.getCommandsInProgressCount();
        stages = db.getFirstStageInProgressPerRequest();
        org.junit.Assert.assertEquals(11, commandsInProgressCount);
        org.junit.Assert.assertEquals(2, stages.size());
        stages.get(0).setHostRoleStatus(hostName, org.apache.ambari.server.Role.HBASE_REGIONSERVER.toString(), org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
        db.hostRoleScheduled(stages.get(0), hostName, org.apache.ambari.server.Role.HBASE_REGIONSERVER.toString());
        commandsInProgressCount = db.getCommandsInProgressCount();
        stages = db.getFirstStageInProgressPerRequest();
        org.junit.Assert.assertEquals(10, commandsInProgressCount);
        org.junit.Assert.assertEquals(2, stages.size());
    }

    @org.junit.Test
    public void testGetStagesInProgressWithManyCommands() throws org.apache.ambari.server.AmbariException {
        for (int i = 0; i < 1000; i++) {
            java.lang.String hostName = "c64-" + i;
            clusters.addHost(hostName);
        }
        int requestCount = 1000;
        for (int i = 0; i < requestCount; i++) {
            java.lang.String hostName = "c64-" + i;
            populateActionDBMultipleStages(3, db, hostName, requestId + i, stageId);
        }
        int commandsInProgressCount = db.getCommandsInProgressCount();
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = db.getFirstStageInProgressPerRequest();
        org.junit.Assert.assertEquals(6000, commandsInProgressCount);
        org.junit.Assert.assertEquals(requestCount, stages.size());
    }

    @org.junit.Test
    public void testPersistActions() throws org.apache.ambari.server.AmbariException {
        populateActionDB(db, hostName, requestId, stageId, false);
        for (org.apache.ambari.server.actionmanager.Stage stage : db.getAllStages(requestId)) {
            org.apache.ambari.server.actionmanager.TestActionDBAccessorImpl.log.info("taskId={}" + stage.getExecutionCommands(hostName).get(0).getExecutionCommand().getTaskId());
            org.junit.Assert.assertTrue(stage.getExecutionCommands(hostName).get(0).getExecutionCommand().getTaskId() > 0);
            org.junit.Assert.assertTrue(executionCommandDAO.findByPK(stage.getExecutionCommands(hostName).get(0).getExecutionCommand().getTaskId()) != null);
        }
    }

    @org.junit.Test
    public void testHostRoleScheduled() throws java.lang.InterruptedException, org.apache.ambari.server.AmbariException {
        populateActionDB(db, hostName, requestId, stageId, false);
        org.apache.ambari.server.actionmanager.Stage stage = db.getStage(org.apache.ambari.server.utils.StageUtils.getActionId(requestId, stageId));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, stage.getHostRoleStatus(hostName, org.apache.ambari.server.Role.HBASE_MASTER.toString()));
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> entities = hostRoleCommandDAO.findByHostRole(hostName, requestId, stageId, org.apache.ambari.server.Role.HBASE_MASTER.toString());
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, entities.get(0).getStatus());
        stage.setHostRoleStatus(hostName, org.apache.ambari.server.Role.HBASE_MASTER.toString(), org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED);
        entities = hostRoleCommandDAO.findByHostRole(hostName, requestId, stageId, org.apache.ambari.server.Role.HBASE_MASTER.toString());
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, stage.getHostRoleStatus(hostName, org.apache.ambari.server.Role.HBASE_MASTER.toString()));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, entities.get(0).getStatus());
        db.hostRoleScheduled(stage, hostName, org.apache.ambari.server.Role.HBASE_MASTER.toString());
        entities = hostRoleCommandDAO.findByHostRole(hostName, requestId, stageId, org.apache.ambari.server.Role.HBASE_MASTER.toString());
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, entities.get(0).getStatus());
        java.lang.Thread thread = new java.lang.Thread() {
            @java.lang.Override
            public void run() {
                org.apache.ambari.server.actionmanager.Stage stage1 = db.getStage("23-31");
                stage1.setHostRoleStatus(hostName, org.apache.ambari.server.Role.HBASE_MASTER.toString(), org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
                db.hostRoleScheduled(stage1, hostName, org.apache.ambari.server.Role.HBASE_MASTER.toString());
                injector.getInstance(javax.persistence.EntityManager.class).clear();
            }
        };
        thread.start();
        thread.join();
        injector.getInstance(javax.persistence.EntityManager.class).clear();
        entities = hostRoleCommandDAO.findByHostRole(hostName, requestId, stageId, org.apache.ambari.server.Role.HBASE_MASTER.toString());
        org.junit.Assert.assertEquals("Concurrent update failed", org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, entities.get(0).getStatus());
    }

    @org.junit.Test
    public void testCustomActionScheduled() throws java.lang.InterruptedException, org.apache.ambari.server.AmbariException {
        populateActionDBWithCustomAction(db, hostName, requestId, stageId);
        org.apache.ambari.server.actionmanager.Stage stage = db.getStage(org.apache.ambari.server.utils.StageUtils.getActionId(requestId, stageId));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, stage.getHostRoleStatus(hostName, actionName));
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> entities = hostRoleCommandDAO.findByHostRole(hostName, requestId, stageId, actionName);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, entities.get(0).getStatus());
        stage.setHostRoleStatus(hostName, actionName, org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED);
        entities = hostRoleCommandDAO.findByHostRole(hostName, requestId, stageId, actionName);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, stage.getHostRoleStatus(hostName, actionName));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, entities.get(0).getStatus());
        db.hostRoleScheduled(stage, hostName, actionName);
        entities = hostRoleCommandDAO.findByHostRole(hostName, requestId, stageId, actionName);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, entities.get(0).getStatus());
        java.lang.Thread thread = new java.lang.Thread() {
            @java.lang.Override
            public void run() {
                org.apache.ambari.server.actionmanager.Stage stage1 = db.getStage("23-31");
                stage1.setHostRoleStatus(hostName, actionName, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
                db.hostRoleScheduled(stage1, hostName, actionName);
                injector.getInstance(javax.persistence.EntityManager.class).clear();
            }
        };
        thread.start();
        thread.join();
        injector.getInstance(javax.persistence.EntityManager.class).clear();
        entities = hostRoleCommandDAO.findByHostRole(hostName, requestId, stageId, actionName);
        org.junit.Assert.assertEquals("Concurrent update failed", org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, entities.get(0).getStatus());
    }

    @org.junit.Test
    public void testServerActionScheduled() throws java.lang.InterruptedException, org.apache.ambari.server.AmbariException {
        populateActionDBWithServerAction(db, null, requestId, stageId);
        final java.lang.String roleName = org.apache.ambari.server.Role.AMBARI_SERVER_ACTION.toString();
        org.apache.ambari.server.actionmanager.Stage stage = db.getStage(org.apache.ambari.server.utils.StageUtils.getActionId(requestId, stageId));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, stage.getHostRoleStatus(null, roleName));
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> entities = hostRoleCommandDAO.findByHostRole(null, requestId, stageId, roleName);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, entities.get(0).getStatus());
        stage.setHostRoleStatus(null, roleName, org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED);
        entities = hostRoleCommandDAO.findByHostRole(null, requestId, stageId, roleName);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, stage.getHostRoleStatus(null, roleName));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, entities.get(0).getStatus());
        db.hostRoleScheduled(stage, null, roleName);
        entities = hostRoleCommandDAO.findByHostRole(null, requestId, stageId, roleName);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, entities.get(0).getStatus());
        java.lang.Thread thread = new java.lang.Thread() {
            @java.lang.Override
            public void run() {
                org.apache.ambari.server.actionmanager.Stage stage1 = db.getStage("23-31");
                stage1.setHostRoleStatus(null, roleName, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
                db.hostRoleScheduled(stage1, null, roleName);
                injector.getInstance(javax.persistence.EntityManager.class).clear();
            }
        };
        thread.start();
        thread.join();
        injector.getInstance(javax.persistence.EntityManager.class).clear();
        entities = hostRoleCommandDAO.findByHostRole(null, requestId, stageId, roleName);
        org.junit.Assert.assertEquals("Concurrent update failed", org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, entities.get(0).getStatus());
    }

    @org.junit.Test
    public void testUpdateHostRole() throws java.lang.Exception {
        populateActionDB(db, hostName, requestId, stageId, false);
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (int i = 0; i < 50000; i++) {
            sb.append("1234567890");
        }
        java.lang.String largeString = sb.toString();
        org.apache.ambari.server.agent.CommandReport commandReport = new org.apache.ambari.server.agent.CommandReport();
        commandReport.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.toString());
        commandReport.setStdOut(largeString);
        commandReport.setStdErr(largeString);
        commandReport.setStructuredOut(largeString);
        commandReport.setExitCode(123);
        db.updateHostRoleState(hostName, requestId, stageId, org.apache.ambari.server.Role.HBASE_MASTER.toString(), commandReport);
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> commandEntities = hostRoleCommandDAO.findByHostRole(hostName, requestId, stageId, org.apache.ambari.server.Role.HBASE_MASTER.toString());
        org.junit.Assert.assertEquals(1, commandEntities.size());
        org.apache.ambari.server.orm.entities.HostRoleCommandEntity commandEntity = commandEntities.get(0);
        org.apache.ambari.server.actionmanager.HostRoleCommand command = db.getTask(commandEntity.getTaskId());
        org.junit.Assert.assertNotNull(command);
        org.junit.Assert.assertEquals(largeString, command.getStdout());
        org.junit.Assert.assertEquals(largeString, command.getStructuredOut());
        org.junit.Assert.assertTrue(command.getEndTime() != (-1));
    }

    @org.junit.Test
    public void testUpdateHostRoleTimeoutRetry() throws java.lang.Exception {
        populateActionDB(db, hostName, requestId, stageId, true);
        org.apache.ambari.server.agent.CommandReport commandReport = new org.apache.ambari.server.agent.CommandReport();
        commandReport.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT.toString());
        commandReport.setStdOut("");
        commandReport.setStdErr("");
        commandReport.setStructuredOut("");
        commandReport.setExitCode(123);
        db.updateHostRoleState(hostName, requestId, stageId, org.apache.ambari.server.Role.HBASE_MASTER.toString(), commandReport);
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> commandEntities = hostRoleCommandDAO.findByHostRole(hostName, requestId, stageId, org.apache.ambari.server.Role.HBASE_MASTER.toString());
        org.apache.ambari.server.orm.entities.HostRoleCommandEntity commandEntity = commandEntities.get(0);
        org.apache.ambari.server.actionmanager.HostRoleCommand command = db.getTask(commandEntity.getTaskId());
        org.junit.Assert.assertNotNull(command);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING_TIMEDOUT, command.getStatus());
    }

    @org.junit.Test
    public void testGetRequestsByStatus() throws org.apache.ambari.server.AmbariException {
        java.util.List<java.lang.Long> requestIds = new java.util.ArrayList<>();
        requestIds.add(requestId + 1);
        requestIds.add(requestId);
        populateActionDB(db, hostName, requestId, stageId, false);
        clusters.addHost("host2");
        populateActionDB(db, hostName, requestId + 1, stageId, false);
        java.util.List<java.lang.Long> requestIdsResult = db.getRequestsByStatus(null, org.apache.ambari.server.api.services.BaseRequest.DEFAULT_PAGE_SIZE, false);
        org.junit.Assert.assertNotNull("List of request IDs is null", requestIdsResult);
        org.junit.Assert.assertEquals("Request IDs not matches", requestIds, requestIdsResult);
    }

    @org.junit.Test
    public void testGetCompletedRequests() throws org.apache.ambari.server.AmbariException {
        java.util.List<java.lang.Long> requestIds = new java.util.ArrayList<>();
        requestIds.add(requestId);
        requestIds.add(requestId + 1);
        populateActionDBWithCompletedRequest(db, hostName, requestId, stageId);
        java.util.List<java.lang.Long> requestIdsResult = db.getRequestsByStatus(org.apache.ambari.server.actionmanager.RequestStatus.COMPLETED, org.apache.ambari.server.api.services.BaseRequest.DEFAULT_PAGE_SIZE, false);
        org.junit.Assert.assertEquals(1, requestIdsResult.size());
        org.junit.Assert.assertTrue(requestIdsResult.contains(requestId));
        populateActionDBWithPartiallyCompletedRequest(db, hostName, requestId + 1, stageId);
        requestIdsResult = db.getRequestsByStatus(org.apache.ambari.server.actionmanager.RequestStatus.COMPLETED, org.apache.ambari.server.api.services.BaseRequest.DEFAULT_PAGE_SIZE, false);
        org.junit.Assert.assertEquals(1, requestIdsResult.size());
        org.junit.Assert.assertTrue(requestIdsResult.contains(requestId));
    }

    @org.junit.Test
    public void testGetRequestsByStatusWithParams() throws org.apache.ambari.server.AmbariException {
        java.util.List<java.lang.Long> ids = new java.util.ArrayList<>();
        for (long l = 1; l <= 10; l++) {
            ids.add(l);
        }
        for (java.lang.Long id : ids) {
            populateActionDB(db, hostName, id, stageId, false);
        }
        java.util.List<java.lang.Long> expected = null;
        java.util.List<java.lang.Long> actual = null;
        actual = db.getRequestsByStatus(null, org.apache.ambari.server.api.services.BaseRequest.DEFAULT_PAGE_SIZE, false);
        expected = reverse(new java.util.ArrayList<>(ids));
        org.junit.Assert.assertEquals("Request IDs not matches", expected, actual);
        actual = db.getRequestsByStatus(null, 4, false);
        expected = reverse(new java.util.ArrayList<>(ids.subList(ids.size() - 4, ids.size())));
        org.junit.Assert.assertEquals("Request IDs not matches", expected, actual);
        actual = db.getRequestsByStatus(null, 7, true);
        expected = new java.util.ArrayList<>(ids.subList(0, 7));
        org.junit.Assert.assertEquals("Request IDs not matches", expected, actual);
    }

    private <T> java.util.List<T> reverse(java.util.List<T> list) {
        java.util.List<T> result = new java.util.ArrayList<>(list);
        java.util.Collections.reverse(result);
        return result;
    }

    @org.junit.Test
    public void testAbortRequest() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.actionmanager.Stage s = stageFactory.createNew(requestId, "/a/b", "cluster1", 1L, "action db accessor test", "commandParamsStage", "hostParamsStage");
        s.setStageId(stageId);
        clusters.addHost("host2");
        clusters.addHost("host3");
        clusters.addHost("host4");
        s.addHostRoleExecutionCommand("host1", org.apache.ambari.server.Role.HBASE_MASTER, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent(org.apache.ambari.server.Role.HBASE_MASTER.toString(), "host1", java.lang.System.currentTimeMillis()), "cluster1", "HBASE", false, false);
        s.addHostRoleExecutionCommand("host2", org.apache.ambari.server.Role.HBASE_MASTER, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent(org.apache.ambari.server.Role.HBASE_MASTER.toString(), "host2", java.lang.System.currentTimeMillis()), "cluster1", "HBASE", false, false);
        s.addHostRoleExecutionCommand("host3", org.apache.ambari.server.Role.HBASE_REGIONSERVER, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent(org.apache.ambari.server.Role.HBASE_REGIONSERVER.toString(), "host3", java.lang.System.currentTimeMillis()), "cluster1", "HBASE", false, false);
        s.addHostRoleExecutionCommand("host4", org.apache.ambari.server.Role.HBASE_REGIONSERVER, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent(org.apache.ambari.server.Role.HBASE_REGIONSERVER.toString(), "host4", java.lang.System.currentTimeMillis()), "cluster1", "HBASE", false, false);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = new java.util.ArrayList<>();
        stages.add(s);
        s.getOrderedHostRoleCommands().get(0).setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING);
        s.getOrderedHostRoleCommands().get(1).setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS);
        s.getOrderedHostRoleCommands().get(2).setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED);
        org.apache.ambari.server.actionmanager.HostRoleCommand cmd = s.getOrderedHostRoleCommands().get(3);
        java.lang.String hostName = cmd.getHostName();
        cmd.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
        org.apache.ambari.server.actionmanager.Request request = new org.apache.ambari.server.actionmanager.Request(stages, "", clusters);
        request.setClusterHostInfo("clusterHostInfo");
        db.persistActions(request);
        db.abortOperation(requestId);
        java.util.List<java.lang.Long> aborted = new java.util.ArrayList<>();
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> commands = db.getRequestTasks(requestId);
        for (org.apache.ambari.server.actionmanager.HostRoleCommand command : commands) {
            if (command.getHostName().equals(hostName)) {
                org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, command.getStatus());
            } else {
                org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, command.getStatus());
                aborted.add(command.getTaskId());
            }
        }
        db.resubmitTasks(aborted);
        commands = db.getRequestTasks(requestId);
        for (org.apache.ambari.server.actionmanager.HostRoleCommand command : commands) {
            if (command.getHostName().equals(hostName)) {
                org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, command.getStatus());
            } else {
                org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, command.getStatus());
            }
        }
    }

    @org.junit.Test
    public void testEntitiesCreatedWithIDs() throws java.lang.Exception {
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = new java.util.ArrayList<>();
        org.apache.ambari.server.actionmanager.Stage stage = createStubStage(hostName, requestId, stageId, false);
        stages.add(stage);
        org.apache.ambari.server.actionmanager.Request request = new org.apache.ambari.server.actionmanager.Request(stages, "", clusters);
        db.persistActions(request);
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> commandEntities = hostRoleCommandDAO.findByRequest(requestId);
        org.junit.Assert.assertEquals(2, commandEntities.size());
        for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity entity : commandEntities) {
            org.junit.Assert.assertEquals(java.lang.Long.valueOf(requestId), entity.getRequestId());
            org.junit.Assert.assertEquals(java.lang.Long.valueOf(stageId), entity.getStageId());
        }
    }

    private static class TestActionDBAccessorModule extends com.google.inject.AbstractModule {
        @java.lang.Override
        protected void configure() {
            bind(org.apache.ambari.server.orm.DBAccessor.class).to(org.apache.ambari.server.actionmanager.TestActionDBAccessorImpl.TestDBAccessorImpl.class);
        }
    }

    @com.google.inject.Singleton
    static class TestDBAccessorImpl extends org.apache.ambari.server.orm.DBAccessorImpl {
        private org.apache.ambari.server.orm.DBAccessor.DbType dbTypeOverride = null;

        @com.google.inject.Inject
        public TestDBAccessorImpl(org.apache.ambari.server.configuration.Configuration configuration) {
            super(configuration);
        }

        @java.lang.Override
        public org.apache.ambari.server.orm.DBAccessor.DbType getDbType() {
            if (dbTypeOverride != null) {
                return dbTypeOverride;
            }
            return super.getDbType();
        }

        public void setDbTypeOverride(org.apache.ambari.server.orm.DBAccessor.DbType dbTypeOverride) {
            this.dbTypeOverride = dbTypeOverride;
        }
    }

    @org.junit.Test
    public void testGet1000TasksFromOracleDB() throws java.lang.Exception {
        org.apache.ambari.server.actionmanager.Stage s = stageFactory.createNew(requestId, "/a/b", "cluster1", 1L, "action db accessor test", "commandParamsStage", "hostParamsStage");
        s.setStageId(stageId);
        for (int i = 1000; i < 2002; i++) {
            java.lang.String host = "host" + i;
            clusters.addHost(host);
            s.addHostRoleExecutionCommand("host" + i, org.apache.ambari.server.Role.HBASE_MASTER, org.apache.ambari.server.RoleCommand.START, null, "cluster1", "HBASE", false, false);
        }
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = new java.util.ArrayList<>();
        stages.add(s);
        org.apache.ambari.server.actionmanager.Request request = new org.apache.ambari.server.actionmanager.Request(stages, "", clusters);
        request.setClusterHostInfo("clusterHostInfo");
        db.persistActions(request);
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> entities = hostRoleCommandDAO.findByRequest(request.getRequestId());
        org.junit.Assert.assertEquals(1002, entities.size());
        java.util.List<java.lang.Long> taskIds = new java.util.ArrayList<>();
        for (org.apache.ambari.server.orm.entities.HostRoleCommandEntity entity : entities) {
            taskIds.add(entity.getTaskId());
        }
        org.apache.ambari.server.actionmanager.TestActionDBAccessorImpl.TestDBAccessorImpl testDBAccessorImpl = ((org.apache.ambari.server.actionmanager.TestActionDBAccessorImpl.TestDBAccessorImpl) (injector.getInstance(org.apache.ambari.server.orm.DBAccessor.class)));
        testDBAccessorImpl.setDbTypeOverride(org.apache.ambari.server.orm.DBAccessor.DbType.ORACLE);
        org.junit.Assert.assertEquals(org.apache.ambari.server.orm.DBAccessor.DbType.ORACLE, injector.getInstance(org.apache.ambari.server.orm.DBAccessor.class).getDbType());
        entities = hostRoleCommandDAO.findByPKs(taskIds);
        org.junit.Assert.assertEquals("Tasks returned from DB match the ones created", taskIds.size(), entities.size());
    }

    private void populateActionDB(org.apache.ambari.server.actionmanager.ActionDBAccessor db, java.lang.String hostname, long requestId, long stageId, boolean retryAllowed) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.actionmanager.Stage s = createStubStage(hostname, requestId, stageId, retryAllowed);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = new java.util.ArrayList<>();
        stages.add(s);
        org.apache.ambari.server.actionmanager.Request request = new org.apache.ambari.server.actionmanager.Request(stages, "", clusters);
        db.persistActions(request);
    }

    private void populateActionDBMultipleStages(int numberOfStages, org.apache.ambari.server.actionmanager.ActionDBAccessor db, java.lang.String hostname, long requestId, long stageId) throws org.apache.ambari.server.AmbariException {
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = new java.util.ArrayList<>();
        for (int i = 0; i < numberOfStages; i++) {
            org.apache.ambari.server.actionmanager.Stage stage = createStubStage(hostname, requestId, stageId + i, false);
            stages.add(stage);
        }
        org.apache.ambari.server.actionmanager.Request request = new org.apache.ambari.server.actionmanager.Request(stages, "", clusters);
        db.persistActions(request);
    }

    private void populateActionDBWithCompletedRequest(org.apache.ambari.server.actionmanager.ActionDBAccessor db, java.lang.String hostname, long requestId, long stageId) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.actionmanager.Stage s = createStubStage(hostname, requestId, stageId, false);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = new java.util.ArrayList<>();
        stages.add(s);
        org.apache.ambari.server.actionmanager.Request request = new org.apache.ambari.server.actionmanager.Request(stages, "", clusters);
        s.setHostRoleStatus(hostname, org.apache.ambari.server.Role.HBASE_REGIONSERVER.name(), org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
        s.setHostRoleStatus(hostname, org.apache.ambari.server.Role.HBASE_MASTER.name(), org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
        db.persistActions(request);
    }

    private void populateActionDBWithPartiallyCompletedRequest(org.apache.ambari.server.actionmanager.ActionDBAccessor db, java.lang.String hostname, long requestId, long stageId) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.actionmanager.Stage s = createStubStage(hostname, requestId, stageId, false);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = new java.util.ArrayList<>();
        stages.add(s);
        org.apache.ambari.server.actionmanager.Request request = new org.apache.ambari.server.actionmanager.Request(stages, "", clusters);
        s.setHostRoleStatus(hostname, org.apache.ambari.server.Role.HBASE_REGIONSERVER.name(), org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING);
        s.setHostRoleStatus(hostname, org.apache.ambari.server.Role.HBASE_MASTER.name(), org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
        db.persistActions(request);
    }

    private org.apache.ambari.server.actionmanager.Stage createStubStage(java.lang.String hostname, long requestId, long stageId, boolean retryAllowed) {
        org.apache.ambari.server.actionmanager.Stage s = stageFactory.createNew(requestId, "/a/b", "cluster1", 1L, "action db accessor test", "commandParamsStage", "hostParamsStage");
        s.setStageId(stageId);
        s.addHostRoleExecutionCommand(hostname, org.apache.ambari.server.Role.HBASE_MASTER, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent(org.apache.ambari.server.Role.HBASE_MASTER.toString(), hostname, java.lang.System.currentTimeMillis()), "cluster1", "HBASE", retryAllowed, false);
        s.addHostRoleExecutionCommand(hostname, org.apache.ambari.server.Role.HBASE_REGIONSERVER, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent(org.apache.ambari.server.Role.HBASE_REGIONSERVER.toString(), hostname, java.lang.System.currentTimeMillis()), "cluster1", "HBASE", false, false);
        return s;
    }

    private void populateActionDBWithCustomAction(org.apache.ambari.server.actionmanager.ActionDBAccessor db, java.lang.String hostname, long requestId, long stageId) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.actionmanager.Stage s = stageFactory.createNew(requestId, "/a/b", "cluster1", 1L, "action db accessor test", "commandParamsStage", "hostParamsStage");
        s.setStageId(stageId);
        s.addHostRoleExecutionCommand(hostname, org.apache.ambari.server.Role.valueOf(actionName), org.apache.ambari.server.RoleCommand.ACTIONEXECUTE, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent(org.apache.ambari.server.Role.HBASE_MASTER.toString(), hostname, java.lang.System.currentTimeMillis()), "cluster1", "HBASE", false, false);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = new java.util.ArrayList<>();
        stages.add(s);
        final org.apache.ambari.server.controller.internal.RequestResourceFilter resourceFilter = new org.apache.ambari.server.controller.internal.RequestResourceFilter("HBASE", "HBASE_MASTER", null);
        java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> resourceFilters = new java.util.ArrayList<org.apache.ambari.server.controller.internal.RequestResourceFilter>() {
            {
                add(resourceFilter);
            }
        };
        org.apache.ambari.server.actionmanager.Request request = new org.apache.ambari.server.actionmanager.Request(stages, "", clusters);
        request.setClusterHostInfo("");
        db.persistActions(request);
    }

    private void populateActionDBWithServerAction(org.apache.ambari.server.actionmanager.ActionDBAccessor db, java.lang.String hostname, long requestId, long stageId) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.actionmanager.Stage s = stageFactory.createNew(requestId, "/a/b", "cluster1", 1L, "action db accessor test", "commandParamsStage", "hostParamsStage");
        s.setStageId(stageId);
        s.addServerActionCommand(serverActionName, null, org.apache.ambari.server.Role.AMBARI_SERVER_ACTION, org.apache.ambari.server.RoleCommand.ACTIONEXECUTE, clusterName, null, null, "command details", null, 300, false, false);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = new java.util.ArrayList<>();
        stages.add(s);
        org.apache.ambari.server.actionmanager.Request request = new org.apache.ambari.server.actionmanager.Request(stages, "", clusters);
        request.setClusterHostInfo("");
        db.persistActions(request);
    }
}