package org.apache.ambari.server.controller;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.mockito.Matchers.any;
@org.junit.runner.RunWith(org.mockito.runners.MockitoJUnitRunner.class)
public class BackgroundCustomCommandExecutionTest {
    private com.google.inject.Injector injector;

    private org.apache.ambari.server.controller.AmbariManagementController controller;

    private org.apache.ambari.server.state.Clusters clusters;

    private static final java.lang.String REQUEST_CONTEXT_PROPERTY = "context";

    private static final java.lang.String UPDATE_REPLICATION_PARAMS = "{\n" + (("              \"replication_cluster_keys\": c7007.ambari.apache.org,c7008.ambari.apache.org,c7009.ambari.apache.org:2181:/hbase,\n" + "              \"replication_peers\": 1\n") + "            }");

    private static final java.lang.String STOP_REPLICATION_PARAMS = "{\n" + ("              \"replication_peers\": 1\n" + "            }");

    @org.mockito.Captor
    org.mockito.ArgumentCaptor<org.apache.ambari.server.actionmanager.Request> requestCapture;

    @org.mockito.Mock
    org.apache.ambari.server.actionmanager.ActionManager am;

    private final java.lang.String STACK_VERSION = "2.0.6";

    private final java.lang.String REPO_VERSION = "2.0.6-1234";

    private final org.apache.ambari.server.state.StackId STACK_ID = new org.apache.ambari.server.state.StackId("HDP", STACK_VERSION);

    private org.apache.ambari.server.orm.entities.RepositoryVersionEntity m_repositoryVersion;

    @org.junit.Before
    public void setup() throws java.lang.Exception {
        org.apache.ambari.server.configuration.Configuration configuration;
        org.apache.ambari.server.topology.TopologyManager topologyManager;
        org.apache.ambari.server.orm.InMemoryDefaultTestModule module = new org.apache.ambari.server.orm.InMemoryDefaultTestModule() {
            @java.lang.Override
            protected void configure() {
                getProperties().put(org.apache.ambari.server.configuration.Configuration.CUSTOM_ACTION_DEFINITION.getKey(), "src/main/resources/custom_action_definitions");
                super.configure();
                bind(org.apache.ambari.server.actionmanager.ActionManager.class).toInstance(am);
            }
        };
        injector = com.google.inject.Guice.createInjector(module);
        injector.getInstance(org.apache.ambari.server.orm.GuiceJpaInitializer.class);
        controller = injector.getInstance(org.apache.ambari.server.controller.AmbariManagementController.class);
        clusters = injector.getInstance(org.apache.ambari.server.state.Clusters.class);
        configuration = injector.getInstance(org.apache.ambari.server.configuration.Configuration.class);
        topologyManager = injector.getInstance(org.apache.ambari.server.topology.TopologyManager.class);
        org.apache.ambari.server.orm.OrmTestHelper ormTestHelper = injector.getInstance(org.apache.ambari.server.orm.OrmTestHelper.class);
        junit.framework.Assert.assertEquals("src/main/resources/custom_action_definitions", configuration.getCustomActionDefinitionPath());
        org.apache.ambari.server.utils.StageUtils.setTopologyManager(topologyManager);
        org.apache.ambari.server.utils.StageUtils.setConfiguration(configuration);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(org.apache.ambari.server.security.TestAuthenticationFactory.createAdministrator());
        m_repositoryVersion = ormTestHelper.getOrCreateRepositoryVersion(STACK_ID, REPO_VERSION);
        junit.framework.Assert.assertNotNull(m_repositoryVersion);
    }

    @org.junit.After
    public void teardown() throws org.apache.ambari.server.AmbariException, java.sql.SQLException {
        org.apache.ambari.server.H2DatabaseCleaner.clearDatabaseAndStopPersistenceService(injector);
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(null);
    }

