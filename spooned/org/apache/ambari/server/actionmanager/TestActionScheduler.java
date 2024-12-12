package org.apache.ambari.server.actionmanager;
import com.google.inject.persist.UnitOfWork;
import javax.persistence.EntityManager;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyCollectionOf;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
public class TestActionScheduler {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(org.apache.ambari.server.actionmanager.TestActionScheduler.class);

    private static final java.lang.String CLUSTER_HOST_INFO = "{all_hosts=[c6403.ambari.apache.org," + (" c6401.ambari.apache.org, c6402.ambari.apache.org], slave_hosts=[c6403.ambari.apache.org," + " c6401.ambari.apache.org, c6402.ambari.apache.org]}");

    private static final java.lang.String CLUSTER_HOST_INFO_UPDATED = "{all_hosts=[c6401.ambari.apache.org," + (" c6402.ambari.apache.org], slave_hosts=[c6401.ambari.apache.org," + " c6402.ambari.apache.org]}");

    private final com.google.inject.Injector injector;

    private final java.lang.String hostname = "ahost.ambari.apache.org";

    private final java.lang.Long hostId = 1L;

    private final int MAX_CYCLE_ITERATIONS = 100;

    @com.google.inject.Inject
    private org.apache.ambari.server.actionmanager.HostRoleCommandFactory hostRoleCommandFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.actionmanager.StageFactory stageFactory;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    private com.google.inject.Provider<javax.persistence.EntityManager> entityManagerProviderMock = org.easymock.EasyMock.niceMock(com.google.inject.Provider.class);

