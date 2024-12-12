package org.apache.ambari.server.serveraction.upgrades;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
@org.junit.runner.RunWith(org.powermock.modules.junit4.PowerMockRunner.class)
@org.powermock.core.classloader.annotations.PrepareForTest({ org.apache.ambari.server.serveraction.upgrades.PluginUpgradeServerAction.class })
public class PluginUpgradeServerActionTest extends org.easymock.EasyMockSupport {
    private static final java.lang.String CLUSTER_NAME = "c1";

    private static final java.lang.String FOO_SITE = "foo-site";

    private static final java.lang.String AUTH_USERNAME = "admin";

    private static final java.lang.String CLASS_NAME = org.apache.ambari.server.serveraction.upgrades.PluginUpgradeServerActionTest.MockUpgradeAction.class.getName();

    private final java.util.Map<java.lang.String, java.lang.String> m_commandParams = new java.util.HashMap<>();

    private final org.apache.ambari.server.state.StackId m_stackId = new org.apache.ambari.server.state.StackId("FOO-STACK-1.0");

    private final org.apache.ambari.server.state.StackInfo m_mockStackInfo = createNiceMock(org.apache.ambari.server.state.StackInfo.class);

    private final org.apache.ambari.server.state.Clusters m_mockClusters = createNiceMock(org.apache.ambari.server.state.Clusters.class);

    private final org.apache.ambari.server.state.Cluster m_mockCluster = createNiceMock(org.apache.ambari.server.state.Cluster.class);

    private final org.apache.ambari.server.state.Config m_mockConfig = createNiceMock(org.apache.ambari.server.state.Config.class);

    private final org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext m_mockUpgradeContext = createNiceMock(org.apache.ambari.server.stack.upgrade.orchestrate.UpgradeContext.class);

    private final org.apache.ambari.server.stack.upgrade.UpgradePack m_mockUpgradePack = createNiceMock(org.apache.ambari.server.stack.upgrade.UpgradePack.class);

    private final java.net.URLClassLoader m_mockClassLoader = createNiceMock(java.net.URLClassLoader.class);

    private final org.apache.ambari.server.api.services.AmbariMetaInfo m_mockMetaInfo = createNiceMock(org.apache.ambari.server.api.services.AmbariMetaInfo.class);

    private final org.apache.ambari.server.controller.AmbariManagementController m_mockController = createNiceMock(org.apache.ambari.server.controller.AmbariManagementController.class);

    private final org.apache.ambari.spi.RepositoryVersion m_repositoryVersion = new org.apache.ambari.spi.RepositoryVersion(1L, "FOO-STACK", "1.0", "FOO-STACK-1.0", "1.0.0.0-b1", org.apache.ambari.spi.RepositoryType.STANDARD);

    private final org.apache.ambari.server.orm.entities.RepositoryVersionEntity m_mocRepoEntity = createNiceMock(org.apache.ambari.server.orm.entities.RepositoryVersionEntity.class);

    private final org.apache.ambari.server.agent.stomp.AgentConfigsHolder m_mockAgentConfigsHolder = createNiceMock(org.apache.ambari.server.agent.stomp.AgentConfigsHolder.class);

    private final org.apache.ambari.server.state.ConfigHelper m_mockConfigHelper = createNiceMock(org.apache.ambari.server.state.ConfigHelper.class);

    private org.apache.ambari.server.serveraction.upgrades.PluginUpgradeServerAction m_action;

    @org.junit.Before
    @java.lang.SuppressWarnings({ "rawtypes", "unchecked" })
    public void before() throws java.lang.Exception {
        m_action = org.powermock.api.easymock.PowerMock.createNicePartialMock(org.apache.ambari.server.serveraction.upgrades.PluginUpgradeServerAction.class, "getUpgradeContext", "createCommandReport", "getClusters");
        EasyMock.expect(m_mockCluster.getHosts()).andReturn(new java.util.ArrayList<>()).once();
        org.apache.ambari.server.agent.ExecutionCommand executionCommand = createNiceMock(org.apache.ambari.server.agent.ExecutionCommand.class);
        EasyMock.expect(executionCommand.getClusterName()).andReturn(org.apache.ambari.server.serveraction.upgrades.PluginUpgradeServerActionTest.CLUSTER_NAME).anyTimes();
        EasyMock.expect(executionCommand.getCommandParams()).andReturn(m_commandParams).once();
        m_action.setExecutionCommand(executionCommand);
        m_action.m_clusters = m_mockClusters;
        m_action.m_metainfoProvider = () -> m_mockMetaInfo;
        m_action.m_amc = m_mockController;
        m_action.m_configHelper = m_mockConfigHelper;
        EasyMock.expect(m_mockController.getAuthName()).andReturn(org.apache.ambari.server.serveraction.upgrades.PluginUpgradeServerActionTest.AUTH_USERNAME).anyTimes();
        EasyMock.expect(m_mocRepoEntity.getStackId()).andReturn(m_stackId).anyTimes();
        EasyMock.expect(m_mocRepoEntity.getVersion()).andReturn("1.0.0.0-b1").anyTimes();
        EasyMock.expect(m_mocRepoEntity.getRepositoryVersion()).andReturn(m_repositoryVersion).anyTimes();
        EasyMock.expect(m_mockUpgradeContext.getUpgradePack()).andReturn(m_mockUpgradePack).atLeastOnce();
        EasyMock.expect(m_mockUpgradeContext.getDirection()).andReturn(org.apache.ambari.server.stack.upgrade.Direction.UPGRADE).anyTimes();
        EasyMock.expect(m_mockUpgradeContext.getRepositoryVersion()).andReturn(m_mocRepoEntity).anyTimes();
        EasyMock.expect(m_mockUpgradePack.getOwnerStackId()).andReturn(m_stackId).atLeastOnce();
        EasyMock.expect(m_mockMetaInfo.getStack(m_stackId)).andReturn(m_mockStackInfo).atLeastOnce();
        EasyMock.expect(m_mockStackInfo.getLibraryClassLoader()).andReturn(m_mockClassLoader).atLeastOnce();
        EasyMock.expect(m_mockStackInfo.getLibraryInstance(org.easymock.EasyMock.anyString())).andReturn(new org.apache.ambari.server.serveraction.upgrades.PluginUpgradeServerActionTest.MockUpgradeAction()).atLeastOnce();
        EasyMock.expect(m_action.getClusters()).andReturn(m_mockClusters).anyTimes();
        EasyMock.expect(m_action.getUpgradeContext(m_mockCluster)).andReturn(m_mockUpgradeContext).once();
        m_action.agentConfigsHolder = m_mockAgentConfigsHolder;
        m_commandParams.put("clusterName", org.apache.ambari.server.serveraction.upgrades.PluginUpgradeServerActionTest.CLUSTER_NAME);
        m_commandParams.put(org.apache.ambari.server.serveraction.ServerAction.WRAPPED_CLASS_NAME, org.apache.ambari.server.serveraction.upgrades.PluginUpgradeServerActionTest.CLASS_NAME);
        EasyMock.expect(m_mockClusters.getCluster(org.apache.ambari.server.serveraction.upgrades.PluginUpgradeServerActionTest.CLUSTER_NAME)).andReturn(m_mockCluster).once();
    }