    @java.lang.SuppressWarnings("serial")
    @org.junit.Test
    public void testRebalanceHdfsCustomCommand() {
        try {
            createClusterFixture();
            java.util.Map<java.lang.String, java.lang.String> requestProperties = new java.util.HashMap<java.lang.String, java.lang.String>() {
                {
                    put(org.apache.ambari.server.controller.BackgroundCustomCommandExecutionTest.REQUEST_CONTEXT_PROPERTY, "Refresh YARN Capacity Scheduler");
                    put("command", "REBALANCEHDFS");
                    put("namenode", "{\"threshold\":13}");
                }
            };
            org.apache.ambari.server.controller.ExecuteActionRequest actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest("c1", "REBALANCEHDFS", new java.util.HashMap<>(), false);
            actionRequest.getResourceFilters().add(new org.apache.ambari.server.controller.internal.RequestResourceFilter("HDFS", "NAMENODE", java.util.Collections.singletonList("c6401")));
            controller.createAction(actionRequest, requestProperties);
            org.mockito.Mockito.verify(am, org.mockito.Mockito.times(1)).sendActions(requestCapture.capture(), Matchers.any(org.apache.ambari.server.controller.ExecuteActionRequest.class));
            org.apache.ambari.server.actionmanager.Request request = requestCapture.getValue();
            junit.framework.Assert.assertNotNull(request);
            junit.framework.Assert.assertNotNull(request.getStages());
            junit.framework.Assert.assertEquals(1, request.getStages().size());
            org.apache.ambari.server.actionmanager.Stage stage = request.getStages().iterator().next();
            java.lang.System.out.println(stage);
            junit.framework.Assert.assertEquals(1, stage.getHosts().size());
            java.util.List<org.apache.ambari.server.actionmanager.ExecutionCommandWrapper> commands = stage.getExecutionCommands("c6401");
            junit.framework.Assert.assertEquals(1, commands.size());
            org.apache.ambari.server.agent.ExecutionCommand command = commands.get(0).getExecutionCommand();
            junit.framework.Assert.assertEquals(org.apache.ambari.server.agent.AgentCommand.AgentCommandType.BACKGROUND_EXECUTION_COMMAND, command.getCommandType());
            junit.framework.Assert.assertEquals("{\"threshold\":13}", command.getCommandParams().get("namenode"));
        } catch (java.lang.Exception e) {
            junit.framework.Assert.fail(e.getMessage());
        }
    }

