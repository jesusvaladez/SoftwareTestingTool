package org.apache.ambari.server.serveraction.upgrades;
import org.easymock.EasyMockSupport;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
@org.junit.runner.RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@org.powermock.core.classloader.annotations.PrepareForTest({ org.apache.ambari.server.serveraction.upgrades.AddComponentAction.class, org.apache.ambari.server.stack.MasterHostResolver.class })
public class AddComponentActionTest extends org.easymock.EasyMockSupport {
    private static final java.lang.String CANDIDATE_SERVICE = "FOO-SERVICE";

    private static final java.lang.String CANDIDATE_COMPONENT = "FOO-COMPONENT";

    private static final java.lang.String NEW_SERVICE = org.apache.ambari.server.serveraction.upgrades.AddComponentActionTest.CANDIDATE_SERVICE;

    private static final java.lang.String NEW_COMPONENT = "FOO-NEW-COMPONENT";

    private static final java.lang.String CLUSTER_NAME = "c1";

    private final java.util.Map<java.lang.String, java.lang.String> m_commandParams = new java.util.HashMap<>();

    private final org.apache.ambari.server.state.Clusters m_mockClusters = createNiceMock(org.apache.ambari.server.state.Clusters.class);

    private final org.apache.ambari.server.state.Cluster m_mockCluster = createNiceMock(org.apache.ambari.server.state.Cluster.class);

    private final org.apache.ambari.server.state.Service m_mockCandidateService = createNiceMock(org.apache.ambari.server.state.Service.class);

    private final org.apache.ambari.server.state.ServiceComponent m_mockCandidateServiceComponent = createNiceMock(org.apache.ambari.server.state.ServiceComponent.class);

    private final org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext m_mockUpgradeContext = createNiceMock(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext.class);

    private final java.lang.String CANDIDATE_HOST_NAME = "c6401.ambari.apache.org";

    private final org.apache.ambari.server.state.Host m_mockHost = createStrictMock(org.apache.ambari.server.state.Host.class);

    private final java.util.Collection<org.apache.ambari.server.state.Host> m_candidateHosts = com.google.common.collect.Lists.newArrayList(m_mockHost);

    private org.apache.ambari.server.serveraction.upgrades.AddComponentAction m_action;

    @org.junit.Before
    public void before() throws java.lang.Exception {
        org.powermock.api.easymock.PowerMock.mockStatic(org.apache.ambari.server.stack.MasterHostResolver.class);
        EasyMock.expect(org.apache.ambari.server.stack.MasterHostResolver.getCandidateHosts(m_mockCluster, org.apache.ambari.server.stack.upgrade.ExecuteHostType.ALL, org.apache.ambari.server.serveraction.upgrades.AddComponentActionTest.CANDIDATE_SERVICE, org.apache.ambari.server.serveraction.upgrades.AddComponentActionTest.CANDIDATE_COMPONENT)).andReturn(m_candidateHosts).once();
        org.powermock.api.easymock.PowerMock.replay(org.apache.ambari.server.stack.MasterHostResolver.class);
        m_action = org.powermock.api.easymock.PowerMock.createNicePartialMock(org.apache.ambari.server.serveraction.upgrades.AddComponentAction.class, "getUpgradeContext", "createCommandReport", "getClusters", "getGson");
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = createNiceMock(org.apache.ambari.server.agent.ExecutionCommand.class);
        EasyMock.expect(executionCommand.getCommandParams()).andReturn(m_commandParams).once();
        m_action.setExecutionCommand(executionCommand);
        EasyMock.expect(m_action.getClusters()).andReturn(m_mockClusters).atLeastOnce();
        EasyMock.expect(m_action.getUpgradeContext(m_mockCluster)).andReturn(m_mockUpgradeContext).once();
        EasyMock.expect(m_action.getGson()).andReturn(new com.google.gson.Gson()).once();
        org.apache.ambari.server.stack.upgrade.AddComponentTask addComponentTask = new org.apache.ambari.server.stack.upgrade.AddComponentTask();
        addComponentTask.service = org.apache.ambari.server.serveraction.upgrades.AddComponentActionTest.NEW_SERVICE;
        addComponentTask.component = org.apache.ambari.server.serveraction.upgrades.AddComponentActionTest.NEW_COMPONENT;
        addComponentTask.hostService = org.apache.ambari.server.serveraction.upgrades.AddComponentActionTest.CANDIDATE_SERVICE;
        addComponentTask.hostComponent = org.apache.ambari.server.serveraction.upgrades.AddComponentActionTest.CANDIDATE_COMPONENT;
        addComponentTask.hosts = org.apache.ambari.server.stack.upgrade.ExecuteHostType.ALL;
        java.lang.String addComponentTaskJson = addComponentTask.toJson();
        m_commandParams.put("clusterName", org.apache.ambari.server.serveraction.upgrades.AddComponentActionTest.CLUSTER_NAME);
        m_commandParams.put(org.apache.ambari.server.stack.upgrade.AddComponentTask.PARAMETER_SERIALIZED_ADD_COMPONENT_TASK, addComponentTaskJson);
        EasyMock.expect(m_mockClusters.getCluster(org.apache.ambari.server.serveraction.upgrades.AddComponentActionTest.CLUSTER_NAME)).andReturn(m_mockCluster).once();
    }

