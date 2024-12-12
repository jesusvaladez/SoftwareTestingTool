package org.apache.ambari.server.actionmanager;
import com.google.inject.persist.UnitOfWork;
import org.apache.commons.collections.CollectionUtils;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
public class TestActionManager {
    private long requestId = 23;

    private long stageId = 31;

    private com.google.inject.Injector injector;

    private java.lang.String hostname = "host1";

    private java.lang.String clusterName = "cluster1";

    private org.apache.ambari.server.state.Clusters clusters;

    private com.google.inject.persist.UnitOfWork unitOfWork;

    private org.apache.ambari.server.actionmanager.StageFactory stageFactory;

    @org.junit.Before
    public void setup() throws org.apache.ambari.server.AmbariException {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
        org.apache.ambari.server.H2DatabaseCleaner.resetSequences(injector);
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        stageFactory = injector.getInstance(org.apache.ambari.server.actionmanager.StageFactory.class);
        clusters.addHost(hostname);
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId("HDP-0.1");
        clusters.addCluster(clusterName, stackId);
        unitOfWork = injector.getInstance(com.google.inject.persist.UnitOfWork.class);
        org.easymock.EasyMock.replay(injector.getInstance(org.apache.ambari.server.audit.AuditLogger.class));
    }

    @org.junit.After
    public void tearDown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    @org.junit.Test
    public void testActionResponse() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.actionmanager.ActionDBAccessor db = injector.getInstance(org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.class);
        org.apache.ambari.server.actionmanager.ActionManager am = injector.getInstance(org.apache.ambari.server.actionmanager.ActionManager.class);
        populateActionDB(db, hostname);
        org.apache.ambari.server.actionmanager.Stage stage = db.getAllStages(requestId).get(0);
        junit.framework.Assert.assertEquals(stageId, stage.getStageId());
        stage.setHostRoleStatus(hostname, "HBASE_MASTER", org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED);
        db.hostRoleScheduled(stage, hostname, "HBASE_MASTER");
        java.util.List<org.apache.ambari.server.agent.CommandReport> reports = new java.util.ArrayList<>();
        org.apache.ambari.server.agent.CommandReport cr = new org.apache.ambari.server.agent.CommandReport();
        cr.setTaskId(1);
        cr.setActionId(org.apache.ambari.server.utils.StageUtils.getActionId(requestId, stageId));
        cr.setRole("HBASE_MASTER");
        cr.setStatus("COMPLETED");
        cr.setStdErr("ERROR");
        cr.setStdOut("OUTPUT");
        cr.setStructuredOut("STRUCTURED_OUTPUT");
        cr.setExitCode(215);
        reports.add(cr);
        am.processTaskResponse(hostname, reports, org.apache.ambari.server.utils.CommandUtils.convertToTaskIdCommandMap(stage.getOrderedHostRoleCommands()));
        org.junit.Assert.assertEquals(215, am.getAction(requestId, stageId).getExitCode(hostname, "HBASE_MASTER"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, am.getAction(requestId, stageId).getHostRoleStatus(hostname, "HBASE_MASTER"));
        org.junit.Assert.assertEquals("ERROR", am.getAction(requestId, stageId).getHostRoleCommand(hostname, "HBASE_MASTER").getStderr());
        org.junit.Assert.assertEquals("OUTPUT", am.getAction(requestId, stageId).getHostRoleCommand(hostname, "HBASE_MASTER").getStdout());
        org.junit.Assert.assertEquals("STRUCTURED_OUTPUT", am.getAction(requestId, stageId).getHostRoleCommand(hostname, "HBASE_MASTER").getStructuredOut());
        org.junit.Assert.assertNotNull(db.getRequest(requestId));
        org.junit.Assert.assertFalse(db.getRequest(requestId).getEndTime() == (-1));
    }

