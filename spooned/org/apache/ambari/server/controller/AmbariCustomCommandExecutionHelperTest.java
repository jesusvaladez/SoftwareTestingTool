package org.apache.ambari.server.controller;
import org.apache.commons.collections.MapUtils;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.EasyMockRule;
import org.easymock.Mock;
import org.easymock.MockType;
import org.springframework.security.core.context.SecurityContextHolder;
public class AmbariCustomCommandExecutionHelperTest {
    private static final java.lang.String REQUEST_CONTEXT_PROPERTY = "context";

    @org.junit.Rule
    public org.easymock.EasyMockRule mocks = new org.easymock.EasyMockRule(this);

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.actionmanager.ActionManager actionManager;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.actionmanager.HostRoleCommand hostRoleCommand;

    @org.easymock.Mock(type = org.easymock.MockType.NICE)
    private org.apache.ambari.server.state.ConfigHelper configHelper;

    private com.google.inject.Injector injector;

    private org.apache.ambari.server.state.Clusters clusters;

    private org.apache.ambari.server.controller.AmbariManagementController ambariManagementController;

    private org.easymock.Capture<org.apache.ambari.server.actionmanager.Request> requestCapture = org.easymock.EasyMock.newCapture();

    private static final java.lang.String OVERRIDDEN_SERVICE_CHECK_TIMEOUT_VALUE = "550";

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        org.easymock.EasyMock.reset(actionManager, hostRoleCommand, configHelper);
        org.apache.ambari.server.orm.InMemoryDefaultTestModule module = new org.apache.ambari.server.orm.InMemoryDefaultTestModule() {
            @java.lang.Override
            protected void configure() {
                getProperties().setProperty(org.apache.ambari.server.configuration.Configuration.AGENT_SERVICE_CHECK_TASK_TIMEOUT.getKey(), org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelperTest.OVERRIDDEN_SERVICE_CHECK_TIMEOUT_VALUE);
                super.configure();
                bind(org.apache.ambari.server.actionmanager.ActionManager.class).toInstance(actionManager);
                bind(org.apache.ambari.server.state.ConfigHelper.class).toInstance(configHelper);
            }
        };
        injector = com.google.inject.Guice.createInjector(module);
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        ambariManagementController = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        org.easymock.EasyMock.expect(configHelper.getPropertyValuesWithPropertyType(org.easymock.EasyMock.anyObject(org.apache.ambari.server.state.StackId.class), org.easymock.EasyMock.anyObject(org.apache.ambari.server.state.PropertyInfo.PropertyType.class), org.easymock.EasyMock.anyObject(org.apache.ambari.server.state.Cluster.class), org.easymock.EasyMock.anyObject(java.util.Map.class))).andReturn(java.util.Collections.EMPTY_SET);
        org.easymock.EasyMock.expect(configHelper.getHostActualConfigs(org.easymock.EasyMock.anyLong())).andReturn(new org.apache.ambari.server.events.AgentConfigsUpdateEvent(null, new java.util.TreeMap<>())).anyTimes();
        org.easymock.EasyMock.replay(configHelper);
        org.apache.ambari.server.utils.StageUtils.setTopologyManager(injector.getInstance(org.apache.ambari.server.topology.TopologyManager.class));
        org.apache.ambari.server.utils.StageUtils.setConfiguration(injector.getInstance(org.apache.ambari.server.configuration.Configuration.class));
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        createClusterFixture("c1", new org.apache.ambari.server.state.StackId("HDP-2.0.6"), "2.0.6-1234", "c1");
        org.easymock.EasyMock.verify(configHelper);
        org.easymock.EasyMock.reset(configHelper);
        org.easymock.EasyMock.expect(hostRoleCommand.getTaskId()).andReturn(1L);
        org.easymock.EasyMock.expect(hostRoleCommand.getStageId()).andReturn(1L);
        org.easymock.EasyMock.expect(hostRoleCommand.getRoleCommand()).andReturn(org.apache.ambari.server.RoleCommand.CUSTOM_COMMAND);
        org.easymock.EasyMock.expect(hostRoleCommand.getRole()).andReturn(org.apache.ambari.server.Role.AMBARI_SERVER_ACTION);
        org.easymock.EasyMock.expect(hostRoleCommand.getStatus()).andReturn(org.apache.ambari.server.actionmanager.HostRoleStatus.PENDING);
        org.easymock.EasyMock.expect(actionManager.getNextRequestId()).andReturn(1L).anyTimes();
        org.easymock.EasyMock.expect(actionManager.getRequestTasks(1L)).andReturn(java.util.Collections.singletonList(hostRoleCommand));
        org.apache.ambari.server.state.StackInfo stackInfo = new org.apache.ambari.server.state.StackInfo();
        stackInfo.setName("HDP");
        stackInfo.setVersion("2.0.6");
        org.apache.ambari.server.state.StackId stackId = new org.apache.ambari.server.state.StackId(stackInfo);
        java.util.Map<java.lang.String, org.apache.ambari.server.state.DesiredConfig> desiredConfigMap = new java.util.HashMap<>();
        java.util.Map<org.apache.ambari.server.state.PropertyInfo, java.lang.String> userProperties = new java.util.HashMap<>();
        java.util.Map<org.apache.ambari.server.state.PropertyInfo, java.lang.String> groupProperties = new java.util.HashMap<>();
        org.apache.ambari.server.state.PropertyInfo userProperty = new org.apache.ambari.server.state.PropertyInfo();
        userProperty.setFilename("zookeeper-env.xml");
        userProperty.setName("zookeeper-user");
        userProperty.setValue("zookeeperUser");
        org.apache.ambari.server.state.PropertyInfo groupProperty = new org.apache.ambari.server.state.PropertyInfo();
        groupProperty.setFilename("zookeeper-env.xml");
        groupProperty.setName("zookeeper-group");
        groupProperty.setValue("zookeeperGroup");
        org.apache.ambari.server.state.ValueAttributesInfo valueAttributesInfo = new org.apache.ambari.server.state.ValueAttributesInfo();
        valueAttributesInfo.setType("user");
        java.util.Set<org.apache.ambari.server.state.UserGroupInfo> userGroupEntries = new java.util.HashSet<>();
        org.apache.ambari.server.state.UserGroupInfo userGroupInfo = new org.apache.ambari.server.state.UserGroupInfo();
        userGroupInfo.setType("zookeeper-env");
        userGroupInfo.setName("zookeeper-group");
        userGroupEntries.add(userGroupInfo);
        valueAttributesInfo.setUserGroupEntries(userGroupEntries);
        userProperty.setPropertyValueAttributes(valueAttributesInfo);
        userProperties.put(userProperty, "zookeeperUser");
        groupProperties.put(groupProperty, "zookeeperGroup");
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> userGroupsMap = new java.util.HashMap<>();
        userGroupsMap.put("zookeeperUser", new java.util.HashSet<>(java.util.Arrays.asList("zookeeperGroup")));
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster("c1");
        org.easymock.EasyMock.expect(configHelper.getPropertiesWithPropertyType(stackId, org.apache.ambari.server.state.PropertyInfo.PropertyType.USER, cluster, desiredConfigMap)).andReturn(userProperties).anyTimes();
        org.easymock.EasyMock.expect(configHelper.getPropertiesWithPropertyType(stackId, org.apache.ambari.server.state.PropertyInfo.PropertyType.GROUP, cluster, desiredConfigMap)).andReturn(groupProperties).anyTimes();
        org.easymock.EasyMock.expect(configHelper.createUserGroupsMap(stackId, cluster, desiredConfigMap)).andReturn(userGroupsMap).anyTimes();
        actionManager.sendActions(org.easymock.EasyMock.capture(requestCapture), org.easymock.EasyMock.anyObject(org.apache.ambari.server.controller.ExecuteActionRequest.class));
        org.easymock.EasyMock.expectLastCall();
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
    }

    @org.junit.Test
    public void testRefreshQueueCustomCommand() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> requestProperties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put(org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelperTest.REQUEST_CONTEXT_PROPERTY, "Refresh YARN Capacity Scheduler");
                put("command", "REFRESHQUEUES");
            }
        };
        org.apache.ambari.server.controller.ExecuteActionRequest actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest("c1", "REFRESHQUEUES", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("forceRefreshConfigTagsBeforeExecution", "true");
            }
        }, false);
        actionRequest.getResourceFilters().add(new org.apache.ambari.server.controller.internal.RequestResourceFilter("YARN", "RESOURCEMANAGER", java.util.Collections.singletonList("c1-c6401")));
        org.easymock.EasyMock.replay(hostRoleCommand, actionManager, configHelper);
        ambariManagementController.createAction(actionRequest, requestProperties);
        org.apache.ambari.server.actionmanager.Request request = requestCapture.getValue();
        junit.framework.Assert.assertNotNull(request);
        junit.framework.Assert.assertNotNull(request.getStages());
        junit.framework.Assert.assertEquals(1, request.getStages().size());
        org.apache.ambari.server.actionmanager.Stage stage = request.getStages().iterator().next();
        junit.framework.Assert.assertEquals(1, stage.getHosts().size());
        java.util.List<org.apache.ambari.server.actionmanager.ExecutionCommandWrapper> commands = stage.getExecutionCommands("c1-c6401");
        junit.framework.Assert.assertEquals(1, commands.size());
        org.apache.ambari.server.agent.ExecutionCommand command = commands.get(0).getExecutionCommand();
        junit.framework.Assert.assertNotNull(command.getHostLevelParams());
        junit.framework.Assert.assertTrue(command.getHostLevelParams().containsKey(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.USER_GROUPS));
        junit.framework.Assert.assertEquals("{\"zookeeperUser\":[\"zookeeperGroup\"]}", command.getHostLevelParams().get(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.USER_GROUPS));
        junit.framework.Assert.assertEquals(false, command.isOverrideConfigs());
        junit.framework.Assert.assertNull(command.getRepositoryFile());
    }

    @org.junit.Test
    public void testHostsFilterHealthy() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> requestProperties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("context", "Restart all components for GANGLIA");
                put("operation_level/level", "SERVICE");
                put("operation_level/service_name", "GANGLIA");
                put("operation_level/cluster_name", "c1");
            }
        };
        org.apache.ambari.server.controller.ExecuteActionRequest actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest("c1", "RESTART", null, java.util.Arrays.asList(new org.apache.ambari.server.controller.internal.RequestResourceFilter("GANGLIA", "GANGLIA_SERVER", java.util.Collections.singletonList("c1-c6401")), new org.apache.ambari.server.controller.internal.RequestResourceFilter("GANGLIA", "GANGLIA_MONITOR", java.util.Collections.singletonList("c1-c6401")), new org.apache.ambari.server.controller.internal.RequestResourceFilter("GANGLIA", "GANGLIA_MONITOR", java.util.Collections.singletonList("c1-c6402"))), new org.apache.ambari.server.controller.internal.RequestOperationLevel(org.apache.ambari.server.controller.spi.Resource.Type.Service, "c1", "GANGLIA", null, null), new java.util.HashMap<>(), false);
        org.easymock.EasyMock.replay(hostRoleCommand, actionManager, configHelper);
        ambariManagementController.createAction(actionRequest, requestProperties);
        org.apache.ambari.server.actionmanager.Request request = requestCapture.getValue();
        junit.framework.Assert.assertNotNull(request);
        junit.framework.Assert.assertNotNull(request.getStages());
        junit.framework.Assert.assertEquals(1, request.getStages().size());
        org.apache.ambari.server.actionmanager.Stage stage = request.getStages().iterator().next();
        junit.framework.Assert.assertEquals(2, stage.getHostRoleCommands().size());
    }

    @org.junit.Test
    public void testHostsFilterUnhealthyHost() throws java.lang.Exception {
        clusters.getHost("c1-c6402").setState(org.apache.ambari.server.state.HostState.HEARTBEAT_LOST);
        java.util.Map<java.lang.String, java.lang.String> requestProperties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("context", "Restart all components for GANGLIA");
                put("operation_level/level", "SERVICE");
                put("operation_level/service_name", "GANGLIA");
                put("operation_level/cluster_name", "c1");
            }
        };
        org.apache.ambari.server.controller.ExecuteActionRequest actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest("c1", "RESTART", null, java.util.Arrays.asList(new org.apache.ambari.server.controller.internal.RequestResourceFilter("GANGLIA", "GANGLIA_SERVER", java.util.Collections.singletonList("c1-c6401")), new org.apache.ambari.server.controller.internal.RequestResourceFilter("GANGLIA", "GANGLIA_MONITOR", java.util.Collections.singletonList("c1-c6401")), new org.apache.ambari.server.controller.internal.RequestResourceFilter("GANGLIA", "GANGLIA_MONITOR", java.util.Collections.singletonList("c1-c6402"))), new org.apache.ambari.server.controller.internal.RequestOperationLevel(org.apache.ambari.server.controller.spi.Resource.Type.Service, "c1", "GANGLIA", null, null), new java.util.HashMap<>(), false);
        org.easymock.EasyMock.replay(hostRoleCommand, actionManager, configHelper);
        ambariManagementController.createAction(actionRequest, requestProperties);
        org.apache.ambari.server.actionmanager.Request request = requestCapture.getValue();
        junit.framework.Assert.assertNotNull(request);
        junit.framework.Assert.assertNotNull(request.getStages());
        junit.framework.Assert.assertEquals(1, request.getStages().size());
        org.apache.ambari.server.actionmanager.Stage stage = request.getStages().iterator().next();
        junit.framework.Assert.assertEquals(1, stage.getHostRoleCommands().size());
    }

    @org.junit.Test
    public void testHostsFilterUnhealthyComponent() throws java.lang.Exception {
        clusters.getCluster("c1").getService("GANGLIA").getServiceComponent("GANGLIA_MONITOR").getServiceComponentHost("c1-c6402").setState(org.apache.ambari.server.state.State.UNKNOWN);
        java.util.Map<java.lang.String, java.lang.String> requestProperties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("context", "Restart all components for GANGLIA");
                put("operation_level/level", "SERVICE");
                put("operation_level/service_name", "GANGLIA");
                put("operation_level/cluster_name", "c1");
            }
        };
        org.apache.ambari.server.controller.ExecuteActionRequest actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest("c1", "RESTART", null, java.util.Arrays.asList(new org.apache.ambari.server.controller.internal.RequestResourceFilter("GANGLIA", "GANGLIA_SERVER", java.util.Collections.singletonList("c1-c6401")), new org.apache.ambari.server.controller.internal.RequestResourceFilter("GANGLIA", "GANGLIA_MONITOR", java.util.Collections.singletonList("c1-c6401")), new org.apache.ambari.server.controller.internal.RequestResourceFilter("GANGLIA", "GANGLIA_MONITOR", java.util.Collections.singletonList("c1-c6402"))), new org.apache.ambari.server.controller.internal.RequestOperationLevel(org.apache.ambari.server.controller.spi.Resource.Type.Host, "c1", "GANGLIA", null, null), new java.util.HashMap<>(), false);
        org.easymock.EasyMock.replay(hostRoleCommand, actionManager, configHelper);
        ambariManagementController.createAction(actionRequest, requestProperties);
        org.apache.ambari.server.actionmanager.Request request = requestCapture.getValue();
        junit.framework.Assert.assertNotNull(request);
        junit.framework.Assert.assertNotNull(request.getStages());
        junit.framework.Assert.assertEquals(1, request.getStages().size());
        org.apache.ambari.server.actionmanager.Stage stage = request.getStages().iterator().next();
        junit.framework.Assert.assertEquals(1, stage.getHostRoleCommands().size());
    }

    @org.junit.Test(expected = org.apache.ambari.server.AmbariException.class)
    public void testNoCandidateHostThrowsException() throws java.lang.Exception {
        long clusterId = clusters.getCluster("c1").getClusterId();
        clusters.getHost("c6402").setMaintenanceState(clusterId, org.apache.ambari.server.state.MaintenanceState.ON);
        injector.getInstance(org.apache.ambari.server.metadata.ActionMetadata.class).addServiceCheckAction("ZOOKEEPER");
        java.util.Map<java.lang.String, java.lang.String> requestProperties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("context", "Service Check ZooKeeper");
                put("operation_level/level", "SERVICE");
                put("operation_level/service_name", "ZOOKEEPER");
                put("operation_level/cluster_name", "c1");
            }
        };
        org.apache.ambari.server.controller.ExecuteActionRequest actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest("c1", "ZOOKEEPER_QUORUM_SERVICE_CHECK", null, java.util.Collections.singletonList(new org.apache.ambari.server.controller.internal.RequestResourceFilter("ZOOKEEPER", "ZOOKEEPER_CLIENT", java.util.Collections.singletonList("c6402"))), new org.apache.ambari.server.controller.internal.RequestOperationLevel(org.apache.ambari.server.controller.spi.Resource.Type.Service, "c1", "ZOOKEEPER", null, null), new java.util.HashMap<>(), false);
        org.easymock.EasyMock.replay(hostRoleCommand, actionManager, configHelper);
        ambariManagementController.createAction(actionRequest, requestProperties);
        junit.framework.Assert.fail("Expected an exception since there are no hosts which can run the ZK service check");
    }

    @org.junit.Test(expected = org.apache.ambari.server.AmbariException.class)
    public void testServiceCheckMaintenanceModeWithMissingComponentName() throws java.lang.Exception {
        long clusterId = clusters.getCluster("c1").getClusterId();
        clusters.getHost("c6402").setMaintenanceState(clusterId, org.apache.ambari.server.state.MaintenanceState.ON);
        injector.getInstance(org.apache.ambari.server.metadata.ActionMetadata.class).addServiceCheckAction("ZOOKEEPER");
        java.util.Map<java.lang.String, java.lang.String> requestProperties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("context", "Service Check ZooKeeper");
                put("operation_level/level", null);
                put("operation_level/service_name", "ZOOKEEPER");
                put("operation_level/cluster_name", "c1");
            }
        };
        org.apache.ambari.server.controller.ExecuteActionRequest actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest("c1", "ZOOKEEPER_QUORUM_SERVICE_CHECK", null, java.util.Collections.singletonList(new org.apache.ambari.server.controller.internal.RequestResourceFilter("ZOOKEEPER", null, java.util.Collections.singletonList("c6402"))), new org.apache.ambari.server.controller.internal.RequestOperationLevel(org.apache.ambari.server.controller.spi.Resource.Type.Service, "c1", "ZOOKEEPER", null, null), new java.util.HashMap<>(), false);
        org.easymock.EasyMock.replay(hostRoleCommand, actionManager, configHelper);
        ambariManagementController.createAction(actionRequest, requestProperties);
        junit.framework.Assert.fail("Expected an exception since there are no hosts which can run the ZK service check");
    }

    @org.junit.Test(expected = org.apache.ambari.server.AmbariException.class)
    public void testServiceCheckComponentWithEmptyHosts() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper ambariCustomCommandExecutionHelper = injector.getInstance(org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.class);
        java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> requestResourceFilter = new java.util.ArrayList<org.apache.ambari.server.controller.internal.RequestResourceFilter>() {
            {
                add(new org.apache.ambari.server.controller.internal.RequestResourceFilter("FLUME", null, null));
            }
        };
        org.apache.ambari.server.controller.ActionExecutionContext actionExecutionContext = new org.apache.ambari.server.controller.ActionExecutionContext("c1", "SERVICE_CHECK", requestResourceFilter);
        org.apache.ambari.server.actionmanager.Stage stage = org.easymock.EasyMock.niceMock(org.apache.ambari.server.actionmanager.Stage.class);
        ambariCustomCommandExecutionHelper.addExecutionCommandsToStage(actionExecutionContext, stage, new java.util.HashMap<>(), null);
        junit.framework.Assert.fail("Expected an exception since there are no hosts which can run the Flume service check");
    }

    @org.junit.Test
    public void testServiceCheckWithOverriddenTimeout() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper ambariCustomCommandExecutionHelper = injector.getInstance(org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.class);
        org.apache.ambari.server.state.Cluster c1 = clusters.getCluster("c1");
        org.apache.ambari.server.state.Service s = c1.getService("ZOOKEEPER");
        org.apache.ambari.server.state.ServiceComponent sc = s.getServiceComponent("ZOOKEEPER_CLIENT");
        junit.framework.Assert.assertTrue(sc.getServiceComponentHosts().keySet().size() > 1);
        java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> requestResourceFilter = new java.util.ArrayList<org.apache.ambari.server.controller.internal.RequestResourceFilter>() {
            {
                add(new org.apache.ambari.server.controller.internal.RequestResourceFilter("ZOOKEEPER", "ZOOKEEPER_CLIENT", java.util.Arrays.asList(new java.lang.String[]{ "c1-c6401" })));
            }
        };
        org.apache.ambari.server.controller.ActionExecutionContext actionExecutionContext = new org.apache.ambari.server.controller.ActionExecutionContext("c1", "SERVICE_CHECK", requestResourceFilter);
        org.apache.ambari.server.actionmanager.Stage stage = org.easymock.EasyMock.niceMock(org.apache.ambari.server.actionmanager.Stage.class);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapper execCmdWrapper = org.easymock.EasyMock.niceMock(org.apache.ambari.server.actionmanager.ExecutionCommandWrapper.class);
        org.apache.ambari.server.agent.ExecutionCommand execCmd = org.easymock.EasyMock.niceMock(org.apache.ambari.server.agent.ExecutionCommand.class);
        org.easymock.Capture<java.util.Map<java.lang.String, java.lang.String>> timeOutCapture = org.easymock.EasyMock.newCapture();
        org.easymock.EasyMock.expect(stage.getClusterName()).andReturn("c1");
        org.easymock.EasyMock.expect(stage.getExecutionCommandWrapper(org.easymock.EasyMock.eq("c1-c6401"), org.easymock.EasyMock.anyString())).andReturn(execCmdWrapper);
        org.easymock.EasyMock.expect(execCmdWrapper.getExecutionCommand()).andReturn(execCmd);
        execCmd.setCommandParams(org.easymock.EasyMock.capture(timeOutCapture));
        org.easymock.EasyMock.expectLastCall();
        java.util.HashSet<java.lang.String> localComponents = new java.util.HashSet<>();
        org.easymock.EasyMock.expect(execCmd.getLocalComponents()).andReturn(localComponents).anyTimes();
        org.easymock.EasyMock.replay(configHelper, stage, execCmdWrapper, execCmd);
        ambariCustomCommandExecutionHelper.addExecutionCommandsToStage(actionExecutionContext, stage, new java.util.HashMap<>(), null);
        java.util.Map<java.lang.String, java.lang.String> configMap = timeOutCapture.getValues().get(0);
        for (java.util.Map.Entry<java.lang.String, java.lang.String> config : configMap.entrySet()) {
            if (config.getKey().equals(org.apache.ambari.server.agent.ExecutionCommand.KeyNames.COMMAND_TIMEOUT)) {
                junit.framework.Assert.assertEquals("Service check timeout should be equal to populated in configs", org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelperTest.OVERRIDDEN_SERVICE_CHECK_TIMEOUT_VALUE, config.getValue());
                return;
            }
        }
        junit.framework.Assert.fail("Expected \"command_timeout\" config not found in execution command configs");
    }

    @org.junit.Test
    public void testServiceCheckPicksRandomHost() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper ambariCustomCommandExecutionHelper = injector.getInstance(org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.class);
        org.apache.ambari.server.state.Cluster c1 = clusters.getCluster("c1");
        org.apache.ambari.server.state.Service s = c1.getService("ZOOKEEPER");
        org.apache.ambari.server.state.ServiceComponent sc = s.getServiceComponent("ZOOKEEPER_CLIENT");
        junit.framework.Assert.assertTrue(sc.getServiceComponentHosts().keySet().size() > 1);
        java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> requestResourceFilter = new java.util.ArrayList<org.apache.ambari.server.controller.internal.RequestResourceFilter>() {
            {
                add(new org.apache.ambari.server.controller.internal.RequestResourceFilter("ZOOKEEPER", "ZOOKEEPER_CLIENT", null));
            }
        };
        org.apache.ambari.server.controller.ActionExecutionContext actionExecutionContext = new org.apache.ambari.server.controller.ActionExecutionContext("c1", "SERVICE_CHECK", requestResourceFilter);
        org.apache.ambari.server.actionmanager.Stage stage = org.easymock.EasyMock.niceMock(org.apache.ambari.server.actionmanager.Stage.class);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapper execCmdWrapper = org.easymock.EasyMock.niceMock(org.apache.ambari.server.actionmanager.ExecutionCommandWrapper.class);
        org.apache.ambari.server.agent.ExecutionCommand execCmd = org.easymock.EasyMock.niceMock(org.apache.ambari.server.agent.ExecutionCommand.class);
        org.easymock.EasyMock.expect(stage.getClusterName()).andReturn("c1");
        org.easymock.EasyMock.expect(stage.getExecutionCommandWrapper(org.easymock.EasyMock.eq("c1-c6401"), org.easymock.EasyMock.anyString())).andReturn(execCmdWrapper);
        org.easymock.EasyMock.expect(stage.getExecutionCommandWrapper(org.easymock.EasyMock.eq("c1-c6402"), org.easymock.EasyMock.anyString())).andReturn(execCmdWrapper);
        org.easymock.EasyMock.expect(execCmdWrapper.getExecutionCommand()).andReturn(execCmd);
        java.util.HashSet<java.lang.String> localComponents = new java.util.HashSet<>();
        org.easymock.EasyMock.expect(execCmd.getLocalComponents()).andReturn(localComponents).anyTimes();
        org.easymock.EasyMock.replay(configHelper, stage, execCmdWrapper, execCmd);
        ambariCustomCommandExecutionHelper.addExecutionCommandsToStage(actionExecutionContext, stage, new java.util.HashMap<>(), null);
    }

    @org.junit.Test
    public void testIsTopologyRefreshRequired() throws java.lang.Exception {
        org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper helper = injector.getInstance(org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.class);
        org.easymock.EasyMock.expect(configHelper.getHostActualConfigs(org.easymock.EasyMock.anyLong())).andReturn(new org.apache.ambari.server.events.AgentConfigsUpdateEvent(null, new java.util.TreeMap<>())).anyTimes();
        org.easymock.EasyMock.replay(configHelper);
        createClusterFixture("c2", new org.apache.ambari.server.state.StackId("HDP-2.1.1"), "2.1.1.0-1234", "c2");
        org.easymock.EasyMock.verify(configHelper);
        junit.framework.Assert.assertTrue(helper.isTopologyRefreshRequired("START", "c2", "HDFS"));
        junit.framework.Assert.assertTrue(helper.isTopologyRefreshRequired("RESTART", "c2", "HDFS"));
        junit.framework.Assert.assertFalse(helper.isTopologyRefreshRequired("STOP", "c2", "HDFS"));
    }

    @org.junit.Test
    public void testAvailableServicesMapContainsVersions() throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.String> requestProperties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put(org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelperTest.REQUEST_CONTEXT_PROPERTY, "Refresh YARN Capacity Scheduler");
                put("command", "REFRESHQUEUES");
            }
        };
        org.apache.ambari.server.controller.ExecuteActionRequest actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest("c1", "REFRESHQUEUES", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("forceRefreshConfigTags", "capacity-scheduler");
            }
        }, false);
        actionRequest.getResourceFilters().add(new org.apache.ambari.server.controller.internal.RequestResourceFilter("YARN", "RESOURCEMANAGER", java.util.Collections.singletonList("c1-c6401")));
        org.easymock.EasyMock.replay(hostRoleCommand, actionManager, configHelper);
        ambariManagementController.createAction(actionRequest, requestProperties);
        org.apache.ambari.server.actionmanager.Request request = requestCapture.getValue();
        org.apache.ambari.server.actionmanager.Stage stage = request.getStages().iterator().next();
        java.util.List<org.apache.ambari.server.actionmanager.ExecutionCommandWrapper> commands = stage.getExecutionCommands("c1-c6401");
        org.apache.ambari.server.agent.ExecutionCommand command = commands.get(0).getExecutionCommand();
        junit.framework.Assert.assertFalse(org.apache.commons.collections.MapUtils.isEmpty(command.getComponentVersionMap()));
        junit.framework.Assert.assertEquals(1, command.getComponentVersionMap().size());
        junit.framework.Assert.assertTrue(command.getComponentVersionMap().containsKey("ZOOKEEPER"));
        junit.framework.Assert.assertNull(command.getRepositoryFile());
    }

    @org.junit.Test
    public void testAvailableServicesMapIsEmptyWhenRepositoriesNotResolved() throws java.lang.Exception {
        org.apache.ambari.server.orm.dao.RepositoryVersionDAO repositoryVersionDAO = injector.getInstance(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        java.util.List<org.apache.ambari.server.orm.entities.RepositoryVersionEntity> repoVersions = repositoryVersionDAO.findAll();
        for (org.apache.ambari.server.orm.entities.RepositoryVersionEntity repoVersion : repoVersions) {
            repoVersion.setResolved(false);
            repositoryVersionDAO.merge(repoVersion);
        }
        java.util.Map<java.lang.String, java.lang.String> requestProperties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put(org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelperTest.REQUEST_CONTEXT_PROPERTY, "Refresh YARN Capacity Scheduler");
                put("command", "REFRESHQUEUES");
            }
        };
        org.apache.ambari.server.controller.ExecuteActionRequest actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest("c1", "REFRESHQUEUES", new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put("forceRefreshConfigTags", "capacity-scheduler");
            }
        }, false);
        actionRequest.getResourceFilters().add(new org.apache.ambari.server.controller.internal.RequestResourceFilter("YARN", "RESOURCEMANAGER", java.util.Collections.singletonList("c1-c6401")));
        org.easymock.EasyMock.replay(hostRoleCommand, actionManager, configHelper);
        ambariManagementController.createAction(actionRequest, requestProperties);
        org.apache.ambari.server.actionmanager.Request request = requestCapture.getValue();
        org.apache.ambari.server.actionmanager.Stage stage = request.getStages().iterator().next();
        java.util.List<org.apache.ambari.server.actionmanager.ExecutionCommandWrapper> commands = stage.getExecutionCommands("c1-c6401");
        org.apache.ambari.server.agent.ExecutionCommand command = commands.get(0).getExecutionCommand();
        junit.framework.Assert.assertTrue(org.apache.commons.collections.MapUtils.isEmpty(command.getComponentVersionMap()));
        junit.framework.Assert.assertNull(command.getRepositoryFile());
    }

    @org.junit.Test
    public void testCommandRepository() throws java.lang.Exception {
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster("c1");
        org.apache.ambari.server.state.Service serviceYARN = cluster.getService("YARN");
        org.apache.ambari.server.state.Service serviceZK = cluster.getService("ZOOKEEPER");
        org.apache.ambari.server.state.ServiceComponent componentRM = serviceYARN.getServiceComponent("RESOURCEMANAGER");
        org.apache.ambari.server.state.ServiceComponent componentZKC = serviceZK.getServiceComponent("ZOOKEEPER_CLIENT");
        org.apache.ambari.server.state.Host host = clusters.getHost("c1-c6401");
        org.apache.ambari.server.orm.dao.StackDAO stackDAO = injector.getInstance(org.apache.ambari.server.orm.dao.StackDAO.class);
        org.apache.ambari.server.orm.dao.RepositoryVersionDAO repoVersionDAO = injector.getInstance(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class);
        org.apache.ambari.server.orm.dao.ServiceComponentDesiredStateDAO componentDAO = injector.getInstance(org.apache.ambari.server.orm.dao.ServiceComponentDesiredStateDAO.class);
        org.apache.ambari.server.stack.upgrade.RepositoryVersionHelper repoVersionHelper = injector.getInstance(org.apache.ambari.server.stack.upgrade.RepositoryVersionHelper.class);
        org.apache.ambari.server.agent.CommandRepository commandRepo = repoVersionHelper.getCommandRepository(cluster, componentRM, host);
        junit.framework.Assert.assertEquals(2, commandRepo.getRepositories().size());
        java.util.List<org.apache.ambari.server.orm.entities.RepoOsEntity> operatingSystems = new java.util.ArrayList<>();
        org.apache.ambari.server.orm.entities.RepoDefinitionEntity repoDefinitionEntity1 = new org.apache.ambari.server.orm.entities.RepoDefinitionEntity();
        repoDefinitionEntity1.setRepoID("new-id");
        repoDefinitionEntity1.setBaseUrl("http://foo");
        repoDefinitionEntity1.setRepoName("HDP");
        org.apache.ambari.server.orm.entities.RepoOsEntity repoOsEntity = new org.apache.ambari.server.orm.entities.RepoOsEntity();
        repoOsEntity.setFamily("redhat6");
        repoOsEntity.setAmbariManaged(true);
        repoOsEntity.addRepoDefinition(repoDefinitionEntity1);
        operatingSystems.add(repoOsEntity);
        org.apache.ambari.server.orm.entities.StackEntity stackEntity = stackDAO.find(cluster.getDesiredStackVersion().getStackName(), cluster.getDesiredStackVersion().getStackVersion());
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = new org.apache.ambari.server.orm.entities.RepositoryVersionEntity(stackEntity, "2.1.1.1-1234", "2.1.1.1-1234", operatingSystems);
        repositoryVersion = repoVersionDAO.merge(repositoryVersion);
        org.apache.ambari.server.orm.entities.ServiceComponentDesiredStateEntity componentEntity = componentDAO.findByName(cluster.getClusterId(), serviceYARN.getName(), componentRM.getName());
        org.apache.ambari.server.orm.entities.ServiceComponentVersionEntity componentVersionEntity = new org.apache.ambari.server.orm.entities.ServiceComponentVersionEntity();
        componentVersionEntity.setRepositoryVersion(repositoryVersion);
        componentVersionEntity.setUserName("admin");
        componentEntity.setDesiredRepositoryVersion(repositoryVersion);
        componentEntity.addVersion(componentVersionEntity);
        componentDAO.merge(componentEntity);
        commandRepo = repoVersionHelper.getCommandRepository(cluster, componentRM, host);
        junit.framework.Assert.assertEquals(1, commandRepo.getRepositories().size());
        org.apache.ambari.server.agent.CommandRepository.Repository repo = commandRepo.getRepositories().iterator().next();
        junit.framework.Assert.assertEquals("http://foo", repo.getBaseUrl());
        commandRepo = repoVersionHelper.getCommandRepository(cluster, componentZKC, host);
        junit.framework.Assert.assertEquals(2, commandRepo.getRepositories().size());
    }

    @org.junit.Test
    public void testFutureCommandsPassValidation() throws java.lang.Exception {
        java.lang.String serviceName = "YARN";
        java.lang.String componentName = "YARN_FOO";
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster("c1");
        org.apache.ambari.server.state.Service service = cluster.getService(serviceName);
        try {
            service.getServiceComponent(componentName);
            junit.framework.Assert.fail("For this test to work, the YARN_FOO component must not be in the cluster yet");
        } catch (org.apache.ambari.server.AmbariException ambariException) {
        }
        org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper ambariCustomCommandExecutionHelper = injector.getInstance(org.apache.ambari.server.controller.AmbariCustomCommandExecutionHelper.class);
        java.util.List<org.apache.ambari.server.controller.internal.RequestResourceFilter> requestResourceFilter = new java.util.ArrayList<org.apache.ambari.server.controller.internal.RequestResourceFilter>() {
            {
                add(new org.apache.ambari.server.controller.internal.RequestResourceFilter(serviceName, componentName, java.util.Collections.singletonList("c1-c6401")));
            }
        };
        org.apache.ambari.server.controller.ActionExecutionContext actionExecutionContext = new org.apache.ambari.server.controller.ActionExecutionContext("c1", "RESTART", requestResourceFilter);
        actionExecutionContext.setIsFutureCommand(true);
        org.apache.ambari.server.actionmanager.Stage stage = org.easymock.EasyMock.niceMock(org.apache.ambari.server.actionmanager.Stage.class);
        org.apache.ambari.server.actionmanager.ExecutionCommandWrapper execCmdWrapper = org.easymock.EasyMock.niceMock(org.apache.ambari.server.actionmanager.ExecutionCommandWrapper.class);
        org.apache.ambari.server.agent.ExecutionCommand execCmd = org.easymock.EasyMock.niceMock(org.apache.ambari.server.agent.ExecutionCommand.class);
        org.easymock.EasyMock.expect(stage.getClusterName()).andReturn("c1");
        org.easymock.EasyMock.expect(stage.getExecutionCommandWrapper(org.easymock.EasyMock.eq("c1-c6401"), org.easymock.EasyMock.anyString())).andReturn(execCmdWrapper);
        org.easymock.EasyMock.expect(execCmdWrapper.getExecutionCommand()).andReturn(execCmd);
        org.easymock.EasyMock.expectLastCall();
        java.util.HashSet<java.lang.String> localComponents = new java.util.HashSet<>();
        org.easymock.EasyMock.expect(execCmd.getLocalComponents()).andReturn(localComponents).anyTimes();
        org.easymock.EasyMock.replay(configHelper, stage, execCmdWrapper, execCmd);
        ambariCustomCommandExecutionHelper.addExecutionCommandsToStage(actionExecutionContext, stage, new java.util.HashMap<>(), null);
        org.easymock.EasyMock.verify(configHelper, stage, execCmdWrapper, execCmd);
    }

    private void createClusterFixture(java.lang.String clusterName, org.apache.ambari.server.state.StackId stackId, java.lang.String respositoryVersion, java.lang.String hostPrefix) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException, java.lang.NoSuchFieldException, java.lang.IllegalAccessException {
        java.lang.String hostC6401 = hostPrefix + "-c6401";
        java.lang.String hostC6402 = hostPrefix + "-c6402";
        org.apache.ambari.server.orm.OrmTestHelper ormTestHelper = injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
        org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion = ormTestHelper.getOrCreateRepositoryVersion(stackId, respositoryVersion);
        createCluster(clusterName, stackId.getStackId());
        addHost(hostC6401, clusterName);
        addHost(hostC6402, clusterName);
        clusters.updateHostMappings(clusters.getHost(hostC6401));
        clusters.updateHostMappings(clusters.getHost(hostC6402));
        org.apache.ambari.server.state.Cluster cluster = clusters.getCluster(clusterName);
        junit.framework.Assert.assertNotNull(cluster);
        createService(clusterName, "YARN", repositoryVersion);
        createService(clusterName, "GANGLIA", repositoryVersion);
        createService(clusterName, "ZOOKEEPER", repositoryVersion);
        createService(clusterName, "FLUME", repositoryVersion);
        createServiceComponent(clusterName, "YARN", "RESOURCEMANAGER", org.apache.ambari.server.state.State.INIT);
        createServiceComponent(clusterName, "YARN", "NODEMANAGER", org.apache.ambari.server.state.State.INIT);
        createServiceComponent(clusterName, "GANGLIA", "GANGLIA_SERVER", org.apache.ambari.server.state.State.INIT);
        createServiceComponent(clusterName, "GANGLIA", "GANGLIA_MONITOR", org.apache.ambari.server.state.State.INIT);
        createServiceComponent(clusterName, "ZOOKEEPER", "ZOOKEEPER_CLIENT", org.apache.ambari.server.state.State.INIT);
        createServiceComponent(clusterName, "FLUME", "FLUME_HANDLER", org.apache.ambari.server.state.State.INIT);
        createServiceComponentHost(clusterName, "YARN", "RESOURCEMANAGER", hostC6401, null);
        createServiceComponentHost(clusterName, "YARN", "NODEMANAGER", hostC6401, null);
        createServiceComponentHost(clusterName, "GANGLIA", "GANGLIA_SERVER", hostC6401, org.apache.ambari.server.state.State.INIT);
        createServiceComponentHost(clusterName, "GANGLIA", "GANGLIA_MONITOR", hostC6401, org.apache.ambari.server.state.State.INIT);
        createServiceComponentHost(clusterName, "ZOOKEEPER", "ZOOKEEPER_CLIENT", hostC6401, org.apache.ambari.server.state.State.INIT);
        createServiceComponentHost(clusterName, "YARN", "NODEMANAGER", hostC6402, null);
        createServiceComponentHost(clusterName, "GANGLIA", "GANGLIA_MONITOR", hostC6402, org.apache.ambari.server.state.State.INIT);
        createServiceComponentHost(clusterName, "ZOOKEEPER", "ZOOKEEPER_CLIENT", hostC6402, org.apache.ambari.server.state.State.INIT);
    }

    private void addHost(java.lang.String hostname, java.lang.String clusterName) throws org.apache.ambari.server.AmbariException {
        clusters.addHost(hostname);
        setOsFamily(clusters.getHost(hostname), "redhat", "6.3");
        clusters.getHost(hostname).setState(org.apache.ambari.server.state.HostState.HEALTHY);
        if (null != clusterName) {
            clusters.mapHostToCluster(hostname, clusterName);
        }
    }

    private void setOsFamily(org.apache.ambari.server.state.Host host, java.lang.String osFamily, java.lang.String osVersion) {
        java.util.Map<java.lang.String, java.lang.String> hostAttributes = new java.util.HashMap<>();
        hostAttributes.put("os_family", osFamily);
        hostAttributes.put("os_release_version", osVersion);
        host.setHostAttributes(hostAttributes);
    }

    private void createCluster(java.lang.String clusterName, java.lang.String stackVersion) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        org.apache.ambari.server.controller.ClusterRequest r = new org.apache.ambari.server.controller.ClusterRequest(null, clusterName, org.apache.ambari.server.state.State.INSTALLED.name(), org.apache.ambari.server.state.SecurityType.NONE, stackVersion, null);
        ambariManagementController.createCluster(r);
    }

    private void createService(java.lang.String clusterName, java.lang.String serviceName, org.apache.ambari.server.orm.entities.RepositoryVersionEntity repositoryVersion) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException, java.lang.NoSuchFieldException, java.lang.IllegalAccessException {
        org.apache.ambari.server.controller.ServiceRequest r1 = new org.apache.ambari.server.controller.ServiceRequest(clusterName, serviceName, repositoryVersion.getId(), null, "false");
        java.util.Set<org.apache.ambari.server.controller.ServiceRequest> requests = new java.util.HashSet<>();
        requests.add(r1);
        org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.createServices(ambariManagementController, injector.getInstance(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class), requests);
    }

    private void createServiceComponent(java.lang.String clusterName, java.lang.String serviceName, java.lang.String componentName, org.apache.ambari.server.state.State desiredState) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String dStateStr = null;
        if (desiredState != null) {
            dStateStr = desiredState.toString();
        }
        org.apache.ambari.server.controller.ServiceComponentRequest r = new org.apache.ambari.server.controller.ServiceComponentRequest(clusterName, serviceName, componentName, dStateStr);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentRequest> requests = new java.util.HashSet<>();
        requests.add(r);
        org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.createComponents(ambariManagementController, requests);
    }

    private void createServiceComponentHost(java.lang.String clusterName, java.lang.String serviceName, java.lang.String componentName, java.lang.String hostname, org.apache.ambari.server.state.State desiredState) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String dStateStr = null;
        if (desiredState != null) {
            dStateStr = desiredState.toString();
        }
        org.apache.ambari.server.controller.ServiceComponentHostRequest r = new org.apache.ambari.server.controller.ServiceComponentHostRequest(clusterName, serviceName, componentName, hostname, dStateStr);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> requests = new java.util.HashSet<>();
        requests.add(r);
        ambariManagementController.createHostComponents(requests);
    }
}