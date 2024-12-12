package org.apache.ambari.server.serveraction;
import org.easymock.IAnswer;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static org.easymock.EasyMock.anyBoolean;
import static org.easymock.EasyMock.anyInt;
import static org.easymock.EasyMock.anyLong;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
@org.junit.Ignore
public class ServerActionExecutorTest {
    private static final int MAX_CYCLE_ITERATIONS = 1000;

    private static final java.lang.String SERVER_HOST_NAME = org.apache.ambari.server.utils.StageUtils.getHostName();

    private static final java.lang.String CLUSTER_HOST_INFO = ((("{all_hosts=[" + org.apache.ambari.server.serveraction.ServerActionExecutorTest.SERVER_HOST_NAME) + "], slave_hosts=[") + org.apache.ambari.server.serveraction.ServerActionExecutorTest.SERVER_HOST_NAME) + "]}";

    private static com.google.inject.Injector injector;

    @com.google.inject.Inject
    static org.apache.ambari.server.actionmanager.StageFactory stageFactory;

    @org.junit.BeforeClass
    public static void beforeClass() throws java.lang.Exception {
        org.apache.ambari.server.serveraction.ServerActionExecutorTest.injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.serveraction.ServerActionExecutorTest.MockModule());
    }

    @org.junit.After
    public void tearDown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(org.apache.ambari.server.serveraction.ServerActionExecutorTest.injector);
    }

    @org.junit.Test
    public void testServerAction() throws java.lang.Exception {
        final org.apache.ambari.server.actionmanager.Request request = createMockRequest();
        final org.apache.ambari.server.actionmanager.Stage s = org.apache.ambari.server.serveraction.ServerActionExecutorTest.getStageWithServerAction(1, 977, null, "test", 300);
        final java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = new java.util.ArrayList<org.apache.ambari.server.actionmanager.Stage>() {
            {
                add(s);
            }
        };
        org.apache.ambari.server.actionmanager.ActionDBAccessor db = createMockActionDBAccessor(request, stages);
        org.apache.ambari.server.serveraction.ServerActionExecutor.init(org.apache.ambari.server.serveraction.ServerActionExecutorTest.injector);
        org.apache.ambari.server.serveraction.ServerActionExecutor executor = new org.apache.ambari.server.serveraction.ServerActionExecutor(db, 10000);
        s.getHostRoleCommand(org.apache.ambari.server.serveraction.ServerActionExecutorTest.SERVER_HOST_NAME, org.apache.ambari.server.Role.AMBARI_SERVER_ACTION.toString()).setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED);
        int cycleCount = 0;
        while ((!getTaskStatus(s).isCompletedState()) && ((cycleCount++) <= org.apache.ambari.server.serveraction.ServerActionExecutorTest.MAX_CYCLE_ITERATIONS)) {
            executor.doWork();
        } 
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, getTaskStatus(s));
    }

    @org.junit.Test
    public void testServerActionManualStage() throws java.lang.Exception {
        final org.apache.ambari.server.actionmanager.Request request = createMockRequest();
        org.apache.ambari.server.serveraction.ServerActionExecutorTest.stageFactory = EasyMock.createNiceMock(org.apache.ambari.server.actionmanager.StageFactory.class);
        final org.apache.ambari.server.actionmanager.Stage stage = org.apache.ambari.server.serveraction.ServerActionExecutorTest.stageFactory.createNew(1, "/tmp", "cluster1", 978, "context", "{\"host_param\":\"param_value\"}", "{\"stage_param\":\"param_value\"}");
        stage.addServerActionCommand(org.apache.ambari.server.serveraction.upgrades.ManualStageAction.class.getName(), null, org.apache.ambari.server.Role.AMBARI_SERVER_ACTION, org.apache.ambari.server.RoleCommand.EXECUTE, "cluster1", new org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent(org.apache.ambari.server.utils.StageUtils.getHostName(), java.lang.System.currentTimeMillis()), java.util.Collections.emptyMap(), null, null, 1200, false, false);
        final java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = new java.util.ArrayList<org.apache.ambari.server.actionmanager.Stage>() {
            {
                add(stage);
            }
        };
        org.apache.ambari.server.actionmanager.ActionDBAccessor db = createMockActionDBAccessor(request, stages);
        org.apache.ambari.server.serveraction.ServerActionExecutor.init(org.apache.ambari.server.serveraction.ServerActionExecutorTest.injector);
        org.apache.ambari.server.serveraction.ServerActionExecutor executor = new org.apache.ambari.server.serveraction.ServerActionExecutor(db, 10000);
        stage.getHostRoleCommand(org.apache.ambari.server.serveraction.ServerActionExecutorTest.SERVER_HOST_NAME, org.apache.ambari.server.Role.AMBARI_SERVER_ACTION.toString()).setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED);
        int cycleCount = 0;
        while ((!getTaskStatus(stage).isHoldingState()) && ((cycleCount++) <= org.apache.ambari.server.serveraction.ServerActionExecutorTest.MAX_CYCLE_ITERATIONS)) {
            executor.doWork();
        } 
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING, getTaskStatus(stage));
    }

    @org.junit.Test
    public void testServerActionTimeout() throws java.lang.Exception {
        final org.apache.ambari.server.actionmanager.Request request = createMockRequest();
        final org.apache.ambari.server.actionmanager.Stage s = org.apache.ambari.server.serveraction.ServerActionExecutorTest.getStageWithServerAction(1, 977, new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put(org.apache.ambari.server.serveraction.MockServerAction.PAYLOAD_FORCE_FAIL, "timeout");
            }
        }, "test", 1);
        final java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = new java.util.ArrayList<org.apache.ambari.server.actionmanager.Stage>() {
            {
                add(s);
            }
        };
        org.apache.ambari.server.actionmanager.ActionDBAccessor db = createMockActionDBAccessor(request, stages);
        org.apache.ambari.server.serveraction.ServerActionExecutor.init(org.apache.ambari.server.serveraction.ServerActionExecutorTest.injector);
        org.apache.ambari.server.serveraction.ServerActionExecutor executor = new org.apache.ambari.server.serveraction.ServerActionExecutor(db, 10000);
        s.getHostRoleCommand(org.apache.ambari.server.serveraction.ServerActionExecutorTest.SERVER_HOST_NAME, org.apache.ambari.server.Role.AMBARI_SERVER_ACTION.toString()).setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED);
        int cycleCount = 0;
        while ((!getTaskStatus(s).isCompletedState()) && ((cycleCount++) <= org.apache.ambari.server.serveraction.ServerActionExecutorTest.MAX_CYCLE_ITERATIONS)) {
            executor.doWork();
        } 
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT, getTaskStatus(s));
    }

    @org.junit.Test
    public void testServerActionFailedException() throws java.lang.Exception {
        final org.apache.ambari.server.actionmanager.Request request = createMockRequest();
        final org.apache.ambari.server.actionmanager.Stage s = org.apache.ambari.server.serveraction.ServerActionExecutorTest.getStageWithServerAction(1, 977, new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put(org.apache.ambari.server.serveraction.MockServerAction.PAYLOAD_FORCE_FAIL, "exception");
            }
        }, "test", 1);
        final java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = new java.util.ArrayList<org.apache.ambari.server.actionmanager.Stage>() {
            {
                add(s);
            }
        };
        org.apache.ambari.server.actionmanager.ActionDBAccessor db = createMockActionDBAccessor(request, stages);
        org.apache.ambari.server.serveraction.ServerActionExecutor.init(org.apache.ambari.server.serveraction.ServerActionExecutorTest.injector);
        org.apache.ambari.server.serveraction.ServerActionExecutor executor = new org.apache.ambari.server.serveraction.ServerActionExecutor(db, 10000);
        s.getHostRoleCommand(org.apache.ambari.server.serveraction.ServerActionExecutorTest.SERVER_HOST_NAME, org.apache.ambari.server.Role.AMBARI_SERVER_ACTION.toString()).setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED);
        int cycleCount = 0;
        while ((!getTaskStatus(s).isCompletedState()) && ((cycleCount++) <= org.apache.ambari.server.serveraction.ServerActionExecutorTest.MAX_CYCLE_ITERATIONS)) {
            executor.doWork();
        } 
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, getTaskStatus(s));
    }

    @org.junit.Test
    public void testServerActionFailedReport() throws java.lang.Exception {
        final org.apache.ambari.server.actionmanager.Request request = createMockRequest();
        final org.apache.ambari.server.actionmanager.Stage s = org.apache.ambari.server.serveraction.ServerActionExecutorTest.getStageWithServerAction(1, 977, new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put(org.apache.ambari.server.serveraction.MockServerAction.PAYLOAD_FORCE_FAIL, "report");
            }
        }, "test", 1);
        final java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = new java.util.ArrayList<org.apache.ambari.server.actionmanager.Stage>() {
            {
                add(s);
            }
        };
        org.apache.ambari.server.actionmanager.ActionDBAccessor db = createMockActionDBAccessor(request, stages);
        org.apache.ambari.server.serveraction.ServerActionExecutor.init(org.apache.ambari.server.serveraction.ServerActionExecutorTest.injector);
        org.apache.ambari.server.serveraction.ServerActionExecutor executor = new org.apache.ambari.server.serveraction.ServerActionExecutor(db, 10000);
        s.getHostRoleCommand(org.apache.ambari.server.serveraction.ServerActionExecutorTest.SERVER_HOST_NAME, org.apache.ambari.server.Role.AMBARI_SERVER_ACTION.toString()).setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED);
        int cycleCount = 0;
        while ((!getTaskStatus(s).isCompletedState()) && ((cycleCount++) <= org.apache.ambari.server.serveraction.ServerActionExecutorTest.MAX_CYCLE_ITERATIONS)) {
            executor.doWork();
        } 
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, getTaskStatus(s));
    }

    private org.apache.ambari.server.actionmanager.HostRoleStatus getTaskStatus(org.apache.ambari.server.actionmanager.Stage stage) {
        return stage.getHostRoleStatus(org.apache.ambari.server.serveraction.ServerActionExecutorTest.SERVER_HOST_NAME, "AMBARI_SERVER_ACTION");
    }

    private org.apache.ambari.server.actionmanager.Request createMockRequest() {
        org.apache.ambari.server.actionmanager.Request request = Mockito.mock(org.apache.ambari.server.actionmanager.Request.class);
        Mockito.when(request.isExclusive()).thenReturn(false);
        Mockito.when(request.getRequestId()).thenReturn(1L);
        return request;
    }

    private org.apache.ambari.server.actionmanager.ActionDBAccessor createMockActionDBAccessor(final org.apache.ambari.server.actionmanager.Request request, final java.util.List<org.apache.ambari.server.actionmanager.Stage> stages) {
        org.apache.ambari.server.actionmanager.ActionDBAccessor db = Mockito.mock(org.apache.ambari.server.actionmanager.ActionDBAccessor.class);
        Mockito.when(db.getFirstStageInProgressPerRequest()).thenReturn(stages);
        Mockito.doAnswer(new org.mockito.stubbing.Answer() {
            @java.lang.Override
            public java.lang.Object answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                org.apache.ambari.server.actionmanager.RequestStatus status = ((org.apache.ambari.server.actionmanager.RequestStatus) (invocation.getArguments()[0]));
                if (status == org.apache.ambari.server.actionmanager.RequestStatus.IN_PROGRESS) {
                    return java.util.Arrays.asList(request);
                } else {
                    return java.util.Collections.emptyList();
                }
            }
        }).when(db).getRequestsByStatus(Matchers.any(org.apache.ambari.server.actionmanager.RequestStatus.class), EasyMock.anyInt(), EasyMock.anyBoolean());
        Mockito.doAnswer(new org.mockito.stubbing.Answer() {
            @java.lang.Override
            public java.lang.Object answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                java.lang.String host = ((java.lang.String) (invocation.getArguments()[0]));
                java.lang.String role = ((java.lang.String) (invocation.getArguments()[3]));
                org.apache.ambari.server.agent.CommandReport commandReport = ((org.apache.ambari.server.agent.CommandReport) (invocation.getArguments()[4]));
                org.apache.ambari.server.actionmanager.HostRoleCommand command = stages.get(0).getHostRoleCommand(host, role);
                command.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.valueOf(commandReport.getStatus()));
                return null;
            }
        }).when(db).updateHostRoleState(Matchers.anyString(), EasyMock.anyLong(), EasyMock.anyLong(), Matchers.anyString(), Matchers.any(org.apache.ambari.server.agent.CommandReport.class));
        Mockito.doAnswer(new org.mockito.stubbing.Answer() {
            @java.lang.Override
            public java.lang.Object answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                java.lang.String host = ((java.lang.String) (invocation.getArguments()[0]));
                java.lang.String role = ((java.lang.String) (invocation.getArguments()[1]));
                org.apache.ambari.server.actionmanager.HostRoleStatus status = ((org.apache.ambari.server.actionmanager.HostRoleStatus) (invocation.getArguments()[2]));
                org.apache.ambari.server.actionmanager.HostRoleCommand task = stages.get(0).getHostRoleCommand(host, role);
                if (task.getStatus() == status) {
                    return java.util.Arrays.asList(task);
                } else {
                    return null;
                }
            }
        }).when(db).getTasksByHostRoleAndStatus(Matchers.anyString(), Matchers.anyString(), Matchers.any(org.apache.ambari.server.actionmanager.HostRoleStatus.class));
        return db;
    }

    private static org.apache.ambari.server.actionmanager.Stage getStageWithServerAction(final long requestId, final long stageId, final java.util.Map<java.lang.String, java.lang.String> payload, final java.lang.String requestContext, final int timeout) {
        org.apache.ambari.server.serveraction.ServerActionExecutorTest.stageFactory = EasyMock.createNiceMock(org.apache.ambari.server.actionmanager.StageFactory.class);
        EasyMock.expect(org.apache.ambari.server.serveraction.ServerActionExecutorTest.stageFactory.createNew(EasyMock.anyLong(), EasyMock.anyObject(java.lang.String.class), EasyMock.anyObject(java.lang.String.class), EasyMock.anyLong(), EasyMock.anyObject(java.lang.String.class), EasyMock.anyObject(java.lang.String.class), EasyMock.anyObject(java.lang.String.class))).andAnswer(new org.easymock.IAnswer<org.apache.ambari.server.actionmanager.Stage>() {
            @java.lang.Override
            public org.apache.ambari.server.actionmanager.Stage answer() throws java.lang.Throwable {
                org.apache.ambari.server.actionmanager.Stage stage = org.apache.ambari.server.serveraction.ServerActionExecutorTest.stageFactory.createNew(requestId, "/tmp", "cluster1", 1L, requestContext, "{}", "{}");
                stage.setStageId(stageId);
                stage.addServerActionCommand(org.apache.ambari.server.serveraction.MockServerAction.class.getName(), null, org.apache.ambari.server.Role.AMBARI_SERVER_ACTION, org.apache.ambari.server.RoleCommand.EXECUTE, "cluster1", new org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent(org.apache.ambari.server.serveraction.ServerActionExecutorTest.SERVER_HOST_NAME, java.lang.System.currentTimeMillis()), payload, "command detail", null, timeout, false, false);
                return stage;
            }
        });
        org.apache.ambari.server.actionmanager.Stage stage = org.apache.ambari.server.serveraction.ServerActionExecutorTest.stageFactory.createNew(requestId, "", "", 1L, "", "", "");
        return stage;
    }

    public static class MockModule extends com.google.inject.AbstractModule {
        @java.lang.Override
        protected void configure() {
            bind(org.apache.ambari.server.state.Clusters.class).toInstance(Mockito.mock(org.apache.ambari.server.state.Clusters.class));
        }
    }
}