    private void createClusterFixture() throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException, java.lang.NoSuchFieldException, java.lang.IllegalAccessException {
        createCluster("c1");
        addHost("c6401", "c1");
        addHost("c6402", "c1");
        addHost("c7007", "c1");
        clusters.updateHostMappings(clusters.getHost("c6401"));
        clusters.updateHostMappings(clusters.getHost("c6402"));
        clusters.updateHostMappings(clusters.getHost("c7007"));
        clusters.getCluster("c1");
        createService("c1", "HDFS", null);
        createService("c1", "HBASE", null);
        createServiceComponent("c1", "HDFS", "NAMENODE", org.apache.ambari.server.state.State.INIT);
        createServiceComponent("c1", "HBASE", "HBASE_MASTER", org.apache.ambari.server.state.State.INIT);
        createServiceComponentHost("c1", "HDFS", "NAMENODE", "c6401", null);
        createServiceComponentHost("c1", "HBASE", "HBASE_MASTER", "c7007", null);
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

    @java.lang.SuppressWarnings("serial")
    @org.junit.Test
    public void testUpdateHBaseReplicationCustomCommand() throws org.apache.ambari.server.security.authorization.AuthorizationException, org.apache.ambari.server.AmbariException, java.lang.IllegalAccessException, java.lang.NoSuchFieldException {
        createClusterFixture();
        java.util.Map<java.lang.String, java.lang.String> requestProperties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put(org.apache.ambari.server.controller.BackgroundCustomCommandExecutionTest.REQUEST_CONTEXT_PROPERTY, "Enable Cross Cluster HBase Replication");
                put("command", "UPDATE_REPLICATION");
                put("parameters", org.apache.ambari.server.controller.BackgroundCustomCommandExecutionTest.UPDATE_REPLICATION_PARAMS);
            }
        };
        org.apache.ambari.server.controller.ExecuteActionRequest actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest("c1", "UPDATE_REPLICATION", new java.util.HashMap<>(), false);
        actionRequest.getResourceFilters().add(new org.apache.ambari.server.controller.internal.RequestResourceFilter("HBASE", "HBASE_MASTER", java.util.Collections.singletonList("c7007")));
        controller.createAction(actionRequest, requestProperties);
        org.mockito.Mockito.verify(am, org.mockito.Mockito.times(1)).sendActions(requestCapture.capture(), Matchers.any(org.apache.ambari.server.controller.ExecuteActionRequest.class));
        org.apache.ambari.server.actionmanager.Request request = requestCapture.getValue();
        junit.framework.Assert.assertNotNull(request);
        junit.framework.Assert.assertNotNull(request.getStages());
        junit.framework.Assert.assertEquals(1, request.getStages().size());
        org.apache.ambari.server.actionmanager.Stage stage = request.getStages().iterator().next();
        junit.framework.Assert.assertEquals(1, stage.getHosts().size());
        java.util.List<org.apache.ambari.server.actionmanager.ExecutionCommandWrapper> commands = stage.getExecutionCommands("c7007");
        junit.framework.Assert.assertEquals(1, commands.size());
        org.apache.ambari.server.agent.ExecutionCommand command = commands.get(0).getExecutionCommand();
        junit.framework.Assert.assertEquals(org.apache.ambari.server.agent.AgentCommand.AgentCommandType.EXECUTION_COMMAND, command.getCommandType());
        junit.framework.Assert.assertEquals("UPDATE_REPLICATION", command.getCommandParams().get("custom_command"));
    }

    @java.lang.SuppressWarnings("serial")
    @org.junit.Test
    public void testStopHBaseReplicationCustomCommand() throws org.apache.ambari.server.security.authorization.AuthorizationException, org.apache.ambari.server.AmbariException, java.lang.IllegalAccessException, java.lang.NoSuchFieldException {
        createClusterFixture();
        java.util.Map<java.lang.String, java.lang.String> requestProperties = new java.util.HashMap<java.lang.String, java.lang.String>() {
            {
                put(org.apache.ambari.server.controller.BackgroundCustomCommandExecutionTest.REQUEST_CONTEXT_PROPERTY, "Disable Cross Cluster HBase Replication");
                put("command", "STOP_REPLICATION");
                put("parameters", org.apache.ambari.server.controller.BackgroundCustomCommandExecutionTest.STOP_REPLICATION_PARAMS);
            }
        };
        org.apache.ambari.server.controller.ExecuteActionRequest actionRequest = new org.apache.ambari.server.controller.ExecuteActionRequest("c1", "STOP_REPLICATION", new java.util.HashMap<>(), false);
        actionRequest.getResourceFilters().add(new org.apache.ambari.server.controller.internal.RequestResourceFilter("HBASE", "HBASE_MASTER", java.util.Collections.singletonList("c7007")));
        controller.createAction(actionRequest, requestProperties);
        org.mockito.Mockito.verify(am, org.mockito.Mockito.times(1)).sendActions(requestCapture.capture(), Matchers.any(org.apache.ambari.server.controller.ExecuteActionRequest.class));
        org.apache.ambari.server.actionmanager.Request request = requestCapture.getValue();
        junit.framework.Assert.assertNotNull(request);
        junit.framework.Assert.assertNotNull(request.getStages());
        junit.framework.Assert.assertEquals(1, request.getStages().size());
        org.apache.ambari.server.actionmanager.Stage stage = request.getStages().iterator().next();
        junit.framework.Assert.assertEquals(1, stage.getHosts().size());
        java.util.List<org.apache.ambari.server.actionmanager.ExecutionCommandWrapper> commands = stage.getExecutionCommands("c7007");
        junit.framework.Assert.assertEquals(1, commands.size());
        org.apache.ambari.server.agent.ExecutionCommand command = commands.get(0).getExecutionCommand();
        junit.framework.Assert.assertEquals(org.apache.ambari.server.agent.AgentCommand.AgentCommandType.EXECUTION_COMMAND, command.getCommandType());
        junit.framework.Assert.assertEquals("STOP_REPLICATION", command.getCommandParams().get("custom_command"));
    }

    private void createCluster(java.lang.String clusterName) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        org.apache.ambari.server.controller.ClusterRequest r = new org.apache.ambari.server.controller.ClusterRequest(null, clusterName, org.apache.ambari.server.state.State.INSTALLED.name(), org.apache.ambari.server.state.SecurityType.NONE, STACK_ID.getStackId(), null);
        controller.createCluster(r);
    }

    private void createService(java.lang.String clusterName, java.lang.String serviceName, org.apache.ambari.server.state.State desiredState) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException, java.lang.NoSuchFieldException, java.lang.IllegalAccessException {
        java.lang.String dStateStr = null;
        if (desiredState != null) {
            dStateStr = desiredState.toString();
        }
        org.apache.ambari.server.controller.ServiceRequest r1 = new org.apache.ambari.server.controller.ServiceRequest(clusterName, serviceName, m_repositoryVersion.getId(), dStateStr);
        java.util.Set<org.apache.ambari.server.controller.ServiceRequest> requests = new java.util.HashSet<>();
        requests.add(r1);
        org.apache.ambari.server.controller.internal.ServiceResourceProviderTest.createServices(controller, injector.getInstance(org.apache.ambari.server.orm.dao.RepositoryVersionDAO.class), requests);
    }

    private void createServiceComponent(java.lang.String clusterName, java.lang.String serviceName, java.lang.String componentName, org.apache.ambari.server.state.State desiredState) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String dStateStr = null;
        if (desiredState != null) {
            dStateStr = desiredState.toString();
        }
        org.apache.ambari.server.controller.ServiceComponentRequest r = new org.apache.ambari.server.controller.ServiceComponentRequest(clusterName, serviceName, componentName, dStateStr);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentRequest> requests = new java.util.HashSet<>();
        requests.add(r);
        org.apache.ambari.server.controller.internal.ComponentResourceProviderTest.createComponents(controller, requests);
    }

    private void createServiceComponentHost(java.lang.String clusterName, java.lang.String serviceName, java.lang.String componentName, java.lang.String hostname, org.apache.ambari.server.state.State desiredState) throws org.apache.ambari.server.AmbariException, org.apache.ambari.server.security.authorization.AuthorizationException {
        java.lang.String dStateStr = null;
        if (desiredState != null) {
            dStateStr = desiredState.toString();
        }
        org.apache.ambari.server.controller.ServiceComponentHostRequest r = new org.apache.ambari.server.controller.ServiceComponentHostRequest(clusterName, serviceName, componentName, hostname, dStateStr);
        java.util.Set<org.apache.ambari.server.controller.ServiceComponentHostRequest> requests = new java.util.HashSet<>();
        requests.add(r);
        controller.createHostComponents(requests);
    }
}