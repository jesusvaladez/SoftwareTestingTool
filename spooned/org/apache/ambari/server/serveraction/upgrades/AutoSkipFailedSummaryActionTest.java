package org.apache.ambari.server.serveraction.upgrades;
import com.google.inject.persist.UnitOfWork;
import javax.persistence.EntityManager;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.anyLong;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.createNiceMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
public class AutoSkipFailedSummaryActionTest {
    private com.google.inject.Injector m_injector;

    private static final org.apache.ambari.server.state.StackId HDP_STACK = new org.apache.ambari.server.state.StackId("HDP-2.2.0");

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.ExecutionCommandDAO executionCommandDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.orm.dao.HostDAO hostDAO;

    @com.google.inject.Inject
    private org.apache.ambari.server.actionmanager.ExecutionCommandWrapperFactory ecwFactory;

    private org.apache.ambari.server.orm.dao.UpgradeDAO upgradeDAOMock;

    private org.apache.ambari.server.orm.dao.HostRoleCommandDAO hostRoleCommandDAOMock;

    private org.apache.ambari.server.state.Clusters clustersMock;

    private org.apache.ambari.server.state.Cluster clusterMock;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        upgradeDAOMock = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.UpgradeDAO.class);
        hostRoleCommandDAOMock = EasyMock.createNiceMock(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class);
        clustersMock = EasyMock.createNiceMock(org.apache.ambari.server.state.Clusters.class);
        clusterMock = EasyMock.createNiceMock(org.apache.ambari.server.state.Cluster.class);
        EasyMock.expect(clustersMock.getCluster(EasyMock.anyString())).andReturn(clusterMock).anyTimes();
        EasyMock.replay(clustersMock);
        EasyMock.expect(clusterMock.getDesiredStackVersion()).andReturn(org.apache.ambari.server.serveraction.upgrades.AutoSkipFailedSummaryActionTest.HDP_STACK).anyTimes();
        EasyMock.replay(clusterMock);
        org.apache.ambari.server.orm.InMemoryDefaultTestModule module = new org.apache.ambari.server.orm.InMemoryDefaultTestModule();
        m_injector = com.google.inject.Guice.createInjector(com.google.inject.util.Modules.override(module).with(new org.apache.ambari.server.serveraction.upgrades.AutoSkipFailedSummaryActionTest.MockModule()));
        m_injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        m_injector.injectMembers(this);
        m_injector.getInstance(com.google.inject.persist.UnitOfWork.class).begin();
        m_injector.getInstance(org.apache.ambari.server.metadata.ActionMetadata.class).addServiceCheckAction("ZOOKEEPER");
    }

    @org.junit.After
    public void teardown() throws java.lang.Exception {
        m_injector.getInstance(com.google.inject.persist.UnitOfWork.class).end();
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabase(m_injector.getProvider(javax.persistence.EntityManager.class).get());
    }

    @org.junit.Test
    public void testAutoSkipFailedSummaryAction__green() throws java.lang.Exception {
        org.apache.ambari.server.serveraction.upgrades.AutoSkipFailedSummaryAction action = new org.apache.ambari.server.serveraction.upgrades.AutoSkipFailedSummaryAction();
        m_injector.injectMembers(action);
        org.apache.ambari.server.state.ServiceComponentHostEvent event = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceComponentHostEvent.class);
        org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = new org.apache.ambari.server.actionmanager.HostRoleCommand("host1", org.apache.ambari.server.Role.AMBARI_SERVER_ACTION, event, org.apache.ambari.server.RoleCommand.EXECUTE, hostDAO, executionCommandDAO, ecwFactory);
        hostRoleCommand.setRequestId(1L);
        hostRoleCommand.setStageId(1L);
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = new org.apache.ambari.server.agent.ExecutionCommand();
        executionCommand.setClusterName("cc");
        executionCommand.setRoleCommand(org.apache.ambari.server.RoleCommand.EXECUTE);
        executionCommand.setRole("AMBARI_SERVER_ACTION");
        executionCommand.setServiceName("");
        executionCommand.setTaskId(1L);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapper wrapper = new org.apache.ambari.server.actionmanager.ExecutionCommandWrapper(executionCommand);
        hostRoleCommand.setExecutionCommandWrapper(wrapper);
        java.lang.reflect.Field f = org.apache.ambari.server.serveraction.AbstractServerAction.class.getDeclaredField("hostRoleCommand");
        f.setAccessible(true);
        f.set(action, hostRoleCommand);
        final org.apache.ambari.server.orm.entities.UpgradeItemEntity upgradeItem1 = new org.apache.ambari.server.orm.entities.UpgradeItemEntity();
        upgradeItem1.setStageId(5L);
        final org.apache.ambari.server.orm.entities.UpgradeItemEntity upgradeItem2 = new org.apache.ambari.server.orm.entities.UpgradeItemEntity();
        upgradeItem2.setStageId(6L);
        org.apache.ambari.server.orm.entities.UpgradeGroupEntity upgradeGroupEntity = new org.apache.ambari.server.orm.entities.UpgradeGroupEntity();
        upgradeGroupEntity.setId(11L);
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeItemEntity> groupUpgradeItems = new java.util.ArrayList<org.apache.ambari.server.orm.entities.UpgradeItemEntity>() {
            {
                add(upgradeItem1);
                add(upgradeItem2);
            }
        };
        upgradeGroupEntity.setItems(groupUpgradeItems);
        org.apache.ambari.server.orm.entities.UpgradeItemEntity upgradeItemEntity = new org.apache.ambari.server.orm.entities.UpgradeItemEntity();
        upgradeItemEntity.setGroupEntity(upgradeGroupEntity);
        EasyMock.expect(upgradeDAOMock.findUpgradeItemByRequestAndStage(EasyMock.anyLong(), EasyMock.anyLong())).andReturn(upgradeItemEntity).anyTimes();
        EasyMock.expect(upgradeDAOMock.findUpgradeGroup(EasyMock.anyLong())).andReturn(upgradeGroupEntity).anyTimes();
        EasyMock.replay(upgradeDAOMock);
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> skippedTasks = new java.util.ArrayList<org.apache.ambari.server.orm.entities.HostRoleCommandEntity>() {
            {
            }
        };
        EasyMock.expect(hostRoleCommandDAOMock.findByStatusBetweenStages(EasyMock.anyLong(), EasyMock.anyObject(org.apache.ambari.server.actionmanager.HostRoleStatus.class), EasyMock.anyLong(), EasyMock.anyLong())).andReturn(skippedTasks).anyTimes();
        EasyMock.replay(hostRoleCommandDAOMock);
        java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> requestSharedDataContext = new java.util.concurrent.ConcurrentHashMap<>();
        org.apache.ambari.server.agent.CommandReport result = action.execute(requestSharedDataContext);
        org.junit.Assert.assertNotNull(result.getStructuredOut());
        org.junit.Assert.assertEquals(0, result.getExitCode());
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.COMPLETED.toString(), result.getStatus());
        org.junit.Assert.assertEquals("There were no skipped failures", result.getStdOut());
        org.junit.Assert.assertEquals("{}", result.getStructuredOut());
        org.junit.Assert.assertEquals("", result.getStdErr());
    }

    @org.junit.Test
    public void testAutoSkipFailedSummaryAction__red() throws java.lang.Exception {
        org.apache.ambari.server.serveraction.upgrades.AutoSkipFailedSummaryAction action = new org.apache.ambari.server.serveraction.upgrades.AutoSkipFailedSummaryAction();
        m_injector.injectMembers(action);
        org.easymock.EasyMock.reset(clusterMock);
        org.apache.ambari.server.state.Service hdfsService = EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(hdfsService.getName()).andReturn("HDFS").anyTimes();
        EasyMock.expect(clusterMock.getServiceByComponentName("DATANODE")).andReturn(hdfsService).anyTimes();
        org.apache.ambari.server.state.Service zkService = EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(zkService.getName()).andReturn("ZOOKEEPER").anyTimes();
        EasyMock.expect(clusterMock.getServiceByComponentName("ZOOKEEPER_CLIENT")).andReturn(zkService).anyTimes();
        EasyMock.replay(clusterMock, hdfsService, zkService);
        org.apache.ambari.server.state.ServiceComponentHostEvent event = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceComponentHostEvent.class);
        final org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = new org.apache.ambari.server.actionmanager.HostRoleCommand("host1", org.apache.ambari.server.Role.AMBARI_SERVER_ACTION, event, org.apache.ambari.server.RoleCommand.EXECUTE, hostDAO, executionCommandDAO, ecwFactory);
        hostRoleCommand.setRequestId(1L);
        hostRoleCommand.setStageId(1L);
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = new org.apache.ambari.server.agent.ExecutionCommand();
        executionCommand.setClusterName("cc");
        executionCommand.setRoleCommand(org.apache.ambari.server.RoleCommand.EXECUTE);
        executionCommand.setRole("AMBARI_SERVER_ACTION");
        executionCommand.setServiceName("");
        executionCommand.setTaskId(1L);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapper wrapper = new org.apache.ambari.server.actionmanager.ExecutionCommandWrapper(executionCommand);
        hostRoleCommand.setExecutionCommandWrapper(wrapper);
        java.lang.reflect.Field f = org.apache.ambari.server.serveraction.AbstractServerAction.class.getDeclaredField("hostRoleCommand");
        f.setAccessible(true);
        f.set(action, hostRoleCommand);
        final org.apache.ambari.server.orm.entities.UpgradeItemEntity upgradeItem1 = new org.apache.ambari.server.orm.entities.UpgradeItemEntity();
        upgradeItem1.setStageId(5L);
        final org.apache.ambari.server.orm.entities.UpgradeItemEntity upgradeItem2 = new org.apache.ambari.server.orm.entities.UpgradeItemEntity();
        upgradeItem2.setStageId(6L);
        org.apache.ambari.server.orm.entities.UpgradeGroupEntity upgradeGroupEntity = new org.apache.ambari.server.orm.entities.UpgradeGroupEntity();
        upgradeGroupEntity.setId(11L);
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeItemEntity> groupUpgradeItems = new java.util.ArrayList<org.apache.ambari.server.orm.entities.UpgradeItemEntity>() {
            {
                add(upgradeItem1);
                add(upgradeItem2);
            }
        };
        upgradeGroupEntity.setItems(groupUpgradeItems);
        org.apache.ambari.server.orm.entities.UpgradeItemEntity upgradeItemEntity = new org.apache.ambari.server.orm.entities.UpgradeItemEntity();
        upgradeItemEntity.setGroupEntity(upgradeGroupEntity);
        EasyMock.expect(upgradeDAOMock.findUpgradeItemByRequestAndStage(EasyMock.anyLong(), EasyMock.anyLong())).andReturn(upgradeItemEntity).anyTimes();
        EasyMock.expect(upgradeDAOMock.findUpgradeGroup(EasyMock.anyLong())).andReturn(upgradeGroupEntity).anyTimes();
        EasyMock.replay(upgradeDAOMock);
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> skippedTasks = new java.util.ArrayList<org.apache.ambari.server.orm.entities.HostRoleCommandEntity>() {
            {
                add(createSkippedTask("DATANODE", "DATANODE", "host1.vm", "RESTART HDFS/DATANODE", org.apache.ambari.server.RoleCommand.CUSTOM_COMMAND, "RESTART"));
                add(createSkippedTask("DATANODE", "DATANODE", "host2.vm", "RESTART HDFS/DATANODE", org.apache.ambari.server.RoleCommand.CUSTOM_COMMAND, "RESTART"));
                add(createSkippedTask("ZOOKEEPER_QUORUM_SERVICE_CHECK", "ZOOKEEPER_CLIENT", "host2.vm", "SERVICE_CHECK ZOOKEEPER", org.apache.ambari.server.RoleCommand.SERVICE_CHECK, null));
            }
        };
        EasyMock.expect(hostRoleCommandDAOMock.findByStatusBetweenStages(EasyMock.anyLong(), EasyMock.anyObject(org.apache.ambari.server.actionmanager.HostRoleStatus.class), EasyMock.anyLong(), EasyMock.anyLong())).andReturn(skippedTasks).anyTimes();
        EasyMock.replay(hostRoleCommandDAOMock);
        java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> requestSharedDataContext = new java.util.concurrent.ConcurrentHashMap<>();
        org.apache.ambari.server.agent.CommandReport result = action.execute(requestSharedDataContext);
        org.junit.Assert.assertNotNull(result.getStructuredOut());
        org.junit.Assert.assertEquals(0, result.getExitCode());
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING.toString(), result.getStatus());
        org.junit.Assert.assertEquals("There were 3 skipped failure(s) that must be addressed " + "before you can proceed. Please resolve each failure before continuing with the upgrade.", result.getStdOut());
        org.junit.Assert.assertEquals("{\"failures\":" + (((("{\"service_check\":[\"ZOOKEEPER\"]," + "\"host_component\":{") + "\"host1.vm\":[{\"component\":\"DATANODE\",\"service\":\"HDFS\"}],") + "\"host2.vm\":[{\"component\":\"DATANODE\",\"service\":\"HDFS\"}]}},") + "\"skipped\":[\"service_check\",\"host_component\"]}"), result.getStructuredOut());
        org.junit.Assert.assertEquals("The following steps failed but were automatically skipped:\n" + (("DATANODE on host1.vm: RESTART HDFS/DATANODE\n" + "DATANODE on host2.vm: RESTART HDFS/DATANODE\n") + "ZOOKEEPER_CLIENT on host2.vm: SERVICE_CHECK ZOOKEEPER\n"), result.getStdErr());
    }

    @org.junit.Test
    public void testAutoSkipFailedSummaryAction__red__service_checks_only() throws java.lang.Exception {
        org.apache.ambari.server.serveraction.upgrades.AutoSkipFailedSummaryAction action = new org.apache.ambari.server.serveraction.upgrades.AutoSkipFailedSummaryAction();
        m_injector.injectMembers(action);
        org.apache.ambari.server.state.ServiceComponentHostEvent event = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceComponentHostEvent.class);
        final org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = new org.apache.ambari.server.actionmanager.HostRoleCommand("host1", org.apache.ambari.server.Role.AMBARI_SERVER_ACTION, event, org.apache.ambari.server.RoleCommand.EXECUTE, hostDAO, executionCommandDAO, ecwFactory);
        hostRoleCommand.setRequestId(1L);
        hostRoleCommand.setStageId(1L);
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = new org.apache.ambari.server.agent.ExecutionCommand();
        executionCommand.setClusterName("cc");
        executionCommand.setRoleCommand(org.apache.ambari.server.RoleCommand.EXECUTE);
        executionCommand.setRole("AMBARI_SERVER_ACTION");
        executionCommand.setServiceName("");
        executionCommand.setTaskId(1L);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapper wrapper = new org.apache.ambari.server.actionmanager.ExecutionCommandWrapper(executionCommand);
        hostRoleCommand.setExecutionCommandWrapper(wrapper);
        java.lang.reflect.Field f = org.apache.ambari.server.serveraction.AbstractServerAction.class.getDeclaredField("hostRoleCommand");
        f.setAccessible(true);
        f.set(action, hostRoleCommand);
        final org.apache.ambari.server.orm.entities.UpgradeItemEntity upgradeItem1 = new org.apache.ambari.server.orm.entities.UpgradeItemEntity();
        upgradeItem1.setStageId(5L);
        final org.apache.ambari.server.orm.entities.UpgradeItemEntity upgradeItem2 = new org.apache.ambari.server.orm.entities.UpgradeItemEntity();
        upgradeItem2.setStageId(6L);
        org.apache.ambari.server.orm.entities.UpgradeGroupEntity upgradeGroupEntity = new org.apache.ambari.server.orm.entities.UpgradeGroupEntity();
        upgradeGroupEntity.setId(11L);
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeItemEntity> groupUpgradeItems = new java.util.ArrayList<org.apache.ambari.server.orm.entities.UpgradeItemEntity>() {
            {
                add(upgradeItem1);
                add(upgradeItem2);
            }
        };
        upgradeGroupEntity.setItems(groupUpgradeItems);
        org.apache.ambari.server.orm.entities.UpgradeItemEntity upgradeItemEntity = new org.apache.ambari.server.orm.entities.UpgradeItemEntity();
        upgradeItemEntity.setGroupEntity(upgradeGroupEntity);
        EasyMock.expect(upgradeDAOMock.findUpgradeItemByRequestAndStage(EasyMock.anyLong(), EasyMock.anyLong())).andReturn(upgradeItemEntity).anyTimes();
        EasyMock.expect(upgradeDAOMock.findUpgradeGroup(EasyMock.anyLong())).andReturn(upgradeGroupEntity).anyTimes();
        EasyMock.replay(upgradeDAOMock);
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> skippedTasks = new java.util.ArrayList<org.apache.ambari.server.orm.entities.HostRoleCommandEntity>() {
            {
                add(createSkippedTask("ZOOKEEPER_QUORUM_SERVICE_CHECK", "ZOOKEEPER_CLIENT", "host2.vm", "SERVICE_CHECK ZOOKEEPER", org.apache.ambari.server.RoleCommand.SERVICE_CHECK, null));
            }
        };
        EasyMock.expect(hostRoleCommandDAOMock.findByStatusBetweenStages(EasyMock.anyLong(), EasyMock.anyObject(org.apache.ambari.server.actionmanager.HostRoleStatus.class), EasyMock.anyLong(), EasyMock.anyLong())).andReturn(skippedTasks).anyTimes();
        EasyMock.replay(hostRoleCommandDAOMock);
        java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> requestSharedDataContext = new java.util.concurrent.ConcurrentHashMap<>();
        org.apache.ambari.server.agent.CommandReport result = action.execute(requestSharedDataContext);
        org.junit.Assert.assertNotNull(result.getStructuredOut());
        org.junit.Assert.assertEquals(0, result.getExitCode());
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING.toString(), result.getStatus());
        org.junit.Assert.assertEquals("There were 1 skipped failure(s) that must be addressed " + "before you can proceed. Please resolve each failure before continuing with the upgrade.", result.getStdOut());
        org.junit.Assert.assertEquals("{\"failures\":{\"service_check\":[\"ZOOKEEPER\"]},\"skipped\":[\"service_check\"]}", result.getStructuredOut());
        org.junit.Assert.assertEquals("The following steps failed but were automatically skipped:\n" + "ZOOKEEPER_CLIENT on host2.vm: SERVICE_CHECK ZOOKEEPER\n", result.getStdErr());
    }

    @org.junit.Test
    public void testAutoSkipFailedSummaryAction__red__host_components_only() throws java.lang.Exception {
        org.apache.ambari.server.serveraction.upgrades.AutoSkipFailedSummaryAction action = new org.apache.ambari.server.serveraction.upgrades.AutoSkipFailedSummaryAction();
        m_injector.injectMembers(action);
        org.easymock.EasyMock.reset(clusterMock);
        org.apache.ambari.server.state.Service hdfsService = EasyMock.createNiceMock(org.apache.ambari.server.state.Service.class);
        EasyMock.expect(hdfsService.getName()).andReturn("HDFS").anyTimes();
        EasyMock.expect(clusterMock.getServiceByComponentName("DATANODE")).andReturn(hdfsService).anyTimes();
        EasyMock.replay(clusterMock, hdfsService);
        org.apache.ambari.server.state.ServiceComponentHostEvent event = EasyMock.createNiceMock(org.apache.ambari.server.state.ServiceComponentHostEvent.class);
        final org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand = new org.apache.ambari.server.actionmanager.HostRoleCommand("host1", org.apache.ambari.server.Role.AMBARI_SERVER_ACTION, event, org.apache.ambari.server.RoleCommand.EXECUTE, hostDAO, executionCommandDAO, ecwFactory);
        hostRoleCommand.setRequestId(1L);
        hostRoleCommand.setStageId(1L);
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = new org.apache.ambari.server.agent.ExecutionCommand();
        executionCommand.setClusterName("cc");
        executionCommand.setRoleCommand(org.apache.ambari.server.RoleCommand.EXECUTE);
        executionCommand.setRole("AMBARI_SERVER_ACTION");
        executionCommand.setServiceName("");
        executionCommand.setTaskId(1L);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapper wrapper = new org.apache.ambari.server.actionmanager.ExecutionCommandWrapper(executionCommand);
        hostRoleCommand.setExecutionCommandWrapper(wrapper);
        java.lang.reflect.Field f = org.apache.ambari.server.serveraction.AbstractServerAction.class.getDeclaredField("hostRoleCommand");
        f.setAccessible(true);
        f.set(action, hostRoleCommand);
        final org.apache.ambari.server.orm.entities.UpgradeItemEntity upgradeItem1 = new org.apache.ambari.server.orm.entities.UpgradeItemEntity();
        upgradeItem1.setStageId(5L);
        final org.apache.ambari.server.orm.entities.UpgradeItemEntity upgradeItem2 = new org.apache.ambari.server.orm.entities.UpgradeItemEntity();
        upgradeItem2.setStageId(6L);
        org.apache.ambari.server.orm.entities.UpgradeGroupEntity upgradeGroupEntity = new org.apache.ambari.server.orm.entities.UpgradeGroupEntity();
        upgradeGroupEntity.setId(11L);
        java.util.List<org.apache.ambari.server.orm.entities.UpgradeItemEntity> groupUpgradeItems = new java.util.ArrayList<org.apache.ambari.server.orm.entities.UpgradeItemEntity>() {
            {
                add(upgradeItem1);
                add(upgradeItem2);
            }
        };
        upgradeGroupEntity.setItems(groupUpgradeItems);
        org.apache.ambari.server.orm.entities.UpgradeItemEntity upgradeItemEntity = new org.apache.ambari.server.orm.entities.UpgradeItemEntity();
        upgradeItemEntity.setGroupEntity(upgradeGroupEntity);
        EasyMock.expect(upgradeDAOMock.findUpgradeItemByRequestAndStage(EasyMock.anyLong(), EasyMock.anyLong())).andReturn(upgradeItemEntity).anyTimes();
        EasyMock.expect(upgradeDAOMock.findUpgradeGroup(EasyMock.anyLong())).andReturn(upgradeGroupEntity).anyTimes();
        EasyMock.replay(upgradeDAOMock);
        java.util.List<org.apache.ambari.server.orm.entities.HostRoleCommandEntity> skippedTasks = new java.util.ArrayList<org.apache.ambari.server.orm.entities.HostRoleCommandEntity>() {
            {
                add(createSkippedTask("DATANODE", "DATANODE", "host1.vm", "RESTART HDFS/DATANODE", org.apache.ambari.server.RoleCommand.CUSTOM_COMMAND, "RESTART"));
                add(createSkippedTask("DATANODE", "DATANODE", "host2.vm", "RESTART HDFS/DATANODE", org.apache.ambari.server.RoleCommand.CUSTOM_COMMAND, "RESTART"));
            }
        };
        EasyMock.expect(hostRoleCommandDAOMock.findByStatusBetweenStages(EasyMock.anyLong(), EasyMock.anyObject(org.apache.ambari.server.actionmanager.HostRoleStatus.class), EasyMock.anyLong(), EasyMock.anyLong())).andReturn(skippedTasks).anyTimes();
        EasyMock.replay(hostRoleCommandDAOMock);
        java.util.concurrent.ConcurrentMap<java.lang.String, java.lang.Object> requestSharedDataContext = new java.util.concurrent.ConcurrentHashMap<>();
        org.apache.ambari.server.agent.CommandReport result = action.execute(requestSharedDataContext);
        org.junit.Assert.assertNotNull(result.getStructuredOut());
        org.junit.Assert.assertEquals(0, result.getExitCode());
        org.junit.Assert.assertEquals(org.apache.ambari.server.actionmanager.HostRoleStatus.HOLDING.toString(), result.getStatus());
        org.junit.Assert.assertEquals("There were 2 skipped failure(s) that must be addressed " + "before you can proceed. Please resolve each failure before continuing with the upgrade.", result.getStdOut());
        org.junit.Assert.assertEquals("{\"failures\":" + ((("{\"host_component\":" + "{\"host1.vm\":[{\"component\":\"DATANODE\",\"service\":\"HDFS\"}],") + "\"host2.vm\":[{\"component\":\"DATANODE\",\"service\":\"HDFS\"}]}},") + "\"skipped\":[\"host_component\"]}"), result.getStructuredOut());
        org.junit.Assert.assertEquals("The following steps failed but were automatically skipped:\n" + ("DATANODE on host1.vm: RESTART HDFS/DATANODE\n" + "DATANODE on host2.vm: RESTART HDFS/DATANODE\n"), result.getStdErr());
    }

    private org.apache.ambari.server.orm.entities.HostRoleCommandEntity createSkippedTask(java.lang.String role, java.lang.String componentName, java.lang.String hostname, java.lang.String commandDetail, org.apache.ambari.server.RoleCommand roleCommand, java.lang.String customCommandName) {
        org.apache.ambari.server.orm.entities.HostRoleCommandEntity result = new org.apache.ambari.server.orm.entities.HostRoleCommandEntity();
        org.apache.ambari.server.state.ServiceComponentHostEvent event = new org.apache.ambari.server.state.svccomphost.ServiceComponentHostOpInProgressEvent(componentName, hostname, 77L);
        org.apache.ambari.server.actionmanager.ServiceComponentHostEventWrapper eventWrapper = new org.apache.ambari.server.actionmanager.ServiceComponentHostEventWrapper(event);
        result.setEvent(eventWrapper.getEventJson());
        org.apache.ambari.server.orm.entities.HostEntity hostEntity = new org.apache.ambari.server.orm.entities.HostEntity();
        hostEntity.setHostName(hostname);
        result.setHostEntity(hostEntity);
        result.setTaskId(7L);
        result.setExitcode(1);
        result.setOutputLog("/output.log");
        result.setErrorLog("/error.log");
        result.setStdOut("Some stdout".getBytes());
        result.setStdError("Some stderr".getBytes());
        result.setCommandDetail(commandDetail);
        result.setRole(org.apache.ambari.server.Role.valueOf(role));
        result.setRoleCommand(roleCommand);
        result.setCustomCommandName(customCommandName);
        result.setStatus(org.apache.ambari.server.actionmanager.HostRoleStatus.SKIPPED_FAILED);
        return result;
    }

    public class MockModule extends com.google.inject.AbstractModule {
        @java.lang.Override
        protected void configure() {
            bind(org.apache.ambari.server.orm.dao.UpgradeDAO.class).toInstance(upgradeDAOMock);
            bind(org.apache.ambari.server.orm.dao.HostRoleCommandDAO.class).toInstance(hostRoleCommandDAOMock);
            bind(org.apache.ambari.server.state.Clusters.class).toInstance(clustersMock);
        }
    }
}