    public TestActionScheduler() {
        injector = com.google.inject.Guice.createInjector(new org.apache.ambari.server.orm.InMemoryDefaultTestModule());
    }

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        injector.getInstance(org.apache.ambari.server.utils.StageUtils.class);
        injector.injectMembers(this);
        EasyMock.expect(entityManagerProviderMock.get()).andReturn(null);
        EasyMock.replay(entityManagerProviderMock);
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    @org.junit.Test
    public void testActionSchedule() throws java.lang.Exception {
        java.lang.reflect.Type type = new com.google.common.reflect.TypeToken<java.util.Map<java.lang.String, java.util.Set<java.lang.String>>>() {}.getType();
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> clusterHostInfo = org.apache.ambari.server.utils.StageUtils.getGson().fromJson(org.apache.ambari.server.actionmanager.TestActionScheduler.CLUSTER_HOST_INFO, type);
        java.util.Properties properties = new java.util.Properties();
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.state.Clusters fsm = Mockito.mock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster oneClusterMock = Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service serviceObj = Mockito.mock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.ServiceComponent scomp = Mockito.mock(org.apache.ambari.server.state.ServiceComponent.class);
        org.apache.ambari.server.state.ServiceComponentHost sch = Mockito.mock(org.apache.ambari.server.state.ServiceComponentHost.class);
        com.google.inject.persist.UnitOfWork unitOfWork = Mockito.mock(com.google.inject.persist.UnitOfWork.class);
        org.apache.ambari.server.events.publishers.AgentCommandsPublisher agentCommandsPublisher = Mockito.mock(org.apache.ambari.server.events.publishers.AgentCommandsPublisher.class);
        Mockito.when(fsm.getCluster(Matchers.anyString())).thenReturn(oneClusterMock);
        Mockito.when(fsm.getClusterById(Matchers.anyLong())).thenReturn(oneClusterMock);
        Mockito.when(oneClusterMock.getService(Matchers.anyString())).thenReturn(serviceObj);
        Mockito.when(oneClusterMock.getClusterId()).thenReturn(java.lang.Long.valueOf(1L));
        Mockito.when(serviceObj.getServiceComponent(Matchers.anyString())).thenReturn(scomp);
        Mockito.when(scomp.getServiceComponentHost(Matchers.anyString())).thenReturn(sch);
        Mockito.when(serviceObj.getCluster()).thenReturn(oneClusterMock);
        org.apache.ambari.server.state.Host host = Mockito.mock(org.apache.ambari.server.state.Host.class);
        java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> hosts = new java.util.HashMap<>();
        hosts.put(hostname, sch);
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = new org.apache.ambari.server.orm.entities.HostEntity();
        hostEntity.setHostName(hostname);
        hostEntity.setHostId(hostId);
        hostDAO.merge(hostEntity);
        Mockito.when(scomp.getServiceComponentHosts()).thenReturn(hosts);
        Mockito.when(fsm.getHost(Matchers.anyString())).thenReturn(host);
        Mockito.when(host.getState()).thenReturn(org.apache.ambari.server.state.HostState.HEALTHY);
        Mockito.when(host.getHostName()).thenReturn(hostname);
        Mockito.when(host.getHostId()).thenReturn(hostId);
        org.apache.ambari.server.actionmanager.ActionDBAccessor db = Mockito.mock(org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.class);
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAOMock = Mockito.mock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        org.mockito.Mockito.doNothing().when(hostRoleCommandDAOMock).publishTaskCreateEvent(Matchers.anyListOf(org.apache.ambari.server.actionmanager.HostRoleCommand.class));
        org.apache.ambari.server.actionmanager.Stage s = org.apache.ambari.server.utils.StageUtils.getATestStage(1, 977, hostname, "{\"host_param\":\"param_value\"}", "{\"stage_param\":\"param_value\"}");
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = java.util.Collections.singletonList(s);
        Mockito.when(db.getCommandsInProgressCount()).thenReturn(stages.size());
        Mockito.when(db.getFirstStageInProgressPerRequest()).thenReturn(stages);
        org.apache.ambari.server.orm.entities.RequestEntity request = Mockito.mock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        Mockito.when(request.isExclusive()).thenReturn(false);
        Mockito.when(request.getClusterHostInfo()).thenReturn(org.apache.ambari.server.actionmanager.TestActionScheduler.CLUSTER_HOST_INFO);
        Mockito.when(db.getRequestEntity(Matchers.anyLong())).thenReturn(request);
        org.apache.ambari.server.actionmanager.ActionScheduler scheduler = new org.apache.ambari.server.actionmanager.ActionScheduler(100, 5, db, fsm, 10000, new org.apache.ambari.server.controller.HostsMap(((java.lang.String) (null))), unitOfWork, null, conf, entityManagerProviderMock, hostRoleCommandDAOMock, null, agentCommandsPublisher);
        scheduler.setTaskTimeoutAdjustment(false);
        java.util.List<org.apache.ambari.server.agent.AgentCommand> commands = waitForQueueSize(hostId, agentCommandsPublisher, 1, scheduler);
        org.junit.Assert.assertTrue((commands != null) && (commands.size() == 1));
        org.apache.ambari.server.agent.AgentCommand scheduledCommand = commands.get(0);
        org.junit.Assert.assertTrue(scheduledCommand instanceof org.apache.ambari.server.agent.ExecutionCommand);
        org.junit.Assert.assertEquals("1-977", ((org.apache.ambari.server.agent.ExecutionCommand) (scheduledCommand)).getCommandId());
        org.junit.Assert.assertEquals(clusterHostInfo, ((org.apache.ambari.server.agent.ExecutionCommand) (scheduledCommand)).getClusterHostInfo());
        commands = waitForQueueSize(hostId, agentCommandsPublisher, 2, scheduler);
        org.junit.Assert.assertTrue((commands != null) && (commands.size() == 2));
        scheduledCommand = commands.get(1);
        org.junit.Assert.assertTrue(scheduledCommand instanceof org.apache.ambari.server.agent.ExecutionCommand);
        org.junit.Assert.assertEquals("1-977", ((org.apache.ambari.server.agent.ExecutionCommand) (scheduledCommand)).getCommandId());
        org.junit.Assert.assertEquals(clusterHostInfo, ((org.apache.ambari.server.agent.ExecutionCommand) (scheduledCommand)).getClusterHostInfo());
        s.setHostRoleStatus(hostname, "NAMENODE", org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
        scheduler.doWork();
        org.easymock.EasyMock.verify(entityManagerProviderMock);
    }

    private java.util.List<org.apache.ambari.server.agent.AgentCommand> waitForQueueSize(java.lang.Long hostId, org.apache.ambari.server.events.publishers.AgentCommandsPublisher agentCommandsPublisher, int expectedQueueSize, org.apache.ambari.server.actionmanager.ActionScheduler scheduler) throws org.apache.ambari.server.AmbariException {
        com.google.common.util.concurrent.AtomicLongMap<java.lang.Long> callCounterByHost = com.google.common.util.concurrent.AtomicLongMap.create(new java.util.HashMap<java.lang.Long, java.lang.Long>());
        java.util.concurrent.atomic.AtomicInteger methodCallCounter = new java.util.concurrent.atomic.AtomicInteger(0);
        org.mockito.ArgumentCaptor<com.google.common.collect.Multimap<java.lang.Long, org.apache.ambari.server.agent.AgentCommand>> executionCommandCaptor = org.mockito.ArgumentCaptor.forClass(((java.lang.Class) (com.google.common.collect.Multimap.class)));
        int counter = 0;
        resetAgentCommandPublisherMock(agentCommandsPublisher, callCounterByHost, methodCallCounter);
        while ((counter++) <= MAX_CYCLE_ITERATIONS) {
            long callsCount = callCounterByHost.get(hostId);
            if (callsCount == expectedQueueSize) {
                Mockito.verify(agentCommandsPublisher, Mockito.times(methodCallCounter.get())).sendAgentCommand(executionCommandCaptor.capture());
                com.google.common.collect.Multimap<java.lang.Long, org.apache.ambari.server.agent.AgentCommand> scheduledCommands = executionCommandCaptor.getValue();
                return new java.util.ArrayList<>(scheduledCommands.asMap().get(hostId));
            } else if (callsCount > expectedQueueSize) {
                org.junit.Assert.fail((("Expected size : " + expectedQueueSize) + " Actual size=") + counter);
            }
            try {
                scheduler.doWork();
            } catch (org.apache.ambari.server.AmbariException e) {
                org.junit.Assert.fail(("Ambari exception : " + e.getMessage()) + e.getStackTrace());
            }
        } 
        return null;
    }

    private void resetAgentCommandPublisherMock(org.apache.ambari.server.events.publishers.AgentCommandsPublisher agentCommandsPublisher, com.google.common.util.concurrent.AtomicLongMap<java.lang.Long> callCounterByHost, java.util.concurrent.atomic.AtomicInteger methodCallCounter) throws org.apache.ambari.server.AmbariException {
        Mockito.reset(agentCommandsPublisher);
        org.mockito.Mockito.doAnswer(i -> {
            Multimap<java.lang.Long, org.apache.ambari.server.agent.AgentCommand> host = ((Multimap<java.lang.Long, org.apache.ambari.server.agent.AgentCommand>) (i.getArguments()[0]));
            if (host != null) {
                host.asMap().forEach((h, l) -> callCounterByHost.addAndGet(h, 1));
            }
            methodCallCounter.incrementAndGet();
            return null;
        }).when(agentCommandsPublisher).sendAgentCommand(Matchers.any(com.google.common.collect.Multimap.class));
    }

    @org.junit.Test
    public void testActionScheduleWithDependencyOrderedCommandExecution() throws java.lang.Exception {
        java.lang.reflect.Type type = new com.google.common.reflect.TypeToken<java.util.Map<java.lang.String, java.util.Set<java.lang.String>>>() {}.getType();
        java.util.Map<java.lang.String, java.util.List<java.lang.String>> clusterHostInfo = org.apache.ambari.server.utils.StageUtils.getGson().fromJson(org.apache.ambari.server.actionmanager.TestActionScheduler.CLUSTER_HOST_INFO, type);
        java.util.Properties properties = new java.util.Properties();
        properties.setProperty("server.stage.command.execution_type", "DEPENDENCY_ORDERED");
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.state.Clusters fsm = Mockito.mock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster oneClusterMock = Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service serviceObj = Mockito.mock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.ServiceComponent scomp = Mockito.mock(org.apache.ambari.server.state.ServiceComponent.class);
        org.apache.ambari.server.state.ServiceComponentHost sch = Mockito.mock(org.apache.ambari.server.state.ServiceComponentHost.class);
        com.google.inject.persist.UnitOfWork unitOfWork = Mockito.mock(com.google.inject.persist.UnitOfWork.class);
        org.apache.ambari.server.events.publishers.AgentCommandsPublisher agentCommandsPublisher = Mockito.mock(org.apache.ambari.server.events.publishers.AgentCommandsPublisher.class);
        org.apache.ambari.server.metadata.RoleCommandOrderProvider rcoProvider = Mockito.mock(org.apache.ambari.server.metadata.RoleCommandOrderProvider.class);
        org.apache.ambari.server.metadata.RoleCommandOrder rco = Mockito.mock(org.apache.ambari.server.metadata.RoleCommandOrder.class);
        Mockito.when(fsm.getCluster(Matchers.anyString())).thenReturn(oneClusterMock);
        Mockito.when(fsm.getClusterById(Matchers.anyLong())).thenReturn(oneClusterMock);
        Mockito.when(oneClusterMock.getService(Matchers.anyString())).thenReturn(serviceObj);
        Mockito.when(oneClusterMock.getClusterId()).thenReturn(java.lang.Long.valueOf(1L));
        Mockito.when(serviceObj.getServiceComponent(Matchers.anyString())).thenReturn(scomp);
        Mockito.when(scomp.getServiceComponentHost(Matchers.anyString())).thenReturn(sch);
        Mockito.when(serviceObj.getCluster()).thenReturn(oneClusterMock);
        Mockito.when(rcoProvider.getRoleCommandOrder(1L)).thenReturn(rco);
        java.util.Map<org.apache.ambari.server.metadata.RoleCommandPair, java.util.Set<org.apache.ambari.server.metadata.RoleCommandPair>> roleCommandDependencies = new java.util.HashMap();
        org.apache.ambari.server.metadata.RoleCommandPair roleCommand = new org.apache.ambari.server.metadata.RoleCommandPair(org.apache.ambari.server.Role.valueOf("NAMENODE"), org.apache.ambari.server.RoleCommand.INSTALL);
        java.util.Set<org.apache.ambari.server.metadata.RoleCommandPair> namenodeInstallDependencies = new java.util.HashSet<>();
        namenodeInstallDependencies.add(roleCommand);
        roleCommandDependencies.put(roleCommand, namenodeInstallDependencies);
        Mockito.when(rco.getDependencies()).thenReturn(roleCommandDependencies);
        org.apache.ambari.server.state.Host host = Mockito.mock(org.apache.ambari.server.state.Host.class);
        java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> hosts = new java.util.HashMap<>();
        hosts.put(hostname, sch);
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = new org.apache.ambari.server.orm.entities.HostEntity();
        hostEntity.setHostName(hostname);
        hostEntity.setHostId(hostId);
        hostDAO.merge(hostEntity);
        Mockito.when(scomp.getServiceComponentHosts()).thenReturn(hosts);
        Mockito.when(fsm.getHost(Matchers.anyString())).thenReturn(host);
        Mockito.when(host.getState()).thenReturn(org.apache.ambari.server.state.HostState.HEALTHY);
        Mockito.when(host.getHostName()).thenReturn(hostname);
        Mockito.when(host.getHostId()).thenReturn(hostId);
        org.apache.ambari.server.actionmanager.ActionDBAccessor db = Mockito.mock(org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.class);
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAOMock = Mockito.mock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        org.mockito.Mockito.doNothing().when(hostRoleCommandDAOMock).publishTaskCreateEvent(Matchers.anyListOf(org.apache.ambari.server.actionmanager.HostRoleCommand.class));
        org.apache.ambari.server.actionmanager.Stage s = org.apache.ambari.server.utils.StageUtils.getATestStage(1, 977, hostname, "{\"host_param\":\"param_value\"}", "{\"stage_param\":\"param_value\"}");
        s.setCommandExecutionType(org.apache.ambari.server.actionmanager.CommandExecutionType.DEPENDENCY_ORDERED);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = java.util.Collections.singletonList(s);
        Mockito.when(db.getCommandsInProgressCount()).thenReturn(stages.size());
        Mockito.when(db.getFirstStageInProgressPerRequest()).thenReturn(stages);
        org.apache.ambari.server.orm.entities.RequestEntity request = Mockito.mock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        Mockito.when(request.isExclusive()).thenReturn(false);
        Mockito.when(request.getClusterHostInfo()).thenReturn(org.apache.ambari.server.actionmanager.TestActionScheduler.CLUSTER_HOST_INFO);
        Mockito.when(db.getRequestEntity(Matchers.anyLong())).thenReturn(request);
        org.apache.ambari.server.actionmanager.ActionScheduler scheduler = new org.apache.ambari.server.actionmanager.ActionScheduler(100, 5, db, fsm, 10000, new org.apache.ambari.server.controller.HostsMap(((java.lang.String) (null))), unitOfWork, null, conf, entityManagerProviderMock, hostRoleCommandDAOMock, null, rcoProvider, agentCommandsPublisher);
        scheduler.setTaskTimeoutAdjustment(false);
        java.util.List<org.apache.ambari.server.agent.AgentCommand> commands = waitForQueueSize(hostId, agentCommandsPublisher, 1, scheduler);
        org.junit.Assert.assertTrue((commands != null) && (commands.size() == 1));
        org.apache.ambari.server.agent.AgentCommand scheduledCommand = commands.get(0);
        org.junit.Assert.assertTrue(scheduledCommand instanceof org.apache.ambari.server.agent.ExecutionCommand);
        org.junit.Assert.assertEquals("1-977", ((org.apache.ambari.server.agent.ExecutionCommand) (scheduledCommand)).getCommandId());
        org.junit.Assert.assertEquals(clusterHostInfo, ((org.apache.ambari.server.agent.ExecutionCommand) (scheduledCommand)).getClusterHostInfo());
        commands = waitForQueueSize(hostId, agentCommandsPublisher, 2, scheduler);
        org.junit.Assert.assertTrue((commands != null) && (commands.size() == 2));
        scheduledCommand = commands.get(1);
        org.junit.Assert.assertTrue(scheduledCommand instanceof org.apache.ambari.server.agent.ExecutionCommand);
        org.junit.Assert.assertEquals("1-977", ((org.apache.ambari.server.agent.ExecutionCommand) (scheduledCommand)).getCommandId());
        org.junit.Assert.assertEquals(clusterHostInfo, ((org.apache.ambari.server.agent.ExecutionCommand) (scheduledCommand)).getClusterHostInfo());
        s.setHostRoleStatus(hostname, "NAMENODE", org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
        scheduler.doWork();
        org.easymock.EasyMock.verify(entityManagerProviderMock);
    }

    @org.junit.Test
    public void testActionTimeout() throws java.lang.Exception {
        java.util.Properties properties = new java.util.Properties();
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.state.Clusters fsm = Mockito.mock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster oneClusterMock = Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service serviceObj = Mockito.mock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.ServiceComponent scomp = Mockito.mock(org.apache.ambari.server.state.ServiceComponent.class);
        org.apache.ambari.server.state.ServiceComponentHost sch = Mockito.mock(org.apache.ambari.server.state.ServiceComponentHost.class);
        com.google.inject.persist.UnitOfWork unitOfWork = Mockito.mock(com.google.inject.persist.UnitOfWork.class);
        org.apache.ambari.server.events.publishers.AgentCommandsPublisher agentCommandsPublisher = Mockito.mock(org.apache.ambari.server.events.publishers.AgentCommandsPublisher.class);
        Mockito.when(fsm.getCluster(Matchers.anyString())).thenReturn(oneClusterMock);
        Mockito.when(oneClusterMock.getService(Matchers.anyString())).thenReturn(serviceObj);
        Mockito.when(serviceObj.getServiceComponent(Matchers.anyString())).thenReturn(scomp);
        Mockito.when(scomp.getServiceComponentHost(Matchers.anyString())).thenReturn(sch);
        Mockito.when(serviceObj.getCluster()).thenReturn(oneClusterMock);
        org.apache.ambari.server.state.Host host = Mockito.mock(org.apache.ambari.server.state.Host.class);
        java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> hosts = new java.util.HashMap<>();
        hosts.put(hostname, sch);
        Mockito.when(scomp.getServiceComponentHosts()).thenReturn(hosts);
        Mockito.when(fsm.getHost(Matchers.anyString())).thenReturn(host);
        Mockito.when(host.getState()).thenReturn(org.apache.ambari.server.state.HostState.HEALTHY);
        Mockito.when(host.getHostName()).thenReturn(hostname);
        Mockito.when(host.getHostId()).thenReturn(hostId);
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = new org.apache.ambari.server.orm.entities.HostEntity();
        hostEntity.setHostName(hostname);
        hostEntity.setHostId(hostId);
        hostDAO.create(hostEntity);
        final org.apache.ambari.server.actionmanager.Stage s = org.apache.ambari.server.utils.StageUtils.getATestStage(1, 977, hostname, "{\"host_param\":\"param_value\"}", "{\"stage_param\":\"param_value\"}");
        s.addHostRoleExecutionCommand(hostname, org.apache.ambari.server.Role.SECONDARY_NAMENODE, org.apache.ambari.server.RoleCommand.INSTALL, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostInstallEvent("SECONDARY_NAMENODE", hostname, java.lang.System.currentTimeMillis(), "HDP-1.2.0"), "cluster1", "HDFS", false, false);
        s.setHostRoleStatus(hostname, "SECONDARY_NAMENODE", org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = java.util.Collections.singletonList(s);
        org.apache.ambari.server.actionmanager.ActionDBAccessor db = Mockito.mock(org.apache.ambari.server.actionmanager.ActionDBAccessor.class);
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAOMock = Mockito.mock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        org.mockito.Mockito.doNothing().when(hostRoleCommandDAOMock).publishTaskCreateEvent(Matchers.anyListOf(org.apache.ambari.server.actionmanager.HostRoleCommand.class));
        Mockito.when(db.getCommandsInProgressCount()).thenReturn(stages.size());
        Mockito.when(db.getFirstStageInProgressPerRequest()).thenReturn(stages);
        org.apache.ambari.server.orm.entities.RequestEntity request = Mockito.mock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        Mockito.when(request.isExclusive()).thenReturn(false);
        Mockito.when(request.getClusterHostInfo()).thenReturn(org.apache.ambari.server.actionmanager.TestActionScheduler.CLUSTER_HOST_INFO);
        Mockito.when(db.getRequestEntity(Matchers.anyLong())).thenReturn(request);
        Mockito.doAnswer(new org.mockito.stubbing.Answer<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                java.lang.String host = ((java.lang.String) (invocation.getArguments()[0]));
                java.lang.String role = ((java.lang.String) (invocation.getArguments()[3]));
                org.apache.ambari.server.actionmanager.HostRoleCommand command = s.getHostRoleCommand(host, role);
                command.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT);
                return null;
            }
        }).when(db).timeoutHostRole(Matchers.anyString(), Matchers.anyLong(), Matchers.anyLong(), Matchers.anyString(), Matchers.anyBoolean(), Matchers.eq(false));
        org.apache.ambari.server.actionmanager.ActionScheduler scheduler = new org.apache.ambari.server.actionmanager.ActionScheduler(100, 0, db, fsm, 3, new org.apache.ambari.server.controller.HostsMap(((java.lang.String) (null))), unitOfWork, null, conf, entityManagerProviderMock, hostRoleCommandDAOMock, null, agentCommandsPublisher);
        scheduler.setTaskTimeoutAdjustment(false);
        int cycleCount = 0;
        scheduler.doWork();
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, stages.get(0).getHostRoleStatus(hostname, "SECONDARY_NAMENODE"));
        java.util.List<org.apache.ambari.server.agent.AgentCommand> commands = waitForQueueSize(hostId, agentCommandsPublisher, 1, scheduler);
        org.junit.Assert.assertTrue((commands != null) && (commands.size() >= 1));
        org.apache.ambari.server.agent.AgentCommand scheduledCommand = commands.get(0);
        org.junit.Assert.assertTrue(scheduledCommand instanceof org.apache.ambari.server.agent.CancelCommand);
        stages.get(0).setHostRoleStatus(hostname, "SECONDARY_NAMENODE", org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS);
        scheduler.doWork();
        org.junit.Assert.assertEquals(2, stages.get(0).getAttemptCount(hostname, "NAMENODE"));
        org.junit.Assert.assertEquals(3, stages.get(0).getAttemptCount(hostname, "SECONDARY_NAMENODE"));
        while ((!stages.get(0).getHostRoleStatus(hostname, "SECONDARY_NAMENODE").equals(org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT)) && ((cycleCount++) <= MAX_CYCLE_ITERATIONS)) {
            scheduler.doWork();
        } 
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT, stages.get(0).getHostRoleStatus(hostname, "SECONDARY_NAMENODE"));
        Mockito.verify(db, Mockito.times(1)).startRequest(Matchers.eq(1L));
        Mockito.verify(db, Mockito.times(1)).abortOperation(1L);
        org.easymock.EasyMock.verify(entityManagerProviderMock);
    }

    @org.junit.Test
    public void testActionTimeoutForLostHost() throws java.lang.Exception {
        java.util.Properties properties = new java.util.Properties();
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.state.Clusters fsm = Mockito.mock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster oneClusterMock = Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service serviceObj = Mockito.mock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.ServiceComponent scomp = Mockito.mock(org.apache.ambari.server.state.ServiceComponent.class);
        org.apache.ambari.server.state.ServiceComponentHost sch = Mockito.mock(org.apache.ambari.server.state.ServiceComponentHost.class);
        com.google.inject.persist.UnitOfWork unitOfWork = Mockito.mock(com.google.inject.persist.UnitOfWork.class);
        org.apache.ambari.server.events.publishers.AgentCommandsPublisher agentCommandsPublisher = Mockito.mock(org.apache.ambari.server.events.publishers.AgentCommandsPublisher.class);
        Mockito.when(fsm.getCluster(Matchers.anyString())).thenReturn(oneClusterMock);
        Mockito.when(oneClusterMock.getService(Matchers.anyString())).thenReturn(serviceObj);
        Mockito.when(serviceObj.getServiceComponent(Matchers.anyString())).thenReturn(scomp);
        Mockito.when(scomp.getServiceComponentHost(Matchers.anyString())).thenReturn(sch);
        Mockito.when(serviceObj.getCluster()).thenReturn(oneClusterMock);
        org.apache.ambari.server.state.Host host = Mockito.mock(org.apache.ambari.server.state.Host.class);
        java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> hosts = new java.util.HashMap<>();
        hosts.put(hostname, sch);
        Mockito.when(scomp.getServiceComponentHosts()).thenReturn(hosts);
        Mockito.when(fsm.getHost(Matchers.anyString())).thenReturn(host);
        Mockito.when(host.getState()).thenReturn(org.apache.ambari.server.state.HostState.HEARTBEAT_LOST);
        Mockito.when(host.getHostName()).thenReturn(hostname);
        final org.apache.ambari.server.actionmanager.Stage s = org.apache.ambari.server.utils.StageUtils.getATestStage(1, 977, hostname, "{\"host_param\":\"param_value\"}", "{\"stage_param\":\"param_value\"}");
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = java.util.Collections.singletonList(s);
        org.apache.ambari.server.actionmanager.ActionDBAccessor db = Mockito.mock(org.apache.ambari.server.actionmanager.ActionDBAccessor.class);
        org.apache.ambari.server.orm.entities.RequestEntity request = Mockito.mock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        Mockito.when(request.isExclusive()).thenReturn(false);
        Mockito.when(db.getRequestEntity(Matchers.anyLong())).thenReturn(request);
        Mockito.when(db.getCommandsInProgressCount()).thenReturn(stages.size());
        Mockito.when(db.getFirstStageInProgressPerRequest()).thenReturn(stages);
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAOMock = Mockito.mock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        org.mockito.Mockito.doNothing().when(hostRoleCommandDAOMock).publishTaskCreateEvent(Matchers.anyListOf(org.apache.ambari.server.actionmanager.HostRoleCommand.class));
        Mockito.doAnswer(new org.mockito.stubbing.Answer<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                java.lang.String host = ((java.lang.String) (invocation.getArguments()[0]));
                java.lang.String role = ((java.lang.String) (invocation.getArguments()[3]));
                org.apache.ambari.server.actionmanager.HostRoleCommand command = s.getHostRoleCommand(host, role);
                command.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED);
                return null;
            }
        }).when(db).timeoutHostRole(Matchers.anyString(), Matchers.anyLong(), Matchers.anyLong(), Matchers.anyString(), Matchers.anyBoolean(), Matchers.eq(true));
        org.apache.ambari.server.events.publishers.AmbariEventPublisher aep = org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class);
        org.apache.ambari.server.actionmanager.ActionScheduler scheduler = new org.apache.ambari.server.actionmanager.ActionScheduler(100, 0, db, fsm, 3, new org.apache.ambari.server.controller.HostsMap(((java.lang.String) (null))), unitOfWork, null, conf, entityManagerProviderMock, hostRoleCommandDAOMock, null, agentCommandsPublisher);
        scheduler.setTaskTimeoutAdjustment(false);
        int cycleCount = 0;
        while ((!stages.get(0).getHostRoleStatus(hostname, "NAMENODE").equals(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED)) && ((cycleCount++) <= MAX_CYCLE_ITERATIONS)) {
            scheduler.doWork();
        } 
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, stages.get(0).getHostRoleStatus(hostname, "NAMENODE"));
        org.easymock.EasyMock.verify(entityManagerProviderMock);
    }

    @org.junit.Test
    public void testOpFailedEventRaisedForAbortedHostRole() throws java.lang.Exception {
        java.util.Properties properties = new java.util.Properties();
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.state.Clusters fsm = Mockito.mock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster oneClusterMock = Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service serviceObj = Mockito.mock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.ServiceComponent scomp = Mockito.mock(org.apache.ambari.server.state.ServiceComponent.class);
        org.apache.ambari.server.state.ServiceComponentHost sch1 = Mockito.mock(org.apache.ambari.server.state.ServiceComponentHost.class);
        org.apache.ambari.server.state.ServiceComponentHost sch2 = Mockito.mock(org.apache.ambari.server.state.ServiceComponentHost.class);
        java.lang.String hostname1 = "host1";
        java.lang.String hostname2 = "host2";
        org.apache.ambari.server.state.Host host1 = Mockito.mock(org.apache.ambari.server.state.Host.class);
        org.apache.ambari.server.state.Host host2 = Mockito.mock(org.apache.ambari.server.state.Host.class);
        org.apache.ambari.server.orm.entities.HostEntity hostEntity1 = new org.apache.ambari.server.orm.entities.HostEntity();
        hostEntity1.setHostName(hostname1);
        org.apache.ambari.server.orm.entities.HostEntity hostEntity2 = new org.apache.ambari.server.orm.entities.HostEntity();
        hostEntity2.setHostName(hostname2);
        hostDAO.merge(hostEntity1);
        hostDAO.merge(hostEntity2);
        java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> hosts = new java.util.HashMap<>();
        hosts.put(hostname1, sch1);
        hosts.put(hostname2, sch2);
        Mockito.when(scomp.getServiceComponentHosts()).thenReturn(hosts);
        Mockito.when(fsm.getHost(hostname1)).thenReturn(host1);
        Mockito.when(fsm.getHost(hostname2)).thenReturn(host2);
        Mockito.when(host1.getState()).thenReturn(org.apache.ambari.server.state.HostState.HEARTBEAT_LOST);
        Mockito.when(host2.getState()).thenReturn(org.apache.ambari.server.state.HostState.HEALTHY);
        Mockito.when(host1.getHostName()).thenReturn(hostname1);
        Mockito.when(host2.getHostName()).thenReturn(hostname2);
        Mockito.when(scomp.getServiceComponentHost(hostname1)).thenReturn(sch1);
        Mockito.when(scomp.getServiceComponentHost(hostname2)).thenReturn(sch2);
        com.google.inject.persist.UnitOfWork unitOfWork = Mockito.mock(com.google.inject.persist.UnitOfWork.class);
        org.apache.ambari.server.events.publishers.AgentCommandsPublisher agentCommandsPublisher = Mockito.mock(org.apache.ambari.server.events.publishers.AgentCommandsPublisher.class);
        Mockito.when(fsm.getCluster(Matchers.anyString())).thenReturn(oneClusterMock);
        Mockito.when(oneClusterMock.getService(Matchers.anyString())).thenReturn(serviceObj);
        Mockito.when(serviceObj.getServiceComponent(Matchers.anyString())).thenReturn(scomp);
        Mockito.when(serviceObj.getCluster()).thenReturn(oneClusterMock);
        final org.apache.ambari.server.actionmanager.Stage stage = stageFactory.createNew(1, "/tmp", "cluster1", 1L, "stageWith2Tasks", "{\"command_param\":\"param_value\"}", "{\"host_param\":\"param_value\"}");
        addInstallTaskToStage(stage, hostname1, "cluster1", org.apache.ambari.server.Role.DATANODE, org.apache.ambari.server.RoleCommand.INSTALL, org.apache.ambari.server.state.Service.Type.HDFS, 1);
        addInstallTaskToStage(stage, hostname2, "cluster1", org.apache.ambari.server.Role.NAMENODE, org.apache.ambari.server.RoleCommand.INSTALL, org.apache.ambari.server.state.Service.Type.HDFS, 2);
        final java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = java.util.Collections.singletonList(stage);
        org.apache.ambari.server.actionmanager.ActionDBAccessor db = Mockito.mock(org.apache.ambari.server.actionmanager.ActionDBAccessor.class);
        org.apache.ambari.server.orm.entities.RequestEntity request = Mockito.mock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        Mockito.when(request.isExclusive()).thenReturn(false);
        Mockito.when(db.getRequestEntity(Matchers.anyLong())).thenReturn(request);
        Mockito.when(db.getCommandsInProgressCount()).thenReturn(stages.size());
        Mockito.when(db.getFirstStageInProgressPerRequest()).thenReturn(stages);
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAOMock = Mockito.mock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        org.mockito.Mockito.doNothing().when(hostRoleCommandDAOMock).publishTaskCreateEvent(Matchers.anyListOf(org.apache.ambari.server.actionmanager.HostRoleCommand.class));
        Mockito.doAnswer(new org.mockito.stubbing.Answer<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                java.lang.String host = ((java.lang.String) (invocation.getArguments()[0]));
                java.lang.String role = ((java.lang.String) (invocation.getArguments()[3]));
                org.apache.ambari.server.actionmanager.HostRoleCommand command = stage.getHostRoleCommand(host, role);
                command.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED);
                return null;
            }
        }).when(db).timeoutHostRole(Matchers.anyString(), Matchers.anyLong(), Matchers.anyLong(), Matchers.anyString(), Matchers.anyBoolean(), Matchers.eq(true));
        Mockito.doAnswer(new org.mockito.stubbing.Answer<java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity>>() {
            @java.lang.Override
            public java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                java.lang.Long requestId = ((java.lang.Long) (invocation.getArguments()[0]));
                java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> abortedCommands = com.google.common.collect.Lists.newArrayList();
                for (org.apache.ambari.server.actionmanager.Stage stage : stages) {
                    if (requestId.equals(stage.getRequestId())) {
                        for (org.apache.ambari.server.actionmanager.HostRoleCommand command : stage.getOrderedHostRoleCommands()) {
                            if (((command.getStatus() == org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED) || (command.getStatus() == org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS)) || (command.getStatus() == org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING)) {
                                command.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED);
                                org.apache.ambari.server.orm.entities.HostRoleCommandEntity hostRoleCommandEntity = command.constructNewPersistenceEntity();
                                hostRoleCommandEntity.setStage(stage.constructNewPersistenceEntity());
                                abortedCommands.add(hostRoleCommandEntity);
                            }
                        }
                    }
                }
                return abortedCommands;
            }
        }).when(db).abortOperation(Matchers.anyLong());
        org.mockito.ArgumentCaptor<org.apache.ambari.server.state.ServiceComponentHostEvent> eventsCapture1 = org.mockito.ArgumentCaptor.forClass(org.apache.ambari.server.state.ServiceComponentHostEvent.class);
        org.mockito.ArgumentCaptor<org.apache.ambari.server.state.ServiceComponentHostEvent> eventsCapture2 = org.mockito.ArgumentCaptor.forClass(org.apache.ambari.server.state.ServiceComponentHostEvent.class);
        org.apache.ambari.server.actionmanager.ActionScheduler scheduler = new org.apache.ambari.server.actionmanager.ActionScheduler(100, 50000, db, fsm, 3, new org.apache.ambari.server.controller.HostsMap(((java.lang.String) (null))), unitOfWork, null, conf, entityManagerProviderMock, hostRoleCommandDAOMock, ((org.apache.ambari.server.actionmanager.HostRoleCommandFactory) (null)), agentCommandsPublisher);
        scheduler.setTaskTimeoutAdjustment(false);
        int cycleCount = 0;
        while ((!(stages.get(0).getHostRoleStatus(hostname1, "DATANODE").equals(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED) && stages.get(0).getHostRoleStatus(hostname2, "NAMENODE").equals(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED))) && ((cycleCount++) <= MAX_CYCLE_ITERATIONS)) {
            scheduler.doWork();
        } 
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, stages.get(0).getHostRoleStatus(hostname1, "DATANODE"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, stages.get(0).getHostRoleStatus(hostname2, "NAMENODE"));
        Mockito.verify(sch1, Mockito.atLeastOnce()).handleEvent(eventsCapture1.capture());
        Mockito.verify(sch2, Mockito.atLeastOnce()).handleEvent(eventsCapture2.capture());
        java.util.List<org.apache.ambari.server.state.ServiceComponentHostEvent> eventTypes = eventsCapture1.getAllValues();
        eventTypes.addAll(eventsCapture2.getAllValues());
        org.junit.Assert.assertNotNull(eventTypes);
        org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpFailedEvent datanodeFailedEvent = null;
        org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpFailedEvent namenodeFailedEvent = null;
        for (org.apache.ambari.server.state.ServiceComponentHostEvent eventType : eventTypes) {
            if (eventType instanceof org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpFailedEvent) {
                org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpFailedEvent event = ((org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpFailedEvent) (eventType));
                if (event.getServiceComponentName().equals("DATANODE")) {
                    datanodeFailedEvent = event;
                } else if (event.getServiceComponentName().equals("NAMENODE")) {
                    namenodeFailedEvent = event;
                }
            }
        }
        org.junit.Assert.assertNotNull("Datanode should be in Install failed state.", datanodeFailedEvent);
        org.junit.Assert.assertNotNull("Namenode should be in Install failed state.", namenodeFailedEvent);
    }

    @org.junit.Test
    public void testServerAction() throws java.lang.Exception {
        java.util.Properties properties = new java.util.Properties();
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.state.Clusters fsm = Mockito.mock(org.apache.ambari.server.state.Clusters.class);
        com.google.inject.persist.UnitOfWork unitOfWork = Mockito.mock(com.google.inject.persist.UnitOfWork.class);
        org.apache.ambari.server.events.publishers.AgentCommandsPublisher agentCommandsPublisher = Mockito.mock(org.apache.ambari.server.events.publishers.AgentCommandsPublisher.class);
        java.util.Map<java.lang.String, java.lang.String> payload = new java.util.HashMap<>();
        final org.apache.ambari.server.actionmanager.Stage s = getStageWithServerAction(1, 977, payload, "test", 1200, false, false);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = java.util.Collections.singletonList(s);
        org.apache.ambari.server.actionmanager.ActionDBAccessor db = Mockito.mock(org.apache.ambari.server.actionmanager.ActionDBAccessor.class);
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAOMock = Mockito.mock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        org.mockito.Mockito.doNothing().when(hostRoleCommandDAOMock).publishTaskCreateEvent(Matchers.anyListOf(org.apache.ambari.server.actionmanager.HostRoleCommand.class));
        org.apache.ambari.server.orm.entities.RequestEntity request = Mockito.mock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        Mockito.when(request.isExclusive()).thenReturn(false);
        Mockito.when(request.getClusterHostInfo()).thenReturn(org.apache.ambari.server.actionmanager.TestActionScheduler.CLUSTER_HOST_INFO);
        Mockito.when(db.getRequestEntity(Matchers.anyLong())).thenReturn(request);
        Mockito.when(db.getCommandsInProgressCount()).thenReturn(stages.size());
        Mockito.when(db.getFirstStageInProgressPerRequest()).thenReturn(stages);
        Mockito.doAnswer(new org.mockito.stubbing.Answer<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                java.lang.String host = ((java.lang.String) (invocation.getArguments()[0]));
                java.lang.String role = ((java.lang.String) (invocation.getArguments()[3]));
                org.apache.ambari.server.agent.CommandReport commandReport = ((org.apache.ambari.server.agent.CommandReport) (invocation.getArguments()[4]));
                org.apache.ambari.server.actionmanager.HostRoleCommand command = null;
                if (null == host) {
                    command = s.getHostRoleCommand(null, role);
                } else {
                    command = s.getHostRoleCommand(host, role);
                }
                command.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.valueOf(commandReport.getStatus()));
                return null;
            }
        }).when(db).updateHostRoleState(Matchers.anyString(), Matchers.anyLong(), Matchers.anyLong(), Matchers.anyString(), Matchers.any(org.apache.ambari.server.agent.CommandReport.class));
        Mockito.doAnswer(new org.mockito.stubbing.Answer<org.apache.ambari.server.actionmanager.HostRoleCommand>() {
            @java.lang.Override
            public org.apache.ambari.server.actionmanager.HostRoleCommand answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                return s.getHostRoleCommand(null, "AMBARI_SERVER_ACTION");
            }
        }).when(db).getTask(Matchers.anyLong());
        Mockito.doAnswer(new org.mockito.stubbing.Answer<java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand>>() {
            @java.lang.Override
            public java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                java.lang.String role = ((java.lang.String) (invocation.getArguments()[0]));
                org.apache.ambari.server.actionmanager.HostRoleStatus status = ((org.apache.ambari.server.actionmanager.HostRoleStatus) (invocation.getArguments()[1]));
                org.apache.ambari.server.actionmanager.HostRoleCommand task = s.getHostRoleCommand(null, role);
                if (task.getStatus() == status) {
                    return java.util.Arrays.asList(task);
                } else {
                    return java.util.Collections.emptyList();
                }
            }
        }).when(db).getTasksByRoleAndStatus(Matchers.anyString(), Matchers.any(org.apache.ambari.server.actionmanager.HostRoleStatus.class));
        org.apache.ambari.server.serveraction.ServerActionExecutor.init(injector);
        org.apache.ambari.server.actionmanager.ActionScheduler scheduler = new org.apache.ambari.server.actionmanager.ActionScheduler(100, 50, db, fsm, 3, new org.apache.ambari.server.controller.HostsMap(((java.lang.String) (null))), unitOfWork, null, conf, entityManagerProviderMock, hostRoleCommandDAOMock, ((org.apache.ambari.server.actionmanager.HostRoleCommandFactory) (null)), agentCommandsPublisher);
        int cycleCount = 0;
        while ((!stages.get(0).getHostRoleStatus(null, "AMBARI_SERVER_ACTION").equals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED)) && ((cycleCount++) <= MAX_CYCLE_ITERATIONS)) {
            scheduler.doWork();
            scheduler.getServerActionExecutor().doWork();
        } 
        org.junit.Assert.assertEquals(stages.get(0).getHostRoleStatus(null, "AMBARI_SERVER_ACTION"), org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
    }

    @org.junit.Test
    public void testServerActionInMultipleRequests() throws java.lang.Exception {
        org.apache.ambari.server.state.Clusters fsm = Mockito.mock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster oneClusterMock = Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service serviceObj = Mockito.mock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.ServiceComponent scomp = Mockito.mock(org.apache.ambari.server.state.ServiceComponent.class);
        org.apache.ambari.server.state.ServiceComponentHost sch = Mockito.mock(org.apache.ambari.server.state.ServiceComponentHost.class);
        com.google.inject.persist.UnitOfWork unitOfWork = Mockito.mock(com.google.inject.persist.UnitOfWork.class);
        org.apache.ambari.server.events.publishers.AgentCommandsPublisher agentCommandsPublisher = Mockito.mock(org.apache.ambari.server.events.publishers.AgentCommandsPublisher.class);
        Mockito.when(fsm.getCluster(Matchers.anyString())).thenReturn(oneClusterMock);
        Mockito.when(oneClusterMock.getService(Matchers.anyString())).thenReturn(serviceObj);
        Mockito.when(serviceObj.getServiceComponent(Matchers.anyString())).thenReturn(scomp);
        Mockito.when(scomp.getServiceComponentHost(Matchers.anyString())).thenReturn(sch);
        Mockito.when(serviceObj.getCluster()).thenReturn(oneClusterMock);
        java.lang.String clusterName = "cluster1";
        java.lang.String hostname1 = "ahost.ambari.apache.org";
        java.lang.String hostname2 = "bhost.ambari.apache.org";
        java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> hosts = new java.util.HashMap<>();
        hosts.put(hostname1, sch);
        hosts.put(hostname2, sch);
        hosts.put(org.apache.ambari.server.actionmanager.Stage.INTERNAL_HOSTNAME, sch);
        Mockito.when(scomp.getServiceComponentHosts()).thenReturn(hosts);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = new java.util.ArrayList<>();
        org.apache.ambari.server.actionmanager.Stage stage01 = createStage(clusterName, 0, 1);
        addTask(stage01, org.apache.ambari.server.actionmanager.Stage.INTERNAL_HOSTNAME, clusterName, org.apache.ambari.server.Role.AMBARI_SERVER_ACTION, org.apache.ambari.server.RoleCommand.ACTIONEXECUTE, "AMBARI", 1);
        org.apache.ambari.server.actionmanager.Stage stage11 = createStage("cluster1", 1, 1);
        addTask(stage11, hostname1, clusterName, org.apache.ambari.server.Role.KERBEROS_CLIENT, org.apache.ambari.server.RoleCommand.CUSTOM_COMMAND, "KERBEROS", 2);
        org.apache.ambari.server.actionmanager.Stage stage02 = createStage("cluster1", 0, 2);
        addTask(stage02, org.apache.ambari.server.actionmanager.Stage.INTERNAL_HOSTNAME, clusterName, org.apache.ambari.server.Role.AMBARI_SERVER_ACTION, org.apache.ambari.server.RoleCommand.ACTIONEXECUTE, "AMBARI", 3);
        org.apache.ambari.server.actionmanager.Stage stage12 = createStage("cluster1", 1, 2);
        addTask(stage12, hostname2, clusterName, org.apache.ambari.server.Role.KERBEROS_CLIENT, org.apache.ambari.server.RoleCommand.CUSTOM_COMMAND, "KERBEROS", 4);
        stages.add(stage01);
        stages.add(stage11);
        stages.add(stage02);
        stages.add(stage12);
        org.apache.ambari.server.actionmanager.ActionDBAccessor db = Mockito.mock(org.apache.ambari.server.actionmanager.ActionDBAccessor.class);
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAOMock = Mockito.mock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        org.mockito.Mockito.doNothing().when(hostRoleCommandDAOMock).publishTaskCreateEvent(Matchers.anyListOf(org.apache.ambari.server.actionmanager.HostRoleCommand.class));
        org.apache.ambari.server.orm.entities.RequestEntity request = Mockito.mock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        Mockito.when(request.isExclusive()).thenReturn(false);
        Mockito.when(request.getClusterHostInfo()).thenReturn(org.apache.ambari.server.actionmanager.TestActionScheduler.CLUSTER_HOST_INFO);
        Mockito.when(db.getRequestEntity(Matchers.anyLong())).thenReturn(request);
        Mockito.when(db.getCommandsInProgressCount()).thenReturn(stages.size());
        Mockito.when(db.getFirstStageInProgressPerRequest()).thenReturn(stages);
        java.util.Properties properties = new java.util.Properties();
        properties.put(org.apache.ambari.server.configuration.Configuration.PARALLEL_STAGE_EXECUTION.getKey(), "true");
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.actionmanager.ActionScheduler scheduler = new org.apache.ambari.server.actionmanager.ActionScheduler(100, 50, db, fsm, 3, new org.apache.ambari.server.controller.HostsMap(((java.lang.String) (null))), unitOfWork, org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class), conf, entityManagerProviderMock, hostRoleCommandDAOMock, ((org.apache.ambari.server.actionmanager.HostRoleCommandFactory) (null)), agentCommandsPublisher);
        scheduler.doWork();
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, stages.get(0).getHostRoleStatus(org.apache.ambari.server.actionmanager.Stage.INTERNAL_HOSTNAME, org.apache.ambari.server.Role.AMBARI_SERVER_ACTION.name()));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, stages.get(1).getHostRoleStatus(hostname1, org.apache.ambari.server.Role.KERBEROS_CLIENT.name()));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, stages.get(2).getHostRoleStatus(org.apache.ambari.server.actionmanager.Stage.INTERNAL_HOSTNAME, org.apache.ambari.server.Role.AMBARI_SERVER_ACTION.name()));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, stages.get(3).getHostRoleStatus(hostname2, org.apache.ambari.server.Role.KERBEROS_CLIENT.name()));
    }

    @org.junit.Test
    public void testServerActionTimeOut() throws java.lang.Exception {
        java.util.Properties properties = new java.util.Properties();
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.state.Clusters fsm = Mockito.mock(org.apache.ambari.server.state.Clusters.class);
        com.google.inject.persist.UnitOfWork unitOfWork = Mockito.mock(com.google.inject.persist.UnitOfWork.class);
        org.apache.ambari.server.events.publishers.AgentCommandsPublisher agentCommandsPublisher = Mockito.mock(org.apache.ambari.server.events.publishers.AgentCommandsPublisher.class);
        java.util.Map<java.lang.String, java.lang.String> payload = new java.util.HashMap<>();
        payload.put(org.apache.ambari.server.serveraction.MockServerAction.PAYLOAD_FORCE_FAIL, "timeout");
        final org.apache.ambari.server.actionmanager.Stage s = getStageWithServerAction(1, 977, payload, "test", 2, false, false);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = java.util.Collections.singletonList(s);
        org.apache.ambari.server.actionmanager.ActionDBAccessor db = Mockito.mock(org.apache.ambari.server.actionmanager.ActionDBAccessor.class);
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAOMock = Mockito.mock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        org.mockito.Mockito.doNothing().when(hostRoleCommandDAOMock).publishTaskCreateEvent(Matchers.anyListOf(org.apache.ambari.server.actionmanager.HostRoleCommand.class));
        org.apache.ambari.server.orm.entities.RequestEntity request = Mockito.mock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        Mockito.when(request.isExclusive()).thenReturn(false);
        Mockito.when(request.getClusterHostInfo()).thenReturn(org.apache.ambari.server.actionmanager.TestActionScheduler.CLUSTER_HOST_INFO);
        Mockito.when(db.getRequestEntity(Matchers.anyLong())).thenReturn(request);
        Mockito.when(db.getCommandsInProgressCount()).thenReturn(stages.size());
        Mockito.when(db.getFirstStageInProgressPerRequest()).thenReturn(stages);
        Mockito.doAnswer(new org.mockito.stubbing.Answer<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                java.lang.String host = ((java.lang.String) (invocation.getArguments()[0]));
                java.lang.String role = ((java.lang.String) (invocation.getArguments()[3]));
                org.apache.ambari.server.agent.CommandReport commandReport = ((org.apache.ambari.server.agent.CommandReport) (invocation.getArguments()[4]));
                org.apache.ambari.server.actionmanager.HostRoleCommand command = null;
                if (null == host) {
                    command = s.getHostRoleCommand(null, role);
                } else {
                    command = s.getHostRoleCommand(host, role);
                }
                command.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.valueOf(commandReport.getStatus()));
                return null;
            }
        }).when(db).updateHostRoleState(Matchers.anyString(), Matchers.anyLong(), Matchers.anyLong(), Matchers.anyString(), Matchers.any(org.apache.ambari.server.agent.CommandReport.class));
        Mockito.doAnswer(new org.mockito.stubbing.Answer<org.apache.ambari.server.actionmanager.HostRoleCommand>() {
            @java.lang.Override
            public org.apache.ambari.server.actionmanager.HostRoleCommand answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                return s.getHostRoleCommand(null, "AMBARI_SERVER_ACTION");
            }
        }).when(db).getTask(Matchers.anyLong());
        Mockito.doAnswer(new org.mockito.stubbing.Answer<java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand>>() {
            @java.lang.Override
            public java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                java.lang.String role = ((java.lang.String) (invocation.getArguments()[0]));
                org.apache.ambari.server.actionmanager.HostRoleStatus status = ((org.apache.ambari.server.actionmanager.HostRoleStatus) (invocation.getArguments()[1]));
                org.apache.ambari.server.actionmanager.HostRoleCommand task = s.getHostRoleCommand(null, role);
                if (task.getStatus() == status) {
                    return java.util.Arrays.asList(task);
                } else {
                    return java.util.Collections.emptyList();
                }
            }
        }).when(db).getTasksByRoleAndStatus(Matchers.anyString(), Matchers.any(org.apache.ambari.server.actionmanager.HostRoleStatus.class));
        org.apache.ambari.server.serveraction.ServerActionExecutor.init(injector);
        org.apache.ambari.server.actionmanager.ActionScheduler scheduler = new org.apache.ambari.server.actionmanager.ActionScheduler(100, 50, db, fsm, 3, new org.apache.ambari.server.controller.HostsMap(((java.lang.String) (null))), unitOfWork, null, conf, entityManagerProviderMock, hostRoleCommandDAOMock, ((org.apache.ambari.server.actionmanager.HostRoleCommandFactory) (null)), agentCommandsPublisher);
        int cycleCount = 0;
        while ((!stages.get(0).getHostRoleStatus(null, "AMBARI_SERVER_ACTION").isCompletedState()) && ((cycleCount++) <= MAX_CYCLE_ITERATIONS)) {
            scheduler.doWork();
            scheduler.getServerActionExecutor().doWork();
        } 
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.TIMEDOUT, stages.get(0).getHostRoleStatus(null, "AMBARI_SERVER_ACTION"));
    }

    @org.junit.Test
    public void testTimeOutWithHostNull() throws org.apache.ambari.server.AmbariException {
        org.apache.ambari.server.actionmanager.Stage s = getStageWithServerAction(1, 977, null, "test", 2, false, false);
        s.setHostRoleStatus(null, org.apache.ambari.server.Role.AMBARI_SERVER_ACTION.toString(), org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS);
        org.apache.ambari.server.actionmanager.ActionScheduler scheduler = org.easymock.EasyMock.createMockBuilder(org.apache.ambari.server.actionmanager.ActionScheduler.class).withConstructor(long.class, long.class, org.apache.ambari.server.actionmanager.ActionDBAccessor.class, org.apache.ambari.server.state.Clusters.class, int.class, org.apache.ambari.server.controller.HostsMap.class, com.google.inject.persist.UnitOfWork.class, org.apache.ambari.server.events.publishers.AmbariEventPublisher.class, org.apache.ambari.server.configuration.Configuration.class, com.google.inject.Provider.class, org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class, org.apache.ambari.server.actionmanager.HostRoleCommandFactory.class, org.apache.ambari.server.events.publishers.AgentCommandsPublisher.class).withArgs(100L, 50L, null, null, -1, null, null, null, null, entityManagerProviderMock, Mockito.mock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class), Mockito.mock(org.apache.ambari.server.actionmanager.HostRoleCommandFactory.class), Mockito.mock(org.apache.ambari.server.events.publishers.AgentCommandsPublisher.class)).createNiceMock();
        org.easymock.EasyMock.replay(scheduler);
        org.junit.Assert.assertEquals(false, scheduler.timeOutActionNeeded(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS, s, null, org.apache.ambari.server.Role.AMBARI_SERVER_ACTION.toString(), -1L, 1L));
        org.easymock.EasyMock.verify(scheduler);
    }

    @org.junit.Test
    public void testTimeoutRequestDueAgentRestartExecuteCommand() throws java.lang.Exception {
        testTimeoutRequest(org.apache.ambari.server.RoleCommand.EXECUTE, false, false);
    }

    @org.junit.Test
    public void testTimeoutRequestDueAgentRestartCustomCommand() throws java.lang.Exception {
        testTimeoutRequest(org.apache.ambari.server.RoleCommand.CUSTOM_COMMAND, false, false);
    }

    @org.junit.Test
    public void testTimeoutRequestDueAgentRestartActionExecute() throws java.lang.Exception {
        testTimeoutRequest(org.apache.ambari.server.RoleCommand.ACTIONEXECUTE, false, false);
    }

    @org.junit.Test
    public void testTimeoutRequestDueAgentRestartServiceCheck() throws java.lang.Exception {
        testTimeoutRequest(org.apache.ambari.server.RoleCommand.SERVICE_CHECK, false, false);
    }

    @org.junit.Test
    public void testTimeoutWithSkippableStageButNotCommand() throws java.lang.Exception {
        testTimeoutRequest(org.apache.ambari.server.RoleCommand.EXECUTE, true, false);
    }

    @org.junit.Test
    public void testTimeoutWithSkippableCommand() throws java.lang.Exception {
        testTimeoutRequest(org.apache.ambari.server.RoleCommand.EXECUTE, true, true);
    }

    private void testTimeoutRequest(org.apache.ambari.server.RoleCommand roleCommand, boolean stageSupportsAutoSkip, boolean autoSkipFailedTask) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.state.fsm.InvalidStateTransitionException {
        final long HOST_REGISTRATION_TIME = 100L;
        final long STAGE_TASK_START_TIME = HOST_REGISTRATION_TIME - 1L;
        org.apache.ambari.server.state.Clusters fsm = org.easymock.EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster cluster = org.easymock.EasyMock.createMock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service service = org.easymock.EasyMock.createMock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.ServiceComponent serviceComponent = org.easymock.EasyMock.createMock(org.apache.ambari.server.state.ServiceComponent.class);
        org.apache.ambari.server.state.ServiceComponentHost serviceComponentHost = org.easymock.EasyMock.createMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        org.apache.ambari.server.state.Host host = org.easymock.EasyMock.createMock(org.apache.ambari.server.state.Host.class);
        org.apache.ambari.server.actionmanager.ActionDBAccessor db = org.easymock.EasyMock.createMock(org.apache.ambari.server.actionmanager.ActionDBAccessor.class);
        org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisher = org.easymock.EasyMock.createMock(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class);
        org.apache.ambari.server.events.publishers.AgentCommandsPublisher agentCommandsPublisher = org.easymock.EasyMock.createStrictMock(org.apache.ambari.server.events.publishers.AgentCommandsPublisher.class);
        org.easymock.EasyMock.expect(fsm.getCluster(org.easymock.EasyMock.anyString())).andReturn(cluster).anyTimes();
        org.easymock.EasyMock.expect(fsm.getHost(org.easymock.EasyMock.anyString())).andReturn(host);
        org.easymock.EasyMock.expect(cluster.getService(org.easymock.EasyMock.anyString())).andReturn(null);
        org.easymock.EasyMock.expect(host.getHostName()).andReturn(org.apache.ambari.server.actionmanager.Stage.INTERNAL_HOSTNAME).anyTimes();
        if (org.apache.ambari.server.RoleCommand.ACTIONEXECUTE.equals(roleCommand)) {
            org.easymock.EasyMock.expect(cluster.getClusterName()).andReturn("clusterName").anyTimes();
            org.easymock.EasyMock.expect(cluster.getClusterId()).andReturn(1L);
            ambariEventPublisher.publish(org.easymock.EasyMock.anyObject(org.apache.ambari.server.events.AmbariEvent.class));
            org.easymock.EasyMock.expectLastCall();
        } else if (org.apache.ambari.server.RoleCommand.EXECUTE.equals(roleCommand)) {
            org.easymock.EasyMock.expect(cluster.getClusterName()).andReturn("clusterName");
            org.easymock.EasyMock.expect(cluster.getService(org.easymock.EasyMock.anyString())).andReturn(service);
            org.easymock.EasyMock.expect(service.getServiceComponent(org.easymock.EasyMock.anyString())).andReturn(serviceComponent);
            org.easymock.EasyMock.expect(serviceComponent.getServiceComponentHost(org.easymock.EasyMock.anyString())).andReturn(serviceComponentHost);
            serviceComponentHost.handleEvent(org.easymock.EasyMock.anyObject(org.apache.ambari.server.state.ServiceComponentHostEvent.class));
            org.easymock.EasyMock.expectLastCall();
        }
        org.apache.ambari.server.actionmanager.Stage s = getStageWithServerAction(1, 977, null, "test", 2, stageSupportsAutoSkip, autoSkipFailedTask);
        s.setStartTime(null, org.apache.ambari.server.Role.AMBARI_SERVER_ACTION.toString(), STAGE_TASK_START_TIME);
        s.setHostRoleStatus(null, org.apache.ambari.server.Role.AMBARI_SERVER_ACTION.toString(), org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS);
        s.getExecutionCommands(null).get(0).getExecutionCommand().setServiceName("Service name");
        s.getExecutionCommands(null).get(0).getExecutionCommand().setRoleCommand(roleCommand);
        java.util.List<org.apache.ambari.server.agent.ExecutionCommand> commandsToSchedule = new java.util.ArrayList<>();
        com.google.common.collect.Multimap<java.lang.Long, org.apache.ambari.server.agent.AgentCommand> commandsToEnqueue = com.google.common.collect.ArrayListMultimap.create();
        boolean taskShouldBeSkipped = stageSupportsAutoSkip && autoSkipFailedTask;
        db.timeoutHostRole(org.easymock.EasyMock.anyString(), org.easymock.EasyMock.anyLong(), org.easymock.EasyMock.anyLong(), org.easymock.EasyMock.anyString(), org.easymock.EasyMock.eq(taskShouldBeSkipped), org.easymock.EasyMock.anyBoolean());
        org.easymock.EasyMock.expectLastCall();
        org.apache.ambari.server.actionmanager.ActionScheduler scheduler = org.easymock.EasyMock.createMockBuilder(org.apache.ambari.server.actionmanager.ActionScheduler.class).withConstructor(long.class, long.class, org.apache.ambari.server.actionmanager.ActionDBAccessor.class, org.apache.ambari.server.state.Clusters.class, int.class, org.apache.ambari.server.controller.HostsMap.class, com.google.inject.persist.UnitOfWork.class, org.apache.ambari.server.events.publishers.AmbariEventPublisher.class, org.apache.ambari.server.configuration.Configuration.class, com.google.inject.Provider.class, org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class, org.apache.ambari.server.actionmanager.HostRoleCommandFactory.class, org.apache.ambari.server.events.publishers.AgentCommandsPublisher.class).withArgs(100L, 50L, db, fsm, -1, null, null, ambariEventPublisher, null, entityManagerProviderMock, Mockito.mock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class), Mockito.mock(org.apache.ambari.server.actionmanager.HostRoleCommandFactory.class), agentCommandsPublisher).createNiceMock();
        org.easymock.EasyMock.replay(scheduler, fsm, host, db, cluster, ambariEventPublisher, service, serviceComponent, serviceComponentHost, agentCommandsPublisher);
        scheduler.processInProgressStage(s, commandsToSchedule, commandsToEnqueue);
        org.easymock.EasyMock.verify(scheduler, fsm, host, db, cluster, ambariEventPublisher, service, serviceComponent, serviceComponentHost, agentCommandsPublisher);
    }

    @org.junit.Test
    public void testServerActionFailed() throws java.lang.Exception {
        java.util.Properties properties = new java.util.Properties();
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.state.Clusters fsm = Mockito.mock(org.apache.ambari.server.state.Clusters.class);
        com.google.inject.persist.UnitOfWork unitOfWork = Mockito.mock(com.google.inject.persist.UnitOfWork.class);
        org.apache.ambari.server.events.publishers.AgentCommandsPublisher agentCommandsPublisher = Mockito.mock(org.apache.ambari.server.events.publishers.AgentCommandsPublisher.class);
        java.util.Map<java.lang.String, java.lang.String> payload = new java.util.HashMap<>();
        payload.put(org.apache.ambari.server.serveraction.MockServerAction.PAYLOAD_FORCE_FAIL, "exception");
        final org.apache.ambari.server.actionmanager.Stage s = getStageWithServerAction(1, 977, payload, "test", 300, false, false);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = java.util.Collections.singletonList(s);
        org.apache.ambari.server.actionmanager.ActionDBAccessor db = Mockito.mock(org.apache.ambari.server.actionmanager.ActionDBAccessor.class);
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAOMock = Mockito.mock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        org.mockito.Mockito.doNothing().when(hostRoleCommandDAOMock).publishTaskCreateEvent(Matchers.anyListOf(org.apache.ambari.server.actionmanager.HostRoleCommand.class));
        org.apache.ambari.server.orm.entities.RequestEntity request = Mockito.mock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        Mockito.when(request.isExclusive()).thenReturn(false);
        Mockito.when(request.getClusterHostInfo()).thenReturn(org.apache.ambari.server.actionmanager.TestActionScheduler.CLUSTER_HOST_INFO);
        Mockito.when(db.getRequestEntity(Matchers.anyLong())).thenReturn(request);
        Mockito.when(db.getCommandsInProgressCount()).thenReturn(stages.size());
        Mockito.when(db.getFirstStageInProgressPerRequest()).thenReturn(stages);
        Mockito.doAnswer(new org.mockito.stubbing.Answer<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                java.lang.String host = ((java.lang.String) (invocation.getArguments()[0]));
                java.lang.String role = ((java.lang.String) (invocation.getArguments()[3]));
                org.apache.ambari.server.agent.CommandReport commandReport = ((org.apache.ambari.server.agent.CommandReport) (invocation.getArguments()[4]));
                org.apache.ambari.server.actionmanager.HostRoleCommand command = null;
                if (null == host) {
                    command = s.getHostRoleCommand(null, role);
                } else {
                    command = s.getHostRoleCommand(host, role);
                }
                command.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.valueOf(commandReport.getStatus()));
                return null;
            }
        }).when(db).updateHostRoleState(Matchers.anyString(), Matchers.anyLong(), Matchers.anyLong(), Matchers.anyString(), Matchers.any(org.apache.ambari.server.agent.CommandReport.class));
        Mockito.doAnswer(new org.mockito.stubbing.Answer<org.apache.ambari.server.actionmanager.HostRoleCommand>() {
            @java.lang.Override
            public org.apache.ambari.server.actionmanager.HostRoleCommand answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                return s.getHostRoleCommand(null, "AMBARI_SERVER_ACTION");
            }
        }).when(db).getTask(Matchers.anyLong());
        Mockito.doAnswer(new org.mockito.stubbing.Answer<java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand>>() {
            @java.lang.Override
            public java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                java.lang.String role = ((java.lang.String) (invocation.getArguments()[0]));
                org.apache.ambari.server.actionmanager.HostRoleStatus status = ((org.apache.ambari.server.actionmanager.HostRoleStatus) (invocation.getArguments()[1]));
                org.apache.ambari.server.actionmanager.HostRoleCommand task = s.getHostRoleCommand(null, role);
                if (task.getStatus() == status) {
                    return java.util.Arrays.asList(task);
                } else {
                    return java.util.Collections.emptyList();
                }
            }
        }).when(db).getTasksByRoleAndStatus(Matchers.anyString(), Matchers.any(org.apache.ambari.server.actionmanager.HostRoleStatus.class));
        org.apache.ambari.server.actionmanager.ActionScheduler scheduler = new org.apache.ambari.server.actionmanager.ActionScheduler(100, 50, db, fsm, 3, new org.apache.ambari.server.controller.HostsMap(((java.lang.String) (null))), unitOfWork, null, conf, entityManagerProviderMock, hostRoleCommandDAOMock, ((org.apache.ambari.server.actionmanager.HostRoleCommandFactory) (null)), agentCommandsPublisher);
        int cycleCount = 0;
        while ((!stages.get(0).getHostRoleStatus(null, "AMBARI_SERVER_ACTION").equals(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED)) && ((cycleCount++) <= MAX_CYCLE_ITERATIONS)) {
            scheduler.doWork();
            scheduler.getServerActionExecutor().doWork();
        } 
        org.junit.Assert.assertEquals(stages.get(0).getHostRoleStatus(null, "AMBARI_SERVER_ACTION"), org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED);
        org.junit.Assert.assertEquals("test", stages.get(0).getRequestContext());
    }

    private org.apache.ambari.server.actionmanager.Stage getStageWithServerAction(long requestId, long stageId, java.util.Map<java.lang.String, java.lang.String> payload, java.lang.String requestContext, int timeout, boolean stageSupportsAutoSkip, boolean autoSkipFailedTask) {
        org.apache.ambari.server.actionmanager.Stage stage = stageFactory.createNew(requestId, "/tmp", "cluster1", 1L, requestContext, "{}", "{}");
        stage.setStageId(stageId);
        stage.setSkippable(stageSupportsAutoSkip);
        stage.setAutoSkipFailureSupported(stageSupportsAutoSkip);
        stage.addServerActionCommand(org.apache.ambari.server.serveraction.MockServerAction.class.getName(), null, org.apache.ambari.server.Role.AMBARI_SERVER_ACTION, org.apache.ambari.server.RoleCommand.EXECUTE, "cluster1", new org.apache.ambari.server.state.svccomphost.ServiceComponentHostServerActionEvent(null, java.lang.System.currentTimeMillis()), payload, null, null, timeout, false, autoSkipFailedTask);
        stage.getExecutionCommands(null).get(0).getExecutionCommand().setTaskId(stage.getOrderedHostRoleCommands().get(0).getTaskId());
        return stage;
    }

    @org.junit.Test
    public void testIndependentStagesExecution() throws java.lang.Exception {
        org.apache.ambari.server.state.Clusters fsm = Mockito.mock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster oneClusterMock = Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service serviceObj = Mockito.mock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.ServiceComponent scomp = Mockito.mock(org.apache.ambari.server.state.ServiceComponent.class);
        org.apache.ambari.server.state.ServiceComponentHost sch = Mockito.mock(org.apache.ambari.server.state.ServiceComponentHost.class);
        com.google.inject.persist.UnitOfWork unitOfWork = Mockito.mock(com.google.inject.persist.UnitOfWork.class);
        org.apache.ambari.server.events.publishers.AgentCommandsPublisher agentCommandsPublisher = Mockito.mock(org.apache.ambari.server.events.publishers.AgentCommandsPublisher.class);
        Mockito.when(fsm.getCluster(Matchers.anyString())).thenReturn(oneClusterMock);
        Mockito.when(oneClusterMock.getService(Matchers.anyString())).thenReturn(serviceObj);
        Mockito.when(serviceObj.getServiceComponent(Matchers.anyString())).thenReturn(scomp);
        Mockito.when(scomp.getServiceComponentHost(Matchers.anyString())).thenReturn(sch);
        Mockito.when(serviceObj.getCluster()).thenReturn(oneClusterMock);
        java.lang.String hostname1 = "ahost.ambari.apache.org";
        java.lang.String hostname2 = "bhost.ambari.apache.org";
        java.lang.String hostname3 = "chost.ambari.apache.org";
        java.lang.String hostname4 = "chost.ambari.apache.org";
        java.lang.Long hostId1 = 1L;
        java.lang.Long hostId2 = 2L;
        java.lang.Long hostId3 = 3L;
        java.lang.Long hostId4 = 4L;
        java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> hosts = new java.util.HashMap<>();
        hosts.put(hostname1, sch);
        hosts.put(hostname2, sch);
        hosts.put(hostname3, sch);
        hosts.put(hostname4, sch);
        Mockito.when(scomp.getServiceComponentHosts()).thenReturn(hosts);
        org.apache.ambari.server.state.Host host1 = Mockito.mock(org.apache.ambari.server.state.Host.class);
        Mockito.when(fsm.getHost(hostname1)).thenReturn(host1);
        Mockito.when(host1.getState()).thenReturn(org.apache.ambari.server.state.HostState.HEALTHY);
        Mockito.when(host1.getHostName()).thenReturn(hostname1);
        Mockito.when(host1.getHostId()).thenReturn(hostId1);
        org.apache.ambari.server.state.Host host2 = Mockito.mock(org.apache.ambari.server.state.Host.class);
        Mockito.when(fsm.getHost(hostname2)).thenReturn(host2);
        Mockito.when(host2.getState()).thenReturn(org.apache.ambari.server.state.HostState.HEALTHY);
        Mockito.when(host2.getHostName()).thenReturn(hostname2);
        Mockito.when(host2.getHostId()).thenReturn(hostId2);
        org.apache.ambari.server.state.Host host3 = Mockito.mock(org.apache.ambari.server.state.Host.class);
        Mockito.when(fsm.getHost(hostname3)).thenReturn(host3);
        Mockito.when(host3.getState()).thenReturn(org.apache.ambari.server.state.HostState.HEALTHY);
        Mockito.when(host3.getHostName()).thenReturn(hostname3);
        Mockito.when(host3.getHostId()).thenReturn(hostId3);
        org.apache.ambari.server.state.Host host4 = Mockito.mock(org.apache.ambari.server.state.Host.class);
        Mockito.when(fsm.getHost(hostname4)).thenReturn(host4);
        Mockito.when(host4.getState()).thenReturn(org.apache.ambari.server.state.HostState.HEALTHY);
        Mockito.when(host4.getHostName()).thenReturn(hostname4);
        Mockito.when(host4.getHostId()).thenReturn(hostId4);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> firstStageInProgressPerRequest = new java.util.ArrayList<>();
        firstStageInProgressPerRequest.add(getStageWithSingleTask(hostname1, "cluster1", org.apache.ambari.server.Role.DATANODE, org.apache.ambari.server.RoleCommand.START, org.apache.ambari.server.state.Service.Type.HDFS, 1, 1, 1));
        firstStageInProgressPerRequest.add(getStageWithSingleTask(hostname1, "cluster1", org.apache.ambari.server.Role.GANGLIA_MONITOR, org.apache.ambari.server.RoleCommand.START, org.apache.ambari.server.state.Service.Type.GANGLIA, 2, 2, 2));
        firstStageInProgressPerRequest.add(getStageWithSingleTask(hostname2, "cluster1", org.apache.ambari.server.Role.DATANODE, org.apache.ambari.server.RoleCommand.START, org.apache.ambari.server.state.Service.Type.HDFS, 3, 3, 3));
        firstStageInProgressPerRequest.add(getStageWithSingleTask(hostname3, "cluster1", org.apache.ambari.server.Role.DATANODE, org.apache.ambari.server.RoleCommand.START, org.apache.ambari.server.state.Service.Type.HDFS, 4, 4, 4));
        org.apache.ambari.server.actionmanager.ActionDBAccessor db = Mockito.mock(org.apache.ambari.server.actionmanager.ActionDBAccessor.class);
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAOMock = Mockito.mock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        org.mockito.Mockito.doNothing().when(hostRoleCommandDAOMock).publishTaskCreateEvent(Matchers.anyListOf(org.apache.ambari.server.actionmanager.HostRoleCommand.class));
        java.util.List<java.lang.String> blockingHostsRequest1 = new java.util.ArrayList<>();
        Mockito.when(hostRoleCommandDAOMock.getBlockingHostsForRequest(1, 1)).thenReturn(blockingHostsRequest1);
        java.util.List<java.lang.String> blockingHostsRequest2 = com.google.common.collect.Lists.newArrayList(hostname1);
        Mockito.when(hostRoleCommandDAOMock.getBlockingHostsForRequest(1, 2)).thenReturn(blockingHostsRequest2);
        org.apache.ambari.server.orm.entities.RequestEntity request = Mockito.mock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        Mockito.when(request.isExclusive()).thenReturn(false);
        Mockito.when(request.getClusterHostInfo()).thenReturn(org.apache.ambari.server.actionmanager.TestActionScheduler.CLUSTER_HOST_INFO);
        Mockito.when(db.getRequestEntity(Matchers.anyLong())).thenReturn(request);
        Mockito.when(db.getCommandsInProgressCount()).thenReturn(firstStageInProgressPerRequest.size());
        Mockito.when(db.getFirstStageInProgressPerRequest()).thenReturn(firstStageInProgressPerRequest);
        java.util.Properties properties = new java.util.Properties();
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.actionmanager.ActionScheduler scheduler = Mockito.spy(new org.apache.ambari.server.actionmanager.ActionScheduler(100, 50, db, fsm, 3, new org.apache.ambari.server.controller.HostsMap(((java.lang.String) (null))), unitOfWork, null, conf, entityManagerProviderMock, hostRoleCommandDAOMock, ((org.apache.ambari.server.actionmanager.HostRoleCommandFactory) (null)), agentCommandsPublisher));
        Mockito.doReturn(false).when(scheduler).wasAgentRestartedDuringOperation(Matchers.any(org.apache.ambari.server.state.Host.class), Matchers.any(org.apache.ambari.server.actionmanager.Stage.class), Matchers.anyString());
        scheduler.doWork();
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, firstStageInProgressPerRequest.get(0).getHostRoleStatus(hostname1, "DATANODE"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, firstStageInProgressPerRequest.get(1).getHostRoleStatus(hostname1, "GANGLIA_MONITOR"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, firstStageInProgressPerRequest.get(2).getHostRoleStatus(hostname2, "DATANODE"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, firstStageInProgressPerRequest.get(3).getHostRoleStatus(hostname3, "DATANODE"));
    }

    @org.junit.Test
    public void testIndependentStagesExecutionDisabled() throws java.lang.Exception {
        org.apache.ambari.server.state.Clusters fsm = Mockito.mock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster oneClusterMock = Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service serviceObj = Mockito.mock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.ServiceComponent scomp = Mockito.mock(org.apache.ambari.server.state.ServiceComponent.class);
        org.apache.ambari.server.state.ServiceComponentHost sch = Mockito.mock(org.apache.ambari.server.state.ServiceComponentHost.class);
        com.google.inject.persist.UnitOfWork unitOfWork = Mockito.mock(com.google.inject.persist.UnitOfWork.class);
        org.apache.ambari.server.events.publishers.AgentCommandsPublisher agentCommandsPublisher = Mockito.mock(org.apache.ambari.server.events.publishers.AgentCommandsPublisher.class);
        Mockito.when(fsm.getCluster(Matchers.anyString())).thenReturn(oneClusterMock);
        Mockito.when(oneClusterMock.getService(Matchers.anyString())).thenReturn(serviceObj);
        Mockito.when(serviceObj.getServiceComponent(Matchers.anyString())).thenReturn(scomp);
        Mockito.when(scomp.getServiceComponentHost(Matchers.anyString())).thenReturn(sch);
        Mockito.when(serviceObj.getCluster()).thenReturn(oneClusterMock);
        java.lang.String hostname1 = "ahost.ambari.apache.org";
        java.lang.String hostname2 = "bhost.ambari.apache.org";
        java.lang.String hostname3 = "chost.ambari.apache.org";
        java.lang.String hostname4 = "chost.ambari.apache.org";
        java.lang.Long hostId1 = 1L;
        java.lang.Long hostId2 = 2L;
        java.lang.Long hostId3 = 3L;
        java.lang.Long hostId4 = 4L;
        java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> hosts = new java.util.HashMap<>();
        hosts.put(hostname1, sch);
        hosts.put(hostname2, sch);
        hosts.put(hostname3, sch);
        hosts.put(hostname4, sch);
        Mockito.when(scomp.getServiceComponentHosts()).thenReturn(hosts);
        org.apache.ambari.server.state.Host host1 = Mockito.mock(org.apache.ambari.server.state.Host.class);
        Mockito.when(fsm.getHost(hostname1)).thenReturn(host1);
        Mockito.when(host1.getState()).thenReturn(org.apache.ambari.server.state.HostState.HEALTHY);
        Mockito.when(host1.getHostName()).thenReturn(hostname1);
        Mockito.when(host1.getHostId()).thenReturn(hostId1);
        org.apache.ambari.server.state.Host host2 = Mockito.mock(org.apache.ambari.server.state.Host.class);
        Mockito.when(fsm.getHost(hostname2)).thenReturn(host2);
        Mockito.when(host2.getState()).thenReturn(org.apache.ambari.server.state.HostState.HEALTHY);
        Mockito.when(host2.getHostName()).thenReturn(hostname2);
        Mockito.when(host2.getHostId()).thenReturn(hostId2);
        org.apache.ambari.server.state.Host host3 = Mockito.mock(org.apache.ambari.server.state.Host.class);
        Mockito.when(fsm.getHost(hostname3)).thenReturn(host3);
        Mockito.when(host3.getState()).thenReturn(org.apache.ambari.server.state.HostState.HEALTHY);
        Mockito.when(host3.getHostName()).thenReturn(hostname3);
        Mockito.when(host3.getHostId()).thenReturn(hostId3);
        org.apache.ambari.server.state.Host host4 = Mockito.mock(org.apache.ambari.server.state.Host.class);
        Mockito.when(fsm.getHost(hostname4)).thenReturn(host4);
        Mockito.when(host4.getState()).thenReturn(org.apache.ambari.server.state.HostState.HEALTHY);
        Mockito.when(host4.getHostName()).thenReturn(hostname4);
        Mockito.when(host4.getHostId()).thenReturn(hostId4);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = new java.util.ArrayList<>();
        org.apache.ambari.server.actionmanager.Stage stage = getStageWithSingleTask(hostname1, "cluster1", org.apache.ambari.server.Role.HIVE_CLIENT, org.apache.ambari.server.RoleCommand.INSTALL, org.apache.ambari.server.state.Service.Type.HIVE, 1, 1, 1);
        java.util.Map<java.lang.String, java.lang.String> hiveSite = new java.util.TreeMap<>();
        hiveSite.put("javax.jdo.option.ConnectionPassword", "password");
        hiveSite.put("hive.server2.thrift.port", "10000");
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> configurations = new java.util.TreeMap<>();
        configurations.put("hive-site", hiveSite);
        stage.getExecutionCommands(hostname1).get(0).getExecutionCommand().setConfigurations(configurations);
        stages.add(stage);
        stages.add(getStageWithSingleTask(hostname1, "cluster1", org.apache.ambari.server.Role.GANGLIA_MONITOR, org.apache.ambari.server.RoleCommand.START, org.apache.ambari.server.state.Service.Type.GANGLIA, 2, 2, 2));
        stages.add(getStageWithSingleTask(hostname2, "cluster1", org.apache.ambari.server.Role.HIVE_CLIENT, org.apache.ambari.server.RoleCommand.INSTALL, org.apache.ambari.server.state.Service.Type.HIVE, 3, 3, 3));
        stages.add(getStageWithSingleTask(hostname3, "cluster1", org.apache.ambari.server.Role.DATANODE, org.apache.ambari.server.RoleCommand.START, org.apache.ambari.server.state.Service.Type.HDFS, 4, 4, 4));
        stages.add(getStageWithSingleTask(hostname4, "cluster1", org.apache.ambari.server.Role.GANGLIA_MONITOR, org.apache.ambari.server.RoleCommand.START, org.apache.ambari.server.state.Service.Type.GANGLIA, 5, 5, 4));
        org.apache.ambari.server.actionmanager.ActionDBAccessor db = Mockito.mock(org.apache.ambari.server.actionmanager.ActionDBAccessor.class);
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAOMock = Mockito.mock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        org.mockito.Mockito.doNothing().when(hostRoleCommandDAOMock).publishTaskCreateEvent(Matchers.anyListOf(org.apache.ambari.server.actionmanager.HostRoleCommand.class));
        org.apache.ambari.server.orm.entities.RequestEntity request = Mockito.mock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        Mockito.when(request.isExclusive()).thenReturn(false);
        Mockito.when(request.getClusterHostInfo()).thenReturn(org.apache.ambari.server.actionmanager.TestActionScheduler.CLUSTER_HOST_INFO);
        Mockito.when(db.getRequestEntity(Matchers.anyLong())).thenReturn(request);
        Mockito.when(db.getCommandsInProgressCount()).thenReturn(stages.size());
        Mockito.when(db.getFirstStageInProgressPerRequest()).thenReturn(stages);
        java.util.Properties properties = new java.util.Properties();
        properties.put(org.apache.ambari.server.configuration.Configuration.PARALLEL_STAGE_EXECUTION.getKey(), "false");
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.actionmanager.ActionScheduler scheduler = Mockito.spy(new org.apache.ambari.server.actionmanager.ActionScheduler(100, 50, db, fsm, 3, new org.apache.ambari.server.controller.HostsMap(((java.lang.String) (null))), unitOfWork, null, conf, entityManagerProviderMock, hostRoleCommandDAOMock, ((org.apache.ambari.server.actionmanager.HostRoleCommandFactory) (null)), agentCommandsPublisher));
        Mockito.doReturn(false).when(scheduler).wasAgentRestartedDuringOperation(Matchers.any(org.apache.ambari.server.state.Host.class), Matchers.any(org.apache.ambari.server.actionmanager.Stage.class), Matchers.anyString());
        scheduler.doWork();
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, stages.get(0).getHostRoleStatus(hostname1, "HIVE_CLIENT"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, stages.get(1).getHostRoleStatus(hostname1, "GANGLIA_MONITOR"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, stages.get(2).getHostRoleStatus(hostname2, "HIVE_CLIENT"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, stages.get(3).getHostRoleStatus(hostname3, "DATANODE"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, stages.get(4).getHostRoleStatus(hostname4, "GANGLIA_MONITOR"));
        org.junit.Assert.assertFalse(stages.get(0).getExecutionCommands(hostname1).get(0).getExecutionCommand().getConfigurations().containsKey("javax.jdo.option.ConnectionPassword"));
    }

    @org.junit.Test
    public void testBackgroundStagesExecutionEnable() throws java.lang.Exception {
        org.apache.ambari.server.state.Clusters fsm = Mockito.mock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster oneClusterMock = Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service serviceObj = Mockito.mock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.ServiceComponent scomp = Mockito.mock(org.apache.ambari.server.state.ServiceComponent.class);
        org.apache.ambari.server.state.ServiceComponentHost sch = Mockito.mock(org.apache.ambari.server.state.ServiceComponentHost.class);
        com.google.inject.persist.UnitOfWork unitOfWork = Mockito.mock(com.google.inject.persist.UnitOfWork.class);
        org.apache.ambari.server.events.publishers.AgentCommandsPublisher agentCommandsPublisher = Mockito.mock(org.apache.ambari.server.events.publishers.AgentCommandsPublisher.class);
        Mockito.when(fsm.getCluster(Matchers.anyString())).thenReturn(oneClusterMock);
        Mockito.when(oneClusterMock.getService(Matchers.anyString())).thenReturn(serviceObj);
        Mockito.when(serviceObj.getServiceComponent(Matchers.anyString())).thenReturn(scomp);
        Mockito.when(scomp.getServiceComponentHost(Matchers.anyString())).thenReturn(sch);
        Mockito.when(serviceObj.getCluster()).thenReturn(oneClusterMock);
        java.lang.String hostname1 = "ahost.ambari.apache.org";
        java.lang.String hostname2 = "bhost.ambari.apache.org";
        java.lang.Long hostId1 = 1L;
        java.lang.Long hostId2 = 2L;
        java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> hosts = new java.util.HashMap<>();
        hosts.put(hostname1, sch);
        hosts.put(hostname2, sch);
        Mockito.when(scomp.getServiceComponentHosts()).thenReturn(hosts);
        org.apache.ambari.server.state.Host host1 = Mockito.mock(org.apache.ambari.server.state.Host.class);
        Mockito.when(fsm.getHost(hostname1)).thenReturn(host1);
        Mockito.when(host1.getState()).thenReturn(org.apache.ambari.server.state.HostState.HEALTHY);
        Mockito.when(host1.getHostName()).thenReturn(hostname1);
        Mockito.when(host1.getHostId()).thenReturn(hostId1);
        org.apache.ambari.server.state.Host host2 = Mockito.mock(org.apache.ambari.server.state.Host.class);
        Mockito.when(fsm.getHost(hostname2)).thenReturn(host2);
        Mockito.when(host2.getState()).thenReturn(org.apache.ambari.server.state.HostState.HEALTHY);
        Mockito.when(host2.getHostName()).thenReturn(hostname2);
        Mockito.when(host2.getHostId()).thenReturn(hostId2);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = new java.util.ArrayList<>();
        org.apache.ambari.server.actionmanager.Stage backgroundStage = null;
        stages.add(backgroundStage = getStageWithSingleTask(hostname1, "cluster1", org.apache.ambari.server.Role.NAMENODE, org.apache.ambari.server.RoleCommand.CUSTOM_COMMAND, "REBALANCEHDFS", org.apache.ambari.server.state.Service.Type.HDFS, 1, 1, 1));
        org.junit.Assert.assertEquals(org.apache.ambari.server.agent.AgentCommand.AgentCommandType.BACKGROUND_EXECUTION_COMMAND, backgroundStage.getExecutionCommands(hostname1).get(0).getExecutionCommand().getCommandType());
        stages.add(getStageWithSingleTask(hostname1, "cluster1", org.apache.ambari.server.Role.GANGLIA_MONITOR, org.apache.ambari.server.RoleCommand.START, org.apache.ambari.server.state.Service.Type.GANGLIA, 2, 2, 2));
        stages.add(getStageWithSingleTask(hostname2, "cluster1", org.apache.ambari.server.Role.DATANODE, org.apache.ambari.server.RoleCommand.START, org.apache.ambari.server.state.Service.Type.HDFS, 3, 3, 3));
        org.apache.ambari.server.actionmanager.ActionDBAccessor db = Mockito.mock(org.apache.ambari.server.actionmanager.ActionDBAccessor.class);
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAOMock = Mockito.mock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        org.mockito.Mockito.doNothing().when(hostRoleCommandDAOMock).publishTaskCreateEvent(Matchers.anyListOf(org.apache.ambari.server.actionmanager.HostRoleCommand.class));
        org.apache.ambari.server.orm.entities.RequestEntity request = Mockito.mock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        Mockito.when(request.isExclusive()).thenReturn(false);
        Mockito.when(request.getClusterHostInfo()).thenReturn(org.apache.ambari.server.actionmanager.TestActionScheduler.CLUSTER_HOST_INFO);
        Mockito.when(db.getRequestEntity(Matchers.anyLong())).thenReturn(request);
        Mockito.when(db.getCommandsInProgressCount()).thenReturn(stages.size());
        Mockito.when(db.getFirstStageInProgressPerRequest()).thenReturn(stages);
        java.util.Properties properties = new java.util.Properties();
        properties.put(org.apache.ambari.server.configuration.Configuration.PARALLEL_STAGE_EXECUTION.getKey(), "true");
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.actionmanager.ActionScheduler scheduler = Mockito.spy(new org.apache.ambari.server.actionmanager.ActionScheduler(100, 50, db, fsm, 3, new org.apache.ambari.server.controller.HostsMap(((java.lang.String) (null))), unitOfWork, null, conf, entityManagerProviderMock, hostRoleCommandDAOMock, ((org.apache.ambari.server.actionmanager.HostRoleCommandFactory) (null)), agentCommandsPublisher));
        Mockito.doReturn(false).when(scheduler).wasAgentRestartedDuringOperation(Matchers.any(org.apache.ambari.server.state.Host.class), Matchers.any(org.apache.ambari.server.actionmanager.Stage.class), Matchers.anyString());
        scheduler.doWork();
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, stages.get(0).getHostRoleStatus(hostname1, "NAMENODE"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, stages.get(2).getHostRoleStatus(hostname2, "DATANODE"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, stages.get(1).getHostRoleStatus(hostname1, "GANGLIA_MONITOR"));
    }

    @org.junit.Test
    public void testRequestFailureOnStageFailure() throws java.lang.Exception {
        org.apache.ambari.server.state.Clusters fsm = Mockito.mock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster oneClusterMock = Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service serviceObj = Mockito.mock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.ServiceComponent scomp = Mockito.mock(org.apache.ambari.server.state.ServiceComponent.class);
        org.apache.ambari.server.state.ServiceComponentHost sch = Mockito.mock(org.apache.ambari.server.state.ServiceComponentHost.class);
        com.google.inject.persist.UnitOfWork unitOfWork = Mockito.mock(com.google.inject.persist.UnitOfWork.class);
        org.apache.ambari.server.events.publishers.AmbariEventPublisher ambariEventPublisher = Mockito.mock(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class);
        org.apache.ambari.server.actionmanager.RequestFactory requestFactory = Mockito.mock(org.apache.ambari.server.actionmanager.RequestFactory.class);
        Mockito.when(fsm.getCluster(Matchers.anyString())).thenReturn(oneClusterMock);
        Mockito.when(oneClusterMock.getService(Matchers.anyString())).thenReturn(serviceObj);
        Mockito.when(serviceObj.getServiceComponent(Matchers.anyString())).thenReturn(scomp);
        Mockito.when(scomp.getServiceComponentHost(Matchers.anyString())).thenReturn(sch);
        Mockito.when(serviceObj.getCluster()).thenReturn(oneClusterMock);
        java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> hosts = new java.util.HashMap<>();
        hosts.put(hostname, sch);
        Mockito.when(scomp.getServiceComponentHosts()).thenReturn(hosts);
        final java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = new java.util.ArrayList<>();
        stages.add(getStageWithSingleTask(hostname, "cluster1", org.apache.ambari.server.Role.NAMENODE, org.apache.ambari.server.RoleCommand.UPGRADE, org.apache.ambari.server.state.Service.Type.HDFS, 1, 1, 1));
        java.util.List<org.apache.ambari.server.actionmanager.Stage> firstStageInProgress = java.util.Collections.singletonList(stages.get(0));
        stages.add(getStageWithSingleTask(hostname, "cluster1", org.apache.ambari.server.Role.DATANODE, org.apache.ambari.server.RoleCommand.UPGRADE, org.apache.ambari.server.state.Service.Type.HDFS, 2, 2, 1));
        org.apache.ambari.server.state.Host host = Mockito.mock(org.apache.ambari.server.state.Host.class);
        Mockito.when(fsm.getHost(Matchers.anyString())).thenReturn(host);
        Mockito.when(host.getState()).thenReturn(org.apache.ambari.server.state.HostState.HEALTHY);
        Mockito.when(host.getHostName()).thenReturn(hostname);
        org.apache.ambari.server.actionmanager.ActionDBAccessor db = Mockito.mock(org.apache.ambari.server.actionmanager.ActionDBAccessor.class);
        org.apache.ambari.server.orm.entities.RequestEntity request = Mockito.mock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        Mockito.when(request.isExclusive()).thenReturn(false);
        Mockito.when(request.getClusterHostInfo()).thenReturn(org.apache.ambari.server.actionmanager.TestActionScheduler.CLUSTER_HOST_INFO);
        Mockito.when(db.getRequestEntity(Matchers.anyLong())).thenReturn(request);
        Mockito.when(db.getCommandsInProgressCount()).thenReturn(stages.size());
        Mockito.when(db.getFirstStageInProgressPerRequest()).thenReturn(firstStageInProgress);
        Mockito.doAnswer(new org.mockito.stubbing.Answer<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                java.util.List<org.apache.ambari.server.agent.CommandReport> reports = ((java.util.List<org.apache.ambari.server.agent.CommandReport>) (invocation.getArguments()[0]));
                for (org.apache.ambari.server.agent.CommandReport report : reports) {
                    java.lang.String actionId = report.getActionId();
                    long[] requestStageIds = org.apache.ambari.server.utils.StageUtils.getRequestStage(actionId);
                    java.lang.Long requestId = requestStageIds[0];
                    java.lang.Long stageId = requestStageIds[1];
                    java.lang.Long id = report.getTaskId();
                    for (org.apache.ambari.server.actionmanager.Stage stage : stages) {
                        if (requestId.equals(stage.getRequestId()) && stageId.equals(stage.getStageId())) {
                            for (org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand : stage.getOrderedHostRoleCommands()) {
                                if (hostRoleCommand.getTaskId() == id) {
                                    hostRoleCommand.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.valueOf(report.getStatus()));
                                }
                            }
                        }
                    }
                }
                return null;
            }
        }).when(db).updateHostRoleStates(Matchers.anyCollectionOf(org.apache.ambari.server.agent.CommandReport.class));
        Mockito.when(db.getTask(Matchers.anyLong())).thenAnswer(new org.mockito.stubbing.Answer<java.lang.Object>() {
            @java.lang.Override
            public java.lang.Object answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                java.lang.Long taskId = ((java.lang.Long) (invocation.getArguments()[0]));
                for (org.apache.ambari.server.actionmanager.Stage stage : stages) {
                    for (org.apache.ambari.server.actionmanager.HostRoleCommand command : stage.getOrderedHostRoleCommands()) {
                        if (taskId.equals(command.getTaskId())) {
                            return command;
                        }
                    }
                }
                return null;
            }
        });
        Mockito.doAnswer(new org.mockito.stubbing.Answer<java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity>>() {
            @java.lang.Override
            public java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                java.lang.Long requestId = ((java.lang.Long) (invocation.getArguments()[0]));
                java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> abortedCommands = com.google.common.collect.Lists.newArrayList();
                for (org.apache.ambari.server.actionmanager.Stage stage : stages) {
                    if (requestId.equals(stage.getRequestId())) {
                        for (org.apache.ambari.server.actionmanager.HostRoleCommand command : stage.getOrderedHostRoleCommands()) {
                            if (((command.getStatus() == org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED) || (command.getStatus() == org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS)) || (command.getStatus() == org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING)) {
                                command.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED);
                                org.apache.ambari.server.orm.entities.HostRoleCommandEntity hostRoleCommandEntity = command.constructNewPersistenceEntity();
                                hostRoleCommandEntity.setStage(stage.constructNewPersistenceEntity());
                                abortedCommands.add(hostRoleCommandEntity);
                            }
                        }
                    }
                }
                return abortedCommands;
            }
        }).when(db).abortOperation(Matchers.anyLong());
        java.util.Properties properties = new java.util.Properties();
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(properties);
        org.easymock.Capture<java.util.Collection<org.apache.ambari.server.actionmanager.HostRoleCommand>> cancelCommandList = org.easymock.EasyMock.newCapture();
        org.apache.ambari.server.actionmanager.ActionScheduler scheduler = org.easymock.EasyMock.createMockBuilder(org.apache.ambari.server.actionmanager.ActionScheduler.class).withConstructor(((long) (100)), ((long) (50)), db, fsm, 3, new org.apache.ambari.server.controller.HostsMap(((java.lang.String) (null))), unitOfWork, org.easymock.EasyMock.createNiceMock(org.apache.ambari.server.events.publishers.AmbariEventPublisher.class), conf, entityManagerProviderMock, Mockito.mock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class), Mockito.mock(org.apache.ambari.server.actionmanager.HostRoleCommandFactory.class), Mockito.mock(org.apache.ambari.server.metadata.RoleCommandOrderProvider.class), Mockito.mock(org.apache.ambari.server.events.publishers.AgentCommandsPublisher.class)).addMockedMethod("cancelHostRoleCommands").createMock();
        scheduler.cancelHostRoleCommands(org.easymock.EasyMock.capture(cancelCommandList), org.easymock.EasyMock.eq(org.apache.ambari.server.actionmanager.ActionScheduler.FAILED_TASK_ABORT_REASONING));
        org.easymock.EasyMock.expectLastCall().once();
        org.easymock.EasyMock.replay(scheduler);
        org.apache.ambari.server.actionmanager.ActionManager am = new org.apache.ambari.server.actionmanager.ActionManager(db, requestFactory, scheduler);
        scheduler.doWork();
        java.util.List<org.apache.ambari.server.agent.CommandReport> reports = new java.util.ArrayList<>();
        reports.add(getCommandReport(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, org.apache.ambari.server.Role.NAMENODE, org.apache.ambari.server.state.Service.Type.HDFS, "1-1", 1));
        am.processTaskResponse(hostname, reports, org.apache.ambari.server.utils.CommandUtils.convertToTaskIdCommandMap(stages.get(0).getOrderedHostRoleCommands()));
        scheduler.doWork();
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, stages.get(0).getHostRoleStatus(hostname, "NAMENODE"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, stages.get(1).getHostRoleStatus(hostname, "DATANODE"));
        org.junit.Assert.assertEquals(cancelCommandList.getValue().size(), 1);
        org.easymock.EasyMock.verify(scheduler, entityManagerProviderMock);
    }

    @org.junit.Test
    public void testRequestAbortsOnlyWhenNoQueuedTaskAndSuccessFactorUnmet() throws java.lang.Exception {
        org.apache.ambari.server.state.Clusters fsm = Mockito.mock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster oneClusterMock = Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service serviceObj = Mockito.mock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.ServiceComponent scomp = Mockito.mock(org.apache.ambari.server.state.ServiceComponent.class);
        org.apache.ambari.server.state.ServiceComponentHost sch = Mockito.mock(org.apache.ambari.server.state.ServiceComponentHost.class);
        com.google.inject.persist.UnitOfWork unitOfWork = Mockito.mock(com.google.inject.persist.UnitOfWork.class);
        org.apache.ambari.server.events.publishers.AgentCommandsPublisher agentCommandsPublisher = Mockito.mock(org.apache.ambari.server.events.publishers.AgentCommandsPublisher.class);
        Mockito.when(fsm.getCluster(Matchers.anyString())).thenReturn(oneClusterMock);
        Mockito.when(oneClusterMock.getService(Matchers.anyString())).thenReturn(serviceObj);
        Mockito.when(serviceObj.getServiceComponent(Matchers.anyString())).thenReturn(scomp);
        Mockito.when(scomp.getServiceComponentHost(Matchers.anyString())).thenReturn(sch);
        Mockito.when(serviceObj.getCluster()).thenReturn(oneClusterMock);
        java.lang.String host1 = "host1";
        java.lang.String host2 = "host2";
        org.apache.ambari.server.state.Host host = Mockito.mock(org.apache.ambari.server.state.Host.class);
        java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> hosts = new java.util.HashMap<>();
        hosts.put(host1, sch);
        hosts.put(host2, sch);
        Mockito.when(scomp.getServiceComponentHosts()).thenReturn(hosts);
        Mockito.when(fsm.getHost(Matchers.anyString())).thenReturn(host);
        Mockito.when(host.getState()).thenReturn(org.apache.ambari.server.state.HostState.HEALTHY);
        Mockito.when(host.getHostName()).thenReturn(host1);
        org.apache.ambari.server.orm.entities.HostEntity hostEntity1 = new org.apache.ambari.server.orm.entities.HostEntity();
        org.apache.ambari.server.orm.entities.HostEntity hostEntity2 = new org.apache.ambari.server.orm.entities.HostEntity();
        hostEntity1.setHostName(host1);
        hostEntity2.setHostName(host2);
        hostDAO.create(hostEntity1);
        hostDAO.create(hostEntity2);
        long now = java.lang.System.currentTimeMillis();
        org.apache.ambari.server.actionmanager.Stage stage = stageFactory.createNew(1, "/tmp", "cluster1", 1L, "testRequestFailureBasedOnSuccessFactor", "", "");
        stage.setStageId(1);
        addHostRoleExecutionCommand(now, stage, org.apache.ambari.server.Role.SQOOP, org.apache.ambari.server.state.Service.Type.SQOOP, org.apache.ambari.server.RoleCommand.INSTALL, host1, "cluster1");
        addHostRoleExecutionCommand(now, stage, org.apache.ambari.server.Role.OOZIE_CLIENT, org.apache.ambari.server.state.Service.Type.OOZIE, org.apache.ambari.server.RoleCommand.INSTALL, host1, "cluster1");
        addHostRoleExecutionCommand(now, stage, org.apache.ambari.server.Role.MAPREDUCE_CLIENT, org.apache.ambari.server.state.Service.Type.MAPREDUCE, org.apache.ambari.server.RoleCommand.INSTALL, host1, "cluster1");
        addHostRoleExecutionCommand(now, stage, org.apache.ambari.server.Role.HBASE_CLIENT, org.apache.ambari.server.state.Service.Type.HBASE, org.apache.ambari.server.RoleCommand.INSTALL, host1, "cluster1");
        addHostRoleExecutionCommand(now, stage, org.apache.ambari.server.Role.GANGLIA_MONITOR, org.apache.ambari.server.state.Service.Type.GANGLIA, org.apache.ambari.server.RoleCommand.INSTALL, host1, "cluster1");
        addHostRoleExecutionCommand(now, stage, org.apache.ambari.server.Role.HBASE_CLIENT, org.apache.ambari.server.state.Service.Type.HBASE, org.apache.ambari.server.RoleCommand.INSTALL, host2, "cluster1");
        addHostRoleExecutionCommand(now, stage, org.apache.ambari.server.Role.GANGLIA_MONITOR, org.apache.ambari.server.state.Service.Type.GANGLIA, org.apache.ambari.server.RoleCommand.INSTALL, host2, "cluster1");
        final java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = java.util.Collections.singletonList(stage);
        org.apache.ambari.server.actionmanager.HostRoleStatus[] statusesAtIterOne = new org.apache.ambari.server.actionmanager.HostRoleStatus[]{ org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED };
        for (int index = 0; index < stage.getOrderedHostRoleCommands().size(); index++) {
            stage.getOrderedHostRoleCommands().get(index).setTaskId(index + 1);
            stage.getOrderedHostRoleCommands().get(index).setStatus(statusesAtIterOne[index]);
        }
        stage.setLastAttemptTime(host1, org.apache.ambari.server.Role.SQOOP.toString(), now);
        stage.setLastAttemptTime(host1, org.apache.ambari.server.Role.MAPREDUCE_CLIENT.toString(), now);
        stage.setLastAttemptTime(host1, org.apache.ambari.server.Role.OOZIE_CLIENT.toString(), now);
        stage.setLastAttemptTime(host1, org.apache.ambari.server.Role.GANGLIA_MONITOR.toString(), now);
        stage.setLastAttemptTime(host1, org.apache.ambari.server.Role.HBASE_CLIENT.toString(), now);
        stage.setLastAttemptTime(host2, org.apache.ambari.server.Role.GANGLIA_MONITOR.toString(), now);
        stage.setLastAttemptTime(host2, org.apache.ambari.server.Role.HBASE_CLIENT.toString(), now);
        org.apache.ambari.server.actionmanager.ActionDBAccessor db = Mockito.mock(org.apache.ambari.server.actionmanager.ActionDBAccessor.class);
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAOMock = Mockito.mock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        org.mockito.Mockito.doNothing().when(hostRoleCommandDAOMock).publishTaskCreateEvent(Matchers.anyListOf(org.apache.ambari.server.actionmanager.HostRoleCommand.class));
        org.apache.ambari.server.orm.entities.RequestEntity request = Mockito.mock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        Mockito.when(request.isExclusive()).thenReturn(false);
        Mockito.when(db.getRequestEntity(Matchers.anyLong())).thenReturn(request);
        Mockito.when(db.getCommandsInProgressCount()).thenReturn(stages.size());
        Mockito.when(db.getFirstStageInProgressPerRequest()).thenReturn(stages);
        Mockito.doAnswer(new org.mockito.stubbing.Answer<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                java.lang.String host = ((java.lang.String) (invocation.getArguments()[0]));
                java.lang.Long requestId = ((java.lang.Long) (invocation.getArguments()[1]));
                java.lang.Long stageId = ((java.lang.Long) (invocation.getArguments()[2]));
                java.lang.String role = ((java.lang.String) (invocation.getArguments()[3]));
                org.apache.ambari.server.agent.CommandReport commandReport = ((org.apache.ambari.server.agent.CommandReport) (invocation.getArguments()[4]));
                for (org.apache.ambari.server.actionmanager.Stage stage : stages) {
                    if (requestId.equals(stage.getRequestId()) && stageId.equals(stage.getStageId())) {
                        org.apache.ambari.server.actionmanager.HostRoleCommand command = stage.getHostRoleCommand(host, role);
                        command.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.valueOf(commandReport.getStatus()));
                    }
                }
                return null;
            }
        }).when(db).updateHostRoleState(Matchers.anyString(), Matchers.anyLong(), Matchers.anyLong(), Matchers.anyString(), Matchers.any(org.apache.ambari.server.agent.CommandReport.class));
        Mockito.when(db.getTask(Matchers.anyLong())).thenAnswer(new org.mockito.stubbing.Answer<java.lang.Object>() {
            @java.lang.Override
            public java.lang.Object answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                java.lang.Long taskId = ((java.lang.Long) (invocation.getArguments()[0]));
                for (org.apache.ambari.server.actionmanager.Stage stage : stages) {
                    for (org.apache.ambari.server.actionmanager.HostRoleCommand command : stage.getOrderedHostRoleCommands()) {
                        if (taskId.equals(command.getTaskId())) {
                            return command;
                        }
                    }
                }
                return null;
            }
        });
        Mockito.doAnswer(new org.mockito.stubbing.Answer<java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity>>() {
            @java.lang.Override
            public java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                java.lang.Long requestId = ((java.lang.Long) (invocation.getArguments()[0]));
                java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> abortedCommands = com.google.common.collect.Lists.newArrayList();
                for (org.apache.ambari.server.actionmanager.Stage stage : stages) {
                    if (requestId.equals(stage.getRequestId())) {
                        for (org.apache.ambari.server.actionmanager.HostRoleCommand command : stage.getOrderedHostRoleCommands()) {
                            if (((command.getStatus() == org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED) || (command.getStatus() == org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS)) || (command.getStatus() == org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING)) {
                                command.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED);
                                org.apache.ambari.server.orm.entities.HostRoleCommandEntity hostRoleCommandEntity = command.constructNewPersistenceEntity();
                                hostRoleCommandEntity.setStage(stage.constructNewPersistenceEntity());
                                abortedCommands.add(hostRoleCommandEntity);
                            }
                        }
                    }
                }
                return abortedCommands;
            }
        }).when(db).abortOperation(Matchers.anyLong());
        java.util.Properties properties = new java.util.Properties();
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.actionmanager.ActionScheduler scheduler = new org.apache.ambari.server.actionmanager.ActionScheduler(100, 10000, db, fsm, 3, new org.apache.ambari.server.controller.HostsMap(((java.lang.String) (null))), unitOfWork, null, conf, entityManagerProviderMock, hostRoleCommandDAOMock, ((org.apache.ambari.server.actionmanager.HostRoleCommandFactory) (null)), agentCommandsPublisher);
        scheduler.doWork();
        org.apache.ambari.server.actionmanager.HostRoleStatus[] expectedStatusesAtIterOne = new org.apache.ambari.server.actionmanager.HostRoleStatus[]{ org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED };
        for (int index = 0; index < stage.getOrderedHostRoleCommands().size(); index++) {
            org.apache.ambari.server.actionmanager.TestActionScheduler.log.info(stage.getOrderedHostRoleCommands().get(index).toString());
            org.junit.Assert.assertEquals(expectedStatusesAtIterOne[index], stage.getOrderedHostRoleCommands().get(index).getStatus());
        }
        org.apache.ambari.server.actionmanager.HostRoleStatus[] statusesAtIterTwo = new org.apache.ambari.server.actionmanager.HostRoleStatus[]{ org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED };
        for (int index = 0; index < stage.getOrderedHostRoleCommands().size(); index++) {
            stage.getOrderedHostRoleCommands().get(index).setStatus(statusesAtIterTwo[index]);
        }
        scheduler.doWork();
        org.apache.ambari.server.actionmanager.HostRoleStatus[] expectedStatusesAtIterTwo = new org.apache.ambari.server.actionmanager.HostRoleStatus[]{ org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED };
        for (int index = 0; index < stage.getOrderedHostRoleCommands().size(); index++) {
            org.apache.ambari.server.actionmanager.TestActionScheduler.log.info(stage.getOrderedHostRoleCommands().get(index).toString());
            org.junit.Assert.assertEquals(expectedStatusesAtIterTwo[index], stage.getOrderedHostRoleCommands().get(index).getStatus());
        }
        org.apache.ambari.server.actionmanager.HostRoleStatus[] statusesAtIterThree = new org.apache.ambari.server.actionmanager.HostRoleStatus[]{ org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED };
        for (int index = 0; index < stage.getOrderedHostRoleCommands().size(); index++) {
            stage.getOrderedHostRoleCommands().get(index).setStatus(statusesAtIterThree[index]);
        }
        scheduler.doWork();
        org.apache.ambari.server.actionmanager.HostRoleStatus[] expectedStatusesAtIterThree = new org.apache.ambari.server.actionmanager.HostRoleStatus[]{ org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED };
        for (int index = 0; index < stage.getOrderedHostRoleCommands().size(); index++) {
            org.apache.ambari.server.actionmanager.TestActionScheduler.log.info(stage.getOrderedHostRoleCommands().get(index).toString());
            org.junit.Assert.assertEquals(expectedStatusesAtIterThree[index], stage.getOrderedHostRoleCommands().get(index).getStatus());
        }
    }

    private void addHostRoleExecutionCommand(long now, org.apache.ambari.server.actionmanager.Stage stage, org.apache.ambari.server.Role role, org.apache.ambari.server.state.Service.Type service, org.apache.ambari.server.RoleCommand command, java.lang.String host, java.lang.String cluster) {
        stage.addHostRoleExecutionCommand(host, role, command, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostInstallEvent(role.toString(), host, now, "HDP-0.2"), cluster, service.toString(), false, false);
        stage.getExecutionCommandWrapper(host, role.toString()).getExecutionCommand();
    }

    @org.junit.Test
    public void testRequestFailureBasedOnSuccessFactor() throws java.lang.Exception {
        org.apache.ambari.server.state.Clusters fsm = Mockito.mock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster oneClusterMock = Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service serviceObj = Mockito.mock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.ServiceComponent scomp = Mockito.mock(org.apache.ambari.server.state.ServiceComponent.class);
        org.apache.ambari.server.state.ServiceComponentHost sch = Mockito.mock(org.apache.ambari.server.state.ServiceComponentHost.class);
        com.google.inject.persist.UnitOfWork unitOfWork = Mockito.mock(com.google.inject.persist.UnitOfWork.class);
        org.apache.ambari.server.events.publishers.AgentCommandsPublisher agentCommandsPublisher = Mockito.mock(org.apache.ambari.server.events.publishers.AgentCommandsPublisher.class);
        org.apache.ambari.server.actionmanager.RequestFactory requestFactory = Mockito.mock(org.apache.ambari.server.actionmanager.RequestFactory.class);
        Mockito.when(fsm.getCluster(Matchers.anyString())).thenReturn(oneClusterMock);
        Mockito.when(oneClusterMock.getService(Matchers.anyString())).thenReturn(serviceObj);
        Mockito.when(serviceObj.getServiceComponent(Matchers.anyString())).thenReturn(scomp);
        Mockito.when(scomp.getServiceComponentHost(Matchers.anyString())).thenReturn(sch);
        Mockito.when(serviceObj.getCluster()).thenReturn(oneClusterMock);
        final java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = new java.util.ArrayList<>();
        long now = java.lang.System.currentTimeMillis();
        org.apache.ambari.server.actionmanager.Stage stage = stageFactory.createNew(1, "/tmp", "cluster1", 1L, "testRequestFailureBasedOnSuccessFactor", "", "");
        stage.setStageId(1);
        stage.addHostRoleExecutionCommand("host1", org.apache.ambari.server.Role.DATANODE, org.apache.ambari.server.RoleCommand.UPGRADE, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostUpgradeEvent(org.apache.ambari.server.Role.DATANODE.toString(), "host1", now, "HDP-0.2"), "cluster1", org.apache.ambari.server.state.Service.Type.HDFS.toString(), false, false);
        stage.getExecutionCommandWrapper("host1", org.apache.ambari.server.Role.DATANODE.toString()).getExecutionCommand();
        stage.addHostRoleExecutionCommand("host2", org.apache.ambari.server.Role.DATANODE, org.apache.ambari.server.RoleCommand.UPGRADE, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostUpgradeEvent(org.apache.ambari.server.Role.DATANODE.toString(), "host2", now, "HDP-0.2"), "cluster1", org.apache.ambari.server.state.Service.Type.HDFS.toString(), false, false);
        stage.getExecutionCommandWrapper("host2", org.apache.ambari.server.Role.DATANODE.toString()).getExecutionCommand();
        stage.addHostRoleExecutionCommand("host3", org.apache.ambari.server.Role.DATANODE, org.apache.ambari.server.RoleCommand.UPGRADE, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostUpgradeEvent(org.apache.ambari.server.Role.DATANODE.toString(), "host3", now, "HDP-0.2"), "cluster1", org.apache.ambari.server.state.Service.Type.HDFS.toString(), false, false);
        stage.getExecutionCommandWrapper("host3", org.apache.ambari.server.Role.DATANODE.toString()).getExecutionCommand();
        stages.add(stage);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stageInProgress = java.util.Collections.singletonList(stage);
        stage.getOrderedHostRoleCommands().get(0).setTaskId(1);
        stage.getOrderedHostRoleCommands().get(1).setTaskId(2);
        stage.getOrderedHostRoleCommands().get(2).setTaskId(3);
        stages.add(getStageWithSingleTask("host1", "cluster1", org.apache.ambari.server.Role.HDFS_CLIENT, org.apache.ambari.server.RoleCommand.UPGRADE, org.apache.ambari.server.state.Service.Type.HDFS, 4, 2, 1));
        org.apache.ambari.server.actionmanager.ActionDBAccessor db = Mockito.mock(org.apache.ambari.server.actionmanager.ActionDBAccessor.class);
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAOMock = Mockito.mock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        org.mockito.Mockito.doNothing().when(hostRoleCommandDAOMock).publishTaskCreateEvent(Matchers.anyListOf(org.apache.ambari.server.actionmanager.HostRoleCommand.class));
        org.apache.ambari.server.orm.entities.RequestEntity request = Mockito.mock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        Mockito.when(request.isExclusive()).thenReturn(false);
        Mockito.when(db.getRequestEntity(Matchers.anyLong())).thenReturn(request);
        Mockito.when(db.getCommandsInProgressCount()).thenReturn(stageInProgress.size());
        Mockito.when(db.getFirstStageInProgressPerRequest()).thenReturn(stageInProgress);
        Mockito.doAnswer(new org.mockito.stubbing.Answer<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                java.util.List<org.apache.ambari.server.agent.CommandReport> reports = ((java.util.List<org.apache.ambari.server.agent.CommandReport>) (invocation.getArguments()[0]));
                for (org.apache.ambari.server.agent.CommandReport report : reports) {
                    java.lang.String actionId = report.getActionId();
                    long[] requestStageIds = org.apache.ambari.server.utils.StageUtils.getRequestStage(actionId);
                    java.lang.Long requestId = requestStageIds[0];
                    java.lang.Long stageId = requestStageIds[1];
                    java.lang.Long id = report.getTaskId();
                    for (org.apache.ambari.server.actionmanager.Stage stage : stages) {
                        if (requestId.equals(stage.getRequestId()) && stageId.equals(stage.getStageId())) {
                            for (org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand : stage.getOrderedHostRoleCommands()) {
                                if (hostRoleCommand.getTaskId() == id) {
                                    hostRoleCommand.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.valueOf(report.getStatus()));
                                }
                            }
                        }
                    }
                }
                return null;
            }
        }).when(db).updateHostRoleStates(Matchers.anyCollectionOf(org.apache.ambari.server.agent.CommandReport.class));
        Mockito.when(db.getTask(Matchers.anyLong())).thenAnswer(new org.mockito.stubbing.Answer<java.lang.Object>() {
            @java.lang.Override
            public java.lang.Object answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                java.lang.Long taskId = ((java.lang.Long) (invocation.getArguments()[0]));
                for (org.apache.ambari.server.actionmanager.Stage stage : stages) {
                    for (org.apache.ambari.server.actionmanager.HostRoleCommand command : stage.getOrderedHostRoleCommands()) {
                        if (taskId.equals(command.getTaskId())) {
                            return command;
                        }
                    }
                }
                return null;
            }
        });
        Mockito.doAnswer(new org.mockito.stubbing.Answer<java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity>>() {
            @java.lang.Override
            public java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                java.lang.Long requestId = ((java.lang.Long) (invocation.getArguments()[0]));
                java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> abortedCommands = com.google.common.collect.Lists.newArrayList();
                for (org.apache.ambari.server.actionmanager.Stage stage : stages) {
                    if (requestId.equals(stage.getRequestId())) {
                        for (org.apache.ambari.server.actionmanager.HostRoleCommand command : stage.getOrderedHostRoleCommands()) {
                            if (((command.getStatus() == org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED) || (command.getStatus() == org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS)) || (command.getStatus() == org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING)) {
                                command.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED);
                                org.apache.ambari.server.orm.entities.HostRoleCommandEntity hostRoleCommandEntity = command.constructNewPersistenceEntity();
                                hostRoleCommandEntity.setStage(stage.constructNewPersistenceEntity());
                                abortedCommands.add(hostRoleCommandEntity);
                            }
                        }
                    }
                }
                return abortedCommands;
            }
        }).when(db).abortOperation(Matchers.anyLong());
        java.util.Properties properties = new java.util.Properties();
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.actionmanager.ActionScheduler scheduler = new org.apache.ambari.server.actionmanager.ActionScheduler(100, 50, db, fsm, 3, new org.apache.ambari.server.controller.HostsMap(((java.lang.String) (null))), unitOfWork, null, conf, entityManagerProviderMock, hostRoleCommandDAOMock, ((org.apache.ambari.server.actionmanager.HostRoleCommandFactory) (null)), agentCommandsPublisher);
        org.apache.ambari.server.actionmanager.ActionManager am = new org.apache.ambari.server.actionmanager.ActionManager(db, requestFactory, scheduler);
        scheduler.doWork();
        java.util.List<org.apache.ambari.server.agent.CommandReport> reports = new java.util.ArrayList<>();
        reports.add(getCommandReport(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, org.apache.ambari.server.Role.DATANODE, org.apache.ambari.server.state.Service.Type.HDFS, "1-1", 1));
        am.processTaskResponse("host1", reports, org.apache.ambari.server.utils.CommandUtils.convertToTaskIdCommandMap(stage.getOrderedHostRoleCommands()));
        reports.clear();
        reports.add(getCommandReport(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, org.apache.ambari.server.Role.DATANODE, org.apache.ambari.server.state.Service.Type.HDFS, "1-1", 2));
        am.processTaskResponse("host2", reports, org.apache.ambari.server.utils.CommandUtils.convertToTaskIdCommandMap(stage.getOrderedHostRoleCommands()));
        reports.clear();
        reports.add(getCommandReport(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, org.apache.ambari.server.Role.DATANODE, org.apache.ambari.server.state.Service.Type.HDFS, "1-1", 3));
        am.processTaskResponse("host3", reports, org.apache.ambari.server.utils.CommandUtils.convertToTaskIdCommandMap(stage.getOrderedHostRoleCommands()));
        scheduler.doWork();
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, stages.get(1).getHostRoleStatus("host1", "HDFS_CLIENT"));
    }

    private org.apache.ambari.server.agent.CommandReport getCommandReport(org.apache.ambari.server.actionmanager.HostRoleStatus status, org.apache.ambari.server.Role role, org.apache.ambari.server.state.Service.Type service, java.lang.String actionId, int taskId) {
        org.apache.ambari.server.agent.CommandReport report = new org.apache.ambari.server.agent.CommandReport();
        report.setExitCode(999);
        report.setStdErr("");
        report.setStdOut("");
        report.setStatus(status.toString());
        report.setRole(role.toString());
        report.setServiceName(service.toString());
        report.setActionId(actionId);
        report.setTaskId(taskId);
        return report;
    }

    private org.apache.ambari.server.actionmanager.Stage createStage(java.lang.String clusterName, int stageId, int requestId) {
        org.apache.ambari.server.actionmanager.Stage stage = stageFactory.createNew(requestId, "/tmp", clusterName, 1L, "getStageWithSingleTask", "{\"host_param\":\"param_value\"}", "{\"stage_param\":\"param_value\"}");
        stage.setStageId(stageId);
        return stage;
    }

    private org.apache.ambari.server.actionmanager.Stage addTask(org.apache.ambari.server.actionmanager.Stage stage, java.lang.String hostname, java.lang.String clusterName, org.apache.ambari.server.Role role, org.apache.ambari.server.RoleCommand roleCommand, java.lang.String serviceName, int taskId) {
        stage.addHostRoleExecutionCommand(hostname, role, roleCommand, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostUpgradeEvent(role.toString(), hostname, java.lang.System.currentTimeMillis(), "HDP-0.2"), clusterName, serviceName, false, false);
        stage.getExecutionCommandWrapper(hostname, role.toString()).getExecutionCommand();
        stage.getOrderedHostRoleCommands().get(0).setTaskId(taskId);
        return stage;
    }

    private org.apache.ambari.server.actionmanager.Stage getStageWithSingleTask(java.lang.String hostname, java.lang.String clusterName, org.apache.ambari.server.Role role, org.apache.ambari.server.RoleCommand roleCommand, org.apache.ambari.server.state.Service.Type service, int taskId, int stageId, int requestId) {
        org.apache.ambari.server.actionmanager.Stage stage = createStage(clusterName, stageId, requestId);
        return addTask(stage, hostname, clusterName, role, roleCommand, service.name(), taskId);
    }

    private org.apache.ambari.server.actionmanager.Stage getStageWithSingleTask(java.lang.String hostname, java.lang.String clusterName, org.apache.ambari.server.Role role, org.apache.ambari.server.RoleCommand roleCommand, java.lang.String customCommandName, org.apache.ambari.server.state.Service.Type service, int taskId, int stageId, int requestId) {
        org.apache.ambari.server.actionmanager.Stage stage = getStageWithSingleTask(hostname, clusterName, role, roleCommand, service, taskId, stageId, requestId);
        org.apache.ambari.server.actionmanager.HostRoleCommand cmd = stage.getHostRoleCommand(hostname, role.name());
        if (cmd != null) {
            cmd.setCustomCommandName(customCommandName);
        }
        stage.getExecutionCommandWrapper(hostname, role.toString()).getExecutionCommand().setCommandType(org.apache.ambari.server.agent.AgentCommand.AgentCommandType.BACKGROUND_EXECUTION_COMMAND);
        return stage;
    }

    private void addInstallTaskToStage(org.apache.ambari.server.actionmanager.Stage stage, java.lang.String hostname, java.lang.String clusterName, org.apache.ambari.server.Role role, org.apache.ambari.server.RoleCommand roleCommand, org.apache.ambari.server.state.Service.Type service, int taskId) {
        stage.addHostRoleExecutionCommand(hostname, role, roleCommand, new org.apache.ambari.server.state.svccomphost.ServiceComponentHostInstallEvent(role.toString(), hostname, java.lang.System.currentTimeMillis(), "HDP-0.2"), clusterName, service.toString(), false, false);
        org.apache.ambari.server.agent.ExecutionCommand command = stage.getExecutionCommandWrapper(hostname, role.toString()).getExecutionCommand();
        command.setTaskId(taskId);
        for (org.apache.ambari.server.actionmanager.HostRoleCommand cmd : stage.getOrderedHostRoleCommands()) {
            if (cmd.getHostName().equals(hostname) && cmd.getRole().equals(role)) {
                cmd.setTaskId(taskId);
            }
        }
    }

    @org.junit.Test
    public void testSuccessFactors() {
        org.apache.ambari.server.actionmanager.Stage s = org.apache.ambari.server.utils.StageUtils.getATestStage(1, 1, org.apache.ambari.server.actionmanager.TestActionScheduler.CLUSTER_HOST_INFO, "{\"host_param\":\"param_value\"}", "{\"stage_param\":\"param_value\"}");
        org.junit.Assert.assertEquals(new java.lang.Float(0.5), new java.lang.Float(s.getSuccessFactor(org.apache.ambari.server.Role.DATANODE)));
        org.junit.Assert.assertEquals(new java.lang.Float(0.5), new java.lang.Float(s.getSuccessFactor(org.apache.ambari.server.Role.TASKTRACKER)));
        org.junit.Assert.assertEquals(new java.lang.Float(0.5), new java.lang.Float(s.getSuccessFactor(org.apache.ambari.server.Role.GANGLIA_MONITOR)));
        org.junit.Assert.assertEquals(new java.lang.Float(0.5), new java.lang.Float(s.getSuccessFactor(org.apache.ambari.server.Role.HBASE_REGIONSERVER)));
        org.junit.Assert.assertEquals(new java.lang.Float(1.0), new java.lang.Float(s.getSuccessFactor(org.apache.ambari.server.Role.NAMENODE)));
        org.junit.Assert.assertEquals(new java.lang.Float(1.0), new java.lang.Float(s.getSuccessFactor(org.apache.ambari.server.Role.GANGLIA_SERVER)));
    }

    @org.junit.Test
    public void testSuccessCriteria() {
        org.apache.ambari.server.actionmanager.ActionScheduler.RoleStats rs1 = new org.apache.ambari.server.actionmanager.ActionScheduler.RoleStats(1, ((float) (0.5)));
        rs1.numSucceeded = 1;
        org.junit.Assert.assertTrue(rs1.isSuccessFactorMet());
        rs1.numSucceeded = 0;
        org.junit.Assert.assertFalse(rs1.isSuccessFactorMet());
        org.apache.ambari.server.actionmanager.ActionScheduler.RoleStats rs2 = new org.apache.ambari.server.actionmanager.ActionScheduler.RoleStats(2, ((float) (0.5)));
        rs2.numSucceeded = 1;
        org.junit.Assert.assertTrue(rs2.isSuccessFactorMet());
        org.apache.ambari.server.actionmanager.ActionScheduler.RoleStats rs3 = new org.apache.ambari.server.actionmanager.ActionScheduler.RoleStats(3, ((float) (0.5)));
        rs3.numSucceeded = 2;
        org.junit.Assert.assertTrue(rs2.isSuccessFactorMet());
        rs3.numSucceeded = 1;
        org.junit.Assert.assertFalse(rs3.isSuccessFactorMet());
        org.apache.ambari.server.actionmanager.ActionScheduler.RoleStats rs4 = new org.apache.ambari.server.actionmanager.ActionScheduler.RoleStats(3, ((float) (1.0)));
        rs4.numSucceeded = 2;
        org.junit.Assert.assertFalse(rs3.isSuccessFactorMet());
    }

    @org.junit.Test
    public void testClusterHostInfoCache() throws java.lang.Exception {
        java.lang.reflect.Type type = new com.google.common.reflect.TypeToken<java.util.Map<java.lang.String, java.util.Set<java.lang.String>>>() {}.getType();
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> clusterHostInfo1 = org.apache.ambari.server.utils.StageUtils.getGson().fromJson(org.apache.ambari.server.actionmanager.TestActionScheduler.CLUSTER_HOST_INFO, type);
        int stageId = 1;
        int requestId1 = 1;
        int requestId2 = 2;
        java.util.Properties properties = new java.util.Properties();
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.state.Clusters fsm = Mockito.mock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster oneClusterMock = Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service serviceObj = Mockito.mock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.ServiceComponent scomp = Mockito.mock(org.apache.ambari.server.state.ServiceComponent.class);
        org.apache.ambari.server.state.ServiceComponentHost sch = Mockito.mock(org.apache.ambari.server.state.ServiceComponentHost.class);
        com.google.inject.persist.UnitOfWork unitOfWork = Mockito.mock(com.google.inject.persist.UnitOfWork.class);
        org.apache.ambari.server.events.publishers.AgentCommandsPublisher agentCommandsPublisher = Mockito.mock(org.apache.ambari.server.events.publishers.AgentCommandsPublisher.class);
        Mockito.when(fsm.getCluster(Matchers.anyString())).thenReturn(oneClusterMock);
        Mockito.when(oneClusterMock.getService(Matchers.anyString())).thenReturn(serviceObj);
        Mockito.when(serviceObj.getServiceComponent(Matchers.anyString())).thenReturn(scomp);
        Mockito.when(scomp.getServiceComponentHost(Matchers.anyString())).thenReturn(sch);
        Mockito.when(serviceObj.getCluster()).thenReturn(oneClusterMock);
        org.apache.ambari.server.state.Host host = Mockito.mock(org.apache.ambari.server.state.Host.class);
        java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> hosts = new java.util.HashMap<>();
        hosts.put(hostname, sch);
        Mockito.when(scomp.getServiceComponentHosts()).thenReturn(hosts);
        Mockito.when(fsm.getHost(Matchers.anyString())).thenReturn(host);
        Mockito.when(host.getState()).thenReturn(org.apache.ambari.server.state.HostState.HEALTHY);
        Mockito.when(host.getHostName()).thenReturn(hostname);
        Mockito.when(host.getHostId()).thenReturn(hostId);
        org.apache.ambari.server.actionmanager.ActionDBAccessor db = Mockito.mock(org.apache.ambari.server.actionmanager.ActionDBAccessorImpl.class);
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAOMock = Mockito.mock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        org.mockito.Mockito.doNothing().when(hostRoleCommandDAOMock).publishTaskCreateEvent(Matchers.anyListOf(org.apache.ambari.server.actionmanager.HostRoleCommand.class));
        org.apache.ambari.server.orm.entities.RequestEntity request = Mockito.mock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        Mockito.when(request.isExclusive()).thenReturn(false);
        Mockito.when(request.getClusterHostInfo()).thenReturn(org.apache.ambari.server.actionmanager.TestActionScheduler.CLUSTER_HOST_INFO);
        Mockito.when(db.getRequestEntity(Matchers.anyLong())).thenReturn(request);
        org.apache.ambari.server.actionmanager.Stage s1 = org.apache.ambari.server.utils.StageUtils.getATestStage(requestId1, stageId, hostname, "{\"host_param\":\"param_value\"}", "{\"stage_param\":\"param_value\"}");
        org.apache.ambari.server.actionmanager.Stage s2 = org.apache.ambari.server.utils.StageUtils.getATestStage(requestId2, stageId, hostname, "{\"host_param\":\"param_value\"}", "{\"stage_param\":\"param_value\"}");
        Mockito.when(db.getCommandsInProgressCount()).thenReturn(1);
        Mockito.when(db.getFirstStageInProgressPerRequest()).thenReturn(java.util.Collections.singletonList(s1));
        org.apache.ambari.server.actionmanager.ActionScheduler scheduler = new org.apache.ambari.server.actionmanager.ActionScheduler(100, 100, db, fsm, 10000, new org.apache.ambari.server.controller.HostsMap(((java.lang.String) (null))), unitOfWork, null, conf, entityManagerProviderMock, hostRoleCommandDAOMock, ((org.apache.ambari.server.actionmanager.HostRoleCommandFactory) (null)), agentCommandsPublisher);
        scheduler.setTaskTimeoutAdjustment(false);
        java.util.List<org.apache.ambari.server.agent.AgentCommand> commands = waitForQueueSize(hostId, agentCommandsPublisher, 1, scheduler);
        org.junit.Assert.assertTrue((commands != null) && (commands.size() == 1));
        org.apache.ambari.server.agent.AgentCommand scheduledCommand = commands.get(0);
        org.junit.Assert.assertTrue(scheduledCommand instanceof org.apache.ambari.server.agent.ExecutionCommand);
        org.junit.Assert.assertEquals((java.lang.String.valueOf(requestId1) + "-") + stageId, ((org.apache.ambari.server.agent.ExecutionCommand) (scheduledCommand)).getCommandId());
        org.junit.Assert.assertEquals(clusterHostInfo1, ((org.apache.ambari.server.agent.ExecutionCommand) (scheduledCommand)).getClusterHostInfo());
        Mockito.when(db.getCommandsInProgressCount()).thenReturn(1);
        Mockito.when(db.getFirstStageInProgressPerRequest()).thenReturn(java.util.Collections.singletonList(s2));
        commands = waitForQueueSize(hostId, agentCommandsPublisher, 1, scheduler);
        org.junit.Assert.assertTrue((commands != null) && (commands.size() == 1));
        scheduledCommand = commands.get(0);
        org.junit.Assert.assertTrue(scheduledCommand instanceof org.apache.ambari.server.agent.ExecutionCommand);
        org.junit.Assert.assertEquals((java.lang.String.valueOf(requestId2) + "-") + stageId, ((org.apache.ambari.server.agent.ExecutionCommand) (scheduledCommand)).getCommandId());
        org.junit.Assert.assertEquals(clusterHostInfo1, ((org.apache.ambari.server.agent.ExecutionCommand) (scheduledCommand)).getClusterHostInfo());
    }

    @org.junit.Test
    public void testCommandAbortForDeletedComponent() throws java.lang.Exception {
        java.util.Properties properties = new java.util.Properties();
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.state.Clusters fsm = Mockito.mock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster oneClusterMock = Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service serviceObj = Mockito.mock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.ServiceComponent scomp = Mockito.mock(org.apache.ambari.server.state.ServiceComponent.class);
        org.apache.ambari.server.state.ServiceComponent scWithDeletedSCH = Mockito.mock(org.apache.ambari.server.state.ServiceComponent.class);
        org.apache.ambari.server.state.ServiceComponentHost sch1 = Mockito.mock(org.apache.ambari.server.state.ServiceComponentHost.class);
        java.lang.String hostname1 = "host1";
        org.apache.ambari.server.state.Host host1 = Mockito.mock(org.apache.ambari.server.state.Host.class);
        Mockito.when(fsm.getHost(hostname1)).thenReturn(host1);
        Mockito.when(host1.getState()).thenReturn(org.apache.ambari.server.state.HostState.HEALTHY);
        Mockito.when(host1.getHostName()).thenReturn(hostname1);
        Mockito.when(scomp.getServiceComponentHost(hostname1)).thenReturn(sch1);
        java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> hosts = new java.util.HashMap<>();
        hosts.put(hostname1, sch1);
        Mockito.when(scomp.getServiceComponentHosts()).thenReturn(hosts);
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAO = Mockito.mock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = new org.apache.ambari.server.orm.entities.HostEntity();
        hostEntity.setHostName(hostname1);
        hostDAO.create(hostEntity);
        com.google.inject.persist.UnitOfWork unitOfWork = Mockito.mock(com.google.inject.persist.UnitOfWork.class);
        Mockito.when(fsm.getCluster(Matchers.anyString())).thenReturn(oneClusterMock);
        Mockito.when(oneClusterMock.getService(Matchers.anyString())).thenReturn(serviceObj);
        Mockito.when(serviceObj.getServiceComponent(org.apache.ambari.server.Role.HBASE_MASTER.toString())).thenReturn(scWithDeletedSCH);
        Mockito.when(serviceObj.getServiceComponent(org.apache.ambari.server.Role.HBASE_REGIONSERVER.toString())).thenReturn(scomp);
        Mockito.when(scWithDeletedSCH.getServiceComponentHost(Matchers.anyString())).thenThrow(new org.apache.ambari.server.ServiceComponentHostNotFoundException("dummyCluster", "dummyService", "dummyComponent", "dummyHostname"));
        Mockito.when(serviceObj.getCluster()).thenReturn(oneClusterMock);
        org.apache.ambari.server.actionmanager.Stage stage1 = stageFactory.createNew(1, "/tmp", "cluster1", 1L, "stageWith2Tasks", "", "");
        addInstallTaskToStage(stage1, hostname1, "cluster1", org.apache.ambari.server.Role.HBASE_MASTER, org.apache.ambari.server.RoleCommand.INSTALL, org.apache.ambari.server.state.Service.Type.HBASE, 1);
        addInstallTaskToStage(stage1, hostname1, "cluster1", org.apache.ambari.server.Role.HBASE_REGIONSERVER, org.apache.ambari.server.RoleCommand.INSTALL, org.apache.ambari.server.state.Service.Type.HBASE, 2);
        final java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = java.util.Collections.singletonList(stage1);
        org.apache.ambari.server.actionmanager.ActionDBAccessor db = Mockito.mock(org.apache.ambari.server.actionmanager.ActionDBAccessor.class);
        org.apache.ambari.server.orm.entities.RequestEntity request = Mockito.mock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        Mockito.when(request.isExclusive()).thenReturn(false);
        Mockito.when(db.getRequestEntity(Matchers.anyLong())).thenReturn(request);
        Mockito.when(db.getCommandsInProgressCount()).thenReturn(stages.size());
        Mockito.when(db.getFirstStageInProgressPerRequest()).thenReturn(stages);
        org.apache.ambari.server.actionmanager.ActionScheduler scheduler = new org.apache.ambari.server.actionmanager.ActionScheduler(100, 50000, db, fsm, 3, new org.apache.ambari.server.controller.HostsMap(((java.lang.String) (null))), unitOfWork, null, conf, entityManagerProviderMock, hostRoleCommandDAO, ((org.apache.ambari.server.actionmanager.HostRoleCommandFactory) (null)), null);
        final java.util.concurrent.CountDownLatch abortCalls = new java.util.concurrent.CountDownLatch(2);
        Mockito.doAnswer(new org.mockito.stubbing.Answer<java.lang.Object>() {
            @java.lang.Override
            public java.lang.Object answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                java.lang.Long requestId = ((java.lang.Long) (invocation.getArguments()[0]));
                for (org.apache.ambari.server.actionmanager.Stage stage : stages) {
                    if (requestId.equals(stage.getRequestId())) {
                        for (org.apache.ambari.server.actionmanager.HostRoleCommand command : stage.getOrderedHostRoleCommands()) {
                            if (((command.getStatus() == org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED) || (command.getStatus() == org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS)) || (command.getStatus() == org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING)) {
                                command.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED);
                            }
                        }
                    }
                }
                abortCalls.countDown();
                return null;
            }
        }).when(db).abortOperation(Matchers.anyLong());
        scheduler.setTaskTimeoutAdjustment(false);
        scheduler.start();
        long timeout = 60;
        abortCalls.await(timeout, java.util.concurrent.TimeUnit.SECONDS);
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, stages.get(0).getHostRoleStatus(hostname1, "HBASE_MASTER"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, stages.get(0).getHostRoleStatus(hostname1, "HBASE_REGIONSERVER"));
        Mockito.verify(db, Mockito.times(2)).abortOperation(Matchers.anyLong());
        scheduler.stop();
    }

    @org.junit.Test
    public void testServerActionWOService() throws java.lang.Exception {
        java.util.Properties properties = new java.util.Properties();
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.state.Clusters fsm = Mockito.mock(org.apache.ambari.server.state.Clusters.class);
        com.google.inject.persist.UnitOfWork unitOfWork = Mockito.mock(com.google.inject.persist.UnitOfWork.class);
        org.apache.ambari.server.events.publishers.AgentCommandsPublisher agentCommandsPublisher = Mockito.mock(org.apache.ambari.server.events.publishers.AgentCommandsPublisher.class);
        java.util.Map<java.lang.String, java.lang.String> payload = new java.util.HashMap<>();
        final org.apache.ambari.server.actionmanager.Stage s = getStageWithServerAction(1, 977, payload, "test", 300, false, false);
        java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = java.util.Collections.singletonList(s);
        org.apache.ambari.server.actionmanager.ActionDBAccessor db = Mockito.mock(org.apache.ambari.server.actionmanager.ActionDBAccessor.class);
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAOMock = Mockito.mock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        org.mockito.Mockito.doNothing().when(hostRoleCommandDAOMock).publishTaskCreateEvent(Matchers.anyListOf(org.apache.ambari.server.actionmanager.HostRoleCommand.class));
        org.apache.ambari.server.orm.entities.RequestEntity request = Mockito.mock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        Mockito.when(request.getClusterHostInfo()).thenReturn(org.apache.ambari.server.actionmanager.TestActionScheduler.CLUSTER_HOST_INFO);
        Mockito.when(request.isExclusive()).thenReturn(false);
        Mockito.when(db.getRequestEntity(Matchers.anyLong())).thenReturn(request);
        Mockito.when(db.getCommandsInProgressCount()).thenReturn(stages.size());
        Mockito.when(db.getFirstStageInProgressPerRequest()).thenReturn(stages);
        Mockito.doAnswer(new org.mockito.stubbing.Answer<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                java.lang.String host = ((java.lang.String) (invocation.getArguments()[0]));
                java.lang.String role = ((java.lang.String) (invocation.getArguments()[3]));
                org.apache.ambari.server.agent.CommandReport commandReport = ((org.apache.ambari.server.agent.CommandReport) (invocation.getArguments()[4]));
                org.apache.ambari.server.actionmanager.HostRoleCommand command = null;
                if (null == host) {
                    command = s.getHostRoleCommand(null, role);
                } else {
                    command = s.getHostRoleCommand(host, role);
                }
                command.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.valueOf(commandReport.getStatus()));
                return null;
            }
        }).when(db).updateHostRoleState(Matchers.anyString(), Matchers.anyLong(), Matchers.anyLong(), Matchers.anyString(), Matchers.any(org.apache.ambari.server.agent.CommandReport.class));
        Mockito.doAnswer(new org.mockito.stubbing.Answer<java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand>>() {
            @java.lang.Override
            public java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                java.lang.String role = ((java.lang.String) (invocation.getArguments()[0]));
                org.apache.ambari.server.actionmanager.HostRoleStatus status = ((org.apache.ambari.server.actionmanager.HostRoleStatus) (invocation.getArguments()[1]));
                org.apache.ambari.server.actionmanager.HostRoleCommand task = s.getHostRoleCommand(null, role);
                if (task.getStatus() == status) {
                    return java.util.Arrays.asList(task);
                } else {
                    return java.util.Collections.emptyList();
                }
            }
        }).when(db).getTasksByRoleAndStatus(Matchers.anyString(), Matchers.any(org.apache.ambari.server.actionmanager.HostRoleStatus.class));
        Mockito.doAnswer(new org.mockito.stubbing.Answer<org.apache.ambari.server.actionmanager.HostRoleCommand>() {
            @java.lang.Override
            public org.apache.ambari.server.actionmanager.HostRoleCommand answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                return s.getHostRoleCommand(null, "AMBARI_SERVER_ACTION");
            }
        }).when(db).getTask(Matchers.anyLong());
        org.apache.ambari.server.serveraction.ServerActionExecutor.init(injector);
        org.apache.ambari.server.actionmanager.ActionScheduler scheduler = new org.apache.ambari.server.actionmanager.ActionScheduler(100, 50, db, fsm, 3, new org.apache.ambari.server.controller.HostsMap(((java.lang.String) (null))), unitOfWork, null, conf, entityManagerProviderMock, hostRoleCommandDAOMock, ((org.apache.ambari.server.actionmanager.HostRoleCommandFactory) (null)), agentCommandsPublisher);
        int cycleCount = 0;
        while ((!stages.get(0).getHostRoleStatus(null, "AMBARI_SERVER_ACTION").equals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED)) && ((cycleCount++) <= MAX_CYCLE_ITERATIONS)) {
            scheduler.doWork();
            scheduler.getServerActionExecutor().doWork();
        } 
        org.junit.Assert.assertEquals(stages.get(0).getHostRoleStatus(null, "AMBARI_SERVER_ACTION"), org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
    }

    @org.junit.Test
    public void testCancelRequests() throws java.lang.Exception {
        org.apache.ambari.server.state.Clusters fsm = Mockito.mock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster oneClusterMock = Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service serviceObj = Mockito.mock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.ServiceComponent scomp = Mockito.mock(org.apache.ambari.server.state.ServiceComponent.class);
        org.apache.ambari.server.state.ServiceComponentHost sch = Mockito.mock(org.apache.ambari.server.state.ServiceComponentHost.class);
        com.google.inject.persist.UnitOfWork unitOfWork = Mockito.mock(com.google.inject.persist.UnitOfWork.class);
        org.apache.ambari.server.events.publishers.AgentCommandsPublisher agentCommandsPublisher = Mockito.mock(org.apache.ambari.server.events.publishers.AgentCommandsPublisher.class);
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAO = Mockito.mock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        org.apache.ambari.server.actionmanager.HostRoleCommandFactory hostRoleCommandFactory = Mockito.mock(org.apache.ambari.server.actionmanager.HostRoleCommandFactory.class);
        Mockito.when(fsm.getCluster(Matchers.anyString())).thenReturn(oneClusterMock);
        Mockito.when(oneClusterMock.getService(Matchers.anyString())).thenReturn(serviceObj);
        Mockito.when(serviceObj.getServiceComponent(Matchers.anyString())).thenReturn(scomp);
        Mockito.when(scomp.getServiceComponentHost(Matchers.anyString())).thenReturn(sch);
        Mockito.when(serviceObj.getCluster()).thenReturn(oneClusterMock);
        final java.lang.Long hostId = 1L;
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = new org.apache.ambari.server.orm.entities.HostEntity();
        hostEntity.setHostName(hostname);
        hostEntity.setHostId(hostId);
        hostDAO.create(hostEntity);
        java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> hosts = new java.util.HashMap<>();
        hosts.put(hostname, sch);
        Mockito.when(scomp.getServiceComponentHosts()).thenReturn(hosts);
        long requestId = 1;
        final java.util.List<org.apache.ambari.server.actionmanager.Stage> allStages = new java.util.ArrayList<>();
        final java.util.List<org.apache.ambari.server.actionmanager.Stage> stagesInProgress = new java.util.ArrayList<>();
        final java.util.List<org.apache.ambari.server.actionmanager.Stage> firstStageInProgress = new java.util.ArrayList<>();
        final java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> tasksInProgress = new java.util.ArrayList<>();
        final java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> hrcEntitiesInProgress = new java.util.ArrayList<>();
        int secondaryNamenodeCmdTaskId = 1;
        int namenodeCmdTaskId = 2;
        int datanodeCmdTaskId = 3;
        org.apache.ambari.server.actionmanager.Stage stageWithTask = getStageWithSingleTask(hostname, "cluster1", org.apache.ambari.server.Role.SECONDARY_NAMENODE, org.apache.ambari.server.RoleCommand.START, org.apache.ambari.server.state.Service.Type.HDFS, secondaryNamenodeCmdTaskId, 1, ((int) (requestId)));
        stageWithTask.getOrderedHostRoleCommands().get(0).setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
        allStages.add(stageWithTask);
        stageWithTask = getStageWithSingleTask(hostname, "cluster1", org.apache.ambari.server.Role.NAMENODE, org.apache.ambari.server.RoleCommand.START, org.apache.ambari.server.state.Service.Type.HDFS, namenodeCmdTaskId, 2, ((int) (requestId)));
        tasksInProgress.addAll(stageWithTask.getOrderedHostRoleCommands());
        firstStageInProgress.add(stageWithTask);
        stagesInProgress.add(stageWithTask);
        allStages.add(stageWithTask);
        stageWithTask = getStageWithSingleTask(hostname, "cluster1", org.apache.ambari.server.Role.DATANODE, org.apache.ambari.server.RoleCommand.START, org.apache.ambari.server.state.Service.Type.HDFS, datanodeCmdTaskId, 3, ((int) (requestId)));
        tasksInProgress.addAll(stageWithTask.getOrderedHostRoleCommands());
        stagesInProgress.add(stageWithTask);
        allStages.add(stageWithTask);
        for (org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand : tasksInProgress) {
            org.apache.ambari.server.orm.entities.HostRoleCommandEntity entity = Mockito.mock(org.apache.ambari.server.orm.entities.HostRoleCommandEntity.class);
            Mockito.when(entity.getTaskId()).thenReturn(hostRoleCommand.getTaskId());
            Mockito.when(entity.getStageId()).thenReturn(hostRoleCommand.getStageId());
            Mockito.when(entity.getRequestId()).thenReturn(hostRoleCommand.getRequestId());
            Mockito.when(entity.getHostId()).thenReturn(hostRoleCommand.getHostId());
            Mockito.when(entity.getHostName()).thenReturn(hostRoleCommand.getHostName());
            Mockito.when(entity.getRole()).thenReturn(hostRoleCommand.getRole());
            Mockito.when(entity.getStatus()).thenReturn(hostRoleCommand.getStatus());
            Mockito.when(entity.getRoleCommand()).thenReturn(hostRoleCommand.getRoleCommand());
            hrcEntitiesInProgress.add(entity);
            Mockito.when(hostRoleCommandFactory.createExisting(entity)).thenReturn(hostRoleCommand);
        }
        org.apache.ambari.server.state.Host host = Mockito.mock(org.apache.ambari.server.state.Host.class);
        Mockito.when(fsm.getHost(Matchers.anyString())).thenReturn(host);
        Mockito.when(host.getState()).thenReturn(org.apache.ambari.server.state.HostState.HEALTHY);
        Mockito.when(host.getHostName()).thenReturn(hostname);
        Mockito.when(host.getHostId()).thenReturn(hostId);
        org.apache.ambari.server.actionmanager.ActionDBAccessor db = Mockito.mock(org.apache.ambari.server.actionmanager.ActionDBAccessor.class);
        org.apache.ambari.server.orm.entities.RequestEntity request = Mockito.mock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        Mockito.when(request.isExclusive()).thenReturn(false);
        Mockito.when(request.getClusterHostInfo()).thenReturn(org.apache.ambari.server.actionmanager.TestActionScheduler.CLUSTER_HOST_INFO);
        Mockito.when(db.getRequestEntity(Matchers.anyLong())).thenReturn(request);
        Mockito.when(db.getCommandsInProgressCount()).thenReturn(stagesInProgress.size());
        Mockito.when(db.getFirstStageInProgressPerRequest()).thenReturn(stagesInProgress);
        Mockito.when(db.getStagesInProgressForRequest(requestId)).thenReturn(stagesInProgress);
        Mockito.when(db.getAllStages(Matchers.anyLong())).thenReturn(allStages);
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> requestTasks = new java.util.ArrayList<>();
        for (org.apache.ambari.server.actionmanager.Stage stage : allStages) {
            requestTasks.addAll(stage.getOrderedHostRoleCommands());
        }
        Mockito.when(db.getRequestTasks(Matchers.anyLong())).thenReturn(requestTasks);
        Mockito.doAnswer(new org.mockito.stubbing.Answer<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                java.util.List<org.apache.ambari.server.agent.CommandReport> reports = ((java.util.List<org.apache.ambari.server.agent.CommandReport>) (invocation.getArguments()[0]));
                for (org.apache.ambari.server.agent.CommandReport report : reports) {
                    java.lang.String actionId = report.getActionId();
                    long[] requestStageIds = org.apache.ambari.server.utils.StageUtils.getRequestStage(actionId);
                    java.lang.Long requestId = requestStageIds[0];
                    java.lang.Long stageId = requestStageIds[1];
                    java.lang.Long id = report.getTaskId();
                    for (org.apache.ambari.server.actionmanager.Stage stage : stagesInProgress) {
                        if (requestId.equals(stage.getRequestId()) && stageId.equals(stage.getStageId())) {
                            for (org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand : stage.getOrderedHostRoleCommands()) {
                                if (hostRoleCommand.getTaskId() == id) {
                                    hostRoleCommand.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.valueOf(report.getStatus()));
                                }
                            }
                        }
                    }
                }
                return null;
            }
        }).when(db).updateHostRoleStates(Matchers.anyCollectionOf(org.apache.ambari.server.agent.CommandReport.class));
        Mockito.when(db.getTask(Matchers.anyLong())).thenAnswer(new org.mockito.stubbing.Answer<java.lang.Object>() {
            @java.lang.Override
            public java.lang.Object answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                java.lang.Long taskId = ((java.lang.Long) (invocation.getArguments()[0]));
                for (org.apache.ambari.server.actionmanager.Stage stage : allStages) {
                    for (org.apache.ambari.server.actionmanager.HostRoleCommand command : stage.getOrderedHostRoleCommands()) {
                        if (taskId.equals(command.getTaskId())) {
                            return command;
                        }
                    }
                }
                return null;
            }
        });
        Mockito.doAnswer(new org.mockito.stubbing.Answer<java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity>>() {
            @java.lang.Override
            public java.util.Collection<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                java.lang.Long requestId = ((java.lang.Long) (invocation.getArguments()[0]));
                java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> abortedCommands = com.google.common.collect.Lists.newArrayList();
                for (org.apache.ambari.server.actionmanager.Stage stage : stagesInProgress) {
                    if (requestId.equals(stage.getRequestId())) {
                        for (org.apache.ambari.server.actionmanager.HostRoleCommand command : stage.getOrderedHostRoleCommands()) {
                            if (((command.getStatus() == org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED) || (command.getStatus() == org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS)) || (command.getStatus() == org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING)) {
                                command.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED);
                                org.apache.ambari.server.orm.entities.HostRoleCommandEntity hostRoleCommandEntity = command.constructNewPersistenceEntity();
                                hostRoleCommandEntity.setStage(stage.constructNewPersistenceEntity());
                                abortedCommands.add(hostRoleCommandEntity);
                            }
                        }
                    }
                }
                return abortedCommands;
            }
        }).when(db).abortOperation(Matchers.anyLong());
        java.util.Map<java.lang.Long, java.util.List<org.apache.ambari.server.agent.AgentCommand>> commands = new java.util.HashMap<>();
        Mockito.doAnswer(new org.mockito.stubbing.Answer<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                java.lang.Long hostId = ((java.lang.Long) (invocation.getArguments()[0]));
                if (!commands.containsKey(hostId)) {
                    commands.put(hostId, new java.util.ArrayList<>());
                }
                commands.get(hostId).add(((org.apache.ambari.server.agent.AgentCommand) (invocation.getArguments()[1])));
                return null;
            }
        }).when(agentCommandsPublisher).sendAgentCommand(Matchers.anyLong(), Matchers.any(org.apache.ambari.server.agent.AgentCommand.class));
        java.util.Properties properties = new java.util.Properties();
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(properties);
        Mockito.when(hostRoleCommandDAO.findByRequestIdAndStatuses(requestId, org.apache.ambari.server.actionmanager.HostRoleStatus.NOT_COMPLETED_STATUSES)).thenReturn(hrcEntitiesInProgress);
        org.apache.ambari.server.actionmanager.ActionScheduler scheduler = new org.apache.ambari.server.actionmanager.ActionScheduler(100, 50, db, fsm, 3, new org.apache.ambari.server.controller.HostsMap(((java.lang.String) (null))), unitOfWork, null, conf, entityManagerProviderMock, hostRoleCommandDAO, hostRoleCommandFactory, agentCommandsPublisher);
        scheduler.doWork();
        java.lang.String reason = "Some reason";
        scheduler.scheduleCancellingRequest(requestId, reason);
        scheduler.doWork();
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED, allStages.get(0).getHostRoleStatus(hostname, "SECONDARY_NAMENODE"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, allStages.get(1).getHostRoleStatus(hostname, "NAMENODE"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED, allStages.get(2).getHostRoleStatus(hostname, "DATANODE"));
        org.junit.Assert.assertEquals(1, commands.get(hostId).size());
        org.apache.ambari.server.agent.CancelCommand cancelCommand = ((org.apache.ambari.server.agent.CancelCommand) (commands.get(hostId).get(0)));
        org.junit.Assert.assertEquals(cancelCommand.getTargetTaskId(), namenodeCmdTaskId);
        org.junit.Assert.assertEquals(cancelCommand.getReason(), reason);
    }

    @org.junit.Test
    public void testExclusiveRequests() throws java.lang.Exception {
        org.apache.ambari.server.state.Clusters fsm = Mockito.mock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster oneClusterMock = Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Service serviceObj = Mockito.mock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.ServiceComponent scomp = Mockito.mock(org.apache.ambari.server.state.ServiceComponent.class);
        org.apache.ambari.server.state.ServiceComponentHost sch = Mockito.mock(org.apache.ambari.server.state.ServiceComponentHost.class);
        com.google.inject.persist.UnitOfWork unitOfWork = Mockito.mock(com.google.inject.persist.UnitOfWork.class);
        org.apache.ambari.server.events.publishers.AgentCommandsPublisher agentCommandsPublisher = Mockito.mock(org.apache.ambari.server.events.publishers.AgentCommandsPublisher.class);
        Mockito.when(fsm.getCluster(Matchers.anyString())).thenReturn(oneClusterMock);
        Mockito.when(oneClusterMock.getService(Matchers.anyString())).thenReturn(serviceObj);
        Mockito.when(serviceObj.getServiceComponent(Matchers.anyString())).thenReturn(scomp);
        Mockito.when(scomp.getServiceComponentHost(Matchers.anyString())).thenReturn(sch);
        Mockito.when(serviceObj.getCluster()).thenReturn(oneClusterMock);
        java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> hosts = new java.util.HashMap<>();
        java.lang.String hostname1 = "hostname1";
        java.lang.String hostname2 = "hostname2";
        java.lang.String hostname3 = "hostname3";
        hosts.put(hostname1, sch);
        hosts.put(hostname2, sch);
        hosts.put(hostname3, sch);
        Mockito.when(scomp.getServiceComponentHosts()).thenReturn(hosts);
        long requestId1 = 1;
        long requestId2 = 2;
        long requestId3 = 3;
        final java.util.List<org.apache.ambari.server.actionmanager.Stage> firstStageInProgressByRequest = new java.util.ArrayList<>();
        final java.util.List<org.apache.ambari.server.actionmanager.Stage> stagesInProgress = new java.util.ArrayList<>();
        int namenodeCmdTaskId = 1;
        org.apache.ambari.server.actionmanager.Stage request1Stage1 = getStageWithSingleTask(hostname1, "cluster1", org.apache.ambari.server.Role.NAMENODE, org.apache.ambari.server.RoleCommand.START, org.apache.ambari.server.state.Service.Type.HDFS, namenodeCmdTaskId, 1, ((int) (requestId1)));
        org.apache.ambari.server.actionmanager.Stage request1Stage2 = getStageWithSingleTask(hostname1, "cluster1", org.apache.ambari.server.Role.DATANODE, org.apache.ambari.server.RoleCommand.START, org.apache.ambari.server.state.Service.Type.HDFS, 2, 2, ((int) (requestId1)));
        org.apache.ambari.server.actionmanager.Stage request2Stage1 = getStageWithSingleTask(hostname2, "cluster1", org.apache.ambari.server.Role.DATANODE, org.apache.ambari.server.RoleCommand.STOP, org.apache.ambari.server.state.Service.Type.HDFS, 3, 3, ((int) (requestId2)));
        org.apache.ambari.server.actionmanager.Stage request3Stage1 = getStageWithSingleTask(hostname3, "cluster1", org.apache.ambari.server.Role.DATANODE, org.apache.ambari.server.RoleCommand.START, org.apache.ambari.server.state.Service.Type.HDFS, 4, 4, ((int) (requestId3)));
        firstStageInProgressByRequest.add(request1Stage1);
        firstStageInProgressByRequest.add(request2Stage1);
        firstStageInProgressByRequest.add(request3Stage1);
        stagesInProgress.add(request1Stage1);
        stagesInProgress.add(request1Stage2);
        stagesInProgress.add(request2Stage1);
        stagesInProgress.add(request3Stage1);
        org.apache.ambari.server.state.Host host1 = Mockito.mock(org.apache.ambari.server.state.Host.class);
        Mockito.when(fsm.getHost(Matchers.anyString())).thenReturn(host1);
        Mockito.when(host1.getState()).thenReturn(org.apache.ambari.server.state.HostState.HEALTHY);
        Mockito.when(host1.getHostName()).thenReturn(hostname);
        org.apache.ambari.server.state.Host host2 = Mockito.mock(org.apache.ambari.server.state.Host.class);
        Mockito.when(fsm.getHost(Matchers.anyString())).thenReturn(host2);
        Mockito.when(host2.getState()).thenReturn(org.apache.ambari.server.state.HostState.HEALTHY);
        Mockito.when(host2.getHostName()).thenReturn(hostname);
        org.apache.ambari.server.state.Host host3 = Mockito.mock(org.apache.ambari.server.state.Host.class);
        Mockito.when(fsm.getHost(Matchers.anyString())).thenReturn(host3);
        Mockito.when(host3.getState()).thenReturn(org.apache.ambari.server.state.HostState.HEALTHY);
        Mockito.when(host3.getHostName()).thenReturn(hostname);
        org.apache.ambari.server.actionmanager.ActionDBAccessor db = Mockito.mock(org.apache.ambari.server.actionmanager.ActionDBAccessor.class);
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAOMock = Mockito.mock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        org.mockito.Mockito.doNothing().when(hostRoleCommandDAOMock).publishTaskCreateEvent(Matchers.anyListOf(org.apache.ambari.server.actionmanager.HostRoleCommand.class));
        Mockito.when(db.getCommandsInProgressCount()).thenReturn(stagesInProgress.size());
        Mockito.when(db.getFirstStageInProgressPerRequest()).thenReturn(firstStageInProgressByRequest);
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> requestTasks = new java.util.ArrayList<>();
        for (org.apache.ambari.server.actionmanager.Stage stage : stagesInProgress) {
            requestTasks.addAll(stage.getOrderedHostRoleCommands());
        }
        Mockito.when(db.getRequestTasks(Matchers.anyLong())).thenReturn(requestTasks);
        Mockito.when(db.getAllStages(Matchers.anyLong())).thenReturn(stagesInProgress);
        Mockito.doAnswer(new org.mockito.stubbing.Answer<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                java.util.List<org.apache.ambari.server.agent.CommandReport> reports = ((java.util.List<org.apache.ambari.server.agent.CommandReport>) (invocation.getArguments()[0]));
                for (org.apache.ambari.server.agent.CommandReport report : reports) {
                    java.lang.String actionId = report.getActionId();
                    long[] requestStageIds = org.apache.ambari.server.utils.StageUtils.getRequestStage(actionId);
                    java.lang.Long requestId = requestStageIds[0];
                    java.lang.Long stageId = requestStageIds[1];
                    java.lang.Long id = report.getTaskId();
                    for (org.apache.ambari.server.actionmanager.Stage stage : stagesInProgress) {
                        if (requestId.equals(stage.getRequestId()) && stageId.equals(stage.getStageId())) {
                            for (org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand : stage.getOrderedHostRoleCommands()) {
                                if (hostRoleCommand.getTaskId() == id) {
                                    hostRoleCommand.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.valueOf(report.getStatus()));
                                }
                            }
                        }
                    }
                }
                return null;
            }
        }).when(db).updateHostRoleStates(Matchers.anyCollectionOf(org.apache.ambari.server.agent.CommandReport.class));
        Mockito.when(db.getTask(Matchers.anyLong())).thenAnswer(new org.mockito.stubbing.Answer<java.lang.Object>() {
            @java.lang.Override
            public java.lang.Object answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                java.lang.Long taskId = ((java.lang.Long) (invocation.getArguments()[0]));
                for (org.apache.ambari.server.actionmanager.Stage stage : stagesInProgress) {
                    for (org.apache.ambari.server.actionmanager.HostRoleCommand command : stage.getOrderedHostRoleCommands()) {
                        if (taskId.equals(command.getTaskId())) {
                            return command;
                        }
                    }
                }
                return null;
            }
        });
        final java.util.Map<java.lang.Long, java.lang.Boolean> startedRequests = new java.util.HashMap<>();
        Mockito.doAnswer(new org.mockito.stubbing.Answer<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                startedRequests.put(((java.lang.Long) (invocation.getArguments()[0])), true);
                return null;
            }
        }).when(db).startRequest(Matchers.anyLong());
        org.apache.ambari.server.orm.entities.RequestEntity request1 = Mockito.mock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        Mockito.when(request1.isExclusive()).thenReturn(false);
        Mockito.when(request1.getClusterHostInfo()).thenReturn(org.apache.ambari.server.actionmanager.TestActionScheduler.CLUSTER_HOST_INFO);
        org.apache.ambari.server.orm.entities.RequestEntity request2 = Mockito.mock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        Mockito.when(request2.isExclusive()).thenReturn(true);
        Mockito.when(request2.getClusterHostInfo()).thenReturn(org.apache.ambari.server.actionmanager.TestActionScheduler.CLUSTER_HOST_INFO);
        org.apache.ambari.server.orm.entities.RequestEntity request3 = Mockito.mock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        Mockito.when(request3.isExclusive()).thenReturn(false);
        Mockito.when(request3.getClusterHostInfo()).thenReturn(org.apache.ambari.server.actionmanager.TestActionScheduler.CLUSTER_HOST_INFO);
        Mockito.when(db.getRequestEntity(requestId1)).thenReturn(request1);
        Mockito.when(db.getRequestEntity(requestId2)).thenReturn(request2);
        Mockito.when(db.getRequestEntity(requestId3)).thenReturn(request3);
        java.util.Properties properties = new java.util.Properties();
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.actionmanager.ActionScheduler scheduler = Mockito.spy(new org.apache.ambari.server.actionmanager.ActionScheduler(100, 50, db, fsm, 3, new org.apache.ambari.server.controller.HostsMap(((java.lang.String) (null))), unitOfWork, null, conf, entityManagerProviderMock, hostRoleCommandDAOMock, ((org.apache.ambari.server.actionmanager.HostRoleCommandFactory) (null)), agentCommandsPublisher));
        Mockito.doReturn(false).when(scheduler).wasAgentRestartedDuringOperation(Matchers.any(org.apache.ambari.server.state.Host.class), Matchers.any(org.apache.ambari.server.actionmanager.Stage.class), Matchers.anyString());
        scheduler.doWork();
        org.junit.Assert.assertTrue(startedRequests.containsKey(requestId1));
        org.junit.Assert.assertFalse(startedRequests.containsKey(requestId2));
        org.junit.Assert.assertFalse(startedRequests.containsKey(requestId3));
        stagesInProgress.remove(0);
        firstStageInProgressByRequest.clear();
        firstStageInProgressByRequest.add(request1Stage2);
        firstStageInProgressByRequest.add(request2Stage1);
        firstStageInProgressByRequest.add(request3Stage1);
        scheduler.doWork();
        org.junit.Assert.assertTrue(startedRequests.containsKey(requestId1));
        org.junit.Assert.assertFalse(startedRequests.containsKey(requestId2));
        org.junit.Assert.assertFalse(startedRequests.containsKey(requestId3));
        stagesInProgress.remove(0);
        firstStageInProgressByRequest.clear();
        firstStageInProgressByRequest.add(request2Stage1);
        firstStageInProgressByRequest.add(request3Stage1);
        scheduler.doWork();
        org.junit.Assert.assertTrue(startedRequests.containsKey(requestId1));
        org.junit.Assert.assertTrue(startedRequests.containsKey(requestId2));
        org.junit.Assert.assertFalse(startedRequests.containsKey(requestId3));
        stagesInProgress.remove(0);
        firstStageInProgressByRequest.clear();
        firstStageInProgressByRequest.add(request3Stage1);
        scheduler.doWork();
        org.junit.Assert.assertTrue(startedRequests.containsKey(requestId1));
        org.junit.Assert.assertTrue(startedRequests.containsKey(requestId2));
        org.junit.Assert.assertTrue(startedRequests.containsKey(requestId3));
    }

    @org.junit.Test
    public void testAbortHolding() throws org.apache.ambari.server.AmbariException {
        com.google.inject.persist.UnitOfWork unitOfWork = org.easymock.EasyMock.createMock(com.google.inject.persist.UnitOfWork.class);
        org.apache.ambari.server.actionmanager.ActionDBAccessor db = org.easymock.EasyMock.createMock(org.apache.ambari.server.actionmanager.ActionDBAccessor.class);
        org.apache.ambari.server.state.Clusters fsm = org.easymock.EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(new java.util.Properties());
        org.apache.ambari.server.orm.entities.HostEntity hostEntity1 = new org.apache.ambari.server.orm.entities.HostEntity();
        hostEntity1.setHostName("h1");
        hostDAO.merge(hostEntity1);
        db.abortHostRole("h1", -1L, -1L, "AMBARI_SERVER_ACTION");
        org.easymock.EasyMock.expectLastCall();
        org.easymock.EasyMock.replay(db);
        org.apache.ambari.server.actionmanager.ActionScheduler scheduler = new org.apache.ambari.server.actionmanager.ActionScheduler(100, 50, db, fsm, 3, new org.apache.ambari.server.controller.HostsMap(((java.lang.String) (null))), unitOfWork, null, conf, entityManagerProviderMock, ((org.apache.ambari.server.orm.dao.HostRoleCommandDAO) (null)), ((org.apache.ambari.server.actionmanager.HostRoleCommandFactory) (null)), null);
        org.apache.ambari.server.actionmanager.HostRoleCommand hrc1 = hostRoleCommandFactory.create("h1", org.apache.ambari.server.Role.NAMENODE, null, org.apache.ambari.server.RoleCommand.EXECUTE);
        hrc1.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
        org.apache.ambari.server.actionmanager.HostRoleCommand hrc3 = hostRoleCommandFactory.create("h1", org.apache.ambari.server.Role.AMBARI_SERVER_ACTION, null, org.apache.ambari.server.RoleCommand.CUSTOM_COMMAND);
        hrc3.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING);
        org.apache.ambari.server.actionmanager.HostRoleCommand hrc4 = hostRoleCommandFactory.create("h1", org.apache.ambari.server.Role.FLUME_HANDLER, null, org.apache.ambari.server.RoleCommand.EXECUTE);
        hrc4.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING);
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommands = java.util.Arrays.asList(hrc1, hrc3, hrc4);
        scheduler.cancelHostRoleCommands(hostRoleCommands, "foo");
        org.easymock.EasyMock.verify(db);
    }

    @org.junit.Test
    public void testAbortAmbariServerAction() throws org.apache.ambari.server.AmbariException {
        com.google.inject.persist.UnitOfWork unitOfWork = org.easymock.EasyMock.createMock(com.google.inject.persist.UnitOfWork.class);
        org.apache.ambari.server.actionmanager.ActionDBAccessor db = org.easymock.EasyMock.createMock(org.apache.ambari.server.actionmanager.ActionDBAccessor.class);
        org.apache.ambari.server.state.Clusters fsm = org.easymock.EasyMock.createMock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(new java.util.Properties());
        org.apache.ambari.server.orm.entities.HostEntity hostEntity1 = new org.apache.ambari.server.orm.entities.HostEntity();
        hostEntity1.setHostName("h1");
        hostDAO.merge(hostEntity1);
        org.easymock.EasyMock.replay(db);
        org.apache.ambari.server.actionmanager.ActionScheduler scheduler = new org.apache.ambari.server.actionmanager.ActionScheduler(100, 50, db, fsm, 3, new org.apache.ambari.server.controller.HostsMap(((java.lang.String) (null))), unitOfWork, null, conf, entityManagerProviderMock, ((org.apache.ambari.server.orm.dao.HostRoleCommandDAO) (null)), ((org.apache.ambari.server.actionmanager.HostRoleCommandFactory) (null)), null);
        org.apache.ambari.server.actionmanager.HostRoleCommand hrc1 = hostRoleCommandFactory.create("h1", org.apache.ambari.server.Role.NAMENODE, null, org.apache.ambari.server.RoleCommand.EXECUTE);
        hrc1.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED);
        org.apache.ambari.server.actionmanager.HostRoleCommand hrc3 = hostRoleCommandFactory.create(null, org.apache.ambari.server.Role.AMBARI_SERVER_ACTION, null, org.apache.ambari.server.RoleCommand.CUSTOM_COMMAND);
        hrc3.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS);
        org.apache.ambari.server.actionmanager.HostRoleCommand hrc4 = hostRoleCommandFactory.create("h1", org.apache.ambari.server.Role.FLUME_HANDLER, null, org.apache.ambari.server.RoleCommand.EXECUTE);
        hrc4.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING);
        java.util.List<org.apache.ambari.server.actionmanager.HostRoleCommand> hostRoleCommands = java.util.Arrays.asList(hrc1, hrc3, hrc4);
        scheduler.cancelHostRoleCommands(hostRoleCommands, "foo");
        org.easymock.EasyMock.verify(db);
    }

    @org.junit.Test
    public void testSkippableCommandFailureDoesNotAbortRequest() throws java.lang.Exception {
        java.util.Properties properties = new java.util.Properties();
        org.apache.ambari.server.configuration.Configuration conf = new org.apache.ambari.server.configuration.Configuration(properties);
        org.apache.ambari.server.state.Clusters fsm = Mockito.mock(org.apache.ambari.server.state.Clusters.class);
        org.apache.ambari.server.state.Cluster oneClusterMock = Mockito.mock(org.apache.ambari.server.state.Cluster.class);
        org.apache.ambari.server.state.Host host = Mockito.mock(org.apache.ambari.server.state.Host.class);
        org.apache.ambari.server.state.Service serviceObj = Mockito.mock(org.apache.ambari.server.state.Service.class);
        org.apache.ambari.server.state.ServiceComponent scomp = Mockito.mock(org.apache.ambari.server.state.ServiceComponent.class);
        org.apache.ambari.server.state.ServiceComponentHost sch = Mockito.mock(org.apache.ambari.server.state.ServiceComponentHost.class);
        com.google.inject.persist.UnitOfWork unitOfWork = Mockito.mock(com.google.inject.persist.UnitOfWork.class);
        org.apache.ambari.server.events.publishers.AgentCommandsPublisher agentCommandsPublisher = Mockito.mock(org.apache.ambari.server.events.publishers.AgentCommandsPublisher.class);
        Mockito.when(fsm.getCluster(Matchers.anyString())).thenReturn(oneClusterMock);
        Mockito.when(fsm.getHost(Matchers.anyString())).thenReturn(host);
        Mockito.when(host.getHostId()).thenReturn(1L);
        Mockito.when(host.getState()).thenReturn(org.apache.ambari.server.state.HostState.HEALTHY);
        Mockito.when(oneClusterMock.getService(Matchers.anyString())).thenReturn(serviceObj);
        Mockito.when(serviceObj.getServiceComponent(Matchers.anyString())).thenReturn(scomp);
        Mockito.when(scomp.getServiceComponentHost(Matchers.anyString())).thenReturn(sch);
        Mockito.when(serviceObj.getCluster()).thenReturn(oneClusterMock);
        java.lang.String hostname1 = "ahost.ambari.apache.org";
        java.util.HashMap<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> hosts = new java.util.HashMap<>();
        hosts.put(hostname1, sch);
        Mockito.when(scomp.getServiceComponentHosts()).thenReturn(hosts);
        org.apache.ambari.server.actionmanager.Stage stage = null;
        org.apache.ambari.server.actionmanager.Stage stage2 = null;
        final java.util.List<org.apache.ambari.server.actionmanager.Stage> stages = new java.util.ArrayList<>();
        final java.util.List<org.apache.ambari.server.actionmanager.Stage> firstStageInProgress = new java.util.ArrayList<>();
        stages.add(stage = getStageWithSingleTask(hostname1, "cluster1", org.apache.ambari.server.Role.NAMENODE, org.apache.ambari.server.RoleCommand.STOP, org.apache.ambari.server.state.Service.Type.HDFS, 1, 1, 1));
        addInstallTaskToStage(stage, hostname1, "cluster1", org.apache.ambari.server.Role.HBASE_MASTER, org.apache.ambari.server.RoleCommand.INSTALL, org.apache.ambari.server.state.Service.Type.HBASE, 1);
        stages.add(stage2 = getStageWithSingleTask(hostname1, "cluster1", org.apache.ambari.server.Role.DATANODE, org.apache.ambari.server.RoleCommand.STOP, org.apache.ambari.server.state.Service.Type.HDFS, 1, 1, 1));
        for (org.apache.ambari.server.actionmanager.Stage stageToMakeSkippable : stages) {
            stageToMakeSkippable.setSkippable(true);
        }
        org.apache.ambari.server.actionmanager.HostRoleCommand command = stage.getOrderedHostRoleCommands().iterator().next();
        command.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED);
        firstStageInProgress.add(stage);
        org.apache.ambari.server.actionmanager.ActionDBAccessor db = Mockito.mock(org.apache.ambari.server.actionmanager.ActionDBAccessor.class);
        org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAOMock = Mockito.mock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        org.mockito.Mockito.doNothing().when(hostRoleCommandDAOMock).publishTaskCreateEvent(Matchers.anyListOf(org.apache.ambari.server.actionmanager.HostRoleCommand.class));
        org.apache.ambari.server.orm.entities.RequestEntity request = Mockito.mock(org.apache.ambari.server.orm.entities.RequestEntity.class);
        Mockito.when(request.getClusterHostInfo()).thenReturn(org.apache.ambari.server.actionmanager.TestActionScheduler.CLUSTER_HOST_INFO);
        Mockito.when(request.isExclusive()).thenReturn(false);
        Mockito.when(db.getRequestEntity(Matchers.anyLong())).thenReturn(request);
        Mockito.when(db.getCommandsInProgressCount()).thenReturn(firstStageInProgress.size());
        Mockito.when(db.getFirstStageInProgressPerRequest()).thenReturn(firstStageInProgress);
        Mockito.doAnswer(new org.mockito.stubbing.Answer<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                java.util.List<org.apache.ambari.server.agent.CommandReport> reports = ((java.util.List<org.apache.ambari.server.agent.CommandReport>) (invocation.getArguments()[0]));
                for (org.apache.ambari.server.agent.CommandReport report : reports) {
                    java.lang.String actionId = report.getActionId();
                    long[] requestStageIds = org.apache.ambari.server.utils.StageUtils.getRequestStage(actionId);
                    java.lang.Long requestId = requestStageIds[0];
                    java.lang.Long stageId = requestStageIds[1];
                    java.lang.Long id = report.getTaskId();
                    for (org.apache.ambari.server.actionmanager.Stage stage : stages) {
                        if (requestId.equals(stage.getRequestId()) && stageId.equals(stage.getStageId())) {
                            for (org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand : stage.getOrderedHostRoleCommands()) {
                                if (hostRoleCommand.getTaskId() == id) {
                                    hostRoleCommand.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.valueOf(report.getStatus()));
                                }
                            }
                        }
                    }
                }
                return null;
            }
        }).when(db).updateHostRoleStates(Matchers.anyCollectionOf(org.apache.ambari.server.agent.CommandReport.class));
        Mockito.when(db.getTask(Matchers.anyLong())).thenAnswer(new org.mockito.stubbing.Answer<java.lang.Object>() {
            @java.lang.Override
            public java.lang.Object answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                java.lang.Long taskId = ((java.lang.Long) (invocation.getArguments()[0]));
                for (org.apache.ambari.server.actionmanager.Stage stage : stages) {
                    for (org.apache.ambari.server.actionmanager.HostRoleCommand command : stage.getOrderedHostRoleCommands()) {
                        if (taskId.equals(command.getTaskId())) {
                            return command;
                        }
                    }
                }
                return null;
            }
        });
        Mockito.doAnswer(new org.mockito.stubbing.Answer<java.lang.Void>() {
            @java.lang.Override
            public java.lang.Void answer(org.mockito.invocation.InvocationOnMock invocation) throws java.lang.Throwable {
                java.lang.Long requestId = ((java.lang.Long) (invocation.getArguments()[0]));
                for (org.apache.ambari.server.actionmanager.Stage stage : stages) {
                    if (requestId.equals(stage.getRequestId())) {
                        for (org.apache.ambari.server.actionmanager.HostRoleCommand command : stage.getOrderedHostRoleCommands()) {
                            if (((command.getStatus() == org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED) || (command.getStatus() == org.apache.ambari.server.actionmanager.HostRoleStatus.IN_PROGRESS)) || (command.getStatus() == org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING)) {
                                command.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.ABORTED);
                            }
                        }
                    }
                }
                return null;
            }
        }).when(db).abortOperation(Matchers.anyLong());
        org.apache.ambari.server.actionmanager.ActionScheduler scheduler = Mockito.spy(new org.apache.ambari.server.actionmanager.ActionScheduler(100, 50, db, fsm, 3, new org.apache.ambari.server.controller.HostsMap(((java.lang.String) (null))), unitOfWork, null, conf, entityManagerProviderMock, hostRoleCommandDAOMock, ((org.apache.ambari.server.actionmanager.HostRoleCommandFactory) (null)), agentCommandsPublisher));
        Mockito.doReturn(false).when(scheduler).wasAgentRestartedDuringOperation(Matchers.any(org.apache.ambari.server.state.Host.class), Matchers.any(org.apache.ambari.server.actionmanager.Stage.class), Matchers.anyString());
        scheduler.doWork();
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED, stages.get(0).getHostRoleStatus(hostname1, "NAMENODE"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.QUEUED, stages.get(0).getHostRoleStatus(hostname1, "HBASE_MASTER"));
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING, stages.get(1).getHostRoleStatus(hostname1, "DATANODE"));
        org.easymock.EasyMock.verify(entityManagerProviderMock);
    }

    @org.junit.Test
    public void testSkippableCommandFailureDoesNotAbortNextStage() throws java.lang.Exception {
        org.apache.ambari.server.actionmanager.Stage previousStage = EasyMock.createMock(org.apache.ambari.server.actionmanager.Stage.class);
        org.apache.ambari.server.actionmanager.Stage nextStage = EasyMock.createMock(org.apache.ambari.server.actionmanager.Stage.class);
        org.apache.ambari.server.actionmanager.ActionDBAccessor actionDBAccessor = EasyMock.createMock(org.apache.ambari.server.actionmanager.ActionDBAccessor.class);
        EasyMock.expect(previousStage.isSkippable()).andReturn(false);
        EasyMock.expect(nextStage.getStageId()).andReturn(5L);
        EasyMock.expect(nextStage.getRequestId()).andReturn(1L);
        EasyMock.expect(actionDBAccessor.getStage("1-4")).andReturn(previousStage);
        java.util.Map<java.lang.String, org.apache.ambari.server.actionmanager.HostRoleCommand> roleCommandMap = new java.util.HashMap<>();
        org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = EasyMock.createMock(org.apache.ambari.server.actionmanager.HostRoleCommand.class);
        EasyMock.expect(hostRoleCommand.getRole()).andReturn(org.apache.ambari.server.Role.DATANODE).anyTimes();
        EasyMock.expect(hostRoleCommand.getStatus()).andReturn(org.apache.ambari.server.actionmanager.HostRoleStatus.SKIPPED_FAILED);
        roleCommandMap.put(org.apache.ambari.server.Role.DATANODE.toString(), hostRoleCommand);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.actionmanager.HostRoleCommand>> hostRoleCommands = new java.util.HashMap<>();
        hostRoleCommands.put("host", roleCommandMap);
        EasyMock.expect(previousStage.getHostRoleCommands()).andReturn(hostRoleCommands).anyTimes();
        EasyMock.expect(previousStage.getSuccessFactor(org.apache.ambari.server.Role.DATANODE)).andReturn(0.5F);
        org.apache.ambari.server.actionmanager.ActionScheduler scheduler = new org.apache.ambari.server.actionmanager.ActionScheduler(100, 50, actionDBAccessor, null, 3, new org.apache.ambari.server.controller.HostsMap(((java.lang.String) (null))), null, null, null, entityManagerProviderMock, ((org.apache.ambari.server.orm.dao.HostRoleCommandDAO) (null)), ((org.apache.ambari.server.actionmanager.HostRoleCommandFactory) (null)), null, null);
        EasyMock.replay(previousStage, nextStage, actionDBAccessor, hostRoleCommand);
        java.lang.reflect.Method method = scheduler.getClass().getDeclaredMethod("hasPreviousStageFailed", org.apache.ambari.server.actionmanager.Stage.class);
        method.setAccessible(true);
        java.lang.Object result = method.invoke(scheduler, nextStage);
        org.junit.Assert.assertFalse(((java.lang.Boolean) (result)));
        org.easymock.EasyMock.verify(previousStage, nextStage, actionDBAccessor, hostRoleCommand);
    }

    @org.junit.Test
    public void testPreviousStageToFailForFirstStage() throws java.lang.Exception {
        org.apache.ambari.server.actionmanager.Stage nextStage = EasyMock.createNiceMock(org.apache.ambari.server.actionmanager.Stage.class);
        EasyMock.expect(nextStage.getStageId()).andReturn(0L);
        org.apache.ambari.server.actionmanager.ActionScheduler scheduler = new org.apache.ambari.server.actionmanager.ActionScheduler(100, 50, null, null, 3, new org.apache.ambari.server.controller.HostsMap(((java.lang.String) (null))), null, null, null, entityManagerProviderMock, null, null, null);
        EasyMock.replay(nextStage);
        java.lang.reflect.Method method = scheduler.getClass().getDeclaredMethod("hasPreviousStageFailed", org.apache.ambari.server.actionmanager.Stage.class);
        method.setAccessible(true);
        java.lang.Object result = method.invoke(scheduler, nextStage);
        org.junit.Assert.assertFalse(((java.lang.Boolean) (result)));
        org.easymock.EasyMock.verify(nextStage);
    }

    @org.junit.Test
    public void testPreviousStageToFailForSecondStage() throws java.lang.Exception {
        org.apache.ambari.server.actionmanager.Stage previousStage = EasyMock.createMock(org.apache.ambari.server.actionmanager.Stage.class);
        org.apache.ambari.server.actionmanager.Stage nextStage = EasyMock.createMock(org.apache.ambari.server.actionmanager.Stage.class);
        org.apache.ambari.server.actionmanager.ActionDBAccessor actionDBAccessor = EasyMock.createMock(org.apache.ambari.server.actionmanager.ActionDBAccessor.class);
        EasyMock.expect(previousStage.isSkippable()).andReturn(false);
        EasyMock.expect(nextStage.getStageId()).andReturn(1L);
        EasyMock.expect(nextStage.getRequestId()).andReturn(1L);
        EasyMock.expect(actionDBAccessor.getStage("1-0")).andReturn(previousStage);
        java.util.Map<java.lang.String, org.apache.ambari.server.actionmanager.HostRoleCommand> roleCommandMap = new java.util.HashMap<>();
        org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = EasyMock.createMock(org.apache.ambari.server.actionmanager.HostRoleCommand.class);
        EasyMock.expect(hostRoleCommand.getRole()).andReturn(org.apache.ambari.server.Role.DATANODE).anyTimes();
        EasyMock.expect(hostRoleCommand.getStatus()).andReturn(org.apache.ambari.server.actionmanager.HostRoleStatus.FAILED);
        roleCommandMap.put(org.apache.ambari.server.Role.DATANODE.toString(), hostRoleCommand);
        java.util.Map<java.lang.String, java.util.Map<java.lang.String, org.apache.ambari.server.actionmanager.HostRoleCommand>> hostRoleCommands = new java.util.HashMap<>();
        hostRoleCommands.put("host", roleCommandMap);
        EasyMock.expect(previousStage.getHostRoleCommands()).andReturn(hostRoleCommands).anyTimes();
        EasyMock.expect(previousStage.getSuccessFactor(org.apache.ambari.server.Role.DATANODE)).andReturn(0.5F);
        org.apache.ambari.server.actionmanager.ActionScheduler scheduler = new org.apache.ambari.server.actionmanager.ActionScheduler(100, 50, actionDBAccessor, null, 3, new org.apache.ambari.server.controller.HostsMap(((java.lang.String) (null))), null, null, null, entityManagerProviderMock, null, null, null);
        EasyMock.replay(previousStage, nextStage, actionDBAccessor, hostRoleCommand);
        java.lang.reflect.Method method = scheduler.getClass().getDeclaredMethod("hasPreviousStageFailed", org.apache.ambari.server.actionmanager.Stage.class);
        method.setAccessible(true);
        java.lang.Object result = method.invoke(scheduler, nextStage);
        org.junit.Assert.assertTrue(((java.lang.Boolean) (result)));
        org.easymock.EasyMock.verify(previousStage, nextStage, actionDBAccessor, hostRoleCommand);
    }

    public static class MockModule extends com.google.inject.AbstractModule {
        @java.lang.Override
        protected void configure() {
            bind(org.apache.ambari.server.state.Clusters.class).toInstance(Mockito.mock(org.apache.ambari.server.state.Clusters.class));
        }
    }
}