    @org.junit.After
    public void after() throws java.lang.Exception {
        org.powermock.api.easymock.PowerMock.verify(m_action);
    }

    @org.junit.Test
    public void testExecute() throws java.lang.Exception {
        EasyMock.expect(m_mockCluster.getDesiredConfigByType(org.apache.ambari.server.serveraction.upgrades.PluginUpgradeServerActionTest.FOO_SITE)).andReturn(m_mockConfig).once();
        java.util.Map<java.lang.String, java.lang.String> configUpdates = new java.util.HashMap<>();
        configUpdates.put("property-name", "property-value");
        m_mockConfig.updateProperties(configUpdates);
        EasyMock.expectLastCall().once();
        m_mockConfig.save();
        EasyMock.expectLastCall().once();
        org.powermock.api.easymock.PowerMock.replay(m_action);
        replayAll();
        m_action.execute(null);
        verifyAll();
    }

    @org.junit.Test
    public void testExecuteAddNewConfiguration() throws java.lang.Exception {
        EasyMock.expect(m_mockCluster.getDesiredConfigByType(org.apache.ambari.server.serveraction.upgrades.PluginUpgradeServerActionTest.FOO_SITE)).andReturn(null).once();
        EasyMock.expect(m_mockCluster.getDesiredConfigByType(org.apache.ambari.server.serveraction.upgrades.PluginUpgradeServerActionTest.FOO_SITE)).andReturn(m_mockConfig).once();
        m_mockConfigHelper.createConfigType(EasyMock.eq(m_mockCluster), EasyMock.eq(m_stackId), EasyMock.eq(m_mockController), EasyMock.eq(org.apache.ambari.server.serveraction.upgrades.PluginUpgradeServerActionTest.FOO_SITE), EasyMock.eq(new java.util.HashMap<>()), EasyMock.eq(org.apache.ambari.server.serveraction.upgrades.PluginUpgradeServerActionTest.AUTH_USERNAME), EasyMock.eq("Upgrade to 1.0.0.0-b1"));
        EasyMock.expectLastCall();
        java.util.Map<java.lang.String, java.lang.String> configUpdates = new java.util.HashMap<>();
        configUpdates.put("property-name", "property-value");
        m_mockConfig.updateProperties(configUpdates);
        EasyMock.expectLastCall().once();
        m_mockConfig.save();
        EasyMock.expectLastCall().once();
        org.powermock.api.easymock.PowerMock.replay(m_action);
        replayAll();
        m_action.execute(null);
        verifyAll();
    }

    public static class MockUpgradeAction implements org.apache.ambari.spi.upgrade.UpgradeAction {
        @java.lang.Override
        public org.apache.ambari.spi.upgrade.UpgradeActionOperations getOperations(org.apache.ambari.spi.ClusterInformation clusterInformation, org.apache.ambari.spi.upgrade.UpgradeInformation upgradeInformation) throws org.apache.ambari.spi.exceptions.UpgradeActionException {
            java.util.List<org.apache.ambari.spi.upgrade.UpgradeActionOperations.ConfigurationChanges> allChanges = new java.util.ArrayList<>();
            org.apache.ambari.spi.upgrade.UpgradeActionOperations.ConfigurationChanges configurationTypeChanges = new org.apache.ambari.spi.upgrade.UpgradeActionOperations.ConfigurationChanges(org.apache.ambari.server.serveraction.upgrades.PluginUpgradeServerActionTest.FOO_SITE);
            configurationTypeChanges.set("property-name", "property-value");
            allChanges.add(configurationTypeChanges);
            org.apache.ambari.spi.upgrade.UpgradeActionOperations upgradeActionOperations = new org.apache.ambari.spi.upgrade.UpgradeActionOperations();
            upgradeActionOperations.setConfigurationChanges(allChanges).setStandardOutput("Standard Output");
            return upgradeActionOperations;
        }
    }
}