    @org.junit.Test
    public void testActionResponsesUnsorted() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.actionmanager.ActionDBAccessor db = injector.getInstance(org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.class);
        org.apache.ambari.server.actionmanager.ActionManager am = injector.getInstance(org.apache.ambari.server.actionmanager.ActionManager.class);
        populateActionDBWithTwoCommands(db, hostname);
        org.apache.ambari.server.actionmanager.Stage stage = db.getAllStages(requestId).get(0);
        junit.framework.Assert.assertEquals(stageId, stage.getStageId());
        stage.setHostRoleStatus(hostname, "HBASE_MASTER", org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED);
        db.hostRoleScheduled(stage, hostname, "HBASE_MASTER");
        java.util.List<org.apache.ambari.server.agent.CommandReport> reports = new java.util.ArrayList<>();
        org.apache.ambari.server.agent.CommandReport cr = new org.apache.ambari.server.agent.CommandReport();
        cr.setTaskId(2);
        cr.setActionId(org.apache.ambari.server.utils.StageUtils.getActionId(requestId, stageId));
        cr.setRole("HBASE_REGIONSERVER");
        cr.setStatus("COMPLETED");
        cr.setStdErr("ERROR");
        cr.setStdOut("OUTPUT");
        cr.setStructuredOut("STRUCTURED_OUTPUT");
        cr.setExitCode(215);
        reports.add(cr);
        org.apache.ambari.server.agent.CommandReport cr2 = new org.apache.ambari.server.agent.CommandReport();
        cr2.setTaskId(1);
        cr2.setActionId(org.apache.ambari.server.utils.StageUtils.getActionId(requestId, stageId));
        cr2.setRole("HBASE_MASTER");
        cr2.setStatus("IN_PROGRESS");
        cr2.setStdErr("ERROR");
        cr2.setStdOut("OUTPUT");
        cr2.setStructuredOut("STRUCTURED_OUTPUT");
        cr2.setExitCode(215);
        reports.add(cr2);
        am.processTaskResponse(hostname, reports, org.apache.ambari.server.utils.CommandUtils.convertToTaskIdCommandMap(am.getTasks(java.util.Arrays.asList(new java.lang.Long[]{ 1L, 2L }))));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS, am.getAction(requestId, stageId).getHostRoleStatus(hostname, "HBASE_MASTER"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, am.getAction(requestId, stageId).getHostRoleStatus(hostname, "HBASE_REGIONSERVER"));
    }

    @org.junit.Test
    public void testLargeLogs() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.actionmanager.ActionDBAccessor db = injector.getInstance(org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.class);
        org.apache.ambari.server.actionmanager.ActionManager am = injector.getInstance(org.apache.ambari.server.actionmanager.ActionManager.class);
        populateActionDB(db, hostname);
        org.apache.ambari.server.actionmanager.Stage stage = db.getAllStages(requestId).get(0);
        junit.framework.Assert.assertEquals(stageId, stage.getStageId());
        stage.setHostRoleStatus(hostname, "HBASE_MASTER", org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED);
        db.hostRoleScheduled(stage, hostname, "HBASE_MASTER");
        java.util.List<org.apache.ambari.server.agent.CommandReport> reports = new java.util.ArrayList<>();
        org.apache.ambari.server.agent.CommandReport cr = new org.apache.ambari.server.agent.CommandReport();
        cr.setTaskId(1);
        cr.setActionId(org.apache.ambari.server.utils.StageUtils.getActionId(requestId, stageId));
        cr.setRole("HBASE_MASTER");
        cr.setStatus("COMPLETED");
        java.lang.String errLog = java.util.Arrays.toString(new byte[100000]);
        java.lang.String outLog = java.util.Arrays.toString(new byte[110000]);
        cr.setStdErr(errLog);
        cr.setStdOut(outLog);
        cr.setStructuredOut(outLog);
        cr.setExitCode(215);
        reports.add(cr);
        am.processTaskResponse(hostname, reports, org.apache.ambari.server.utils.CommandUtils.convertToTaskIdCommandMap(stage.getOrderedHostRoleCommands()));
        org.junit.Assert.assertEquals(215, am.getAction(requestId, stageId).getExitCode(hostname, "HBASE_MASTER"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, am.getAction(requestId, stageId).getHostRoleStatus(hostname, "HBASE_MASTER"));
        org.junit.Assert.assertEquals(errLog.length(), am.getAction(requestId, stageId).getHostRoleCommand(hostname, "HBASE_MASTER").getStderr().length());
        org.junit.Assert.assertEquals(outLog.length(), am.getAction(requestId, stageId).getHostRoleCommand(hostname, "HBASE_MASTER").getStdout().length());
        org.junit.Assert.assertEquals(outLog.length(), am.getAction(requestId, stageId).getHostRoleCommand(hostname, "HBASE_MASTER").getStructuredOut().length());
    }

    private void populateActionDB(org.apache.ambari.server.actionmanager.ActionDBAccessor db, java.lang.String hostname) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.actionmanager.Stage s = stageFactory.createNew(requestId, "/a/b", "cluster1", 1L, "action manager test", "commandParamsStage", "hostParamsStage");
        s.setStageId(stageId);
        s.addHostRoleExecutionCommand(hostname, org.apache.ambari.server.Role.HBASE_MASTER, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent(org.apache.ambari.server.Role.HBASE_MASTER.toString(), hostname, java.lang.System.currentTimeMillis()), "cluster1", "HBASE", false, false);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = new java.util.ArrayList<>();
        stages.add(s);
        org.apache.ambari.server.actionmanager.Request request = new org.apache.ambari.server.actionmanager.Request(stages, "clusterHostInfo", clusters);
        db.persistActions(request);
    }

    private void populateActionDBWithTwoCommands(org.apache.ambari.server.actionmanager.ActionDBAccessor db, java.lang.String hostname) throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.actionmanager.Stage s = stageFactory.createNew(requestId, "/a/b", "cluster1", 1L, "action manager test", "commandParamsStage", "hostParamsStage");
        s.setStageId(stageId);
        s.addHostRoleExecutionCommand(hostname, org.apache.ambari.server.Role.HBASE_MASTER, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent(org.apache.ambari.server.Role.HBASE_MASTER.toString(), hostname, java.lang.System.currentTimeMillis()), "cluster1", "HBASE", false, false);
        s.addHostRoleExecutionCommand(hostname, org.apache.ambari.server.Role.HBASE_REGIONSERVER, org.apache.ambari.server.RoleCommand.START, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostStartEvent(org.apache.ambari.server.Role.HBASE_REGIONSERVER.toString(), hostname, java.lang.System.currentTimeMillis()), "cluster1", "HBASE", false, false);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = new java.util.ArrayList<>();
        stages.add(s);
        org.apache.ambari.server.actionmanager.Request request = new org.apache.ambari.server.actionmanager.Request(stages, "clusterHostInfo", clusters);
        db.persistActions(request);
    }

    @org.junit.Ignore
    @org.junit.Test
    public void testCascadeDeleteStages() throws java.lang.Exception {
        org.apache.ambari.server.actionmanager.ActionDBAccessor db = injector.getInstance(org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.class);
        org.apache.ambari.server.actionmanager.ActionManager am = injector.getInstance(org.apache.ambari.server.actionmanager.ActionManager.class);
        populateActionDB(db, hostname);
        org.junit.Assert.assertEquals(1, clusters.getClusters().size());
        clusters.getCluster(clusterName);
        clusters.deleteCluster(clusterName);
        org.junit.Assert.assertEquals(0, clusters.getClusters().size());
    }

    @org.junit.Test
    public void testGetActions() throws java.lang.Exception {
        int requestId = 500;
        org.apache.ambari.server.actionmanager.ActionDBAccessor db = EasyMock.createStrictMock(org.apache.ambari.server.actionmanager.ActionDBAccessor.class);
        org.apache.ambari.server.state.Clusters clusters = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.actionmanager.Stage stage1 = EasyMock.createNiceMock(org.apache.ambari.server.actionmanager.Stage.class);
        org.apache.ambari.server.actionmanager.Stage stage2 = EasyMock.createNiceMock(org.apache.ambari.server.actionmanager.Stage.class);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> listStages = new java.util.ArrayList<>();
        listStages.add(stage1);
        listStages.add(stage2);
        EasyMock.expect(db.getLastPersistedRequestIdWhenInitialized()).andReturn(java.lang.Long.valueOf(1000));
        EasyMock.expect(db.getAllStages(requestId)).andReturn(listStages);
        EasyMock.replay(db, clusters);
        org.apache.ambari.server.actionmanager.ActionScheduler actionScheduler = new org.apache.ambari.server.actionmanager.ActionScheduler(0, 0, db, EasyMock.createNiceMock(org.apache.ambari.server.events.publishers.JPAEventPublisher.class));
        org.apache.ambari.server.actionmanager.ActionManager manager = new org.apache.ambari.server.actionmanager.ActionManager(db, injector.getInstance(org.apache.ambari.server.actionmanager.RequestFactory.class), actionScheduler);
        org.junit.Assert.assertSame(listStages, manager.getActions(requestId));
        EasyMock.verify(db, clusters);
    }

    @org.junit.Test
    public void testPersistCommandsWithStages() throws java.lang.Exception {
        org.apache.ambari.server.actionmanager.ActionDBAccessor db = injector.getInstance(org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.class);
        populateActionDBWithTwoCommands(db, hostname);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = db.getAllStages(requestId);
        org.junit.Assert.assertEquals(1, stages.size());
        org.apache.ambari.server.actionmanager.Stage stage = stages.get(0);
        org.apache.ambari.server.orm.entities.StageEntityPK pk = new org.apache.ambari.server.orm.entities.StageEntityPK();
        pk.setRequestId(stage.getRequestId());
        pk.setStageId(stage.getStageId());
        org.apache.ambari.server.orm.dao.StageDAO dao = injector.getInstance(org.apache.ambari.server.orm.dao.StageDAO.class);
        org.apache.ambari.server.orm.entities.StageEntity stageEntity = dao.findByPK(pk);
        org.junit.Assert.assertNotNull(stageEntity);
        java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> commandEntities = stageEntity.getHostRoleCommands();
        org.junit.Assert.assertTrue(org.apache.commons.collections.CollectionUtils.isNotEmpty(commandEntities));
    }
}