    @org.junit.After
    public void after() throws java.lang.Exception {
        org.powermock.api.easymock.PowerMock.verify(m_action);
    }

    @org.junit.Test
    public void testAddComponentDuringUpgrade() throws java.lang.Exception {
        EasyMock.expect(m_mockCluster.getService(org.apache.ambari.server.serveraction.upgrades.AddComponentActionTest.NEW_SERVICE)).andReturn(m_mockCandidateService).once();
        EasyMock.expect(m_mockCandidateService.getServiceComponent(org.apache.ambari.server.serveraction.upgrades.AddComponentActionTest.NEW_COMPONENT)).andThrow(new org.apache.ambari.server.ServiceComponentNotFoundException(org.apache.ambari.server.serveraction.upgrades.AddComponentActionTest.CLUSTER_NAME, org.apache.ambari.server.serveraction.upgrades.AddComponentActionTest.NEW_SERVICE, org.apache.ambari.server.serveraction.upgrades.AddComponentActionTest.NEW_COMPONENT));
        EasyMock.expect(m_mockCandidateService.addServiceComponent(org.apache.ambari.server.serveraction.upgrades.AddComponentActionTest.NEW_COMPONENT)).andReturn(m_mockCandidateServiceComponent).once();
        EasyMock.expect(m_mockHost.getHostName()).andReturn(CANDIDATE_HOST_NAME).atLeastOnce();
        m_mockCandidateServiceComponent.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        EasyMock.expectLastCall().once();
        java.util.Map<java.lang.String, org.apache.ambari.server.state.ServiceComponentHost> existingSCHs = new java.util.HashMap<>();
        EasyMock.expect(m_mockCandidateServiceComponent.getServiceComponentHosts()).andReturn(existingSCHs).once();
        org.apache.ambari.server.state.ServiceComponentHost mockServiceComponentHost = createNiceMock(org.apache.ambari.server.state.ServiceComponentHost.class);
        EasyMock.expect(m_mockCandidateServiceComponent.addServiceComponentHost(CANDIDATE_HOST_NAME)).andReturn(mockServiceComponentHost).once();
        mockServiceComponentHost.setState(org.apache.ambari.server.state.State.INSTALLED);
        EasyMock.expectLastCall().once();
        mockServiceComponentHost.setDesiredState(org.apache.ambari.server.state.State.INSTALLED);
        EasyMock.expectLastCall().once();
        mockServiceComponentHost.setVersion(org.apache.ambari.server.events.listeners.upgrade.StackVersionListener.UNKNOWN_VERSION);
        EasyMock.expectLastCall().once();
        org.powermock.api.easymock.PowerMock.replay(m_action);
        replayAll();
        m_action.execute(null);
        verifyAll();
    }

    @org.junit.Test
    public void testAddComponentDuringUpgradeFailsWithNoCandidates() throws java.lang.Exception {
        org.powermock.api.easymock.PowerMock.replay(m_action);
        replayAll();
        m_candidateHosts.clear();
        m_action.execute(null);
        verifyAll();
    }

    @org.junit.Test
    public void testAddComponentWhereServiceIsNotInstalled() throws java.lang.Exception {
        EasyMock.expect(m_mockCluster.getService(org.apache.ambari.server.serveraction.upgrades.AddComponentActionTest.NEW_SERVICE)).andThrow(new org.apache.ambari.server.ServiceNotFoundException(org.apache.ambari.server.serveraction.upgrades.AddComponentActionTest.CLUSTER_NAME, org.apache.ambari.server.serveraction.upgrades.AddComponentActionTest.CANDIDATE_SERVICE)).once();
        org.powermock.api.easymock.PowerMock.replay(m_action);
        replayAll();
        m_action.execute(null);
        verifyAll();
    }
}